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

/if not defined(MBRD0100)
/define MBRD0100

DCL-DS mbrd0100_t qualified template;
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
END-DS;

/endif
