/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model.maintenance.conversion;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.model.maintenance.conversion.shared.ConversionKey;
import biz.rapidfire.core.model.maintenance.conversion.shared.NewFieldName;

public class ConversionValues implements Cloneable {

    private ConversionKey key;
    private String newFieldName;
    private String[] conversions;

    public static String[] getNewFieldNameSpecialValues() {
        return NewFieldName.labels();
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
        return conversions;
    }

    public void setConversions(String[] conversions) {
        this.conversions = conversions;
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
