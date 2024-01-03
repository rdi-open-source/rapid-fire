/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.subsystem.resources;

import biz.rapidfire.core.model.IRapidFireConversionResource;

public class RapidFireConversionResourceDelegate implements Comparable<IRapidFireConversionResource> {

    private String dataLibrary;
    private String job;
    private int position;
    private String fieldToConvert;
    private String newFieldName;
    private String[] conversions;

    public RapidFireConversionResourceDelegate(String dataLibrary, String job, int position, String fieldToConvert) {

        this.dataLibrary = dataLibrary;
        this.job = job;
        this.position = position;
        this.fieldToConvert = fieldToConvert;
    }

    /*
     * IRapidFireResource methods
     */

    public String getDataLibrary() {
        return dataLibrary;
    }

    /*
     * IRapidFireFileResource methods
     */

    public String getJob() {
        return job;
    }

    public int getPosition() {
        return position;
    }

    public String getFieldToConvert() {
        return fieldToConvert;
    }

    public String getNewFieldName() {
        return newFieldName;
    }

    public void setNewFieldName(String fieldName) {
        this.newFieldName = fieldName;
    }

    public String[] getConversions() {
        return conversions;
    }

    public void setConversions(String[] conversions) {
        this.conversions = conversions;
    }

    public int compareTo(IRapidFireConversionResource resource) {

        if (resource == null) {
            return 1;
        }

        int result = resource.getDataLibrary().compareTo(getDataLibrary());
        if (result != 0) {
            return result;
        }

        result = resource.getJob().compareTo(getJob());
        if (result != 0) {
            return result;
        }

        if (position > resource.getPosition()) {
            return 1;
        } else if (position < resource.getPosition()) {
            return -1;
        }

        return getFieldToConvert().compareTo(resource.getFieldToConvert());
    }

    @Override
    public String toString() {
        return getFieldToConvert();
    }

}
