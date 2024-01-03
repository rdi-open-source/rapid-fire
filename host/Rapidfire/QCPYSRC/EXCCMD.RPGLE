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

DCL-PR EXCCMD_execute_Command;
  *N CHAR(4096) Const;
END-PR;

DCL-PR EXCCMD_get_Message_Id
       CHAR(7);                                                          // Message-Id.
END-PR;

DCL-PR EXCCMD_get_Message_Data
       CHAR(256);                                                        // Message data
END-PR;

DCL-S EXCCMD_Message_Id
      CHAR(7) Import;

DCL-S EXCCMD_Message_Data
      CHAR(256) Import;
