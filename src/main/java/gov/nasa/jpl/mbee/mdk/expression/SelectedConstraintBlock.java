package gov.nasa.jpl.mbee.mdk.expression;

import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Constraint;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.NamedElement;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property;
import com.nomagic.uml2.ext.magicdraw.compositestructures.mdinternalstructures.StructuredClassifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class SelectedConstraintBlock {

    Element constraintBlock;
    Collection<Constraint> constraints;

    public SelectedConstraintBlock(Element _selectedBlock) {

        constraintBlock = _selectedBlock;
        constraints = ((StructuredClassifier) constraintBlock).get_constraintOfConstrainedElement();
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

    /*public void setConstraints(Collection<Constraint> constraints) {
        this.constraints = constraints;
    }*/
    public List<String> getOperandsInString() {
        List<String> l = new ArrayList<String>();
        getOperands().forEach(o -> l.add(o.getName()));
        return l;
    }

    public Collection<Property> getOperands() {
        return ((StructuredClassifier) constraintBlock).getPart();
    }

    public Property getOperand(String _lookingfor) { //Property.get() throws NoSuchElementException if not found
    /*	getOperands().stream().forEach(e->{
            System.out.println(((NamedElement)e).getName());
		});*/

        Optional<Property> p = getOperands().stream()
                //.filter(e-> ((Element)e).getHumanName().substring(DiagramContextMenuConfigurator.CONSTRAINT_PARAMETER_.length()).equals(_lookingfor))
                .filter(e -> ((NamedElement) e).getName().equals(_lookingfor))
                .findFirst();//.get();
        if (p.hashCode() != 0) {
            return p.get();
        }
        else {
            return null;
        }
    }
}