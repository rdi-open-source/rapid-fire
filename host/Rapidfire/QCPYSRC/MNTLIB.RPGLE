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

DCL-PR MNTLIB_checkAction IND // <-- Success
       EXTPROC('MNTLIB_checkAction');
  *N CHAR(10) CONST;           // --> Action
  *N CHAR(10) CONST;           // --> Job
  *N CHAR(10) CONST;           // --> Library
  *N CHAR(1);                  // <-- Success
  *N CHAR(256);                // <-- Message
END-PR;

DCL-PR MNTLIB_getValidActions
       EXTPROC('MNTLIB_getValidActions');
  *N CHAR(10) CONST;   // --> Job
  *N CHAR(10) CONST;   // --> Library
  *N PACKED(2 : 0);    // <-- Number of actions
  *N CHAR(10) DIM(25); // <-- Actions
END-PR;

DCL-C MNTLIB_ERROR   '*ERROR';
DCL-C MNTLIB_NONE    '*NONE';
DCL-C MNTLIB_SELECT  '*SELECT';
DCL-C MNTLIB_CREATE  '*CREATE';
DCL-C MNTLIB_COPY    '*COPY';
DCL-C MNTLIB_CHANGE  '*CHANGE';
DCL-C MNTLIB_DELETE  '*DELETE';
DCL-C MNTLIB_DISPLAY '*DISPLAY';
DCL-C MNTLIB_CHKACT  '*CHKACT';
