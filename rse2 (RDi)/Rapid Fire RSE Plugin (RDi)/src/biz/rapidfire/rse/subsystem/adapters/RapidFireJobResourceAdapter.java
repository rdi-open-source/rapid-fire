/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem.adapters;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.rse.core.subsystems.AbstractResource;
import org.eclipse.rse.ui.SystemMenuManager;
import org.eclipse.rse.ui.view.ISystemRemoteElementAdapter;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.properties.JobNameProperties;
import biz.rapidfire.rse.Messages;
import biz.rapidfire.rse.RapidFireRSEPlugin;
import biz.rapidfire.rse.model.RapidFireJobResource;
import biz.rapidfire.rse.subsystem.FilesNode;
import biz.rapidfire.rse.subsystem.LibrariesNode;

public class RapidFireJobResourceAdapter extends AbstractResourceAdapter implements ISystemRemoteElementAdapter {

    private static final String DATA_LIBRARY = "DATA_LIBRARY"; //$NON-NLS-1$
    private static final String JOB = "JOB"; //$NON-NLS-1$
    private static final String DESCRIPTION = "DESCRIPTION"; //$NON-NLS-1$
    private static final String CREATE_ENVIRONMENT = "CREATE_ENVIRONMENT"; //$NON-NLS-1$
    private static final String JOB_QUEUE_LIBRARY = "JOB_QUEUE_LIBRARY"; //$NON-NLS-1$
    private static final String JOB_QUEUE = "JOB_QUEUE"; //$NON-NLS-1$
    private static final String STATUS = "STATUS"; //$NON-NLS-1$
    private static final String PHASE = "PHASE"; //$NON-NLS-1$
    private static final String ERROR = "ERROR"; //$NON-NLS-1$
    private static final String ERROR_TEXT = "ERROR_TEXT"; //$NON-NLS-1$
    private static final String BATCH_JOB = "BATCH_JOB"; //$NON-NLS-1$

    // private static final String STOP_APPLY_CHANGES = "STOP_APPLY_CHANGES";
    // private static final String CMONE_FORM = "CMONE_FORM";

    public RapidFireJobResourceAdapter() {
        super();
    }

    @Override
    public void addActions(SystemMenuManager menu, IStructuredSelection selection, Shell shell, String menuGroup) {
    }

    @Override
    public ImageDescriptor getImageDescriptor(Object object) {
        return RapidFireRSEPlugin.getDefault().getImageRegistry().getDescriptor(RapidFireRSEPlugin.IMAGE_RAPID_FIRE_JOB);
    }

    @Override
    public boolean handleDoubleClick(Object object) {
        return false;
    }

    /**
     * Returns the name of the resource, e.g. for showing the name in the status
     * line.
     */
    @Override
    public String getText(Object element) {

        RapidFireJobResource resource = (RapidFireJobResource)element;

        return resource.getName() + " - " + resource.getStatus(); //$NON-NLS-1$ 
    }

    /**
     * Returns the absolute name of the resource for building hash values.
     */
    @Override
    public String getAbsoluteName(Object element) {

        RapidFireJobResource resource = (RapidFireJobResource)element;

        String name = "RapidFireJob." + resource.getDataLibrary() + "." + resource.getName(); //$NON-NLS-1$ //$NON-NLS-2$

        return name;
    }

    /**
     * Returns the type of the resource. Used for qualifying the name of the
     * resource, when displayed on the status line.
     */
    @Override
    public String getType(Object element) {
        return Messages.Resource_Rapid_Fire_Job;
    }

    @Override
    public String getRemoteType(Object element) {
        return "job";
    }

    @Override
    public Object getParent(Object element) {
        return null;
    }

    @Override
    public boolean hasChildren(IAdaptable element) {
        return true;
    }

    @Override
    public Object[] getChildren(IAdaptable element, IProgressMonitor progressMonitor) {

        AbstractResource[] children = new AbstractResource[2];
        children[0] = new FilesNode((RapidFireJobResource)element);
        children[1] = new LibrariesNode((RapidFireJobResource)element);

        return children;
    }

    @Override
    protected IPropertyDescriptor[] internalGetPropertyDescriptors() {

        PropertyDescriptor[] ourPDs = new PropertyDescriptor[11];
        ourPDs[0] = new PropertyDescriptor(DATA_LIBRARY, Messages.DataLibrary_name);
        ourPDs[0].setDescription(Messages.Tooltip_DataLibrary_name);
        ourPDs[1] = new PropertyDescriptor(JOB, Messages.Job_name);
        ourPDs[1].setDescription(Messages.Tooltip_Job_name);
        ourPDs[2] = new PropertyDescriptor(DESCRIPTION, Messages.Description);
        ourPDs[2].setDescription(Messages.Tooltip_Description);
        ourPDs[3] = new PropertyDescriptor(BATCH_JOB, Messages.Batch_job);
        ourPDs[3].setDescription(Messages.Tooltip_Batch_job);
        ourPDs[4] = new PropertyDescriptor(CREATE_ENVIRONMENT, Messages.Create_environment);
        ourPDs[4].setDescription(Messages.Tooltip_Create_environment);
        ourPDs[5] = new PropertyDescriptor(JOB_QUEUE, Messages.Job_queue);
        ourPDs[5].setDescription(Messages.Tooltip_Job_queue);
        ourPDs[6] = new PropertyDescriptor(JOB_QUEUE_LIBRARY, Messages.Job_queue_library);
        ourPDs[6].setDescription(Messages.Tooltip_Job_queue_library);
        ourPDs[7] = new PropertyDescriptor(STATUS, Messages.Status);
        ourPDs[7].setDescription(Messages.Tooltip_Status);
        ourPDs[8] = new PropertyDescriptor(PHASE, Messages.Phase);
        ourPDs[8].setDescription(Messages.Tooltip_Phase);
        ourPDs[9] = new PropertyDescriptor(ERROR, Messages.Error);
        ourPDs[9].setDescription(Messages.Tooltip_Error);
        ourPDs[10] = new PropertyDescriptor(ERROR_TEXT, Messages.Error_text);
        ourPDs[10].setDescription(Messages.Tooltip_Error_text);

        return ourPDs;
    }

    @Override
    public Object internalGetPropertyValue(Object propKey) {

        final IRapidFireJobResource resource = (IRapidFireJobResource)propertySourceInput;

        if (propKey.equals(DATA_LIBRARY)) {
            return resource.getDataLibrary();
        } else if (propKey.equals(JOB)) {
            return resource.getName();
        } else if (propKey.equals(DESCRIPTION)) {
            return resource.getDescription();
        } else if (propKey.equals(BATCH_JOB)) {
            return new JobNameProperties(resource);
        } else if (propKey.equals(CREATE_ENVIRONMENT)) {
            return resource.isDoCreateEnvironment();
        } else if (propKey.equals(JOB_QUEUE)) {
            return resource.getJobQueueName();
        } else if (propKey.equals(JOB_QUEUE_LIBRARY)) {
            return resource.getJobQueueLibrary();
        } else if (propKey.equals(STATUS)) {
            return resource.getStatus().label;
        } else if (propKey.equals(PHASE)) {
            return resource.getPhase().label;
        } else if (propKey.equals(ERROR)) {
            return resource.isError();
        } else if (propKey.equals(ERROR_TEXT)) {
            return resource.getErrorText();
        }

        return null;
    }
}