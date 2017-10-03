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
    public static String Add_Rapid_Fire_Instance;
    public static String Instance_Filter_dots;

    public static String Tooltip_Add_Rapid_Fire_Instance;

    /*
     * Error messages
     */
    public static String Instance_A_exists;
    public static String The_library_name_must_be_specified;

    /*
     * Dialog titles
     */
    public static String Rapid_Fire_Instance_Filter;
    public static String Create_a_new_filter_to_list_Rapid_Fire_instances;
    public static String Change_Rapid_Fire_filter;
    public static String My_Rapid_Fire;

    /*
     * Filter types
     */
    public static String Instance_Filter_type;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
