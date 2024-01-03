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

DCL-PR RF0011PG ExtPgm('RF0011PG');
  *N CHAR(10) Const;                                                     // --> Modus
  *N CHAR(10) Const;                                                     // --> Job
  *N PACKED(6) Const;                                                    // --> Position
  *N CHAR(10);                                                           // <-> Typ
  *N PACKED(3);                                                          // <-> Sequenz
  *N CHAR(10);                                                           // <-- Rückkehrcode
END-PR;
