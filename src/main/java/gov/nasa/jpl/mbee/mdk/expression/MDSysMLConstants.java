package gov.nasa.jpl.mbee.mdk.expression;

import java.util.Arrays;
import java.util.List;

public class MDSysMLConstants {

    public static final String CONSTRAINTPARAMETER = "ConstraintParameter";
    public static final String CONSTRAINTBLOCK = "ConstraintBlock";
    public static final String OPERATION = "Operation";

    static List<String> suffixParentheses1;
    static List<String> suffixParentheses2;

    static {
        suffixParentheses1 = Arrays.asList(
                  /* standard functions */"Lim", "sin", "cos", "tan", "sinh", "cosh", "tanh", "cot", "sec", "csc", "arcsin", "arccos", "arctan", "coth", "sech", "csch", "exp", "abs", "norm", "floor", "ceil", "log", "ln", "det", "dim", "mod", "gcd", "lcm", "lub", "glb", "min", "max",
				  /*commands with argument*/"sqrt", "root", "dot", "ddot", "text", "mbox", "cancel"
        );
        suffixParentheses2 = Arrays.asList(
				  /*commands with argument */"color", "frac"
        );
    }

    //commands with argument
	/*  {input:"sqrt", tag:"msqrt", output:"sqrt", tex:null, ttype:UNARY},
	  {input:"root", tag:"mroot", output:"root", tex:null, ttype:BINARY},
	  {input:"frac", tag:"mfrac", output:"/",    tex:null, ttype:BINARY},
	  {input:"/",    tag:"mfrac", output:"/",    tex:null, ttype:INFIX},
	  {input:"stackrel", tag:"mover", output:"stackrel", tex:null, ttype:BINARY},
	  {input:"overset", tag:"mover", output:"stackrel", tex:null, ttype:BINARY},
	  {input:"underset", tag:"munder", output:"stackrel", tex:null, ttype:BINARY},
	  {input:"_",    tag:"msub",  output:"_",    tex:null, ttype:INFIX},
	  {input:"^",    tag:"msup",  output:"^",    tex:null, ttype:INFIX},
	  {input:"hat", tag:"mover", output:"\u005E", tex:null, ttype:UNARY, acc:true},
	  {input:"bar", tag:"mover", output:"\u00AF", tex:"overline", ttype:UNARY, acc:true},
	  {input:"vec", tag:"mover", output:"\u2192", tex:null, ttype:UNARY, acc:true},
	 x{input:"dot", tag:"mover", output:".",      tex:null, ttype:UNARY, acc:true},
	 x{input:"ddot", tag:"mover", output:"..",    tex:null, ttype:UNARY, acc:true},
	  {input:"ul", tag:"munder", output:"\u0332", tex:"underline", ttype:UNARY, acc:true},
	  {input:"ubrace", tag:"munder", output:"\u23DF", tex:"underbrace", ttype:UNARYUNDEROVER, acc:true},
	  {input:"obrace", tag:"mover", output:"\u23DE", tex:"overbrace", ttype:UNARYUNDEROVER, acc:true},
	  x{input:"text", tag:"mtext", output:"text", tex:null, ttype:TEXT},
	  x{input:"mbox", tag:"mtext", output:"mbox", tex:null, ttype:TEXT},
	  x{input:"color", tag:"mstyle", ttype:BINARY},
	  x{input:"cancel", tag:"menclose", output:"cancel", tex:null, ttype:UNARY},
	  */
	  
	/*public enum standardFunctions {
	    lim("lim"),
	    Lim("Lim");
		
	    private String fName;

	    standardFunctions(String _s) {
	        this.fName = _s;
	    }

	    public String fName() {
	        return fName;
	    }
	}*/

    //standard functions

}
