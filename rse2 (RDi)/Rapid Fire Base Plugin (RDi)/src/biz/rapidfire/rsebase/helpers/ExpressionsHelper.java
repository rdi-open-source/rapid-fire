/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rsebase.helpers;

import org.eclipse.core.expressions.IEvaluationContext;

public class ExpressionsHelper {

    private static final String SELECTION = "selection";

    public static Object getSelection(Object evaluationContext) {
        return getVariable(evaluationContext, SELECTION);
    }

    private static Object getVariable(Object evaluationContext, String key) {
        IEvaluationContext context = (IEvaluationContext)evaluationContext;
        return context.getVariable(SELECTION);
    }

}
