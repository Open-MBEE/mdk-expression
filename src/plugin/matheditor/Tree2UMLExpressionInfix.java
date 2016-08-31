package plugin.matheditor;

import org.antlr.v4.runtime.tree.ParseTree;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ElementValue;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Expression;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralReal;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralString;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ValueSpecification;

import asciimathml.parser.ArithmeticBinaryParser;


public class Tree2UMLExpressionInfix {
	
	private ParseTree root;
	private Element[] operations, operands, functions;
	private String[] operationsString, operandsString, customFuncString;
	public static boolean error = false;
	
	public Tree2UMLExpressionInfix(ParseTree root, Element[] operations, Element[] operands, Element[] functions, String[] operationsString, String[] operandsString, String[] customFuncString){
		this.root = root;
		this.operations = operations;
		this.operands = operands;
		this.operationsString = operationsString;
		this.operandsString = operandsString;
		this.functions = functions;
		this.customFuncString = customFuncString;
	}
	
	public ValueSpecification parse(){
		error = false;
		return traverse0(root);		
	}
	
	private ValueSpecification traverse0(ParseTree n){
		
		//distinguish between the different cases defined in the grammar and set stop criteria
		if(n instanceof ArithmeticBinaryParser.BinaryExp1Context || n instanceof ArithmeticBinaryParser.BinaryExp2Context 
				|| n instanceof ArithmeticBinaryParser.BinaryExp3Context || n instanceof ArithmeticBinaryParser.EqExpContext){	//=> BINARY EXPRESSION
			
			//************************************DO****************************************
			Expression exp = Application.getInstance().getProject().getElementsFactory().createExpressionInstance();
			ElementValue elemVal = Application.getInstance().getProject().getElementsFactory().createElementValueInstance();
			//find the correct operation
			try{
				int i = 0;
				while(i<operationsString.length && !operationsString[i].equals(n.getChild(1).getText())){ i++; }
				if(operationsString[i].equals(n.getChild(1).getText())){
					elemVal.setElement(operations[i]);
				}else{ throw new Exception(); }
			}catch(Exception e){
				error = true;
			}
			
			//*********************************TRAVERSE*************************************
			exp.getOperand().add(traverse0(n.getChild(0)));	//left child

			exp.getOperand().add(elemVal);	//add operation to expression infix
			
			exp.getOperand().add(traverse0(n.getChild(2)));	//right child
			
			//**********************************RETURN**************************************
			return exp;
		
		}else if(n instanceof ArithmeticBinaryParser.UnaryExpContext){	//=> UNARY EXPRESSION
			
			//************************************DO****************************************
			Expression exp = Application.getInstance().getProject().getElementsFactory().createExpressionInstance();
			ElementValue elemVal = Application.getInstance().getProject().getElementsFactory().createElementValueInstance();
			//find the correct operation
			try{
				int i = 0;
				while(i<operationsString.length && !operationsString[i].equals(n.getChild(0).getChild(0).getText())){ i++; }
				if(operationsString[i].equals(n.getChild(0).getChild(0).getText())){
					elemVal.setElement(operations[i]);
				}else{ throw new Exception(); }
			}catch(Exception e){
				error = true;
			}
			//add operation to expression
			exp.getOperand().add(elemVal);
			
			//*********************************TRAVERSE*************************************
			exp.getOperand().add(traverse0(n.getChild(2)));
			
			//**********************************RETURN**************************************
			return exp;
		
		}else if(n instanceof ArithmeticBinaryParser.FunExpContext){	//=> CUSTOMIZED FUNCTION EXPRESSION
			
			//************************************DO****************************************
			Expression exp = Application.getInstance().getProject().getElementsFactory().createExpressionInstance();
			ElementValue elemVal = Application.getInstance().getProject().getElementsFactory().createElementValueInstance();
			//find the correct operation
			try{
				int i = 0;
				while(i<customFuncString.length && !customFuncString[i].equals(n.getChild(0).getText())){ 
					i++; 
				}
				if(customFuncString[i].equals(n.getChild(0).getText())){
					elemVal.setElement(functions[i]);
				}else{ throw new Exception(); }
			}catch(Exception e){
				javax.swing.JOptionPane.showMessageDialog(null, "Error: Couldn't find function " + n.getChild(0).getText() + " in customFunctions-Block!");
				error = true;
			}
			//add operation to expression
			exp.getOperand().add(elemVal);
			
			//*********************************TRAVERSE*************************************
			int max = n.getChildCount()-2;
			for(int i=2; i<=max; i=i+2){
				exp.getOperand().add(traverse0(n.getChild(i)));
			}
			
			//**********************************RETURN**************************************
			return exp;
			
		}else if(n instanceof ArithmeticBinaryParser.ParExpContext){	//=> PARENTHESES EXPRESSION
			
			//**************************TRAVERSE AND RETURN*********************************
			return traverse0(n.getChild(1));
			
		}else if(n instanceof ArithmeticBinaryParser.NegExpContext){	//=> NEGATIVE EXPRESSION
			
			//************************************DO****************************************
			Expression exp = Application.getInstance().getProject().getElementsFactory().createExpressionInstance();
			ElementValue elemVal = Application.getInstance().getProject().getElementsFactory().createElementValueInstance();
			//find the correct operation
			try{
				int i = 0;
				while(i<operationsString.length && !operationsString[i].equals(n.getChild(0).getText())){ i++; }
				if(operationsString[i].equals(n.getChild(0).getText())){
					elemVal.setElement(operations[i]);
				}else{ throw new Exception(); }
			}catch(Exception e){
				javax.swing.JOptionPane.showMessageDialog(null, "Error: Couldn't find operation " + n.getChild(0).getText() + " in sysmlBlock!");
				error = true;
			}
			//add operation to expression
			exp.getOperand().add(elemVal);
			
			//*********************************TRAVERSE*************************************
			exp.getOperand().add(traverse0(n.getChild(1)));
			
			//**********************************RETURN**************************************
			return exp;
						
		}else if(n instanceof ArithmeticBinaryParser.NegLitExpContext){	//=> NEGATIVE LITERAL EXPRESSION
			
			//************************************DO****************************************
			Expression exp = Application.getInstance().getProject().getElementsFactory().createExpressionInstance();
			ElementValue elemVal = Application.getInstance().getProject().getElementsFactory().createElementValueInstance();
			//find the correct operation
			try{
				int i = 0;
				while(i<operationsString.length && !operationsString[i].equals(n.getChild(0).getText())){ i++; }
				if(operationsString[i].equals(n.getChild(0).getText())){
					elemVal.setElement(operations[i]);
				}else{ throw new Exception(); }
			}catch(Exception e){
				javax.swing.JOptionPane.showMessageDialog(null, "Error: Couldn't find operation " + n.getChild(0).getText() + " in sysmlBlock!");
				error = true;
			}
			
			try{	//LITERAL REAL
				
				//************************************DO****************************************
				double lRealDouble = Double.parseDouble(n.getChild(1).getChild(0).getText());
				LiteralReal lReal = Application.getInstance().getProject().getElementsFactory().createLiteralRealInstance();
				lReal.setValue(lRealDouble);
				
				//add operation to expression
				exp.getOperand().add(elemVal);
				//add LiteralReal to expression
				exp.getOperand().add(lReal);
				
				//**********************************RETURN**************************************
				return exp;
				
			}catch(NumberFormatException e){	//ELEMENT VALUE
								
				//************************************DO****************************************
				//find the correct operand
				ElementValue elemVal1 = Application.getInstance().getProject().getElementsFactory().createElementValueInstance();
				try{
					int i = 0;
					while(i<operandsString.length && !operandsString[i].equals(n.getChild(1).getChild(0).getText())){ i++; }
					if(operandsString[i].equals(n.getChild(1).getChild(0).getText())){
						elemVal1.setElement(operands[i]);
					}else{ throw new Exception(); }
				}catch(Exception e1){
					javax.swing.JOptionPane.showMessageDialog(null, "Error: Couldn't find operand " + n.getChild(1).getChild(0).getText() + " in sysmlBlock!");
					error = true;
				}
				//add operation to expression
				exp.getOperand().add(elemVal);
				//add operand to expression
				exp.getOperand().add(elemVal1);
			
				//**********************************RETURN**************************************
				return exp;
			}
			
			
		}else if(n instanceof ArithmeticBinaryParser.LitExpContext){	//=> LITERAL EXPRESSION
			
			try{	//LITERAL REAL
				
				//************************************DO****************************************
				double lRealDouble = Double.parseDouble(n.getChild(0).getText());
				LiteralReal lReal = Application.getInstance().getProject().getElementsFactory().createLiteralRealInstance();
				lReal.setValue(lRealDouble);
				
				//**********************************RETURN**************************************
				return lReal;
				
			}catch(NumberFormatException e){	//ELEMENT VALUE
				
				//************************************DO****************************************
				ElementValue elemVal = Application.getInstance().getProject().getElementsFactory().createElementValueInstance();
				//find the correct operands
				try{
					int i = 0;
					while(i<operandsString.length && !operandsString[i].equals(n.getChild(0).getText())){ i++; }
					if(operandsString[i].equals(n.getChild(0).getText())){
						elemVal.setElement(operands[i]);
					}else{ throw new Exception(); }
				}catch(Exception e1){
					javax.swing.JOptionPane.showMessageDialog(null, "Error: Couldn't find operand " + n.getChild(0).getText() + " in sysmlBlock!");
					error = true;
				}
				
				//**********************************RETURN**************************************
				return elemVal;
			}
			
		}else{ //=> couldn't match any Expression => error
			
			javax.swing.JOptionPane.showMessageDialog(null, "Error: Couldn't match any Expression Context!");
			error = true;
			//javax.swing.JOptionPane.showMessageDialog(null, "class: " + n.getClass().toString());
		
		}
		
		return Application.getInstance().getProject().getElementsFactory().createExpressionInstance();
	}
	
}
