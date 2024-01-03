/*******************************************************************************
 * Copyright (c) 2017-2018 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.host.files;

import biz.rapidfire.rsebase.host.files.AbstractField;

/**
 * Represents a field of a file.
 * 
 * @author Thomas Raddatz
 */
public class Field extends AbstractField implements Comparable<Field> {

    private String name = null;

    private String text = null;

    private String type = null;

    private Integer length = null;

    private Integer decimalPosition = null;

    private String referencedField = null;

    private String referencedFile = null;

    private String referencedLibrary = null;

    private String referencedRecordFormat = null;

    private boolean isNumeric;

    @Override
    protected void setNumeric(boolean isNumeric) {
        this.isNumeric = isNumeric;
    }

    @Override
    protected void setName(String aName) {
        name = aName;
    }

    public String getName() {
        return name;
    }

    @Override
    protected void setText(String aText) {
        text = aText;
    }

    public String getText() {
        return text;
    }

    @Override
    protected void setType(char aType) {
        type = String.valueOf(aType);
    }

    public String getType() {
        return type;
    }

    @Override
    protected void setLength(Integer aLength) {
        length = aLength;
    }

    public Integer getLength() {
        return length;
    }

    @Override
    protected void setDecimalPosition(Integer aDecimalPosition) {
        decimalPosition = aDecimalPosition;
    }

    public Integer getDecimalPosition() {
        return decimalPosition;
    }

    @Override
    protected void setAbsoluteReferencedField(String aReferencedField) {

        if (aReferencedField == null) {
            referencedLibrary = null;
            referencedFile = null;
            referencedRecordFormat = null;
            referencedField = null;
            return;
        }

        String[] tParts = aReferencedField.split("[ ]+");

        if (tParts.length >= 1) {
            referencedLibrary = tParts[0];
        }
        if (tParts.length >= 2) {
            referencedFile = tParts[1];
        }
        if (tParts.length >= 3) {
            referencedRecordFormat = tParts[2];
        }
        if (tParts.length >= 4) {
            referencedField = tParts[3];
        }
    }

    public String getReferencedField() {
        return referencedField;
    }

    public String getReferencedFile() {
        return referencedFile;
    }

    public String getReferencedLibrary() {
        return referencedLibrary;
    }

    public String getReferencedRecordFormat() {
        return referencedRecordFormat;
    }

    public boolean isNumeric() {
        return isNumeric;
    }

    public boolean isInteger() {
        return "B".equals(getType());
    }

    @Override
    public String toString() {

        StringBuilder buffer = new StringBuilder();

        buffer.append(getName());
        buffer.append(" :"); //$NON-NLS-1$
        buffer.append(getType());
        buffer.append("("); //$NON-NLS-1$
        buffer.append(getLength());
        if (isNumeric() && !isInteger()) {
            buffer.append(","); //$NON-NLS-1$
            buffer.append(getDecimalPosition());
        }
        buffer.append(")"); //$NON-NLS-1$

        return buffer.toString();
    }

    public int compareTo(Field field) {

        if (field == null || field.getName() == null) {
            return 1;
        } else if (getName() == null) {
            return -1;
        }

        return getName().compareTo(field.getName());
    }
}
