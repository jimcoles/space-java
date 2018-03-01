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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * <p>
 * The top-level executive context for loading Space meta objects and running a
 * Space program.  Holds and manages instance level objects, call stack, etc..
 * <p>
 * An Executor manages interactions between second-level
 * elements including XmlLoader, ExprProcessor, Querier.
 * <p>
 * The general pattern is:
 * <ul>
 * <li>eval() methods handle executable bits like the program itself and statements.</li>
 * <li>eval() methods handle expression</li>
 * </ul>
 * </p>
 *
 * @author J. Coles
 * @version 1.0
 */
public class Executor extends ExprProcessor {

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

    private Stack<FunctionCall> callStack = new Stack<>();

    /**
     * TODO: Use specialized expression processors for each type of expression, e.g.,
     * unary int, binary int, unary string, binary string, etc.
     * <p>
     * Mapping from expression type to expression handler. Some handlers will be
     * Space standard, some will be user-provided.
     */
    private Map<String, ExprProcessor> exprProcessors = null;
    public static SpaceTypeDefn MATH_TYPE_DEF;
    public static SpaceTypeDefn CHAR_SEQ_TYPE_DEF;
    public static SpaceTypeDefn space_opers_type_def;
    public static SpaceTypeDefn op_sys_type_def;

    public Executor() {
//        userAstFactory.newProgram(new NativeSourceInfo(null), "root");
        initRuntime();
        loadNativeMetaObjects();
    }

    private void initRuntime() {
        AstFactory astFactory = new AstFactory();
        langRootSchema = astFactory.newAstSchema(new CodeSourceInfo(), "lang");

        dirChain.add(langRootSchema);
        trackMetaObject(langRootSchema);
    }

    public void run(String... filePath) throws RuntimeException {
        File file = FileUtils.getFile(filePath);
        if (!file.exists()) {
            throw new RuntimeException("Input file [" + filePath + "] does not exist.");
        }
        run(file);
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
            throw new RuntimeException("can not find or load source loader [" + parserImplClassName + "]", e);
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
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException("Failed running program", ex);
        }
    }

    private void dumpAsts() {
        for (Schema schema : dirChain) {
            log.debug("AST Root /: " + AstUtils.print(schema));
        }
    }

    public void linkAndExec(List<RuntimeError> errors, Schema runSchema) throws Exception {
        log.debug("linking loaded program");
        errors = linkRefs(errors, langRootSchema);
        errors = linkRefs(errors, runSchema);
        if (errors.size() == 0) {
            log.info("no linker errors");
            exec(runSchema);
        }
        else {
            log.warn("Found " + errors.size() + " linker errors: " + JavaHelper.EOL + Strings.buildNewlineList(errors));
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
                        NamedElement refElem =
                            AstUtils.lookupLenientMetaObject(dirChain, reference.getLexicalContext(),
                                                             reference.getSpacePathExpr()
                            );
                        if (refElem == null) {
                            errors.add(
                                new RuntimeError(reference.getSpacePathExpr().getSourceInfo(), 0,
                                                 "can not resolve symbol '" +
                                                     reference.getSpacePathExpr().getFullPath() + "'"));
                        }
                        else {
                            reference.setResolvedMetaObj(refElem);
                            reference.setState(LoadState.RESOLVED);
                            log.debug("resolved ref [" + reference + "]");
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
        CHAR_SEQ_TYPE_DEF = loadNativeClass(JnCharSequence.class, AstUtils.getLangRoot(dirChain));
        op_sys_type_def = loadNativeClass(JnOpSys.class, AstUtils.getLangRoot(dirChain));
        MATH_TYPE_DEF = loadNativeClass(JnMath.class, AstUtils.getLangRoot(dirChain));
        space_opers_type_def = loadNativeClass(JnSpaceOpers.class, AstUtils.getLangRoot(dirChain));

        OPER_NAV = lookupOperator("nav");
        OPER_NEW_TUPLE = lookupOperator("newTuple");
        OPER_NEW_CHAR = lookupOperator("newCharSequence");
        OPER_ASSIGN = lookupOperator("assign");
    }

    private SpaceTypeDefn loadNativeClass(Class jnClass, Schema rootAstSchema) {
        AstFactory astFactory = new AstFactory();
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
                astFactory.newSpaceTypeDefn(jMethodInfo, "_methodSig_" + jMethod.getName())
            );
            //
            spaceTypeDefn.getBody().addFunctionDefn(functionDefnAST);
            trackMetaObject(functionDefnAST);
            //
            functionDefnAST.getArgSpaceTypeDefn().setBody(astFactory.newTypeDefnBody(jMethodInfo));
            //
            Parameter[] jParameters = jMethod.getParameters();
            for (Parameter jParam : jParameters) {
                PrimitiveType spcPrim = javaToSpace(jParam);
                if (spcPrim == PrimitiveType.TEXT) {
                    NativeSourceInfo jParamInfo = new NativeSourceInfo(jParam);
                    functionDefnAST.getArgSpaceTypeDefn().addAssocDefn(
                        astFactory.newAssociationDefn(jParamInfo, jParam.getName(),
                                                      astFactory.newSpacePathExpr(jParamInfo, null,
                                                                                  CHAR_SEQ_TYPE_DEF.getName(),
                                                                                  null)));
                }
                else {
                    functionDefnAST.getArgSpaceTypeDefn().addVariable(
                        astFactory.newVariableDefn(new CodeSourceInfo(), jParam.getName(), spcPrim));
                }
            }
        }
        return spaceTypeDefn;
    }

    private PrimitiveType javaToSpace(Parameter jParam) {
        PrimitiveType spcPrimType = PrimitiveType.TEXT;
        Class<?> jType = jParam.getType();
        if (jType == String.class) {
            spcPrimType = PrimitiveType.TEXT;
        }
        else if (jType == Boolean.TYPE) {
            spcPrimType = PrimitiveType.BOOLEAN;
        }
        else if (jType == Integer.TYPE) {
            spcPrimType = PrimitiveType.CARD;
        }
        else if (jType == Float.TYPE) {
            spcPrimType = PrimitiveType.REAL;
        }
        return spcPrimType;
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
            throw new RuntimeException("space lang object not found with path [" + operSimpleName + "]");
        return operActionDefn;
    }

    private String toSpaceTypeName(Class spaceJavaWrapperClass) {
        return spaceJavaWrapperClass.getSimpleName().substring(2);
    }

    private void dumpSymbolTables() {
        log.debug("normalized meta object table: " + JavaHelper.EOL
                      + Strings.buildNewlineList(metaObjectNormalTable));
//        log.debug("dump namespace meta object index: " + JavaHelper.EOL
//                + Strings.buildNewlineList(metaObjectExtendedInfoMap.values()));
    }

    // ------------------------- Execs ------------------------

    /**
     * Executes a Schema.
     */
    public ModelElement exec(Schema programSchema) throws Exception {
        log.debug("eval: " + programSchema);
        SpaceTypeDefn firstSpaceTypeDefn = programSchema.getFirstSpaceDefn();
        Tuple ctxObject = newTuple(firstSpaceTypeDefn);
        FunctionDefn spMainActionDefn = (FunctionDefn) firstSpaceTypeDefn.getBody().getFunction("main");
//        List<SpaceObject> objectHeap = programSchema.getObjectHeap();
//        for (SpaceObject spaceObj : objectHeap) {
//            trackInstanceObject(spaceObj);
//        }

        try {
            eval(ctxObject, firstSpaceTypeDefn.getBody().getInitBlock());
            eval(ctxObject, spMainActionDefn, newTuple(spMainActionDefn.getArgSpaceTypeDefn()));
        } catch (RuntimeException ex) {
            log.error("error executing", ex);
        }
        log.debug("exiting Space program execution");
        return null;
    }

    private Assignable eval(Tuple spcContext, AbstractFunctionDefn function, Tuple argTuple)
        throws RuntimeException
    {
        log.debug("delegate eval: " + function);
        Assignable value = null;

        if (function instanceof FunctionDefn)
            value = eval(spcContext, (FunctionDefn) function, argTuple);
        else if (function instanceof NativeFunctionDefn)
            value = eval(spcContext, (NativeFunctionDefn) function, argTuple);

        return value;
    }

    /**
     * Execution of FunctionDefn's is the core work of the executor.
     * The executor must recurse into a depth-first evaluation of nested actions,
     * resolve all coordinate references, resolve operator calls, push and pop the call stack,
     * and move values (spaces) from action execution into the proper space for
     * parent actions.
     *
     * @param ctxObject    Might be a local function space or an argument call space.
     * @param spcActionDefn The composite, imperative action defined via Space source code (i.e., not Native).
     */
    private Assignable eval(Tuple ctxObject, FunctionDefn spcActionDefn, Tuple argTuple) throws RuntimeException {
        log.debug("eval: " + spcActionDefn);
        Assignable value = null;
        log.debug("eval: " + spcActionDefn);
        FunctionCall functionCall = getObjBuilder().newFunctionCall(ctxObject, spcActionDefn, argTuple);
        callStack.push(functionCall);
        StatementBlock statementBlock = spcActionDefn.getStatementBlock();
        eval(ctxObject, statementBlock);
//        functionCall.setReturnSpace(cal);
        callStack.pop();
        log.debug("popped call stack. size [" + callStack.size() + "]");
        value = functionCall.getReturnValue();
        return value;
    }

    private Assignable eval(Tuple spcContext, StatementBlock statementBlock) {
        log.debug("eval: " + statementBlock);
        Assignable value = null;
        List<Statement> statementSequence = statementBlock.getStatementSequence();
        for (Statement statement : statementSequence) {
            if (statement instanceof ExprStatement) {
                ExprStatement exprHolderStatement = (ExprStatement) statement;
                eval(spcContext, exprHolderStatement.getExpression());
            }
            else if (statement instanceof IfStatement) {
                // TODO
            }
            else if (statement instanceof ForEachStatement) {
                // TODO
            }
            else if (statement instanceof StatementBlock) {
                eval(spcContext, ((StatementBlock) statement));
            }

        }
        return value;
    }

    private Assignable eval(Tuple spcContext, ValueExpr expression) {
        log.debug("eval: " + expression);
        Assignable value = null;
        if (expression instanceof FunctionCallExpr) {
            value = eval(spcContext, (FunctionCallExpr) expression);
        }
        else if (expression instanceof AssignmentExpr) {
            value = eval(spcContext, (AssignmentExpr) expression);
        }
        else if (expression instanceof LiteralExpr) {
            value = eval(spcContext, (LiteralExpr) expression);
        }
        else if (expression instanceof MetaReference) {
            value = eval(spcContext, ((MetaReference) expression));
        }
        else
            throw new RuntimeException("don't know how to evaluate " + expression);

        return value;
    }

    /**
     * Invokes a Java native method all via Java reflection.  Native actions
     * do not have nested actions, at least none that are executed or controlled
     * by this executor.
     */
    private Assignable eval(Tuple ctxObject, NativeFunctionDefn nativeFunctionDefn, Tuple argTuple) {
        log.debug("eval: " + nativeFunctionDefn);
        Assignable value = null;
        try {
            Object[] jArgs = new Object[argTuple.getSize()];
            for (int idxArg = 0; idxArg < argTuple.getValuesHolders().size(); idxArg++) {
                Assignable spcArgValue = argTuple.getValuesHolders().get(idxArg);
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
            }
            Object jObjectDummy = nativeFunctionDefn.getjMethod().getDeclaringClass().newInstance();
            nativeFunctionDefn.getjMethod().invoke(jObjectDummy, jArgs);
        } catch (InstantiationException e) {
            log.error(e);
        } catch (IllegalAccessException e) {
            log.error(e);
        } catch (InvocationTargetException e) {
            log.error(e);
        }
        return value;
    }

    /**
     * The central eval method of this executor.
     * An {@link FunctionCallExpr} is a reference to a {@link FunctionDefn} along with the argument
     * space for the call. In general, the arguments themselves are value expressions that must be
     * evaluated, recursively.
     *
     * Conceptually, a Function call is the same as a 'join' operation in RDB parlance.
     *
     * @param ctxObj  The space object against which this function is being invoked.
     * @param spcCallExpr The action-invoking expression to be evaluated (not the instance-level object).
     *                    Can be a Space function call or a Native (Java) function call.
     */
    private Assignable eval(Tuple ctxObj, FunctionCallExpr spcCallExpr) throws RuntimeException {
        log.debug("eval: call to " + spcCallExpr.getFunctionRef().getSpacePathExpr().getFullPath());
        Assignable value = null;
        //
        AbstractFunctionDefn targetFunctionDefn = spcCallExpr.getFunctionRef().getResolvedMetaObj();

        if (targetFunctionDefn == null)
            throw new RuntimeException("unresolved function ref " + spcCallExpr.getFunctionRef());

        // must add a nested Space context beneath the incoming context space to
        // represent the call stack
        Tuple argTuple = newTuple(spcCallExpr.getFunctionRef().getResolvedMetaObj().getArgSpaceTypeDefn());

        // assignments may be by name or by order (like a SQL update statement)
        ValueExpr[] argumentExprs = spcCallExpr.getArgumentExprs();
        for (int idxArg = 0; idxArg < argumentExprs.length; idxArg++) {
            ValueExpr argumentExpr = argumentExprs[idxArg];
            Assignable argValue = eval(ctxObj, argumentExpr);
            argValue = castRightSideAsNeeded(argTuple.getDefn().getAllMembers().get(idxArg), argValue);
            argTuple.setValueAt(idxArg, argValue);
        }
        value = eval(ctxObj, targetFunctionDefn, argTuple);
        //
        return value;
    }

    private Assignable eval(Tuple ctxObject, MetaReference<?> metaRef) {
        log.debug("eval: " + metaRef + " wrt " + ctxObject);
        return ctxObject.get(metaRef.getResolvedMetaObj());
    }

    private Assignable eval(Tuple ctxObject, AssignmentExpr assignmentExpr) {
        NamedElement leftSideDefnObj = assignmentExpr.getMemberRef().getResolvedMetaObj();
        Assignable rightSideValue = eval(ctxObject, assignmentExpr.getValueExpr());
        rightSideValue = castRightSideAsNeeded(leftSideDefnObj, rightSideValue);
        Assignable leftSideHolder = SpaceUtils.assignOper(ctxObject, leftSideDefnObj, rightSideValue);
        log.debug("eval: resulting assigned value: " + leftSideHolder);
        return leftSideHolder;
    }

    private Assignable castRightSideAsNeeded(NamedElement leftSideDefnObj, Assignable rightSideValue) {
        if ( leftSideDefnObj instanceof AssociationDefn &&
            ((AssociationDefn) leftSideDefnObj).getToType().getOid().equals(Executor.CHAR_SEQ_TYPE_DEF.getOid())
             && !(rightSideValue instanceof Reference) )
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
//    private SpaceObject eval(Space userSpaceContext, SpacePathExpr pathExpr) {
//        SpaceObject value = null;
//
//        return value;
//    }

    /**
     * Returns the literal value object associated with the {@link LiteralExpr}.
     * The returned object may be a scalar, a character sequence, or a complete
     * space.
     */
    private Assignable eval(Tuple spcContext, LiteralExpr literalExpr) {
        log.debug("eval: " + literalExpr);
        Assignable value = null;
        switch (literalExpr.getPrimitiveType()) {
            case TEXT:
                CharacterSequence csLiteral = newCharacterSequence(literalExpr.getValueExpr());
                value = newReference(csLiteral);
                break;
            case CARD:
                value = getObjBuilder().newCardinalValue(new Integer(literalExpr.getValueExpr()));
                break;
            case BOOLEAN:
                value = getObjBuilder().newBooleanValue(Boolean.valueOf(literalExpr.getValueExpr()));
                break;
            case CHAR:
            case REAL:
            case RATIONAL:
            default:
                throw new RuntimeException("Can not yet handle literals other than Java Strings");
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

    // -------------- Tracking of runtime/instance things ---------------

    private void trackMetaObject(ModelElement modelElement) {
        // add to normalized object table ...
        metaObjectNormalTable.add(modelElement);
//        metaObjectIndexByFullPath.put(modelElement.getFullPath(), modelElement);

        // track denormalized info for fast lookup ...
//        if (modelElement instanceof NamedElement) {
//            MetaInfo metaInfo = new MetaInfo((NamedElement) modelElement);
//            metaObjectExtendedInfoMap.put((NamedElement) modelElement, metaInfo);
//
//            // if this object has a parent, add this object to parent's child map
//            if (modelElement.hasParent()) {
//                metaObjectExtendedInfoMap.get(modelElement.getParent()).addChild((NamedElement) modelElement);
//            }
//        }
    }

    private void trackInstanceObject(SpaceObject spaceObject) {
        objectTable.addObject(spaceObject);
    }

    public SpaceObject dereference(SpaceOid referenceOid) throws RuntimeException {
        SpaceObject spaceObject = objectTable.getObjectByOid(referenceOid);
        if (spaceObject == null)
            throw new RuntimeException("Space Oid [" + referenceOid + "] not found.");
        return spaceObject;
    }
}
