/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.conversion;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.maintenance.IResourceValues;
import biz.rapidfire.core.maintenance.conversion.shared.ConversionKey;
import biz.rapidfire.core.maintenance.conversion.shared.NewFieldName;
import biz.rapidfire.core.maintenance.file.shared.FileKey;
import biz.rapidfire.core.maintenance.job.shared.JobKey;

public class ConversionValues implements IResourceValues {

    private static final String EMPTY = ""; //$NON-NLS-1$

    private ConversionKey key;
    private String newFieldName;
    private FieldConversions conversions;

    public static String[] getNewFieldNameSpecialValues() {
        return NewFieldName.labels();
    }

    public ConversionValues() {
        this.conversions = new FieldConversions();
    }

    public static ConversionValues createInitialized() {

        ConversionValues conversionValues = new ConversionValues();
        conversionValues.setKey(new ConversionKey(new FileKey(new JobKey(EMPTY), 0), EMPTY));
        conversionValues.setNewFieldName(EMPTY);
        conversionValues.setConversions(new String[] { EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY });

        return conversionValues;
    }

    public ConversionKey getKey() {
        ensureKey();
        return key;
    }

    public void setKey(ConversionKey key) {
        ensureKey();
        this.key = key;
    }

    public String getNewFieldName() {
        return newFieldName;
    }

    public void setNewFieldName(String fieldName) {
        this.newFieldName = fieldName;
    }

    public String[] getConversions() {
        return conversions.getConversions();
    }

    public void setConversions(String[] conversions) {
        this.conversions.setConversions(conversions);
    }

    public void clear() {
        setNewFieldName(null);
        setConversions(null);
    }

    private void ensureKey() {

        if (key == null) {
            key = new ConversionKey(null, null);
        }
    }

    @Override
    public ConversionValues clone() {

        try {

            ConversionValues conversionValues = (ConversionValues)super.clone();
            conversionValues.setKey((ConversionKey)getKey().clone());

            return conversionValues;

        } catch (CloneNotSupportedException e) {
            RapidFireCorePlugin.logError("*** Clone not supported. ***", e); //$NON-NLS-1$
            throw new biz.rapidfire.core.exceptions.CloneNotSupportedException(ExceptionHelper.getLocalizedMessage(e), e);
        }
    }
}
