/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.model.dao;

import java.sql.Connection;

import biz.rapidfire.core.model.dao.AbstractBaseDAO;
import biz.rapidfire.core.model.dao.IBaseDAO;
import biz.rapidfire.rse.Messages;

import com.ibm.as400.access.AS400;
import com.ibm.etools.iseries.subsystems.qsys.api.IBMiConnection;

class BaseDAO extends AbstractBaseDAO implements IBaseDAO {

    private IBMiConnection ibmiConnection;
    private AS400 system;
    private Connection jdbcConnection;
    private boolean isCommitControl;

    public BaseDAO(String connectionName, String libraryName, boolean isCommitControl) throws Exception {
        super(libraryName);

        if (connectionName == null) {
            throw new Exception(Messages.bind(Messages.RseBaseDAO_Invalid_or_missing_connection_name_A, connectionName));
        }

        this.isCommitControl = isCommitControl;
        this.ibmiConnection = IBMiConnection.getConnection(connectionName);
        if (ibmiConnection == null) {
            throw new Exception(Messages.bind(Messages.RseBaseDAO_Connection_A_not_found, connectionName));
        }

        this.system = this.ibmiConnection.getAS400ToolboxObject();
    }

    @Override
    public AS400 getSystem() {
        return system;
    }

    @Override
    public String getHostName() {
        return ibmiConnection.getHostName();
    }

    @Override
    public String getConnectionName() {
        return ibmiConnection.getConnectionName();
    }

    /*
     * Does not work at the moment due to a bug in
     * IBMiConnection.getJdbcConnection().
     */
    @Override
    public Connection getJdbcConnection() throws Exception {

        String properties;
        if (getLibraryName() == null) {
            properties = "";
        } else {
            properties = ";libraries=" + getLibraryName() + ",*LIBL";
        }

        if (isCommitControl) {
            properties += ";transaction isolation=read committed";
        }

        Connection localJdbcConnection = ibmiConnection.getJDBCConnection(properties, false);
        if (localJdbcConnection == jdbcConnection) {
            return jdbcConnection;
        }

        if (jdbcConnection != null) {
            jdbcConnection.close();
        }

        jdbcConnection = localJdbcConnection;
        jdbcConnection.setAutoCommit(false);

        // Bugfix, because getJDBCConnection does not use the transaction
        // isolation of the JDBC properties.
        // (PMR 91446,031,724)
        if (isCommitControl) {
            jdbcConnection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        } else {
            jdbcConnection.setTransactionIsolation(Connection.TRANSACTION_NONE);
        }

        // Bugfix, because getJDBCConnection does not set the default
        // schema.
        // (PMR 91446,031,724)
        setCurrentLibrary(jdbcConnection);

        return jdbcConnection;
    }
}
