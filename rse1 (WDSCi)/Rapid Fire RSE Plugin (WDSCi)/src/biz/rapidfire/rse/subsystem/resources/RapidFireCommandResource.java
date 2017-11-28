/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem.resources;

import biz.rapidfire.core.exceptions.IllegalParameterException;
import biz.rapidfire.core.model.IRapidFireCommandResource;
import biz.rapidfire.core.model.IRapidFireFileResource;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.maintenance.command.shared.CommandType;
import biz.rapidfire.core.subsystem.IRapidFireSubSystem;
import biz.rapidfire.core.subsystem.resources.RapidFireCommandResourceDelegate;

import com.ibm.etools.systems.subsystems.SubSystem;
import com.ibm.etools.systems.subsystems.impl.AbstractResource;

public class RapidFireCommandResource extends AbstractResource implements IRapidFireCommandResource, Comparable<IRapidFireCommandResource> {

    private IRapidFireJobResource parentJob;
    private RapidFireCommandResourceDelegate delegate;

    public static RapidFireCommandResource createEmptyInstance(IRapidFireFileResource file) {
        return new RapidFireCommandResource(file, CommandType.COMPILE, 0); //$NON-NLS-1$
    }

    public RapidFireCommandResource(IRapidFireFileResource file, CommandType commandType, int sequence) {

        if (commandType == null) {
            throw new IllegalParameterException("fieldToConvert", null); //$NON-NLS-1$
        }

        this.parentJob = file.getParentJob();
        this.delegate = new RapidFireCommandResourceDelegate(parentJob.getDataLibrary(), parentJob.getName(), file.getPosition(), commandType,
            sequence);
        super.setSubSystem((SubSystem)file.getParentSubSystem());
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

    /*
     * IRapidFireCommandResource methods
     */

    public String getJob() {
        return delegate.getJob();
    }

    public int getPosition() {
        return delegate.getPosition();
    }

    public CommandType getCommandType() {
        return delegate.getCommandType();
    }

    public int getSequence() {
        return delegate.getSequence();
    }

    public String getCommand() {
        return delegate.getCommand();
    }

    public void setCommand(String command) {
        delegate.setCommand(command);
    }

    public int compareTo(IRapidFireCommandResource resource) {
        return delegate.compareTo(resource);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

}
