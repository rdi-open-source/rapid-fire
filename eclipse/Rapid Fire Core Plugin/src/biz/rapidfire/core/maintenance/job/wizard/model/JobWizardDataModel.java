/*******************************************************************************
 * Copyright (c) 2017-2018 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.job.wizard.model;

import biz.rapidfire.core.maintenance.librarylist.LibraryListEntries;
import biz.rapidfire.core.maintenance.librarylist.LibraryListEntry;
import biz.rapidfire.core.maintenance.wizard.model.WizardDataModel;
import biz.rapidfire.core.model.QualifiedProgramName;

public class JobWizardDataModel extends WizardDataModel {

    // Job page
    private String jobDescription;
    private boolean isCreateEnvironment;
    private QualifiedProgramName qualifiedJobQueueName;
    private boolean isCancelASPThresholdExceeds;

    // Library page
    private String libraryName;
    private String shadowLibraryName;

    // Library list page
    private String libraryListName;
    private String libraryListDescription;
    private LibraryListEntries libraryListEntries;

    public static JobWizardDataModel createInitialized() {

        JobWizardDataModel model = new JobWizardDataModel();

        model.initialize();

        model.qualifiedJobQueueName = new QualifiedProgramName(EMPTY, EMPTY);

        // Job page
        model.setJobDescription(EMPTY);
        model.setCreateEnvironment(true);
        model.setCancelASPThresholdExceeds(true);

        // Library page
        model.setLibraryName(EMPTY);
        model.setShadowLibraryName(EMPTY);

        // Library list page
        model.setLibraryListName(EMPTY);
        model.setLibraryListDescription(EMPTY);
        model.setLibraryListEntries(new LibraryListEntries());

        return model;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String description) {
        this.jobDescription = description;
    }

    public boolean isCreateEnvironment() {
        return isCreateEnvironment;
    }

    public void setCreateEnvironment(boolean isCreateEnvironment) {
        this.isCreateEnvironment = isCreateEnvironment;
    }

    public QualifiedProgramName getQualifiedJobQueueName() {
        return qualifiedJobQueueName;
    }

    public String getJobQueueName() {
        return qualifiedJobQueueName.getName();
    }

    public void setJobQueueName(String programName) {
        this.qualifiedJobQueueName.setName(programName);
    }

    public String getJobQueueLibraryName() {
        return qualifiedJobQueueName.getLibrary();
    }

    public void setJobQueueLibraryName(String libraryName) {
        this.qualifiedJobQueueName.setLibrary(libraryName);
    }

    public boolean isCancelASPThresholdExceeds() {
        return isCancelASPThresholdExceeds;
    }

    public void setCancelASPThresholdExceeds(boolean isCancelASPThresholdExceeds) {
        this.isCancelASPThresholdExceeds = isCancelASPThresholdExceeds;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    public String getShadowLibraryName() {
        return shadowLibraryName;
    }

    public void setShadowLibraryName(String shadowLibraryName) {
        this.shadowLibraryName = shadowLibraryName;
    }

    public String getLibraryListName() {
        return libraryListName;
    }

    public void setLibraryListName(String libraryListName) {
        this.libraryListName = libraryListName;
    }

    public String getLibraryListDescription() {
        return libraryListDescription;
    }

    public void setLibraryListDescription(String description) {
        this.libraryListDescription = description;
    }

    public LibraryListEntries getLibraryListEntries() {
        return libraryListEntries;
    }

    public LibraryListEntry[] getLibraryListEntriesForUI() {
        return this.libraryListEntries.getLibraryListEntries();
    }

    public void setLibraryListEntries(LibraryListEntries libraryListEntries) {
        this.libraryListEntries = libraryListEntries;
    }

    public void setLibraryListEntriesFromUI(LibraryListEntry[] libraryListEntries) {
        this.libraryListEntries.setLibraryListEntries(libraryListEntries);
    }
}
