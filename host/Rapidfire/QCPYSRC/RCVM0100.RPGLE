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

DCL-DS RCVM0100 Qualified;
  Bytes_Returned
  INT(10);
  Bytes_Available
  INT(10);
  Message_Servity
  INT(10);
  Message_Id
  CHAR(7);
  Message_Type
  CHAR(2);
  Message_Key
  CHAR(4);
  Reserved
  CHAR(7);
  CCSID_Message_Data
  INT(10);
  CCSID_Message_Text
  INT(10);
  Length_Of_Message_Text_Returned
  INT(10);
  Length_Of_Message_Text_Available
  INT(10);
  Message_Text
  CHAR(1024);
END-DS;
