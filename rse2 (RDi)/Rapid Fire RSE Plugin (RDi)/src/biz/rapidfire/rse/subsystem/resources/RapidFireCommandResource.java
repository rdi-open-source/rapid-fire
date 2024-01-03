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
import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.exceptions.IllegalParameterException;
import biz.rapidfire.core.maintenance.command.shared.CommandKey;
import biz.rapidfire.core.maintenance.command.shared.CommandType;
import biz.rapidfire.core.model.IRapidFireCommandResource;
import biz.rapidfire.core.model.IRapidFireFileResource;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.IRapidFireNodeResource;
import biz.rapidfire.core.subsystem.IRapidFireSubSystem;
import biz.rapidfire.core.subsystem.resources.RapidFireCommandResourceDelegate;

public class RapidFireCommandResource extends AbstractResource implements IRapidFireCommandResource, Comparable<IRapidFireCommandResource> {

    private IRapidFireNodeResource parentNode;
    private IRapidFireJobResource parentJob;
    private IRapidFireFileResource parentFile;
    private RapidFireCommandResourceDelegate delegate;

    public static RapidFireCommandResource createEmptyInstance(IRapidFireFileResource file) {
        return new RapidFireCommandResource(file, CommandType.COMPILE, 0);
    }

    public RapidFireCommandResource(IRapidFireFileResource file, CommandType commandType, int sequence) {

        if (commandType == null) {
            throw new IllegalParameterException("fieldToConvert", null); //$NON-NLS-1$
        }

        this.parentJob = file.getParentJob();
        this.parentFile = file;
        this.delegate = new RapidFireCommandResourceDelegate(parentJob.getDataLibrary(), parentJob.getName(), file.getPosition(), commandType,
            sequence);
        super.setSubSystem((ISubSystem)file.getParentSubSystem());
    }

    public CommandKey getKey() {
        return new CommandKey(parentFile.getKey(), delegate.getCommandType(), delegate.getSequence());
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

    public IRapidFireFileResource getParentResource() {
        return parentFile;
    }

    public IRapidFireNodeResource getParentNode() {
        return parentNode;
    }

    public void setParentNode(IRapidFireNodeResource parentNode) {
        this.parentNode = parentNode;
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

    public void reload(Shell shell) throws Exception {

        IRapidFireCommandResource command = getParentSubSystem().getCommand(getParentResource(), getCommandType(), getSequence(), shell);

        delegate.setCommand(command.getCommand());
    }

    public int compareTo(IRapidFireCommandResource resource) {
        return delegate.compareTo(resource);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

}
