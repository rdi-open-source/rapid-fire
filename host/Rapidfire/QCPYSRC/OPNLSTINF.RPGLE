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

DCL-DS OPNLSTINF Qualified;
  Total_Records                     INT(10);  // <-- Total records
  Records_Returned                  INT(10);  // <-- Records returned
  Request_Handle                    CHAR(4);  // <-- Request handle
  Record_Length                     INT(10);  // <-- Record length
  Information_Complete_Indicator    CHAR(1);  // <-- Information complete indicator
  Date_And_Time_Created             CHAR(13); // <-- Date and time created
  List_Status_Indicator             CHAR(1);  // <-- List status indicator
  Reserved_1                        CHAR(1);  // <-- Reserved
  Length_Of_Information_Returned    INT(10);  // <-- Length of information returned
  First_Record_In_Receiver_Variable INT(10);  // <-- First record in receiver variable
  Reserved_2                        CHAR(40); // <-- Reserved
END-DS;
