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
import org.jkcsoft.java.util.Lister;
import org.jkcsoft.java.util.Strings;
import org.jkcsoft.space.SpaceHome;
import org.jkcsoft.space.lang.ast.*;
import org.jkcsoft.space.lang.ast.sji.SjiFunctionDefnImpl;
import org.jkcsoft.space.lang.ast.sji.SjiTypeDefn;
import org.jkcsoft.space.lang.instance.*;
import org.jkcsoft.space.lang.loader.*;
import org.jkcsoft.space.lang.metameta.MetaType;
import org.jkcsoft.space.lang.runtime.jnative.math.Math;
import org.jkcsoft.space.lang.runtime.jnative.opsys.OpSys;
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

    private static final Logger log = Logger.getLogger(Executor.class);

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

    private NSRegistry nsRegistry = NSRegistry.getInstance();

    private ObjectTable objectTable = new ObjectTable();
    private Stack<FunctionCallContext> callStack = new Stack<>();
    private Map<String, ExprProcessor> exprProcessors = null;
    private ExeSettings exeSettings;
    private AstLoader astLoader;

    public Executor(ExeSettings exeSettings) {
        this.exeSettings = exeSettings;
        initRuntime();
        preLoadSpecialNativeTypes();
        loadSpecialOperators();
    }

    private void initRuntime() {
        loadAstLoader("org.jkcsoft.space.antlr.loaders.G2AntlrParser");
        NSRegistry.getInstance();
    }

    private void loadAstLoader(String parserImplClassName) {
        try {
            Class<?> aClass = Class.forName(parserImplClassName);
            log.debug(String.format("Found loader provider class [%s]", parserImplClassName));
            astLoader = (AstLoader) aClass.newInstance();
            log.debug(String.format("Found source loader [%s]", astLoader.getName()));
        } catch (Exception e) {
            throw new SpaceX("can not find or load source loader [" + parserImplClassName + "]", e);
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
                DirLoadResults dirLoadResults = loadSrcRootDir(srcRootDir);
                //
                runResults.addSrcDirLoadResult(dirLoadResults);
            }

            // 2. Determine all needed Java classes and load corresponding Space wrappers
            java.util.Set<ImportExpr> javaImports =
                AstUtils.queryAst(nsRegistry.getUserNs().getRootDir(),
                                  new QueryAstConsumer<>(
                                      ImportExpr.class,
                                      elem -> elem instanceof ImportExpr && AstUtils.isJavaNs((ImportExpr) elem)
                                  )
                );
            for (ImportExpr javaImport : javaImports) {
                TypeRefImpl targetJavaTypeRef = javaImport.getTypeRefExpr();
                try {
                    // the following call to getDeepLoad() ensures the Space wrapper for the
                    // Java class is loaded and ready for lookup for subsequent linking
                    SpaceHome.getSjiService().getDeepLoadSpaceWrapper(targetJavaTypeRef.getUrlPathSpec());
                } catch (ClassNotFoundException e) {
                    log.warn("could not find Java import class ["+targetJavaTypeRef.getUrlPathSpec()+"]");
                    AstLoadError astLoadError = new AstLoadError(AstLoadError.Type.LINK, javaImport.getSourceInfo(),
                                                                 "could not find Java import class [" +
                                                                     targetJavaTypeRef.getUrlPathSpec() + "] in " +
                                                                     "the Java classpath");
                    runResults.addFileError(astLoadError);
                }
            }

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
                        log.info("no linker errors in [" + parseUnit + "]");
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

            if (runResults.getAllErrors().size() > 0) {
                log.warn("skipping exec due to [" + runResults.getAllErrors().size() + "] link errors.");
                presentAllErrors(runResults);
            }
            else {
                // Execute the specified type 'main' function
                exec(exeSettings.getExeMain());
            }
        }
        catch (SpaceX spex) {
            nsRegistry.dumpAsts();
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

        op_sys_type_def = (ComplexType) SpaceHome.getSjiService().getDeepLoadSpaceWrapper(OpSys.class);

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
//        if (jClassPathDir.isFile()) try {
//            JarFile jarFile = new JarFile(jClassPathDir);
//            Enumeration<JarEntry> jarEntries = jarFile.entries();
//            while (jarEntries.hasMoreElements()) {
//                JarEntry jarEntry = jarEntries.nextElement();
//                String classFileName = jarEntry.getName();
//                SpaceHome.getSjiService().getOrLoadSpaceWrapper(
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
        DirLoadResults dirLoadResults = null;
        try {
            dirLoadResults = astLoader.loadDir(srcRootDir);
        } catch (Exception ex) {
            throw new SpaceX("Failed loading source", ex);
        }

        if (!dirLoadResults.getSpaceRootDir().isRootDir())
            throw new SpaceX("AST loader did not return a valid root directory.");

        mergeNewChildren(nsRegistry.getUserNs().getRootDir(), dirLoadResults.getSpaceRootDir());

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

    public ParseUnit loadFile(Directory spaceDir, File singleFile) {

        FileLoadResults loadResults;
        AstFileLoadErrorSet loadErrors = new AstFileLoadErrorSet(singleFile);
        try {
            loadResults = astLoader.loadFile(spaceDir, singleFile);
        } catch (Exception ex) {
            throw new SpaceX("Failed loading source", ex);
        }


//        loadErrors.checkErrors();

        mergeParseUnit(loadResults.getParseUnit());
        nsRegistry.trackMetaObject(loadResults.getParseUnit());

        return loadResults.getParseUnit();
    }

    private void mergeParseUnit(ParseUnit fileParseUnit) {
        // TODO
    }


    public void linkAndCheckUnit(ParseUnit parseUnit, List<AstLoadError> unitLoadErrors) {
        // link ...
        linkUnitRefs(parseUnit, unitLoadErrors);

        // type check ...
        if (unitLoadErrors.size() == 0) {
            typeCheck(parseUnit, unitLoadErrors);
        }
        else {
            if (log.isDebugEnabled())
                nsRegistry.dumpAsts();
        }
    }

    private void typeCheck(ParseUnit parseUnit, List<AstLoadError> errors) {
        // TODO: Traverse full linked AST
        /*
        1. Ensure compatible left/right side of assignments.
         */
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
        AstUtils.walkAstDepthFirst(parseUnit, new RefLinker(parseUnit, errors));
        return;
    }

    public void resolveImports(ParseUnit parseUnit, List<AstLoadError> unitErrors) {
        List<ImportExpr> importExprExprs = parseUnit.getImportExprs();
        for (ImportExpr importTypeRefExpr : importExprExprs) {
            if (importTypeRefExpr.getTypeRefExpr().isInitialized()) {
                resolveAstPath(importTypeRefExpr.getTypeRefExpr(), unitErrors);
                if (importTypeRefExpr.getTypeRefExpr().isSingleton()) {
                    if (importTypeRefExpr.getTypeRefExpr().isResolvedValid())
                        parseUnit.addToAllImportedTypes(
                            (DatumType) importTypeRefExpr.getTypeRefExpr().getResolvedMetaObj());
                    else
                        log.warn("import reference not fully resolved ["+importTypeRefExpr+"]");
                }
                else {
                    Set<DatumType> refElems = AstUtils.querySiblingTypes(importTypeRefExpr.getTypeRefExpr());
                    parseUnit.getAllImportedTypes().addAll(refElems);

                    // add all types local to this parse unit's directory
                    parseUnit.getAllImportedTypes().addAll(
                        AstUtils.queryAst(parseUnit.getParent(), new QueryAstConsumer<>(SpaceTypeDefn.class))
                    );
                }
            }
        }
    }

    private void resolveAstPath(MetaReference reference, List<AstLoadError> errors) {
        AstUtils.resolveAstPath(reference);
        if (!reference.isResolved()) {
            errors.add(new AstLoadError(AstLoadError.Type.LINK, reference.getSourceInfo(),
                                        "can not resolve symbol '" + reference + "'"));
        }
        else {
            if (reference.getTargetMetaType() == reference.getResolvedMetaObj().getMetaType()) {
                reference.setTypeCheckState(TypeCheckState.VALID);
                log.debug("resolved ref [" + reference + "]");
            }
            else
                errors.add(new AstLoadError(AstLoadError.Type.LINK, reference.getSourceInfo(),
                                            "expression '" + reference + "' must reference a " +
                                                reference.getTargetMetaType()));
        }
    }

    /**
     * The entry point method for 'running' a Space program. Executes the 'main' function.
     */
    public ModelElement exec(String mainSpacePath) {
        log.debug("exec: " + mainSpacePath);

        IntrinsicSourceInfo sourceInfo = new IntrinsicSourceInfo();
        String[] pathNodes = mainSpacePath.split("/.");
        TypeRefImpl exeTypeRef = getAstFactory().newTypeRef(sourceInfo, null, null);
        MetaRefPart<Namespace> userNsRefPart = getAstFactory().newMetaRefPart(sourceInfo, nsRegistry.getUserNs().getName());
        exeTypeRef.setNsRefPart(userNsRefPart);
        AstUtils.addNewMetaRefParts(exeTypeRef, sourceInfo, pathNodes);
        AstUtils.resolveAstPath(exeTypeRef);
        SpaceTypeDefn bootTypeDefn = (SpaceTypeDefn) exeTypeRef.getResolvedType() ;
//        SpaceTypeDefn bootTypeDefn = progSpaceDir.getFirstSpaceDefn();
        Tuple mainTypeTuple = newTupleImpl(bootTypeDefn);
        EvalContext evalContext = new EvalContext();
        SpaceFunctionDefn spMainActionDefn = bootTypeDefn.getBody().getFunction("main");

        ProgSourceInfo progSourceInfo = new ProgSourceInfo();
        FunctionCallExpr bootMainCallExpr = getAstFactory().newFunctionCallExpr(progSourceInfo);
        MetaReference mainFuncRef = getAstFactory().newMetaReference(sourceInfo, MetaType.FUNCTION, userNsRefPart);
        mainFuncRef
            .addNextPart(getAstFactory().newMetaRefPart(getAstFactory().newNamePartExpr(sourceInfo, null, "main")));
        bootMainCallExpr.setFunctionRef(mainFuncRef);
        bootMainCallExpr.getFunctionRef().getFirstPart().setResolvedMetaObj(spMainActionDefn);
        bootMainCallExpr.getFunctionRef().setTypeCheckState(TypeCheckState.VALID);
        LinkedList<ValueExpr> argExprList = new LinkedList<>();
        argExprList.add(getAstFactory().newCharSeqLiteralExpr(progSourceInfo, "(put CLI here)"));
        bootMainCallExpr.setArgTupleExpr(getAstFactory().newTupleExpr(progSourceInfo).setValueExprs(argExprList));
        ParseUnit synthParseUnit = getAstFactory().newParseUnit(sourceInfo);
        synthParseUnit.addChild(bootTypeDefn);
        //
        linkRefs(synthParseUnit, null);
        FunctionCallContext bootCallCtxt =
            getObjFactory().newFunctionCall(mainTypeTuple, bootMainCallExpr, null, null);
        // init the context tuple
        callStack.push(bootCallCtxt);
        // this evaluates the init statements for the type such as var inits.
        exec(evalContext, (StatementBlock) bootTypeDefn.getBody());
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
        TypeRefImpl typeRef = getAstFactory().newTypeRef(new ProgSourceInfo(), datumType);
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
     * TODO I think we'll eventually want all statements to get put on an Action Queue
     * or Transaction Queue or such.
     */
    private void exec(EvalContext evalContext, StatementBlock statementBlock) {
        log.debug("exec: " + statementBlock);
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
                exec(evalContext, ((StatementBlock) statement));
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

    private Value eval(EvalContext spcContext, ValueExpr expression) {
        log.debug("eval: (deleg) " + expression);
        Value value = null;
        if (expression instanceof FunctionCallExpr) {
            value = eval(spcContext, (FunctionCallExpr) expression);
        }
        else if (expression instanceof AssignmentExpr) {
            value = eval(spcContext, (AssignmentExpr) expression);
        }
        else if (expression instanceof PrimitiveLiteralExpr) {
            value = eval(spcContext, (PrimitiveLiteralExpr) expression);
        }
        else if (expression instanceof SequenceLiteralExpr) {
            value = eval(spcContext, ((SequenceLiteralExpr) expression));
        }
        else if (expression instanceof MetaReference) {
            value = eval(spcContext, ((MetaReference) expression));
        }
        else if (expression instanceof OperatorExpr) {
            value = eval(spcContext, ((OperatorExpr) expression));
        }
        else if (expression instanceof NewObjectExpr) {
            value = eval(spcContext, ((NewObjectExpr) expression));
        }
        else if (expression instanceof NewSetExpr) {
            value = eval(spcContext, ((NewSetExpr) expression));
        }
        else
            throw new SpaceX("don't know how to evaluate " + expression);

        log.debug("eval -> " + value);
        return value;
    }

    private Tuple eval(EvalContext evalContext, NewObjectExpr newObjectExpr) {
        log.debug("eval: " + newObjectExpr);
        Tuple value = eval(evalContext, (ComplexType) newObjectExpr.getTypeRef().getResolvedMetaObj(),
                           newObjectExpr.getTupleExpr());
        return value;
    }

    private Value eval(EvalContext evalContext, NewSetExpr newSetExpr) {
        log.debug("eval: " + newSetExpr);
        SetSpace value = newSet(null, null);
        newSetExpr.getNewObjectExprs().forEach(
            newObjectExpr -> value.addTuple(eval(evalContext, newObjectExpr))
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

    private OperEvaluator lookupOperEval(OperEnum operEnum) {
        return operEvalMap.get(operEnum);
    }

    private Value eval(EvalContext evalContext, FunctionCallExpr functionCallExpr) {
        log.debug("eval: " + functionCallExpr);
        Value value = null;

        FunctionDefn functionDefn = (FunctionDefn) functionCallExpr.getFunctionRef().getResolvedMetaObj();
        ComplexType argTypeDefn = functionDefn.getArgSpaceTypeDefn();
        TupleImpl argTuple = eval(evalContext, argTypeDefn, functionCallExpr.getArgTupleExpr());
        ValueHolder retValHolder = functionDefn.isReturnVoid() ? newVoidHolder()
            : newHolder(null, newAnonDecl(functionDefn.getReturnType()));
        FunctionCallContext functionCallContext =
            getObjFactory().newFunctionCall(evalContext.peekStack().getCtxObject(), functionCallExpr, argTuple,
                                            retValHolder);
        // push call onto stack
        callStack.push(functionCallContext);
        log.debug("pushed call stack. size [" + callStack.size() + "]");
        try {
            if (functionDefn instanceof SpaceFunctionDefn) {
                SpaceFunctionDefn targetFunctionDefn =
                    (SpaceFunctionDefn) functionDefn;
                exec(evalContext, targetFunctionDefn.getStatementBlock());
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

    private TupleImpl eval(EvalContext evalContext, ComplexType tupleTypeDefn, TupleExpr tupleExpr)
    {
        log.debug("eval: new tuple => " + tupleTypeDefn + ", " + tupleExpr);
        TupleImpl tuple = newTupleImpl(tupleTypeDefn);
        List<ValueExpr> valueExprs = tupleExpr != null ? tupleExpr.getValueExprs() : null;
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
                ValueHolder leftSideHolder = tuple.get(tuple.getNthMember(idxArg));
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
            List<ValueHolder> sArgHolders = functionCallContext.getArgTuple().getValuesHolders();
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
    private Value eval(EvalContext evalContext, MetaReference<?> metaRef) {
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
            case OBJECT:
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

    private ValueHolder findInBlocksRec(Named toMember, LinkedList<BlockContext> blocks) {
        ValueHolder assignable = null;
        Iterator<BlockContext> blockContextIterator = blocks.descendingIterator();
        while (assignable == null && blockContextIterator.hasNext()) {
            BlockContext blockContext = blockContextIterator.next();
            assignable = blockContext.getDataTuple().get((Declaration) toMember);
        }
        return assignable;
    }

    private Value eval(EvalContext evalContext, AssignmentExpr assignmentExpr) {
        log.debug("eval: " + assignmentExpr);
        ValueHolder leftSideHolder = (ValueHolder) eval(evalContext, assignmentExpr.getMemberRef());
        Value rightSideValue = eval(evalContext, assignmentExpr.getValueExpr());
        autoCastAssign(evalContext, leftSideHolder, rightSideValue);
        log.debug("eval -> " + leftSideHolder);
        return leftSideHolder.getValue();
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
                    newCharacterSequence(((Variable) rightSideValue).getScalarValue().getJvalue().toString());
                newValue = characterSequence.getOid();
                log.debug("cast variable (1-D) value to CharSequence");
            }
        }
        else if (leftSideTypeDefn instanceof NumPrimitiveTypeDefn && rightSideValue instanceof ScalarValue) {
            ScalarValue rsScalarValue = (ScalarValue) rightSideValue;
            if (leftSideTypeDefn == NumPrimitiveTypeDefn.CARD
                && rsScalarValue.getType() == NumPrimitiveTypeDefn.REAL) {
                newValue =
                    ObjectFactory.getInstance().newCardinalValue(((Double) rsScalarValue.getJvalue()).intValue());
                log.debug("cast real to card");
            }
        }
        return newValue;
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

    private TupleImpl newTupleImpl(ComplexType defn) {
        TupleImpl tuple = getObjFactory().newTupleImpl(defn);
        initTrackTuple(defn, tuple);
        return tuple;
    }

    private Tuple newSjiTuple(SjiTypeDefn defn, Object jObject) {
        Tuple tuple = getObjFactory().newSjiTuple(defn, jObject);
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
            return filter;
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
        private List<AstLoadError> errors;
        private Collection<MetaReference> unresolvedRefs;

        public RefLinker(ParseUnit parseUnit, List<AstLoadError> errors) {
            this.parseUnit = parseUnit;
            this.errors = errors;
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

        private Collection<MetaReference> getUnresolvedRefs(ModelElement testNode) {
            java.util.Set<MetaReference> references = testNode.getReferences();
            return (Collection<MetaReference>) CollectionUtils.select(
                references, obj -> ((MetaReference) obj).getState() == LinkState.INITIALIZED);
        }

        @Override
        public boolean upon(ModelElement astNode) {
            if (errors == null)
                errors = new LinkedList<>();

            // NOTE: using unresolvedRefs from the predicate test function
            for (MetaReference reference : unresolvedRefs) {
                log.debug("resolving reference [" + reference + "]");
                if (reference.getTargetMetaType() == MetaType.TYPE && reference.isSinglePart()) {
                    DatumType importedTypeDefn = (DatumType) CollectionUtils.find(
                        parseUnit.getAllImportedTypes(),
                        object -> ((DatumType) object).getName().equals(reference.getFirstPart().getName())
                    );
                    if (importedTypeDefn != null) {
                        // we have an import match
                        reference.getFirstPart().setResolvedMetaObj((NamedElement) importedTypeDefn);
                        reference.setImportMatch(true);
                        reference.getFirstPart().setState(LinkState.RESOLVED);
                    }
                    else {
                        errors.add(new AstLoadError(AstLoadError.Type.LINK, reference.getSourceInfo(),
                                                 "can not resolve symbol '" + reference + "'"));
                    }
                }
                else {
                    resolveAstPath(reference, errors);
                }
            }
            // Null out instance-level field reused across AST nodes
            this.unresolvedRefs = null;
            return true;
        }

        @Override
        public void after(ModelElement astNode) {

        }

        public List<AstLoadError> getErrors() {
            return errors;
        }
    }

}
