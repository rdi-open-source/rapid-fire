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
import org.eclipse.rse.ui.view.ISystemRemoteElementAdapter;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import biz.rapidfire.core.model.IRapidFireCommandResource;
import biz.rapidfire.rse.Messages;
import biz.rapidfire.rse.RapidFireRSEPlugin;
import biz.rapidfire.rse.subsystem.resources.RapidFireCommandResource;

public class RapidFireCommandResourceAdapter extends AbstractResourceAdapter<IRapidFireCommandResource> implements ISystemRemoteElementAdapter {

    private static final String DATA_LIBRARY = "DATA_LIBRARY"; //$NON-NLS-1$
    private static final String JOB = "JOB"; //$NON-NLS-1$
    private static final String POSITION = "POSITION"; //$NON-NLS-1$
    private static final String COMMAND_TYPE = "COMMAND_TYPE"; //$NON-NLS-1$
    private static final String SEQUENCE = "SEQUENCE"; //$NON-NLS-1$
    private static final String COMMAND = "COMMAND"; //$NON-NLS-1$

    public RapidFireCommandResourceAdapter() {
        super();
    }

    @Override
    public ImageDescriptor getImageDescriptor(Object object) {
        return RapidFireRSEPlugin.getDefault().getImageRegistry().getDescriptor(RapidFireRSEPlugin.IMAGE_COMMAND);
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

        RapidFireCommandResource resource = getResource(element);

        StringBuilder text = new StringBuilder();

        text.append(resource.getCommandType());
        text.append(": "); //$NON-NLS-1$
        text.append(getCommand(resource.getCommand()));

        return text.toString();
    }

    private String getCommand(String command) {

        int x = command.indexOf(" "); //$NON-NLS-1$
        if (x >= 0) {
            return command.substring(0, x);
        }

        return command;
    }

    /**
     * Returns the absolute name of the resource for building hash values.
     */
    @Override
    public String getAbsoluteName(Object element) {

        RapidFireCommandResource resource = getResource(element);

        String name = getAbsoluteNamePrefix() + resource.getDataLibrary()
            + "." + resource.getJob() + "." + resource.getPosition() + "." + resource.getCommandType() + "." + resource.getSequence(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

        return name;
    }

    @Override
    public String getAbsoluteNamePrefix() {
        return "RapidFireCommand."; //$NON-NLS-1$
    }

    /**
     * Returns the type of the resource. Used for qualifying the name of the
     * resource, when displayed on the status line.
     */
    @Override
    public String getType(Object element) {
        return Messages.Resource_Rapid_Fire_Command;
    }

    @Override
    public String getRemoteType(Object element) {
        return "command"; //$NON-NLS-1$
    }

    @Override
    public Object getParent(Object element) {
        return null;
    }

    @Override
    public boolean hasChildren(IAdaptable element) {
        return false;
    }

    @Override
    public Object[] getChildren(IAdaptable element, IProgressMonitor progressMonitor) {
        return null;
    }

    @Override
    protected IPropertyDescriptor[] internalGetPropertyDescriptors() {

        PropertyDescriptor[] ourPDs = new PropertyDescriptor[6];
        ourPDs[0] = new PropertyDescriptor(DATA_LIBRARY, Messages.DataLibrary_name);
        ourPDs[0].setDescription(Messages.Tooltip_DataLibrary_name);
        ourPDs[1] = new PropertyDescriptor(JOB, Messages.Job_name);
        ourPDs[1].setDescription(Messages.Tooltip_Job_name);
        ourPDs[2] = new PropertyDescriptor(POSITION, Messages.Position);
        ourPDs[2].setDescription(Messages.Tooltip_Position);
        ourPDs[3] = new PropertyDescriptor(COMMAND_TYPE, Messages.Command_type);
        ourPDs[3].setDescription(Messages.Tooltip_Command_type);
        ourPDs[4] = new PropertyDescriptor(SEQUENCE, Messages.Command_sequence);
        ourPDs[4].setDescription(Messages.Tooltip_Command_sequence);
        ourPDs[5] = new PropertyDescriptor(COMMAND, Messages.Command_command);
        ourPDs[5].setDescription(Messages.Tooltip_Command_command);

        return ourPDs;
    }

    @Override
    public Object internalGetPropertyValue(Object propKey) {

        final IRapidFireCommandResource resource = (IRapidFireCommandResource)propertySourceInput;

        if (propKey.equals(DATA_LIBRARY)) {
            return resource.getDataLibrary();
        } else if (propKey.equals(JOB)) {
            return resource.getJob();
        } else if (propKey.equals(POSITION)) {
            return resource.getPosition();
        } else if (propKey.equals(COMMAND_TYPE)) {
            return resource.getCommandType();
        } else if (propKey.equals(SEQUENCE)) {
            return Integer.toString(resource.getSequence());
        } else if (propKey.equals(COMMAND)) {
            return resource.getCommand();
        }
        return null;
    }

    private RapidFireCommandResource getResource(Object element) {
        return (RapidFireCommandResource)element;
    }
}