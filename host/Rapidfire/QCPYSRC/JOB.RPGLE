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

DCL-PR JOB_start;
  *N CHAR(10) Const;                                                     // --> Job
  *N CHAR(10) Const;                                                     // --> Test
END-PR;

DCL-PR JOB_end;
  *N CHAR(10) Const;                                                     // --> Job
END-PR;

DCL-PR JOB_reset;
  *N CHAR(10) Const;                                                     // --> Job
  *N CHAR(1) Const;                                                      // --> Delete shadow
END-PR;

DCL-PR JOB_is_Job_Active
       IND;
  *N CHAR(10) Const;                                                     // --> Job
  *N CHAR(10) Const;                                                     // --> User
  *N CHAR(6) Const;                                                      // --> Number
END-PR;

DCL-PR JOB_is_Job_Active_SP;
  *N CHAR(10) Const;                                                     // --> Job
  *N CHAR(10) Const;                                                     // --> User
  *N CHAR(6) Const;                                                      // --> Number
  *N CHAR(1);                                                            // <-- Active
END-PR;
