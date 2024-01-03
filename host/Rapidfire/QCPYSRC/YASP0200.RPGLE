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

DCL-DS YASP0200 Qualified;
  ASP_Number                               INT(10);  // <-- ASP number
  Number_Of_Disk_Units                     INT(10);  // <-- Number of disk units
  ASP_Capacity_Total                       INT(10);  // <-- ASP capacity - total
  ASP_Capacity_Available_Total             INT(10);  // <-- ASP capacity available - total
  ASP_Capacity_Protected                   INT(10);  // <-- ASP capacity - protected
  ASP_Capacity_Available_Protected         INT(10);  // <-- ASP capacity available - protected
  ASP_Capacity_Unprotected                 INT(10);  // <-- ASP capacity - unprotected
  ASP_Capacity_Available_Unprotected       INT(10);  // <-- ASP capacity available - unprotected
  ASP_System_Storage                       INT(10);  // <-- ASP system storage
  Overflow_Storage                         INT(10);  // <-- Overflow storage
  Space_Allocated_To_The_Error_Log         INT(10);  // <-- Space allocated to the error log
  Space_Allocated_To_The_Machine_Log       INT(10);  // <-- Space allocated to the machine log
  Space_Allocated_To_The_Machine_Trace     INT(10);  // <-- Space allocated to the machine trace
  Space_Allocated_For_Main_Storage_Dump    INT(10);  // <-- Space allocated for main storage dump
  Space_Allocated_To_The_Microcode         INT(10);  // <-- Space allocated to the microcode
  Storage_Threshold_Percentage             INT(10);  // <-- Storage threshold percentage
  ASP_Type                                 CHAR(2);  // <-- ASP type
  Overflow_Recovery_Result                 CHAR(1);  // <-- Overflow recovery result
  End_Immediate_Control                    CHAR(1);  // <-- End immediate control
  Compression_Recovery_Policy              CHAR(1);  // <-- Compression recovery policy
  Compressed_Disk_Units_In_ASP             CHAR(1);  // <-- Compressed disk units in ASP
  Balance_Status                           CHAR(1);  // <-- Balance status
  Balance_Type                             CHAR(1);  // <-- Balance type
  Balance_Date_And_Time                    CHAR(13); // <-- Balance date and time
  Reserved_1                               CHAR(3);  // <-- Reserved
  Balance_Data_Moved                       INT(10);  // <-- Balance data moved
  Balance_Data_Remaining                   INT(10);  // <-- Balance data remaining
  Trace_Duration                           INT(10);  // <-- Trace duration
  Trace_Status                             CHAR(1);  // <-- Trace status
  Trace_Date_And_Time                      CHAR(13); // <-- Trace date and time
  Changes_Written_To_Disk                  CHAR(1);  // <-- Changes written to disk
  Reserved_2                               CHAR(1);  // <-- Reserved
  Number_Of_Multiple_Connection_Disk_Units INT(10);  // <-- Number of multiple connection disk units
  Geographic_Mirror_Role                   INT(10);  // <-- Geographic mirror role
  Geographic_Mirror_Copy_State             INT(10);  // <-- Geographic mirror copy state
  Geographic_Mirror_Copy_Data_State        INT(10);  // <-- Geographic mirror copy data state
  Geographic_Mirror_Performance_Mode       INT(10);  // <-- Geographic mirror performance mode
  Geographic_Mirror_Resume_Priority        INT(10);  // <-- Geographic mirror resume priority
  Geographic_Mirror_Suspend_Timeout        INT(10);  // <-- Geographic mirror suspend timeout
  Disk_Unit_Presence                       INT(10);  // <-- Disk unit presence
END-DS;
