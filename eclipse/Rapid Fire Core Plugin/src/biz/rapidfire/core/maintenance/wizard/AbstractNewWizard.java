/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.wizard;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.helpers.RapidFireHelper;
import biz.rapidfire.core.helpers.StringHelper;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.Success;
import biz.rapidfire.core.model.IRapidFireChildResource;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.IRapidFireResource;
import biz.rapidfire.core.preferences.Preferences;
import biz.rapidfire.core.subsystem.IRapidFireSubSystem;
import biz.rapidfire.rsebase.helpers.SystemConnectionHelper;

import com.ibm.as400.access.AS400;

// Source: http://www.vogella.com/tutorials/EclipseWizards/article.html
public abstract class AbstractNewWizard extends Wizard implements INewWizard, IPageChangedListener {

    private String initialConnectionName;
    private String initialDataLibrary;
    private String initialJobName;

    private Map<String, Boolean> pagesVisibility;

    public AbstractNewWizard() {

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

    public String getInitialJobName() {
        return initialJobName;
    }

    public void pageChanged(PageChangedEvent event) {

        Object page = event.getSelectedPage();
        if (page instanceof AbstractWizardPage) {
            AbstractWizardPage abstractWizardPage = (AbstractWizardPage)page;
            abstractWizardPage.setFocus();
        }
    }

    @Override
    public void addPages() {

        DataLibraryPage page = new DataLibraryPage();
        addPage(page);

        if (!StringHelper.isNullOrEmpty(initialConnectionName)) {
            page.setConnectionName(initialConnectionName);
        }
        if (!StringHelper.isNullOrEmpty(initialDataLibrary)) {
            page.setDataLibraryName(initialDataLibrary);
        }
        if (!StringHelper.isNullOrEmpty(initialJobName)) {
            page.setJobName(initialJobName);
        }
    }

    @Override
    public void addPage(IWizardPage page) {
        super.addPage(page);

        showPage(page.getName());
    }

    @Override
    public void createPageControls(Composite pageContainer) {
        super.createPageControls(pageContainer);

        updatePagesVisibility();
    }

    protected Result validateDataLibrary() throws Exception {

        DataLibraryPage dataLibraryPage = (DataLibraryPage)getPage(DataLibraryPage.NAME);
        String connectionName = dataLibraryPage.getConnectionName();
        String dataLibrary = dataLibraryPage.getDataLibraryName();

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

    protected void setActivePage(IWizardPage page) {

        WizardDialog dialog = (WizardDialog)getContainer();
        dialog.showPage(page);
    }

    protected DataLibraryPage getDataLibraryPage() {

        DataLibraryPage dataLibraryPage = (DataLibraryPage)getPage(DataLibraryPage.NAME);

        return dataLibraryPage;
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

    protected boolean isDataLibraryPageEnabled() {

        AbstractWizardPage dataLibraryPage = (AbstractWizardPage)getPage(DataLibraryPage.NAME);

        return dataLibraryPage.isEnabled();
    }

    public void init(IWorkbench workbench, IStructuredSelection selection) {

        this.initialConnectionName = null;
        this.initialDataLibrary = Preferences.getInstance().getRapidFireLibrary();
        this.initialJobName = null;

        if (selection instanceof IStructuredSelection) {
            IStructuredSelection structuredSelection = selection;
            if (!structuredSelection.isEmpty()) {
                Object element = structuredSelection.getFirstElement();
                System.out.println("==> selection: " + element);
                if (element instanceof IRapidFireResource) {
                    IRapidFireResource resource = (IRapidFireResource)element;
                    this.initialConnectionName = resource.getParentSubSystem().getConnectionName();
                    this.initialDataLibrary = resource.getDataLibrary();
                } else if (element instanceof IRapidFireSubSystem) {
                    IRapidFireSubSystem subSystem = (IRapidFireSubSystem)element;
                    this.initialConnectionName = subSystem.getConnectionName();
                } else if (SystemConnectionHelper.isFilterReference(element)) {
                    Object subSystem = SystemConnectionHelper.getSubSystemOfFilterReference(element);
                    if (subSystem instanceof IRapidFireSubSystem) {
                        this.initialConnectionName = ((IRapidFireSubSystem)subSystem).getConnectionName();
                    }
                }

                if (element instanceof IRapidFireChildResource) {
                    IRapidFireChildResource<?> resource = (IRapidFireChildResource<?>)element;
                    this.initialJobName = resource.getParentJob().getName();
                } else if (element instanceof IRapidFireJobResource) {
                    IRapidFireJobResource resource = (IRapidFireJobResource)element;
                    this.initialJobName = resource.getName();
                }
            }
        }

        return;
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

    @Override
    public IWizardPage getNextPage(IWizardPage page) {

        IWizardPage nextPage = super.getNextPage(page);
        if (nextPage == null) {
            return null;
        }

        if (nextPage instanceof AbstractWizardPage) {
            AbstractWizardPage nextAbstractWizardPage = (AbstractWizardPage)nextPage;
            if (nextAbstractWizardPage.isEnabled()) {
                return nextAbstractWizardPage;
            } else {
                return getNextPage(nextAbstractWizardPage);
            }
        }

        return null;
    }

    @Override
    public IWizardPage getPreviousPage(IWizardPage page) {

        IWizardPage previousPage = super.getPreviousPage(page);
        if (previousPage == null) {
            return null;
        }

        if (previousPage instanceof AbstractWizardPage) {
            AbstractWizardPage previousAbstractWizardPage = (AbstractWizardPage)previousPage;
            if (!previousAbstractWizardPage.isEnabled()) {
                return previousAbstractWizardPage;
            } else {
                return getPreviousPage(previousAbstractWizardPage);
            }
        }

        return null;
    }

    public void setPageEnablement(String pageId, boolean enabled) {

        if (enabled) {
            showPage(pageId);
        } else {
            hidePage(pageId);
        }
    }

    private void hidePage(String name) {

        pagesVisibility.put(name, Boolean.FALSE);

        IWizardPage page = getPage(name);
        if (isPageCompletelyInitialized(page)) {
            ((AbstractWizardPage)page).setEnabled(false);
        }
    }

    private void showPage(String name) {

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
                showPage(entry.getKey());
            } else {
                hidePage(entry.getKey());
            }
        }

    }

    private boolean isPageCompletelyInitialized(IWizardPage page) {
        return page != null && page.getControl() != null;
    }
}
