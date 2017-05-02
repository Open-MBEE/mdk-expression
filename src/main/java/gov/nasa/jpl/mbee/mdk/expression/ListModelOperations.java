package gov.nasa.jpl.mbee.mdk.expression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.AbstractListModel;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.NamedElement;

public class ListModelOperations extends AbstractListModel {
	private static final long serialVersionUID = 1L;
	List<Element> model;
	
	public ListModelOperations(Collection<Element> _model)
	{
		//Collection is unmodifiableCollection.  So create List and use allAll.
		this.model = new ArrayList<Element>();
		this.model.addAll(_model);
	}
	public boolean removeElement(Element _element) {
		boolean removed = model.remove(_element);
		if (removed)
			fireContentsChanged(this, 0 , getSize());
		return removed;
	}
	public void addAll(Collection<Element> _newElements){
		model.addAll(_newElements);
		fireContentsChanged(this, 0, getSize());
	}
	
	@Override
	public Object getElementAt(int _index) {
		return ((NamedElement) model.toArray()[_index]).getName();
	}

	@Override
	public int getSize() {
		return model.size();
	}

	public void reset(Collection<Element> _model){
		model.clear();
		addAll(_model);
	}
}
