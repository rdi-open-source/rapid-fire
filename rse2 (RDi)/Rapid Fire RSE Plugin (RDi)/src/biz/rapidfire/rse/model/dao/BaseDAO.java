/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.model.dao;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.rse.core.PasswordPersistenceManager;
import org.eclipse.rse.core.model.SystemSignonInformation;

import biz.rapidfire.core.model.dao.AbstractBaseDAO;
import biz.rapidfire.core.model.dao.IBaseDAO;
import biz.rapidfire.rse.Messages;

import com.ibm.as400.access.AS400;
import com.ibm.etools.iseries.subsystems.qsys.api.IBMiConnection;

class BaseDAO extends AbstractBaseDAO implements IBaseDAO {

    private static final String PROPERTY_PROMPT = "prompt";
    private static final String PROPERTY_BIG_DECIMAL = "big decimal";
    private static final String PROPERTIES_LIBRARIES = "libraries";
    private static final String PROPERTY_TRANSACTION_ISOLATION = "transaction isolation";

    private static final String JDBC_TRANSACTION_ISOLATION_NONE = "none";
    private static final String JDBC_TRANSACTION_ISOLATION_READ_UNCOMMITED = "read committed";
    private static final String JDBC_TRANSACTION_ISOLATION_READ_COMMITTED = "read uncommitted";
    private static final String JDBC_TRANSACTION_ISOLATION_REPEATABLE_READ = "repeatable read";
    private static final String JDBC_TRANSACTION_ISOLATION_SERIALIZABLE = "serializable";

    private static final String JDBC_FALSE = "false";
    private static final String JDBC_TRUE = "true";

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

        Map<String, String> jdbcProperties = new HashMap<String, String>();

        // Properties of ToolboxConnectorService
        jdbcProperties.put(PROPERTY_PROMPT, JDBC_FALSE);
        jdbcProperties.put(PROPERTY_BIG_DECIMAL, JDBC_FALSE);

        // add schema
        if (getLibraryName() != null) {
            jdbcProperties.put(PROPERTIES_LIBRARIES, getLibraryName() + ",*LIBL");
        }

        // add isolation level
        if (isCommitControl) {
            jdbcProperties.put(PROPERTY_TRANSACTION_ISOLATION, JDBC_TRANSACTION_ISOLATION_READ_UNCOMMITED);
        } else {
            jdbcProperties.put(PROPERTY_TRANSACTION_ISOLATION, JDBC_TRANSACTION_ISOLATION_NONE);
        }

        // TODO: fix me
        String properties = serializeJDBCProperties(jdbcProperties);
        Connection localJdbcConnection = ibmiConnection.getJDBCConnection(properties, false);
        if (localJdbcConnection == jdbcConnection) {
            return jdbcConnection;
        }

        if (jdbcConnection != null) {
            jdbcConnection.close();
        }

        jdbcConnection = localJdbcConnection;

        jdbcConnection.setAutoCommit(false);

        System.out.println("Before: " + jdbcConnection.hashCode() + ": " + jdbcConnection.getTransactionIsolation());

        // Bugfix, because getJDBCConnection does not use the transaction
        // isolation of the JDBC properties.
        // (PMR 91446,031,724)
        if (jdbcProperties.containsKey(PROPERTY_TRANSACTION_ISOLATION)) {
            int transactionIsolation = getTransactionIsolation(jdbcProperties);
            if (transactionIsolation != -1) {
                jdbcConnection.setTransactionIsolation(transactionIsolation);
            }
        }

        System.out.println("After: " + jdbcConnection.hashCode() + ": " + jdbcConnection.getTransactionIsolation());

        // Bugfix, because getJDBCConnection does not set the default
        // schema.
        // (PMR 91446,031,724)
        setCurrentLibrary(jdbcConnection);

        return jdbcConnection;
    }

    private Integer getTransactionIsolation(Map<String, String> jdbcProperties) {

        String transactionIsolation = jdbcProperties.get(PROPERTY_TRANSACTION_ISOLATION);
        if (JDBC_TRANSACTION_ISOLATION_NONE.equals(transactionIsolation)) {
            return Integer.valueOf(Connection.TRANSACTION_NONE);
        } else if (JDBC_TRANSACTION_ISOLATION_READ_UNCOMMITED.equals(transactionIsolation)) {
            return Integer.valueOf(Connection.TRANSACTION_READ_UNCOMMITTED);
        } else if (JDBC_TRANSACTION_ISOLATION_READ_COMMITTED.equals(transactionIsolation)) {
            return Integer.valueOf(Connection.TRANSACTION_READ_COMMITTED);
        } else if (JDBC_TRANSACTION_ISOLATION_REPEATABLE_READ.equals(transactionIsolation)) {
            return Integer.valueOf(Connection.TRANSACTION_REPEATABLE_READ);
        } else if (JDBC_TRANSACTION_ISOLATION_SERIALIZABLE.equals(transactionIsolation)) {
            return Integer.valueOf(Connection.TRANSACTION_SERIALIZABLE);
        }

        return -1;
    }

    private String serializeJDBCProperties(Map<String, String> properties) {

        StringBuilder buffer = new StringBuilder();

        Set<Entry<String, String>> entries = properties.entrySet();
        for (Entry<String, String> entry : entries) {
            buffer.append(";"); //$NON-NLS-1$
            buffer.append(entry.getKey());
            buffer.append("="); //$NON-NLS-1$
            buffer.append(entry.getValue());
        }

        return buffer.toString();
    }

    private String getPassword(IBMiConnection connection) {

        /*
         * Does not help, when the password has not been saved.
         */

        PasswordPersistenceManager ppm = PasswordPersistenceManager.getInstance();
        SystemSignonInformation signOnInformation = ppm.find(connection.getHost().getSystemType(), connection.getHostName(), connection.getUserID());

        return signOnInformation.getPassword();
    }
}
