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

import biz.rapidfire.core.model.IRapidFireAreaResource;
import biz.rapidfire.rse.Messages;
import biz.rapidfire.rse.RapidFireRSEPlugin;
import biz.rapidfire.rse.subsystem.resources.RapidFireAreaResource;

public class RapidFireAreaResourceAdapter extends AbstractResourceAdapter<IRapidFireAreaResource> implements ISystemRemoteElementAdapter {

    private static final String DATA_LIBRARY = "DATA_LIBRARY"; //$NON-NLS-1$
    private static final String JOB = "JOB"; //$NON-NLS-1$
    private static final String POSITION = "POSITION"; //$NON-NLS-1$
    private static final String AREA = "AREA"; //$NON-NLS-1$
    private static final String LIBRARY = "LIBRARY"; //$NON-NLS-1$
    private static final String LIBRARY_LIST = "LIBRARY_LIST"; //$NON-NLS-1$
    private static final String LIBRARY_CCSID = "LIBRARY_CCSID"; //$NON-NLS-1$
    private static final String COMMAND_EXTENSION = "COMMAND_EXTENSION"; //$NON-NLS-1$

    public RapidFireAreaResourceAdapter() {
        super();
    }

    @Override
    public ImageDescriptor getImageDescriptor(Object object) {
        return RapidFireRSEPlugin.getDefault().getImageRegistry().getDescriptor(RapidFireRSEPlugin.IMAGE_AREA);
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

        RapidFireAreaResource resource = getResource(element);

        return resource.getName();
    }

    /**
     * Returns the absolute name of the resource for building hash values.
     */
    @Override
    public String getAbsoluteName(Object element) {

        RapidFireAreaResource resource = getResource(element);

        String name = getAbsoluteNamePrefix() + resource.getDataLibrary()
            + "." + resource.getJob() + "." + resource.getPosition() + "." + resource.getName(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        return name;
    }

    @Override
    public String getAbsoluteNamePrefix() {
        return "RapidFireArea."; //$NON-NLS-1$
    }

    /**
     * Returns the type of the resource. Used for qualifying the name of the
     * resource, when displayed on the status line.
     */
    @Override
    public String getType(Object element) {
        return Messages.Resource_Rapid_Fire_Area;
    }

    @Override
    public String getRemoteType(Object element) {
        return "area"; //$NON-NLS-1$
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

        PropertyDescriptor[] ourPDs = new PropertyDescriptor[8];
        ourPDs[0] = new PropertyDescriptor(DATA_LIBRARY, Messages.DataLibrary_name);
        ourPDs[0].setDescription(Messages.Tooltip_DataLibrary_name);
        ourPDs[1] = new PropertyDescriptor(JOB, Messages.Job_name);
        ourPDs[1].setDescription(Messages.Tooltip_Job_name);
        ourPDs[2] = new PropertyDescriptor(POSITION, Messages.Position);
        ourPDs[2].setDescription(Messages.Tooltip_Position);
        ourPDs[3] = new PropertyDescriptor(AREA, Messages.Area_name);
        ourPDs[3].setDescription(Messages.Tooltip_Area_name);
        ourPDs[4] = new PropertyDescriptor(LIBRARY, Messages.Area_library);
        ourPDs[4].setDescription(Messages.Tooltip_Area_library);
        ourPDs[5] = new PropertyDescriptor(LIBRARY_LIST, Messages.Area_library_list);
        ourPDs[5].setDescription(Messages.Tooltip_Area_library_list);
        ourPDs[6] = new PropertyDescriptor(LIBRARY_CCSID, Messages.Area_library_CCSID);
        ourPDs[6].setDescription(Messages.Tooltip_Area_library_CCSID);
        ourPDs[7] = new PropertyDescriptor(COMMAND_EXTENSION, Messages.Area_command_extension);
        ourPDs[7].setDescription(Messages.Tooltip_Area_command_extension);

        return ourPDs;
    }

    @Override
    public Object internalGetPropertyValue(Object propKey) {

        final IRapidFireAreaResource resource = (IRapidFireAreaResource)propertySourceInput;

        if (propKey.equals(DATA_LIBRARY)) {
            return resource.getDataLibrary();
        } else if (propKey.equals(JOB)) {
            return resource.getJob();
        } else if (propKey.equals(POSITION)) {
            return resource.getPosition();
        } else if (propKey.equals(AREA)) {
            return resource.getName();
        } else if (propKey.equals(LIBRARY)) {
            return resource.getLibrary();
        } else if (propKey.equals(LIBRARY_LIST)) {
            return resource.getLibraryList();
        } else if (propKey.equals(LIBRARY_CCSID)) {
            return resource.getLibraryCcsid();
        } else if (propKey.equals(COMMAND_EXTENSION)) {
            return resource.getCommandExtension();
        }
        return null;
    }

    private RapidFireAreaResource getResource(Object element) {
        return (RapidFireAreaResource)element;
    }
}