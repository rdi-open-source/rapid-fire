/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.filecopyprogramgenerator;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

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

    @Override
    public boolean equals(Object object) {

        if (object == this) return true;
        if (!(object instanceof FileCopyProgramGeneratorValues)) {
            return false;
        }

        FileCopyProgramGeneratorValues value = (FileCopyProgramGeneratorValues)object;

        EqualsBuilder equalsBuilder = new EqualsBuilder();

        equalsBuilder.append(sourceFile, value.getSourceFile());
        equalsBuilder.append(sourceFileLibrary, value.getSourceFileLibrary());
        equalsBuilder.append(sourceMember, value.getSourceMember());

        equalsBuilder.append(isReplace(), value.isReplace());
        equalsBuilder.append(area, value.getArea());

        equalsBuilder.append(library, value.getLibrary());
        equalsBuilder.append(shadowLibrary, value.getShadowLibrary());

        equalsBuilder.append(conversionProgram, value.getConversionProgram());
        equalsBuilder.append(conversionProgramLibrary, value.getConversionProgramLibrary());

        return equalsBuilder.isEquals();
    }

    @Override
    public int hashCode() {

        HashCodeBuilder hashBuilder = new HashCodeBuilder(37, 3);

        hashBuilder.append(sourceFile);
        hashBuilder.append(sourceFileLibrary);
        hashBuilder.append(sourceMember);

        hashBuilder.append(isReplace());
        hashBuilder.append(area);

        hashBuilder.append(library);
        hashBuilder.append(shadowLibrary);

        hashBuilder.append(conversionProgram);
        hashBuilder.append(conversionProgramLibrary);

        return hashBuilder.toHashCode();
    }
}
