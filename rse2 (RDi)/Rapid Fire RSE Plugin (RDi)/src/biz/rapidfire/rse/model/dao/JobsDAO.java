/*******************************************************************************
 * Copyright (c) 2012-2017 iSphere Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.model.dao;

import biz.rapidfire.core.model.IRapidFireInstanceResource;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.dao.AbstractJobsDAO;
import biz.rapidfire.core.model.dao.IJobsDAO;
import biz.rapidfire.rse.model.RapidFireJobResource;

import com.ibm.as400.access.QSYSObjectPathName;

public class JobsDAO extends AbstractJobsDAO implements IJobsDAO {

    public JobsDAO(String connectionName) throws Exception {
        super(new BaseDAO(connectionName));
    }

    @Override
    protected IRapidFireJobResource createJobInstance(IRapidFireInstanceResource parent, String name, String description,
        boolean doCreateEnvironment, QSYSObjectPathName jobQueue) {
        return new RapidFireJobResource(parent, name, description, doCreateEnvironment, jobQueue);
    }
}
