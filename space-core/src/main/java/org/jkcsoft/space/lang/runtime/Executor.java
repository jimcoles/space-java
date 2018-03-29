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
import org.jkcsoft.java.util.JavaHelper;
import org.jkcsoft.java.util.Lister;
import org.jkcsoft.java.util.Strings;
import org.jkcsoft.space.lang.ast.*;
import org.jkcsoft.space.lang.instance.*;
import org.jkcsoft.space.lang.loader.AstLoadError;
import org.jkcsoft.space.lang.loader.AstLoader;
import org.jkcsoft.space.lang.runtime.typecasts.CastTransforms;
import org.jkcsoft.space.lang.runtime.jnative.math.JnMath;
import org.jkcsoft.space.lang.runtime.jnative.opsys.JnOpSys;
import org.jkcsoft.space.lang.runtime.jnative.space.JnSpaceOpers;

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

    private static final Logger log = Logger.getLogger(Executor.class);

    public static final StackTraceLister STACK_TRACE_LISTER = new StackTraceLister();

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
    public static StreamTypeDefn CHAR_SEQ_TYPE_DEF;
    public static SpaceTypeDefn space_opers_type_def;
    public static SpaceTypeDefn op_sys_type_def;
    /**
     * Meta objects are loaded as we parse the source code. Intrinsic and native meta
     * objects are loaded prior to parsing and source files.  Must be able to lookup
     * a meta object by name or by id.  Some meta object are anonymous.
     */
    private final List<Schema> dirChain = new LinkedList<>();
    public AbstractFunctionDefn OPER_NAV;
    public AbstractFunctionDefn OPER_NEW_TUPLE;
    public AbstractFunctionDefn OPER_NEW_CHAR;
    public AbstractFunctionDefn OPER_ASSIGN;
    /**
     * A special directory root to hold intrinsic operators.
     */
    private Schema langRootSchema;
    /**
     * The central meta object table.
     */
    private Set<ModelElement> metaObjectNormalTable = new HashSet<>();
    /**
     * (not used currently) Idea is to hold redundantly accumulated info useful
     * for lookup during execution.
     */
//    private Map<NamedElement, MetaInfo> metaObjectExtendedInfoMap = new TreeMap<>();

    private ObjectTable objectTable = new ObjectTable();
    private Stack<FunctionCallContext> callStack = new Stack<>();
    /*
     * TODO: Use specialized expression processors for each type of expression.
     * E.g., unary int, binary int, unary string, binary string, etc. Mapping from expression type
     * to expression handler. Some handlers will be Space standard, some will be user-provided.
     */
    private Map<String, ExprProcessor> exprProcessors = null;

    public Executor() {
//        userAstFactory.newProgram(new NativeSourceInfo(null), "root");
        initRuntime();
        loadNativeMetaObjects();
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
        }
        catch (SpaceX spex) {
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

    private void writeSpaceStackTrace(StringBuffer sb, Stack<FunctionCallContext> spaceTrace) {
        sb.append(Strings.buildDelList(spaceTrace, STACK_TRACE_LISTER, JavaHelper.EOL));
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
        }
        catch (Exception ex) {
            throw new SpaceX("Failed loading source", ex);
        }

        Collection syntaxErrors =
            CollectionUtils.select(loadErrors,
                                   object -> ((AstLoadError) object).getType() == AstLoadError.Type.SYNTAX);

        if (thisSchema == null || (syntaxErrors != null && syntaxErrors.size() > 0))
        {
            System.err.println(Strings.buildNewlineList(syntaxErrors));
            throw new SpaceX("found "+syntaxErrors.size()+" loader errors");
        }

        try {
            List<RuntimeError> runErrors = new LinkedList<>();
            dirChain.add(thisSchema);
            trackMetaObject(thisSchema);

            if (log.isDebugEnabled())
                dumpSymbolTables();

//            if (log.isDebugEnabled())
//                dumpAsts();

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
            if (log.isDebugEnabled())
                dumpAsts();
            //
            typeCheck(runErrors, runSchema);
            //
            exec(runSchema);
            //
        }
        else {
            log.warn("skipping exec due to link errors.");
        }
    }

    private void typeCheck(List<RuntimeError> errors, Schema runSchema) {
        // TODO: Traverse full linked AST
        /*
        1. Ensure compatible left/right side of assignments.
         */
    }

    private void link(List<RuntimeError> runErrors, Schema runSchema) throws Exception {
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
            Set<MetaReference> references = astNode.getReferences();
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
    private ObjectFactory getObjFactory() {
        return ObjectFactory.getInstance();
    }

    private void loadNativeMetaObjects() {
        // use Java annotations Java-native objects
//        SpaceNative.class.getAnnotations();
        CHAR_SEQ_TYPE_DEF = loadSequenceType(AstUtils.getLangRoot(dirChain));
        op_sys_type_def = loadNativeType(JnOpSys.class, AstUtils.getLangRoot(dirChain));
        MATH_TYPE_DEF = loadNativeType(JnMath.class, AstUtils.getLangRoot(dirChain));
        space_opers_type_def = loadNativeType(JnSpaceOpers.class, AstUtils.getLangRoot(dirChain));

        OPER_NAV = lookupOperator("nav");
        OPER_NEW_TUPLE = lookupOperator("newTuple");
        OPER_NEW_CHAR = lookupOperator("newCharSequence");
        OPER_ASSIGN = lookupOperator("assign");
    }

    private StreamTypeDefn loadSequenceType(Schema rootAstSchema) {
        StreamTypeDefn streamTypeDefn = getAstFactory().newStreamTypeDefn(new IntrinsicSourceInfo(), "CharSequence");
        //
        trackMetaObject(streamTypeDefn);
        //
        rootAstSchema.addStreamTypeDefn(streamTypeDefn);
        //
        return streamTypeDefn;
    }

    private SpaceTypeDefn loadNativeType(Class jnClass, Schema rootAstSchema) {
        AstFactory astFactory = getAstFactory();
        SpaceTypeDefn spaceTypeDefn = astFactory.newSpaceTypeDefn(
            new NativeSourceInfo(jnClass), toSpaceTypeName(jnClass)
        );
        trackMetaObject(spaceTypeDefn);
        //
        rootAstSchema.addSpaceDefn(spaceTypeDefn);
        //
        spaceTypeDefn.setBody(astFactory.newTypeDefnBody(new NativeSourceInfo(jnClass)));
        //
        Method[] methods = jnClass.getMethods();
        for (Method jMethod : methods) {
            if (isExcludedNative(jMethod))
                continue;

            NativeSourceInfo jMethodInfo = new NativeSourceInfo(jMethod);
            NativeFunctionDefn functionDefnAST = astFactory.newNativeFunctionDefn(
                jMethodInfo,
                jMethod.getName(),
                jMethod,
                astFactory.newSpaceTypeDefn(jMethodInfo, "_sig_" + jMethod.getName()),
                astFactory.newSpacePathExpr(jMethodInfo, null, javaToSpace(jMethod.getReturnType()).getName(), null)
            );
            //
            spaceTypeDefn.getBody().addFunctionDefn(functionDefnAST);
            trackMetaObject(functionDefnAST);
            //
            functionDefnAST.getArgSpaceTypeDefn().setBody(astFactory.newTypeDefnBody(jMethodInfo));
            //
            Parameter[] jParameters = jMethod.getParameters();
            for (Parameter jParam : jParameters) {
                DatumType spcPrim = javaToSpace(jParam.getType());
                if (!(spcPrim instanceof PrimitiveTypeDefn)) {
                    if (spcPrim == CHAR_SEQ_TYPE_DEF) {
                        NativeSourceInfo jParamInfo = new NativeSourceInfo(jParam);
                        functionDefnAST.getArgSpaceTypeDefn().addAssocDefn(
                            astFactory.newAssociationDefn(jParamInfo, jParam.getName(),
                                                          astFactory.newSpacePathExpr(jParamInfo, null,
                                                                                      CHAR_SEQ_TYPE_DEF.getName(),
                                                                                      null)));
                    }
                    else {

                    }
                }
                else {
                    functionDefnAST.getArgSpaceTypeDefn().addVariable(
                        astFactory.newVariableDefn(new ProgSourceInfo(), jParam.getName(), ((PrimitiveTypeDefn) spcPrim)));
                }
            }
        }
        return spaceTypeDefn;
    }

    private AstFactory getAstFactory() {
        return new AstFactory();
    }

    private DatumType javaToSpace(Class<?> jType) {
        DatumType spcDatumType = VoidType.VOID;
        if (jType == String.class) {
            spcDatumType = CHAR_SEQ_TYPE_DEF;
        }
        else if (jType == Boolean.TYPE) {
            spcDatumType = PrimitiveTypeDefn.BOOLEAN;
        }
        else if (jType == Integer.TYPE) {
            spcDatumType = PrimitiveTypeDefn.CARD;
        }
        else if (jType == Float.TYPE) {
            spcDatumType = PrimitiveTypeDefn.REAL;
        }
        return spcDatumType;
    }

    private boolean isExcludedNative(Method jMethod) {
        Method[] baseMethods = Object.class.getMethods();
        return ArrayUtils.contains(baseMethods, jMethod);
    }

    private AbstractFunctionDefn lookupOperator(String operSimpleName) {
        NamedElement opersSpaceDefn = AstUtils.lookupImmediateChild(langRootSchema, toSpaceTypeName(JnSpaceOpers.class));
        AbstractFunctionDefn operActionDefn =
            (AbstractFunctionDefn) AstUtils.lookupImmediateChild(opersSpaceDefn, operSimpleName);
        if (operActionDefn == null)
            throw new SpaceX("space lang object not found with path [" + operSimpleName + "]");
        return operActionDefn;
    }

    private String toSpaceTypeName(Class spaceJavaWrapperClass) {
        return spaceJavaWrapperClass.getSimpleName().substring(2);
    }

    private void dumpSymbolTables() {
        log.debug("normalized meta object table: " + JavaHelper.EOL
                      + Strings.buildNewlineList(metaObjectNormalTable));
    }


    // ------------------------- Expression Evaluators ------------------------
    /**
     * The entry point for 'running a program': exec the 'main' function.
     */
    public ModelElement exec(Schema programSchema) {
        log.debug("exec: " + programSchema);
        SpaceTypeDefn bootTypeDefn = programSchema.getFirstSpaceDefn();
        Tuple mainTypeTuple = newTuple(bootTypeDefn);
        EvalContext evalContext = new EvalContext();
        FunctionDefn spMainActionDefn = bootTypeDefn.getBody().getFunction("main");

        FunctionCallExpr mainFunctionCallExpr = getAstFactory().newFunctionCallExpr(new ProgSourceInfo());
        mainFunctionCallExpr.setFunctionDefnRef(
            getAstFactory().newSpacePathExpr(new IntrinsicSourceInfo(), null, "main", null)
        );
        mainFunctionCallExpr.getFunctionDefnRef().setResolvedMetaObj(spMainActionDefn);
        mainFunctionCallExpr.getFunctionDefnRef().setState(LoadState.RESOLVED);
        LinkedList<ValueExpr> argExprList = new LinkedList<>();
        argExprList.add(getAstFactory().newSequenceLiteralExpr(new ProgSourceInfo(), "(put CLI here)"));
        mainFunctionCallExpr.setArgumentExprs(argExprList);
        linkRefs(null, mainFunctionCallExpr);
        FunctionCallContext bootCallCtxt =
            getObjFactory().newFunctionCall(mainTypeTuple, mainFunctionCallExpr, null);
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
                Assignable retVal = eval(evalContext, ((ReturnExpr) statement).getValueExpr());
                autoCastAssign(evalContext, functionCallContext.getReturnValue(), retVal);
                functionCallContext.setPendingReturn(true);
            }
        }
        functionCallContext.popBlock();
        return;
    }

    private Assignable eval(EvalContext spcContext, ValueExpr expression) {
        log.debug("eval: (deleg) " + expression);
        Assignable value = null;
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
            value = eval(spcContext, ((SequenceLiteralExpr) expression) );
        }
        else if (expression instanceof MetaReference) {
            value = eval(spcContext, ((MetaReference) expression));
        }
        else if (expression instanceof OperatorExpr) {
            value = eval(spcContext, ((OperatorExpr) expression));
        }
        else
            throw new SpaceX("don't know how to evaluate " + expression);

        log.debug("eval -> " + value);
        return value;
    }


    private Assignable eval(EvalContext evalContext, OperatorExpr operatorExpr) {
        log.debug("eval: " + operatorExpr);
        Assignable value = null;
        OperEvaluator oper = lookupOperEval(operatorExpr.getOper());
        if (oper == null)
            throw new SpaceX(evalContext.newRuntimeError("no operator evaluator for " + operatorExpr.getOper()));
        Assignable[] argValues = new Assignable[operatorExpr.getArgs().size()];
        for (int idxArg = 0; idxArg < operatorExpr.getArgs().size(); idxArg++) {
            argValues[idxArg] = eval(evalContext, operatorExpr.getArgs().get(idxArg));
        }
        value = oper.eval(argValues);
        return value;
    }

    private static Map<OperEnum, OperEvaluator> operEvalMap = new TreeMap<>();

    static {
        operEvalMap.put(OperEnum.ADD, JnMath::addNum);
        operEvalMap.put(OperEnum.SUB, JnMath::subNum);
        operEvalMap.put(OperEnum.MULT, JnMath::multNum);
        operEvalMap.put(OperEnum.DIV, JnMath::divNum);
        //
        operEvalMap.put(OperEnum.COND_AND, JnMath::condAnd);
        //
        operEvalMap.put(OperEnum.EQ, JnMath::equal);
        operEvalMap.put(OperEnum.LT, JnMath::lt);
    }

    private OperEvaluator lookupOperEval(OperEnum operEnum) {
        return operEvalMap.get(operEnum);
    }

    private Assignable eval(EvalContext evalContext, FunctionCallExpr functionCallExpr) {
        log.debug("eval: " + functionCallExpr);
        Assignable value = null;

        Tuple argTuple =
            newTuple((functionCallExpr.getFunctionDefnRef().getResolvedMetaObj()).getArgSpaceTypeDefn());

        // validation
        int numArgExprs = functionCallExpr.getArgumentExprs() == null ? 0 : functionCallExpr.getArgumentExprs().size();
        if (numArgExprs != argTuple.getSize())
            throw new SpaceX(evalContext.newRuntimeError(
                "call expression arguments tuple does not match function definition. expected "+argTuple.getSize()
                    +" received "+numArgExprs+""));

        // Arg assignments may be by name or by order (like a SQL update statement), but not both.
        List<ValueExpr> argumentExprs = functionCallExpr.getArgumentExprs();
        if (argumentExprs != null) {
            int idxArg = 0;
            for (ValueExpr argumentExpr : argumentExprs) {
                Assignable argValue = eval(evalContext, argumentExpr);
                argValue =
                    autoCast(((TupleDefn) argTuple.getDefn()).getAllMembers().get(idxArg), argValue);
                Assignable leftSideHolder = argTuple.get(argTuple.getNthMember(idxArg));
                SpaceUtils.assignNoCast(evalContext, leftSideHolder, argValue);
                idxArg++;
            }
        }

//        Tuple callTargetCtxObject = resolvePathObjects(evalContext, functionCallExpr.getFunctionDefnRef().getSpacePathExpr());
        FunctionCallContext functionCallContext =
            getObjFactory().newFunctionCall(evalContext.peekStack().getCtxObject(), functionCallExpr, argTuple);

        // push call onto stack
        callStack.push(functionCallContext);
        log.debug("pushed call stack. size [" + callStack.size() + "]");
        try {
            if (functionCallExpr.getFunctionDefnRef().getResolvedMetaObj() instanceof FunctionDefn) {
                FunctionDefn targetFunctionDefn =  (FunctionDefn) functionCallExpr.getFunctionDefnRef().getResolvedMetaObj();
                exec(evalContext, targetFunctionDefn.getStatementBlock());
                value = evalContext.peekStack().getReturnValue();
            }
            else if (functionCallExpr.getFunctionDefnRef().getResolvedMetaObj() instanceof NativeFunctionDefn)
                value = evalNative(evalContext, functionCallExpr);
        }
        finally {
            FunctionCallContext popCall = callStack.pop();
            popCall.setPendingReturn(false);
            log.debug("popped call stack. size [" + callStack.size() + "]");
        }
        log.debug("eval -> " + value);
        return value;
    }

    private Assignable eval(EvalContext evalContext, NamedElement member) {
        return evalContext.peekStack().getCtxObject().get(member);
    }

    /**
     * Invokes a Java native method all via Java reflection.  Native actions
     * do not have nested actions, at least none that are executed or controlled
     * by this executor.
     */
    private Assignable evalNative(EvalContext evalContext, FunctionCallExpr functionCallExpr) {
        log.debug("eval: " + functionCallExpr);
        Assignable value = null;
        try {
            List<Assignable> sArgValues = evalContext.peekStack().getArgTuple().getValuesHolders();
            Object jArgs[] = new Object[sArgValues.size()];
            int idxArg = 0;
            for (Assignable spcArgValue : sArgValues) {
                Object jArg = "?";
                if (spcArgValue instanceof Reference)
                    //
                    jArg = dereference(((Reference) spcArgValue).getToOid()).toString();
                else {
                    jArg = spcArgValue.toString();
                }
                // TODO: 1/29/17 Remove hardcodings and improve casting generalization
                CastTransforms casters = new CastTransforms();
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
        } catch (Exception e) {
            log.error("error invoking native method", e);
            throw new SpaceX(newRuntimeError("error invoking native method " + e.getMessage()));
        }
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
    private Assignable eval(EvalContext evalContext, MetaReference<?> metaRef) {
        log.debug("eval: " + metaRef);
        Assignable value = null;
        NamedElement toMember = metaRef.getResolvedMetaObj();
        FunctionCallContext functionCallContext = evalContext.peekStack();
        switch (metaRef.getResolvedDatumScope()) {
            case BLOCK:
                value = findInBlocksRec(toMember, functionCallContext.getBlockContexts());
                break;
            case ARG:
                value = functionCallContext.getArgTuple().get(toMember);
                break;
            case OBJECT:
                value = functionCallContext.getCtxObject().get(toMember);
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
    private Assignable findInBlocksRec(NamedElement toMember, LinkedList<BlockContext> blocks) {
        Assignable assignable = null;
        Iterator<BlockContext> blockContextIterator = blocks.descendingIterator();
        while (assignable == null && blockContextIterator.hasNext()) {
            BlockContext blockContext = blockContextIterator.next();
            assignable = blockContext.getDataTuple().get(toMember);
        }
        return assignable;
    }

    private Assignable eval(EvalContext evalContext, AssignmentExpr assignmentExpr) {
        log.debug("eval: " + assignmentExpr);
        Assignable leftSideHolder = eval(evalContext, assignmentExpr.getMemberRef());
        Assignable rightSideValue = eval(evalContext, assignmentExpr.getValueExpr());
        autoCastAssign(evalContext, leftSideHolder, rightSideValue);
        log.debug("eval -> " + leftSideHolder);
        return leftSideHolder;
    }

    private void autoCastAssign(EvalContext evalContext, Assignable leftSideHolder, Assignable rightSideValue)
    {
        NamedElement leftSideDefnObj =
            leftSideHolder instanceof Variable ?
                ((Variable) leftSideHolder).getDefinition()
                : (leftSideHolder instanceof ScalarValue ?
                    ((ScalarValue) leftSideHolder).getType()
                    : (leftSideHolder instanceof Reference ?
                        ((Reference) leftSideHolder).getDefn()
                        : null));

        rightSideValue = autoCast(leftSideDefnObj, rightSideValue);
        SpaceUtils.assignNoCast(evalContext, leftSideHolder, rightSideValue);
    }

    private Assignable autoCast(NamedElement leftSideDefnObj, Assignable rightSideValue) {
        Assignable newValue = rightSideValue;
        // TODO Add some notion of 'casting' and 'auto-(un)boxing'.
        if (leftSideDefnObj instanceof AssociationDefn &&
            ((AssociationDefn) leftSideDefnObj).getToType().getOid().equals(Executor.CHAR_SEQ_TYPE_DEF.getOid())
            && !(rightSideValue instanceof Reference))
        {
            if (rightSideValue instanceof ScalarValue) {
                CharacterSequence characterSequence =
                    newCharacterSequence(((ScalarValue) rightSideValue).asString());
                newValue = newReference(characterSequence);
                log.debug("cast scalar value to CharSequence");
            }
            else if (rightSideValue instanceof Variable) {
                CharacterSequence characterSequence =
                    newCharacterSequence(((Variable) rightSideValue).getScalarValue().getJvalue().toString());
                newValue = newReference(characterSequence);
                log.debug("cast variable (1-D) value to CharSequence");
            }
        }
        else if (leftSideDefnObj instanceof PrimitiveTypeDefn && rightSideValue instanceof ScalarValue) {
            ScalarValue rsScalarValue = (ScalarValue) rightSideValue;
            if (leftSideDefnObj == PrimitiveTypeDefn.CARD
                && rsScalarValue.getType() == PrimitiveTypeDefn.REAL) {
                newValue = ObjectFactory.getInstance().newCardinalValue(((Double) rsScalarValue.getJvalue()).intValue());
                log.debug("cast real to card");
            }
        }
        return newValue;
    }


//    private SpaceObject exec(Space userSpaceContext, SpacePathExpr pathExpr) {
//        SpaceObject value = null;
//
//        return value;
//    }
    /**
     * Returns the literal value object associated with the {@link PrimitiveLiteralExpr}.
     * The returned object may be a scalar, a character sequence, or a complete
     * space.
     */
    private Assignable eval(EvalContext evalContext, PrimitiveLiteralExpr primitiveLiteralExpr) {
        log.debug("eval: " + primitiveLiteralExpr);
        Assignable value = null;
        if (primitiveLiteralExpr.getTypeDefn() == PrimitiveTypeDefn.CARD) {
//            case TEXT:
//                CharacterSequence csLiteral = newCharacterSequence(primitiveLiteralExpr.getValueExpr());
//                value = newReference(csLiteral);
//                break;
            value = getObjFactory().newCardinalValue(new Integer(primitiveLiteralExpr.getValueExpr()));
        }
        else if (primitiveLiteralExpr.getTypeDefn() == PrimitiveTypeDefn.BOOLEAN) {
            value = getObjFactory().newBooleanValue(Boolean.valueOf(primitiveLiteralExpr.getValueExpr()));
        }
        else if (primitiveLiteralExpr.getTypeDefn() == PrimitiveTypeDefn.CHAR) {
            value = getObjFactory().newCharacterValue(primitiveLiteralExpr.getValueExpr().charAt(0));
        }
        else if (primitiveLiteralExpr.getTypeDefn() == PrimitiveTypeDefn.REAL) {
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

    private Assignable eval(EvalContext evalContext, SequenceLiteralExpr sequenceLiteralExpr) {
        log.debug("eval: " + sequenceLiteralExpr);
        Assignable value = null;
        if (sequenceLiteralExpr.getTypeRef().getResolvedMetaObj() == CHAR_SEQ_TYPE_DEF) {
            CharacterSequence csLiteral = newCharacterSequence(sequenceLiteralExpr.getValueExpr());
            value = newReference(csLiteral);
        }
        else {
            throw new SpaceX(
                evalContext.newRuntimeError("Can not yet handle sequence literal of type " + sequenceLiteralExpr));
        }
        log.debug("eval -> " + value);
        return value;
    }


    // ---------------------------- New Space Objects ------------------------
    private Space newSpace(Space spaceContext, SpaceTypeDefn firstSpaceTypeDefn) {
        Space rootSpace = getObjFactory().newSpace(spaceContext, firstSpaceTypeDefn);
        trackInstanceObject(rootSpace);
        return rootSpace;
    }

    private Tuple newTuple(TupleDefn defn) {
        Tuple tuple = getObjFactory().newTuple(defn);
        trackInstanceObject(tuple);
        return tuple;
    }

    private Reference newReference(SpaceObject toObject) {
        Reference reference = newReference(toObject.getOid());
//        trackInstanceObject(reference);
        return reference;
    }

    private Reference newReference(SpaceOid toOid) {
        return getObjFactory().newObjectReference(null, toOid);
    }

    CharacterSequence newCharacterSequence(String stringValue) {
        CharacterSequence newCs = getObjFactory().newCharacterSequence(stringValue);
        trackInstanceObject(newCs);
        return newCs;
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

    private void dumpAsts() {
        for (Schema schema : dirChain) {
            String dump = AstUtils.print(schema);
            log.debug("AST Root /: " + dump);
            Logger.getLogger("dumpFile").info(dump);
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

        RuntimeError newRuntimeError(String msg) {
            return Executor.this.newRuntimeError(msg);
        }
    }
}
