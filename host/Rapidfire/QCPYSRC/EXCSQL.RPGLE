**FREE

//*-----------------------------------------------------------------------------------------*
//*                                                                                         *
//* Copyright (c) 2018 Task Force IT-Consulting GmbH, Waltrop (Germany)                     *
//* This software and the accompanying materials are made available                         *
//* under the terms of the GNU General Public License version 2.0 (GPLv2)                   *
//* which accompanies this distribution and is available at                                 *
//* http://www.gnu.org/licenses/gpl-2.0.html                                                *
//*                                                                                         *
//*-----------------------------------------------------------------------------------------*

DCL-PR EXCSQL_execute_Stmt Ind;
    *N CHAR(4096) Const;
    *N CHAR(10) Const Options(*NoPass: *Omit);
END-PR;

DCL-PR EXCSQL_execute_Script Ind;
    *N CHAR(10) Const;
    *N CHAR(10) Const;
    *N CHAR(10) Const;
    *N CHAR(10) Const Options(*NoPass: *Omit);
END-PR;

DCL-PR EXCSQL_get_Message
       VARCHAR(1024);                                                      // Error Message
END-PR;

DCL-S EXCSQL_Message
      VARCHAR(1024) Import;
