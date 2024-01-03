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
//*                                                                                         *
//* Maintain Activity Schedule                                                              *
//*                                                                                         *
//*-----------------------------------------------------------------------------------------*
//*  >>PRE-COMPILER<<                                                                       *
//*    >>CRTCMD<< CRTSQLRPGI   OBJ(&LI/&OB) +                                               *
//*                              SRCFILE(&SL/&SF) SRCMBR(&SM) +                             *
//*                              OBJTYPE(*MODULE);                                          *
//*    >>IMPORTANT<<                                                                        *
//*      >>PARM<<  OUTPUT(*PRINT);                                                          *
//*      >>PARM<<  GENLVL(0);                                                               *
//*      >>PARM<<  DBGVIEW(*SOURCE);                                                        *
//*      >>PARM<<  OPTION(*EVENTF);                                                         *
//*      >>PARM<<  TGTRLS(&TR);                                                             *
//*    >>END-IMPORTANT<<                                                                    *
//*    >>EXECUTE<<                                                                          *
//*  >>END-PRE-COMPILER<<                                                                   *
//*------------+--------+-------------------------------------------------------------------*
//*   Datum    I Progr. I Beschreibung                                                      *
//*------------+--------+-------------------------------------------------------------------*
//* 24.11.2017 I   FH   I Creation                                                          *
//*------------+--------+-------------------------------------------------------------------*

CTL-OPT NOMAIN;
CTL-OPT DATFMT(*ISO) TIMFMT(*ISO) DECEDIT('0,');
CTL-OPT COPYRIGHT('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT BNDDIR('RAPIDFIRE');
CTL-OPT EXTBININT(*YES);

//*-----------------------------------------------------------------------------------------*

/INCLUDE QCPYSRC,MNTJOB

/INCLUDE QCPYSRC,MNTAS

/INCLUDE QCPYSRC,RAPIDFIRE

/INCLUDE QCPYSRC,SQLSTS

/INCLUDE QCPYSRC,RTVMSGD

//*-----------------------------------------------------------------------------------------*

// Set SQL options

Exec SQL
  Set Option
  DatFmt=*ISO,
  TimFmt=*ISO,
  Commit=*CHG,
  CloSQLCsr=*ENDMOD;

//*=========================================================================================*
//* Set Activity                                                                            *
//*=========================================================================================*

DCL-PROC MNTAS_setActivity EXPORT;

  DCL-PI MNTAS_setActivity;
    p_JOB CHAR(10) CONST; // --> Job
    p_STR TIME CONST;     // --> Start time
  END-PI;

  Exec SQL
    Update Activity_Schedule
    Set Activity = Case When Activity = 'Y' Then 'N' Else 'Y' End
    Where Job = :p_JOB And Start_Time = :p_STR;
  setIgnoreWarnings();
  SqlCheckState(sqlState);

  if RAPIDFIRE_getAutoCommit() = 'Y';
    commit;
  endif;

END-PROC MNTAS_setActivity;

//*=========================================================================================*
//* Check Action                                                                            *
//*=========================================================================================*

DCL-PROC MNTAS_checkAction EXPORT;

  DCL-PI MNTAS_checkAction IND; // <-- Success
    p_ACT     CHAR(10) CONST;   // --> Action
    p_JOB     CHAR(10) CONST;   // --> Job
    p_STR     TIME CONST;       // --> Start time
    p_SUCCESS CHAR(1);          // <-- Success
    p_MSG     CHAR(256);        // <-- Message
  END-PI;

  DCL-S l_MSGID   CHAR(7);   // Message-Id.
  DCL-S l_MSGDTA  CHAR(256); // Message data
  DCL-S l_SUCCESS CHAR(1);   // Success
  DCL-S l_MSG     CHAR(256); // Message
  DCL-S l_OPN     IND;       // Open indicator

  // Initialize returning fields
  p_MSG = *blanks;

  // Initialize message fields
  l_MSGID = *blanks;
  l_MSGDTA = *blanks;

  dou (1 = 1);

    // The action is not valid for such type of jobs
    if (not MNTJOB_checkAction(
              MNTJOB_MNTAS : // --> Action
              p_JOB :        // --> Job
              l_SUCCESS :    // <-- Success
              l_MSG          // <-- Message
            )
       );
      l_MSGID = 'ERR0001';
      l_MSGDTA = l_MSG;
      leave;
    endif;

  enddo;

  // Return
  if (l_MSGID = *blanks);

    p_SUCCESS = 'Y';
    return *on;

  else;

    p_MSG = rtvMsgText(
      l_MSGID : // --> Message-Id.
      'MNTAS' : // --> Message file
      '*LIBL' : // --> Library
      l_MSGDTA  // --> Message data
    );

    p_SUCCESS = 'N';
    return *off;

  endif;

END-PROC MNTAS_checkAction;

//*=========================================================================================*
//* Get Valid Actions                                                                       *
//*=========================================================================================*

DCL-PROC MNTAS_getValidActions EXPORT;

  DCL-PI MNTAS_getValidActions;
    p_JOB     CHAR(10) CONST;   // --> Job
    p_STR     TIME CONST;       // --> Start time
    p_NBRACT  PACKED(2 : 0);    // <-- Number of actions
    p_ACT     CHAR(10) DIM(25); // <-- Actions
  END-PI;

  DCL-S l_ACT     CHAR(10);      // --> Action
  DCL-S l_SUCCESS CHAR(1);       // <-- Success
  DCL-S l_MSG     CHAR(256);     // <-- Message

  p_NBRACT = *zero;
  p_ACT(*) = *blanks;

  l_ACT = MNTAS_ACTVTY;
    exsr checkAction;

  return;

  begsr checkAction;

    if (MNTAS_checkAction(
          l_ACT :     // --> Action
          p_JOB :     // --> Job
          p_STR :     // --> Start time
          l_SUCCESS : // <-- Success
          l_MSG       // <-- Message
        ));
      p_NBRACT += 1;
      p_ACT(p_NBRACT) = l_ACT;
    endif;

  endsr;

END-PROC MNTAS_getValidActions;
