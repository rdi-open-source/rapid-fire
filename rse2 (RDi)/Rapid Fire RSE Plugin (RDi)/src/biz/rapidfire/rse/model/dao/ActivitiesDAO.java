/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.model.dao;

import java.sql.Time;

import biz.rapidfire.core.model.IRapidFireActivityResource;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.dao.AbstractActivitiesDAO;
import biz.rapidfire.core.model.dao.IActivitiesDAO;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;
import biz.rapidfire.rse.subsystem.resources.RapidFireActivityResource;

public class ActivitiesDAO extends AbstractActivitiesDAO implements IActivitiesDAO {

    public ActivitiesDAO(String connectionName, String libraryName) throws Exception {
        super(JDBCConnectionManager.getInstance().getConnectionForRead(connectionName, libraryName));
    }

    @Override
    protected IRapidFireActivityResource createActivityInstance(IRapidFireJobResource job, Time startTime) {
        return new RapidFireActivityResource(job, startTime);
    }

}
