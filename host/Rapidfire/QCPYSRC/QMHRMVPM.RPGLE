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

DCL-PR QMHRMVPM ExtPgm('QMHRMVPM');
  *N CHAR(256) Const;                                                    // --> Call stack en
  *N INT(10) Const;                                                      // --> Call stack co
  *N CHAR(4) Const;                                                      // --> Message key
  *N CHAR(10) Const;                                                     // --> Messages to r
  *N CHAR(32767) Options(*VarSize);                                      // <-> Error code
  *N INT(10) Const Options(*NoPass);                                     // --> Length of cal
  *N CHAR(20) Const Options(*NoPass);                                    // --> Call stack en
  *N CHAR(10) Const Options(*NoPass);                                    // --> Remove unhand
  *N CHAR(10) Const Options(*NoPass);                                    // --> Call stack en
END-PR;
