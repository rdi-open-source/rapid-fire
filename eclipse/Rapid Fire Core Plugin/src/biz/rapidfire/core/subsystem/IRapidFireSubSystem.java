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
import biz.rapidfire.core.model.IRapidFireActivityResource;
import biz.rapidfire.core.model.IRapidFireAreaResource;
import biz.rapidfire.core.model.IRapidFireCommandResource;
import biz.rapidfire.core.model.IRapidFireConversionResource;
import biz.rapidfire.core.model.IRapidFireFileResource;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.IRapidFireLibraryListResource;
import biz.rapidfire.core.model.IRapidFireLibraryResource;
import biz.rapidfire.core.model.IRapidFireNotificationResource;

public interface IRapidFireSubSystem {

    public IRapidFireJobResource[] getJobs(String library, Shell shell) throws Exception;

    public IRapidFireActivityResource[] getActivities(IRapidFireJobResource file, Shell shell) throws Exception;

    public IRapidFireFileResource[] getFiles(IRapidFireJobResource job, Shell shell) throws Exception;

    public IRapidFireLibraryListResource[] getLibraryLists(IRapidFireJobResource job, Shell shell) throws Exception;

    public IRapidFireLibraryResource[] getLibraries(IRapidFireJobResource job, Shell shell) throws Exception;

    public IRapidFireNotificationResource[] getNotifications(IRapidFireJobResource job, Shell shell) throws Exception;

    public IRapidFireAreaResource[] getAreas(IRapidFireFileResource file, Shell shell) throws Exception;

    public IRapidFireConversionResource[] getConversions(IRapidFireFileResource file, Shell shell) throws Exception;

    public IRapidFireCommandResource[] getCommands(IRapidFireFileResource file, Shell shell) throws Exception;

    public IFileCopyStatus[] getFileCopyStatus(IRapidFireJobResource job, Shell shell) throws Exception;

    public String getConnectionName();
}
