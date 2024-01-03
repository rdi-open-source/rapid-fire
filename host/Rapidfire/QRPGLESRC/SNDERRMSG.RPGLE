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
//* Send Error Message                                                                      *
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
//*              *CNT3    --> Nachricht wird an die vorvorhergehende Prozedur gesendet.     *
//*              *CNT4    --> *CNT3 + 1                                                     *
//*              *CNT5    --> *CNT4 + 1                                                     *
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

// Prozedurenprototyp für Prozedur 'SndErrMsg'

/Copy QCPYSRC,SNDERRMSG

// Prozedurenprototyp für Prozedur 'QMHSNDPM'

/Copy QCPYSRC,QMHSNDPM

// Datenstruktur für Format 'ERRC0100' für Fehlercode

/Copy QCPYSRC,ERRC0100

// Sonstige Felddeklarationen

DCL-S ITCnt INT(10);                                                     // Aufrufstapelzähle
DCL-S ITMsgKey CHAR(4);                                                  // Nachrichtenschlüs
DCL-S ITMsgType CHAR(10);                                                // Nachrichtenart

//*=========================================================================================*
//* Fehlernachricht senden                                                                  *
//*=========================================================================================*

DCL-PROC SndErrMsg EXPORT;

  DCL-PI SndErrMsg CHAR(10);                                               // <-- Rückkehrwert
    PICallStk CHAR(10) Const;                                              // --> Aufrufstapele
    PIMsgId CHAR(7) Const;                                                 // --> Nachrichten-I
    PIMsgF CHAR(10) Const;                                                 // --> Nachrichtenda
    PILibMsgF CHAR(10) Const;                                              // --> Bibliothek Na
    PIMsgDta CHAR(256) Const;                                              // --> Nachrichtenda
    PIMsgType CHAR(10) Const Options(*NoPass);                             // --> Nachrichtenar
  END-PI;

  // Aufrufstapelzähler ermitteln
  Select;
  When PICallStk = '*SAME';
    ITCnt = 1;
  When PICallStk = '*PRV';
    ITCnt = 2;
  When PICallStk = '*CNT3';
    ITCnt = 3;
  When PICallStk = '*CNT4';
    ITCnt = 4;
  When PICallStk = '*CNT5';
    ITCnt = 5;
  EndSl;
  // Nachrichtenart ermitteln
  If %Parms = 5;
    ITMsgType = '*INFO';
  Else;
    ITMsgType = PIMsgType;
  EndIf;
  // Nachricht in die Programmnachrichtenwarteschlange senden
  Reset ERRC0100;
  QMHSNDPM(
   PIMsgId :                                                               // --> Message ident
   PIMsgF + PILibMsgF :                                                    // --> Qualified mes
   PIMsgDta :                                                              // --> Message data
   %Size(PIMsgDta) :                                                       // --> Length of mes
   ITMsgType :                                                             // --> Message type
   '*' :                                                                   // --> Call stack en
   ITCnt :                                                                 // --> Call stack co
   ITMsgKey :                                                              // <-- Message key
   ERRC0100 :                                                              // <-> Error code
   %Size('*') :                                                            // --> Length of cal
   '*NONE     *NONE' :                                                     // --> Call stack en
   0
  );
  If ERRC0100.Exeption_Id <> *Blanks;
    Return '*EXCEPTION';
  EndIf;
  // Prozedur verlassen
  Return '*PROCESS';

END-PROC SndErrMsg;
