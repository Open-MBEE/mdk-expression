package gov.nasa.jpl.mbee.mdk.expression;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.ui.actions.DefaultDiagramAction;
import com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdtemplates.StringExpression;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Constraint;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ElementValue;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Expression;
import com.nomagic.uml2.ext.magicdraw.compositestructures.mdinternalstructures.StructuredClassifier;

import javax.annotation.CheckForNull;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;

class SimpleContextAction extends DefaultDiagramAction{ //MDAction
	
	public static String ACTION_COMMAND = "";
	private final String NEW = "New...";
	String OPERATION = "Operation ";
	
    public SimpleContextAction(@CheckForNull String id, String name)
    {
        super(id, name, null, null);
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
	public void actionPerformed(ActionEvent e){
    	
    	//*********************************SET LIBRARY IF NECESSARY********************************
    	if(AddContextMenuButton.asciiMathLibraryBlock == null || AddContextMenuButton.customFuncBlock == null){
			//javax.swing.JOptionPane.showMessageDialog(null, "Please select Libraries first!");		
			LibrarySelector ls = new LibrarySelector();
			if(!ls.openDialog()){return; }
		}
    	
    	
    	//**************************************SET OPERATIONS*************************************
    	java.util.Collection<Element> operationsCollection = AddContextMenuButton.asciiMathLibraryBlock.getOwnedElement();
		Object[] operationsObjectArray = operationsCollection.toArray();	//operationsObjectArray[0].getClass() ...OperationsImpl
		
			//cast from Object to Element
		DiagramContextMenuConfigurator.operations = new Element[operationsObjectArray.length];
		DiagramContextMenuConfigurator.operationsString = new String[operationsObjectArray.length - 1];
		for(int j=0; j<operationsObjectArray.length; j++){ 
			DiagramContextMenuConfigurator.operations[j] = (Element) operationsObjectArray[j];  //contains operations as Elements
			if(DiagramContextMenuConfigurator.operations[j].getHumanName().startsWith(OPERATION)){
				DiagramContextMenuConfigurator.operationsString[j] = DiagramContextMenuConfigurator.operations[j].getHumanName().substring(OPERATION.length()); //contains operation String names
			}
		}
    	
    	//************************************SET CUST FUNCTIONS***********************************
		java.util.Collection<Element> functionsCollection = AddContextMenuButton.customFuncBlock.getOwnedElement();
		Object[] functionsObjectArray = functionsCollection.toArray();	//operationsObjectArray[0].getClass() ...OperationsImpl
	
			//cast from Object to Element
		DiagramContextMenuConfigurator.functions = new Element[functionsObjectArray.length];
		DiagramContextMenuConfigurator.customFuncString = new String[functionsObjectArray.length - 1];
		for(int k=0; k<functionsObjectArray.length; k++){ 
			DiagramContextMenuConfigurator.functions[k] = (Element) functionsObjectArray[k];  //contains operations as Elements
			if(DiagramContextMenuConfigurator.functions[k].getHumanName().startsWith(OPERATION)){
				DiagramContextMenuConfigurator.customFuncString[k] = DiagramContextMenuConfigurator.functions[k].getHumanName().substring(OPERATION.length()); //contains operation String names
			}
		}
    	
    	//*********************************IF CLICKED ON BLOCK**************************************
    	if(DiagramContextMenuConfigurator.clickedOnBlock){
    		ACTION_COMMAND = e.getActionCommand();
    		
    		if(ACTION_COMMAND.equals(NEW)){
    			
    			Constraint newConst = Application.getInstance().getProject().getElementsFactory().createConstraintInstance();
    			DiagramContextMenuConfigurator.c = newConst;
    			DiagramContextMenuConfigurator.c.setOwner(DiagramContextMenuConfigurator.selectedBlock);	//under which block it lives
    			((StructuredClassifier)DiagramContextMenuConfigurator.selectedBlock).get_constraintOfConstrainedElement().add(newConst);	//to which block it's referred
    			DiagramContextMenuConfigurator.edit = false;
    			
    		}else{
    			//***************************SET SELECTED CONSTRAINT*********************************
    			int i = 0;
    			while(!DiagramContextMenuConfigurator.constraintNames[i].equals(ACTION_COMMAND)){ i++; }	//better campare the ID because more can have same name ""
    			DiagramContextMenuConfigurator.c = DiagramContextMenuConfigurator.constraints[i];
    		
    			//*******************************SHOULD EDIT?****************************************
    			if(DiagramContextMenuConfigurator.c.getOwnedElement().iterator().next() instanceof Expression 
    					&& ((Expression) DiagramContextMenuConfigurator.c.getOwnedElement().iterator().next()).getOperand().size()!=0 
    					&& ((Expression) DiagramContextMenuConfigurator.c.getOwnedElement().iterator().next()).getOperand().get(0) instanceof ElementValue//){	//true if constraint is not empty
    					&& !(DiagramContextMenuConfigurator.c.getOwnedElement().iterator().next() instanceof StringExpression)
    					){
    				
    				UMLExpression2String uml2strg = new UMLExpression2String((Expression) DiagramContextMenuConfigurator.c.getOwnedElement().iterator().next());	//give Expression to uml2string
    				DiagramContextMenuConfigurator.editExp = uml2strg.parse();
    				
    				DiagramContextMenuConfigurator.edit = true;
    				DiagramContextMenuConfigurator.isStringExp = false;
    				
    			}else if(DiagramContextMenuConfigurator.c.getOwnedElement().iterator().next() instanceof StringExpression 
    					&& ((StringExpression) DiagramContextMenuConfigurator.c.getOwnedElement().iterator().next()).getOperand().size()!=0 
    					){
    				
    				UMLStringExpression2String uml2strg = new UMLStringExpression2String((StringExpression) DiagramContextMenuConfigurator.c.getOwnedElement().iterator().next());	//give Expression to uml2string
    				DiagramContextMenuConfigurator.editExp = uml2strg.parse();
    				
    				DiagramContextMenuConfigurator.edit = true;
    				DiagramContextMenuConfigurator.isStringExp = true;
    				
    			}else{
    				
    				DiagramContextMenuConfigurator.edit = false;
    				
    			}
    		}
    	}
    	   	  	
    	//**************************INITIALIZE MATH EDITOR*******************************	
    	EventQueue.invokeLater(new Runnable() {		
			public void run() {
				try {
					MathEditorMain1 mathEditor = new MathEditorMain1();	//with list selection
					mathEditor.initialize();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
        
    }

}