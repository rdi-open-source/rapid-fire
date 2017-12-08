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

DCL-PR MNTLIB_openFiles
       EXTPROC('MNTLIB_openFiles');
END-PR;

DCL-PR MNTLIB_closeFiles
       EXTPROC('MNTLIB_closeFiles');
END-PR;

DCL-PR MNTLIB_initialize IND // <-- Success
       EXTPROC('MNTLIB_initialize');
  *N CHAR(10) CONST;         // --> Mode
  *N CHAR(10) CONST;         // --> Job
  *N CHAR(10) CONST;         // --> Library
  *N CHAR(1);                // <-- Success
  *N CHAR(256);              // <-- Message
END-PR;

DCL-PR MNTLIB_getValues
       EXTPROC('MNTLIB_getValues');
  *N CHAR(10); // <-- Library
  *N CHAR(10); // <-- Shadow library
END-PR;

DCL-PR MNTLIB_setValues
       EXTPROC('MNTLIB_setValues');
  *N CHAR(10) CONST; // <-- Library
  *N CHAR(10) CONST; // <-- Shadow library
END-PR;

DCL-PR MNTLIB_check IND // <-- Success
       EXTPROC('MNTLIB_check');
  *N CHAR(1);           // <-- Success
  *N CHAR(10);          // <-- Field
  *N CHAR(256);         // <-- Message
END-PR;

DCL-PR MNTLIB_book
       EXTPROC('MNTLIB_book');
END-PR;
