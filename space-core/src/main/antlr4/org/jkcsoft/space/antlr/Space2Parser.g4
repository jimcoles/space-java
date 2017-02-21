// The Antlr grammar for Space if I can get it to work
parser grammar Space2Parser;

options {
    tokenVocab = Space2Lexer;
}

/*
Examples to prime the pump:


space-def MySpaceDef [equates-to OtherSpaceDef YetAnotherSpaceDef ...] (
    // Scalars
    int myIntDim [=<int>];  // dim-def
    boolean myBoolDim [=];  // dim-defn

    // These define Associations (ala Foreign Key Relationships)
    My
)

function myFunction[int arg1, boolean arg2] (

)

// Equations are a kind of Relation.  They are just predicates in normal form analogous to
// ax + by + cz = 0
// Equations reduce the number of degrees of freedom (DOF) of a system by one.
equality-constraint MyEquation (

)

// A Constraint is a general predicate that must be true at the end of
// every transaction.
constraint MyConstraint (

)

// An Enumerated Relation is just a set of tuples aligned to some Space definition
// (Base or
enum-relation MyRelation [] (
    []
)

// Physical
index MyIndex (

)

// Operators

+
=
&&
||

// Space operators
(rootSpace=MySpace1
  (select <locallyQualified>)  // implies need joins
  (<annonymoust constraint definition> or <constraintRef>)
)


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

spaceDefn :
    accessModifier? defnTypeModifier?
    SpaceKeyword identifier
    (ExtendsKeyword identifierRefList)?
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
    coordinateDefn
    | associationDefn
    | actionDefn
    ;

coordinateDefn :
    elementDefnHeader? primitiveTypeName identifier assignment? StatementEnd
    ;

associationDefn :
    elementDefnHeader? identifierRef identifier assignment? StatementEnd
    ;

actionDefn :
    accessModifier? anyTypeRef identifier ListStart anySpaceElementDefn* ListEnd
    elementDefnHeader?
    actionDefnBody
    ;

actionDefnBody :
    ListStart actionCallDefn* ListEnd
    ;

actionCallDefn :
    identifierRef TupleStart valueExpr* TupleEnd StatementEnd
    ;

valueExpr :
    literal
    | identifierRef
    | actionCallDefn
    ;

setDecl : SetStart SetEnd;

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
    | identifierRef;

primitiveTypeName :
    BooleanKeyword
    | OrdinalKeyword
    | CardinalKeyword
    | RealKeyword
    | VoidKeyword
    ;

assignment :
    AssignOper rightSide;

rightSide :
    literal
    | identifierRef
    ;

literal :
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

identifierRef :
    identifier (NavOper identifier)*
    ;

identifierRefList :
    identifierRef*
    ;