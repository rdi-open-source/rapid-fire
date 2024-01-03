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

DCL-PR CPLSTSE_open
       EXTPROC('CPLSTSE_open');
END-PR;

DCL-PR CPLSTSE_close
       EXTPROC('CPLSTSE_close');
END-PR;

DCL-PR CPLSTSE_completeStatusEntry
       EXTPROC('CPLSTSE_completeStatusEntry');
  *N CHAR(10)       CONST; // --> Job
  *N PACKED(6 : 0)  CONST; // --> Position
  *N CHAR(10)       CONST; // --> Area
  *N CHAR(10);             // <-- File
  *N CHAR(10);             // <-- Library
  *N PACKED(10 : 0);       // <-- Records in production library
  *N PACKED(10 : 0);       // <-- Records in shadow library
  *N PACKED(10 : 0);       // <-- Records to copy
  *N PACKED(10 : 0);       // <-- Records copied
  *N CHAR(11);             // <-- Estimated time
  *N PACKED(10 : 0);       // <-- Records with duplicate key
  *N PACKED(10 : 0);       // <-- Changes to apply
  *N PACKED(10 : 0);       // <-- Changes applied
  *N PACKED(3 : 0);        // <-- Percent done
END-PR;
