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

DCL-PR GNRCPYPGM_openFiles
       EXTPROC('GNRCPYPGM_openFiles');
END-PR;

DCL-PR GNRCPYPGM_closeFiles
       EXTPROC('GNRCPYPGM_closeFiles');
END-PR;

DCL-PR GNRCPYPGM_initialize IND // <-- Success
       EXTPROC('GNRCPYPGM_initialize');
    *N CHAR(10) CONST;          // --> Job
    *N PACKED(6 : 0) CONST;     // --> Position
    *N CHAR(1);                 // <-- Success
    *N CHAR(256);               // <-- Message
END-PR;

DCL-PR GNRCPYPGM_getValues
       EXTPROC('GNRCPYPGM_getValues');
    *N CHAR(10); // <-- Source file
    *N CHAR(10); // <-- Source file library
    *N CHAR(10); // <-- Source member
    *N CHAR(10); // <-- Replace
    *N CHAR(10); // <-- Area
    *N CHAR(10); // <-- Library
    *N CHAR(10); // <-- Shadow library
    *N CHAR(10); // <-- Conversion program
    *N CHAR(10); // <-- Conversion program library
END-PR;

DCL-PR GNRCPYPGM_setValues
       EXTPROC('GNRCPYPGM_setValues');
    *N CHAR(10) CONST; // --> Source file
    *N CHAR(10) CONST; // --> Source file library
    *N CHAR(10) CONST; // --> Source member
    *N CHAR(10) CONST; // --> Replace
    *N CHAR(10) CONST; // --> Area
    *N CHAR(10) CONST; // --> Library
    *N CHAR(10) CONST; // --> Shadow library
    *N CHAR(10) CONST; // --> Conversion program
    *N CHAR(10) CONST; // --> Conversion program library
END-PR;

DCL-PR GNRCPYPGM_check IND // <-- Success
       EXTPROC('GNRCPYPGM_check');
  *N CHAR(1);              // <-- Success
  *N CHAR(10);             // <-- Field
  *N CHAR(256);            // <-- Message
END-PR;

DCL-PR GNRCPYPGM_book IND // <-- Success
       EXTPROC('GNRCPYPGM_book');
  *N CHAR(1);             // <-- Success
  *N CHAR(256);           // <-- Message
END-PR;
