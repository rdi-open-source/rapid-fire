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

DCL-DS GHDR0300 Based(GHDR0300_Pointer) Qualified;
  User_Area
  CHAR(64);
  Size_Of_Generic_Header
  INT(10);
  Structures_Release_And_Level
  CHAR(4);
  Format_Name
  CHAR(8);
  API_Used
  CHAR(10);
  Date_And_Time_Created
  CHAR(13);
  Information_Status
  CHAR(1);
  Size_Of_User_Space_Used
  INT(10);
  Offset_To_Input_Parameter_Section
  INT(10);
  Size_Of_Input_Parameter_Section
  INT(10);
  Offset_To_Header_Section
  INT(10);
  Size_Of_Header_Section
  INT(10);
  Offset_To_List_Data_Section
  INT(10);
  Size_Of_List_Data_Section
  INT(10);
  Number_Of_List_Entries
  INT(10);
  Size_Of_Each_Entry
  INT(10);
  CCSID_Of_Data_In_The_List_Entries
  INT(10);
  Country_Id
  CHAR(2);
  Language_Id
  CHAR(3);
  Subsetted_List_Indicator
  CHAR(1);
  Reserved_1
  CHAR(42);
  API_Entry_Point_Name
  CHAR(256);
  Reserved_2
  CHAR(128);
END-DS;

DCL-S GHDR0300_Pointer
      POINTER;
