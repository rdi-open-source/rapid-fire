/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem.resources;

import org.eclipse.rse.core.subsystems.AbstractResource;

import biz.rapidfire.core.model.IRapidFireFileResource;
import biz.rapidfire.rse.Messages;

public class CommandsNode extends AbstractResource {

    private String label;
    private IRapidFireFileResource file;

    public CommandsNode(IRapidFireFileResource file) {
        this.label = Messages.NodeText_Commands;
        this.file = file;
    }

    public String getLabel() {
        return label;
    }

    public IRapidFireFileResource getFile() {
        return file;
    }

}
