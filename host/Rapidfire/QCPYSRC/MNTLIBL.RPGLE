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

DCL-PR MNTLIBL_openFiles
       EXTPROC('MNTLIBL_openFiles');
END-PR;

DCL-PR MNTLIBL_closeFiles
       EXTPROC('MNTLIBL_closeFiles');
END-PR;

DCL-PR MNTLIBL_initialize IND // <-- Success
       EXTPROC('MNTLIBL_initialize');
  *N CHAR(10) CONST;          // --> Mode
  *N CHAR(10) CONST;          // --> Job
  *N CHAR(10) CONST;          // --> Library list
  *N CHAR(1);                 // <-- Success
  *N CHAR(256);               // <-- Message
END-PR;

DCL-PR MNTLIBL_getValues
       EXTPROC('MNTLIBL_getValues');
  *N CHAR(10);              // <-- Library list
  *N CHAR(35);              // <-- Description
  *N ZONED(4 : 0) DIM(250); // <-- Sequence
  *N CHAR(10) DIM(250);     // <-- Library
END-PR;

DCL-PR MNTLIBL_getLibraryList
       EXTPROC('MNTLIBL_getLibraryList');
  *N ZONED(4 : 0) DIM(250); // <-- Sequence
  *N CHAR(10) DIM(250);     // <-- Library
END-PR;

DCL-PR MNTLIBL_setValues
       EXTPROC('MNTLIBL_setValues');
  *N CHAR(10) CONST;              // --> Library list
  *N CHAR(35) CONST;              // --> Description
  *N ZONED(4 : 0) DIM(250) CONST; // --> Sequence
  *N CHAR(10) DIM(250) CONST;     // --> Library
END-PR;

DCL-PR MNTLIBL_check IND // <-- Success
       EXTPROC('MNTLIBL_check');
  *N CHAR(1);            // <-- Success
  *N CHAR(10);           // <-- Field
  *N PACKED(4 : 0);      // <-- Record
  *N CHAR(256);          // <-- Message
END-PR;

DCL-PR MNTLIBL_book
       EXTPROC('MNTLIBL_book');
END-PR;
