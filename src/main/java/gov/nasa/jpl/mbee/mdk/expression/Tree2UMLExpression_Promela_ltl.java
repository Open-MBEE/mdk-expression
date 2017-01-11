package gov.nasa.jpl.mbee.mdk.expression;

import gov.nasa.jpl.mbee.mdk.expression.antlr.generated.ArithmeticBinaryParser;

import org.antlr.v4.runtime.tree.ParseTree;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ElementValue;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Expression;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralReal;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ValueSpecification;

/// originally Tree2UMLExpressionInfix
public class Tree2UMLExpression_Promela_ltl extends Tree2UMLExpression {
	
	public Tree2UMLExpression_Promela_ltl(MathEditorMain1Controller _controller, ParseTree root, ValueSpecification _originalvs) {
		super(_controller, root, _originalvs);
	}
	
	protected ValueSpecification traverse0(ParseTree n){
		
		System.out.println(n.getClass().getName());
		//distinguish between the different cases defined in the grammar and set stop criteria
		if(n instanceof ArithmeticBinaryParser.BinaryExp1Context || n instanceof ArithmeticBinaryParser.BinaryExp2Context
				|| n instanceof ArithmeticBinaryParser.BinaryExp3Context || n instanceof ArithmeticBinaryParser.EqExpContext){	//=> BINARY EXPRESSION
			
			System.out.println("1:" + n.getChild(1).getText());
			System.out.println("0:" + n.getChild(0).getText());
			System.out.println("2:" + n.getChild(2).getText());
			System.out.println("Traverse child0 - left child");
			traverse0(n.getChild(0));
			System.out.println("Traverse child2 - right child");
			traverse0(n.getChild(2));
			
			return null;
			
			/*ElementValue elemVal = createElementValueFromOperation(n.getChild(1).getText(), AddContextMenuButton.asciiMathLibraryBlock);
			if (elemVal != null) {
				Expression exp;// = Application.getInstance().getProject().getElementsFactory().createExpressionInstance();
				if (isRoot){
					exp = (Expression) originalvs;
					isRoot = false;
				}
				else
					exp =  Application.getInstance().getProject().getElementsFactory().createExpressionInstance();
				//add operation to expression
				exp.getOperand().add(elemVal);
				exp.getOperand().add(traverse0(n.getChild(0)));	//left child
				exp.getOperand().add(traverse0(n.getChild(2)));	//right child
				return exp;
			}
			return null;
			*/
			
		
		}else if(n instanceof ArithmeticBinaryParser.UnaryExpContext){	//=> UNARY EXPRESSION
			
			System.out.println("0 child :" + n.getChild(0).getChild(0).getText());
			System.out.println("2:" + n.getChild(2).getText());
			System.out.println( "traverse child2");
			traverse0(n.getChild(2));
			return null;
			/*ElementValue elemVal = createElementValueFromOperation(n.getChild(0).getChild(0).getText(), AddContextMenuButton.asciiMathLibraryBlock);
			if (elemVal != null) {
				Expression exp;// = Application.getInstance().getProject().getElementsFactory().createExpressionInstance();
				if (isRoot) {
					exp = (Expression) originalvs;
					isRoot = false;
				}
				else
					exp =  Application.getInstance().getProject().getElementsFactory().createExpressionInstance();
				//add operation to expression
				exp.getOperand().add(elemVal);
				exp.getOperand().add(traverse0(n.getChild(2)));
				return exp;
			}
			*/
			
		
		
		}else if(n instanceof ArithmeticBinaryParser.FunExpContext){	//=> CUSTOMIZED FUNCTION EXPRESSION
			
			System.out.println("0 child :" + n.getChild(0).getChild(0).getText());
			
			int max = n.getChildCount()-2;
			for(int i=2; i<=max; i=i+2){
				System.out.println( "traverse child " + i);
				traverse0(n.getChild(i));
			}
		
			return null;
			
			
			/*ElementValue elemVal = createElementValueFromOperation(n.getChild(0).getText(), AddContextMenuButton.customFuncBlock);
			if (elemVal != null) {
				Expression exp;
				if (isRoot){
					exp = (Expression) originalvs;
					isRoot = false;
				}
				else
					exp =  Application.getInstance().getProject().getElementsFactory().createExpressionInstance();
				//add operation to expression
				exp.getOperand().add(elemVal);
				//*********************************TRAVERSE*************************************
				int max = n.getChildCount()-2;
				for(int i=2; i<=max; i=i+2){
					exp.getOperand().add(traverse0(n.getChild(i)));
				}
				
				return exp;
			}
			*/
			
		}else if(n instanceof ArithmeticBinaryParser.ParExpContext){	//=> PARENTHESES EXPRESSION
			
			
			System.out.println(n.getChild(1));
			System.out.println("traverse child1");
			traverse0(n.getChild(1));
			return null;
			//**************************TRAVERSE AND RETURN*********************************
			//return traverse0(n.getChild(1));
			
		}else if(n instanceof ArithmeticBinaryParser.NegExpContext){	//=> NEGATIVE EXPRESSION
			
			
			System.out.println("0:" + n.getChild(0).getText());
			System.out.println("1:" + n.getChild(1).getText());
			System.out.println("traverse child1");
			traverse0(n.getChild(1));
			return null;

			/*ElementValue elemVal = createElementValueFromOperation(n.getChild(0).getText(), AddContextMenuButton.asciiMathLibraryBlock);
			if (elemVal != null) {
				Expression exp;
				if (isRoot){
					exp = (Expression) originalvs;
					isRoot = false;
				}
				else
					exp =  Application.getInstance().getProject().getElementsFactory().createExpressionInstance();
				//add operation to expression
				exp.getOperand().add(elemVal);
				exp.getOperand().add(traverse0(n.getChild(1)));
				return exp;
			}
			*/
			
		}
		else if(n instanceof ArithmeticBinaryParser.NegLitExpContext){	//=> NEGATIVE LITERAL EXPRESSION
			System.out.println("0:" + n.getChild(0).getText());
			System.out.println("1 child0:" + n.getChild(1).getChild(0).getText());
			return null;

			
			/*ElementValue elemVal = createElementValueFromOperation(n.getChild(0).getText(), AddContextMenuButton.asciiMathLibraryBlock);
			if (elemVal != null) {
				Expression exp;
				if (isRoot){
					exp = (Expression) originalvs;
					isRoot = false;
				}
				else
					exp =  Application.getInstance().getProject().getElementsFactory().createExpressionInstance();
				//add operation to expression
				exp.getOperand().add(elemVal);
				//LITERAL REAL
				try {
					double lRealDouble = Double.parseDouble(n.getChild(1).getChild(0).getText());
					LiteralReal lReal = Application.getInstance().getProject().getElementsFactory().createLiteralRealInstance();
					lReal.setValue(lRealDouble);
					//add LiteralReal to expression
					exp.getOperand().add(lReal);
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
			*/
		}
		else if(n instanceof ArithmeticBinaryParser.LitExpContext){	//=> LITERAL EXPRESSION
			
			System.out.println("0:" + n.getChild(0).getText());
			try{
				System.out.println("is real?");
				double lRealDouble = Double.parseDouble(n.getChild(0).getText());
				System.out.println("no");
				return null;
			}
			catch( NumberFormatException e){
				System.out.println("no");
			}
			return null;
			/*
			try{	//LITERAL REAL
				
				//************************************DO****************************************
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
			*/
			
		}
		else{ //=> couldn't match any Expression => error
			
			javax.swing.JOptionPane.showMessageDialog(null, "Error: Couldn't match any Expression Context!");
			error = true;
			
			//javax.swing.JOptionPane.showMessageDialog(null, "class: " + n.getClass().toString());
		
		}
		
		
		return Application.getInstance().getProject().getElementsFactory().createExpressionInstance();
	}
	
}
