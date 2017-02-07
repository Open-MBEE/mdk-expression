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

public class Tree2UMLExpression_InfixString extends Tree2UMLExpression {
	
	public boolean DEBUG = false;
	public String debug = "";
	public Tree2UMLExpression_InfixString(MathEditorMain1Controller _controller, ParseTree root, ValueSpecification _originalvs){ 
		super(_controller, root, _originalvs);
	}
	
	public ValueSpecification parse(){
		ValueSpecification vs = traverse0(root);
		return vs;
	}

	protected ValueSpecification traverse0(ParseTree n){
		
		System.out.println(n.getClass().getName());
		
		//distinguish between the different cases defined in the grammar and set stop criteria
		if(n instanceof ArithmeticBinaryParser.BinaryExp1Context  || n instanceof ArithmeticBinaryParser.BinaryExp2Context
				|| n instanceof ArithmeticBinaryParser.BinaryExp3Context || n instanceof ArithmeticBinaryParser.EqExpContext){	//=> BINARY EXPRESSION
			
			if (DEBUG){
				traverse0(n.getChild(0));
				debug += n.getChild(1).getText();
				traverse0(n.getChild(2));
				return null;
			}
			try {
				
				ElementValue elemVal = createElementValueFromOperation(n.getChild(1).getText(), null);
				if ( elemVal != null)
				{
					Expression exp = super.getExpression();
					//*********************************TRAVERSE*************************************
					exp.getOperand().add(traverse0(n.getChild(0)));	//left child
					exp.getOperand().add(elemVal);	//add operation to expression infix
					debug += n.getChild(1).getText();
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
		} else if(n instanceof ArithmeticBinaryParser.UnaryExpContext){	//=> UNARY EXPRESSION
		
			if (DEBUG){
				//ie., sin(x), sin_1(x) sin_1^2(xxx)
				debug += n.getChild(0).getText(); //i.e., sin
				for ( int i = 1; i < n.getChildCount(); i++) {
					traverse0(n.getChild(i));
				}
				return null;
			}
			
			try {
			
				//find the correct operation
				ElementValue elemVal = createElementValueFromOperation(n.getChild(0).getText(), AddContextMenuButton.asciiMathLibraryBlock);
				if (elemVal != null){
				
					Expression exp = getExpression();
					//add operation to expression
					exp.getOperand().add(elemVal);
					debug += n.getChild(0).getText(); //i.e., sin
					
					for ( int i = 1; i < n.getChildCount(); i++) {
						exp.getOperand().add(traverse0(n.getChild(i)));
					}
					
					/*
					if ( n.getChildCount() == 3) { //ie., sin(x)
						LiteralString parL = Application.getInstance().getProject().getElementsFactory().createLiteralStringInstance();
						parL.setValue(n.getChild(1).getText());
						LiteralString parR = Application.getInstance().getProject().getElementsFactory().createLiteralStringInstance();
						parR.setValue(n.getChild(3).getText());

						//add operation to expression
						exp.getOperand().add(elemVal);
						//add left parentheses
						exp.getOperand().add(parL);
						//*********************************TRAVERSE*************************************
						exp.getOperand().add(traverse0(n.getChild(2)));
						//add right parentheses
						exp.getOperand().add(parR);
					
					//**********************************RETURN**************************************
					}
					else { //ie., sin_1(x) sin_1^2(xxx)
						
						
					}*/
					return exp;
				}
			}
			catch (Exception e){
				e.printStackTrace();
				showNotAbleToFindError(n.getChild(0).getChild(0).getText(), AddContextMenuButton.customFuncBlock);
				return null;
			}
		
		}
		else if (n instanceof ArithmeticBinaryParser.LcbracketContext || n instanceof ArithmeticBinaryParser.RcbracketContext 
				|| n instanceof ArithmeticBinaryParser.CommaContext) {
			
			if ( DEBUG){
				debug += n.getChild(0).getText();
				return null;
			}
			LiteralString bracket = Application.getInstance().getProject().getElementsFactory().createLiteralStringInstance();
			bracket.setValue(n.getChild(0).getText());
			Expression exp = getExpression();
			exp.getOperand().add(bracket);
			debug += n.getChild(0).getText();
			return exp;
		}
		else if (n instanceof ArithmeticBinaryParser.UnarySuperExpContext 
				|| n instanceof ArithmeticBinaryParser.UnarySubExpContext
				|| 
				n instanceof ArithmeticBinaryParser.DerivativeOperatorExpContext){//^expression, _expression
			if ( DEBUG){
				debug += n.getChild(0).getText();
				for ( int i = 1; i < n.getChildCount(); i++) {
					traverse0(n.getChild(i));
				}
				return null;
			}
			ElementValue elemVal = createElementValueFromOperation(n.getChild(0).getText(), AddContextMenuButton.asciiMathLibraryBlock);
			if ( elemVal != null){
				
				Expression exp = super.getExpression();
				
				//add operation to expression
				exp.getOperand().add(elemVal);
				debug += n.getChild(0).getText();
				for ( int i = 1; i < n.getChildCount(); i++) {
					exp.getOperand().add(traverse0(n.getChild(i)));
				}
				return exp;
			}	
		}
		else if(n instanceof ArithmeticBinaryParser.FunExpContext){	//=> CUSTOMIZED FUNCTION EXPRESSION
			
			if (DEBUG){
				debug += n.getChild(0).getText();
				for ( int i = 1; i < n.getChildCount(); i++) {
					traverse0(n.getChild(i));
				}
				return null;
			}
			
			//find the correct operation
			try{
				ElementValue elemVal = createElementValueFromOperation(n.getChild(0).getText(), AddContextMenuButton.customFuncBlock);
				if ( elemVal != null){
					
					Expression exp = super.getExpression();
				
					//add operation to expression
					exp.getOperand().add(elemVal);//0
				
					for ( int i = 1; i < n.getChildCount(); i++) {
						exp.getOperand().add(traverse0(n.getChild(i)));
					}
					return exp;
				}
				
			} catch(Exception e){
				e.printStackTrace();
				showNotAbleToFindError(n.getChild(0).getText(), AddContextMenuButton.customFuncBlock);
				return null;
			}
			
			
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
		else if(n instanceof ArithmeticBinaryParser.FracExpContext || n instanceof ArithmeticBinaryParser.FExpContext){	
			if ( DEBUG) {
				debug += n.getChild(0).getText();
				for ( int i = 1; i < n.getChildCount(); i++){
					//debug += n.getChild(i).getText();
					traverse0(n.getChild(i));
				}
				return null;
			}
			ElementValue elemVal = createElementValueFromOperation(n.getChild(0).getText(), AddContextMenuButton.asciiMathLibraryBlock);
			if ( elemVal != null){
				
				Expression exp = super.getExpression();
				//add operation to expression
				exp.getOperand().add(elemVal);
				for ( int i = 1; i < n.getChildCount(); i++) {
					exp.getOperand().add(traverse0(n.getChild(i)));
				}
				return exp;
			}	
			
		}

		else if(n instanceof ArithmeticBinaryParser.ParExpContext){	//=> PARENTHESES EXPRESSION
			if ( DEBUG) {
				for ( int i = 0; i < n.getChildCount(); i++)
					traverse0(n.getChild(i));
				return null;
			}
			
			try {
				//******************************DO AND TRAVERSE*********************************
				Expression exp = super.getExpression();
			
				for ( int i = 0; i < n.getChildCount(); i++)
					exp.getOperand().add(traverse0(n.getChild(i)));
				
				return exp;
				
			}
			catch (Exception e){
				e.printStackTrace();
				showNotAbleToFindError(n.getChild(0).getText(), AddContextMenuButton.asciiMathLibraryBlock);
				return null;
			}
			
		}
		else if(n instanceof ArithmeticBinaryParser.NegExpContext){	//=> NEGATIVE EXPRESSION
			
			if (DEBUG){
				debug += n.getChild(0).getText();
				traverse0(n.getChild(1));
				return null;
			}
			
			//ElementValue elemVal = Application.getInstance().getProject().getElementsFactory().createElementValueInstance();
			//find the correct operation
			try{
				ElementValue elemVal = createElementValueFromOperation(n.getChild(0).getText(), AddContextMenuButton.asciiMathLibraryBlock);
				if (elemVal != null){
					
					Expression exp = super.getExpression();
					
					debug+=n.getChild(0).getText();
					//add operation to expression
					exp.getOperand().add(elemVal);
					//*********************************TRAVERSE*************************************
					exp.getOperand().add(traverse0(n.getChild(1)));
					//**********************************RETURN**************************************
					return exp;
				}
				else {
					//not found message is shown during the call to createElementValueFromOperation so just return null
					return null;
				}
				
			}catch(Exception e){
				e.printStackTrace();
				showNotAbleToFindError(n.getChild(0).getText(), AddContextMenuButton.asciiMathLibraryBlock);
				return null;
			}
			
						
		}else if(n instanceof ArithmeticBinaryParser.NegLitExpContext){	//=> NEGATIVE LITERAL EXPRESSION
			
			//find the correct operation
			if (DEBUG){
				debug += n.getChild(0).getText();
				debug += n.getChild(1).getText();
				return null;
			}
			
			try{
				ElementValue elemVal = createElementValueFromOperation(n.getChild(0).getText(), AddContextMenuButton.asciiMathLibraryBlock);
				if (elemVal != null){
					Expression exp = super.getExpression();
					debug+=n.getChild(0).getText();
					
					try{	
						//integer
						try{
							int lInteger = Integer.parseInt(n.getChild(1).getChild(0).getText());
							LiteralInteger lInt = createLiteralInteger();
							lInt.setValue(lInteger);
							//add operation to expression
							exp.getOperand().add(elemVal);
							//add LiteralReal to expression
							exp.getOperand().add(lInt);
							return exp;
						}
						catch (NumberFormatException e){}//ignore
						
						//real
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
		else if(n instanceof ArithmeticBinaryParser.LitExpContext|| n instanceof ArithmeticBinaryParser.LiteralContext){	//=> LITERAL EXPRESSION
			
			System.out.println(n.getChildCount());
			if( DEBUG){
				for ( int i = 0; i < n.getChildCount(); i++){
					debug+=n.getChild(i).getText();
					System.out.println(n.getChild(i).getClass().getCanonicalName());
					System.out.println(debug);
				}
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
				
				//double
				double lRealDouble = Double.parseDouble(n.getChild(0).getText());
				LiteralReal lReal = createLiteralReal();
				lReal.setValue(lRealDouble);
				
				
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
