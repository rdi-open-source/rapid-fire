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

DCL-PR MNTFILE_openFiles
       EXTPROC('MNTFILE_openFiles');
END-PR;

DCL-PR MNTFILE_closeFiles
       EXTPROC('MNTFILE_closeFiles');
END-PR;

DCL-PR MNTFILE_initialize IND // <-- Success
       EXTPROC('MNTFILE_initialize');
  *N CHAR(10) CONST;          // --> Mode
  *N CHAR(10) CONST;          // --> Job
  *N PACKED(6 : 0) CONST;     // --> Position
  *N CHAR(1);                 // <-- Success
  *N CHAR(256);               // <-- Message
END-PR;

DCL-PR MNTFILE_getValues
       EXTPROC('MNTFILE_getValues');
  *N PACKED(6 : 0); // <-- Position
  *N CHAR(10);      // <-- File
  *N CHAR(10);      // <-- Type
  *N CHAR(10);      // <-- Copy program (Library)
  *N CHAR(10);      // <-- Copy program
  *N CHAR(10);      // <-- Conversion program (Library)
  *N CHAR(10);      // <-- Conversion program
END-PR;

DCL-PR MNTFILE_setValues
       EXTPROC('MNTFILE_setValues');
  *N PACKED(6 : 0) CONST; // <-- Position
  *N CHAR(10) CONST;      // <-- File
  *N CHAR(10) CONST;      // <-- Type
  *N CHAR(10) CONST;      // <-- Copy program (Library)
  *N CHAR(10) CONST;      // <-- Copy program
  *N CHAR(10) CONST;      // <-- Conversion program (Library)
  *N CHAR(10) CONST;      // <-- Conversion program
END-PR;

DCL-PR MNTFILE_check IND // <-- Success
       EXTPROC('MNTFILE_check');
  *N CHAR(1);            // <-- Success
  *N CHAR(10);           // <-- Field
  *N CHAR(256);          // <-- Message
END-PR;

DCL-PR MNTFILE_book
       EXTPROC('MNTFILE_book');
END-PR;

DCL-PR MNTFILE_checkAction IND // <-- Success
       EXTPROC('MNTFILE_checkAction');
  *N CHAR(10) CONST;           // --> Action
  *N CHAR(10) CONST;           // --> Job
  *N PACKED(6 : 0) CONST;      // --> Position
  *N CHAR(1);                  // <-- Success
  *N CHAR(256);                // <-- Message
END-PR;

DCL-PR MNTFILE_getValidActions
       EXTPROC('MNTFILE_getValidActions');
  *N CHAR(10) CONST;      // --> Job
  *N PACKED(6 : 0) CONST; // --> Position
  *N PACKED(2 : 0);       // <-- Number of actions
  *N CHAR(10) DIM(25);    // <-- Actions
END-PR;

DCL-C MNTFILE_ERROR     '*ERROR';
DCL-C MNTFILE_NONE      '*NONE';
DCL-C MNTFILE_SELECT    '*SELECT';
DCL-C MNTFILE_CREATE    '*CREATE';
DCL-C MNTFILE_COPY      '*COPY';
DCL-C MNTFILE_CHANGE    '*CHANGE';
DCL-C MNTFILE_DELETE    '*DELETE';
DCL-C MNTFILE_DISPLAY   '*DISPLAY';
DCL-C MNTFILE_CHKACT    '*CHKACT';
DCL-C MNTFILE_MNTARA    '*MNTARA';
DCL-C MNTFILE_MNTCNV    '*MNTCNV';
DCL-C MNTFILE_MNTCMD    '*MNTCMD';
DCL-C MNTFILE_GNRCPYPGM '*GNRCPYPGM';
