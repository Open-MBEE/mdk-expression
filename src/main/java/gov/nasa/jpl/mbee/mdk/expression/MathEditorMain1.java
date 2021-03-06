package gov.nasa.jpl.mbee.mdk.expression;

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.context.Parameter;
import net.sourceforge.jeuclid.converter.Converter;
import org.w3c.dom.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MathEditorMain1 {

    private JFrame frmMathEditor;
    private JTextField nameField;
    private JButton btnName;
    private JTextField textExpression;

    public static final String CONFIRM = "Confirm";

    public enum RadioButton {
        PREFIX, INFIX, PROMELALTL;
    }

    public enum nameButton {
        SAVE("Save"),
        EDIT("Edit");

        private String buttonName;

        nameButton(String _buttonName) {
            this.buttonName = _buttonName;
        }

        public String buttonName() {
            return buttonName;
        }
    }

    private JList<String> list_1;
    private JList<String> list;
    private JLabel lblRender;
    private final ButtonGroup buttonGroup = new ButtonGroup();
    //private boolean infix = false, stringExp = false;
    public static boolean setLibraries = false;
    private JRadioButton rdbtnInfixStringExp, rdbtnPrefixExp;//, rdbtnPromelaltl;

    MathEditorMain1Controller controller;
    ListModelOperands operandslistModel;
    ListModelOperations operationsListModel;
    int defaultFontSize = 12;
    LayoutContextImpl l;

    /**
     * Create the application.
     *
     * @param operationsListModel
     */
    public MathEditorMain1(MathEditorMain1Controller _controller, ListModelOperands _operandslistModel, ListModelOperations _operationsListModel) {
        controller = _controller;
        operandslistModel = _operandslistModel;
        operationsListModel = _operationsListModel;
    }

    /**
     * Initialize the contents of the frame.
     */
    public void initialize() {

        frmMathEditor = new JFrame("Constraint Editor");
        frmMathEditor.setIconImage(Toolkit.getDefaultToolkit().getImage(MathEditorMain1.class.getResource("/sum.png")));

        JPanel mainPane = new JPanel();
        mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));
        mainPane.setOpaque(true);

        textExpression = new JTextField() {
            @Override
            public Dimension getPreferredSize() {

                int fontsize = defaultFontSize;
                if (this.getParent() != null && this.getParent().getSize().width > 440) {
                    Double ratio = (new Double(this.getParent().getSize().width / 440.0));
                    fontsize = ratio.intValue() + fontsize;
                }
                this.setFont(MathEditorMain1.this.getFont(fontsize));

                return super.getPreferredSize();
            }
        };
        textExpression.setFocusTraversalKeysEnabled(false);//keep cursor in the text field
        textExpression.setColumns(10);

        nameField = new JTextField() {
            @Override
            public Dimension getPreferredSize() {

                int fontsize = defaultFontSize;
                if (this.getParent() != null && this.getParent().getSize().width > 440) {
                    Double ratio = (new Double(this.getParent().getSize().width / 440.0));
                    fontsize = ratio.intValue() + fontsize;
                }
                this.setFont(MathEditorMain1.this.getFont(fontsize));

                return super.getPreferredSize();
            }
        };
        nameField.setColumns(10);
        nameField.setEditable(false);

        btnName = new JButton("Edit");
        btnName.addActionListener(controller);
        ;
        btnName.setFont(getFont(defaultFontSize));

        Dimension level0 = new Dimension(600, 460);
        mainPane.setPreferredSize(level0);
        mainPane.setSize(level0);

        //TopPanel
        Dimension level1_top = new Dimension(600, 100);
        JPanel topPanel = new ResizablePanel(level1_top, level0);
        topPanel.setPreferredSize(level1_top);

        Dimension level1_top_L = new Dimension(600, 100);
        JPanel topLeftPanel = new ResizablePanel(level1_top_L, level1_top);
        topLeftPanel.setLayout(new BorderLayout());
        topLeftPanel.setLayout(new BoxLayout(topLeftPanel, BoxLayout.PAGE_AXIS));


        JPanel topLeftTopPanel = new JPanel();
        topLeftTopPanel.setBorder(BorderFactory.createTitledBorder("Name:"));
        topLeftTopPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.anchor = GridBagConstraints.PAGE_START; //bottom of space
        c.gridx = 0;
        c.gridwidth = 10;
        c.gridy = 0;
        topLeftTopPanel.add(nameField, c);
        c.anchor = GridBagConstraints.PAGE_END;
        c.gridx = 10;
        c.gridwidth = 1;
        c.gridy = 0;
        c.weightx = 0.1;
        c.insets = new Insets(0, 10, 0, 0);
        topLeftTopPanel.add(btnName, c);

        JPanel topLeftBottomPanel = new JPanel();
        topLeftBottomPanel.setBorder(BorderFactory.createTitledBorder("Enter asciiMathML Expression:"));
        topLeftBottomPanel.setLayout(new BoxLayout(topLeftBottomPanel, BoxLayout.PAGE_AXIS));
        topLeftBottomPanel.add(textExpression, BorderLayout.CENTER);


        topLeftPanel.add(topLeftTopPanel, BorderLayout.CENTER);
        topLeftPanel.add(topLeftBottomPanel, BorderLayout.CENTER);

        JButton lbotton = new JButton("Libraries...");
        lbotton.setFont(getFont(defaultFontSize));
        lbotton.addActionListener(controller.getLibraryButtonActionListener());
        topPanel.add(topLeftPanel);
        topLeftPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        //Middle Panel
        Dimension level1_middle = new Dimension(600, 306);
        JPanel middlePanel = new ResizablePanel(level1_middle, level0);
        //middlePanel.setBackground(Color.GREEN);

        Dimension level1_middle_L = new Dimension(450, 300);
        Dimension level1_middle_R = new Dimension(140, 300);
        //middleLeft
        JPanel middleLeftPanel = new ResizablePanel(level1_middle_L, level1_middle);

        middleLeftPanel.setLayout(new BorderLayout());
        middleLeftPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        middleLeftPanel.setBackground(Color.WHITE);

        lblRender = new JLabel();
        lblRender.setVerticalAlignment(SwingConstants.CENTER);
        lblRender.setBackground(Color.WHITE);
        lblRender.setOpaque(true);

        middleLeftPanel.add(lblRender, BorderLayout.CENTER);

        //moddleRight
        JPanel middleRightPanel = new ResizablePanel(level1_middle_R, level1_middle);
        middlePanel.add(middleLeftPanel);
        middlePanel.add(middleRightPanel);

        int rPanel_h = 290;
        int rPanel_list_h = 130;

        JScrollPane scrollPaneOperands = new JScrollPane();
        list = createJList(operandslistModel);
        scrollPaneOperands.setViewportView(list);

        JScrollPane scrollPaneOperations = new JScrollPane();
        list_1 = createJList(operationsListModel); //initialize with a string array
        scrollPaneOperations.setViewportView(list_1);

        //Top Panel - Operands
        JPanel top_listPane = createListPane("Operands", rPanel_h, rPanel_list_h, scrollPaneOperands);
        //Bottom Panel - Operations
        JPanel bottom_listPane = createListPane("Operations: ", rPanel_h, rPanel_list_h, scrollPaneOperations);

        middleRightPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        middleRightPanel.add(lbotton);
        middleRightPanel.add(top_listPane);
        middleRightPanel.add(bottom_listPane);

        //Bottom Panel
        Dimension level1_bottom = new Dimension(600, 30);
        JPanel bottomPanel = new ResizablePanel(level1_bottom, level0);

        Dimension level1_bottom_L = new Dimension(290, 30);
        Dimension level1_bottom_R = new Dimension(290, 30);
        JPanel bottomLeftPanel = new ResizablePanel(level1_bottom_L, level1_bottom);
        JPanel bottomRightPanel = new ResizablePanel(level1_bottom_R, level1_bottom);
        bottomRightPanel.setLayout(new GridBagLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        bottomPanel.add(bottomLeftPanel, BorderLayout.LINE_START);
        bottomPanel.add(bottomRightPanel, BorderLayout.LINE_END);

        //bottomLeftPanel
        rdbtnInfixStringExp = new JRadioButton("infix String Exp.");
        rdbtnInfixStringExp.setFont(getFont(defaultFontSize));
        buttonGroup.add(rdbtnInfixStringExp);

        rdbtnPrefixExp = new JRadioButton("prefix Exp.");
        rdbtnPrefixExp.setFont(getFont(defaultFontSize));
        buttonGroup.add(rdbtnPrefixExp);

        //this.rdbtnPromelaltl= new JRadioButton("Promela-ltl");
        //this.rdbtnPromelaltl.setFont(getFont(defaultFontSize));
        //buttonGroup.add(this.rdbtnPromelaltl);

        bottomLeftPanel.setLayout(new BoxLayout(bottomLeftPanel, BoxLayout.LINE_AXIS));
        bottomLeftPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        //bottomLeftPanel.add(rdbtnPrefixExp);
        //bottomLeftPanel.add(rdbtnInfixStringExp);
        //bottomLeftPanel.add(rdbtnPromelaltl);

        //bottomRightPanel
        //Confirm
        JButton btnConfirm = new JButton("Confirm");
        btnConfirm.addActionListener(controller);
        btnConfirm.setFont(getFont(defaultFontSize));
        //Close
        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frmMathEditor.dispose();
            }
        });
        btnClose.setFont(getFont(defaultFontSize));

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;       //aligned with button 2
        c.gridwidth = 1;   //2 columns wide
        c.gridy = 1;       //third row
        bottomRightPanel.add(btnClose, c);
        c.gridx = 1;
        bottomRightPanel.add(btnConfirm, c);

        //mainPane
        mainPane.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        mainPane.add(Box.createRigidArea(new Dimension(0, 3)));
        mainPane.add(topPanel);
        mainPane.add(Box.createRigidArea(new Dimension(0, 3)));
        mainPane.add(middlePanel);
        mainPane.add(Box.createRigidArea(new Dimension(0, 3)));
        mainPane.add(bottomPanel);

        mainPane.add(Box.createGlue());

        //addm main to frame
        frmMathEditor.getContentPane().add(mainPane);
        frmMathEditor.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Display
        frmMathEditor.pack();
        frmMathEditor.setVisible(true);

    }


    public void displayExpression(String _editExpression, boolean _isStringExpression, String _name) {

        if (_editExpression.length() != 0) {//means no expression yet = NEW constraint

            if (_isStringExpression) {
                rdbtnInfixStringExp.setSelected(true);
            }
            else {
                rdbtnPrefixExp.setSelected(true);
            }

            //String modifiedExp = _editExpression = _editExpression.replace("\u2223", "|");
            textExpression.setText(_editExpression.replace("\u2223", "|")); //need to have | instead of \u2223

            //**************************RENDER EXPRESSION************************
            AsciiMathParser amp = new AsciiMathParser();
            Document docExp = amp.parseAsciiMath(_editExpression); //need to have \u2223 instead of |
            Converter getPic = Converter.getInstance();
            LayoutContextImpl l = (LayoutContextImpl) LayoutContextImpl.getDefaultLayoutContext();
            l.setParameter(Parameter.MATHSIZE, 30);
            l.setParameter(Parameter.MATHCOLOR, Color.BLACK);
            BufferedImage pic;////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.,mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm
            try {
                pic = getPic.render(docExp, (LayoutContext) l);
                lblRender.setIcon(new ImageIcon(pic));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        else //if no expression set the radio button as Infix as default
        {
            rdbtnInfixStringExp.setSelected(true);
        }
        if (_name.length() == 0) {//no expression more likely NEW costraint
            setNameFileldEditable(true);
            setButtonName(MathEditorMain1.nameButton.SAVE.buttonName());
        }
        else {
            nameField.setText(_name);
        }
    }

    public void setNameFileldEditable(boolean _b) {
        this.nameField.setEditable(_b);
    }

    public void setButtonName(String _buttonName) {
        this.btnName.setText(_buttonName);
    }

    public RadioButton selectedRadioButton() {
        if (rdbtnPrefixExp.isSelected()) {
            return RadioButton.PREFIX;
        }
        else if (rdbtnInfixStringExp.isSelected()) {
            return RadioButton.INFIX;
        }
        else {
            return RadioButton.PROMELALTL;
        }
    }

    public String getName() {
        return this.nameField.getText();
    }

    public String getTextExpression() {
        return this.textExpression.getText();
    }

    public void setLblRenderIcon(BufferedImage pic) {
        lblRender.setIcon(new ImageIcon(pic));
    }

    public void setTextExpressionCaretNotVisible() {
        this.textExpression.getCaret().setVisible(false);
    }

    public JTextField getTextField() {
        return this.textExpression;
    }

    private class ResizablePanel extends JPanel {
        private static final long serialVersionUID = 1L;
        Resizable c;

        public ResizablePanel(Dimension _d, Dimension _parent) {
            super();
            c = new Resizable(_d, _parent, this);
        }

        @Override
        public Dimension getMaximumSize() {
            return c.getMaximumSize();
        }

        @Override
        public Dimension getMinimumSize() {
            return c.getMinimumSize();
        }

        @Override
        public Dimension getPreferredSize() {

            //return c.getPreferredSize();

            //change font size based on ratio
            Dimension d = c.getPreferredSize();
            if (this.getComponents().length > 0 && this.getComponents()[0] instanceof JScrollPane) {
                JScrollPane jscrollpane = (JScrollPane) this.getComponents()[0];
                for (int i = 0; i < jscrollpane.getComponentCount(); i++) {
                    if (jscrollpane.getComponent(i) instanceof JViewport) {

                        int fontsize = defaultFontSize;
                        if (d.width > 140) { //middle right column default = 140
                            Double ratio = (new Double(d.width / 140.0));
                            fontsize = ratio.intValue() + fontsize;
                        }
                        ((JList) ((JViewport) jscrollpane.getComponent(i)).getComponent(0)).setFont(MathEditorMain1.this.getFont(fontsize));
                        break;
                    }
                }
            }
            return d;
        }
    }

    private class Resizable {

        Component myC;
        Dimension parentD;
        double ratio_h, ratio_w;

        public Resizable(Dimension _d, Dimension _parent, Component _c) {
            myC = _c;
            myC.setPreferredSize(_d);
            parentD = _parent;
            ratio_w = _d.width / new Double(_parent.width);
            ratio_h = _d.height / new Double(_parent.height);
            myC.setMinimumSize(_d);
        }

        private Dimension getCustomDimensions() {
            Dimension d;
            if (myC.getParent() == null) {
                d = new Dimension((new Double(parentD.width * ratio_w)).intValue(), (new Double(parentD.height * ratio_h)).intValue());
            }
            else {
                d = new Dimension((new Double(myC.getParent().getSize().width * ratio_w)).intValue(), (new Double(myC.getParent().getSize().height * ratio_h)).intValue());
            }
            return d;
        }

        public Dimension getMaximumSize() {
            return getCustomDimensions();
        }

        public Dimension getMinimumSize() {
            return getCustomDimensions();
        }

        public Dimension getPreferredSize() {
            return getCustomDimensions();
        }

    }

    ;

    private JList<String> createJList(AbstractListModel _model) {

        JList<String> list = new JList<String>(_model); //initialize with a string array
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int position = textExpression.getCaretPosition();
                    String currentText = textExpression.getText();

                    String selectedValue = list.getSelectedValue();
                    if (MDSysMLConstants.suffixParentheses1.contains(selectedValue)) {
                        selectedValue = selectedValue + ("( )");
                        textExpression.setText(currentText.substring(0, position) + selectedValue + currentText.substring(position));
                        textExpression.setCaretPosition(position + selectedValue.length() - 1); //put caret inside ()
                    }
                    else if (MDSysMLConstants.suffixParentheses2.contains(selectedValue)) {
                        selectedValue = selectedValue + ("( )( )");
                        textExpression.setText(currentText.substring(0, position) + selectedValue + currentText.substring(position));
                        textExpression.setCaretPosition(position + selectedValue.length() - 1); //put caret inside ()
                    }
                    else {
                        textExpression.setText(currentText.substring(0, position) + selectedValue + currentText.substring(position));
                        textExpression.setCaretPosition(position + selectedValue.length());
                    }
                    textExpression.getCaret().setVisible(true);
                    textExpression.requestFocus();
                    list.clearSelection();
                }
            }
        });
        return list;
    }

    private JPanel createListPane(String _title, int rPanel_h, int rPanel_list_h, JScrollPane top_listScroller) {

        top_listScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        top_listScroller.setAlignmentX(Component.CENTER_ALIGNMENT);
        //Lay out the label and scroll pane from top to bottom.
        JPanel top_listPane = new ResizablePanel(new Dimension(100, rPanel_list_h), new Dimension(100, rPanel_h));
        top_listPane.setLayout(new BoxLayout(top_listPane, BoxLayout.PAGE_AXIS));
        top_listPane.add(top_listScroller);
        top_listPane.setBorder(BorderFactory.createTitledBorder(_title));

        return top_listPane;

    }

    private Font getFont(int fontsize) {
        return new Font("Tahoma", Font.PLAIN, fontsize);
    }
}
