// The Antlr grammar for Space if I can get it to work
grammar Space;

//space : spaceDecl;

space : '(' atoms ')';

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
     | list
     ;

atoms : (atom)* ;

list : '(' atoms ')';

IDENTIFIER
    : ID_HEAD_CHAR ID_TAIL_CHAR*
    ;

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

