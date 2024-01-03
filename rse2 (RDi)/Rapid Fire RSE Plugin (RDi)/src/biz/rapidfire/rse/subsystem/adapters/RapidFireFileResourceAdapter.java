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
import org.eclipse.rse.core.subsystems.AbstractResource;
import org.eclipse.rse.ui.view.ISystemRemoteElementAdapter;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import biz.rapidfire.core.maintenance.file.shared.FileType;
import biz.rapidfire.core.model.IRapidFireFileResource;
import biz.rapidfire.rse.Messages;
import biz.rapidfire.rse.RapidFireRSEPlugin;
import biz.rapidfire.rse.subsystem.resources.AreasNode;
import biz.rapidfire.rse.subsystem.resources.CommandsNode;
import biz.rapidfire.rse.subsystem.resources.ConversionsNode;
import biz.rapidfire.rse.subsystem.resources.RapidFireFileResource;

public class RapidFireFileResourceAdapter extends AbstractResourceAdapter<IRapidFireFileResource> implements ISystemRemoteElementAdapter {

    private static final String DATA_LIBRARY = "DATA_LIBRARY"; //$NON-NLS-1$
    private static final String JOB = "JOB"; //$NON-NLS-1$
    private static final String POSITION = "POSITION"; //$NON-NLS-1$
    private static final String FILE = "FILE"; //$NON-NLS-1$
    private static final String FILE_TYPE = "FILE_TYPE"; //$NON-NLS-1$
    private static final String COPY_PROGRAM_NAME = "COPY_PROGRAM_NAME"; //$NON-NLS-1$
    private static final String COPY_PROGRAM_LIBRARY = "COPY_PROGRAM_LIBRARY"; //$NON-NLS-1$
    private static final String CONVERSION_PROGRAM_NAME = "CONVERSION_PROGRAM_NAME"; //$NON-NLS-1$
    private static final String CONVERSION_PROGRAM_LIBRARY = "CONVERSION_PROGRAM_LIBRARY"; //$NON-NLS-1$

    public RapidFireFileResourceAdapter() {
        super();
    }

    @Override
    public ImageDescriptor getImageDescriptor(Object element) {

        RapidFireFileResource resource = getResource(element);

        if (resource.getFileType() == FileType.LOGICAL) {
            return RapidFireRSEPlugin.getDefault().getImageRegistry().getDescriptor(RapidFireRSEPlugin.IMAGE_LOGICAL_FILE);
        } else {
            return RapidFireRSEPlugin.getDefault().getImageRegistry().getDescriptor(RapidFireRSEPlugin.IMAGE_FILE);
        }
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

        RapidFireFileResource resource = getResource(element);

        return resource.getName();
    }

    /**
     * Returns the absolute name of the resource for building hash values.
     */
    @Override
    public String getAbsoluteName(Object element) {

        RapidFireFileResource resource = getResource(element);

        String name = getAbsoluteNamePrefix() + resource.getDataLibrary() + "." + resource.getJob() + "." + resource.getPosition(); //$NON-NLS-1$ //$NON-NLS-2$

        return name;
    }

    @Override
    public String getAbsoluteNamePrefix() {
        return "RapidFireFile."; //$NON-NLS-1$
    }

    /**
     * Returns the type of the resource. Used for qualifying the name of the
     * resource, when displayed on the status line.
     */
    @Override
    public String getType(Object element) {
        return Messages.Resource_Rapid_Fire_File;
    }

    @Override
    public String getRemoteType(Object element) {
        return "file"; //$NON-NLS-1$
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

        IRapidFireFileResource file = (IRapidFireFileResource)element;

        AbstractResource[] children = new AbstractResource[3];
        children[0] = new AreasNode(file);
        children[1] = new ConversionsNode(file);
        children[2] = new CommandsNode(file);

        return children;
    }

    @Override
    protected IPropertyDescriptor[] internalGetPropertyDescriptors() {

        PropertyDescriptor[] ourPDs = new PropertyDescriptor[9];
        ourPDs[0] = new PropertyDescriptor(DATA_LIBRARY, Messages.DataLibrary_name);
        ourPDs[0].setDescription(Messages.Tooltip_DataLibrary_name);
        ourPDs[1] = new PropertyDescriptor(JOB, Messages.Job_name);
        ourPDs[1].setDescription(Messages.Tooltip_Job_name);
        ourPDs[2] = new PropertyDescriptor(POSITION, Messages.Position);
        ourPDs[2].setDescription(Messages.Tooltip_Position);
        ourPDs[3] = new PropertyDescriptor(FILE, Messages.File_name);
        ourPDs[3].setDescription(Messages.Tooltip_File_name);
        ourPDs[4] = new PropertyDescriptor(FILE_TYPE, Messages.FileType);
        ourPDs[4].setDescription(Messages.Tooltip_FileType);
        ourPDs[5] = new PropertyDescriptor(COPY_PROGRAM_NAME, Messages.Copy_program_name);
        ourPDs[5].setDescription(Messages.Tooltip_Copy_program_name);
        ourPDs[6] = new PropertyDescriptor(COPY_PROGRAM_LIBRARY, Messages.Copy_program_library);
        ourPDs[6].setDescription(Messages.Tooltip_Copy_program_library);
        ourPDs[7] = new PropertyDescriptor(CONVERSION_PROGRAM_NAME, Messages.Conversion_program_name);
        ourPDs[7].setDescription(Messages.Tooltip_Conversion_program_name);
        ourPDs[8] = new PropertyDescriptor(CONVERSION_PROGRAM_LIBRARY, Messages.Conversion_program_library);
        ourPDs[8].setDescription(Messages.Tooltip_Conversion_program_library);

        return ourPDs;
    }

    @Override
    public Object internalGetPropertyValue(Object propKey) {

        final IRapidFireFileResource resource = (IRapidFireFileResource)propertySourceInput;

        if (propKey.equals(DATA_LIBRARY)) {
            return resource.getDataLibrary();
        } else if (propKey.equals(JOB)) {
            return resource.getJob();
        } else if (propKey.equals(POSITION)) {
            return resource.getPosition();
        } else if (propKey.equals(FILE)) {
            return resource.getName();
        } else if (propKey.equals(FILE_TYPE)) {
            return resource.getFileType().label();
        } else if (propKey.equals(COPY_PROGRAM_NAME)) {
            return resource.getCopyProgramName();
        } else if (propKey.equals(COPY_PROGRAM_LIBRARY)) {
            return resource.getCopyProgramLibrary();
        } else if (propKey.equals(CONVERSION_PROGRAM_NAME)) {
            return resource.getConversionProgramName();
        } else if (propKey.equals(CONVERSION_PROGRAM_LIBRARY)) {
            return resource.getConversionProgramLibrary();
        }
        return null;
    }

    private RapidFireFileResource getResource(Object element) {
        return (RapidFireFileResource)element;
    }
}