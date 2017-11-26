/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rsebase.model.dao;

import java.sql.CallableStatement;
import java.sql.SQLException;

import com.ibm.as400.access.AS400;
import com.ibm.etools.iseries.core.api.ISeriesConnection;

public abstract class AbstractDAOManager {

    public AS400 getSystem(String connectionName) throws Exception {

        ISeriesConnection connection = ISeriesConnection.getConnection(connectionName);
        if (connection == null) {
            return null;
        }

        return connection.getAS400ToolboxObject(null);
    }

    protected String getStringTrim(CallableStatement statement, int parameterIndex) throws SQLException {
        return statement.getString(parameterIndex);
    }
}
