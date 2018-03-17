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

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.jkcsoft.java.util.JavaHelper;
import org.jkcsoft.java.util.Lister;
import org.jkcsoft.java.util.Strings;
import org.jkcsoft.space.lang.ast.*;
import org.jkcsoft.space.lang.instance.*;
import org.jkcsoft.space.lang.loader.AstLoader;
import org.jkcsoft.space.lang.runtime.impl.CastTransforms;
import org.jkcsoft.space.lang.runtime.jnative.JnCharSequence;
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
        catch (SpaceX e) {
            PrintStream ps = System.err;
            StringBuffer sb = new StringBuffer();
            ps.println("error: " + e.getMessage());
            if (e.getError() != null) {
                sb.append(e.getError().getMessage());
                if (e.getError().getSpaceTrace() != null) {
                    sb.append(
                        Strings.buildDelList(e.getError().getSpaceTrace(),
                                             new Lister() {
                                                 @Override
                                                 public String getListString(Object o) {
                                                     return ((FunctionCallContext) o).toString();
                                                 }
                                             },
                                             JavaHelper.EOL)
                    );
                }
            }
            if (e.getCause() != null) {
//                log.error("", e.getCause());
                e.getCause().printStackTrace(System.err);
            }
            ps.println(sb.toString());
        }
    }

    public void run(File file) {
        AstLoader astLoader;
        String parserImplClassName = "org.jkcsoft.space.antlr.loaders.G2AntlrParser";
//            String parserImplClassName = "org.jkcsoft.space.antlr.test.TestGrammarParser";
        try {
            Class<?> aClass = Class.forName(parserImplClassName);
            log.debug(String.format("Found loader provider class [%s]", parserImplClassName));
            astLoader = (AstLoader) aClass.newInstance();
            log.debug(String.format("Found source loader [%s]", astLoader.getName()));
        } catch (Exception e) {
            throw new SpaceX("can not find or load source loader [" + parserImplClassName + "]", e);
        }
        List<RuntimeError> errors = new LinkedList<>();
        try {
            Schema thisSchema = astLoader.load(file);
            dirChain.add(thisSchema);

            trackMetaObject(thisSchema);

            if (log.isDebugEnabled())
                dumpSymbolTables();

            if (log.isDebugEnabled())
                dumpAsts();

            linkAndExec(errors, thisSchema);
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

    private void dumpAsts() {
        for (Schema schema : dirChain) {
            log.debug("AST Root /: " + AstUtils.print(schema));
        }
    }

    public void linkAndExec(List<RuntimeError> errors, Schema runSchema) throws Exception {
        link(errors, runSchema);
        if (errors.size() == 0) {
            exec(runSchema);
        }
        else {
            log.warn("skipping exec due to link errors.");
        }
    }

    private void link(List<RuntimeError> errors, Schema runSchema) throws Exception {
        log.debug("linking loaded program");
        errors = linkRefs(errors, langRootSchema);
        errors = linkRefs(errors, runSchema);
        if (errors.size() == 0) {
            log.info("no linker errors");
        }
        else {
            String redMsg = "Found [" + errors.size() + "] linker errors";
            log.warn(redMsg + ". See error stream.");
            System.err.println(redMsg + ":");
            System.err.println(Strings.buildNewlineList(errors));
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
            log.debug("linking refs held by [" + astNode + "]");
            Set<MetaReference> references = astNode.getReferences();
            if (references != null) {
                for (MetaReference reference : references) {
                    if (reference.getState() == LoadState.INITIALIZED) {
                        log.debug("resolving reference [" + reference + "]");
                        NamedElement lexParent = AstUtils.getLexParent(astNode);
                        reference.setLexicalContext(lexParent);
                        log.debug("found lexical parent [" + lexParent + "]");
                        NamedElement refElem = AstUtils.lookupLenientMetaObject(dirChain, reference.getLexicalContext(),
                                                                                reference.getSpacePathExpr());
                        if (refElem == null) {
                            errors.add(
                                new RuntimeError(reference.getSpacePathExpr().getSourceInfo(), 0,
                                                 "can not resolve symbol '" +
                                                     reference.getSpacePathExpr().getText() + "'"));
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

    private ObjectFactory getObjBuilder() {
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
        NamedElement opersSpaceDefn = AstUtils.lookupMetaObject(langRootSchema, toSpaceTypeName(JnSpaceOpers.class));
        AbstractFunctionDefn operActionDefn =
            (AbstractFunctionDefn) AstUtils.lookupMetaObject(opersSpaceDefn, operSimpleName);
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
    //

    /**
     * The entry point for 'running a program': exec the 'main' function.
     */
    public ModelElement exec(Schema programSchema) {
        log.debug("exec: " + programSchema);
        SpaceTypeDefn bootTypeDefn = programSchema.getFirstSpaceDefn();
        Tuple mainTypeTuple = newTuple(bootTypeDefn);
        EvalContext evalContext = new EvalContext();
        FunctionDefn spMainActionDefn = bootTypeDefn.getBody().getFunction("main");
        FunctionCallContext dummyCtxt =
            getObjBuilder().newFunctionCall(mainTypeTuple, null, null);
        // init the tuple
        callStack.push(dummyCtxt);
        exec(evalContext, bootTypeDefn.getBody().getInitBlock());
        callStack.pop();
        // call the 'main' function a dummy function call expr
        AstFactory tmpAstFactory = getAstFactory();
        FunctionCallExpr mainFunctionCallExpr = tmpAstFactory.newFunctionCallExpr(new ProgSourceInfo());
        mainFunctionCallExpr
            .setFunctionDefnRef(tmpAstFactory.newSpacePathExpr(new IntrinsicSourceInfo(), null, "main", null));
        mainFunctionCallExpr.getFunctionDefnRef().setResolvedMetaObj(spMainActionDefn);
        mainFunctionCallExpr.getFunctionDefnRef().setState(LoadState.RESOLVED);

        eval(evalContext, mainFunctionCallExpr);
        log.debug("exiting Space program execution");
        return null;
    }

    /**
     * TODO I think we'll eventually want all statements to get put on an Action Queue
     * or Transaction Queue or such.
     */
    private void exec(EvalContext evalContext, StatementBlock statementBlock) {
        log.debug("exec: " + statementBlock);
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
                if (evalContext.peekStack().isPendingReturn())
                    return;
            }
            else if (statement instanceof ReturnExpr) {
                Assignable retVal = eval(evalContext, ((ReturnExpr) statement).getValueExpr());
                SpaceUtils.assignOper(evalContext, evalContext.peekStack().getReturnValue(), retVal);
                evalContext.peekStack().setPendingReturn(true);
            }
        }
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
        else
            throw new SpaceX("don't know how to evaluate " + expression);

        return value;
    }

    private Assignable eval(EvalContext evalContext, FunctionCallExpr functionCallExpr) {
        log.debug("eval: " + functionCallExpr);
        Assignable value = null;

        // Eval the argument expressions to specific values
        List<SpaceObject> functionPath = eval(evalContext, functionCallExpr.getFunctionDefnRef().getSpacePathExpr());
        Tuple argTuple =
            newTuple(((FunctionDefn) functionCallExpr.getFunctionDefnRef().getResolvedMetaObj()).getArgSpaceTypeDefn());

        // Arg assignments may be by name or by order (like a SQL update statement), but not both.
        List<ValueExpr> argumentExprs = functionCallExpr.getArgumentExprs();
        int idxArg = 0;
        for (ValueExpr argumentExpr : argumentExprs) {
            Assignable argValue = eval(evalContext, argumentExpr);
            argValue = castRightSideAsNeeded(argTuple.getDefn().getAllMembers().get(idxArg), argValue);
            Assignable leftSideHolder = argTuple.get(argTuple.getNthMember(idxArg));
            SpaceUtils.assignOper(evalContext, leftSideHolder, argValue);
            idxArg++;
        }

        Tuple callTargetCtxObject = (Tuple) functionPath.get(functionPath.size() - 2);  // 2nd to last
        FunctionCallContext functionCallContext =
            getObjBuilder().newFunctionCall(callTargetCtxObject, functionCallExpr, argTuple);

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

        return value;
    }

    private List<SpaceObject> eval(EvalContext evalContext, SpacePathExpr spacePathExpr) {
        return null;
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
            NativeFunctionDefn funcDef = (NativeFunctionDefn) functionCallExpr.getDefn();
            Object jObjectDummy =
                funcDef.getjMethod().getDeclaringClass().newInstance();
            Object jValue = funcDef.getjMethod().invoke(jObjectDummy, jArgs);
        } catch (Exception e) {
            log.error(e);
            throw new SpaceX(newRuntimeError("error invoking native method " + e.getMessage()));
        }
        return value;
    }

    /*
     Must generalize this exec to find values in all possible locations:
      - call args
       - context object (tuple)
        - static objects
         */
    private Assignable eval(EvalContext evalContext, MetaReference<?> metaRef) {
        log.debug("eval: " + metaRef);
        NamedElement toMetaObj = metaRef.getResolvedMetaObj();
        return evalContext.peekStack().getCtxObject().get(toMetaObj);
    }

    private Assignable eval(EvalContext evalContext, AssignmentExpr assignmentExpr) {
        log.debug("eval: " + assignmentExpr);
        NamedElement leftSideDefnObj = assignmentExpr.getMemberRef().getResolvedMetaObj();
        Assignable rightSideValue = eval(evalContext, assignmentExpr.getValueExpr());
        rightSideValue = castRightSideAsNeeded(leftSideDefnObj, rightSideValue);
        // TODO Add some notion of 'casting' and 'auto-(un)boxing'.
        Assignable leftSideHolder = evalContext.peekStack().getCtxObject().get(leftSideDefnObj);
        SpaceUtils.assignOper(evalContext, leftSideHolder, rightSideValue);
        log.debug("eval: resulting assigned value >> " + leftSideHolder);
        return leftSideHolder;
    }

    private Assignable castRightSideAsNeeded(NamedElement leftSideDefnObj, Assignable rightSideValue) {
        if (leftSideDefnObj instanceof AssociationDefn &&
            ((AssociationDefn) leftSideDefnObj).getToType().getOid().equals(Executor.CHAR_SEQ_TYPE_DEF.getOid())
            && !(rightSideValue instanceof Reference))
        {
            if (rightSideValue instanceof ScalarValue) {
                CharacterSequence characterSequence =
                    newCharacterSequence(((ScalarValue) rightSideValue).getValue().toString());
                rightSideValue = newReference(characterSequence);
                log.debug("cast scalar value to CharSequence");
            }
            else if (rightSideValue instanceof Variable) {
                CharacterSequence characterSequence =
                    newCharacterSequence(((Variable) rightSideValue).getScalarValue().getValue().toString());
                rightSideValue = newReference(characterSequence);
                log.debug("cast scalar value to CharSequence");
            }
        }
        return rightSideValue;
    }

    /**
     * Returns the user-space object associated with the {@link SpacePathExpr}.
     * If pathExpr is relative (versus absolute), the expression is evaluated with respect to
     * userSpaceContext. May return a single scalar, a tuple, or a space with multiple
     * tuples, depending on the expression. */
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
            value = getObjBuilder().newCardinalValue(new Integer(primitiveLiteralExpr.getValueExpr()));
        }
        else if (primitiveLiteralExpr.getTypeDefn() == PrimitiveTypeDefn.BOOLEAN) {
            value = getObjBuilder().newBooleanValue(Boolean.valueOf(primitiveLiteralExpr.getValueExpr()));
        }
        else if (primitiveLiteralExpr.getTypeDefn() == PrimitiveTypeDefn.CHAR) {

        }
        else if (primitiveLiteralExpr.getTypeDefn() == PrimitiveTypeDefn.REAL) {

        }
//       else if (primitiveLiteralExpr.getTypeDefn() ==  RATIONAL) {
//       }
        else {
            throw new SpaceX("Can not yet handle literal of type " + primitiveLiteralExpr.getTypeDefn());
        }
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
            throw new SpaceX("Can not yet handle literal of type " + sequenceLiteralExpr);
        }
        return value;
    }

    // ---------------------------- New Space Objects ------------------------

    private Space newSpace(Space spaceContext, SpaceTypeDefn firstSpaceTypeDefn) {
        Space rootSpace = getObjBuilder().newSpace(spaceContext, firstSpaceTypeDefn);
        trackInstanceObject(rootSpace);
        return rootSpace;
    }

    private Tuple newTuple(SpaceTypeDefn defn) {
        Tuple tuple = getObjBuilder().newTuple(defn);
        trackInstanceObject(tuple);
        return tuple;
    }

    private Reference newReference(SpaceObject toObject) {
        Reference reference = newReference(toObject.getOid());
//        trackInstanceObject(reference);
        return reference;
    }

    private Reference newReference(SpaceOid toOid) {
        return getObjBuilder().newObjectReference(null, toOid);
    }

    CharacterSequence newCharacterSequence(String stringValue) {
        CharacterSequence newCs = getObjBuilder().newCharacterSequence(stringValue);
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

    /**
     * Everything needed by an exec( ) method to resolve values in the lexical
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
