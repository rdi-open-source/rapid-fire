/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.filecopyprogramgenerator;

import biz.rapidfire.core.maintenance.IResourceValues;
import biz.rapidfire.core.maintenance.filecopyprogramgenerator.shared.Replace;

public class FileCopyProgramGeneratorValues implements IResourceValues {

    private String sourceFile;
    private String sourceFileLibrary;
    private String sourceMember;
    private Replace replace;
    private String area;
    private String library;
    private String shadowLibrary;
    private String conversionProgram;
    private String conversionProgramLibrary;

    public String getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    public String getSourceFileLibrary() {
        return sourceFileLibrary;
    }

    public void setSourceFileLibrary(String sourceFileLibrary) {
        this.sourceFileLibrary = sourceFileLibrary;
    }

    public String getSourceMember() {
        return sourceMember;
    }

    public void setSourceMember(String sourceMember) {
        this.sourceMember = sourceMember;
    }

    public boolean isReplace() {

        if (replace == Replace.YES) {
            return true;
        }

        return false;
    }

    public String getReplace() {

        if (replace == null) {
            return ""; //$NON-NLS-1$
        } else {
            return replace.label();
        }
    }

    public void setReplace(Replace replace) {
        this.replace = replace;
    }

    public void setReplace(boolean replace) {

        if (replace) {
            this.replace = Replace.YES;
        } else {
            this.replace = Replace.NO;
        }
    }

    public void setReplace(String replace) {

        if (replace == null || replace.trim().length() == 0) {
            this.replace = null;
        } else {
            this.replace = Replace.find(replace.trim());
        }
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLibrary() {
        return library;
    }

    public void setLibrary(String library) {
        this.library = library;
    }

    public String getShadowLibrary() {
        return shadowLibrary;
    }

    public void setShadowLibrary(String shadowLibrary) {
        this.shadowLibrary = shadowLibrary;
    }

    public String getConversionProgram() {
        return conversionProgram;
    }

    public void setConversionProgram(String conversionProgram) {
        this.conversionProgram = conversionProgram;
    }

    public String getConversionProgramLibrary() {
        return conversionProgramLibrary;
    }

    public void setConversionProgramLibrary(String conversionProgramLibrary) {
        this.conversionProgramLibrary = conversionProgramLibrary;
    }
}
