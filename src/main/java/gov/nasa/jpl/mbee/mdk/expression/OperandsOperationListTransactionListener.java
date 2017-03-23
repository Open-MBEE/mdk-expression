package gov.nasa.jpl.mbee.mdk.expression;


import com.nomagic.uml2.ext.jmi.UML2MetamodelConstants;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Classifier;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.transaction.TransactionCommitListener;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Operation;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property;

import java.beans.PropertyChangeEvent;
import java.util.Collection;

/**
 * Transaction listener, which listens if attribute is created or deleted in transaction, and 
 * 1) if attribute is Operation and its owner is one of library blocks (AsciiMathLibrary or CustomFunction), then update the UI operation list from the library blocks elements.
 * 2) if attribute is Property and its owner is the constraint block currently browsing, then update the UI operand list from the constraint block's elements.
 * 
 * @author Miyako Wilson
 */
public class OperandsOperationListTransactionListener implements TransactionCommitListener
{
	MathEditorMain1Controller controller;
	
	public void setController(MathEditorMain1Controller _c){
		this.controller = _c;
	}

	@Override
	public Runnable transactionCommited(final Collection<PropertyChangeEvent> events)
	{
		return () -> {
			if ( controller != null){
				for (PropertyChangeEvent event : events)
				{
					Object ss = event.getSource();
					if (UML2MetamodelConstants.INSTANCE_CREATED.equals(event.getPropertyName()) ||
							UML2MetamodelConstants.INSTANCE_DELETED.equals(event.getPropertyName())	)
					{
						Object source = event.getSource();
						if (source instanceof Operation)
						{
							Element owner = ((Operation)source).getOwner();
							if (owner instanceof Classifier){
								SelectedOperationBlocks scb;
								if ((scb = controller.getModel().getSelectedOperationBlocks()).isBlock(owner)) {
									scb.reset(); //update model
									controller.updateOperationsListModel(); //updating the operation list from the md model 
								}
							}
							else if ( owner == null && controller.isOperationsListChange()) //deleting so woner is null
								controller.updateOperationsListModel();
						}
						else if ( source instanceof Property){
							Element owner = ((Property)source).getOwner();
							if (owner == controller.getModel().getSelectedConstraintBlock().getConstraintBlock()) {
								controller.updateOperandsListModel(); //updating the operand list from the md model
							} else if (owner == null && controller.isOperandsListChanged()) //deleting so owner is null
								controller.updateOperandsListModel(); //updating the operand list from the md model
						}
					}
						
				}
			}//end of controler != null
			
		};
	}
}