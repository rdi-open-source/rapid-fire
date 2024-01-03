/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.wizard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.progress.UIJob;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.helpers.RapidFireHelper;
import biz.rapidfire.core.host.files.Field;
import biz.rapidfire.core.maintenance.AbstractManager;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.Success;
import biz.rapidfire.core.maintenance.file.wizard.model.FileWizardDataModel;
import biz.rapidfire.core.maintenance.wizard.model.WizardDataModel;
import biz.rapidfire.core.model.IRapidFireChildResource;
import biz.rapidfire.core.model.IRapidFireFileResource;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.IRapidFireLibraryListResource;
import biz.rapidfire.core.model.IRapidFireLibraryResource;
import biz.rapidfire.core.model.IRapidFireNodeResource;
import biz.rapidfire.core.model.IRapidFireResource;
import biz.rapidfire.core.preferences.Preferences;
import biz.rapidfire.core.subsystem.IRapidFireSubSystem;
import biz.rapidfire.rsebase.helpers.SystemConnectionHelper;

import com.ibm.as400.access.AS400;

// Source: http://www.vogella.com/tutorials/EclipseWizards/article.html
public abstract class AbstractNewWizard<M extends WizardDataModel> extends Wizard implements INewWizard, IPageChangedListener {

    protected M model;

    private Map<String, Boolean> pagesVisibility;

    private Job delayedJob;

    public AbstractNewWizard(M model) {

        this.model = model;
        this.pagesVisibility = new HashMap<String, Boolean>();

        setHelpAvailable(false);
    }

    @Override
    public void setContainer(IWizardContainer wizardContainer) {
        super.setContainer(wizardContainer);

        if (wizardContainer != null) {
            WizardDialog dialog = (WizardDialog)getContainer();
            dialog.addPageChangedListener(this);
        }
    }

    public void pageChanged(PageChangedEvent event) {

        Object page = event.getSelectedPage();
        if (page instanceof AbstractWizardPage) {
            AbstractWizardPage abstractWizardPage = (AbstractWizardPage)page;

            updatePageValues(abstractWizardPage);

            updatePageMode(abstractWizardPage);

            updatePageEnablement(abstractWizardPage);

            prepareForDisplay(abstractWizardPage);

            abstractWizardPage.setFocus();
        }
    }

    @Override
    public void addPages() {

        DataLibraryPage page = new DataLibraryPage(model);
        addPage(page);
    }

    @Override
    public void addPage(IWizardPage page) {
        super.addPage(page);

        enablePage(page.getName());
    }

    @Override
    public void createPageControls(Composite pageContainer) {
        super.createPageControls(pageContainer);

        updatePagesVisibility();
    }

    public void init(IWorkbench workbench, IStructuredSelection selection) {

        if (selection instanceof IStructuredSelection) {
            IStructuredSelection structuredSelection = selection;
            if (!structuredSelection.isEmpty()) {
                Object element = structuredSelection.getFirstElement();
                if (element instanceof IRapidFireResource) {
                    IRapidFireResource resource = (IRapidFireResource)element;
                    model.setConnectionName(resource.getParentSubSystem().getConnectionName());
                    model.setDataLibraryName(resource.getDataLibrary());
                } else if (element instanceof IRapidFireSubSystem) {
                    IRapidFireSubSystem subSystem = (IRapidFireSubSystem)element;
                    model.setConnectionName(subSystem.getConnectionName());
                } else if (SystemConnectionHelper.isFilterReference(element)) {
                    Object subSystem = SystemConnectionHelper.getSubSystemOfFilterReference(element);
                    if (subSystem instanceof IRapidFireSubSystem) {
                        model.setConnectionName(((IRapidFireSubSystem)subSystem).getConnectionName());
                    }
                }

                if (element instanceof IRapidFireNodeResource) {
                    IRapidFireNodeResource resource = (IRapidFireNodeResource)element;
                    model.setJobName(resource.getJob().getName());
                } else if (element instanceof IRapidFireChildResource) {
                    IRapidFireChildResource<?> resource = (IRapidFireChildResource<?>)element;
                    model.setJobName(resource.getParentJob().getName());
                } else if (element instanceof IRapidFireJobResource) {
                    IRapidFireJobResource resource = (IRapidFireJobResource)element;
                    model.setJobName(resource.getName());
                } else if (element instanceof IRapidFireFileResource) {
                    IRapidFireFileResource resource = (IRapidFireFileResource)element;
                    model.setJobName(resource.getName());
                    if (model instanceof FileWizardDataModel) {
                        FileWizardDataModel fileWizardModel = (FileWizardDataModel)model;
                        fileWizardModel.setPosition(resource.getPosition());
                    }
                }
            }
        }

        return;
    }

    @Override
    public IWizardPage getNextPage(IWizardPage page) {

        IWizardPage nextPage = super.getNextPage(page);
        if (nextPage == null) {
            return null;
        }

        if (isPageVisible(nextPage.getName())) {
            return nextPage;
        } else {
            return getNextPage(nextPage);
        }
    }

    @Override
    public IWizardPage getPreviousPage(IWizardPage page) {

        IWizardPage previousPage = super.getPreviousPage(page);
        if (previousPage == null) {
            return null;
        }

        if (isPageVisible(previousPage.getName())) {
            return previousPage;
        } else {
            return getPreviousPage(previousPage);
        }
    }

    protected void setActivePage(IWizardPage page) {

        WizardDialog dialog = (WizardDialog)getContainer();
        dialog.showPage(page);
    }

    protected DataLibraryPage getDataLibraryPage() {

        DataLibraryPage dataLibraryPage = (DataLibraryPage)getPage(DataLibraryPage.NAME);

        return dataLibraryPage;
    }

    protected Result validateDataLibrary() throws Exception {

        String connectionName = model.getConnectionName();
        String dataLibrary = model.getDataLibraryName();

        Result result;
        StringBuilder errorMessage = new StringBuilder();

        AS400 system = SystemConnectionHelper.getSystem(connectionName);
        if (system == null) {
            result = new Result(Success.NO.label(), Messages.bindParameters(Messages.Could_not_connect_to_A, connectionName));
        } else if (!RapidFireHelper.checkLibrary(system, dataLibrary)) {
            result = new Result(Success.NO.label(), Messages.bindParameters(Messages.Library_A_not_found_on_system_B, dataLibrary, connectionName));
        } else if (!RapidFireHelper.checkRapidFireLibrary(getShell(), system, dataLibrary, errorMessage)) {
            result = new Result(Success.NO.label(), errorMessage.toString());
        } else {
            result = Result.createSuccessResult();
        }

        return result;
    }

    protected void displayError(Result result, String name) {

        IWizardPage page = getPage(name);
        if (page instanceof WizardPage) {
            WizardPage wizardPage = (WizardPage)page;
            String pageTitle = wizardPage.getTitle();
            String message = Messages.bindParameters(Messages.Error_on_wizard_page_A_B, pageTitle, result.getMessage());
            MessageDialog.openError(getShell(), Messages.E_R_R_O_R, message);
        }
    }

    protected void setPageEnablement(String pageId, boolean enabled) {

        if (enabled) {
            enablePage(pageId);
        } else {
            disablePage(pageId);
        }

        updatePageMode(getPage(pageId));
    }

    protected void updatePageMode(IWizardPage page) {

        if (page instanceof AbstractWizardPage) {
            AbstractWizardPage wizardPage = (AbstractWizardPage)page;
            wizardPage.updateMode();
        }
    }

    protected void updatePageValues(AbstractWizardPage page) {
    }

    protected void updatePageEnablement(AbstractWizardPage page) {
    }

    protected void prepareForDisplay(AbstractWizardPage page) {
    }

    protected String[] getJobNames(IRapidFireJobResource[] jobs) {

        List<String> names = new ArrayList<String>();

        for (IRapidFireJobResource job : jobs) {
            names.add(job.getName());
        }

        String[] sortedNames = names.toArray(new String[names.size()]);

        Arrays.sort(sortedNames);

        return sortedNames;
    }

    protected String[] getLibraryNames(IRapidFireLibraryResource[] libraries) {

        List<String> names = new ArrayList<String>();

        for (IRapidFireLibraryResource library : libraries) {
            names.add(library.getName());
        }

        String[] sortedNames = names.toArray(new String[names.size()]);

        Arrays.sort(sortedNames);

        return sortedNames;
    }

    protected String[] getLibraryListNames(IRapidFireLibraryListResource[] libraryLists) {

        List<String> names = new ArrayList<String>();

        if (libraryLists != null) {
            for (IRapidFireLibraryListResource libraryList : libraryLists) {
                names.add(libraryList.getName());
            }
        } else {
            names.add("<Could not library lists>");
        }

        String[] sortedNames = names.toArray(new String[names.size()]);

        Arrays.sort(sortedNames);

        return sortedNames;
    }

    protected String[] getFieldNames(Field[] fields) {

        List<String> names = new ArrayList<String>();

        if (fields != null) {
            for (Field field : fields) {
                names.add(field.getName());
            }
        } else {
            names.add("<Could not load field names>");
        }

        String[] sortedNames = names.toArray(new String[names.size()]);

        Arrays.sort(sortedNames);

        return sortedNames;
    }

    protected void storePreferences() {

        IWizardPage[] pages = getPages();
        for (IWizardPage page : pages) {
            if (page instanceof AbstractWizardPage) {
                AbstractWizardPage wizardPage = (AbstractWizardPage)page;
                wizardPage.storePreferences();
            }
        }
    }

    protected void closeFilesOfManager(AbstractManager<?, ?, ?, ?> manager) {

        if (manager != null) {
            try {
                manager.closeFiles();
            } catch (Exception e) {
                RapidFireCorePlugin.logError("*** Could not terminate manager '" + manager.getClass().getSimpleName() + "' ***", e); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
    }

    private boolean isPageVisible(String name) {

        Boolean isVisible = pagesVisibility.get(name);

        if (isVisible != null) {
            return isVisible.booleanValue();
        }

        return true;
    }

    private void disablePage(String name) {

        if (Preferences.getInstance().skipDisabledWizardPages()) {
            pagesVisibility.put(name, Boolean.FALSE);
        } else {
            pagesVisibility.put(name, Boolean.TRUE);
        }

        IWizardPage page = getPage(name);
        if (isPageCompletelyInitialized(page)) {
            ((AbstractWizardPage)page).setEnabled(false);
        }
    }

    private void enablePage(String name) {

        pagesVisibility.put(name, Boolean.TRUE);

        IWizardPage page = getPage(name);
        if (isPageCompletelyInitialized(page)) {
            ((AbstractWizardPage)page).setEnabled(true);
        }
    }

    private void updatePagesVisibility() {

        Set<Entry<String, Boolean>> pages = pagesVisibility.entrySet();
        for (Entry<String, Boolean> entry : pages) {
            if (entry.getValue()) {
                enablePage(entry.getKey());
            } else {
                disablePage(entry.getKey());
            }
        }

    }

    private boolean isPageCompletelyInitialized(IWizardPage page) {
        return page != null && page.getControl() != null;
    }

    protected void scheduleUpdatePageComplete(IUpdatePageCompleteHandler handler, Object source) {

        if (delayedJob != null) {
            delayedJob.cancel();
        }

        delayedJob = new UpdatePageCompleteJob(handler, source);
        delayedJob.schedule(400);

    }

    private class UpdatePageCompleteJob extends Job {

        private IUpdatePageCompleteHandler handler;
        private Object source;

        public UpdatePageCompleteJob(IUpdatePageCompleteHandler handler, Object source) {
            super(""); //$NON-NLS-1$

            this.handler = handler;
            this.source = source;
        }

        @Override
        protected IStatus run(IProgressMonitor progressMonitor) {

            String message = this.handler.performUpdatePageComplete(source);

            UIJob updateUI = new UpdatePageCompleteFinishedJob(handler, source, message);
            updateUI.schedule();

            delayedJob = null;

            return Status.OK_STATUS;
        }
    }

    private class UpdatePageCompleteFinishedJob extends UIJob {

        private IUpdatePageCompleteHandler handler;
        private Object source;
        private String message;

        public UpdatePageCompleteFinishedJob(IUpdatePageCompleteHandler handler, Object source, String message) {
            super(""); //$NON-NLS-1$

            this.handler = handler;
            this.source = source;
            this.message = message;
        }

        @Override
        public IStatus runInUIThread(IProgressMonitor arg0) {

            handler.performUpdatePageCompleteFinished(source, message);

            return Status.OK_STATUS;
        }
    }
}
