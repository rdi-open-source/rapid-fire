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
//* Maintain Areas                                                                          *
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

DCL-F FILES
      DISK(*EXT)
      USAGE(*INPUT)
      KEYED
      RENAME(FILES : FILERF)
      USROPN;

DCL-F AREAS
      DISK(*EXT)
      USAGE(*INPUT : *OUTPUT : *UPDATE : *DELETE)
      KEYED
      RENAME(AREAS : AREARF)
      COMMIT
      USROPN;

DCL-F LIBRARIES
      DISK(*EXT)
      USAGE(*INPUT)
      KEYED
      RENAME(LIBRARIES : LIBRF)
      USROPN;

DCL-F LIBLS
      DISK(*EXT)
      USAGE(*INPUT)
      KEYED
      RENAME(LIBLS : LIBLSTRF)
      USROPN;

//*-----------------------------------------------------------------------------------------*

/INCLUDE QCPYSRC,MNTFILE

/INCLUDE QCPYSRC,MNTAREA

/INCLUDE QCPYSRC,RAPIDFIRE

/INCLUDE QCPYSRC,HDLRCDLCK

/INCLUDE QCPYSRC,RTVMSGD

/INCLUDE QCPYSRC,CHKVAL

DCL-DS PSDS LEN(429) QUALifIED PSDS;
END-DS;

DCL-S g_MODE  CHAR(10);      // Mode
DCL-S g_JOB   CHAR(10);      // Job
DCL-S g_POS   PACKED(6 : 0); // Position
DCL-S g_ARA   CHAR(10);      // Area
DCL-S g_LIB   CHAR(10);      // Library
DCL-S g_LIBL  CHAR(10);      // Library list
DCL-S g_CCSID CHAR(10);      // Coded character set ID
DCL-S g_CEXT  CHAR(1024);    // Command extension
DCL-S g_OPN   IND INZ(*OFF); // Open indicator

//*=========================================================================================*
//* Open Files                                                                              *
//*=========================================================================================*

DCL-PROC MNTAREA_openFiles EXPORT;

  DCL-PI MNTAREA_openFiles;
  END-PI;

  open JOBS;
  open FILES;
  open AREAS;
  open LIBRARIES;
  open LIBLS;

  g_OPN = *on;

END-PROC MNTAREA_openFiles;

//*=========================================================================================*
//* Close Files                                                                             *
//*=========================================================================================*

DCL-PROC MNTAREA_closeFiles EXPORT;

  DCL-PI MNTAREA_closeFiles;
  END-PI;

  close JOBS;
  close FILES;
  close AREAS;
  close LIBRARIES;
  close LIBLS;

  g_OPN = *off;

END-PROC MNTAREA_closeFiles;

//*=========================================================================================*
//* Initialize                                                                              *
//*=========================================================================================*
//*                                                                                         *
//* Error codes and their meanings                                                          *
//* ==============================                                                          *
//*                                                                                         *
//* '001' = Record in file JOBS not found                                                   *
//* '002' = Record in file FILES not found                                                  *
//* '003' = Record in file AREAS not found                                                  *
//* '004' = Wrong mode                                                                      *
//*                                                                                         *
//*=========================================================================================*

DCL-PROC MNTAREA_initialize EXPORT;

  DCL-PI MNTAREA_initialize IND;   // <-- Success
    p_MODE    CHAR(10) CONST;      // --> Mode
    p_JOB     CHAR(10) CONST;      // --> Job
    p_POS     PACKED(6 : 0) CONST; // --> Position
    p_ARA     CHAR(10) CONST;      // --> Area
    p_SUCCESS CHAR(1);             // <-- Success
    p_MSG     CHAR(256);           // <-- Message
  END-PI;

  g_MODE = p_MODE;
  g_JOB = p_JOB;
  g_POS = p_POS;
  g_ARA = p_ARA;

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

      when (g_MODE = MNTAREA_CREATE);

        p_SUCCESS = 'Y';
        p_MSG = *blanks;
        return *on;

      when (g_MODE = MNTAREA_CHANGE or
            g_MODE = MNTAREA_DELETE);

        dou (not %error);
          chain(e) (g_JOB : g_POS : g_ARA) AREARF;
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

      when (g_MODE = MNTAREA_DISPLAY or
            g_MODE = MNTAREA_COPY);

        chain(n) (g_JOB : g_POS : g_ARA) AREARF;
        if (not %found);
          p_SUCCESS = 'N';
          p_MSG = '003';
          return *off;
        else;
          p_SUCCESS = 'Y';
          p_MSG = *blanks;
          return *on;
        endif;

      when (g_MODE = MNTAREA_CHKACT);

        if (g_ARA <> *blanks);
          chain(n) (g_JOB : g_POS : g_ARA) AREARF;
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

END-PROC MNTAREA_initialize;

//*=========================================================================================*
//* Get Values                                                                              *
//*=========================================================================================*

DCL-PROC MNTAREA_getValues EXPORT;

  DCL-PI MNTAREA_getValues;
    p_ARA   CHAR(10);   // <-- Area
    p_LIB   CHAR(10);   // <-- Library
    p_LIBL  CHAR(10);   // <-- Library list
    p_CCSID CHAR(10);   // <-- Coded character set ID
    p_CEXT  CHAR(1024); // <-- Command extension
  END-PI;

  if (g_MODE = MNTAREA_CREATE);

    p_ARA   = *blanks;
    p_LIB   = *blanks;
    p_LIBL  = *blanks;
    p_CCSID = *blanks;
    p_CEXT  = *blanks;

  else;

    p_ARA   = XDARA;
    p_LIB   = XDLIB;
    p_LIBL  = XDLIBL;
    p_CCSID = XDCCSID;
    p_CEXT  = XDCEXT;

  endif;

END-PROC MNTAREA_getValues;

//*=========================================================================================*
//* Set Values                                                                              *
//*=========================================================================================*

DCL-PROC MNTAREA_setValues EXPORT;

  DCL-PI MNTAREA_setValues;
    p_ARA   CHAR(10) CONST;   // --> Area
    p_LIB   CHAR(10) CONST;   // --> Library
    p_LIBL  CHAR(10) CONST;   // --> Library list
    p_CCSID CHAR(10) CONST;   // --> Coded character set ID
    p_CEXT  CHAR(1024) CONST; // --> Command extension
  END-PI;

  g_ARA   = p_ARA;
  g_LIB   = p_LIB;
  g_LIBL  = p_LIBL;
  g_CCSID = p_CCSID;
  g_CEXT  = p_CEXT;

END-PROC MNTAREA_setValues;

//*=========================================================================================*
//* Check                                                                                   *
//*=========================================================================================*

DCL-PROC MNTAREA_check EXPORT;

  DCL-PI MNTAREA_check IND; // <-- Success
    p_SUCCESS CHAR(1);      // <-- Success
    p_FLD     CHAR(10);     // <-- Field
    p_MSG     CHAR(256);    // <-- Message
  END-PI;

  DCL-S l_MSGID  CHAR(7);   // Message-Id.
  DCL-S l_MSGDTA CHAR(256); // Message data
  DCL-S l_CCSID ZONED(10);  // CCSID

  // Initialize returning fields
  p_FLD = *blanks;
  p_MSG = *blanks;

  // Initialize message fields
  l_MSGID = *blanks;
  l_MSGDTA = *blanks;

  dou (1 = 1);

    //*** Check only for mode CREATE and COPY ***
    if (g_MODE = MNTAREA_CREATE or
        g_MODE = MNTAREA_COPY);

      // The value in field 'Area' is not valid
      if g_ARA <> '*NONE' and
         ChkVal('*NAME' : 10 : %Addr(g_ARA)) = '*INVALID';
        p_FLD = 'ARA';
        l_MSGID = 'ERR0002';
        leave;
      endif;

      // The area does already exist
      SetLL ( g_JOB : g_POS : g_ARA) AREARF;
      if %Equal;
        p_FLD = 'ARA';
        l_MSGID = 'ERR0001';
        leave;
      endif;

    endif;

    //*** Check only for mode CREATE, COPY and CHANGE ***
    if (g_MODE = MNTAREA_CREATE or
        g_MODE = MNTAREA_COPY or
        g_MODE = MNTAREA_CHANGE);

      // The value in field 'Library' is not valid
      if ChkVal('*NAME' : 10 : %Addr(g_LIB)) = '*INVALID';
        p_FLD = 'LIB';
        l_MSGID = 'ERR0019';
        leave;
      endif;

      // The library has not defined
      SetLL ( g_JOB : g_LIB) LIBRF;
      if not %Equal;
        p_FLD = 'LIB';
        l_MSGID = 'ERR0006';
        leave;
      endif;

      if X1CRTE = '*NO';

        // The library list has to be *NONE, if create environment is *NO
        if g_LIBL <> '*NONE';
          p_FLD = 'LIBL';
          l_MSGID = 'ERR0007';
          leave;
        endif;

      else;

        // The value in field 'Library list' is not valid
        if ChkVal('*NAME' : 10 : %Addr(g_LIBL)) = '*INVALID';
          p_FLD = 'LIBL';
          l_MSGID = 'ERR0003';
          leave;
        endif;

        // The library list has not defined
        SetLL ( g_JOB : g_LIBL ) LIBLSTRF;
        if Not %Equal;
          p_FLD = 'LIBL';
          l_MSGID = 'ERR0005';
          leave;
        endif;

      endif;

      // The value in field 'CCSID' is not valid
      if g_CCSID <> '*JOB';
        l_CCSID = *Zero;
        If ChkVal('*NUM' : 10 : %Addr(g_CCSID)) = '*VALID';
          l_CCSID = %Dec(g_CCSID : 10 : 0);
        EndIf;
        If Not (l_CCSID >= 1 And l_CCSID <= 65535);
          p_FLD = 'CCSID';
          l_MSGID = 'ERR0008';
          leave;
        EndIf;
      endif;

      //*** Check only for mode DELETE ***
      if (g_MODE = MNTAREA_DELETE);
      endif;

    endif;

  enddo;

  // Return
  if (l_MSGID = *blanks);
    p_SUCCESS = 'Y';
    return *on;

  else;

    p_MSG = rtvMsgText(
      l_MSGID :  // --> Message-Id.
      'MNTAREA' : // --> Message file
      '*LIBL' :  // --> Library
      l_MSGDTA   // --> Message data
    );

    p_SUCCESS = 'N';
    return *off;

  endif;

END-PROC MNTAREA_check;

//*=========================================================================================*
//* Book                                                                                    *
//*=========================================================================================*

DCL-PROC MNTAREA_book EXPORT;

  DCL-PI MNTAREA_book;
  END-PI;

  select;

  when (g_MODE = MNTAREA_CREATE);
    exsr modeCreate;

  when (g_MODE = MNTAREA_COPY);
    exsr modeCopy;

  when (g_MODE = MNTAREA_CHANGE);
    exsr modeChange;

  when (g_MODE = MNTAREA_DELETE);
    exsr modeDelete;

  when (g_MODE = MNTAREA_DISPLAY);
    exsr modeDisplay;

  endsl;

  //*------------------------------------------------------------------------------------------*
  //* Mode Create                                                                              *
  //*------------------------------------------------------------------------------------------*

  begsr modeCreate;

    clear AREARF;
    XDJOB = g_JOB;
    XDPOS = g_POS;

    exsr fillRecord;
    XDFILE = *Blanks;
    XDRCDW = *Blanks;
    XDRCDTW = *Blanks;
    XDCHGW = *Blanks;
    XDLOGW = *Blanks;
    XDSDTSRC = *Blanks;
    XDSDTTGT = *Blanks;
    XDJLNG = *Blank;
    XDJRNL = *Blanks;
    XDJRN = *Blanks;
    XDRTO = *Zero;
    XDRCO = *Zero;
    XDTCS = *Zero;
    XDCTA = *Zero;
    XDCAP = *Zero;
    write AREARF;

    if RAPIDFIRE_getAutoCommit() = 'Y';
      commit;
    endif;

  endsr;

  //*------------------------------------------------------------------------------------------*
  //* Mode Copy                                                                                *
  //*------------------------------------------------------------------------------------------*

  begsr modeCopy;

    exsr fillRecord;

    XDFILE = *Blanks;
    XDRCDW = *Blanks;
    XDRCDTW = *Blanks;
    XDCHGW = *Blanks;
    XDLOGW = *Blanks;
    XDSDTSRC = *Blanks;
    XDSDTTGT = *Blanks;
    XDJLNG = *Blank;
    XDJRNL = *Blanks;
    XDJRN = *Blanks;
    XDRTO = *Zero;
    XDRCO = *Zero;
    XDTCS = *Zero;
    XDCTA = *Zero;
    XDCAP = *Zero;
    write AREARF;

    if RAPIDFIRE_getAutoCommit() = 'Y';
      commit;
    endif;

  endsr;

  //*------------------------------------------------------------------------------------------*
  //* Mode Change                                                                              *
  //*------------------------------------------------------------------------------------------*

  begsr modeChange;

    exsr fillRecord;

    update AREARF;

    if RAPIDFIRE_getAutoCommit() = 'Y';
      commit;
    endif;

  endsr;

  //*------------------------------------------------------------------------------------------*
  //* Mode Delete                                                                              *
  //*------------------------------------------------------------------------------------------*

  begsr modeDelete;

    delete AREARF;

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

    XDARA = g_ARA;
    XDLIB = g_LIB;
    XDLIBL = g_LIBL;
    XDCCSID = g_CCSID;
    XDCEXT = g_CEXT;

  endsr;

END-PROC MNTAREA_book;

//*=========================================================================================*
//* Check Action                                                                            *
//*=========================================================================================*

DCL-PROC MNTAREA_checkAction EXPORT;

  DCL-PI MNTAREA_checkAction IND;  // <-- Success
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

    // Invalid parameter values. Error = Area has to be blanks.
    if (p_ACT = MNTAREA_CREATE and
        p_ARA <> *blanks);
      l_MSGID = 'ERR0004';
      leave;
    endif;

    // Invalid parameter values. Error = &1.
    l_OPN = g_OPN;
    if (not l_OPN);
      MNTAREA_openFiles();
    endif;
    MNTAREA_initialize(
      MNTAREA_CHKACT : // --> Mode
      p_JOB :          // --> Job
      p_POS :          // --> Position
      p_ARA :          // --> Area
      l_SUCCESS :      // <-- Success
      l_MSG            // <-- Message
    );
    if (not l_OPN);
      MNTAREA_closeFiles();
    endif;
    if (l_SUCCESS = 'N');
      l_MSGID = 'ERR0009';
      l_MSGDTA = l_MSG;
      leave;
    endif;

    // The action is not valid for such type of files
    if (not MNTFILE_checkAction(
              MNTFILE_MNTARA : // --> Action
              p_JOB :          // --> Job
              p_POS :          // --> Position
              l_SUCCESS :      // <-- Success
              l_MSG            // <-- Message
            )
       );
      l_MSGID = 'ERR0010';
      l_MSGDTA = l_MSG;
      leave;
    endif;

    // The action is only valid for jobs with status *RDY
    if ((p_ACT = MNTAREA_CREATE or
         p_ACT = MNTAREA_COPY or
         p_ACT = MNTAREA_CHANGE or
         p_ACT = MNTAREA_DELETE) and
        X1STS <> '*RDY');
      l_MSGID = 'ERR0011';
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
      'MNTAREA' : // --> Message file
      '*LIBL' :   // --> Library
      l_MSGDTA    // --> Message data
    );

    p_SUCCESS = 'N';
    return *off;

  endif;

END-PROC MNTAREA_checkAction;

//*=========================================================================================*
//* Get Valid Actions                                                                       *
//*=========================================================================================*

DCL-PROC MNTAREA_getValidActions EXPORT;

  DCL-PI MNTAREA_getValidActions;
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

  l_ACT = MNTAREA_COPY;
    exsr checkAction;

  l_ACT = MNTAREA_CHANGE;
    exsr checkAction;

  l_ACT = MNTAREA_DELETE;
    exsr checkAction;

  l_ACT = MNTAREA_DISPLAY;
    exsr checkAction;

  return;

  begsr checkAction;

    if (MNTAREA_checkAction(
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

END-PROC MNTAREA_getValidActions;
