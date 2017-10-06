package biz.rapidfire.rse.subsystem.actions;

import java.util.Vector;

import biz.rapidfire.rse.model.RapidFireJobResource;

import com.ibm.etools.systems.core.SystemPlugin;
import com.ibm.etools.systems.model.ISystemRemoteChangeEvents;
import com.ibm.etools.systems.model.SystemRegistry;

public class ResetJobAfterAbortionAction extends AbstractJobAction {

    public String execute(RapidFireJobResource jobResource) {

        // TODO: Do something
        String message = null;
        System.out.println("Resetting Rapid Fire job after abortion: " + jobResource.getName());
        
        if (message == null) {
            SystemRegistry sr = SystemPlugin.getDefault().getSystemRegistry();
            Vector<RapidFireJobResource> jobVector = new Vector<RapidFireJobResource>();
            jobVector.addElement(jobResource);
            sr.fireRemoteResourceChangeEvent(ISystemRemoteChangeEvents.SYSTEM_REMOTE_RESOURCE_CREATED, jobVector, null, null, null, null);
        }

        return message;
    }

}