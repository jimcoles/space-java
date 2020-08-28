/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.runtime;

import org.apache.commons.collections.CollectionUtils;
import org.jkcsoft.java.util.JavaHelper;
import org.jkcsoft.java.util.Strings;
import org.jkcsoft.space.lang.ast.*;
import org.jkcsoft.space.lang.instance.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Predicate;

/**
 * Utility methods for querying and traversing the Space AST, which can also be thought of as a directory of nodes.
 *
 * @author Jim Coles
 */
public class AstUtils {

    private static final Logger log = LoggerFactory.getLogger(AstUtils.class);

    private static IntrinsicContainer INTRINSIC_TYPES;
    private static Executor.FindFirstAstConsumer<Directory> dirFinderAction =
        new Executor.FindFirstAstConsumer<>(Directory.class);
    private static Executor.FindFirstAstConsumer<ParseUnit> parseUnitFinderAction =
        new Executor.FindFirstAstConsumer<>(ParseUnit.class);
    private static Executor.FindFirstAstConsumer<Namespace> nsFinderAction =
        new Executor.FindFirstAstConsumer<>(Namespace.class);
    private static Executor.FindFirstAstConsumer<FunctionDefn> funcDefnfinderAction =
        new Executor.FindFirstAstConsumer<>(FunctionDefn.class);
    private static Executor.FindFirstAstConsumer<StatementBlock> statementBlockfinderAction =
        new Executor.FindFirstAstConsumer<>(StatementBlock.class);
    private static Executor.FindFirstAstConsumer<NewTupleExpr> newTupleExprfinderAction =
        new Executor.FindFirstAstConsumer<>(NewTupleExpr.class);
    private static Executor.FindFirstAstConsumer<ModelElement> typeOrDirContainerFinder =
        new Executor.FindFirstAstConsumer<>(
            ModelElement.class,
            modelElement -> modelElement instanceof TypeDefnImpl || modelElement instanceof Directory
        );

    /** Of the two specified arg types, returns the 'largest' of the two. */
    private static TypeDefn[][] typePrecedenceMap = new TypeDefn[][]{
        {NumPrimitiveTypeDefn.REAL, NumPrimitiveTypeDefn.CARD}
    };

    static {
        INTRINSIC_TYPES = getAstFactory().newIntrinsicContainer();
        INTRINSIC_TYPES.addChild(VoidType.VOID);
        for (Map.Entry<String, PrimitiveTypeDefn> primTypeDefnEntry : NumPrimitiveTypeDefn.getEnumsByName().entrySet())
        {
            INTRINSIC_TYPES.addChild(primTypeDefnEntry.getValue());
        }
    }

    private static ModelElement getIntrinsicTypes() {
        return INTRINSIC_TYPES;
    }

    /** The nearest named parent of an element, e, is the the element thru which 'e' may be referenced. */
    public static NamedElement getNearestNamedParent(ModelElement anElem) {
        NamedElement lexParent = null;
        if (anElem.getParent() instanceof NamedElement
            && ((NamedElement) anElem.getParent()).hasName()) {
            lexParent = (NamedElement) anElem.getParent();
        }
        else if (anElem.getParent() != null) {
            lexParent = getNearestNamedParent(anElem.getParent());
        }
        return lexParent;
    }

    /** The lexical parent of an element, e, is the the element by which 'e' may be referenced. */
    public static ModelElement getNearestScopeParent(ModelElement anElem) {
        ModelElement lexParent = null;
        if (hasAnnotation(anElem.getParent(), LexicalNode.class)) {
            lexParent = anElem.getParent();
        }
        else if (anElem.getParent() != null) {
            lexParent = getNearestScopeParent(anElem.getParent());
        }
        return lexParent;
    }

    public static Directory getRunDir(Directory thisDirectory) {
        Directory directory =
            (Directory) CollectionUtils.find(thisDirectory.getChildren(),
                                             object -> object instanceof Directory);
        return directory;
    }

    public static boolean isGroupingNode(ModelElement child) {
        return child.isGroupingNode() || hasAnnotation(child, GroupingNode.class);
    }

    private static boolean hasAnnotation(ModelElement child, Class annotationClass) {
        return child.getClass().getAnnotation(annotationClass) != null;
    }

    /**
     * <p>Entry point for resolving static references by the linker prior to execution.
     * Rules for resolution vary depending on the type of (meta) object referenced
     * and the context of the reference:
     * <br>
     * Valid paths by meta type:
     *
     *  Type
     *  Type.Type[... Type]
     *  dir.dir.dir.Type
     *
     *  Datum
     *  this.Datum
     *  Type.Datum (static)
     *  dir.dir.Type.Datum
     *  Func.Datum
     *
     *  Func
     *  dir.dir.Type.Func
     *  Type.Func
     *  this.Func
     *  Func.Func
     *
     * <p>Ref -> (all meta types)
     * <p>
     *    1. Look for first part<br>
     *    1.1 In current parse unit<br>
     *    1.2 In current directory<br>
     *    1.3 In implicit import dir<br>
     *    1.4 In explicit import sequence<br>
     *
     * <p>Ref -> Type: must be either fully qualified, possibly via an 'import', or unqualified.
     * <p>Multi-part Type ref must be [dir. ...]dir.Type[.Type]
     *     - Linker:<br>
     *    2. Otherwise resolve FQ ref via imports.<br>
     *         Then locate from root of AST.
     * <p>
     * - Exe: (no need)
     *
     * <p>Ref -> Datum:
     *  - Linker: If first node in path (including a 1-length path), resolve
     *  according to this search order (precedence):
     * <p>
     *  1. block local -> parent blocks(s)
     *  2. function args (if ref is inside function body)
     *  3. "context object/type" member datums
     *  4. static/shared datums, fully qualified
     *<p>
     *  If not first in chain, resolve with respect to type of preceding object.
     *<p>
     *  - Exe: resolves to value or ref.
     *
     * <p>Ref -> Function:
     * <p>
     *  1. "context object/type" member functions
     *  2. static functions, fully qualified
     *
     *
     * @param parseUnit
     * @param refChain
     * @return
     */
    public static void resolveAstRef(ParseUnit parseUnit, ExpressionChain refChain) {
        Namespace refNs = getNs(refChain);
        checkSetResolve(refChain.getNsRefPart(), refNs);
        AstScopeCollection scopeSequence = getScopeCollection(parseUnit, refChain);

        // TODO Handle dependency between references: e.g., might have a ref to a function; i
        //      resolve the function, but its return type has not yet been resolved

        //
        if (refChain.getFirstPart().hasNameRef())
            resolveFirst(refChain, scopeSequence);
        //
        if (refChain.isMultiPart()
            && refChain.getFirstPart().getNameRef().getRefAsNameRef().isResolved())
        {
            resolveRest(refChain);
        }
    }

    /**
     * Resolves first link in the chain if that link has a reference.
     *
     * @param refChain
     * @param scopeSequence
     */
    private static void resolveFirst(ExpressionChain refChain, AstScopeCollection scopeSequence) {

//        Namespace refNs = (Namespace) refChain.getNsRefPart().getRef().getResolvedMetaObj();

        LinkSource firstPart = refChain.getFirstPart();
        if (!firstPart.hasNameRef())
            return;

        MetaRef firstExprRef = firstPart.getNameRef().getRefAsNameRef();
        for (StaticScope staticScope : scopeSequence) {
            ModelElement scopeElem = staticScope.getContext();
            log.debug("attempting to resolve [" + firstExprRef + "] under [" + staticScope + "]");
            if (scopeElem instanceof ExpressionChain) {
                refChain.setAstLoadError(((ExpressionChain) scopeElem).getLoadError());
            }
            else {
                NamedElement lookup =
                    lookupImmediateChild(scopeElem, firstExprRef.getKeyOrName());
                checkSetResolve(firstPart, lookup);
            }
            //
            if (firstExprRef.isResolved()) {
                log.debug("resolved [" + firstExprRef + "] under [" + staticScope + "]");
                break;
            }
            else
                log.debug("could not resolve [" + firstExprRef + "] under [" + staticScope + "]");

        }

    /*
        switch (MetaType.DATUM) {
            case TYPE:
                resolveIntrinsics(((NameRefExpr) refChain.getFirstPart()));
                if (refChain.isSinglePart()) {
                    // check for 'siblings': in same parse unit or directory
                    if (refChain.isAtInitState()) {
                        ParseUnit parentParseUnit = findParentParseUnit(refChain, parseUnitFinderAction);
                        resolveFromScopeDown(parentParseUnit, refChain);
                        if (refChain.isAtInitState()) {
                            Directory parentDir = findParentDir(refChain, dirFinderAction);
                            resolveFromScopeDown(parentDir, refChain);
                        }
                    }
                }
                // check as full path from root
                if (refChain.isAtInitState()) {
                    resolveFromRoot(refNs.getRootDirLookupChain(), refChain);
                }
                break;
            case DATUM:
                resolveInNearestScope(scopeSequence, refChain, null);
                if (refChain.isAtInitState()) {
                    resolveFromRoot(refNs.getRootDirLookupChain(), refChain);
                    if (refChain.isResolved())
                        refChain.setResolvedDatumScope(ScopeKind.STATIC);
                }
                break;
            case FUNCTION:
//                resolveFromRoot(refNs.getRootDirLookupChain(), reference);
                if (refChain.isAtInitState() ) {
                    if (!refChain.isMultiPart()) {
//                        resolveInNearestScope(scopeSequence, AstUtils.getNearestScopeParent(refChain), refChain, null);
                        resolveInNearestScope(scopeSequence, refChain, null);
                    }
                    else {
                        resolveFromScopeDown(refChain.getNamedParent(), refChain);
                    }
                }
                break;
        }
    */

    }

    /**
     * Resolves all refs after the first using type info from each
     * predecessor.
     *
     * @param refChain
     */
    private static void resolveRest(ExpressionChain refChain) {
//        resolveIntrinsics(((NameRefExpr) refChain.getFirstPart()));
        NamedElement lookup = null;
        LinkSource currentlhsLink = refChain.getFirstPart();
        List<NameRefOrHolder> restLinks = refChain.getRestLinks();
        for (NameRefOrHolder rhsLink : restLinks) {
            String keyOrName = rhsLink.getNameRef().getRefAsNameRef().getKeyOrName();
            if (currentlhsLink.hasNameRef()) {
                Named lhsContainer = currentlhsLink.getNameRef().getRefAsNameRef().getResolvedMetaObj();
//                lookup = lhsContainer.getChildByName(keyOrName);
                lookup = lookupImmediateChild(lhsContainer, keyOrName);
            }
            if (lookup == null && currentlhsLink.hasTypedExpr()) {
                TypeDefn lhsType = currentlhsLink.getTypedExpr().getDatumType();
                lookup = lookupImmediateChild(lhsType, keyOrName);
            }
            checkSetResolve(rhsLink, lookup);
            if (!rhsLink.getRefAsNameRef().isResolved()) {
                break;
            }
            else {
                currentlhsLink = rhsLink;
            }
        }
    }

    /**
     * The scope rules are a function of:
     *
     * 1. The type of thing we're trying to find.
     * 2. If this is a pronoun reference, e.g., "this.".
     * 3. The kind of scope from which the reference is made.
     *
     * @param parseUnit
     * @param reference
     * @return The appropriate {@link AstScopeCollection} for the reference.
     */
    private static AstScopeCollection getScopeCollection(ParseUnit parseUnit, ExpressionChain reference) {
        ScopeKind refFromScopeKind = inferScopeKind(reference.getParent());
        // TODO add logic to reuse scope collections where possible, e.g., references from
        // the same context have the same scopes, etc.
        ScopeCollectionWalker walker = new ScopeCollectionWalker(parseUnit, reference);
        return walker.getAllScopesAsList();
    }

    public static void checkSetResolve(LinkSource exprLink, NamedElement lookup) {
        if (exprLink != null && lookup != null) {
            ByNameMetaRef metaRef = exprLink.getNameRef().getRefAsNameRef();
            metaRef.setResolvedMetaObj(lookup);
            metaRef.setState(LinkState.RESOLVED);
        }
    }

    public static Namespace getNs(ExpressionChain reference) {
        Namespace ns = null;
        if (reference.hasNs())
            ns = NSRegistry.getInstance().getNamespace(reference.getNsRefPart().getNameExprText());
        else
            ns = AstUtils.findParentNs(reference);

        return ns;
    }

    private static ModelElement findParentTypeDefnOrDir(ModelElement astElement) {
        return findFirstParent(astElement, typeOrDirContainerFinder);
    }

    private static FunctionDefn findParentFunctionDefn(ModelElement astNode) {
        return findFirstParent(astNode, funcDefnfinderAction);
    }

    private static Namespace findParentNs(ModelElement astNode) {
        return findFirstParent(astNode, nsFinderAction);
    }

    public static <T extends ModelElement> T findFirstParent(ModelElement astNode,
                                                             Executor.FindFirstAstConsumer<T> finderAction)
    {
        finderAction.clearResult();
        AstUtils.iterateParents(astNode.getParent(), finderAction);
        return finderAction.getResult();
    }

    private static <T extends ModelElement> T findFirstParentOfClass(ModelElement astElement, Class<T> modelClazz) {
        // TODO avoid repetitive creation of 'FindFirstAstConsumer objects
        return findFirstParent(astElement, new Executor.FindFirstAstConsumer<T>(modelClazz));
    }

    public static Directory findParentDir(ModelElement astNode) {
        return findFirstParent(astNode, dirFinderAction);
    }

    private static StatementBlock findParentStatementBlock(ModelElement astNode) {
        return findFirstParent(astNode, statementBlockfinderAction);
    }

    public static ParseUnit findParentParseUnit(ModelElement astNode) {
        return findFirstParent(astNode, parseUnitFinderAction);
    }

    private static NewTupleExpr findParentNewTupleExpr(ModelElement astNode) {
        return findFirstParent(astNode, newTupleExprfinderAction);
    }

    public static Set<TypeDefn> getSiblingTypes(FullTypeRefImpl reference) {
        MetaRef parentRefPart = getParentRefPart(reference);
        return getChildTypes((Directory) parentRefPart.getResolvedMetaObj());
    }

//    public static void resolveInNearestScope(AstScopeCollection scopeWalker, ExpressionChain reference,
//                                             ScopeKind containerScopeKind)
//    {
//        StaticScope staticScope = scopeWalker.nextScope();
//        if (containerScopeKind == null)
//            containerScopeKind = staticScope.getScopeKind();
//        NamedElement lookup =
//            lookupImmediateChild(staticScope.getContext(), ((SimpleNameRefExpr) reference.getFirstPart()).getNameExprText());
//        checkSetResolve(reference.getFirstPart(), lookup);
//        if (reference.isResolved())
//            reference.setResolvedDatumScope(containerScopeKind);
//        else {
//            resolveInNearestScope(scopeWalker, reference, containerScopeKind);
//        }
//    }

    public static Set<TypeDefn> getChildTypes(Directory dir) {
        Set<TypeDefn> childTypes = new HashSet<>();
        dir.getParseUnits().forEach(parseUnit -> childTypes.addAll(parseUnit.getTypeDefns()));
        return childTypes;
    }

    private static MetaRef getParentRefPart(FullTypeRefImpl fullRef) {
        List<MetaRef> pathParts = fullRef.extractMetaRefPath().getLinks();
        return pathParts.get(pathParts.size() - 2);
    }

    private static void resolveFromRoot(Directory[] dirChain, ExpressionChain reference) {
        for (Directory rootDir : dirChain) {
            log.debug("trying to find [" + reference + "] under [" + rootDir.getFQName() + "]");
            resolveAbsolute(rootDir, reference.getAllLinksAsHolders().iterator());
            if (reference.isResolved()) {
                break;
            }
            else {
                log.debug("could not fully resolve [" + reference + "] under [" + rootDir.getFQName() + "]");
            }
        }
    }

    private static void resolveFromScopeDown(ModelElement astNode, ExpressionChain exprChain) {
        log.debug("trying to find [" + exprChain + "] under [" + astNode + "]");
        NamedElement lookup =
            lookupImmediateChild(astNode, (((SimpleNameRefExpr) exprChain.getFirstPart()).getNameExprText()));
        checkSetResolve(exprChain.getFirstPart(), lookup);
        resolveAbsolute(lookup, exprChain.getRestLinks().iterator());
    }

    public static void resolveAbsolute(ModelElement nameContext, Iterator<NameRefOrHolder> refHolderIter) {
        if (nameContext != null && refHolderIter.hasNext()) {
            NameRefOrHolder refPart = refHolderIter.next();
            NamedElement targetChild =
                lookupImmediateChild(nameContext, refPart.getRefAsNameRef().getNameExprText());
            checkSetResolve(refPart, targetChild);
            if (refPart.getRefAsNameRef().isResolved()) {
                if (refHolderIter.hasNext()) {
                    resolveAbsolute(refPart.getRefAsNameRef().getResolvedMetaObj(), refHolderIter);
                }
            }
        }
    }

    public static ScopeKind inferScopeKind(ModelElement context) {
        ScopeKind scopeKind = null;
        if (context instanceof StatementBlock
            /* && context.getNamedParent() instanceof FunctionDefn */)
            scopeKind = ScopeKind.BLOCK;
        else if (context instanceof SpaceFunctionDefn)
            scopeKind = ScopeKind.BLOCK;
        else if (context instanceof TypeDefnImpl)
            scopeKind = ScopeKind.TYPE_DEFN;
        else if (context instanceof TupleValueList)
            scopeKind = ScopeKind.TYPE_DEFN;

        return scopeKind;
    }

    /** Will traverse into child grouping nodes and grouping nodes only. */
    public static NamedElement lookupImmediateChild(ModelElement context, String name) {
//        if (name.equals("firstName")) {
//            log.debug("child names for [" + context +"] => " + Strings.buildCommaDelList(context.getNamedChildMap().keySet()));
//        }
        NamedElement childByName = context.getChildByName(name);
        if (childByName == null) {
            if (context.hasGroupingNodes()) {
                for (ModelElement groupElement : context.getGroupingNodes()) {
                    childByName = lookupImmediateChild(groupElement, name);
                    if (childByName != null)
                        break;
                }
            }
        }
        return childByName;
    }

    private static void resolveIntrinsics(SimpleNameRefExpr refPart) {
        NamedElement lookup = INTRINSIC_TYPES.getChildByName(refPart.getNameExprText());
//        if (refPart.getExpression().getNameExpr().equals(VoidType.VOID.getName())) {
//            lookup = VoidType.VOID;
//        }
//        else {
//            lookup = NumPrimitiveTypeDefn.valueOf(refPart.getExpression().getNameExpr());
//        }
        checkSetResolve(refPart, lookup);
    }

    public static void walkAstDepthFirst(ModelElement astNode, AstScanConsumer astAction) {
        boolean match =
            astAction.getFilter() == null
                || astAction.getFilter().test(astNode);
        if (match)
            astAction.upon(astNode);
        List<ModelElement> children = astNode.getChildren();
        for (ModelElement child : children) {
            walkAstDepthFirst(child, astAction);
        }
        if (match)
            astAction.after(astNode);
    }

    public static void walkNamedAstDepthFirst(ModelElement astNode, AstScanConsumer astAction) {
        boolean match =
            astAction.getFilter() == null
                || astAction.getFilter().test(astNode);
        if (match)
            astAction.upon(astNode);

        Collection<NamedElement> children = astNode.getNamedChildren();
        for (NamedElement child : children) {
            walkNamedAstDepthFirst(child, astAction);
        }

        List<ModelElement> allChildren = astNode.getChildren();
        for (ModelElement child : allChildren) {
            if (isGroupingNode(child))
                walkNamedAstDepthFirst(child, astAction);
        }

        if (match)
            astAction.after(astNode);
    }

    public static void iterateParents(ModelElement astNode, AstScanConsumer astAction) {

        if (astNode == null)
            return;

        boolean match = astAction.getFilter() == null || astAction.getFilter().test(astNode);
        boolean keepGoing = true;

        if (match) {
            keepGoing = astAction.upon(astNode);
        }

        if (keepGoing && astNode.hasParent())
            iterateParents(astNode.getParent(), astAction);

        if (match)
            astAction.after(astNode);
    }

    public static String printFullAst(ModelElement modelElement) {
        PrintFullAstConsumer astPrinter = new PrintFullAstConsumer();
        walkAstDepthFirst(modelElement, astPrinter);
        return astPrinter.getSb().toString();
    }

    public static String printFlatNamedAst(Named modelElement) {
        PrintFlatAstConsumer astPrinter = new PrintFlatAstConsumer();
        walkNamedAstDepthFirst(modelElement, astPrinter);
        return astPrinter.getSb().toString();
    }

    public static boolean contains(Directory parentDir, String name) {
        return getSubDirByName(parentDir, name) != null;
    }

    public static Directory getSubDirByName(Directory parentDir, String name) {
        Directory subDir = null;
        if (parentDir.hasChildDirs()) {
            for (Directory directory : parentDir.getChildDirectories()) {
                if (directory.getName().equals(name)) {
                    subDir = directory;
                    break;
                }
            }
        }
        return subDir;
    }

    public static <T extends ModelElement> Set<T> queryAst(ModelElement queryContextElem,
                                                           Executor.QueryAstConsumer<T> astQueryAction)
    {
        walkAstDepthFirst(queryContextElem, astQueryAction);
        return astQueryAction.getResults();
    }

    public static Directory ensureDir(Directory nsRootDir, String[] classNameParts) {
        if (!nsRootDir.isRootDir())
            throw new IllegalArgumentException("specified dir must be a namespace root: [" + nsRootDir + "]");

        Directory leafDir = nsRootDir;
        for (int idxName = 0; idxName < classNameParts.length; idxName++) {
            String name = classNameParts[idxName];
            if (contains(leafDir, name)) {
                leafDir = leafDir.getChildDir(name);
            }
            else {
                leafDir = leafDir.addDir(getAstFactory().newAstDir(new ProgSourceInfo(), name));
                log.trace("created new Space dir [" + leafDir + "]");
            }
        }

        return leafDir;
    }

    private static AstFactory getAstFactory() {
        return AstFactory.getInstance();
    }

    public static boolean isJavaNs(ImportExpr importExpr) {
        SimpleNameRefExpr nsRefPart = importExpr.getTypeRefExpr().getNsRefPart();
        return nsRefPart != null
            && nsRefPart.getNameExprText().equals(NSRegistry.getInstance().getJavaNs().getName());
    }

    public static void addNewMetaRefParts(ExpressionChain parentPath, SourceInfo sourceInfo, String... nameExprs) {
        for (String nameExpr : nameExprs) {
            parentPath.addNextPart(getAstFactory().newNameRefExpr(sourceInfo, nameExpr));
        }
        return;
    }

    public static TypeDefn larger(TypeDefn type1, TypeDefn type2) {
        TypeDefn larger = null;
//        for (TypeDefn[] typePair : typePrecedenceMap) {
//            if typePair[0] = type1
//        }
        return larger;
    }

    public static Comparator<Tuple> buildComparator(KeyDefnImpl keyDefn) {
        Comparator<Tuple> comp = null;
        for (ProjectionDecl proj : keyDefn.getProjectionDeclList()) {
            if (comp == null)
                comp = proj.getDatumComparator();
            else
                comp.thenComparing(proj.getDatumComparator());
        }
        return comp;
    }

    public static Comparator<Tuple> buildDatumComparator(Declaration datumDecl) {
        return (tuple1, tuple2) -> datumDecl.getType().getTypeComparator().compare(
            tuple1.get(datumDecl).getValue(),
            tuple2.get(datumDecl).getValue());
    }

    private static class PrintFullAstConsumer implements AstScanConsumer {

        private final StringBuilder sb = new StringBuilder();

        public PrintFullAstConsumer() {
        }

        @Override
        public Predicate<ModelElement> getFilter() {
            return null;
        }

        @Override
        public boolean upon(ModelElement astNode) {
            String indent = Strings.multiplyString("\t", astNode.getTreeDepth());
            sb.append(JavaHelper.EOL)
              .append(indent).append("(").append(astNode.toString());
            return true;
        }

        @Override
        public void after(ModelElement astNode) {
            if (astNode.hasChildren()) {
                String indent = Strings.multiplyString("\t", astNode.getTreeDepth());
                sb.append(JavaHelper.EOL).append(indent);
            }
            sb.append(")");
        }

        public StringBuilder getSb() {
            return sb;
        }
    }

    private static class PrintFlatAstConsumer implements AstScanConsumer {

        private final StringBuilder sb = new StringBuilder();

        public PrintFlatAstConsumer() {
        }

        @Override
        public Predicate<ModelElement> getFilter() {
            return astNode ->
                astNode instanceof Named;
        }

        @Override
        public boolean upon(ModelElement astNode) {
            if (astNode instanceof Named && ((Named) astNode).hasName()) {
                sb.append(astNode.getDisplayName())
                  .append(JavaHelper.EOL);
            }
            return true;
        }

        @Override
        public void after(ModelElement astNode) {
            //
        }

        public StringBuilder getSb() {
            return sb;
        }
    }

    /**
     * Used to find the first (possibly only) item in an expression chain.
     */
    private static class ScopeCollectionWalker {
        //
        private ParseUnit parseUnit;
        private ExpressionChain theRefChain;
//        private StaticScope staticScope;
        //
        private List<AstScopeCollection> scopeCollGrouped = new LinkedList<>();
        private AstScopeCollection allScopes;
        private AstScopeCollection intrinsicsScopeColl = new AstScopeCollection("Intrinsic Types");
        {
            intrinsicsScopeColl.add(new StaticScope(intrinsicsScopeColl, INTRINSIC_TYPES, ScopeKind.GLOBAL));
        }

        public ScopeCollectionWalker(ParseUnit parseUnit, ExpressionChain exprChain) {
            this.parseUnit = parseUnit;
            this.theRefChain = exprChain;
            // init scope to the reference element itself
//            this.staticScope = new StaticScope(exprChain);
            //
            buildScopeSequence(exprChain);
        }

        private void buildScopeSequence(ExpressionChain exprChain) {
            switch (exprChain.getTargetMetaType()) {
                case TYPE:
                    scopeCollGrouped = buildTypeRefScopes();
                    break;
                case DATUM:
                    if (isLhsTupleDatumRef(exprChain)) {
                        scopeCollGrouped = buildLhsTupleDatumRefScopes();
                    }
                    else
                        scopeCollGrouped = buildDatumRefScopes();
                    break;
                case FUNCTION:
                    scopeCollGrouped = buildFunctionRefScopes();
                    break;
                case PACKAGE:
                    scopeCollGrouped = buildPackageRefScopes();
                    break;
            }
        }

        private List<AstScopeCollection> buildTypeRefScopes() {
            List<AstScopeCollection> refScopes = new LinkedList<>();
            //
            if (!theRefChain.isImportRef()) {
                refScopes.add(buildIntrinsicTypeScopes());
                refScopes.add(buildImportsScopes());
                refScopes.add(buildTypeDefnContainerScopes());
            }
            refScopes.add(buildNsRootScopes());
            //
            return refScopes;
        }

        private List<AstScopeCollection> buildDatumRefScopes() {
            List<AstScopeCollection> refScopes = new LinkedList<>();
            //
            refScopes.add(buildImportsScopes());
            refScopes.add(buildFunctionLocalBlocksScopes());
            refScopes.add(buildArgTypeDefnScopes());
            refScopes.add(buildTypeDefnContainerScopes());
            //
            return refScopes;
        }

        private List<AstScopeCollection> buildLhsTupleDatumRefScopes() {
            List<AstScopeCollection> refScopes = new LinkedList<>();
            //
            refScopes.add(buildTupleTypeDefnScopes());
            //
            return refScopes;
        }

        private List<AstScopeCollection> buildFunctionRefScopes() {
            List<AstScopeCollection> refScopes = new LinkedList<>();
            //
            refScopes.add(buildTypeDefnContainerScopes());
            refScopes.add(buildImportsScopes());
            //
            return refScopes;
        }

        private List<AstScopeCollection> buildPackageRefScopes() {
            List<AstScopeCollection> refScopes = new LinkedList<>();
            //
            refScopes.add(buildNsRootScopes());
            //
            return refScopes;
        }

        private boolean isLhsTupleDatumRef(ExpressionChain exprChain) {
            return (exprChain.getParent() instanceof AssignmentExpr
                && exprChain.getParent().getParent() instanceof TupleValueList)
                && ((AssignmentExpr) exprChain.getParent()).isLhsExpr(exprChain);
        }

        // ====================================================================
        //  The following methods build individual scope collections
        // ====================================================================

        /**
         * Looks in the root Dir of the Namespace
         */
        private AstScopeCollection buildNsRootScopes() {
            AstScopeCollection theColl = new AstScopeCollection("Directories in Root of JS");
            //
            theColl.add(new StaticScope(theColl, getNs(theRefChain).getRootDir(), ScopeKind.GLOBAL));
            //
            return theColl;
        }

        /** Looks at intrinsic types. */
        private AstScopeCollection buildIntrinsicTypeScopes() {
            return intrinsicsScopeColl;
        }

        /**
         * Looks in the imports of the current parse unit.
         */
        private AstScopeCollection buildImportsScopes() {
            AstScopeCollection theColl = new AstScopeCollection("Imported Types");
            //
            if (parseUnit != null) {
                theColl.add(new StaticScope(theColl, parseUnit.getImportedTypeContainer()));
            }
            //
            return theColl;
        }

        /**
         * Iterates from current scope to broader scopes, stopping at the top-level type
         * definition.
         */
        private AstScopeCollection buildFunctionLocalBlocksScopes() {
            AstScopeCollection theColl = new AstScopeCollection("Local Function Blocks");
            //
            boolean isFuncScope = findParentFunctionDefn(theRefChain) != null;
            if (isFuncScope) {
                ModelElement next = findParentStatementBlock(theRefChain);
                while (next != null) {
                    theColl.add(new StaticScope(theColl, next, ScopeKind.BLOCK));
                    next = findParentStatementBlock(next);
                }
            }
            //
            return theColl;
        }

        /**
         * Iterates through the reference's parent types (possibly nested) and Directory.
         */
        private AstScopeCollection buildTypeDefnContainerScopes() {
            AstScopeCollection theColl = new AstScopeCollection("Function Parent Containers");
            ModelElement thisTypeDefn = findParentTypeDefnOrDir(theRefChain);
            //
            while (thisTypeDefn != null) {
                theColl.add(new StaticScope(theColl, thisTypeDefn, ScopeKind.TYPE_DEFN));
                thisTypeDefn = findParentTypeDefnOrDir(thisTypeDefn);
            }
            //
            return theColl;
        }

        /**
         * Includes the implicit type definition of the function argument list. Used
         * to link to arguments within a function definition or for by-name assignment
         * expressions in function calls and tuple construction.
         */
        private AstScopeCollection buildArgTypeDefnScopes() {
            AstScopeCollection theColl = new AstScopeCollection("Imports");
            FunctionDefn funcDefn = findParentFunctionDefn(theRefChain);
            if (funcDefn != null)
                theColl.add(new StaticScope(theColl, funcDefn.getArgSpaceTypeDefn(), ScopeKind.TYPE_DEFN));
            //
            return theColl;
        }

        private AstScopeCollection buildTupleTypeDefnScopes() {
            AstScopeCollection theColl = new AstScopeCollection("Tuple Type");
            NewTupleExpr newTupleExpr = findParentNewTupleExpr(theRefChain);
            if (newTupleExpr != null) {
                FullTypeRefImpl typeRef = newTupleExpr.getTypeRef();
                if (typeRef.isResolved()) {
                    theColl.add(new StaticScope(theColl, typeRef.getResolvedType(), ScopeKind.TYPE_DEFN));
                }
                else {
                    theColl.add(new StaticScope(theColl, typeRef));
                }
            }
            //
            return theColl;
        }

        public AstScopeCollection getAllScopesAsList() {
            if (allScopes == null) {
                allScopes = new AstScopeCollection("All Scopes");
                scopeCollGrouped.forEach(scopeColl -> allScopes.addAll(scopeColl));
            }
            return allScopes;
        }

    }
}
