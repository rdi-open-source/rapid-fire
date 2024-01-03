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
//* Maintain File                                                                           *
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
      RENAME(JOBS: JOBRF)
      USROPN;

DCL-F FILES
      DISK(*EXT)
      USAGE(*INPUT : *OUTPUT : *UPDATE : *DELETE)
      KEYED
      RENAME(FILES : FILERF)
      COMMIT
      USROPN;

//*-----------------------------------------------------------------------------------------*

/INCLUDE QCPYSRC,MNTJOB

/INCLUDE QCPYSRC,MNTFILE

/INCLUDE QCPYSRC,RAPIDFIRE

/INCLUDE QCPYSRC,HDLRCDLCK

/INCLUDE QCPYSRC,RTVMSGD

/INCLUDE QCPYSRC,CHKVAL

DCL-DS PSDS LEN(429) QUALIFIED PSDS;
END-DS;

DCL-S g_MODE CHAR(10);      // Mode
DCL-S g_JOB  CHAR(10);      // Job
DCL-S g_POS  PACKED(6 : 0); // <-- Position
DCL-S g_FILE CHAR(10);      // <-- File
DCL-S g_TYPE CHAR(10);      // <-- Type
DCL-S g_CPL  CHAR(10);      // <-- Copy program (Library)
DCL-S g_CP   CHAR(10);      // <-- Copy program
DCL-S g_VPL  CHAR(10);      // <-- Conversion program (Library)
DCL-S g_VP   CHAR(10);      // <-- Conversion program
DCL-S g_OPN  IND INZ(*OFF); // Open indicator

//*=========================================================================================*
//* Open Files                                                                              *
//*=========================================================================================*

DCL-PROC MNTFILE_openFiles EXPORT;

  DCL-PI MNTFILE_openFiles;
  END-PI;

  open JOBS;
  open FILES;

  g_OPN = *on;

END-PROC MNTFILE_openFiles;

//*=========================================================================================*
//* Close Files                                                                             *
//*=========================================================================================*

DCL-PROC MNTFILE_closeFiles EXPORT;

  DCL-PI MNTFILE_closeFiles;
  END-PI;

  close JOBS;
  close FILES;

  g_OPN = *off;

END-PROC MNTFILE_closeFiles;

//*=========================================================================================*
//* Initialize                                                                              *
//*=========================================================================================*
//*                                                                                         *
//* Error codes and their meanings                                                          *
//* ==============================                                                          *
//*                                                                                         *
//* '001' = Record in file JOBS not found                                                   *
//* '002' = Record in file FILES not found                                                  *
//* '003' = Wrong mode                                                                      *
//*                                                                                         *
//*=========================================================================================*

DCL-PROC MNTFILE_initialize EXPORT;

  DCL-PI MNTFILE_initialize IND;   // <-- Success
    p_MODE    CHAR(10)      CONST; // --> Mode
    p_JOB     CHAR(10)      CONST; // --> Job
    p_POS     PACKED(6 : 0) CONST; // --> Position
    p_SUCCESS CHAR(1);             // <-- Success
    p_MSG     CHAR(256);           // <-- Message
  END-PI;

  g_MODE = p_MODE;
  g_JOB = p_JOB;
  g_POS = p_POS;

  chain (g_JOB) JOBRF;
  if (not %found);

    p_SUCCESS = 'N';
    p_MSG = '001';
    return *off;

  else;

    select;

    when (g_MODE = MNTFILE_CREATE);

      p_SUCCESS = 'Y';
      p_MSG = *blanks;
      return *on;

    when (g_MODE = MNTFILE_CHANGE or
          g_MODE = MNTFILE_DELETE);

      dou (not %error);
        chain(e) (g_JOB : g_POS) FILERF;
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

    when (g_MODE = MNTFILE_DISPLAY or
          g_MODE = MNTFILE_COPY);

      chain(n) (g_JOB : g_POS) FILERF;
      if (not %found);
        p_SUCCESS = 'N';
        p_MSG = '002';
        return *off;
      else;
        p_SUCCESS = 'Y';
        p_MSG = *blanks;
        return *on;
      endif;

    when (g_MODE = MNTFILE_CHKACT);

      if (g_POS <> *zero);
        chain(n) (g_JOB : g_POS) FILERF;
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

END-PROC MNTFILE_initialize;

//*=========================================================================================*
//* Get Values                                                                              *
//*=========================================================================================*

DCL-PROC MNTFILE_getValues EXPORT;

  DCL-PI MNTFILE_getValues;
    p_POS  PACKED(6 : 0); // <-- Position
    p_FILE CHAR(10);      // <-- File
    p_TYPE CHAR(10);      // <-- Type
    p_CPL  CHAR(10);      // <-- Copy program (Library)
    p_CP   CHAR(10);      // <-- Copy program
    p_VPL  CHAR(10);      // <-- Conversion program (Library)
    p_VP   CHAR(10);      // <-- Conversion program
  END-PI;

  if (g_MODE = MNTFILE_CREATE);

    p_POS  = *zero;
    p_FILE = *blanks;
    p_TYPE = *blanks;
    p_CPL  = *blanks;
    p_CP   = *blanks;
    p_VPL  = *blanks;
    p_VP   = '*NONE';

  else;

    p_POS  = X3POS;
    p_FILE = X3FILE;
    p_TYPE = X3TYPE;
    p_CPL  = X3CPL;
    p_CP   = X3CP;
    p_VPL  = X3VPL;
    p_VP   = X3VP;

  endif;

END-PROC MNTFILE_getValues;

//*=========================================================================================*
//* Set Values                                                                              *
//*=========================================================================================*

DCL-PROC MNTFILE_setValues EXPORT;

  DCL-PI MNTFILE_setValues;
    p_POS  PACKED(6 : 0) CONST; // --> Position
    p_FILE CHAR(10) CONST;      // --> File
    p_TYPE CHAR(10) CONST;      // --> Type
    p_CPL  CHAR(10) CONST;      // --> Copy program (Library)
    p_CP   CHAR(10) CONST;      // --> Copy program
    p_VPL  CHAR(10) CONST;      // --> Conversion program (Library)
    p_VP   CHAR(10) CONST;      // --> Conversion program
  END-PI;

  g_POS = p_POS;
  g_FILE = p_FILE;
  g_TYPE = p_TYPE;
  g_CPL = p_CPL;
  g_CP = p_CP;
  g_VPL = p_VPL;
  g_VP = p_VP;

END-PROC MNTFILE_setValues;

//*=========================================================================================*
//* Check                                                                                   *
//*=========================================================================================*

DCL-PROC MNTFILE_check EXPORT;

  DCL-PI MNTFILE_check IND; // <-- Success
    p_SUCCESS CHAR(1);      // <-- Success
    p_FLD     CHAR(10);     // <-- Field
    p_MSG     CHAR(256);    // <-- Message
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
    if (g_MODE = MNTFILE_CREATE or
        g_MODE = MNTFILE_COPY);

      // The value in field 'Position' is not valid
      if g_POS <= *zero;
        p_FLD = 'POS';
        l_MSGID = 'ERR0002';
        leave;
      endif;

      // The position does already exist
      SetLL ( g_JOB : g_POS ) FILERF;
      if %Equal;
        p_FLD = 'POS';
        l_MSGID = 'ERR0001';
        leave;
      endif;

    endif;

    //*** Check only for mode CREATE, COPY and CHANGE ***
    if (g_MODE = MNTFILE_CREATE or
        g_MODE = MNTFILE_COPY or
        g_MODE = MNTFILE_CHANGE);

      // The value in field 'File' is not valid
      if ChkVal('*NAME' : 10 : %Addr(g_FILE)) = '*INVALID';
        p_FLD = 'FILE';
        l_MSGID = 'ERR0005';
        leave;
      endif;

      // The value in field 'Type' is not valid
      if g_TYPE <> '*PHYSICAL' and g_TYPE <> '*LOGICAL';
        p_FLD = 'TYPE';
        l_MSGID = 'ERR0006';
        leave;
      endif;

      // The value in field 'Copy program' is not valid
      if g_CP <> '*GEN' and g_CP <> '*NONE' and
         ChkVal('*NAME' : 10 : %Addr(g_CP)) = '*INVALID';
        p_FLD = 'CP';
        l_MSGID = 'ERR0009';
        leave;
      endif;

      // For physical files the value *NONE is not allowed
      if g_TYPE = '*PHYSICAL' and
         g_CP = '*NONE';
        p_FLD = 'CP';
        l_MSGID = 'ERR0010';
        leave;
      endif;

      // For logical files only the value *NONE is allowed
      if g_TYPE = '*LOGICAL' and
         g_CP <> '*NONE';
        p_FLD = 'CP';
        l_MSGID = 'ERR0011';
        leave;
      endif;

      // The value in field 'Copy program - Library' is not valid
      if g_CPL <> *blanks and
         ChkVal('*NAME' : 10 : %Addr(g_CPL)) = '*INVALID';
        p_FLD = 'CPL';
        l_MSGID = 'ERR0012';
        leave;
      endif;

      // The library has to be specified
      if g_CP <> '*GEN' and
         g_CP <> '*NONE' and
         g_CPL = *blanks;
        p_FLD = 'CPL';
        l_MSGID = 'ERR0013';
        leave;
      endif;

      // The library must not be specified
      if (g_CP = '*GEN' or
         g_CP = '*NONE') and
         g_CPL <> *blanks;
        p_FLD = 'CPL';
        l_MSGID = 'ERR0014';
        leave;
      endif;

      // The value in field 'Conversion program' is not valid
      if g_VP <> '*NONE' and
         ChkVal('*NAME' : 10 : %Addr(g_VP)) = '*INVALID';
        p_FLD = 'VP';
        l_MSGID = 'ERR0015';
        leave;
      endif;

      // For logical files only the value *NONE is allowed
      if g_TYPE = '*LOGICAL' and
         g_VP <> '*NONE';
        p_FLD = 'VP';
        l_MSGID = 'ERR0016';
        leave;
      endif;

      // The value in field 'Conversion program - Library' is not valid
      if g_VPL <> *blanks and
         ChkVal('*NAME' : 10 : %Addr(g_VPL)) = '*INVALID';
        p_FLD = 'VPL';
        l_MSGID = 'ERR0017';
        leave;
      endif;

      // The library has to be specified
      if g_VP <> '*NONE' and
         g_VPL = *blanks;
        p_FLD = 'VPL';
        l_MSGID = 'ERR0018';
        leave;
      endif;

      // The library must not be specified
      if g_VP = '*NONE' and
         g_VPL <> *blanks;
        p_FLD = 'VPL';
        l_MSGID = 'ERR0019';
        leave;
      endif;

    endif;

    //*** Check only for mode DELETE ***
    if (g_MODE = MNTFILE_DELETE);

    endif;

  enddo;

  // Return
  if (l_MSGID = *blanks);
    p_SUCCESS = 'Y';
    return *on;

  else;

    p_MSG = rtvMsgText(
      l_MSGID :  // --> Message-Id.
      'MNTFILE' : // --> Message file
      '*LIBL' :  // --> Library
      l_MSGDTA   // --> Message data
    );

    p_SUCCESS = 'N';
    return *off;

  endif;

END-PROC MNTFILE_check;

//*=========================================================================================*
//* Book                                                                                    *
//*=========================================================================================*

DCL-PROC MNTFILE_book EXPORT;

  DCL-PI MNTFILE_book;
  END-PI;

  select;

  when (g_MODE = MNTFILE_CREATE);
    exsr modeCreate;

  when (g_MODE = MNTFILE_COPY);
    exsr modeCopy;

  when (g_MODE = MNTFILE_CHANGE);
    exsr modeChange;

  when (g_MODE = MNTFILE_DELETE);
    exsr modeDelete;

  when (g_MODE = MNTFILE_DISPLAY);
    exsr modeDisplay;

  endsl;

  //*------------------------------------------------------------------------------------------*
  //* Mode Create                                                                              *
  //*------------------------------------------------------------------------------------------*

  begsr modeCreate;

    clear FILERF;
    X3JOB = g_JOB;

    exsr fillRecord;

    write FILERF;

    if RAPIDFIRE_getAutoCommit() = 'Y';
      commit;
    endif;

  endsr;

  //*------------------------------------------------------------------------------------------*
  //* Mode Copy                                                                                *
  //*------------------------------------------------------------------------------------------*

  begsr modeCopy;

    exsr fillRecord;

    write FILERF;

    if RAPIDFIRE_getAutoCommit() = 'Y';
      commit;
    endif;

  endsr;

  //*------------------------------------------------------------------------------------------*
  //* Mode Change                                                                              *
  //*------------------------------------------------------------------------------------------*

  begsr modeChange;

    exsr fillRecord;

    update FILERF;

    if RAPIDFIRE_getAutoCommit() = 'Y';
      commit;
    endif;

  endsr;

  //*------------------------------------------------------------------------------------------*
  //* Mode Delete                                                                              *
  //*------------------------------------------------------------------------------------------*

  begsr modeDelete;

    delete FILERF;

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

    X3POS = g_POS;
    X3FILE = g_FILE;
    X3TYPE = g_TYPE;
    X3CPL = g_CPL;
    X3CP = g_CP;
    X3VPL = g_VPL;
    X3VP = g_VP;

  endsr;

END-PROC MNTFILE_book;

//*=========================================================================================*
//* Check Action                                                                            *
//*=========================================================================================*

DCL-PROC MNTFILE_checkAction EXPORT;

  DCL-PI MNTFILE_checkAction IND;  // <-- Success
    p_ACT     CHAR(10) CONST;      // --> Action
    p_JOB     CHAR(10) CONST;      // --> Job
    p_POS     PACKED(6 : 0) CONST; // --> Position
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

    // Invalid parameter values. Error = Position has to be zero.
    if (p_ACT = MNTFILE_CREATE and
        p_POS <> *zero);
      l_MSGID = 'ERR0021';
      leave;
    endif;

    // Invalid parameter values. Error = &1.
    l_OPN = g_OPN;
    if (not l_OPN);
      MNTFILE_openFiles();
    endif;
    MNTFILE_initialize(
      MNTFILE_CHKACT : // --> Mode
      p_JOB :          // --> Job
      p_POS :          // --> Position
      l_SUCCESS :      // <-- Success
      l_MSG            // <-- Message
    );
    if (not l_OPN);
      MNTFILE_closeFiles();
    endif;
    if (l_SUCCESS = 'N');
      l_MSGID = 'ERR0003';
      l_MSGDTA = l_MSG;
      leave;
    endif;

    // The action is not valid for such type of jobs
    if (not MNTJOB_checkAction(
              MNTJOB_MNTFILE : // --> Action
              p_JOB :          // --> Job
              l_SUCCESS :      // <-- Success
              l_MSG            // <-- Message
            )
       );
      l_MSGID = 'ERR0022';
      l_MSGDTA = l_MSG;
      leave;
    endif;

    // The action is only valid for jobs with status *RDY
    if ((p_ACT = MNTFILE_CREATE or
         p_ACT = MNTFILE_COPY or
         p_ACT = MNTFILE_CHANGE or
         p_ACT = MNTFILE_DELETE) and
        X1STS <> '*RDY');
      l_MSGID = 'ERR0004';
      leave;
    endif;

    // The action is only valid for files with "Type" *PHYSICAL
    If ((p_ACT = MNTFILE_MNTCNV Or
         p_ACT = MNTFILE_GNRCPYPGM) And
       X3TYPE <> '*PHYSICAL');
      l_MSGID = 'ERR0007';
      leave;
    EndIf;

    // The action is only valid for jobs with "Create environment" *YES
    If (p_ACT = MNTFILE_MNTCMD And
       X1CRTE <> '*YES');
      l_MSGID = 'ERR0020';
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
      'MNTFILE' : // --> Message file
      '*LIBL' :   // --> Library
      l_MSGDTA    // --> Message data
    );

    p_SUCCESS = 'N';
    return *off;

  endif;

END-PROC MNTFILE_checkAction;

//*=========================================================================================*
//* Get Valid Actions                                                                       *
//*=========================================================================================*

DCL-PROC MNTFILE_getValidActions EXPORT;

  DCL-PI MNTFILE_getValidActions;
    p_JOB     CHAR(10) CONST;      // --> Job
    p_POS     PACKED(6 : 0) CONST; // --> Position
    p_NBRACT  PACKED(2 : 0);       // <-- Number of actions
    p_ACT     CHAR(10) DIM(25);    // <-- Actions
  END-PI;

  DCL-S l_ACT     CHAR(10);      // --> Action
  DCL-S l_SUCCESS CHAR(1);       // <-- Success
  DCL-S l_MSG     CHAR(256);     // <-- Message

  p_NBRACT = *zero;
  p_ACT(*) = *blanks;

  l_ACT = MNTFILE_COPY;
    exsr checkAction;

  l_ACT = MNTFILE_CHANGE;
    exsr checkAction;

  l_ACT = MNTFILE_DELETE;
    exsr checkAction;

  l_ACT = MNTFILE_DISPLAY;
    exsr checkAction;

  l_ACT = MNTFILE_MNTARA;
    exsr checkAction;

  l_ACT = MNTFILE_MNTCNV;
    exsr checkAction;

  l_ACT = MNTFILE_MNTCMD;
    exsr checkAction;

  l_ACT = MNTFILE_GNRCPYPGM;
    exsr checkAction;

  return;

  begsr checkAction;

    if (MNTFILE_checkAction(
          l_ACT :     // --> Action
          p_JOB :     // --> Job
          p_POS :     // --> Position
          l_SUCCESS : // <-- Success
          l_MSG       // <-- Message
        ));
      p_NBRACT += 1;
      p_ACT(p_NBRACT) = l_ACT;
    endif;

  endsr;

END-PROC MNTFILE_getValidActions;
