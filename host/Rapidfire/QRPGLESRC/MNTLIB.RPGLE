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
//* Maintain Libraries                                                                      *
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
//* 27.10.2017 I   ML   I Creation                                                          *
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

DCL-F LIBRARIES
      DISK(*EXT)
      USAGE(*INPUT : *OUTPUT : *UPDATE : *DELETE)
      KEYED
      RENAME(LIBRARIES : LIBRF)
      COMMIT
      USROPN;

DCL-F LIBRARII01
      DISK(*EXT)
      USAGE(*INPUT)
      KEYED
      RENAME(LIBRARIES : LIBRF01)
      USROPN;

//*-----------------------------------------------------------------------------------------*

/INCLUDE QCPYSRC,MNTJOB

/INCLUDE QCPYSRC,MNTLIB

/INCLUDE QCPYSRC,RAPIDFIRE

/INCLUDE QCPYSRC,HDLRCDLCK

/INCLUDE QCPYSRC,RTVMSGD

/INCLUDE QCPYSRC,CHKVAL

DCL-DS PSDS LEN(429) QUALIFIED PSDS;
END-DS;

DCL-S g_MODE CHAR(10); // Mode
DCL-S g_JOB  CHAR(10); // Job
DCL-S g_LIB  CHAR(10); // Library
DCL-S g_SLIB CHAR(10); // Shadow library
DCL-S g_OPN  IND INZ(*OFF); // Open indicator

//*=========================================================================================*
//* Open Files                                                                              *
//*=========================================================================================*

DCL-PROC MNTLIB_openFiles EXPORT;

  DCL-PI MNTLIB_openFiles;
  END-PI;

  open JOBS;
  open LIBRARIES;
  open LIBRARII01;

  g_OPN = *on;

END-PROC MNTLIB_openFiles;

//*=========================================================================================*
//* Close Files                                                                             *
//*=========================================================================================*

DCL-PROC MNTLIB_closeFiles EXPORT;

  DCL-PI MNTLIB_closeFiles;
  END-PI;

  close JOBS;
  close LIBRARIES;
  close LIBRARII01;

  g_OPN = *off;

END-PROC MNTLIB_closeFiles;

//*=========================================================================================*
//* Initialize                                                                              *
//*=========================================================================================*
//*                                                                                         *
//* Error codes and their meanings                                                          *
//* ==============================                                                          *
//*                                                                                         *
//* '001' = Record in file JOBS not found                                                   *
//* '002' = Record in file LIBRARIES not found                                              *
//* '003' = Wrong mode                                                                      *
//*                                                                                         *
//*=========================================================================================*

DCL-PROC MNTLIB_initialize EXPORT;

  DCL-PI MNTLIB_initialize IND; // <-- Success
    p_MODE    CHAR(10) CONST;   // --> Mode
    p_JOB     CHAR(10) CONST;   // --> Job
    p_LIB     CHAR(10) CONST;   // --> Library
    p_SUCCESS CHAR(1);          // <-- Success
    p_MSG     CHAR(256);        // <-- Message
  END-PI;

  g_MODE = p_MODE;
  g_JOB = p_JOB;
  g_LIB = p_LIB;

  chain (g_JOB) JOBRF;
  if (not %found);

    p_SUCCESS = 'N';
    p_MSG = '001';
    return *off;

  else;

    select;

    when (g_MODE = MNTLIB_CREATE);

      p_SUCCESS = 'Y';
      p_MSG = *blanks;
      return *on;

    when (g_MODE = MNTLIB_CHANGE or
          g_MODE = MNTLIB_DELETE);

      dou (not %error);
        chain(e) (g_JOB : g_LIB) LIBRF;
        if (%error);
          HdlRcdLck(PSDS);
        endif;
      enddo;
      if (not %found);
        p_SUCCESS = 'N';
        p_MSG = '002';
        return *off;
      else;
        p_SUCCESS = 'Y';
        p_MSG = *blanks;
        return *on;
      endif;

    when (g_MODE = MNTLIB_DISPLAY or
          g_MODE = MNTLIB_COPY);

      chain(n) (g_JOB : g_LIB) LIBRF;
      if (not %found);
        p_SUCCESS = 'N';
        p_MSG = '002';
        return *off;
      else;
        p_SUCCESS = 'Y';
        p_MSG = *blanks;
        return *on;
      endif;

    when (g_MODE = MNTLIB_CHKACT);

      if (g_LIB <> *blanks);
        chain(n) (g_JOB : g_LIB) LIBRF;
        if (not %found);
          p_SUCCESS = 'N';
          p_MSG = '002';
          return *off;
        else;
          p_SUCCESS = 'Y';
          p_MSG = *blanks;
          return *on;
        endif;
      else;
        p_SUCCESS = 'Y';
        p_MSG = *blanks;
        return *on;
      endif;

    other;

      p_SUCCESS = 'N';
      p_MSG = '003';
      return *off;

    endsl;

  endif;

END-PROC MNTLIB_initialize;

//*=========================================================================================*
//* Get Values                                                                              *
//*=========================================================================================*

DCL-PROC MNTLIB_getValues EXPORT;

  DCL-PI MNTLIB_getValues;
    p_LIB  CHAR(10); // <-- Library
    p_SLIB CHAR(10); // <-- Shadow library
  END-PI;

  if (g_MODE = MNTLIB_CREATE);

    p_LIB  = *blanks;
    p_SLIB = *blanks;

  else;

    p_LIB  = X2LIB;
    p_SLIB = X2SLIB;

  endif;

END-PROC MNTLIB_getValues;

//*=========================================================================================*
//* Set Values                                                                              *
//*=========================================================================================*

DCL-PROC MNTLIB_setValues EXPORT;

  DCL-PI MNTLIB_setValues;
    p_LIB  CHAR(10) CONST; // --> Library
    p_SLIB CHAR(10) CONST; // --> Shadow library
  END-PI;

  g_LIB  = p_LIB;
  g_SLIB = p_SLIB;

END-PROC MNTLIB_setValues;

//*=========================================================================================*
//* Check                                                                                   *
//*=========================================================================================*

DCL-PROC MNTLIB_check EXPORT;

  DCL-PI MNTLIB_check IND; // <-- Success
    p_SUCCESS CHAR(1);     // <-- Success
    p_FLD     CHAR(10);    // <-- Field
    p_MSG     CHAR(256);   // <-- Message
  END-PI;

  DCL-S l_MSGID  CHAR(7);   // Message-Id.
  DCL-S l_MSGDTA CHAR(256); // Message data

  // Initialize returning fields
  p_FLD = *blanks;
  p_MSG = *blanks;

  // Initialize message fields
  l_MSGID = *blanks;
  l_MSGDTA = *blanks;

  dou (1 = 1);

    //*** Check only for mode CREATE and COPY ***
    if (g_MODE = MNTLIB_CREATE or
        g_MODE = MNTLIB_COPY);

      // The value in field 'Library' is not valid
      if ChkVal('*NAME' : 10 : %Addr(g_LIB)) = '*INVALID';
        p_FLD = 'LIB';
        l_MSGID = 'ERR0002';
        Leave;
      endif;

      // The library does already exist
      SetLL ( g_JOB : g_LIB) LIBRF;
      if %Equal;
        p_FLD = 'LIB';
        l_MSGID = 'ERR0001';
        Leave;
      endif;

    endif;

    //*** Check only for mode CREATE, COPY and CHANGE ***
    if (g_MODE = MNTLIB_CREATE or
        g_MODE = MNTLIB_COPY or
        g_MODE = MNTLIB_CHANGE);

      // The value in field 'Shadow library' is not valid
      if ChkVal('*NAME' : 10 : %Addr(g_SLIB)) = '*INVALID';
        p_FLD = 'SLIB';
        l_MSGID = 'ERR0003';
        Leave;
      endif;

      // The shadow library does already exist
      if Not (g_MODE = MNTLIB_CHANGE and
              g_SLIB = X2SLIB);
        SetLL (g_JOB : g_SLIB) LIBRF01;
        if %Equal;
          p_FLD = 'SLIB';
          l_MSGID = 'ERR0004';
          Leave;
        endif;
      endif;

      // Shadow library must not be equal to library
      if g_SLIB = g_LIB;
        p_FLD = 'SLIB';
        l_MSGID = 'ERR0005';
        Leave;
      endif;

    endif;

    //*** Check only for mode DELETE ***
    if (g_MODE = MNTLIB_DELETE);
    endif;

  enddo;

  // Return
  if (l_MSGID = *blanks);
    p_SUCCESS = 'Y';
    return *on;

  else;

    p_MSG = rtvMsgText(
      l_MSGID :  // --> Message-Id.
      'MNTLIB' : // --> Message file
      '*LIBL' :  // --> Library
      l_MSGDTA   // --> Message data
    );

    p_SUCCESS = 'N';
    return *off;

  endif;

END-PROC MNTLIB_check;

//*=========================================================================================*
//* Book                                                                                    *
//*=========================================================================================*

DCL-PROC MNTLIB_book EXPORT;

  DCL-PI MNTLIB_book;
  END-PI;

  select;

  when (g_MODE = MNTLIB_CREATE);
    exsr modeCreate;

  when (g_MODE = MNTLIB_COPY);
    exsr modeCopy;

  when (g_MODE = MNTLIB_CHANGE);
    exsr modeChange;

  when (g_MODE = MNTLIB_DELETE);
    exsr modeDelete;

  when (g_MODE = MNTLIB_DISPLAY);
    exsr modeDisplay;

  endsl;

  //*------------------------------------------------------------------------------------------*
  //* Mode Create                                                                              *
  //*------------------------------------------------------------------------------------------*

  begsr modeCreate;

    clear LIBRF;
    X2JOB  = g_JOB;

    exsr fillRecord;

    X2ASPNBR = *Zero;
    X2ASPDEV = *Blanks;
    write LIBRF;

    if RAPIDFIRE_getAutoCommit() = 'Y';
      commit;
    endif;

  endsr;

  //*------------------------------------------------------------------------------------------*
  //* Mode Copy                                                                                *
  //*------------------------------------------------------------------------------------------*

  begsr modeCopy;

    exsr fillRecord;

    X2ASPNBR = *Zero;
    X2ASPDEV = *Blanks;
    write LIBRF;

    if RAPIDFIRE_getAutoCommit() = 'Y';
      commit;
    endif;

  endsr;

  //*------------------------------------------------------------------------------------------*
  //* Mode Change                                                                              *
  //*------------------------------------------------------------------------------------------*

  begsr modeChange;

    exsr fillRecord;

    update LIBRF;

    if RAPIDFIRE_getAutoCommit() = 'Y';
      commit;
    endif;

  endsr;

  //*------------------------------------------------------------------------------------------*
  //* Mode Delete                                                                              *
  //*------------------------------------------------------------------------------------------*

  begsr modeDelete;

    delete LIBRF;

    if RAPIDFIRE_getAutoCommit() = 'Y';
      commit;
    endif;

  endsr;

  //*------------------------------------------------------------------------------------------*
  //* Mode Display                                                                             *
  //*------------------------------------------------------------------------------------------*

  begsr modeDisplay;

  endsr;

  //*------------------------------------------------------------------------------------------*
  //* Fill Record                                                                              *
  //*------------------------------------------------------------------------------------------*

  begsr fillRecord;

    X2LIB  = g_LIB;
    X2SLIB = g_SLIB;

  endsr;

END-PROC MNTLIB_book;

//*=========================================================================================*
//* Check Action                                                                            *
//*=========================================================================================*

DCL-PROC MNTLIB_checkAction EXPORT;

  DCL-PI MNTLIB_checkAction IND;  // <-- Success
    p_ACT     CHAR(10) CONST;      // --> Action
    p_JOB     CHAR(10) CONST;      // --> Job
    p_LIB     CHAR(10) CONST;      // --> Library
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

    // Invalid parameter values. Error = Library has to be blanks.
    if (p_ACT = MNTLIB_CREATE and
        p_LIB <> *blanks);
      l_MSGID = 'ERR0006';
      leave;
    endif;

    // Invalid parameter values. Error = &1.
    l_OPN = g_OPN;
    if (not l_OPN);
      MNTLIB_openFiles();
    endif;
    MNTLIB_initialize(
      MNTLIB_CHKACT : // --> Mode
      p_JOB :          // --> Job
      p_LIB :          // --> Library
      l_SUCCESS :      // <-- Success
      l_MSG            // <-- Message
    );
    if (not l_OPN);
      MNTLIB_closeFiles();
    endif;
    if (l_SUCCESS = 'N');
      l_MSGID = 'ERR0007';
      l_MSGDTA = l_MSG;
      leave;
    endif;

    // The action is not valid for such type of jobs
    if (not MNTJOB_checkAction(
              MNTJOB_MNTLIB : // --> Action
              p_JOB :         // --> Job
              l_SUCCESS :     // <-- Success
              l_MSG           // <-- Message
            )
       );
      l_MSGID = 'ERR0009';
      l_MSGDTA = l_MSG;
      leave;
    endif;

    // The action is only valid for jobs with status *RDY
    if ((p_ACT = MNTLIB_CREATE or
         p_ACT = MNTLIB_COPY or
         p_ACT = MNTLIB_CHANGE or
         p_ACT = MNTLIB_DELETE) and
        X1STS <> '*RDY');
      l_MSGID = 'ERR0008';
      leave;
    endif;

  enddo;

  // Return
  if (l_MSGID = *blanks);

    p_SUCCESS = 'Y';
    return *on;

  else;

    p_MSG = rtvMsgText(
      l_MSGID :   // --> Message-Id.
      'MNTLIB' : // --> Message file
      '*LIBL' :   // --> Library
      l_MSGDTA    // --> Message data
    );

    p_SUCCESS = 'N';
    return *off;

  endif;

END-PROC MNTLIB_checkAction;

//*=========================================================================================*
//* Get Valid Actions                                                                       *
//*=========================================================================================*

DCL-PROC MNTLIB_getValidActions EXPORT;

  DCL-PI MNTLIB_getValidActions;
    p_JOB     CHAR(10) CONST;   // --> Job
    p_LIB     CHAR(10) CONST;   // --> Library
    p_NBRACT  PACKED(2 : 0);    // <-- Number of actions
    p_ACT     CHAR(10) DIM(25); // <-- Actions
  END-PI;

  DCL-S l_ACT     CHAR(10);      // --> Action
  DCL-S l_SUCCESS CHAR(1);       // <-- Success
  DCL-S l_MSG     CHAR(256);     // <-- Message

  p_NBRACT = *zero;
  p_ACT(*) = *blanks;

  l_ACT = MNTLIB_COPY;
    exsr checkAction;

  l_ACT = MNTLIB_CHANGE;
    exsr checkAction;

  l_ACT = MNTLIB_DELETE;
    exsr checkAction;

  l_ACT = MNTLIB_DISPLAY;
    exsr checkAction;

  return;

  begsr checkAction;

    if (MNTLIB_checkAction(
          l_ACT :     // --> Action
          p_JOB :     // --> Job
          p_LIB :     // --> Library
          l_SUCCESS : // <-- Success
          l_MSG       // <-- Message
        ));
      p_NBRACT += 1;
      p_ACT(p_NBRACT) = l_ACT;
    endif;

  endsr;

END-PROC MNTLIB_getValidActions;
