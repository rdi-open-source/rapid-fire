/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model.maintenance.file;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.helpers.StringHelper;

public class FileValues implements Cloneable, CopyProgram, ConversionProgram {

    private FileKey key;
    private String fileName;
    private Type type;
    private String copyProgramLibraryName;
    private String copyProgramName;
    private String conversionProgramLibraryName;
    private String conversionProgramName;

    public static String[] getTypeLabels() {

        String[] labels = new String[2];

        labels[0] = Type.PHYSICAL.label();
        labels[1] = Type.LOGICAL.label();

        return labels;
    }

    public static String[] getCopyProgramSpecialValues() {

        String[] labels = new String[2];

        labels[0] = CopyProgram.GEN;
        labels[1] = CopyProgram.NONE;

        return labels;
    }

    public static String[] getConversionProgramSpecialValues() {

        String[] labels = new String[1];

        labels[0] = ConversionProgram.NONE;

        return labels;
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

    public String getType() {

        if (type == null) {
            return ""; //$NON-NLS-1$
        } else {
            return type.label();
        }
    }

    public void setType(String type) {

        if (StringHelper.isNullOrEmpty(type.trim())) {
            this.type = null; //$NON-NLS-1$
        } else {
            this.type = Type.valueOf(type.trim().substring(1));
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
        setType(null);
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
