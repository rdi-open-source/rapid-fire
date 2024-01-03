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

DCL-PR MNTAS_setActivity
       EXTPROC('MNTAS_setActivity');
  *N CHAR(10) CONST; // --> Job
  *N TIME CONST;     // --> Start time
END-PR;

DCL-PR MNTAS_checkAction IND // <-- Success
       EXTPROC('MNTAS_checkAction');
  *N CHAR(10) CONST;         // --> Action
  *N CHAR(10) CONST;         // --> Job
  *N TIME CONST;             // --> Start time
  *N CHAR(1);                // <-- Success
  *N CHAR(256);              // <-- Message
END-PR;

DCL-PR MNTAS_getValidActions
       EXTPROC('MNTAS_getValidActions');
  *N CHAR(10) CONST;   // --> Job
  *N TIME CONST;       // --> Start time
  *N PACKED(2 : 0);    // <-- Number of actions
  *N CHAR(10) DIM(25); // <-- Actions
END-PR;

DCL-C MNTAS_ERROR  '*ERROR';
DCL-C MNTAS_NONE   '*NONE';
DCL-C MNTAS_ACTVTY '*ACTVTY';
