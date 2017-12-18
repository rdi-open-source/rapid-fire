/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem.resources;

import org.eclipse.rse.core.subsystems.AbstractResource;
import org.eclipse.rse.core.subsystems.ISubSystem;

import biz.rapidfire.core.exceptions.IllegalParameterException;
import biz.rapidfire.core.maintenance.conversion.shared.ConversionKey;
import biz.rapidfire.core.maintenance.wizard.shared.IWizardSupporter;
import biz.rapidfire.core.model.IRapidFireConversionResource;
import biz.rapidfire.core.model.IRapidFireFileResource;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.subsystem.IRapidFireSubSystem;
import biz.rapidfire.core.subsystem.resources.RapidFireConversionResourceDelegate;

public class RapidFireConversionResource extends AbstractResource implements IWizardSupporter, IRapidFireConversionResource,
    Comparable<IRapidFireConversionResource> {

    private IRapidFireJobResource parentJob;
    private IRapidFireFileResource parentFile;
    private RapidFireConversionResourceDelegate delegate;

    public static RapidFireConversionResource createEmptyInstance(IRapidFireFileResource file) {
        return new RapidFireConversionResource(file, ""); //$NON-NLS-1$
    }

    public RapidFireConversionResource(IRapidFireFileResource file, String fieldToConvert) {

        if (fieldToConvert == null) {
            throw new IllegalParameterException("fieldToConvert", null); //$NON-NLS-1$
        }

        this.parentJob = file.getParentJob();
        this.parentFile = file;
        this.delegate = new RapidFireConversionResourceDelegate(parentJob.getDataLibrary(), parentJob.getName(), file.getPosition(), fieldToConvert);
        super.setSubSystem((ISubSystem)file.getParentSubSystem());
    }

    public ConversionKey getKey() {
        return new ConversionKey(parentFile.getKey(), delegate.getFieldToConvert());
    }

    /*
     * IRapidFireResource methods
     */

    public String getDataLibrary() {
        return delegate.getDataLibrary();
    }

    public IRapidFireSubSystem getParentSubSystem() {
        return (IRapidFireSubSystem)super.getSubSystem();
    }

    public IRapidFireJobResource getParentJob() {
        return this.parentJob;
    }

    public IRapidFireFileResource getParent() {
        return parentFile;
    }

    /*
     * IRapidFireConversionResource methods
     */

    public String getJob() {
        return delegate.getJob();
    }

    public int getPosition() {
        return delegate.getPosition();
    }

    public String getFieldToConvert() {
        return delegate.getFieldToConvert();
    }

    public String getNewFieldName() {
        return delegate.getNewFieldName();
    }

    public void setNewFieldName(String fieldName) {
        delegate.setNewFieldName(fieldName);
    }

    public String[] getConversions() {
        return delegate.getConversions();
    }

    public void setConversions(String[] conversions) {
        delegate.setConversions(conversions);
    }

    public int compareTo(IRapidFireConversionResource resource) {
        return delegate.compareTo(resource);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

}
