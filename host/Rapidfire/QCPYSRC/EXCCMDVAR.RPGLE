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

DCL-PR EXCCMDVAR_execute_Command;
  *N CHAR(32702) Const Options(*VarSize); // --> Command
  *N PACKED(5 : 0) Const;                 // --> Length of command
END-PR;

DCL-PR EXCCMDVAR_get_Message_Id CHAR(7); // <-- Message-Id.
END-PR;

DCL-PR EXCCMDVAR_get_Message_Data CHAR(256); // <-- Message data
END-PR;

DCL-S EXCCMDVAR_Message_Id CHAR(7) Import; // Message-Id.

DCL-S EXCCMDVAR_Message_Data CHAR(256) Import; // Message data
