package gov.nasa.jpl.mbee.mdk.expression;

import com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdtemplates.StringExpression;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.*;

import java.util.List;

public class UMLExpression2String extends UML2String {

    private final String OPERATIONS_PRES = "+ - * / ^";        //set a parenthesis if difference at which position they occur in the string is greater than 1!!!
    private List<String> customFunctionsString; //used only root is NOT StringExpression

    public UMLExpression2String(ValueSpecification root, List<String> _customFunctionsString) {
        super(root);
        this.customFunctionsString = _customFunctionsString;
    }

    public String parse() {
        parse0(root);
        return strg;
    }

    private void parse0(ValueSpecification n) {

        if (n instanceof StringExpression) {
            ValueSpecification zeroNode = ((StringExpression) n).getOperand().get(0);
            boolean isLiteralString = zeroNode instanceof LiteralString ? true : false;

            if (isLiteralString) {    //LITERAL STRING
                //**************************************DO & TRAVERSE********************************
                for (int i = 0; i < ((StringExpression) n).getOperand().size(); i++) {
                    parse0(((ValueSpecification) ((StringExpression) n).getOperand().get(i)));    //child
                }
                //****************************************RETURN*************************************
                return;
            }
            else { // Not literal string

                if (((StringExpression) n).getOperand().size() == 3) {//BINARY NODE

                    //**************************************DO & TRAVERSE********************************
                    parse0(zeroNode);    //left child
                    strg += ((NamedElement) ((ElementValue) ((StringExpression) n).getOperand().get(1)).getElement()).getName();    //get operation
                    parse0(((ValueSpecification) ((StringExpression) n).getOperand().get(2)));    //right child

                    //****************************************RETURN*************************************
                    return;

                }
                else {
                    String zeroNodeExpression = ((NamedElement) ((ElementValue) zeroNode).getElement()).getName();
                    strg += zeroNodeExpression;

                    if (((StringExpression) n).getOperand().size() == 4) { //UNARY NODE
                        //**************************************DO & TRAVERSE********************************
                        parse0(((ValueSpecification) ((Expression) n).getOperand().get(1)));
                        parse0(((ValueSpecification) ((Expression) n).getOperand().get(2)));    //child
                        parse0(((ValueSpecification) ((Expression) n).getOperand().get(3)));

                        //****************************************RETURN*************************************
                        return;
                    }
                    else if (((StringExpression) n).getOperand().size() > 4) {//CUSTOMIZED FUNCTION (if lenght < 4 goes into unary node; could change that, but makes no difference for now; be aware of it though)
                        //**************************************DO & TRAVERSE********************************
                        strg += "(";
                        for (int i = 2; i < ((Expression) n).getOperand().size() - 1; i++) {
                            parse0(((ValueSpecification) ((Expression) n).getOperand().get(i)));    //child
                        }
                        strg += ")";
                        //****************************************RETURN*************************************
                        return;

                    }
                    else if (((Expression) n).getOperand().size() == 2) {////NEGATIVE VALUE
                        //**************************************DO & TRAVERSE********************************
                        parse0(((ValueSpecification) ((Expression) n).getOperand().get(1)));
                        //****************************************RETURN*************************************
                        return;
                    }
                }
            }// end of !LiteralString
        }//end of if StringExpression
        else if (n instanceof Expression) {
            String currentNode = ((NamedElement) ((ElementValue) ((Expression) n).getOperand().get(0)).getElement()).getName();
            if (customFunctionsString.contains(currentNode))//CUSTOMIZED FUNCTION NODE
            {
                //**************************************DO & TRAVERSE********************************
                strg += currentNode;    //get operation
                strg += "(";
                for (int i = 1; i < ((Expression) n).getOperand().size(); i++) {
                    parse0(((ValueSpecification) ((Expression) n).getOperand().get(i)));    //child
                    strg += ",";
                }
                strg = strg.substring(0, strg.length() - 1);
                strg += ")";

                //****************************************RETURN*************************************
                return;
            }
            else if (((Expression) n).getOperand().size() == 3) {    //BINARY NODE

                //*******************************ARE PARENTHESES NEEDED?*****************************
                String parent = "defaultStartValue";

                boolean parIsNeeded = false;
                if (n != root) {
                    parent = ((NamedElement) ((ElementValue) ((Expression) n.getOwner()).getOperand().get(0)).getElement()).getName();
                }
                if (OPERATIONS_PRES.contains(parent) && (OPERATIONS_PRES.indexOf(parent) - OPERATIONS_PRES.indexOf(currentNode)) > 1) {    //parent!= unary and root
                    parIsNeeded = true;
                }

                //**************************************DO & TRAVERSE********************************
                if (parIsNeeded) {
                    strg += "(";
                }    //add parentheses only when necessary
                parse0(((ValueSpecification) ((Expression) n).getOperand().get(1)));    //left child
                strg += currentNode;    //get operation
                parse0(((ValueSpecification) ((Expression) n).getOperand().get(2)));    //right child
                if (parIsNeeded) {
                    strg += ")";
                }    //add parentheses only when necessary

                //****************************************RETURN*************************************
                return;

            }
            else if (((Expression) n).getOperand().size() == 2) {        //UNARY NODE

                //**************************************DO & TRAVERSE********************************
                strg += currentNode;    //get operation
                strg += "(";
                parse0(((ValueSpecification) ((Expression) n).getOperand().get(1)));    //child
                strg += ")";

                //****************************************RETURN*************************************
                return;
            }
        }

        else if (n instanceof ElementValue) {    //leaf

            //******************************************DO***************************************
            strg += ((NamedElement) ((ElementValue) n).getElement()).getName();

            //****************************************RETURN*************************************
            return;

        }
        else if (n instanceof LiteralReal) {        //leaf

            //******************************************DO***************************************
            strg += ((LiteralReal) n).getValue();    //get literal real

            //****************************************RETURN*************************************
            return;
        }

    }
}
