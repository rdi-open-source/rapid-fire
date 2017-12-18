/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.job.wizard;

import org.eclipse.jface.dialogs.MessageDialog;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.dialogs.MessageDialogAsync;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.maintenance.AbstractManager;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.job.JobManager;
import biz.rapidfire.core.maintenance.job.JobValues;
import biz.rapidfire.core.maintenance.job.shared.JobKey;
import biz.rapidfire.core.maintenance.library.LibraryManager;
import biz.rapidfire.core.maintenance.library.LibraryValues;
import biz.rapidfire.core.maintenance.library.shared.LibraryKey;
import biz.rapidfire.core.maintenance.librarylist.LibraryListManager;
import biz.rapidfire.core.maintenance.librarylist.LibraryListValues;
import biz.rapidfire.core.maintenance.librarylist.shared.LibraryListKey;
import biz.rapidfire.core.maintenance.wizard.AbstractNewWizard;
import biz.rapidfire.core.maintenance.wizard.AbstractWizardPage;
import biz.rapidfire.core.maintenance.wizard.DataLibraryPage;
import biz.rapidfire.core.model.dao.IJDBCConnection;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;

public class NewJobWizard extends AbstractNewWizard {

    JobPage jobPage;
    LibraryPage librariesPage;
    LibraryListPage librarylistPage;

    public NewJobWizard() {
        setWindowTitle(Messages.Wizard_Title_New_Job_wizard);
        setNeedsProgressMonitor(false);
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

    protected void setSkipLibraryListPage(boolean skip) {

        if (skip) {
            hidePage(LibraryListPage.NAME);
        } else {
            showPage(LibraryListPage.NAME);
        }
    }

    @Override
    public boolean canFinish() {

        for (int i = 0; i < getPageCount(); i++) {
            AbstractWizardPage page = (AbstractWizardPage)getPages()[i];
            if (LibraryListPage.NAME.equals(page.getName()) && !page.isEnabled()) {
                // ignore library list page
            } else {
                if (!page.isPageComplete()) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean performFinish() {

        JobManager jobManager = null;
        LibraryManager libraryManager = null;
        LibraryListManager libraryListManager = null;

        IJDBCConnection connection = null;

        try {

            Result result;

            result = validateDataLibrary();
            if (result.isError()) {
                setActivePage(getDataLibraryPage());
                displayError(result, DataLibraryPage.NAME);
                return false;
            }

            DataLibraryPage dataLibraryPage = (DataLibraryPage)getPage(DataLibraryPage.NAME);

            String connectionName = dataLibraryPage.getConnectionName();
            String dataLibrary = dataLibraryPage.getDataLibraryName();

            connection = JDBCConnectionManager.getInstance().getConnection(connectionName, dataLibrary, true);

            jobManager = new JobManager(connection);

            libraryManager = new LibraryManager(connection);

            AbstractWizardPage libraryListPage = getLibraryListPage();

            if (libraryListPage.isEnabled()) {
                libraryListManager = new LibraryListManager(connection);
            }

            result = validateJob(jobManager);
            if (result.isError()) {
                setActivePage(getJobPage());
                displayError(result, JobPage.NAME);
                return false;
            }

            result = validateLibrary(libraryManager);
            if (result.isError()) {
                setActivePage(getLibraryPage());
                displayError(result, LibraryPage.NAME);
                return false;
            }

            if (libraryListManager != null) {
                result = validateLibraryList(libraryListManager);
                if (result.isError()) {
                    setActivePage(getLibraryListPage());
                    displayError(result, LibraryPage.NAME);
                    return false;
                }
            }

            jobManager.book();
            libraryManager.book();

            if (libraryListManager != null) {
                libraryListManager.book();
            }

            storePreferences();

            MessageDialog.openInformation(getShell(), Messages.Wizard_Title_New_Job_wizard,
                Messages.bindParameters(Messages.NewJobWizard_Rapid_Fire_job_A_created, getJobValues().getKey().getJobName()));

            return true;

        } catch (Exception e) {

            RapidFireCorePlugin.logError("*** Failed to execute Job wizard ***", e); //$NON-NLS-1$
            MessageDialogAsync.displayError(getShell(), ExceptionHelper.getLocalizedMessage(e));

        } finally {

            closeFilesOfManager(jobManager);
            closeFilesOfManager(libraryManager);
            closeFilesOfManager(libraryListManager);
        }

        return false;
    }

    private void closeFilesOfManager(AbstractManager<?, ?, ?, ?> manager) {

        if (manager != null) {
            try {
                manager.closeFiles();
            } catch (Exception e) {
                RapidFireCorePlugin.logError("*** Could not terminate manager '" + manager.getClass().getSimpleName() + "' ***", e); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
    }

    private JobPage getJobPage() {

        JobPage jobPage = (JobPage)getPage(JobPage.NAME);

        return jobPage;
    }

    private LibraryPage getLibraryPage() {

        LibraryPage libraryPage = (LibraryPage)getPage(LibraryPage.NAME);

        return libraryPage;
    }

    private LibraryListPage getLibraryListPage() {

        LibraryListPage libraryListPage = (LibraryListPage)getPage(LibraryListPage.NAME);

        return libraryListPage;
    }

    private JobValues getJobValues() {

        JobPage jobPage = getJobPage();
        JobValues jobValues = jobPage.getValues();

        return jobValues;
    }

    private LibraryValues getLibraryValues() {

        JobValues jobValues = getJobValues();

        LibraryPage libraryPage = getLibraryPage();
        LibraryValues libraryValues = libraryPage.getValues();
        libraryValues.setKey(new LibraryKey(jobValues.getKey(), libraryValues.getKey().getLibrary()));

        return libraryValues;
    }

    private LibraryListValues getLibraryListValues() {

        JobValues jobValues = getJobValues();

        LibraryListPage libraryListPage = getLibraryListPage();
        LibraryListValues libraryListValues = libraryListPage.getValues();
        libraryListValues.setKey(new LibraryListKey(jobValues.getKey(), libraryListValues.getKey().getLibraryList()));

        return libraryListValues;
    }

    private Result validateJob(JobManager jobManager) throws Exception {

        JobValues jobValues = getJobValues();

        jobManager.openFiles();
        jobManager.initialize(MaintenanceMode.CREATE, new JobKey(jobValues.getKey().getJobName()));
        jobManager.setValues(jobValues);
        Result result = jobManager.check();

        return result;
    }

    private Result validateLibrary(LibraryManager libraryManager) throws Exception {

        LibraryValues libraryValues = getLibraryValues();

        libraryManager.openFiles();
        libraryManager.initialize(MaintenanceMode.CREATE, new LibraryKey(new JobKey(libraryValues.getKey().getJobName()), libraryValues.getKey()
            .getLibrary()));
        libraryManager.setValues(libraryValues);
        Result result = libraryManager.check();

        return result;
    }

    private Result validateLibraryList(LibraryListManager libraryListManager) throws Exception {

        LibraryListValues libraryListValues = getLibraryListValues();

        libraryListManager.openFiles();
        libraryListManager.initialize(MaintenanceMode.CREATE, new LibraryListKey(new JobKey(libraryListValues.getKey().getJobName()),
            libraryListValues.getKey().getLibraryList()));
        libraryListManager.setValues(libraryListValues);
        Result result = libraryListManager.check();

        return result;
    }
}
