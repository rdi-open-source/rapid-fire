/*******************************************************************************
 * Copyright (c) 2012-2017 iSphere Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model;

import java.util.List;

import biz.rapidfire.core.model.dao.IJobsDAO;

public abstract class AbstractJobs {

    private String connectionName;
    private String library;

    public AbstractJobs(String connectionName, String library) throws Exception {

        this.connectionName = connectionName;
        this.library = library;
    }

    public List<IJob> load() throws Exception {

        IJobsDAO jobsDAO = createDAO(connectionName, library);

        return jobsDAO.load();
    }

    protected abstract IJobsDAO createDAO(String connectionName, String library) throws Exception;
}
