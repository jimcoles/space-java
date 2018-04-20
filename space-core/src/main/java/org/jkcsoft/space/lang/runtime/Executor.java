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
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.RollingFileAppender;
import org.jkcsoft.java.util.JavaHelper;
import org.jkcsoft.java.util.Lister;
import org.jkcsoft.java.util.Strings;
import org.jkcsoft.space.lang.ast.*;
import org.jkcsoft.space.lang.instance.*;
import org.jkcsoft.space.lang.instance.Set;
import org.jkcsoft.space.lang.loader.AstLoadError;
import org.jkcsoft.space.lang.loader.AstLoader;
import org.jkcsoft.space.lang.runtime.jnative.math.Math;
import org.jkcsoft.space.lang.runtime.jnative.opsys.OpSys;
import org.jkcsoft.space.lang.runtime.jnative.space.Lang;
import org.jkcsoft.space.lang.runtime.jnative.space.SpaceOpers;
import org.jkcsoft.space.lang.runtime.typecasts.CastTransforms;

import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

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

    public static final StackTraceLister STACK_TRACE_LISTER = new StackTraceLister();
    private static final Logger log = Logger.getLogger(Executor.class);
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
    public static SpaceTypeDefn MATH_TYPE_DEF;
    public static SpaceTypeDefn space_opers_type_def;
    public static SpaceTypeDefn op_sys_type_def;
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
    /**
     * Meta objects are loaded as we parse the source code. Intrinsic and native meta
     * objects are loaded prior to parsing and source files.  Must be able to lookup
     * a meta object by name or by id.  Some meta object are anonymous.
     */
    private final List<Schema> dirChain = new LinkedList<>();
    /**
     * A special directory root to hold intrinsic operators.
     */
    private Schema langRootSchema;
    /**
     * The central meta object table.
     */
    private java.util.Set metaObjectNormalTable = new HashSet<>();
    /**
     * (not used currently) Idea is to hold redundantly accumulated info useful
     * for lookup during execution.
     */
//    private Map<NamedElement, MetaInfo> metaObjectExtendedInfoMap = new TreeMap<>();

    private ObjectTable objectTable = new ObjectTable();
    private Stack<FunctionCallContext> callStack = new Stack<>();
    private Map<String, ExprProcessor> exprProcessors = null;

    public Executor() {
//        userAstFactory.newProgram(new NativeSourceInfo(null), "root");
        initRuntime();
        loadNativeTypes();
    }

    private void initRuntime() {
        AstFactory astFactory = getAstFactory();
        langRootSchema = astFactory.newAstSchema(new ProgSourceInfo(), "lang");

        dirChain.add(langRootSchema);
        trackMetaObject(langRootSchema);
    }

    public void run(String... filePath) {
        try {
            File file = FileUtils.getFile(filePath);
            if (!file.exists()) {
                throw new SpaceX("Input file [" + file + "] does not exist.");
            }
            run(file);
        } catch (SpaceX spex) {
            dumpAsts();
//            PrintStream ps = System.err;
            PrintStream psOut = System.out;
            StringBuffer sb = new StringBuffer();
            psOut.println("Space Exception: " + spex.getMessage());
            if (spex.getError() != null) {
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

    public void run(File file) {
        AstLoader astLoader;
        String parserImplClassName = "org.jkcsoft.space.antlr.loaders.G2AntlrParser";
//            String parserImplClassName = "org.jkcsoft.space.antlr.test.TestGrammarParser";

        // 1. Find loader impl
        try {
            Class<?> aClass = Class.forName(parserImplClassName);
            log.debug(String.format("Found loader provider class [%s]", parserImplClassName));
            astLoader = (AstLoader) aClass.newInstance();
            log.debug(String.format("Found source loader [%s]", astLoader.getName()));
        } catch (Exception e) {
            throw new SpaceX("can not find or load source loader [" + parserImplClassName + "]", e);
        }
        // 2. Load (parse and build AST)
        Schema thisSchema = null;
        List<AstLoadError> loadErrors;
        try {
            loadErrors = new LinkedList<>();
            thisSchema = astLoader.load(loadErrors, file);
        } catch (Exception ex) {
            throw new SpaceX("Failed loading source", ex);
        }

        Collection syntaxErrors =
            CollectionUtils.select(loadErrors,
                                   object -> ((AstLoadError) object).getType() == AstLoadError.Type.SYNTAX);

        if (thisSchema == null || (syntaxErrors != null && syntaxErrors.size() > 0)) {
            System.err.println(Strings.buildNewlineList(syntaxErrors));
            throw new SpaceX("found " + syntaxErrors.size() + " loader errors");
        }

        try {
            List<RuntimeError> runErrors = new LinkedList<>();
            dirChain.add(thisSchema);
            trackMetaObject(thisSchema);

            if (log.isDebugEnabled())
                dumpSymbolTables();

//            if (log.isDebugEnabled())
//                dumpAsts();

            // 3. Link AST refs and execute using the AST
            linkAndExec(runErrors, thisSchema);
        } catch (SpaceX ex) {
            throw ex;
        } catch (Exception ex) {
            throw new SpaceX("Failed running program", ex);
        }
    }

    public Stack<FunctionCallContext> getCallStack() {
        Stack<FunctionCallContext> copy = new Stack<>();
        copy.addAll(callStack);
        return copy;
    }

    public void linkAndExec(List<RuntimeError> runErrors, Schema runSchema) throws Exception {
        link(runErrors, runSchema);
        if (runErrors.size() == 0) {
            //
            typeCheck(runErrors, runSchema);
            //
            exec(runSchema);
            //
        }
        else {
            log.warn("skipping exec due to link errors.");
            //
            if (log.isDebugEnabled())
                dumpAsts();
        }
    }

    private void typeCheck(List<RuntimeError> errors, Schema runSchema) {
        // TODO: Traverse full linked AST
        /*
        1. Ensure compatible left/right side of assignments.
         */
    }

    private void link(List<RuntimeError> runErrors, Schema runSchema) {
        log.debug("linking loaded program");
        runErrors = linkRefs(runErrors, langRootSchema);
        runErrors = linkRefs(runErrors, runSchema);
        if (runErrors.size() == 0) {
            log.info("no linker errors");
        }
        else {
            String redMsg = "Found [" + runErrors.size() + "] linker errors";
            log.warn(redMsg + ". See error stream.");
            System.err.println(redMsg + ":");
            System.err.println(Strings.buildNewlineList(runErrors));
        }
    }

    /*
     Augments named references to oid-based references after all meta objects
     have been loaded.  Has the effect of doing semantic validation.
     Could also do some linking while loading.
     */
    private List<RuntimeError> linkRefs(List<RuntimeError> errors, ModelElement astNode) {
        if (errors == null)
            errors = new LinkedList<>();
        if (astNode.hasReferences()) {
            java.util.Set<MetaReference> references = astNode.getReferences();
            if (references != null) {
                log.debug("linking refs held by [" + astNode + "]");
                for (MetaReference reference : references) {
                    if (reference.getState() == LoadState.INITIALIZED) {
                        log.debug("resolving reference [" + reference + "]");
                        NamedElement refElem = AstUtils.resolveAstPath(dirChain, reference);
                        if (refElem == null) {
                            errors.add(new RuntimeError(reference.getSpacePathExpr().getSourceInfo(), 0,
                                                        "can not resolve symbol " +
                                                            "'" + reference + "'"));
                        }
                        else {
                            if (reference.getTargetMetaType() == refElem.getMetaType()) {
                                reference.setResolvedMetaObj(refElem);
                                reference.setState(LoadState.RESOLVED);
                                log.debug("resolved ref [" + reference + "]");
                            }
                            else
                                errors.add(new RuntimeError(reference.getSpacePathExpr().getSourceInfo(), 0,
                                                            "expression '" + reference.getSpacePathExpr() +
                                                                "' must reference a " + reference.getTargetMetaType()));
                        }
                    }
                }
            }
        }
        List<ModelElement> children = astNode.getChildren();
        for (ModelElement child : children) {
            linkRefs(errors, child);
        }
        return errors;
    }

    private void loadNativeTypes() {
        // use Java annotations Java-native objects
//        SpaceNative.class.getAnnotations();
        //
        loadNativeType(Lang.class, AstUtils.getLangRoot(dirChain));
        op_sys_type_def = loadNativeType(OpSys.class, AstUtils.getLangRoot(dirChain));
//        MATH_TYPE_DEF = loadNativeType(Math.class, AstUtils.getLangRoot(dirChain));
//        space_opers_type_def = loadNativeType(SpaceOpers.class, AstUtils.getLangRoot(dirChain));

    }

    private SpaceTypeDefn loadNativeType(Class jnClass, Schema rootAstSchema) {
        AstFactory astFactory = getAstFactory();
        NativeSourceInfo jSourceInfo = new NativeSourceInfo(jnClass);
        SpaceTypeDefn spaceTypeDefn = astFactory.newSpaceTypeDefn(jSourceInfo,
                                                                  astFactory.newTextNode(jSourceInfo,
                                                                                         toSpaceTypeName(jnClass)));
        trackMetaObject(spaceTypeDefn);
        //
        rootAstSchema.addSpaceDefn(spaceTypeDefn);
        //
        spaceTypeDefn.setBody(astFactory.newTypeDefnBody(jSourceInfo));
        //
        Method[] methods = jnClass.getMethods();
        for (Method jMethod : methods) {
            if (isExcludedNative(jMethod))
                continue;

            NativeSourceInfo jMethodInfo = new NativeSourceInfo(jMethod);
            Class<?> jType = jMethod.getReturnType();
            SpaceTypeInfo spcRetInfo = new SpaceTypeInfo(jType, jMethodInfo);
            NativeFunctionDefn functionDefnAST = astFactory.newNativeFunctionDefn(
                jMethodInfo,
                jMethod.getName(),
                jMethod,
                astFactory
                    .newSpaceTypeDefn(jMethodInfo, astFactory.newTextNode(jMethodInfo, "_sig_" + jMethod.getName())),
                spcRetInfo.getSpaceTypeRef()
            );
            //
            spaceTypeDefn.getBody().addFunctionDefn(functionDefnAST);
            trackMetaObject(functionDefnAST);
            //
            functionDefnAST.getArgSpaceTypeDefn().setBody(astFactory.newTypeDefnBody(jMethodInfo));
            //
            Parameter[] jParameters = jMethod.getParameters();
            for (Parameter jParam : jParameters) {
                NativeSourceInfo jParamInfo = new NativeSourceInfo(jParam);
                SpaceTypeInfo spcTypeInfo = new SpaceTypeInfo(jParam.getType(), jParamInfo);
                if (!spcTypeInfo.isSpacePrim()) {
                    functionDefnAST.getArgSpaceTypeDefn()
                                   .addAssocDefn(
                                       astFactory.newAssociationDecl(jParamInfo,
                                                                     jParam.getName(),
                                                                     spcTypeInfo.getSpaceTypeRef()
                                       )
                                   );
                }
                else {
                    functionDefnAST.getArgSpaceTypeDefn()
                                   .addVariable(
                                       astFactory.newVariableDecl(
                                           new ProgSourceInfo(), jParam.getName(),
                                           ((NumPrimitiveTypeDefn) spcTypeInfo.getSpacePrimType()))
                                   );
                }
            }
        }
        return spaceTypeDefn;
    }

    private TypeRef.CollectionType javaToSpaceCollType(Class clazz) {
        TypeRef.CollectionType spCollType = null;
        if (clazz == String.class) {
            spCollType = TypeRef.CollectionType.SEQUENCE;
        }
        else {
            spCollType = clazz.isArray() ? TypeRef.CollectionType.SEQUENCE : null;
        }
        return spCollType;
    }

    private boolean isExcludedNative(Method jMethod) {
        Method[] baseMethods = Object.class.getMethods();
        return ArrayUtils.contains(baseMethods, jMethod);
    }

    private String toSpaceTypeName(Class spaceJavaWrapperClass) {
//        return spaceJavaWrapperClass.getSimpleName().substring(2);
        return spaceJavaWrapperClass.getSimpleName();
    }

    // ------------------------- Expression Evaluators ------------------------

    private void dumpSymbolTables() {
        log.debug("normalized meta object table: " + JavaHelper.EOL
                      + Strings.buildNewlineList(metaObjectNormalTable));
    }

    /**
     * The entry point for 'running a program': exec the 'main' function.
     */
    public ModelElement exec(Schema programSchema) {
        log.debug("exec: " + programSchema);
        SpaceTypeDefn bootTypeDefn = programSchema.getFirstSpaceDefn();
        Tuple mainTypeTuple = newTuple(bootTypeDefn);
        EvalContext evalContext = new EvalContext();
        FunctionDefn spMainActionDefn = bootTypeDefn.getBody().getFunction("main");

        ProgSourceInfo progSourceInfo = new ProgSourceInfo();
        FunctionCallExpr mainFunctionCallExpr = getAstFactory().newFunctionCallExpr(progSourceInfo);
        mainFunctionCallExpr.setFunctionDefnRef(
            getAstFactory().newSpacePathExpr(new IntrinsicSourceInfo(), null, "main", null)
        );
        mainFunctionCallExpr.getFunctionDefnRef().setResolvedMetaObj(spMainActionDefn);
        mainFunctionCallExpr.getFunctionDefnRef().setState(LoadState.RESOLVED);
        LinkedList<ValueExpr> argExprList = new LinkedList<>();
        argExprList.add(getAstFactory().newCharSeqLiteralExpr(progSourceInfo, "(put CLI here)"));
        mainFunctionCallExpr.setTupleExpr(getAstFactory().newTupleExpr(progSourceInfo).setValueExprs(argExprList));
        linkRefs(null, mainFunctionCallExpr);
        FunctionCallContext bootCallCtxt =
            getObjFactory().newFunctionCall(mainTypeTuple, mainFunctionCallExpr, null, null);
        // init the context tuple
        callStack.push(bootCallCtxt);
        // this evaluates the init statements for the type such as var inits.
        exec(evalContext, (StatementBlock) bootTypeDefn.getBody());
        // call the 'main' function a dummy function call expr
        eval(evalContext, mainFunctionCallExpr);
        callStack.pop();
        log.debug("exiting Space program execution");
        return null;
    }

    //

    private Declartion newAnonDecl(DatumType datumType) {
        Declartion decl = null;
        DatumType rootType = getRootType(datumType);
        if (rootType instanceof NumPrimitiveTypeDefn) {
            decl = getAstFactory().newVariableDecl(
                new ProgSourceInfo(), null, ((NumPrimitiveTypeDefn) datumType));
        }
        else if (rootType instanceof SpaceTypeDefn) {
            decl =
                getAstFactory().newAssociationDecl(new ProgSourceInfo(), null, getAstFactory().newTypeRef(datumType));
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
            functionCallContext.getCtxObject() : newTuple(statementBlock);
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
        Tuple value = eval(evalContext, newObjectExpr.getTypeRef().getResolvedMetaObj(), newObjectExpr.getTupleExpr());
        return value;
    }

    private Value eval(EvalContext evalContext, NewSetExpr newSetExpr) {
        log.debug("eval: " + newSetExpr);
        Set value = newSet(null, null);
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

        AbstractFunctionDefn functionDefn = functionCallExpr.getFunctionDefnRef().getResolvedMetaObj();
        SpaceTypeDefn argSpaceTypeDefn = functionDefn.getArgSpaceTypeDefn();
        Tuple argTuple = eval(evalContext, argSpaceTypeDefn, functionCallExpr.getTupleExpr());
        ValueHolder retValHolder = functionDefn.isReturnVoid() ? newVoidHolder()
            : newHolder(null, newAnonDecl(functionDefn.getReturnTypeRef().getResolvedMetaObj()));
        FunctionCallContext functionCallContext =
            getObjFactory().newFunctionCall(evalContext.peekStack().getCtxObject(), functionCallExpr, argTuple,
                                            retValHolder);
        // push call onto stack
        callStack.push(functionCallContext);
        log.debug("pushed call stack. size [" + callStack.size() + "]");
        try {
            if (functionDefn instanceof FunctionDefn) {
                FunctionDefn targetFunctionDefn =
                    (FunctionDefn) functionDefn;
                exec(evalContext, targetFunctionDefn.getStatementBlock());
            }
            else if (functionDefn instanceof NativeFunctionDefn) {
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

    private Tuple eval(EvalContext evalContext, SpaceTypeDefn tupleTypeDefn, TupleExpr tupleExpr)
    {
        log.debug("eval: new tuple => " + tupleTypeDefn + ", " + tupleExpr);
        Tuple tuple = newTuple(tupleTypeDefn);
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
                    autoCast(((TupleDefn) tuple.getDefn()).getDatumDeclList().get(idxArg).getType(), argValue);
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
            NativeFunctionDefn funcDef =
                (NativeFunctionDefn) functionCallExpr.getFunctionDefnRef().getResolvedMetaObj();
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
                value = functionCallContext.getArgTuple().get(toMember).getValue();
                break;
            case OBJECT:
                value = functionCallContext.getCtxObject().get(toMember).getValue();
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
            assignable = blockContext.getDataTuple().get(toMember);
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

    private Tuple newTuple(TupleDefn defn) {
        Tuple tuple = getObjFactory().newTuple(defn);
        // initialize
        if (defn.hasDatums()) {
            List<Declartion> declList = defn.getDatumDeclList();
            for (Declartion datumDecl : declList) {
                tuple.initHolder(newHolder(tuple, datumDecl));
            }
        }

        trackInstanceObject(tuple);
        return tuple;
    }

    // ---------------------------- New Space Objects ------------------------

    private Set newSet(Set contextSpace, SetTypeDefn setTypeDefn) {
        Set set = getObjFactory().newSet(contextSpace, setTypeDefn);
        trackInstanceObject(set);
        return set;
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


    public ValueHolder newHolder(Tuple tuple, Declartion declartion) {
        ValueHolder holder = null;
        ObjectFactory ofact = getObjFactory();
        if (declartion.getType() instanceof PrimitiveTypeDefn) {
            holder = new Variable(tuple, ((VariableDecl) declartion));
        }
        else if (declartion instanceof SpaceTypeDefn) {
            holder = ofact.newObjectReference(null, null, null);
        }
        else if (declartion instanceof StreamTypeDefn) {
            holder = ofact.newObjectReference(null, null, null);
        }
        else if (declartion instanceof SequenceTypeDefn) {
            holder = ofact.newObjectReference(null, null, null);
        }
        else if (declartion instanceof SetTypeDefn) {
            holder = ofact.newObjectReference(null, null, null);
        }
        return holder;
    }

    public ValueHolder newVoidHolder() {
        ValueHolder holder = getObjFactory().newVoidHolder();
        return holder;
    }

    public RuntimeError newRuntimeError(String msg) {
        return new RuntimeError(getCallStack(), -1, msg);
    }

    // -------------- Tracking of runtime/instance things ---------------
    private void trackMetaObject(ModelElement modelElement) {
        // Add to normalized object table ...
        metaObjectNormalTable.add(modelElement);
//        metaObjectIndexByFullPath.put(modelElement.getFullPath(), modelElement);

        // track denormalized info for fast lookup ...
//        if (modelElement instanceof NamedElement) {
//            MetaInfo metaInfo = new MetaInfo((NamedElement) modelElement);
//            metaObjectExtendedInfoMap.put((NamedElement) modelElement, metaInfo);
//
//            // if this object has a parent, Add this object to parent's child map
//            if (modelElement.hasParent()) {
//                metaObjectExtendedInfoMap.get(modelElement.getParent()).addChild((NamedElement) modelElement);
//            }
//        }
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

    private void dumpAsts() {
        log.info("see dump log file for AST dumps");
        for (Schema schema : dirChain) {
            String dump = AstUtils.print(schema);
//            log.debug("AST Root /: " + dump);
            Logger.getLogger("dumpFile").info(dump);
        }
    }

    private void writeSpaceStackTrace(StringBuffer sb, Stack<FunctionCallContext> spaceTrace) {
        sb.append(Strings.buildDelList(spaceTrace, STACK_TRACE_LISTER, JavaHelper.EOL));
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

    private class SpaceTypeInfo {
        private boolean isSpacePrim = false;
        private TypeRef spaceTypeRef;
        private PrimitiveTypeDefn spcPrimType;

        public SpaceTypeInfo(Class jClass, SourceInfo jSourceInfo) {
            spcPrimType = null;
            if (jClass == Boolean.TYPE) {
                spcPrimType = NumPrimitiveTypeDefn.BOOLEAN;
            }
            else if (jClass == Integer.TYPE) {
                spcPrimType = NumPrimitiveTypeDefn.CARD;
            }
            else if (jClass == Float.TYPE) {
                spcPrimType = NumPrimitiveTypeDefn.REAL;
            }
            else if (jClass == Void.TYPE) {
                spcPrimType = VoidType.VOID;
            }

            this.isSpacePrim = spcPrimType != null && !jClass.isArray();

            // special cases for now: Java java.lang.String -> Space char sequence
            if (jClass == String.class) {
                spaceTypeRef = getAstFactory().newTypeRef(
                    getAstFactory().newSpacePathExpr(jSourceInfo, null, NumPrimitiveTypeDefn.CHAR.getName(), null),
                    Collections.singletonList(TypeRef.CollectionType.SEQUENCE)
                );
            }
            else {
                String spcTypeRefName = spcPrimType != null ? spcPrimType.getName() : toSpaceTypeName(jClass);
                spaceTypeRef = getAstFactory().newTypeRef(
                    getAstFactory().newSpacePathExpr(jSourceInfo, null, spcTypeRefName, null),
                    Collections.singletonList(javaToSpaceCollType(jClass))
                );
            }
        }

        public boolean isSpacePrim() {
            return this.isSpacePrim;
        }

        ;

        public TypeRef getSpaceTypeRef() {
            return spaceTypeRef;
        }

        public PrimitiveTypeDefn getSpacePrimType() {
            return this.spcPrimType;
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
}
