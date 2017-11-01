/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
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

    public static final String E_R_R_O_R = "E R R O R"; //$NON-NLS-1$

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    // Dialog titles
    public static String Specify_a_filter;

    // Dialog labels
    public static String Library_colon;
    public static String Job_colon;
    public static String Status_colon;
    public static String Label_Full_generic_string;

    // Property labels
    public static String Batch_job_name;
    public static String Batch_job_user;
    public static String Batch_job_number;

    public static String Tooltip_Batch_job_name;
    public static String Tooltip_Batch_job_user;
    public static String Tooltip_Batch_job_number;

    // Error messages
    public static String Library_name_A_is_not_valid;
    public static String The_library_name_must_be_specified;
    public static String The_job_name_must_be_specified;
    public static String The_job_status_must_be_specified;

    private Messages() {
    }
}
