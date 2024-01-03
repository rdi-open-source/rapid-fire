/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.model.dao;

import biz.rapidfire.core.model.dao.AbstractFileCopyStatusDAO;
import biz.rapidfire.core.model.dao.IFileCopyStatusDAO;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;

public class FileCopyStatusDAO extends AbstractFileCopyStatusDAO implements IFileCopyStatusDAO {

    public FileCopyStatusDAO(String connectionName, String libraryName) throws Exception {
        super(JDBCConnectionManager.getInstance().getConnectionForRead(connectionName, libraryName));
    }

}
