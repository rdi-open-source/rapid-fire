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

// Header
DCL-DS RJRN0100 Qualified;
  Bytes_Returned
  INT(10);
  Bytes_Available
  INT(10);
END-DS;

// General information
DCL-DS RJRN0100_A Based(RJRN0100_A_Pointer) Qualified;
  Bytes_Returned
  INT(10);
  Bytes_Available
  INT(10);
  Filler_1
  CHAR(192);
  Journal_Receiver
  CHAR(10);
  Journal_Receiver_Library
  CHAR(10);
  Filler_2
  CHAR(232);
END-DS;

DCL-S RJRN0100_A_Pointer
      POINTER;
