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

DCL-PR CHGTSTJOB_change_Test_Job CHAR(80); // <-- Nachricht
  *N CHAR(10) Const;                       // --> Job
  *N PACKED(6 : 0) Const;                  // --> From row
  *N PACKED(6 : 0) Const;                  // --> To row
  *N CHAR(3) Const;                        // --> Change value
END-PR;
