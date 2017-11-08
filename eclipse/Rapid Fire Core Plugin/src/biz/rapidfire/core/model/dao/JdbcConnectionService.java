/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model.dao;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import biz.rapidfire.core.RapidFireCorePlugin;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCDriver;

public class JdbcConnectionService {

    private String hostName;
    private AS400 system;

    private boolean isJdbcDriverRegistered;
    private Map<String, Connection> jdbcConnections;

    public JdbcConnectionService(String hostName, AS400 system) {

        this.hostName = hostName;
        this.system = system;
        this.jdbcConnections = new HashMap<String, Connection>();
    }

    public Connection getJdbcConnection(String userId, String password, String defaultLibrary) throws Exception {

        if (!isJdbcDriverRegistered) {
            isJdbcDriverRegistered = registerJdbcDriver();
            if (!isJdbcDriverRegistered) {
                return null;
            }
        }

        String properties;
        if (defaultLibrary == null) {
            properties = ""; //$NON-NLS-1$
        } else {
            properties = ";libraries=" + defaultLibrary + ",*LIBL"; //$NON-NLS-1$ //$NON-NLS-2$
        }

        properties = properties + ";prompt=true;big decimal=false"; //$NON-NLS-1$

        String jdbcConnectioyKey = hostName + properties;

        Connection jdbcConnection = jdbcConnections.get(jdbcConnectioyKey);
        if (jdbcConnection == null) {
            String driverUrl = "jdbc:as400://"; //$NON-NLS-1$
            Driver driver = DriverManager.getDriver(driverUrl);
            if (system != null && driver instanceof AS400JDBCDriver) {
                // Try to connect with AS400 to reuse password.
                AS400JDBCDriver as400Driver = (AS400JDBCDriver)driver;
                jdbcConnection = as400Driver.connect(system, getProperties(properties), properties);
            }
            if (jdbcConnection == null) {
                // Try to connect with DriverManager, prompting for the password
                // if necessary.
                String jdbcUrl = driverUrl + hostName + properties; //$NON-NLS-1$
                jdbcConnection = DriverManager.getConnection(jdbcUrl, userId, password);
            }
            jdbcConnections.put(jdbcConnectioyKey, jdbcConnection);
        }

        return jdbcConnection;
    }

    private Properties getProperties(String propsString) throws Exception {

        Properties properties = new Properties();

        while (propsString.startsWith(";")) { //$NON-NLS-1$
            propsString = propsString.substring(1);
        }

        if (propsString != null) {
            String[] props = propsString.split(";"); //$NON-NLS-1$
            for (String property : props) {
                String[] parts = property.split("="); //$NON-NLS-1$
                if (parts.length == 2) {
                    properties.put(parts[0], parts[1]);
                } else {
                    throw new Exception("Invalid property string: " + property); //$NON-NLS-1$
                }
            }
        }

        return properties;
    }

    private boolean registerJdbcDriver() {

        try {
            DriverManager.registerDriver(new AS400JDBCDriver());
            return true;
        } catch (SQLException e) {
            RapidFireCorePlugin.logError("*** Error registering JDBC connection ***", e); //$NON-NLS-1$
        }

        return false;
    }

    public void destroy() {

        for (Connection jdbcConnection : jdbcConnections.values()) {
            if (jdbcConnection != null) {
                try {
                    if (!jdbcConnection.isClosed()) {
                        jdbcConnection.close();
                    }
                } catch (Throwable e) {
                    RapidFireCorePlugin.logError("*** Could not destroy connection ***", e); //$NON-NLS-1$
                }
                jdbcConnections.clear();
            }
        }
    }

}
