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

DCL-PR QMHSNDPM ExtPgm('QMHSNDPM');
  *N CHAR(7) Const;                                                      // --> Message ident
  *N CHAR(20) Const;                                                     // --> Qualified mes
  *N CHAR(32767) Const Options(*VarSize);                                // --> Message data
  *N INT(10) Const;                                                      // --> Length of mes
  *N CHAR(10) Const;                                                     // --> Message type
  *N CHAR(256) Const;                                                    // --> Call stack en
  *N INT(10) Const;                                                      // --> Call stack co
  *N CHAR(4);                                                            // <-- Message key
  *N CHAR(32767) Options(*VarSize);                                      // <-> Error code
  *N INT(10) Const Options(*NoPass);                                     // --> Length of cal
  *N CHAR(20) Const Options(*NoPass);                                    // --> Call stack en
  *N INT(10) Const Options(*NoPass);                                     // --> Display progr
  *N CHAR(10) Const Options(*NoPass);                                    // --> Call stack en
  *N INT(10) Const Options(*NoPass);                                     // --> Coded charact
END-PR;
