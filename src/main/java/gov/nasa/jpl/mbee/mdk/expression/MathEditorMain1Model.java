package gov.nasa.jpl.mbee.mdk.expression;

import com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdtemplates.StringExpression;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.*;

import java.util.Collection;
import java.util.List;

public class MathEditorMain1Model {

    SelectedOperationBlocks selectedOperationBlocks;
    SelectedConstraintBlock selectedConstraintBlock;
    Constraint currentConstraint;

    private boolean isStringExpression;
    private String editExpression;

    //private List<String> customFunctionsString;
    //private LinkedHashMap<String, Element> combinedOperationsMap;  //key = function names, value = property

    public MathEditorMain1Model(SelectedConstraintBlock _selectedConstraintBlock, Constraint _currentConstraint, SelectedOperationBlocks _selectedOperationBlocks) {

        selectedConstraintBlock = _selectedConstraintBlock;
        selectedOperationBlocks = _selectedOperationBlocks;
        currentConstraint = _currentConstraint;
        setExpression();

    }

    //call from cunstructor
    //call when "Library..." button is pressed
    public void resetSelectedOperationBlocks(Element _a, Element _c) { //call from constructor
        selectedOperationBlocks.reset(_a, _c);
    }

    public Element getConstraintBlock() {
        return this.selectedConstraintBlock.getConstraintBlock();
    }

    public Collection<Property> getOperands() {
        return this.selectedConstraintBlock.getOperands();
    }

    public List<String> getOperandsAndOperationsInStringForAutoComplete() {
        List<String> l = selectedConstraintBlock.getOperandsInString();
        l.addAll(selectedOperationBlocks.getOperationsInStringForAutoComplete());
        return l;
    }

    public Element getOperand(String _lookingfor) {
        return this.selectedConstraintBlock.getOperand(_lookingfor);
    }

    public Collection<Element> getOperations() { //used to create JList in UI
        return selectedOperationBlocks.getOperations();
    }

    public Element getOperationAsciiLibrary(String _lookingfor) {
        return selectedOperationBlocks.getOperationAsciiMathLibrary(_lookingfor);
    }

    public Element getOperationCustom(String _lookingfor) {
        return this.selectedOperationBlocks.getOperationCustom(_lookingfor);
    }

    public Element getAsciiMathLibraryBlock() {
        return this.selectedOperationBlocks.getAsciiMathLibraryBlock();
    }

    public Element getCustomFunctionBlock() {
        return this.selectedOperationBlocks.getCustomFuncBlock();
    }

    /*public Element getCombinedOperation(String _lookingfor){
        System.out.println( _lookingfor);
        //this.combinedOperationsMap.keySet().forEach(c->{System.out.println(c);});

        return selectedOperationBlocks.getOperation(_lookingfor);

        //return this.combinedOperationsMap.get(_lookingfor);
    }*/
    //set constraint name if different
    public void setCurrentConstraintName(String _name) {
        if (this.currentConstraint.getName().compareTo(_name) != 0) {
            this.currentConstraint.setName(_name);
        }
    }

    //setting editExpression from MD object
    private void setExpression() {

        //*******************************SHOULD EDIT?****************************************
        Element firstElement = this.currentConstraint.getOwnedElement().iterator().next();

        if (firstElement instanceof Expression && ((Expression) firstElement).getOperand().size() != 0) { //StringExpression is sub of Expression

            try {
                UML2String uml2string;
                if (firstElement instanceof StringExpression) { //1st operand is StringExpression
                    this.isStringExpression = true; //InfixStringExp
                    uml2string = new UMLStringExpression2String(((ValueSpecification) firstElement));
                    this.editExpression = uml2string.parse();

                }
                else { //1st operand is ElementValue
                    this.isStringExpression = false; //prefixExp
                    if (((Expression) firstElement).getOperand().get(0) instanceof ElementValue) {    //true if constraint is not empty
                        uml2string = new UMLExpression2String(((ValueSpecification) firstElement), this.selectedOperationBlocks.getCustomOperations());
                        this.editExpression = uml2string.parse();
                    }
                }
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(null, "Not able to convert to a expression.");
                this.editExpression = "";
            }
        }
        else {
            this.editExpression = "";
        }
    }

    public String getEditExpression() {
        return this.editExpression;
    }

    //called when the expression is successfully saved in MD
    public void setExpression(String exp) {
        this.editExpression = exp;
    }

    public boolean isStringExpression() { //for selecting "prefix Exp" or "infix String Exp" radio button
        return this.isStringExpression;
    }

    public String getName() {
        return this.currentConstraint.getName();
    }

    public SelectedOperationBlocks getSelectedOperationBlocks() {
        return selectedOperationBlocks;
    }

    public SelectedConstraintBlock getSelectedConstraintBlock() {
        return selectedConstraintBlock;
    }

}
