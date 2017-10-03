/*******************************************************************************
 * Copyright (c) 2012-2017 iSphere Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "biz.rapidfire.core.messages"; //$NON-NLS-1$

    public static final String EMPTY = ""; //$NON-NLS-1$
    public static final String SPACE = " "; //$NON-NLS-1$

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    // Dialog titles
    public static String Specify_a_filter;

    // Dialog labels
    public static String Name_colon;
    public static String Library_colon;

    // Property labels
    public static String Batch_job_name;
    public static String Batch_job_user;
    public static String Batch_job_number;

    public static String Tooltip_Batch_job_name;
    public static String Tooltip_Batch_job_user;
    public static String Tooltip_Batch_job_number;

    // Error messages
    public static String Library_name_A_is_not_valid;

    private Messages() {
    }
}
