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

import biz.rapidfire.core.model.IRapidFireConversionResource;
import biz.rapidfire.rse.Messages;
import biz.rapidfire.rse.RapidFireRSEPlugin;
import biz.rapidfire.rse.subsystem.resources.RapidFireConversionResource;

public class RapidFireConversionResourceAdapter extends AbstractResourceAdapter<IRapidFireConversionResource> implements ISystemRemoteElementAdapter {

    private static final String DATA_LIBRARY = "DATA_LIBRARY"; //$NON-NLS-1$
    private static final String JOB = "JOB"; //$NON-NLS-1$
    private static final String POSITION = "POSITION"; //$NON-NLS-1$
    private static final String FIELD_TO_CONVERT = "FIELD_TO_CONVERT"; //$NON-NLS-1$
    private static final String NEW_FIELD_NAME = "NEW_FIELD_NAME"; //$NON-NLS-1$

    //    private static final String CONVERSIONS = "CONVERSIONS"; //$NON-NLS-1$

    public RapidFireConversionResourceAdapter() {
        super();
    }

    @Override
    public ImageDescriptor getImageDescriptor(Object object) {
        return RapidFireRSEPlugin.getDefault().getImageRegistry().getDescriptor(RapidFireRSEPlugin.IMAGE_CONVERSION);
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

        RapidFireConversionResource resource = getResource(element);

        return resource.getFieldToConvert();
    }

    /**
     * Returns the absolute name of the resource for building hash values.
     */
    @Override
    public String getAbsoluteName(Object element) {

        RapidFireConversionResource resource = getResource(element);

        String name = getAbsoluteNamePrefix() + resource.getDataLibrary()
            + "." + resource.getJob() + "." + resource.getPosition() + "." + resource.getFieldToConvert(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        return name;
    }

    @Override
    public String getAbsoluteNamePrefix() {
        return "RapidFireConversion."; //$NON-NLS-1$
    }

    /**
     * Returns the type of the resource. Used for qualifying the name of the
     * resource, when displayed on the status line.
     */
    @Override
    public String getType(Object element) {
        return Messages.Resource_Rapid_Fire_Conversion;
    }

    @Override
    public String getRemoteType(Object element) {
        return "conversion"; //$NON-NLS-1$
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

        PropertyDescriptor[] ourPDs = new PropertyDescriptor[5];
        ourPDs[0] = new PropertyDescriptor(DATA_LIBRARY, Messages.DataLibrary_name);
        ourPDs[0].setDescription(Messages.Tooltip_DataLibrary_name);
        ourPDs[1] = new PropertyDescriptor(JOB, Messages.Job_name);
        ourPDs[1].setDescription(Messages.Tooltip_Job_name);
        ourPDs[2] = new PropertyDescriptor(POSITION, Messages.Position);
        ourPDs[2].setDescription(Messages.Tooltip_Position);
        ourPDs[3] = new PropertyDescriptor(FIELD_TO_CONVERT, Messages.Area_name);
        ourPDs[3].setDescription(Messages.Tooltip_Area_name);
        ourPDs[4] = new PropertyDescriptor(NEW_FIELD_NAME, Messages.Area_library);
        ourPDs[4].setDescription(Messages.Tooltip_Area_library);

        return ourPDs;
    }

    @Override
    public Object internalGetPropertyValue(Object propKey) {

        final IRapidFireConversionResource resource = (IRapidFireConversionResource)propertySourceInput;

        if (propKey.equals(DATA_LIBRARY)) {
            return resource.getDataLibrary();
        } else if (propKey.equals(JOB)) {
            return resource.getJob();
        } else if (propKey.equals(POSITION)) {
            return resource.getPosition();
        } else if (propKey.equals(FIELD_TO_CONVERT)) {
            return resource.getFieldToConvert();
        } else if (propKey.equals(NEW_FIELD_NAME)) {
            return resource.getNewFieldName();
        }
        return null;
    }

    private RapidFireConversionResource getResource(Object element) {
        return (RapidFireConversionResource)element;
    }
}