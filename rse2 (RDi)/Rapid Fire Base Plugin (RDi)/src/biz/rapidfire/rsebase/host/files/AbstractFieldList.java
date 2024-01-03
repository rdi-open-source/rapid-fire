/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rsebase.host.files;

import java.sql.SQLException;

import com.ibm.etools.iseries.services.qsys.api.IQSYSDatabaseField;
import com.ibm.etools.iseries.services.qsys.api.IQSYSFileField;
import com.ibm.etools.iseries.services.qsys.api.IQSYSFileRecordFormat;
import com.ibm.etools.iseries.services.qsys.api.IQSYSObject;
import com.ibm.etools.iseries.subsystems.qsys.api.IBMiConnection;
import com.ibm.etools.iseries.subsystems.qsys.objects.QSYSRemoteDatabaseFile;
import com.ibm.etools.iseries.subsystems.qsys.objects.QSYSRemoteDisplayFile;
import com.ibm.etools.iseries.subsystems.qsys.objects.QSYSRemoteFile;
import com.ibm.etools.iseries.subsystems.qsys.objects.QSYSRemotePrinterFile;

public abstract class AbstractFieldList {

    private IBMiConnection connection;

    protected void initialize(String connectionName) throws Exception {

        connection = IBMiConnection.getConnection(connectionName);

        IQSYSObject object = connection.getObject(getLibrary(), getFile(), "*FILE", null);
        if (object instanceof QSYSRemoteDatabaseFile) {
            QSYSRemoteDatabaseFile databaseFile = (QSYSRemoteDatabaseFile)object;
            loadDatabaseFileFields(databaseFile);
        } else if (object instanceof QSYSRemoteDisplayFile) {
            QSYSRemoteFile displayFile = (QSYSRemoteFile)object;
            loadDisplayAndPrinterFileFields(displayFile);
        } else if (object instanceof QSYSRemotePrinterFile) {
            QSYSRemoteFile printerFile = (QSYSRemoteFile)object;
            loadDisplayAndPrinterFileFields(printerFile);
        } else {
            throw new SQLException("File not found: " + getLibrary() + "/" + getFile());
        }
    }

    private void loadDisplayAndPrinterFileFields(QSYSRemoteFile file) throws Exception {

        setLibrary(file.getLibrary());
        setRecordFormat(resolveRecordFormat(file, getRecordFormat()));

        IQSYSFileField[] qsysFields = connection.listFields(getLibrary(), getFile(), getRecordFormat(), null);
        for (IQSYSFileField qsysField : qsysFields) {
            AbstractField field = produceField(qsysField);
            addField(field);
        }
    }

    private void loadDatabaseFileFields(QSYSRemoteDatabaseFile file) throws Exception {
        setLibrary(file.getLibrary());
        setRecordFormat(resolveRecordFormat(file, getRecordFormat()));

        IQSYSDatabaseField[] qsysFields = connection.getQSYSObjectSubSystem().listDatabaseFields(file.getRecordFormat(null), null);
        for (IQSYSDatabaseField qsysField : qsysFields) {
            AbstractField field = produceField(qsysField);
            addField(field);
        }
    }

    private String resolveRecordFormat(QSYSRemoteFile file, String recordFormat) {
        try {
            IQSYSFileRecordFormat tRecordFormat = file.getRecordFormat(recordFormat, null);
            return tRecordFormat.getName();
        } catch (Exception e) {
            throw new RuntimeException(produceMessage("Record format '" + recordFormat + "' of file '" + file.getLibrary() + "/" + file.getName()
                + "' not found."));
        }
    }

    private AbstractField produceField(IQSYSFileField qsysField) {

        AbstractField field = createEmptyField();
        initializeField(field, qsysField);

        return field;
    }

    private void initializeField(AbstractField field, IQSYSFileField qsysField) {

        field.setName(qsysField.getName());
        field.setText(qsysField.getDescription());
        field.setType(qsysField.getDataType());
        field.setLength(qsysField.getLength());
        field.setDecimalPosition(qsysField.getDecimalPosition());
        field.setNumeric(qsysField.isNumeric());

        if (qsysField instanceof IQSYSDatabaseField) {
            IQSYSDatabaseField tDatabaseField = (IQSYSDatabaseField)qsysField;
            field.setAbsoluteReferencedField(tDatabaseField.getReferencedField());
        }
    }

    protected abstract AbstractField createEmptyField();

    protected abstract void setLibrary(String library);

    protected abstract void setRecordFormat(String resolveRecordFormat);

    public abstract String getLibrary();

    public abstract String getFile();

    public abstract String getRecordFormat();

    protected abstract void addField(AbstractField field);

    protected abstract String produceMessage(String text);

}
