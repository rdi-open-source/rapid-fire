/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.job.wizard;

import org.eclipse.jface.wizard.IWizardPage;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.maintenance.job.JobValues;
import biz.rapidfire.core.maintenance.library.LibraryValues;
import biz.rapidfire.core.maintenance.librarylist.LibraryListValues;
import biz.rapidfire.core.maintenance.wizard.AbstractNewWizard;

public class NewJobWizard extends AbstractNewWizard {

    JobPage jobPage;
    LibraryPage librariesPage;
    LibraryListPage librarylistPage;
    private boolean doSkipLibraryListPage;

    public NewJobWizard() {
        setWindowTitle(Messages.Wizard_Title_New_Job_wizard);
        setNeedsProgressMonitor(true);
    }

    @Override
    public void addPages() {
        super.addPages(); // Adds the data library page, if necessary

        JobValues jobValues = JobValues.createInitialized();
        LibraryValues libraryValues = LibraryValues.createInitialized();
        LibraryListValues libraryListValues = LibraryListValues.createInitialized();

        addPage(new JobPage(jobValues));
        addPage(new LibraryPage(libraryValues));
        addPage(new LibraryListPage(libraryListValues));
    }

    public boolean isSkipLibraryListPage() {
        return doSkipLibraryListPage;
    }

    protected void setSkipLibraryListPage(boolean skip) {
        this.doSkipLibraryListPage = skip;
    }

    @Override
    public boolean canFinish() {

        JobPage jobPage = (JobPage)getPage(JobPage.NAME);

        boolean skipLibraryListPage;
        if (jobPage.getValues().isCreateEnvironment()) {
            skipLibraryListPage = false;
        } else {
            skipLibraryListPage = true;
        }

        for (int i = 0; i < getPageCount(); i++) {
            IWizardPage page = getPages()[i];
            if (LibraryListPage.NAME.equals(page.getName()) && skipLibraryListPage) {
                // ignore library list page
            } else {
                if (!page.isPageComplete()) {
                    return false;
                }
            }
        }

        return true;
    }
}
