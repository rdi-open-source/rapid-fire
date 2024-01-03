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
//* Who Am I                                                                                *
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
//* 18.03.2016 I   FH   I Creation                                                          *
//*------------+--------+-------------------------------------------------------------------*

// Control

CTL-OPT NoMain;
CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,');
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT BndDir('RAPIDFIRE');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

// Includes

/Include QCPYSRC,WHOAMI

//*=========================================================================================*
//* Who Am I                                                                                *
//*=========================================================================================*

DCL-PROC Who_Am_I EXPORT;

  DCL-PI Who_Am_I
         CHAR(300);                                                        // <-- I Am
  END-PI;

  // Locale field declarations

  DCL-S l_Who_Am_I
        CHAR(300);

  Who_Am_I_Internal_2(l_Who_Am_I);

  Return l_Who_Am_I;

END-PROC Who_Am_I;

//*=========================================================================================*
//* Who Am I With Call Stack Entry                                                          *
//*=========================================================================================*

DCL-PROC Who_Am_I_With_Call_Stack_Entry EXPORT;

  DCL-PI Who_Am_I_With_Call_Stack_Entry
         CHAR(300);                                                        // <-- I Am
    p_Program
    CHAR(10) Const;                                                        // --> Program
    p_Module
    CHAR(10) Const;                                                        // --> Modul
    p_Procedure
    CHAR(256) Const;                                                       // --> Procedure
  END-PI;

  // Locale field declarations

  DCL-S l_Who_Am_I
        CHAR(300);

  Who_Am_I_Internal_3(p_Program : p_Module : p_Procedure : l_Who_Am_I);

  Return l_Who_Am_I;

END-PROC Who_Am_I_With_Call_Stack_Entry;
