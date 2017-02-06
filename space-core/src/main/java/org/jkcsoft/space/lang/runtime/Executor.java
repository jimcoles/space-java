/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2016. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.runtime;

import org.apache.log4j.Logger;
import org.jkcsoft.java.util.Strings;
import org.jkcsoft.space.lang.ast.*;
import org.jkcsoft.space.lang.instance.*;
import org.jkcsoft.space.lang.runtime.impl.CastTransforms;
import org.jkcsoft.space.lang.runtime.jnative.math.JnMath;
import org.jkcsoft.space.lang.runtime.jnative.opsys.JnOpSys;
import org.jkcsoft.space.lang.runtime.loaders.xml.XmlLoader;
import org.jkcsoft.space.util.Namespace;

import java.io.File;
import java.io.FileInputStream;
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

    private static final Logger log = Logger.getRootLogger();
    private static Executor instance;

    public static Executor getInstance() {
        return instance;
    }

    /**
     *
     */
    public void main(String[] args) {
        try {
            String stFilePath = args[0];
            File file = new File(stFilePath);
            if (!file.exists()) {
                throw new Exception("Input file [" + stFilePath + "] does not exist.");
            }
            XmlLoader loader = new XmlLoader(new FileInputStream(file));
            SpaceProgram program = loader.load();
            Executor exec = new Executor();
            exec.exec(program);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }


    /** Definition objects are loaded as we parse the source code.  Some objects are loaded
     *  with static logic.  These are the intrinsic, native objects.
     */
    private Set<SpaceDefn> tableDefnObjects = new HashSet<>();
    private Map<String, SpaceDefn> indexDefnObjectsByFullPath = new TreeMap<>();

    private Stack<Action> callStack = new Stack<>();

    /** The 'object' tables for 'instance' objects associated with the running program.
     *  This map is added to as the program runs.
     */
    private Map<SpaceOid, SpaceObject> indexObjectsByOid = new TreeMap<>();

    /**
     * Mapping from expression type to expression handler. Some handlers will be
     * Space standard, some will be user-provided.
     */
    private Map _exprProcessors = null;

    public Executor() {
        loadNativeSpaces();
    }

    private void loadNativeSpaces() {
        // use Java annotations Java-native objects
//        SpaceNative.class.getAnnotations();
        loadNativeClass(JnOpSys.class);
        loadNativeClass(JnMath.class);
        dumpSymbolTables();
    }

    private void dumpSymbolTables() {
        log.debug("dump table of definition objects: " + Strings.buildCommaDelList(tableDefnObjects));
        log.debug("dump map of definition objects: " + Strings.buildCommaDelList(indexDefnObjectsByFullPath.entrySet()));
    }

    private void loadNativeClass(Class jnClass) {
        EntityDefn spaceDefn = new EntityDefn(null, jnClass.getSimpleName());
//        spaceDefn.setName(jnClass.getSimpleName());
        Method[] methods = jnClass.getMethods();
        for (Method jMethod: methods) {
            EntityDefn nativeArgSpaceDefn = new EntityDefn(spaceDefn, null);
            jMethod.getParameters();
            nativeArgSpaceDefn.addDimension(new CoordinateDefn("arg1", null));
            spaceDefn.addActionDefn(new NativeActionDefn(spaceDefn, jMethod.getName(), jMethod, nativeArgSpaceDefn));
        }
        addDefnObject(spaceDefn);
    }

    private void addDefnObject(EntityDefn spaceDefn) {
        tableDefnObjects.add(spaceDefn);
        indexDefnObjectsByFullPath.put(spaceDefn.getName(), spaceDefn);
    }

    public ModelElement eval(ModelElement action) {
        return null;
    }

    /**
     * Evaluates a SpaceProgram.
     */
    public ModelElement exec(SpaceProgram program) throws Exception {
        SpaceDefn firstSpace = program.getFirstSpace();
        Space rootContext = new Space(null, firstSpace);
        SpaceActionDefn spMainActionDefn = (SpaceActionDefn) firstSpace.getFunction("main");
        List<SpaceObject> objectHeap = program.getObjectHeap();
        for (SpaceObject spaceObj:objectHeap) {
            indexObjectsByOid.put(spaceObj.getOid(), spaceObj);
        }

        try {
            exec(rootContext, spMainActionDefn);
        }
        catch (RuntimeException ex) {
            log.error("error executing", ex);
        }
        log.info("exiting Space program execution");
        return null;
    }

    // ------------------------- Execs ------------------------
    // delegateExec( ) methods take as arguments the instance-level context space
    // along with the action definition to be executed.
    //

    private void delegateExec(Space spcContext, AbstractActionDefn function) throws RuntimeException {
        log.debug("enter delegateExec: " + function);
        if (function instanceof SpaceActionDefn)
            exec(spcContext, (SpaceActionDefn) function);
        else if (function instanceof CallActionDefn)
            exec(spcContext, (CallActionDefn) function);
        else if (function instanceof NativeActionDefn)
            exec(spcContext, (NativeActionDefn) function);
    }

    /**
     * Execution of SpaceActionDefn's is the core work of the executor.
     * The executor must recurse into a depth-first evaluaton of nested actions,
     * resolve all coordinate references, resolve operator calls, push and pop the call stack,
     * and move values (spaces) from action execution into the proper space for
     * parent actions.
     *
     * @param spcContext Might be a local function space or an argument call space.
     * @param spcActionDefn The composite, imperative action defined via Space source code (i.e., not Native).
     */
    private void exec(Space spcContext, SpaceActionDefn spcActionDefn) throws RuntimeException {
        Action action = getObjBuilder().newAction(spcContext, spcActionDefn);
        log.debug("push to call stack and exec: " + spcActionDefn);
        callStack.push(action);
        List<AbstractActionDefn> childActions = spcActionDefn.getNestedActions();
        for (AbstractActionDefn childAction: childActions) {
            delegateExec(spcContext, childAction);
        }
        callStack.pop();
        log.debug("popped call stack. size ["+callStack.size()+"]");
    }

    /** Invokes a Java native method all via Java reflection.  Native actions
     * do not have nested actions, at least none that are executed or controlled
     * by this executor. */
    private void exec(Space spcContext, NativeActionDefn nativeActionDefn) {
        try {
            log.debug("native Java call: " + nativeActionDefn);
            ScalarValue arg1 = spcContext.iterator().next().getValueAt(0);
            Object jArg1 = "?";
            if (arg1 instanceof ObjectReference)
                //
                jArg1 = dereference( ((ObjectReference) arg1).getReferenceOid()).toString();
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
     * A CallAction is a reference to an SpaceActionDefn along with the argument space for the call.
     * @param spcContext The argument space (instance-level with values set)
     * @param spcCallDefn The definition of the call (not the instance-level object).
     */
    private void exec(Space spcContext, CallActionDefn spcCallDefn) throws RuntimeException {
        log.debug("function call: " + spcCallDefn);
        Namespace fullNs = new Namespace(spcCallDefn.getFunctionRefId());
        String spacePath = fullNs.subPath(0, fullNs.getSize() - 2);
        String functionName = fullNs.getLast();
        SpaceDefn targetSpaceDefn = indexDefnObjectsByFullPath.get(spacePath);
        if (targetSpaceDefn == null)
            throw new RuntimeException("Space defn [" + spacePath + "] not found");
        AbstractActionDefn targetFunctionDefn = targetSpaceDefn.getFunction(functionName);
        if (targetFunctionDefn == null)
            throw new RuntimeException("Function defn [" + functionName + "] not found in Space Defn [" + spacePath + "]");
        if (! (targetFunctionDefn instanceof Callable) )
            throw new RuntimeException("Function defn [" + functionName + "] does not implement the ["+Callable.class.getName()+"] interface.");

//        SpaceActionDefn spcActionFunctionDefn = (SpaceActionDefn) spcFunctionDefn;

        // must add a nested Space context beneath the incoming context space to
        // represent the call stack
        Space argSpace = getObjBuilder().newSpace(spcContext, ((Callable) targetFunctionDefn).getArgSpaceDefn());
        // assignments may be by name or by order (like a SQL update statement)
        Tuple argTuple = getObjBuilder().newTuple(argSpace, spcCallDefn.getAssignmentDefns()[0].getRightSideValue());
        argSpace.addTuple(argTuple);
        //
        delegateExec(argSpace, targetFunctionDefn);
    }

    private ObjectBuilder getObjBuilder() {
        return ObjectBuilder.getInstance();
    }

    public SpaceObject dereference(SpaceOid referenceOid) throws RuntimeException {
        SpaceObject spaceObject = indexObjectsByOid.get(referenceOid);
        if (spaceObject == null)
            throw new RuntimeException("Space Oid ["+referenceOid+"] not found.");
        return spaceObject;
    }
}
