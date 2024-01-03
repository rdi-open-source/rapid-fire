/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.file;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.maintenance.IResourceValues;
import biz.rapidfire.core.maintenance.file.shared.ConversionProgram;
import biz.rapidfire.core.maintenance.file.shared.CopyProgram;
import biz.rapidfire.core.maintenance.file.shared.FileKey;
import biz.rapidfire.core.maintenance.file.shared.FileType;
import biz.rapidfire.core.maintenance.job.shared.JobKey;

public class FileValues implements IResourceValues {

    private static final String EMPTY = ""; //$NON-NLS-1$

    private FileKey key;
    private String fileName;
    private FileType fileType;
    private String copyProgramLibraryName;
    private String copyProgramName;
    private String conversionProgramLibraryName;
    private String conversionProgramName;

    public static String[] getTypeLabels() {
        return FileType.labels();
    }

    public static String[] getCopyProgramSpecialValues() {
        return CopyProgram.labels();
    }

    public static String[] getConversionProgramSpecialValues() {
        return ConversionProgram.labels();
    }

    public static FileValues createInitialized() {

        FileValues fileValues = new FileValues();
        fileValues.setKey(new FileKey(new JobKey(EMPTY), 0));
        fileValues.setFileName(EMPTY);
        fileValues.setFileType(FileType.PHYSICAL.label());
        fileValues.setCopyProgramName(CopyProgram.GEN.label());
        fileValues.setCopyProgramLibraryName(EMPTY);
        fileValues.setConversionProgramName(ConversionProgram.NONE.label());
        fileValues.setConversionProgramLibraryName(EMPTY);

        return fileValues;
    }

    public FileKey getKey() {
        ensureKey();
        return key;
    }

    public void setKey(FileKey key) {
        ensureKey();
        this.key = key;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName.trim();
    }

    public String getFileType() {

        if (fileType == null) {
            return ""; //$NON-NLS-1$
        } else {
            return fileType.label();
        }
    }

    public void setFileType(String type) {

        if (type == null || type.trim().length() == 0) {
            this.fileType = null;
        } else {
            this.fileType = FileType.find(type.trim());
        }
    }

    public String getCopyProgramLibraryName() {
        return copyProgramLibraryName;
    }

    public void setCopyProgramLibraryName(String copyProgramLibraryName) {
        this.copyProgramLibraryName = copyProgramLibraryName.trim();
    }

    public String getCopyProgramName() {
        return copyProgramName;
    }

    public void setCopyProgramName(String copyProgramName) {
        this.copyProgramName = copyProgramName.trim();
    }

    public String getConversionProgramLibraryName() {
        return conversionProgramLibraryName;
    }

    public void setConversionProgramLibraryName(String conversionProgramLibraryName) {
        this.conversionProgramLibraryName = conversionProgramLibraryName.trim();
    }

    public String getConversionProgramName() {
        return conversionProgramName;
    }

    public void setConversionProgramName(String conversionProgramName) {
        this.conversionProgramName = conversionProgramName.trim();
    }

    public void clear() {
        setFileName(null);
        setFileType(null);
        setCopyProgramLibraryName(null);
        setCopyProgramName(null);
        setConversionProgramLibraryName(null);
        setConversionProgramName(null);
    }

    private void ensureKey() {

        if (key == null) {
            key = new FileKey(null, 0);
        }
    }

    @Override
    public FileValues clone() {

        try {

            FileValues fileValues = (FileValues)super.clone();
            fileValues.setKey((FileKey)getKey().clone());

            return fileValues;

        } catch (CloneNotSupportedException e) {
            RapidFireCorePlugin.logError("*** Clone not supported. ***", e); //$NON-NLS-1$
            throw new biz.rapidfire.core.exceptions.CloneNotSupportedException(ExceptionHelper.getLocalizedMessage(e), e);
        }
    }
}
