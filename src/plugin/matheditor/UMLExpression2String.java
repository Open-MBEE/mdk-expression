package plugin.matheditor;

import java.util.Collection;
import java.util.Iterator;

import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ElementValue;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Expression;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralReal;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ValueSpecification;

public class UMLExpression2String {
	
	private String strg = "";
	private final String OPERATION = "Operation ", CONSTRAINT_PARAMETER = "Constraint Parameter ";
	private ValueSpecification root;
	private final String OPERATIONS_PRES = "+ - * / ^";		//set a parenthesis if difference at which position they occur in the string is greater than 1!!!
	
	public UMLExpression2String(ValueSpecification root){
		this.root = root;
	}
	
	public String parse(){	
		parse0(root);
		return strg;
	}
	
	private void parse0(ValueSpecification n){
			
		boolean isCustomFunc = false;
		if(n instanceof Expression && DiagramContextMenuConfigurator.customFuncString!=null
				&& !(DiagramContextMenuConfigurator.customFuncString.length==0) ){
			for(int i=0; i<DiagramContextMenuConfigurator.customFuncString.length; i++){
				if(DiagramContextMenuConfigurator.customFuncString[i]!=null && DiagramContextMenuConfigurator.customFuncString[i].startsWith(OPERATION)){
					if(DiagramContextMenuConfigurator.customFuncString[i].equals(((ElementValue)((Expression)n).getOperand().get(0)).getElement().getHumanName().toString().substring(OPERATION.length()))){
						isCustomFunc = true;
					}
				}else{break;}
			}
		}
		
		if(n instanceof Expression && ((Expression)n).getOperand().size() == 3 && !isCustomFunc){	//BINARY NODE
			
			//*******************************ARE PARENTHESES NEEDED?*****************************
			String parent = "defaultStartValue";
			String currentNode = ((ElementValue)((Expression)n).getOperand().get(0)).getElement().getHumanName().toString().substring(OPERATION.length());
			boolean parIsNeeded = false;
			if(n != root){
				parent = ((ElementValue)((Expression)n.getOwner()).getOperand().get(0)).getElement().getHumanName().toString().substring(OPERATION.length());
			}
			if(OPERATIONS_PRES.contains(parent) && (OPERATIONS_PRES.indexOf(parent)-OPERATIONS_PRES.indexOf(currentNode))>1){	//parent!= unary and root
				parIsNeeded = true;
			}
			
			
			//**************************************DO & TRAVERSE********************************
			if(parIsNeeded){ strg += "("; }	//add parentheses only when necessary
			parse0(((ValueSpecification)((Expression)n).getOperand().get(1)));	//left child
			strg += ((ElementValue)((Expression)n).getOperand().get(0)).getElement().getHumanName().toString().substring(OPERATION.length());	//get operation
			parse0(((ValueSpecification)((Expression)n).getOperand().get(2)));	//right child
			if(parIsNeeded){ strg += ")"; }	//add parentheses only when necessary
			
			//****************************************RETURN*************************************
			return;
			
		}else if(n instanceof Expression && ((Expression)n).getOperand().size() == 2 && !isCustomFunc){		//UNARY NODE
			
			//**************************************DO & TRAVERSE********************************
			strg += ((ElementValue)((Expression)n).getOperand().get(0)).getElement().getHumanName().toString().substring(OPERATION.length());	//get operation
			strg += "(";
			parse0(((ValueSpecification)((Expression)n).getOperand().get(1)));	//child
			strg += ")";
			
			//****************************************RETURN*************************************
			return;
			
		}else if(n instanceof Expression && isCustomFunc){		//CUSTOMIZED FUNCTION NODE
			
			//**************************************DO & TRAVERSE********************************
			strg += ((ElementValue)((Expression)n).getOperand().get(0)).getElement().getHumanName().toString().substring(OPERATION.length());	//get operation
			strg += "(";
			for(int i=1; i<((Expression)n).getOperand().size(); i++){
				parse0(((ValueSpecification)((Expression)n).getOperand().get(i)));	//child
				strg += ",";
			}
			strg = strg.substring(0, strg.length()-1);
			strg += ")";
			
			//****************************************RETURN*************************************
			return;
			
		}else if(n instanceof ElementValue){	//leaf
			
			//******************************************DO***************************************
			strg += ((ElementValue)n).getElement().getHumanName().toString().substring(CONSTRAINT_PARAMETER.length());	//get operand
			
			//****************************************RETURN*************************************
			return;
			
		}else if(n instanceof LiteralReal){		//leaf
			
			//******************************************DO***************************************
			strg += ((LiteralReal)n).getValue();	//get literal real
			
			//****************************************RETURN*************************************
			return;
		}			
		
	}
}
