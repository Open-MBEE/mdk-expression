package gov.nasa.jpl.mbee.mdk.expression;

import java.util.List;

import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ElementValue;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Expression;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralInteger;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralReal;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralString;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ValueSpecification;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.NamedElement;

public class UMLExpression2String extends UML2String {
	
	//private String strg;
	//private ValueSpecification root;
	private final String OPERATIONS_PRES = "+ - * / ^ _";		//set a parenthesis if difference at which position they occur in the string is greater than 1!!!
	private List<String> customFunctionsString; //used only root is NOT StringExpression
		
	public UMLExpression2String(ValueSpecification root, List<String> _customFunctionsString){
		super(root);
		//this.root = root;
		this.customFunctionsString = _customFunctionsString;
		//strg = "";
	}
	
	public String parse(){	
		parse0(root);
		return strg;
	}
	private void parse0(ValueSpecification n){
		
		System.out.println(n.getHumanName());
		//Expression does not contain "(" , ")" or ",", so added when converting to string
		if ( n instanceof Expression){
			ValueSpecification zerovs = ((Expression)n).getOperand().get(0);
			/*if ( zerovs instanceof Expression){
				String firstOperandName = ((NamedElement)((ElementValue)((Expression)n).getOperand().get(0)).getElement()).getName();
				System.out.println(firstOperandName);
			}*/
			if (zerovs instanceof ElementValue){
				String firstOperandName = ((NamedElement)((ElementValue)((Expression)n).getOperand().get(0)).getElement()).getName();
	
				System.out.println(firstOperandName);
				//if custom function, requires to insert "(", ")" and "," between values(ElementValues, or LiteralReal) i.e., fun(a, b, c, 5)
				if(customFunctionsString.contains(firstOperandName)){		//CUSTOMIZED FUNCTION NODE
					strg += firstOperandName;	//get operation
					strg += "(";
					for(int i=1; i<((Expression)n).getOperand().size(); i++){
						parse0(((ValueSpecification)((Expression)n).getOperand().get(i)));	//child
						strg += ",";
					}
					strg = strg.substring(0, strg.length()-1); //remove last ","
					strg += ")";
					return;
				}
				else { //! customFuncs
					
					System.out.println(((Expression)n).getOperand().size() );
					//UNARY NODE (i.e., sin)
					if( ((Expression)n).getOperand().size() == 2 ) { 
						strg += firstOperandName;	//get operation
						if (OPERATIONS_PRES.contains(firstOperandName)){ //(^-1)'s -1 is also (Expression)n).getOperand().size()=2, so fileter that out.
							parse0(((ValueSpecification)((Expression)n).getOperand().get(1)));	//child
						}
						else { //i.e., sin
							strg += "(";
							parse0(((ValueSpecification)((Expression)n).getOperand().get(1)));	//child
							strg += ")";
						}
						return;
					}

					else  {//
						boolean parIsNeeded = false;
						if(n != root){
							String parent = "defaultStartValue";
							parent = ((NamedElement)((ElementValue)((Expression)n.getOwner()).getOperand().get(0)).getElement()).getName();
							if(OPERATIONS_PRES.contains(parent) && (OPERATIONS_PRES.indexOf(parent)-OPERATIONS_PRES.indexOf(firstOperandName))>1){	//parent!= unary and root
								parIsNeeded = true;
							}
						}
						
						if( ((Expression)n).getOperand().size() == 3  && OPERATIONS_PRES.contains(firstOperandName)){	//BINARY NODE a + b
							if(parIsNeeded){ strg += "("; }	//add parentheses only when necessary
							parse0(((ValueSpecification)((Expression)n).getOperand().get(1)));	//left child
							strg += firstOperandName;	//get operation
							parse0(((ValueSpecification)((Expression)n).getOperand().get(2)));	//right child
							if(parIsNeeded){ strg += ")"; }	//add parentheses only when necessary
							return;
						}
						else { //i.e., infix = sin^-11^x, prefix = sin,(^(-1)),x  
							if(parIsNeeded){ strg += "("; }	//add parentheses only when necessary
							strg += firstOperandName;	//get operation
							for ( int i = 1; i < ((Expression)n).getOperand().size(); i++ ){
								parse0(((ValueSpecification)((Expression)n).getOperand().get(i)));
							}
							if(parIsNeeded){ strg += ")"; }	//add parentheses only when necessary
							return;
						}
					}
				}
			}
			else { //1st operands is NOT ElementValue
				//not supported
				return;
			}
		}
		else if(n instanceof ElementValue){	//leaf
			strg += ((NamedElement)((ElementValue)n).getElement()).getName(); 
			return;
		}
		else if(n instanceof LiteralReal){		//leaf
			strg += ((LiteralReal)n).getValue();	//get literal real
			return;
		}
		else if ( n instanceof LiteralInteger){ //leaf
			strg += ((LiteralInteger)n).getValue();
			return;
		}
		else if ( n instanceof LiteralString){ //leaf
			strg += ((LiteralString)n).getValue();
			return;
		}
		
	}
}