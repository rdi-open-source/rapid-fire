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

DCL-PR Who_Am_I
       CHAR(300);                                                        // <-- I Am
END-PR;

DCL-PR Who_Am_I_With_Call_Stack_Entry
       CHAR(300);                                                        // <-- I Am
  *N CHAR(10) Const;                                                     // --> Program
  *N CHAR(10) Const;                                                     // --> Modul
  *N CHAR(256) Const;                                                    // --> Procedure
END-PR;

DCL-PR Who_Am_I_Internal_2
       ExtProc('WHOAMI2');
  *N CHAR(300);                                                          // <-- I Am
END-PR;

DCL-PR Who_Am_I_Internal_3
       ExtProc('WHOAMI3');
  *N CHAR(10) Const;                                                     // --> Program
  *N CHAR(10) Const;                                                     // --> Modul
  *N CHAR(256) Const;                                                    // --> Procedure
  *N CHAR(300);                                                          // <-- I Am
END-PR;
