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
//* Rapid Fire                                                                              *
//*                                                                                         *
//*-----------------------------------------------------------------------------------------*
//*  >>PRE-COMPILER<<                                                                       *
//*    >>CRTCMD<< CRTSQLRPGI   OBJ(&LI/&OB) +                                               *
//*                              SRCFILE(&SL/&SF) SRCMBR(&SM) +                             *
//*                              OBJTYPE(*MODULE);                                          *
//*    >>IMPORTANT<<                                                                        *
//*      >>PARM<<  OUTPUT(*PRINT);                                                          *
//*      >>PARM<<  GENLVL(0);                                                               *
//*      >>PARM<<  DBGVIEW(*SOURCE);                                                        *
//*      >>PARM<<  OPTION(*EVENTF);                                                         *
//*      >>PARM<<  TGTRLS(&TR);                                                             *
//*    >>END-IMPORTANT<<                                                                    *
//*    >>EXECUTE<<                                                                          *
//*  >>END-PRE-COMPILER<<                                                                   *
//*------------+--------+-------------------------------------------------------------------*
//*   Datum    I Progr. I Beschreibung                                                      *
//*------------+--------+-------------------------------------------------------------------*
//* 20.12.2016 I   FH   I STRPREPRC Header hinzugefügt.                                     *
//*------------+--------+-------------------------------------------------------------------*

// Control

CTL-OPT NoMain;
CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,');
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

// Includes

/Include QCPYSRC,RAPIDFIRE
/Include QCPYSRC,RTVMSG
/Include QCPYSRC,SQLSTS
/Include QCPYSRC,RFCCSTR
/Include QCPYSRC,RFCCEND
/Include QCPYSRC,FILE
/Include QCPYSRC,LANGUAGE

//*-----------------------------------------------------------------------------------------*

// Global field declarations

DCL-S g_isError IND Inz(*OFF);
DCL-S g_AUTCMT CHAR(1) Inz('Y');
DCL-S g_RSLTLNG CHAR(1); // Result language

//*-----------------------------------------------------------------------------------------*

// Set SQL options

Exec SQL
  Set Option
  DatFmt=*ISO,
  TimFmt=*ISO,
  Commit=*NONE,
  CloSQLCsr=*ENDMOD;

//*=========================================================================================*
//* Log Error                                                                               *
//*=========================================================================================*

DCL-PROC RAPIDFIRE_log_Error EXPORT;

  DCL-PI RAPIDFIRE_log_Error;
    p_Job
    CHAR(10) Const;
    p_Message_Id_1
    CHAR(7) Const;
    p_Message_Data_1
    CHAR(256) Const;
    p_Message_Id_2
    CHAR(7) Const;
    p_Message_Data_2
    CHAR(256) Const;
  END-PI;

  // Locale field declarations

  DCL-S l_Message
        CHAR(256);
  DCL-S l_Error_Text
        CHAR(256);

  If p_job = RAPIDFIRE_JOB_NONE; // Used by PROMOTER.generate_Copy_Program()
    RAPIDFIRE_set_Error(p_Job: *ON);
    Return;
  EndIf;

  If Not RAPIDFIRE_is_Error(p_Job);

    RTVMSG('RAPIDFIRE' : '*LIBL' :
      p_Message_Id_1 : p_Message_Data_1 : l_Message);

    l_Error_Text = '(' + p_Message_Id_1 + ') - ' + %Trim(l_Message);

    If p_Message_Id_2 <> *Blanks;

      RTVMSG('QCPFMSG' : '*LIBL' :
        p_Message_Id_2 : p_Message_Data_2 : l_Message);

      If l_Message <> *Blanks;

        l_Error_Text = %Trim(l_Error_Text) + ' : ' +
          '(' + p_Message_Id_2 + ') - ' + %Trim(l_Message);

      Else;

        l_Error_Text = %Trim(l_Error_Text) + ' : ' +
          '(' + p_Message_Id_2 + ') - ' + p_Message_Data_2;

      EndIf;

    EndIf;

    Exec SQL
      Update Jobs
      Set Status = '*END-PND',
          Phase = '*ABORT',
          Error = 'Y',
          Error_Text = :l_Error_Text
      Where Job = :p_Job;
    setIgnoreWarnings();
    SqlCheckState(sqlState);

  EndIf;

END-PROC RAPIDFIRE_log_Error;

//*=========================================================================================*
//* Is Error                                                                                *
//*=========================================================================================*

DCL-PROC RAPIDFIRE_is_Error EXPORT;

  DCL-PI RAPIDFIRE_is_Error
         IND;
    p_Job
    CHAR(10) Const;
  END-PI;

  // Locale field declarations

  DCL-S l_Error
        CHAR(1);

  If p_job = RAPIDFIRE_JOB_NONE; // Used by PROMOTER.generate_Copy_Program()
    Return g_isError;
  EndIf;

  Exec SQL
    Select Error Into :l_Error
    From Jobs
    Where Job = :p_Job;
  setIgnoreWarnings();
  If SqlCheckState(sqlState);

    If l_Error = 'N';
      Return *Off;
    Else;
      Return *On;
    EndIf;

  Else;

    Return *On;

  EndIf;

END-PROC RAPIDFIRE_is_Error;

//*=========================================================================================*
//* Set Error                                                                               *
//*=========================================================================================*

DCL-PROC RAPIDFIRE_set_Error EXPORT;

  DCL-PI RAPIDFIRE_set_Error;
    p_Job
    CHAR(10) Const;
    p_Error
    IND Const;
  END-PI;

  // Locale field declarations

  DCL-S l_Error
        CHAR(1);

  If p_job = RAPIDFIRE_JOB_NONE; // Used by PROMOTER.generate_Copy_Program()
    g_isError = p_Error;
    Return;
  EndIf;

  If p_Error;
    l_Error = 'Y';
  Else;
    l_Error = 'N';
  EndIf;

  Exec SQL
    Update Jobs
    Set Error = :l_Error,
        Error_Text = ''
    Where Job = :p_Job;
  setIgnoreWarnings();
  SqlCheckState(sqlState);

END-PROC RAPIDFIRE_set_Error;

//*=========================================================================================*
//* Start                                                                                   *
//*=========================================================================================*

DCL-PROC RAPIDFIRE_start EXPORT;

  DCL-PI RAPIDFIRE_start IND; // <-- Success
    p_SUCCESS CHAR(1);        // <-- Success
    p_MSG     CHAR(256);      // <-- Message
  END-PI;

  If Not FILE_is_File_Journaled(
    '*LIBL' : // --> Library - File
    'JOBS'    // --> File
  );

    p_SUCCESS = 'N';
    p_MSG =
      'The Raid Fire files are not journaled. ' +
      'Probaly you unchecked the button ' +
      '"Start journaling Rapid Fire files" ' +
      'when you transfered the Rapid Fire library ' +
      'to the host. Execute ' +
      'CALL ' +
      'PGM(MY_RAPID_FIRE_LIB/STRENDJRN) ' +
      'PARM(''*START'' ''MY_RAPID_FIRE_LIB'')';

  Else;

    g_RSLTLNG = LANGUAGE_setLanguage();

    RFCCSTR(
      p_SUCCESS : // <-- Success
      p_MSG       // <-- Message
    );

  EndIf;

  if (p_SUCCESS = 'Y');
    return *on;
  else;
    return *off;
  endif;

END-PROC RAPIDFIRE_start;

//*=========================================================================================*
//* Stop                                                                                    *
//*=========================================================================================*

DCL-PROC RAPIDFIRE_stop EXPORT;

  DCL-PI RAPIDFIRE_stop IND; // <-- Success
    p_SUCCESS CHAR(1);       // <-- Success
    p_MSG     CHAR(256);     // <-- Message
  END-PI;

  RFCCEND(
    p_SUCCESS : // <-- Success
    p_MSG       // <-- Message
  );

  If g_RSLTLNG = 'Y';
    LANGUAGE_resetLanguage();
  EndIf;

  if (p_SUCCESS = 'Y');
    return *on;
  else;
    return *off;
  endif;

END-PROC RAPIDFIRE_stop;

//*=========================================================================================*
//* Get Auto Commit                                                                         *
//*=========================================================================================*

DCL-PROC RAPIDFIRE_getAutoCommit EXPORT;

  DCL-PI RAPIDFIRE_getAutoCommit CHAR(1); // <-- Auto Commit
  END-PI;

  return g_AUTCMT;

END-PROC RAPIDFIRE_getAutoCommit;

//*=========================================================================================*
//* Set Auto Commit                                                                         *
//*=========================================================================================*

DCL-PROC RAPIDFIRE_setAutoCommit EXPORT;

  DCL-PI RAPIDFIRE_setAutoCommit IND; // <-- Success
    p_AUTCMT  CHAR(1) CONST;          // --> Auto Commit
    p_SUCCESS CHAR(1);                // <-- Success
    p_MSG     CHAR(256);              // <-- Message
  END-PI;

  if (p_AUTCMT = 'Y' or
      p_AUTCMT = 'N');
    g_AUTCMT = p_AUTCMT;
    p_SUCCESS = 'Y';
    p_MSG = *blanks;
  else;
    p_SUCCESS = 'N';
    p_MSG = '001';
  endif;

  if (p_SUCCESS = 'Y');
    return *on;
  else;
    return *off;
  endif;

END-PROC RAPIDFIRE_setAutoCommit;

//*=========================================================================================*
//* Commit                                                                                  *
//*=========================================================================================*

DCL-PROC RAPIDFIRE_commit EXPORT;

  DCL-PI RAPIDFIRE_commit IND; // <-- Success
    p_SUCCESS CHAR(1);         // <-- Success
    p_MSG     CHAR(256);       // <-- Message
  END-PI;

  commit(e);

  if (not %error);
    p_SUCCESS = 'Y';
    p_MSG = *blanks;
  else;
    p_SUCCESS = 'N';
    p_MSG = '001';
  endif;

  if (p_SUCCESS = 'Y');
    return *on;
  else;
    return *off;
  endif;

END-PROC RAPIDFIRE_commit;

//*=========================================================================================*
//* Rollback                                                                                *
//*=========================================================================================*

DCL-PROC RAPIDFIRE_rollback EXPORT;

  DCL-PI RAPIDFIRE_rollback IND; // <-- Success
    p_SUCCESS CHAR(1);           // <-- Success
    p_MSG     CHAR(256);         // <-- Message
  END-PI;

  rolbk(e);

  if (not %error);
    p_SUCCESS = 'Y';
    p_MSG = *blanks;
  else;
    p_SUCCESS = 'N';
    p_MSG = '001';
  endif;

  if (p_SUCCESS = 'Y');
    return *on;
  else;
    return *off;
  endif;

END-PROC RAPIDFIRE_rollback;
