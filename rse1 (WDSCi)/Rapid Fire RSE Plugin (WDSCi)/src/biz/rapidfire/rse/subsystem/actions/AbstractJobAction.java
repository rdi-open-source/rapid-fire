package biz.rapidfire.rse.subsystem.actions;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.ISources;

import com.ibm.etools.iseries.core.ui.actions.isv.ISeriesAbstractQSYSPopupMenuExtensionAction;

public abstract class AbstractJobAction extends ISeriesAbstractQSYSPopupMenuExtensionAction {

    @Override
    public void run() {

        try {

            Object[] selection = getSelectedRemoteObjects();

            Map<String, Object> properties = new HashMap<String, Object>();
            properties.put(ISources.ACTIVE_CURRENT_SELECTION_NAME, new StructuredSelection(selection));
            ExecutionEvent event = new ExecutionEvent(null, properties, null, null);
            
            execute(event);
            
        } catch (ExecutionException e) {
            e.printStackTrace(); // TODO: fix it
        }
    }

    public abstract void execute(ExecutionEvent event) throws ExecutionException ;

}