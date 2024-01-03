/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model;

import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.maintenance.command.shared.CommandKey;
import biz.rapidfire.core.maintenance.command.shared.CommandType;

public interface IRapidFireCommandResource extends IRapidFireChildResource<IRapidFireFileResource> {

    public CommandKey getKey();

    /*
     * Key attributes
     */

    public String getJob();

    public int getPosition();

    public CommandType getCommandType();

    public int getSequence();

    /*
     * Other attributes
     */

    public String getCommand();

    public void setCommand(String command);

    public void reload(Shell shell) throws Exception;
}
