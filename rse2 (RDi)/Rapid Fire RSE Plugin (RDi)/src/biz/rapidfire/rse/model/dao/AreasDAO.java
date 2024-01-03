/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.model.dao;

import biz.rapidfire.core.model.IRapidFireAreaResource;
import biz.rapidfire.core.model.IRapidFireFileResource;
import biz.rapidfire.core.model.dao.AbstractAreasDAO;
import biz.rapidfire.core.model.dao.IAreasDAO;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;
import biz.rapidfire.rse.subsystem.resources.RapidFireAreaResource;

public class AreasDAO extends AbstractAreasDAO implements IAreasDAO {

    public AreasDAO(String connectionName, String libraryName) throws Exception {
        super(JDBCConnectionManager.getInstance().getConnectionForRead(connectionName, libraryName));
    }

    @Override
    protected IRapidFireAreaResource createAreaInstance(IRapidFireFileResource file, String area) {
        return new RapidFireAreaResource(file, area);
    }

}
