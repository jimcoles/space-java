// The Antlr grammar for Space if I can get it to work
parser grammar SpaceParser;

options {
    tokenVocab = SpaceLexer;
}

// -------------------------------- Parse Rules Section ------------------------------------

// If matched by the ANTLR recognizer, these will generate Rule Context nodes
// in the parse tree with the rule name specified.  These rules should reflect
// high-level concepts of the language.
//
// NOTE: Parse Rule names must start with a lowercase letter.

//space : spaceDecl;

//space : '(' atoms ')';

// space : list;

//spaceDecl
//    : '(' 'space' '(';
//
//primitiveType
//    :   'boolean'
//    |   'char'
//    |   'byte'
//    |   'short'
//    |   'int'
//    |   'long'
//    |   'float'
//    |   'double'
//    ;
//
//metaNotion
//    :   'space'
//    |   'object'
//    |   'scalar-dim'    // dim defn is int, float, string?
//    |   'space-dim'    // dim defn is a space
//    |   'const'
//    |   'var'
//    ;

list : comment? ListStart (atom | list | comment)* ListEnd comment?;

atom : identifier
     | string
     | integer
     | float1
     ;

comment
    : singleLineComment
    | multiLineComment
    ;

//
singleLineComment : SingleLineComment;
multiLineComment : BlockComment;

string : StringLiteral;
identifier : Identifier;
integer : Integer;
float1 : Float;
