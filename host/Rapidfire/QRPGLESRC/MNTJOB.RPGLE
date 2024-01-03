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
//* Maintain Job                                                                            *
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
//* 26.10.2017 I   FH   I Creation                                                          *
//*------------+--------+-------------------------------------------------------------------*

CTL-OPT NOMAIN;
CTL-OPT DATFMT(*ISO) TIMFMT(*ISO) DECEDIT('0,');
CTL-OPT COPYRIGHT('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT BNDDIR('RAPIDFIRE');
CTL-OPT EXTBININT(*YES);

//*-----------------------------------------------------------------------------------------*

DCL-F JOBS
      DISK(*EXT)
      USAGE(*INPUT : *OUTPUT : *UPDATE : *DELETE)
      KEYED
      RENAME(JOBS : JOBRF)
      COMMIT
      USROPN;

DCL-F LIBLS
      DISK(*EXT)
      USAGE(*INPUT : *DELETE : *OUTPUT)
      KEYED
      RENAME(LIBLS : LIBLSTRF)
      COMMIT
      USROPN;

//*-----------------------------------------------------------------------------------------*

/INCLUDE QCPYSRC,MNTJOB

/INCLUDE QCPYSRC,RAPIDFIRE

/INCLUDE QCPYSRC,HDLRCDLCK

/INCLUDE QCPYSRC,RTVMSGD

/INCLUDE QCPYSRC,CHKVAL

/INCLUDE QCPYSRC,JOB

DCL-DS PSDS LEN(429) QUALifIED PSDS;
END-DS;

DCL-S g_MODE CHAR(10);              // Mode
DCL-S g_JOB  CHAR(10);              // Job
DCL-S g_DSCR CHAR(35);              // Description
DCL-S g_CRTE CHAR(10);              // Create environment
DCL-S g_JQ   CHAR(10);              // Job queue
DCL-S g_JQL  CHAR(10);              // Job queue (Library)
DCL-S g_CASPTE CHAR(10);            // Cancel, if ASP threshold exceeds
DCL-S g_LIBL CHAR(10) INZ('*NONE'); // Library list
DCL-S g_OPN  IND INZ(*OFF);         // Open indicator

//*=========================================================================================*
//* Open Files                                                                              *
//*=========================================================================================*

DCL-PROC MNTJOB_openFiles EXPORT;

  DCL-PI MNTJOB_openFiles;
  END-PI;

  open JOBS;
  open LIBLS;

  g_OPN = *on;

END-PROC MNTJOB_openFiles;

//*=========================================================================================*
//* Close Files                                                                             *
//*=========================================================================================*

DCL-PROC MNTJOB_closeFiles EXPORT;

  DCL-PI MNTJOB_closeFiles;
  END-PI;

  close JOBS;
  close LIBLS;

  g_OPN = *off;

END-PROC MNTJOB_closeFiles;

//*=========================================================================================*
//* Initialize                                                                              *
//*=========================================================================================*
//*                                                                                         *
//* Error codes and their meanings                                                          *
//* ==============================                                                          *
//*                                                                                         *
//* '001' = Record in file JOBS not found                                                   *
//* '002' = Record in file JOBS has not status *RDY                                         *
//* '003' = Wrong mode                                                                      *
//*                                                                                         *
//*=========================================================================================*

DCL-PROC MNTJOB_initialize EXPORT;

  DCL-PI MNTJOB_initialize IND; // <-- Success
    p_MODE    CHAR(10) CONST;   // --> Mode
    p_JOB     CHAR(10) CONST;   // --> Job
    p_SUCCESS CHAR(1);          // <-- Success
    p_MSG     CHAR(256);        // <-- Message
  END-PI;

  g_MODE = p_MODE;
  g_JOB = p_JOB;

  select;

  when (g_MODE = MNTJOB_CREATE);

    p_SUCCESS = 'Y';
    p_MSG = *blanks;
    return *on;

  when (g_MODE = MNTJOB_CHANGE or
        g_MODE = MNTJOB_DELETE);

    dou (not %error);
      chain(e) (g_JOB) JOBRF;
      if (%error);
        HdlRcdLck(PSDS);
      endif;
    enddo;
    if (not %found);
      p_SUCCESS = 'N';
      p_MSG = '001';
      return *off;
    else;
      if (X1STS <> '*RDY');
        unlock JOBS;
        p_SUCCESS = 'N';
        p_MSG = '002';
        return *off;
      else;
        p_SUCCESS = 'Y';
        p_MSG = *blanks;
        return *on;
      endif;
    endif;

  when (g_MODE = MNTJOB_DISPLAY or
        g_MODE = MNTJOB_COPY);

    chain(n) (g_JOB) JOBRF;
    if (not %found);
      p_SUCCESS = 'N';
      p_MSG = '001';
      return *off;
    else;
      p_SUCCESS = 'Y';
      p_MSG = *blanks;
      return *on;
    endif;

  when (g_MODE = MNTJOB_CHKACT);

    if (g_JOB <> *blanks);
      chain(n) (g_JOB) JOBRF;
      if (not %found);
        p_SUCCESS = 'N';
        p_MSG = '001';
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
    p_CASPTE CHAR(10); // <-- Cancel, if ASP threshold exceeds
  END-PI;

  if (g_MODE = MNTJOB_CREATE);

    p_JOB = *blanks;
    p_DSCR = *blanks;
    p_CRTE = *blanks;
    p_JQ = *blanks;
    p_JQL = *blanks;
    p_CASPTE = *blanks;

  else;

    p_JOB = X1JOB;
    p_DSCR = X1DSCR;
    p_CRTE = X1CRTE;
    p_JQ = X1JQ;
    p_JQL = X1JQL;
    p_CASPTE = X1CASPTE;

  endif;

END-PROC MNTJOB_getValues;

//*=========================================================================================*
//* Set Values                                                                              *
//*=========================================================================================*

DCL-PROC MNTJOB_setValues EXPORT;

  DCL-PI MNTJOB_setValues;
    p_JOB  CHAR(10) CONST; // --> Job
    p_DSCR CHAR(35) CONST; // --> Description
    p_CRTE CHAR(10) CONST; // --> Create environment
    p_JQ   CHAR(10) CONST; // --> Job queue
    p_JQL  CHAR(10) CONST; // --> Job queue (Library)
    p_CASPTE CHAR(10) CONST; // --> Cancel, if ASP threshold exceeds
  END-PI;

  g_JOB = p_JOB;
  g_DSCR = p_DSCR;
  g_CRTE = p_CRTE;
  g_JQ = p_JQ;
  g_JQL = p_JQL;
  g_CASPTE = p_CASPTE;

END-PROC MNTJOB_setValues;

//*=========================================================================================*
//* Check                                                                                   *
//*=========================================================================================*

DCL-PROC MNTJOB_check EXPORT;

  DCL-PI MNTJOB_check IND; // <-- Success
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
    if (g_MODE = MNTJOB_CREATE or
        g_MODE = MNTJOB_COPY);

      // The value in field 'Job' is not valid
      if ChkVal('*NAME' : 10 : %Addr(g_JOB)) = '*INVALID';
        p_FLD = 'JOB';
        l_MSGID = 'ERR0002';
        leave;
      endif;

      // The job does already exist
      SetLL ( g_JOB ) JOBRF;
      if %Equal;
        p_FLD = 'JOB';
        l_MSGID = 'ERR0001';
        leave;
      endif;

    endif;

    //*** Check only for mode CREATE, COPY and CHANGE ***
    if (g_MODE = MNTJOB_CREATE or
        g_MODE = MNTJOB_COPY or
        g_MODE = MNTJOB_CHANGE);

      // The value in field 'Description' is not valid
      if g_DSCR = *Blanks;
        p_FLD = 'DSCR';
        l_MSGID = 'ERR0003';
        leave;
      endif;

      // The value in field 'Create environment' is not valid
      if g_CRTE <> '*YES' And
         g_CRTE <> '*NO';
        p_FLD = 'CRTE';
        l_MSGID = 'ERR0004';
        leave;
      endif;

      // The value in field 'Job queue' is not valid
      if ChkVal('*NAME' : 10 : %Addr(g_JQ)) = '*INVALID';
        p_FLD = 'JQ';
        l_MSGID = 'ERR0045';
        leave;
      endif;

      // The value in field 'Job queue - Library' is not valid
      if g_JQL <> '*LIBL' And
         g_JQL <> '*CURLIB' And
         ChkVal('*NAME' : 10 : %Addr(g_JQL)) =
        '*INVALID';
        p_FLD = 'JQL';
        l_MSGID = 'ERR0048';
        leave;
      endif;

      // The value in field 'Cancel, if ASP threshold exceeds' is not valid
      if g_CASPTE <> '*YES' And
         g_CASPTE <> '*NO';
        p_FLD = 'CASPTE';
        l_MSGID = 'ERR0012';
        leave;
      endif;

    endif;

    //*** Check only for mode DELETE ***
    if (g_MODE = MNTJOB_DELETE);
    endif;

  enddo;

  // Return
  if (l_MSGID = *blanks);

    p_SUCCESS = 'Y';
    return *on;

  else;

    p_MSG = rtvMsgText(
      l_MSGID :  // --> Message-Id.
      'MNTJOB' : // --> Message file
      '*LIBL' :  // --> Library
      l_MSGDTA   // --> Message data
    );

    p_SUCCESS = 'N';
    return *off;

  endif;

END-PROC MNTJOB_check;

//*=========================================================================================*
//* Book                                                                                    *
//*=========================================================================================*

DCL-PROC MNTJOB_book EXPORT;

  DCL-PI MNTJOB_book;
  END-PI;

  select;

  when (g_MODE = MNTJOB_CREATE);
    exsr modeCreate;

  when (g_MODE = MNTJOB_COPY);
    exsr modeCopy;

  when (g_MODE = MNTJOB_CHANGE);
    exsr modeChange;

  when (g_MODE = MNTJOB_DELETE);
    exsr modeDelete;

  when (g_MODE = MNTJOB_DISPLAY);
    exsr modeDisplay;

  endsl;

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

    ExSr libraryList;

    if RAPIDFIRE_getAutoCommit() = 'Y';
      commit;
    endif;

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

    ExSr libraryList;

    if RAPIDFIRE_getAutoCommit() = 'Y';
      commit;
    endif;

  endsr;

  //*------------------------------------------------------------------------------------------*
  //* Mode Change                                                                              *
  //*------------------------------------------------------------------------------------------*

  begsr modeChange;

    exsr fillRecord;

    update JOBRF;

    ExSr libraryList;

    if RAPIDFIRE_getAutoCommit() = 'Y';
      commit;
    endif;

  endsr;

  //*------------------------------------------------------------------------------------------*
  //* Mode Delete                                                                              *
  //*------------------------------------------------------------------------------------------*

  begsr modeDelete;

    delete JOBRF;

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

    X1JOB = g_JOB;
    X1DSCR = g_DSCR;
    X1CRTE = g_CRTE;
    X1JQ = g_JQ;
    X1JQL = g_JQL;
    X1CASPTE = g_CASPTE;

  endsr;

  //*------------------------------------------------------------------------------------------*
  //* Library List *NONE                                                                       *
  //*------------------------------------------------------------------------------------------*

  begsr libraryList;

    if g_CRTE = '*NO';
      SetLL ( g_JOB : g_LIBL ) LIBLSTRF;
      if Not %Equal;
        Clear *All LIBLSTRF;
        X4JOB = g_JOB;
        X4LIBL = g_LIBL;
        X4DSCR = g_LIBL;
        Write LIBLSTRF;
      endif;
    else;
      dou Not %Error;
        Chain(E) ( g_JOB : g_LIBL ) LIBLSTRF;
        if %Error;
          HdlRcdLck(PSDS);
        endif;
      EndDo;
      if %Found;
        Delete LIBLSTRF;
      endif;
    endif;

  endsr;

END-PROC MNTJOB_book;

//*=========================================================================================*
//* Check Action                                                                            *
//*=========================================================================================*

DCL-PROC MNTJOB_checkAction EXPORT;

  DCL-PI MNTJOB_checkAction IND; // <-- Success
    p_ACT     CHAR(10) CONST;    // --> Action
    p_JOB     CHAR(10) CONST;    // --> Job
    p_SUCCESS CHAR(1);           // <-- Success
    p_MSG     CHAR(256);         // <-- Message
  END-PI;

  DCL-S l_MSGID   CHAR(7);   // Message-Id.
  DCL-S l_MSGDTA  CHAR(256); // Message data
  DCL-S l_SUCCESS CHAR(1);   // Success
  DCL-S l_MSG     CHAR(256); // Message
  DCL-S l_OPN     IND;       // Open indicator
  DCL-S l_STS     CHAR(10);  // Status
  DCL-S l_PHS     CHAR(10);  // Phase
  DCL-S l_ERR     CHAR(1);   // Error

  // Initialize returning fields
  p_MSG = *blanks;

  // Initialize message fields
  l_MSGID = *blanks;
  l_MSGDTA = *blanks;

  dou (1 = 1);

    // Invalid parameter values. Error = Job has to be blanks.
    if (p_ACT = MNTJOB_CREATE and
        p_JOB <> *blanks);
      l_MSGID = 'ERR0011';
      leave;
    endif;

    // Invalid parameter values. Error = &1.
    l_OPN = g_OPN;
    if (not l_OPN);
      MNTJOB_openFiles();
    endif;
    MNTJOB_initialize(
      MNTJOB_CHKACT : // --> Mode
      p_JOB :         // --> Job
      l_SUCCESS :     // <-- Success
      l_MSG           // <-- Message
    );
    if (not l_OPN);
      MNTJOB_closeFiles();
    endif;
    if (l_SUCCESS = 'N');
      l_MSGID = 'ERR0005';
      l_MSGDTA = l_MSG;
      leave;
    endif;

    // Set status
    l_STS = X1STS;
    l_PHS = X1PHS;
    l_ERR = X1ERR;
    if (l_STS = '*RUN' and
        not JOB_is_Job_Active(
          X1BJOB : // --> Job
          X1BUSR : // --> User
          X1BNBR   // --> Number
        )
       );
      l_STS = '*ABORT';
      l_PHS = '*NONE';
      l_ERR = 'N';
    endif;

    // The action is only valid for jobs with status *RDY
    if ((p_ACT = MNTJOB_CHANGE or
         p_ACT = MNTJOB_DELETE or
         p_ACT = MNTJOB_TSTJOB or
         p_ACT = MNTJOB_STRJOB) and
        l_STS <> '*RDY');
      l_MSGID = 'ERR0006';
      leave;
    endif;

    // The action is only valid for jobs with status *RUN-PND, *RUN, *END-PND or *ABORT
    if (p_ACT = MNTJOB_RESETJOBA and
        l_STS <> '*RUN-PND' and
        l_STS <> '*RUN' and
        l_STS <> '*END-PND' and
        l_STS <> '*ABORT');
      l_MSGID = 'ERR0007';
      leave;
    endif;

    // The action is only valid for jobs with status *RUN
    if (p_ACT = MNTJOB_ENDJOB and
        l_STS <> '*RUN');
      l_MSGID = 'ERR0008';
      leave;
    endif;

    // The action is only valid for jobs with status *END
    if (p_ACT = MNTJOB_RESETJOB and
        l_STS <> '*END');
      l_MSGID = 'ERR0009';
      leave;
    endif;

    // The action is only valid for jobs with "Create environment" *YES
    if (p_ACT = MNTJOB_MNTLIBL and
        X1CRTE <> '*YES');
      l_MSGID = 'ERR0010';
      leave;
    endif;

  enddo;

  // Return
  if (l_MSGID = *blanks);

    p_SUCCESS = 'Y';
    return *on;

  else;

    p_MSG = rtvMsgText(
      l_MSGID :  // --> Message-Id.
      'MNTJOB' : // --> Message file
      '*LIBL' :  // --> Library
      l_MSGDTA   // --> Message data
    );

    p_SUCCESS = 'N';
    return *off;

  endif;

END-PROC MNTJOB_checkAction;

//*=========================================================================================*
//* Get Valid Actions                                                                       *
//*=========================================================================================*

DCL-PROC MNTJOB_getValidActions EXPORT;

  DCL-PI MNTJOB_getValidActions;
    p_JOB     CHAR(10) CONST;   // --> Job
    p_NBRACT  PACKED(2 : 0);    // <-- Number of actions
    p_ACT     CHAR(10) DIM(25); // <-- Actions
  END-PI;

  DCL-S l_ACT     CHAR(10);  // --> Action
  DCL-S l_SUCCESS CHAR(1);   // <-- Success
  DCL-S l_MSG     CHAR(256); // <-- Message

  p_NBRACT = *zero;
  p_ACT(*) = *blanks;

  l_ACT = MNTJOB_COPY;
    exsr checkAction;

  l_ACT = MNTJOB_CHANGE;
    exsr checkAction;

  l_ACT = MNTJOB_DELETE;
    exsr checkAction;

  l_ACT = MNTJOB_DISPLAY;
    exsr checkAction;

  l_ACT = MNTJOB_MNTLIB;
    exsr checkAction;

  l_ACT = MNTJOB_MNTLIBL;
    exsr checkAction;

  l_ACT = MNTJOB_MNTFILE;
    exsr checkAction;

  l_ACT = MNTJOB_MNTSTBN;
    exsr checkAction;

  l_ACT = MNTJOB_MNTAS;
    exsr checkAction;

  l_ACT = MNTJOB_TSTJOB;
    exsr checkAction;

  l_ACT = MNTJOB_STRJOB;
    exsr checkAction;

  l_ACT = MNTJOB_ENDJOB;
    exsr checkAction;

  l_ACT = MNTJOB_RESETJOB;
    exsr checkAction;

  l_ACT = MNTJOB_RESETJOBA;
    exsr checkAction;

  l_ACT = MNTJOB_DSPSTS;
    exsr checkAction;

  l_ACT = MNTJOB_DSPERR;
    exsr checkAction;

  return;

  begsr checkAction;

    if (MNTJOB_checkAction(
          l_ACT :     // --> Action
          p_JOB :     // --> Job
          l_SUCCESS : // <-- Success
          l_MSG       // <-- Message
        ));
      p_NBRACT += 1;
      p_ACT(p_NBRACT) = l_ACT;
    endif;

  endsr;

END-PROC MNTJOB_getValidActions;
