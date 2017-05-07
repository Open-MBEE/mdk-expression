package gov.nasa.jpl.mbee.mdk.expression;

import javax.swing.JOptionPane;

import org.antlr.v4.runtime.tree.ParseTree;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ElementValue;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Expression;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralInteger;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralReal;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralString;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ValueSpecification;
import com.nomagic.uml2.ext.magicdraw.compositestructures.mdports.Port;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;


public abstract class Tree2UMLExpression {
	
	protected ParseTree root;
	public boolean error;
	MathEditorMain1Controller controller;
	protected ValueSpecification originalvs;
	boolean isRoot;
	
	public Tree2UMLExpression(MathEditorMain1Controller _controller, ParseTree root, ValueSpecification _originalvs) {
		this.root = root;
		this.controller = _controller;
		error = false;
		this.originalvs = _originalvs;
		isRoot = false;
	}
	
	public ValueSpecification parse() throws Exception{
		return traverse0(root);		
	}
	//Util functions
	
	private ElementValue createElementValue(Element _lookingFor, String _lookingForName, Element _block, boolean _showError){
		ElementValue elemVal = Application.getInstance().getProject().getElementsFactory().createElementValueInstance();
		if ( _lookingFor != null ){
			elemVal.setElement(_lookingFor);
			return elemVal;
		}
		else {
			if ( _showError)
				showNotAbleToFindError(_lookingForName, _block);
			return null;
		}
	}
	protected Expression getExpression() {
		Expression exp;// = Application.getInstance().getProject().getElementsFactory().createExpressionInstance();
		if ( isRoot){
			exp = (Expression) originalvs;
			isRoot = false;
		}
		else
			exp = Application.getInstance().getProject().getElementsFactory().createExpressionInstance();
		return exp;
	}
	
	/////////operations
	protected ElementValue createElementValueFromOperation(String _lookingForOperation, boolean _showError){
		return createElementValue(controller.getOperationAsciiMathLibrary(_lookingForOperation), _lookingForOperation, controller.getAsciiMathLibraryBlock(), _showError);
	}
	protected ElementValue createElementValueFromOperation(String _lookingForOperation){
		return createElementValueFromOperation(_lookingForOperation, false);
	}
	protected ElementValue createElementValueFromOperationCustom(String _lookingForOperation, boolean _showError){
		return createElementValue(controller.getOperationCustom(_lookingForOperation), _lookingForOperation, controller.getCustomFunctionBlock(), _showError);
	}
	protected ElementValue createElementValueFromOperationCustom(String _lookingForOperation){
		return createElementValueFromOperationCustom(_lookingForOperation, false);
	}
	
	//operands
	protected ElementValue createElementValueFromOperands(String _lookingForOperations, Element _block, boolean _showError){
		return createElementValue(controller.getOperand(_lookingForOperations), _lookingForOperations, _block, _showError);
	}
	protected ElementValue createElementValueFromOperands(String _lookingForOperations, Element _block){
		return createElementValue(controller.getOperand(_lookingForOperations), _lookingForOperations, _block, false);
	}
	public boolean getError(){
		return this.error;
	}
	protected LiteralReal createLiteralReal(){
		return Application.getInstance().getProject().getElementsFactory().createLiteralRealInstance();
	}
	protected LiteralInteger createLiteralInteger(){
		return Application.getInstance().getProject().getElementsFactory().createLiteralIntegerInstance();
	}
	protected LiteralString createLiteralString(){
		return Application.getInstance().getProject().getElementsFactory().createLiteralStringInstance();
	}
	
	protected void showNotAbleToFindError(String _lookingForOperation, Element _block){
		showNotAbleToFindError(_lookingForOperation, _block, "");
	}
	protected void showNotAbleToFindError(String _lookingForOperation, Element _block , String _appendMsg){
		if ( _block != null)
			showMessage("Error: Couldn't find operation " + _lookingForOperation + " in " + _block.getHumanName() + "!" + _appendMsg);
		else
			showMessage("Error: Couldn't find operation " + _lookingForOperation +  "!" + _appendMsg);
		error = true;
	}
	
	protected static void showMessage(String _msg){
		javax.swing.JOptionPane.showMessageDialog(null, _msg);
	}
	
	protected void showNotAbleToCreateError(String _lookingForOperation, Element _constraintBlock){
		showMessage("Not able to create " +  _lookingForOperation + " in " +  _constraintBlock.getHumanName() + "!");
		error = true;
	}
	
	protected static boolean askToCreateAConstraintParameter(String _name){
		Object[] options = {"Yes", "No"};
		int n = JOptionPane.showOptionDialog(null, "Would you like a constraint parameter \"" + _name + "\" to be created?",	"Question",
		JOptionPane.YES_NO_OPTION,
		JOptionPane.QUESTION_MESSAGE,
		null,     //do not use a custom Icon
		options,  //the titles of buttons
		options[0]); //default button title
		if ( n == JOptionPane.YES_OPTION)
			return true;
		else
			return false;
	}
	protected ElementValue createConstaintParameter(String _lookingFor){
		
		ElementValue elemVal = Application.getInstance().getProject().getElementsFactory().createElementValueInstance();
		Stereotype stereotype = StereotypesHelper.getStereotype(Application.getInstance().getProject(),MDSysMLConstants.CONSTRAINTPARAMETER);
		Port p = Application.getInstance().getProject().getElementsFactory().createPortInstance();
		p.setName(_lookingFor);
		StereotypesHelper.addStereotype(p, stereotype);
		p.setOwner(controller.getConstraintBlock());
		p.setType((com.nomagic.uml2.ext.magicdraw.classes.mdkernel.DataType)Application.getInstance().getProject().getElementByID("_11_5EAPbeta_be00301_1147431819399_50461_1671")); //setAsReal
		//Stereotype stereotype = MDCustomizationForSysMLProfile.getInstance(Application.getInstance().getProject()).getStereotype("ConstraintParameter");
		elemVal.setElement(p);
		return elemVal;
	}
	//end of Util functions
	
	protected abstract ValueSpecification traverse0(ParseTree n) throws Exception;
	
}
