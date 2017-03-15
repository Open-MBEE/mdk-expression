grammar Arithmetic;

@lexer::header {
    package gov.nasa.jpl.mbee.mdk.expression.antlr.generated;
}

@parser::header {
    package gov.nasa.jpl.mbee.mdk.expression.antlr.generated;
}

equation
   : expression relop expression
   ;

expression
   : multiplyingExpression ((PLUS | MINUS) multiplyingExpression)*
   ;

multiplyingExpression
   : powExpression ((TIMES | DIV) powExpression)*
   ;

powExpression
   : atom (POW expression)?
   ;

atom
   : variable
   | LPAREN expression RPAREN
   //| trig LPAREN expression RPAREN
   ;//| scientific
   
/*
scientific
   : number (E number)?
   ;
*/
relop
   : EQ
   | GT
   | LT
   ;

number
   : MINUS? DIGIT + (POINT DIGIT +)?
   ;

variable
   : MINUS? LETTER (LETTER | DIGIT)*
   ;
/*
trig
   : 'sin'
   | 'cos'
   | 'tan'
   ;
   */

LPAREN
   : '('
   ;


RPAREN
   : ')'
   ;


PLUS
   : '+'
   ;


MINUS
   : '-'
   ;


TIMES
   : '*'
   ;


DIV
   : '/'
   ;


GT
   : '>'
   ;


LT
   : '<'
   ;


EQ
   : '='
   ;


POINT
   : '.'
   ;

/*
E
   : 'e' | 'E'
   ;
*/

POW
   : '^'
   ;


LETTER
   : ('a' .. 'z') | ('A' .. 'Z')
   ;


DIGIT
   : ('0' .. '9')
   ;


WS
   : [ \r\n\t] + -> channel (HIDDEN)
   ;