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
 * Transaction listener, which listens if attribute is created in transaction, and 
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
					if (UML2MetamodelConstants.INSTANCE_CREATED.equals(event.getPropertyName()))
					{
						Object source = event.getSource();
						if (source instanceof Operation)
						{
							Operation operation = (Operation) source;
							Element owner = operation.getOwner();
							
							if (owner instanceof Classifier){
								SelectedOperationBlocks scb;
								if ((scb = controller.getModel().getSelectedOperationBlocks()).isBlock(owner)) {
									scb.reset(); //update model
									controller.updateOperationsListModel(); //updating the operation list from the md model 
								}
							}
						}
						else if ( source instanceof Property){
							Element owner = ((Property)source).getOwner();
							if (owner == controller.getModel().getSelectedConstraintBlock().getConstraintBlock()) {
								controller.updateOperandsListModel(); //updating the operand list from the md model
							}
						}
					}
				}
			}
		};
	}
}