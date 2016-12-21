package gov.nasa.jpl.mbee.mdk.expression;

import com.nomagic.actions.ActionsCategory;
import com.nomagic.actions.ActionsManager;
import com.nomagic.magicdraw.actions.DiagramContextAMConfigurator;
import com.nomagic.magicdraw.uml.symbols.DiagramPresentationElement;
import com.nomagic.magicdraw.uml.symbols.PresentationElement;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Constraint;

public class DiagramContextMenuConfigurator implements DiagramContextAMConfigurator {

	@Override
	public int getPriority()
	{
		return DiagramContextAMConfigurator.MEDIUM_PRIORITY;
	}
	
	@Override
	public void configure(ActionsManager manager, DiagramPresentationElement myDiagramType, PresentationElement[] selected,
			PresentationElement requestor) { 
		
		ActionsCategory category = new ActionsCategory(null, "Constraint Editor");
		
		//if constraint block is selected show a menu ("Constraint Editor")
		if(requestor != null && selected.length == 1 ) {
			if ( StereotypesHelper.hasStereotype(selected[0].getElement(), MDSysMLConstants.CONSTRAINTBLOCK)){	
				DiagramContextMenuConfiguratorController controller = new DiagramContextMenuConfiguratorController(selected[0].getElement());
				category.setNested(true);
				controller.setActions(category);
			}
			else if (selected[0].getElement() instanceof Constraint) {
				DiagramContextMenuConfiguratorController controller = new DiagramContextMenuConfiguratorController(selected[0].getElement().getOwner());
				category.setNested(true);
				controller.setAction(category, (Constraint) selected[0].getElement());
			}
		}
		
		manager.addCategory(category);
	}

}