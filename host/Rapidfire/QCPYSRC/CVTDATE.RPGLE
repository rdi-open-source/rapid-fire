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

DCL-PR CvtDate ExtPgm('CVTDATE');
  *N CHAR(10) Const;                                                    // --> From date
  *N CHAR(10) Const;                                                    // --> From format
  *N CHAR(10) Const;                                                    // --> To format
  *N CHAR(10) Const;                                                    // --> To separator
  *N CHAR(10);                                                          // --> To date
END-PR;
