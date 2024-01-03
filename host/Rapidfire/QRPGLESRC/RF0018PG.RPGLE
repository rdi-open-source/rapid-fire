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
//* Generate Copy Program                                                                   *
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
//* PIRtCd     / *F3        --> Der Benutzer hat die Taste F3 gedrückt                      *
//*              *F12       --> Der Benutzer hat die Taste F12 gedrückt                     *
//*              *ENTER     --> Der Benutzer hat die Taste ENTER gedrückt                   *
//*                                                                                         *
//* Aufruf von der Befehlszeile                                                             *
//* ===========================                                                             *
//*                                                                                         *
//* CALL PGM(RF0018PG) PARM('DIV6.B' X'0000001F' 'DIV6' '')                                 *
//*                                                                                         *
//*-----------------------------------------------------------------------------------------*
//*  >>PRE-COMPILER<<                                                                       *
//*    >>CRTCMD<< CRTBNDRPG PGM(&OL/&OB) SRCFILE(&SL/&SF) SRCMBR(&SM);                      *
//*    >>IMPORTANT<<                                                                        *
//*        >>PARM<<  OPTION(*XREF *EVENTF);                                                 *
//*    >>END-IMPORTANT<<                                                                    *
//*    >>EXECUTE<<                                                                          *
//*  >>END-PRE-COMPILER<<                                                                   *
//*------------+--------+-------------------------------------------------------------------*
//*   Datum    I Progr. I Beschreibung                                                      *
//*------------+--------+-------------------------------------------------------------------*
//* 24.03.2016 I   FH   I Programm erstellt                                                 *
//*------------+--------+-------------------------------------------------------------------*
//* 14.07.2016 I   TR   I Opened fields 'Library' and 'Shadow library' to let the user      *
//*            I        I specify the data and shadow libraries, when either of them does   *
//*            I        I does not yet exist, as specified by the area.                     *
//*------------+--------+-------------------------------------------------------------------*

// Control

CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,');
CTL-OPT DftActGrp(*NO) ActGrp('RAPIDFIRE') BNDDIR('RAPIDFIRE');
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

DCL-F RF0018DF Workstn(*EXT) USAGE(*INPUT:*OUTPUT)
      InfDS(FIDS_RF0018DF);
DCL-F FILES Disk(*EXT) USAGE(*INPUT) KEYED Rename(FILES : FILESF);
DCL-F AREAS Disk(*EXT) USAGE(*INPUT) KEYED Rename(AREAS : AREARF);
DCL-F LIBRARIES Disk(*EXT) USAGE(*INPUT)
      KEYED Rename(LIBRARIES : LIBRARYF);

//*-----------------------------------------------------------------------------------------*

// Prozedurenprototyp für Prozedur 'RF0018PG'

/Copy QCPYSRC,RF0018PG

// Prozedurenschnittstelle für Prozedur 'RF0008PG'

DCL-PI RF0018PG;
  PIJOB CHAR(10) Const;                                                  // --> Job
  PIPOS PACKED(6) Const;                                                 // --> Position
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

// Prozedurenprototyp für Prozedur 'RF0016PG'

/Copy QCPYSRC,RF0016PG

// Datenstruktur für Format 'ERRC0100' für Fehlercode

/Copy QCPYSRC,ERRC0100

// Prozedurenprototyp für Prozedur 'GNRCPYPGM'

/Copy QCPYSRC,GNRCPYPGM

// Dateiinformationsdatenstruktur für RF0018DF

DCL-DS FIDS_RF0018DF;
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
DCL-S ITAra CHAR(10);                                                    // Prompt: Area
DCL-S ITXFld CHAR(10);                                                   // Feld
DCL-S ITXMsg CHAR(256);                                                  // Nachricht
DCL-S ITSUCC CHAR(1);                                                    // Erfolg

//*-----------------------------------------------------------------------------------------*

// Bezugszahl für 'Schlüsselwort SFLEND in Nachrichtensubdatei aktivieren' auf 'Ja' setzen
*IN50 = *On;
// Dateien öffnen
GNRCPYPGM_openFiles();
// Initialisieren
If GNRCPYPGM_initialize(
  PIJOB  : // --> Job
  PIPOS  : // --> Position
  ITSUCC : // <-- Success
  ITXMsg   // <-- Message
);
  // Kopffelder initialisieren
  DFJOB = PIJOB;
  DFPOS = PIPOS;
  Chain ( PIJOB : PIPOS ) FILESF;
  If Not (%Found);
    DFFILE = *Blanks;
  Else;
    DFFILE = X3FILE;
  EndIf;
  // Bildschirm initialisieren
  ExSr SR030;
  // Bildschirm verarbeiten
  ExSr SR045;
EndIf;
// Dateien schließen
GNRCPYPGM_closeFiles();
// Programm nicht hauptspeicherresident verlassen
*INLR = *On;

//*-----------------------------------------------------------------------------------------*
//* Bildschirm initialisieren                                                               *
//*-----------------------------------------------------------------------------------------*

BegSr SR030;
  GNRCPYPGM_getValues(
    DFSRCF : // <-- Source file
    DFSRCL : // <-- Source file library
    DFSRCM : // <-- Source member
    DFREP :  // <-- Replace
    DFARA :  // <-- Area
    DFLIB :  // <-- Library
    DFSLIB : // <-- Shadow library
    DFVP :   // <-- Conversion program
    DFVPL    // <-- Conversion program library
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
      DFPGMQ = '*';
      Write DSPF999C;
      Write DSPF010;
      // Generieren Kopierprogramm
      ExSr SR070;
      If PIRtCd = '*F10';
        PIRtCd = '*F12';
        Leave;
      EndIf;
    EndIf;
  EndDo;
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Bildschirmfelder auf Fehler überprüfen                                                  *
//*-----------------------------------------------------------------------------------------*

BegSr SR050;

  // Namensfelder linksbündig ausrichten
  DFSRCF = %TRIML(DFSRCF);
  DFSRCL = %TRIML(DFSRCL);
  DFSRCM = %TRIML(DFSRCM);
  DFREP  = %TRIML(DFREP);
  DFARA  = %TRIML(DFARA);
  DFLIB  = %TRIML(DFLIB);
  DFSLIB = %TRIML(DFSLIB);
  DFVP   = %TRIML(DFVP);
  DFVPL  = %TRIML(DFVPL);

  // Werte übergeben
  GNRCPYPGM_setValues(
    DFSRCF : // --> Source file
    DFSRCL : // --> Source file library
    DFSRCM : // --> Source member
    DFREP :  // --> Replace
    DFARA :  // --> Area
    DFLIB :  // --> Library
    DFSLIB : // --> Shadow library
    DFVP :   // --> Conversion program
    DFVPL    // --> Conversion program library
  );

  // Prüfen
  If Not GNRCPYPGM_check(
    ITSUCC : // <-- Erfolg
    ITXFld : // <-- Feld
    ITXMsg   // <-- Nachricht
  );
    // Fehler setzen
    ITMsgId = 'CPF9897';
    ITMsgDta = ITXMsg;
    ITMsgF = 'QCPFMSG';
    Select;
    When (ITXFld = 'SRCF');
      *IN(87) = *ON;
      *IN(88) = *OFF;
      *IN(89) = *OFF;
    When (ITXFld = 'SRCL');
      *IN(87) = *OFF;
      *IN(88) = *ON;
      *IN(89) = *OFF;
    When (ITXFld = 'SRCM');
      *IN(87) = *ON;
      *IN(88) = *ON;
      *IN(89) = *OFF;
    When (ITXFld = 'REP');
      *IN(87) = *ON;
      *IN(88) = *ON;
      *IN(89) = *ON;
    When (ITXFld = 'ARA_LIB');
      *IN(87) = *OFF; // Area
      *IN(88) = *OFF; // Area
      *IN(89) = *ON;  // Area
      *IN(84) = *ON;  // Library
      *IN(85) = *OFF; // Library
      *IN(86) = *OFF; // Library
    When (ITXFld = 'ARA');
      *IN(87) = *OFF;
      *IN(88) = *OFF;
      *IN(89) = *ON;
    When (ITXFld = 'LIB');
      *IN(84) = *ON;
      *IN(85) = *OFF;
      *IN(86) = *OFF;
    When (ITXFld = 'SLIB');
      *IN(84) = *OFF;
      *IN(85) = *ON;
      *IN(86) = *OFF;
    When (ITXFld = 'VP');
      *IN(87) = *ON;
      *IN(88) = *OFF;
      *IN(89) = *ON;
    When (ITXFld = 'VPL');
      *IN(87) = *OFF;
      *IN(88) = *ON;
      *IN(89) = *ON;
    Endsl;

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
   ITMsgF  :                                                             // --> Nachrichtenda
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
    ITHIPG(ITNbr) = 'CPYPGM';
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
    // Modul 'CPYPGM'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'CPYPGM';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'SRCFILE';
    If DFFLD010 = 'DFSRCF';
      ITHlp = 'J';
      ITDsp(1) = ITNbr;
      ITDsp(2) = ITNbr;
    EndIf;
    // Modul 'CPYPGM'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'CPYPGM';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'SRCLIB';
    If DFFLD010 = 'DFSRCL';
      ITHlp = 'J';
      ITDsp(1) = ITNbr;
      ITDsp(2) = ITNbr;
    EndIf;
    // Modul 'CPYPGM'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'CPYPGM';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'SRCMBR';
    If DFFLD010 = 'DFSRCM';
      ITHlp = 'J';
      ITDsp(1) = ITNbr;
      ITDsp(2) = ITNbr;
    EndIf;
    // Modul 'CPYPGM'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'CPYPGM';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'REPLACE';
    If DFFLD010 = 'DFREP';
      ITHlp = 'J';
      ITDsp(1) = ITNbr;
      ITDsp(2) = ITNbr;
    EndIf;
    // Modul 'AREAS'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'AREAS';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'ARA';
    If DFFLD010 = 'DFARA';
      ITHlp = 'J';
      ITDsp(1) = ITNbr;
      ITDsp(2) = ITNbr;
    EndIf;
    // Modul 'LIBRARIES'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'LIBRARIES';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'LIB';
    If DFFLD010 = 'DFLIB';
      ITHlp = 'J';
      ITDsp(1) = ITNbr;
      ITDsp(2) = ITNbr;
    EndIf;
    // Modul 'LIBRARIES'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'LIBRARIES';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'SLIB';
    If DFFLD010 = 'DFSLIB';
      ITHlp = 'J';
      ITDsp(1) = ITNbr;
      ITDsp(2) = ITNbr;
    EndIf;
    // Modul 'FILES'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'FILES';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'VP';
    If DFFLD010 = 'DFVP';
      ITHlp = 'J';
      ITDsp(1) = ITNbr;
      ITDsp(2) = ITNbr;
    EndIf;
    // Modul 'FILES'
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
    ITHIPG(ITNbr) = 'RF0018GP';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'FKT';
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
    // Bedienerführung für das Feld 'Area'
  When DFFLD010 = 'DFARA';
    RF0016PG(
     '*SELECT' :                                                         // --> Modus
     PIJOB :                                                             // --> Job
     PIPOS :                                                             // --> Position
     ITARA :                                                             // <-> Area
     PIRtCd                                                              // <-- Rückkehrcode
    );
    If PIRtCd = '*SELECT';
      DFARA = ITARA;
      ExSr SR075;
    EndIf;
    // No prompt for the requested field available
  Other;
    ITMsgId = 'ERR0015';
    ITMsgDta = *Blanks;
    ITMsgF = 'RF0018MF';
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

//*-----------------------------------------------------------------------------------------*
//* Generieren Kopierprogramm                                                               *
//*-----------------------------------------------------------------------------------------*

BegSr SR070;

  DoW ITLoop;
    // Satzformate auf den Bildschirm ausgeben
    ExFmt DSPF020W;
    // Funktionstaste abrufen
    ITFktKey = RtvFktKey(AIDCde);
    // Funktionstaste : 'F10=Job starten'
    If ITFktKey = '*F10';
      PIRtCd = '*F10';
      Leave;
    EndIf;
    // Funktionstaste : 'F12=Abbrechen'
    If ITFktKey = '*F12';
      PIRtCd = '*F12';
      Leave;
    EndIf;
  EndDo;
  // Job starten
  If PIRtCd = '*F10';
    // Verbuchen
    If Not GNRCPYPGM_book(ITSUCC : ITXMsg);
      Exfmt DSPF030W;
    EndIf;
  EndIf;

EndSr;

//*-----------------------------------------------------------------------------------------*
//* Einlesen und belegen der Felder 'Library' und 'Shadow library'                          *
//*-----------------------------------------------------------------------------------------*

BegSr SR075;
  DFLIB = '';
  DFSLIB = '';
  Chain ( DFJOB : DFPOS : DFARA ) AREARF;
  If %found(AREAS);
    Chain ( XDJOB : XDLIB ) LIBRARYF;
    If %found(LIBRARIES);
      DFLIB = X2LIB;
      DFSLIB = X2SLIB;
    EndIf;
  EndIf;
EndSr;
