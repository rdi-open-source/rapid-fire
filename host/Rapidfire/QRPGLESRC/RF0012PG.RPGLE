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
//* Maintain Command                                                                        *
//*                                                                                         *
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
//* 92      --> Der Cursor soll auf das Feld 'Befehl' positioniert werden                   *
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
//* >>PRE-COMPILER<<                                                                        *
//*   >>CRTCMD<< CRTBNDRPG PGM(&LI/&OB) SRCFILE(&SL/&SF) SRCMBR(&SM);                       *
//*   >>IMPORTANT<<                                                                         *
//*     >>PARM<< DFTACTGRP(*NO);                                                            *
//*     >>PARM<< DBGVIEW(&U1);                                                              *
//*     >>PARM<< ACTGRP(&U2);                                                               *
//*     >>PARM<< TGTRLS(&TR);                                                               *
//*     >>PARM<< OPTION(*EVENTF);                                                           *
//*   >>END-IMPORTANT<<                                                                     *
//*   >>EXECUTE<<                                                                           *
//* >>END-PRE-COMPILER<<                                                                    *
//*-----------------------------------------------------------------------------------------*

// Control

CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,');
CTL-OPT DftActGrp(*NO) ActGrp('RAPIDFIRE') BNDDIR('RAPIDFIRE');
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

DCL-F RF0012DF Workstn(*EXT) USAGE(*INPUT:*OUTPUT)
      InfDS(FIDS_RF0012DF);
DCL-F JOBS Disk(*EXT) USAGE(*INPUT) KEYED Rename(JOBS : JOBRF);
DCL-F FILES Disk(*EXT) USAGE(*INPUT) KEYED Rename(FILES : FILERF);
DCL-F COMMANDS Disk(*EXT) USAGE(*UPDATE:*DELETE:*OUTPUT)
      KEYED Rename(COMMANDS : CMDRF) Commit;

//*-----------------------------------------------------------------------------------------*

// Prozedurenprototyp für Prozedur 'RF0012PG'

/Copy QCPYSRC,RF0012PG

// Prozedurenschnittstelle für Prozedur 'RF0012PG'

DCL-PI RF0012PG;
  PIMode CHAR(10) Const;                                                 // --> Modus
  PIJOB CHAR(10) Const;                                                  // --> Job
  PIPOS PACKED(6) Const;                                                 // --> Position
  PITYPE CHAR(10);                                                       // <-> Typ
  PISEQ PACKED(3);                                                       // <-> Sequenz
  PIRtCd CHAR(10);                                                       // <-- Rückkehrcode
END-PI;

//*-----------------------------------------------------------------------------------------*

// Prozedurenprototyp für Prozedur 'RtvFktKey'

/Copy QCPYSRC,RTVFKTKEY

// Prozedurenprototyp für Prozedur 'RmvErrMsg'

/Copy QCPYSRC,RMVERRMSG

// Prozedurenprototyp für Prozedur 'SndErrMsg'

/Copy QCPYSRC,SNDERRMSG

// Prozedurenprototyp für Prozedur 'HdlRcdLck'

/Copy QCPYSRC,HDLRCDLCK

// Prozedurenprototyp für Prozedur 'QUHDSPH'

/Copy QCPYSRC,QUHDSPH

// Prozedurenprototyp für Prozedur 'ChkVal'

/Copy QCPYSRC,CHKVAL

// Prozedurenprototyp für Prozedur 'QCAPCMD'

/Copy QCPYSRC,QCAPCMD

// Datenstruktur für Format 'CPOP0100' für Prozedur 'QCAPCMD'

/Copy QCPYSRC,CPOP0100

// Datenstruktur für Format 'ERRC0100' für Fehlercode

/Copy QCPYSRC,ERRC0100

// Programmstatusdatenstruktur

DCL-DS PSDS LEN(429) PSDS;
END-DS;

// Dateiinformationsdatenstruktur für RF0012DF

DCL-DS FIDS_RF0012DF;
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
DCL-S ITFld CHAR(10);                                                    // Feld
DCL-S ITMsgId CHAR(7);                                                   // Nachrichten-Id.
DCL-S ITDsp INT(10) Dim(2);                                              // Anzeige Hilfetext
DCL-S ITNbr INT(10);                                                     // Anzahl Hilfetextm
DCL-S ITLftC INT(10) Dim(2);                                             // Linke Ecke
DCL-S ITRghC INT(10) Dim(2);                                             // Rechte Ecke
DCL-S ITCsrL INT(10) Dim(2);                                             // Cursorposition
DCL-S ITHlp CHAR(1);                                                     // Kontextbezogene H
DCL-S ITCmdIn CHAR(2048);                                                // Eingehender Befeh
DCL-S ITCmdOut CHAR(2048);                                               // Ausgehender Befeh
DCL-S ITLenOut INT(10);                                                  // Ausgehende Länge
DCL-S DO_X PACKED(7);

//*-----------------------------------------------------------------------------------------*

// Bezugszahl für 'Schlüsselwort SFLEND in Nachrichtensubdatei aktivieren' auf 'Ja' setzen
*IN50 = *On;
// Datensatz lesen
Chain ( PIJOB ) JOBRF;
// Fehler : Der Datensatz konnte nicht gefunden werden
If Not %Found;
  PIRtCd = '*ERROR';
Else;
  // Datensatz lesen
  Chain ( PIJOB : PIPOS ) FILERF;
  // Fehler : Der Datensatz konnte nicht gefunden werden
  If Not %Found;
    PIRtCd = '*ERROR';
  Else;
    // Job initialisieren
    DFJOB = PIJOB;
    // Position initialisieren
    DFPOS = PIPOS;
    Select;
      // Modus : 'Erstellen'
    When PIMode = '*CREATE';
      ExSr SR005;
      If PIRtCd = '*ENTER';
        PITYPE = DFTYPE;
        PISEQ = DFSEQ;
      EndIf;
      // Modus : 'Ändern'
    When PIMode = '*CHANGE';
      ExSr SR010;
      // Modus : 'Kopieren'
    When PIMode = '*COPY';
      ExSr SR015;
      If PIRtCd = '*ENTER';
        PITYPE = DFTYPE;
        PISEQ = DFSEQ;
      EndIf;
      // Modus : 'Löschen'
    When PIMode = '*DELETE';
      ExSr SR020;
      // Modus : 'Anzeigen'
    When PIMode = '*DISPLAY';
      ExSr SR025;
    EndSl;
    // Datensatz entsperren
    Unlock COMMANDS;
  EndIf;
EndIf;
// Programm nicht hauptspeicherresident verlassen
*INLR = *On;

//-----------------------------------------------------------------------
// Erstellen
//-----------------------------------------------------------------------

BegSr SR005;
  // Überschrift für Modus 'Erstellen' anzeigen
  *IN(1) = *ON;
  *IN(2) = *OFF;
  *IN(3) = *OFF;
  *IN(4) = *OFF;
  *IN(5) = *OFF;
  // Bildschirm mit Standartwerten füllen
  ExSr SR030;
  // Bildschirm verarbeiten
  ExSr SR050;
  If PIRtCd = '*ENTER';
    // Datensatz initialisieren
    Clear CMDRF;
    X6JOB = PIJOB;
    X6POS = PIPOS;
    // Datenbank mit Werten aus dem Bildschirm füllen
    ExSr SR045;
    // Datensatz schreiben
    Write CMDRF;
    // Modifikationen festschreiben
    Commit;
  EndIf;
EndSr;

//-------------------------------------------------------------------------------------------
// Ändern
//-------------------------------------------------------------------------------------------

BegSr SR010;
  // Überschrift für Modus 'Ändern' anzeigen
  *IN(1) = *OFF;
  *IN(2) = *ON;
  *IN(3) = *OFF;
  *IN(4) = *OFF;
  *IN(5) = *OFF;
  // Datensatz lesen
  DoU Not %Error;
    Chain(E) ( PIJOB : PIPOS : PITYPE : PISEQ ) CMDRF;
    If %Error;
      HdlRcdLck(PSDS);
    EndIf;
  EndDo;
  // Fehler : Der Datensatz konnte nicht gefunden werden
  If Not %Found;
    PIRtCd = '*ERROR';
  Else;
    // Bildschirm mit Werten aus der Datenbank füllen
    ExSr SR035;
    // Bildschirm verarbeiten
    ExSr SR050;
    If PIRtCd = '*ENTER';
      // Datenbank mit Werten aus dem Bildschirm füllen
      ExSr SR045;
      // Datensatz aktualisieren
      Update CMDRF;
      // Modifikationen festschreiben
      Commit;
    EndIf;
  EndIf;
EndSr;

//-------------------------------------------------------------------------------------------
// Kopieren
//-------------------------------------------------------------------------------------------

BegSr SR015;
  // Überschrift für Modus 'Kopieren' anzeigen
  *IN(1) = *OFF;
  *IN(2) = *OFF;
  *IN(3) = *ON;
  *IN(4) = *OFF;
  *IN(5) = *OFF;
  // Datensatz lesen
  Chain(N) ( PIJOB : PIPOS : PITYPE : PISEQ ) CMDRF;
  // Fehler : Der Datensatz konnte nicht gefunden werden
  If Not %Found;
    PIRtCd = '*ERROR';
  Else;
    // Bildschirm mit Werten aus der Datenbank füllen
    ExSr SR035;
    // Bildschirm verarbeiten
    ExSr SR050;
    If PIRtCd = '*ENTER';
      // Datenbank mit Werten aus dem Bildschirm füllen
      ExSr SR045;
      // Datensatz schreiben
      Write CMDRF;
      // Modifikationen festschreiben
      Commit;
    EndIf;
  EndIf;
EndSr;

//-------------------------------------------------------------------------------------------
// Löschen
//-------------------------------------------------------------------------------------------

BegSr SR020;
  // Überschrift für Modus 'Löschen' anzeigen
  *IN(1) = *OFF;
  *IN(2) = *OFF;
  *IN(3) = *OFF;
  *IN(4) = *ON;
  *IN(5) = *OFF;
  // Datensatz lesen
  DoU Not %Error;
    Chain(E) ( PIJOB : PIPOS : PITYPE : PISEQ ) CMDRF;
    If %Error;
      HdlRcdLck(PSDS);
    EndIf;
  EndDo;
  // Fehler : Der Datensatz konnte nicht gefunden werden
  If Not %Found;
    PIRtCd = '*ERROR';
  Else;
    // Bildschirm mit Werten aus der Datenbank füllen
    ExSr SR035;
    // Bildschirm verarbeiten
    ExSr SR050;
    If PIRtCd = '*ENTER';
      // Bestätigung für Löschung einholen
      ExFmt DSPF020W;
      // Funktionstaste abrufen
      ITFktKey = RtvFktKey(AIDCde);
      // Datensatz löschen
      If ITFktKey = '*ENTER';
        // Datensatz löschen
        Delete CMDRF;
        // Modifikationen festschreiben
        Commit;
      Else;
        // Löschung abgebrochen
        PIRtCd = '*F12';
      EndIf;
    EndIf;
  EndIf;
EndSr;

//-------------------------------------------------------------------------------------------
// Anzeigen
//-------------------------------------------------------------------------------------------

BegSr SR025;
  // Überschrift für Modus 'Anzeigen' anzeigen
  *IN(1) = *OFF;
  *IN(2) = *OFF;
  *IN(3) = *OFF;
  *IN(4) = *OFF;
  *IN(5) = *ON;
  // Datensatz lesen
  Chain(N) ( PIJOB : PIPOS : PITYPE : PISEQ ) CMDRF;
  // Fehler : Der Datensatz konnte nicht gefunden werden
  If Not %Found;
    PIRtCd = '*ERROR';
  Else;
    // Bildschirm mit Werten aus der Datenbank füllen
    ExSr SR035;
    // Bildschirm verarbeiten
    ExSr SR050;
  EndIf;
EndSr;

//-------------------------------------------------------------------------------------------
// Bildschirm mit Standartwerten füllen
//-------------------------------------------------------------------------------------------

BegSr SR030;
  // Einzelwerte mit Standartwerten füllen
  DFTYPE = *Blanks;
  DFSEQ = *Zero;
  DFCMD = *Blanks;
EndSr;

//-------------------------------------------------------------------------------------------
// Bildschirm mit Werten aus der Datenbank füllen
//-------------------------------------------------------------------------------------------

BegSr SR035;
  // Einzelwerte mit Werten aus der Datenbank füllen
  DFTYPE = X6TYPE;
  DFSEQ = X6SEQ;
  DFCMD = X6CMD;
EndSr;

//-------------------------------------------------------------------------------------------
// Datenbank mit Werten aus dem Bildschirm füllen
//-------------------------------------------------------------------------------------------

BegSr SR045;
  // Datenbank mit Einzelwerten füllen
  X6TYPE = DFTYPE;
  X6SEQ = DFSEQ;
  X6CMD = DFCMD;
EndSr;

//-------------------------------------------------------------------------------------------
// Bildschirm verarbeiten
//-------------------------------------------------------------------------------------------

BegSr SR050;
  // Fehlernachricht entfernen
  ITSts = RmvErrMsg('*SAME');
  // Fehlernachrichten-Id. initialisieren
  ITMsgId = *Blanks;
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
      ExSr SR060;
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
        ExSr SR065;
      EndIf;
    EndDo;
    // Fehlernachricht entfernen
    ITSts = RmvErrMsg('*SAME');
    // Fehlernachrichten-Id. initialisieren
    ITMsgId = *Blanks;
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
      ExSr SR070;
      If PIRtCd = '*F3';
        Leave;
      Else;
        Iter;
      EndIf;
    EndIf;
    // Funktionstaste : 'Enter'
    If ITFktKey = '*ENTER';
      // Bildschirmfelder auf Fehler überprüfen
      ExSr SR055;
      If ITMsgId <> *Blanks;
        Iter;
      EndIf;
      // Rückkehrcode setzen
      PIRtCd = '*ENTER';
      Leave;
    endif;
  EndDo;
EndSr;

//-------------------------------------------------------------------------------------------
// Bildschirmfelder auf Fehler überprüfen
//-------------------------------------------------------------------------------------------

BegSr SR055;
  // Namensfelder linksbündig ausrichten
  If PIMode = '*CREATE' Or
    PIMode = '*COPY' Or
    PIMode = '*CHANGE';
    DFTYPE = %TRIML(DFTYPE);
    DFCMD = %TRIML(DFCMD);
  EndIf;
  FOR DO_X = 1 TO 1;
    // *** Fehlerprüfung nur für Modus 'Erstellen' und 'Kopieren' ***
    If PIMode = '*CREATE' Or
      PIMode = '*COPY';
      // The value in field 'Type' is not valid
      If DFTYPE <> '*PRERUN' And
        DFTYPE <> '*COMPILE' And
        DFTYPE <> '*POSTRUN';
        ITMsgId = 'ERR0002';
        *IN(81) = *OFF;
        *IN(82) = *OFF;
        *IN(83) = *ON;
        Leave;
      EndIf;
      // The value in field 'Sequence' is not valid
      If DFSEQ = *Zero;
        ITMsgId = 'ERR0009';
        *IN(81) = *ON;
        *IN(82) = *ON;
        *IN(83) = *ON;
        Leave;
      EndIf;
      // If 'Type' is *COMPILE, then 'Sequence' has to be 5
      If DFTYPE = '*COMPILE' And
        DFSEQ <> 5;
        ITMsgId = 'ERR0006';
        *IN(81) = *ON;
        *IN(82) = *ON;
        *IN(83) = *ON;
        Leave;
      EndIf;
      // Type/Sequence does already exist
      SetLL ( PIJOB : PIPOS : DFTYPE : DFSEQ ) CMDRF;
      If %Equal;
        ITMsgId = 'ERR0001';
        *IN(81) = *ON;
        *IN(82) = *OFF;
        *IN(83) = *ON;
        Leave;
      EndIf;
    EndIf;
    // *** Fehlerprüfung nur für Modus 'Erstellen', 'Kopieren' und 'Ändern' ***
    If PIMode = '*CREATE' Or
      PIMode = '*COPY' Or
      PIMode = '*CHANGE';
      // The value in field 'Command' is not valid
      If DFCMD = *Blanks;
        ITMsgId = 'ERR0019';
        *IN(81) = *OFF;
        *IN(82) = *ON;
        *IN(83) = *OFF;
        Leave;
      EndIf;
    EndIf;
  ENDFOR;
EndSr;

//-------------------------------------------------------------------------------------------
// Fehlernachricht in die Programmnachrichtenwarteschlange schreiben
//-------------------------------------------------------------------------------------------

BegSr SR060;
  // Bezugszahl für 'Es liegt ein Fehler vor' auf 'Ja' setzen
  *IN80 = *On;
  // Fehlernachricht in die Programmnachrichtenwarteschlange schreiben
  ITSts = SndErrMsg(
   '*SAME' :                                                             // --> Aufrufstapele
   ITMsgId :                                                             // --> Nachrichten-I
   'RF0012MF' :                                                          // --> Nachrichtenda
   '*LIBL' :                                                             // --> Bibliothek Na
   *Blanks                                                               // --> Nachrichtenda
  );
EndSr;

//-------------------------------------------------------------------------------------------
// Hilfe
//-------------------------------------------------------------------------------------------

BegSr SR065;
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
    ITHIPG(ITNbr) = 'COMMANDS';
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
    // Modul 'TYPE'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'COMMANDS';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'TYPE';
    If DFFLD010 = 'DFTYPE';
      ITHlp = 'J';
      ITDsp(1) = ITNbr;
      ITDsp(2) = ITNbr;
    EndIf;
    // Modul 'SEQ'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'COMMANDS';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'SEQ';
    If DFFLD010 = 'DFSEQ';
      ITHlp = 'J';
      ITDsp(1) = ITNbr;
      ITDsp(2) = ITNbr;
    EndIf;
    // Modul 'CMD'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'COMMANDS';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'CMD';
    If DFFLD010 = 'DFCMD';
      ITHlp = 'J';
      ITDsp(1) = ITNbr;
      ITDsp(2) = ITNbr;
    EndIf;
    // Modul 'FKT'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'RF0012GP';
    ITHILP(ITNbr) = '*LIBL';
    Select;
    When PIMode = '*CREATE' Or
      PIMode = '*COPY' Or
      PIMode = '*CHANGE';
      ITHIMO(ITNbr) = 'FKT/EDT';
    When PIMode = '*DISPLAY' Or
      PIMode = '*DELETE';
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

//-------------------------------------------------------------------------------------------
// Bedienerführung
//-------------------------------------------------------------------------------------------

BegSr SR070;
  Select;
    // Bedienerführung für das Feld 'Befehl'
  When DFFLD010 = 'DFCMD';
    // Befehl initialisieren
    ITCmdIn = DFCMD;
    // Befehl verarbeiten
    CPOP0100.Type_Of_Command_Processing = 8;                             // --> Type of comma
    CPOP0100.DBCS_Data_Handling = '0';                                   // --> DBCS data han
    CPOP0100.Prompter_Action = '1';                                      // --> Prompter acti
    CPOP0100.Command_String_Syntax = '0';                                // --> Command strin
    CPOP0100.Message_Retrieve_Key = *Blanks;                             // --> Message retri
    ITCmdOut = *Blanks;
    Reset ERRC0100;
    QCAPCMD(
     ITCmdIn :                                                           // --> Source comman
     %Size(ITCmdIn) :                                                    // --> Length of sou
     CPOP0100 :                                                          // --> Options contr
     %Size(CPOP0100) :                                                   // --> Options contr
     'CPOP0100' :                                                        // --> Options contr
     ITCmdOut :                                                          // <-- Changed comma
     %Size(ITCmdOut) :                                                   // --> Length availa
     ITLenOut :                                                          // <-- Length availa
     ERRC0100                                                            // <-> Error code
    );
    Select;
      // *** Der Befehl wurde mit F3 abgebrochen
    When ERRC0100.Exeption_Id = 'CPF6801' And
      %SubSt(ERRC0100.Exeption_Data:1:3) = 'F3';
      PIRtCd = '*F3';
      // *** Der Befehl wurde mit F12 abgebrochen
    When ERRC0100.Exeption_Id = 'CPF6801' And
      %SubSt(ERRC0100.Exeption_Data:1:3) = 'F12';
      PIRtCd = '*F12';
      // *** Der Befehl oder ein oder mehrere Befehlsparameter sind fehlerhaft
    When ERRC0100.Exeption_Id <> *Blanks;
      PIRtCd = '*ERROR';
      ITMsgId = 'ERR0015';
      // *** Der Befehl ist in Ordnung
    Other;
      DFCMD = %SubSt(ITCmdOut : 1 : ITLenOut);
      PIRtCd = '*OK';
    EndSl;
    // Es ist keine Bedienerführung für das angeforderte Feld vorhanden
  Other;
    ITMsgId = 'ERR0004';
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
