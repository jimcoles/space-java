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
//    |   'scalar-dim'    // dim type is int, float, string?
//    |   'space-dim'    // dim type is a space
//    |   'const'
//    |   'var'
//    ;

list : comment? LIST_START (atom | list | comment)* LIST_END comment?;

atom : identifier
     | string
     | integer
     | float
     ;

comment
    : singleLineComment
    | multiLineComment
    ;

//
singleLineComment : SingleLineComment ;
//multiLineComment : MLC_START MLC_BODY MLC_END;
multiLineComment : BlockComment;

string : StringLiteral;
identifier : Identifier;
integer : Integer;
float : Float;
