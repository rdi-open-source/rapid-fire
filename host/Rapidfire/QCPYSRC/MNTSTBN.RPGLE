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

DCL-PR MNTSTBN_openFiles
       EXTPROC('MNTSTBN_openFiles');
END-PR;

DCL-PR MNTSTBN_closeFiles
       EXTPROC('MNTSTBN_closeFiles');
END-PR;

DCL-PR MNTSTBN_initialize IND // <-- Success
       EXTPROC('MNTSTBN_initialize');
  *N CHAR(10) CONST;          // --> Mode
  *N CHAR(10) CONST;          // --> Job
  *N PACKED(3 : 0) CONST;     // --> Position
  *N CHAR(1);                 // <-- Success
  *N CHAR(256);               // <-- Message
END-PR;

DCL-PR MNTSTBN_getValues
       EXTPROC('MNTSTBN_getValues');
  *N PACKED(3 : 0); // <-- Position
  *N CHAR(10);      // <-- Type
  *N CHAR(10);      // <-- User
  *N CHAR(10);      // <-- Message queue (Library)
  *N CHAR(10);      // <-- Message queue
END-PR;

DCL-PR MNTSTBN_setValues
       EXTPROC('MNTSTBN_setValues');
  *N PACKED(3 : 0) CONST; // <-- Position
  *N CHAR(10) CONST;      // <-- Type
  *N CHAR(10) CONST;      // <-- User
  *N CHAR(10) CONST;      // <-- Message queue (Library)
  *N CHAR(10) CONST;      // <-- Message queue
END-PR;

DCL-PR MNTSTBN_check IND // <-- Success
       EXTPROC('MNTSTBN_check');
  *N CHAR(1);            // <-- Success
  *N CHAR(10);           // <-- Field
  *N CHAR(256);          // <-- Message
END-PR;

DCL-PR MNTSTBN_book
       EXTPROC('MNTSTBN_book');
END-PR;
