package plugin.matheditor;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Toolkit;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

import org.antlr.v4.runtime.tree.ParseTree;
import org.w3c.dom.Document;

import com.nomagic.magicdraw.openapi.uml.SessionManager;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;


import asciimathml.parser.AsciiMathML2Tree;

public class MathEditorMain {

	private JFrame frmMathEditor;
	private JTextField textExpression;
	private String ASCII_MATH_SYNTAX_URI = "http://asciimath.org/#syntax";

	/**
	 * Launch the application.
	 */
/*	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MathEditorMain window = new MathEditorMain();
					window.frmMathEditor.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
*/
	/**
	 * Create the application.
	 */
	public MathEditorMain() {
	/*	EventQueue.invokeLater(new Runnable() {		//Maybe a problem if I have to be in eventqueue to run new MathEditor
			public void run() {
				try {
					initialize();
					frmMathEditor.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	*/	
		//initialize();
	}
	/*
	public MathEditorMain(Element[] operations, Element[] operands, String[] operationsString, String[] operandsString){
		this.operations = operations;
		this.operands = operands;
		this.operationsString = operationsString;
		this.operandsString = operandsString;
	}
*/
	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		frmMathEditor = new JFrame();
		frmMathEditor.setIconImage(Toolkit.getDefaultToolkit().getImage(MathEditorMain.class.getResource("/plugin/matheditor/sum.png")));
		frmMathEditor.setTitle("Math Editor");
		frmMathEditor.setBounds(100, 100, 450, 300);
		frmMathEditor.setResizable(false);
		frmMathEditor.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmMathEditor.getContentPane().setLayout(null);
		
		textExpression = new JTextField();
		textExpression.setBounds(12, 32, 420, 22);
		frmMathEditor.getContentPane().add(textExpression);
		textExpression.setColumns(10);
		
		JLabel lblEnterAsciiExp = new JLabel("Enter asciiMathML Expression:");
		lblEnterAsciiExp.setBounds(12, 13, 210, 16);
		frmMathEditor.getContentPane().add(lblEnterAsciiExp);
		
		JLabel lblRender = new JLabel("");
		lblRender.setVerticalAlignment(SwingConstants.BOTTOM);
		lblRender.setBackground(Color.WHITE);
		lblRender.setBounds(12, 67, 420, 156);
		lblRender.setOpaque(true);
		frmMathEditor.getContentPane().add(lblRender);
		JButton btnSave = new JButton("Confirm");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				SessionManager.getInstance().createSession("testing");
				AsciiMathML2Tree a2t = new AsciiMathML2Tree(textExpression.getText());
				ParseTree pt = a2t.parse();
				a2t.showTree();
				Tree2UMLExpression t2uml = new Tree2UMLExpression(pt, DiagramContextMenuConfigurator.operations, DiagramContextMenuConfigurator.operands, DiagramContextMenuConfigurator.functions, DiagramContextMenuConfigurator.operationsString, DiagramContextMenuConfigurator.operandsString, DiagramContextMenuConfigurator.customFuncString);
				t2uml.parse().setOwner(DiagramContextMenuConfigurator.c);
				SessionManager.getInstance().closeSession();
		/*		
				//**************************RENDER EXPRESSION************************
				//org.w3c.dom, 
				AsciiMathParser amp = new AsciiMathParser();
				Document docExp = amp.parseAsciiMath(textExpression);
				Converter getPic = Converter.getInstance();
				pic = getPic.render(docExp, LayoutContextImpl.getDefaultLayoutContext());
				lblRender.setIcon(new ImageIcon(pic));
				//*******************************************************************
		*/		
			}
		});
		btnSave.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnSave.setBounds(344, 236, 88, 22);
		frmMathEditor.getContentPane().add(btnSave);
		
		JButton btnHelp = new JButton("Help");
		btnHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
				
			}
		});
		btnHelp.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnHelp.setBounds(244, 236, 88, 22);
		frmMathEditor.getContentPane().add(btnHelp);
		frmMathEditor.setVisible(true); 	//just added today
	}
}
