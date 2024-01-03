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
//* Retrieve Function Key                                                                   *
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
//*                                                                                         *
//* Prozedurergebnis                                                                        *
//* ================                                                                        *
//*                                                                                         *
//* *F1        --> Taste 'F1'                                                               *
//* *F2        --> Taste 'F2'                                                               *
//* *F3        --> Taste 'F3'                                                               *
//* *F4        --> Taste 'F4'                                                               *
//* *F5        --> Taste 'F5'                                                               *
//* *F6        --> Taste 'F6'                                                               *
//* *F7        --> Taste 'F7'                                                               *
//* *F8        --> Taste 'F8'                                                               *
//* *F9        --> Taste 'F9'                                                               *
//* *F10       --> Taste 'F10'                                                              *
//* *F11       --> Taste 'F11'                                                              *
//* *F12       --> Taste 'F12'                                                              *
//* *F13       --> Taste 'F13'                                                              *
//* *F14       --> Taste 'F14'                                                              *
//* *F15       --> Taste 'F15'                                                              *
//* *F16       --> Taste 'F16'                                                              *
//* *F17       --> Taste 'F17'                                                              *
//* *F18       --> Taste 'F18'                                                              *
//* *F19       --> Taste 'F19'                                                              *
//* *F20       --> Taste 'F20'                                                              *
//* *F21       --> Taste 'F21'                                                              *
//* *F22       --> Taste 'F22'                                                              *
//* *F23       --> Taste 'F23'                                                              *
//* *F24       --> Taste 'F24'                                                              *
//* *ROLLUP    --> Taste 'PageDown'                                                         *
//* *ROLLDOWN  --> Taste 'PageUp'                                                           *
//* *PRINT     --> Taste 'Print'                                                            *
//* *HOME      --> Taste 'Home'                                                             *
//* *HELP      --> Taste 'Help'                                                             *
//* *ENTER     --> Taste 'Enter'                                                            *
//* *CLEAR     --> Taste 'Clear'                                                            *
//* *AUTOENTER --> Automatische Satzfreigabe                                                *
//* *INVALID   --> Ungültiger AID-Code                                                      *
//*                                                                                         *
//*------------+--------+-------------------------------------------------------------------*
//*   Datum    I Progr. I Beschreibung                                                      *
//*------------+--------+-------------------------------------------------------------------*
//* 01.07.2014 I   FH   I Programm erstellt                                                 *
//*------------+--------+-------------------------------------------------------------------*

CTL-OPT NoMain;
CTL-OPT DatFmt(*Eur) TimFmt(*Eur) DecEdit('0,');
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

// Prozedurenprototyp für Prozedur 'RtvFktKey'

/Copy QCPYSRC,RTVFKTKEY

//*=========================================================================================*
//* Funktionstaste abrufen                                                                  *
//*=========================================================================================*

DCL-PROC RtvFktKey EXPORT;

  DCL-PI RtvFktKey CHAR(10);                                               // <-- Funktionstast
    PIAIDCde CHAR(1) Const;                                                // --> AID-Code
  END-PI;

  // AID-Code umsetzen
  Select;
  When PIAIDCde = X'31';
    Return '*F1';
  When PIAIDCde = X'32';
    Return '*F2';
  When PIAIDCde = X'33';
    Return '*F3';
  When PIAIDCde = X'34';
    Return '*F4';
  When PIAIDCde = X'35';
    Return '*F5';
  When PIAIDCde = X'36';
    Return '*F6';
  When PIAIDCde = X'37';
    Return '*F7';
  When PIAIDCde = X'38';
    Return '*F8';
  When PIAIDCde = X'39';
    Return '*F9';
  When PIAIDCde = X'3A';
    Return '*F10';
  When PIAIDCde = X'3B';
    Return '*F11';
  When PIAIDCde = X'3C';
    Return '*F12';
  When PIAIDCde = X'3F';
    Return '*AUTOENTER';
  When PIAIDCde = X'B1';
    Return '*F13';
  When PIAIDCde = X'B2';
    Return '*F14';
  When PIAIDCde = X'B3';
    Return '*F15';
  When PIAIDCde = X'B4';
    Return '*F16';
  When PIAIDCde = X'B5';
    Return '*F17';
  When PIAIDCde = X'B6';
    Return '*F18';
  When PIAIDCde = X'B7';
    Return '*F19';
  When PIAIDCde = X'B8';
    Return '*F20';
  When PIAIDCde = X'B9';
    Return '*F21';
  When PIAIDCde = X'BA';
    Return '*F22';
  When PIAIDCde = X'BB';
    Return '*F23';
  When PIAIDCde = X'BC';
    Return '*F24';
  When PIAIDCde = X'BD';
    Return '*CLEAR';
  When PIAIDCde = X'F1';
    Return '*ENTER';
  When PIAIDCde = X'F3';
    Return '*HELP';
  When PIAIDCde = X'F4';
    Return '*ROLLDOWN';
  When PIAIDCde = X'F5';
    Return '*ROLLUP';
  When PIAIDCde = X'F6';
    Return '*PRINT';
  When PIAIDCde = X'F8';
    Return '*HOME';
  Other;
    Return '*INVALID';
  EndSl;

END-PROC RtvFktKey;
