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

DCL-F FILESI01
      DISK(*EXT)
      USAGE(*INPUT)
      KEYED
      RENAME(FILES : FILERF01)
      USROPN;

//*-----------------------------------------------------------------------------------------*

/INCLUDE QCPYSRC,MNTFILE

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

DCL-S g_MODE CHAR(10);      // Mode
DCL-S g_JOB  CHAR(10);      // Job
DCL-S g_POS  PACKED(6 : 0); // <-- Position
DCL-S g_FILE CHAR(10);      // <-- File
DCL-S g_TYPE CHAR(10);      // <-- Type
DCL-S g_CPL  CHAR(10);      // <-- Copy program (Library)
DCL-S g_CP   CHAR(10);      // <-- Copy program
DCL-S g_VPL  CHAR(10);      // <-- Conversion program (Library)
DCL-S g_VP   CHAR(10);      // <-- Conversion program

//*=========================================================================================*
//* Open Files                                                                              *
//*=========================================================================================*

DCL-PROC MNTFILE_openFiles EXPORT;

  DCL-PI MNTFILE_openFiles;
  END-PI;

  open JOBS;
  open FILES;
  open FILESI01;

END-PROC MNTFILE_openFiles;

//*=========================================================================================*
//* Close Files                                                                             *
//*=========================================================================================*

DCL-PROC MNTFILE_closeFiles EXPORT;

  DCL-PI MNTFILE_closeFiles;
  END-PI;

  close JOBS;
  close FILES;
  close FILESI01;

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

    when (g_MODE = MODE_CREATE);

      p_SUCCESS = 'Y';
      p_MSG = *blanks;
      return *on;

    when (g_MODE = MODE_CHANGE or
          g_MODE = MODE_DELETE);

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

    when (g_MODE = MODE_DISPLAY or
          g_MODE = MODE_COPY);

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

  if (g_MODE = MODE_CREATE);

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
    if (g_MODE = MODE_CREATE or
        g_MODE = MODE_COPY);

      // The value in field 'Position' is not valid
      if g_POS = *zero;
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
    if (g_MODE = MODE_CREATE or
        g_MODE = MODE_COPY or
        g_MODE = MODE_CHANGE);

      // The value in field 'File' is not valid
      if ChkVal('*NAME' : 10 : %Addr(g_FILE)) = '*INVALID';
        p_FLD = 'FILE';
        l_MSGID = 'ERR0005';
        leave;
      endif;

      // The file does already exist
      if Not (g_MODE = MODE_CHANGE and
              g_FILE = X3FILE);
        SetLL ( g_JOB : g_FILE) FILERF01;
        if %Equal;
          p_FLD = 'FILE';
          l_MSGID = 'ERR0008';
          leave;
        endif;
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
    if (g_MODE = MODE_DELETE);

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

  //*------------------------------------------------------------------------------------------*
  //* Mode Create                                                                              *
  //*------------------------------------------------------------------------------------------*

  begsr modeCreate;

    clear FILERF;
    X3JOB = g_JOB;

    exsr fillRecord;

    write FILERF;

    commit;

  endsr;

  //*------------------------------------------------------------------------------------------*
  //* Mode Copy                                                                                *
  //*------------------------------------------------------------------------------------------*

  begsr modeCopy;

    exsr fillRecord;

    write FILERF;

    commit;

  endsr;

  //*------------------------------------------------------------------------------------------*
  //* Mode Change                                                                              *
  //*------------------------------------------------------------------------------------------*

  begsr modeChange;

    exsr fillRecord;

    update FILERF;

    commit;

  endsr;

  //*------------------------------------------------------------------------------------------*
  //* Mode Delete                                                                              *
  //*------------------------------------------------------------------------------------------*

  begsr modeDelete;

    delete FILERF;

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

    X3POS = g_POS;
    X3FILE = g_FILE;
    X3TYPE = g_TYPE;
    X3CPL = g_CPL;
    X3CP = g_CP;
    X3VPL = g_VPL;
    X3VP = g_VP;

  endsr;

END-PROC MNTFILE_book;
