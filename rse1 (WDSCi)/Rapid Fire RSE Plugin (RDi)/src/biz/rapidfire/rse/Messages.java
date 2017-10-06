/*******************************************************************************
 * Copyright (c) 2012-2017 iSphere Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "biz.rapidfire.rse.messages"; //$NON-NLS-1$

    public static final String EMPTY = ""; //$NON-NLS-1$
    public static final String SPACE = " "; //$NON-NLS-1$

    public static final String E_R_R_O_R = "E R R O R"; //$NON-NLS-1$

    /*
     * DAO strings
     */

    public static String RseBaseDAO_Invalid_or_missing_connection_name_A;
    public static String RseBaseDAO_Invalid_or_missing_library_name_A;
    public static String RseBaseDAO_Connection_A_not_found;
    public static String RseBaseDAO_Failed_to_connect_to_A;

    /*
     * Menu options
     */
    public static String Add_Rapid_Fire_Filter_dots;

    /*
     * Error messages
     */
    public static String Rapid_Fire_filter_contains_invalid_values;

    /*
     * Dialog titles
     */
    public static String Rapid_Fire_Filter;
    public static String Create_a_new_filter_to_list_Rapid_Fire_jobs;
    public static String Change_Rapid_Fire_filter;
    public static String My_Rapid_Fire;

    /*
     * Dialog lables
     */

    /*
     * Dialog properties
     */
    public static String Job_name;
    public static String Description;
    public static String Batch_job;
    public static String Create_environment;
    public static String Job_queue;
    public static String Job_queue_library;
    public static String Status;
    public static String Phase;
    public static String Error;
    public static String Error_text;

    public static String Tooltip_Job_name;
    public static String Tooltip_Description;
    public static String Tooltip_Batch_job;
    public static String Tooltip_Create_environment;
    public static String Tooltip_Job_queue;
    public static String Tooltip_Job_queue_library;
    public static String Tooltip_Status;
    public static String Tooltip_Phase;
    public static String Tooltip_Error;
    public static String Tooltip_Error_text;

    /*
     * Filter types
     */
    public static String Rapid_Fire_filter_type;

    /*
     * Resource types
     */
    public static String Resource_Rapid_Fire_Job;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
