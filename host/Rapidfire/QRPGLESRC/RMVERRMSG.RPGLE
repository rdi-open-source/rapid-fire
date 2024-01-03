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
//* Remove Error Message                                                                    *
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
//* *EXCEPTION --> Die Prozedur wurde mit einer Ausnahmebedingung beendet.                  *
//* *PROCESS   --> Die Prozedur wurde mit Verarbeitungsstatus normal beendet.               *
//*                                                                                         *
//* Prozedurschnittstelle                                                                   *
//* =====================                                                                   *
//*                                                                                         *
//* PICallStk  / *SAME    --> Nachicht wird an die aktuelle Prozedur gesendet.              *
//*              *PRV     --> Nachricht wird an die vorhergehende Prozedur gesendet.        *
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

// Prozedurenprototyp für Prozedur 'RmvErrMsg'

/Copy QCPYSRC,RMVERRMSG

// Prozedurenprototyp für Prozedur 'QMHRMVPM'

/Copy QCPYSRC,QMHRMVPM

// Datenstruktur für Format 'ERRC0100' für Fehlercode

/Copy QCPYSRC,ERRC0100

// Sonstige Felddeklarationen

DCL-S PICnt INT(10);                                                     // Aufrufstapelzähle

//*=========================================================================================*
//* Fehlernachricht entfernen                                                               *
//*=========================================================================================*

DCL-PROC RmvErrMsg EXPORT;

  DCL-PI RmvErrMsg CHAR(10);                                               // <-- Rückkehrwert
    PICallstk CHAR(10) Const;                                              // --> Aufrufstapele
  END-PI;

  // Aufrufstapelzähler ermitteln
  Select;
  When PICallstk = '*SAME';
    PICnt = 1;
  When PICallstk = '*PRV';
    PICnt = 2;
  EndSl;
  // Programmnachrichtenwarteschlange bereinigen
  Reset ERRC0100;
  QMHRMVPM(
   '*' :                                                                   // --> Call stack en
   PICnt :                                                                 // --> Call stack co
   *Blanks :                                                               // --> Message key
   '*ALL' :                                                                // --> Messages to r
   ERRC0100 :                                                              // <-> Error code
   1 :                                                                     // --> Length of cal
   '*NONE     *NONE' :                                                     // --> Call stack en
   '*YES'                                                                  // --> Remove unhand
  );
  If ERRC0100.Exeption_Id <> *Blanks;
    Return '*EXCEPTION';
  EndIf;
  // Prozedur verlassen
  Return '*PROCESS';

END-PROC RmvErrMsg;
