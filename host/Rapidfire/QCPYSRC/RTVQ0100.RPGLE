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

DCL-DS RTVQ0100 Qualified;
  Bytes_Returned
  INT(10);
  Bytes_Available
  INT(10);
  Message_Key
  CHAR(4);
  Reserved
  CHAR(20);
  Length_Of_Message_Returned
  INT(10);
  Length_Of_Message_Available
  INT(10);
  Request_Message
  CHAR(1024);
END-DS;
