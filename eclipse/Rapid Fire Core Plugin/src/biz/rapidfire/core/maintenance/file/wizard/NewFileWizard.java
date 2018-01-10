/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.file.wizard;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.eclipse.jface.dialogs.PageChangedEvent;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.helpers.StringHelper;
import biz.rapidfire.core.host.files.Field;
import biz.rapidfire.core.host.files.FieldList;
import biz.rapidfire.core.maintenance.area.AreaValues;
import biz.rapidfire.core.maintenance.command.CommandValues;
import biz.rapidfire.core.maintenance.conversion.ConversionValues;
import biz.rapidfire.core.maintenance.file.FileValues;
import biz.rapidfire.core.maintenance.wizard.AbstractNewWizard;
import biz.rapidfire.core.maintenance.wizard.AbstractWizardPage;
import biz.rapidfire.core.maintenance.wizard.DataLibraryPage;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.IRapidFireLibraryListResource;
import biz.rapidfire.core.model.IRapidFireLibraryResource;
import biz.rapidfire.core.model.Status;
import biz.rapidfire.core.subsystem.IRapidFireSubSystem;
import biz.rapidfire.rsebase.helpers.SystemConnectionHelper;

public class NewFileWizard extends AbstractNewWizard {

    public NewFileWizard() {
        setWindowTitle(Messages.Wizard_Title_New_File_wizard);
        setNeedsProgressMonitor(false);
    }

    @Override
    public void addPages() {
        super.addPages(); // Adds the data library page, if necessary

        FileValues fileValues = FileValues.createInitialized();
        AreaValues areaValues = AreaValues.createInitialized();
        CommandValues commandValues = CommandValues.createInitialized();
        ConversionValues conversionValues = ConversionValues.createInitialized();

        addPage(new FilePage(fileValues));
        addPage(new AreaPage(areaValues));
        addPage(new CommandPage(commandValues));
        addPage(new ConversionPage(conversionValues));
    }

    @Override
    public void pageChanged(PageChangedEvent event) {

        try {

            DataLibraryPage dataLibraryPage = (DataLibraryPage)getPage(DataLibraryPage.NAME);
            String connectionName = dataLibraryPage.getConnectionName();
            String dataLibraryName = dataLibraryPage.getDataLibraryName();

            if (event.getSelectedPage() instanceof FilePage) {

                FilePage filePage = (FilePage)event.getSelectedPage();
                if (StringHelper.isNullOrEmpty(filePage.getJobName())) {
                    filePage.setJobName(getInitialJobName());
                    IRapidFireSubSystem subSystem = (IRapidFireSubSystem)SystemConnectionHelper.getSubSystem(connectionName,
                        IRapidFireSubSystem.class);
                    if (subSystem != null) {
                        IRapidFireJobResource[] allJobs = subSystem.getJobs(dataLibraryName, getShell());
                        if (allJobs != null) {
                            Vector<String> jobNames = new Vector<String>();
                            for (IRapidFireJobResource job : allJobs) {
                                if (Status.RDY.equals(job.getStatus())) {
                                    jobNames.addElement(job.getName());
                                }
                            }
                            filePage.setJobNames(jobNames.toArray(new String[jobNames.size()]));
                        }
                    }
                }

                filePage.setErrorMessage(null);

            } else if (event.getSelectedPage() instanceof AreaPage) {

                AreaPage areaPage = (AreaPage)event.getSelectedPage();

                FilePage filePage = (FilePage)getPage(FilePage.NAME);
                String jobName = filePage.getJobName();

                IRapidFireSubSystem subSystem = (IRapidFireSubSystem)SystemConnectionHelper.getSubSystem(connectionName, IRapidFireSubSystem.class);
                if (subSystem != null) {
                    IRapidFireJobResource job = subSystem.getJob(dataLibraryName, jobName, getShell());
                    if (job != null) {
                        IRapidFireLibraryResource[] libraries = subSystem.getLibraries(job, getShell());
                        areaPage.setLibraryNames(getLibraryNames(libraries));
                        IRapidFireLibraryListResource[] libraryLists = subSystem.getLibraryLists(job, getShell());
                        areaPage.setLibraryListNames(getLibraryListNames(libraryLists));
                    }
                }

                areaPage.setErrorMessage(null);

            } else if (event.getSelectedPage() instanceof ConversionPage) {

                ConversionPage conversionPage = (ConversionPage)event.getSelectedPage();

                FilePage filePage = (FilePage)getPage(FilePage.NAME);
                AreaPage areaPage = (AreaPage)getPage(AreaPage.NAME);

                String jobName = filePage.getJobName();
                String fileName = filePage.getValues().getFileName();
                String libraryResourceName = areaPage.getValues().getLibrary();

                IRapidFireSubSystem subSystem = (IRapidFireSubSystem)SystemConnectionHelper.getSubSystem(connectionName, IRapidFireSubSystem.class);
                IRapidFireJobResource jobResource = subSystem.getJob(dataLibraryName, jobName, getShell());
                IRapidFireLibraryResource libraryResource = subSystem.getLibrary(jobResource, libraryResourceName, getShell());

                FieldList fieldList = new FieldList(connectionName, fileName, libraryResource.getKey().getLibrary());

                conversionPage.setFieldsToConvert(getFieldNames(fieldList.getFields()));

                filePage.setErrorMessage(null);

            }

        } catch (Exception e) {
            RapidFireCorePlugin.logError("*** Could not set page values of 'FilePage' ***", e); //$NON-NLS-1$
        }

        super.pageChanged(event);
    }

    private String[] getLibraryNames(IRapidFireLibraryResource[] libraries) {

        List<String> namesList = new LinkedList<String>();
        for (IRapidFireLibraryResource library : libraries) {
            namesList.add(library.getName());
        }

        String[] names = namesList.toArray(new String[namesList.size()]);
        Arrays.sort(names);

        return names;
    }

    private String[] getLibraryListNames(IRapidFireLibraryListResource[] libraryLists) {

        List<String> namesList = new LinkedList<String>();
        for (IRapidFireLibraryListResource libraryList : libraryLists) {
            namesList.add(libraryList.getName());
        }

        String[] names = namesList.toArray(new String[namesList.size()]);
        Arrays.sort(names);

        return names;
    }

    private String[] getFieldNames(Field[] fields) {

        List<String> fieldNames = new LinkedList<String>();
        for (Field field : fields) {
            fieldNames.add(field.getName());
        }

        String[] names = fieldNames.toArray(new String[fieldNames.size()]);
        Arrays.sort(names);

        return names;
    }

    @Override
    public boolean canFinish() {

        for (int i = 0; i < getPageCount(); i++) {
            AbstractWizardPage page = (AbstractWizardPage)getPages()[i];
            if (page.isEnabled() && !page.isPageComplete()) {
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
