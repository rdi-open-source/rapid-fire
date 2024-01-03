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

DCL-PR MNTJOB_openFiles
       EXTPROC('MNTJOB_openFiles');
END-PR;

DCL-PR MNTJOB_closeFiles
       EXTPROC('MNTJOB_closeFiles');
END-PR;

DCL-PR MNTJOB_initialize IND // <-- Success
       EXTPROC('MNTJOB_initialize');
  *N CHAR(10) CONST;         // --> Mode
  *N CHAR(10) CONST;         // --> Job
  *N CHAR(1);                // <-- Success
  *N CHAR(256);              // <-- Message
END-PR;

DCL-PR MNTJOB_getValues
       EXTPROC('MNTJOB_getValues');
  *N CHAR(10); // <-- Job
  *N CHAR(35); // <-- Description
  *N CHAR(10); // <-- Create environment
  *N CHAR(10); // <-- Job queue
  *N CHAR(10); // <-- Job queue (Library)
  *N CHAR(10); // <-- Cancel, if ASP threshold exceeds
END-PR;

DCL-PR MNTJOB_setValues
       EXTPROC('MNTJOB_setValues');
  *N CHAR(10) CONST; // --> Job
  *N CHAR(35) CONST; // --> Description
  *N CHAR(10) CONST; // --> Create environment
  *N CHAR(10) CONST; // --> Job queue
  *N CHAR(10) CONST; // --> Job queue (Library)
  *N CHAR(10) CONST; // --> Cancel, if ASP threshold exceeds
END-PR;

DCL-PR MNTJOB_check IND // <-- Success
       EXTPROC('MNTJOB_check');
  *N CHAR(1);           // <-- Success
  *N CHAR(10);          // <-- Field
  *N CHAR(256);         // <-- Message
END-PR;

DCL-PR MNTJOB_book
       EXTPROC('MNTJOB_book');
END-PR;

DCL-PR MNTJOB_checkAction IND // <-- Success
       EXTPROC('MNTJOB_checkAction');
  *N CHAR(10) CONST;          // --> Action
  *N CHAR(10) CONST;          // --> Job
  *N CHAR(1);                 // <-- Success
  *N CHAR(256);               // <-- Message
END-PR;

DCL-PR MNTJOB_getValidActions
       EXTPROC('MNTJOB_getValidActions');
  *N CHAR(10) CONST;   // --> Job
  *N PACKED(2 : 0);    // <-- Number of actions
  *N CHAR(10) DIM(25); // <-- Actions
END-PR;

DCL-C MNTJOB_ERROR     '*ERROR';
DCL-C MNTJOB_NONE      '*NONE';
DCL-C MNTJOB_SELECT    '*SELECT';
DCL-C MNTJOB_CREATE    '*CREATE';
DCL-C MNTJOB_COPY      '*COPY';
DCL-C MNTJOB_CHANGE    '*CHANGE';
DCL-C MNTJOB_DELETE    '*DELETE';
DCL-C MNTJOB_DISPLAY   '*DISPLAY';
DCL-C MNTJOB_CHKACT    '*CHKACT';
DCL-C MNTJOB_MNTLIB    '*MNTLIB';
DCL-C MNTJOB_MNTLIBL   '*MNTLIBL';
DCL-C MNTJOB_MNTFILE   '*MNTFILE';
DCL-C MNTJOB_MNTSTBN   '*MNTSTBN';
DCL-C MNTJOB_MNTAS     '*MNTAS';
DCL-C MNTJOB_TSTJOB    '*TSTJOB';
DCL-C MNTJOB_STRJOB    '*STRJOB';
DCL-C MNTJOB_ENDJOB    '*ENDJOB';
DCL-C MNTJOB_RESETJOB  '*RESETJOB';
DCL-C MNTJOB_RESETJOBA '*RESETJOBA';
DCL-C MNTJOB_DSPSTS    '*DSPSTS';
DCL-C MNTJOB_DSPERR    '*DSPERR';
