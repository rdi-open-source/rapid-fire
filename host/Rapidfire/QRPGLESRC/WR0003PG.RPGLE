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
//* Wrapper - PROMOTER_apply_Changes                                                        *
//*                                                                                         *
//*-----------------------------------------------------------------------------------------*
//*  >>PRE-COMPILER<<                                                                       *
//*    >>CRTCMD<< CRTBNDRPG    PGM(&LI/&OB) +                                               *
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
//* 10.07.2015 I   FH   I Programm erstellt                                                 *
//*------------+--------+-------------------------------------------------------------------*

// Control

CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,');
CTL-OPT DftActGrp(*NO) ActGrp('RAPIDFIRE') BNDDIR('RAPIDFIRE');
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

// Prozedurenprototyp für Prozedur 'WR0003PG'

/Copy QCPYSRC,WR0003PG

// Prozedurenschnittstelle für Prozedur 'WR0003PG'

DCL-PI WR0003PG;
  p_Job
  CHAR(10) Const;                                                        // --> Job
  p_File_Library
  CHAR(10) Const;                                                        // --> Bibliothek Da
  p_File
  CHAR(10) Const;                                                        // --> Datei
END-PI;

//*-----------------------------------------------------------------------------------------*

// Prozedurenprototypen für Modul 'PROMOTER'

/Copy QCPYSRC,PROMOTER

//*-----------------------------------------------------------------------------------------*

// Serviceprogramm aufrufen
PROMOTER_apply_Changes(
p_JOB :                                                                  // --> Job
p_File_Library :                                                         // --> Bibliothek Da
p_File                                                                   // --> Datei
);
// Prozedur verlassen
Return;
