grammar ArithmeticBinary;

@lexer::header {
    package gov.nasa.jpl.mbee.mdk.expression.antlr.generated;
}

@parser::header {
    package gov.nasa.jpl.mbee.mdk.expression.antlr.generated;
}

//*****************PARSER RULES******************

expression
   : LPAREN expression RPAREN #parExp
   | literal	#litExp
   | MINUS literal #negLitExp
   | unary LPAREN expression RPAREN	#unaryExp	
   | Variable LPAREN expression (COM expression)* RPAREN #funExp	//added this
   | expression (POW) expression #binaryExp1
   | expression (TIMES|DIV) expression	#binaryExp2
   | expression (PLUS|MINUS) expression #binaryExp3
   | expression (EQ|GT|LT|EGT|ELT) expression #eqExp		//added this
   | MINUS expression	#negExp
   ;

literal
   : Number		#num
   | Variable	#var
   ;	

Number
   : DIGIT + (POINT DIGIT +)? //removed minus
   ;

Variable
   : LETTER (LETTER | DIGIT)*
   ;

unary
   : 'sin'	#sin
   | 'cos'	#cos
   | 'tan'	#tan
   | 'lg'	#lg
   | 'ln'	#ln
   | 'sqrt'	#sqrt
   | 'sum'	#sum
   | 'int' 	#int
   | 'grad'	#grad
   ;
   
//*****************TOKEN RULES*******************

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

EGT				//added this
   : '>='		
   ;
   
ELT
   : '<='
   ;

EQ
   : '='
   ;


POINT
   : '.'
   ;

POW
   : '^'
   ;
   
COM				//added this
   : ','
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