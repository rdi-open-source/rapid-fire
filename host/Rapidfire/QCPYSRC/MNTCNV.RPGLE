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
  *N PACKED(6 : 0) CONST;    // --> Position
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

DCL-PR MNTCNV_checkAction IND // <-- Success
       EXTPROC('MNTCNV_checkAction');
  *N CHAR(10) CONST;           // --> Action
  *N CHAR(10) CONST;           // --> Job
  *N PACKED(6 : 0) CONST;      // --> Position
  *N CHAR(10) CONST;           // --> Field to convert
  *N CHAR(1);                  // <-- Success
  *N CHAR(256);                // <-- Message
END-PR;

DCL-PR MNTCNV_getValidActions
       EXTPROC('MNTCNV_getValidActions');
  *N CHAR(10) CONST;      // --> Job
  *N PACKED(6 : 0) CONST; // --> Position
  *N CHAR(10) CONST;      // --> Field to convert
  *N PACKED(2 : 0);       // <-- Number of actions
  *N CHAR(10) DIM(25);    // <-- Actions
END-PR;

DCL-C MNTCNV_ERROR   '*ERROR';
DCL-C MNTCNV_NONE    '*NONE';
DCL-C MNTCNV_SELECT  '*SELECT';
DCL-C MNTCNV_CREATE  '*CREATE';
DCL-C MNTCNV_COPY    '*COPY';
DCL-C MNTCNV_CHANGE  '*CHANGE';
DCL-C MNTCNV_DELETE  '*DELETE';
DCL-C MNTCNV_DISPLAY '*DISPLAY';
DCL-C MNTCNV_CHKACT  '*CHKACT';
