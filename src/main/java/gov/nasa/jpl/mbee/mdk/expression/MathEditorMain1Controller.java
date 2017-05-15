package gov.nasa.jpl.mbee.mdk.expression;

import com.nomagic.magicdraw.openapi.uml.SessionManager;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Constraint;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ValueSpecification;
import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.context.Parameter;
import net.sourceforge.jeuclid.converter.Converter;
import org.antlr.v4.runtime.tree.ParseTree;
import org.w3c.dom.Document;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;


public class MathEditorMain1Controller implements ActionListener {

    private static boolean DEBUG = false;
    private MathEditorMain1Model model;
    private MathEditorMain1Controller controller;
    private MathEditorMain1 view;
    private ActionListener libraryButtonActionListener;

    private ListModelOperands operandsListModel;
    private ListModelOperations operationsListModel;

    public MathEditorMain1Controller(SelectedOperationBlocks _selectedOperationBlocks, SelectedConstraintBlock _selectedConstraintBlock, Constraint _currentConstraint) {

        model = new MathEditorMain1Model(_selectedConstraintBlock, _currentConstraint, _selectedOperationBlocks);
        controller = this;

        libraryButtonActionListener = (event -> { //Updating Operations by asking a user to select asciiMathLibraryBlock and CustomFunction
            //when "Libraries..." button is pressed.
            LibrarySelector lso = new LibrarySelector();
            if (lso.openDialog()) {
                model.resetSelectedOperationBlocks(lso.getAsciiLibrary(), lso.getCustomFunction());
            }
            updateOperationsListModel(); //updating view's operations
        }
        );
    }

    public void showView() {
        EventQueue.invokeLater(() -> {
            try {
                operandsListModel = new ListModelOperands(model.getOperands());
                operationsListModel = new ListModelOperations(model.getOperations());

                view = new MathEditorMain1(controller, operandsListModel, operationsListModel);    //with list selection
                view.initialize();

                //AutoComplete in the expression.  The suggestion only contains operands and custom functions.
                List<String> words = model.getOperandsAndOperationsInStringForAutoComplete();
                //model.getOperandsInString();
                new AutoCompleteJTextField(view.getTextField(), words);
                view.displayExpression(model.getEditExpression(), model.isStringExpression(), model.getName());

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    //return true if GUI operandsList size is same as md model's data.  Otherwise return false
    public boolean isOperandsListChanged() {
        if (operandsListModel.getSize() != model.getSelectedConstraintBlock().getOperands().size()) {
            return true;
        }
        return false;
    }

    //return true if GUI operationsList size is the same as md model's data.  Otherwise return false
    public boolean isOperationsListChange() {
        if (operationsListModel.getSize() != model.getSelectedOperationBlocks().getOperationSize()) {
            return true;
        }
        return false;
    }


    public void updateOperationsListModel() {
        this.operationsListModel.reset(model.getOperations());
    }

    public void updateOperandsListModel() {
        if (this.operandsListModel != null) //null when testing
        {
            this.operandsListModel.reset(model.getOperands());
        }
    }

    public ActionListener getLibraryButtonActionListener() {
        return this.libraryButtonActionListener;
    }

    public Element getConstraintBlock() {
        return model.getConstraintBlock();
    }

    public Element getAsciiMathLibraryBlock() {
        return this.model.getAsciiMathLibraryBlock();
    }

    public Element getCustomFunctionBlock() {
        return this.model.getConstraintBlock();
    }

    public Element getOperationAsciiMathLibrary(String _lookingfor) {
        return model.getOperationAsciiLibrary(_lookingfor);
    }

    public Element getOperationCustom(String _lookingfor) {
        return model.getOperationCustom(_lookingfor);
    }

    public Element getOperand(String _operandString) {
        return model.getOperand(_operandString);
    }

    //used to add a constraint parameter created during "confirm"
    /*public void addOperand(Property _newOperand){
		this.operandsListModel.add(_newOperand);
	}*/
	/*
	public void addOperation(Element _newOperation){
		this.operationsListModel.add(_newOperation);
	}*/
    public void setName(String _name) {
        this.model.setCurrentConstraintName(_name);
    }

    public void actionPerformed(ActionEvent e) {

        if (SessionManager.getInstance().isSessionCreated()) {
            SessionManager.getInstance().closeSession();
        }
        SessionManager.getInstance().createSession(e.getActionCommand());

        //action for confirm button
        if (e.getActionCommand() == MathEditorMain1.CONFIRM) {

            String textExpression = view.getTextExpression();
            if (textExpression.compareTo(this.model.getEditExpression()) != 0) {

                //save name in case it changed
                saveConstraintName();

                ValueSpecification vs;
                ValueSpecification originalvs = this.model.currentConstraint.getSpecification();

                Tree2UMLExpression t2uml = null;
                if (view.selectedRadioButton() == MathEditorMain1.RadioButton.PREFIX) {    //PARSE IN PREFIX NOTATION

                    AsciiMathML2Tree a2t = new AsciiMathML2Tree(textExpression);
                    ParseTree pt = null;
                    try {
                        pt = a2t.parse();
                        if (DEBUG) {
                            a2t.showTree();    //shows the LISP tree generated by an
                        }
                    } catch (Exception pe) {
                        //parsing failed.
                        Tree2UMLExpression.showMessage("Error: Problem in expression for prefix.");
                        return;
                    }

                    t2uml = new Tree2UMLExpression_Prefix(controller, pt, originalvs);
                    try {
                        vs = t2uml.parse();
                    } catch (Exception e2) {
                        Tree2UMLExpression.showMessage("Error: Problem in expression for prefix.");
                        return;
                    }
                    AsciiMathParser amp = new AsciiMathParser();
                    Document docExp = amp.parseAsciiMath(textExpression/*, opt*/);
                    render(docExp, false);
                    this.model.setExpression(textExpression);

                    if (!t2uml.getError()) {
                        vs.setOwner(this.model.currentConstraint);
                        this.model.setExpression(textExpression);
                    }

                }
                else if (view.selectedRadioButton() == MathEditorMain1.RadioButton.INFIX) {
                    //**************************RENDER EXPRESSION************************
                    //optional to be included in AsciiMathParser.parseAsciiMath method to include annotataion of orinal expression
                    //AsciiMathParserOptions opt = new AsciiMathParserOptions();
                    //opt.setAddSourceAnnotation(true);

                    AsciiMathParser amp = new AsciiMathParser();
                    textExpression = textExpression.replace("|", "\u2223"); //having | character having problem with creating xml
					/*
					try {
					    String myString = "\u0048\u0065\u006C\u006C\u006F World";
					    byte[] utf8Bytes = myString.getBytes("UTF8");
					    String text = new String(utf8Bytes,"UTF8");
					}
					catch (UnsupportedEncodingException e) {
					    e.printStackTrace();
					}
					*/

                    Document docExp = amp.parseAsciiMath(textExpression/*, opt*/);

                    if (DEBUG) {
                        String x = Doc2InfixStringUtil.printXML(docExp);
                        System.out.println(x);
                    }

                    DocPreprocess p = new DocPreprocess(docExp);
                    docExp = p.process();
                    if (DEBUG) {
                        String x = Doc2InfixStringUtil.printXML(docExp);
                        System.out.println(x);
                    }


                    Doc2InfixString gg = new Doc2InfixString(controller, docExp);
                    try {
                        vs = gg.traverse0(null);
                        Exp2StringExp transE2SE = new Exp2StringExp(vs);
                        ValueSpecification vsNew = transE2SE.transform();
                        vsNew.setOwner(this.model.currentConstraint);


                        UMLStringExpression2String uml2string = new UMLStringExpression2String(((ValueSpecification) vsNew));
                        String newTextExpression = uml2string.parse();
                        newTextExpression = newTextExpression.replace("|", "\u2223"); //having | character having problem with creating xml
                        Document newDocExp = amp.parseAsciiMath(newTextExpression);

                        if (DEBUG) {
                            String x = Doc2InfixStringUtil.printXML(newDocExp);
                            System.out.println(x);
                        }


                        render(newDocExp, false);
                        //render(docExp, false);
                        this.model.setExpression(textExpression);

                    } catch (Exception e2) {
                        // TODO Auto-generated catch block
                        e2.printStackTrace();
                        Tree2UMLExpression.showMessage("Error: " + e2.getMessage());

                        try {
                            docExp.getFirstChild().appendChild(createMrowMessage(docExp, "(Invalid!)"));
                            render(docExp, true);
                        } catch (Exception ee) {
                            ee.printStackTrace();
                        }
                    }
                }
                //save name in case it changed

                saveConstraintName();
            }
            else {
                javax.swing.JOptionPane.showMessageDialog(null, "Expression not changed!");
            }

        }
        else if (e.getActionCommand() == MathEditorMain1.nameButton.EDIT.buttonName()) { //press edit button for name
            this.view.setNameFileldEditable(true);
            this.view.setButtonName(MathEditorMain1.nameButton.SAVE.buttonName());
        }
        else if (e.getActionCommand() == MathEditorMain1.nameButton.SAVE.buttonName()) { //press save button for name
            saveConstraintName();
        }
        SessionManager.getInstance().closeSession();
    }

    private org.w3c.dom.Element createMrowMessage(Document docExp, String mrowMessage) {

        char c[] = mrowMessage.toCharArray();
        org.w3c.dom.Element mrow = docExp.createElement("mrow");
        org.w3c.dom.Element mi0 = null;
        for (int i = 0; i < c.length; i++) {
            mi0 = docExp.createElement("mi");
            mi0.appendChild(docExp.createTextNode(new String(c)));
        }
        mrow.appendChild(mi0);
        return mrow;
    }


    private void render(Document docExp, boolean isError) {
        Converter getPic = Converter.getInstance();
        LayoutContextImpl l = (LayoutContextImpl) LayoutContextImpl.getDefaultLayoutContext();
        l.setParameter(Parameter.MATHSIZE, 30);
        if (isError) {
            l.setParameter(Parameter.MATHCOLOR, Color.RED);
        }
        else {
            l.setParameter(Parameter.MATHCOLOR, Color.BLACK);
        }
        BufferedImage pic;
        try {
            pic = getPic.render(docExp, (LayoutContext) l);    //LayoutContextImpl.getDefaultLayoutContext());
            view.setLblRenderIcon(pic);
            view.setTextExpressionCaretNotVisible();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void saveConstraintName() {
        this.model.setCurrentConstraintName(view.getName());
        this.view.setButtonName(MathEditorMain1.nameButton.EDIT.buttonName());
        this.view.setNameFileldEditable(false);
    }

    public MathEditorMain1Model getModel() {
        return model;
    }

    public ListModelOperands getOperandsListModel() {
        return operandsListModel;
    }

    public ListModelOperations getOperationsListModel() {
        return operationsListModel;
    }

}
