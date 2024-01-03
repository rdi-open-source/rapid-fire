/*******************************************************************************
 * Copyright (c) 2017-2021 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "biz.rapidfire.core.messages"; //$NON-NLS-1$

    public static final String EMPTY = ""; //$NON-NLS-1$
    public static final String SPACE = " "; //$NON-NLS-1$

    public static String E_R_R_O_R;
    public static String Internal_error_colon;
    public static String Could_not_determine_the_version_of_the_Rapid_Fire_library;

    // Dialog titles
    public static String DialogTitle_Specify_a_filter;
    public static String DialogTitle_Job;
    public static String DialogTitle_File;
    public static String DialogTitle_Library;
    public static String DialogTitle_Library_List;
    public static String DialogTitle_Notification;
    public static String DialogTitle_Area;
    public static String DialogTitle_Command;
    public static String DialogTitle_Activity_Schedule;
    public static String DialogTitle_Transfer_Rapid_Fire_library;
    public static String DialogTitle_Delete_Object;
    public static String DialogTitle_Sign_On;
    public static String DialogTitle_Test_Job_A;
    public static String DialogTitle_Start_Job_A;
    public static String DialogTitle_End_Job_A;
    public static String DialogTitle_Reset_Job_A;
    public static String DialogTitle_Reset_Job_A_after_abortion;
    public static String DialogTitle_Job_Error_Message;
    public static String DialogFile_Copy_Program_Generator;
    public static String DialogTitle_Select_area;
    public static String DialogTitle_Reapply_all_changes;
    public static String DialogTitle_Select_conversion_program;
    public static String DialogTitle_Fields_with_GENERATED_clause_A;

    public static String Wizard_Title_New_Job_wizard;
    public static String Wizard_Page_Data_Library;
    public static String Wizard_Page_Data_Library_description;
    public static String Wizard_Page_Job;
    public static String Wizard_Page_Job_description;
    public static String Wizard_Page_Libraries;
    public static String Wizard_Page_Libraries_description;
    public static String Wizard_Page_Library_List;
    public static String Wizard_Page_Library_List_description;

    public static String Wizard_Title_New_File_wizard;
    public static String Wizard_Page_File;
    public static String Wizard_Page_File_description;
    public static String Wizard_Page_Area;
    public static String Wizard_Page_Area_description;
    public static String Wizard_Page_Command;
    public static String Wizard_Page_Command_description;
    public static String Wizard_Page_Conversion;
    public static String Wizard_Page_Conversion_description;

    // Dialog action titles
    public static String DialogMode_CREATE;
    public static String DialogMode_COPY;
    public static String DialogMode_CHANGE;
    public static String DialogMode_DELETE;
    public static String DialogMode_DISPLAY;

    // Column headings
    public static String ColumnLabel_File;
    public static String ColumnLabel_Library;
    public static String ColumnLabel_Records_in_production_library;
    public static String ColumnLabel_Records_in_shadow_library;
    public static String ColumnLabel_Records_to_copy;
    public static String ColumnLabel_Records_copied;
    public static String ColumnLabel_Estimated_time;
    public static String ColumnLabel_Records_with_duplicate_key;
    public static String ColumnLabel_Changes_to_apply;
    public static String ColumnLabel_Changes_applied;
    public static String ColumnLabel_Progress;
    public static String ColumnLabel_Active;
    public static String ColumnLabel_Start_time;
    public static String ColumnLabel_End_time;
    public static String ColumnLabel_Area;
    public static String ColumnLabel_Conversion_program;

    // Dialog labels
    public static String Label_E_Mail_colon;
    public static String Label_Internet_colon;
    public static String Label_Telefax_colon;
    public static String Label_Telefon_colon;

    public static String Label_Maintain_ativity_status_usage_info;

    public static String Label_Library_colon;
    public static String Label_Job_colon;
    public static String Label_Description_colon;
    public static String Label_Create_environment_colon;
    public static String Label_Job_queue_name_colon;
    public static String Label_Job_queue_library_name_colon;
    public static String Label_Cancel_ASP_threshold_exceeds_colon;
    public static String Label_Status_colon;
    public static String Label_Phase_colon;
    public static String Label_Full_generic_string;
    public static String Label_Enable_large_progress_bar;
    public static String Label_Position_colon;
    public static String Label_File_colon;
    public static String Label_FileType_colon;
    public static String Label_Copy_program_name_colon;
    public static String Label_Copy_program_library_name_colon;
    public static String Label_Conversion_program_name_colon;
    public static String Label_Conversion_program_library_name_colon;
    public static String Label_Shadow_library_colon;
    public static String Label_Library_list_colon;
    public static String Label_NotificationType_colon;
    public static String Label_User_colon;
    public static String Label_Message_queue_name_colon;
    public static String Label_Message_queue_library_name_colon;
    public static String Label_Area_colon;
    public static String Label_Area_library_colon;
    public static String Label_Area_library_list_colon;
    public static String Label_Area_library_ccsid;
    public static String Label_Command_extension_colon;
    public static String Label_Field_to_convert_colon;
    public static String Label_Rename_field_in_old_file_to_colon;
    public static String Label_Conversions_colon;
    public static String Label_Command_type_colon;
    public static String Label_Command_sequence_colon;
    public static String Label_Command_command_colon;
    public static String Label_Host_name_colon;
    public static String Label_FTP_port_number_colon;
    public static String Label_Signon_User_colon;
    public static String Label_Password_colon;
    public static String Label_Rapid_Fire_library_colon;
    public static String Label_ASP_group_colon;
    public static String Label_Version_colon;
    public static String Label_Show_logical_files_colon;
    public static String Label_Label_Decorations_RSE_host_objects_Description;
    public static String Label_DateAndTimeFormats;
    public static String Label_Date_colon;
    public static String Label_Time_colon;
    public static String Label_Job_status_view;
    public static String Label_Open_member_colon;
    public static String Label_Remote_Connection_settings;
    public static String Label_Is_slow_connection;
    public static String Label_Enable_action_cache;
    public static String Label_Wizard_settings;
    public static String Label_Skip_disabled_pages;
    public static String Label_Uninstall_library;
    public static String Label_Uninstal_library_instructions;
    public static String Label_Start_journaling_Rapid_Fire_files;
    public static String Label_Start_journaling_Rapid_Fire_files_help;
    public static String Label_Connection_colon;
    public static String Label_Cancel;
    public static String Label_Continue;
    public static String Label_Library;
    public static String Label_File;
    public static String Label_Field;
    public static String Label_Text;
    
    public static String Tooltip_Library;
    public static String Tooltip_Job;
    public static String Tooltip_Description;
    public static String Tooltip_Create_environment;
    public static String Tooltip_Job_queue_name;
    public static String Tooltip_Job_queue_library_name;
    public static String Tooltip_Cancel_ASP_threshold_exceeds;
    public static String Tooltip_Enable_large_progress_bar;
    public static String Tooltip_Position;
    public static String Tooltip_File;
    public static String Tooltip_FileType;
    public static String Tooltip_Copy_program_name;
    public static String Tooltip_Copy_program_library_name;
    public static String Tooltip_Conversion_program_name;
    public static String Tooltip_Conversion_program_library_name;
    public static String Tooltip_Shadow_library;
    public static String Tooltip_Library_list;
    public static String Tooltip_NotificationType;
    public static String Tooltip_User;
    public static String Tooltip_Message_queue_name;
    public static String Tooltip_Message_queue_library_name;
    public static String Tooltip_Area;
    public static String Tooltip_Area_library;
    public static String Tooltip_Area_library_list;
    public static String Tooltip_Area_library_ccsid;
    public static String Tooltip_Command_extension;
    public static String Tooltip_Field_to_convert;
    public static String Tooltip_Rename_field_in_old_file_to;
    public static String Tooltip_Conversions;
    public static String Tooltip_Command_type;
    public static String Tooltip_Command_sequence;
    public static String Tooltip_Command_command;
    public static String Tooltip_Host_name;
    public static String Tooltip_FTP_port_number;
    public static String Tooltip_Signon_User;
    public static String Tooltip_Password;
    public static String Tooltip_Rapid_Fire_library;
    public static String Tooltip_ASP_group;
    public static String Tooltip_Version;
    public static String Tooltip_Connection_name;
    public static String Tooltip_Specifies_the_format_for_displaying_date_values;
    public static String Tooltip_Specifies_the_format_for_displaying_time_values;
    public static String Tooltip_Open_member;
    public static String Tooltip_Select_libraries_by_area;
    public static String Tooltip_Is_slow_connection;
    public static String Tooltip_Enable_action_cache;
    public static String Tooltip_Select_conversion_program_of_file;
    public static String Tooltip_Skip_disabled_pages;
    public static String Tooltip_Start_journaling_Rapid_Fire_files;
    public static String Tooltip_Label_Connection_colon;

    // Questions
    public static String Question_Do_you_want_to_delete_library_A;
    public static String Question_Do_you_want_to_delete_object_A_B_type_C;
    public static String Question_Do_you_want_to_test_job_A;
    public static String Question_Do_you_want_to_start_job_A;
    public static String Question_Do_you_want_to_end_job_A;
    public static String Question_Do_you_want_to_reset_job_A;
    public static String Question_Do_you_want_to_reset_job_A_after_abortion;
    public static String Question_Delete_shadow_library;
    public static String Question_Do_you_want_to_reapply_all_changes_to_file_B_of_copy_job_A;
    public static String Question_Could_not_generate_copy_program_Do_you_want_to_try_again;

    // Status messages
    public static String About_to_transfer_library_A_ASP_group_D_to_host_B_using_port_C;
    public static String Checking_library_A_for_existence;
    public static String Checking_file_B_in_library_A_for_existence;
    public static String Creating_save_file_B_in_library_A;
    public static String Sending_save_file_to_host;
    public static String Using_Ftp_port_number;
    public static String Deleting_library_A;
    public static String Deleting_object_A_B_of_type_C;
    public static String Restoring_library_A;
    public static String Journaling_will_be_started_by_the_installer;
    public static String Initializing_library_A;
    public static String Library_A_successfull_transfered;
    public static String Error_occurred_while_transfering_library_A;
    public static String Error_occurred_while_setting_the_asp_group_to_A;
    public static String No_errors;
    public static String NewJobWizard_Rapid_Fire_job_A_created;
    public static String NewFileWizard_File_A_created;
    public static String Copy_program_successfully_generated;
    public static String Job_log_has_been_printed;
    public static String Please_select_a_connection;
    public static String Connecting_to_A;
    public static String Operation_has_been_canceled_by_the_user;

    // Action labels
    public static String ActionLabel_Refresh;
    public static String ActionLabel_Auto_refresh_menu_item;
    public static String ActionLabel_Auto_refresh_menu_item_stop;
    public static String ActionLabel_Auto_refresh_menu_item_every_A_seconds;
    public static String ActionLabel_Enable_activity_time_frame;
    public static String ActionLabel_Disable_activity_time_frame;
    public static String ActionLabel_Start_Transfer;
    public static String ActionLabel_Print_job_log;
    public static String ActionLabel_Close;
    public static String ActionLabel_Copy;
    public static String ActionLabel_Copy_all;
    public static String ActionLabel_Transfer_Rapid_Fire_library;
    public static String ActionLabel_Reapply_changes;

    public static String ActionTooltip_Refresh;
    public static String ActionTooltip_Auto_refresh_menu_item_every_A_seconds;
    public static String ActionTooltip_Auto_refresh_menu_item_stop;
    public static String ActionTooltip_Enable_activity_time_frame;
    public static String ActionTooltip_Disable_activity_time_frame;
    public static String ActionTooltip_Start_Transfer;
    public static String ActionTooltip_Close;
    public static String ActionTooltip_Copy;
    public static String ActionTooltip_Transfer_Rapid_Fire_library;
    public static String ActionTooltip_Reapply_changes;

    // Property labels
    public static String Batch_job_name;
    public static String Batch_job_user;
    public static String Batch_job_number;

    public static String Tooltip_Batch_job_name;
    public static String Tooltip_Batch_job_user;
    public static String Tooltip_Batch_job_number;

    // Job labels
    public static String JobLabel_Refreshing_file_copy_statuses;

    // Information labels
    public static String Wizard_Not_applicable_for_jobs_that_do_not_create_a_shadow_environment;
    public static String Wizard_File_page_info_box;
    public static String Wizard_Area_page_info_box;
    public static String Wizard_Command_page_info_box;
    public static String Wizard_Conversion_page_info_box_1;
    public static String Wizard_Conversion_page_info_box_2;

    // Error messages
    public static String Job_name_A_is_not_valid;
    public static String Description_A_is_not_valid;
    public static String Library_name_A_is_not_valid;
    public static String Create_environment_value_has_been_rejected;
    public static String Job_queue_name_A_is_not_valid;
    public static String Cancel_ASP_threshold_exceeds_value_has_been_rejected;
    public static String The_library_name_must_be_specified;
    public static String The_job_name_must_be_specified;
    public static String The_job_status_must_be_specified;
    public static String Rapid_Fire_library_A_on_system_B_is_of_version_C_but_at_least_version_D_is_required_Please_update_the_Rapid_Fire_library;
    public static String The_installed_Rapid_Fire_plug_in_version_A_is_outdated_because_the_installed_Rapid_Fire_library_requires_at_least_version_B_of_the_Rapid_Fire_plug_in_Please_update_your_Rapid_Fire_plug_in;
    public static String Rapid_Fire_library_A_does_not_exist_on_system_B_Please_install_Rapid_Fire_library_A_on_system_B;
    public static String The_specified_library_A_on_system_B_is_not_a_Rapid_Fire_library;
    public static String Could_not_initialize_job_manager_for_job_A_in_library_B;
    public static String Could_not_initialize_file_manager_for_file_at_position_C_of_job_A_in_library_B;
    public static String File_position_A_is_not_valid;
    public static String File_name_A_is_not_valid;
    public static String File_type_A_is_not_valid;
    public static String Copy_program_name_A_is_not_valid;
    public static String Copy_program_library_name_A_is_not_valid;
    public static String Conversion_program_name_A_is_not_valid;
    public static String Conversion_program_library_name_A_is_not_valid;
    public static String Could_not_initialize_library_manager_for_library_C_of_job_A_in_library_B;
    public static String Could_not_initialize_library_list_manager_for_library_list_C_of_job_A_in_library_B;
    public static String Could_not_start_a_Rapid_Fire_JDBC_connection;
    public static String Could_not_stop_the_Rapid_Fire_JDBC_connection;
    public static String Could_not_initialize_notification_manager_for_notification_at_position_C_of_job_A_in_library_B;
    public static String Name_of_library_A_is_not_valid;
    public static String Name_of_shadow_library_A_is_not_valid;
    public static String Library_list_name_A_is_not_valid;
    public static String Library_list_description_A_is_not_valid;
    public static String Invalid_sequence_number_A;
    public static String Notification_position_A_is_not_valid;
    public static String Notification_type_A_is_not_valid;
    public static String User_name_A_is_not_valid;
    public static String Message_queue_name_A_is_not_valid;
    public static String Could_not_initialize_area_manager_for_area_D_of_file_C_of_job_A_in_library_B;
    public static String Area_name_A_is_not_valid;
    public static String Ccsid_A_is_not_valid;
    public static String Command_extension_A_is_not_valid;
    public static String Could_not_initialize_conversion_manager_for_field_D_of_file_C_of_job_A_in_library_B;
    public static String Field_name_A_is_not_valid;
    public static String Field_names_must_not_match;
    public static String Conversion_statement_is_missing;
    public static String Could_not_initialize_command_manager_for_command_type_D_and_sequence_E_of_file_C_of_job_A_in_library_B;
    public static String The_requested_operation_is_invalid_for_job_status_A;
    public static String Enter_a_host_name;
    public static String Enter_a_user_name;
    public static String Enter_a_password;
    public static String Library_A_does_already_exist;
    public static String File_B_in_library_A_does_already_exist;
    public static String Could_not_create_save_file_B_in_library_A;
    public static String Could_not_restore_library_A;
    public static String Could_not_send_save_file_to_host;
    public static String Rapid_Fire_library_not_set_in_preferences;
    public static String The_name_of_the_Rapid_Fire_library_is_invalid;
    public static String The_name_of_the_asp_group_is_invalid;
    public static String Host_A_not_found_in_configured_RSE_connections;
    public static String Rapid_Fire_version_information_not_found_in_library_A;
    public static String Could_not_rereieve_Rapid_Fire_version_information_due_to_backend_error_A;
    public static String Connection_A_not_found;
    public static String Connection_is_missing;
    public static String Could_not_connect_to_A;
    public static String The_Rapid_Fire_product_library_name_is_missing;
    public static String Library_list_entries_are_missing;
    public static String Error_on_wizard_page_A_B;
    public static String Library_A_not_found_on_system_B;
    public static String Could_not_commit_transaction_of_connection_A;
    public static String Could_not_rollback_transaction_of_connection_A;
    public static String Could_set_auto_commit_property_for_connection_A;
    public static String Field_list_not_available_Areas_have_not_yet_been_defined;
    public static String File_A_not_found_in_areas;
    public static String File_C_not_found_in_library_B_on_system_A;
    public static String Member_D_not_found_in_File_B_C_on_system_A;
    public static String Member_A_exists_Do_you_want_to_replace_the_member_A;
    public static String Member_A_already_exists;
    public static String Library_name_is_missing;
    public static String File_name_is_missing;
    public static String Member_name_is_missing;
    public static String Could_not_initialize_file_copy_program_generator_manager_for_file_at_position_C_of_job_A_in_library_B;
    public static String Could_not_initialize_reapply_changes_manager_for_job_A_in_library_B;
    public static String Could_not_create_resource_Resource_not_found;
    public static String Could_not_copy_resource_Resource_not_found;
    public static String Object_compile_command_is_missing;
    public static String Field_to_convert_is_missing;
    public static String Error_A;

    // Exception
    public static String AutoReconnectErrorException_Connection_broken_Could_not_reconnect_to_system_A;

    // API error messages
    public static String EntityManager_Unknown_error_code_A;

    public static String RapidFire_Start_001;

    public static String RapidFire_Stop_001;

    public static String RapidFire_Set_Auto_Commit_001;

    public static String RapidFire_Commit_001;

    public static String RapidFire_Rollback_001;

    public static String JobManager_001;
    public static String JobManager_002;
    public static String JobManager_003;

    public static String FileManager_001;
    public static String FileManager_002;
    public static String FileManager_003;

    public static String LibraryManager_001;
    public static String LibraryManager_002;
    public static String LibraryManager_003;

    public static String LibraryListManager_001;
    public static String LibraryListManager_002;
    public static String LibraryListManager_003;

    public static String NotificationManager_001;
    public static String NotificationManager_002;
    public static String NotificationManager_003;

    public static String AreaManager_001;
    public static String AreaManager_002;
    public static String AreaManager_003;
    public static String AreaManager_004;

    public static String ConversionManager_001;
    public static String ConversionManager_002;
    public static String ConversionManager_003;
    public static String ConversionManager_004;

    public static String CommandManager_001;
    public static String CommandManager_002;
    public static String CommandManager_003;
    public static String CommandManager_004;

    // String compare
    public static String Server_job_colon;

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
