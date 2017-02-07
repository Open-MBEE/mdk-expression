package gov.nasa.jpl.mbee.mdk.expression;

import gov.nasa.jpl.mbee.mdk.expression.antlr.generated.ArithmeticBinaryParser;

import org.antlr.v4.runtime.tree.ParseTree;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ElementValue;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Expression;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralInteger;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralReal;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralString;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ValueSpecification;

public class Tree2UMLExpression_Prefix extends Tree2UMLExpression {
	
	public boolean DEBUG = false;
	public String debug = "";
	
	public Tree2UMLExpression_Prefix(MathEditorMain1Controller _controller, ParseTree root, ValueSpecification _originalvs) {
		super(_controller, root, _originalvs);
	}
	
	protected ValueSpecification traverse0(ParseTree n){
		
		System.out.println(n.getClass().getName());
		//distinguish between the different cases defined in the grammar and set stop criteria
		if(n instanceof ArithmeticBinaryParser.BinaryExp1Context || n instanceof ArithmeticBinaryParser.BinaryExp2Context
				|| n instanceof ArithmeticBinaryParser.BinaryExp3Context || n instanceof ArithmeticBinaryParser.EqExpContext){	//=> BINARY EXPRESSION
			
			if ( DEBUG){
				debug += n.getChild(1).getText(); 
				traverse0(n.getChild(0));
				traverse0(n.getChild(2));
				return null;
			}
			
			ElementValue elemVal = createElementValueFromOperation(n.getChild(1).getText(), AddContextMenuButton.asciiMathLibraryBlock);
			if (elemVal != null) {
				Expression exp = getExpression();
				//add operation to expression
				exp.getOperand().add(elemVal);
				exp.getOperand().add(traverse0(n.getChild(0)));	//left child
				exp.getOperand().add(traverse0(n.getChild(2)));	//right child
				return exp;
			}
			return null;
			
			
			/*Expression exp = createExpression(n.getChild(1).getText(), AddContextMenuButton.asciiMathLibraryBlock);
			if (exp != null){
				//*********************************TRAVERSE*************************************
				exp.getOperand().add(traverse0(n.getChild(0)));	//left child
				exp.getOperand().add(traverse0(n.getChild(2)));	//right child
			}
			return exp;*/
			
		}
		else if ( n instanceof ArithmeticBinaryParser.FactorialExpContext){
			if (DEBUG){
				for ( int i = 0; i < n.getChildCount()-1; i++) {
					traverse0(n.getChild(i));
				}
				debug += n.getChild(n.getChildCount()-1).getText(); //i.e., sin
				return null;
			}
			ElementValue elemVal = createElementValueFromOperation(n.getChild(n.getChildCount()-1).getText(), AddContextMenuButton.asciiMathLibraryBlock);
			if ( elemVal != null){
				Expression exp = getExpression();

				//add operation to expression
				ValueSpecification rvs;
				for ( int i = 0; i < n.getChildCount()-1; i++) {
					System.out.println(n.getChild(i).getText());
					rvs = traverse0(n.getChild(i)); //lbracket, rbracket, comma
					if ( rvs != null) 
						exp.getOperand().add(rvs);
				}
				exp.getOperand().add(elemVal); //0
				return exp;
			}	
		}
		else if ( //n instanceof ArithmeticBinaryParser.UnarySuperExpContext ||  n instanceof ArithmeticBinaryParser.UnarySubExpContext ||
				n instanceof ArithmeticBinaryParser.DerivativeOperatorExpContext){//^expression

			if (DEBUG){
				//ie., sin(x), sin_1(x) sin_1^2(xxx)
				debug += n.getChild(0).getText(); //i.e., sin
				for ( int i = 1; i < n.getChildCount(); i++) {
					traverse0(n.getChild(i));
				}
				return null;
			}
		
			
			ElementValue elemVal = createElementValueFromOperation(n.getChild(0).getText(), AddContextMenuButton.asciiMathLibraryBlock);
			if ( elemVal != null){
				Expression exp = getExpression();
				//add operation to expression
				exp.getOperand().add(elemVal); //0
				
				ValueSpecification rvs;
				for ( int i = 1; i < n.getChildCount(); i++) {
					System.out.println(n.getChild(i).getText());
					rvs = traverse0(n.getChild(i)); //lbracket, rbracket, comma
					if ( rvs != null) 
						exp.getOperand().add(rvs);
				}
				return exp;
			}	
		}
		else if(n instanceof ArithmeticBinaryParser.UnaryExpContext  ){	//=> UNARY EXPRESSION
			
			if (DEBUG){
				//ie., sin(x), sin_1(x) sin_1^2(xxx)
				debug += n.getChild(0).getText(); //i.e., sin
				for ( int i = 1; i < n.getChildCount(); i++) {
					traverse0(n.getChild(i));
				}
				return null;
			}	
			
			
			ElementValue elemVal = createElementValueFromOperation(n.getChild(0).getText(), AddContextMenuButton.asciiMathLibraryBlock);
			if (elemVal != null) {
				Expression exp = getExpression();
				//add operation to expression
				exp.getOperand().add(elemVal);
				//assuming followed by ()
				//exp.getOperand().add(traverse0(n.getChild(2)));
				
				//not assume followed by ()
				ValueSpecification rvs;
				for ( int i = 1; i < n.getChildCount(); i++)  {
					System.out.println(n.getChild(i).getText());
					rvs = traverse0(n.getChild(i)); //can be lbracket, rbracket, comma
					if ( rvs != null) 
						exp.getOperand().add(rvs);
				}
				return exp;
			}
			
			/*Expression exp = createExpression(n.getChild(0).getChild(0).getText(), AddContextMenuButton.asciiMathLibraryBlock);
			if (exp != null){
				//*********************************TRAVERSE*************************************
				exp.getOperand().add(traverse0(n.getChild(2)));
			}
			return exp;*/
		
		}else if(n instanceof ArithmeticBinaryParser.FunExpContext){	//=> CUSTOMIZED FUNCTION EXPRESSION
			
			if ( DEBUG){
				debug += n.getChild(0).getText();
				for ( int i = 1; i < n.getChildCount(); i++) {
					traverse0(n.getChild(i));
				}
				return null;
			}

			ElementValue elemVal = createElementValueFromOperation(n.getChild(0).getText(), AddContextMenuButton.customFuncBlock);
			if (elemVal != null) {
				Expression exp = getExpression();
				//add operation to expression
				exp.getOperand().add(elemVal);
				
				//not assume , between
				ValueSpecification rvs;
				for ( int i = 1; i < n.getChildCount(); i++)  {
					System.out.println(n.getChild(i).getText());
					rvs = traverse0(n.getChild(i)); //can be lbracket, rbracket, comma
					if ( rvs != null) 
						exp.getOperand().add(rvs);
				}
				
				//*********************************TRAVERSE*************************************
				/*int max = n.getChildCount()-2;
				for(int i=2; i<=max; i=i+2){ //assuming "," between parameters
					exp.getOperand().add(traverse0(n.getChild(i)));
				}
				*/
				
				return exp;
			}
			
		}else if(n instanceof ArithmeticBinaryParser.ParExpContext){	//=> PARENTHESES EXPRESSION
			
			if ( DEBUG) {
				if ( n.getChildCount() == 3)
					traverse0(n.getChild(1));
				else {
					for ( int i = 0; i < n.getChildCount(); i++)
						traverse0(n.getChild(i));
				}
				return null;
			}
			if ( n.getChildCount() == 3){ //i.e., "( a + b )"
				return traverse0(n.getChild(1));
			}
			else { //i.e., "( a,b )"
				Expression exp = getExpression();
				ValueSpecification vs;
				for ( int i = 0; i < n.getChildCount(); i++){
					vs = traverse0(n.getChild(i));
					if ( vs != null )
						exp.getOperand().add(vs);
				}
				return exp;
			}
					
		}else if(n instanceof ArithmeticBinaryParser.NegExpContext){	//=> NEGATIVE EXPRESSION
			if ( DEBUG) {
				debug += n.getChild(0).getText();
				traverse0(n.getChild(1));
				return null;
			}

			
			ElementValue elemVal = createElementValueFromOperation(n.getChild(0).getText(), AddContextMenuButton.asciiMathLibraryBlock);
			if (elemVal != null) {
				Expression exp = getExpression();
				//add operation to expression
				exp.getOperand().add(elemVal);
				exp.getOperand().add(traverse0(n.getChild(1)));
				return exp;
			}
		}
		else if( n instanceof ArithmeticBinaryParser.NegLitExpContext){	//=> NEGATIVE LITERAL EXPRESSION
			if ( DEBUG) {
				debug += n.getChild(0).getText();
				traverse0(n.getChild(1));
				return null;
			}
			
			ElementValue elemVal = createElementValueFromOperation(n.getChild(0).getText(), AddContextMenuButton.asciiMathLibraryBlock);
			if (elemVal != null) {
				Expression exp = getExpression();
				//add operation to expression
				exp.getOperand().add(elemVal);
				//LITERAL REAL or real
				try {
					//integer
					try{
						int lInteger = Integer.parseInt(n.getChild(1).getChild(0).getText());
						LiteralInteger lInt = createLiteralInteger();
						lInt.setValue(lInteger);
						exp.getOperand().add(lInt);
						return exp;
					}
					catch (NumberFormatException e){}//ignore
					
					//real
					double lRealDouble = Double.parseDouble(n.getChild(1).getChild(0).getText());
					LiteralReal lReal = Application.getInstance().getProject().getElementsFactory().createLiteralRealInstance();
					lReal.setValue(lRealDouble);
					//add LiteralReal to expression
					exp.getOperand().add(lReal);
					return exp;
				}
				catch(NumberFormatException e){	//ELEMENT VALUE
					//************************************DO****************************************
					//find the correct operand
					ElementValue elemVal1 = createElementValueFromOperation(n.getChild(1).getChild(0).getText(), AddContextMenuButton.asciiMathLibraryBlock);
					if ( elemVal1 != null)
					{
						//add operand to expression
						exp.getOperand().add(elemVal1);
					}
				}
				return exp;
			}
		}
		else if(n instanceof ArithmeticBinaryParser.LitExpContext){	//=> LITERAL EXPRESSION
			
			if ( DEBUG) {
				debug += n.getChild(0).getText();
				return null;
			}
			
			try{
				//integer
				try{
					int lInteger = Integer.parseInt(n.getChild(0).getText());
					LiteralInteger lInt = createLiteralInteger();
					lInt.setValue(lInteger);
					return lInt;
				}
				catch (NumberFormatException e){}//ignore
				
				//real
				double lRealDouble = Double.parseDouble(n.getChild(0).getText());
				LiteralReal lReal = Application.getInstance().getProject().getElementsFactory().createLiteralRealInstance();
				lReal.setValue(lRealDouble);
				
				//**********************************RETURN**************************************
				return lReal;
				
			}
			catch(NumberFormatException e){	//ELEMENT VALUE
				
				System.out.println("Prefix");
				//find the correct operands -- error message show up if the constraint parameter not found in the current constraint block.
				ElementValue elemVal = createElementValueFromOperands(n.getChild(0).getText(), controller.getConstraintBlock());
				if (elemVal == null){ //not able to find a constraint parameter from the constaint block
					if (askToCreateAConstraintParameter(n.getChild(0).getText())){
						
						//!!!!!!!!!!!!!!! even created not show in the editor????
						try {
							System.out.println("Warn: Couldn't find " + n.getChild(0).getText() + " so try to create the constraint parameter...");
							elemVal = createConstaintParameter(n.getChild(0).getText());
							if ( elemVal != null) {//reset error
								error = false;
								this.controller.addOperand((Property)elemVal.getElement());//add newly created constraintParameter(Property) to the view's listoperandsmodel
								return elemVal;
							}
							else {
								showNotAbleToCreateError( n.getChild(0).getText() ,  controller.getConstraintBlock());
								//error = true; --- it is already set as true when not able to find the constraint parameter.
								//this is how implemented in the original version if the constraint parameter is not found
								return Application.getInstance().getProject().getElementsFactory().createExpressionInstance();
							}
							
						}
						catch(Exception e1){
							e1.printStackTrace();
							System.out.println("Not able to create a constraint parameter - " + e1.getMessage());
							showNotAbleToCreateError( n.getChild(0).getText() ,  controller.getConstraintBlock());
							//this is how implemented in the original version if the constraint parameter is not found
							return Application.getInstance().getProject().getElementsFactory().createExpressionInstance();
						}
					}
					else {
						error = true;
						//this is how implemented in the original version.
						return Application.getInstance().getProject().getElementsFactory().createExpressionInstance();
					}
				}
				else { //found
					return elemVal;
				}
			}
			
		}
		else if (n instanceof ArithmeticBinaryParser.LcbracketContext || n instanceof ArithmeticBinaryParser.RcbracketContext 
				|| n instanceof ArithmeticBinaryParser.CommaContext) {
			
			//ignore
			return null;
		}
		else{ //=> couldn't match any Expression => error
			
			javax.swing.JOptionPane.showMessageDialog(null, "Error: Couldn't match any Expression Context!");
			error = true;
		}
		
		
		return Application.getInstance().getProject().getElementsFactory().createExpressionInstance();
	}
	
}
