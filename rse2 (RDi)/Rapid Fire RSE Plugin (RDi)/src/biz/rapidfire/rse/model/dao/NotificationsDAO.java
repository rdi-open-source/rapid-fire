/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.model.dao;

import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.IRapidFireNotificationResource;
import biz.rapidfire.core.model.dao.AbstractNotificationsDAO;
import biz.rapidfire.core.model.dao.INotificationsDAO;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;
import biz.rapidfire.rse.subsystem.resources.RapidFireNotificationResource;

public class NotificationsDAO extends AbstractNotificationsDAO implements INotificationsDAO {

    public NotificationsDAO(String connectionName, String libraryName) throws Exception {
        super(JDBCConnectionManager.getInstance().getConnectionForRead(connectionName, libraryName));
    }

    @Override
    protected IRapidFireNotificationResource createNotificationInstance(IRapidFireJobResource job, int position) {
        return new RapidFireNotificationResource(job, position);
    }

}
