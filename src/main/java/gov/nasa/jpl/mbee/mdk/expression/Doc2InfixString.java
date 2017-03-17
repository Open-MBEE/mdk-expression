package gov.nasa.jpl.mbee.mdk.expression;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.tree.ParseTree;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ElementValue;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Expression;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralInteger;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralReal;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralString;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ValueSpecification;

public class Doc2InfixString  extends Tree2UMLExpression {
	 
	private class Vs {
		
		public ValueSpecification value;
		public int offset;
		public Vs(ValueSpecification _value, int _offset){
			value = _value;
			offset = _offset;
		}
	}
	
	Document root;
	
	public Doc2InfixString(MathEditorMain1Controller _controller, Document _root){
		super(_controller, null, null);
		this.root = _root;
	}
	@Override
	protected ValueSpecification traverse0(ParseTree n) throws Exception { 
		//DocumentElement document = DOMBuilder.getInstance().createJeuclidDom(root, true, true);
	    return(traverse0x(root));
	}
	protected ValueSpecification traverse0x(Node n) throws Exception {
		
		Expression exp = getExpression();
		Node node = root.getElementsByTagName("math").item(0);
		//Node node = root.getElementsByTagName("semantics").item(0);
		//stringExpression = root.getElementsByTagName("annotation").item(0).getFirstChild().getNodeValue();
		return processChildrens(node, exp);
	}

	
	//f(x) or g(xy) then <mi>f</mi><mrow><mo>(</mo><mi>x</mi><mo>)</mo></mrow> then f is created from asciilibrary's f
	private String isNextSiblingMRowEnclosedbyBrackets(Node sibiling) {
		try{
			if ( sibiling.getNodeName().equals("mrow")){
				List<Node>scs = Doc2InfixStringUtil.getChildElementNodes(sibiling);
				//at least 3 parameters "(" a parameter and ")" 
				if ( scs.size() >= 3 && scs.get(0).getNodeName().equals("mo") && scs.get(0).getFirstChild().getNodeValue().equals("(") 
					&& scs.get(scs.size()-1).getNodeName().equals("mo") && scs.get(scs.size()-1).getFirstChild().getNodeValue().equals(")"))
					return createStringNodeValue(scs);
			}
			return null;
		}
		catch (Exception e) {return null;}
	}
	//constract string from nodevalues;
	private String createStringNodeValue(List<Node>scs){
		String s = "";
		for(Node n: scs){
			s+=n.getFirstChild().getNodeValue();
		}
		return s;
	}
	private Vs getPositiveOrNegativeNumber(Node n)
	{
		String s = "";
		int i = -1;
		if (n.getNodeName().equals("mo") && (
				n.getFirstChild().getNodeValue().equals("-")
				||n.getFirstChild().getNodeValue().equals("+"))) {
			s+=n.getFirstChild().getNodeValue(); // + or -
			i++;
		}
		else
			return null;
		if (n.getNextSibling() != null && n.getNextSibling().getNodeName().equals("mn")){ //next is number
			if (n.getPreviousSibling() == null || 
					(!n.getPreviousSibling().getNodeName().equals("mi") && !n.getPreviousSibling().getNodeName().equals("mn")) ){
				s+=n.getNextSibling().getFirstChild().getNodeValue();
				return new Vs( getNumber(s), ++i);
			}
		}
		return null;
	}
	private ValueSpecification createAndAddElementValueBracket(String moValue){
		if (isBracket(moValue)) 
			return createBracket(moValue);
		else
			return null;
	}
	private Vs createConstraintParameter(String s, int offset)  throws Exception {
		ElementValue ev;
		if (askToCreateAConstraintParameter(s)){
			try {
				System.out.println("Warn: Couldn't find " + s + " so try to create the constraint parameter...");
				ev = createConstaintParameter(s);
				if ( ev != null) { //success creating the constraint parameter
					this.controller.addOperand((Property)ev.getElement());//add newly created constraintParameter(Property) to the view's listoperandsmodel
					return new Vs(ev, offset); //n itself and n's sibling msub(and childrens)
				}
				else {
					throw new Exception("Not able to create " +  s + " in " + controller.getConstraintBlock().getHumanName() + "!");
				}
			}
			catch(Exception e1){
				e1.printStackTrace();
				throw new Exception("Not able to create " +  s + " in " + controller.getConstraintBlock().getHumanName() + "!");
			}
		}
		else {
			throw new Exception ("\"" + s + "\" is not a constraint parameter nor custom function.");
		}
	}
	private Vs getMisMns(Node n, boolean searchSibiling) throws Exception
	{
		String s = "";
		String t;
		int i = -1;
		Node previous_node = null;
		while ((t = n.getNodeName()).equals("mi") || t.equals("mn")){
			s+= this.toStringFromUnicodeMI(n.getFirstChild().getNodeValue());
			i++;
			if (!searchSibiling)
			  break;
			previous_node = n;
			n = n.getNextSibling();
			if (n == null)
				break;
		}
		if ( s.length() == 1 && ( s.equals("f")|| s.equals("g")) ) {
			String bracketEnclosedString = isNextSiblingMRowEnclosedbyBrackets(n); //(n at this point is original n's sibiling) f(x) or g(x+y)... 
			if ( bracketEnclosedString != null) {
				ElementValue ev = createElementValueFromOperands(s + bracketEnclosedString, controller.getConstraintBlock());
				if ( ev != null)
					return new Vs(ev, i+1); //f(x) or g(x) etc... so next mrow with () info is already processed so offset is +1
				else //create constraint parameter or throw exception
					return createConstraintParameter(s + bracketEnclosedString, i+1);
			}
			//else means is "f" or "g" a constraint parameter processed by following code
		}
		
		ElementValue ev;
		//xy_123 <mi>x <msub><mi>y<mn>123....
		Node msub;
		if(previous_node != null && (msub = previous_node.getNextSibling()) != null && msub.getNodeName().equals("msub")){
			//assume msub always has two childrens.
			Node firstChild = msub.getFirstChild();
			Node secondChild = firstChild.getNextSibling();
			if ( (firstChild.getNodeName().equals("mi")|| firstChild.getNodeName().equals("mn"))
					&&
					(secondChild.getNodeName().equals("mi")|| secondChild.getNodeName().equals("mn"))){
				s+=toStringFromUnicodeMI(firstChild.getFirstChild().getNodeValue()) + "_" + toStringFromUnicodeMI(secondChild.getFirstChild().getNodeValue());
				ev = createElementValueFromOperands(s, controller.getConstraintBlock());
				if ( ev != null)
					return new Vs(ev, i+1); //n itself and n's sibling msub(and childrens)
				else
					return createConstraintParameter(s, i+1);
			}
		}
		//msub was not mimn's sibling or not valid msub to be a constraint parameter.
		
		//search from constraintblock
		ev = createElementValueFromOperands(s, controller.getConstraintBlock());
		if ( ev == null) {//may be customFunction
			ev = createElementValueFromOperation(s, MDKExpressionPlugin.customFuncBlock);
			if (ev == null)  //not able to find a constraint parameter from the constaint block
				return createConstraintParameter(s, i);
			else
				return new Vs(ev, i); //custom function
		}
		else
			return new Vs(ev, i); //is constraint parameter
	}
	private Vs isSubscriptedConstraintParameter(Node n, String s) throws Exception
	{
		int i = -1;
		if( n.getNodeName().equals("mi")){
			s+= n.getFirstChild().getNodeValue();
			i++;
		}
		Node sibiling = n.getNextSibling();
		if ( sibiling.getNodeName().equals("mn")){
			s = this.toStringFromUnicodeMI(s);
			s+="_";
			s+= sibiling.getFirstChild().getNodeValue();
		}
		else
			return null; //it is easier to handle by returning null if it is not subscripted constraint parameter.
		//search from constraintblock
		ElementValue ev = createElementValueFromOperands(s, controller.getConstraintBlock());
		if ( ev == null) 
			throw new Exception ("\"" + s + "\" is not a constraint parameter.");
		return new Vs(ev, i); //is constraint parameter
	}
	
	private boolean isConstraintParameter(Node n, int allchildrensNumber){ //starts with mi and sibiling must be all mi and mn
		String s = "";
		int counter = 0;
		if ( n.getNodeName().equals("mi")){
			s+= n.getFirstChild().getNodeValue();
			counter++;
		}
		else
			return false;// not start with <mi> alphabet
		Node sibiling;
		while ((sibiling = n.getNextSibling()) != null){
			if ( sibiling.getNodeName().equals("mi") || sibiling.getNodeName().equals("mn")){
				s+= sibiling.getFirstChild().getNodeValue();
				n = sibiling;
				counter++;
			}
			else 
				return false;
		}
		if ( counter != allchildrensNumber)
			return false;
		ElementValue ev = createElementValueFromOperands(s, controller.getConstraintBlock());
		if ( ev == null) //not exist as a constraint parameter
			return false;
		else
			return true;
	}
	
	//TODO may need to improve
	private boolean needBrackets(List<Node> childNodes){
		//a constraint parameter enclosed by mrow means () is purposely added around the constraint parameter.
		//if (isConstraintParameter(childNodes.get(0), childNodes.size()))
			//return false;
	
		if ( childNodes.get(0).getNodeName().equals("mo") && isBracket(childNodes.get(0).getFirstChild().getNodeValue()))//1st mrow child is a bracket "(" etc..
				return false;
			
			/*
	not need 													need
	<mrow>														<mrow>
      <msubsup>														<mi>y</mi>
        <mi>z</mi>
        <mn>12</mn>												</mrow>
        <mn>34</mn>
      </msubsup>
    </mrow>
			 */
			/* need
		<mrow>
            <mfrac> #of child = 1
              <mi>a</mi>
              <mi>b</mi>
            </mfrac>
        </mrow>
			 */
		//todo other case like mfrac????
		else if ( childNodes.size() == 1 && childNodes.get(0).hasChildNodes() && childNodes.get(0).getFirstChild().getNodeType() != Node.TEXT_NODE 
				&& !childNodes.get(0).getNodeName().equals("msub") && !childNodes.get(0).getNodeName().equals("mfrac") && !childNodes.get(0).getNodeName().equals("msqrt") && !childNodes.get(0).getNodeName().equals("mroot")) {
			return false;
		}
			//else if if 2 childNodes of number like -100 +10, then () may not needed  
		else if ( childNodes.size() == 2 && (
					//sin(a+b) 
					(childNodes.get(0).getNodeName().equals("mo") && childNodes.get(1).getNodeName().equals("mrow") && isBracket(childNodes.get(1).getFirstChild().getFirstChild().getNodeValue()) ) 
					||
					//-a <mo>-</mo><mi>a</mi> || cos a <mo>cos</mo><mi>a</mi>
					(childNodes.get(0).getNodeName().equals("mo") /*&& (childNodes.get(0).getFirstChild().getNodeValue().equals("-") || childNodes.get(0).getFirstChild().getNodeValue().equals("-"))*/
							&& childNodes.get(1).getNodeName().equals("mi"))
					
					||
					//f or g always included in mrow and childnode number is always 2 but () is not necessary
					(childNodes.get(0).getNodeName().equals("mi") &&  (childNodes.get(0).getFirstChild().getNodeValue().equals("f")||childNodes.get(0).getFirstChild().getNodeValue().equals("g")))
					||
					(childNodes.get(0).getNodeName().equals("mi") && childNodes.get(0).getFirstChild().getNodeValue().equals("d") &&
							childNodes.get(1).getNodeName().equals("mi") &&
							createElementValueFromOperation(childNodes.get(0).getFirstChild().getNodeValue() + childNodes.get(1).getFirstChild().getNodeValue(), MDKExpressionPlugin.asciiMathLibraryBlock) != null ) //can be dx, dy, dz, dt etc...
					)
				)
			return false;
		else 
			return true;
	}

	public static String unescapeJava(String in) {
	    StringBuilder b = new StringBuilder();

	    for (char c : in.toCharArray()) {
	        if (c >= 128)
	            b.append("\\u").append(String.format("%04X", (int) c));
	        else
	            b.append(c);
	    }

	    return b.toString();
	}
	private Vs processNode(Node n, boolean searchSibiling) throws Exception{
		
		String temp;
		boolean tempb;
		Vs tempVs = null;
			if ( n.getNodeName().equals("mn")) //number
				return new Vs(getNumber(n, ""), 0); //nextIndex = i+1 automatically
			else if(searchSibiling && (tempVs = getPositiveOrNegativeNumber(n)) != null){ //number prefix by + or -
				return tempVs;
			} 
			else if ( n.getNodeName().equals("mo")) {//operator
				temp = n.getFirstChild().getNodeValue(); //movalue
				ValueSpecification b = createAndAddElementValueBracket(temp); //brackets and comma do not need conversion
				if ( b != null) //it was bracket or comma
					return new Vs(b, 0);
				else {
					/*if ( temp.equals("\u2217"))
						System.out.println("**");
					if (temp.equals("\u22C6"))
						System.out.println("***");
			    	*/
					
					//\u2192 can be vec, ->, or rarr.  for vec's case its parent is mover.  rarr is return as "->" because no way to distinguish(?)
					if ( temp.equals("\u2192")){
						if ( n.getParentNode().getNodeName().equals("mover"))
							temp = "vec";
						else
							temp = "->"; // rarr and -> both save uml as "->"
					}
					else {
						String text = unescapeJava(temp);
						System.out.println(text);
					
						temp = toStringFromUnicodeMO(temp);
					}
				
					ElementValue ev = createElementValueFromOperation(temp, MDKExpressionPlugin.asciiMathLibraryBlock);
					if (ev == null){
						String cp = toStringFromUnicodeMI(temp);
						if ( !cp.equals(temp)){
							ev = createElementValueFromOperands(cp, controller.getConstraintBlock()); //Delta, Gamma,... capital greek characters
							if ( ev == null)
								throw new Exception("\"" + temp + "\" is not defined as a constraint parameter.");
						}
						else
							throw new Exception("\"" + temp + "\" is not in AsciiMath Library");
					}
					return new Vs(ev, 0);
				}
			}
			else if (n.getNodeName().equals("mi"))
				return getMisMns(n, searchSibiling);
			else if (n.getNodeName().equals("mrow")){
				Expression expNew = getExpression();
				int parentOffset = 0;
				List<Node> nl = Doc2InfixStringUtil.getChildElementNodes(n); 
				if ( tempb = needBrackets(nl))
					expNew.getOperand().add(createBracket("("));
				for ( int i = 0; i < nl.size(); i++){
					tempVs = processNode(nl.get(i), true);// searchSibiling);
					if ( tempVs == null){ //TODO this may not necessary  - this was to handle special case like <mrow><mi>f<mi>u</mrow><mi>n where fun is customized function "f" caused problem - but this is not nesessay because of DocPreprocess added now.
						if ( nl.get(i).getNodeName().equals("mi")){
							Object[] sibilingsS =  getMiMnsInSibilings(nl.get(i));
							String possible = nl.get(i).getFirstChild().getNodeValue() + sibilingsS[0];
							if ( ((int)sibilingsS[1] + 1)== nl.size()){ //all children's mrow can be a variable.
								Object[] mimnParentSibilings = getMiMnsInSibilings(n); //n = mrow
								if ( ((String)mimnParentSibilings[0]).length() > 0){
									possible = possible + mimnParentSibilings[0];
									ElementValue ev = createElementValueFromOperands(possible, controller.getConstraintBlock());
									if ( ev == null)
										ev = createElementValueFromOperation(possible, MDKExpressionPlugin.customFuncBlock);
										if (ev == null)
											throw new Exception( "\"" + possible + "\" is not a constraint parameter or custom function.");
									parentOffset = (int) mimnParentSibilings[1];
									tempVs = new Vs(ev, (int)sibilingsS[1]);
								}
								else
									throw new Exception( "\"" + possible + "\" is not a constraint parameter or custom function.");
							}
							else
								throw new Exception( "\"" + possible + "\" is not a constraint parameter or custom function.");
							
						}
						else
							throw new Exception ("Not supported - problem in " + nl.get(i).getNodeName());
					}
					expNew.getOperand().add(tempVs.value);
					i = i + tempVs.offset;
				}
				
				if (nl.get(nl.size()-1).getNodeName().equals("mi")) {
				    /*<mrow> tan abc123a wher abc123a is a constraint parameter
				      <mo>tan</mo>
				      <mi>a</mi>
				    </mrow>
				    <mi>b</mi>
				    <mi>c</mi>
				    <mn>123</mn>
				    <mi>a</mi>
					*/
					Object[] mimnsibilings = getMiMnsInSibilings(n); //tanabc123a where abc123a is a constraint parameter then first a is in mrow's last mi, bc123a are in next <mi>b<mi>c<mn>123<mi>a so this postfix has bc123a
					if ( ((String)mimnsibilings[0]).length() > 0 ){
						String possibleconstraintvariable = (String)getMiMnsMyselfAndPreviousSibilings(nl.get(nl.size()-1))[0] + (String)mimnsibilings[0];
						ElementValue ev = createElementValueFromOperands(possibleconstraintvariable, controller.getConstraintBlock());
						if (ev != null){
							//remove from a variable (ie., "a")
							int lastIndex = expNew.getOperand().size() -1;
							expNew.getOperand().remove(lastIndex);
							//add a variable (ie., "abc123a")
							expNew.getOperand().add(ev);
							parentOffset = (int) mimnsibilings[1];
						}
						//else not do anyhing.  For example tanxy where x is constraint parameter but xy is not a constraint parameter, then not do anything here
							//throw new Exception("The constraint parameter \"" + possibleconstraintvariable + "\" not found.");
					}
				}
				if ( tempb )expNew.getOperand().add(createBracket(")"));
				return new Vs(expNew, parentOffset);
			}
			else if ( Doc2InfixStringUtil.COMMAND_W_ARGS.get(n.getNodeName()) == Doc2InfixStringUtil.TType.UNARY) {//msqrt)
				Expression expNew = getExpression();
				//ie., sqrt
				ElementValue ev = createElementValueFromOperation(Doc2InfixStringUtil.FN.get(n.getNodeName()), MDKExpressionPlugin.asciiMathLibraryBlock);
				expNew.getOperand().add(ev);
				
				List<Node> nl = Doc2InfixStringUtil.getChildElementNodes(n); 
				if ( nl.size() != 1)
					throw new Exception ("Not supported - problem in " + n + ". The function should have one argument.");
				
				tempVs = processNode(nl.get(0), false);
				if ( tempVs == null)
					throw new Exception ("Not supported - problem in " + nl.get(0).getNodeName());
				expNew.getOperand().add(tempVs.value);
				
				return new Vs(expNew, 0);
			}
			else if ( Doc2InfixStringUtil.COMMAND_W_ARGS.get(n.getNodeName()) == Doc2InfixStringUtil.TType.BINARY) {//root 
				Expression expNew = getExpression();
				
				ElementValue ev = createElementValueFromOperation(Doc2InfixStringUtil.FN.get(n.getNodeName()), MDKExpressionPlugin.asciiMathLibraryBlock);
				expNew.getOperand().add(ev);
				
				List<Node> nl = Doc2InfixStringUtil.getChildElementNodes(n); 
				if( nl.size() != 2) { //root and one argument 
					throw new Exception ("Not supported - problem in " + n + ". The function should have two arguments.");
				}
				for ( int i = 0; i < 2; i++){
					tempVs = processNode(nl.get(i), false);
					if ( tempVs == null)
						throw new Exception ("Not supported - problem in " + nl.get(i).getNodeName());
					expNew.getOperand().add(tempVs.value);
				}
				return new Vs(expNew, 0);
			}
			//both are mover/munder but hat case requires flipping 2 childrens and lim case is not 
			
			/*
			hat(ab) bar(xy) ulA vec v dotx ddoty"	
				<math>
			    <mover>
			      <mrow>
			        <mi>a</mi>
			        <mi>b</mi>
			      </mrow>
			      <mo>^</mo>
			    </mover>
			    <mover>
			      <mrow>
			        <mi>x</mi>
			        <mi>y</mi>
			      </mrow>
			      <mo>¯</mo>
			    </mover>
			    <munder>
			      <mi>A</mi>
			      <mo>?</mo>
			    </munder>
			    <mover>
			      <mi>v</mi>
			      <mo>?</mo>
			    </mover>
			    <mover>
			      <mi>x</mi>
			      <mo>.</mo>
			    </mover>
			    <mover>
			      <mi>y</mi>
			      <mo>..</mo>
			    </mover>
			  </math>

		    lim(h->0)
		    <munder>
		      <mo>lim</mo>
		      <mrow>
		        <mi>h</mi>
		        <mo>?</mo>
		        <mn>0</mn>
		      </mrow>
		    </munder>
			*/
			else if ( Doc2InfixStringUtil.COMMAND_W_ARGS.get(n.getNodeName()) == Doc2InfixStringUtil.TType.MOVERORMUNDER) {//mover or munder
				Expression expNew = getExpression();
				List<Node> nl = Doc2InfixStringUtil.getChildElementNodes(n); 
				if(nl.size() != 2) { //2 children arguments 
					throw new Exception ("Not supported - problem in " + n + ". The function should have two arguments.");
				}
				for ( int i = 0; i < 2; i++){
					tempVs = processNode(nl.get(i), false);
					if ( tempVs == null)
						throw new Exception ("Not supported - problem in " + nl.get(i).getNodeName());
					expNew.getOperand().add(tempVs.value);
				}
				//flip exp  hat bar vec dot ddot ul (ie., <mover><mi>a</mi><mo>vec</mo></mover> "a vec" to "vec a" , "ab"bar to bar(ab)", "aul" to "ula"
				//no need to add mover(^) or munder(_)
				if (n.getChildNodes().item(1).getNodeName().equals("mo")){
					ValueSpecification exp1 = expNew.getOperand().get(0);
					ValueSpecification exp2 = expNew.getOperand().get(1);
					expNew.getOperand().clear();
					expNew.getOperand().add(exp2); 
					//expNew = createAndAddElementValueToExpression_asciiMathLibraryBlock(expNew, Doc2InfixStringUtil.FN.get(nl.get(i).getNodeName())); //mover not need to add this cases
					expNew.getOperand().add(exp1);
				}
				else{
					///*lim_(x->0) <munder> <mo>lim</mo><mrow><mi>x</mi><mo>-></mo><mn>0</mn></mrow></munder>*/
					//adding "^" or "-" to expNew (ab to a^b or a_b)
					ValueSpecification exp1 = expNew.getOperand().get(1);
					expNew.getOperand().remove(1); 
					expNew.getOperand().add(createElementValueFromOperation(Doc2InfixStringUtil.FN.get(n.getNodeName()), MDKExpressionPlugin.asciiMathLibraryBlock)); //adding ^ or _
					expNew.getOperand().add(exp1);
				}
				return new Vs(expNew, 0);
			}
			//msub can be a constraint paremter like x_1
			else if (n.getNodeName().equals("msub")) { //msub
				Expression expNew = getExpression();

				List<Node> nl = Doc2InfixStringUtil.getChildElementNodes(n); 
				if(nl.size() != 2) { //2 children arguments 
					throw new Exception ("Not supported - problem in " + n + ". The function should have two arguments.");
				}
				//x_12(constraint parameter)
				if (nl.get(0).getNodeName().equals("mi") ) {//may like be x_1
					if ( nl.get(1).getNodeName().equals("mn") || nl.get(1).getNodeName().equals("mi")){ //x_1 or a_i
						String s= toStringFromUnicodeMI(nl.get(0).getFirstChild().getNodeValue()) + "_" + toStringFromUnicodeMI(nl.get(1).getFirstChild().getNodeValue());
						ElementValue ev = createElementValueFromOperands(s, controller.getConstraintBlock());
						if ( ev != null)
							return new Vs(ev, 0);
					}
					//msub with 1st child mi must be a constraint parameter (ie., x_1)
					throw new Exception (nl.get(0).getFirstChild().getNodeValue() + "_" + nl.get(1).getFirstChild().getNodeValue() + " is not a constraint parameter.");
				}
				// not a constraint parameter like x_1 or a_i
				tempVs = processNode(nl.get(0), false);
				if ( tempVs == null)
					throw new Exception ("Not supported - problem in " + nl.get(0).getNodeName());
				expNew.getOperand().add(tempVs.value);

				//adding "^" or "-" to expNew (ab to a^b or a_b)
				if ( tempVs.offset == 0) {
					expNew.getOperand().add(createElementValueFromOperation(Doc2InfixStringUtil.FN.get(n.getNodeName()), MDKExpressionPlugin.asciiMathLibraryBlock)); //_ 
					tempVs = processNode(nl.get(1), false);
					if ( tempVs == null)
						throw new Exception ("Not supported - problem in " + nl.get(1).getNodeName());
					expNew.getOperand().add(tempVs.value);
				}
				return new Vs(expNew, 0);
			}
			else if ( Doc2InfixStringUtil.COMMAND_W_ARGS.get(n.getNodeName()) == Doc2InfixStringUtil.TType.INFIX) { //mfrac msup
				Expression expNew = getExpression();
				List<Node> nchilds = Doc2InfixStringUtil.getChildElementNodes(n); 
				if(nchilds.size() != 2) { //2 children arguments 
					throw new Exception ("Not supported - problem in " + n + ". The function should have two arguments.");
				}
				tempVs = processNode(nchilds.get(0), false);
				if ( tempVs == null)
					throw new Exception ("Not supported - problem in " + nchilds.get(0).getNodeName());
				
				
				//handle for d/dx d/dy... dy/dx where y is constraint parameter and d/dx is operator
				if ( n.getNodeName().equals("mfrac")){
						boolean possibleOperator = true;
						temp = null;
						if( nchilds.get(0).getNodeName().equals("mi") &&  nchilds.get(1).getNodeName().equals("mrow")){
							temp = nchilds.get(0).getFirstChild().getNodeValue(); //d
							if (temp.equals("d")){
								temp += Doc2InfixStringUtil.FN.get(n.getNodeName()); // /
								List<Node> mrowchilds = Doc2InfixStringUtil.getChildElementNodes(nchilds.get(1));
								if ( mrowchilds.get(0).getNodeName().equals("mi") && mrowchilds.get(0).getFirstChild().getNodeValue().equals("d")){
									temp+="d";
									if ( mrowchilds.get(1).getNodeName().equals("mi"))
										temp+= mrowchilds.get(1).getFirstChild().getNodeValue();
									else
										possibleOperator = false;
								}
								else 
									possibleOperator = false;
								
								if ( possibleOperator == true && temp != null){
									ElementValue ev = createElementValueFromOperation(temp, MDKExpressionPlugin.asciiMathLibraryBlock); //ie., d/dx
									if ( ev != null){
										expNew.getOperand().add(ev);
										return new Vs(expNew, 0);
									}
								}
							}
							
						}
						else if ( nchilds.get(0).getNodeName().equals("mrow") && nchilds.get(1).getNodeName().equals("mrow")){
							List<Node> firstmrowchilds = Doc2InfixStringUtil.getChildElementNodes(nchilds.get(0));
							if ( firstmrowchilds.get(0).getNodeName().equals("mi") && firstmrowchilds.get(0).getFirstChild().getNodeValue().equals("d")){
								String constraintparameter = "";
								temp = "d";
								temp += Doc2InfixStringUtil.FN.get(n.getNodeName()); // /
								for ( int j = 1; j < firstmrowchilds.size(); j++){
									if ( firstmrowchilds.get(j).getNodeName().equals("mi"))
										constraintparameter+=firstmrowchilds.get(j).getFirstChild().getNodeValue();
									else {
										possibleOperator = false;
										break;
									}
								} //end for forloop
								List<Node> secondmrowchilds = Doc2InfixStringUtil.getChildElementNodes(nchilds.get(1));
								if ( secondmrowchilds.get(0).getNodeName().equals("mi") && secondmrowchilds.get(0).getFirstChild().getNodeValue().equals("d")){
									temp+="d";
									if ( secondmrowchilds.get(1).getNodeName().equals("mi"))
										temp+= secondmrowchilds.get(1).getFirstChild().getNodeValue();
									else
										possibleOperator = false;
								}
								else 
									possibleOperator = false;
								
								if ( possibleOperator == true && temp != null){
									ElementValue ev = createElementValueFromOperation(temp, MDKExpressionPlugin.asciiMathLibraryBlock); //ie., d/dx
									if ( ev != null){
										expNew.getOperand().add(ev);
										ElementValue cpev = createElementValueFromOperands(constraintparameter, controller.getConstraintBlock());
										if ( cpev != null){
											expNew.getOperand().add(cpev);
											return new Vs(expNew, 0);
										}
									}
								}
							}
						}
				}
				
				expNew.getOperand().add(tempVs.value);

				//adding "^" or "-" to expNew (ab to a^b or a_b)
				expNew.getOperand().add(createElementValueFromOperation(Doc2InfixStringUtil.FN.get(n.getNodeName()), MDKExpressionPlugin.asciiMathLibraryBlock)); //adding ^ or _ or /
				tempVs = processNode(nchilds.get(1), false);
				if ( tempVs == null)
					throw new Exception ("Not supported - problem in " + nchilds.get(1).getNodeName());
				expNew.getOperand().add(tempVs.value);
				return new Vs(expNew, 0);
			}
			
			else if (n.getNodeName().equals("msubsup") || n.getNodeName().equals("munderover")){
				Expression expNew = getExpression();
				List<Node> nl = Doc2InfixStringUtil.getChildElementNodes(n); 
				if(nl.size() != 3) { //3 children arguments 
					throw new Exception ("Not supported - problem in " + n.getNodeName() + ". The function should have three arguments.");
				}
				
				//adding "_" or "^" to expNew  (abc to a_b^c)				
				//i = 0
				if (nl.get(0).getNodeName().equals("mi")) {//may like be x_1
					tempVs = processNode(nl.get(0), true);
				}
				else
					tempVs = processNode(nl.get(0), false);
				if ( tempVs == null)
					throw new Exception ("Not supported - problem in " + nl.get(0).getNodeName());
				expNew.getOperand().add(tempVs.value);
				expNew = createAndAddElementValueToExpression_asciiMathLibraryBlock(expNew, "_");//"^ or "_"
				//i = 1 to 2
				for ( int i = 1; i < 3; i++){
					tempVs = processNode(nl.get(i), false);
					if ( tempVs == null)
						throw new Exception ("Not supported - problem in " + nl.get(i).getNodeName());
					expNew.getOperand().add(tempVs.value);
					if ( i == 1)
						expNew = createAndAddElementValueToExpression_asciiMathLibraryBlock(expNew, "^");//"^ or "_"
				}
				return new Vs(expNew, 0);
			}
			else if (n.getNodeName().equals("mtd")){
				Expression expNew = getExpression();
				List<Node> nl = Doc2InfixStringUtil.getChildElementNodes(n); 
				for ( int i = 0; i < nl.size(); i++) { 
					tempVs = processNode(nl.get(i), true);
					if ( tempVs == null)
						throw new Exception ("Not supported - problem in " + nl.get(i).getNodeName());
					expNew.getOperand().add(tempVs.value);
					i = i + tempVs.offset;
				}
				return new Vs(expNew, 0);
			}
			else if ( n.getNodeName().equals("mtr")){
				Expression expNew = getExpression();
				List<Node> nl = Doc2InfixStringUtil.getChildElementNodes(n); 
				for ( int i = 0; i < nl.size(); i++) { 
					tempVs = processNode(nl.get(i), false);
					if ( tempVs == null)
						throw new Exception ("Not supported - problem in " + nl.get(i).getNodeName());
					expNew.getOperand().add(tempVs.value);
					i = i + tempVs.offset;
				}
				return new Vs(addCommandBetweenOperands(expNew,  getEnclosedBracketSet(n.getParentNode())), 0);
			}
			else if (n.getNodeName().equals("mtable")){
				Expression expNew = getExpression();
				List<Node> nl = Doc2InfixStringUtil.getChildElementNodes(n); 
				for ( int i = 0; i < nl.size(); i++) { 
					tempVs = processNode(nl.get(i), false);
					if ( tempVs == null)
						throw new Exception ("Not supported - problem in " + nl.get(i).getNodeName());
					expNew.getOperand().add(tempVs.value);
					i = i + tempVs.offset;
				}
				return new Vs(addCommandBetweenOperands(expNew,  null), 0);
				
			}
			else 
				throw new Exception ("\""+ n.getNodeName() + "\" not supported.");
			
	 }
	 Expression processChildrens(Node n, Expression exp) throws Exception{
		
		List<Node> nl = Doc2InfixStringUtil.getChildElementNodes(n);
		Vs vs;
		for ( int i = 0; i < nl.size(); i++){
			vs = processNode(nl.get(i), true);
			exp.getOperand().add(vs.value);
			i = i + vs.offset;
		}
		return exp;
	}
	private Object[] getMiMnsMyselfAndPreviousSibilings(Node n){
		String nname;
		String s = n.getFirstChild().getNodeValue();
		Node sibiling;
		int counter = 0;
	
		while( (sibiling = n.getPreviousSibling())!= null){
			if ((nname = sibiling.getNodeName()).equals("mi") || nname.equals("mn")){
				s+= sibiling.getFirstChild().getNodeValue();
				n = sibiling;
				counter++;
			}
			else
				break;
		}
		return new Object[]{s, counter};
	}
	private Object[] getMiMnsInSibilings(Node n){
		String nname;
		String s= "";
		Node sibiling;
		int counter = 0;
		while( (sibiling = n.getNextSibling())!= null){
			if ((nname = sibiling.getNodeName()).equals("mi") || nname.equals("mn")){
				s+= sibiling.getFirstChild().getNodeValue();
				n = sibiling;
				counter++;
			}
			else
				break;
		}
		return new Object[]{s, counter};
	}
	private Object[] getEnclosedBracketSet(Node n){
		while ( !n.getNodeName().equals("mtable")){
			n = n.getParentNode();
		}
		//n is mtable
		//assmes n.getNextSibiling has ending bracket of the same type.
		if (n.getPreviousSibling().getNodeName().equals("mo")){
			String s = n.getPreviousSibling().getFirstChild().getNodeValue();
			if ( s.equals("["))
				return new Object[] {"[", "]"};
			else if ( s.equals("("))
				return new Object[] {"(", ")"};
			else if ( s.equals("{"))
				return new Object[] {"{", "}"};
			else if ( s.equals("(:"))
				return new Object[] {"(:", ":)"};
			else if ( s.equals("{:"))
				return new Object[] {"{:", ":}"};
		}
		return null;
	
	}
	private Expression addCommandBetweenOperands(Expression _exp, Object[] enclosedBrackets ){
		Expression expNew = getExpression();
		if ( enclosedBrackets != null)
			expNew.getOperand().add(createBracket((String)enclosedBrackets[0]));
		List<ValueSpecification> tempList = new ArrayList<ValueSpecification>();
		for ( int j = 0; j < _exp.getOperand().size(); j++) {
			if (j != 0) 
				tempList.add(createBracket(","));
			tempList.add(_exp.getOperand().get(j));
		}
		for ( int i = 0; i < tempList.size(); i++)
			expNew.getOperand().add(tempList.get(i));
		if ( enclosedBrackets != null)
			expNew.getOperand().add(createBracket((String)enclosedBrackets[1]));
		return expNew;
	}

	private String toStringFromUnicodeMO(String _s){
		String fromUnicode = Doc2InfixStringUtil.MO.get(_s);
		if ( fromUnicode != null && !fromUnicode.equals(_s))
			_s = fromUnicode; //replace _s
		return _s;
	}
	
	
	//convert to ie., /u... to "alpha" 
	//convert if necessary for mi
	private String toStringFromUnicodeMI(String _s){
	
		if (_s.length() == 1){
			String fromUnicode = Doc2InfixStringUtil.MI.get(_s);
			if ( fromUnicode != null && !fromUnicode.equals(_s))
				_s = fromUnicode;//replace _s
		}
		return _s;
	}
	private LiteralString createBracket(String _s){
		LiteralString bracket = Application.getInstance().getProject().getElementsFactory().createLiteralStringInstance();
		bracket.setValue(_s);
		return bracket;
	}
	private boolean isBracket(String moValue){
		if ( moValue.equals("(") || moValue.equals("[")|| moValue.equals("{") || moValue.equals("(:") || moValue.equals("{:")  //leftbrackets       
    			|| moValue.equals(")") || moValue.equals("]") ||moValue.equals("}") ||moValue.equals(":)") ||moValue.equals(":}") //right brackets 
    			|| moValue.equals(",") )  
			return true;
		else
			return false;
		
	}
	private Expression createAndAddElementValueToExpression_asciiMathLibraryBlock(Expression exp, String variableString){
		variableString = toStringFromUnicodeMI(variableString);
		ElementValue  elemVal = createElementValueFromOperation(variableString, MDKExpressionPlugin.asciiMathLibraryBlock);
		if ( elemVal != null)
			exp.getOperand().add(elemVal);
		else
			return null;
		return exp;
	}
	private ValueSpecification getNumber(String _num){
		try{
			int lInteger = Integer.parseInt(_num);
			LiteralInteger lInt = createLiteralInteger();
			lInt.setValue(lInteger);
			return lInt;
		}
		catch (NumberFormatException e){}//ignore
		//double
		double lRealDouble = Double.parseDouble(_num);
		LiteralReal lReal = createLiteralReal();
		lReal.setValue(lRealDouble);
		return lReal;
	}
	private ValueSpecification getNumber(Node n, String prefix){
		try{
			int lInteger = Integer.parseInt(prefix + n.getFirstChild().getNodeValue());
			LiteralInteger lInt = createLiteralInteger();
			lInt.setValue(lInteger);
			return lInt;
		}
		catch (NumberFormatException e){}//ignore
		
		//double
		double lRealDouble = Double.parseDouble(prefix + n.getFirstChild().getNodeValue());
		LiteralReal lReal = createLiteralReal();
		lReal.setValue(lRealDouble);
		return lReal;
	}
		
}
