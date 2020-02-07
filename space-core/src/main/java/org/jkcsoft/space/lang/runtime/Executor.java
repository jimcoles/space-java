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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jkcsoft.java.util.Lister;
import org.jkcsoft.java.util.Strings;
import org.jkcsoft.space.SpaceHome;
import org.jkcsoft.space.lang.ast.*;
import org.jkcsoft.space.lang.ast.sji.SjiFunctionDefnImpl;
import org.jkcsoft.space.lang.ast.sji.SjiTypeDefn;
import org.jkcsoft.space.lang.instance.*;
import org.jkcsoft.space.lang.loader.AstFileLoadErrorSet;
import org.jkcsoft.space.lang.loader.AstLoadError;
import org.jkcsoft.space.lang.loader.AstLoader;
import org.jkcsoft.space.lang.loader.DirLoadResults;
import org.jkcsoft.space.lang.metameta.MetaType;
import org.jkcsoft.space.lang.runtime.jnative.math.Math;
import org.jkcsoft.space.lang.runtime.jnative.opsys.JOpSys;
import org.jkcsoft.space.lang.runtime.typecasts.CastTransforms;

import java.io.File;
import java.io.PrintStream;
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
public class Executor extends ExprProcessor implements ExeContext {

    private static final Logger log = LoggerFactory.getLogger(Executor.class);

    public static final StackTraceLister STACK_TRACE_LISTER = new StackTraceLister();
    private static final SequenceTypeDefn CHAR_SEQ_TYPE_DEF = NumPrimitiveTypeDefn.CHAR.getSequenceOfType();

//    private static Executor instance;
//
//    public static Executor getInstance() {
//        if (instance == null)
//            instance = new Executor();
//        return instance;
//    }

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
    public static ComplexType MATH_TYPE_DEF;
    public static ComplexType space_opers_type_def;
    public static ComplexType op_sys_type_def;
    private static Map<OperEnum, OperEvaluator> operEvalMap = new TreeMap<>();

    static {
        operEvalMap.put(OperEnum.ADD, Math::addNum);
        operEvalMap.put(OperEnum.SUB, Math::subNum);
        operEvalMap.put(OperEnum.MULT, Math::multNum);
        operEvalMap.put(OperEnum.DIV, Math::divNum);
        //
        operEvalMap.put(OperEnum.COND_AND, Math::condAnd);
        //
        operEvalMap.put(OperEnum.EQ, Math::equal);
        operEvalMap.put(OperEnum.LT, Math::lt);
    }

    //--------------------------------------------------------------------------
    //

    private static AstFactory getAstFactory() {
        return AstFactory.getInstance();
    }

    private static ObjectFactory getObjFactory() {
        return ObjectFactory.getInstance();
    }

    private NSRegistry nsRegistry = SpaceHome.getNsRegistry();

    private ObjectTable objectTable = new ObjectTable();
    private Stack<FunctionCallContext> callStack = new Stack<>();
    private Map<String, ExprProcessor> exprProcessors = null;
    private ExeSettings exeSettings;
    private AstLoader astLoader;
    private Directory spaceLangDir;
    private Set<DatumType> implicitImportTypes;

    public Executor(ExeSettings exeSettings) {
        this.exeSettings = exeSettings;
        initRuntime();
        preLoadSpecialNativeTypes();
        loadSpecialOperators();
    }

    private void initRuntime() {
        loadAstLoader("org.jkcsoft.space.antlr.loaders.G2AntlrParser");
        SpaceHome.getSjiService().getSjiBindings().registerPToPBinding("org.jkcsoft.space.lang.runtime.jnative", "space");
//        NSRegistry.getInstance();
    }

    private void loadAstLoader(String parserImplClassName) {
        try {
            Class<?> aClass = Class.forName(parserImplClassName);
            log.debug(String.format("Found loader provider class [%s]", parserImplClassName));
            astLoader = (AstLoader) aClass.newInstance();
            log.debug(String.format("Found source loader [%s]", astLoader.getName()));
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
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
                log.info("loaded source root ["+srcRootDir+"]");
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
                        String redMsg = "Found [" + unitLoadErrors.size() + "] linker errors in [" + parseUnit +  "]";
                        log.warn(redMsg + ". See error stream.");
                        System.err.println(redMsg + ":");
                        System.err.println(Strings.buildNewlineList(unitLoadErrors));
                    }
                    unitLoadErrors.forEach(err ->  runResults.addFileError(err));
                }
            }
            catch (SpaceX ex) {
                throw ex;
            }
            catch (Exception ex) {
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
        }
        catch (SpaceX spex) {
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
        }
        finally {
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
                SpaceHome.getSjiService().getDeepLoadSpaceWrapper(targetJavaTypeRef.getFullUrlSpec(), null);
            }
            catch (ClassNotFoundException e) {
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

        Class[] jNativeTypes = new Class[] {
            JOpSys.class
        };

        for (Class jNativeType : jNativeTypes) {
            SpaceHome.getSjiService().getDeepLoadSpaceWrapper(jNativeType, null);
        }

        FullTypeRefImpl opSysRef = FullTypeRefImpl.newFullTypeRef(
            nsRegistry.getJavaNs().getName()
                + ":" + SpaceHome.getSjiService().getSjiBindings().applyToJavaClassname(JOpSys.class.getName())
        );
        AstUtils.resolveAstRef(null, opSysRef);
        if (opSysRef.isResolvedValid())
            op_sys_type_def = (ComplexType) opSysRef.getResolvedType();
        else
            log.error("could not resolve ["+opSysRef+"]");

        //        String jClassSeq = System.getProperty("java.class.path");
//        String[] jClassPaths = jClassSeq.split(File.pathSeparator);
//        for (String jClassPath : jClassPaths) {
////            loadJavaClassArchive(FileUtils.getFile(jClassPath));
//        }


    }

    private void loadSpecialOperators() {
//        op_sys_type_def =
//            (ComplexType) AstUtils.resolveAstPath(nsRegistry.getRootDirs(), TypeRef.newTypeRef(OpSys.class.getTypeName()));
//        MATH_TYPE_DEF = loadNativeType(Math.class, AstUtils.getLangRoot(dirChain));
//        space_opers_type_def = loadNativeType(SpaceOpers.class, AstUtils.getLangRoot(dirChain));
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
                            (DatumType) importTypeRefExpr.getTypeRefExpr().getResolvedMetaObj());
//                    else
//                        log.warn("import reference not fully resolved ["+importTypeRefExpr+"]");
                }
                else {
                    Set<DatumType> refElems = AstUtils.getSiblingTypes(importTypeRefExpr.getTypeRefExpr());
                    parseUnit.getAllImportedTypes().addAll(refElems);

                    // add all types local to this parse unit's directory
                    parseUnit.getAllImportedTypes().addAll(
                        AstUtils.queryAst(parseUnit.getParent(), new QueryAstConsumer<>(SpaceTypeDefn.class))
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
        if ( refChain.getFirstPart().hasNameRef()
             && !refChain.getFirstPart().getNameRef().getRefAsNameRef().isResolved() )
        {
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
        SpaceTypeDefn bootTypeDefn = (SpaceTypeDefn) exeTypeRef.getResolvedType();
//        SpaceTypeDefn bootTypeDefn = progSpaceDir.getFirstSpaceDefn();
        Tuple mainTypeTuple = newTupleImpl(bootTypeDefn);
        EvalContext evalContext = new EvalContext();
        SpaceFunctionDefn spMainActionDefn = bootTypeDefn.getBody().getFunction("main");

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
        eval(evalContext, bootTypeDefn.getBody());
        // call the 'main' function a dummy function call expr
        eval(evalContext, bootMainCallExpr);
        callStack.pop();
        log.debug("exiting Space program execution");
        return null;
    }

    //

    private Declaration newAnonDecl(DatumType datumType) {
        Declaration decl = null;
        DatumType rootType = getRootType(datumType);
        FullTypeRefImpl typeRef = getAstFactory().newTypeRef(new ProgSourceInfo(), datumType);
        if (rootType instanceof NumPrimitiveTypeDefn) {
            decl = getAstFactory().newVariableDecl(new ProgSourceInfo(), null, typeRef);
        }
        else if (rootType instanceof SpaceTypeDefn) {
            decl = getAstFactory().newAssociationDecl(new ProgSourceInfo(), null, typeRef);
        }

        return decl;
    }

    private static DatumType getRootType(DatumType datumType) {
        DatumType baseType = datumType;
        if (datumType instanceof AbstractCollectionTypeDefn) {
            baseType = getRootType(((AbstractCollectionTypeDefn) datumType).getContainedElementType());
        }
        return baseType;
    }

    /**
     * The dispatcher eval method.
     */
    private Value eval(EvalContext spcContext, ValueExpr expression) {
        log.debug("eval: (deleg) " + expression);
        Value value = null;
        if (expression instanceof NewTupleExpr) {
            value = eval(spcContext, (NewTupleExpr) expression);
        }
        else if (expression instanceof NewSetExpr) {
            value = eval(spcContext, (NewSetExpr) expression);
        }
        else if (expression instanceof OperatorExpr) {
            value = eval(spcContext, (OperatorExpr) expression);
        }
        else if (expression instanceof PrimitiveLiteralExpr) {
            value = eval(spcContext, (PrimitiveLiteralExpr) expression);
        }
        else if (expression instanceof SequenceLiteralExpr) {
            value = eval(spcContext, (SequenceLiteralExpr) expression);
        }
        else if (expression instanceof ThisArgExpr) {
            value = eval(spcContext, (ThisArgExpr) expression);
        }
        else if (expression instanceof ThisTupleExpr) {
            value = eval(spcContext, (ThisTupleExpr) expression);
        }
        else if (expression instanceof MetaRefPath) {
            value = eval(spcContext, (MetaRefPath) expression);
        }
        else if (expression instanceof AssignmentExpr) {
            value = eval(spcContext, (AssignmentExpr) expression);
        }
        else if (expression instanceof FunctionCallExpr) {
            value = eval(spcContext, (FunctionCallExpr) expression);
        }
        else if (expression instanceof ExpressionChain) {
            value = eval(spcContext, ((ExpressionChain) expression).extractValueExprChain());
        }
        else
            throw new SpaceX("don't know how to evaluate " + expression);

        log.debug("eval -> " + value);
        return value;
    }

    private Value eval(EvalContext evalContext, ThisTupleExpr thisTupleExpr) {
        log.debug("exec: " + thisTupleExpr);
        return evalContext.peekStack().getCtxObject();
    }

    private Value eval(EvalContext evalContext, ThisArgExpr thisArgExpr) {
        log.debug("exec: " + thisArgExpr);
        return evalContext.peekStack().getArgTuple();
    }

    /**
     * TODO I think we'll eventually want all statements to get put on an Action Queue
     * or Transaction Queue or such. In effect, each statement becomes a first-class
     * action / job.
     */
    private void eval(EvalContext evalContext, StatementBlock statementBlock) {
        log.debug("eval: " + statementBlock);
        FunctionCallContext functionCallContext = evalContext.peekStack();
        Tuple blockTuple = statementBlock instanceof SpaceTypeDefnBody ?
            functionCallContext.getCtxObject() : newTupleImpl(statementBlock);
        functionCallContext.addBlockContext(getObjFactory().newBlockContext(statementBlock, blockTuple));
        List<Statement> statementSequence = statementBlock.getStatementSequence();
        for (Statement statement : statementSequence) {
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
                // Klunky (?) handling of space 'return' statement
                if (functionCallContext.isPendingReturn())
                    return;
            }
            else if (statement instanceof ReturnExpr) {
                Value retVal = eval(evalContext, ((ReturnExpr) statement).getValueExpr());
                autoCastAssign(evalContext, functionCallContext.getReturnValueHolder(), retVal);
                functionCallContext.setPendingReturn(true);
            }
        }
        functionCallContext.popBlock();
        return;
    }

    private Value eval(EvalContext evalContext, ValueExprChain exprChain) {
        for (ValueExpr valueExpr : exprChain.getChain()) {
            eval(evalContext, valueExpr);
        }
        return getCallStack().peek().getReturnValueHolder().getValue();
    }

//    private Tuple eval(EvalContext evalContext, NewTupleExpr newTupleExpr) {
//        log.debug("eval: " + newTupleExpr);
//        Tuple value = eval(evalContext, (ComplexType) newTupleExpr.getTypeRef().getResolvedMetaObj(),
//                           newTupleExpr.getTupleExpr());
//        return value;
//    }

    private Value eval(EvalContext evalContext, NewSetExpr newSetExpr) {
        log.debug("eval: " + newSetExpr);
        SetSpace value = newSet(null, null);
        newSetExpr.getNewTupleExprs().forEach(
            newTupleExpr -> value.addTuple(eval(evalContext, newTupleExpr))
        );
        return value;
    }

    private Value eval(EvalContext evalContext, OperatorExpr operatorExpr) {
        log.debug("eval: " + operatorExpr);
        Value value = null;
        OperEvaluator oper = lookupOperEval(operatorExpr.getOper());
        if (oper == null)
            throw new SpaceX(evalContext.newRuntimeError("no operator evaluator for " + operatorExpr.getOper()));
        Value[] argValues = new Value[operatorExpr.getArgs().size()];
        for (int idxArg = 0; idxArg < operatorExpr.getArgs().size(); idxArg++) {
            argValues[idxArg] = eval(evalContext, operatorExpr.getArgs().get(idxArg));
        }
        value = oper.eval(argValues);
        return value;
    }

    private Value eval(EvalContext evalContext, FunctionCallExpr functionCallExpr) {
        log.debug("eval: " + functionCallExpr);
        Value value = null;

        FunctionDefn functionDefn = (FunctionDefn) functionCallExpr.getFunctionRef().getResolvedMetaObj();
        ComplexType argTypeDefn = functionDefn.getArgSpaceTypeDefn();
        TupleImpl argTupleValue = (TupleImpl) eval(evalContext, functionCallExpr.getArgValueExpr());
        ValueHolder retValHolder = functionDefn.isReturnVoid() ? newVoidHolder()
            : newHolder(null, newAnonDecl(functionDefn.getReturnType()));
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
            value = evalContext.peekStack().getReturnValueHolder().getValue();
        } finally {
            FunctionCallContext popCall = callStack.pop();
            popCall.setPendingReturn(false);
            log.debug("popped call stack. size [" + callStack.size() + "]");
        }
        log.debug("eval -> " + value);
        return value;
    }

    private Tuple eval(EvalContext evalContext, NewTupleExpr newTupleExpr)
    {
        log.debug("eval: new tuple => " + newTupleExpr);
        TupleImpl tuple = newTupleImpl((ComplexType) newTupleExpr.getTypeRef().getResolvedType());
        List<ValueExpr> valueExprs = newTupleExpr != null ? newTupleExpr.getTupleValueList().getValueExprs() : null;
        // validation
        int numValueExprs = valueExprs == null ? 0 : valueExprs.size();
        if (numValueExprs != tuple.getSize())
            throw new SpaceX(evalContext.newRuntimeError(
                "tuple value list does not match type definition. expected " + tuple.getSize()
                    + " received " + numValueExprs + ""));

        // Tuple value assignments may be by name or by order (like a SQL update statement), but not both.
        if (valueExprs != null) {
            int idxArg = 0;
            for (ValueExpr argumentExpr : valueExprs) {
                Value argValue = eval(evalContext, argumentExpr);
                argValue =
                    autoCast(((ComplexType) tuple.getDefn()).getDatumDeclList().get(idxArg).getType(), argValue);
                ValueHolder leftSideHolder = tuple.get(tuple.getDeclAt(idxArg));
                SpaceUtils.assignNoCast(evalContext, leftSideHolder, argValue);
                idxArg++;
            }
        }
        return tuple;
    }

    /**
     * Invokes a Java native method all via Java reflection.  Native actions
     * do not have nested actions, at least none that are executed or controlled
     * by this executor.
     */
    private Value evalNative(EvalContext evalContext, FunctionCallExpr functionCallExpr) {
        log.debug("eval: " + functionCallExpr);
        Value value = null;
        CastTransforms casters = new CastTransforms();
        FunctionCallContext functionCallContext = evalContext.peekStack();
        try {
            List<ValueHolder> sArgHolders = functionCallContext.getArgTuple().getValueHolders();
            Object jArgs[] = new Object[sArgHolders.size()];
            int idxArg = 0;
            for (ValueHolder spcArgHolder : sArgHolders) {
                Object jArg = "?";
                if (spcArgHolder instanceof Reference)
                    //
                    jArg = dereference(((Reference) spcArgHolder).getToOid()).toString();
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
            if (jValue instanceof String)
                value = casters.stringToCharSeq(evalContext, ((String) jValue));
        } catch (Exception e) {
            log.error("error invoking native method", e);
            throw new SpaceX(newRuntimeError("error invoking native method " + e.getMessage()));
        }
        if (value != null) {
            autoCastAssign(evalContext, functionCallContext.getReturnValueHolder(), value);
        }
        functionCallContext.setPendingReturn(true);
        log.debug("eval -> " + value);
        return value;
    }

    /*
     Find values in all possible locations:
     1. current block
     2. parent block(s)
     3. call args
     4. context object (tuple)
     5. static objects
    */
    private Value eval(EvalContext evalContext, MetaRefPath metaRef) {
        log.debug("eval: " + metaRef);
        Value value = null;
        Named toMember = metaRef.getResolvedMetaObj();
        FunctionCallContext functionCallContext = evalContext.peekStack();
        switch (metaRef.getResolvedDatumScope()) {
            case BLOCK:
                value = findInBlocksRec(toMember, functionCallContext.getBlockContexts()).getValue();
                break;
            case ARG:
                value = functionCallContext.getArgTuple().get((Declaration) toMember).getValue();
                break;
            case TYPE_DEFN:
                value = functionCallContext.getCtxObject().get((Declaration) toMember).getValue();
                break;
            case STATIC:
                throw new SpaceX(evalContext.newRuntimeError("don't yet eval static datums"));
//                break;
        }

        if (value == null)
            throw new SpaceX(evalContext.newRuntimeError("could not resolve reference " + metaRef));

        log.debug("eval -> " + value);
        return value;
    }

    private Value eval(EvalContext evalContext, AssignmentExpr assignmentExpr) {
        log.debug("eval: " + assignmentExpr);
        ValueHolder leftSideHolder = (ValueHolder) eval(evalContext, assignmentExpr.getLeftSideDatumRef());
        Value rightSideValue = eval(evalContext, assignmentExpr.getRightSideValueExpr());
        autoCastAssign(evalContext, leftSideHolder, rightSideValue);
        log.debug("eval -> " + leftSideHolder);
        return leftSideHolder.getValue();
    }

    /**
     * Returns the literal value object associated with the {@link PrimitiveLiteralExpr}.
     * The returned object may be a scalar, a character sequence, or a complete
     * space.
     */
    private Value eval(EvalContext evalContext, PrimitiveLiteralExpr primitiveLiteralExpr) {
        log.debug("eval: " + primitiveLiteralExpr);
        Value value = null;
        if (primitiveLiteralExpr.getTypeDefn() == NumPrimitiveTypeDefn.CARD) {
//            case TEXT:
//                CharacterSequence csLiteral = newCharacterSequence(primitiveLiteralExpr.getValueExpr());
//                value = newReference(csLiteral);
//                break;
            value = getObjFactory().newCardinalValue(new Integer(primitiveLiteralExpr.getValueExpr()));
        }
        else if (primitiveLiteralExpr.getTypeDefn() == NumPrimitiveTypeDefn.BOOLEAN) {
            value = getObjFactory().newBooleanValue(Boolean.valueOf(primitiveLiteralExpr.getValueExpr()));
        }
        else if (primitiveLiteralExpr.getTypeDefn() == NumPrimitiveTypeDefn.CHAR) {
            value = getObjFactory().newCharacterValue(primitiveLiteralExpr.getValueExpr().charAt(0));
        }
        else if (primitiveLiteralExpr.getTypeDefn() == NumPrimitiveTypeDefn.REAL) {
            value = getObjFactory().newRealValue(Double.valueOf(primitiveLiteralExpr.getValueExpr()));
        }
//       else if (primitiveLiteralExpr.getTypeDefn() ==  RATIONAL) {
//       }
        else {
            throw new SpaceX(evalContext.newRuntimeError(
                "Can not yet handle primitive literal of type " + primitiveLiteralExpr.getTypeDefn()));
        }
        log.debug("eval -> " + value);
        return value;
    }

//    private SpaceObject exec(Space userSpaceContext, SpacePathExpr pathExpr) {
//        SpaceObject value = null;
//
//        return value;
//    }

    private Value eval(EvalContext evalContext, SequenceLiteralExpr sequenceLiteralExpr) {
        log.debug("eval: " + sequenceLiteralExpr);
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
        return value;
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

    private OperEvaluator lookupOperEval(OperEnum operEnum) {
        return operEvalMap.get(operEnum);
    }

    private void autoCastAssign(EvalContext evalContext, ValueHolder leftSideHolder, Value rightSideValue) {
        DatumType leftSideDefnObj =
            leftSideHolder instanceof Variable ?
                ((Variable) leftSideHolder).getDeclaration().getType()
                : (leftSideHolder instanceof Reference ?
                ((Reference) leftSideHolder).getDeclaration().getType()
                : null);

        rightSideValue = autoCast(leftSideDefnObj, rightSideValue);
        SpaceUtils.assignNoCast(evalContext, leftSideHolder, rightSideValue);
    }

    private Value autoCast(DatumType leftSideTypeDefn, Value rightSideValue) {
        Value newValue = rightSideValue;
        // TODO Add some notion of 'casting' and 'auto-(un)boxing'.
        if (leftSideTypeDefn.getOid().equals(Executor.CHAR_SEQ_TYPE_DEF.getOid())
            && !(rightSideValue instanceof Reference)) {
            if (rightSideValue instanceof ScalarValue) {
                CharacterSequence characterSequence =
                    newCharacterSequence(((ScalarValue) rightSideValue).asString());
                newValue = characterSequence.getOid();
                log.debug("cast scalar value to CharSequence");
            }
            else if (rightSideValue instanceof Variable) {
                CharacterSequence characterSequence =
                    newCharacterSequence(((Variable) rightSideValue).getScalarValue().getJValue().toString());
                newValue = characterSequence.getOid();
                log.debug("cast variable (1-D) value to CharSequence");
            }
        }
        else if (leftSideTypeDefn instanceof NumPrimitiveTypeDefn && rightSideValue instanceof ScalarValue) {
            ScalarValue rsScalarValue = (ScalarValue) rightSideValue;
            if (leftSideTypeDefn == NumPrimitiveTypeDefn.CARD
                && rsScalarValue.getType() == NumPrimitiveTypeDefn.REAL) {
                newValue =
                    ObjectFactory.getInstance().newCardinalValue(((Double) rsScalarValue.getJValue()).intValue());
                log.debug("cast real to card");
            }
        }
        return newValue;
    }

    private TupleImpl newTupleImpl(ComplexType defn) {
        TupleImpl tuple = getObjFactory().newTupleImpl(defn);
        initTrackTuple(defn, tuple);
        return tuple;
    }

    private void initTrackTuple(ComplexType defn, Tuple tuple) {
        // initialize
        if (defn.hasDatums()) {
            List<Declaration> declList = defn.getDatumDeclList();
            for (Declaration datumDecl : declList) {
                tuple.initHolder(newHolder(tuple, datumDecl));
            }
        }

        trackInstanceObject((SpaceObject) tuple);
    }

    // ---------------------------- New Space Objects ------------------------

    private SetSpace newSet(SetSpace contextSpace, SetTypeDefn setTypeDefn) {
        SetSpace setSpace = getObjFactory().newSet(contextSpace, setTypeDefn);
        trackInstanceObject(setSpace);
        return setSpace;
    }

    private Reference newReference(AssociationDecl assocDecl, Tuple tuple, SpaceObject toObject) {
        return newReference(assocDecl, tuple, toObject.getOid());
    }

    private Reference newReference(AssociationDecl assocDecl, Tuple tuple, SpaceOid toOid) {
        return getObjFactory().newObjectReference(assocDecl, tuple, toOid);
    }

    CharacterSequence newCharacterSequence(String stringValue) {
        CharacterSequence newCs = getObjFactory().newCharacterSequence(stringValue);
        trackInstanceObject(newCs);
        return newCs;
    }


    public ValueHolder newHolder(Tuple tuple, Declaration declaration) {
        ValueHolder holder = null;
        ObjectFactory ofact = getObjFactory();
        if (declaration.getType() instanceof PrimitiveTypeDefn) {
            holder = new Variable(tuple, ((VariableDecl) declaration));
        }
        else if (declaration instanceof SpaceTypeDefn) {
            holder = ofact.newObjectReference(null, null, null);
        }
        else if (declaration instanceof StreamTypeDefn) {
            holder = ofact.newObjectReference(null, null, null);
        }
        else if (declaration instanceof SequenceTypeDefn) {
            holder = ofact.newObjectReference(null, null, null);
        }
        else if (declaration instanceof SetTypeDefn) {
            holder = ofact.newObjectReference(null, null, null);
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

    private void trackInstanceObject(SpaceObject spaceObject) {
        objectTable.addObject(spaceObject);
    }

    public SpaceObject dereference(SpaceOid referenceOid) throws SpaceX {
        SpaceObject spaceObject = objectTable.getObjectByOid(referenceOid);
        if (spaceObject == null)
            throw new SpaceX("Space Oid [" + referenceOid + "] not found.");
        return spaceObject;
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
            DatumType importedTypeMatch =
                (DatumType) CollectionUtils.find(
                    parseUnit.getAllImportedTypes(),
                    object -> ((DatumType) object).getName().equals(exprLink.getNameRef().getRefAsNameRef())
                );
           AstUtils.checkSetResolve(exprLink, (NamedElement) importedTypeMatch);
        }

        private void checkImplicitImports(LinkSource expLink) {
            DatumType importedTypeMatch = (DatumType) CollectionUtils.find(
                implicitImportTypes,
                object -> ((DatumType) object).getName().equals(
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
