**FREE

//*-----------------------------------------------------------------------------------------*
//*                                                                                         *
//* Copyright (c) 2016 Task Force IT-Consulting GmbH, Waltrop (Germany)                     *
//* This software and the accompanying materials are made available                         *
//* under the terms of the GNU General Public License version 2.0 (GPLv2)                   *
//* which accompanies this distribution and is available at                                 *
//* http://www.gnu.org/licenses/gpl-2.0.html                                                *
//*                                                                                         *
//*-----------------------------------------------------------------------------------------*

// Header
DCL-DS MBRD0300 Qualified;
  Bytes_Returned
  INT(10);
  Bytes_Available
  INT(10);
END-DS;

// General information
DCL-DS MBRD0300_A Based(MBRD0300_A_Pointer) Qualified;
  Bytes_Returned
  INT(10);
  Bytes_Available
  INT(10);
  Database_File
  CHAR(10);
  Database_File_Library
  CHAR(10);
  Member
  CHAR(10);
  File_Attribute
  CHAR(10);
  Source_Type
  CHAR(10);
  Creation_Date_And_Time
  CHAR(13);
  Last_Source_Change_Date_And_Time
  CHAR(13);
  Member_Text
  CHAR(50);
  Source_File
  CHAR(1);
  Remote_File
  CHAR(1);
  Logical_File_Or_Physical_File
  CHAR(1);
  ODP_Sharing
  CHAR(1);
  Reserved_1
  CHAR(2);
  Number_Of_Current_Records_Old
  INT(10);
  Number_Of_Deleted_Records_Old
  INT(10);
  Data_Space_Size
  INT(10);
  Access_Path_Size
  INT(10);
  Based_On_File_Number
  INT(10);
  Change_Date_And_Time
  CHAR(13);
  Save_Date_And_Time
  CHAR(13);
  Restore_Date_And_Time
  CHAR(13);
  Expiration_Date
  CHAR(7);
  Reserved_2
  CHAR(6);
  Number_Of_Days_Used
  INT(10);
  Date_Last_Used
  CHAR(7);
  Use_Reset_Date
  CHAR(7);
  Reserved_3
  CHAR(2);
  Data_Space_Size_Multiplier
  INT(10);
  Access_Path_Size_Multiplier
  INT(10);
  Member_Text_CCSID
  INT(10);
  Offset_To_Additional_Info
  INT(10);
  Length_Of_Additional_Info
  INT(10);
  Number_Of_Current_Records
  UNS(10);
  Number_Of_Deleted_Records
  UNS(10);
  Reserved_4
  CHAR(6);
  Join_Member
  CHAR(1);
  Access_Path_Maintenance
  CHAR(1);
  SQL_File_Type
  CHAR(10);
  Reserved_5
  CHAR(1);
  Allow_Read_Operation
  CHAR(1);
  Allow_Write_Operation
  CHAR(1);
  Allow_Update_Operation
  CHAR(1);
  Allow_Delete_Operation
  CHAR(1);
  Reserved_6
  CHAR(1);
  Records_To_Force_A_Write
  INT(10);
  Maximum_Percent_Deleted_Records
  INT(10);
  Initial_Number_Of_Records
  INT(10);
  Increment_Number_Of_Records
  INT(10);
  Maximum_Number_Of_Increments
  INT(10);
  Current_Number_Of_Increments
  INT(10);
  Record_Capacity
  INT(10);
  Record_Format_Selector_Program
  CHAR(10);
  Record_Format_Selector_Library
  CHAR(10);
  Number_Of_Constraint_Indexes
  INT(5);
  Offset_To_Constraint_Indexes_Info
  INT(10);
  Reserved_7
  CHAR(46);
END-DS;

DCL-S MBRD0300_A_Pointer POINTER;

// Record format and based-on file list
DCL-DS MBRD0300_B Based(MBRD0300_B_Pointer) Qualified;
  Based_On_File
  CHAR(10);
  Based_On_File_Library
  CHAR(10);
  Based_On_File_Member
  CHAR(10);
  Format
  CHAR(10);
  Logical_File_Record_Format_Number
  INT(10);
  Current_Number_Of_Records
  INT(10);
  Number_Of_Deleted_Records
  INT(10);
  Access_Path_Size
  INT(10);
  Access_Path_Size_Multiplier
  INT(10);
  Access_Path_Shared
  CHAR(1);
  Access_Path_Valid
  CHAR(1);
  Access_Path_Held
  CHAR(1);
  Access_Path_Owner_File
  CHAR(10);
  Access_Path_Owner_Library
  CHAR(10);
  Access_Path_Owner_Member
  CHAR(10);
  Reserved
  CHAR(19);
END-DS;

DCL-S MBRD0300_B_Pointer
      POINTER;
