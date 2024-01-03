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

DCL-PR QMHMOVPM ExtPgm('QMHMOVPM');
  *N CHAR(4) Const;                                                      // --> Message key
  *N CHAR(40) Const;                                                     // --> Message types
  *N INT(10) Const;                                                      // --> Number of mes
  *N CHAR(256) Const;                                                    // --> To call stack
  *N INT(10) Const;                                                      // --> To call stack
  *N CHAR(32767) Options(*VarSize);                                      // <-> Error code
  *N INT(10) Const Options(*NoPass);                                     // --> Length of to
  *N CHAR(20) Const Options(*NoPass);                                    // --> To call stack
  *N CHAR(10) Const Options(*NoPass);                                    // --> To call stack
  *N CHAR(16) Const Options(*NoPass);                                    // --> From call sta
  *N INT(10) Const Options(*NoPass);                                     // --> From call sta
END-PR;
