package gov.nasa.jpl.mbee.mdk.expression;

import java.util.Iterator;

import com.nomagic.actions.ActionsCategory;
import com.nomagic.actions.ActionsManager;
import com.nomagic.magicdraw.actions.DiagramContextAMConfigurator;
import com.nomagic.magicdraw.uml.symbols.DiagramPresentationElement;
import com.nomagic.magicdraw.uml.symbols.PresentationElement;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Constraint;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property;
import com.nomagic.uml2.ext.magicdraw.compositestructures.mdinternalstructures.StructuredClassifier;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class;

public class DiagramContextMenuConfigurator implements DiagramContextAMConfigurator{

	String CONSTRAINT = "Constraint";
	String CONSTRAINT_BLOCK = "Constraint Block";
	String CONSTRAINT_PARAMETER_ = "Constraint Parameter ";
	//String ASCII_LIBRARY = "Block asciiMathOperatorLibrary";
	//String CUSTOM_FUNC = "Block customFunctions";
	String OPERATION = "Operation ";
	Boolean DEBUG = false;
	public static Element[] operations, operands, functions;
	public static String[] operationsString, operandsString, constraintNames, customFuncString;
	public static Constraint[] constraints;
	public static Constraint c;
	public static String editExp = "";
	public static Element selectedBlock;
	public static boolean edit = false, clickedOnBlock = false, isInfix = false, isStringExp = false;
			
	@Override
	public int getPriority()
	{
		return DiagramContextAMConfigurator.MEDIUM_PRIORITY;
	}
	
	@Override
	public void configure(ActionsManager manager, DiagramPresentationElement myDiagramType, PresentationElement[] selected,
			PresentationElement requestor) { 
		
		ActionsCategory category = new ActionsCategory(null, "Constraint Editor");
		
		if(requestor != null && selected.length == 1 && selected[0].getElement() instanceof Class && selected[0].getHumanName().startsWith(CONSTRAINT_BLOCK)){	//requestor is block
			
			//********************************CLICKED ON BLOCK?********************************
			clickedOnBlock = true;
			
			//******************************ADD ACTION TO CATEGORY*****************************
			StructuredClassifier constBlockSC = (StructuredClassifier)selected[0].getElement();
			selectedBlock = selected[0].getElement();
			
			//**********************************GET STRING NAMES*******************************
			Iterator<Constraint> itC = constBlockSC.get_constraintOfConstrainedElement().iterator();
			constraintNames = new String[constBlockSC.get_constraintOfConstrainedElement().size()];
			for(int i=0; itC.hasNext(); i++){
				constraintNames[i] = itC.next().getName();
			}
			
			//*******************************GET ALL CONSTRAINTS*******************************
			Iterator<Constraint> itC2 = constBlockSC.get_constraintOfConstrainedElement().iterator();
			constraints = new Constraint[constBlockSC.get_constraintOfConstrainedElement().size()];
			for(int i=0; itC2.hasNext(); i++){
				constraints[i] = itC2.next();
			}
			
			//**********************************GET OPERANDS***********************************
			java.util.Collection<Property> operandsCollection = constBlockSC.getPart(); 
			Object[] operandsArray = operandsCollection.toArray(); //to access the single methods
					
				//cast Object Array to Element Array
			operands = new Element[operandsArray.length];
			operandsString = new String[operands.length];  //write strings in a array
			for(int i=0; i<operandsArray.length; i++){
				operands[i] = (Element) operandsArray[i];  //contains operands as Elements
				operandsString[i] = operands[i].getHumanName().substring(CONSTRAINT_PARAMETER_.length()); //contains operand String names
			}
	
			//*******************************CREATE CONTEXT MENU*******************************
			category.setNested(true);
			category.addAction(new SimpleContextAction(null, "New..."));
			
			for(int l=0; l<constraintNames.length; l++){
				category.addAction(new SimpleContextAction(null, constraintNames[l]));
			}
		}
		
		manager.addCategory(category);
		
	}

}