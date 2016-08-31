package plugin.matheditor;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import java.awt.List;

import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Desktop;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Toolkit;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

import org.antlr.v4.runtime.tree.ParseTree;
import org.w3c.dom.Document;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.openapi.uml.SessionManager;
import com.nomagic.magicdraw.ui.dialogs.MDDialogParentProvider;
import com.nomagic.magicdraw.ui.dialogs.SelectElementInfo;
import com.nomagic.magicdraw.ui.dialogs.SelectElementTypes;
import com.nomagic.magicdraw.ui.dialogs.selection.ElementSelectionDlg;
import com.nomagic.magicdraw.ui.dialogs.selection.ElementSelectionDlgFactory;
import com.nomagic.magicdraw.uml.BaseElement;
import com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdtemplates.StringExpression;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ElementValue;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Expression;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.NamedElement;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ValueSpecification;
import com.nomagic.uml2.ext.magicdraw.compositestructures.mdinternalstructures.StructuredClassifier;

import asciimathml.parser.AsciiMathML2Tree;
import javassist.bytecode.Descriptor.Iterator;
import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.context.Parameter;
import net.sourceforge.jeuclid.converter.Converter;

import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JScrollBar;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class MathEditorMain1 {

	private JFrame frmMathEditor;
	private JTextField textExpression;
	//private String expression;
	private String OPERATION = "Operation ";
	private JList<String> list_1;
	private JList<String> list;
	private JLabel lblRender;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private boolean infix = false, stringExp = false;
	public static boolean setLibraries = false;
	private JRadioButton rdbtnInfixStringExp, rdbtnPrefixExp;

	/**
	 * Create the application.
	 */
	public MathEditorMain1() {
		//initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		frmMathEditor = new JFrame();
		frmMathEditor.setIconImage(Toolkit.getDefaultToolkit().getImage(MathEditorMain1.class.getResource("/plugin/matheditor/sum.png")));
		frmMathEditor.setTitle("Constraint Editor");
		frmMathEditor.setBounds(100, 100, 470, 342);
		frmMathEditor.setResizable(false);
		frmMathEditor.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmMathEditor.getContentPane().setLayout(null);
		
		textExpression = new JTextField();
		textExpression.setBounds(12, 32, 330, 22);
		frmMathEditor.getContentPane().add(textExpression);
		textExpression.setColumns(10);
		
		JLabel lblEnterAsciiExp = new JLabel("Enter asciiMathML Expression:");
		lblEnterAsciiExp.setBounds(12, 13, 210, 16);
		frmMathEditor.getContentPane().add(lblEnterAsciiExp);
		
		lblRender = new JLabel("");
		lblRender.setVerticalAlignment(SwingConstants.CENTER);
		lblRender.setBackground(Color.WHITE);
		lblRender.setBounds(12, 67, 330, 192);
		lblRender.setOpaque(true);
		frmMathEditor.getContentPane().add(lblRender);
		
		JButton btnSave = new JButton("Confirm");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				SessionManager.getInstance().createSession("testing");
				AsciiMathML2Tree a2t = new AsciiMathML2Tree(textExpression.getText());
				ParseTree pt = a2t.parse();
				ValueSpecification vs;
				
				if(rdbtnPrefixExp.isSelected()){	//PARSE IN PREFIX NOTATION
					Tree2UMLExpression t2uml = new Tree2UMLExpression(pt, DiagramContextMenuConfigurator.operations, DiagramContextMenuConfigurator.operands, DiagramContextMenuConfigurator.functions, DiagramContextMenuConfigurator.operationsString, DiagramContextMenuConfigurator.operandsString, DiagramContextMenuConfigurator.customFuncString);
					vs = t2uml.parse();
				}else{	//PARSE IN INFIX NOTATION
					Tree2UMLExpressionInfixPar t2uml = new Tree2UMLExpressionInfixPar(pt, DiagramContextMenuConfigurator.operations, DiagramContextMenuConfigurator.operands, DiagramContextMenuConfigurator.functions, DiagramContextMenuConfigurator.operationsString, DiagramContextMenuConfigurator.operandsString, DiagramContextMenuConfigurator.customFuncString);
					vs = t2uml.parse();
				}
				
				if(!Tree2UMLExpression.error){
					//a2t.showTree();	//shows the LISP tree generated by antlr
					
					vs.setOwner(DiagramContextMenuConfigurator.c);
				
					if(rdbtnInfixStringExp.isSelected()){
						//***********************************2 STRING EXPRESSION******************************************
						Exp2StringExp transE2SE = new Exp2StringExp(vs);
						ValueSpecification vsNew = transE2SE.transform();
						vsNew.setOwner(DiagramContextMenuConfigurator.c);
					}
								
					//**************************RENDER EXPRESSION************************
					AsciiMathParser amp = new AsciiMathParser();
					Document docExp = amp.parseAsciiMath(textExpression.getText());
					Converter getPic = Converter.getInstance();
					LayoutContextImpl l = (LayoutContextImpl) LayoutContextImpl.getDefaultLayoutContext();
					l.setParameter(Parameter.MATHSIZE, 30);
					BufferedImage pic;
					try {
						pic = getPic.render(docExp, (LayoutContext) l);	//LayoutContextImpl.getDefaultLayoutContext());
						lblRender.setIcon(new ImageIcon(pic));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					//*******************************************************************
				}
				
				SessionManager.getInstance().closeSession();
			
			}
		});
		btnSave.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnSave.setBounds(354, 272, 98, 22);
		frmMathEditor.getContentPane().add(btnSave);
	
		/*	//button help would display the asciimathml syntax in browser
		JButton btnHelp = new JButton("Help");
		btnHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Desktop.getDesktop().browse(new URI(ASCII_MATH_SYNTAX_URI));
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}
		});
		btnHelp.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnHelp.setBounds(152, 272, 88, 22);
		frmMathEditor.getContentPane().add(btnHelp);
		 */	
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(354, 85, 98, 75);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		frmMathEditor.getContentPane().add(scrollPane);
		
		list = new JList(DiagramContextMenuConfigurator.operandsString);
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2){
					textExpression.setText(textExpression.getText() + list.getSelectedValue());
				}
			}
		});
		scrollPane.setViewportView(list);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(354, 184, 98, 75);
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		frmMathEditor.getContentPane().add(scrollPane_1);
		
		//create a string array for operations and customized functions
		int opL = DiagramContextMenuConfigurator.operationsString.length;
		int custL = DiagramContextMenuConfigurator.customFuncString.length;
		int totalLength = opL + custL;
		String[] combinedOpString = new String[totalLength];
		for(int i=0; i < opL; i++){ combinedOpString[i] = DiagramContextMenuConfigurator.operationsString[i];}
		for(int i=0; i < custL; i++){ combinedOpString[opL + i] = DiagramContextMenuConfigurator.customFuncString[i];}
		
		list_1 = new JList<String>(combinedOpString); //initialize with a string array
		list_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2){
					textExpression.setText(textExpression.getText() + list_1.getSelectedValue());
				}
			}
		});
		scrollPane_1.setViewportView(list_1);
	
		JLabel lblOperands = new JLabel("Operands:");
		lblOperands.setBounds(354, 67, 88, 16);
		frmMathEditor.getContentPane().add(lblOperands);
		
		JLabel lblOperations = new JLabel("Operations:");
		lblOperations.setBounds(354, 164, 88, 16);
		frmMathEditor.getContentPane().add(lblOperations);
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmMathEditor.dispose();
			}
		});
		btnClose.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnClose.setBounds(254, 272, 88, 22);
		frmMathEditor.getContentPane().add(btnClose);
		frmMathEditor.setVisible(true);
		
		rdbtnInfixStringExp = new JRadioButton("infix String Exp.");
		buttonGroup.add(rdbtnInfixStringExp);
		rdbtnInfixStringExp.setBounds(104, 271, 117, 25);
		frmMathEditor.getContentPane().add(rdbtnInfixStringExp);
		
		rdbtnPrefixExp = new JRadioButton("prefix Exp.");
		rdbtnPrefixExp.setSelected(true);
		buttonGroup.add(rdbtnPrefixExp);
		rdbtnPrefixExp.setBounds(12, 271, 88, 25);
		frmMathEditor.getContentPane().add(rdbtnPrefixExp);
		
		JButton btnLibraries = new JButton("Libraries...");
		btnLibraries.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				LibrarySelector ls = new LibrarySelector();
				if(ls.openDialog()){
				
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
				}
				
				//create a string array for operations and customized functions
				int opL = DiagramContextMenuConfigurator.operationsString.length;
				int custL = DiagramContextMenuConfigurator.customFuncString.length;
				int totalLength = opL + custL;
				String[] combinedOpString = new String[totalLength];
				for(int i=0; i < opL; i++){ combinedOpString[i] = DiagramContextMenuConfigurator.operationsString[i];}
				for(int i=0; i < custL; i++){ combinedOpString[opL + i] = DiagramContextMenuConfigurator.customFuncString[i];}
				
				list_1.setListData(combinedOpString);
			}
		});
		
		btnLibraries.setBounds(354, 32, 98, 22);
		frmMathEditor.getContentPane().add(btnLibraries);
		
		if(DiagramContextMenuConfigurator.edit){	//if true, display and render existing string expression on opening
			
			if(DiagramContextMenuConfigurator.isStringExp){
				rdbtnInfixStringExp.setSelected(true);
			}else{
				rdbtnPrefixExp.setSelected(true);
			}
			
			textExpression.setText(DiagramContextMenuConfigurator.editExp);
			
			//**************************RENDER EXPRESSION************************
			AsciiMathParser amp = new AsciiMathParser();
			Document docExp = amp.parseAsciiMath(textExpression.getText());
			Converter getPic = Converter.getInstance();
			LayoutContextImpl l = (LayoutContextImpl) LayoutContextImpl.getDefaultLayoutContext();
			l.setParameter(Parameter.MATHSIZE, 30);
			BufferedImage pic;
			try {
				pic = getPic.render(docExp, (LayoutContext) l);
				lblRender.setIcon(new ImageIcon(pic));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			//*******************************************************************
			
		}
	}
}
