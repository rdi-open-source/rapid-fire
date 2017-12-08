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

DCL-PR MNTCNV_openFiles
       EXTPROC('MNTCNV_openFiles');
END-PR;

DCL-PR MNTCNV_closeFiles
       EXTPROC('MNTCNV_closeFiles');
END-PR;

DCL-PR MNTCNV_initialize IND // <-- Success
       EXTPROC('MNTCNV_initialize');
  *N CHAR(10) CONST;         // --> Mode
  *N CHAR(10) CONST;         // --> Job
  *N PACKED(6) CONST;        // --> Position
  *N CHAR(10) CONST;         // --> Field to convert
  *N CHAR(1);                // <-- Success
  *N CHAR(256);              // <-- Message
END-PR;

DCL-PR MNTCNV_getValues
       EXTPROC('MNTCNV_getValues');
  *N CHAR(10); // <-- Field to convert
  *N CHAR(10); // <-- Rename field in old file to
  *N CHAR(60); // <-- Statement
  *N CHAR(60); // <-- Statement
  *N CHAR(60); // <-- Statement
  *N CHAR(60); // <-- Statement
  *N CHAR(60); // <-- Statement
  *N CHAR(60); // <-- Statement
END-PR;

DCL-PR MNTCNV_setValues
       EXTPROC('MNTCNV_setValues');
  *N CHAR(10) CONST; // <-- Field to convert
  *N CHAR(10) CONST; // <-- Rename field in old file to
  *N CHAR(60) CONST; // <-- Statement
  *N CHAR(60) CONST; // <-- Statement
  *N CHAR(60) CONST; // <-- Statement
  *N CHAR(60) CONST; // <-- Statement
  *N CHAR(60) CONST; // <-- Statement
  *N CHAR(60) CONST; // <-- Statement
END-PR;

DCL-PR MNTCNV_check IND // <-- Success
       EXTPROC('MNTCNV_check');
  *N CHAR(1);           // <-- Success
  *N CHAR(10);          // <-- Field
  *N CHAR(256);         // <-- Message
END-PR;

DCL-PR MNTCNV_book
       EXTPROC('MNTCNV_book');
END-PR;
