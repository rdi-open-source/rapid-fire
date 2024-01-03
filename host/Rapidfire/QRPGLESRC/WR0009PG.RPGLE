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
//* Wrapper - Delete Job                                                                    *
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
//* 08.03.2018 I   FH   I Programm erstellt                                                 *
//*------------+--------+-------------------------------------------------------------------*

// Control

CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,');
CTL-OPT DftActGrp(*NO) ActGrp('RAPIDFIRE') BNDDIR('RAPIDFIRE');
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

// Prozedurenprototyp für Prozedur 'WR0009PG'

/Copy QCPYSRC,WR0009PG

// Prozedurenschnittstelle für Prozedur 'WR0009PG'

DCL-PI WR0009PG;
  p_JOB CHAR(10) Const; // --> Job
  p_SUC CHAR(1);        // <-- Success
  p_MSG CHAR(256);      // <-- Message
END-PI;

//*-----------------------------------------------------------------------------------------*

// Prozedurenprototyp für Prozedur 'MNTJOB'

/Copy QCPYSRC,MNTJOB

// Prozedurenprototypen für Modul 'EXCCMD'

/Copy QCPYSRC,EXCCMD

// Prozedurenprototyp für Prozedur 'QTNRCMTI'

/Copy QCPYSRC,QTNRCMTI

// Datenstruktur für Format 'CMTI0100' für Prozedur 'QTNRCMTI'

/Copy QCPYSRC,CMTI0100

// Datenstruktur für Format 'ERRC0100' für Fehlercode

/Copy QCPYSRC,ERRC0100

// Sonstige Felddeklarationen

DCL-S g_STS CHAR(10);                                                    // Nachrichten-Id. b
DCL-S g_PRC CHAR(10);                                                    // Verarbeitung
DCL-S g_FLD CHAR(10);                                                    // Feld

//*-----------------------------------------------------------------------------------------*

// Erfolg initialisieren
p_SUC = 'N';
p_MSG = '*** UNKNOWN ERROR ***';

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
  g_PRC = '*YES';

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
      g_PRC = '*NO';
    EndIf;

  EndIf;

  If g_PRC = '*YES';

    // Dateien öffnen
    MNTJOB_openFiles();

    // Initialisieren
    If MNTJOB_initialize(
      MNTJOB_DELETE : // --> Modus
      p_JOB :         // --> Job
      p_SUC :         // <-- Erfolg
      p_MSG           // <-- Nachricht
    );

      If MNTJOB_check(
        p_SUC : // <-- Erfolg
        g_FLD : // <-- Feld
        p_MSG   // <-- Nachricht
      );

        // Verbuchen
        MNTJOB_book();

        // Erfolg setzen
        p_SUC = 'Y';
        p_MSG = *Blanks;

      EndIf;

    EndIf;

    // Dateien schließen
    MNTJOB_closeFiles();

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
