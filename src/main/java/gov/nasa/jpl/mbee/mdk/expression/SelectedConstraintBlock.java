package gov.nasa.jpl.mbee.mdk.expression;

import java.util.Collection;
import java.util.Optional;

import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Constraint;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property;
import com.nomagic.uml2.ext.magicdraw.compositestructures.mdinternalstructures.StructuredClassifier;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.NamedElement;

public class SelectedConstraintBlock {
	
	Element constraintBlock;
	Collection<Constraint> constraints;
	
	public SelectedConstraintBlock(Element _selectedBlock){
		
		constraintBlock = _selectedBlock;
		constraints = ((StructuredClassifier)constraintBlock).get_constraintOfConstrainedElement();
	}

	public Element getConstraintBlock() {
		return constraintBlock;
	}

	public void setConstraintBlock(Element constraintBlock) {
		this.constraintBlock = constraintBlock;
	}

	public Collection<Constraint> getConstraints() {
		return constraints;
	}

	public void setConstraints(Collection<Constraint> constraints) {
		this.constraints = constraints;
	}

	public Collection<Property> getOperands() {
		return ((StructuredClassifier)constraintBlock).getPart();
	}
	public Property getOperand(String _lookingfor){ //Property.get() throws NoSuchElementException if not found
		Optional<Property> p =  getOperands().stream()
				//.filter(e-> ((Element)e).getHumanName().substring(DiagramContextMenuConfigurator.CONSTRAINT_PARAMETER_.length()).equals(_lookingfor))
				.filter(e ->  ((NamedElement)e).getName().equals(_lookingfor))
				.findFirst();//.get();
		if ( p.hashCode() != 0)
			return p.get();
		else
			return null;
	}
}