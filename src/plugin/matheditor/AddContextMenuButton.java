package plugin.matheditor;

import com.nomagic.actions.ActionsCategory;
import com.nomagic.actions.NMAction;
import com.nomagic.magicdraw.actions.ActionsConfiguratorsManager;
import com.nomagic.magicdraw.plugins.Plugin;
import com.nomagic.magicdraw.uml.DiagramType;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;



public class AddContextMenuButton extends Plugin{

	public static Element asciiMathLibraryBlock = null, customFuncBlock = null;
	
	public void init(){	
		ActionsConfiguratorsManager manager = ActionsConfiguratorsManager.getInstance();
		manager.addDiagramContextConfigurator(DiagramType.UML_ANY_DIAGRAM, new DiagramContextMenuConfigurator()); //adding submenu
	}
	public boolean close(){return true;}	
	public boolean isSupported(){return true;}
	
}