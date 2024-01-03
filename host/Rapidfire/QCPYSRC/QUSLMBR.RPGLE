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

DCL-PR QUSLMBR ExtPgm('QUSLMBR');
  *N CHAR(20) Const;                                                     // --> Qualified use
  *N CHAR(8) Const;                                                      // --> Format name
  *N CHAR(20) Const;                                                     // --> Qualified dat
  *N CHAR(10) Const;                                                     // --> Member name
  *N CHAR(1) Const;                                                      // --> Override proc
  *N CHAR(32767) Options(*VarSize : *NoPass);                            // <-> Error code
END-PR;
