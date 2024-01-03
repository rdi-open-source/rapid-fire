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

DCL-PR CRTTSTJOB_create_Test_Job CHAR(80); // <-- Nachricht
  *N CHAR(10) Const;                       // --> Job
  *N CHAR(10) Const;                       // --> Production library
  *N CHAR(10) Const;                       // --> Shadow library
  *N PACKED(3 : 0) Const;                  // --> Number of tables
  *N PACKED(3 : 0) Const;                  // --> Number of columns
  *N PACKED(6 : 0) Const;                  // --> Number of rows
  *N PACKED(3 : 0) Const;                  // --> Number of indices
  *N CHAR(10) Const;                       // --> Additional column (Shadow)
  *N CHAR(10) Const;                       // --> Job queue library
  *N CHAR(10) Const;                       // --> Job queue
END-PR;
