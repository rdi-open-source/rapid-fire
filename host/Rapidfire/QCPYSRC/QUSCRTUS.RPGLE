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

DCL-PR QUSCRTUS ExtPgm('QUSCRTUS');
  *N CHAR(20) Const;                                                     // --> Qualified use
  *N CHAR(10) Const;                                                     // --> Extended attr
  *N INT(10) Const;                                                      // --> Initial size
  *N CHAR(1) Const;                                                      // --> Initial value
  *N CHAR(10) Const;                                                     // --> Public author
  *N CHAR(50) Const;                                                     // --> Text descript
  *N CHAR(10) Const Options(*NoPass);                                    // --> Replace
  *N CHAR(32767) Options(*VarSize : *NoPass);                            // <-> Error code
  *N CHAR(10) Const Options(*NoPass);                                    // --> Domain
END-PR;
