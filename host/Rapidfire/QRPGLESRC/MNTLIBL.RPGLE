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
//* Maintain Library List                                                                   *
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
//* 16.11.2017 I   FH  I Creation                                                          *
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

DCL-F LIBLS
      DISK(*EXT)
      USAGE(*INPUT : *OUTPUT : *UPDATE : *DELETE)
      KEYED
      RENAME(LIBLS : LIBLSTRF)
      COMMIT
      USROPN;

DCL-F LIBLETYS
      DISK(*EXT)
      USAGE(*INPUT : *OUTPUT : *UPDATE : *DELETE)
      KEYED
      RENAME(LIBLETYS : LIBLERF)
      COMMIT
      USROPN;

//*-----------------------------------------------------------------------------------------*

/INCLUDE QCPYSRC,MNTJOB

/INCLUDE QCPYSRC,MNTLIBL

/INCLUDE QCPYSRC,RAPIDFIRE

/INCLUDE QCPYSRC,HDLRCDLCK

/INCLUDE QCPYSRC,RTVMSGD

/INCLUDE QCPYSRC,CHKVAL

DCL-DS PSDS LEN(429) QUALIFIED PSDS;
END-DS;

DCL-S g_MODE CHAR(10);              // Mode
DCL-S g_JOB  CHAR(10);              // Job
DCL-S g_LIBL CHAR(10);              // Library list
DCL-S g_DSCR CHAR(35);              // Description
DCL-S g_SEQ  ZONED(4 : 0) DIM(250); // Sequence
DCL-S g_LIB  CHAR(10) DIM(250);     // Library
DCL-S g_OPN  IND INZ(*OFF);         // Open indicator

//*=========================================================================================*
//* Open Files                                                                              *
//*=========================================================================================*

DCL-PROC MNTLIBL_openFiles EXPORT;

  DCL-PI MNTLIBL_openFiles;
  END-PI;

  open JOBS;
  open LIBLS;
  open LIBLETYS;

  g_OPN = *on;

END-PROC MNTLIBL_openFiles;

//*=========================================================================================*
//* Close Files                                                                             *
//*=========================================================================================*

DCL-PROC MNTLIBL_closeFiles EXPORT;

  DCL-PI MNTLIBL_closeFiles;
  END-PI;

  close JOBS;
  close LIBLS;
  close LIBLETYS;

  g_OPN = *off;

END-PROC MNTLIBL_closeFiles;

//*=========================================================================================*
//* Initialize                                                                              *
//*=========================================================================================*
//*                                                                                         *
//* Error codes and their meanings                                                          *
//* ==============================                                                          *
//*                                                                                         *
//* '001' = Record in file JOBS not found                                                   *
//* '002' = Record in file LIBLS not found                                                  *
//* '003' = Wrong mode                                                                      *
//*                                                                                         *
//*=========================================================================================*

DCL-PROC MNTLIBL_initialize EXPORT;

  DCL-PI MNTLIBL_initialize IND; // <-- Success
    p_MODE    CHAR(10) CONST;    // --> Mode
    p_JOB     CHAR(10) CONST;    // --> Job
    p_LIBL    CHAR(10) CONST;    // --> Library list
    p_SUCCESS CHAR(1);           // <-- Success
    p_MSG     CHAR(256);         // <-- Message
  END-PI;

  g_MODE = p_MODE;
  g_JOB = p_JOB;
  g_LIBL = p_LIBL;

  chain (g_JOB) JOBRF;
  if (not %found);

    p_SUCCESS = 'N';
    p_MSG = '001';
    return *off;

  else;

    select;

    when (g_MODE = MNTLIBL_CREATE);

      p_SUCCESS = 'Y';
      p_MSG = *blanks;
      return *on;

    when (g_MODE = MNTLIBL_CHANGE or
          g_MODE = MNTLIBL_DELETE);

      dou (not %error);
        chain(e) (g_JOB : g_LIBL) LIBLSTRF;
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

    when (g_MODE = MNTLIBL_DISPLAY or
          g_MODE = MNTLIBL_COPY);

      chain(n) (g_JOB : g_LIBL) LIBLSTRF;
      if (not %found);
        p_SUCCESS = 'N';
        p_MSG = '002';
        return *off;
      else;
        p_SUCCESS = 'Y';
        p_MSG = *blanks;
        return *on;
      endif;

    when (g_MODE = MNTLIBL_CHKACT);

      if (g_LIBL <> *blanks);
        chain(n) (g_JOB : g_LIBL) LIBLSTRF;
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

END-PROC MNTLIBL_initialize;

//*=========================================================================================*
//* Get Values                                                                              *
//*=========================================================================================*

DCL-PROC MNTLIBL_getValues EXPORT;

  DCL-PI MNTLIBL_getValues;
    p_LIBL CHAR(10);               // <-- Library list
    p_DSCR CHAR(35);               // <-- Description
    p_SEQ  ZONED(4 : 0) DIM(250); // <-- Sequence
    p_LIB  CHAR(10) DIM(250);      // <-- Library
  END-PI;

  DCL-S l_CNT PACKED(4 : 0); // Counter

  if (g_MODE = MNTLIBL_CREATE);

    p_LIBL = *blanks;
    p_DSCR = *blanks;
    for l_CNT = 1 to %elem(p_LIB) by 1;
      p_SEQ(l_CNT) = l_CNT * 10;
    endfor;
    p_LIB(*) = *blanks;

  else;

    p_LIBL = X4LIBL;
    p_DSCR = X4DSCR;
    for l_CNT = 1 to %elem(p_LIB) by 1;
      p_SEQ(l_CNT) = l_CNT * 10;
    endfor;
    l_CNT = *zero;
    p_LIB(*) = *blanks;
    SetLL ( g_JOB : g_LIBL ) LIBLERF;
    DoU %EOF;
      ReadE(N) ( g_JOB : g_LIBL ) LIBLERF;
      If Not %EOF;
        l_CNT += 1;
        p_LIB(l_CNT) = X5LIB;
      EndIf;
    EndDo;

  endif;

END-PROC MNTLIBL_getValues;

//*=========================================================================================*
//* Get Library List                                                                        *
//*=========================================================================================*

DCL-PROC MNTLIBL_getLibraryList EXPORT;

  DCL-PI MNTLIBL_getLibraryList;
    p_SEQ  ZONED(4 : 0) DIM(250); // <-- Sequence
    p_LIB  CHAR(10) DIM(250);      // <-- Library
  END-PI;

  p_SEQ = g_SEQ;
  p_LIB = g_LIB;

END-PROC MNTLIBL_getLibraryList;

//*=========================================================================================*
//* Set Values                                                                              *
//*=========================================================================================*

DCL-PROC MNTLIBL_setValues EXPORT;

  DCL-PI MNTLIBL_setValues;
    p_LIBL CHAR(10) CONST;               // --> Library list
    p_DSCR CHAR(35) CONST;               // --> Description
    p_SEQ  ZONED(4 : 0) DIM(250) CONST; // --> Sequence
    p_LIB  CHAR(10) DIM(250) CONST;      // --> Library
  END-PI;

  g_LIBL = p_LIBL;
  g_DSCR = p_DSCR;
  g_SEQ  = p_SEQ;
  g_LIB  = p_LIB;

END-PROC MNTLIBL_setValues;

//*=========================================================================================*
//* Check                                                                                   *
//*=========================================================================================*

DCL-PROC MNTLIBL_check EXPORT;

  DCL-PI MNTLIBL_check IND;  // <-- Success
    p_SUCCESS CHAR(1);       // <-- Success
    p_FLD     CHAR(10);      // <-- Field
    p_RCD     PACKED(4 : 0); // <-- Record
    p_MSG     CHAR(256);     // <-- Message
  END-PI;

  DCL-S l_MSGID  CHAR(7);        // Message-Id.
  DCL-S l_MSGDTA CHAR(256);      // Message data
  DCL-S l_CNT    PACKED(4 : 0);  // Counter
  DCL-S l_POS    PACKED(4 : 0);  // Counter

  DCL-DS *N;
    l_LL CHAR(14) Dim(250);
    l_LLSEQ ZONED(4 : 0) Overlay(l_LL);         // Sequence
    l_LLLIB CHAR(10)     Overlay(l_LL : *Next); // Library
  END-DS;

  // Initialize returning fields
  p_FLD = *blanks;
  p_RCD = *zero;
  p_MSG = *blanks;

  // Initialize message fields
  l_MSGID = *blanks;
  l_MSGDTA = *blanks;

  dou (1 = 1);

    //*** Check only for mode CREATE and COPY ***
    if (g_MODE = MNTLIBL_CREATE or
        g_MODE = MNTLIBL_COPY);

      // The value in field 'Library list' is not valid
      If ChkVal('*NAME' : 10 : %Addr(g_LIBL)) = '*INVALID';
        p_FLD = 'LIBL';
        l_MSGID = 'ERR0003';
        Leave;
      EndIf;

      // The library list does already exist
      SetLL ( g_JOB : g_LIBL ) LIBLSTRF;
      If %Equal;
        p_FLD = 'LIBL';
        l_MSGID = 'ERR0009';
        Leave;
      EndIf;

    endif;

    //*** Check only for mode CREATE, COPY and CHANGE ***
    if (g_MODE = MNTLIBL_CREATE or
        g_MODE = MNTLIBL_COPY or
        g_MODE = MNTLIBL_CHANGE);

      // The value in field 'Description' is not valid
      If g_DSCR = *Blanks;
        p_FLD = 'DSCR';
        l_MSGID = 'ERR0010';
        Leave;
      EndIf;

      // Tabellen initialisieren
      l_POS = *Zero;
      l_LLSEQ(*) = *Hival;
      l_LLLIB(*) = *Blanks;

      for l_CNT = 1 to %elem(g_LIB) by 1;

        // Die Position enthält Werte
        If g_LIB(l_CNT) <> *Blanks;

          // The sequenz may not be 9999
          If g_SEQ(l_CNT) >= 9999;
            p_FLD = 'SEQ';
            p_RCD = l_CNT;
            l_MSGID = 'ERR0001';
            leave;
          EndIf;

          // The value in field 'Library' is not valid
          If ChkVal('*NAME' : 10 : %Addr(g_LIB(l_CNT))) = '*INVALID';
            p_FLD = 'LIB';
            p_RCD = l_CNT;
            l_MSGID = 'ERR0002';
            leave;
          EndIf;

          // The Library has already been defined on an other position
          If %Lookup(g_LIB(l_CNT) : l_LLLIB) <> *Zero;
            p_FLD = 'DUP';
            p_RCD = l_CNT;
            l_MSGID = 'ERR0007';
            leave;
          EndIf;

          // Position in Tabelle stellen
          l_POS += 1;
          l_LLSEQ(l_POS) = g_SEQ(l_CNT);
          l_LLLIB(l_POS) = g_LIB(l_CNT);

        EndIf;

      endfor;

      // Error found
      If l_MSGID <> *Blanks;
        Leave;
      EndIf;

      // Sort table by sequence
      SortA l_LLSEQ;

      // Fill libraries once again
      g_SEQ(*) = *zero;
      g_LIB(*) = *blanks;
      for l_CNT = 1 to %elem(l_LLLIB) by 1;
        g_SEQ(l_CNT) = l_CNT * 10;
        g_LIB(l_CNT) = l_LLLIB(l_CNT);
      endfor;

    endif;

    //*** Check only for mode DELETE ***
    if (g_MODE = MNTLIBL_DELETE);
    endif;

  enddo;

  // Return
  if (l_MSGID = *blanks);
    p_SUCCESS = 'Y';
    return *on;

  else;

    p_MSG = rtvMsgText(
      l_MSGID :   // --> Message-Id.
      'MNTLIBL' : // --> Message file
      '*LIBL' :   // --> Library
      l_MSGDTA    // --> Message data
    );

    p_SUCCESS = 'N';
    return *off;

  endif;

END-PROC MNTLIBL_check;

//*=========================================================================================*
//* Book                                                                                    *
//*=========================================================================================*

DCL-PROC MNTLIBL_book EXPORT;

  DCL-PI MNTLIBL_book;
  END-PI;

  DCL-S l_CNT PACKED(4 : 0); // Counter

  select;

  when (g_MODE = MNTLIBL_CREATE);
    exsr modeCreate;

  when (g_MODE = MNTLIBL_COPY);
    exsr modeCopy;

  when (g_MODE = MNTLIBL_CHANGE);
    exsr modeChange;

  when (g_MODE = MNTLIBL_DELETE);
    exsr modeDelete;

  when (g_MODE = MNTLIBL_DISPLAY);
    exsr modeDisplay;

  endsl;

  //*------------------------------------------------------------------------------------------*
  //* Mode Create                                                                              *
  //*------------------------------------------------------------------------------------------*

  begsr modeCreate;

    clear LIBLSTRF;
    X4JOB = g_JOB;

    exsr fillRecord;

    write LIBLSTRF;

    exsr fillLibraries;

    if RAPIDFIRE_getAutoCommit() = 'Y';
      commit;
    endif;

  endsr;

  //*------------------------------------------------------------------------------------------*
  //* Mode Copy                                                                                *
  //*------------------------------------------------------------------------------------------*

  begsr modeCopy;

    exsr fillRecord;

    write LIBLSTRF;

    exsr fillLibraries;

    if RAPIDFIRE_getAutoCommit() = 'Y';
      commit;
    endif;

  endsr;

  //*------------------------------------------------------------------------------------------*
  //* Mode Change                                                                              *
  //*------------------------------------------------------------------------------------------*

  begsr modeChange;

    exsr fillRecord;

    update LIBLSTRF;

    exsr fillLibraries;

    if RAPIDFIRE_getAutoCommit() = 'Y';
      commit;
    endif;

  endsr;

  //*------------------------------------------------------------------------------------------*
  //* Mode Delete                                                                              *
  //*------------------------------------------------------------------------------------------*

  begsr modeDelete;

    delete LIBLSTRF;

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

    X4LIBL = g_LIBL;
    X4DSCR = g_DSCR;

  endsr;

  //*------------------------------------------------------------------------------------------*
  //* Fill Libraries                                                                           *
  //*------------------------------------------------------------------------------------------*

  begsr fillLibraries;

    if g_MODE <> MNTLIBL_CREATE and
       g_MODE <> MNTLIBL_COPY;
      SetLL ( g_JOB : g_LIBL ) LIBLERF;
      DoU %EOF;
        DoU Not %Error;
          ReadE(E) ( g_JOB : g_LIBL ) LIBLERF;
          If %Error;
            HdlRcdLck(PSDS);
          EndIf;
        EndDo;
        If Not %EOF;
          Delete LIBLERF;
        EndIf;
      EndDo;
    EndIf;

    For l_CNT = 1 To %elem(g_LIB) By 1;
      If g_LIB(l_CNT) <> *Blanks;
        Clear *All LIBLERF;
        X5JOB = g_JOB;
        X5LIBL = g_LIBL;
        X5SEQ = g_SEQ(l_CNT);
        X5LIB = g_LIB(l_CNT);
        Write LIBLERF;
      EndIf;
    EndFor;

  endsr;

END-PROC MNTLIBL_book;

//*=========================================================================================*
//* Check Action                                                                            *
//*=========================================================================================*

DCL-PROC MNTLIBL_checkAction EXPORT;

  DCL-PI MNTLIBL_checkAction IND;  // <-- Success
    p_ACT     CHAR(10) CONST;      // --> Action
    p_JOB     CHAR(10) CONST;      // --> Job
    p_LIBL    CHAR(10) CONST;      // --> Library list
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

    // Invalid parameter values. Error = Library list has to be blanks.
    if (p_ACT = MNTLIBL_CREATE and
        p_LIBL <> *blanks);
      l_MSGID = 'ERR0004';
      leave;
    endif;

    // Invalid parameter values. Error = &1.
    l_OPN = g_OPN;
    if (not l_OPN);
      MNTLIBL_openFiles();
    endif;
    MNTLIBL_initialize(
      MNTLIBL_CHKACT : // --> Mode
      p_JOB :          // --> Job
      p_LIBL :         // --> Library list
      l_SUCCESS :      // <-- Success
      l_MSG            // <-- Message
    );
    if (not l_OPN);
      MNTLIBL_closeFiles();
    endif;
    if (l_SUCCESS = 'N');
      l_MSGID = 'ERR0005';
      l_MSGDTA = l_MSG;
      leave;
    endif;

    // The action is not valid for such type of jobs
    if (not MNTJOB_checkAction(
              MNTJOB_MNTLIBL : // --> Action
              p_JOB :          // --> Job
              l_SUCCESS :      // <-- Success
              l_MSG            // <-- Message
            )
       );
      l_MSGID = 'ERR0006';
      l_MSGDTA = l_MSG;
      leave;
    endif;

    // The action is only valid for jobs with status *RDY
    if ((p_ACT = MNTLIBL_CREATE or
         p_ACT = MNTLIBL_COPY or
         p_ACT = MNTLIBL_CHANGE or
         p_ACT = MNTLIBL_DELETE) and
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
      'MNTLIBL' : // --> Message file
      '*LIBL' :   // --> Library
      l_MSGDTA    // --> Message data
    );

    p_SUCCESS = 'N';
    return *off;

  endif;

END-PROC MNTLIBL_checkAction;

//*=========================================================================================*
//* Get Valid Actions                                                                       *
//*=========================================================================================*

DCL-PROC MNTLIBL_getValidActions EXPORT;

  DCL-PI MNTLIBL_getValidActions;
    p_JOB     CHAR(10) CONST;   // --> Job
    p_LIBL    CHAR(10) CONST;   // --> Library list
    p_NBRACT  PACKED(2 : 0);    // <-- Number of actions
    p_ACT     CHAR(10) DIM(25); // <-- Actions
  END-PI;

  DCL-S l_ACT     CHAR(10);      // --> Action
  DCL-S l_SUCCESS CHAR(1);       // <-- Success
  DCL-S l_MSG     CHAR(256);     // <-- Message

  p_NBRACT = *zero;
  p_ACT(*) = *blanks;

  l_ACT = MNTLIBL_COPY;
    exsr checkAction;

  l_ACT = MNTLIBL_CHANGE;
    exsr checkAction;

  l_ACT = MNTLIBL_DELETE;
    exsr checkAction;

  l_ACT = MNTLIBL_DISPLAY;
    exsr checkAction;

  return;

  begsr checkAction;

    if (MNTLIBL_checkAction(
          l_ACT :     // --> Action
          p_JOB :     // --> Job
          p_LIBL :    // --> Library list
          l_SUCCESS : // <-- Success
          l_MSG       // <-- Message
        ));
      p_NBRACT += 1;
      p_ACT(p_NBRACT) = l_ACT;
    endif;

  endsr;

END-PROC MNTLIBL_getValidActions;
