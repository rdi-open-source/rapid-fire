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

DCL-F LIBRARIES
      DISK(*EXT)
      USAGE(*INPUT : *OUTPUT : *UPDATE : *DELETE)
      KEYED
      RENAME(LIBRARIES : LIBRF)
      COMMIT
      USROPN;

//*-----------------------------------------------------------------------------------------*

/INCLUDE QCPYSRC,MNTJOB

/INCLUDE QCPYSRC,HDLRCDLCK

/INCLUDE QCPYSRC,RTVMSGD

/INCLUDE QCPYSRC,CHKVAL

DCL-DS PSDS LEN(429) QUALIFIED PSDS;
END-DS;

DCL-C MODE_CREATE '*CREATE';
DCL-C MODE_COPY '*COPY';
DCL-C MODE_CHANGE '*CHANGE';
DCL-C MODE_DELETE '*DELETE';
DCL-C MODE_DISPLAY '*DISPLAY';

DCL-S g_MODE CHAR(10); // Mode
DCL-S g_JOB  CHAR(10); // Job
DCL-S g_DSCR CHAR(35); // Description
DCL-S g_CRTE CHAR(10); // Create environment
DCL-S g_JQ   CHAR(10); // Job queue
DCL-S g_JQL  CHAR(10); // Job queue (Library)

//*=========================================================================================*
//* Open Files                                                                              *
//*=========================================================================================*

DCL-PROC MNTJOB_openFiles EXPORT;

  DCL-PI MNTJOB_openFiles;
  END-PI;

  open JOBS;

END-PROC MNTJOB_openFiles;

//*=========================================================================================*
//* Close Files                                                                             *
//*=========================================================================================*

DCL-PROC MNTJOB_closeFiles EXPORT;

  DCL-PI MNTJOB_closeFiles;
  END-PI;

  close JOBS;

END-PROC MNTJOB_closeFiles;

//*=========================================================================================*
//* Initialize                                                                              *
//*=========================================================================================*

DCL-PROC MNTJOB_initialize EXPORT;

  DCL-PI MNTJOB_initialize IND; // <-- Success
    p_MODE CHAR(10) CONST; // --> Mode
    p_JOB  CHAR(10) CONST; // --> Job
  END-PI;

  g_MODE = p_MODE;
  g_JOB = p_JOB;

  select;

  when (g_MODE = MODE_CREATE);

    return *on;

  when (g_MODE = MODE_CHANGE or
        g_MODE = MODE_DELETE);

    dou (not %error);
      chain(e) (g_JOB) JOBRF;
      if (%error);
        HdlRcdLck(PSDS);
      endif;
    enddo;
    if (%found);
      if (X1STS = '*RDY');
        return *on;
      else;
        unlock JOBS;
      endif;
    endif;

  when (g_MODE = MODE_DISPLAY or
        g_MODE = MODE_COPY);

    chain(n) (g_JOB) JOBRF;
    if (%found);
      return *on;
    endif;

  endsl;

  return *off;

END-PROC MNTJOB_initialize;

//*=========================================================================================*
//* Get Values                                                                              *
//*=========================================================================================*

DCL-PROC MNTJOB_getValues EXPORT;

  DCL-PI MNTJOB_getValues;
    p_JOB  CHAR(10); // <-- Job
    p_DSCR CHAR(35); // <-- Description
    p_CRTE CHAR(10); // <-- Create environment
    p_JQ   CHAR(10); // <-- Job queue
    p_JQL  CHAR(10); // <-- Job queue (Library)
  END-PI;

  if (g_MODE = MODE_CREATE);

    p_JOB = *blanks;
    p_DSCR = *blanks;
    p_CRTE = *blanks;
    p_JQ = *blanks;
    p_JQL = *blanks;

  else;

    p_JOB = X1JOB;
    p_DSCR = X1DSCR;
    p_CRTE = X1CRTE;
    p_JQ = X1JQ;
    p_JQL = X1JQL;

  endif;

END-PROC MNTJOB_getValues;

//*=========================================================================================*
//* Set Values                                                                              *
//*=========================================================================================*

DCL-PROC MNTJOB_setValues EXPORT;

  DCL-PI MNTJOB_setValues;
    p_JOB  CHAR(10) CONST; // <-- Job
    p_DSCR CHAR(35) CONST; // <-- Description
    p_CRTE CHAR(10) CONST; // <-- Create environment
    p_JQ   CHAR(10) CONST; // <-- Job queue
    p_JQL  CHAR(10) CONST; // <-- Job queue (Library)
  END-PI;

  g_JOB = p_JOB;
  g_DSCR = p_DSCR;
  g_CRTE = p_CRTE;
  g_JQ = p_JQ;
  g_JQL = p_JQL;

END-PROC MNTJOB_setValues;

//*=========================================================================================*
//* Check                                                                                   *
//*=========================================================================================*

DCL-PROC MNTJOB_check EXPORT;

  DCL-PI MNTJOB_check IND; // <-- Success
    p_FLD CHAR(10);  // <-- Feld
    p_MSG CHAR(256); // <-- Nachricht
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
    if (g_MODE = MODE_CREATE or
        g_MODE = MODE_COPY);

      // The value in field 'Job' is not valid
      If ChkVal('*NAME' : 10 : %Addr(g_JOB)) = '*INVALID';
        p_FLD = 'JOB';
        l_MSGID = 'ERR0002';
        Leave;
      EndIf;

      // The job does already exist
      SetLL ( g_JOB ) JOBRF;
      If %Equal;
        p_FLD = 'JOB';
        l_MSGID = 'ERR0001';
        Leave;
      EndIf;

    endif;

    //*** Check only for mode CREATE, COPY and CHANGE ***
    if (g_MODE = MODE_CREATE or
        g_MODE = MODE_COPY or
        g_MODE = MODE_CHANGE);

      // The value in field 'Description' is not valid
      If g_DSCR = *Blanks;
        p_FLD = 'DSCR';
        l_MSGID = 'ERR0003';
        Leave;
      EndIf;

      // The value in field 'Create environment' is not valid
      If g_CRTE <> '*YES' And
         g_CRTE <> '*NO';
        p_FLD = 'CRTE';
        l_MSGID = 'ERR0004';
        Leave;
      EndIf;

      // The value in field 'Job queue' is not valid
      If ChkVal('*NAME' : 10 : %Addr(g_JQ)) = '*INVALID';
        p_FLD = 'JQ';
        l_MSGID = 'ERR0045';
        Leave;
      EndIf;

      // The value in field 'Job queue - Library' is not valid
      If g_JQL <> '*LIBL' And
         g_JQL <> '*CURLIB' And
         ChkVal('*NAME' : 10 : %Addr(g_JQL)) =
        '*INVALID';
        p_FLD = 'JQL';
        l_MSGID = 'ERR0048';
        Leave;
      EndIf;

    endif;

    //*** Check only for mode DELETE ***
    if (g_MODE = MODE_DELETE);
    endif;

  enddo;

  // Return
  if (l_MSGID = *blanks);

    return *on;

  else;

    p_MSG = rtvMsgText(
      l_MSGID :  // --> Message-Id.
      'MNTJOB' : // --> Message file
      '*LIBL' :  // --> Library
      l_MSGDTA   // --> Message data
    );

    return *off;

  endif;

END-PROC MNTJOB_check;

//*=========================================================================================*
//* Book                                                                                    *
//*=========================================================================================*

DCL-PROC MNTJOB_book EXPORT;

  DCL-PI MNTJOB_book IND; // <-- Success
  END-PI;

  select;

  when (g_MODE = MODE_CREATE);
    exsr modeCreate;

  when (g_MODE = MODE_COPY);
    exsr modeCopy;

  when (g_MODE = MODE_CHANGE);
    exsr modeChange;

  when (g_MODE = MODE_DELETE);
    exsr modeDelete;

  when (g_MODE = MODE_DISPLAY);
    exsr modeDisplay;

  endsl;

  return *on;

  //*------------------------------------------------------------------------------------------*
  //* Mode Create                                                                              *
  //*------------------------------------------------------------------------------------------*

  begsr modeCreate;

    clear JOBRF;

    exsr fillRecord;

    X1STS = '*RDY';
    X1PHS = '*NONE';
    X1ERR = 'N';
    X1ETXT = *Blanks;
    X1BJOB = *Blanks;
    X1BUSR = *Blanks;
    X1BNBR = *Blanks;
    X1SAC = 'N';
    X1CFRM = *Blanks;
    write JOBRF;

    commit;

  endsr;

  //*------------------------------------------------------------------------------------------*
  //* Mode Copy                                                                                *
  //*------------------------------------------------------------------------------------------*

  begsr modeCopy;

    exsr fillRecord;

    X1STS = '*RDY';
    X1PHS = '*NONE';
    X1ERR = 'N';
    X1ETXT = *Blanks;
    X1BJOB = *Blanks;
    X1BUSR = *Blanks;
    X1BNBR = *Blanks;
    X1SAC = 'N';
    X1CFRM = *Blanks;
    write JOBRF;

    commit;

  endsr;

  //*------------------------------------------------------------------------------------------*
  //* Mode Change                                                                              *
  //*------------------------------------------------------------------------------------------*

  begsr modeChange;

    exsr fillRecord;

    update JOBRF;

    commit;

  endsr;

  //*------------------------------------------------------------------------------------------*
  //* Mode Delete                                                                              *
  //*------------------------------------------------------------------------------------------*

  begsr modeDelete;

    delete JOBRF;

    commit;

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

    X1JOB = g_JOB;
    X1DSCR = g_DSCR;
    X1CRTE = g_CRTE;
    X1JQ = g_JQ;
    X1JQL = g_JQL;

  endsr;

END-PROC MNTJOB_book;
