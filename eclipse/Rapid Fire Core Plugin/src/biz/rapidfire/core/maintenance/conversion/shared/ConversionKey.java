/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.conversion.shared;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.maintenance.file.shared.FileKey;
import biz.rapidfire.core.model.maintenance.IResourceKey;

public class ConversionKey implements IResourceKey {

    private FileKey fileKey;
    private String fieldToConvert;

    public ConversionKey(FileKey fileKey, String fieldName) {

        this.fileKey = fileKey;
        this.fieldToConvert = fieldName;
    }

    public String getJobName() {
        return fileKey.getJobName();
    }

    public int getPosition() {
        return fileKey.getPosition();
    }

    public String getFieldToConvert() {
        return fieldToConvert;
    }

    public void setFieldToConvert(String fieldToConvert) {
        this.fieldToConvert = fieldToConvert;
    }

    @Override
    public Object clone() {
        try {

            ConversionKey conversionKey = (ConversionKey)super.clone();
            conversionKey.fileKey = (FileKey)fileKey.clone();

            return conversionKey;

        } catch (CloneNotSupportedException e) {
            RapidFireCorePlugin.logError("*** Clone not supported. ***", e); //$NON-NLS-1$
            throw new biz.rapidfire.core.exceptions.CloneNotSupportedException(ExceptionHelper.getLocalizedMessage(e), e);
        }
    }
}
