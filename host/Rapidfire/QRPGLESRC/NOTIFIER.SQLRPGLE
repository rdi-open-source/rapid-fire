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
//* Notifier                                                                                *
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

/Include QCPYSRC,RTVMSG
/Include QCPYSRC,EXCCMD
/Include QCPYSRC,SQLSTS

//*-----------------------------------------------------------------------------------------*

// Global field declarations

DCL-S g_Loop
      IND Inz(*On);

// File declarations

DCL-DS Subjects_To_Be_Notified
       Extname('SUBTOBENOT') Alias Qualified;
END-DS;

//*-----------------------------------------------------------------------------------------*

// Set SQL options

Exec SQL
  Set Option
  DatFmt=*ISO,
  TimFmt=*ISO,
  Commit=*NONE,
  CloSQLCsr=*ENDMOD;

//*=========================================================================================*
//* Notify Subjects                                                                         *
//*=========================================================================================*

DCL-PROC NOTIFIER_notify_Subjects EXPORT;

  DCL-PI NOTIFIER_notify_Subjects;
    p_Job
    CHAR(10) Const;
    p_Message_File_Library
    CHAR(10) Const;
    p_Message_File
    CHAR(10) Const;
    p_Message_Id
    CHAR(7) Const;
    p_Message_Data
    CHAR(256) Const;
  END-PI;

  // Locale field declarations

  DCL-S l_Message
        CHAR(256);
  DCL-S l_Command
        CHAR(1024);

  // Retrieve message to send
  RTVMSG(p_Message_File : p_Message_File_Library :
    p_Message_Id : p_Message_Data : l_Message);

  // Declare Cursor
  Exec SQL
    Declare Subjects_To_Be_Notified Cursor For
      Select *
      From Subjects_To_Be_Notified
      Where Job = :p_Job
      For Read Only;

  // Open cursor
  Exec SQL
    Open Subjects_To_Be_Notified;
  setIgnoreWarnings();
  If SqlCheckState(sqlState);

    DoW g_Loop;

      // Read 'Subjects to be notified'
      Exec SQL
        Fetch Next
        From Subjects_To_Be_Notified
        Into :Subjects_To_Be_Notified;

      // End of file
      setIgnoreWarnings();
      If Not SqlCheckState(sqlState);
        Leave;
      EndIf;

      // Send message
      l_Command =
        'SNDMSG ' +
        'MSG(''' + %Trim(l_Message) + ''')';
      Select;
      When Subjects_To_Be_Notified.Type = '*USR';
        l_Command =
          %Trim(l_Command) + ' ' +
          'TOUSR(' + %Trim(Subjects_To_Be_Notified.User) + ')';
      When Subjects_To_Be_Notified.Type = '*MSGQ';
        l_Command =
          %Trim(l_Command) + ' ' +
          'TOMSGQ(' +
            %Trim(Subjects_To_Be_Notified.Message_Queue_Library) + '/' +
            %Trim(Subjects_To_Be_Notified.Message_Queue) +
          ')';
      EndSl;
      EXCCMD_execute_Command(
        %Trim(l_Command)
      );

    EndDo;

    // Close cursor
    Exec SQL
      Close Subjects_To_Be_Notified;
    setIgnoreWarnings();
    SqlCheckState(sqlState);

  EndIf;

END-PROC NOTIFIER_notify_Subjects;
