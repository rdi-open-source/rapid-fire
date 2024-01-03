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
//* Retrieve Command Line                                                                   *
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
//* PIRtvType  / *FIRST   --> Die erste Nachricht auffinden.                                *
//*              *LAST    --> Die letzte Nachricht auffinden.                               *
//*              *NEXT    --> Die nächste Nachricht auffinden.                              *
//*              *PRV     --> Die vorherige Nachricht auffinden.                            *
//*                                                                                         *
//* PIRtvCmd   / *YES     --> Es konnte ein Befehl abgerufen werden.                        *
//*              *NO      --> Es konnte kein Befehl mehr abgerufen werden.                  *
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

// Prozedurenprototyp für Prozedur 'RtvCmdLin'

/Copy QCPYSRC,RTVCMDLIN

// Prozedurenprototyp für Prozedur 'QMHRTVRQ'

/Copy QCPYSRC,QMHRTVRQ

// Datenstruktur für Format 'RTVQ0100' für Prozedur 'QMHRTVRQ'

/Copy QCPYSRC,RTVQ0100

// Datenstruktur für Format 'ERRC0100' für Fehlercode

/Copy QCPYSRC,ERRC0100

//*=========================================================================================*
//* Befehl für Befehlszeile auffinden                                                       *
//*=========================================================================================*

DCL-PROC RtvCmdLin EXPORT;

  DCL-PI RtvCmdLin CHAR(10);                                               // <-- Rückkehrwert
    PIRtvType CHAR(10) Const;                                              // --> Abrufart
    PIMsgKey CHAR(4);                                                      // <-> Nachrichtensc
    PICmd CHAR(1024);                                                      // <-- Befehl
    PIRtvCmd CHAR(10);                                                     // <-- Befehl abgeru
  END-PI;

  // Anforderungsnachricht abrufen, die der Benutzer verursacht hat
  DoU RTVQ0100.Bytes_Available = *Zero or
    (PICmd <> '/*      */' And
    PICmd <> *Blanks);
    // Anforderungsnachricht abrufen
    Reset ERRC0100;
    QMHRTVRQ(
     RTVQ0100 :                                                            // <-- Message infor
     %Size(RTVQ0100) :                                                     // --> Length of mes
     'RTVQ0100' :                                                          // --> Format name
     PIRtvType :                                                           // --> Message type
     PIMsgKey :                                                            // --> Message key
     ERRC0100                                                              // <-> Error code
    );
    If ERRC0100.Exeption_Id <> *Blanks;
      Return '*EXCEPTION';
    EndIf;
    // Rückkehrparameter füllen
    If RTVQ0100.Bytes_Available = *Zero;
      PIRtvCmd = '*NO';
      PIMsgKey = *Blanks;
      PICmd = *Blanks;
    Else;
      PIRtvCmd = '*YES';
      PIMsgKey = RTVQ0100.Message_Key;
      PICmd = %Subst(RTVQ0100.Request_Message:1:
                    RTVQ0100.Bytes_Returned - 40);
    EndIf;
  EndDo;
  // Prozedur verlassen
  Return '*PROCESS';

END-PROC RtvCmdLin;
