// The Antlr grammar for Space if I can get it to work
parser grammar SpaceParser;

options {
    tokenVocab = SpaceLexer;
}

/*
Meta-Naming Conventions:

  __Defn : A construct that defines the structure of a type (generalization)
           or meta notion.
  __Expr : A construct that references declared elements and expresses
           some sequence of actions possibly resulting in a value at
           runtime.  Might express a mix of operations and truths
           (predicates).
           ?? Do we need ActionExpr vs EquationExpr.
  __Stmt : Expresses a thing to be executed such as a function call,
            not a structure;

Examples to prime the pump:

space-def MySpaceDef [equates-to OtherSpaceDef YetAnotherSpaceDef ...] (
    // Scalars
    int myIntVar [=<int>];  // var-defn
    boolean myBoolVar [=];  // var-defn

    // These define Associations (ala Foreign Key Relationships)
    MyOtherSpaceDef ref;

)

function myFunction solves <MyEquation> for [<var1>, <var2>] given [<var3>, <var4>] (

)

function myFunction solves <MyEquation> for [<var3>, <var4>] given [<var1>, <var2>] (

)

// Equations are Relations expressed symbolically, i.e., symbolic expressions.
// They are just predicates in normal form analogous to
// ax + by + cz = 0
// Every (orthogonal) Equation reduces the number of degrees of freedom (DOF) of a system by one.
equality-constraint MyEquation (

)

// A Constraint is a general predicate that must be true at the end of
// every transaction.
constraint MyConstraint (

)

// An Enumerated Relation is just a set of tuples aligned to some Space definition
enum-relation MyRelation [] (
    []
)

// Physical

index MyIndex (
)

// specifies vars of root and associated types with filter criteria
query-def <queryName> (

    '/' <rootObject> // may be a type

    [joins
        ./<assocName> [as <alias>]
        {  // nested associations
            ./<assocName> [as <alias>]
        }
    ]

    vars
        <assocName|alias>.var1 [, ...]

    filter

        // Should/May not contain object refs, only variable refs ??

        [ ( <assocName|alias>.var <boolOper> <valueExpr> )
          <boolOper> [<...> ]

        | <namedEquation>

)

// Operators produce a new thing from existing thing(s)

// Navigation opers = Query opers
. - LEFT nav.  Do we need a RIGHT nav operator to tell engine that reference is in the right space?


// integer and real opers
+
==

// Boolean opers
&&
||

// Space operators
(rootSpace=MySpace1
  (select <locallyQualified>)  // implies need joins
  (<annonymoust constraint definition> or <constraintRef>)
)

// Regex opers?  In effect, make regexes part of the language

*/

// -------------------------------- Parse Rules Section ------------------------------------

// If matched by the ANTLR recognizer, these will generate Rule Context nodes
// in the parse tree with the rule name specified.  Parse rules should reflect
// high-level concepts of the language.
//
// NOTE: Parse Rule names must start with a lowercase letter.

parseUnit :
    spaceTypeDefn | equationDefn | actionDefn
    ;

/*
  [  // a tuple
    space-type = "SpaceDef",
    name = "MySpace",
    sp
*/

parseUnitRelational :
    anyThing*
    ;

anyThing :
    spaceTypeDefn
    | equationDefn
    | associationDefn
    | queryDefn
    | regularExpr
    ;

spaceTypeDefn :
    accessModifier? defnTypeModifier?
    TypeDefKeyword identifier
    (ExtendsKeyword spacePathList)?
    elementDefnHeader?
    spaceTypeDefnBody
    ;

equationDefn :
    EquationDefKeyword
    ;

accessModifier :
    AccessKeyword
    ;

defnTypeModifier :
    SpaceDefnType
    ;

elementDefnHeader :
    comment
    ;

/*
   Enforce structure:
    1. vars, then assocs, then actions.
    2. decl/defn before any initializaiton.
*/
spaceTypeDefnBody :
    BlockStart
    variableDefnStmt*
    associationDefnStmt*
    actionDefn*
    spaceTypeDefn*
    BlockEnd
    ;

/* Var seq only important for order-based initialization. */
//variableDefnList :
//    SeqStart variableDefn (',' variableDefn)* SeqEnd
//    ;

variableDefnStmt :
    variableDefn ';'
    ;

variableDefn :
    variableDecl rightAssignmentExpr?
    ;

variableDecl :
    elementDefnHeader? primitiveTypeName identifier
    ;

//associationDefnList :
//    SeqStart associationDefn (',' associationDefn)* SeqEnd
//    ;

associationDefnStmt :
    associationDefn ';'
    ;

associationDefn :
    associationDecl rightAssignmentExpr?
    ;

associationDecl :
    elementDefnHeader? spacePathExpr identifier
    ;

parameterDecl :
    variableDecl |
    associationDecl
    ;

//actionDefnList :
//    SeqStart actionDefn (',' actionDefn) SeqEnd
//    ;

actionDefn :
    'function-def' accessModifier? anyTypeRef identifier parameterDefnList
    elementDefnHeader?
    actionDefnBody
    ;

parameterDefnList : '(' (parameterDecl (',' parameterDecl)*)? ')' ;

actionDefnBody :
    BlockStart
    statement*
    BlockEnd
    ;

statement :
    expression ';'
    ;

expression :
    variableDefn |
    associationDefn |
    actionCallExpr |
    assignmentExpr
    ;

// Important that an action call may be a list of params or a single tuple
// object holding parameters.  The language runtime knows the names/paths of
// all elements in a Tuple.
actionCallExpr :
    spacePathExpr TupleStart valueOrAssignmentExprList? TupleEnd
    ;

// ===============================================================================
//
// ===============================================================================

objectExpr :
    'new' spacePathExpr TupleStart valueOrAssignmentExprList? TupleEnd
    ;

valueExpr :
    literalExpr
    | spacePathExpr
    | actionCallExpr
    | objectExpr
    ;

valueOrAssignmentExprList :
    valueOrAssignmentExpr (',' valueOrAssignmentExpr)*
    ;

valueOrAssignmentExpr :
    valueExpr | assignmentExpr
    ;

setLiteral : UserSetStart UserSetEnd;

spaceDecl : SpaceStart SpaceEnd;

comment
    : singleLineComment
    | multiLineComment
    ;

//
singleLineComment : SingleLineComment;
//multiLineComment : MLC_START MLC_BODY MLC_END;
multiLineComment : BlockComment;

anyTypeRef :
    primitiveTypeName
    | spacePathExpr;

primitiveTypeName :
    BooleanKeyword
    | OrdinalKeyword
    | CardinalKeyword
    | RealKeyword
    | VoidKeyword
    ;

rightAssignmentExpr :
    '=' valueExpr
    ;

assignmentExpr :
    spacePathExpr rightAssignmentExpr
    ;

//rightSide :
//    literal
//    | spacePathExpr
//    ;

literalExpr :
    scalarLiteral
    | stringLiteral
    ;

scalarLiteral :
    integerLiteral
    | floatLiteral
    | booleanLiteral;

stringLiteral : StringLiteral;
integerLiteral : IntegerLiteral;
floatLiteral : FloatLiteral;
booleanLiteral : BooleanLiteral;

identifier : Identifier;

// ------------ QUERY EXPRESSIONS --------------

queryDefn :
    'query-def'
    ;

// ------------ SPACE PATH EXPRESSIONS -----------
spacePathExpr :
    identifier
    | identifier SPathNavAssocToOper spacePathExpr
    | identifier SPathNavAssocToOper2 spacePathExpr
    ;

spacePathList :
    spacePathExpr (',' spacePathExpr)*
    ;

// ------------ GRAMMAR EXPRESSIONS ---------------

// TODO Develop grammar grammar that will help Space coders map
//      complex strings to Spaces
grammarExpression :

    ;

// ------------ REGULAR EXPRESSIONS ---------------

// TODO Develop grammar for regular expression notation
regularExpr :
    'regex-def'
    ;