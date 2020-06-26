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
import org.jkcsoft.space.lang.ast.*;
import org.jkcsoft.space.lang.ast.sji.SjiFunctionDefnImpl;
import org.jkcsoft.space.lang.ast.sji.SjiService;
import org.jkcsoft.space.lang.instance.*;
import org.jkcsoft.space.lang.instance.sji.SjiTuple;
import org.jkcsoft.space.lang.loader.AstFileLoadErrorSet;
import org.jkcsoft.space.lang.loader.AstLoadError;
import org.jkcsoft.space.lang.loader.AstLoader;
import org.jkcsoft.space.lang.loader.DirLoadResults;
import org.jkcsoft.space.lang.metameta.MetaType;
import org.jkcsoft.space.lang.runtime.jnative.math.Math;
import org.jkcsoft.space.lang.runtime.jnative.opsys.JOpSys;
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
public class Executor extends ExprProcessor implements ExeContext, InternalExeContext, ApiExeContext {

    public static final SequenceTypeDefn CHAR_SEQ_TYPE_DEF = NumPrimitiveTypeDefn.CHAR.getSequenceOfType();

    private static final Logger log = LoggerFactory.getLogger(Executor.class);

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

    private Stack<FunctionCallContext> callStack = new Stack<>();
    private Map<String, ExprProcessor> exprProcessors = null;
    private Set<TypeDefn> implicitImportTypes;

    private Executor(ExeSettings exeSettings) {
        this.exeSettings = exeSettings;
        this.nsRegistry = NSRegistry.getInstance();
        this.astFactory = AstFactory.getInstance();
        this.defaultSpace = new InMemorySpaceImpl(null, null);
        initRuntime();
        preLoadSpecialNativeTypes();
        loadSpecialOperators();
    }

    private void initRuntime() {
        this.sjiService = new SjiService(this);
        this.sjiService.registerPackageBinding("org.jkcsoft.space.lang.runtime.jnative", "space");
        loadAstLoader("org.jkcsoft.space.antlr.loaders.G2AntlrParser");
    }

    @Override
    public void trackInstanceObject(SpaceObject spaceObject) {
        defaultSpace.trackInstanceObject(spaceObject);
    }

    @Override
    public SpaceObject dereference(SpaceOid referenceOid) throws SpaceX {
        return defaultSpace.dereference(referenceOid);
    }

    @Override
    public AstFactory getAstFactory() {
        return astFactory;
    }

    @Override
    public ObjectFactory getObjFactory() {
        return ObjectFactory.getInstance();
    }

    @Override
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
        List<Declaration> datumDecls = tupleSet.getContainedObjectType().getDatumDecls();
        datumDecls.forEach(
            datumDecl -> sb.append("\"" + datumDecl.getName() + "\" ")
        );
        sb.append(EOL);
        tupleSet.forEach( (oRefHolder) -> {
              SpaceObject targetObject =
                  dereference(((FreeReferenceHolder<SpaceOid>) oRefHolder).getValue().getJavaValue());
              appendString(sb, targetObject, datumDecls);
              sb.append(EOL);
        }
        );
        return sb.toString();
    }

    private void appendString(StringBuilder sb, SpaceObject spaceObject, List<Declaration> datumDecls) {
        datumDecls.forEach(
            declaration -> {
                if (spaceObject instanceof SjiTuple) {
                    ValueHolder sjiValueHolder = ((SjiTuple) spaceObject).get(declaration);
                    Value spaceValue = sjiValueHolder.getValue();
                    sb.append(spaceValue.getJavaValue().toString() + "\t");
                }
            }
        );
    }

    private void appendString(StringBuilder sb, JavaReference javaReference) {
        sb.append("(Java Object as tuple)" + EOL);
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

    public void run() {
        try {
//            File exeFile = FileUtils.getFile(exeSettings.getExeMain());
            RunResults runResults = new RunResults();

            // Load Space libs and source
            List<File> srcRootDirs = exeSettings.getSpaceDirs();
//            if (!exeFile.exists()) {
//                throw new SpaceX("Input file [" + exeFile + "] does not exist.");
//            }

            // 1. Parse all source and load ASTs into memory
            for (File srcRootDir : srcRootDirs) {
                //
                DirLoadResults dirLoadResults = loadSrcRootDir(srcRootDir, nsRegistry.getUserNs());
                log.info("loaded source root [" + srcRootDir + "]");
                //
                runResults.addSrcDirLoadResult(dirLoadResults);
            }

            resolveLangDir();

            // 2. Determine all needed Java classes and load corresponding Space wrappers
            wrapJavaImports(runResults);

            //
            java.util.Set<ParseUnit> newParseUnits =
                AstUtils.queryAst(nsRegistry.getUserNs().getRootDir(), new QueryAstConsumer(ParseUnit.class));

            // 3. Link all refs within the AST.
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
                    unitLoadErrors.forEach(err -> runResults.addFileError(err));
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
                // Execute the specified type 'main' function
                exec(exeSettings.getExeMain());
            }
        } catch (SpaceX spex) {
//            PrintStream ps = System.err;
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
    }

    private void wrapJavaImports(RunResults runResults) {
        Set<ImportExpr> javaImports =
            AstUtils.queryAst(nsRegistry.getUserNs().getRootDir(),
                              new QueryAstConsumer<>(
                                  ImportExpr.class,
                                  elem -> elem instanceof ImportExpr && AstUtils.isJavaNs((ImportExpr) elem)
                              )
            );
        for (ImportExpr javaImport : javaImports) {
            FullTypeRefImpl targetJavaTypeRef = javaImport.getTypeRefExpr();
            try {
                // the following call to getDeepLoad() ensures the Space wrapper for the
                // Java class is loaded and ready for lookup for subsequent linking
                getSjiService().getSjiTypeProxyDeepLoad(targetJavaTypeRef.getFullUrlSpec(), null);
            } catch (ClassNotFoundException e) {
                // NOTE Java class might have been loaded already under the specified full path
                AstUtils.resolveAstRef(AstUtils.findParentParseUnit(targetJavaTypeRef), targetJavaTypeRef);
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
        Collection nonNullKeyFiles = CollectionUtils.select(keyFiles, keyFile -> keyFile != null);
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
            getSjiService().getSjiTypeProxyDeepLoad(jNativeType, null);
        }

        FullTypeRefImpl opSysRef = FullTypeRefImpl.newFullTypeRef(
            nsRegistry.getJavaNs().getName()
                + ":" + getSjiService().getFQSpaceName(JOpSys.class.getName())
        );
        AstUtils.resolveAstRef(null, opSysRef);
        if (opSysRef.isResolvedValid())
            op_sys_type_def = (TypeDefn) opSysRef.getResolvedType();
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
            existingParentDir.addParseUnit(newChildParseUnit);
        }
        //
        for (Directory childOfNewDir : newDir.getChildDirectories()) {
            Directory existingSubDir = AstUtils.getSubDirByName(existingParentDir, childOfNewDir.getName());
            if (existingSubDir != null) {
                mergeNewChildren(existingSubDir, childOfNewDir);
            }
            else {
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
            if (chain.getTargetMetaType() != null) {
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
        AstUtils.resolveAstRef(parseUnit, reference);
        if (!reference.isResolved()) {
            AstLoadError error = new AstLoadError(AstLoadError.Type.LINK, reference.getSourceInfo(),
                                                  "could resolve symbol '" + getFirstUnresolved(reference) + "'");
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
            for (NameRefOrHolder link : refChain.getRestLinks()) {
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
        FullTypeRefImpl exeTypeRef = getAstFactory().newTypeRef(sourceInfo, null, null);
        SimpleNameRefExpr userNsRefPart = getAstFactory().newNameRefExpr(sourceInfo, nsRegistry.getUserNs().getName());
        exeTypeRef.setNsRefPart(userNsRefPart);
        AstUtils.addNewMetaRefParts(exeTypeRef, sourceInfo, pathNodes);
        AstUtils.resolveAstRef(null, exeTypeRef);
        TypeDefnImpl bootTypeDefn = (TypeDefnImpl) exeTypeRef.getResolvedType();
//        SpaceTypeDefn bootTypeDefn = progSpaceDir.getFirstSpaceDefn();
        Tuple mainTypeTuple = newTupleImpl(bootTypeDefn);
        EvalContext evalContext = new EvalContext();
        SpaceFunctionDefn spMainActionDefn = bootTypeDefn.getFunction("main");

        ProgSourceInfo progSourceInfo = new ProgSourceInfo();
        FunctionCallExpr bootMainCallExpr = getAstFactory().newFunctionCallExpr(progSourceInfo);
        SimpleNameRefExpr<FunctionDefn> mainFuncRef =
            getAstFactory().newNameRefExpr(getAstFactory().newNamePartExpr(sourceInfo, null, "main"));
        bootMainCallExpr.setFunctionRef(mainFuncRef);
        bootMainCallExpr.getFunctionRef().setResolvedMetaObj(spMainActionDefn);
        bootMainCallExpr.getFunctionRef().setTypeCheckState(TypeCheckState.VALID);
        LinkedList<ValueExpr> argExprList = new LinkedList<>();
        argExprList.add(getAstFactory().newCharSeqLiteralExpr(progSourceInfo, "(put CLI here)"));
        bootMainCallExpr.setArgValueExpr(
            getAstFactory().newNewObjectExpr(progSourceInfo, null,
                                             getAstFactory().newTupleExpr(progSourceInfo).setValueExprs(argExprList)
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
        eval(evalContext, bootTypeDefn.getInitBlock());
        // call the 'main' function a dummy function call expr
        eval(evalContext, bootMainCallExpr);
        callStack.pop();
        log.debug("exiting Space program execution");
        return null;
    }

    private Declaration newAnonDecl(TypeDefn typeDefn) {
        Declaration decl = null;
        TypeDefn rootType = getRootType(typeDefn);
        FullTypeRefImpl typeRef = getAstFactory().newTypeRef(new ProgSourceInfo(), typeDefn);
        if (rootType instanceof NumPrimitiveTypeDefn) {
            decl = getAstFactory().newVariableDecl(new ProgSourceInfo(), null, typeRef);
        }
        else if (rootType instanceof TypeDefnImpl) {
            decl = getAstFactory().newAssociationDecl(new ProgSourceInfo(), null, typeRef);
        }

        return decl;
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
        else if (expression instanceof SequenceLiteralExpr) {
            valueHolder = eval(spcContext, (SequenceLiteralExpr) expression);
        }
        else if (expression instanceof ThisArgExpr) {
            valueHolder = eval(spcContext, (ThisArgExpr) expression);
        }
        else if (expression instanceof ThisTupleExpr) {
            valueHolder = eval(spcContext, (ThisTupleExpr) expression);
        }
        else if (expression instanceof MetaRefPath) {
            valueHolder = eval(spcContext, (MetaRefPath) expression);
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
            valueHolder = eval(spcContext, ((ExpressionChain) expression).extractValueExprChain());
        }
        else
            throw new SpaceX("don't know how to evaluate " + expression);

        log.debug("eval -> " + valueHolder);
        return valueHolder;
    }

    private ValueHolder eval(EvalContext evalContext, ThisTupleExpr thisTupleExpr) {
        log.debug("exec: " + thisTupleExpr);
        Tuple ctxObject = evalContext.peekStack().getCtxObject();
        return getObjFactory().newReferenceByOidHolder(ctxObject, null, ctxObject.getOid());
    }

    private ValueHolder eval(EvalContext evalContext, ThisArgExpr thisArgExpr) {
        log.debug("exec: " + thisArgExpr);
        TupleImpl argTuple = evalContext.peekStack().getArgTuple();
        return getObjFactory().newReferenceByOidHolder(argTuple, null, argTuple.getOid());
    }

    /**
     * TODO I think we'll eventually want all statements to get put on an Action Queue
     * or Transaction Queue or such. In effect, each statement becomes a first-class
     * action / job.
     */
    private void eval(EvalContext evalContext, StatementBlock statementBlock) {
        log.debug("eval: " + statementBlock);
        FunctionCallContext functionCallContext = evalContext.peekStack();
        Tuple blockTuple = statementBlock instanceof TypeDefn ?
            functionCallContext.getCtxObject() : newTupleImpl(statementBlock);
        functionCallContext.addBlockContext(getObjFactory().newBlockContext(statementBlock, blockTuple));
        List<Statement> statementSequence = statementBlock.getStatementSequence();
        for (Statement statement : statementSequence) {
            eval(evalContext, statement);
            if (evalContext.peekStack().isPendingReturn())
                break;
        }
        functionCallContext.popBlock();
        return;
    }

    private void eval(EvalContext evalContext, Statement statement) {
        FunctionCallContext functionCallContext = evalContext.peekStack();
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
            autoCastAssign(evalContext, functionCallContext.getReturnValueHolder(), retVal);
            functionCallContext.setPendingReturn(true);
        }
        return;
    }

    private ValueHolder eval(EvalContext evalContext, ValueExprChain exprChain) {
        for (ValueExpr valueExpr : exprChain.getChain()) {
            eval(evalContext, valueExpr);
        }
        return getCallStack().peek().getReturnValueHolder();
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

        FunctionDefn functionDefn = (FunctionDefn) functionCallExpr.getFunctionRef().getResolvedMetaObj();
        TypeDefn argTypeDefn = functionDefn.getArgSpaceTypeDefn();
        TupleImpl argTupleValue = (TupleImpl) eval(evalContext, functionCallExpr.getArgValueExpr());
        ValueHolder retValHolder = functionDefn.isReturnVoid() ? newVoidHolder()
            : newEmptyVarHolder(null, newAnonDecl(functionDefn.getReturnType()));
        FunctionCallContext functionCallContext =
            getObjFactory().newFunctionCall(evalContext.peekStack().getCtxObject(), functionCallExpr, argTupleValue,
                                                          retValHolder);
        // push call onto stack
        callStack.push(functionCallContext);
        log.debug("pushed call stack. size [" + callStack.size() + "]");
        try {
            if (functionDefn instanceof SpaceFunctionDefn) {
                SpaceFunctionDefn targetFunctionDefn =
                    (SpaceFunctionDefn) functionDefn;
                eval(evalContext, targetFunctionDefn.getStatementBlock());
            }
            else if (functionDefn instanceof SjiFunctionDefnImpl) {
                evalNative(evalContext, functionCallExpr);
            }
            evalContext.peekStack().getReturnValueHolder().getValue();
        } finally {
            FunctionCallContext popCall = callStack.pop();
            popCall.setPendingReturn(false);
            log.debug("popped call stack. size [" + callStack.size() + "]");
        }
        log.debug("eval -> " + retValHolder);
        return retValHolder;
    }

    private DetachedHolder<ReferenceByOid, SpaceOid> eval(EvalContext evalContext, NewTupleExpr newTupleExpr)
    {
        log.debug("eval: new tuple => " + newTupleExpr);
        TupleImpl newTuple = newTupleImpl((TypeDefn) newTupleExpr.getTypeRef().getResolvedType());
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
                argValueHolder.setValue(
                    autoCast(newTuple.getDefn().getDatumDecls().get(idxArg).getType(), argValueHolder)
                );
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
        CastTransforms casters = new CastTransforms();
        FunctionCallContext functionCallContext = evalContext.peekStack();
        try {
            List<ValueHolder> sArgHolders = functionCallContext.getArgTuple().getValueHolders();
            Object jArgs[] = new Object[sArgHolders.size()];
            int idxArg = 0;
            for (ValueHolder spcArgHolder : sArgHolders) {
                Object jArg = "?";
                if (spcArgHolder instanceof DeclaredReferenceHolder) {
                    DeclaredReferenceHolder refHolder = (DeclaredReferenceHolder) spcArgHolder;
                    jArg = refHolder.getValue().getJavaValue();
                }
                else {
                    jArg = spcArgHolder.toString();
                }
                // TODO: Remove hardcodings and improve casting generalization
                if (jArg instanceof CharacterSequence)
                    jArg = casters.charSequenceToString((CharacterSequence) jArg);
                jArgs[idxArg] = jArg;
                idxArg++;
            }
            SjiFunctionDefnImpl funcDef =
                (SjiFunctionDefnImpl) functionCallExpr.getFunctionRef().getResolvedMetaObj();
            Object jObjectDummy =
                funcDef.getjMethod().getDeclaringClass().newInstance();
            Object jValue = funcDef.getjMethod().invoke(jObjectDummy, jArgs);

            // TODO Do we need special handling / casting for Java Strings?

//            if (jValue instanceof String) {
            SjiTuple sjiTupleValue = getSjiService().getOrCreateSjiObjectProxy(jValue);
            valueHolder = getObjFactory().newReferenceByOidHolder(null, null, sjiTupleValue.getOid());
//                valueHolder = casters.stringToCharSeq(evalContext, ((String) jValue));
//            }
        } catch (Exception e) {
            log.error("error invoking native method", e);
            throw new SpaceX(newRuntimeError("error invoking native method " + e.getMessage()));
        }
        if (valueHolder != null) {
            autoCastAssign(evalContext, functionCallContext.getReturnValueHolder(), valueHolder);
        }
        functionCallContext.setPendingReturn(true);
        log.debug("eval -> " + valueHolder);
        return valueHolder;
    }

    /*
     Find values in all possible locations:
     1. current block
     2. parent block(s)
     3. call args
     4. context object (tuple)
     5. static objects
    */
    private ValueHolder eval(EvalContext evalContext, MetaRefPath metaRef) {
        log.debug("eval: " + metaRef);
        ValueHolder valueHolder = null;
        Named toMember = metaRef.getResolvedMetaObj();
        FunctionCallContext functionCallContext = evalContext.peekStack();
        switch (metaRef.getResolvedDatumScope()) {
            case BLOCK:
                valueHolder = findInBlocksRec(toMember, functionCallContext.getBlockContexts());
                break;
            case ARG:
                valueHolder = functionCallContext.getArgTuple().get((Declaration) toMember);
                break;
            case TYPE_DEFN:
                valueHolder = functionCallContext.getCtxObject().get((Declaration) toMember);
                break;
            case STATIC:
                throw new SpaceX(evalContext.newRuntimeError("don't yet eval static datums"));
//                break;
        }

        if (valueHolder == null)
            throw new SpaceX(evalContext.newRuntimeError("could not resolve reference " + metaRef));

        log.debug("eval -> " + valueHolder);
        return valueHolder;
    }

    private ValueHolder eval(EvalContext evalContext, AssignmentExpr assignmentExpr) {
        log.debug("eval: " + assignmentExpr);
        ValueHolder leftSideHolder = eval(evalContext, assignmentExpr.getLeftSideDatumRef());
        ValueHolder rightSideHolder = eval(evalContext, assignmentExpr.getRightSideValueExpr());
        autoCastAssign(evalContext, leftSideHolder, rightSideHolder);
        log.debug("eval -> " + leftSideHolder);
        return leftSideHolder;
    }

    public ValueHolder eval(EvalContext evalContext, UpdateExpr updateExpr) {
        log.debug("eval: " + updateExpr);
        ValueHolder<ReferenceValue<SpaceOid>, SpaceOid> baseTupleHolder = eval(evalContext, updateExpr.getLeftSideDatumRef());
        Tuple baseTuple = (Tuple) dereference(baseTupleHolder.getValue().getJavaValue());
        // TODO Validate: must be the basis tuple, not a view
        ValueHolder rightSideHolder = eval(evalContext, updateExpr.getRightSideValueExpr());
        Tuple deltaTuple = (Tuple) dereference((SpaceOid) rightSideHolder.getValue().getJavaValue());
        //
        deltaTuple.getValueHolders().forEach(
            inputSlot -> {
                autoCastAssign(evalContext, baseTuple.get(inputSlot.getDeclaration()), inputSlot);
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
                evalContext.peekStack().getCtxObject(), null,
                Integer.parseInt(primitiveLiteralExpr.getValueExpr())
            );
        }
        else if (primitiveLiteralExpr.getTypeDefn() == NumPrimitiveTypeDefn.BOOLEAN) {
            valueHolder = getObjFactory().newBooleanValueHolder(
                evalContext.peekStack().getCtxObject(), null,
                Boolean.parseBoolean(primitiveLiteralExpr.getValueExpr())
            );
        }
        else if (primitiveLiteralExpr.getTypeDefn() == NumPrimitiveTypeDefn.CHAR) {
            valueHolder = getObjFactory().newCharacterValueHolder(
                evalContext.peekStack().getCtxObject(), null,
                primitiveLiteralExpr.getValueExpr().charAt(0)
            );
        }
        else if (primitiveLiteralExpr.getTypeDefn() == NumPrimitiveTypeDefn.REAL) {
            valueHolder = getObjFactory().newRealValueHolder(
                evalContext.peekStack().getCtxObject(), null,
                Double.valueOf(primitiveLiteralExpr.getValueExpr())
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

//    private SpaceObject exec(Space userSpaceContext, SpacePathExpr pathExpr) {
//        SpaceObject value = null;
//
//        return value;
//    }

    private ValueHolder eval(EvalContext evalContext, SequenceLiteralExpr sequenceLiteralExpr) {
        log.debug("eval: " + sequenceLiteralExpr);
        ValueHolder valueHolder = null;
        Value value = null;
        if (sequenceLiteralExpr.getTypeRef().getResolvedMetaObj() == CHAR_SEQ_TYPE_DEF) {
            value = newCharacterSequence(sequenceLiteralExpr.getValueExpr());
//            value = newReference(null, null, csLiteral);
        }
        else {
            throw new SpaceX(
                evalContext.newRuntimeError("Can not yet handle sequence literal of type " + sequenceLiteralExpr));
        }
        log.debug("eval -> " + value);
        return getObjFactory().newDetachedHolder(CHAR_SEQ_TYPE_DEF, value);
    }

    private ValueHolder findInBlocksRec(Named toMember, LinkedList<BlockContext> blocks) {
        ValueHolder assignable = null;
        Iterator<BlockContext> blockContextIterator = blocks.descendingIterator();
        while (assignable == null && blockContextIterator.hasNext()) {
            BlockContext blockContext = blockContextIterator.next();
            assignable = blockContext.getDataTuple().get((Declaration) toMember);
        }
        return assignable;
    }

    private OperEvaluator lookupOperEval(Operators.Operator operator) {
        return operEvalMap.get(operator);
    }

    @Override
    public void autoCastAssign(EvalContext evalContext, ValueHolder leftSideHolder, ValueHolder rightSideHolder) {
        TypeDefn leftSideDefnObj =
            leftSideHolder instanceof VariableValueHolder ?
                ((VariableValueHolder) leftSideHolder).getDeclaration().getType()
                : (leftSideHolder instanceof DeclaredReferenceHolder ? leftSideHolder.getType() : null);

        rightSideHolder.setValue(autoCast(leftSideDefnObj, rightSideHolder));
        SpaceUtils.assignNoCast(evalContext, leftSideHolder, rightSideHolder);
    }

    private Value autoCast(TypeDefn leftSideTypeDefn, ValueHolder rightSideHolder) {
        Value newValue = rightSideHolder.getValue();
        // TODO Add some notion of 'casting' and 'auto-(un)boxing'.
        if (leftSideTypeDefn.getOid().equals(Executor.CHAR_SEQ_TYPE_DEF.getOid())
            && !(rightSideHolder instanceof ReferenceByOid)) {
            if (rightSideHolder instanceof ScalarValue) {
                CharacterSequence characterSequence =
                    newCharacterSequence(((ScalarValue) rightSideHolder).asString());
                newValue = characterSequence.getOid();
                log.debug("cast scalar value to CharSequence");
            }
            else if (rightSideHolder instanceof VariableValueHolder) {
                CharacterSequence characterSequence =
                    newCharacterSequence(
                        ((VariableValueHolder) rightSideHolder).getScalarValue().getJavaValue().toString());
                newValue = characterSequence.getOid();
                log.debug("cast variable (1-D) value to CharSequence");
            }
        }
        else if (leftSideTypeDefn instanceof NumPrimitiveTypeDefn && rightSideHolder instanceof ScalarValue) {
            ScalarValue rsScalarValue = (ScalarValue) rightSideHolder;
            if (leftSideTypeDefn == NumPrimitiveTypeDefn.CARD
                && rsScalarValue.getType() == NumPrimitiveTypeDefn.REAL) {
                newValue =
                    getObjFactory().newCardinalValue(((Double) rsScalarValue.getJavaValue()).intValue());
                log.debug("cast real to card");
            }
        }
        return newValue;
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
            List<Declaration> declList = defn.getDatumDecls();
            for (Declaration datumDecl : declList) {
                tuple.initHolder(newEmptyVarHolder(tuple, datumDecl));
            }
        }

        trackInstanceObject(tuple);
    }

    // ---------------------------- New Space Objects ------------------------

    @Override
    public TupleSetImpl newSet(SetTypeDefn setTypeDefn) {
        TupleSetImpl tupleSet = getObjFactory().newSet(setTypeDefn);
        trackInstanceObject(tupleSet);
        return tupleSet;
    }

    private DeclaredReferenceHolder<SpaceOid> newReferenceHolderByOid(AssociationDefn assocDecl, Tuple tuple,
                                                                      SpaceOid toOid)
    {
        return getObjFactory().newReferenceByOidHolder(tuple, assocDecl, toOid);
    }

    @Override
    public CharacterSequence newCharacterSequence(String stringValue) {
        CharacterSequence newCs = getObjFactory().newCharacterSequence(stringValue);
        trackInstanceObject(newCs);
        return newCs;
    }

    public ValueHolder newEmptyVarHolder(Tuple tuple, Declaration declaration) {
        ValueHolder holder = null;
        ObjectFactory ofact = getObjFactory();
        if (declaration.getType() instanceof PrimitiveTypeDefn) {
            holder = new VariableValueHolder(tuple, ((VariableDecl) declaration));
        }
        else if (declaration instanceof TypeDefnImpl) {
            holder = ofact.newReferenceByOidHolder(tuple, null, (SpaceOid) null);
        }
        else if (declaration instanceof StreamTypeDefn) {
            holder = ofact.newReferenceByOidHolder(tuple, null, (SpaceOid) null);
        }
        else if (declaration instanceof SequenceTypeDefn) {
            holder = ofact.newReferenceByOidHolder(tuple, null, (SpaceOid) null);
        }
        else if (declaration instanceof SetTypeDefn) {
            holder = ofact.newReferenceByOidHolder(tuple, null, (SpaceOid) null);
        }
        return holder;
    }

    public ValueHolder newVoidHolder() {
        ValueHolder holder = getObjFactory().newVoidHolder();
        return holder;
    }

    public RuntimeError newRuntimeError(String msg) {
        return new RuntimeError(getCallStack(), msg);
    }

    public Stack<FunctionCallContext> getCallStack() {
        Stack<FunctionCallContext> copy = new Stack<>();
        copy.addAll(callStack);
        return copy;
    }

    private void writeSpaceStackTrace(StringBuffer sb, Stack<FunctionCallContext> spaceTrace) {
        sb.append(Strings.buildDelList(spaceTrace, STACK_TRACE_LISTER, EOL));
    }

    //--------------------------------------------------------------------------
    //

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
        private Predicate<ModelElement> filter = modelElement -> false;

        public QueryAstConsumer(Class<T> clazz, Predicate<ModelElement> filter) {
            this.clazz = clazz;
            this.filter = filter;
        }

        public QueryAstConsumer(Class<T> clazz) {
            this.clazz = clazz;
            this.filter = modelElement -> this.clazz.isAssignableFrom(modelElement.getClass());
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

    /**
     * Everything needed by an eval( ) method to resolve values in the lexical
     * context chain.
     *
     * @author Jim Coles
     */
    public class EvalContext {

        public FunctionCallContext peekStack() {
            return callStack.peek();
        }

        public RuntimeError newRuntimeError(String msg) {
            return Executor.this.newRuntimeError(msg);
        }

        public CharacterSequence newCharSequence(String jString) {
            return Executor.this.newCharacterSequence(jString);
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
                    object -> ((TypeDefn) object).getName().equals(exprLink.getNameRef().getRefAsNameRef())
                );
            AstUtils.checkSetResolve(exprLink, (NamedElement) importedTypeMatch);
        }

        private void checkImplicitImports(LinkSource expLink) {
            TypeDefn importedTypeMatch = (TypeDefn) CollectionUtils.find(
                implicitImportTypes,
                object -> ((TypeDefn) object).getName().equals(
                    expLink.getNameRef().getRefAsNameRef().getResolvedMetaObj())
            );
            AstUtils.checkSetResolve(expLink, (NamedElement) importedTypeMatch);
        }

        @Override
        public void after(ModelElement astNode) {

        }

        public List<AstLoadError> getErrors() {
            return errors;
        }
    }

}
