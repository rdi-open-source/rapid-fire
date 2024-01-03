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
//* Wrapper - Maintain Job                                                                  *
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
//* 26.08.2015 I   FH   I Programm erstellt                                                 *
//*------------+--------+-------------------------------------------------------------------*

// Control

CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,');
CTL-OPT DftActGrp(*NO) ActGrp('RAPIDFIRE') BNDDIR('RAPIDFIRE');
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

// Prozedurenprototyp für Prozedur 'WR0007PG'

/Copy QCPYSRC,WR0007PG

// Prozedurenschnittstelle für Prozedur 'WR0007PG'

DCL-PI WR0007PG;
  PIMode CHAR(10) Const;                                                 // --> Modus
  PIFLTJOB CHAR(10) Const;                                               // --> Filter Job
  PIRtCd CHAR(10);                                                       // <-- Rückkehrcode
END-PI;

//*-----------------------------------------------------------------------------------------*

// Prozedurenprototyp für Prozedur 'RF0001PG'

/Copy QCPYSRC,RF0001PG

// Prozedurenprototypen für Modul 'EXCCMD'

/Copy QCPYSRC,EXCCMD

// Prozedurenprototyp für Prozedur 'QTNRCMTI'

/Copy QCPYSRC,QTNRCMTI

// Datenstruktur für Format 'CMTI0100' für Prozedur 'QTNRCMTI'

/Copy QCPYSRC,CMTI0100

// Datenstruktur für Format 'ERRC0100' für Fehlercode

/Copy QCPYSRC,ERRC0100

// Sonstige Felddeklarationen

DCL-S ITSts CHAR(10);                                                    // Nachrichten-Id. b
DCL-S ITPrc CHAR(10);                                                    // Verarbeitung
DCL-S ITJOB CHAR(10);                                                    // Job

//*-----------------------------------------------------------------------------------------*

// Commitment Informationen auffinden
Reset ERRC0100;
QTNRCMTI(
 CMTI0100 :                                                              // <-- Receiver vari
 %Size(CMTI0100) :                                                       // --> Length of rec
 'CMTI0100' :                                                            // --> Format name
 ERRC0100                                                                // <-> Error code
);
// Commitment Informationen wurden aufgefunden
If ERRC0100.Exeption_Id = *Blanks;
  // Verarbeitung initialisieren
  ITPrc = '*YES';
  // Commitment Control muss gestartet werden
  If CMTI0100.Commit_Definition_Status = 'I';
    // Commitment-Control starten
    EXCCMD_execute_Command(
     'STRCMTCTL ' +
     'LCKLVL(*CHG) ' +
     'CMTSCOPE(*ACTGRP) ' +
     'TEXT(''Rapid Fire'')'
    );
    // Commitment-Control konnte nicht gestartet werden
    If EXCCMD_Message_Id <> *Blanks;
      ITPrc = '*NO';
    EndIf;
  EndIf;
  If ITPrc = '*YES';
    // Maintain Jobs
    RF0001PG(
     PIMode :                                                            // --> Modus
     PIFLTJOB :                                                          // --> Filter Job
     ITJOB :                                                             // <-> Job
     PIRtCd                                                              // <-- Rückkehrcode
    );
    // Commitment Control muss beendet werden
    If CMTI0100.Commit_Definition_Status = 'I';
      // Commitment-Control beenden
      EXCCMD_execute_Command(
      'ENDCMTCTL'
      );
    EndIf;
  EndIf;
EndIf;
// Programm nicht hauptspeicherresident verlassen
*INLR = *On;
