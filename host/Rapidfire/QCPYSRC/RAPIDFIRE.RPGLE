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

DCL-C RAPIDFIRE_JOB_NONE
      '*NONE';

DCL-PR RAPIDFIRE_log_Error;
  *N CHAR(10) Const;                                                     // --> Job
  *N CHAR(7) Const;                                                      // --> Message Id 1
  *N CHAR(256) Const;                                                    // --> Message Data
  *N CHAR(7) Const;                                                      // --> Message Id 2
  *N CHAR(256) Const;                                                    // --> Message Data
END-PR;

DCL-PR RAPIDFIRE_is_Error
       IND;
  *N CHAR(10) Const;                                                     // --> Job
END-PR;

DCL-PR RAPIDFIRE_set_Error;
  *N CHAR(10) Const;                                                     // --> Job
  *N IND Const;                                                          // --> Error
END-PR;

DCL-PR RAPIDFIRE_start IND // <-- Success
       EXTPROC('RAPIDFIRE_start');
  *N CHAR(1);   // <-- Success
  *N CHAR(256); // <-- Message
END-PR;

DCL-PR RAPIDFIRE_stop IND // <-- Success
       EXTPROC('RAPIDFIRE_stop');
  *N CHAR(1);   // <-- Success
  *N CHAR(256); // <-- Message
END-PR;
