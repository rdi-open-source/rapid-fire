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
//* File Handler                                                                            *
//*                                                                                         *
//*-----------------------------------------------------------------------------------------*
//*  >>PRE-COMPILER<<                                                                       *
//*    >>CRTCMD<< CRTSQLRPGI   OBJ(&LI/&OB) +                                               *
//*                              SRCFILE(&SL/&SF) SRCMBR(&SM) +                             *
//*                              OBJTYPE(*PGM);                                             *
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

CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,');
CTL-OPT DftActGrp(*NO) ActGrp('RAPIDFIRE') BNDDIR('RAPIDFIRE');
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

// Includes

/Include QCPYSRC,RAPIDFIRE
/Include QCPYSRC,EXCCMD
/Include QCPYSRC,PROMOTER
/Include QCPYSRC,SQLSTS

//*-----------------------------------------------------------------------------------------*

// Parameter for this program

DCL-PR FILEHDLR
       ExtPgm('FILEHDLR');
  p_Job
  CHAR(10) Const;
  p_File_Library
  CHAR(10) Const;
  p_File
  CHAR(10) Const;
END-PR;

DCL-PI FILEHDLR;
  p_Job
  CHAR(10) Const;
  p_File_Library
  CHAR(10) Const;
  p_File
  CHAR(10) Const;
END-PI;

//*-----------------------------------------------------------------------------------------*

// Program status data structure

DCL-DS PSDS
       LEN(429) Qualified PSDS;
  Job
  CHAR(10) POS(244);
  User
  CHAR(10) POS(254);
  Number
  CHAR(6) POS(264);
END-DS;

// Global field declarations

DCL-S g_Loop
      IND Inz(*On);
DCL-S g_Status
      CHAR(10);
DCL-S g_Set_Stop_Apply_Changes
      IND;
DCL-S g_wait_Seconds
      ZONED(3) Inz(15);
DCL-S g_Stop_Apply_Changes
      CHAR(1);

//*-----------------------------------------------------------------------------------------*

// Set SQL options

Exec SQL
  Set Option
  DatFmt=*ISO,
  TimFmt=*ISO,
  Commit=*NONE,
  CloSQLCsr=*ENDMOD;

//*-----------------------------------------------------------------------------------------*

DoW g_Loop;

  Exec SQL
    Update RFJobs
    Set Status = '*RUN',
        Phase = '*CPY-RCD'
    Where Job = :p_Job And
          Batch_Job = :PSDS.Job And
          Batch_User = :PSDS.User And
          Batch_Number = :PSDS.Number;
  setIgnoreWarnings();
  SqlCheckState(sqlState);

  PROMOTER_promote(p_Job : p_File_Library : p_File);
  If RAPIDFIRE_is_Error(p_Job);
    Leave;
  EndIf;

  Exec SQL
    Select Status Into :g_Status
    From Jobs
    where Job = :p_Job;
  setIgnoreWarnings();
  If Not (SqlCheckState(sqlState) And g_Status = '*RUN');
    Leave;
  EndIf;

  Exec SQL
    Update Areas
    Set Records_To_Copy = Records_Copied
    Where Job = :p_Job And
          Library = :p_File_Library And
          File = :p_File;
  setIgnoreWarnings();
  SqlCheckState(sqlState);

  Exec SQL
    Update RFJobs
    Set Phase = '*APY-CHG'
    Where Job = :p_Job And
          Batch_Job = :PSDS.Job And
          Batch_User = :PSDS.User And
          Batch_Number = :PSDS.Number;
  setIgnoreWarnings();
  SqlCheckState(sqlState);

  g_Stop_Apply_Changes = 'N';

  g_Set_Stop_Apply_Changes = *Off;

  DoW g_Loop;

    If g_Stop_Apply_Changes = 'N';

      PROMOTER_apply_Changes(p_Job : p_File_Library : p_File);
      If RAPIDFIRE_is_Error(p_Job);
        Leave;
      EndIf;

    Else;

      If Not g_Set_Stop_Apply_Changes;

        g_Set_Stop_Apply_Changes = *On;

        Exec SQL
          Update RFJobs
          Set Stop_Apply_Changes = 'Y'
          Where Job = :p_Job And
                Batch_Job = :PSDS.Job And
                Batch_User = :PSDS.User And
                Batch_Number = :PSDS.Number;
        setIgnoreWarnings();
        SqlCheckState(sqlState);

      EndIf;

    EndIf;

    Exec SQL
      Select Status, Stop_Apply_Changes
      Into :g_Status, :g_Stop_Apply_Changes
      From Jobs
      where Job = :p_Job;
    setIgnoreWarnings();
    If Not (SqlCheckState(sqlState) And g_Status = '*RUN');
      Leave;
    EndIf;

    EXCCMD_execute_Command(
      'DLYJOB DLY(' + %Char(g_wait_Seconds) + ')'
    );

  EndDo;

  Leave;

EndDo;

// Leave this program
*INLR = *On;
