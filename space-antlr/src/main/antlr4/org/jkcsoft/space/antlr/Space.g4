// The Antlr grammar for Space if I can get it to work
grammar Space;


// -------------------------------- Rules section ------------------------------------

//space : spaceDecl;

//space : '(' atoms ')';

space : list;

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

atom : IDENTIFIER
     | string
     ;

list : '(' (atom | list)* ')';

string : STRING;

// -------------------------------- Token section ------------------------------------

IDENTIFIER
    : ID_HEAD_CHAR ID_TAIL_CHAR*
    ;

// interpret the following as: A double-quote ('"') followed by any number of (*)
// other characters
// (except double-quotes, unless it's an escaped double-quote)
// followed by closing double-quote
STRING : '"' ( ~'"' | '\\' '"' )* '"' ;

//fragment
//IDENTIFIER
//    : ID_HEAD_CHAR ID_TAIL_CHAR*
//    ;

fragment
ID_HEAD_CHAR : ('a'..'z' | 'A'..'Z' | '_') ;

fragment
ID_TAIL_CHAR : ID_HEAD_CHAR | '0'..'9' | '-';

//fragment
//NAME: SYMBOL_HEAD SYMBOL_REST* (':' SYMBOL_REST+)* ;
//
//fragment
//SYMBOL_HEAD
//    : ~('0' .. '9'
//        | '^' | '`' | '\'' | '"' | '#' | '~' | '@' | ':' | '/' | '%' | '(' | ')' | '[' | ']' | '{' | '}' // FIXME: could be one group
//        | [ \n\r\t\,] // FIXME: could be WS
//        )
//    ;
//
//fragment
//SYMBOL_REST
//    : SYMBOL_HEAD
//    | '0'..'9'
//    | '.'
//    ;

WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines

