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
//* Maintain Conversions                                                                    *
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

DCL-F CNVS
      DISK(*EXT)
      USAGE(*INPUT : *OUTPUT : *UPDATE : *DELETE)
      KEYED
      RENAME(CNVS : CNVRF)
      COMMIT
      USROPN;

//*-----------------------------------------------------------------------------------------*

/INCLUDE QCPYSRC,MNTFILE

/INCLUDE QCPYSRC,MNTCNV

/INCLUDE QCPYSRC,RAPIDFIRE

/INCLUDE QCPYSRC,HDLRCDLCK

/INCLUDE QCPYSRC,RTVMSGD

/INCLUDE QCPYSRC,CHKVAL

DCL-DS PSDS LEN(429) QUALifIED PSDS;
END-DS;

DCL-S g_MODE CHAR(10);       // Mode
DCL-S g_JOB  CHAR(10);       // Job
DCL-S g_POS  PACKED(6 : 0);  // Position
DCL-S g_FTC  CHAR(10);       // The field to convert
DCL-s g_RFT  CHAR(10);       // Rename field in old file to
DCL-s g_STM1 CHAR(60);       // Statement
DCL-s g_STM2 CHAR(60);       // Statement
DCL-s g_STM3 CHAR(60);       // Statement
DCL-s g_STM4 CHAR(60);       // Statement
DCL-s g_STM5 CHAR(60);       // Statement
DCL-s g_STM6 CHAR(60);       // Statement
DCL-S g_OPN   IND INZ(*OFF); // Open indicator

//*=========================================================================================*
//* Open Files                                                                              *
//*=========================================================================================*

DCL-PROC MNTCNV_openFiles EXPORT;

  DCL-PI MNTCNV_openFiles;
  END-PI;

  open JOBS;
  open FILES;
  open CNVS;

  g_OPN = *on;

END-PROC MNTCNV_openFiles;

//*=========================================================================================*
//* Close Files                                                                             *
//*=========================================================================================*

DCL-PROC MNTCNV_closeFiles EXPORT;

  DCL-PI MNTCNV_closeFiles;
  END-PI;

  close JOBS;
  close FILES;
  close CNVS;

  g_OPN = *off;

END-PROC MNTCNV_closeFiles;

//*=========================================================================================*
//* Initialize                                                                              *
//*=========================================================================================*
//*                                                                                         *
//* Error codes and their meanings                                                          *
//* ==============================                                                          *
//*                                                                                         *
//* '001' = Record in file JOBS not found                                                   *
//* '002' = Record in file FILES not found                                                  *
//* '003' = Record in file CNVS not found                                                   *
//* '004' = Wrong mode                                                                      *
//*                                                                                         *
//*=========================================================================================*

DCL-PROC MNTCNV_initialize EXPORT;

  DCL-PI MNTCNV_initialize IND;    // <-- Success
    p_MODE    CHAR(10) CONST;      // --> Mode
    p_JOB     CHAR(10) CONST;      // --> Job
    p_POS     PACKED(6 : 0) CONST; // --> Position
    p_FTC     CHAR(10) CONST;      // --> Field to convert
    p_SUCCESS CHAR(1);             // <-- Success
    p_MSG     CHAR(256);           // <-- Message
  END-PI;

  g_MODE = p_MODE;
  g_JOB = p_JOB;
  g_POS = p_POS;
  g_FTC = p_FTC;

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

      when (g_MODE = MNTCNV_CREATE);

        p_SUCCESS = 'Y';
        p_MSG = *blanks;
        return *on;

      when (g_MODE = MNTCNV_CHANGE or
            g_MODE = MNTCNV_DELETE);

        dou (not %error);
          chain(e) (g_JOB : g_POS : g_FTC) CNVRF;
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

      when (g_MODE = MNTCNV_DISPLAY or
            g_MODE = MNTCNV_COPY);

        chain(n) (g_JOB : g_POS : g_FTC) CNVRF;
        if (not %found);
          p_SUCCESS = 'N';
          p_MSG = '003';
          return *off;
        else;
          p_SUCCESS = 'Y';
          p_MSG = *blanks;
          return *on;
        endif;

      when (g_MODE = MNTCNV_CHKACT);

        if (g_FTC <> *blanks);
          chain(n) (g_JOB : g_POS : g_FTC) CNVRF;
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

END-PROC MNTCNV_initialize;

//*=========================================================================================*
//* Get Values                                                                              *
//*=========================================================================================*

DCL-PROC MNTCNV_getValues EXPORT;

  DCL-PI MNTCNV_getValues;
    p_FTC  CHAR(10); // <-- Field to convert
    p_RFT  CHAR(10); // <-- Rename field in old file to
    p_STM1 CHAR(60); // <-- Statement
    p_STM2 CHAR(60); // <-- Statement
    p_STM3 CHAR(60); // <-- Statement
    p_STM4 CHAR(60); // <-- Statement
    p_STM5 CHAR(60); // <-- Statement
    p_STM6 CHAR(60); // <-- Statement
  END-PI;

  if (g_MODE = MNTCNV_CREATE);

    p_FTC  = *Blanks;
    p_RFT  = *Blanks;
    p_STM1 = *Blanks;
    p_STM2 = *Blanks;
    p_STM3 = *Blanks;
    p_STM4 = *Blanks;
    p_STM5 = *Blanks;
    p_STM6 = *Blanks;

  else;

    p_FTC  = XBFTC;
    p_RFT  = XBRFT;
    p_STM1 = XBSTM1;
    p_STM2 = XBSTM2;
    p_STM3 = XBSTM3;
    p_STM4 = XBSTM4;
    p_STM5 = XBSTM5;
    p_STM6 = XBSTM6;

  endif;

END-PROC MNTCNV_getValues;

//*=========================================================================================*
//* Set Values                                                                              *
//*=========================================================================================*

DCL-PROC MNTCNV_setValues EXPORT;

  DCL-PI MNTCNV_setValues;
    p_FTC  CHAR(10) CONST; // --> Field to convert
    p_RFT  CHAR(10) CONST; // --> Rename field in old file to
    p_STM1 CHAR(60) CONST; // --> Statement
    p_STM2 CHAR(60) CONST; // --> Statement
    p_STM3 CHAR(60) CONST; // --> Statement
    p_STM4 CHAR(60) CONST; // --> Statement
    p_STM5 CHAR(60) CONST; // --> Statement
    p_STM6 CHAR(60) CONST; // --> Statement
  END-PI;

  g_FTC = p_FTC;
  g_RFT = p_RFT;
  g_STM1 = p_STM1;
  g_STM2 = p_STM2;
  g_STM3 = p_STM3;
  g_STM4 = p_STM4;
  g_STM5 = p_STM5;
  g_STM6 = p_STM6;

END-PROC MNTCNV_setValues;

//*=========================================================================================*
//* Check                                                                                   *
//*=========================================================================================*

DCL-PROC MNTCNV_check EXPORT;

  DCL-PI MNTCNV_check IND; // <-- Success
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
    if (g_MODE = MNTCNV_CREATE or
        g_MODE = MNTCNV_COPY);

      // The value in field 'Field to convert' is not valid
      if ChkVal('*NAME' : 10 : %Addr(g_FTC)) = '*INVALID';
        p_FLD = 'FTC';
        l_MSGID = 'ERR0002';
        leave;
      endif;

      // The field to convert does already exist
      SetLL ( g_JOB : g_POS : g_FTC) CNVRF;
      if %Equal;
        p_FLD = 'FTC';
        l_MSGID = 'ERR0001';
        leave;
      endif;

    endif;

    //*** Check only for mode CREATE, COPY and CHANGE ***
    if (g_MODE = MNTCNV_CREATE or
        g_MODE = MNTCNV_COPY or
        g_MODE = MNTCNV_CHANGE);

      // The value in field 'Rename field in old file to' is not valid
      if g_RFT <> '*NONE' And
         ChkVal('*NAME' : 10 : %Addr(g_RFT)) = '*INVALID';
        p_FLD = 'RFT';
        l_MSGID = 'ERR0003';
        leave;
      endif;

      // Both fields have to be different
      if g_RFT <> '*NONE' and
         g_FTC = g_RFT;
        p_FLD = 'FTCRFT';
        l_MSGID = 'ERR0005';
        leave;
      endif;

      // The value in field 'Statement' is not valid
      if g_STM1 = *Blanks And
         g_STM2 = *Blanks And
         g_STM3 = *Blanks And
         g_STM4 = *Blanks And
         g_STM5 = *Blanks And
         g_STM6 = *Blanks;
        p_FLD = 'STM';
        l_MSGID = 'ERR0006';
      endif;

    endif;

    //*** Check only for mode DELETE ***
    if (g_MODE = MNTCNV_DELETE);

    endif;

  enddo;

  // Return
  if (l_MSGID = *blanks);
    p_SUCCESS = 'Y';
    return *on;

  else;

    p_MSG = rtvMsgText(
      l_MSGID :  // --> Message-Id.
      'MNTCNV' : // --> Message file
      '*LIBL' :  // --> Library
      l_MSGDTA   // --> Message data
    );

    p_SUCCESS = 'N';
    return *off;

  endif;

END-PROC MNTCNV_check;

//*=========================================================================================*
//* Book                                                                                    *
//*=========================================================================================*

DCL-PROC MNTCNV_book EXPORT;

  DCL-PI MNTCNV_book;
  END-PI;

  select;

  when (g_MODE = MNTCNV_CREATE);
    exsr modeCreate;

  when (g_MODE = MNTCNV_COPY);
    exsr modeCopy;

  when (g_MODE = MNTCNV_CHANGE);
    exsr modeChange;

  when (g_MODE = MNTCNV_DELETE);
    exsr modeDelete;

  when (g_MODE = MNTCNV_DISPLAY);
    exsr modeDisplay;

  endsl;

  //*------------------------------------------------------------------------------------------*
  //* Mode Create                                                                              *
  //*------------------------------------------------------------------------------------------*

  begsr modeCreate;

    clear CNVRF;
    XBJOB = g_JOB;
    XBPOS = g_POS;

    exsr fillRecord;

    write CNVRF;

    if RAPIDFIRE_getAutoCommit() = 'Y';
      commit;
    endif;

  endsr;

  //*------------------------------------------------------------------------------------------*
  //* Mode Copy                                                                                *
  //*------------------------------------------------------------------------------------------*

  begsr modeCopy;

    exsr fillRecord;

    write CNVRF;

    if RAPIDFIRE_getAutoCommit() = 'Y';
      commit;
    endif;

  endsr;

  //*------------------------------------------------------------------------------------------*
  //* Mode Change                                                                              *
  //*------------------------------------------------------------------------------------------*

  begsr modeChange;

    exsr fillRecord;

    update CNVRF;

    if RAPIDFIRE_getAutoCommit() = 'Y';
      commit;
    endif;

  endsr;

  //*------------------------------------------------------------------------------------------*
  //* Mode Delete                                                                              *
  //*------------------------------------------------------------------------------------------*

  begsr modeDelete;

    delete CNVRF;

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

    XBFTC = g_FTC;
    XBRFT = g_RFT;
    XBSTM1 = g_STM1;
    XBSTM2 = g_STM2;
    XBSTM3 = g_STM3;
    XBSTM4 = g_STM4;
    XBSTM5 = g_STM5;
    XBSTM6 = g_STM6;

  endsr;

END-PROC MNTCNV_book;

//*=========================================================================================*
//* Check Action                                                                            *
//*=========================================================================================*

DCL-PROC MNTCNV_checkAction EXPORT;

  DCL-PI MNTCNV_checkAction IND;   // <-- Success
    p_ACT     CHAR(10) CONST;      // --> Action
    p_JOB     CHAR(10) CONST;      // --> Job
    p_POS     PACKED(6 : 0) CONST; // --> Position
    p_FTC     CHAR(10) CONST;      // --> Field to convert
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

    // Invalid parameter values. Error = "Field to convert" has to be blanks.
    if (p_ACT = MNTCNV_CREATE and
        p_FTC <> *blanks);
      l_MSGID = 'ERR0004';
      leave;
    endif;

    // Invalid parameter values. Error = &1.
    l_OPN = g_OPN;
    if (not l_OPN);
      MNTCNV_openFiles();
    endif;
    MNTCNV_initialize(
      MNTCNV_CHKACT : // --> Mode
      p_JOB :         // --> Job
      p_POS :         // --> Position
      p_FTC :         // --> Field to convert
      l_SUCCESS :     // <-- Success
      l_MSG           // <-- Message
    );
    if (not l_OPN);
      MNTCNV_closeFiles();
    endif;
    if (l_SUCCESS = 'N');
      l_MSGID = 'ERR0007';
      l_MSGDTA = l_MSG;
      leave;
    endif;

    // The action is not valid for such type of files
    if (not MNTFILE_checkAction(
              MNTFILE_MNTCNV : // --> Action
              p_JOB :          // --> Job
              p_POS :          // --> Position
              l_SUCCESS :      // <-- Success
              l_MSG            // <-- Message
            )
       );
      l_MSGID = 'ERR0008';
      l_MSGDTA = l_MSG;
      leave;
    endif;

    // The action is only valid for jobs with status *RDY
    if ((p_ACT = MNTCNV_CREATE or
         p_ACT = MNTCNV_COPY or
         p_ACT = MNTCNV_CHANGE or
         p_ACT = MNTCNV_DELETE) and
        X1STS <> '*RDY');
      l_MSGID = 'ERR0009';
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
      'MNTCNV' : // --> Message file
      '*LIBL' :  // --> Library
      l_MSGDTA   // --> Message data
    );

    p_SUCCESS = 'N';
    return *off;

  endif;

END-PROC MNTCNV_checkAction;

//*=========================================================================================*
//* Get Valid Actions                                                                       *
//*=========================================================================================*

DCL-PROC MNTCNV_getValidActions EXPORT;

  DCL-PI MNTCNV_getValidActions;
    p_JOB    CHAR(10) CONST;      // --> Job
    p_POS    PACKED(6 : 0) CONST; // --> Position
    p_FTC    CHAR(10) CONST;      // --> Field to convert
    p_NBRACT PACKED(2 : 0);       // <-- Number of actions
    p_ACT    CHAR(10) DIM(25);    // <-- Actions
  END-PI;

  DCL-S l_ACT     CHAR(10);      // --> Action
  DCL-S l_SUCCESS CHAR(1);       // <-- Success
  DCL-S l_MSG     CHAR(256);     // <-- Message

  p_NBRACT = *zero;
  p_ACT(*) = *blanks;

  l_ACT = MNTCNV_COPY;
    exsr checkAction;

  l_ACT = MNTCNV_CHANGE;
    exsr checkAction;

  l_ACT = MNTCNV_DELETE;
    exsr checkAction;

  l_ACT = MNTCNV_DISPLAY;
    exsr checkAction;

  return;

  begsr checkAction;

    if (MNTCNV_checkAction(
          l_ACT :     // --> Action
          p_JOB :     // --> Job
          p_POS :     // --> Position
          p_FTC :     // --> Field to convert
          l_SUCCESS : // <-- Success
          l_MSG       // <-- Message
        ));
      p_NBRACT += 1;
      p_ACT(p_NBRACT) = l_ACT;
    endif;

  endsr;

END-PROC MNTCNV_getValidActions;
