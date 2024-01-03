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

DCL-PR FILE_get_Number_Of_Records UNS(10); // <-- Number of records
  *N CHAR(10) Const;                       // --> Library
  *N CHAR(10) Const;                       // --> File
  *N CHAR(10) Const;                       // --> Member
END-PR;

DCL-PR FILE_is_File_Journaled IND;             // <-- Result
  p_Library_File CHAR(10) Const;               // --> Library - File
  p_File CHAR(10) Const;                       // --> File
  p_Library_Journal CHAR(10) Options(*NoPass); // <-- Library - Journal
  p_Journal CHAR(10) Options(*NoPass);         // <-- Journal
END-PR;
