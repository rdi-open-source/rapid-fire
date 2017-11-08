/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model.dao;

import java.util.HashMap;
import java.util.Map;

import com.ibm.as400.access.AS400;

public final class JdbcConnectionManager {

    /**
     * The instance of this Singleton class.
     */
    private static JdbcConnectionManager instance;

    /**
     * Private constructor to ensure the Singleton pattern.
     */
    private JdbcConnectionManager() {

        this.services = new HashMap<String, JdbcConnectionService>();
    }

    private Map<String, JdbcConnectionService> services;

    /**
     * Thread-safe method that returns the instance of this Singleton class.
     */
    public synchronized static JdbcConnectionManager getInstance() {
        if (instance == null) {
            instance = new JdbcConnectionManager();
        }
        return instance;
    }

    public JdbcConnectionService getJdbcConnectionService(String hostName, AS400 system) {

        JdbcConnectionService service = services.get(hostName);
        if (service == null) {
            service = new JdbcConnectionService(hostName, system); 
            services.put(hostName, service);
        }

        return service;
    }

    public void destroy() {

        for (JdbcConnectionService service : services.values()) {
            service.destroy();
        }

        services.clear();
    }
}