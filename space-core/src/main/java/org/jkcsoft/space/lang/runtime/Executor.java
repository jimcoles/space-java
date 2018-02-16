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
import org.apache.log4j.Logger;
import org.jkcsoft.java.util.Strings;
import org.jkcsoft.space.lang.ast.*;
import org.jkcsoft.space.lang.instance.*;
import org.jkcsoft.space.lang.loader.AstLoader;
import org.jkcsoft.space.lang.runtime.impl.CastTransforms;
import org.jkcsoft.space.lang.runtime.jnative.math.JnMath;
import org.jkcsoft.space.lang.runtime.jnative.opsys.JnOpSys;
import org.jkcsoft.space.lang.runtime.jnative.space.SpaceOpers;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * The top-level executive for Space.  It manages interactions between second-level
 * elements including XmlLoader, ExprProcessor, Querier.
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

    public void run(String ... filePath) throws RuntimeException {
        File file = FileUtils.getFile(filePath);
        if (!file.exists()) {
            throw new RuntimeException("Input file [" + filePath + "] does not exist.");
        }
        run(file);
    }

    public void run(File file) {
        AstLoader astLoader;
        try {
            String parserImplClassName = "org.jkcsoft.space.lang.runtime.loaders.antlr.g2.G2AntlrParser";
            Class<?> aClass = Class.forName(parserImplClassName);
            astLoader = (AstLoader) aClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Could not load parser [{0}]", e);
        }
        try {
            AstFactory astFactory = astLoader.load(file);
            if (log.isDebugEnabled())
                log.debug("AST dump: " + astFactory.print());
            linkAndExec(astFactory.getAstRoot());
        }
        catch (Exception ex) {
            throw new RuntimeException("Failed running program", ex);
        }
    }

    public void linkAndExec(SpaceProgram sprog) throws Exception {
        linkRefs(sprog);
        exec(sprog);
    }

    /*
     Augments named references to oid-based references after all meta objects
     have been loaded.  Has the effect of doing semantic validation.
     Could also do this while loading.
     TODO implement linker
     */
    private void linkRefs(ModelElement astRoot) {
        log.debug(astRoot.getName());
        List<ModelElement> children = astRoot.getChildren();
        for (ModelElement child : children) {
            linkRefs(child);
        }
    }


    /** Definition objects are loaded as we parse the source code.  Some objects are loaded
     *  with static logic.  These are the intrinsic, native objects.
     */
    private AstFactory rootModelBuilder = new AstFactory();
    private Set<ModelElement>           spaceModelObjects = new HashSet<>();
    private Map<String, ModelElement>   indexModelObjectsByFullPath = new TreeMap<>();
    /** The 'object' tables for 'instance' objects associated with the running program.
     *  This map is added to as the program runs.
     */
    private Map<SpaceOid, SpaceObject>  indexObjectsByOid = new TreeMap<>();
    /**
     * Key=the referenced object's Oid.
     * Value=the set of object Oids holding reference to the key Oid.
     */
    private Map<SpaceOid, Set<SpaceOid>> objectReferenceMap = new TreeMap<>();

    private Stack<ActionCall>           callStack = new Stack<>();


    public AbstractActionDefn OPER_NAV;
    public AbstractActionDefn OPER_NEW_TUPLE;
    public AbstractActionDefn OPER_NEW_CHAR;
    public AbstractActionDefn OPER_ASSIGN;

    /**
     * Mapping from expression type to expression handler. Some handlers will be
     * Space standard, some will be user-provided.
     */
    private Map _exprProcessors = null;

    public Executor() {
        rootModelBuilder.initProgram("");
        loadNativeSpaces();
    }

    private ObjectFactory getObjBuilder() {
        return ObjectFactory.getInstance();
    }

    private void loadNativeSpaces() {
        // use Java annotations Java-native objects
//        SpaceNative.class.getAnnotations();
        loadNativeClass(JnOpSys.class);
        loadNativeClass(JnMath.class);
        loadNativeClass(SpaceOpers.class);
        dumpSymbolTables();

        OPER_NAV = findOper("nav");
        OPER_NEW_TUPLE = findOper("newTuple");
        OPER_NEW_CHAR = findOper("newCharSequence");
        OPER_ASSIGN = findOper("assign");
    }

    private void loadNativeClass(Class jnClass) {
        SpaceTypeDefn spaceTypeDefn = rootModelBuilder.newSpaceTypeDefn(jnClass.getSimpleName());
//        spaceDefn.setName(jnClass.getSimpleName());
        Method[] methods = jnClass.getMethods();
        for (Method jMethod: methods) {
            SpaceTypeDefn nativeArgSpaceTypeDefn = rootModelBuilder.newSpaceTypeDefn(null);
            jMethod.getParameters();    // TODO build dynamic arg space
            nativeArgSpaceTypeDefn.addVariable(rootModelBuilder.newVariableDefn("arg1", null));
            AbstractActionDefn actionDefn = spaceTypeDefn.addActionDefn(rootModelBuilder.newNativeActionDefn
                (jMethod.getName(), jMethod, nativeArgSpaceTypeDefn));
            trackMetaObject(actionDefn);
        }
        trackMetaObject(spaceTypeDefn);
    }

    private AbstractActionDefn findOper(String functionPath) {
        AbstractActionDefn operActionDefn = (AbstractActionDefn) indexModelObjectsByFullPath.get(functionPath);
        if (operActionDefn == null)
            throw new RuntimeException("operation not found with path ["+functionPath+"]");
        return operActionDefn;
    }

    /**
     * Returns the meta-space object associated with the pathExpr.
     * NOTE: Once we get our AST backed by Space object we can eliminate
     * this special method.
     */
    private ModelElement lookupMetaElement(ValueExpr pathExpr) {
        ModelElement modelElement = null;

        if (pathExpr instanceof MetaObjectRefLiteral) {
            modelElement = ((MetaObjectRefLiteral) pathExpr).getSpaceMetaObject();
        }

//        indexModelObjectsByFullPath.get(spacePath);
//        if (targetFunctionDefn == null)
//            throw new RuntimeException("Function defn [" + functionName + "] " +
//                                           "not found in Space Defn [" + spacePath + "]");
//        if (! (targetFunctionDefn instanceof Callable) )
//            throw new RuntimeException("Function defn [" + functionName + "] " +
//                                           "does not implement the ["+Callable.class.getName()+"] interface.");

        return modelElement;
    }

    private Object lookupMetaElement(SpacePathExpr functionPathExpr) {
        return null;
    }

    private void dumpSymbolTables() {
        log.debug("dump table of definition objects: " + Strings.buildCommaDelList(spaceModelObjects));
        log.debug("dump map of definition objects: " + Strings.buildCommaDelList(indexModelObjectsByFullPath.entrySet()));
    }

    // ------------------------- Execs ------------------------

    /**
     * Executes a SpaceProgram.
     */
    public ModelElement exec(SpaceProgram program) throws Exception {
        log.info("enter program exec for " + program.getName());
        SpaceTypeDefn firstSpaceTypeDefn = program.getFirstSpaceDefn();
        Space rootSpace = newSpace(null, firstSpaceTypeDefn);
        SpaceActionDefn spMainActionDefn = (SpaceActionDefn) firstSpaceTypeDefn.getFunction("main");
        List<SpaceObject> objectHeap = program.getObjectHeap();
        for (SpaceObject spaceObj:objectHeap) {
            trackSpaceObject(spaceObj);
        }

        try {
            exec(rootSpace, spMainActionDefn);
        }
        catch (RuntimeException ex) {
            log.error("error executing", ex);
        }
        log.info("exiting Space program execution");
        return null;
    }

    private void delegateExec(Space spcContext, AbstractActionDefn function) throws RuntimeException {
        log.debug("enter delegateExec: " + function);
        if (function instanceof SpaceActionDefn)
            exec(spcContext, (SpaceActionDefn) function);
        else if (function instanceof NativeActionDefn)
            exec(spcContext, (NativeActionDefn) function);
    }

    /**
     * Execution of SpaceActionDefn's is the core work of the executor.
     * The executor must recurse into a depth-first evaluation of nested actions,
     * resolve all coordinate references, resolve operator calls, push and pop the call stack,
     * and move values (spaces) from action execution into the proper space for
     * parent actions.
     *
     * @param spcContext Might be a local function space or an argument call space.
     * @param spcActionDefn The composite, imperative action defined via Space source code (i.e., not Native).
     */
    private Assignable exec(Space spcContext, SpaceActionDefn spcActionDefn) throws RuntimeException {
        ActionCall actionCall = getObjBuilder().newAction(spcContext, spcActionDefn);
        log.debug("push to call stack and exec: " + spcActionDefn);
        callStack.push(actionCall);
        List<ActionCallExpr> childActions = spcActionDefn.getNestedActions();
        for (ActionCallExpr childActionExpr: childActions) {
            Assignable callValueObject = eval(spcContext, childActionExpr);
        }
        callStack.pop();
        log.debug("popped call stack. size [" + callStack.size() + "]");
        return actionCall.getReturnSpace();
    }

    /** Invokes a Java native method all via Java reflection.  Native actions
     * do not have nested actions, at least none that are executed or controlled
     * by this executor. */
    private void exec(Space spcContext, NativeActionDefn nativeActionDefn) {
        try {
            log.debug("native Java call: " + nativeActionDefn);
            Assignable arg1 = spcContext.iterator().next().getAssignableAt(0);
            Object jArg1 = "?";
            if (arg1 instanceof Association)
                //
                jArg1 = dereference( ((Association) arg1).getReferenceOid()).toString();
            else {
                jArg1 = arg1.toString();
            }
            // TODO: 1/29/17 Remove hardcodings and improve casting generalization
            CastTransforms casters = new CastTransforms();
            if (jArg1 instanceof CharacterSequence)
                jArg1 = casters.charSequenceToString((CharacterSequence) jArg1);
            nativeActionDefn.getjMethod().invoke(new JnOpSys(), jArg1);
        }
        catch (IllegalAccessException e) {
            log.error(e);
        }
        catch (InvocationTargetException e) {
            log.error(e);
        }
    }

    /**
     * The central eval method of this executor.
     * An {@link ActionCallExpr} is a reference to a {@link SpaceActionDefn} along with the argument
     * space for the call. In general, the arguments themselves are value expressions that must be
     * evaluated, recursively.
     *
     * @param spcContext The argument space (instance-level with values accessible for read/write).
     * @param spcCallExpr The action-invoking expression to be evaluated (not the instance-level object).
     *                    Can be a Space function call or a Native (Java) function call.
     */
    private Assignable eval(Space spcContext, ActionCallExpr spcCallExpr) throws RuntimeException {
        log.debug("function call: " + spcCallExpr);
        Assignable value = null;
        //
//        Namespace fullNs = new Namespace(spcCallExpr.getFunctionPathExpr());
//        String spacePath = fullNs.subPath(0, fullNs.getSize() - 2);
//        SpaceDefn targetSpaceDefn = lookupMetaElement(spcCallExpr.getFunctionPathExpr());
//        if (targetSpaceDefn == null)
//            throw new RuntimeException("Space defn [" + spcCallExpr.getFunctionPathExpr() + "] not found");
//        String functionName = fullNs.getLast();
        AbstractActionDefn targetFunctionDefn
            = (AbstractActionDefn) lookupMetaElement(spcCallExpr.getFunctionPathExpr());

        // must add a nested Space context beneath the incoming context space to
        // represent the call stack
        Space argSpace = newSpace(spcContext, ((Callable) targetFunctionDefn).getArgSpaceTypeDefn());
        Tuple argTuple = newTuple(argSpace);
        argSpace.addTuple(argTuple);

        // assignments may be by name or by order (like a SQL update statement)
        ValueExpr[] argumentExprs = spcCallExpr.getArgumentExprs();
        for (int idxArg = 0; idxArg < argumentExprs.length; idxArg++) {

            ValueExpr argumentExpr = argumentExprs[idxArg];
            if (argumentExpr instanceof LiteralExpr) {
                Assignable literalValue = eval(argSpace, (LiteralExpr) argumentExpr);
                addValueOrRef(argTuple, idxArg, literalValue);
            }
            else if (argumentExpr instanceof  ActionCallExpr) {
                Assignable actionArgValue = eval(argSpace, (ActionCallExpr) argumentExpr);
                addValueOrRef(argTuple, idxArg, actionArgValue);
            }
            else {
                throw new RuntimeException("Space: Invalid argument expression []");
            }
        }
        delegateExec(argSpace, targetFunctionDefn);
        //
        return value;
    }

    private void addValueOrRef(Tuple argTuple, int idxArg, Assignable literalValue) {
        argTuple.setValue(idxArg, literalValue);
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

    /** Returns the literal value object associated with the {@link LiteralExpr}.
     * The returned object may be a scalar, a character sequence, or a complete
     * space. */
    private Assignable eval(Space spcContext, LiteralExpr literalExpr) {
        Assignable value = null;
        if (literalExpr.isString()) {
            CharacterSequence arg1 = newCharacterSequence(literalExpr.getAsString());
            value = newAssociation(arg1);
        }
        else {
            throw new RuntimeException("Can not yet handle literals other than Java Strings");
        }
        return value;
    }

    // ---------------------------- New Space Objects ------------------------

    private Space newSpace(Space spaceContext, SpaceTypeDefn firstSpaceTypeDefn) {
        Space rootSpace = getObjBuilder().newSpace(spaceContext, firstSpaceTypeDefn);
        trackSpaceObject(rootSpace);
        return rootSpace;
    }

    private Tuple newTuple(Space argSpace) {
        Tuple tuple = getObjBuilder().newTuple(argSpace);
        trackSpaceObject(tuple);
        return tuple;
    }

    private Association newAssociation(CharacterSequence arg1) {
        Association association = getObjBuilder().newObjectReference(null, arg1.getOid());
        trackSpaceObject(association);
        return association;
    }

    private CharacterSequence newCharacterSequence(String stringValue) {
        CharacterSequence newCs = getObjBuilder().newCharacterSequence(stringValue);
        trackSpaceObject(newCs);
        return newCs;
    }

    // -------------- Tracking of runtime/instance things ---------------

    private void trackMetaObject(ModelElement modelElement) {
        spaceModelObjects.add(modelElement);
        indexModelObjectsByFullPath.put(modelElement.getName(), modelElement);
    }

    private void trackSpaceObject(SpaceObject spaceObject) {
        indexObjectsByOid.put(spaceObject.getOid(), spaceObject);
    }

    public SpaceObject dereference(SpaceOid referenceOid) throws RuntimeException {
        SpaceObject spaceObject = indexObjectsByOid.get(referenceOid);
        if (spaceObject == null)
            throw new RuntimeException("Space Oid ["+referenceOid+"] not found.");
        return spaceObject;
    }
}
