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

DCL-DS CMTI0100 Qualified;
  Bytes_Returned
  INT(10);                                                               // <-- Bytes returne
  Bytes_Available
  INT(10);                                                               // <-- Bytes availab
  Commit_Definition_Status
  CHAR(1);                                                               // <-- Cmt. def. sta

  *N CHAR(10);                                                           // <-- Cmt. def. def

  *N CHAR(1);                                                            // <-- Cmt. def. sco

  *N CHAR(1);                                                            // <-- Cmt. def. sta

  *N CHAR(1);                                                            // <-- Wait for outc

  *N CHAR(1);                                                            // <-- Action if pro

  *N CHAR(1);                                                            // <-- Vote read-onl

  *N CHAR(1);                                                            // <-- Action if END

  *N CHAR(10);                                                           // <-- Default journ

  *N CHAR(10);                                                           // <-- Default journ

  *N CHAR(39);                                                           // <-- Logical unit

  *N CHAR(1);                                                            // <-- Last agent pe

  *N CHAR(1);                                                            // <-- OK to leave o

  *N CHAR(1);                                                            // <-- Accept vote r
END-DS;
