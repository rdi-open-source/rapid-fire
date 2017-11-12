/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.subsystem;

import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.model.IFileCopyStatus;
import biz.rapidfire.core.model.IRapidFireFileResource;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.IRapidFireLibraryResource;
import biz.rapidfire.core.model.maintenance.job.JobManager;

public interface IRapidFireSubSystem {

    public IRapidFireJobResource[] getJobs(String library, Shell shell) throws Exception;

    public IRapidFireFileResource[] getFiles(String library, String job, Shell shell) throws Exception;

    public IRapidFireLibraryResource[] getLibraries(String library, String job, Shell shell) throws Exception;

    public IFileCopyStatus[] getFileCopyStatus(String library, String job, Shell shell) throws Exception;

    public String getConnectionName();

    public JobManager getJobManager(String connectionName, String libraryName) throws Exception;
}
