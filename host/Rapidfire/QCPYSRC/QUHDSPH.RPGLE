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

DCL-PR QUHDSPH ExtPgm('QUHDSPH');
  *N CHAR(52) Dim(2000) Const Options(*VarSize);                         // --> Help identifi
  *N INT(10) Const;                                                      // --> Number of hel
  *N INT(10) Dim(2) Const;                                               // --> Help type
  *N CHAR(55) Const;                                                     // --> Full display
  *N CHAR(20) Const;                                                     // --> Qualified sea
  *N CHAR(1) Const;                                                      // --> Display type
  *N INT(10) Dim(2) Const;                                               // --> Upper left co
  *N INT(10) Dim(2) Const;                                               // --> Lower right c
  *N INT(10) Dim(2) Const;                                               // --> Cursor locati
  *N CHAR(32767) Options(*VarSize);                                      // <-> Error code
  *N CHAR(8) Const Options(*NoPass);                                     // --> Help bookshel
  *N CHAR(1) Dim(2) Const Options(*NoPass);                              // --> Border attrib
  *N CHAR(1) Dim(8) Const Options(*NoPass);                              // --> Border charac
END-PR;
