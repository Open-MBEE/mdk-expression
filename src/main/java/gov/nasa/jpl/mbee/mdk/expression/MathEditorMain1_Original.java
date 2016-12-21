package gov.nasa.jpl.mbee.mdk.expression;

import javax.swing.JFrame;

import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.JLabel;
import java.awt.Color;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Toolkit;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

import org.w3c.dom.Document;

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.context.Parameter;
import net.sourceforge.jeuclid.converter.Converter;

import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MathEditorMain1_Original {

	private JFrame frmMathEditor;
	private JTextField textExpression;
	private JList<String> list_1;
	private JList<String> list;
	private JLabel lblRender;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private boolean infix = false, stringExp = false;
	public static boolean setLibraries = false;
	private JRadioButton rdbtnInfixStringExp, rdbtnPrefixExp;

	MathEditorMain1Controller controller;
	ListModelOperands operandslistModel;
	ListModelOperations operationsListModel;
	/**
	 * Create the application.
	 * @param operationsListModel 
	 */
	public MathEditorMain1_Original(MathEditorMain1Controller _controller, ListModelOperands _operandslistModel, ListModelOperations _operationsListModel) {
		controller = _controller;
		operandslistModel = _operandslistModel;
		operationsListModel = _operationsListModel;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		frmMathEditor = new JFrame();
		frmMathEditor.setIconImage(Toolkit.getDefaultToolkit().getImage(MathEditorMain1_Original.class.getResource("/sum.png")));
		frmMathEditor.setTitle("Constraint Editor");
		frmMathEditor.setBounds(100, 100, 470, 342);
		frmMathEditor.setResizable(false);
		frmMathEditor.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmMathEditor.getContentPane().setLayout(null);
		
		textExpression = new JTextField();
		textExpression.setBounds(12, 32, 330, 22);
		textExpression.setCaretColor(Color.RED);
		textExpression.setFocusTraversalKeysEnabled(false);//keep cursor in the text field
		frmMathEditor.getContentPane().add(textExpression);
		textExpression.setColumns(10);
		
		JLabel textExpressionLable = new JLabel("Enter asciiMathML Expression:");
		textExpressionLable.setBounds(12, 13, 210, 16);
		frmMathEditor.getContentPane().add(textExpressionLable);
		
		lblRender = new JLabel("");
		lblRender.setVerticalAlignment(SwingConstants.CENTER);
		lblRender.setBackground(Color.WHITE);
		lblRender.setBounds(12, 67, 330, 192);
		lblRender.setOpaque(true);
		frmMathEditor.getContentPane().add(lblRender);
		
		JButton btnConfirm = new JButton("Confirm");
		btnConfirm.addActionListener(controller);

		btnConfirm.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnConfirm.setBounds(354, 272, 98, 22);
		frmMathEditor.getContentPane().add(btnConfirm);

		JScrollPane scrollPaneOperands = new JScrollPane();
		scrollPaneOperands.setBounds(354, 85, 98, 75);
		scrollPaneOperands.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		frmMathEditor.getContentPane().add(scrollPaneOperands);
				
		list = new JList<String>(operandslistModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2){
					int position = textExpression.getCaretPosition();
					String currentText = textExpression.getText();
					textExpression.setText( currentText.substring(0, position) + list.getSelectedValue() + currentText.substring(position));
					textExpression.setCaretPosition(position+ list.getSelectedValue().length());
					textExpression.getCaret().setVisible(true);
					textExpression.requestFocus();
					list.clearSelection();
					
				}
			}
		});
		scrollPaneOperands.setViewportView(list);
		
		JScrollPane scrollPaneOperations = new JScrollPane();
		scrollPaneOperations.setBounds(354, 184, 98, 75);
		scrollPaneOperations.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		frmMathEditor.getContentPane().add(scrollPaneOperations);
		
		//create a string array for operations and customized functions
		/*int opL = DiagramContextMenuConfigurator.operationsString.length;
		int custL = DiagramContextMenuConfigurator.customFuncString.length;
		int totalLength = opL + custL;
		String[] combinedOpString = new String[totalLength];
		for(int i=0; i < opL; i++){ combinedOpString[i] = DiagramContextMenuConfigurator.operationsString[i];}
		for(int i=0; i < custL; i++){ combinedOpString[opL + i] = DiagramContextMenuConfigurator.customFuncString[i];}*/
		
		list_1 = new JList<String>(operationsListModel); //initialize with a string array
		list_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2){
					int position = textExpression.getCaretPosition();
					String currentText = textExpression.getText();
					textExpression.setText( currentText.substring(0, position)+ list_1.getSelectedValue() + currentText.substring(position));
					textExpression.setCaretPosition(position+ list_1.getSelectedValue().length());
					textExpression.getCaret().setVisible(true);
					textExpression.requestFocus();
					list_1.clearSelection();
				}
			}
		});
		scrollPaneOperations.setViewportView(list_1);
	
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
		btnLibraries.addActionListener(controller.getLibraryButtonActionListener());
		btnLibraries.setBounds(354, 32, 98, 22);
		frmMathEditor.getContentPane().add(btnLibraries);
		
	}
	public void displayExpression(String _editExpression, boolean _isStringExpression){
			
		if (_editExpression.length() != 0 ){
			if(_isStringExpression){
				rdbtnInfixStringExp.setSelected(true);
			}else{
				rdbtnPrefixExp.setSelected(true);
			}
			
			textExpression.setText(_editExpression);
			
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
	public boolean isPrefixExpSelected(){
		if(rdbtnPrefixExp.isSelected()) 
			return true;
		else
			return false;
	}
	public String getTextExpression(){
		return this.textExpression.getText();
	}
	public void setLblRenderIcon(BufferedImage pic){
		lblRender.setIcon(new ImageIcon(pic));
	}
	public void setTextExpressionCaretNotVisible(){
		this.textExpression.getCaret().setVisible(false);
	}
	public JTextField getTextField(){
		return this.textExpression;
	}
}
