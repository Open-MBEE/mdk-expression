package gov.nasa.jpl.mbee.mdk.expression;

import gov.nasa.jpl.mbee.mdk.expression.antlr.generated.ArithmeticBinaryParser;
import org.antlr.v4.runtime.tree.ParseTree;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ElementValue;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Expression;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralReal;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralString;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ValueSpecification;

public class Tree2UMLExpression_InfixString extends Tree2UMLExpression {
	
	public Tree2UMLExpression_InfixString(MathEditorMain1Controller _controller, ParseTree root, ValueSpecification _originalvs){ 
		super(_controller, root, _originalvs);
	}
	
	public ValueSpecification parse(){
		return traverse0(root);		
	}

	protected ValueSpecification traverse0(ParseTree n){
		
		//distinguish between the different cases defined in the grammar and set stop criteria
		if(n instanceof ArithmeticBinaryParser.BinaryExp1Context || n instanceof ArithmeticBinaryParser.BinaryExp2Context
				|| n instanceof ArithmeticBinaryParser.BinaryExp3Context || n instanceof ArithmeticBinaryParser.EqExpContext){	//=> BINARY EXPRESSION
			try {
				
				ElementValue elemVal = createElementValueFromOperation(n.getChild(1).getText(), null);
				if ( elemVal != null)
				{
					Expression exp;// = exp = Application.getInstance().getProject().getElementsFactory().createExpressionInstance();
					if ( isRoot){
						exp = (Expression) originalvs;
						isRoot = false;
					}
					else 
						exp = Application.getInstance().getProject().getElementsFactory().createExpressionInstance();
					//*********************************TRAVERSE*************************************
					exp.getOperand().add(traverse0(n.getChild(0)));	//left child
	
					exp.getOperand().add(elemVal);	//add operation to expression infix
					
					exp.getOperand().add(traverse0(n.getChild(2)));	//right child
					
					//**********************************RETURN**************************************
					return exp;
				}			
			}
			catch (Exception e){
				e.printStackTrace();
				showNotAbleToFindError(n.getChild(0).getChild(0).getText(), AddContextMenuButton.asciiMathLibraryBlock);
				return null;
			}
			
		}else if(n instanceof ArithmeticBinaryParser.UnaryExpContext){	//=> UNARY EXPRESSION
			
			try {
				//find the correct operation
				ElementValue elemVal = createElementValueFromOperation(n.getChild(0).getChild(0).getText(), null);
				if (elemVal != null){
				
					Expression exp;// = Application.getInstance().getProject().getElementsFactory().createExpressionInstance();
					if ( isRoot){
						exp = (Expression) originalvs;
						isRoot = false;
					}
					else
						exp = Application.getInstance().getProject().getElementsFactory().createExpressionInstance();
					LiteralString parL = Application.getInstance().getProject().getElementsFactory().createLiteralStringInstance();
					parL.setValue("(");
					LiteralString parR = Application.getInstance().getProject().getElementsFactory().createLiteralStringInstance();
					parR.setValue(")");
					
					//add operation to expression
					exp.getOperand().add(elemVal);
					//add left parentheses
					exp.getOperand().add(parL);
					//*********************************TRAVERSE*************************************
					exp.getOperand().add(traverse0(n.getChild(2)));
					//add right parentheses
					exp.getOperand().add(parR);
					//**********************************RETURN**************************************
					return exp;
				}
			}
			catch (Exception e){
				e.printStackTrace();
				showNotAbleToFindError(n.getChild(0).getChild(0).getText(), AddContextMenuButton.customFuncBlock);
				return null;
			}
		
		}else if(n instanceof ArithmeticBinaryParser.FunExpContext){	//=> CUSTOMIZED FUNCTION EXPRESSION
			
			//find the correct operation
			try{
				ElementValue elemVal = createElementValueFromOperation(n.getChild(0).getText(), AddContextMenuButton.customFuncBlock);
				if ( elemVal != null){
					
					Expression exp;// = Application.getInstance().getProject().getElementsFactory().createExpressionInstance();
					if ( isRoot){
						exp = (Expression) originalvs;
						isRoot = false;
					}
					else
						exp = Application.getInstance().getProject().getElementsFactory().createExpressionInstance();
					
					LiteralString parL = createLiteralString();
					parL.setValue("(");
					LiteralString parR = createLiteralString();
					parR.setValue(")");

					//add operation to expression
					exp.getOperand().add(elemVal);
					//add left parentheses
					exp.getOperand().add(parL);
					
					//*********************************TRAVERSE*************************************
					int max = n.getChildCount()-2;
					for(int i=2; i<=max; i=i+2){
						exp.getOperand().add(traverse0(n.getChild(i)));
						if(i<max){
							LiteralString com = createLiteralString();
							com.setValue(",");
							exp.getOperand().add(com);
						}
					}
					//add right parentheses
					exp.getOperand().add(parR);
					//**********************************RETURN**************************************
					return exp;
				}
				
			} catch(Exception e){
				e.printStackTrace();
				showNotAbleToFindError(n.getChild(0).getText(), AddContextMenuButton.customFuncBlock);
				return null;
			}
			
			
		}
		else if(n instanceof ArithmeticBinaryParser.ParExpContext){	//=> PARENTHESES EXPRESSION
			
			try {
				//******************************DO AND TRAVERSE*********************************
				Expression exp;// = Application.getInstance().getProject().getElementsFactory().createExpressionInstance();
				if ( isRoot){
					exp = (Expression) originalvs;
					isRoot = false;
				}
				else
					exp = Application.getInstance().getProject().getElementsFactory().createExpressionInstance();
				
				LiteralString parL = createLiteralString();
				parL.setValue("(");
				LiteralString parR = createLiteralString();
				parR.setValue(")");
				
				exp.getOperand().add(parL);
				
				exp.getOperand().add(traverse0(n.getChild(1)));
				
				exp.getOperand().add(parR);
				
				//**********************************RETURN************************************
				return exp;
			}
			catch (Exception e){
				e.printStackTrace();
				showNotAbleToFindError(n.getChild(0).getText(), AddContextMenuButton.asciiMathLibraryBlock);
				return null;
			}
			
		}
		else if(n instanceof ArithmeticBinaryParser.NegExpContext){	//=> NEGATIVE EXPRESSION
			
			//************************************DO****************************************
			
			//ElementValue elemVal = Application.getInstance().getProject().getElementsFactory().createElementValueInstance();
			//find the correct operation
			try{
				ElementValue elemVal = createElementValueFromOperation(n.getChild(0).getText(), AddContextMenuButton.asciiMathLibraryBlock);
				if (elemVal != null){
					
					Expression exp;// = Application.getInstance().getProject().getElementsFactory().createExpressionInstance();
					if ( isRoot){
						exp = (Expression) originalvs;
						isRoot = false;
					}
					else
						exp = Application.getInstance().getProject().getElementsFactory().createExpressionInstance();
					
					//add operation to expression
					exp.getOperand().add(elemVal);
					//*********************************TRAVERSE*************************************
					exp.getOperand().add(traverse0(n.getChild(1)));
					//**********************************RETURN**************************************
					return exp;
				}
				else {
					showNotAbleToFindError(n.getChild(0).getText(), AddContextMenuButton.asciiMathLibraryBlock);
					return null;
				}
				
			}catch(Exception e){
				e.printStackTrace();
				showNotAbleToFindError(n.getChild(0).getText(), AddContextMenuButton.asciiMathLibraryBlock);
				return null;
			}
			
						
		}else if(n instanceof ArithmeticBinaryParser.NegLitExpContext){	//=> NEGATIVE LITERAL EXPRESSION
			
			//find the correct operation
			try{
				ElementValue elemVal = createElementValueFromOperation(n.getChild(0).getText(), AddContextMenuButton.asciiMathLibraryBlock);
				if (elemVal != null){
					Expression exp;// = Application.getInstance().getProject().getElementsFactory().createExpressionInstance();
					if ( isRoot){
						exp = (Expression) originalvs;
						isRoot = false;
					}
					else
						exp = Application.getInstance().getProject().getElementsFactory().createExpressionInstance();
					
					try{	//LITERAL REAL
						
						//************************************DO****************************************
						double lRealDouble = Double.parseDouble(n.getChild(1).getChild(0).getText());
						LiteralReal lReal = createLiteralReal();
						lReal.setValue(lRealDouble);
						
						//add operation to expression
						exp.getOperand().add(elemVal);
						//add LiteralReal to expression
						exp.getOperand().add(lReal);
						
						//**********************************RETURN**************************************
						return exp;
						
					} catch(NumberFormatException e){	//ELEMENT VALUE

						try{
							//find the correct operand
							ElementValue elemVal1 = createElementValueFromOperation(n.getChild(1).getChild(0).getText(), AddContextMenuButton.asciiMathLibraryBlock);
							if ( elemVal1 != null ){
								//add operation to expression
								exp.getOperand().add(elemVal);
								//add operand to expression
								exp.getOperand().add(elemVal1);
							
								//**********************************RETURN**************************************
								return exp;	
							}
							else
								return null;
						} catch(Exception e1){
							e1.printStackTrace();
							showNotAbleToFindError(n.getChild(1).getChild(0).getText(), AddContextMenuButton.asciiMathLibraryBlock);
							return null;	
						}
					}
				}
				else {
					showNotAbleToFindError(n.getChild(0).getText(), AddContextMenuButton.asciiMathLibraryBlock);
					return null;
				}
			}catch(Exception e){
				e.printStackTrace();
				showNotAbleToFindError(n.getChild(0).getText(), AddContextMenuButton.asciiMathLibraryBlock);
				return null;
			}
		}
		else if(n instanceof ArithmeticBinaryParser.LitExpContext){	//=> LITERAL EXPRESSION
			
			try{	//LITERAL REAL
				
				//************************************DO****************************************
				double lRealDouble = Double.parseDouble(n.getChild(0).getText());
				LiteralReal lReal = createLiteralReal();
				lReal.setValue(lRealDouble);
				
				//**********************************RETURN**************************************
				return lReal;
				
			}
			catch(NumberFormatException e){	//ELEMENT VALUE

				System.out.println("InfixString-2nd button");
				//find the correct operands -- error message show up if the constraint parameter not found in the current constraint block.
				ElementValue elemVal = createElementValueFromOperands(n.getChild(0).getText(), controller.getConstraintBlock());
				if (elemVal == null){  //not able to find a constraint parameter from the constaint block
					if (askToCreateAConstraintParameter(n.getChild(0).getText())){
						try {
							System.out.println("Warn: Couldn't find " + n.getChild(0).getText() + " so try to create the constraint parameter...");
							elemVal = createConstaintParameter(n.getChild(0).getText());
							if ( elemVal != null) { //sucess creating the constraint parameter
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
							System.out.println(e1.getMessage());
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
				else { //found Element Value n.getChild(0).getText();
					return elemVal;
				}
			}
			
		}
		else{ //=> couldn't match any Expression => error
			
			showMessage("Error: Couldn't match any Expression Context!");
			error = true;
		}
		
		return Application.getInstance().getProject().getElementsFactory().createExpressionInstance();
	}
	
}
