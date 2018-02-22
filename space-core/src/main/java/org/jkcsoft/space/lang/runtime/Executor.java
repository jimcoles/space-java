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
 *     The top-level executive context for loading Space meta objects and running a
 * Space program.  Holds and manages instance level objects, call stack, etc..
 * <p>
 *     An Executor manages interactions between second-level
 * elements including XmlLoader, ExprProcessor, Querier.
 * <p>
 *     The general pattern is:
 *  <ul>
 *      <li>exec() methods handle executable bits like the program itself and statements.</li>
 *      <li>eval() methods handle expression</li>
 *  </ul>
 *  </p>
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

    public AbstractActionDefn OPER_NAV;
    public AbstractActionDefn OPER_NEW_TUPLE;
    public AbstractActionDefn OPER_NEW_CHAR;
    public AbstractActionDefn OPER_ASSIGN;
    /**
     * Meta objects are loaded as we parse the source code. Intrinsic and native meta
     * objects are loaded prior to parsing and source files.  Must be able to lookup
     * a meta object by name or by id.  Some meta object are anonymous.
     */
    private final List<Schema> dirChain = new LinkedList<>();
    /** A special directory root to hold intrinsic operators. */
    private Schema langRootSchema;

    /** The central meta object table. */
    private Set<ModelElement> metaObjectNormalTable = new HashSet<>();
    /** (not used currently) Idea is to hold redundantly accumulated info useful
     * for lookup during execution. */
    private Map<NamedElement, MetaInfo> metaObjectExtendedInfoMap = new TreeMap<>();
    /**
     * The 'object' tables for 'instance' objects associated with the running program.
     * This map is added to as the program runs.
     */
    private Map<SpaceOid, SpaceObject> instObjectsIndexByOid = new TreeMap<>();
    /**
     * Key=the referenced object's Oid.
     * Value=the set of object Oids holding reference to the key Oid.
     */
    private Map<SpaceOid, Set<SpaceOid>> objectReferenceMap = new TreeMap<>();

    private Stack<ActionCall> callStack = new Stack<>();

    /**
     * TODO: Use specialized expression processors for each type of expression, e.g.,
     * unary int, binary int, unary string, binary string, etc.
     *
     * Mapping from expression type to expression handler. Some handlers will be
     * Space standard, some will be user-provided.
     */
    private Map<String, ExprProcessor> exprProcessors = null;

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

    public void run(String ... filePath) throws RuntimeException {
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
            throw new RuntimeException("Could not find or load source loader ["+parserImplClassName+"]", e);
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
        }
        catch (RuntimeException ex) {
            throw ex;
        }
        catch (Exception ex) {
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
            log.debug("linking refs held by ["+astNode+"]");
            Set<MetaReference> references = astNode.getReferences();
            if (references != null) {
                for (MetaReference reference : references) {
                    if (reference.getState() == LoadState.INITIALIZED) {
                        log.debug("resolving reference [" + reference + "]");
                        NamedElement lexParent = AstUtils.getLexParent(astNode);
                        reference.setLexicalContext(lexParent);
                        log.debug("found lexical parent [" + lexParent + "]");
                        NamedElement refElem =
                            lookupLenientMetaObject(reference.getLexicalContext(), reference.getSpacePathExpr());
                        if (refElem == null) {
                            errors.add(
                                new RuntimeError(reference.getSpacePathExpr().getSourceInfo(), 0,
                                                 "could not resolve space path expr [" + reference + "]"));
                        }
                        else {
                            reference.setResolvedMetaObj(refElem);
                            reference.setState(LoadState.RESOLVED);
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
        loadNativeClass(JnOpSys.class, AstUtils.getLangRoot(dirChain));
        loadNativeClass(JnMath.class, AstUtils.getLangRoot(dirChain));
        loadNativeClass(JnCharSequence.class, AstUtils.getLangRoot(dirChain));
        loadNativeClass(JnSpaceOpers.class, AstUtils.getLangRoot(dirChain));

        OPER_NAV = lookupOperator("nav");
        OPER_NEW_TUPLE = lookupOperator("newTuple");
        OPER_NEW_CHAR = lookupOperator("newCharSequence");
        OPER_ASSIGN = lookupOperator("assign");
    }

    private void loadNativeClass(Class jnClass, Schema rootAstSpace) {
        AstFactory astFactory = new AstFactory();
        SpaceTypeDefn spaceTypeDefn = astFactory.newSpaceTypeDefn(
            new NativeSourceInfo(jnClass), toSpaceTypeName(jnClass)
        );
        rootAstSpace.addSpaceDefn(spaceTypeDefn);
        trackMetaObject(spaceTypeDefn);
//        spaceDefn.setName(jnClass.getSimpleName());
        Method[] methods = jnClass.getMethods();
        for (Method jMethod : methods) {
            if (isExcludedNative(jMethod))
                continue;

            NativeActionDefn actionDefn = astFactory.newNativeActionDefn(
                    new NativeSourceInfo(jMethod),
                    jMethod.getName(),
                    jMethod,
                    spaceTypeDefn
            );
            spaceTypeDefn.addActionDefn(actionDefn);
            trackMetaObject(actionDefn);
            //
            jMethod.getParameters();    // TODO build dynamic arg space
            Parameter jParam = null;
//            VariableDefn variableDefn = actionDefn.addParameter(
//                    userAstFactory.newVariableDefn(
//                            new NativeSourceInfo(jParam),
//                            "arg1",
//                            null
//                    )
//            );
//            trackMetaObject(variableDefn);
        }
    }

    private boolean isExcludedNative(Method jMethod) {
        Method[] baseMethods = Object.class.getMethods();
        return ArrayUtils.contains(baseMethods, jMethod);
    }

    private AbstractActionDefn lookupOperator(String operSimpleName) {
        NamedElement opersSpaceDefn = lookupMetaObject(langRootSchema, toSpaceTypeName(JnSpaceOpers.class));
        AbstractActionDefn operActionDefn =
                (AbstractActionDefn) lookupMetaObject(opersSpaceDefn, operSimpleName);
        if (operActionDefn == null)
            throw new RuntimeException("space lang object not found with path [" + operSimpleName + "]");
        return operActionDefn;
    }

    private String toSpaceTypeName(Class spaceJavaWrapperClass) {
        return spaceJavaWrapperClass.getSimpleName().substring(2);
    }

    public NamedElement lookupLenientMetaObject(NamedElement context, SpacePathExpr spacePathExpr) {
        NamedElement lookup = null;
        List<NamedElement> trySequence = new LinkedList<>();
        trySequence.add(context);
        trySequence.addAll(dirChain);
        for (NamedElement tryContext : trySequence) {
            lookup = lookupMetaObject(tryContext, spacePathExpr);
            if (lookup != null)
                break;
        }
        return lookup;
    }

    public static NamedElement lookupMetaObject(NamedElement context, SpacePathExpr spacePathExpr) {
        log.debug("lookup meta object from " + context.getName() + " -> " + spacePathExpr );
        NamedElement targetChild = lookupMetaObject(context, spacePathExpr.getText());
        if (spacePathExpr.hasNextExpr()) {
            targetChild = lookupMetaObject(targetChild, spacePathExpr.getNextExpr());
        }
        return targetChild;
    }

    public static NamedElement lookupMetaObject(NamedElement context, String name) {
        NamedElement childByName = context.getChildByName(name);
        if (childByName == null)
            log.warn("element ["+context+"] does not contain named child ["+name+"]");
        return childByName;
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
        log.debug("exec: " + programSchema);
        SpaceTypeDefn firstSpaceTypeDefn = programSchema.getFirstSpaceDefn();
        Space rootSpace = newSpace(null, firstSpaceTypeDefn);
        SpaceActionDefn spMainActionDefn = (SpaceActionDefn) firstSpaceTypeDefn.getFunction("main");
//        List<SpaceObject> objectHeap = programSchema.getObjectHeap();
//        for (SpaceObject spaceObj : objectHeap) {
//            trackInstanceObject(spaceObj);
//        }

        try {
            exec(rootSpace, spMainActionDefn);
        } catch (RuntimeException ex) {
            log.error("error executing", ex);
        }
        log.debug("exiting Space program execution");
        return null;
    }

    private void delegateExec(Space spcContext, AbstractActionDefn function) throws RuntimeException {
        log.debug("delegate exec: " + function);
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
     * @param spcContext    Might be a local function space or an argument call space.
     * @param spcActionDefn The composite, imperative action defined via Space source code (i.e., not Native).
     */
    private Assignable exec(Space spcContext, SpaceActionDefn spcActionDefn) throws RuntimeException {
        log.debug("exec: " + spcActionDefn);
        ActionCall actionCall = getObjBuilder().newAction(spcContext, spcActionDefn);
        callStack.push(actionCall);
        List<ActionCallExpr> childActions = spcActionDefn.getNestedActions();
        for (ActionCallExpr childActionExpr : childActions) {
            Assignable callValueObject = eval(spcContext, childActionExpr);
        }
        callStack.pop();
        log.debug("popped call stack. size [" + callStack.size() + "]");
        return actionCall.getReturnSpace();
    }

    /**
     * Invokes a Java native method all via Java reflection.  Native actions
     * do not have nested actions, at least none that are executed or controlled
     * by this executor.
     */
    private void exec(Space spcContext, NativeActionDefn nativeActionDefn) {
        try {
            log.debug("native Java call: " + nativeActionDefn);
            Assignable arg1 = spcContext.iterator().next().getAssignableAt(0);
            Object jArg1 = "?";
            if (arg1 instanceof Association)
                //
                jArg1 = dereference(((Association) arg1).getReferenceOid()).toString();
            else {
                jArg1 = arg1.toString();
            }
            // TODO: 1/29/17 Remove hardcodings and improve casting generalization
            CastTransforms casters = new CastTransforms();
            if (jArg1 instanceof CharacterSequence)
                jArg1 = casters.charSequenceToString((CharacterSequence) jArg1);
            nativeActionDefn.getjMethod().invoke(new JnOpSys(), jArg1);
        } catch (IllegalAccessException e) {
            log.error(e);
        } catch (InvocationTargetException e) {
            log.error(e);
        }
    }

    /**
     * The central eval method of this executor.
     * An {@link ActionCallExpr} is a reference to a {@link SpaceActionDefn} along with the argument
     * space for the call. In general, the arguments themselves are value expressions that must be
     * evaluated, recursively.
     *
     * @param spcContext  The argument space (instance-level with values accessible for read/write).
     * @param spcCallExpr The action-invoking expression to be evaluated (not the instance-level object).
     *                    Can be a Space function call or a Native (Java) function call.
     */
    private Assignable eval(Space spcContext, ActionCallExpr spcCallExpr) throws RuntimeException {
        log.debug("eval: call to " + spcCallExpr.getFunctionRef());
        Assignable value = null;
        //
//        Namespace fullNs = new Namespace(spcCallExpr.getFunctionPathExpr());
//        String spacePath = fullNs.subPath(0, fullNs.getSize() - 2);
//        SpaceDefn targetSpaceDefn = lookupMetaObject(spcCallExpr.getFunctionPathExpr());
//        if (targetSpaceDefn == null)
//            throw new RuntimeException("Space defn [" + spcCallExpr.getFunctionPathExpr() + "] not found");
//        String functionName = fullNs.getLast();

        AbstractActionDefn targetFunctionDefn = spcCallExpr.getFunctionRef().getResolvedMetaObj();

        if (targetFunctionDefn == null)
            throw new RuntimeException("unresolved function ref " + spcCallExpr.getFunctionRef());

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
            } else if (argumentExpr instanceof ActionCallExpr) {
                Assignable actionArgValue = eval(argSpace, (ActionCallExpr) argumentExpr);
                addValueOrRef(argTuple, idxArg, actionArgValue);
            } else {
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

    /**
     * Returns the literal value object associated with the {@link LiteralExpr}.
     * The returned object may be a scalar, a character sequence, or a complete
     * space.
     */
    private Assignable eval(Space spcContext, LiteralExpr literalExpr) {
        Assignable value = null;
        if (literalExpr.isString()) {
            CharacterSequence arg1 = newCharacterSequence(literalExpr.getAsString());
            value = newAssociation(arg1);
        } else {
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

    private Tuple newTuple(Space argSpace) {
        Tuple tuple = getObjBuilder().newTuple(argSpace);
        trackInstanceObject(tuple);
        return tuple;
    }

    private Association newAssociation(CharacterSequence arg1) {
        Association association = getObjBuilder().newObjectReference(null, arg1.getOid());
        trackInstanceObject(association);
        return association;
    }

    private CharacterSequence newCharacterSequence(String stringValue) {
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
        instObjectsIndexByOid.put(spaceObject.getOid(), spaceObject);
    }

    public SpaceObject dereference(SpaceOid referenceOid) throws RuntimeException {
        SpaceObject spaceObject = instObjectsIndexByOid.get(referenceOid);
        if (spaceObject == null)
            throw new RuntimeException("Space Oid [" + referenceOid + "] not found.");
        return spaceObject;
    }
}
