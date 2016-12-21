package gov.nasa.jpl.mbee.mdk.expression;

import com.nomagic.magicdraw.ui.actions.DefaultDiagramAction;
import javax.annotation.CheckForNull;
import java.awt.event.ActionEvent;


class SimpleContextAction extends DefaultDiagramAction { //MDAction
	
	DiagramContextMenuConfiguratorController controller;
	
    public SimpleContextAction(DiagramContextMenuConfiguratorController _controller, @CheckForNull String id, String name)
    {
        super(id, name, null, null);
        controller = _controller;
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
	public void actionPerformed(ActionEvent e){
    	
    	//*********************************SET LIBRARY IF NECESSARY********************************
    	if(AddContextMenuButton.asciiMathLibraryBlock == null || AddContextMenuButton.customFuncBlock == null){
			//javax.swing.JOptionPane.showMessageDialog(null, "Please select Libraries first!");		
			LibrarySelector ls = new LibrarySelector();
			if(!ls.openDialog()){return; }
		}
 		
    	controller.handleAction(e);
    }
}