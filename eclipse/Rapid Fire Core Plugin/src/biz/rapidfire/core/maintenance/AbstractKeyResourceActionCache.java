/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance;

import biz.rapidfire.core.model.IRapidFireJobResource;

public abstract class AbstractKeyResourceActionCache {

    public static final String SEPARATOR = ", "; //$NON-NLS-1$

    public static final String IS_EMPTY = "IS_EMPTY"; //$NON-NLS-1$
    public static final String IS_NOT_EMPTY = "IS_NOT_EMPTY"; //$NON-NLS-1$

    public static final String IS_ZERO = "IS_ZERO"; //$NON-NLS-1$
    public static final String IS_NOT_ZERO = "IS_NOT_ZERO"; //$NON-NLS-1$

    public static final String IS_TRUE = "IS_TRUE"; //$NON-NLS-1$
    public static final String IS_FALSE = "IS_FALSE"; //$NON-NLS-1$

    private String value;

    public AbstractKeyResourceActionCache(IRapidFireJobResource job, String... keyExtensions) {
        produceKey(job, keyExtensions);
    }

    private void produceKey(IRapidFireJobResource job, String... keyExtensions) {

        StringBuilder buffer = new StringBuilder();

        // Add job attributes that affect the possible actions:
        // - Status
        // - Create Environment
        // These attributes also affect all other dependent objects.
        if (job != null) {
            buffer.append(job.getStatus());
            buffer.append(SEPARATOR);
            buffer.append(isBooleanValueTrue(job.isDoCreateEnvironment()));
        }

        // Add attributes that are specific to the other Rapid Fire resources,
        // such as 'Libraries', 'Library Lists', 'Files', etc.
        if (keyExtensions != null && keyExtensions.length > 0) {
            for (String keyExtension : keyExtensions) {
                buffer.append(SEPARATOR);
                buffer.append(keyExtension);
            }
        }

        value = buffer.toString();
    }

    protected static String isNumericValueZero(int value) {

        if (value == 0) {
            return IS_ZERO;
        } else {
            return IS_NOT_ZERO;
        }
    }

    protected static String isStringValueEmpty(String value) {

        if (value.trim().length() == 0) {
            return IS_EMPTY;
        } else {
            return IS_NOT_EMPTY;
        }
    }

    protected static String isBooleanValueTrue(boolean value) {

        if (value) {
            return IS_TRUE;
        } else {
            return IS_FALSE;
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getValue();
    }
}
