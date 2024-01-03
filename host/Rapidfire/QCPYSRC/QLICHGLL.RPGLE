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

DCL-PR QLICHGLL ExtPgm('QLICHGLL');
  *N CHAR(11) Const;                                                     // --> Current libra
  *N CHAR(11) Const;                                                     // --> First product
  *N CHAR(11) Const;                                                     // --> Second produc
  *N CHAR(11) Const Dim(250);                                            // --> User library
  *N INT(10) Const;                                                      // --> Number of use
  *N CHAR(32767) Options(*VarSize);                                      // <-> Error code
END-PR;
