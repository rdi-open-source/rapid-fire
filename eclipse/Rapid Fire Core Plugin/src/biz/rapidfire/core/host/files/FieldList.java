/*******************************************************************************
 * Copyright (c) 2017-2018 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.host.files;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import biz.rapidfire.rsebase.host.files.AbstractField;
import biz.rapidfire.rsebase.host.files.AbstractFieldList;

/**
 * Represents the key list of a keyed physical file.
 * 
 * @author Thomas Raddatz
 */
public class FieldList extends AbstractFieldList {

    private String file = null;

    private String library = null;

    private String recordFormat = null;

    private List<AbstractField> fieldList = null;

    public FieldList(String connectionName, String file, String library) throws Exception {
        this(connectionName, file, library, "*FIRST");
    }

    public FieldList(String connectionName, String file, String library, String recordFormat) throws Exception {

        this.file = file;
        this.library = library;
        this.recordFormat = recordFormat;
        this.fieldList = new ArrayList<AbstractField>();

        initialize(connectionName);
    }

    @Override
    public String getFile() {
        return file;
    }

    @Override
    public String getLibrary() {
        return library;
    }

    @Override
    public String getRecordFormat() {
        return recordFormat;
    }

    public Field[] getFields() {

        List<Field> fields = new LinkedList<Field>();
        for (AbstractField field : fieldList) {
            fields.add((Field)field);
        }

        return fields.toArray(new Field[fields.size()]);
    }

    @Override
    protected void setLibrary(String aLibrary) {
        this.library = aLibrary;
    }

    @Override
    protected void setRecordFormat(String recordFormat) {
        this.recordFormat = recordFormat;
    }

    @Override
    protected void addField(AbstractField field) {
        this.fieldList.add(field);
    }

    @Override
    protected AbstractField createEmptyField() {
        return new Field();
    }

    @Override
    protected String produceMessage(String text) {
        return getClass().getSimpleName() + ": " + text;
    }
}
