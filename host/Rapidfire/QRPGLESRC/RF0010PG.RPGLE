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
//* Maintain Conversion                                                                     *
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

DCL-F RF0010DF Workstn(*EXT) USAGE(*INPUT:*OUTPUT)
      InfDS(FIDS_RF0010DF);
DCL-F JOBS Disk(*EXT) USAGE(*INPUT) KEYED Rename(JOBS : JOBRF);
DCL-F FILES Disk(*EXT) USAGE(*INPUT) KEYED Rename(FILES : FILERF);
DCL-F CNVS Disk(*EXT) USAGE(*UPDATE:*DELETE:*OUTPUT)
      KEYED Rename(CNVS : CNVRF) Commit;

//*-----------------------------------------------------------------------------------------*

// Prozedurenprototyp für Prozedur 'RF0010PG'

/Copy QCPYSRC,RF0010PG

// Prozedurenschnittstelle für Prozedur 'RF0010PG'

DCL-PI RF0010PG;
  PIMode CHAR(10) Const;                                                 // --> Modus
  PIJOB CHAR(10) Const;                                                  // --> Job
  PIPOS PACKED(6) Const;                                                 // --> Position
  PIFTC CHAR(10);                                                        // <-> Zu konvertier
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

// Datenstruktur für Format 'ERRC0100' für Fehlercode

/Copy QCPYSRC,ERRC0100

// Programmstatusdatenstruktur

DCL-DS PSDS LEN(429) PSDS;
END-DS;

// Dateiinformationsdatenstruktur für RF0010DF

DCL-DS FIDS_RF0010DF;
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
DCL-S ITDsp INT(10) Dim(2);                                              // Anzeige Hilfetext
DCL-S ITNbr INT(10);                                                     // Anzahl Hilfetextm
DCL-S ITLftC INT(10) Dim(2);                                             // Linke Ecke
DCL-S ITRghC INT(10) Dim(2);                                             // Rechte Ecke
DCL-S ITCsrL INT(10) Dim(2);                                             // Cursorposition
DCL-S ITHlp CHAR(1);                                                     // Kontextbezogene H
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
        PIFTC = DFFTC;
      EndIf;
      // Modus : 'Ändern'
    When PIMode = '*CHANGE';
      ExSr SR010;
      // Modus : 'Kopieren'
    When PIMode = '*COPY';
      ExSr SR015;
      If PIRtCd = '*ENTER';
        PIFTC = DFFTC;
      EndIf;
      // Modus : 'Löschen'
    When PIMode = '*DELETE';
      ExSr SR020;
      // Modus : 'Anzeigen'
    When PIMode = '*DISPLAY';
      ExSr SR025;
    EndSl;
    // Datensatz entsperren
    Unlock CNVS;
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
  ExSr SR045;
  If PIRtCd = '*ENTER';
    // Datensatz initialisieren
    Clear CNVRF;
    XBJOB = PIJOB;
    XBPOS = PIPOS;
    // Datenbank mit Werten aus dem Bildschirm füllen
    ExSr SR040;
    // Datensatz schreiben
    Write CNVRF;
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
    Chain(E) ( PIJOB : PIPOS : PIFTC ) CNVRF;
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
    ExSr SR045;
    If PIRtCd = '*ENTER';
      // Datenbank mit Werten aus dem Bildschirm füllen
      ExSr SR040;
      // Datensatz aktualisieren
      Update CNVRF;
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
  Chain(N) ( PIJOB : PIPOS : PIFTC ) CNVRF;
  // Fehler : Der Datensatz konnte nicht gefunden werden
  If Not %Found;
    PIRtCd = '*ERROR';
  Else;
    // Bildschirm mit Werten aus der Datenbank füllen
    ExSr SR035;
    // Bildschirm verarbeiten
    ExSr SR045;
    If PIRtCd = '*ENTER';
      // Datenbank mit Werten aus dem Bildschirm füllen
      ExSr SR040;
      // Datensatz schreiben
      Write CNVRF;
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
    Chain(E) ( PIJOB : PIPOS : PIFTC ) CNVRF;
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
    ExSr SR045;
    If PIRtCd = '*ENTER';
      // Bestätigung für Löschung einholen
      ExFmt DSPF020W;
      // Funktionstaste abrufen
      ITFktKey = RtvFktKey(AIDCde);
      // Datensatz löschen
      If ITFktKey = '*ENTER';
        // Datensatz löschen
        Delete CNVRF;
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
  Chain(N) ( PIJOB : PIPOS : PIFTC ) CNVRF;
  // Fehler : Der Datensatz konnte nicht gefunden werden
  If Not %Found;
    PIRtCd = '*ERROR';
  Else;
    // Bildschirm mit Werten aus der Datenbank füllen
    ExSr SR035;
    // Bildschirm verarbeiten
    ExSr SR045;
  EndIf;
EndSr;

//-------------------------------------------------------------------------------------------
// Bildschirm mit Standartwerten füllen
//-------------------------------------------------------------------------------------------

BegSr SR030;
  DFFTC = *Blanks;
  DFRFT = *Blanks;
  DFSTM1 = *Blanks;
  DFSTM2 = *Blanks;
  DFSTM3 = *Blanks;
  DFSTM4 = *Blanks;
  DFSTM5 = *Blanks;
  DFSTM6 = *Blanks;
EndSr;

//-------------------------------------------------------------------------------------------
// Bildschirm mit Werten aus der Datenbank füllen
//-------------------------------------------------------------------------------------------

BegSr SR035;
  DFFTC = XBFTC;
  DFRFT = XBRFT;
  DFSTM1 = XBSTM1;
  DFSTM2 = XBSTM2;
  DFSTM3 = XBSTM3;
  DFSTM4 = XBSTM4;
  DFSTM5 = XBSTM5;
  DFSTM6 = XBSTM6;
EndSr;

//-------------------------------------------------------------------------------------------
// Datenbank mit Werten aus dem Bildschirm füllen
//-------------------------------------------------------------------------------------------

BegSr SR040;
  XBFTC = DFFTC;
  XBRFT = DFRFT;
  XBSTM1 = DFSTM1;
  XBSTM2 = DFSTM2;
  XBSTM3 = DFSTM3;
  XBSTM4 = DFSTM4;
  XBSTM5 = DFSTM5;
  XBSTM6 = DFSTM6;
EndSr;

//-------------------------------------------------------------------------------------------
// Bildschirm verarbeiten
//-------------------------------------------------------------------------------------------

BegSr SR045;
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

//-------------------------------------------------------------------------------------------
// Bildschirmfelder auf Fehler überprüfen
//-------------------------------------------------------------------------------------------

BegSr SR050;
  // Namensfelder linksbündig ausrichten
  If PIMode = '*CREATE' Or
    PIMode = '*COPY' Or
    PIMode = '*CHANGE';
    DFFTC = %TRIML(DFFTC);
    DFRFT = %TRIML(DFRFT);
  EndIf;
  FOR DO_X = 1 TO 1;
    // *** Fehlerprüfung nur für Modus 'Erstellen' und 'Kopieren' ***
    If PIMode = '*CREATE' Or
      PIMode = '*COPY';
      // The value in field 'Field to convert' is not valid
      If ChkVal('*NAME' : 10 : %Addr(DFFTC)) =
        '*INVALID';
        ITMsgId = 'ERR0002';
        *IN(87) = *OFF;
        *IN(88) = *OFF;
        *IN(89) = *ON;
        Leave;
      EndIf;
      // The field to convert does already exist
      SetLL ( PIJOB : PIPOS : DFFTC ) CNVRF;
      If %Equal;
        ITMsgId = 'ERR0001';
        *IN(87) = *OFF;
        *IN(88) = *OFF;
        *IN(89) = *ON;
        Leave;
      EndIf;
    EndIf;
    // *** Fehlerprüfung nur für Modus 'Erstellen', 'Kopieren' und 'Ändern' ***
    If PIMode = '*CREATE' Or
      PIMode = '*COPY' Or
      PIMode = '*CHANGE';
      // The value in field 'Rename field in old file to' is not valid
      If DFRFT <> '*NONE' And
        ChkVal('*NAME' : 10 : %Addr(DFRFT)) =
        '*INVALID';
        ITMsgId = 'ERR0003';
        *IN(87) = *ON;
        *IN(88) = *OFF;
        *IN(89) = *ON;
        Leave;
      EndIf;
      // Both fields have to be different
      If DFRFT <> '*NONE' And
        (DFFTC = DFRFT);
        ITMsgId = 'ERR0005';
        *IN(87) = *OFF;
        *IN(88) = *OFF;
        *IN(89) = *OFF;
        Leave;
      EndIf;
      // The value in field 'Statement' is not valid
      If DFSTM1 = *Blanks And
        DFSTM2 = *Blanks And
        DFSTM3 = *Blanks And
        DFSTM4 = *Blanks And
        DFSTM5 = *Blanks And
        DFSTM6 = *Blanks;
        ITMsgId = 'ERR0006';
        *IN(87) = *OFF;
        *IN(88) = *ON;
        *IN(89) = *ON;
        Leave;
      EndIf;
    EndIf;
  ENDFOR;
EndSr;

//-------------------------------------------------------------------------------------------
// Fehlernachricht in die Programmnachrichtenwarteschlange schreiben
//-------------------------------------------------------------------------------------------

BegSr SR055;
  // Bezugszahl für 'Es liegt ein Fehler vor' auf 'Ja' setzen
  *IN80 = *On;
  // Fehlernachricht in die Programmnachrichtenwarteschlange schreiben
  ITSts = SndErrMsg(
   '*SAME' :                                                             // --> Aufrufstapele
   ITMsgId :                                                             // --> Nachrichten-I
   'RF0010MF' :                                                          // --> Nachrichtenda
   '*LIBL' :                                                             // --> Bibliothek Na
   *Blanks                                                               // --> Nachrichtenda
  );
EndSr;

//-------------------------------------------------------------------------------------------
// Hilfe
//-------------------------------------------------------------------------------------------

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
    ITHIPG(ITNbr) = 'CNVS';
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
    // Modul 'FTC'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'CNVS';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'FTC';
    If DFFLD010 = 'DFFTC';
      ITHlp = 'J';
      ITDsp(1) = ITNbr;
      ITDsp(2) = ITNbr;
    EndIf;
    // Modul 'RFT'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'CNVS';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'RFT';
    If DFFLD010 = 'DFRFT';
      ITHlp = 'J';
      ITDsp(1) = ITNbr;
      ITDsp(2) = ITNbr;
    EndIf;
    // Modul 'STM'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'CNVS';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'STM';
    If DFFLD010 = 'DFSTM1' Or
      DFFLD010 = 'DFSTM2' Or
      DFFLD010 = 'DFSTM3' Or
      DFFLD010 = 'DFSTM4' Or
      DFFLD010 = 'DFSTM5' Or
      DFFLD010 = 'DFSTM6';
      ITHlp = 'J';
      ITDsp(1) = ITNbr;
      ITDsp(2) = ITNbr;
    EndIf;
    // Modul 'FKT'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'RF0010GP';
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

BegSr SR065;
  Select;
    // No prompt for the requested field available
  Other;
    ITMsgId = 'ERR0007';
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
