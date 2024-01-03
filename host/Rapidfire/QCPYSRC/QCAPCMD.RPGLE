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

DCL-PR QCAPCMD ExtPgm('QCAPCMD');
  *N CHAR(32767) Const Options(*VarSize);                                // --> Source comman
  *N INT(10) Const;                                                      // --> Length of sou
  *N CHAR(32767) Const Options(*VarSize);                                // --> Options contr
  *N INT(10) Const;                                                      // --> Options contr
  *N CHAR(8) Const;                                                      // --> Options contr
  *N CHAR(32767) Options(*VarSize);                                      // <-- Changed comma
  *N INT(10) Const;                                                      // --> Length availa
  *N INT(10);                                                            // <-- Length availa
  *N CHAR(32767) Options(*VarSize);                                      // <-> Error code
END-PR;
