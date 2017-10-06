package biz.rapidfire.rse.subsystem.actions;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.ISources;

import biz.rapidfire.core.dialogs.MessageDialogAsync;
import biz.rapidfire.core.handlers.ChangeJobHandler;
import biz.rapidfire.rse.model.RapidFireJobResource;

import com.ibm.etools.systems.core.SystemPlugin;
import com.ibm.etools.systems.model.ISystemRemoteChangeEvents;
import com.ibm.etools.systems.model.SystemRegistry;

public class ChangeJobAction extends AbstractJobAction {

    private ChangeJobHandler handler = new ChangeJobHandler();

    public String execute(RapidFireJobResource jobResource) {

        String message = null;

        try {

            Map<String, Object> properties = new HashMap<String, Object>();
            properties.put(ISources.ACTIVE_CURRENT_SELECTION_NAME, new StructuredSelection(jobResource));
            ExecutionEvent event = new ExecutionEvent(null, properties, null, null);

            System.out.println("Calling handler: Changing Rapid Fire job: " + jobResource.getName());
            handler.execute(event);

            if (message == null) {
                SystemRegistry sr = SystemPlugin.getDefault().getSystemRegistry();
                Vector<RapidFireJobResource> jobVector = new Vector<RapidFireJobResource>();
                jobVector.addElement(jobResource);
                sr.fireRemoteResourceChangeEvent(ISystemRemoteChangeEvents.SYSTEM_REMOTE_RESOURCE_CREATED, jobVector, null, null, null, null);
            }

        } catch (Exception e) {
            MessageDialogAsync.displayError(getShell(), e.getLocalizedMessage());
        }

        return message;
    }

}