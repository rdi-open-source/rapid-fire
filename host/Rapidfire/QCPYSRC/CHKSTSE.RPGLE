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

DCL-PR CHKSTSE_openFiles
       EXTPROC('CHKSTSE_openFiles');
END-PR;

DCL-PR CHKSTSE_closeFiles
       EXTPROC('CHKSTSE_closeFiles');
END-PR;

DCL-PR CHKSTSE_checkAction IND // <-- Success
       EXTPROC('CHKSTSE_checkAction');
  *N CHAR(10) CONST;           // --> Action
  *N CHAR(10) CONST;           // --> Job
  *N PACKED(6 : 0) CONST;      // --> Position
  *N CHAR(10) CONST;           // --> Area
  *N CHAR(1);                  // <-- Success
  *N CHAR(256);                // <-- Message
END-PR;

DCL-PR CHKSTSE_getValidActions
       EXTPROC('CHKSTSE_getValidActions');
  *N CHAR(10) CONST;      // --> Job
  *N PACKED(6 : 0) CONST; // --> Position
  *N CHAR(10) CONST;      // --> Area
  *N PACKED(2 : 0);       // <-- Number of actions
  *N CHAR(10) DIM(25);    // <-- Actions
END-PR;

DCL-C CHKSTSE_ERROR        '*ERROR';
DCL-C CHKSTSE_NONE         '*NONE';
DCL-C CHKSTSE_DSPRCDDUPKEY '*DSPDUPKEY';
DCL-C CHKSTSE_REAPYCHG     '*REAPYCHG';
