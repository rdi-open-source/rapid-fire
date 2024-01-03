/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model;

import biz.rapidfire.core.maintenance.area.shared.AreaKey;

public interface IFileCopyStatus extends IRapidFireResource {

    public AreaKey getKey();

    public IRapidFireJobResource getJob();

    public int getPosition();

    public String getArea();

    public String getFile();

    public String getLibrary();

    public long getRecordsInProductionLibrary();

    public long getRecordsInShadowLibrary();

    public long getRecordsToCopy();

    public long getRecordsCopied();

    public String getEstimatedTime();

    public long getRecordsWithDuplicateKey();

    public long getChangesToApply();

    public long getChangesApplied();

    public int getPercentDone();
}
