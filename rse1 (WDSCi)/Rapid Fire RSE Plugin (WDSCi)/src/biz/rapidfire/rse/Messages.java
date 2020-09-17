/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "biz.rapidfire.rse.messages"; //$NON-NLS-1$

    public static final String EMPTY = ""; //$NON-NLS-1$
    public static final String SPACE = " "; //$NON-NLS-1$

    public static String E_R_R_O_R;

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
    public static String DataLibrary_name;
    public static String Job_name;
    public static String File_name;
    public static String Description;
    public static String Batch_job;
    public static String Create_environment;
    public static String Job_queue;
    public static String Job_queue_library;
    public static String Cancel_ASP_threshold_exceeds;
    public static String Status;
    public static String Phase;
    public static String Error;
    public static String Error_text;
    public static String Position;
    public static String FileType;
    public static String Copy_program_name;
    public static String Copy_program_library;
    public static String Conversion_program_name;
    public static String Conversion_program_library;
    public static String Library_name;
    public static String ShadowLibrary_name;
    public static String Library_list_name;
    public static String User_name;
    public static String NotificationType;
    public static String Area_name;
    public static String Area_library;
    public static String Area_library_list;
    public static String Area_library_CCSID;
    public static String Area_command_extension;
    public static String Command_type;
    public static String Command_sequence;
    public static String Command_command;

    public static String Tooltip_DataLibrary_name;
    public static String Tooltip_Job_name;
    public static String Tooltip_File_name;
    public static String Tooltip_Description;
    public static String Tooltip_Batch_job;
    public static String Tooltip_Create_environment;
    public static String Tooltip_Job_queue;
    public static String Tooltip_Job_queue_library;
    public static String Tooltip_Cancel_ASP_threshold_exceeds;
    public static String Tooltip_Status;
    public static String Tooltip_Phase;
    public static String Tooltip_Error;
    public static String Tooltip_Error_text;
    public static String Tooltip_Position;
    public static String Tooltip_FileType;
    public static String Tooltip_Copy_program_name;
    public static String Tooltip_Copy_program_library;
    public static String Tooltip_Conversion_program_name;
    public static String Tooltip_Conversion_program_library;
    public static String Tooltip_Library_name;
    public static String Tooltip_ShadowLibrary_name;
    public static String Tooltip_Library_list_name;
    public static String Tooltip_User_name;
    public static String Tooltip_NotificationType;
    public static String Tooltip_Area_name;
    public static String Tooltip_Area_library;
    public static String Tooltip_Area_library_list;
    public static String Tooltip_Area_library_CCSID;
    public static String Tooltip_Area_command_extension;
    public static String Tooltip_Command_type;
    public static String Tooltip_Command_sequence;
    public static String Tooltip_Command_command;

    /*
     * Filter types
     */
    public static String Rapid_Fire_filter_type;

    /*
     * Resource types
     */
    public static String Resource_Rapid_Fire_Job;
    public static String Resource_Rapid_Fire_File;
    public static String Resource_Rapid_Fire_Library;
    public static String Resource_Rapid_Fire_Notification;
    public static String Resource_Rapid_Fire_Area;
    public static String Resource_Rapid_Fire_Conversion;
    public static String Resource_Rapid_Fire_Command;

    public static String NodeText_Files;
    public static String NodeText_LibraryLists;
    public static String NodeText_Libraries;
    public static String NodeText_Notifications;
    public static String NodeText_Areas;
    public static String NodeText_Conversions;
    public static String NodeText_Commands;

    public static String NodeType_Files;
    public static String NodeType_LibraryLists;
    public static String NodeType_Libraries;
    public static String NodeType_Notifications;
    public static String NodeType_Areas;
    public static String NodeType_Conversions;
    public static String NodeType_Commands;

    // Action labels
    public static String ActionLabel_New_Job;
    public static String ActionLabel_New_File;
    public static String ActionLabel_New_Library;
    public static String ActionLabel_New_Library_List;
    public static String ActionLabel_New_Notification;
    public static String ActionLabel_New_Area;
    public static String ActionLabel_New_Conversion;
    public static String ActionLabel_New_Command;

    public static String ActionTooltip_New_Job;
    public static String ActionTooltip_New_File;
    public static String ActionTooltip_New_Library;
    public static String ActionTooltip_New_Library_List;
    public static String ActionTooltip_New_Notification;
    public static String ActionTooltip_New_Area;
    public static String ActionTooltip_New_Conversion;
    public static String ActionTooltip_New_Command;

    private Messages() {
    }

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    public static String bindParameters(String message, Object... values) {

        List<Object> bindings = new LinkedList<Object>();
        for (Object value : values) {
            bindings.add(value);
        }

        return bind(message, bindings.toArray());
    }
}
