package gov.nasa.jpl.mbee.mdk.expression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdtemplates.StringExpression;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Constraint;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ElementValue;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Expression;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ValueSpecification;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.NamedElement;

public class MathEditorMain1Model {
		
	SelectedConstraintBlock selectedConstraintBlock; 
	Constraint currentConstraint;
	
	private boolean isStringExpression; 
	private String editExpression;
	
	private List<String> customFunctionsString;
	private LinkedHashMap<String, Element> combinedOperationsMap;  //key = function names, value = property
	
	public MathEditorMain1Model(SelectedConstraintBlock _selectedConstraintBlock, Constraint _currentConstraint){
		
		selectedConstraintBlock = _selectedConstraintBlock;
		currentConstraint = _currentConstraint;
		
		setOperationAndCustromFunctions();
		setExpression();
		
	}
	//call from cunstructor
	//call when "Library..." button is pressed
	public void setOperationAndCustromFunctions(){ //call from constructor
		
		java.util.Collection<Element> operationsCollection = MDKExpressionPlugin.asciiMathLibraryBlock.getOwnedElement();
    	java.util.Collection<Element> functionsCollection = MDKExpressionPlugin.customFuncBlock.getOwnedElement();
		setOperationAndCustromFunctions(operationsCollection, functionsCollection);
	}
	private void setOperationAndCustromFunctions(java.util.Collection<Element> operationsCollection, java.util.Collection<Element> functionsCollection){
		
		combinedOperationsMap = new LinkedHashMap<String, Element>();
		
    	operationsCollection.stream()
			.filter(c-> ((Element)c).getHumanType().equals(MDSysMLConstants.OPERATION))
			.forEach(c -> {
				//combinedOperationsMap.put(c.getHumanName().substring(OPERATION.length()), (Element)c);
				combinedOperationsMap.put(((NamedElement)c).getName(), (Element)c);
			});
    	
    	customFunctionsString = new ArrayList<String>();
    	functionsCollection.stream()
			.filter(c-> ((Element)c).getHumanType().equals(MDSysMLConstants.OPERATION))
			.forEach(c -> {
				customFunctionsString.add(((NamedElement)c).getName());
				combinedOperationsMap.put(((NamedElement)c).getName(), (Element)c);
			});
	}
	public Element getConstraintBlock(){
		return this.selectedConstraintBlock.getConstraintBlock();
	}
	public Collection<Property> getOperands(){
		return this.selectedConstraintBlock.getOperands();
	}
	public List<String> getOperandsInString(){
		List<String> l = new ArrayList<String>();
		getOperands().forEach(o -> l.add(o.getName()));
		return l;
	}
	public Element getOperand(String _lookingfor){
		return this.selectedConstraintBlock.getOperand(_lookingfor);
	}
	public Collection<Element> getCombinedOperations(){
		return this.combinedOperationsMap.values();
	}
	public List<String> getCustomOperationsInString(){
		return customFunctionsString;
	}
	public Element getCombinedOperation(String _lookingfor){
		System.out.println( _lookingfor);
		this.combinedOperationsMap.keySet().forEach(c->{System.out.println(c);});
		
		
		return this.combinedOperationsMap.get(_lookingfor);
	}
	//set constraint name if different
	public void setCurrentConstraintName(String _name){
		if ( this.currentConstraint.getName().compareTo(_name) != 0)
			this.currentConstraint.setName(_name);
	}
	
	private void setExpression() {
		
		//*******************************SHOULD EDIT?****************************************
		Element firstElement = this.currentConstraint.getOwnedElement().iterator().next();
		
		if ( firstElement instanceof Expression && ((Expression) firstElement).getOperand().size()!= 0 ) { //StringExpression is sub of Expression

			try{
				UML2String uml2string;
				if ( firstElement instanceof StringExpression ){ //1st operand is StringExpression
					this.isStringExpression = true; //InfixStringExp
					uml2string = new UMLStringExpression2String(((ValueSpecification) firstElement)); 
					this.editExpression = uml2string.parse();
					
				}
				else { //1st operand is ElementValue
					this.isStringExpression = false; //prefixExp
					if (((Expression) firstElement).getOperand().get(0) instanceof ElementValue ){	//true if constraint is not empty
						uml2string = new UMLExpression2String(((ValueSpecification) firstElement), customFunctionsString);	
						this.editExpression = uml2string.parse();
					}
				}
			}
			catch (Exception e){
				javax.swing.JOptionPane.showMessageDialog(null, "Not able to convert to a expression.");
				this.editExpression = "";
			}
		}
		else {
			this.editExpression =  "";
		}
	}
	
	public String getEditExpression() {
		return this.editExpression;
	}
	public boolean isStringExpression(){ //for selecting "prefix Exp" or "infix String Exp" radio button 
		return this.isStringExpression;
	}
	public String getName(){
		return this.currentConstraint.getName();
	}
	
}
