/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rsebase.host.files;

import java.sql.SQLException;

import org.eclipse.swt.widgets.Display;

import com.ibm.etools.iseries.core.api.ISeriesConnection;
import com.ibm.etools.iseries.core.api.ISeriesDatabaseField;
import com.ibm.etools.iseries.core.api.ISeriesDisplayFile;
import com.ibm.etools.iseries.core.api.ISeriesField;
import com.ibm.etools.iseries.core.api.ISeriesFile;
import com.ibm.etools.iseries.core.api.ISeriesObject;
import com.ibm.etools.iseries.core.api.ISeriesRecord;

public abstract class AbstractFieldList {

    private ISeriesConnection connection;

    protected void initialize(String connectionName) throws Exception {

        connection = ISeriesConnection.getConnection(connectionName);

        ISeriesObject[] tObjects = connection.listObjects(Display.getCurrent().getActiveShell(), getLibrary(), getFile(), new String[] { "*FILE" });
        if (tObjects != null && tObjects.length == 1) {
            ISeriesObject tObject = tObjects[0];
            if (tObject instanceof ISeriesFile) {
                ISeriesFile databaseFile = (ISeriesFile)tObject;
                loadFileFields(databaseFile);
            } else if (tObject instanceof ISeriesDisplayFile) {
                ISeriesFile displayFile = (ISeriesFile)tObject;
                loadFileFields(displayFile);
            } else {
                throw new SQLException("File not found: " + getLibrary() + "/" + getFile());
            }
        }
    }
    private void loadFileFields(ISeriesFile file) throws Exception {

        setLibrary(file.getLibrary());
        setRecordFormat(resolveRecordFormat(file, getRecordFormat()));

        ISeriesField[] qsysFields = connection.getISeriesFileSubSystem().listFields(null, file.getRecord(null, "*FIRST"));
        for (ISeriesField qsysField : qsysFields) {
            AbstractField field = produceField(qsysField);
            addField(field);
        }
    }

//    private void loadDisplayAndPrinterFileFields(ISeriesFile file) throws Exception {
//
//        setLibrary(file.getLibrary());
//        setRecordFormat(resolveRecordFormat(file, getRecordFormat()));
//
//        ISeriesField[] qsysFields = connection.getISeriesFileSubSystem().listFields(null, file.getRecord(null, "*FIRST"));
//        for (ISeriesField qsysField : qsysFields) {
//            AbstractField field = produceField(qsysField);
//            addField(field);
//        }
//    }
//
//    private void loadDatabaseFileFields(ISeriesFile file) throws Exception {
//        
//        setLibrary(file.getLibrary());
//        setRecordFormat(resolveRecordFormat(file, getRecordFormat()));
//
//        ISeriesField[] qsysFields = connection.getISeriesFileSubSystem().listFields(null, file.getRecord(null, "*FIRST"));
//        for (ISeriesField qsysField : qsysFields) {
//            AbstractField field = produceField(qsysField);
//            addField(field);
//        }
//    }

    private String resolveRecordFormat(ISeriesFile file, String recordFormat) {
        try {
            ISeriesRecord  tRecordFormat = file.getRecord(null,recordFormat);
            return tRecordFormat.getName();
        } catch (Exception e) {
            throw new RuntimeException(produceMessage("Record format '" + recordFormat + "' of file '" + file.getLibrary() + "/" + file.getName()
                + "' not found."));
        }
    }

    private AbstractField produceField(ISeriesField qsysField) {

        AbstractField field = createEmptyField();
        initializeField(field, qsysField);

        return field;
    }

    private void initializeField(AbstractField field, ISeriesField qsysField) {

        field.setName(qsysField.getName());
        field.setText(qsysField.getDescription());
        field.setType(qsysField.getDataType());
        field.setLength(qsysField.getLength());
        field.setDecimalPosition(qsysField.getDecimalPosition());
        field.setNumeric(isNumeric(qsysField));

        if (qsysField instanceof ISeriesDatabaseField) {
            ISeriesDatabaseField tDatabaseField = (ISeriesDatabaseField)qsysField;
            field.setAbsoluteReferencedField(tDatabaseField.getReferencedField());
        }
    }

    private boolean isNumeric(ISeriesField qsysField) {

        switch (qsysField.getDataType()) {
        case 'B':
        case 'F':
        case 'P':
        case 'S':
            return true;
        }

        return false;
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
