/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.model.dao;

import java.util.HashMap;
import java.util.Map;

import biz.rapidfire.core.model.dao.IBaseDAO;

public class DAOManager {

    /**
     * The instance of this Singleton class.
     */
    private static DAOManager instance;

    private Map<String, IBaseDAO> baseDAOs;

    /**
     * Private constructor to ensure the Singleton pattern.
     */
    private DAOManager() {
        this.baseDAOs = new HashMap<String, IBaseDAO>();
    }

    /**
     * Thread-safe method that returns the instance of this Singleton class.
     */
    public synchronized static DAOManager getInstance() {
        if (instance == null) {
            instance = new DAOManager();
        }
        return instance;
    }

    public IBaseDAO getBaseDAO(String connectionName, String libraryName, boolean isCommitControl) throws Exception {

        String key = connectionName + ":" + libraryName + ":commit=" + isCommitControl;

        IBaseDAO baseDAO = baseDAOs.get(key);
        if (baseDAO == null) {
            baseDAO = produceBaseDAO(connectionName, libraryName, isCommitControl);
            baseDAOs.put(key, baseDAO);
        }

        return baseDAO;
    }

    protected IBaseDAO produceBaseDAO(String connectionName, String libraryName, boolean isCommitControl) throws Exception {
        return new BaseDAO(connectionName, libraryName, isCommitControl);
    }

    public void destroy() {
        instance = null;
    }
}