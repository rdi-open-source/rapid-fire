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
//* Maintain Subjects To Be Notified                                                        *
//*                                                                                         *
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

DCL-F SUBTOBENOT
      DISK(*EXT)
      USAGE(*INPUT : *OUTPUT : *UPDATE : *DELETE)
      KEYED
      RENAME(SUBTOBENOT : SUBTOBERF)
      COMMIT
      USROPN;

//*-----------------------------------------------------------------------------------------*

/INCLUDE QCPYSRC,MNTSTBN

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

DCL-S g_MODE  CHAR(10);      // Mode
DCL-S g_JOB   CHAR(10);      // Job
DCL-S g_POS   PACKED(6 : 0); // Position
DCL-S g_TYPE  CHAR(10);      // Type
DCL-S g_USR   CHAR(10);      // User
DCL-S g_MSGQL CHAR(10);      // Message queue (Library)
DCL-S g_MSGQ  CHAR(10);      // Message queue

//*=========================================================================================*
//* Open Files                                                                              *
//*=========================================================================================*

DCL-PROC MNTSTBN_openFiles EXPORT;

  DCL-PI MNTSTBN_openFiles;
  END-PI;

  open JOBS;
  open SUBTOBENOT;

END-PROC MNTSTBN_openFiles;

//*=========================================================================================*
//* Close Files                                                                             *
//*=========================================================================================*

DCL-PROC MNTSTBN_closeFiles EXPORT;

  DCL-PI MNTSTBN_closeFiles;
  END-PI;

  close JOBS;
  close SUBTOBENOT;

END-PROC MNTSTBN_closeFiles;

//*=========================================================================================*
//* Initialize                                                                              *
//*=========================================================================================*
//*                                                                                         *
//* Error codes and their meanings                                                          *
//* ==============================                                                          *
//*                                                                                         *
//* '001' = Record in file JOBS not found                                                   *
//* '002' = Record in file SUBTOBENOT not found                                             *
//* '003' = Wrong mode                                                                      *
//*                                                                                         *
//*=========================================================================================*

DCL-PROC MNTSTBN_initialize EXPORT;

  DCL-PI MNTSTBN_initialize IND;   // <-- Success
    p_MODE    CHAR(10) CONST;      // --> Mode
    p_JOB     CHAR(10) CONST;      // --> Job
    p_POS     PACKED(3 : 0) CONST; // --> Position
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
        chain(e) (g_JOB : g_POS) SUBTOBERF;
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

      chain(n) (g_JOB : g_POS) SUBTOBERF;
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

END-PROC MNTSTBN_initialize;

//*=========================================================================================*
//* Get Values                                                                              *
//*=========================================================================================*

DCL-PROC MNTSTBN_getValues EXPORT;

  DCL-PI MNTSTBN_getValues;
    p_POS   PACKED(3 : 0); // <-- Position
    p_TYPE  CHAR(10);      // <-- Type
    p_USR   CHAR(10);      // <-- User
    p_MSGQL CHAR(10);      // <-- Message queue (Library)
    p_MSGQ  CHAR(10);      // <-- Message queue
  END-PI;

  if (g_MODE = MODE_CREATE);

    p_POS = *zero;
    p_TYPE = *blanks;
    p_USR = *blanks;
    p_MSGQL = *blanks;
    p_MSGQ = *blanks;

  else;

    p_POS = XCPOS;
    p_TYPE = XCTYPE;
    p_USR = XCUSR;
    p_MSGQL = XCMSGQL;
    p_MSGQ = XCMSGQ;

  endif;

END-PROC MNTSTBN_getValues;

//*=========================================================================================*
//* Set Values                                                                              *
//*=========================================================================================*

DCL-PROC MNTSTBN_setValues EXPORT;

  DCL-PI MNTSTBN_setValues;
    p_POS   PACKED(3 : 0) CONST; // --> Position
    p_TYPE  CHAR(10) CONST;      // --> Type
    p_USR   CHAR(10) CONST;      // --> User
    p_MSGQL CHAR(10) CONST;      // --> Message queue (Library)
    p_MSGQ  CHAR(10) CONST;      // --> Message queue
  END-PI;

  g_POS = p_POS;
  g_TYPE = p_TYPE;
  g_USR = p_USR;
  g_MSGQL = p_MSGQL;
  g_MSGQ = p_MSGQ;

END-PROC MNTSTBN_setValues;

//*=========================================================================================*
//* Check                                                                                   *
//*=========================================================================================*

DCL-PROC MNTSTBN_check EXPORT;

  DCL-PI MNTSTBN_check IND; // <-- Success
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

  Dou (1 = 1);

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
      SetLL ( g_JOB : g_POS ) SUBTOBERF;
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

      // The value in field 'Type' is not valid
      if g_TYPE <> '*USR' And
         g_TYPE <> '*MSGQ';
        p_FLD = 'TYPE';
        l_MSGID = 'ERR0006';
        leave;
      endif;

      // The value in field 'User' is not valid
      if (g_TYPE = '*USR' And
         g_USR <> '*SYSOPR' And
         ChkVal('*NAME' : 10 : %Addr(g_USR)) = '*INVALID') Or
         (g_TYPE <> '*USR' And
         g_USR <> *blanks);
        p_FLD = 'USER';
        l_MSGID = 'ERR0005';
        leave;
      endif;

      // The value in field 'Message queue' is not valid
      if (g_TYPE = '*MSGQ' And
         ChkVal('*NAME' : 10 : %Addr(g_MSGQ)) = '*INVALID') Or
         (g_TYPE <> '*MSGQ' And
         g_MSGQ <> *blanks);
        p_FLD = 'MSGQ';
        l_MSGID = 'ERR0009';
        leave;
      endif;

      // The value in field 'Message queue - Library' is not valid
      if (g_TYPE = '*MSGQ' And
        g_MSGQL <> '*LIBL' And
        g_MSGQL <> '*CURLIB' And
        ChkVal('*NAME' : 10 : %Addr(g_MSGQL)) = '*INVALID') Or
        (g_TYPE <> '*MSGQ' And
        g_MSGQL <> *blanks);
        p_FLD = 'MSGQL';
        l_MSGID = 'ERR0012';
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
      'MNTSTBN' : // --> Message file
      '*LIBL' :  // --> Library
      l_MSGDTA   // --> Message data
    );

    p_SUCCESS = 'N';
    return *off;

  endif;

END-PROC MNTSTBN_check;

//*=========================================================================================*
//* Book                                                                                    *
//*=========================================================================================*

DCL-PROC MNTSTBN_book EXPORT;

  DCL-PI MNTSTBN_book;
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

    clear SUBTOBERF;
    XCJOB = g_JOB;

    exsr fillRecord;

    write SUBTOBERF;

    commit;

  endsr;

  //*------------------------------------------------------------------------------------------*
  //* Mode Copy                                                                                *
  //*------------------------------------------------------------------------------------------*

  begsr modeCopy;

    exsr fillRecord;

    write SUBTOBERF;

    commit;

  endsr;

  //*------------------------------------------------------------------------------------------*
  //* Mode Change                                                                              *
  //*------------------------------------------------------------------------------------------*

  begsr modeChange;

    exsr fillRecord;

    update SUBTOBERF;

    commit;

  endsr;

  //*------------------------------------------------------------------------------------------*
  //* Mode Delete                                                                              *
  //*------------------------------------------------------------------------------------------*

  begsr modeDelete;

    delete SUBTOBERF;

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

    XCPOS = g_POS;
    XCTYPE = g_TYPE;
    XCUSR = g_USR;
    XCMSGQL = g_MSGQL;
    XCMSGQ = g_MSGQ;

  endsr;

END-PROC MNTSTBN_book;
