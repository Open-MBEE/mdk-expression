grammar ArithmeticBinary;

@lexer::header {
    package gov.nasa.jpl.mbee.mdk.expression.antlr.generated;
}

@parser::header {
    package gov.nasa.jpl.mbee.mdk.expression.antlr.generated;
}

//*****************PARSER RULES******************

expression
   : lbrackets expression (comma expression)* rbrackets #parExp //[a,b]
   | MINUS literal #negLitExp
   | SUB expression SUPER expression #subsuperExp
   | unary expression* lbrackets expression rbrackets	#unaryExp	
   | binary lbrackets expression rbrackets #binaryExp
   | Variable lbrackets expression (comma expression)* rbrackets #funExp	//added this
   | expression (SUPER|SUB|DSL|ARR) expression #binaryExp1
   | expression (TIMES|DIV) expression	#binaryExp2
   | expression (PLUS|MINUS) expression #binaryExp3
   | expression (EQ|GT|LT|EGT|ELT) expression #eqExp		//added this
   | MINUS expression	#negExp
   | SUB expression #unarySubExp
   | SUPER expression #unarySuperExp
   | literal	#litExp
   | 'frac' lbrackets literal rbrackets lbrackets literal rbrackets #fracExp
   |  ('f'|'g') expression* lbrackets expression rbrackets #fExp
   | 'd/dx'expression #derivativeOperatorExp
   | expression '!' #factorialExp
   ;
   

literal
   : Number	//	#num
   | Variable//	#var
   ;	

Number
   : DIGIT + (POINT DIGIT +)? //removed minus
   ;

Variable
   : LETTER (LETTER | DIGIT)*
   ;

unary ://unary symbols
   'sqrt' | 'text' | 'bb' |'tilde'//  |'f'  |'g'  
   
    //standard function
   | 'sin' |'cos'  |'tan'  |'sinh'  |'cosh' |'tanh' |'cot' |'sec' |'csc' |'arcsin' |'arccos' |'arctan' |'coth' |'sech' |'csch'
    |'exp' |'abs'|'norm' |'floor' |'ceil' |'log' |'ln' |'det'| 'dim'| 'lim'| 'mod'| 'gcd'| 'lcm'| 'min'| 'max'| 'int'
    //
 |'gcd' |'lcm' | 'hat'|'bar'  |'vec' |'dot' |'ddot' |'ul' |'ubrace'|'obrace' |'cancel' |'bb' |'mathbf' |'sf' |'mathsf'
 |'bbb' |'mathbb'|'cc' |'mathcal'|'tt'|'mathtt' |'fr' |'mathfrak' ;
 

binary :// binary symbols
 'frac'  | 'root'  | 'stackrel' | 'overset' | 'underset' | 'color';
    
    
//*****************TOKEN RULES*******************



lbrackets : '(' #lcbracket | '[' #lcbracket | '{' #lcbracket | '(:' #lcbracket | '{:' #lcbracket;  //leftbrackets       
rbrackets : ')' #rcbracket| ']' #rcbracket | '}' #rcbracket | ':)' #rcbracket | ':}' #rcbracket;//          right brackets

DSL: '//';

ARR:'->';

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


SUPER
   : '^'
   ;
SUB
	: '_'
	;   

comma 		//added this
   : ',' #com 
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