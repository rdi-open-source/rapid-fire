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

DCL-PR QLIRLIBD ExtPgm('QLIRLIBD');
  *N CHAR(32767) Options(*VarSize);                                      // <-- Receiver vari
  *N INT(10) Const;                                                      // --> Length of rec
  *N CHAR(10) Const;                                                     // --> Library name
  *N CHAR(32767) Const Options(*VarSize);                                // --> Attributes to
  *N CHAR(32767) Options(*VarSize);                                      // <-> Error code
END-PR;
