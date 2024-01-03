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
//* Check Status Entry                                                                      *
//*                                                                                         *
//*-----------------------------------------------------------------------------------------*
//*  >>PRE-COMPILER<<                                                                       *
//*    >>CRTCMD<< CRTRPGMOD    MODULE(&LI/&OB) +                                            *
//*                              SRCFILE(&SL/&SF) SRCMBR(&SM);                              *
//*    >>IMPORTANT<<                                                                        *
//*      >>PARM<<  DBGVIEW(*ALL);                                                           *
//*      >>PARM<<  OPTION(*EVENTF);                                                         *
//*      >>PARM<<  TGTRLS(&TR);                                                             *
//*    >>END-IMPORTANT<<                                                                    *
//*    >>EXECUTE<<                                                                          *
//*  >>END-PRE-COMPILER<<                                                                   *
//*------------+--------+-------------------------------------------------------------------*
//*   Datum    I Progr. I Beschreibung                                                      *
//*------------+--------+-------------------------------------------------------------------*
//* 19.12.2017 I   FH   I Creation                                                          *
//*------------+--------+-------------------------------------------------------------------*

CTL-OPT NOMAIN;
CTL-OPT DATFMT(*ISO) TIMFMT(*ISO) DECEDIT('0,');
CTL-OPT COPYRIGHT('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT BNDDIR('RAPIDFIRE');
CTL-OPT EXTBININT(*YES);

//*-----------------------------------------------------------------------------------------*

DCL-F JOBS
      DISK(*EXT)
      USAGE(*INPUT)
      KEYED
      RENAME(JOBS : JOBRF)
      USROPN;

//*-----------------------------------------------------------------------------------------*

/INCLUDE QCPYSRC,CHKSTSE

/INCLUDE QCPYSRC,RTVMSGD

DCL-S g_JOB CHAR(10);      // Job
DCL-S g_OPN IND INZ(*OFF); // Open indicator

//*=========================================================================================*
//* Open Files                                                                              *
//*=========================================================================================*

DCL-PROC CHKSTSE_openFiles EXPORT;

  DCL-PI CHKSTSE_openFiles;
  END-PI;

  Open JOBS;

  g_OPN = *on;

END-PROC CHKSTSE_openFiles;

//*=========================================================================================*
//* Close Files                                                                             *
//*=========================================================================================*

DCL-PROC CHKSTSE_closeFiles EXPORT;

  DCL-PI CHKSTSE_closeFiles;
  END-PI;

  Close JOBS;

  g_OPN = *off;

END-PROC CHKSTSE_closeFiles;

//*=========================================================================================*
//* Initialize                                                                              *
//*=========================================================================================*
//*                                                                                         *
//* Error codes and their meanings                                                          *
//* ==============================                                                          *
//*                                                                                         *
//* '001' = Record in file JOBS not found                                                   *
//*                                                                                         *
//*=========================================================================================*

DCL-PROC CHKSTSE_initialize EXPORT;

  DCL-PI CHKSTSE_initialize IND;   // <-- Success
    p_JOB     CHAR(10) CONST;      // --> Job
    p_SUCCESS CHAR(1);             // <-- Success
    p_MSG     CHAR(256);           // <-- Message
  END-PI;

  g_JOB = p_JOB;

  chain (g_JOB) JOBRF;
  if (not %found);

    p_SUCCESS = 'N';
    p_MSG = '001';
    return *off;

  else;

    p_SUCCESS = 'Y';
    p_MSG = *blanks;
    return *on;

  endif;

END-PROC CHKSTSE_initialize;

//*=========================================================================================*
//* Check Action                                                                            *
//*=========================================================================================*

DCL-PROC CHKSTSE_checkAction EXPORT;

  DCL-PI CHKSTSE_checkAction IND;  // <-- Success
    p_ACT     CHAR(10) CONST;      // --> Action
    p_JOB     CHAR(10) CONST;      // --> Job
    p_POS     PACKED(6 : 0) CONST; // --> Position
    p_ARA     CHAR(10) CONST;      // --> Area
    p_SUCCESS CHAR(1);             // <-- Success
    p_MSG     CHAR(256);           // <-- Message
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

    // Invalid parameter values. Error = &1.
    l_OPN = g_OPN;
    if (not l_OPN);
      CHKSTSE_openFiles();
    endif;
    CHKSTSE_initialize(
      p_JOB :      // --> Job
      l_SUCCESS :  // <-- Success
      l_MSG        // <-- Message
    );
    if (not l_OPN);
      CHKSTSE_closeFiles();
    endif;
    if (l_SUCCESS = 'N');
      l_MSGID = 'ERR0001';
      l_MSGDTA = l_MSG;
      leave;
    endif;

    // The option "Display records with duplicate key" is only valid in status *RUN
    if (p_ACT = CHKSTSE_DSPRCDDUPKEY and
        X1STS <> '*RUN');
      l_MSGID = 'ERR0003';
      leave;
    EndIf;

    // The option "Reapply all changes" is only valid in status *RUN and phase *APY-CHG
    if (p_ACT = CHKSTSE_REAPYCHG and
        not (X1STS = '*RUN' And
        X1PHS = '*APY-CHG'));
      l_MSGID = 'ERR0002';
      leave;
    EndIf;

  enddo;

  // Return
  if (l_MSGID = *blanks);

    p_SUCCESS = 'Y';
    return *on;

  else;

    p_MSG = rtvMsgText(
      l_MSGID :   // --> Message-Id.
      'CHKSTSE' : // --> Message file
      '*LIBL' :   // --> Library
      l_MSGDTA    // --> Message data
    );

    p_SUCCESS = 'N';
    return *off;

  endif;

END-PROC CHKSTSE_checkAction;

//*=========================================================================================*
//* Get Valid Actions                                                                       *
//*=========================================================================================*

DCL-PROC CHKSTSE_getValidActions EXPORT;

  DCL-PI CHKSTSE_getValidActions;
    p_JOB    CHAR(10) CONST;      // --> Job
    p_POS    PACKED(6 : 0) CONST; // --> Position
    p_ARA    CHAR(10) CONST;      // --> Area
    p_NBRACT PACKED(2 : 0);       // <-- Number of actions
    p_ACT    CHAR(10) DIM(25);    // <-- Actions
  END-PI;

  DCL-S l_ACT     CHAR(10);      // --> Action
  DCL-S l_SUCCESS CHAR(1);       // <-- Success
  DCL-S l_MSG     CHAR(256);     // <-- Message

  p_NBRACT = *zero;
  p_ACT(*) = *blanks;

  l_ACT = CHKSTSE_DSPRCDDUPKEY;
    exsr checkAction;

  l_ACT = CHKSTSE_REAPYCHG;
    exsr checkAction;

  return;

  begsr checkAction;

    if (CHKSTSE_checkAction(
          l_ACT :     // --> Action
          p_JOB :     // --> Job
          p_POS :     // --> Position
          p_ARA :     // --> Area
          l_SUCCESS : // <-- Success
          l_MSG       // <-- Message
        ));
      p_NBRACT += 1;
      p_ACT(p_NBRACT) = l_ACT;
    endif;

  endsr;

END-PROC CHKSTSE_getValidActions;
