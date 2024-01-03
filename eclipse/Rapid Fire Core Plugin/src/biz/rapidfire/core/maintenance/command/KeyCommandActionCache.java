/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.command;

import biz.rapidfire.core.maintenance.AbstractKeyResourceActionCache;
import biz.rapidfire.core.model.IRapidFireCommandResource;

/**
 * This class produces the key value for the CommandActionCache.
 * 
 * <pre>
 * Form of the key:    [jobStatus], [createEnvironment], [position}, [commandType_isEmpty], [sequence_isZero]
 * Example key value:  RDY, true, 10, IS_NOT_EMPTY, IS_ZERO
 * </pre>
 * 
 * The key is composed from the attributes of the job ('status' and 'create
 * environment') plus the file ('position') and the command identifiers ('type'
 * and 'sequence'). The type is translated to IS_EMPTY or IS_NOT_EMPTY, because
 * the actual name is not relevant. The same thing applies to the sequence
 * number.
 */
public class KeyCommandActionCache extends AbstractKeyResourceActionCache {

    public KeyCommandActionCache(IRapidFireCommandResource command) {
        super(command.getParentJob(), Integer.toString(command.getPosition()), isStringValueEmpty(command.getCommandType().toString()),
            isNumericValueZero(command.getSequence()));
    }
}
