/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.conversion;

import biz.rapidfire.core.maintenance.AbstractKeyResourceActionCache;
import biz.rapidfire.core.model.IRapidFireConversionResource;

/**
 * This class produces the key value for the FileActionCache.
 * 
 * <pre>
 * Form of the key:    [jobStatus], [createEnvironment], [position], [fieldToConvert_isEmpty]
 * Example key value:  RDY, true, IS_ZERO, IS_EMPTY
 * </pre>
 * 
 * The key is composed from the attributes of the job ('status' and 'create
 * environment') plus the file ('position') and the conversion identifiers
 * ('field to convert'). The value of 'position' is translated to IS_ZERO or
 * IS_NOT_ZERO, because the actual position is not relevant. The same applies to
 * 'field to convert'.
 */
public class KeyConversionActionCache extends AbstractKeyResourceActionCache {

    public KeyConversionActionCache(IRapidFireConversionResource conversion) {
        super(conversion.getParentJob(), isNumericValueZero(conversion.getPosition()), isStringValueEmpty(conversion.getFieldToConvert()));
    }
}
