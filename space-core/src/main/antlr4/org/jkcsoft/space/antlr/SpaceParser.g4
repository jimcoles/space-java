// The Antlr grammar for Space if I can get it to work
parser grammar SpaceParser;

options {
    tokenVocab = SpaceLexer;
}

/*
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
// in the parse tree with the rule name specified.  These rules should reflect
// high-level concepts of the language.
//
// NOTE: Parse Rule names must start with a lowercase letter.

parseUnit :
    spaceDefn | equationDefn | actionDefn
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
    spaceDefn
    | equationDefn
    | associationDefn
    ;

spaceDefn :
    accessModifier? defnTypeModifier?
    SpaceKeyword identifier
    (ExtendsKeyword spacePathList)?
    elementDefnHeader?
    spaceDefnBody
    ;

equationDefn :
    EquationKeyword
    ;

accessModifier :
    AccessKeyword
    ;

defnTypeModifier :
    SpaceDefnType
    ;

elementDefnHeader : comment
    ;

spaceDefnBody :
    ListStart anySpaceElementDefn* ListEnd
    ;

anySpaceElementDefn :
    variableDefn
    | associationDefn
    | actionDefn
    ;

variableDefn :
    elementDefnHeader? primitiveTypeName identifier assignmentExpr? StatementEnd
    ;

associationDefn :
    elementDefnHeader? spacePathExpr identifier assignmentExpr? StatementEnd
    ;

actionDefn :
    accessModifier? anyTypeRef identifier ListStart anySpaceElementDefn* ListEnd
    elementDefnHeader?
    actionDefnBody
    ;

actionDefnBody :
    ListStart actionCallExpr* ListEnd
    ;

actionCallExpr :
    spacePathExpr TupleStart valueExpr* TupleEnd StatementEnd
    ;

valueExpr :
    literalExpr
    | spacePathExpr
    | actionCallExpr
    ;

setLiteral : SetStart SetEnd;

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

assignmentExpr :
    spacePathExpr AssignOper valueExpr;

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
    | floatLiteral;

stringLiteral : StringLiteral;
integerLiteral : IntegerLiteral;
floatLiteral : FloatLiteral;

identifier : Identifier;

spacePathExpr :
    identifier (SPathNavAssocToOper spacePathExpr?)?
    ;

spacePathList :
    spacePathExpr*
    ;

regularExpr :
    // TODO Develop grammar for regular expression notation
    ;