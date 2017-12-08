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

DCL-PR MNTCMD_openFiles
       EXTPROC('MNTCMD_openFiles');
END-PR;

DCL-PR MNTCMD_closeFiles
       EXTPROC('MNTCMD_closeFiles');
END-PR;

DCL-PR MNTCMD_initialize IND // <-- Success
       EXTPROC('MNTCMD_initialize');
  *N CHAR(10) CONST;            // --> Mode
  *N  CHAR(10) CONST;           // --> Job
  *N PACKED(6) CONST;           // --> Position
  *N CHAR(10) CONST;            // --> Type
  *N PACKED(3) CONST;           // --> Sequence
  *N CHAR(1);                   // <-- Success
  *N CHAR(256);                 // <-- Message
END-PR;

DCL-PR MNTCMD_getValues
       EXTPROC('MNTCMD_getValues');
  *N CHAR(10);   // <-- Type
  *N PACKED(3);  // <-- Sequence
  *N CHAR(1024); // <-- Command
END-PR;

DCL-PR MNTCMD_setValues
       EXTPROC('MNTCMD_setValues');
  *N CHAR(10) CONST;   // <-- Type
  *N PACKED(3) CONST;  // <-- Sequence
  *N CHAR(1024) CONST; // <-- Command
END-PR;

DCL-PR MNTCMD_check IND // <-- Success
       EXTPROC('MNTCMD_check');
  *N CHAR(1);           // <-- Success
  *N CHAR(10);          // <-- Field
  *N CHAR(256);         // <-- Message
END-PR;

DCL-PR MNTCMD_book
       EXTPROC('MNTCMD_book');
END-PR;
