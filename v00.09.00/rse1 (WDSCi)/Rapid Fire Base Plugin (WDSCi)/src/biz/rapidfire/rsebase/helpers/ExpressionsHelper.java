/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rsebase.helpers;

/**
 * Compile dummy as a replacement for
 * 'biz.rapidfire.rsebase.helpers.ExpressionsHelper', which is not available
 * for WDSCi.
 */
public class ExpressionsHelper {

    public static Object getSelection(Object evaluationContext) {
        throw new RuntimeException("Method 'getSelection()' must not be called for WDSCi.");
    }
}
