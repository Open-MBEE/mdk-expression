package gov.nasa.jpl.mbee.mdk.expression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractListModel;

import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property;

public class ListModelOperands extends AbstractListModel  {

	List<Property> model;
	
	public ListModelOperands(Collection<Property> _model)
	{
		//Collection is unmodifiableCollection.  So create List and use allAll.
		this.model = new ArrayList<Property>();
		this.model.addAll(_model);
	}
	public void add(Property _element) {
		if ( model.add(_element)) {
			fireContentsChanged(this, 0, getSize());
		}
	}
/*	public boolean removeElement(Property _element) {
		boolean removed = model.remove(_element);
		if (removed)
			fireContentsChanged(this, 0 , getSize());
		return removed;
	}*/
	/*public void addAll(Collection<Property> _newElements){
		model.addAll(new LinkedList(_newElements));
		fireContentsChanged(this, 0, getSize());
	}*/
	
	@Override
	public Object getElementAt(int _index) {
		return ((Property)model.toArray()[_index]).getName(); //return a constraint parameter contains the operand (string).
	}

	@Override
	public int getSize() {
		return model.size();
	}

}
