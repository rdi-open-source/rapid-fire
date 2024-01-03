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

DCL-PR JOURNALRTN_set_Job;
  *N CHAR(10) Const;
END-PR;

DCL-PR JOURNALRTN_get_Job
       CHAR(10);
END-PR;

DCL-PR JOURNALRTN_set_Return_Code;
  *N CHAR(10) Const;
END-PR;

DCL-PR JOURNALRTN_get_Return_Code
       CHAR(10);
END-PR;

DCL-PR JOURNALRTN_set_Date;
  *N CHAR(6) Const;
END-PR;

DCL-PR JOURNALRTN_get_Date
       CHAR(6);
END-PR;

DCL-PR JOURNALRTN_set_Time;
  *N CHAR(6) Const;
END-PR;

DCL-PR JOURNALRTN_get_Time
       CHAR(6);
END-PR;
