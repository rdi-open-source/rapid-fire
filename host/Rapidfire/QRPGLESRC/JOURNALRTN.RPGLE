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
//* Returncode of Exit Program for Command RCVJRNE                                          *
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
//*-----------------------------------------------------------------------------------------*

// Control

CTL-OPT NoMain;
CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,') AlwNull(*USRCTL);
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

// Global field declarations

DCL-S g_Job
      CHAR(10);
DCL-S g_Return_Code
      CHAR(10);
DCL-S g_Date
      CHAR(6);
DCL-S g_Time
      CHAR(6);

//*=========================================================================================*
//* Set Job                                                                                 *
//*=========================================================================================*

DCL-PROC JOURNALRTN_set_Job EXPORT;

  DCL-PI JOURNALRTN_set_Job;
    p_Job
    CHAR(10) Const;
  END-PI;

  g_Job = p_Job;

END-PROC JOURNALRTN_set_Job;

//*=========================================================================================*
//* Get Job                                                                                 *
//*=========================================================================================*

DCL-PROC JOURNALRTN_get_Job EXPORT;

  DCL-PI JOURNALRTN_get_Job
         CHAR(10);
  END-PI;

  Return g_Job;

END-PROC JOURNALRTN_get_Job;

//*=========================================================================================*
//* Set Return Code                                                                         *
//*=========================================================================================*

DCL-PROC JOURNALRTN_set_Return_Code EXPORT;

  DCL-PI JOURNALRTN_set_Return_Code;
    p_Return_Code
    CHAR(10) Const;
  END-PI;

  g_Return_Code = p_Return_Code;

END-PROC JOURNALRTN_set_Return_Code;

//*=========================================================================================*
//* Get Return Code                                                                         *
//*=========================================================================================*

DCL-PROC JOURNALRTN_get_Return_Code EXPORT;

  DCL-PI JOURNALRTN_get_Return_Code
         CHAR(10);
  END-PI;

  Return g_Return_Code;

END-PROC JOURNALRTN_get_Return_Code;

//*=========================================================================================*
//* Set Date                                                                                *
//*=========================================================================================*

DCL-PROC JOURNALRTN_set_Date EXPORT;

  DCL-PI JOURNALRTN_set_Date;
    p_Date
    CHAR(6) Const;
  END-PI;

  g_Date = p_Date;

END-PROC JOURNALRTN_set_Date;

//*=========================================================================================*
//* Get Date                                                                                *
//*=========================================================================================*

DCL-PROC JOURNALRTN_get_Date EXPORT;

  DCL-PI JOURNALRTN_get_Date
         CHAR(6);
  END-PI;

  Return g_Date;

END-PROC JOURNALRTN_get_Date;

//*=========================================================================================*
//* Set Time                                                                                *
//*=========================================================================================*

DCL-PROC JOURNALRTN_set_Time EXPORT;

  DCL-PI JOURNALRTN_set_Time;
    p_Time
    CHAR(6) Const;
  END-PI;

  g_Time = p_Time;

END-PROC JOURNALRTN_set_Time;

//*=========================================================================================*
//* Get Time                                                                                *
//*=========================================================================================*

DCL-PROC JOURNALRTN_get_Time EXPORT;

  DCL-PI JOURNALRTN_get_Time
         CHAR(6);
  END-PI;

  Return g_Time;

END-PROC JOURNALRTN_get_Time;
