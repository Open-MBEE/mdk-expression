package plugin.matheditor;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdtemplates.StringExpression;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ElementValue;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Expression;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralReal;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralString;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ValueSpecification;

public class Exp2StringExp {

	private ValueSpecification root;
	
	public Exp2StringExp(ValueSpecification root){
		this.root = root;
	}
	
	public ValueSpecification transform(){
		return transform0(root);
	}
	
	private ValueSpecification transform0(ValueSpecification n){
		
		if(n instanceof Expression){
			
			StringExpression strgExp = Application.getInstance().getProject().getElementsFactory().createStringExpressionInstance();
			
			for(int i=0; i<((Expression)n).getOperand().size(); i++){
					strgExp.getOperand().add(transform0(((Expression)n).getOperand().get(i)));
			}
		
			return strgExp;
			
		}else if(n instanceof LiteralString){
			
			LiteralString litString = Application.getInstance().getProject().getElementsFactory().createLiteralStringInstance();
			litString.setValue(((LiteralString) n).getValue());
			return litString;
			
		}else if(n instanceof ElementValue){
			
			ElementValue elemVal = Application.getInstance().getProject().getElementsFactory().createElementValueInstance();
			elemVal.setElement(((ElementValue) n).getElement());
			return elemVal;
			
			
		}else if(n instanceof LiteralReal){
			
			LiteralReal litReal = Application.getInstance().getProject().getElementsFactory().createLiteralRealInstance();
			litReal.setValue(((LiteralReal) n).getValue());
			return litReal;
			
		}else{
			
			javax.swing.JOptionPane.showMessageDialog(null, "Error during transformation from Expression to StringExpression!");
			return Application.getInstance().getProject().getElementsFactory().createStringExpressionInstance();
			
		}
		
	}
	
}
