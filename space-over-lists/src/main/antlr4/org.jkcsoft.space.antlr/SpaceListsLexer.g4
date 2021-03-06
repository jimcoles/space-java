/* Space Lexer Grammar */

lexer grammar SpaceListsLexer;

//
// -------------------------------- Lexer Rules Section ------------------------------------
//
// Lexer rules match character sequences (the small things) to generate Token streams.
// The set of Tokens is also called the 'Vocabulary' of the language.
// Parser Rules then match patterns amongst the Tokens.
// These will generate 'Terminal' nodes with the valueExpr of the matched string
// (Token) available in the node object. These should be low-level notions.
//
// NOTE: Lexer rules begin with an uppercase letter.
// Convention used here:
//   ALL UPPPER : Fragments
//   CamelCaseUpper : Tokens

ListStart : '(';
ListEnd : ')';

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

Integer : INT;

Float
    :   '-'? INT '.' INT Exp?   // 1.35, 1.35E-9, 0.3, -4.5
    |   '-'? INT Exp            // 1e10 -3e4
    |   '-'? INT                // -3, 45
    ;

WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines


//SLC_START : '//' -> pushMode(QUOTED);
//MLC_START : '/*' -> pushMode(QUOTED);

// COMMENT : '/*' .*? '*/' -> channel(HIDDEN) ;

// STRING :  '"' (ESC | ~["\\])* '"' ;


//
// --------------------------- Token fragment definitions ---------------------------
//
// Fragments are like substitution macros convenient for defining Tokens.  Fragments
// are named but Fragments are never returned as part of the Token stream
// to the parser.

fragment Esc : '\\' (["\\/bfnrt] | Unicode) ;
fragment Unicode : 'u' HEX HEX HEX HEX ;
fragment HEX : [0-9a-fA-F] ;


fragment INT :   '0' | [1-9] [0-9]* ; // no leading zeros
fragment Exp :   [Ee] [+\-]? INT ;

// fragment Number : '0'..'9'*;

fragment IdStartChars : ('a'..'z' | 'A'..'Z' | '_') ;

fragment IdTailChars : IdStartChars | '0'..'9' | '-';

// Tool-generated comment token
BlockComment : '/*' .*? '*/'; // -> channel(HIDDEN);
SingleLineComment : '//' ~[\n\r]*? [\n\r]; // -> channel(HIDDEN);

//mode QUOTED;

//SLC_BODY    : ~('\n' | '\r')*?;
//SLC_END     : [\n\r] -> popMode;
//MLC_BODY    : .*?;
//MLC_END     : '*/' -> popMode;
