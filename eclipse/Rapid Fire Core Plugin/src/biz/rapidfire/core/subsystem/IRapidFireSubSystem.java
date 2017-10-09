/*******************************************************************************
 * Copyright (c) 2012-2017 iSphere Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.subsystem;

import biz.rapidfire.core.model.IRapidFireFileResource;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.IRapidFireLibraryResource;

public interface IRapidFireSubSystem {

    public IRapidFireJobResource[] getJobs(String library) throws Exception;

    public IRapidFireFileResource[] getFiles(String library, String job) throws Exception;

    public IRapidFireLibraryResource[] getLibraries(String library, String job) throws Exception;
}
