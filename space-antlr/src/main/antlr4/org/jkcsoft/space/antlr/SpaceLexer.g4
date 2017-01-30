/* Space Lexer Grammar */

lexer grammar SpaceLexer;

//
// -------------------------------- Lexer Rules Section ------------------------------------
//
// These will generate 'Terminal' nodes with the value of the matched string
// (Token) available in the node object. These should be low-level notions.
//
// NOTE: Lexer rules begin with an uppercase letter.

LIST_START : '(';
LIST_END : ')';

Identifier
    : IdStartChars IdTailChars*
    ;

/*
    Interpret the following definition of "StringLiteral" as:

        A double-quote ('"') followed by any number (*) of other characters
        (except double-quotes, unless it's an escaped double-quote), followed
        by closing double-quote.
*/

StringLiteral : '"' ( ~'"' | '\\' '"' )* '"' ;

Integer : Int;

Float
    :   '-'? Int '.' Int Exp?   // 1.35, 1.35E-9, 0.3, -4.5
    |   '-'? Int Exp            // 1e10 -3e4
    |   '-'? Int                // -3, 45
    ;

WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines


//SLC_START : '//' -> pushMode(QUOTED);
//MLC_START : '/*' -> pushMode(QUOTED);

// COMMENT : '/*' .*? '*/' -> channel(HIDDEN) ;

// STRING :  '"' (ESC | ~["\\])* '"' ;


//
// --------------------------- Token fragment definitions ---------------------------
//
// Fragments are like substitution macros convenient for defining Tokens.

fragment Esc : '\\' (["\\/bfnrt] | Unicode) ;
fragment Unicode : 'u' HEX HEX HEX HEX ;
fragment HEX : [0-9a-fA-F] ;


fragment Int :   '0' | [1-9] [0-9]* ; // no leading zeros
fragment Exp :   [Ee] [+\-]? Int ;

// fragment Number : '0'..'9'*;

fragment IdStartChars : ('a'..'z' | 'A'..'Z' | '_') ;

fragment IdTailChars : IdStartChars | '0'..'9' | '-';

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

// fragment EMPTY : ;

// Tool-generated comment token
BlockComment : '/*' .*? '*/'; // -> channel(HIDDEN);
SingleLineComment : '//' ~[\n\r]*? [\n\r]; // -> channel(HIDDEN);

//mode QUOTED;

//SLC_BODY    : ~('\n' | '\r')*?;
//SLC_END     : [\n\r] -> popMode;
//MLC_BODY    : .*?;
//MLC_END     : '*/' -> popMode;
