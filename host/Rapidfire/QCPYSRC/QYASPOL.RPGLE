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

DCL-PR QYASPOL ExtPgm('QYASPOL');
  *N CHAR(32767) Options(*VarSize);                 // <-- Receiver variable
  *N INT(10) Const;                                 // --> Length of receiver variable
  *N CHAR(80);                                      // <-- List information
  *N INT(10) Const;                                 // --> Number of records to return
  *N INT(10) Const;                                 // --> Number of filters
  *N CHAR(32767) Options(*VarSize) Const;           // --> Filter information
  *N CHAR(8) Const;                                 // --> Format name
  *N CHAR(32767) Options(*VarSize);                 // <-> Error code
  *N CHAR(32767) Options(*VarSize : *NoPass) Const; // --> Sort information
END-PR;
