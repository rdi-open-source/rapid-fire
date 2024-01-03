/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model.queries;

import biz.rapidfire.core.maintenance.area.shared.AreaKey;
import biz.rapidfire.core.maintenance.file.shared.FileKey;
import biz.rapidfire.core.model.IFileCopyStatus;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.subsystem.IRapidFireSubSystem;

public class FileCopyStatus implements IFileCopyStatus {

    private IRapidFireJobResource job;
    private int position;
    private String area;
    private String file;
    private String library;
    private long recordsInProductionLibrary;
    private long recordsInShadowLibrary;
    private long recordsToCopy;
    private long recordsCopied;
    private String estimatedTime; // dd-hh:MM:ss
    private long recordsWithDuplicateKey;
    private long changesToApply;
    private long changesApplied;
    private int percentDone;

    public AreaKey getKey() {

        FileKey fileKey = new FileKey(job.getKey(), position);
        AreaKey key = new AreaKey(fileKey, area);

        return key;
    }

    public String getDataLibrary() {
        return job.getDataLibrary();
    }

    public IRapidFireSubSystem getParentSubSystem() {
        return job.getParentSubSystem();
    }

    public IRapidFireJobResource getJob() {
        return job;
    }

    public void setJob(IRapidFireJobResource job) {
        this.job = job;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getLibrary() {
        return library;
    }

    public void setLibrary(String library) {
        this.library = library;
    }

    public long getRecordsInProductionLibrary() {
        return recordsInProductionLibrary;
    }

    public void setRecordsInProductionLibrary(long recordsInProductionLibrary) {
        this.recordsInProductionLibrary = recordsInProductionLibrary;
    }

    public long getRecordsInShadowLibrary() {
        return recordsInShadowLibrary;
    }

    public void setRecordsInShadowLibrary(long recordsInShadowLibrary) {
        this.recordsInShadowLibrary = recordsInShadowLibrary;
    }

    public long getRecordsToCopy() {
        return recordsToCopy;
    }

    public void setRecordsToCopy(long recordsToCopy) {
        this.recordsToCopy = recordsToCopy;
    }

    public long getRecordsCopied() {
        return recordsCopied;
    }

    public void setRecordsCopied(long recordsCopied) {
        this.recordsCopied = recordsCopied;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public long getRecordsWithDuplicateKey() {
		return recordsWithDuplicateKey;
	}

	public void setRecordsWithDuplicateKey(long recordsWithDuplicateKey) {
		this.recordsWithDuplicateKey = recordsWithDuplicateKey;
	}

	public long getChangesToApply() {
        return changesToApply;
    }

    public void setChangesToApply(long changesToApply) {
        this.changesToApply = changesToApply;
    }

    public long getChangesApplied() {
        return changesApplied;
    }

    public void setChangesApplied(long changesApplied) {
        this.changesApplied = changesApplied;
    }

    public int getPercentDone() {
        return percentDone;
    }

    public void setPercentDone(int percentDone) {
        this.percentDone = percentDone;
    }

    public Object getAdapter(Class arg0) {
        return null;
    }
}
