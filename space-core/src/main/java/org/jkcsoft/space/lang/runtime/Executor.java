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
import org.jkcsoft.java.util.Lister;
import org.jkcsoft.java.util.Strings;
import org.jkcsoft.space.jlib.math.Math;
import org.jkcsoft.space.jlib.opsys.JOpSys;
import org.jkcsoft.space.lang.ast.Comparators;
import org.jkcsoft.space.lang.ast.*;
import org.jkcsoft.space.lang.ast.sji.SjiFunctionDefnImpl;
import org.jkcsoft.space.lang.ast.sji.SjiService;
import org.jkcsoft.space.lang.ast.sji.SjiTypeDefn;
import org.jkcsoft.space.lang.instance.*;
import org.jkcsoft.space.lang.instance.sji.SjiTuple;
import org.jkcsoft.space.lang.loader.AstFileLoadErrorSet;
import org.jkcsoft.space.lang.loader.AstLoadError;
import org.jkcsoft.space.lang.loader.AstLoader;
import org.jkcsoft.space.lang.loader.DirLoadResults;
import org.jkcsoft.space.lang.metameta.MetaType;
import org.jkcsoft.space.lang.runtime.typecasts.CastTransforms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.jkcsoft.java.util.JavaHelper.EOL;

/**
 * <p> The top-level executive context for loading Space meta objects and running a
 * Space program.  Holds and manages instance level objects, call stack, etc..
 *
 * <p> An Executor manages interactions between second-level
 * elements including AstLoader's, ExprProcessor's, Querier.
 *
 * <p> The general pattern is:
 * <ul>
 * <li>exec() methods handle executable bits like the program itself and statements.</li>
 * <li>exec() methods handle expression</li>
 * </ul>
 * </p>
 *
 * @author Jim Coles
 * @version 1.0
 */
public class Executor extends ExprProcessor
    implements ExeContext, InternalExeContext, EvalContext
{

    private static final Logger log = LoggerFactory.getLogger(Executor.class);

    public static final SequenceTypeDefn CHAR_SEQ_TYPE_DEF = NumPrimitiveTypeDefn.CHAR.getSequenceOfType();
    public static final StackTraceLister STACK_TRACE_LISTER = new StackTraceLister();

    // ================== The starting point for using Space to execute Space programs

    // The following "Space" types if we want to back our 'definition' model with our
    // own notions.  The problem is that it will take time to build a
    // "Space model of Space" itself.

//    private Space relationDefns;
//    private Space assocDefns;
//    private Space actionSequenceDefns;

    // Uses Space constructs to hold a table (space) of SpaceOids to
//    private ObjectFactory spaceBuilder = ObjectFactory.getInstance();

    // ==================
    public static TypeDefn MATH_TYPE_DEF;
    public static TypeDefn space_opers_type_def;
    public static TypeDefn op_sys_type_def;
    /** Useful for simple API access from Java. */
    private static Executor instance;
    private static final Map<Operators.Operator, OperEvaluator> operEvalMap = new HashMap<>();

    static {
        operEvalMap.put(Operators.IntAlgOper.ADD, Math::addNum);
        operEvalMap.put(Operators.IntAlgOper.SUB, Math::subNum);
        operEvalMap.put(Operators.IntAlgOper.MULT, Math::multNum);
        operEvalMap.put(Operators.IntAlgOper.DIV, Math::divNum);
        //
        operEvalMap.put(Operators.BoolOper.COND_AND, Math::condAnd);
        //
        operEvalMap.put(Operators.IntCompOper.EQ, Math::equal);
        operEvalMap.put(Operators.IntCompOper.LT, Math::lt);
    }

    public static Executor defaultInstance() {
        if (instance == null)
            instance = new Executor(new ExeSettings() {
                @Override
                public String getExeMain() {
                    return null;
                }

                @Override
                public List<File> getSpaceDirs() {
                    return null;
                }
            });
        return instance;
    }

    public static Executor getInstance(ExeSettings exeSettings) {
        return new Executor(exeSettings);
    }

    private static TypeDefn getRootType(TypeDefn typeDefn) {
        TypeDefn baseType = typeDefn;
        if (typeDefn instanceof AbstractCollectionTypeDefn) {
            baseType = getRootType(((AbstractCollectionTypeDefn) typeDefn));
        }
        return baseType;
    }

    //--------------------------------------------------------------------------
    //

    private ExeSettings exeSettings;
    private AstLoader astLoader;
    private AstFactory astFactory;
    private NSRegistry nsRegistry;
    private Directory spaceLangDir;
    private SjiService sjiService;
    private InMemorySpaceImpl defaultSpace;

    private StaticExeContext staticExeContext;
    private Stack<FunctionCallContext> callStack = new Stack<>();
    private Map<String, ExprProcessor> exprProcessors = null;
    private Set<TypeDefn> implicitImportTypes;
    private CastTransforms casters;

    private Executor(ExeSettings exeSettings) {
        this.exeSettings = exeSettings;
        this.nsRegistry = NSRegistry.newInstance();
        this.astFactory = AstFactory.getInstance();
        this.defaultSpace = new InMemorySpaceImpl(null, null);
        initRuntime();
        preLoadSpecialNativeTypes();
        loadSpecialOperators();
    }

    private void initRuntime() {
        this.sjiService = new SjiService(this);
        // clunky way of doing package name alterations
        this.sjiService.registerPackageBinding("org.jkcsoft.space.jnative", "space.java");
        casters = new CastTransforms(this);
        //
        loadAstLoader("org.jkcsoft.space.antlr.loaders.G2AntlrParser");
//        nsRegistry.getUserNs().
    }

    @Override
    public StaticExeContext getStaticExeContext() {
        return staticExeContext;
    }

    @Override
    public void trackInstanceObject(SpaceObject spaceObject) {
        defaultSpace.trackInstanceObject(spaceObject);
    }

    @Override
    public SpaceObject dereferenceByOid(SpaceOid referenceOid) throws SpaceX {
        return defaultSpace.dereference(referenceOid);
    }

    @Override
    public SpaceObject getRef(Tuple tuple, DatumDecl datumDecl) {
        if (!datumDecl.hasAssoc()) {
            throw new SpaceX("invalid navigation of non-associative datum {}", datumDecl);
        }
        return dereference(((ReferenceValue) tuple.get(datumDecl).getValue()));
    }

    private SpaceObject dereference(ReferenceValue referenceValue) {
        SpaceObject obj = null;
        if (referenceValue instanceof ReferenceByOid)
            obj = dereferenceByOid(((ReferenceByOid) referenceValue).getToOid());
        else if (referenceValue instanceof ReferenceByAddress) {
            // TODO: use URL protocol to get possibly remote object; including Space TP
        }
        else if (referenceValue instanceof ReferenceByKey) {
            // TODO: lookup in indices
        }
        else if (referenceValue instanceof JavaReference)
            obj = resolveSjiObject((JavaReference) referenceValue);

        return obj;
    }

    /** Does a lazy-init of the SJI proxy for the Java object. */
    private SpaceObject resolveSjiObject(JavaReference value) {
        SpaceObject spaceObject = null;
        if (value.hasResolvedObject())
            spaceObject = value.getResolvedObject();
        else {
            spaceObject = sjiService.getOrCreateSjiObjectProxy(value.getJavaObject());
            value.setResovledObject(spaceObject);
        }
        return spaceObject;
    }

    @Override
    public AstFactory getAstFactory() {
        return astFactory;
    }

    @Override
    public ObjectFactory getObjFactory() {
        return ObjectFactory.getInstance();
    }

    public NSRegistry getNsRegistry() {
        return nsRegistry;
    }

    public SjiService getSjiService() {
        return sjiService;
    }

    @Override
    public String print(TupleSet tupleSet) {
        StringBuilder sb = new StringBuilder();
        sb.append("Tuple Set: " + EOL);
        List<DatumDecl> datumDecls = tupleSet.getContainedObjectType().getDatumDeclList();
        datumDecls.forEach(
            datumDecl -> sb.append("\"" + datumDecl.getNamePart() + "\" ")
        );
        sb.append(EOL);
        tupleSet.forEach(
            (oRefHolder) -> {
                SpaceObject targetObject = dereference(((FreeReferenceHolder) oRefHolder).getValue());
                if (targetObject != null)
                    appendString(sb, targetObject, datumDecls);
                else
                    throw new SpaceX("broken object reference {}", oRefHolder);
                sb.append(EOL);
            }
        );
        return sb.toString();
    }

    private void appendString(StringBuilder sb, SpaceObject spaceObject, List<DatumDecl> datumDecls) {
        datumDecls.forEach(
            datumDecl -> {
                String datumString = "?";
                if (spaceObject instanceof Tuple) {
                    Tuple tuple = (Tuple) spaceObject;
                    ValueHolder valueHolder = tuple.get(datumDecl);
                    if (datumDecl.hasAssoc()) {
                        AssociationDefn assocDecl = (AssociationDefn) datumDecl;
                        if (assocDecl.getAssociationKind() == AssociationKind.DEPENDENT_TYPE) {
                            // case: ref 0 or 1 dependent; treat as inner value; e.g., a char[]
                            SpaceObject toObject = getRef(tuple, datumDecl);
                            datumString = toObject != null ? toObject.toString() : "(broken object ref)";
                        }
                        else {
                            if (assocDecl.getToEnd().isSingular()) {
                                // case: ref to 0 or 1 independent; e.g., some primary entity type
                                // case: collective association
                                datumString = "(" + valueHolder + ")";
                            }
                        }
                    }
                    else {
                        // case: simple object (scalar) variable
                        Value value = valueHolder.getValue();
                        datumString = value.isNull() ? value.toString() : value.getJavaValue().toString();
                    }
                    sb.append(datumString + "\t");
                }
            }
        );
    }

    private void loadAstLoader(String parserImplClassName) {
        try {
            Class<?> aClass = Class.forName(parserImplClassName);
            log.debug(String.format("Found loader provider class [%s]", parserImplClassName));
            astLoader = (AstLoader) aClass.getDeclaredConstructor().newInstance();
            log.debug(String.format("Found source loader [%s]", astLoader.getName()));
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException |
            NoSuchMethodException | InvocationTargetException e) {
            log.warn("can not find or load source loader [{}]", parserImplClassName, e);
        }
    }

    public RunResults run() {

        RunResults runResults = new RunResults();

        try {

//            File exeFile = FileUtils.getFile(exeSettings.getExeMain());

            // Load Space libs and source
            List<File> srcRootDirs = exeSettings.getSpaceDirs();
//            if (!exeFile.exists()) {
//                throw new SpaceX("Input file [" + exeFile + "] does not exist.");
//            }

            // # Parse all source and load ASTs into memory
            if (srcRootDirs != null) {
                int validCount = 0;
                for (File srcRootDir : srcRootDirs) {
                    //
                    if (srcRootDir.exists()) {
                        log.info("start loading source root [" + srcRootDir + "]");
                        DirLoadResults dirLoadResults = loadSrcRootDir(srcRootDir, nsRegistry.getUserNs());
                        log.info("finished loading source root [" + srcRootDir + "]");
                        //
                        runResults.addSrcDirLoadResult(dirLoadResults);
                        validCount++;
                    }
                    else
                        log.warn("source directory [{}] does not exist", srcRootDir);
                }
            }
            else {
                log.info("no source dirs specified");
            }

            // serves to ensure that the base lang libs were loaded above
            resolveLangDir();

            // # Determine all needed Java classes and load corresponding Space wrappers
            wrapJavaImports(runResults);

            //
            java.util.Set<ParseUnit> newParseUnits =
                AstUtils.queryAst(nsRegistry.getUserNs().getRootDir(), new QueryAstConsumer(ParseUnit.class));

            // # Link all refs within the AST.
            // NOTE: Pure Space units that using Java classes will link to corresponding Space wrappers.
            try {

                if (log.isDebugEnabled())
                    nsRegistry.dumpSymbolTables();

//            if (log.isDebugEnabled())
//                dumpAsts();

                List<AstLoadError> unitLoadErrors = new LinkedList<>();
                for (ParseUnit parseUnit : newParseUnits) {
                    unitLoadErrors.clear();
                    //
                    linkAndCheckUnit(parseUnit, unitLoadErrors);
                    //
                    if (unitLoadErrors.size() == 0) {
                        log.info("no link or semantic errors in [" + parseUnit + "]");
                    }
                    else {
                        String redMsg = "Found [" + unitLoadErrors.size() + "] linker errors in [" + parseUnit + "]";
                        log.warn(redMsg + ". See error stream.");
                        System.err.println(redMsg + ":");
                        System.err.println(Strings.buildNewlineList(unitLoadErrors));
                    }
                    unitLoadErrors.forEach(runResults::addFileError);
                }
            } catch (SpaceX ex) {
                throw ex;
            } catch (Exception ex) {
                throw new SpaceX("Failed running program", ex);
            }

            int[] levelCounts = runResults.getLevelCounts();
            int errCount = levelCounts[AstLoadError.Level.ERROR.ordinal()];
            if (errCount > 0) {
                log.warn("skipping exec due to [" + errCount + "] errors.");

                presentAllErrors(runResults);
            }
            else {
                //
                AstRuler astRuler = new AstRuler();
                astRuler.setDerivedAstInfo();

                // Execute the specified type 'main' function
                if (exeSettings.getExeMain() != null)
                    exec(exeSettings.getExeMain());
                else
                    log.info("no exec main() to run");
            }
        }
        catch (SpaceX spex) {
//            PrintStream ps = System.err;
            runResults.setRuntimeError(spex);

            PrintStream psOut = System.out;
            StringBuffer sb = new StringBuffer();
            psOut.println("Space Exception: " + spex.getMessage());
            if (spex.hasError()) {
//                sb.append(e.getError().getMessage());
                if (spex.getError().getSpaceTrace() != null) {
                    writeSpaceStackTrace(sb, spex.getError().getSpaceTrace());
                }
            }
            if (spex.getCause() != null) {
//                log.error("", e.getCause());
                spex.getCause().printStackTrace(psOut);
            }
            psOut.println(sb.toString());
            log.error("java trace for Space Exception", spex);
        } finally {
            nsRegistry.dumpAsts();
        }
        return runResults;
    }

    private void wrapJavaImports(RunResults runResults) {
        Set<ImportExpr> javaImports =
            AstUtils.queryAst(nsRegistry.getUserNs().getRootDir(),
                              new QueryAstConsumer<>(
                                  ImportExpr.class,
                                  elem -> elem instanceof ImportExpr && AstUtils.isJavaNs(nsRegistry, (ImportExpr) elem)
                              )
            );
        for (ImportExpr javaImport : javaImports) {
            TypeRefImpl targetJavaTypeRef = javaImport.getTypeRefExpr();
            try {
                // the following call to getDeepLoad() ensures the Space wrapper for the
                // Java class is loaded and ready for lookup for subsequent linking
                getSjiService().getSjiTypeProxyDeepLoad(targetJavaTypeRef.getFullUrlSpec());
            } catch (ClassNotFoundException e) {
                // NOTE Java class might have been loaded already under the specified full path
                AstUtils.resolveAstRef(nsRegistry, AstUtils.findParentParseUnit(targetJavaTypeRef), targetJavaTypeRef);
                if (!targetJavaTypeRef.isResolved()) {
                    String msg = "could not find Java import class [" + targetJavaTypeRef.getFullUrlSpec() + "]" +
                        " in the Java classpath";
                    log.warn(msg);
                    AstLoadError astLoadError =
                        new AstLoadError(AstLoadError.Type.LINK, javaImport.getSourceInfo(), msg);
                    runResults.addFileError(astLoadError);
                }
            }
        }
    }

    private void resolveLangDir() {
        spaceLangDir = nsRegistry.getUserNs().getRootDir().getChildDir("space").getChildDir("lang");
        implicitImportTypes = AstUtils.getChildTypes(spaceLangDir);
    }

    private void presentAllErrors(RunResults runResults) {
        Set<File> keyFiles = runResults.getErrorsBySrcFile().keySet();
        Collection<File> nonNullKeyFiles = CollectionUtils.select(keyFiles, Objects::nonNull);
        List<File> sortedKeyList = new LinkedList<>(nonNullKeyFiles);
        Collections.sort(sortedKeyList);
        printFileErrors(runResults).accept(null);
        sortedKeyList.forEach(printFileErrors(runResults));
    }

    private Consumer<File> printFileErrors(RunResults runResults) {
        return file ->
        {
            AstFileLoadErrorSet fileErrorSet = runResults.getErrorsBySrcFile().get(file);
            if (fileErrorSet != null) {
                // Note: using system out instead of log per
                System.out.println(
                    (file != null ? file.getPath() : "(internal errors)") + ": " + fileErrorSet.getSummary() + EOL
                        + toString(fileErrorSet)
                );
            }
        };
    }

    private String toString(AstFileLoadErrorSet fileErrorSet) {
        return Strings.buildNewlineList(fileErrorSet.getErrorsAtLevel(AstLoadError.Level.ERROR));
    }

    private void preLoadSpecialNativeTypes() {

        Class[] jNativeTypes = new Class[]{
            JOpSys.class
        };

        for (Class jNativeType : jNativeTypes) {
            getSjiService().getSjiTypeProxyDeepLoad(jNativeType);
        }

        TypeRefImpl opSysRef = TypeRefImpl.newFullTypeRef(
            Language.JAVA.getCodeName() + ":" + getSjiService().getFQSpaceName(JOpSys.class)
        );
        AstUtils.resolveAstRef(nsRegistry, null, opSysRef);
        if (opSysRef.isResolvedValid())
            op_sys_type_def = opSysRef.getResolvedType();
        else
            log.error("could not resolve [" + opSysRef + "]");

        //        String jClassSeq = System.getProperty("java.class.path");
//        String[] jClassPaths = jClassSeq.split(File.pathSeparator);
//        for (String jClassPath : jClassPaths) {
////            loadJavaClassArchive(FileUtils.getFile(jClassPath));
//        }

    }

//    private void loadJavaClassArchive(File jClassPathDir) {
//        if (jClassPathDir.isFile())
//            try {
//            JarFile jarFile = new JarFile(jClassPathDir);
//            Enumeration<JarEntry> jarEntries = jarFile.entries();
//            while (jarEntries.hasMoreElements()) {
//                JarEntry jarEntry = jarEntries.nextElement();
//                String classFileName = jarEntry.getName();
//                SpaceHome.getSjiService().getDeepLoadSpaceWrapper(
//                    classFileName.substring(0, classFileName.length() - ".class".length())
//                );
//            }
//        } catch (IOException e) {
//            throw new SpaceX("failed loading Java class path", e);
//        }
//        else {
//            throw new IllegalArgumentException(
//                "The Space runtime only loads Java libs as JAR files, not exploded directories, " +
//                    "[" + jClassPathDir + "]");
//        }
//    }

    private void loadSpecialOperators() {
//        op_sys_type_def =
//            (TypeDefn) AstUtils.resolveAstPath(nsRegistry.getRootDirs(), TypeRef.newTypeRef(OpSys.class.getTypeName()));
//        MATH_TYPE_DEF = loadNativeType(Math.class, AstUtils.getLangRoot(dirChain));
//        space_opers_type_def = loadNativeType(SpaceOpers.class, AstUtils.getLangRoot(dirChain));
    }

    public DirLoadResults loadSrcRootDir(File srcRootDir) {
        return loadSrcRootDir(srcRootDir, nsRegistry.getUserNs());
    }

    public DirLoadResults loadSrcRootDir(File srcRootDir, Namespace namespace) {
        DirLoadResults dirLoadResults = null;
        try {
            dirLoadResults = astLoader.loadDir(srcRootDir);
        } catch (Exception ex) {
            throw new SpaceX("Failed loading source", ex);
        }

        if (!dirLoadResults.getSpaceRootDir().isRootDir())
            throw new SpaceX("AST loader did not return a valid root directory.");

        mergeNewChildren(namespace.getRootDir(), dirLoadResults.getSpaceRootDir());

        nsRegistry.trackMetaObject(dirLoadResults.getSpaceRootDir());

        return dirLoadResults;
    }

    /**
     * Both arg dirs have the same name and are at the same tree level, so
     * add/merge the children of newDir into parentDir.
     * @param existingParentDir
     * @param newDir
     */
    private void mergeNewChildren(Directory existingParentDir, Directory newDir) {
        //
        for (ParseUnit newChildParseUnit : newDir.getParseUnits()) {
            log.debug("adding new parse unit [{}] to dir [{}]", newChildParseUnit, existingParentDir.getFQName());
            existingParentDir.addParseUnit(newChildParseUnit);
        }
        //
        for (Directory childOfNewDir : newDir.getChildDirectories()) {
            Directory existingSubDir =
                AstUtils.getSubDirByName(existingParentDir, childOfNewDir.getNamePart().getText());
            if (existingSubDir != null) {
                mergeNewChildren(existingSubDir, childOfNewDir);
            }
            else {
                log.debug("adding new dir [{}] to [{}]", childOfNewDir.getFQName(), existingParentDir.getFQName());
                existingParentDir.addDir(childOfNewDir);
            }
        }
    }

    public void linkAndCheckUnit(ParseUnit parseUnit, List<AstLoadError> unitLoadErrors) {
        // link ...
        linkUnitRefs(parseUnit, unitLoadErrors);

        // type check ...
        if (unitLoadErrors.size() == 0) {
            validateSemantics(parseUnit, unitLoadErrors);
        }
    }

    private void validateSemantics(ParseUnit parseUnit, List<AstLoadError> errors) {
    /*
        - Check left-side of assignment is a datum.
        - Check right-side of assignment is a value expr.
        - Check RHS type is assignable to LHS.
        - Check nested function call arg type matches function arg type
        - Insert implicit cast of new tuples if needed
        - Check symbolic expression arg types match that of operator
        - Check chained expression types
        - Check namespace uniqueness / ambiguity
     */
        AstUtils.queryAst(parseUnit, new QueryAstConsumer<>(AssignmentExpr.class));
        AstUtils.queryAst(parseUnit, new QueryAstConsumer<>(FunctionCallExpr.class));
        AstUtils.queryAst(parseUnit, new QueryAstConsumer<>(AssignmentExpr.class));

        validateMetaTypes(parseUnit, errors);

        String msg = "datum expected";
    }

    private void validateMetaTypes(ParseUnit parseUnit, List<AstLoadError> errors) {
        Set<ExpressionChain> chains =
            AstUtils.queryAst(parseUnit,
                              new QueryAstConsumer<>(ExpressionChain.class,
                                                     astNode ->
                                                         astNode instanceof ExpressionChain &&
                                                             ((ExpressionChain) astNode).isResolved())
            );
        for (ExpressionChain chain : chains) {
            if (chain.hasTargetMetaType()) {
                if (chain.getTargetMetaType() == chain.getResolvedMetaObj().getMetaType()) {
                    chain.setTypeCheckState(TypeCheckState.VALID);
                }
                else {
                    AstLoadError error = new AstLoadError(AstLoadError.Type.SEMANTIC, chain.getSourceInfo(),
                                                          "expression '" + chain + "' must reference a " +
                                                              chain.getTargetMetaType());
                    errors.add(error);
                    log.info(error.toString());
                }
            }
        }
    }

    private void linkUnitRefs(ParseUnit parseUnit, List<AstLoadError> unitLoadErrors) {

        resolveImports(parseUnit, unitLoadErrors);

        linkRefs(parseUnit, unitLoadErrors);

    }

    /*
     Augments named references to oid-based references after all meta objects
     have been loaded.  Has the effect of doing semantic validation.
     Could also do some linking while loading.
     */
    private void linkRefs(ParseUnit parseUnit, List<AstLoadError> errors) {
        RefLinker astLinker = new RefLinker(parseUnit, errors);
        // link all type refs
        AstUtils.walkAstDepthFirst(parseUnit, astLinker);
        // link all other unresolved refs
        astLinker.setFirstPass(false);
        AstUtils.walkAstDepthFirst(parseUnit, astLinker);
        return;
    }

    public void resolveImports(ParseUnit parseUnit, List<AstLoadError> unitErrors) {
        List<ImportExpr> importExprExprs = parseUnit.getImportExprs();
        for (ImportExpr importTypeRefExpr : importExprExprs) {
            if (importTypeRefExpr.getTypeRefExpr().isAtInitState()) {
                resolveAstPath(parseUnit, importTypeRefExpr.getTypeRefExpr(), unitErrors);
                if (importTypeRefExpr.getTypeRefExpr().isSingleton()) {
                    if (importTypeRefExpr.getTypeRefExpr().isResolved())
                        parseUnit.addToAllImportedTypes(
                            (TypeDefn) importTypeRefExpr.getTypeRefExpr().getResolvedMetaObj());
//                    else
//                        log.warn("import reference not fully resolved ["+importTypeRefExpr+"]");
                }
                else {
                    Set<TypeDefn> refElems = AstUtils.getSiblingTypes(importTypeRefExpr.getTypeRefExpr());
                    parseUnit.getAllImportedTypes().addAll(refElems);

                    // add all types local to this parse unit's directory
                    parseUnit.getAllImportedTypes().addAll(
                        AstUtils.queryAst(parseUnit.getParent(), new QueryAstConsumer<>(TypeDefnImpl.class))
                    );
                }
            }
        }
    }

    private void resolveAstPath(ParseUnit parseUnit, ExpressionChain reference, List<AstLoadError> errors) {
        AstUtils.resolveAstRef(nsRegistry, parseUnit, reference);
        if (!reference.isResolved()) {
            AstLoadError error = new AstLoadError(AstLoadError.Type.LINK, reference.getSourceInfo(),
                                                  "could not resolve symbol '" + getFirstUnresolved(reference) + "'");
            errors.add(error);
            reference.setAstLoadError(error);
            log.info(error.toString());
        }
        else {
            log.debug("resolved ref [" + reference + "]");
        }
    }

    private NameRefOrHolder getFirstUnresolved(ExpressionChain refChain) {
        NameRefOrHolder unresolved = null;
        if (refChain.getFirstPart().hasNameRef()
            && !refChain.getFirstPart().getNameRef().getRefAsNameRef().isResolved()) {
            unresolved = (NameRefOrHolder) refChain.getFirstPart();
        }

        if (unresolved == null) {
            List<NameRefOrHolder> restLinks = refChain.getRestLinks();
            for (NameRefOrHolder link : restLinks) {
                if (link.hasNameRef() && !link.getRefAsNameRef().isResolved()) {
                    unresolved = link;
                    break;
                }
            }
        }
        return unresolved;
    }

    /**
     * The entry point method for 'running' a Space program. Executes the 'main' function.
     */
    public ModelElement exec(String mainSpacePath) {
        log.debug("exec: " + mainSpacePath);

        IntrinsicSourceInfo sourceInfo = new IntrinsicSourceInfo();
        String[] pathNodes = mainSpacePath.split("/.");
        TypeRefImpl exeTypeRef = getAstFactory().newTypeRef(sourceInfo, null, null);
        SimpleNameRefExpr userNsRefPart = getAstFactory().newNameRefExpr(sourceInfo, NSRegistry.NS_USER);
        exeTypeRef.setNsRefPart(userNsRefPart);
        getAstFactory().addNewMetaRefParts(exeTypeRef, sourceInfo, pathNodes);
        AstUtils.resolveAstRef(nsRegistry, null, exeTypeRef);
        TypeDefnImpl bootTypeDefn = (TypeDefnImpl) exeTypeRef.getResolvedType();
//        SpaceTypeDefn bootTypeDefn = progSpaceDir.getFirstSpaceDefn();
        if (bootTypeDefn == null)
            throw new SpaceX("main type '{}' not found", exeTypeRef);

        Tuple mainTypeTuple = newTupleImpl(bootTypeDefn);
//        evalContext = new EvalContext();
        FunctionDefnImpl bootMainFunctionDefn = bootTypeDefn.getFunction("main");
        if (bootMainFunctionDefn == null)
            throw new SpaceX("no main() function in type '{}'", exeTypeRef);

        ProgSourceInfo progSourceInfo = new ProgSourceInfo();
        FunctionCallExpr bootMainCallExpr = getAstFactory().newFunctionCallExpr(progSourceInfo);
        SimpleNameRefExpr<FunctionDefn> mainFuncRef =
            getAstFactory().newNameRefExpr(getAstFactory().newNamePartExpr(sourceInfo, null, "main"));
        bootMainCallExpr.setFunctionRef(mainFuncRef);
        bootMainCallExpr.getFunctionRef().setResolvedMetaObj(bootMainFunctionDefn);
        bootMainCallExpr.getFunctionRef().setTypeCheckState(TypeCheckState.VALID);
        ValueExprSequenceExpr theMainArg = getAstFactory().newSequenceExpr(
            progSourceInfo,
            getAstFactory().newTypeRef(CHAR_SEQ_TYPE_DEF.getSequenceOfType())
        );
        theMainArg.addValueExpr(getAstFactory().newCharSeqLiteralExpr(progSourceInfo, "(put CLI here)"));
        LinkedList<ValueExpr> argExprList = new LinkedList<>();
        argExprList.add(theMainArg);
        bootMainCallExpr.setArgValueExpr(
            getAstFactory().newNewObjectExpr(
                progSourceInfo,
                getAstFactory().newTypeRef(bootMainFunctionDefn.getArgumentsDefn()),
                getAstFactory().newTupleValueExprList(progSourceInfo).setValueExprs(argExprList)
            ));
        ParseUnit synthParseUnit = getAstFactory().newParseUnit(sourceInfo);
        synthParseUnit.addChild(bootTypeDefn);
        //
        linkRefs(synthParseUnit, null);
        FunctionCallContext bootCallCtxt =
            getObjFactory().newFunctionCall(mainTypeTuple, bootMainCallExpr, null, null);
        // init the context tuple
        callStack.push(bootCallCtxt);
        // this evaluates the init statements for the type such as var inits.
        eval(this, bootTypeDefn.getInitializations());
        // call the 'main' function a dummy function call expr
        eval(this, bootMainCallExpr);
        callStack.pop();
        log.debug("exiting Space program execution");
        return null;
    }

    private void eval(EvalContext evalContext, List<Statement> initStatements) {
        for (Statement statement : initStatements) {
            // must be assignments or statement blocks
            eval(evalContext, statement);
        }
    }

    /**
     * The dispatcher eval method.
     */
    private ValueHolder eval(EvalContext spcContext, ValueExpr expression) {
        log.debug("eval: (deleg) " + expression);
        ValueHolder valueHolder = null;
        if (expression instanceof NewTupleExpr) {
            valueHolder = eval(spcContext, (NewTupleExpr) expression);
        }
        else if (expression instanceof NewSetExpr) {
            valueHolder = eval(spcContext, (NewSetExpr) expression);
        }
        else if (expression instanceof OperatorExpr) {
            valueHolder = eval(spcContext, (OperatorExpr) expression);
        }
        else if (expression instanceof PrimitiveLiteralExpr) {
            valueHolder = eval(spcContext, (PrimitiveLiteralExpr) expression);
        }
        else if (expression instanceof ValueSequenceLiteralExpr) {
            valueHolder = eval(spcContext, (ValueSequenceLiteralExpr) expression);
        }
        else if (expression instanceof ThisArgExpr) {
            valueHolder = eval(spcContext, (ThisArgExpr) expression);
        }
        else if (expression instanceof ThisTupleExpr) {
            valueHolder = eval(spcContext, (ThisTupleExpr) expression);
        }
        else if (expression instanceof MetaRefPath) {
            valueHolder = eval(spcContext, ((MetaRefPath) expression).getLastLink());
        }
        else if (expression instanceof MetaRef) {
            valueHolder = eval(spcContext, (MetaRef) expression);
        }
        else if (expression instanceof AssignmentExpr) {
            valueHolder = eval(spcContext, (AssignmentExpr) expression);
        }
        else if (expression instanceof UpdateExpr) {
            valueHolder = eval(spcContext, (UpdateExpr) expression);
        }
        else if (expression instanceof FunctionCallExpr) {
            valueHolder = eval(spcContext, (FunctionCallExpr) expression);
        }
        else if (expression instanceof ExpressionChain) {
            ValueExprChain valueExprChain = ((ExpressionChain) expression).extractValueExprChain();
            if (!valueExprChain.isEmpty())
                valueHolder = eval(spcContext, valueExprChain);
        }
        else
            throw new SpaceX("don't know how to evaluate " + expression);

        log.debug("eval -> " + valueHolder);
        return valueHolder;
    }

    private ValueHolder eval(EvalContext evalContext, ThisTupleExpr thisTupleExpr) {
        log.debug("exec: " + thisTupleExpr);
        Tuple ctxObject = (Tuple) evalContext.peekCallStack().getCtxObject();
        return getObjFactory().newReferenceByOidHolder(ctxObject, null, ctxObject.getOid());
    }

    private ValueHolder eval(EvalContext evalContext, ThisArgExpr thisArgExpr) {
        log.debug("exec: " + thisArgExpr);
        TupleImpl argTuple = evalContext.peekCallStack().getArgTuple();
        return getObjFactory().newReferenceByOidHolder(argTuple, null, argTuple.getOid());
    }

    /**
     * TODO I think we'll eventually want all statements to get put on an Action Queue
     * or Transaction Queue or such. In effect, each statement becomes a first-class
     * action / job.
     */
    private void eval(EvalContext evalContext, StatementBlock statementBlock) {
        log.debug("eval: " + statementBlock);
        FunctionCallContext functionCallContext = evalContext.peekCallStack();
        BlockDatumMap blockDatumMap = getObjFactory().newBlockDatumMap(statementBlock);
        functionCallContext.addBlockContext(getObjFactory().newBlockContext(statementBlock, blockDatumMap));
        List<Statement> statementSequence = statementBlock.getStatementSequence();
        for (Statement statement : statementSequence) {
            eval(evalContext, statement);
            if (evalContext.peekCallStack().isPendingReturn())
                break;
        }
        functionCallContext.popBlock();
        return;
    }

    private void eval(EvalContext evalContext, Statement statement) {
        FunctionCallContext functionCallContext = evalContext.peekCallStack();
        if (statement instanceof ExprStatement) {
            ExprStatement exprHolderStatement = (ExprStatement) statement;
            eval(evalContext, exprHolderStatement.getExpression());
        }
        else if (statement instanceof IfStatement) {
            // TODO
        }
        else if (statement instanceof ForEachStatement) {
            // TODO
        }
        else if (statement instanceof StatementBlock) {
            eval(evalContext, ((StatementBlock) statement));
        }
        else if (statement instanceof ReturnExpr) {
            ValueHolder retVal = eval(evalContext, ((ReturnExpr) statement).getValueExpr());
            autoCastAssign(functionCallContext.getReturnValueHolder(), retVal);
            functionCallContext.setPendingReturn(true);
        }
        return;
    }


//    private Tuple eval(EvalContext evalContext, NewTupleExpr newTupleExpr) {
//        log.debug("eval: " + newTupleExpr);
//        Tuple value = eval(evalContext, (TypeDefn) newTupleExpr.getTypeRef().getResolvedMetaObj(),
//                           newTupleExpr.getTupleExpr());
//        return value;
//    }
    private ValueHolder eval(EvalContext evalContext, NewSetExpr newSetExpr) {
        log.debug("eval: " + newSetExpr);
        TupleSetImpl tupleSet = newSet(null);
        newSetExpr.getNewTupleExprs().forEach(
            newTupleExpr -> {
                DetachedHolder tupleRefHolder = eval(evalContext, newTupleExpr);
                tupleSet.addTuple((SpaceOid) tupleRefHolder.getValue());
            }
        );
        return tupleSet;
    }

    private ValueHolder eval(EvalContext evalContext, OperatorExpr operatorExpr) {
        log.debug("eval: " + operatorExpr);
        ValueHolder valueHolder = null;
        OperEvaluator oper = lookupOperEval(operatorExpr.getOper());
        if (oper == null)
            throw new SpaceX(evalContext.newRuntimeError("no operator evaluator for " + operatorExpr.getOper()));
        Value[] argValues = new Value[operatorExpr.getArgs().size()];
        for (int idxArg = 0; idxArg < operatorExpr.getArgs().size(); idxArg++) {
            argValues[idxArg] = eval(evalContext, operatorExpr.getArgs().get(idxArg)).getValue();
        }
        Value value = oper.eval(argValues);
        valueHolder = getObjFactory().newDetachedHolder(operatorExpr.getOper().getOperType(), value);
        return valueHolder;
    }

    private ValueHolder eval(EvalContext evalContext, FunctionCallExpr functionCallExpr) {
        log.debug("eval: " + functionCallExpr);

        FunctionDefn functionDefn = functionCallExpr.getFunctionRef().getResolvedMetaObj();
        TypeDefn argTypeDefn = functionDefn.getArgumentsDefn();
        TupleImpl argTupleValue = (TupleImpl) eval(evalContext, functionCallExpr.getArgValueExpr());
        ValueHolder retValHolder = functionDefn.isReturnVoid() ? evalContext.newVoidHolder()
            : getObjFactory().newEmptyVarHolder(null, functionDefn.getReturnAnonDecl());
        FunctionCallContext functionCallContext =
            getObjFactory().newFunctionCall(((Tuple) evalContext.peekCallStack().getCtxObject()),
                                            functionCallExpr, argTupleValue, retValHolder);
        // push call onto stack
        evalContext.pushCallStack(functionCallContext);
        log.debug("pushed call stack. size [" + evalContext.callStackSize() + "]");
        try {
            if (functionDefn instanceof FunctionDefnImpl) {
                FunctionDefnImpl targetFunctionDefn =
                    (FunctionDefnImpl) functionDefn;
                eval(evalContext, targetFunctionDefn.getStatementBlock());
            }
            else if (functionDefn instanceof SjiFunctionDefnImpl) {
                evalNative(evalContext, functionCallExpr);
            }
            evalContext.peekCallStack().getReturnValueHolder().getValue();
        } finally {
            FunctionCallContext popCall = evalContext.popCallStack();
            popCall.setPendingReturn(false);
            log.debug("popped call stack. size [" + evalContext.callStackSize() + "]");
        }
        log.debug("eval -> " + retValHolder);
        return retValHolder;
    }

    private DetachedHolder<ReferenceByOid, SpaceOid> eval(EvalContext evalContext, NewTupleExpr newTupleExpr) {
        log.debug("eval: new tuple => " + newTupleExpr);
        TupleImpl newTuple = newTupleImpl(newTupleExpr.getTypeRef().getResolvedType());
        List<ValueExpr> valueExprs = newTupleExpr != null ? newTupleExpr.getTupleValueList().getValueExprs() : null;
        // validation
        int numValueExprs = valueExprs == null ? 0 : valueExprs.size();
        if (numValueExprs != newTuple.getSize())
            throw new SpaceX(evalContext.newRuntimeError(
                "tuple value list does not match type definition. expected " + newTuple.getSize()
                    + " received " + numValueExprs + ""));

        // Tuple value assignments may be by name or by order (like a SQL update statement), but not both.
        if (valueExprs != null) {
            int idxArg = 0;
            for (ValueExpr argumentExpr : valueExprs) {
                ValueHolder argValueHolder = eval(evalContext, argumentExpr);
                argValueHolder = autoCastOrBox(newTuple.getDefn().getDatumDeclList().get(idxArg).getType(), argValueHolder);
                ValueHolder leftSideHolder = newTuple.get(newTuple.getDeclAt(idxArg));
                SpaceUtils.assignNoCast(evalContext, leftSideHolder, argValueHolder);
                idxArg++;
            }
        }

        return getObjFactory().newDetachedReferenceHolder(newTuple.getDefn(), newTuple.getOid());
    }

    /**
     * Invokes a Java native method all via Java reflection.  Native actions
     * do not have nested actions, at least none that are executed or controlled
     * by this executor.
     */
    private ValueHolder evalNative(EvalContext evalContext, FunctionCallExpr functionCallExpr) {
        log.debug("eval: " + functionCallExpr);
        ValueHolder valueHolder = null;
        FunctionCallContext functionCallContext = evalContext.peekCallStack();
        try {
            List<ValueHolder> spaceArgHolders = functionCallContext.getArgTuple().getValueHolders();
            Object javaArgs[] = new Object[spaceArgHolders.size()];
            int idxArg = 0;
            for (ValueHolder spaceArgHolder : spaceArgHolders) {
                ValueHolder javaArgHolder = spaceArgHolder;
                if (!spaceArgHolder.getType().isSjiWrapper()) {
                    SjiTypeDefn sjiArgsDef = (SjiTypeDefn)
                        functionCallExpr.getFunctionRef().getResolvedMetaObj().getArgumentsDefn();
                    DatumDecl sjiArgDecl = sjiArgsDef.getDatumDeclList().get(idxArg);
                    if (!sjiArgDecl.getType().equals(spaceArgHolder.getType())) {
                        javaArgHolder = casters.cast(spaceArgHolder, sjiArgDecl.getType());
                    }
                }
                javaArgs[idxArg] = javaArgHolder.getValue().getJavaValue();
                idxArg++;
            }
            SjiFunctionDefnImpl funcDef =
                (SjiFunctionDefnImpl) functionCallExpr.getFunctionRef().getResolvedMetaObj();
            Object jObjectDummy =
                funcDef.getjMethod().getDeclaringClass().getDeclaredConstructor().newInstance();
            Object jValue = funcDef.getjMethod().invoke(jObjectDummy, javaArgs);

            // TODO Do we need special handling / casting for Java Strings?

//            if (jValue instanceof String) {
            SjiTuple sjiTupleValue = getSjiService().getOrCreateSjiObjectProxy(jValue);
            valueHolder = getObjFactory().newReferenceByOidHolder(null, null, sjiTupleValue.getOid());
//                valueHolder = casters.stringToCharSeq(evalContext, ((String) jValue));
//            }
        } catch (Exception e) {
            log.error("error invoking native method", e);
            throw new SpaceX(evalContext.newRuntimeError("error invoking native method " + e.getMessage()));
        }
        if (valueHolder != null) {
            autoCastAssign(functionCallContext.getReturnValueHolder(), valueHolder);
        }
        functionCallContext.setPendingReturn(true);
        log.debug("eval -> " + valueHolder);
        return valueHolder;
    }

    private ValueHolder eval(EvalContext evalContext, AssignmentExpr assignmentExpr) {
        log.debug("eval: " + assignmentExpr);
        ValueHolder leftSideHolder =
            eval(evalContext, assignmentExpr.getLeftSideDatumRef().extractMetaRefPath().getLastLink());
        ValueHolder rightSideHolder = eval(evalContext, assignmentExpr.getRightSideValueExpr());
        autoCastAssign(leftSideHolder, rightSideHolder);
        log.debug("eval -> " + leftSideHolder);
        return leftSideHolder;
    }

    public ValueHolder eval(EvalContext evalContext, UpdateExpr updateExpr) {
        log.debug("eval: " + updateExpr);
        ValueHolder<ReferenceValue<SpaceOid>, SpaceOid> baseTupleHolder = eval(evalContext, updateExpr.getLeftSideDatumRef());
        Tuple baseTuple = (Tuple) evalContext.dereferenceByOid(baseTupleHolder.getValue().getJavaValue());
        // TODO Validate: must be the basis tuple, not a view
        ValueHolder rightSideHolder = eval(evalContext, updateExpr.getRightSideValueExpr());
        Tuple deltaTuple = (Tuple) evalContext.dereferenceByOid((SpaceOid) rightSideHolder.getValue().getJavaValue());
        //
        deltaTuple.getValueHolders().forEach(
            inputSlot -> {
                autoCastAssign(baseTuple.get(inputSlot.getDeclaration()), inputSlot);
            }
        );
        log.debug("eval -> " + baseTuple);
        return baseTupleHolder;
    }

    /**
     * Returns the literal value object associated with the {@link PrimitiveLiteralExpr}.
     * The returned object may be a scalar, a character sequence, or a complete
     * space.
     */
    private ValueHolder eval(EvalContext evalContext, PrimitiveLiteralExpr primitiveLiteralExpr) {
        log.debug("eval: " + primitiveLiteralExpr);
        ValueHolder valueHolder = null;
        if (primitiveLiteralExpr.getTypeDefn() == NumPrimitiveTypeDefn.CARD) {
//            case TEXT:
//                CharacterSequence csLiteral = newCharacterSequence(primitiveLiteralExpr.getValueExpr());
//                value = newReference(csLiteral);
//                break;
            valueHolder = getObjFactory().newCardinalValueHolder(
                ((Tuple) evalContext.peekCallStack().getCtxObject()),
                newAnonVarDecl(primitiveLiteralExpr.getSourceInfo(), null, NumPrimitiveTypeDefn.CARD),
                Integer.parseInt(primitiveLiteralExpr.getStringValue())
            );
        }
        else if (primitiveLiteralExpr.getTypeDefn() == NumPrimitiveTypeDefn.BOOLEAN) {
            valueHolder = getObjFactory().newBooleanValueHolder(
                ((Tuple) evalContext.peekCallStack().getCtxObject()),
                newAnonVarDecl(primitiveLiteralExpr.getSourceInfo(), null, NumPrimitiveTypeDefn.BOOLEAN),
                Boolean.parseBoolean(primitiveLiteralExpr.getStringValue())
            );
        }
        else if (primitiveLiteralExpr.getTypeDefn() == NumPrimitiveTypeDefn.CHAR) {
            valueHolder = getObjFactory().newCharacterValueHolder(
                ((Tuple) evalContext.peekCallStack().getCtxObject()),
                newAnonVarDecl(primitiveLiteralExpr.getSourceInfo(), null, NumPrimitiveTypeDefn.CARD),
                primitiveLiteralExpr.getStringValue().charAt(0)
            );
        }
        else if (primitiveLiteralExpr.getTypeDefn() == NumPrimitiveTypeDefn.REAL) {
            valueHolder = getObjFactory().newRealValueHolder(
                ((Tuple) evalContext.peekCallStack().getCtxObject()),
                newAnonVarDecl(primitiveLiteralExpr.getSourceInfo(), null, NumPrimitiveTypeDefn.REAL),
                Double.valueOf(primitiveLiteralExpr.getStringValue())
            );
        }
//       else if (primitiveLiteralExpr.getTypeDefn() ==  RATIONAL) {
//       }
        else {
            throw new SpaceX(evalContext.newRuntimeError(
                "Can not yet handle primitive literal of type " + primitiveLiteralExpr.getTypeDefn()));
        }
        log.debug("eval -> " + valueHolder);
        return valueHolder;
    }

    private VariableDecl newAnonVarDecl(SourceInfo sourceInfo, DatumDeclContext fromDatumContext,
                                            NumPrimitiveTypeDefn varTypeDefn)
    {
        return astFactory.newVariableDecl(sourceInfo, null, fromDatumContext,
                                          astFactory.newTypeRef(varTypeDefn));
    }

//    private SpaceObject exec(Space userSpaceContext, SpacePathExpr pathExpr) {
//        SpaceObject value = null;
//
//        return value;
//    }

    private ValueHolder eval(EvalContext evalContext, ValueSequenceLiteralExpr sequenceLiteralExpr) {
        log.debug("eval: " + sequenceLiteralExpr);
        ValueHolder valueHolder = null;
        Value value = null;
        if (sequenceLiteralExpr.getTypeRef().getResolvedType() == CHAR_SEQ_TYPE_DEF) {
            CharacterSequence charSequence = evalContext.newCharSequence(sequenceLiteralExpr.getStringValue());
            value = evalContext.getObjFactory().newReferenceByOid(charSequence.getOid());
            valueHolder = getObjFactory().newDetachedHolder(CHAR_SEQ_TYPE_DEF, value);
        }
        else {
            throw new SpaceX(
                evalContext.newRuntimeError("Can not yet handle sequence literal of type " + sequenceLiteralExpr.getTypeRef()));
        }
        log.debug("eval -> " + valueHolder);
        return valueHolder;
    }

    private ValueHolder eval(EvalContext evalContext,
                             ValueExprSequenceExpr valueExprSequenceExpr,
                             TypeDefn containedTypeDef)
    {
        log.debug("eval: " + valueExprSequenceExpr);
        ValueHolder valueHolder = null;
        ObjectRefSequenceImpl sequence = getObjFactory().newObjectSequence(containedTypeDef);
        for (ValueExpr valueExpr : valueExprSequenceExpr.getValueExprs()) {
            sequence.add((ReferenceValue) eval(evalContext, valueExpr).getValue());
        }
        valueHolder = getObjFactory().newDetachedReferenceHolder(containedTypeDef, sequence.getOid());
        log.debug("eval -> " + valueHolder);
        return valueHolder;
    }

    private ValueHolder eval(EvalContext evalContext, ExpressionChain exprChain) {
        log.debug("eval: " + exprChain);
        ValueHolder valueHolder = null;
        if (exprChain.hasValueChain())
            valueHolder = eval(evalContext, exprChain.extractValueExprChain());

        log.debug("eval -> " + valueHolder);
        return valueHolder;
    }

    /**
     * This method expects a path of 1 or more parts, the final of which must
     * refer to a datum. Used for LHS and RHS datum eval.
     *
     * TODO Good place to enforce access control
     *
     * @return The Value Holder corresponding to the referenced datum.
     */
    private ValueHolder eval(EvalContext evalContext, MetaRef scopedRef) {
        log.debug("getValueHolder: " + scopedRef);
        ValueHolder valueHolder = null;
        DatumDecl toMember = (DatumDecl) scopedRef.getResolvedMetaObj();
        FunctionCallContext functionCallContext = evalContext.peekCallStack();
        DatumMap valDatumMap = null;
        switch (scopedRef.getScopeKind()) {
            case OBJECT:
                valDatumMap = ((Tuple) functionCallContext.getCtxObject());
                break;
            case BLOCK:
                valDatumMap = findInBlocksRec(toMember, functionCallContext.getBlockContexts());
                break;
            case ARG:
                valDatumMap = functionCallContext.getArgTuple();
                break;
            case STATIC:
                valDatumMap = evalContext.getStaticExeContext().getStaticTuple(toMember.getType());
                break;
        }

        valueHolder = valDatumMap.get(toMember);

        if (valueHolder == null)
            throw new SpaceX(evalContext.newRuntimeError("could not resolve reference " + scopedRef));

        log.debug("eval -> " + valueHolder);
        return valueHolder;
    }

    private ValueHolder eval(EvalContext evalContext, ValueExprChain valueExprChain) {
        ValueHolder chainValueHolder = null;
        if (!valueExprChain.isEmpty()) {
            for (ValueExpr valueExpr : valueExprChain.getChain()) {
                chainValueHolder = eval(evalContext, valueExpr);
                if (chainValueHolder.getDeclaration().isRef()) {
                    SpaceOid tupleOid = (SpaceOid) chainValueHolder.getValue();
                    evalContext.peekCallStack().pushCtxObject(evalContext.dereferenceByOid(tupleOid));
                }
            }
        }
        return chainValueHolder;
    }

    private BlockDatumMap findInBlocksRec(Named toMember, LinkedList<BlockContext> blocks) {
        ValueHolder assignable = null;
        BlockContext blockContext = null;
        Iterator<BlockContext> blockContextIterator = blocks.descendingIterator();
        while (assignable == null && blockContextIterator.hasNext()) {
            blockContext = blockContextIterator.next();
            assignable = blockContext.getBlockDatumMap().get((DatumDecl) toMember);
        }
        return blockContext.getBlockDatumMap();
    }

    private OperEvaluator lookupOperEval(Operators.Operator operator) {
        return operEvalMap.get(operator);
    }

    @Override
    public void autoCastAssign(ValueHolder leftSideHolder, ValueHolder rightSideHolder) {
        ValueHolder rhsActual = rightSideHolder;
        // cast the assigned RHS value if possible to be consistent with the LHS
        if (!leftSideHolder.getType().getOid().equals(rightSideHolder.getType().getOid())) {
            rhsActual = autoCastOrBox(leftSideHolder.getType(), rightSideHolder);
        }
        // set the value object of LHS, taking into account references vs scalar values
        SpaceUtils.assignNoCast(this, leftSideHolder, rhsActual);
    }

    private ValueHolder autoCastOrBox(TypeDefn targetTypeDefn, ValueHolder fromHolder) {
        return casters.cast(fromHolder, targetTypeDefn);
    }

    @Override
        public TupleImpl newTupleImpl(TypeDefn defn) {
            TupleImpl tuple = getObjFactory().newTupleImpl(defn);
            initTrackTuple(defn, tuple);
            return tuple;
        }

        private void initTrackTuple(TypeDefn defn, Tuple tuple) {
            // initialize
            if (defn.hasDatums()) {
                List<DatumDecl> declList = defn.getDatumDeclList();
                for (DatumDecl datumDecl : declList) {
                    tuple.initHolder(getObjFactory().newEmptyVarHolder(tuple, datumDecl));
                }
            }

            trackInstanceObject(tuple);
        }

        @Override
        public void apiAstLoadComplete() {

            run();

        }

    private void setTypeKeyComprators() {
        // set key comparators
        Set<TypeDefn> newTypeDefs =
            AstUtils.queryAst(nsRegistry.getUserNs().getRootDir(),
                              new QueryAstConsumer<>(
                                  TypeDefn.class, (typeDefn) ->
                                  getNsRegistry().getUserNs().getTypeInfo(typeDefn) == null
                              )
            );

        newTypeDefs.forEach( typeDefn -> {
            if (typeDefn.hasPrimaryKey()) {
                Namespace.TypeDerivedInfo typeInfo = new Namespace.TypeDerivedInfo(typeDefn);
                typeInfo.setPkComparator(Comparators.buildProjectionComparator(typeDefn.getPrimaryKeyDefn()));
                getNsRegistry().getUserNs().setTypeInfo(typeDefn, typeInfo);
                log.info("set derived PK comparator for {}", typeDefn);
            }
        });
    }

    public void attachTypesToUserNs(TypeDefn ... typeDefs) {
        ParseUnit parseUnit = getAstFactory().newParseUnit(SourceInfo.API);
        for (TypeDefn typeDef : typeDefs) {
            parseUnit.addTypeDefn(typeDef);
        }
        // Must add type elements to a namespace before anything will work
        getNsRegistry().getUserNs().getRootDir().addParseUnit(parseUnit);
    }

    @Override
    public Space getDefaultSpace() {
        return defaultSpace;
    }

    @Override
    public Space newSpace() {
        return null;
    }

    @Override
    public TupleSetImpl newSet(SetTypeDefn setTypeDefn) {
        TupleSetImpl tupleSet = getObjFactory().newSet(setTypeDefn);
        trackInstanceObject(tupleSet);
        return tupleSet;
    }

    private DeclaredReferenceHolder<SpaceOid> newReferenceHolderByOid(DatumDecl datumDecl, Tuple tuple,
                                                                      SpaceOid toOid)
    {
        return getObjFactory().newReferenceByOidHolder(tuple, datumDecl, toOid);
    }

    @Override
    public CharacterSequence newCharSequence(String stringValue) {
        CharacterSequence newCs = getObjFactory().newCharacterSequence(stringValue);
        trackInstanceObject(newCs);
        return newCs;
    }

    public ValueHolder newVoidHolder() {
        ValueHolder holder = getObjFactory().newVoidHolder();
        return holder;
    }

    @Override
    public void pushCallStack(FunctionCallContext functionCallContext) {
        callStack.push(functionCallContext);
    }

    @Override
    public FunctionCallContext peekCallStack() {
        return callStack.peek();
    }
    @Override
    public int callStackSize() {
        return callStack.size();
    }

    @Override
    public FunctionCallContext popCallStack() {
        return callStack.pop();
    }

    @Override
    public RuntimeError newRuntimeError(String msg) {
        return new RuntimeError(copyCallStack(), msg);
    }

    public Stack<FunctionCallContext> copyCallStack() {
        Stack<FunctionCallContext> copy = new Stack<>();
        copy.addAll(callStack);
        return copy;
    }

    private void writeSpaceStackTrace(StringBuffer sb, Stack<FunctionCallContext> spaceTrace) {
        sb.append(Strings.buildDelList(spaceTrace, STACK_TRACE_LISTER, EOL));
    }

    public interface ExeSettings {

        String getExeMain();

        List<File> getSpaceDirs();

    }

    private static class StackTraceLister implements Lister {

        @Override
        public String getListString(Object obj) {
            FunctionCallContext cctx = (FunctionCallContext) obj;
            NamedElement lexParent = AstUtils.getNearestNamedParent(cctx.getAstNode());
            SourceInfo sourceInfo = cctx.getAstNode().getSourceInfo();
            return "\t at "
                + (lexParent != null ? lexParent.getFQName() : "(init)")
                + "("
                + (sourceInfo instanceof FileSourceInfo ?
                ((FileSourceInfo) sourceInfo).getFile().getName()
                    + ":" + ((FileSourceInfo) sourceInfo).getStart().getLine()
                : "(boot)")
                + ")";
        }

    }

    public static class QueryAstConsumer<T extends ModelElement> implements AstScanConsumer {

        private java.util.Set<T> results = new HashSet<>();
        private Class<T> clazz;
        private Predicate<T> filter = modelElement -> false;
        private Predicate<T> classCheckPredicate;

        public QueryAstConsumer(Class<T> clazz, Predicate<T> extraFilter) {
            this.clazz = clazz;
            this.classCheckPredicate = modelElement -> this.clazz.isAssignableFrom(modelElement.getClass());
            this.filter = extraFilter != null ?
                testee -> classCheckPredicate.test(testee) && extraFilter.test(testee) :
                classCheckPredicate;
        }

        public QueryAstConsumer(Class<T> clazz) {
            this(clazz, null);
        }

        @Override
        public Predicate<ModelElement> getFilter() {
            return (Predicate<ModelElement>) filter;
        }

        @Override
        public boolean upon(ModelElement astNode) {
            if (clazz.isAssignableFrom(astNode.getClass()))
                results.add((T) astNode);
            return true;
        }

        @Override
        public void after(ModelElement astNode) {

        }

        public java.util.Set<T> getResults() {
            return results;
        }
    }

    public static class FindFirstAstConsumer<T extends ModelElement> implements AstScanConsumer {

        private T result = null;
        private Class<T> clazz;
        private Predicate<ModelElement> filter = modelElement -> false;

        public FindFirstAstConsumer(Class<T> clazz, Predicate<ModelElement> filter) {
            this.clazz = clazz;
            this.filter = filter;
        }

        public FindFirstAstConsumer(Class<T> clazz) {
            this.clazz = clazz;
            this.filter = modelElement -> this.clazz.isAssignableFrom(modelElement.getClass());
        }

        public void clearResult() {
            this.result = null;
        }

        @Override
        public Predicate<ModelElement> getFilter() {
            return filter;
        }

        @Override
        public boolean upon(ModelElement astNode) {
            boolean keepGoing = true;
            if (clazz.isAssignableFrom(astNode.getClass())) {
                result = (T) astNode;
                keepGoing = false;
            }
            return keepGoing;
        }

        @Override
        public void after(ModelElement astNode) {

        }

        public boolean hasResult() {
            return result != null;
        }

        public T getResult() {
            return result;
        }
    }

    private class RefLinker implements AstScanConsumer {

        private ParseUnit parseUnit;
        private boolean firstPass = true;
        private List<AstLoadError> errors;
        private Collection<ExpressionChain> unresolvedRefs;

        public RefLinker(ParseUnit parseUnit, List<AstLoadError> errors) {
            this.parseUnit = parseUnit;
            this.errors = errors;
        }

        public void setFirstPass(boolean firstPass) {
            this.firstPass = firstPass;
        }

        @Override
        public Predicate<ModelElement> getFilter() {
            return testNode -> {
                boolean hasUnresolvedRefs = false;
                if (testNode.hasReferences()) {
                    unresolvedRefs = getUnresolvedRefs(testNode);
                    hasUnresolvedRefs = !unresolvedRefs.isEmpty();
                }
                return hasUnresolvedRefs;
            };
        }

        private Collection<ExpressionChain> getUnresolvedRefs(ModelElement testNode) {
            java.util.Set<ExpressionChain> references = testNode.getExpressionChains();
            return (Collection<ExpressionChain>) CollectionUtils.select(
                references, obj -> {
                    ExpressionChain exprChain = (ExpressionChain) obj;
                    return exprChain.hasRef()
                        && (this.firstPass ?
                        exprChain.getTargetMetaType() == MetaType.TYPE
                        : exprChain.getTargetMetaType() != MetaType.TYPE)
                        && exprChain.isAtInitState()
                        && !exprChain.isImportRef();
                }
            );
        }

        @Override
        public boolean upon(ModelElement astNode) {
            if (errors == null)
                errors = new LinkedList<>();
//            else
//                errors.clear();

            // NOTE: using unresolvedRefs from the predicate test function
            for (ExpressionChain reference : unresolvedRefs) {
                log.debug("resolving reference [" + reference + "]");
//                if (reference.getTargetMetaType() == MetaType.TYPE && reference.isSinglePart()) {
//
//                    checkImports(reference);
//
//                    if (reference.isResolved()) {
//                        reference.setImportMatch(true);
//                        log.debug("resolved [" + reference + "] as imported type");
//                    }
//                }
//                else {
                resolveAstPath(parseUnit, reference, errors);
//                    if (reference.isResolved())
//                        log.debug("resolved ["+reference+"] as full path usage");
//                }
                //
//                if (reference.isAtInitState()) {
//                    errors.add(new AstLoadError(AstLoadError.Type.LINK, reference.getSourceInfo(),
//                                                "can not resolve symbol '" + reference + "'"));
//                }
            }
            // Null out instance-level field reused across AST nodes
            this.unresolvedRefs = null;
            return true;
        }

        private void checkImports(ExpressionChain reference) {
            checkImplicitImports(reference.getFirstPart());
            //
            if (reference.isAtInitState()) {
                checkUnitImports(reference.getFirstPart());
            }
        }

        private void checkUnitImports(LinkSource exprLink) {
            TypeDefn importedTypeMatch =
                (TypeDefn) CollectionUtils.find(
                    parseUnit.getAllImportedTypes(),
                    object -> ((TypeDefn) object).getNamePart().equals(exprLink.getNameRef().getRefAsNameRef())
                );
            AstUtils.checkSetResolve(exprLink, (NamedElement) importedTypeMatch, null);
        }

        private void checkImplicitImports(LinkSource expLink) {
            TypeDefn importedTypeMatch = (TypeDefn) CollectionUtils.find(
                implicitImportTypes,
                object -> ((TypeDefn) object).getNamePart().equals(
                    expLink.getNameRef().getRefAsNameRef().getResolvedMetaObj())
            );
            AstUtils.checkSetResolve(expLink, (NamedElement) importedTypeMatch, null);
        }

        @Override
        public void after(ModelElement astNode) {

        }

        public List<AstLoadError> getErrors() {
            return errors;
        }
    }

    private class AstRuler {

        /** Sets derived AST info for types */
        public void setDerivedAstInfo() {

            log.info("setting inferred AST elements");

            setTypeKeyComprators();

            inferAssociationKinds();
        }

        private void inferAssociationKinds() {
            List<Namespace> nsChain = getNsRegistry().getNsChain();
            nsChain.forEach((namespace) -> {
                Set<AssociationDefn> unsetAssocKinds = queryUnsetAssocKinds(namespace);
                unsetAssocKinds.forEach(this::inferAssociationKind);
            });
        }

        private Set<AssociationDefn> queryUnsetAssocKinds(Namespace namespace) {
            return AstUtils.queryAst(namespace.getRootDir(),
                                     new QueryAstConsumer<>(
                                         AssociationDefn.class,
                                         assoc -> assoc.getAssociationKind() == null
                                     ));
        }

        void inferAssociationKind(AssociationDefn assocDefn) {
            if (!assocDefn.getToEnd().getEndTargetType().hasPrimaryKey()) {
                assocDefn.setAssociationKind(AssociationKind.DEPENDENT_TYPE);
            }
            else {
                assocDefn.setAssociationKind(AssociationKind.INDEPENDENT_TYPE);
            }
            log.trace("inferred assoc kind {} for {}", assocDefn.getAssociationKind(), assocDefn);
        }
    }

}
