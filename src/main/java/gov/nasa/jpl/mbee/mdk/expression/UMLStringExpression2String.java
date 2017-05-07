package gov.nasa.jpl.mbee.mdk.expression;

import java.util.List;

import com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdtemplates.StringExpression;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ElementValue;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralInteger;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralReal;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralString;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.NamedElement;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ValueSpecification;

public class UMLStringExpression2String extends UML2String {
	
	
	public UMLStringExpression2String(ValueSpecification root){
		super(root);
	}
	
	public String parse(){	
		parse0(root);
		return strg;
	}
	
	private void parse0(ValueSpecification n){
		if(n instanceof StringExpression ) {
			List<ValueSpecification> ops = ((StringExpression)n).getOperand();
			for ( int i = 0; i < ops.size(); i++ ){
				parse0(ops.get(i));
			}
		} else if(n instanceof ElementValue){	//leaf
			String ev = ((NamedElement)((ElementValue)n).getElement()).getName();
			ev = ev.replace("|", "\u2223"); //having | character having problem with creating xml
			
			strg += ev;	//get operand
			return;
		
		} else if(n instanceof LiteralString ) { //"(" is LiteralString
			strg += ((LiteralString)n).getValue();
			return;
		} else if(n instanceof LiteralReal){		//leaf
			strg += ((LiteralReal)n).getValue();	//get literal real
			return;
		} else if ( n instanceof LiteralInteger){
			strg += ((LiteralInteger)n).getValue();	//get literal integer
			return;
		} 
		
	}
}
/*
private void parse0(ValueSpecification n){

if(n instanceof StringExpression ) {
	if (!(((StringExpression)n).getOperand().get(0) instanceof LiteralString)) {
		if ( ((StringExpression)n).getOperand().size() == 3	){		//BINARY NODE
		
			//**************************************DO & TRAVERSE********************************
			parse0(((ValueSpecification)((StringExpression)n).getOperand().get(0)));	//left child
			//strg += ((ElementValue)((StringExpression)n).getOperand().get(1)).getElement().getHumanName().toString().substring(OPERATION.length());	//get operation
			strg += ((NamedElement) ((ElementValue)((StringExpression)n).getOperand().get(1)).getElement()).getName();	//get operation
			parse0(((ValueSpecification)((StringExpression)n).getOperand().get(2)));	//right child
			
			//****************************************RETURN*************************************
			return;		
		
		} else if( ((StringExpression)n).getOperand().size() == 4 ){		//UNARY NODE
		
			//**************************************DO & TRAVERSE********************************
			//strg += ((ElementValue)((StringExpression)n).getOperand().get(0)).getElement().getHumanName().toString().substring(OPERATION.length());	//get operation
			strg += ((NamedElement)((ElementValue)((StringExpression)n).getOperand().get(0)).getElement()).getName();	//get operation
			parse0(((ValueSpecification)((StringExpression)n).getOperand().get(1)));
			parse0(((ValueSpecification)((StringExpression)n).getOperand().get(2)));	//child
			parse0(((ValueSpecification)((StringExpression)n).getOperand().get(3)));
			
			//****************************************RETURN*************************************
			return;
			
		} else if(((StringExpression)n).getOperand().size() > 4	){		//CUSTOMIZED FUNCTION (if lenght < 4 goes into unary node; could change that, but makes no difference for now; be aware of it though)
		
			//**************************************DO & TRAVERSE********************************
			//strg += ((ElementValue)((StringExpression)n).getOperand().get(0)).getElement().getHumanName().toString().substring(OPERATION.length());	//get operation
			strg += ((NamedElement)((ElementValue)((StringExpression)n).getOperand().get(0)).getElement()).getName();	//get operation
			strg += "(";
			for(int i=2; i<((StringExpression)n).getOperand().size()-1; i++){
				parse0(((ValueSpecification)((StringExpression)n).getOperand().get(i)));	//child
			}
			strg += ")";
			
			//****************************************RETURN*************************************
			return;
		} else if( ((StringExpression)n).getOperand().size() == 2 ){ 		//NEGATIVE VALUE
			
			//**************************************DO & TRAVERSE********************************
			//strg += ((ElementValue)((StringExpression)n).getOperand().get(0)).getElement().getHumanName().toString().substring(OPERATION.length());
			strg += ((NamedElement) ((ElementValue)((StringExpression)n).getOperand().get(0)).getElement()).getName();
			parse0(((ValueSpecification)((StringExpression)n).getOperand().get(1)));
			
			//****************************************RETURN*************************************
			return;
		}
	}
	else if(((StringExpression)n).getOperand().get(0) instanceof LiteralString){	//LITERAL STRING
	
		//**************************************DO & TRAVERSE********************************			
		for(int i=0; i<((StringExpression)n).getOperand().size(); i++){
			parse0(((ValueSpecification)((StringExpression)n).getOperand().get(i)));	//child
		}		
		
		//****************************************RETURN*************************************
		return;
	}
	
} else if(n instanceof LiteralString ) {
	
	if (((LiteralString)n).getValue().equals("(")){ 	//left parentheses (could be remove and put into //LITERAL STRING)
	
		strg += "(";
	
		return;

	}else if (((LiteralString)n).getValue().equals(")")){ 	//right parentheses (could be remove and put into //LITERAL STRING)
	
		strg += ")";
	
		return;
	
	}else if (((LiteralString)n).getValue().equals(",")){ 	//comma (could be remove and put into //LITERAL STRING)
	
		strg += ",";
	
		return;
	}
} else if(n instanceof ElementValue){	//leaf
	
	//******************************************DO***************************************
	strg += ((NamedElement)((ElementValue)n).getElement()).getName();	//get operand
	
	//****************************************RETURN*************************************
	return;
	
} else if(n instanceof LiteralReal){		//leaf
	
	//******************************************DO***************************************
	strg += ((LiteralReal)n).getValue();	//get literal real
	
	//****************************************RETURN*************************************
	return;
} else if ( n instanceof LiteralInteger){
	strg += ((LiteralInteger)n).getValue();	//get literal integer
}

}
*/