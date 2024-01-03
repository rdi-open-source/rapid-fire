/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.model.dao;

import biz.rapidfire.core.maintenance.command.shared.CommandType;
import biz.rapidfire.core.model.IRapidFireCommandResource;
import biz.rapidfire.core.model.IRapidFireFileResource;
import biz.rapidfire.core.model.dao.AbstractCommandsDAO;
import biz.rapidfire.core.model.dao.ICommandsDAO;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;
import biz.rapidfire.rse.subsystem.resources.RapidFireCommandResource;

public class CommandsDAO extends AbstractCommandsDAO implements ICommandsDAO {

    public CommandsDAO(String connectionName, String libraryName) throws Exception {
        super(JDBCConnectionManager.getInstance().getConnectionForRead(connectionName, libraryName));
    }

    @Override
    protected IRapidFireCommandResource createCommandInstance(IRapidFireFileResource file, CommandType commandType, int sequence) {
        return new RapidFireCommandResource(file, commandType, sequence);
    }

}
