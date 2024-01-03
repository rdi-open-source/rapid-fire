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

DCL-DS CPOP0100 Qualified;
  Type_Of_Command_Processing
  INT(10);
  DBCS_Data_Handling
  CHAR(1);
  Prompter_Action
  CHAR(1);
  Command_String_Syntax
  CHAR(1);
  Message_Retrieve_Key
  CHAR(4);
  Reserved
  CHAR(9) Inz(X'000000000000000000');
END-DS;
