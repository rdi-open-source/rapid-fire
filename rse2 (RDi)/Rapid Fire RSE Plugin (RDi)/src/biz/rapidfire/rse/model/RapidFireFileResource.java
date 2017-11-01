/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.model;

import org.eclipse.rse.core.subsystems.AbstractResource;
import org.eclipse.rse.core.subsystems.ISubSystem;

import biz.rapidfire.core.model.FileType;
import biz.rapidfire.core.model.IRapidFireFileResource;
import biz.rapidfire.core.subsystem.IRapidFireSubSystem;

public class RapidFireFileResource extends AbstractResource implements IRapidFireFileResource, Comparable<IRapidFireFileResource> {

    private String dataLibrary;
    private String job;
    private int position;
    private String file;
    private FileType fileType;
    private String copyProgramName;
    private String copyProgramLibrary;
    private String conversionProgramName;
    private String conversionProgramLibrary;

    public RapidFireFileResource(String library, String job, int position) {

        this.dataLibrary = library;
        this.job = job;
        this.position = position;
    }

    /*
     * IRapidFireFileResource methods
     */

    public String getDataLibrary() {
        return dataLibrary;
    }

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

        return getName().compareTo(resource.getName());
    }

    public void setParentSubSystem(IRapidFireSubSystem subSystem) {
        super.setSubSystem((ISubSystem)subSystem);
    }

    public IRapidFireSubSystem getParentSubSystem() {
        return (IRapidFireSubSystem)super.getSubSystem();
    }

    @Override
    public String toString() {
        return getName();
    }

}
