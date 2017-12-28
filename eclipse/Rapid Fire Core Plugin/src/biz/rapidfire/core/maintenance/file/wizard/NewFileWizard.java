/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.file.wizard;

import java.util.Vector;

import org.eclipse.jface.dialogs.PageChangedEvent;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.helpers.StringHelper;
import biz.rapidfire.core.maintenance.file.FileValues;
import biz.rapidfire.core.maintenance.library.LibraryValues;
import biz.rapidfire.core.maintenance.librarylist.LibraryListValues;
import biz.rapidfire.core.maintenance.wizard.AbstractNewWizard;
import biz.rapidfire.core.maintenance.wizard.AbstractWizardPage;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.Status;

public class NewFileWizard extends AbstractNewWizard {

    public NewFileWizard() {
        setWindowTitle(Messages.Wizard_Title_New_File_wizard);
        setNeedsProgressMonitor(false);
    }

    @Override
    public void addPages() {
        super.addPages(); // Adds the data library page, if necessary

        FileValues fileValues = FileValues.createInitialized();
        LibraryValues libraryValues = LibraryValues.createInitialized();
        LibraryListValues libraryListValues = LibraryListValues.createInitialized();

        addPage(new FilePage(fileValues));
        addPage(new AreaPage());
        addPage(new CommandPage());
        addPage(new ConversionPage());
    }

    @Override
    public void pageChanged(PageChangedEvent event) {

        try {

            if (event.getSelectedPage() instanceof FilePage) {
                FilePage page = (FilePage)event.getSelectedPage();
                if (StringHelper.isNullOrEmpty(page.getJobName())) {
                    page.setJobName(getJobName());
                    if (getSubSystem() != null) {
                        IRapidFireJobResource[] allJobs = getSubSystem().getJobs(getDataLibrary(), getShell());
                        if (allJobs != null) {
                            Vector<String> jobNames = new Vector<String>();
                            for (IRapidFireJobResource job : allJobs) {
                                if (Status.RDY.equals(job.getStatus())) {
                                    jobNames.addElement(job.getName());
                                }
                            }
                            page.setJobNames(jobNames.toArray(new String[jobNames.size()]));
                        }
                    }
                }
            }

        } catch (Exception e) {
            RapidFireCorePlugin.logError("*** Could not set page values of 'FilePage' ***", e); //$NON-NLS-1$
        }

        super.pageChanged(event);
    }

    @Override
    public boolean canFinish() {

        for (int i = 0; i < getPageCount(); i++) {
            AbstractWizardPage page = (AbstractWizardPage)getPages()[i];
            if (!page.isPageComplete()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean performFinish() {
        // TODO Auto-generated method stub
        return false;
    }

}
