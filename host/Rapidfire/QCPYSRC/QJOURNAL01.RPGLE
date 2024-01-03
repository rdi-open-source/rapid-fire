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

DCL-PR QRTVJRNINF
       ExtProc(
       'QjoRetrieveJournalInformation'
       );
  *N CHAR(32767) Options(*VarSize);                                      // <-- Receiver vari
  *N INT(10) Const;                                                      // --> Length of rec
  *N CHAR(20) Const;                                                     // --> Qualified jou
  *N CHAR(8) Const;                                                      // --> Format name
  *N CHAR(32767) Const Options(*VarSize);                                // --> Journal infor
  *N CHAR(32767) Options(*VarSize : *NoPass);                            // <-> Error code
END-PR;

DCL-S QRTVJRNINF_Heap
      CHAR(1) Based(QRTVJRNINF_Heap_Pointer);

DCL-S QRTVJRNINF_Heap_Pointer
      POINTER;
