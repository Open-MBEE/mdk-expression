package gov.nasa.jpl.mbee.mdk.expression;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.w3c.dom.Document;

import com.nomagic.magicdraw.openapi.uml.SessionManager;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Constraint;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ValueSpecification;

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.context.Parameter;
import net.sourceforge.jeuclid.converter.Converter;



public class MathEditorMain1Controller implements ActionListener {
	
	private MathEditorMain1Model model;
	private MathEditorMain1Controller controller;
	private MathEditorMain1  view;
	private ActionListener libraryButtonActionListener;
	
	private ListModelOperands operandsListModel;
	private ListModelOperations operationsListModel;
	
	public MathEditorMain1Controller(SelectedConstraintBlock _selectedConstraintBlock, Constraint _currentConstraint){
		
		model = new MathEditorMain1Model(_selectedConstraintBlock, _currentConstraint);
		controller = this;
		
		libraryButtonActionListener = ( event -> { //Updating Operations by asking a user to select asciiMathLibraryBlock and CustomFunction
				//when "Libraries..." button is pressed.
				LibrarySelector ls = new LibrarySelector();
				if(ls.openDialog()){
					model.setOperationAndCustromFunctions();
				}
				updateOperationsListModel(); //updating view's operations
			}
		);
	}
	public void showView() 
	{
		EventQueue.invokeLater(() -> {
				try {
					operandsListModel = new ListModelOperands(model.getOperands());
					operationsListModel = new ListModelOperations(model.getCombinedOperations());
					
					view = new MathEditorMain1(controller, operandsListModel, operationsListModel);	//with list selection
					view.initialize();
					
					//AutoComplete in the expression.  The suggestion only contains operands and custom functions.
					List<String> words = model.getOperandsInString();
					words.addAll(model.getCustomOperationsInString());
					new AutoCompleteJTextField( view.getTextField(), words);
					
					view.displayExpression(model.getEditExpression(), model.isStringExpression());
					
				} catch (Exception e) {
					e.printStackTrace();
				}
		});
	} 
	private void updateOperationsListModel(){
		this.operationsListModel.reset(model.getCombinedOperations());
	}
	public ActionListener getLibraryButtonActionListener() { return this.libraryButtonActionListener;} 
	
	public Element getConstraintBlock(){
		return model.getConstraintBlock();
	}
	public Element getCombinedOperation(String _operationString){
		return model.getCombinedOperation(_operationString);
	}
	public Element getOperand(String _operandString){
		return model.getOperand(_operandString);
	}
	public void addOperand(Property _newOperand){
		this.operandsListModel.add(_newOperand);
	}
	
	public void actionPerformed(ActionEvent e) {
		
		SessionManager.getInstance().createSession("Confirm Pressed");
			
		String textExpression = view.getTextExpression();
		AsciiMathML2Tree a2t = new AsciiMathML2Tree(textExpression);
		ParseTree pt = a2t.parse();
		ValueSpecification vs;
		
		Tree2UMLExpression t2uml = null;
		if(view.isPrefixExpSelected()){	//PARSE IN PREFIX NOTATION
			t2uml = new Tree2UMLExpression_Prefix(controller, pt);
		} 
		else {	//PARSE IN INFIX NOTATION
			t2uml = new Tree2UMLExpression_InfixString(controller, pt);
		}
		vs = t2uml.parse();	
			
		if(!t2uml.getError()){
			//a2t.showTree();	//shows the LISP tree generated by antlr
			
			vs.setOwner(this.model.currentConstraint);
		
			if(!view.isPrefixExpSelected()){
				//***********************************2 STRING EXPRESSION******************************************
				Exp2StringExp transE2SE = new Exp2StringExp(vs);
				ValueSpecification vsNew = transE2SE.transform();
				vsNew.setOwner(this.model.currentConstraint);
			}
						
			//**************************RENDER EXPRESSION************************
			AsciiMathParser amp = new AsciiMathParser();
			Document docExp = amp.parseAsciiMath(textExpression);
			Converter getPic = Converter.getInstance();
			LayoutContextImpl l = (LayoutContextImpl) LayoutContextImpl.getDefaultLayoutContext();
			l.setParameter(Parameter.MATHSIZE, 30);
			BufferedImage pic;
			try {
				pic = getPic.render(docExp, (LayoutContext) l);	//LayoutContextImpl.getDefaultLayoutContext());
				view.setLblRenderIcon(pic);
				view.setTextExpressionCaretNotVisible();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			//*******************************************************************
		}
		SessionManager.getInstance().closeSession();
	}
}
