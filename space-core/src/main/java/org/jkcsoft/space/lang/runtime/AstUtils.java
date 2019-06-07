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
import org.apache.log4j.Logger;
import org.jkcsoft.java.util.JavaHelper;
import org.jkcsoft.java.util.Strings;
import org.jkcsoft.space.SpaceHome;
import org.jkcsoft.space.lang.ast.*;
import org.jkcsoft.space.lang.metameta.MetaType;

import java.util.*;
import java.util.function.Predicate;

/**
 * Utility methods for querying and traversing the Space AST, which can also be thought of as a directory of nodes.
 *
 * @author Jim Coles
 */
public class AstUtils {

    private static final Logger log = Logger.getLogger(AstUtils.class);

    /** The nearest named parent of an element, e, is the the element thru which 'e' may be referenced. */
    public static NamedElement getNearestNamedParent(ModelElement anElem) {
        NamedElement lexParent = null;
        if (anElem.getParent() instanceof NamedElement
            && ((NamedElement) anElem.getParent()).isNamed())
        {
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
     * @param refChain
     * @return
     */
    public static void resolveAstRef(ExpressionChain refChain) {
        Namespace refNs = getNs(refChain);
        checkSetResolve(refChain.getNsRefPart(), refNs);
        AstPathIterator pathIterator = getPathIterator(refChain);

        // TODO Handle fact that first link in chain might not contain a ref
        // TODO Finding first link/ref is the complex part. 2nd link and beyond are all type-based
        // TODO Handle dependency between references: e.g., might have a ref to a function; i
        //      resolve the function, but its return type has not yet been resolved

        //
        resolveFirst(refChain, pathIterator);
        //
        if (refChain.isMultiPart() && refChain.getFirstPart().hasResolvedType())
            resolveRest(refChain);
    }

    /**
     * Resolves first link in the chain if that link has a reference.
     *
     * @param refChain
     * @param pathIterator
     */
    private static void resolveFirst(ExpressionChain refChain, AstPathIterator pathIterator) {

        Namespace refNs = (Namespace) refChain.getNsRefPart().getRef().getResolvedMetaObj();

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
                resolveInNearestScope(pathIterator, refChain, null);
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
//                        resolveInNearestScope(pathIterator, AstUtils.getNearestScopeParent(refChain), refChain, null);
                        resolveInNearestScope(pathIterator, refChain, null);
                    }
                    else {
                        resolveFromScopeDown(refChain.getNamedParent(), refChain);
                    }
                }
                break;
        }

    }

    /**
     * Resolves all refs after the first using type info from each
     * predecessor.
     *
     * @param refChain
     */
    private static void resolveRest(ExpressionChain refChain) {
        resolveIntrinsics(((NameRefExpr) refChain.getFirstPart()));
        refChain.getRestLinks();
    }

    /**
     * The scope rules are a function of:
     *
     * 1. The type of thing we're trying to find.
     * 2. If this is a pronoun reference, e.g., "this.".
     * 3. The kind of scope from which the reference is made.
     *
     * @param reference
     * @return The appropriate {@link AstPathIterator} for the reference.
     */
    private static AstPathIterator getPathIterator(ExpressionChain reference) {
        ScopeKind scopeKind = inferScopeKind(reference.getParent());
        AstPathIterator walker = null;
        switch (scopeKind) {
            case SPACE_DEFN:
                walker = new ScopeCollectionWalker(reference);
        }
        return walker;
    }

    static void checkSetResolve(TypedExpr exprLink, NamedElement lookup) {
        if (exprLink != null && lookup != null) {
            MetaRef metaRef = exprLink.getRef();
            metaRef.setResolvedMetaObj(lookup);
            metaRef.setState(LinkState.RESOLVED);
        }
    }

    public static Namespace getNs(ExpressionChain reference) {
        Namespace ns = null;
        if (reference.hasNs())
            ns = SpaceHome.getNsRegistry().getNamespace(reference.getNsRefPart().getNameExprText());
        else
            ns = AstUtils.findParentNs(reference);

        return ns;
    }

    private static Namespace findParentNs(ModelElement astNode) {
        return findFirstParent(astNode, nsFinderAction);
    }

    public static <T extends ModelElement> T findFirstParent(ModelElement astNode, Executor.FindFirstAstConsumer<T> finderAction) {
        finderAction.clearResult();
        AstUtils.iterateParents(astNode, finderAction);
        return finderAction.getResult();
    }

    public static Directory findParentDir(ModelElement astNode, Executor.FindFirstAstConsumer<Directory> finderAction) {
        return findFirstParent(astNode, finderAction);
    }

    public static ParseUnit findParentParseUnit(ModelElement astNode, Executor.FindFirstAstConsumer<ParseUnit> finderAction) {
        return findFirstParent(astNode, finderAction);
    }

    public static Set<DatumType> getSiblingTypes(FullTypeRefImpl reference) {
        MetaRef parentRefPart = getParentRefPart(reference);
        return getChildTypes((Directory) parentRefPart.getResolvedMetaObj());
    }

    public static Set<DatumType> getChildTypes(Directory dir) {
        Set<DatumType> childTypes = new HashSet<>();
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
        log.debug("trying to find ["+exprChain+"] under [" + astNode + "]");
        NamedElement lookup =
            lookupImmediateChild(astNode, (((NameRefExpr) exprChain.getFirstPart()).getNameExprText()));
        checkSetResolve(exprChain.getFirstPart(), lookup);
        resolveAbsolute(lookup, exprChain.getRestLinks().iterator());
    }

    public static void resolveAbsolute(ModelElement nameContext, Iterator<MemberRefHolder> refHolderIter) {
        if (nameContext != null && refHolderIter.hasNext()) {
            MemberRefHolder refPart = refHolderIter.next();
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

    public static void resolveInNearestScope(AstPathIterator scopeWalker, ExpressionChain reference,
                                             ScopeKind containerScopeKind)
    {
        StaticScope staticScope = scopeWalker.moveToNext();
        if (containerScopeKind == null)
            containerScopeKind = staticScope.getScopeKind();
        NamedElement lookup =
            lookupImmediateChild(staticScope.getContext(), ((NameRefExpr) reference.getFirstPart()).getNameExprText());
        checkSetResolve(reference.getFirstPart(), lookup);
        if (reference.isResolved())
            reference.setResolvedDatumScope(containerScopeKind);
        else {
            resolveInNearestScope(scopeWalker, reference, containerScopeKind);
        }
    }

    public static ScopeKind inferScopeKind(ModelElement context) {
        ScopeKind scopeKind = null;
        if (context instanceof StatementBlock
            /* && context.getNamedParent() instanceof FunctionDefn */ )
            scopeKind = ScopeKind.BLOCK;
        else if (context instanceof SpaceFunctionDefn)
            scopeKind = ScopeKind.BLOCK;
        else if (context instanceof SpaceTypeDefn)
            scopeKind = ScopeKind.SPACE_DEFN;
        else if (context instanceof TupleExpr)
            scopeKind = ScopeKind.SPACE_DEFN;

        return scopeKind;
    }

    /** Will traverse into child grouping nodes and grouping nodes only. */
    public static NamedElement lookupImmediateChild(ModelElement context, String name) {
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

    private static void resolveIntrinsics(NameRefExpr refPart) {
        NamedElement lookup = null;
        if (refPart.getExpression().getNameExpr().equals(VoidType.VOID.getName())) {
            lookup = VoidType.VOID;
        }
        else {
            lookup = NumPrimitiveTypeDefn.valueOf(refPart.getExpression().getNameExpr());
        }
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
        boolean match = astAction.getFilter() == null || astAction.getFilter().test(astNode);
        if (match)
            astAction.upon(astNode);

        if (astNode.hasParent())
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

    public static <T extends ModelElement> Set<T> queryAst(ModelElement queryContextElem, Executor.QueryAstConsumer<T> astQueryAction) {
        walkAstDepthFirst(queryContextElem, astQueryAction);
        return astQueryAction.getResults();
    }

    public static Directory ensureDir(Directory nsRootDir, String[] classNameParts) {
        if (!nsRootDir.isRootDir())
            throw new IllegalArgumentException("specified dir must be a namespace root: ["+nsRootDir+"]");

        Directory leafDir = nsRootDir;
        for (int idxName = 0; idxName < classNameParts.length; idxName++) {
            String name = classNameParts[idxName];
            if (contains(leafDir, name)) {
                leafDir = leafDir.getChildDir(name);
            }
            else {
                leafDir = leafDir.addDir(SpaceHome.getAstFactory().newAstDir(new ProgSourceInfo(), name));
                log.trace("created new Space dir ["+leafDir+"]");
            }
        }

        return leafDir;
    }

    public static boolean isJavaNs(ImportExpr importExpr) {
        NameRefExpr nsRefPart = importExpr.getTypeRefExpr().getNsRefPart();
        return nsRefPart != null
            && nsRefPart.getNameExprText().equals(SpaceHome.getNsRegistry().getJavaNs().getName());
    }

    public static void addNewMetaRefParts(ExpressionChain parentPath, SourceInfo sourceInfo, String... nameExprs) {
        for (String nameExpr : nameExprs) {
            parentPath.addNextPart(SpaceHome.getAstFactory().newNameRefExpr(sourceInfo, nameExpr));
        }
        return;
    }

    private static Executor.FindFirstAstConsumer<Directory> dirFinderAction =
        new Executor.FindFirstAstConsumer<>(Directory.class);

    private static Executor.FindFirstAstConsumer<ParseUnit> parseUnitFinderAction =
        new Executor.FindFirstAstConsumer<>(ParseUnit.class);

    private static Executor.FindFirstAstConsumer<Namespace> nsFinderAction =
        new Executor.FindFirstAstConsumer<>(Namespace.class);

    /** Of the two specified arg types, returns the 'largest' of the two. */
    private static DatumType[][] typePrecedenceMap = new DatumType[][] {
        {NumPrimitiveTypeDefn.REAL, NumPrimitiveTypeDefn.CARD}
    };
    public static DatumType larger(DatumType type1, DatumType type2) {
        DatumType larger = null;
//        for (DatumType[] typePair : typePrecedenceMap) {
//            if typePair[0] = type1
//        }
        return larger;
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
            if (astNode instanceof Named && ((Named) astNode).isNamed()) {
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
    private static class ScopeCollectionWalker implements AstPathIterator {

        {
            AstPathIterator[] typeScopes = {
                this::intrinsicsIter,
                this::importsIter,
                this::typeContainerIter
            };
            AstPathIterator[] datumScopes = {
                this::blockIter,
                this::fuctionDefnBlocksIter,
            };
            AstPathIterator[] functionScopes = {
                this::blockIter,
                this::fuctionDefnBlocksIter,
            };
            AstPathIterator[] lhsTupleDatumScopes = {
                this::blockIter
            };
        }

        //
        private StaticScope staticScope;
        //
        //
        private List<AstPathIterator> scopeIterAggregator = new LinkedList<>();
        private Iterator<AstPathIterator> outerIterator;
        private AstPathIterator currentIterator;

        public ScopeCollectionWalker(ExpressionChain exprChain) {

            if (outerIterator == null && scopeIterAggregator.size() > 0) {
                outerIterator = scopeIterAggregator.iterator();
            }
        }

        @Override
        public StaticScope moveToNext() {
            StaticScope next = null;
            if (currentIterator != null) {
                next = currentIterator.moveToNext();
            }

            if (next == null) {
                // jump to next scope iterator
                if (outerIterator != null && outerIterator.hasNext())
                    currentIterator = outerIterator.next();
                if (currentIterator != null) {
                    next = currentIterator.moveToNext();
                }
            }

            return next;
        }

        public StaticScope getStaticScope() {
            return staticScope;
        }

        private ModelElement getContext() {
            return staticScope.getContext();
        }

        /** Implements {@link AstPathIterator} */
        private StaticScope blockIter() {
//            if (context instanceof StatementBlock) {
                if (getContext().hasParent()) {
                    // not found so just go up the basic tree
                    staticScope.setContext(getContext().getParent());
                    if (getContext() instanceof StatementBlock)
                        staticScope.setScopeKind(ScopeKind.BLOCK);
                    else if (getContext() instanceof SpaceFunctionDefn)
                        staticScope.setScopeKind(ScopeKind.ARG);
                }
//            }
            return staticScope;
        }


        /** Implements {@link AstPathIterator} */
        private StaticScope intrinsicsIter() {
            // TODO
            return staticScope;
        }

        /** Implements {@link AstPathIterator} */
        private StaticScope importsIter() {
            // TODO
            return staticScope;
        }

        /** Implements {@link AstPathIterator} */
        private StaticScope typeContainerIter() {
            // TODO
            return staticScope;
        }

        /** Implements {@link AstPathIterator} */
        private StaticScope argDefnIter() {
//            if (! (scopeKind == ScopeKind.ARG)) {
            staticScope.setContext(((FunctionDefn) getContext()).getArgSpaceTypeDefn(), ScopeKind.ARG);
            return staticScope;
        }

        /** Implements {@link AstPathIterator} */
        public StaticScope fuctionDefnBlocksIter() {
            staticScope.setContext(staticScope.getContext().getParent(), ScopeKind.SPACE_DEFN);
            return staticScope;
        }

    }

}
