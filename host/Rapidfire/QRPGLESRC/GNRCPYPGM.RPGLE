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
//* Generate Copy Program                                                                   *
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
//* 18.12.2017 I   FH   I Creation                                                          *
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

DCL-F AREAS
      DISK(*EXT)
      USAGE(*INPUT)
      KEYED
      RENAME(AREAS : AREARF)
      USROPN;

DCL-F LIBRARIES
      DISK(*EXT)
      USAGE(*INPUT)
      KEYED
      RENAME(LIBRARIES : LIBRARYF)
      USROPN;

//*-----------------------------------------------------------------------------------------*

/INCLUDE QCPYSRC,GNRCPYPGM

/INCLUDE QCPYSRC,RTVMSGD

/INCLUDE QCPYSRC,CHKVAL

/INCLUDE QCPYSRC,CHKOBJ

/INCLUDE QCPYSRC,PROMOTER

/INCLUDE QCPYSRC,RAPIDFIRE

DCL-S g_JOB  CHAR(10);      // Job
DCL-S g_POS  PACKED(6 : 0); // Position
DCL-S g_SRCF CHAR(10);      // Source file
DCL-S g_SRCL CHAR(10);      // Source file library
DCL-S g_SRCM CHAR(10);      // Source member
DCL-S g_REP  CHAR(10);      // Replace
DCL-S g_ARA  CHAR(10);      // Area
DCL-S g_LIB  CHAR(10);      // Library
DCL-S g_SLIB CHAR(10);      // Shadow library
DCL-S g_VP   CHAR(10);      // Conversion program
DCL-S g_VPL  CHAR(10);      // Conversion program library
DCL-DS g_qSrcMbr likeds(qMbr_t) inz;

//*=========================================================================================*
//* Open Files                                                                              *
//*=========================================================================================*

DCL-PROC GNRCPYPGM_openFiles EXPORT;

  DCL-PI GNRCPYPGM_openFiles;
  END-PI;

  open JOBS;
  open FILES;
  open AREAS;
  open LIBRARIES;

END-PROC GNRCPYPGM_openFiles;

//*=========================================================================================*
//* Close Files                                                                             *
//*=========================================================================================*

DCL-PROC GNRCPYPGM_closeFiles EXPORT;

  DCL-PI GNRCPYPGM_closeFiles;
  END-PI;

  close JOBS;
  close FILES;
  close AREAS;
  close LIBRARIES;

END-PROC GNRCPYPGM_closeFiles;

//*=========================================================================================*
//* Initialize                                                                              *
//*=========================================================================================*
//*                                                                                         *
//* Error codes and their meanings                                                          *
//* ==============================                                                          *
//*                                                                                         *
//* '001' = Record in file JOBS not found                                                   *
//* '002' = Record in file FILES not found                                                  *
//*                                                                                         *
//*=========================================================================================*

DCL-PROC GNRCPYPGM_initialize EXPORT;

  DCL-PI GNRCPYPGM_initialize IND; // <-- Success
    p_JOB     CHAR(10) CONST;      // --> Job
    p_POS     PACKED(6 : 0) CONST; // --> Position
    p_SUCCESS CHAR(1);             // <-- Success
    p_MSG     CHAR(256);           // <-- Message
  END-PI;

  // Set default values

  g_SRCF = *BLANKS;
  g_SRCL = *BLANKS;
  g_SRCM = *BLANKS;
  g_REP  = '*NO';
  g_ARA  = *BLANKS;
  g_LIB  = *BLANKS;
  g_SLIB = *BLANKS;
  g_VP   = '*NONE';
  g_VPL  = *BLANKS;

  // Read JOBS and FILES records.

  g_JOB = p_JOB;
  g_POS = p_POS;

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

      p_SUCCESS = 'Y';
      p_MSG = *blanks;
      return *on;

    endif;

  endif;

END-PROC GNRCPYPGM_initialize;

//*=========================================================================================*
//* Get Values                                                                              *
//*=========================================================================================*

DCL-PROC GNRCPYPGM_getValues EXPORT;

  DCL-PI GNRCPYPGM_getValues;
    p_SRCF CHAR(10); // <-- Source file
    p_SRCL CHAR(10); // <-- Source file library
    p_SRCM CHAR(10); // <-- Source member
    p_REP  CHAR(10); // <-- Replace
    p_ARA  CHAR(10); // <-- Area
    p_LIB  CHAR(10); // <-- Library
    p_SLIB CHAR(10); // <-- Shadow library
    p_VP   CHAR(10); // <-- Conversion program
    p_VPL  CHAR(10); // <-- Conversion program library
  END-PI;

  p_SRCF = g_SRCF;
  p_SRCL = g_SRCL;
  p_SRCM = g_SRCM;
  p_REP  = g_REP;
  p_ARA  = g_ARA;
  p_LIB  = g_LIB;
  p_SLIB = g_SLIB;
  p_VP   = g_VP;
  p_VPL  = g_VPL;

END-PROC GNRCPYPGM_getValues;

//*=========================================================================================*
//* Set Values                                                                              *
//*=========================================================================================*

DCL-PROC GNRCPYPGM_setValues EXPORT;

  DCL-PI GNRCPYPGM_setValues;
    p_SRCF CHAR(10) CONST; // --> Source file
    p_SRCL CHAR(10) CONST; // --> Source file library
    p_SRCM CHAR(10) CONST; // --> Source member
    p_REP  CHAR(10) CONST; // --> Replace
    p_ARA  CHAR(10) CONST; // --> Area
    p_LIB  CHAR(10) CONST; // --> Library
    p_SLIB CHAR(10) CONST; // --> Shadow library
    p_VP   CHAR(10) CONST; // --> Conversion program
    p_VPL  CHAR(10) CONST; // --> Conversion program library
  END-PI;

  g_SRCF = p_SRCF;
  g_SRCL = p_SRCL;
  g_SRCM = p_SRCM;
  g_REP  = p_REP;
  g_ARA  = p_ARA;
  g_LIB  = p_LIB;
  g_SLIB = p_SLIB;
  g_VP   = p_VP;
  g_VPL  = p_VPL;

END-PROC GNRCPYPGM_setValues;

//*=========================================================================================*
//* Check                                                                                   *
//*=========================================================================================*

DCL-PROC GNRCPYPGM_check EXPORT;

  DCL-PI GNRCPYPGM_check IND; // <-- Success
    p_SUCCESS CHAR(1);        // <-- Success
    p_FLD     CHAR(10);       // <-- Field
    p_MSG     CHAR(256);      // <-- Message
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

    // The value in field 'Source File' is not valid.
    If ChkVal('*NAME' : 10 : %Addr(g_SRCF)) = '*INVALID';
      p_FLD = 'SRCF';
      l_MSGID = 'ERR0001';
      Leave;
    EndIf;

    // The value in field 'Library' is not valid.
    If ChkVal('*NAME' : 10 : %Addr(g_SRCL)) = '*INVALID';
      p_FLD = 'SRCL';
      l_MSGID = 'ERR0002';
      Leave;
    EndIf;

    // The value in field 'Source member' is not valid.
    If ChkVal('*NAME' : 10 : %Addr(g_SRCM)) = '*INVALID';
      p_FLD = 'SRCM';
      l_MSGID = 'ERR0003';
      Leave;
    EndIf;

    // Library does not exist.
    If not Library_exist (g_SRCL);
      p_FLD = 'SRCL';
      l_MSGID = 'ERR0004';
      Leave;
    EndIf;

    // File does not exist.
    If not File_exist (g_SRCF: g_SRCL);
      p_FLD = 'SRCF';
      l_MSGID = 'ERR0005';
      Leave;
    EndIf;

    // File is not a source file.
    If not File_isSourceFile(g_SRCF: g_SRCL);
      p_FLD = 'SRCF';
      l_MSGID = 'ERR0006';
      Leave;
    EndIf;

    // The value in field 'Replace' is not valid.
    If g_REP <> '*YES' And g_REP <> '*NO';
      p_FLD = 'REP';
      l_MSGID = 'ERR0014';
      Leave;
    EndIf;

    // Member already exists.
    If g_REP <> '*YES' And Member_exist (g_SRCF: g_SRCL: g_SRCM);
      p_FLD = 'SRCM';
      l_MSGID = 'ERR0007';
      Leave;
    EndIf;

    // Area and libraries are missing. Specify the area or the libraries.
    If g_ARA = '' And g_LIB = '' And g_SLIB = '';
      p_FLD = 'ARA_LIB';
      l_MSGID = 'ERR0017';
      Leave;
    EndIf;

    // The value in field 'Area' is not valid.
    If g_ARA <> '*NONE' And g_ARA <> '' And
       ChkVal('*NAME' : 10 : %Addr(g_ARA)) = '*INVALID';
      p_FLD = 'ARA';
      l_MSGID = 'ERR0008';
      Leave;
    EndIf;

    If g_ARA <> '';

      // Area does not exist.
      Setll ( g_JOB : g_POS : g_ARA ) AREARF;
      If not %equal(AREAS);
        p_FLD = 'ARA';
        l_MSGID = 'ERR0009';
        Leave;
      EndIf;

      // Set libraries
      g_LIB = '';
      g_SLIB = '';
      Chain ( g_JOB : g_POS : g_ARA ) AREARF;
      If %found(AREAS);
        Chain ( XDJOB : XDLIB ) LIBRARYF;
        If %found(LIBRARIES);
          g_LIB = X2LIB;
          g_SLIB = X2SLIB;
        Else;
          // Library record of area not found.
          p_FLD = 'ARA';
          l_MSGID = 'ERR0010';
          Leave;
        EndIf;
      EndIf;

    Endif;

    // Data library with old file does not exist.
    If not Library_exist (g_LIB);
      p_FLD = 'LIB';
      l_MSGID = 'ERR0018';
      Leave;
    EndIf;

    // Old file does not exist in data library.
    If not File_exist (X3FILE: g_LIB);
      p_FLD = 'LIB';
      l_MSGID = 'ERR0020';
      Leave;
    EndIf;

    // Shadow library with new file does not exist.
    If not Library_exist (g_SLIB);
      p_FLD = 'SLIB';
      l_MSGID = 'ERR0019';
      Leave;
    EndIf;

    // New file does not exist in shadow library.
    If not File_exist (X3FILE: g_SLIB);
      p_FLD = 'SLIB';
      l_MSGID = 'ERR0021';
      Leave;
    EndIf;

    // The value in field 'Conversion program' is not valid.
    If g_VP <> '*NONE' And
      ChkVal('*NAME' : 10 : %Addr(g_VP)) = '*INVALID';
      p_FLD = 'VP';
      l_MSGID = 'ERR0011';
      Leave;
    EndIf;

    If g_VP = '*NONE';

      // The library must not be specified
      If g_VPL <> '';
        p_FLD = 'VPL';
        l_MSGID = 'ERR0013';
        Leave;
      EndIf;

    Else;

      // The value in field 'Library' is not valid
      If ChkVal('*NAME' : 10 : %Addr(g_VPL)) = '*INVALID';
        p_FLD = 'VPL';
        l_MSGID = 'ERR0012';
        Leave;
      EndIf;

    EndIf;

  enddo;

  // Return
  if (l_MSGID = *blanks);
    p_SUCCESS = 'Y';
    return *on;

  else;

    p_MSG = rtvMsgText(
      l_MSGID :     // --> Message-Id.
      'GNRCPYPGM' : // --> Message file
      '*LIBL' :     // --> Library
      l_MSGDTA      // --> Message data
    );

    p_SUCCESS = 'N';
    return *off;

  endif;

END-PROC GNRCPYPGM_check;

//*=========================================================================================*
//* Book                                                                                    *
//*=========================================================================================*

DCL-PROC GNRCPYPGM_book EXPORT;

  DCL-PI GNRCPYPGM_book IND; // <-- Success
    p_SUCCESS CHAR(1);       // <-- Success
    p_MSG     CHAR(256);     // <-- Message
  END-PI;

  RAPIDFIRE_set_Error(
    RAPIDFIRE_JOB_NONE :
    *OFF
  );

  g_qSrcMbr.file = g_SRCF;
  g_qSrcMbr.lib = g_SRCL;
  g_qSrcMbr.name = g_SRCM;

  PROMOTER_generate_Copy_Program(
    g_JOB :
    g_POS :
    g_ARA :
    g_LIB :
    g_SLIB :
    X3FILE :
    g_VPL :
    g_VP :
    *ON :
    g_qSrcMbr
  );

  if (not RAPIDFIRE_is_Error(RAPIDFIRE_JOB_NONE));

    p_SUCCESS = 'Y';
    p_MSG = *blanks;
    return *on;

  else;

    p_SUCCESS = 'N';
    p_MSG = '001';
    return *off;

  endif;

END-PROC GNRCPYPGM_book;
