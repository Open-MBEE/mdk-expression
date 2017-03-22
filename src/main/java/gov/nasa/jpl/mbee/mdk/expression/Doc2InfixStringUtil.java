package gov.nasa.jpl.mbee.mdk.expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Doc2InfixStringUtil {

	public static final HashMap<String, String> FN = new HashMap<String, String>(){
		private static final long serialVersionUID = 1L;

	{
		put("msub", "_");
		put("msup", "^");
		put("mfrac", "/");
		put("msqrt", "sqrt");
		put("mroot", "root");
		/*<munder> lim_(x->0)
        <mo>lim</mo>
        <mrow>
          <mi>x</mi>
          <mo>/u...</mo>
          <mn>0</mn>
        </mrow>
      </munder>*/
		put("munder", "_");
		put("mover", "^");
	}};
	
	public static enum TType {UNARY, MOVERORMUNDER, BINARY, INFIX, TEXT };

/*	
	//mover tag
//	<mover>
//  <mi>y</mi>
//  <mo>..</mo>
// </mover>
	//miscellaneous symbols
	{input:"tilde", tag:"mover", output:"~", tex:null, ttype:UNARY, acc:true},
	
	//commands with argument
	{input:"stackrel", tag:"mover", output:"stackrel", tex:null, ttype:BINARY},
	{input:"overset", tag:"mover", output:"stackrel", tex:null, ttype:BINARY},
	
	{input:"hat", tag:"mover", output:"\u005E", tex:null, ttype:UNARY, acc:true},
	{input:"bar", tag:"mover", output:"\u00AF", tex:"overline", ttype:UNARY, acc:true},
	{input:"vec", tag:"mover", output:"\u2192", tex:null, ttype:UNARY, acc:true},
	{input:"dot", tag:"mover", output:".",      tex:null, ttype:UNARY, acc:true},
	{input:"ddot", tag:"mover", output:"..",    tex:null, ttype:UNARY, acc:true},
	{input:"obrace", tag:"mover", output:"\u23DE", tex:"overbrace", ttype:UNARYUNDEROVER, acc:true},
	
	//munder
	{input:"underset", tag:"munder", output:"stackrel", tex:null, ttype:BINARY},
	{input:"ul", tag:"munder", output:"\u0332", tex:"underline", ttype:UNARY, acc:true},
	{input:"ubrace", tag:"munder", output:"\u23DF", tex:"underbrace", ttype:UNARYUNDEROVER, acc:true},
*/
	
	//public static final HashMap<String, String> UNARYUNDEROVER = new HashMap<String, String>(){{

		/*
		// </mover>
		//miscellaneous symbols
		{input:"tilde", tag:"mover", output:"~", tex:null, ttype:UNARY, acc:true},
		//commands with argument
		{input:"stackrel", tag:"mover", output:"stackrel", tex:null, ttype:BINARY},
		{input:"overset", tag:"mover", output:"stackrel", tex:null, ttype:BINARY},
		{input:"hat", tag:"mover", output:"\u005E", tex:null, ttype:UNARY, acc:true},
		{input:"bar", tag:"mover", output:"\u00AF", tex:"overline", ttype:UNARY, acc:true},
		{input:"vec", tag:"mover", output:"\u2192", tex:null, ttype:UNARY, acc:true},
		{input:"dot", tag:"mover", output:".",      tex:null, ttype:UNARY, acc:true},
		{input:"ddot", tag:"mover", output:"..",    tex:null, ttype:UNARY, acc:true},
		{input:"obrace", tag:"mover", output:"\u23DE", tex:"overbrace", ttype:UNARYUNDEROVER, acc:true},

		//munder
		{input:"underset", tag:"munder", output:"stackrel", tex:null, ttype:BINARY},
		{input:"ul", tag:"munder", output:"\u0332", tex:"underline", ttype:UNARY, acc:true},
		{input:"ubrace", tag:"munder", output:"\u23DF", tex:"underbrace", ttype:UNARYUNDEROVER, acc:true},
		*/
		
//		//mover
//		put("~", "tilde");
//		put("stackrel", "stackrel");
//		put("overset", "stackrel");
//		put("\u005E", "hat");
//		put("\u00AF", "bar");
//		put("\u2192", "vec");
//		put(".", "dot");
//		put("..","ddot");
//		put("\u23DE","obrace");
//		
//		//munder
//		put("stackrel", "underset");
//		put("\u0332", "ul");
//		put("\u23DF", "ubrace");
	//}};

	
	
	//commands with argument
	public static final HashMap<String, TType> COMMAND_W_ARGS = new HashMap<String ,TType>() {
		private static final long serialVersionUID = 1L;

	{
    	    	
    	put ("tilde", TType.UNARY);
		
		put(/*input:"sqrt", tag:*/"msqrt",/*( output:"sqrt", tex:null,*/ TType.UNARY);
    	put(/*"cancel", tag:*/"menclose", /*output:"cancel", tex:null,*/ TType.UNARY);
    
    	
    	//unary but 2nd tag is function type
    	put("munder", TType.MOVERORMUNDER); /////////////////////////// this maybe the same as msub
        put("mover", TType.MOVERORMUNDER); /////////////////////////// this maybe the same as mover

        put(/*"root", tag:*/"mroot", /*/*output:"root", tex:null,*/ TType.BINARY);
        
        //TODO how to handle mfrac infix and mfrac binary
    	//put(/*"frac", tag:*/"mfrac", /*/*output:"/",    tex:null,*/ TType.BINARY);
    	put(/*"color", tag:*/"mstyle",TType.BINARY);
    	
    	put(/*"/",    tag:*/"mfrac", /*/*output:"/",    tex:null,*/ TType.INFIX);  //The both inputs "frac(a)(b)" or "a/b" are <mfrac><mi>a</mi><mi>b</mi></mfrac> to both return as "a/b" 
    	put(/*"_",    tag:*/"msub",  /*output:"_",    tex:null,*/ TType.INFIX);
    	put(/*"^",    tag:*/"msup",  /*output:"^",    tex:null,*/ TType.INFIX);
    	
    	//mtext
    	put(/*"text", tag:*/"mtext", /*output:"text", tex:null,*/ TType.TEXT);
    	//put(/*"mbox", tag:*/"mtext", /*output:"mbox", tex:null,*/ TType.TEXT);
	}};
		
	public static final HashMap<String, String> MI = new HashMap<String , String>() {
		private static final long serialVersionUID = 1L;

	{
	    //some greek symbols
    	put("\u03B1", "alpha");
    	put("\u03B2","beta");
    	put("\u03C7","chi");
    	put("\u03B4","delta");
    	put("\u03B5","epsilon"); //epsi 	put("epsi",   "\u03B5", tex:"epsilon",*/ TType.CONST);
    	put("\u025B","varepsilon");
    	put("\u03B7","eta");
    	put("\u03B3","gamma");
    	put("\u03B9","iota");
    	put("\u03BA","kappa");
    	put("\u03BB","lambda");
    	//put("\u03BB","lamda");
    	put("\u03BC","mu");
    	put("\u03BD","nu");
    	put("\u03C9","omega");
    	//TODO: not sure how to handle this tag
    	//put("phi",    tag:*/"mi", /*output:fixphi?"\u03D5":"\u03C6", tex:null,*/ TType.CONST);
    	//put("varphi", tag:*/"mi", /*output:fixphi?"\u03C6":"\u03D5", tex:null,*/ TType.CONST);
    	//put("\u03D5","phi");
    	//put("\u03C6", "varphi");
    	
    	put("\u03C6","phi");
    	put("\u03D5", "varphi");
    	
    	put("\u03C0","pi");
    	put("\u03C8","psi");
    	put("\u03A8","Psi");
    	put("\u03C1","rho");
    	put( "\u03C3","sigma");
    	put("\u03C4","tau");
    	put("\u03B8","theta");
    	put("\u03D1","vartheta");
    	put("\u03C5", "upsilon");
    	put("\u03BE","xi");
    	put("\u03B6","zeta");
    	//greek
    	put("\u0394","Delta");
    	put("\u0393","Gamma");
    	put("\u039B","Lambda");
    	put("\u03A9","Omega");
    	put("\u03A6","Phi");
    	put("\u03A0","Pi");
    	put("\u03A3","Sigma");
    	put("\u0398","Theta");
    	put("\u039E","Xi");
	}};

	
	//miscellaneous symbols
//put("dx",   "{:d x:}", tex:null,*/ TType.DEFINITION);
//put("dy",   "{:d y:}", tex:null,*/ TType.DEFINITION);
//put("dz",   "{:d z:}", tex:null,*/ TType.DEFINITION);
//put("dt",   "{:d t:}", tex:null,*/ TType.DEFINITION);

	public static final HashMap<String, String> MO = new HashMap<String , String>() {/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	{
		//TODO check if duplicate keys
		    	
		    	//binary operation symbols
		    	put("\u22C5","*");
		    	put("\u2217","**");
		    	put("\u22C6","***");
		    	// put("/","//");
		    	put("\\","\\\\");
		    	put("\\","setminus");
		    	put("\u00D7","xx");
		    	put("\u22C9","|><");
		    	put("\u22CA", "><|");
		    	put("\u22C8","|><|");
		    	put("\u00F7","-:");
		    	put("-:","divide");
		    	put("\u2218", "@");
		    	put("\u2295","o+");
		    	put("\u2297","ox");
		    	put("\u2299","o.");
		    	put("\u2211", "sum");
		    	put("\u220F","prod");
		    	put("\u2227","^^");
		    	put("\u22C0","^^^");
		    	put("\u2228","vv");
		    	put("\u22C1","vvv");
		    	put("\u2229","nn");
		    	put("\u22C2","nnn");
		    	put("\u222A","uu");
		    	put("\u22C3","uuu");
		    	//binary relation symbols
		    	put("\u2260","!=");
		    	//put(":=",":=");
		    	put("<", "lt");
		    	put("\u2264","<=");
		    	put("\u2264","lt=");
		    	put(">","gt");
		    	put("\u2265",">=");
		    	put("\u2265","gt=");
		    	put("\u227A","-<");
		    	put("\u227A","-lt");
		    	put("\u227B",">-");
		    	put("\u2AAF","-<=");
		    	put("\u2AB0",">-=");
		    	put("\u2208","in");
		    	put("\u2209", "!in");
		    	put("\u2282", "sub");
		    	put("\u2283", "sup");
		    	put("\u2286", "sube");
		    	put("\u2287", "supe");
		    	put("\u2261", "-=");
		    	put("\u2245", "~=");
		    	put("\u2248", "~~");
		    	put("\u221D", "prop");
		    	//logical symbols
		    	put("\u00AC","not");
		    	put("\u21D2", "=>");
		    	//put("if",  "if",     tex:null,*/ TType.SPACE);
		    	put("\u21D4", "<=>");
		    	put("\u2200", "AA");
		    	put("\u2203", "EE");
		    	put("\u22A5", "_|_");
		    	put("\u22A4", "TT");
		    	put("\u22A2", "|--");
		    	put("\u22A8", "|==");
		    	//grouping brackets

		    	put("\u2329", "(:");
		    	put("\u232A",":)");
		    	put("\u2329", "<<");
		    	put("\u232A", ">>");
		    	//put("{:", "{:");
		    	//put(":}", ":}");
		    	//miscellaneous symbols
		    	put("\u222B", "int");
		    	put("\u222E","oint");
		    	put("\u2202", "del");
		    	put("\u2207", "grad");
		    	put("\u00B1", "+-");
		    	put("\u2205", "O/");
		    	put("\u221E", "oo");
		    	put("\u2135","aleph");
		    	//put("...",  "...",    tex:"ldots",*/ TType.CONST);
		    	put("\u2234",":.");
		    	put("\u2235",  ":'");
		    	put("\u2220",  "/_");
		    	put("\u25B3","/_\\");
		    	put("\u2032",  "'");
		    	//put("~", "tilde"); //{input:"tilde", tag:"mover", output:"~", tex:null, ttype:UNARY, acc:true},
		    	put("\u00A0","\\ ");
		    	put("\u2322","frown");
		    	put("\u00A0\u00A0","quad");
		    	put("\u00A0\u00A0\u00A0\u00A0","qquad");
		    	put("\u22EF","cdots");
		    	put("\u22EE","vdots");
		    	put("\u22F1","ddots");
		    	put("\u22C4","diamond");
		    	put("\u25A1","square");
		    	put("\u230A","|__");
		    	put("\u230B", "__|");
		    	put("\u2308",  "|~");
		    	put("\u2309",  "~|");
		    	put("\u2102","CC");
		    	put("\u2115","NN");
		    	put("\u211A","QQ");
		    	put("\u211D","RR");
		    	put("\u2124","ZZ");
		    	//arrows
		    	put("\u2191", "uarr");
		    	put("\u2193", "darr");
		    	
		    	//right arrow
		    	//Handled in code in "mo" tag.  vec's parent is always mover
		    	//put("\u2192", "rarr");
		    	//put("\u2192", "->");
		    	//put("\u2192", "vec");
		    	
		    	put("\u21A3", ">->");
		    	put("\u21A0", "->>");
		    	put("\u2916", ">->>");
		    	put("\u21A6", "|->");
		    	put("\u2190", "larr");
		    	put("\u2194", "harr");
		    	put("\u21D2", "rArr");
		    	put("\u21D0", "lArr");
		    	put("\u21D4", "hArr");
		    	
		    	//command with argument 
		    	//mover
				//put("~", "tilde");//////////////asciimath parser does not recognize "tilde"
				put("stackrel", "stackrel");
				//put("stackrel", "overset");//////////////asciimath parser does not recognize "overset"
				put("\u005E", "hat");
				put("\u00AF", "bar");
				
				put(".", "dot");
				put("..","ddot");
				//put("\u23DE","obrace");//////////////asciimath parser does not recognize "obrace"
				
				//munder
				//put("stackrel", "underset");//////////////asciimath parser does not recognize "underset"
				put("\u0332", "ul");
				//put("\u23DF", "ubrace");//////////////asciimath parser does not recognize "ubrace"
				
				
				//Added by mw
				put("\u2223", "|"); //| in windows is considered as \u2223 in asciimathparser <mo>|</mo>  between mo is \u2223 not "|"
		    	
}};
	public static List<Node> getChildElementNodes(Node n)
	{
		List<Node> elementNodes = new ArrayList<Node>();
		NodeList nl = n.getChildNodes();
		Node nc;
		    for (int i = 0; i < nl.getLength(); i++) {
		           nc = nl.item(i);
		           if (nc.getNodeType() == Node.ELEMENT_NODE){
		        	   elementNodes.add(nc);
		           }
		    }
		    return elementNodes;
	}
	static boolean skipNL;
	public static String printXML(Node rootNode) {
		String tab = "";
		skipNL = false;
		return(printXML(rootNode, tab));
	}
	public static String printXML(Node rootNode, String tab) {
		String print = "";
		if(rootNode.getNodeType()==Node.ELEMENT_NODE) {
			print += "\n"+tab+"<"+rootNode.getNodeName()+">";
		}
		NodeList nl = rootNode.getChildNodes();
		if(nl.getLength()>0) {
			for (int i = 0; i < nl.getLength(); i++) {
				print += printXML(nl.item(i), tab+"  ");    // \t
			}
		} else {
			if(rootNode.getNodeValue()!=null) {
				print = rootNode.getNodeValue();
			}
			skipNL = true;
		}
		if(rootNode.getNodeType()==Node.ELEMENT_NODE) {
			if(!skipNL) {
				print += "\n"+tab;
			}
			skipNL = false;
			print += "</"+rootNode.getNodeName()+">";
		}
		return(print);
	}
}
