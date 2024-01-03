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
//* Maintain File                                                                           *
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
//*-----------------------------------------------------------------------------------------*
//*                                                                                         *
//* (01 - 49) Freie Bezugszahlen für sonstige Dinge                                         *
//*                                                                                         *
//* 01      --> Modus : 'Erstellen'                                                         *
//* 02      --> Modus : 'Ändern'                                                            *
//* 03      --> Modus : 'Kopieren'                                                          *
//* 04      --> Modus : 'Löschen'                                                           *
//* 05      --> Modus : 'Anzeigen'                                                          *
//*                                                                                         *
//*-----------------------------------------------------------------------------------------*
//*                                                                                         *
//* (50 - 59) Reservierte Bezugszahlen für Diverses                                         *
//*                                                                                         *
//* 50      --> Schlüsselwort SFLEND in Nachrichtensubdatei aktivieren                      *
//* 51      --> Ergebnis für LookUp Operation                                               *
//*                                                                                         *
//*-----------------------------------------------------------------------------------------*
//*                                                                                         *
//* (60 - 79) Reservierte Bezugszahlen für Subdateiverarbeitung                             *
//*                                                                                         *
//* 60,64,68,72,76  --> (Subdatei 1 - 5) Inhalt der Subdatei löschen                        *
//* 61,65,69,73,77  --> (Subdatei 1 - 5) Keine Sätze in der Subdatei vorhanden              *
//* 62,66,70,74,78  --> (Subdatei 1 - 5) Letzter Satz befindet sich bereits in der Subdatei *
//* 63,67,71,75,79  --> (Subdatei 1 - 5) Subdateisatz ist ein geänderter Satz               *
//*                                                                                         *
//*-----------------------------------------------------------------------------------------*
//*                                                                                         *
//* (80 - 89) Reservierte Bezugszahlen für Fehlermeldungen                                  *
//*                                                                                         *
//* 80      --> Es liegt ein Fehler vor (Spezifikation durch BZ 81 - 89)                    *
//* 81 - 83 --> (Block 1) Fehlermeldungen                                                   *
//* 84 - 86 --> (Block 2) Fehlermeldungen                                                   *
//* 87 - 89 --> (Block 3) Fehlermeldungen                                                   *
//*                                                                                         *
//*-----------------------------------------------------------------------------------------*
//*                                                                                         *
//* (90 - 99) Reservierte Bezugszahlen für Cursorpositionierung                             *
//*                                                                                         *
//* 90      --> Es soll positioniert werden (Spezifikation durch 91 - 99)                   *
//* 91      --> Der Cursor soll auf Koordinate Zeile / Spalte positioniert werden           *
//*                                                                                         *
//*-----------------------------------------------------------------------------------------*
//*                                                                                         *
//* Übergabeparameter                                                                       *
//* =================                                                                       *
//*                                                                                         *
//* PIMode     / *CREATE  --> Modus : 'Erstellen'                                           *
//*              *CHANGE  --> Modus : 'Ändern'                                              *
//*              *COPY    --> Modus : 'Kopieren'                                            *
//*              *DELETE  --> Modus : 'Löschen'                                             *
//*              *DISPLAY --> Modus : 'Anzeigen'                                            *
//*                                                                                         *
//* PIRtCd     / *F3      --> Der Benutzer hat die Taste F3 gedrückt                        *
//*              *F12     --> Der Benutzer hat die Taste F12 gedrückt                       *
//*              *ENTER   --> Der Benutzer hat die Taste ENTER gedrückt                     *
//*                                                                                         *
//*------------+--------+-------------------------------------------------------------------*
//*   Datum    I Progr. I Beschreibung                                                      *
//*------------+--------+-------------------------------------------------------------------*
//* 01.07.2014 I   FH   I Programm erstellt                                                 *
//*------------+--------+-------------------------------------------------------------------*
//* 13.06.2016 I   TR   I Enabled program for '*DISPLAY' mode.                              *
//*------------+--------+-------------------------------------------------------------------*

// Control

CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,');
CTL-OPT DftActGrp(*NO) ActGrp('RAPIDFIRE') BNDDIR('RAPIDFIRE');
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

DCL-F RF0008DF Workstn(*EXT) USAGE(*INPUT:*OUTPUT)
      InfDS(FIDS_RF0008DF);

//*-----------------------------------------------------------------------------------------*

// Prozedurenprototyp für Prozedur 'RF0008PG'

/Copy QCPYSRC,RF0008PG

// Prozedurenschnittstelle für Prozedur 'RF0008PG'

DCL-PI RF0008PG;
  PIMode CHAR(10) Const;                                                 // --> Modus
  PIJOB CHAR(10) Const;                                                  // --> Job
  PIPOS PACKED(6);                                                       // <-> Position
  PIRtCd CHAR(10);                                                       // <-- Rückkehrcode
END-PI;

//*-----------------------------------------------------------------------------------------*

// Prozedurenprototyp für Prozedur 'RtvFktKey'

/Copy QCPYSRC,RTVFKTKEY

// Prozedurenprototyp für Prozedur 'RmvErrMsg'

/Copy QCPYSRC,RMVERRMSG

// Prozedurenprototyp für Prozedur 'SndErrMsg'

/Copy QCPYSRC,SNDERRMSG

// Prozedurenprototyp für Prozedur 'QUHDSPH'

/Copy QCPYSRC,QUHDSPH

// Datenstruktur für Format 'ERRC0100' für Fehlercode

/Copy QCPYSRC,ERRC0100

// Prozedurenprototyp für Prozedur 'MNTFILE'

/Copy QCPYSRC,MNTFILE

// Dateiinformationsdatenstruktur für RF0008DF

DCL-DS FIDS_RF0008DF;
  AIDCde CHAR(1) POS(369);
END-DS;

// Hilfe-Id.

DCL-DS *N;
  ITHlpId CHAR(52) Dim(100);                                             // Hilfe-Id.
  ITHIPG CHAR(10) Overlay(ITHlpId : 1);                                  // Panelgroup
  ITHILP CHAR(10) Overlay(ITHlpId : 11);                                 // Bibliothek Panelg
  ITHIMO CHAR(32) Overlay(ITHlpId : 21);                                 // Modul
END-DS;

// Sonstige Felddeklarationen

DCL-S ITLoop IND Inz(*On);                                               // Schleife
DCL-S ITFktKey CHAR(10);                                                 // Funktionstaste
DCL-S ITSts CHAR(10);                                                    // Nachrichten-Id. b
DCL-S ITMsgId CHAR(7);                                                   // Nachrichten-Id.
DCL-S ITMsgDta CHAR(256);                                                // Nachrichtendaten
DCL-S ITMsgF   CHAR(10);                                                 // Nachrichtendatei
DCL-S ITDsp INT(10) Dim(2);                                              // Anzeige Hilfetext
DCL-S ITNbr INT(10);                                                     // Anzahl Hilfetextm
DCL-S ITLftC INT(10) Dim(2);                                             // Linke Ecke
DCL-S ITRghC INT(10) Dim(2);                                             // Rechte Ecke
DCL-S ITCsrL INT(10) Dim(2);                                             // Cursorposition
DCL-S ITHlp CHAR(1);                                                     // Kontextbezogene H
DCL-S ITXFld CHAR(10);                                                   // Feld
DCL-S ITXMsg CHAR(256);                                                  // Nachricht
DCL-S ITSUCC CHAR(1);                                                    // Erfolg

//*-----------------------------------------------------------------------------------------*

// Bezugszahl für 'Schlüsselwort SFLEND in Nachrichtensubdatei aktivieren' auf 'Ja' setzen
*IN50 = *On;
// Dateien öffnen
MNTFILE_openFiles();
// Initialisieren
If MNTFILE_initialize(
  PIMode : // --> Mode
  PIJOB :  // --> Job
  PIPOS :  // --> Position
  ITSUCC : // <-- Success
  ITXMsg   // <-- Message
);
  // Job initialisieren
  DFJOB = PIJOB;
  Select;
    // Modus : 'Erstellen'
  When PIMode = MNTFILE_CREATE;
    ExSr SR005;
    If PIRtCd = '*ENTER';
      PIPOS = DFPOS;
    EndIf;
    // Modus : 'Ändern'
  When PIMode = MNTFILE_CHANGE;
    ExSr SR010;
    // Modus : 'Kopieren'
  When PIMode = MNTFILE_COPY;
    ExSr SR015;
    If PIRtCd = '*ENTER';
      PIPOS = DFPOS;
    EndIf;
    // Modus : 'Löschen'
  When PIMode = MNTFILE_DELETE;
    ExSr SR020;
    // Modus : 'Anzeigen'
  When PIMode = MNTFILE_DISPLAY;
    ExSr SR025;
  EndSl;
EndIf;
// Dateien schließen
MNTFILE_closeFiles();
// Programm nicht hauptspeicherresident verlassen
*INLR = *On;

//*-----------------------------------------------------------------------------------------*
//* Erstellen                                                                               *
//*-----------------------------------------------------------------------------------------*

BegSr SR005;
  // Überschrift für Modus 'Erstellen' anzeigen
  *IN(1) = *ON;
  *IN(2) = *OFF;
  *IN(3) = *OFF;
  *IN(4) = *OFF;
  *IN(5) = *OFF;
  // Bildschirm mit Werten füllen
  ExSr SR030;
  // Bildschirm verarbeiten
  ExSr SR045;
  If PIRtCd = '*ENTER';
    // Verbuchen
    MNTFILE_book();
  EndIf;
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Ändern                                                                                  *
//*-----------------------------------------------------------------------------------------*

BegSr SR010;
  // Überschrift für Modus 'Ändern' anzeigen
  *IN(1) = *OFF;
  *IN(2) = *ON;
  *IN(3) = *OFF;
  *IN(4) = *OFF;
  *IN(5) = *OFF;
  // Bildschirm mit Werten füllen
  ExSr SR030;
  // Bildschirm verarbeiten
  ExSr SR045;
  If PIRtCd = '*ENTER';
    // Verbuchen
    MNTFILE_book();
  EndIf;
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Kopieren                                                                                *
//*-----------------------------------------------------------------------------------------*

BegSr SR015;
  // Überschrift für Modus 'Kopieren' anzeigen
  *IN(1) = *OFF;
  *IN(2) = *OFF;
  *IN(3) = *ON;
  *IN(4) = *OFF;
  *IN(5) = *OFF;
  // Bildschirm mit Werten füllen
  ExSr SR030;
  // Bildschirm verarbeiten
  ExSr SR045;
  If PIRtCd = '*ENTER';
    // Verbuchen
    MNTFILE_book();
  EndIf;
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Löschen                                                                                 *
//*-----------------------------------------------------------------------------------------*

BegSr SR020;
  // Überschrift für Modus 'Löschen' anzeigen
  *IN(1) = *OFF;
  *IN(2) = *OFF;
  *IN(3) = *OFF;
  *IN(4) = *ON;
  *IN(5) = *OFF;
  // Bildschirm mit Werten füllen
  ExSr SR030;
  // Bildschirm verarbeiten
  ExSr SR045;
  If PIRtCd = '*ENTER';
    // Bestätigung für Löschung einholen
    ExFmt DSPF020W;
    // Funktionstaste abrufen
    ITFktKey = RtvFktKey(AIDCde);
    // Datensatz löschen
    If ITFktKey = '*ENTER';
      // Verbuchen
      MNTFILE_book();
    Else;
      // Löschung abgebrochen
      PIRtCd = '*F12';
    EndIf;
  EndIf;
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Anzeigen                                                                                *
//*-----------------------------------------------------------------------------------------*

BegSr SR025;
  // Überschrift für Modus 'Anzeigen' anzeigen
  *IN(1) = *OFF;
  *IN(2) = *OFF;
  *IN(3) = *OFF;
  *IN(4) = *OFF;
  *IN(5) = *ON;
  // Bildschirm mit Werten füllen
  ExSr SR030;
  // Bildschirm verarbeiten
  ExSr SR045;
  If PIRtCd = '*ENTER';
    // Verbuchen
    MNTFILE_book();
  EndIf;
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Bildschirm mit Werten füllen                                                            *
//*-----------------------------------------------------------------------------------------*

BegSr SR030;
  MNTFILE_getValues(
    DFPOS   :    // <-- Position
    DFFILE  :    // <-- File
    DFTYPE  :    // <-- Type
    DFCPL   :    // <-- Copy program (Library)
    DFCP    :    // <-- Copy program
    DFVPL   :    // <-- Conversion program (Library)
    DFVP         // <-- Conversion program
  );
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Bildschirm verarbeiten                                                                  *
//*-----------------------------------------------------------------------------------------*

BegSr SR045;
  // Fehlernachricht entfernen
  ITSts = RmvErrMsg('*SAME');
  // Fehlernachrichten-Id. initialisieren
  ITMsgId = *Blanks;
  ITMsgDta = *Blanks;
  ITMsgF = *Blanks;
  // Fehlerbezugszahlen initialisieren
  *IN(80) = *OFF;
  *IN(81) = *OFF;
  *IN(82) = *OFF;
  *IN(83) = *OFF;
  *IN(84) = *OFF;
  *IN(85) = *OFF;
  *IN(86) = *OFF;
  *IN(87) = *OFF;
  *IN(88) = *OFF;
  *IN(89) = *OFF;
  // Positionierung deaktivieren
  *IN(90) = *OFF;
  *IN(91) = *OFF;
  *IN(92) = *OFF;
  *IN(93) = *OFF;
  *IN(94) = *OFF;
  *IN(95) = *OFF;
  *IN(96) = *OFF;
  *IN(97) = *OFF;
  *IN(98) = *OFF;
  *IN(99) = *OFF;
  DoW ITLoop;
    // Fehlernachricht in die Programmnachrichtenwarteschlange schreiben
    If ITMsgId <> *Blanks;
      ExSr SR055;
    EndIf;
    DoU ITFktKey <> '*F1';
      // Satzformate auf den Bildschirm ausgeben
      DFPGMQ = '*';
      Write DSPF999C;
      ExFmt DSPF010;
      // Funktionstaste abrufen
      ITFktKey = RtvFktKey(AIDCde);
      // Funktionstaste : 'F1=Hilfe'
      If ITFktKey = '*F1';
        ExSr SR060;
      EndIf;
    EndDo;
    // Fehlernachricht entfernen
    ITSts = RmvErrMsg('*SAME');
    // Fehlernachrichten-Id. initialisieren
    ITMsgId = *Blanks;
    ITMsgDta = *Blanks;
    ITMsgF = *Blanks;
    // Fehlerbezugszahlen initialisieren
    *IN(80) = *OFF;
    *IN(81) = *OFF;
    *IN(82) = *OFF;
    *IN(83) = *OFF;
    *IN(84) = *OFF;
    *IN(85) = *OFF;
    *IN(86) = *OFF;
    *IN(87) = *OFF;
    *IN(88) = *OFF;
    *IN(89) = *OFF;
    // Positionierung deaktivieren
    *IN(90) = *OFF;
    *IN(91) = *OFF;
    *IN(92) = *OFF;
    *IN(93) = *OFF;
    *IN(94) = *OFF;
    *IN(95) = *OFF;
    *IN(96) = *OFF;
    *IN(97) = *OFF;
    *IN(98) = *OFF;
    *IN(99) = *OFF;
    // Funktionstaste : 'F3=Verlassen'
    If ITFktKey = '*F3';
      PIRtCd = '*F3';
      Leave;
    EndIf;
    // Funktionstaste : 'F12=Abbrechen'
    If ITFktKey = '*F12';
      PIRtCd = '*F12';
      Leave;
    EndIf;
    // Funktionstaste : 'F4=Bedienerführung'
    If ITFktKey = '*F4';
      ExSr SR065;
      If PIRtCd = '*F3';
        Leave;
      Else;
        Iter;
      EndIf;
    EndIf;
    // Funktionstaste : 'Enter'
    If ITFktKey = '*ENTER';
      // Bildschirmfelder auf Fehler überprüfen
      ExSr SR050;
      If ITMsgId <> *Blanks;
        Iter;
      EndIf;
      // Rückkehrcode setzen
      PIRtCd = '*ENTER';
      Leave;
    endIf;
  EndDo;
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Bildschirmfelder auf Fehler überprüfen                                                  *
//*-----------------------------------------------------------------------------------------*

BegSr SR050;
  // Namensfelder linksbündig ausrichten
  If PIMode = MNTFILE_CREATE Or
    PIMode = MNTFILE_COPY Or
    PIMode = MNTFILE_CHANGE;
    DFFILE = %TRIML(DFFILE);
    DFTYPE = %TRIML(DFTYPE);
    DFCP = %TRIML(DFCP);
    DFCPL = %TRIML(DFCPL);
    DFVP = %TRIML(DFVP);
    DFVPL = %TRIML(DFVPL);
  EndIf;
  // *** Fehlerprüfung nur für Modus 'Erstellen', 'Kopieren' und 'Ändern' ***
  If PIMode = MNTFILE_CREATE Or
    PIMode = MNTFILE_COPY Or
    PIMode = MNTFILE_CHANGE;
    MNTFILE_setValues(
      DFPOS   :    // <-- Position
      DFFILE  :    // <-- File
      DFTYPE  :    // <-- Type
      DFCPL   :    // <-- Copy program (Library)
      DFCP    :    // <-- Copy program
      DFVPL   :    // <-- Conversion program (Library)
      DFVP         // <-- Conversion program
    );
  EndIf;
  // Prüfen
  If Not MNTFILE_check(
    ITSUCC : // <-- Erfolg
    ITXFld : // <-- Feld
    ITXMsg   // <-- Nachricht
  );
    // Fehler setzen
    ITMsgId = 'CPF9897';
    ITMsgDta = ITXMsg;
    ITMsgF = 'QCPFMSG';
    Select;
    When (ITXFld = 'POS');
      *IN(87) = *OFF;
      *IN(88) = *OFF;
      *IN(89) = *ON;
    When (ITXFld = 'FILE');
      *IN(87) = *OFF;
      *IN(88) = *ON;
      *IN(89) = *OFF;
    When (ITXFld = 'FILE');
      *IN(87) = *OFF;
      *IN(88) = *ON;
      *IN(89) = *ON;
    When (ITXFld = 'TYPE');
      *IN(87) = *ON;
      *IN(88) = *OFF;
      *IN(89) = *OFF;
    When (ITXFld = 'CP');
      *IN(87) = *ON;
      *IN(88) = *ON;
      *IN(89) = *OFF;
    When (ITXFld = 'CPL');
      *IN(87) = *ON;
      *IN(88) = *ON;
      *IN(89) = *ON;
    When (ITXFld = 'VP');
      *IN(81) = *OFF;
      *IN(82) = *OFF;
      *IN(83) = *ON;
    When (ITXFld = 'VPL');
      *IN(81) = *OFF;
      *IN(82) = *ON;
      *IN(83) = *OFF;
    EndSl;
  EndIf;
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Fehlernachricht in die Programmnachrichtenwarteschlange schreiben                       *
//*-----------------------------------------------------------------------------------------*

BegSr SR055;
  // Bezugszahl für 'Es liegt ein Fehler vor' auf 'Ja' setzen
  *IN80 = *On;
  // Fehlernachricht in die Programmnachrichtenwarteschlange schreiben
  ITSts = SndErrMsg(
   '*SAME' :                                                             // --> Aufrufstapele
   ITMsgId :                                                             // --> Nachrichten-I
   ITMsgF  :                                                             // --> Nachrichtendatei
   '*LIBL' :                                                             // --> Bibliothek Na
   ITMsgDta                                                              // --> Nachrichtenda
  );
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Hilfe                                                                                   *
//*-----------------------------------------------------------------------------------------*

BegSr SR060;
  // Kontextbezogene Hilfe initialisiern
  ITHlp = 'N';
  // Nummer Hilfetextmodul initialisieren
  ITNbr = *Zero;
  // Hilfe für Fehlernachrichtensubdatei angefordert
  If DFRCD010 = 'DSPF999S';
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'CMOREFGP';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'REF/MSGINF';
    ITHlp = 'J';
    ITDsp(1) = ITNbr;
    ITDsp(2) = ITNbr;
  Else;
    // Modul 'GNR'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'FILES';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'GNR';
    // Modul 'JOB'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'JOBS';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'JOB';
    If DFFLD010 = 'DFJOB';
      ITHlp = 'J';
      ITDsp(1) = ITNbr;
      ITDsp(2) = ITNbr;
    EndIf;
    // Modul 'POS'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'FILES';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'POS';
    If DFFLD010 = 'DFPOS';
      ITHlp = 'J';
      ITDsp(1) = ITNbr;
      ITDsp(2) = ITNbr;
    EndIf;
    // Modul 'FILE'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'FILES';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'FILE';
    If DFFLD010 = 'DFFILE';
      ITHlp = 'J';
      ITDsp(1) = ITNbr;
      ITDsp(2) = ITNbr;
    EndIf;
    // Modul 'TYPE'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'FILES';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'TYPE';
    If DFFLD010 = 'DFTYPE';
      ITHlp = 'J';
      ITDsp(1) = ITNbr;
      ITDsp(2) = ITNbr;
    EndIf;
    // Modul 'CP'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'FILES';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'CP';
    If DFFLD010 = 'DFCP';
      ITHlp = 'J';
      ITDsp(1) = ITNbr;
      ITDsp(2) = ITNbr;
    EndIf;
    // Modul 'CPL'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'FILES';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'CPL';
    If DFFLD010 = 'DFCPL';
      ITHlp = 'J';
      ITDsp(1) = ITNbr;
      ITDsp(2) = ITNbr;
    EndIf;
    // Modul 'VP'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'FILES';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'VP';
    If DFFLD010 = 'DFVP';
      ITHlp = 'J';
      ITDsp(1) = ITNbr;
      ITDsp(2) = ITNbr;
    EndIf;
    // Modul 'VPL'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'FILES';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'VPL';
    If DFFLD010 = 'DFVPL';
      ITHlp = 'J';
      ITDsp(1) = ITNbr;
      ITDsp(2) = ITNbr;
    EndIf;
    // Modul 'FKT'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'RF0008GP';
    ITHILP(ITNbr) = '*LIBL';
    Select;
    When PIMode = MNTFILE_CREATE Or
      PIMode = MNTFILE_COPY Or
      PIMode = MNTFILE_CHANGE;
      ITHIMO(ITNbr) = 'FKT/EDT';
    When PIMode = MNTFILE_DISPLAY Or
      PIMode = MNTFILE_DELETE;
      ITHIMO(ITNbr) = 'FKT/DSP';
    EndSl;
    If DFROW010 >= 23 And
      DFROW010 <= 23 And
      DFCOL010 >= 2 And
      DFCOL010 <= 79;
      ITHlp = 'J';
      ITDsp(1) = ITNbr;
      ITDsp(2) = ITNbr;
    EndIf;
  EndIf;
  // Keine kontextbezogene Hilfe ausgewählt
  If ITHlp = 'N';
    ITDsp(1) = 1;
    ITDsp(2) = ITNbr;
  EndIf;
  // Hilfetext anzeigen
  ITLftC(1) = 1;
  ITLftC(2) = 1;
  ITRghC(1) = 24;
  ITRghC(2) = 80;
  ITCsrL(1) = DFROW010;
  ITCsrL(2) = DFCOL010;
  Reset ERRC0100;
  QUHDSPH(
   ITHlpId :                                                             // --> Help identifi
   ITNbr :                                                               // --> Number of hel
   ITDsp :                                                               // --> Help type
   *Blanks :                                                             // --> Full display
   '*NONE' :                                                             // --> Qualified sea
   'N' :                                                                 // --> Display type
   ITLftC :                                                              // --> Upper left co
   ITRghC :                                                              // --> Lower right c
   ITCsrL :                                                              // --> Cursor locati
   ERRC0100                                                              // <-> Error code
  );
  // Cursor auf der letzten Position positionieren
  *IN(90) = *ON;
  *IN(91) = *ON;
  *IN(92) = *OFF;
  *IN(93) = *OFF;
  *IN(94) = *OFF;
  *IN(95) = *OFF;
  *IN(96) = *OFF;
  *IN(97) = *OFF;
  *IN(98) = *OFF;
  *IN(99) = *OFF;
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Bedienerführung                                                                         *
//*-----------------------------------------------------------------------------------------*

BegSr SR065;
  Select;
    // No prompt for the requested field available
  Other;
    ITMsgId = 'ERR0007';
    ITMsgDta = *Blanks;
    ITMsgF = 'RF0008MF';
  EndSl;
  // Cursor auf der letzten Position positionieren
  *IN(90) = *ON;
  *IN(91) = *ON;
  *IN(92) = *OFF;
  *IN(93) = *OFF;
  *IN(94) = *OFF;
  *IN(95) = *OFF;
  *IN(96) = *OFF;
  *IN(97) = *OFF;
  *IN(98) = *OFF;
  *IN(99) = *OFF;
EndSr;
