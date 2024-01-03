/*******************************************************************************
 * Copyright (c) 2017-2018 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.file.wizard.model;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.helpers.RapidFireHelper;
import biz.rapidfire.core.helpers.StringHelper;
import biz.rapidfire.core.host.files.Field;
import biz.rapidfire.core.host.files.FieldList;
import biz.rapidfire.core.maintenance.area.shared.Ccsid;
import biz.rapidfire.core.maintenance.command.shared.CommandType;
import biz.rapidfire.core.maintenance.conversion.ConversionManager;
import biz.rapidfire.core.maintenance.conversion.FieldConversions;
import biz.rapidfire.core.maintenance.conversion.shared.NewFieldName;
import biz.rapidfire.core.maintenance.file.shared.ConversionProgram;
import biz.rapidfire.core.maintenance.file.shared.FileType;
import biz.rapidfire.core.maintenance.wizard.model.WizardDataModel;
import biz.rapidfire.core.model.IRapidFireLibraryResource;
import biz.rapidfire.core.model.QualifiedProgramName;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;
import biz.rapidfire.core.subsystem.IRapidFireSubSystem;
import biz.rapidfire.rsebase.helpers.SystemConnectionHelper;

public class FileWizardDataModel extends WizardDataModel {

    // File page
    private int position;
    private String fileName;
    private FileType fileType;
    private QualifiedProgramName qualifiedCopyProgramName;
    private QualifiedProgramName qualifiedConversionProgramName;

    // Area page
    private String areaName;
    private String libraryName;
    private String libraryListName;
    private String libraryCcsid;
    private String commandExtension;

    // Command page
    private CommandType commandType;
    private int sequence;
    private String command;

    // Conversion page
    private String fieldToConvertName;
    private String newFieldName;
    private FieldConversions conversions;

    // File dependent resources
    private Field[] fields;
    private String sourceFieldieldsPrefix;
    private String targetFieldieldsPrefix;

    public static FileWizardDataModel createInitialized() {

        FileWizardDataModel model = new FileWizardDataModel();

        model.initialize();

        model.qualifiedCopyProgramName = new QualifiedProgramName(EMPTY, EMPTY);
        model.qualifiedConversionProgramName = new QualifiedProgramName(EMPTY, EMPTY);

        // File page
        model.setPosition(0);
        model.setFileName(EMPTY);
        model.setFileType(FileType.PHYSICAL);
        model.setCopyProgramName(EMPTY);
        model.setConversionProgramLibraryName(EMPTY);
        model.setCopyProgramName(EMPTY);
        model.setConversionProgramLibraryName(EMPTY);

        // Area page
        model.setAreaName(EMPTY);
        model.setLibraryName(EMPTY);
        model.setLibraryListName(EMPTY);
        model.setLibraryCcsid(Ccsid.JOB.label());
        model.setCommandExtension(EMPTY);

        // Command page
        model.setCommandType(CommandType.COMPILE);
        model.setSequence(CommandType.COMPILE.defaultSequence());
        model.setCommand(EMPTY);

        // Conversion page
        model.setFieldToConvertName(EMPTY);
        model.setNewFieldName(NewFieldName.NONE.label());
        model.setConversions(new FieldConversions());

        return model;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {

        if (!hasFileChanged(fileName)) {
            return;
        }

        this.fileName = fileName;

        clearFileDependantResources();
    }

    public FileType getFileType() {
        return fileType;
    }

    public String getFileTypeForUI() {
        return fileType.label();
    }

    public void setFileType(FileType fileType) {

        if (!hasFileTypeChanged(fileType)) {
            return;
        }

        this.fileType = fileType;

        clearFileDependantResources();
    }

    public void setFileTypeFromUI(String fileType) {

        if (!hasFileTypeChanged(FileType.find(fileType))) {
            return;
        }

        this.fileType = FileType.find(fileType);

        clearFileDependantResources();
    }

    public QualifiedProgramName getQualifiedCopyProgramName() {
        return qualifiedCopyProgramName;
    }

    public String getCopyProgramName() {
        return qualifiedCopyProgramName.getName();
    }

    public void setCopyProgramName(String programName) {
        this.qualifiedCopyProgramName.setName(programName);
    }

    public String getCopyProgramLibraryName() {
        return qualifiedCopyProgramName.getLibrary();
    }

    public void setCopyProgramLibraryName(String libraryName) {
        this.qualifiedCopyProgramName.setLibrary(libraryName);
    }

    public QualifiedProgramName getQualifiedConversionProgramName() {
        return qualifiedConversionProgramName;
    }

    public String getConversionProgramName() {
        return qualifiedConversionProgramName.getName();
    }

    public void setConversionProgramName(String programName) {
        this.qualifiedConversionProgramName.setName(programName);
    }

    public String getConversionProgramLibraryName() {
        return qualifiedConversionProgramName.getLibrary();
    }

    public void setConversionProgramLibraryName(String libraryName) {
        this.qualifiedConversionProgramName.setLibrary(libraryName);
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {

        if (!hasAreaChanged(areaName)) {
            return;
        }

        this.areaName = areaName;

        clearFileDependantResources();
    }

    public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    public String getLibraryListName() {
        return libraryListName;
    }

    public void setLibraryListName(String libraryListName) {
        this.libraryListName = libraryListName;
    }

    public String getLibraryCcsid() {
        return libraryCcsid;
    }

    public void setLibraryCcsid(String libraryCcsid) {
        this.libraryCcsid = libraryCcsid;
    }

    public String getCommandExtension() {
        return commandExtension;
    }

    public void setCommandExtension(String commandExtension) {
        this.commandExtension = commandExtension;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public String getCommandTypeForUI() {
        return commandType.label();
    }

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }

    public void setCommandTypeFromUI(String commandType) {
        this.commandType = CommandType.find(commandType);
    }

    public boolean hasCommand() {

        if (StringHelper.isNullOrEmpty(getCommand())) {
            return false;
        }

        return true;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getFieldToConvertName() {
        return fieldToConvertName;
    }

    public void setFieldToConvertName(String fieldToConvertName) {
        this.fieldToConvertName = fieldToConvertName;
    }

    public String getNewFieldName() {
        return newFieldName;
    }

    public void setNewFieldName(String newFieldName) {
        this.newFieldName = newFieldName;
    }

    public FieldConversions getConversions() {
        return conversions;
    }

    public String[] getConversionsForUI() {
        return this.conversions.getConversions();
    }

    public void setConversions(FieldConversions conversions) {
        this.conversions = conversions;
    }

    public void setConversionsFromUI(String[] conversions) {
        this.conversions.setConversions(conversions);
    }

    public boolean hasConversions() {

        if (StringHelper.isNullOrEmpty(getFieldToConvertName())) {
            return false;
        }

        return true;
    }

    public Field[] getFields() {

        if (fields == null) {
            fields = loadFields();
        }

        return fields;
    }

    public String getSourceFieldsPrefix() {

        // if (sourceFieldieldsPrefix == null) {
        sourceFieldieldsPrefix = loadSourceFieldPrefix();
        // }

        return sourceFieldieldsPrefix;
    }

    public String getTargetFieldsPrefix() {

        // if (targetFieldieldsPrefix == null) {
        targetFieldieldsPrefix = loadTargetFieldPrefix();
        // }

        return targetFieldieldsPrefix;
    }

    private boolean hasFileChanged(String newFileName) {

        if (this.fileName == null || !this.fileName.equals(newFileName)) {
            return true;
        }

        return false;
    }

    private boolean hasFileTypeChanged(FileType newFileType) {

        if (this.fileType == null || !this.fileType.equals(newFileType)) {
            return true;
        }

        return false;
    }

    private boolean hasAreaChanged(String newAreaName) {

        if (this.areaName == null || !this.areaName.equals(newAreaName)) {
            return true;
        }

        return false;
    }

    private void clearFileDependantResources() {

        this.fields = null;
        this.sourceFieldieldsPrefix = null;
        this.targetFieldieldsPrefix = null;
    }

    private Field[] loadFields() {

        IRapidFireSubSystem subSystem = (IRapidFireSubSystem)SystemConnectionHelper.getSubSystem(getConnectionName(), IRapidFireSubSystem.class);
        if (subSystem == null) {
            return null;
        }

        Shell shell = Display.getCurrent().getActiveShell();

        try {

            IRapidFireLibraryResource libraryResource = getLibrary(libraryName);

            FieldList fieldList = new FieldList(getConnectionName(), fileName, libraryResource.getName());

            return fieldList.getFields();

        } catch (Throwable e) {
            MessageDialog.openError(shell, Messages.E_R_R_O_R, ExceptionHelper.getLocalizedMessage(e));
        }

        return null;
    }

    private String loadSourceFieldPrefix() {

        loadFieldprefixes();

        return sourceFieldieldsPrefix;
    }

    private String loadTargetFieldPrefix() {

        loadFieldprefixes();

        return targetFieldieldsPrefix;
    }

    private void loadFieldprefixes() {

        Shell shell = Display.getCurrent().getActiveShell();

        try {

            IRapidFireLibraryResource libraryResource = getLibrary(libraryName);
            String conversionProgram = getConversionProgramName();
            String fileName = getFileName();
            String fromLibrary = libraryResource.getDataLibrary();
            String shadowLibrary = libraryResource.getShadowLibrary();

            ConversionManager manager = new ConversionManager(JDBCConnectionManager.getInstance().getConnectionForRead(getConnectionName(),
                getDataLibraryName()));

            boolean isConversionProgram;
            if (ConversionProgram.NONE.label().equals(conversionProgram)) {
                isConversionProgram = false;
            } else {
                isConversionProgram = true;
            }

            if (!RapidFireHelper.checkFile(SystemConnectionHelper.getSystem(getConnectionName()), fromLibrary, fileName)) {
                sourceFieldieldsPrefix = "File not found.";
            } else {
                sourceFieldieldsPrefix = manager.getSourceFilePrefix(isConversionProgram, fromLibrary, fileName, shadowLibrary, fileName);
            }

            if (!RapidFireHelper.checkFile(SystemConnectionHelper.getSystem(getConnectionName()), shadowLibrary, fileName)) {
                targetFieldieldsPrefix = "File not found.";
            } else {
                targetFieldieldsPrefix = manager.getTargetFilePrefix(isConversionProgram, fromLibrary, fileName, shadowLibrary, fileName);
            }

        } catch (Throwable e) {
            MessageDialog.openError(shell, Messages.E_R_R_O_R, ExceptionHelper.getLocalizedMessage(e));
        }
    }
}
