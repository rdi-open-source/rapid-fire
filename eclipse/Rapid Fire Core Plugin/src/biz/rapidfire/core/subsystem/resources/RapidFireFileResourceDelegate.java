/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.subsystem.resources;

import biz.rapidfire.core.maintenance.file.shared.FileType;
import biz.rapidfire.core.model.IRapidFireFileResource;

public class RapidFireFileResourceDelegate implements Comparable<IRapidFireFileResource> {

    private String dataLibrary;
    private String job;
    private int position;
    private String file;
    private FileType fileType;
    private String copyProgramName;
    private String copyProgramLibrary;
    private String conversionProgramName;
    private String conversionProgramLibrary;

    public RapidFireFileResourceDelegate(String dataLibrary, String job, int position) {

        this.dataLibrary = dataLibrary;
        this.job = job;
        this.position = position;
    }

    /*
     * IRapidFireResource methods
     */

    public String getDataLibrary() {
        return dataLibrary;
    }

    /*
     * IRapidFireFileResource methods
     */

    public String getJob() {
        return job;
    }

    public int getPosition() {
        return position;
    }

    public String getName() {
        return file;
    }

    public void setName(String file) {
        this.file = file;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public String getCopyProgramName() {
        return copyProgramName;
    }

    public void setCopyProgramName(String copyProgramName) {
        this.copyProgramName = copyProgramName;
    }

    public String getCopyProgramLibrary() {
        return copyProgramLibrary;
    }

    public void setCopyProgramLibrary(String copyProgramLibrary) {
        this.copyProgramLibrary = copyProgramLibrary;
    }

    public String getConversionProgramName() {
        return conversionProgramName;
    }

    public void setConversionProgramName(String conversionProgramName) {
        this.conversionProgramName = conversionProgramName;
    }

    public String getConversionProgramLibrary() {
        return conversionProgramLibrary;
    }

    public void setConversionProgramLibrary(String conversionProgramLibrary) {
        this.conversionProgramLibrary = conversionProgramLibrary;
    }

    public boolean isLogicalFile() {
        return getFileType().equals(FileType.LOGICAL);
    }

    public int compareTo(IRapidFireFileResource resource) {

        if (resource == null) {
            return 1;
        }

        int result = resource.getDataLibrary().compareTo(getDataLibrary());
        if (result != 0) {
            return result;
        }

        result = resource.getJob().compareTo(getJob());
        if (result != 0) {
            return result;
        }

        if (getPosition() > resource.getPosition()) {
            return 1;
        } else if (getPosition() < resource.getPosition()) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return getName();
    }

}
