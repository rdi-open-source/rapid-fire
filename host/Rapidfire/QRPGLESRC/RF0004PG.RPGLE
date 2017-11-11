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
//* Maintain Library List                                                                   *
//*                                                                                         *
//*-----------------------------------------------------------------------------------------*
//*                                                                                         *
//* (01 - 49) Freie Bezugszahlen für sonstige Dinge                                         *
//*                                                                                         *
//* 01      --> Modus : 'Erstellen'                                                         *
//* 02      --> Modus : 'Ändern'                                                            *
//* 04      --> Modus : 'Löschen'                                                           *
//* 05      --> Modus : 'Anzeigen'                                                          *
//* 30      --> Position im Subdateisatz geändert (Abfragen)                                *
//* 31      --> Position im Subdateisatz geändert (Setzen)                                  *
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
//* 14.06.2016 I   TR   I Enabled program for '*DISPLAY' mode.                              *
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

DCL-F RF0004DF Workstn(*EXT) USAGE(*INPUT:*OUTPUT)
      SFile(DSPF010S : ITRRN010) InfDS(FIDS_RF0004DF);
DCL-F JOBS Disk(*EXT) USAGE(*INPUT) KEYED Rename(JOBS : JOBRF);
DCL-F LIBLS Disk(*EXT) USAGE(*UPDATE:*DELETE:*OUTPUT)
      KEYED Rename(LIBLS : LIBLSTRF) Commit;
DCL-F LIBLETYS Disk(*EXT) USAGE(*UPDATE:*DELETE:*OUTPUT)
      KEYED Rename(LIBLETYS : LIBLERF) Commit;

//*-----------------------------------------------------------------------------------------*

// Prozedurenprototyp für Prozedur 'RF0004PG'

/Copy QCPYSRC,RF0004PG

// Prozedurenschnittstelle für Prozedur 'RF0004PG'

DCL-PI RF0004PG;
  PIMode CHAR(10) Const;                                                 // --> Modus
  PIJOB CHAR(10) Const;                                                  // --> Job
  PILIBL CHAR(10);                                                       // <-> Bibliotheksli
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

// Prozedurenprototyp für Prozedur 'ChkVal'

/Copy QCPYSRC,CHKVAL

// Prozedurenprototyp für Prozedur 'QUHDSPH'

/Copy QCPYSRC,QUHDSPH

// Datenstruktur für Format 'ERRC0100' für Fehlercode

/Copy QCPYSRC,ERRC0100

// Programmstatusdatenstruktur

DCL-DS PSDS LEN(429) PSDS;
END-DS;

// Dateiinformationsdatenstruktur für RF0004DF

DCL-DS FIDS_RF0004DF;
  AIDCde CHAR(1) POS(369);
END-DS;

// Datenstruktur für Speicherung der Bibliotheksliste

DCL-DS *N;
  ITLL CHAR(14) Dim(250);
  ITLLSEQ ZONED(4) Overlay(ITLL);                                        // Sequenz
  ITLLLIB CHAR(10) Overlay(ITLL : *Next);                                // Bibliothek
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
DCL-S ITRRN010 ZONED(4);                                                 // (Subdatei) Aktuel
DCL-S ITCnt010 ZONED(4);                                                 // (Subdatei) Letzte
DCL-S ITEOF CHAR(1);                                                     // Dateiende
DCL-S ITEOFS CHAR(1);                                                    // Dateiende Subdate
DCL-S ITEOFP CHAR(1);                                                    // Dateiende Paramet
DCL-S ITSeq Like(X5SEQ);                                                 // Sequenz
DCL-S ITPos ZONED(5);                                                    // Zähler
DCL-S ITChg CHAR(1);                                                     // Subdateisatz geän
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
  // Job initialisieren
  DFJOB = PIJOB;
  Select;
    // Modus : 'Erstellen'
  When PIMode = '*CREATE';
    ExSr SR005;
    If PIRtCd = '*ENTER';
      PILIBL = DFLIBL;
    EndIf;
    // Modus : 'Ändern'
  When PIMode = '*CHANGE';
    ExSr SR010;
    // Modus : 'Löschen'
  When PIMode = '*DELETE';
    ExSr SR015;
    // Modus : 'Anzeigen'
  When PIMode = '*DISPLAY';
    ExSr SR020;
  EndSl;
  // Datensatz entsperren
  Unlock LIBLS;
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
  ExSr SR025;
  // Bildschirm verarbeiten
  ExSr SR060;
  If PIRtCd = '*ENTER';
    // Datensatz initialisieren
    Clear LIBLSTRF;
    X4JOB = PIJOB;
    // Datenbank mit Werten aus dem Bildschirm füllen
    ExSr SR050;
    // Datensatz schreiben
    Write LIBLSTRF;
    // Datenbank mit Bibliotheken füllen
    ExSr SR055;
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
    Chain(E) ( PIJOB : PILIBL ) LIBLSTRF;
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
    ExSr SR060;
    If PIRtCd = '*ENTER';
      // Datenbank mit Werten aus dem Bildschirm füllen
      ExSr SR050;
      // Datensatz aktualisieren
      Update LIBLSTRF;
      // Datenbank mit Bibliotheken füllen
      ExSr SR055;
      // Modifikationen festschreiben
      Commit;
    EndIf;
  EndIf;
EndSr;

//-------------------------------------------------------------------------------------------
// Löschen
//-------------------------------------------------------------------------------------------

BegSr SR015;
  // Überschrift für Modus 'Löschen' anzeigen
  *IN(1) = *OFF;
  *IN(2) = *OFF;
  *IN(3) = *OFF;
  *IN(4) = *ON;
  *IN(5) = *OFF;
  // Datensatz lesen
  DoU Not %Error;
    Chain(E) ( PIJOB : PILIBL ) LIBLSTRF;
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
    ExSr SR060;
    If PIRtCd = '*ENTER';
      // Bestätigung für Löschung einholen
      ExFmt DSPF020W;
      // Funktionstaste abrufen
      ITFktKey = RtvFktKey(AIDCde);                                      // --> AID-Code
      // Datensatz löschen
      If ITFktKey = '*ENTER';
        // Datensatz löschen
        Delete LIBLSTRF;
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

BegSr SR020;
  // Überschrift für Modus 'Anzeigen' anzeigen
  *IN(1) = *OFF;
  *IN(2) = *OFF;
  *IN(3) = *OFF;
  *IN(4) = *OFF;
  *IN(5) = *ON;
  // Datensatz lesen
  Chain(N) ( PIJOB : PILIBL ) LIBLSTRF;
  // Fehler : Der Datensatz konnte nicht gefunden werden
  If Not %Found;
    PIRtCd = '*ERROR';
  Else;
    // Bildschirm mit Werten aus der Datenbank füllen
    ExSr SR035;
    // Bildschirm verarbeiten
    ExSr SR060;
  EndIf;
EndSr;

//-------------------------------------------------------------------------------------------
// Bildschirm mit Standartwerten füllen
//-------------------------------------------------------------------------------------------

BegSr SR025;
  // Einzelwerte mit Standartwerten füllen
  DFLIBL = *Blanks;
  DFDSCR = *Blanks;
  // Bibliotheken mit Standartwerten füllen
  ExSr SR030;
EndSr;

//-------------------------------------------------------------------------------------------
// Bibliotheken mit Standartwerten füllen
//-------------------------------------------------------------------------------------------

BegSr SR030;
  // Inhalt der Subdatei löschen
  *IN60 = *On;
  Write DSPF010C;
  *IN60 = *Off;
  // Fehlerbezugszahlen initialisieren
  *IN(81) = *OFF;
  *IN(82) = *OFF;
  *IN(83) = *OFF;
  *IN(84) = *OFF;
  *IN(85) = *OFF;
  *IN(86) = *OFF;
  *IN(87) = *OFF;
  *IN(88) = *OFF;
  *IN(89) = *OFF;
  FOR ITCnt010 = 1 TO 250;
    // Positionssatz mit Standardwerten füllen
    DFSEQ = ITCnt010 * 10;
    DFLIB = *Blanks;
    // Aktuellen Satz auf letzten Satz setzen
    ITRRN010 = ITCnt010;
    // Subdateisatz schreiben
    Write DSPF010S;
  ENDFOR;
EndSr;

//-------------------------------------------------------------------------------------------
// Bildschirm mit Werten aus der Datenbank füllen
//-------------------------------------------------------------------------------------------

BegSr SR035;
  // Einzelwerte mit Werten aus der Datenbank füllen
  DFLIBL = X4LIBL;
  DFDSCR = X4DSCR;
  // Bibliotheken mit Werten aus der Datenbank füllen
  ExSr SR045;
EndSr;

//-------------------------------------------------------------------------------------------
// Bibliotheken mit Werten aus der Datenbank füllen
//-------------------------------------------------------------------------------------------

BegSr SR045;
  // Inhalt der Subdatei löschen
  *IN60 = *On;
  Write DSPF010C;
  *IN60 = *Off;
  // Fehlerbezugszahlen initialisieren
  *IN(81) = *OFF;
  *IN(82) = *OFF;
  *IN(83) = *OFF;
  *IN(84) = *OFF;
  *IN(85) = *OFF;
  *IN(86) = *OFF;
  *IN(87) = *OFF;
  *IN(88) = *OFF;
  *IN(89) = *OFF;
  // Kz. Dateiende initialisieren
  ITEOF = 'N';
  // Positionsdatei positionieren
  SetLL ( PIJOB : PILIBL ) LIBLERF;
  FOR ITCnt010 = 1 TO 250;
    // Positionsdatei lesen
    If ITEOF = 'N';
      ReadE(N) ( PIJOB : PILIBL ) LIBLERF;
      // Für die restlichen Sätze Standardwerte verwenden
      If %EOF;
        // Kz. Dateiende setzen
        ITEOF = 'J';
        // Satz mit Standardwerten füllen
        DFLIB = *Blanks;
        // Satz mit Werten aus der Positionsdatei füllen
      Else;
        DFLIB = X5LIB;
      EndIf;
    EndIf;
    // Reihenfolge initialisieren
    DFSEQ = ITCnt010 * 10;
    // Aktuellen Satz auf letzten Satz setzen
    ITRRN010 = ITCnt010;
    // Bezugszahl für 'Position im Subdateisatz geändert (Setzen)' auf 'Nein' setzen
    *IN31 = *Off;
    // Subdateisatz schreiben
    Write DSPF010S;
  ENDFOR;
EndSr;

//-------------------------------------------------------------------------------------------
// Datenbank mit Werten aus dem Bildschirm füllen
//-------------------------------------------------------------------------------------------

BegSr SR050;
  // Datenbank mit Einzelwerten füllen
  X4LIBL = DFLIBL;
  X4DSCR = DFDSCR;
EndSr;

//-------------------------------------------------------------------------------------------
// Datenbank mit Bibliotheken füllen
//-------------------------------------------------------------------------------------------

BegSr SR055;
  // Bibliothekslisteneinträge löschen
  If PIMode <> '*CREATE';
    SetLL ( PIJOB : PILIBL ) LIBLERF;
    DoU %EOF;
      DoU Not %Error;
        ReadE(E) ( PIJOB : PILIBL ) LIBLERF;
        If %Error;
          HdlRcdLck(PSDS);
        EndIf;
      EndDo;
      If Not %EOF;
        Delete LIBLERF;
      EndIf;
    EndDo;
  EndIf;
  // Sequenz initialisieren
  ITSeq = *Zero;
  For ITCnt010 = 1 To 250 By 1;
    // Subdatei lesen
    Chain ITCnt010 DSPF010S;
    If %Found And
      DFLIB <> *Blanks;
      // Sequenz erhöhen
      ITSeq = ITSeq + 10;
      // Bibliothekslisteneintrage schreiben
      Clear *All LIBLERF;
      X5JOB = PIJOB;
      X5LIBL = DFLIBL;
      X5SEQ = ITSeq;
      X5LIB = DFLIB;
      Write LIBLERF;
    EndIf;
  EndFor;
EndSr;

//-------------------------------------------------------------------------------------------
// Bildschirm verarbeiten
//-------------------------------------------------------------------------------------------

BegSr SR060;
  // Anzuzeigenden Subdateisatz initialisieren
  DFSET010 = 1;
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
      ExSr SR075;
    EndIf;
    DoU ITFktKey <> '*F1';
      // Satzformate auf den Bildschirm ausgeben
      DFPGMQ = '*';
      Write DSPF999C;
      Write DSPF010F;
      ExFmt DSPF010C;
      // Funktionstaste abrufen
      ITFktKey = RtvFktKey(AIDCde);                                      // --> AID-Code
      // Anzuzeigenden Subdateisatz setzen
      If ITFktKey = '*F1' Or
        ITFktKey = '*F4';
        DFSET010 = DFLST010;
      Else;
        DFSET010 = 1;
      EndIf;
      // Funktionstaste : 'F1=Hilfe'
      If ITFktKey = '*F1';
        ExSr SR080;
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
      ExSr SR085;
      If PIRtCd = '*F3';
        Leave;
      Else;
        Iter;
      EndIf;
    EndIf;
    // Funktionstaste : 'Enter'
    If ITFktKey = '*ENTER';
      // Bildschirmfelder auf Fehler überprüfen
      ExSr SR065;
      If ITMsgId <> *Blanks;
        Iter;
      EndIf;
      // Subdatei neu aufbauen
      If ITChg = 'J';
        ExSr SR070;
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

BegSr SR065;
  // 'Subdateisatz geändert' initialisieren
  ITChg = 'N';
  If PIMode = '*CREATE' Or
    PIMode = '*CHANGE';
    // Namensfelder linksbündig ausrichten (Normalfelder)
    DFLIBL = %TRIML(DFLIBL);
    // Namensfelder linksbündig ausrichten (Subdateifelder)
    FOR ITCnt010 = 1 TO 250;
      // Subdateisatz lesen
      Chain ITCnt010 DSPF010S;
      // Dateiende
      If Not %Found;
        Leave;
      EndIf;
      // Position im Subdateisatz wurde geändert
      If *IN30 = *On;
        ITChg = 'J';
      EndIf;
      // Felder linksbündig ausrichten
      DFLIB = %TrimL(DFLIB);
      // Bezugszahl für 'Position im Subdateisatz geändert (Setzen)' auf 'Nein' setzen
      *IN31 = *Off;
      // Subdateisatz aktualisieren
      Update DSPF010S;
    ENDFOR;
  EndIf;
  FOR DO_X = 1 TO 1;
    // *** Fehlerprüfung nur für Modus 'Erstellen' ***
    If PIMode = '*CREATE';
      // The value in field 'Library list' is not valid
      If ChkVal('*NAME' : 10 : %Addr(DFLIBL)) =
        '*INVALID';
        ITMsgId = 'ERR0003';
        *IN(81) = *OFF;
        *IN(82) = *OFF;
        *IN(83) = *ON;
        Leave;
      EndIf;
      // The library list does already exist
      SetLL ( PIJOB : DFLIBL ) LIBLSTRF;
      If %Equal;
        ITMsgId = 'ERR0009';
        *IN(81) = *OFF;
        *IN(82) = *OFF;
        *IN(83) = *ON;
        Leave;
      EndIf;
    EndIf;
    // *** Fehlerprüfung nur für Modus 'Erstellen' und 'Ändern' ***
    If PIMode = '*CREATE' Or
      PIMode = '*CHANGE';
      // The value in field 'Description' is not valid
      If DFDSCR = *Blanks;
        ITMsgId = 'ERR0010';
        *IN(81) = *ON;
        *IN(82) = *ON;
        *IN(83) = *OFF;
        Leave;
      EndIf;
      // Tabellen initialisieren
      ITLLSEQ(*) = *Hival;
      ITLLLIB(*) = *Blanks;
      ITPos = *Zero;
      // Fehler in der Subdatei verarbeiten
      FOR ITCnt010 = 1 TO 250;
        // Subdateisatz lesen
        Chain ITCnt010 DSPF010S;
        // Dateiende
        If Not %Found;
          Leave;
        EndIf;
        // Fehlerbezugszahlen initialisieren
        *IN(81) = *OFF;
        *IN(82) = *OFF;
        *IN(83) = *OFF;
        *IN(84) = *OFF;
        *IN(85) = *OFF;
        *IN(86) = *OFF;
        *IN(87) = *OFF;
        *IN(88) = *OFF;
        *IN(89) = *OFF;
        // Die Position enthält Werte
        If DFLIB <> *Blanks;
          // The sequenz may not be 9999
          If ITMsgId = *Blanks And
            DFSEQ >= 9999;
            DFSET010 = ITRRN010;
            ITMsgId = 'ERR0001';
            *IN(81) = *ON;
            *IN(82) = *OFF;
            *IN(83) = *ON;
          EndIf;
          // The value in field 'Library' is not valid
          If ITMsgId = *Blanks And
            ChkVal('*NAME' : 10 : %Addr(DFLIB)) =
            '*INVALID';
            DFSET010 = ITRRN010;
            ITMsgId = 'ERR0002';
            *IN(81) = *OFF;
            *IN(82) = *ON;
            *IN(83) = *OFF;
          EndIf;
          // The Library has already been defined on an other position
          If ITMsgId = *Blanks;
            If %Lookup(DFLIB : ITLLLIB) <> *Zero;
              DFSET010 = ITRRN010;
              ITMsgId = 'ERR0007';
              *IN(81) = *ON;
              *IN(82) = *OFF;
              *IN(83) = *OFF;
            EndIf;
          EndIf;
          // Position in Tabelle stellen
          ITPos = ITPos + 1;
          ITLLSEQ(ITPos) = DFSEQ;
          ITLLLIB(ITPos) = DFLIB;
        EndIf;
        // Bezugszahl für 'Position im Subdateisatz geändert (Setzen)' auf 'Nein' setzen
        *IN31 = *Off;
        // Subdateisatz aktualisieren
        Update DSPF010S;
      ENDFOR;
      // Fehler in der Subfile gefunden
      If ITMsgId <> *Blanks;
        Leave;
      EndIf;
    EndIf;
  ENDFOR;
EndSr;

//-------------------------------------------------------------------------------------------
// Subdatei neu aufbauen
//-------------------------------------------------------------------------------------------

BegSr SR070;
  // Tabelle nach der Sequenz sortieren
  SortA ITLLSEQ;
  // Inhalt der Subdatei löschen
  *IN60 = *On;
  Write DSPF010C;
  *IN60 = *Off;
  // Fehlerbezugszahlen initialisieren
  *IN(81) = *OFF;
  *IN(82) = *OFF;
  *IN(83) = *OFF;
  *IN(84) = *OFF;
  *IN(85) = *OFF;
  *IN(86) = *OFF;
  *IN(87) = *OFF;
  *IN(88) = *OFF;
  *IN(89) = *OFF;
  FOR ITCnt010 = 1 TO 250;
    // Satz mit Tabellenwerten füllen
    DFLIB = ITLLLIB(ITCnt010);
    // Reihenfolge initialisieren
    DFSEQ = ITCnt010 * 10;
    // Aktuellen Satz auf letzten Satz setzen
    ITRRN010 = ITCnt010;
    // Bezugszahl für 'Position im Subdateisatz geändert (Setzen)' auf 'Nein' setzen
    *IN31 = *Off;
    // Subdateisatz schreiben
    Write DSPF010S;
  ENDFOR;
EndSr;

//-------------------------------------------------------------------------------------------
// Fehlernachricht in die Programmnachrichtenwarteschlange schreiben
//-------------------------------------------------------------------------------------------

BegSr SR075;
  // Bezugszahl für 'Es liegt ein Fehler vor' auf 'Ja' setzen
  *IN80 = *On;
  // Fehlernachricht in die Programmnachrichtenwarteschlange schreiben
  ITSts = SndErrMsg(
   '*SAME' :                                                             // --> Aufrufstapele
   ITMsgId :                                                             // --> Nachrichten-I
   'RF0004MF' :                                                          // --> Nachrichtenda
   '*LIBL' :                                                             // --> Bibliothek Na
   *Blanks                                                               // --> Nachrichtenda
  );
EndSr;

//-------------------------------------------------------------------------------------------
// Hilfe
//-------------------------------------------------------------------------------------------

BegSr SR080;
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
    ITHIPG(ITNbr) = 'LIBLS';
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
    // Modul 'LIBL'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'LIBLS';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'LIBL';
    If DFFLD010 = 'DFLIBL';
      ITHlp = 'J';
      ITDsp(1) = ITNbr;
      ITDsp(2) = ITNbr;
    EndIf;
    // Modul 'DSCR'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'LIBLS';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'DSCR';
    If DFFLD010 = 'DFDSCR';
      ITHlp = 'J';
      ITDsp(1) = ITNbr;
      ITDsp(2) = ITNbr;
    EndIf;
    // Modul 'SEQ'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'LIBLS';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'SEQ';
    If DFFLD010 = 'DFSEQ';
      ITHlp = 'J';
      ITDsp(1) = ITNbr;
      ITDsp(2) = ITNbr;
    EndIf;
    // Modul 'LIB'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'LIBLS';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'LIB';
    If DFFLD010 = 'DFLIB';
      ITHlp = 'J';
      ITDsp(1) = ITNbr;
      ITDsp(2) = ITNbr;
    EndIf;
    // Modul 'FKT'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'RF0004GP';
    ITHILP(ITNbr) = '*LIBL';
    Select;
    When PIMode = '*CREATE' Or
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

BegSr SR085;
  Select;
    // No prompt for the requested field available
  Other;
    ITMsgId = 'ERR0008';
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
