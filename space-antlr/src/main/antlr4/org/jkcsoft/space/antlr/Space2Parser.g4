// The Antlr grammar for Space if I can get it to work
parser grammar Space2Parser;

options {
    tokenVocab = Space2Lexer;
}

/*
 [space-def-modifiers] space-def <name> {
    [coor]
 }

E.g.,

 space-def MySpaceDef [extends] {
    int myIntDim [=<int>];  // dim-def
    boolean myBoolDim [=];  // dim-defn

    myFunction(int arg1, boolean arg2) {

    }
 }

*/
// -------------------------------- Parse Rules Section ------------------------------------

// If matched by the ANTLR recognizer, these will generate Rule Context nodes
// in the parse tree with the rule name specified.  These rules should reflect
// high-level concepts of the language.
//
// NOTE: Parse Rule names must start with a lowercase letter.

parseUnit :
    spaceDefn
    ;

spaceDefn :
    accessModifier? defnTypeModifier?
    SpaceKeyword identifier
    (ExtendsKeyword identifier)?
    elementDefnHeader?
    spaceDefnBody
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
    ListStart anyCoordinateOrActionDefn* ListEnd
    ;

anyCoordinateOrActionDefn :
    coordinateDefn
    | associationDefn
    | actionDefn
    ;

coordinateDefn :
    elementDefnHeader? typeRef identifier assignment? StatementEnd
    ;

associationDefn :
    elementDefnHeader? identifierRef identifier assignment? StatementEnd
    ;

actionDefn :
    accessModifier? typeRef identifier ListStart coordinateDefn* ListEnd
    ListStart actionCallDefn* ListEnd
    ;

actionCallDefn :
    identifierRef TupleStart valueExpr TupleEnd StatementEnd
    ;


valueExpr :
    literal
    | actionCallDefn
    | variableRef
    ;

variableRef :

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

typeRef :
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
    string
    | integer
    | floatLit
    ;

string : StringLiteral;
integer : IntegerLiteral;
floatLit : FloatLiteral;
identifier : Identifier;
identifierRef :
    identifier
    | identifierRef NavOper Identifier
    ;