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
//* Maintain Commands                                                                       *
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
      USAGE(*INPUT)
      KEYED
      RENAME(JOBS : JOBRF)
      USROPN;

DCL-F FILES
      DISK(*EXT)
      USAGE(*INPUT)
      KEYED
      RENAME(FILES : FILERF)
      USROPN;

DCL-F COMMANDS
      DISK(*EXT)
      USAGE(*INPUT : *OUTPUT : *UPDATE : *DELETE)
      KEYED
      RENAME(COMMANDS : CMDRF)
      COMMIT
      USROPN;

//*-----------------------------------------------------------------------------------------*

/INCLUDE QCPYSRC,MNTFILE

/INCLUDE QCPYSRC,MNTCMD

/INCLUDE QCPYSRC,RAPIDFIRE

/INCLUDE QCPYSRC,HDLRCDLCK

/INCLUDE QCPYSRC,RTVMSGD

/INCLUDE QCPYSRC,CHKVAL

DCL-DS PSDS LEN(429) QUALifIED PSDS;
END-DS;

DCL-S g_MODE CHAR(10);      // Mode
DCL-S g_JOB  CHAR(10);      // Job
DCL-S g_POS  PACKED(6 : 0); // Position
DCL-S g_TYPE CHAR(10);      // Type
DCL-S g_SEQ  PACKED(3 : 0); // Sequence
DCL-S g_CMD  CHAR(1024);    // Command
DCL-S g_OPN   IND INZ(*OFF); // Open indicator

//*=========================================================================================*
//* Open Files                                                                              *
//*=========================================================================================*

DCL-PROC MNTCMD_openFiles EXPORT;

  DCL-PI MNTCMD_openFiles;
  END-PI;

  open JOBS;
  open FILES;
  open COMMANDS;

  g_OPN = *on;

END-PROC MNTCMD_openFiles;

//*=========================================================================================*
//* Close Files                                                                             *
//*=========================================================================================*

DCL-PROC MNTCMD_closeFiles EXPORT;

  DCL-PI MNTCMD_closeFiles;
  END-PI;

  close JOBS;
  close FILES;
  close COMMANDS;

  g_OPN = *off;

END-PROC MNTCMD_closeFiles;

//*=========================================================================================*
//* Initialize                                                                              *
//*=========================================================================================*
//*                                                                                         *
//* Error codes and their meanings                                                          *
//* ==============================                                                          *
//*                                                                                         *
//* '001' = Record in file JOBS not found                                                   *
//* '002' = Record in file FILES not found                                                  *
//* '003' = Record in file COMMANDS not found                                               *
//* '004' = Wrong mode                                                                      *
//*                                                                                         *
//*=========================================================================================*

DCL-PROC MNTCMD_initialize EXPORT;

  DCL-PI MNTCMD_initialize IND;    // <-- Success
    p_MODE    CHAR(10) CONST;      // --> Mode
    p_JOB     CHAR(10) CONST;      // --> Job
    p_POS     PACKED(6 : 0) CONST; // --> Position
    p_TYPE    CHAR(10) CONST;      // --> Type
    p_SEQ     PACKED(3 : 0) CONST; // --> Sequence
    p_SUCCESS CHAR(1);             // <-- Success
    p_MSG     CHAR(256);           // <-- Message
  END-PI;

  g_MODE = p_MODE;
  g_JOB = p_JOB;
  g_POS = p_POS;
  g_TYPE = p_TYPE;
  g_SEQ = p_SEQ;

  chain (g_JOB) JOBRF;
  if (not %found);

    p_SUCCESS = 'N';
    p_MSG = '001';
    return *off;

  else;

    chain (g_JOB : g_POS) FILERF;
    if (not %found);

      p_SUCCESS = 'N';
      p_MSG = '002';
      return *off;

    else;

      select;

      when (g_MODE = MNTCMD_CREATE);

        p_SUCCESS = 'Y';
        p_MSG = *blanks;
        return *on;

      when (g_MODE = MNTCMD_CHANGE or
            g_MODE = MNTCMD_DELETE);

        dou (not %error);
          chain(e) (g_JOB : g_POS : g_TYPE : g_SEQ) CMDRF;
          if (%error);
            HdlRcdLck(PSDS);
          endif;
        enddo;
        if (not %found);
          p_SUCCESS = 'N';
          p_MSG = '003';
          return *off;
        else;
          p_SUCCESS = 'Y';
          p_MSG = *blanks;
          return *on;
        endif;

      when (g_MODE = MNTCMD_DISPLAY or
            g_MODE = MNTCMD_COPY);

        chain(n) (g_JOB : g_POS : g_TYPE : g_SEQ) CMDRF;
        if (not %found);
          p_SUCCESS = 'N';
          p_MSG = '003';
          return *off;
        else;
          p_SUCCESS = 'Y';
          p_MSG = *blanks;
          return *on;
        endif;

      when (g_MODE = MNTCMD_CHKACT);

        if (g_TYPE <> *blanks or
            g_SEQ <> *zero);
          chain(n) (g_JOB : g_POS : g_TYPE : g_SEQ) CMDRF;
          if (not %found);
            p_SUCCESS = 'N';
            p_MSG = '003';
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
        p_MSG = '004';
        return *off;

      endsl;

    endif;

  endif;

END-PROC MNTCMD_initialize;

//*=========================================================================================*
//* Get Values                                                                              *
//*=========================================================================================*

DCL-PROC MNTCMD_getValues EXPORT;

  DCL-PI MNTCMD_getValues;
    p_TYPE CHAR(10);      // <-- Type
    p_SEQ  PACKED(3 : 0); // <-- Sequence
    p_CMD  CHAR(1024);    // <-- Command
  END-PI;

  if (g_MODE = MNTCMD_CREATE);

    p_TYPE = *blanks;
    p_SEQ = *zero;
    p_CMD = *blanks;

  else;

    p_TYPE = X6TYPE;
    p_SEQ =  X6SEQ;
    p_CMD =  X6CMD;

  endif;

END-PROC MNTCMD_getValues;

//*=========================================================================================*
//* Set Values                                                                              *
//*=========================================================================================*

DCL-PROC MNTCMD_setValues EXPORT;

  DCL-PI MNTCMD_setValues;
    p_TYPE CHAR(10) CONST;      // --> Type
    p_SEQ  PACKED(3 : 0) CONST; // --> Sequence
    p_CMD  CHAR(1024) CONST;    // --> Command
  END-PI;

  g_TYPE = p_TYPE;
  g_SEQ = p_SEQ;
  g_CMD = p_CMD;

END-PROC MNTCMD_setValues;

//*=========================================================================================*
//* Check                                                                                   *
//*=========================================================================================*

DCL-PROC MNTCMD_check EXPORT;

  DCL-PI MNTCMD_check IND; // <-- Success
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
    if (g_MODE = MNTCMD_CREATE or
        g_MODE = MNTCMD_COPY);

      // The value in field 'Type' is not valid
      if g_TYPE <> '*PRERUN' And
         g_TYPE <> '*COMPILE' And
         g_TYPE <> '*POSTRUN';
        p_FLD = 'TYPE';
        l_MSGID = 'ERR0002';
        leave;
      endif;

      // The value in field 'Sequence' is not valid
      if g_SEQ <= *Zero;
        p_FLD = 'SEQ';
        l_MSGID = 'ERR0009';
        leave;
      endif;

      // if 'Type' is *COMPILE, then 'Sequence' has to be 5
      if g_TYPE = '*COMPILE' And
         g_SEQ <> 5;
        p_FLD = 'SEQ';
        l_MSGID = 'ERR0006';
        leave;
      endif;

      // Type/Sequence does already exist
      SetLL ( g_JOB : g_POS : g_TYPE : g_SEQ ) CMDRF;
      if %Equal;
        p_FLD = 'TYPESEQ';
        l_MSGID = 'ERR0001';
        leave;
      endif;

    endif;

    //*** Check only for mode CREATE, COPY and CHANGE ***
    if (g_MODE = MNTCMD_CREATE or
        g_MODE = MNTCMD_COPY or
        g_MODE = MNTCMD_CHANGE);

      // The value in field 'Command' is not valid
      if g_CMD = *Blanks;
        p_FLD = 'CMD';
        l_MSGID = 'ERR0019';
        leave;
      endif;

    endif;

    //*** Check only for mode DELETE ***
    if (g_MODE = MNTCMD_DELETE);
    endif;

  enddo;

  // Return
  if (l_MSGID = *blanks);

    p_SUCCESS = 'Y';
    return *on;

  else;

    p_MSG = rtvMsgText(
      l_MSGID :  // --> Message-Id.
      'MNTCMD' : // --> Message file
      '*LIBL' :  // --> Library
      l_MSGDTA   // --> Message data
    );

    p_SUCCESS = 'N';
    return *off;

  endif;

END-PROC MNTCMD_check;

//*=========================================================================================*
//* Book                                                                                    *
//*=========================================================================================*

DCL-PROC MNTCMD_book EXPORT;

  DCL-PI MNTCMD_book;
  END-PI;

  select;

  when (g_MODE = MNTCMD_CREATE);
    exsr modeCreate;

  when (g_MODE = MNTCMD_COPY);
    exsr modeCopy;

  when (g_MODE = MNTCMD_CHANGE);
    exsr modeChange;

  when (g_MODE = MNTCMD_DELETE);
    exsr modeDelete;

  when (g_MODE = MNTCMD_DISPLAY);
    exsr modeDisplay;

  endsl;

  //*------------------------------------------------------------------------------------------*
  //* Mode Create                                                                              *
  //*------------------------------------------------------------------------------------------*

  begsr modeCreate;

    clear CMDRF;
    X6JOB = g_JOB;
    X6POS = g_POS;

    exsr fillRecord;

    write CMDRF;

    if RAPIDFIRE_getAutoCommit() = 'Y';
      commit;
    endif;

  endsr;

  //*------------------------------------------------------------------------------------------*
  //* Mode Copy                                                                                *
  //*------------------------------------------------------------------------------------------*

  begsr modeCopy;

    exsr fillRecord;

    write CMDRF;

    if RAPIDFIRE_getAutoCommit() = 'Y';
      commit;
    endif;

  endsr;

  //*------------------------------------------------------------------------------------------*
  //* Mode Change                                                                              *
  //*------------------------------------------------------------------------------------------*

  begsr modeChange;

    exsr fillRecord;

    update CMDRF;

    if RAPIDFIRE_getAutoCommit() = 'Y';
      commit;
    endif;

  endsr;

  //*------------------------------------------------------------------------------------------*
  //* Mode Delete                                                                              *
  //*------------------------------------------------------------------------------------------*

  begsr modeDelete;

    delete CMDRF;

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

    X6TYPE = g_TYPE;
    X6SEQ = g_SEQ;
    X6CMD = g_CMD;

  endsr;

END-PROC MNTCMD_book;

//*=========================================================================================*
//* Check Action                                                                            *
//*=========================================================================================*

DCL-PROC MNTCMD_checkAction EXPORT;

  DCL-PI MNTCMD_checkAction IND;   // <-- Success
    p_ACT     CHAR(10) CONST;      // --> Action
    p_JOB     CHAR(10) CONST;      // --> Job
    p_POS     PACKED(6 : 0) CONST; // --> Position
    p_TYPE    CHAR(10) CONST;      // --> Type
    p_SEQ     PACKED(3 : 0) CONST; // --> Sequence
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

    // Invalid parameter values. Error = Type has to be blanks and sequence has to be zero.
    if (p_ACT = MNTCMD_CREATE and
        (p_TYPE <> *blanks or
        p_SEQ <> *zero));
      l_MSGID = 'ERR0003';
      leave;
    endif;

    // Invalid parameter values. Error = &1.
    l_OPN = g_OPN;
    if (not l_OPN);
      MNTCMD_openFiles();
    endif;
    MNTCMD_initialize(
      MNTCMD_CHKACT : // --> Mode
      p_JOB :         // --> Job
      p_POS :         // --> Position
      p_TYPE :        // --> Type
      p_SEQ :         // --> Sequence
      l_SUCCESS :     // <-- Success
      l_MSG           // <-- Message
    );
    if (not l_OPN);
      MNTCMD_closeFiles();
    endif;
    if (l_SUCCESS = 'N');
      l_MSGID = 'ERR0004';
      l_MSGDTA = l_MSG;
      leave;
    endif;

    // The action is not valid for such type of files
    if (not MNTFILE_checkAction(
              MNTFILE_MNTCMD : // --> Action
              p_JOB :          // --> Job
              p_POS :          // --> Position
              l_SUCCESS :      // <-- Success
              l_MSG            // <-- Message
            )
       );
      l_MSGID = 'ERR0005';
      l_MSGDTA = l_MSG;
      leave;
    endif;

    // The action is only valid for jobs with status *RDY
    if ((p_ACT = MNTCMD_CREATE or
         p_ACT = MNTCMD_COPY or
         p_ACT = MNTCMD_CHANGE or
         p_ACT = MNTCMD_DELETE) and
        X1STS <> '*RDY');
      l_MSGID = 'ERR0007';
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
      'MNTCMD' : // --> Message file
      '*LIBL' :  // --> Library
      l_MSGDTA   // --> Message data
    );

    p_SUCCESS = 'N';
    return *off;

  endif;

END-PROC MNTCMD_checkAction;

//*=========================================================================================*
//* Get Valid Actions                                                                       *
//*=========================================================================================*

DCL-PROC MNTCMD_getValidActions EXPORT;

  DCL-PI MNTCMD_getValidActions;
    p_JOB    CHAR(10) CONST;      // --> Job
    p_POS    PACKED(6 : 0) CONST; // --> Position
    p_TYPE   CHAR(10) CONST;      // --> Type
    p_SEQ    PACKED(3 : 0) CONST; // --> Sequence
    p_NBRACT PACKED(2 : 0);       // <-- Number of actions
    p_ACT    CHAR(10) DIM(25);    // <-- Actions
  END-PI;

  DCL-S l_ACT     CHAR(10);      // --> Action
  DCL-S l_SUCCESS CHAR(1);       // <-- Success
  DCL-S l_MSG     CHAR(256);     // <-- Message

  p_NBRACT = *zero;
  p_ACT(*) = *blanks;

  l_ACT = MNTCMD_COPY;
    exsr checkAction;

  l_ACT = MNTCMD_CHANGE;
    exsr checkAction;

  l_ACT = MNTCMD_DELETE;
    exsr checkAction;

  l_ACT = MNTCMD_DISPLAY;
    exsr checkAction;

  return;

  begsr checkAction;

    if (MNTCMD_checkAction(
          l_ACT :     // --> Action
          p_JOB :     // --> Job
          p_POS :     // --> Position
          p_TYPE :    // --> Type
          p_SEQ :     // --> Sequence
          l_SUCCESS : // <-- Success
          l_MSG       // <-- Message
        ));
      p_NBRACT += 1;
      p_ACT(p_NBRACT) = l_ACT;
    endif;

  endsr;

END-PROC MNTCMD_getValidActions;
