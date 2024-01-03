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

DCL-PR MNTAREA_openFiles
       EXTPROC('MNTAREA_openFiles');
END-PR;

DCL-PR MNTAREA_closeFiles
       EXTPROC('MNTAREA_closeFiles');
END-PR;

DCL-PR MNTAREA_initialize IND // <-- Success
       EXTPROC('MNTAREA_initialize');
  *N CHAR(10) CONST;          // --> Mode
  *N CHAR(10) CONST;          // --> Job
  *N PACKED(6 : 0) CONST;     // --> Position
  *N CHAR(10) CONST;          // --> Area
  *N CHAR(1);                 // <-- Success
  *N CHAR(256);               // <-- Message
END-PR;

DCL-PR MNTAREA_getValues
       EXTPROC('MNTAREA_getValues');
  *N CHAR(10);   // <-- Area
  *N CHAR(10);   // <-- Library
  *N CHAR(10);   // <-- Library list
  *N CHAR(10);   // <-- Coded character set ID
  *N CHAR(1024); // <-- Command extension
END-PR;

DCL-PR MNTAREA_setValues
       EXTPROC('MNTAREA_setValues');
  *N CHAR(10) CONST;   // <-- Area
  *N CHAR(10) CONST;   // <-- Library
  *N CHAR(10) CONST;   // <-- Library list
  *N CHAR(10) CONST;   // <-- Coded character set ID
  *N CHAR(1024) CONST; // <-- Command extension
END-PR;

DCL-PR MNTAREA_check IND // <-- Success
       EXTPROC('MNTAREA_check');
  *N CHAR(1);            // <-- Success
  *N CHAR(10);           // <-- Field
  *N CHAR(256);          // <-- Message
END-PR;

DCL-PR MNTAREA_book
       EXTPROC('MNTAREA_book');
END-PR;

DCL-PR MNTAREA_checkAction IND // <-- Success
       EXTPROC('MNTAREA_checkAction');
  *N CHAR(10) CONST;           // --> Action
  *N CHAR(10) CONST;           // --> Job
  *N PACKED(6 : 0) CONST;      // --> Position
  *N CHAR(10) CONST;           // --> Area
  *N CHAR(1);                  // <-- Success
  *N CHAR(256);                // <-- Message
END-PR;

DCL-PR MNTAREA_getValidActions
       EXTPROC('MNTAREA_getValidActions');
  *N CHAR(10) CONST;      // --> Job
  *N PACKED(6 : 0) CONST; // --> Position
  *N CHAR(10) CONST;      // --> Area
  *N PACKED(2 : 0);       // <-- Number of actions
  *N CHAR(10) DIM(25);    // <-- Actions
END-PR;

DCL-C MNTAREA_ERROR   '*ERROR';
DCL-C MNTAREA_NONE    '*NONE';
DCL-C MNTAREA_SELECT  '*SELECT';
DCL-C MNTAREA_CREATE  '*CREATE';
DCL-C MNTAREA_COPY    '*COPY';
DCL-C MNTAREA_CHANGE  '*CHANGE';
DCL-C MNTAREA_DELETE  '*DELETE';
DCL-C MNTAREA_DISPLAY '*DISPLAY';
DCL-C MNTAREA_CHKACT  '*CHKACT';
