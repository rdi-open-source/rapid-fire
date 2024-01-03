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

DCL-PR QUSRMBRD ExtPgm('QUSRMBRD');
  *N CHAR(32767) Options(*VarSize);                                      // <-- Receiver vari
  *N INT(10) Const;                                                      // --> Length of rec
  *N CHAR(8) Const;                                                      // --> Format name
  *N CHAR(20) Const;                                                     // --> Qualified dat
  *N CHAR(10) Const;                                                     // --> Database memb
  *N CHAR(1) Const;                                                      // --> Override proc
  *N CHAR(32767) Options(*VarSize : *NoPass);                            // <-> Error code
  *N CHAR(1) Options(*NoPass);                                           // --> Find member p
END-PR;

DCL-S QUSRMBRD_Heap
      CHAR(1) Based(QUSRMBRD_Heap_Pointer);

DCL-S QUSRMBRD_Heap_Pointer
      POINTER;
