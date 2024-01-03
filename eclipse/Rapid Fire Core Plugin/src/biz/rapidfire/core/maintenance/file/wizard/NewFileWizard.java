/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.file.wizard;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.PageChangedEvent;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.dialogs.MessageDialogAsync;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.helpers.RapidFireHelper;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.area.AreaManager;
import biz.rapidfire.core.maintenance.area.AreaValues;
import biz.rapidfire.core.maintenance.area.shared.AreaKey;
import biz.rapidfire.core.maintenance.command.CommandManager;
import biz.rapidfire.core.maintenance.command.CommandValues;
import biz.rapidfire.core.maintenance.command.shared.CommandKey;
import biz.rapidfire.core.maintenance.conversion.ConversionManager;
import biz.rapidfire.core.maintenance.conversion.ConversionValues;
import biz.rapidfire.core.maintenance.conversion.shared.ConversionKey;
import biz.rapidfire.core.maintenance.file.FileManager;
import biz.rapidfire.core.maintenance.file.FileValues;
import biz.rapidfire.core.maintenance.file.shared.FileKey;
import biz.rapidfire.core.maintenance.file.wizard.model.FileWizardDataModel;
import biz.rapidfire.core.maintenance.job.shared.JobKey;
import biz.rapidfire.core.maintenance.job.wizard.JobPage;
import biz.rapidfire.core.maintenance.job.wizard.LibraryPage;
import biz.rapidfire.core.maintenance.wizard.AbstractNewWizard;
import biz.rapidfire.core.maintenance.wizard.AbstractWizardPage;
import biz.rapidfire.core.maintenance.wizard.DataLibraryPage;
import biz.rapidfire.core.model.IRapidFireFileResource;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.dao.IJDBCConnection;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;
import biz.rapidfire.core.preferences.Preferences;
import biz.rapidfire.core.subsystem.IRapidFireSubSystem;
import biz.rapidfire.rsebase.helpers.SystemConnectionHelper;

import com.ibm.as400.access.AS400;

public class NewFileWizard extends AbstractNewWizard<FileWizardDataModel> {

    public NewFileWizard() {
        super(FileWizardDataModel.createInitialized());

        setWindowTitle(Messages.Wizard_Title_New_File_wizard);
        setNeedsProgressMonitor(false);
    }

    @Override
    public void addPages() {
        super.addPages(); // Adds the data library page, if necessary

        addPage(new FilePage(model));
        addPage(new AreaPage(model));
        addPage(new CommandPage(model));
        addPage(new ConversionPage(model));
    }

    @Override
    public void pageChanged(PageChangedEvent event) {
        super.pageChanged(event);
    }

    protected void updatePageValues(AbstractWizardPage page) {

        if (page instanceof FilePage) {
            FilePage filePage = (FilePage)page;
            filePage.setJobNames(getJobNames(model.getJobs()));
        } else if (page instanceof AreaPage) {
            AreaPage areaPage = (AreaPage)page;
            areaPage.setLibraryNames(getLibraryNames(model.getLibraries()));
            areaPage.setLibraryListNames(getLibraryListNames(model.getLibraryLists()));
        } else if (page instanceof ConversionPage) {
            ConversionPage conversionPage = (ConversionPage)page;
            conversionPage.setFieldsToConvert(getFieldNames(model.getFields()));
            conversionPage.setSourceFieldsPrefix(model.getSourceFieldsPrefix());
            conversionPage.setTargetFieldsPrefix(model.getTargetFieldsPrefix());
        }
    }

    @Override
    protected void updatePageEnablement(AbstractWizardPage page) {

        IRapidFireJobResource job = null;
        AS400 as400 = SystemConnectionHelper.getSystemChecked(model.getConnectionName());
        if (as400 != null) {
            StringBuilder errorMessage = new StringBuilder();
            if (RapidFireHelper.checkRapidFireLibrary(getShell(), as400, model.getDataLibraryName(), errorMessage)) {
                job = model.getJob();
            }
        }

        if (job != null && job.isDoCreateEnvironment()) {
            setPageEnablement(CommandPage.NAME, true);
        } else {
            setPageEnablement(CommandPage.NAME, false);
        }
    }

    @Override
    protected void prepareForDisplay(AbstractWizardPage page) {

        page.prepareForDisplay();
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

        FileManager fileManager = null;
        AreaManager areaManager = null;
        CommandManager commandManager = null;
        ConversionManager conversionManager = null;

        IJDBCConnection connection = null;

        try {

            storePreferences();

            Result result;

            result = validateDataLibrary();
            if (result.isError()) {
                setActivePage(getDataLibraryPage());
                displayError(result, DataLibraryPage.NAME);
                return false;
            }

            String connectionName = model.getConnectionName();
            String dataLibrary = model.getDataLibraryName();

            /*
             * Get JDBC connection with manual commit control (auto commit
             * disabled).
             */
            connection = JDBCConnectionManager.getInstance().getConnectionForUpdateNoAutoCommit(connectionName, dataLibrary);

            fileManager = new FileManager(connection);
            areaManager = new AreaManager(connection);

            if (model.hasConversions()) {
                conversionManager = new ConversionManager(connection);
            }

            if (model.hasCommand()) {
                commandManager = new CommandManager(connection);
            }

            result = validateFile(fileManager);
            if (result.isError()) {
                setActivePage(getFilePage());
                displayError(result, JobPage.NAME);
                return false;
            }

            result = validateArea(areaManager);
            if (result.isError()) {
                setActivePage(getAreaPage());
                displayError(result, LibraryPage.NAME);
                return false;
            }

            if (commandManager != null) {
                result = validateCommand(commandManager);
                if (result.isError()) {
                    setActivePage(getCommandPage());
                    displayError(result, LibraryPage.NAME);
                    return false;
                }
            }

            if (conversionManager != null) {
                result = validateConversions(conversionManager);
                if (result.isError()) {
                    setActivePage(getConversionPage());
                    displayError(result, LibraryPage.NAME);
                    return false;
                }
            }

            fileManager.book();
            areaManager.book();

            if (commandManager != null) {
                commandManager.book();
            }

            if (conversionManager != null) {
                conversionManager.book();
            }

            JDBCConnectionManager.getInstance().commit(connection);

            IRapidFireSubSystem subSystem = (IRapidFireSubSystem)SystemConnectionHelper.getSubSystem(connection.getConnectionName(),
                IRapidFireSubSystem.class);
            if (subSystem != null) {
                IRapidFireJobResource job = model.getJob();
                if (job != null) {
                    IRapidFireFileResource newFile = subSystem.getFile(job, model.getPosition(), getShell());
                    if (newFile != null) {
                        if (newFile.getParentNode() != null) {
                            boolean isSlowConnection = Preferences.getInstance().isSlowConnection();
                            // TODO: figure out how to get the damned parent
                            // node
                            // SystemConnectionHelper.refreshUICreated(isSlowConnection,
                            // subSystem, newFile, newFile.getParentNode());
                        }
                    }
                }
            }

            MessageDialog.openInformation(getShell(), Messages.Wizard_Title_New_File_wizard,
                Messages.bindParameters(Messages.NewFileWizard_File_A_created, model.getJobName(), model.getFileName()));

            return true;

        } catch (Exception e) {

            if (connection != null) {
                try {
                    JDBCConnectionManager.getInstance().rollback(connection);
                } catch (Exception e2) {
                    RapidFireCorePlugin.logError("*** Could not rollback connection '" + connection.getConnectionName() + "' ***", e); //$NON-NLS-1$ //$NON-NLS-2$                
                }
            }

            RapidFireCorePlugin.logError("*** Failed to execute Job wizard ***", e); //$NON-NLS-1$
            MessageDialogAsync.displayError(getShell(), ExceptionHelper.getLocalizedMessage(e));

        } finally {

            closeFilesOfManager(fileManager);
            closeFilesOfManager(areaManager);
            closeFilesOfManager(commandManager);
        }

        return true;
    }

    private FilePage getFilePage() {

        FilePage filePage = (FilePage)getPage(FilePage.NAME);

        return filePage;
    }

    private AreaPage getAreaPage() {

        AreaPage areaPage = (AreaPage)getPage(AreaPage.NAME);

        return areaPage;
    }

    private CommandPage getCommandPage() {

        CommandPage commandPage = (CommandPage)getPage(CommandPage.NAME);

        return commandPage;
    }

    private ConversionPage getConversionPage() {

        ConversionPage conversionPage = (ConversionPage)getPage(ConversionPage.NAME);

        return conversionPage;
    }

    private FileValues getFileValues() {

        FileValues fileValues = FileValues.createInitialized();
        fileValues.setKey(new FileKey(new JobKey(model.getJobName()), model.getPosition()));
        fileValues.setFileName(model.getFileName());
        fileValues.setFileType(model.getFileType().label());
        fileValues.setCopyProgramName(model.getCopyProgramName());
        fileValues.setCopyProgramLibraryName(model.getCopyProgramLibraryName());
        fileValues.setConversionProgramName(model.getConversionProgramName());
        fileValues.setConversionProgramLibraryName(model.getConversionProgramLibraryName());

        return fileValues;
    }

    private AreaValues getAreaValues() {

        AreaValues areaValues = AreaValues.createInitialized();
        areaValues.setKey(new AreaKey(new FileKey(new JobKey(model.getJobName()), model.getPosition()), model.getAreaName()));
        areaValues.setLibrary(model.getLibraryName());
        areaValues.setLibraryList(model.getLibraryListName());
        areaValues.setLibraryCcsid(model.getLibraryCcsid());
        areaValues.setCommandExtension(model.getCommandExtension());

        return areaValues;
    }

    private CommandValues getCommandValues() {

        CommandValues commandValues = CommandValues.createInitialized();
        commandValues.setKey(new CommandKey(new FileKey(new JobKey(model.getJobName()), model.getPosition()), model.getCommandType(), model
            .getSequence()));
        commandValues.setCommand(model.getCommand());

        return commandValues;
    }

    private ConversionValues getConversionValues() {

        ConversionValues conversionValues = ConversionValues.createInitialized();
        conversionValues.setKey(new ConversionKey(new FileKey(new JobKey(model.getJobName()), model.getPosition()), model.getFieldToConvertName()));
        conversionValues.setNewFieldName(model.getNewFieldName());
        conversionValues.setConversions(model.getConversions().getConversions());

        return conversionValues;
    }

    private Result validateFile(FileManager fileManager) throws Exception {

        FileValues fileValues = getFileValues();

        fileManager.openFiles();
        fileManager.initialize(MaintenanceMode.CREATE, fileValues.getKey());
        fileManager.setValues(fileValues);
        Result result = fileManager.check();

        return result;
    }

    private Result validateArea(AreaManager areaManager) throws Exception {

        AreaValues areaValues = getAreaValues();

        areaManager.openFiles();
        areaManager.initialize(MaintenanceMode.CREATE, areaValues.getKey());
        areaManager.setValues(areaValues);
        Result result = areaManager.check();

        return result;
    }

    private Result validateCommand(CommandManager commandManager) throws Exception {

        CommandValues commandValues = getCommandValues();

        commandManager.openFiles();
        commandManager.initialize(MaintenanceMode.CREATE, commandValues.getKey());
        commandManager.setValues(commandValues);
        Result result = commandManager.check();

        return result;
    }

    private Result validateConversions(ConversionManager conversionManager) throws Exception {

        ConversionValues conversionValues = getConversionValues();

        conversionManager.openFiles();
        conversionManager.initialize(MaintenanceMode.CREATE, conversionValues.getKey());
        conversionManager.setValues(conversionValues);
        Result result = conversionManager.check();

        return result;
    }

}
