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
DCL-DS RRCV0100 Qualified;
  Bytes_Returned
  INT(10);
  Bytes_Available
  INT(10);
END-DS;

// General information
DCL-DS RRCV0100_A Based(RRCV0100_A_Pointer) Qualified;
  Bytes_Returned
  INT(10);
  Bytes_Available
  INT(10);
  Filler_1
  CHAR(404);
  First_Sequence_Number
  ZONED(20);
  Last_Sequence_Number
  ZONED(20);
  Filler_2
  CHAR(60);
END-DS;

DCL-S RRCV0100_A_Pointer
      POINTER;
