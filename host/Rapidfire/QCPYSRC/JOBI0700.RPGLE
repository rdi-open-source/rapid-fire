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

DCL-DS JOBI0700 Qualified;
  Bytes_Returned
  INT(10);
  Bytes_Available
  INT(10);
  Job_Name
  CHAR(10);
  User_Name
  CHAR(10);
  Job_Number
  CHAR(6);
  Internal_Job_Identifier
  CHAR(16);
  Job_Status
  CHAR(10);
  Job_Type
  CHAR(1);
  Job_Subtype
  CHAR(1);
  Reserved
  CHAR(2);
  System_Library_List_Number
  INT(10);
  Product_Library_List_Number
  INT(10);
  Current_Library_List_Number
  INT(10);
  User_Library_List_Number
  INT(10);
  Library_Area
  CHAR(1100);
END-DS;
