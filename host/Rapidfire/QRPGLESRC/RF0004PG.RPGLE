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
//* 14.06.2016 I   TR   I Enabled program for '*DISPLAY' mode.                              *
//*------------+--------+-------------------------------------------------------------------*

// Control

CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,');
CTL-OPT DftActGrp(*NO) ActGrp('RAPIDFIRE') BNDDIR('RAPIDFIRE');
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

DCL-F RF0004DF Workstn(*EXT) USAGE(*INPUT:*OUTPUT)
      SFile(DSPF010S : ITRRN010) InfDS(FIDS_RF0004DF);

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

// Prozedurenprototyp für Prozedur 'QUHDSPH'

/Copy QCPYSRC,QUHDSPH

// Datenstruktur für Format 'ERRC0100' für Fehlercode

/Copy QCPYSRC,ERRC0100

// Prozedurenprototyp für Prozedur 'MNTLIBL'

/Copy QCPYSRC,MNTLIBL

// Dateiinformationsdatenstruktur für RF0004DF

DCL-DS FIDS_RF0004DF;
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
DCL-S ITMsgDta CHAR(256);                                                // Nachrichtendaten
DCL-S ITMsgF CHAR(10);                                                   // Nachrichtendatei
DCL-S ITDsp INT(10) Dim(2);                                              // Anzeige Hilfetext
DCL-S ITNbr INT(10);                                                     // Anzahl Hilfetextm
DCL-S ITLftC INT(10) Dim(2);                                             // Linke Ecke
DCL-S ITRghC INT(10) Dim(2);                                             // Rechte Ecke
DCL-S ITCsrL INT(10) Dim(2);                                             // Cursorposition
DCL-S ITHlp CHAR(1);                                                     // Kontextbezogene H
DCL-S ITXFld CHAR(10);                                                   // Feld
DCL-S ITXRcd PACKED(4 : 0);                                              // Satz
DCL-S ITXMsg CHAR(256);                                                  // Nachricht
DCL-S ITSUCC CHAR(1);                                                    // Erfolg
DCL-S ITRRN010 ZONED(4);                                                 // (Subdatei) Aktuel
DCL-S ITCnt010 ZONED(4);                                                 // (Subdatei) Letzte
DCL-S ITChg CHAR(1);                                                     // Subdateisatz geän
DCL-S ITLLSEQ ZONED(4 : 0) Dim(250); // Sequenz
DCL-S ITLLLIB CHAR(10) Dim(250);      // Bibliothek

//*-----------------------------------------------------------------------------------------*

// Bezugszahl für 'Schlüsselwort SFLEND in Nachrichtensubdatei aktivieren' auf 'Ja' setzen
*IN50 = *On;
// Dateien öffnen
MNTLIBL_openFiles();
// Initialisieren
If MNTLIBL_initialize(
  PIMode : // --> Mode
  PIJOB  : // --> Job
  PILIBL : // --> Library list
  ITSUCC : // <-- Success
  ITXMsg   // <-- Message
);
  // Job initialisieren
  DFJOB = PIJOB;
  Select;
    // Modus : 'Erstellen'
  When PIMode = MNTLIBL_CREATE;
    ExSr SR005;
    If PIRtCd = '*ENTER';
      PILIBL = DFLIBL;
    EndIf;
    // Modus : 'Ändern'
  When PIMode = MNTLIBL_CHANGE;
    ExSr SR010;
    // Modus : 'Kopieren'
  When PIMode = MNTLIBL_COPY;
    ExSr SR013;
    If PIRtCd = '*ENTER';
      PILIBL = DFLIBL;
    EndIf;
    // Modus : 'Löschen'
  When PIMode = MNTLIBL_DELETE;
    ExSr SR015;
    // Modus : 'Anzeigen'
  When PIMode = MNTLIBL_DISPLAY;
    ExSr SR020;
  EndSl;
EndIf;
// Dateien schließen
MNTLIBL_closeFiles();
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
  ExSr SR025;
  // Bildschirm verarbeiten
  ExSr SR060;
  If PIRtCd = '*ENTER';
    // Verbuchen
    MNTLIBL_book();
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
  ExSr SR025;
  // Bildschirm verarbeiten
  ExSr SR060;
  If PIRtCd = '*ENTER';
    // Verbuchen
    MNTLIBL_book();
  EndIf;
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Kopieren                                                                                *
//*-----------------------------------------------------------------------------------------*

BegSr SR013;
  // Überschrift für Modus 'Kopieren' anzeigen
  *IN(1) = *OFF;
  *IN(2) = *OFF;
  *IN(3) = *ON;
  *IN(4) = *OFF;
  *IN(5) = *OFF;
  // Bildschirm mit Werten füllen
  ExSr SR025;
  // Bildschirm verarbeiten
  ExSr SR060;
  If PIRtCd = '*ENTER';
    // Verbuchen
    MNTLIBL_book();
  EndIf;
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Löschen                                                                                 *
//*-----------------------------------------------------------------------------------------*

BegSr SR015;
  // Überschrift für Modus 'Löschen' anzeigen
  *IN(1) = *OFF;
  *IN(2) = *OFF;
  *IN(3) = *OFF;
  *IN(4) = *ON;
  *IN(5) = *OFF;
  // Bildschirm mit Werten füllen
  ExSr SR025;
  // Bildschirm verarbeiten
  ExSr SR060;
  If PIRtCd = '*ENTER';
    // Bestätigung für Löschung einholen
    ExFmt DSPF020W;
    // Funktionstaste abrufen
    ITFktKey = RtvFktKey(AIDCde);                                      // --> AID-Code
    // Datensatz löschen
    If ITFktKey = '*ENTER';
      // Verbuchen
      MNTLIBL_book();
    Else;
      // Löschung abgebrochen
      PIRtCd = '*F12';
    EndIf;
  EndIf;
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Anzeigen                                                                                *
//*-----------------------------------------------------------------------------------------*

BegSr SR020;
  // Überschrift für Modus 'Anzeigen' anzeigen
  *IN(1) = *OFF;
  *IN(2) = *OFF;
  *IN(3) = *OFF;
  *IN(4) = *OFF;
  *IN(5) = *ON;
  // Bildschirm mit Werten füllen
  ExSr SR025;
  // Bildschirm verarbeiten
  ExSr SR060;
  If PIRtCd = '*ENTER';
    // Verbuchen
    MNTLIBL_book();
  EndIf;
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Bildschirm mit Werten füllen                                                            *
//*-----------------------------------------------------------------------------------------*

BegSr SR025;
  MNTLIBL_getValues(
    DFLIBL :  // <-- Library list
    DFDSCR :  // <-- Description
    ITLLSEQ : // <-- Sequence
    ITLLLIB   // <-- Library
  );
  // Subdatei mit Werten füllen
  ExSr SR030;
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Subdatei mit Werten füllen                                                              *
//*-----------------------------------------------------------------------------------------*

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
    DFSEQ = ITLLSEQ(ITCnt010);
    DFLIB = ITLLLIB(ITCnt010);
    // Aktuellen Satz auf letzten Satz setzen
    ITRRN010 = ITCnt010;
    // Subdateisatz schreiben
    Write DSPF010S;
  ENDFOR;
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Bildschirm verarbeiten                                                                  *
//*-----------------------------------------------------------------------------------------*

BegSr SR060;
  // Anzuzeigenden Subdateisatz initialisieren
  DFSET010 = 1;
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

//*-----------------------------------------------------------------------------------------*
//* Bildschirmfelder auf Fehler überprüfen                                                  *
//*-----------------------------------------------------------------------------------------*

BegSr SR065;
  // 'Subdateisatz geändert' initialisieren
  ITChg = 'N';
  // Tabellen initialisieren
  ITLLSEQ(*) = *Zero;
  ITLLLIB(*) = *Blanks;
  If PIMode = MNTLIBL_CREATE Or
    PIMode = MNTLIBL_COPY Or
    PIMode = MNTLIBL_CHANGE;
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
      // Tabellen initialisieren
      ITLLSEQ(ITCnt010) = DFSEQ;
      ITLLLIB(ITCnt010) = DFLIB;
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
  // *** Fehlerprüfung nur für Modus 'Erstellen', 'Kopieren' und 'Ändern' ***
  If PIMode = MNTLIBL_CREATE Or
    PIMode = MNTLIBL_COPY Or
    PIMode = MNTLIBL_CHANGE;
    MNTLIBL_setValues(
      DFLIBL :  // --> Library list
      DFDSCR :  // --> Description
      ITLLSEQ : // --> Sequence
      ITLLLIB   // --> Library
    );
  EndIf;
  // Prüfen
  If Not MNTLIBL_check(
    ITSUCC : // <-- Erfolg
    ITXFld : // <-- Feld
    ITXRcd : // <-- Satz
    ITXMsg   // <-- Nachricht
  );
    // Fehler setzen
    ITMsgId = 'CPF9897';
    ITMsgDta = ITXMsg;
    ITMsgF = 'QCPFMSG';
    select;
    when (ITXFld = 'LIBL');
      *IN(81) = *OFF;
      *IN(82) = *OFF;
      *IN(83) = *ON;
    when (ITXFld = 'DSCR');
      *IN(81) = *ON;
      *IN(82) = *ON;
      *IN(83) = *OFF;
    when (ITXFld = 'SEQ' or
          ITXFld = 'LIB' or
          ITXFld = 'DUP');
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
        // Die Position ist fehlerhaft
        If ITCnt010 = ITXRcd;
          Select;
          When (ITXFld = 'SEQ');
            DFSET010 = ITRRN010;
            *IN(81) = *ON;
            *IN(82) = *OFF;
            *IN(83) = *ON;
          When (ITXFld = 'LIB');
            DFSET010 = ITRRN010;
            *IN(81) = *OFF;
            *IN(82) = *ON;
            *IN(83) = *OFF;
          When (ITXFld = 'DUP');
            DFSET010 = ITRRN010;
            *IN(81) = *ON;
            *IN(82) = *OFF;
            *IN(83) = *OFF;
          EndSl;
        EndIf;
        // Bezugszahl für 'Position im Subdateisatz geändert (Setzen)' auf 'Nein' setzen
        *IN31 = *Off;
        // Subdateisatz aktualisieren
        Update DSPF010S;
      ENDFOR;
    endsl;
  endif;
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Subdatei neu aufbauen                                                                   *
//*-----------------------------------------------------------------------------------------*

BegSr SR070;
  MNTLIBL_getLibraryList(
    ITLLSEQ : // <-- Sequence
    ITLLLIB   // <-- Library
  );
  // Subdatei mit Werten füllen
  ExSr SR030;
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Fehlernachricht in die Programmnachrichtenwarteschlange schreiben                       *
//*-----------------------------------------------------------------------------------------*

BegSr SR075;
  // Bezugszahl für 'Es liegt ein Fehler vor' auf 'Ja' setzen
  *IN80 = *On;
  // Fehlernachricht in die Programmnachrichtenwarteschlange schreiben
  ITSts = SndErrMsg(
   '*SAME' :                                                             // --> Aufrufstapele
   ITMsgId :                                                             // --> Nachrichten-I
   ITMsgF  :                                                             // --> Nachrichtenda
   '*LIBL' :                                                             // --> Bibliothek Na
   ITMsgDta                                                              // --> Nachrichtenda
  );
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Hilfe                                                                                   *
//*-----------------------------------------------------------------------------------------*

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
    When PIMode = MNTLIBL_CREATE Or
      PIMode = MNTLIBL_COPY Or
      PIMode = MNTLIBL_CHANGE;
      ITHIMO(ITNbr) = 'FKT/EDT';
    When PIMode = MNTLIBL_DISPLAY Or
      PIMode = MNTLIBL_DELETE;
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

BegSr SR085;
  Select;
    // No prompt for the requested field available
  Other;
    ITMsgId = 'ERR0008';
    ITMsgDta = *Blanks;
    ITMsgF = 'RF0004MF';
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
