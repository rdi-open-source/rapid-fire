/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.properties;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.model.IRapidFireJobResource;

public class JobNameProperties implements IPropertySource {

    private static final String BATCH_JOB = "JOB"; //$NON-NLS-1$
    private static final String BATCH_USER = "USER"; //$NON-NLS-1$
    private static final String BATCH_NUMBER = "NUMBER"; //$NON-NLS-1$

    private IRapidFireJobResource resource;

    public JobNameProperties(IRapidFireJobResource resource) {
        this.resource = resource;
    }

    public Object getEditableValue() {
        return null;
    }

    public IPropertyDescriptor[] getPropertyDescriptors() {

        PropertyDescriptor[] ourPDs = new PropertyDescriptor[3];
        ourPDs[0] = new PropertyDescriptor(BATCH_JOB, Messages.Batch_job_name);
        ourPDs[0].setDescription(Messages.Tooltip_Batch_job_name);
        ourPDs[1] = new PropertyDescriptor(BATCH_USER, Messages.Batch_job_user);
        ourPDs[1].setDescription(Messages.Tooltip_Batch_job_user);
        ourPDs[2] = new PropertyDescriptor(BATCH_NUMBER, Messages.Batch_job_number);
        ourPDs[2].setDescription(Messages.Tooltip_Batch_job_number);

        return ourPDs;
    }

    public Object getPropertyValue(Object propertyKey) {

        if (propertyKey.equals(BATCH_JOB)) {
            return resource.getBatchJob().getName();
        } else if (propertyKey.equals(BATCH_USER)) {
            return resource.getBatchJob().getUser();
        } else if (propertyKey.equals(BATCH_NUMBER)) {
            return resource.getBatchJob().getNumber();
        }

        return Messages.EMPTY;
    }

    public boolean isPropertySet(Object propertyKey) {
        return false;
    }

    public void resetPropertyValue(Object propertyKey) {
    }

    public void setPropertyValue(Object propertyKey, Object value) {
    }

    @Override
    public String toString() {
        return resource.getBatchJob().toString();
    }
}
