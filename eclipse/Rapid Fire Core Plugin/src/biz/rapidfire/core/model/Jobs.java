/*******************************************************************************
 * Copyright (c) 2012-2017 iSphere Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model;

import java.util.List;

import biz.rapidfire.core.model.dao.DAOBase;
import biz.rapidfire.core.model.dao.JobsDAO;

public class Jobs extends DAOBase {

    private String library;

    public Jobs(String connectionName, String library) throws Exception {
        super(connectionName);

        this.library = library;
    }

    public List<Job> load() throws Exception {

        JobsDAO jobsDAO = new JobsDAO(getConnectionName());

        return jobsDAO.load(library);
    }
}
