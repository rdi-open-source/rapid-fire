/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.model.dao;

import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.IRapidFireLibraryResource;
import biz.rapidfire.core.model.dao.AbstractLibrariesDAO;
import biz.rapidfire.core.model.dao.ILibrariesDAO;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;
import biz.rapidfire.rse.subsystem.resources.RapidFireLibraryResource;

public class LibrariesDAO extends AbstractLibrariesDAO implements ILibrariesDAO {

    public LibrariesDAO(String connectionName, String libraryName) throws Exception {
        super(JDBCConnectionManager.getInstance().getConnectionForRead(connectionName, libraryName));
    }

    @Override
    protected IRapidFireLibraryResource createLibraryInstance(IRapidFireJobResource job, String library) {
        return new RapidFireLibraryResource(job, library);
    }
}
