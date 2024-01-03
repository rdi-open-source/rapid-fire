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
//* Display Status                                                                          *
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
//* PIRtCd     / *F3      --> Der Benutzer hat die Taste F3 gedrückt                        *
//*              *F12     --> Der Benutzer hat die Taste F12 gedrückt                       *
//*              *ENTER   --> Der Benutzer hat die Taste ENTER gedrückt                     *
//*                                                                                         *
//*------------+--------+-------------------------------------------------------------------*
//*   Datum    I Progr. I Beschreibung                                                      *
//*------------+--------+-------------------------------------------------------------------*
//* 01.07.2014 I   FH   I Programm erstellt                                                 *
//*------------+--------+-------------------------------------------------------------------*

// Control

CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,');
CTL-OPT DftActGrp(*NO) ActGrp('RAPIDFIRE') BNDDIR('RAPIDFIRE');
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

DCL-F RF0013DF Workstn(*EXT) USAGE(*INPUT:*OUTPUT)
      SFile(DSPF010S : ITRRN010) InfDS(FIDS_RF0013DF);
DCL-F JOBS Disk(*EXT) USAGE(*INPUT) KEYED Rename(JOBS : JOBRF);
DCL-F FILES Disk(*EXT) USAGE(*INPUT) KEYED Rename(FILES : FILERF);
DCL-F AREAS Disk(*EXT) USAGE(*INPUT) KEYED Rename(AREAS : AREARF);
DCL-F LIBRARIES Disk(*EXT) USAGE(*INPUT) KEYED Rename(LIBRARIES : LIBRF);

//*-----------------------------------------------------------------------------------------*

// Prozedurenprototyp für Prozedur 'RF0013PG'

/Copy QCPYSRC,RF0013PG

// Prozedurenschnittstelle für Prozedur 'RF0013PG'

DCL-PI RF0013PG;
  PIJOB CHAR(10) Const;                                                  // --> Job
  PIRtCd CHAR(10);                                                       // <-- Rückkehrcode
END-PI;

//*-----------------------------------------------------------------------------------------*

// Prozedurenprototyp für Prozedur 'RtvFktKey'

/Copy QCPYSRC,RTVFKTKEY

// Prozedurenprototyp für Prozedur 'RmvErrMsg'

/Copy QCPYSRC,RMVERRMSG

// Prozedurenprototyp für Prozedur 'SndErrMsg'

/Copy QCPYSRC,SNDERRMSG

// Prozedurenprototyp für Prozedur 'RF0019PG'

/Copy QCPYSRC,RF0019PG

// Prozedurenprototyp für Prozedur 'QUHDSPH'

/Copy QCPYSRC,QUHDSPH

// Datenstruktur für Format 'ERRC0100' für Fehlercode

/Copy QCPYSRC,ERRC0100

// Prozedurenprototyp für Prozedur 'CPLSTSE'

/Copy QCPYSRC,CPLSTSE

// Prozedurenprototyp für Prozedur 'CHKSTSE'

/Copy QCPYSRC,CHKSTSE

// Prozedurenprototyp für Prozedur 'EXCCMD'

/Copy QCPYSRC,EXCCMD

// Dateiinformationsdatenstruktur für RF0013DF

DCL-DS FIDS_RF0013DF;
  AIDCde CHAR(1) POS(369);
END-DS;

// Anzahl Subdateisätze pro Seite

DCL-C RcdPerPage 3;

// Satzdatenstruktur für gelesene Sätze aus der Primärdatei

// *** Enthält den aktuell gelesenen Datensatz ***
DCL-DS CUR_AREAS ExtName('AREAS');
END-DS;
// *** Enthält die Tabelle der aktuellen Datensätze ***
DCL-DS TBC_AREAS ExtName('AREAS')
       Occurs(RcdPerPage)
       Prefix(TBC_);
END-DS;
// *** Enthält die Tabelle der gesicherten Datensätze ***
DCL-DS TBS_AREAS ExtName('AREAS')
       Occurs(RcdPerPage)
       Prefix(TBS_);
END-DS;

// Felder für Speicherung der Positionierungen

DCL-S StrPOS PACKED(6);                                                  // (Start) Position
DCL-S StrARA CHAR(10);                                                   // (Start) Bereich

DCL-S EndPOS PACKED(6);                                                  // (Ende) Position
DCL-S EndARA CHAR(10);                                                   // (Ende) Bereich

// Felder für Speicherung der Primärschlüssel

DCL-DS *N;
  FileKey;                                                               // Schlüssel
  DFPOS PACKED(6) Overlay(FileKey);                                      // Position
  DFARA CHAR(10) Overlay(FileKey : *Next);                               // Bereich
END-DS;

DCL-DS *N;
  ErrKey;                                                                // Schlüssel
  ErrPOS PACKED(6) Overlay(ErrKey);                                      // Position
  ErrARA CHAR(10) Overlay(ErrKey : *Next);                               // Bereich
END-DS;

DCL-DS *N;
  PosKey;                                                                // Schlüssel
  PosPOS PACKED(6) Overlay(PosKey);                                      // Position
  PosARA CHAR(10) Overlay(PosKey : *Next);                               // Bereich
END-DS;

DCL-DS *N;
  SlcKey;                                                                // Schlüssel
  SlcPOS PACKED(6) Overlay(SlcKey);                                      // Position
  SlcARA CHAR(10) Overlay(SlcKey : *Next);                               // Bereich
END-DS;

DCL-DS *N;
  CnlKey;                                                                // Schlüssel
  CnlPOS PACKED(6) Overlay(CnlKey);                                      // Position
  CnlARA CHAR(10) Overlay(CnlKey : *Next);                               // Bereich
END-DS;

// Felder für Speicherung der Auswahlen

DCL-S OptCnt ZONED(5) Inz(*Zero);                                        // Zähler für Option

DCL-S OptAct CHAR(1) Dim(32767) Based(OptActPtr);                        // Aktiv oder Inakti
DCL-S OptActPtr POINTER;                                                 // Zeiger

DCL-S OptOpt CHAR(2) Dim(32767) Based(OptOptPtr);                        // Auswahl
DCL-S OptOptPtr POINTER;                                                 // Zeiger

DCL-S OptKey Dim(32767) Based(OptKeyPtr)                                 // Schlüssel
Like(FileKey);
DCL-S OptKeyPtr POINTER;                                                 // Zeiger

// Hilfe-Id.

DCL-DS *N;
  ITHlpId CHAR(52) Dim(100);                                             // Hilfe-Id.
  ITHIPG CHAR(10) Overlay(ITHlpId : 1);                                  // Panelgroup
  ITHILP CHAR(10) Overlay(ITHlpId : 11);                                 // Bibliothek Panelg
  ITHIMO CHAR(32) Overlay(ITHlpId : 21);                                 // Modul
END-DS;

// Sonstige Felddeklarationen

DCL-S ITLoop IND Inz(*On);                                               // Schleife
DCL-S ITFnd CHAR(1);                                                     // Gefunden
DCL-S ITLen ZONED(9);                                                    // Zähler
DCL-S ITPag010 ZONED(4) Inz(RcdPerPage);                                 // (Subdatei) Anzahl
DCL-S ITRRN010 ZONED(4);                                                 // (Subdatei) Aktuel
DCL-S ITCnt010 ZONED(4);                                                 // (Subdatei) Letzte
DCL-S ITMsgId CHAR(7);                                                   // Nachrichten-Id.
DCL-S ITMsgDta CHAR(256);                                                // Nachrichtendaten
DCL-S ITMsgF   CHAR(10);                                                 // Nachrichtendatei
DCL-S ITAct CHAR(10);                                                    // Aktion
DCL-S ITMode CHAR(10);                                                   // Modus
DCL-S ITMore CHAR(10);                                                   // Weitere Sätze zur
DCL-S ITOpt ZONED(5);                                                    // Zähler für Auswah
DCL-S ITAra CHAR(10);                                                    // Positionierungsbe
DCL-S ITOEvl ZONED(5);                                                   // Zähler für Auswah
DCL-S ITOPos ZONED(5);                                                   // Zähler für Auswah
DCL-S ITPos CHAR(10);                                                    // Positionierung au
DCL-S ITCnt ZONED(2);                                                    // Satz für Subfile
DCL-S ITTbl CHAR(10) Dim(RcdPerPage);                                    // Satz für Subfile
DCL-S ITPnt ZONED(2) Dim(RcdPerPage);                                    // Satz für Subfile
DCL-S ITCntC ZONED(2);                                                   // Satz-DS (Zähler f
DCL-S ITPntC ZONED(2) Dim(RcdPerPage);                                   // Satz-DS (Zeiger f
DCL-S ITCntS ZONED(2);                                                   // Satz-DS (Zähler f
DCL-S ITPntS ZONED(2) Dim(RcdPerPage);                                   // Satz-DS (Zeiger f
DCL-S ITCntSav ZONED(2);                                                 // Satz-DS (Sicherun
DCL-S ITCnt1 ZONED(5);                                                   // Temporärer Zähler
DCL-S ITCnt2 ZONED(5);                                                   // Temporärer Zähler
DCL-S ITFktKey CHAR(10);                                                 // Funktionstaste
DCL-S ITSts CHAR(10);                                                    // Nachrichten-Id. b
DCL-S ITDsp INT(10) Dim(2);                                              // Anzeige Hilfetext
DCL-S ITNbr INT(10);                                                     // Anzahl Hilfetextm
DCL-S ITLftC INT(10) Dim(2);                                             // Linke Ecke
DCL-S ITRghC INT(10) Dim(2);                                             // Rechte Ecke
DCL-S ITCsrL INT(10) Dim(2);                                             // Cursorposition
DCL-S ITHlp CHAR(1);                                                     // Kontextbezogene H
DCL-S ITFTC CHAR(10);                                                    // Zu konvertierende
DCL-S ITTYPE CHAR(10);                                                   // Typ
DCL-S ITSEQ PACKED(3);                                                   // Sequenz
DCL-S ITPrcs CHAR(10);                                                   // Verarbeitung
DCL-S ITFILE CHAR(10);                                                   // File
DCL-S ITLIB CHAR(10);                                                    // Library
DCL-S ITRPL PACKED(10 : 0);                                              // Records in produc
DCL-S ITRSL PACKED(10 : 0);                                              // Records in shadow
DCL-S ITRTO PACKED(10 : 0);                                              // Records to copy
DCL-S ITRCO PACKED(10 : 0);                                              // Records copied
DCL-S ITETC CHAR(11);                                                    // <-- Estimated time
DCL-S ITCTA PACKED(10 : 0);                                              // Changes to apply
DCL-S ITCAP PACKED(10 : 0);                                              // Changes applied
DCL-S ITPRZ PACKED(3 : 0);                                               // Prozent
DCL-S ITRDK PACKED(10 : 0);                                              // Records with duplicate key
DCL-S DO_X PACKED(7 : 0);
DCL-S ITXMsg CHAR(256);                                                  // Nachricht
DCL-S ITSUCC CHAR(1);                                                    // Erfolg

//*-----------------------------------------------------------------------------------------*

// Bezugszahl für 'Schlüsselwort SFLEND in Nachrichtensubdatei aktivieren' auf 'Ja' setzen
*IN50 = *On;
// Datensatz lesen
Chain ( PIJOB ) JOBRF;
// Fehler : Der Datensatz konnte nicht gefunden werden
If Not %Found;
  PIRtCd = '*ERROR';
Else;
  // Dateien öffnen
  CPLSTSE_open();
  // Bildschirm initialisieren
  ExSr SR005;
  // Bildschirm verarbeiten
  ExSr SR010;
  // Heap-Speicher freigeben
  DeAlloc OptActPtr;
  DeAlloc OptOptPtr;
  DeAlloc OptKeyPtr;
  // Dateien schließen
  CPLSTSE_close();
EndIf;
// Programm nicht hauptspeicherresident verlassen
*INLR = *On;

//*-----------------------------------------------------------------------------------------*
//* Bildschirm initialisieren                                                               *
//*-----------------------------------------------------------------------------------------*

BegSr SR005;
  // Job initialisieren
  DFJOB = PIJOB;
  // Positionierung initialisieren
  DFPOPOS = *Zero;
  DFPOARA = *Blanks;
  // Zähler für Auswahlen initialisieren
  ITOpt = *Zero;
  // Subdatei auf den Anfang setzen
  ITAct = '*TOP';
  ExSr SR075;
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Bildschirm verarbeiten                                                                  *
//*-----------------------------------------------------------------------------------------*

BegSr SR010;
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
  // Cursor auf ersten Subdateisatz positionieren
  DFRRN010 = 1;
  DoW ITLoop;
    // Fehlernachricht in die Programmnachrichtenwarteschlange schreiben
    If ITMsgId <> *Blanks;
      ExSr SR100;
    EndIf;
    DoU ITFktKey <> '*F1';
      // Satzformate auf den Bildschirm ausgeben
      DFPGMQ = '*';
      Write DSPF999C;
      Write DSPF010F;
      If *IN61 = *On;
        Write DSPF010L;
      EndIf;
      ExFmt DSPF010C;
      // Befehlszeile lesen
      Read DSPF010F;
      // Funktionstaste abrufen
      ITFktKey = RtvFktKey(AIDCde);
      // Funktionstaste : 'F1=Hilfe'
      If ITFktKey = '*F1';
        ExSr SR020;
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
    // Cursor auf ersten Subdateisatz positionieren
    DFRRN010 = 1;
    // Letzte Auswahl für die spätere Positionierung merken, wenn Enter gedrückt wurde
    If ITFktKey = '*ENTER';
      ITPos = '*YES';
    Else;
      ITPos = '*NO';
    EndIf;
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
    // Funktionstaste : 'F5=Aktualisieren'
    If ITFktKey = '*F5';
      ExSr SR065;
      Iter;
    EndIf;
    If *IN61 = *Off;
      // Auswahlen in der Subfile auf Fehler überprüfen (Seite)
      ExSr SR095;
      // Es wurde ein Fehler gefunden
      If ITMsgId <> *Blanks;
        Iter;
      EndIf;
      // Auswahlen in der Subfile in die Optionstabelle übertragen
      ExSr SR105;
    EndIf;
    // Funktionstaste : 'F17=Anfang'
    If ITFktKey = '*F17';
      ExSr SR040;
      Iter;
    EndIf;
    // Funktionstaste : 'F18=Ende'
    If ITFktKey = '*F18';
      ExSr SR045;
      Iter;
    EndIf;
    // Funktionstaste : 'Vorwärtsblättern'
    If ITFktKey = '*ROLLUP';
      ExSr SR055;
      Iter;
    EndIf;
    // Funktionstaste : 'Rückwärtsblättern'
    If ITFktKey = '*ROLLDOWN';
      ExSr SR060;
      Iter;
    EndIf;
    // Funktionstaste : 'Enter'
    If ITFktKey = '*ENTER';
      // Neue Werte in Positionierungsfelder eingegeben
      If DFPOPOS <> *Zero Or
        DFPOARA <> *Blanks;
        ExSr SR070;
        Iter;
      EndIf;
      If ITOpt <> *Zero;
        // Auswahlen in der Subfile auf Fehler überprüfen und auswerten (Alles)
        ExSr SR115;
        // Es wurde ein Fehler gefunden
        If ITMsgId <> *Blanks;
          Iter;
        EndIf;
        // Das Programm soll verlassen werden
        If PIRtCd = '*F3';
          Leave;
        EndIf;
        // Die aktuelle Funktion soll abgebrochen werden
        If PIRtCd = '*F12';
          Iter;
        EndIf;
        // Anzeige aktualisieren
        ExSr SR065;
        Iter;
      EndIf;
      // Programm nach Drücken von Enter ohne sonstige Auswahl verlassen
      PIRtCd = '*ENTER';
      Leave;
    EndIf;
  EndDo;
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Hilfe                                                                                   *
//*-----------------------------------------------------------------------------------------*

BegSr SR020;
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
    ITHIPG(ITNbr) = 'RF0013GP';
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
    // Modul 'POXXX'
    //                  Eval      ITNbr = ITNbr + 1
    //                  Eval      ITHIPG(ITNbr) = 'RF0013GP'
    //                  Eval      ITHILP(ITNbr) = '*LIBL'
    //                  Eval      ITHIMO(ITNbr) = 'POXXX'
    //                  If        DFFLD010 = 'DFPOXXX'
    //                  Eval      ITHlp = 'J'
    //                  Eval      ITDsp(1) = ITNbr
    //                  Eval      ITDsp(2) = ITNbr
    //                  EndIf
    // Modul 'OPT'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'RF0013GP';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'OPT';
    If DFFLD010 = 'DFOPT';
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
    // Modul 'LIB'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'AREAS';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'LIB';
    If DFFLD010 = 'DFLIB';
      ITHlp = 'J';
      ITDsp(1) = ITNbr;
      ITDsp(2) = ITNbr;
    EndIf;
    // Modul 'RPL'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'RF0013GP';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'RPL';
    If DFFLD010 = 'DFRPL';
      ITHlp = 'J';
      ITDsp(1) = ITNbr;
      ITDsp(2) = ITNbr;
    EndIf;
    // Modul 'RSL'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'RF0013GP';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'RSL';
    If DFFLD010 = 'DFRSL';
      ITHlp = 'J';
      ITDsp(1) = ITNbr;
      ITDsp(2) = ITNbr;
    EndIf;
    // Modul 'RTO'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'RF0013GP';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'RTO';
    If DFFLD010 = 'DFRTO';
      ITHlp = 'J';
      ITDsp(1) = ITNbr;
      ITDsp(2) = ITNbr;
    EndIf;
    // Modul 'RCO'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'RF0013GP';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'RCO';
    If DFFLD010 = 'DFRCO';
      ITHlp = 'J';
      ITDsp(1) = ITNbr;
      ITDsp(2) = ITNbr;
    EndIf;
    // Modul 'ETC'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'RF0013GP';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'ETC';
    If DFFLD010 = 'DFETC';
      ITHlp = 'J';
      ITDsp(1) = ITNbr;
      ITDsp(2) = ITNbr;
    EndIf;
    // Modul 'RDK'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'RF0013GP';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'RDK';
    If DFFLD010 = 'DFRDK';
      ITHlp = 'J';
      ITDsp(1) = ITNbr;
      ITDsp(2) = ITNbr;
    EndIf;
    // Modul 'CTA'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'RF0013GP';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'CTA';
    If DFFLD010 = 'DFCTA';
      ITHlp = 'J';
      ITDsp(1) = ITNbr;
      ITDsp(2) = ITNbr;
    EndIf;
    // Modul 'CAP'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'RF0013GP';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'CAP';
    If DFFLD010 = 'DFCAP';
      ITHlp = 'J';
      ITDsp(1) = ITNbr;
      ITDsp(2) = ITNbr;
    EndIf;
    // Modul 'FKT'
    ITNbr = ITNbr + 1;
    ITHIPG(ITNbr) = 'RF0013GP';
    ITHILP(ITNbr) = '*LIBL';
    ITHIMO(ITNbr) = 'FKT';
    If DFROW010 >= 26 And
      DFROW010 <= 26 And
      DFCOL010 >= 2 And
      DFCOL010 <= 131;
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
  ITRghC(1) = 27;
  ITRghC(2) = 132;
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
//* Anfang                                                                                  *
//*-----------------------------------------------------------------------------------------*

BegSr SR040;
  // Die ersten Sätze in die Subfile stellen
  ITAct = '*TOP';
  ExSr SR075;
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Ende                                                                                    *
//*-----------------------------------------------------------------------------------------*

BegSr SR045;
  // Die letzten Sätze in die Subfile stellen
  ITAct = '*BOTTOM';
  ExSr SR075;
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Vorwärtsblättern                                                                        *
//*-----------------------------------------------------------------------------------------*

BegSr SR055;
  // Die Sätze der nächsten Seite in die Subfile stellen
  ITAct = '*FOREWARD';
  ExSr SR075;
  // The bottom of the list has already been reached
  If ITMore = '*NO';
    ITMsgId = 'ERR0003';
    ITMsgDta = *Blanks;
    ITMsgF = 'RF0013MF';
  EndIf;
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Rückwärtsblättern                                                                       *
//*-----------------------------------------------------------------------------------------*

BegSr SR060;
  // Die Sätze der vorherigen Seite in die Subfile stellen
  ITAct = '*BACKWARD';
  ExSr SR075;
  // The top of the list has already been reached
  If ITMore = '*NO';
    ITMsgId = 'ERR0002';
    ITMsgDta = *Blanks;
    ITMsgF = 'RF0013MF';
  EndIf;
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Aktualisieren                                                                           *
//*-----------------------------------------------------------------------------------------*

BegSr SR065;
  // Zähler für Auswahlen initialisieren
  ITOpt = *Zero;
  // Die aktuellen Sätze nocheinmal in die Subfile stellen
  ITAct = '*REFRESH';
  ExSr SR075;
  // Wenn keine Sätze gelesen werden konnten, dann die letzten Sätze in die Subfile stellen
  If ITMore = '*NO';
    ITAct = '*BOTTOM';
    ExSr SR075;
  EndIf;
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Positionieren                                                                           *
//*-----------------------------------------------------------------------------------------*

BegSr SR070;
  // Die Sätze ab dem Positionierungswert in die Subfile stellen
  ITAct = '*POSITION';
  ITAra = '*SCREEN';
  ExSr SR075;
  // Wenn keine Sätze gelesen werden konnten, dann die letzten Sätze in die Subfile stellen
  If ITMore = '*NO';
    ITAct = '*BOTTOM';
    ExSr SR075;
  EndIf;
  // Positionierungsfelder initialisieren
  DFPOPOS = *Zero;
  DFPOARA = *Blanks;
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Subdatei mit Sätzen aus der Primärdatei füllen                                          *
//*-----------------------------------------------------------------------------------------*

BegSr SR075;
  // Zähler für die aktuelle Satzdatenstruktur sichern
  ItCntSav = ITCntC;
  // Aktuelle Satzdatenstruktur mit Sätzen aus der Primärdatei füllen
  ExSr SR080;
  If ITCntC > *Zero;
    // Weitere Sätze zur Anzeige bereitgestellt
    ITMore = '*YES';
    // Tabelle mit Zeigern für die in der Subfile anzuzeigenden Sätze füllen
    ExSr SR085;
    // Subfile mit den Werten aus der Satzdatenstruktur füllen
    ExSr SR090;
  Else;
    // Keine weiteren Sätze zur Anzeige bereitgestellt
    ITMore = '*NO';
// Bei Aktionen 'Vorwärtsbl.' und 'Rückwärtsbl.' den Zähler für aktuelle Satzdatenstruktur wi
    If ITAct = '*FOREWARD' Or
      ITAct = '*BACKWARD';
      ITCntC = ITCntSav;
      // Bei allen anderen Aktionen die Subdatei löschen
    Else;
      // Inhalt der Subdatei löschen
      *IN60 = *On;
      Write DSPF010C;
      *IN60 = *Off;
      // Letzter Satz initialisieren
      ITCnt010 = *Zero;
    EndIf;
  EndIf;
  If ITCnt010 = *Zero;
    // Bezugszahl für 'Keine Sätze in der Subdatei vorhanden' auf 'Ja' setzen
    *IN61 = *On;
  Else;
    // Bezugszahl für 'Keine Sätze in der Subdatei vorhanden' auf 'Nein' setzen
    *IN61 = *Off;
  EndIf;
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Aktuelle Satzdatenstruktur mit Sätzen aus der Primärdatei füllen                        *
//*-----------------------------------------------------------------------------------------*

BegSr SR080;
  // Aktuelle Satzdatenstruktur sichern
  If ITAct = '*FOREWARD' Or
    ITAct = '*BACKWARD';
    ITCntS = ITCntC;
    FOR ITCnt1 = 1 TO ITCntC;
      %OCCUR(TBC_AREAS) = ITCnt1;
      %OCCUR(TBS_AREAS) = ITCnt1;
      TBS_AREAS = TBC_AREAS;
      ITPntS(ITCnt1) = ITPntC(ITCnt1);
    ENDFOR;
  EndIf;
  // Zähler für aktuelle Satzdatenstruktur initialisieren
  ITCntC = *Zero;
  // Primärdatei positionieren
  Select;
    // *** Vorwärts ***
  When ITAct = '*FOREWARD';
    SetGt ( PIJOB : EndPOS : EndARA ) AREARF;
    // *** Rückwärts ***
  When ITAct = '*BACKWARD';
    SetLL ( PIJOB : StrPOS : StrARA ) AREARF;
    // *** Positionieren ***
  When ITAct = '*POSITION';
    Select;
    When ITAra = '*SCREEN';
      SetLL ( PIJOB : DFPOPOS : DFPOARA ) AREARF;
    When ITAra = '*CANCEL';
      SetLL ( PIJOB : CnlPOS : CnlARA ) AREARF;
    When ITAra = '*ERROR';
      SetLL ( PIJOB : ErrPOS : ErrARA ) AREARF;
    EndSl;
    // *** Anfang ***
  When ITAct = '*TOP';
    ITCnt = *Zero;
    ITCntC = *Zero;
    ITCntS = *Zero;
    StrPOS = *Loval;
    StrARA = *Loval;
    EndPOS = *Loval;
    EndARA = *Loval;
    SetGt ( PIJOB : StrPOS : StrARA ) AREARF;
    // *** Ende ***
  When ITAct = '*BOTTOM';
    ITCnt = *Zero;
    ITCntC = *Zero;
    ITCntS = *Zero;
    StrPOS = *Hival;
    StrARA = *Hival;
    EndPOS = *Hival;
    EndARA = *Hival;
    SetGt ( PIJOB : EndPOS : EndARA ) AREARF;
    // *** Aktualisieren ***
  When ITAct = '*REFRESH';
    SetLL ( PIJOB : StrPOS : StrARA ) AREARF;
  EndSl;
  DoU %EOF(AREAS) Or
    ITCntC = ITPag010;
    // Primärdatei lesen
    DoU %EOF(AREAS) Or
      ITPrcs = '*YES';
      Select;
        // *** Vorwärts, Positionierung, Anfang, Aktualisieren ***
      When ITAct = '*FOREWARD' Or
        ITAct = '*POSITION' Or
        ITAct = '*TOP' Or
        ITAct = '*REFRESH';
        ReadE ( PIJOB ) AREARF;
        // *** Rückwärts, Ende ***
      When ITAct = '*BACKWARD' Or
        ITAct = '*BOTTOM';
        ReadPE ( PIJOB ) AREARF;
      EndSl;
      If Not %EOF(AREAS);
        ExSr SR092;
      EndIf;
    EndDo;
    If Not %EOF(AREAS);
      // Zähler der aktuellen Satzdatenstruktur auf das nächste Vorkommen setzen
      ITCntC = ITCntC + 1;
      // Vorkommen der aktuellen Satzdatenstruktur auf den Wert des Zählers setzen
      %OCCUR(TBC_AREAS) = ITCntC;
      // Aktuellen Datensatz in die aktuelle Satzdatenstruktur übertragen
      TBC_AREAS = CUR_AREAS;
    EndIf;
  EndDo;
  If ITCntC > *Zero;
    // Zeiger zu den Sätzen in der aktuellen Satzdatenstruktur festlegen
    Select;
      // *** Vorwärts, Positionierung, Anfang, Aktualisieren ***
    When ITAct = '*FOREWARD' Or
      ITAct = '*POSITION' Or
      ITAct = '*TOP' Or
      ITAct = '*REFRESH';
      FOR ITCnt1 = 1 TO ITCntC;
        ITPntC(ITCnt1) = ITCnt1;
      ENDFOR;
      // *** Rückwärts, Ende ***
    When ITAct = '*BACKWARD' Or
      ITAct = '*BOTTOM';
      ITCnt1 = ITCntC;
      FOR ITCnt2 = 1 TO ITCntC;
        ITPntC(ITCnt2) = ITCnt1;
        ITCnt1 = ITCnt1 - 1;
      ENDFOR;
    EndSl;
  EndIf;
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Tabelle mit Zeigern für die in der Subfile anzuzeigenden Sätze füllen                   *
//*-----------------------------------------------------------------------------------------*

BegSr SR085;
  // Zähler für die in der Subfile anzuzeigenden Sätze festlegen
  ITCnt = ITCntC;
  Select;
    // *** Vorwärts, Positionierung, Anfang, Aktualisieren ***
  When ITAct = '*FOREWARD' Or
    ITAct = '*POSITION' Or
    ITAct = '*TOP' Or
    ITAct = '*REFRESH';
    // Tabelle mit Zeigern für die in der Subfile anzuzeigenden Sätze füllen
    FOR ITCnt1 = 1 TO ITCntC;
      ITTbl(ITCnt1) = '*CURRENT';
      ITPnt(ITCnt1) = ITPntC(ITCnt1);
    ENDFOR;
    // *** Rückwärts, Ende ***
  When ITAct = '*BACKWARD' Or
    ITAct = '*BOTTOM';
    // Tabelle mit Zeigern für die in der Subfile anzuzeigenden Sätze füllen
    FOR ITCnt1 = 1 TO ITCntC;
      ITTbl(ITCnt1) = '*CURRENT';
      ITPnt(ITCnt1) = ITPntC(ITCnt1);
    ENDFOR;
    // Es sind beim Rückwärtslesen nicht genügend Sätze für eine Seite gefunden worden
    If ITAct = '*BACKWARD' And
      ITCntC < ITPag010 And
      ITCntS > *Zero;
      // Anzahl Sätze, die aus der Sicherung übernommen werden sollen berechnen
      ITCnt1 = ITPag010 - ITCntC;
      If ITCnt1 > ITCntS;
        ITCnt1 = ITCntS;
      EndIf;
      // Tabelle mit Zeigern für die in der Subfile anzuzeigenden Sätze füllen
      FOR ITCnt2 = 1 TO ITCnt1;
        ITCnt = ITCnt + 1;
        ITTbl(ITCnt) = '*SAVE';
        ITPnt(ITCnt) = ITPntS(ITCnt2);
      ENDFOR;
    EndIf;
  EndSl;
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Subfile mit den Werten aus der Satzdatenstruktur füllen                                 *
//*-----------------------------------------------------------------------------------------*

BegSr SR090;
  // Inhalt der Subdatei löschen
  *IN60 = *On;
  Write DSPF010C;
  *IN60 = *Off;
  // Letzter Satz initialisieren
  ITCnt010 = *Zero;
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
  // Fehlerhafte Position gefunden initialisieren
  ITFnd = 'N';
  FOR ITCnt1 = 1 TO ITCnt;
    Select;
      // Anzuzeigender Datensatz befindet sich in der aktuellen Satzdatenstruktur
    When ITTbl(ITCnt1) = '*CURRENT';
      %OCCUR(TBC_AREAS) = ITPnt(ITCnt1);
      CUR_AREAS = TBC_AREAS;
      // Anzuzeigender Datensatz befindet sich in der gesicherten Satzdatenstruktur
    When ITTbl(ITCnt1) = '*SAVE';
      %OCCUR(TBS_AREAS) = ITPnt(ITCnt1);
      CUR_AREAS = TBS_AREAS;
    EndSl;
    // Schlüssel für Startsatz der Subfile initialisieren
    If ITCnt1 = 1;
      StrPOS = XDPOS;
      StrARA = XDARA;
    EndIf;
    // Schlüssel für Endsatz der Subfile initialisieren
    If ITCnt1 = ITCnt;
      EndPOS = XDPOS;
      EndARA = XDARA;
    EndIf;
    // Felder im Subdateisatz füllen
    DFPOS = XDPOS;
    DFARA = XDARA;
    CPLSTSE_completeStatusEntry(
      PIJOB :  // --> Job
      XDPOS :  // --> Position
      XDARA :  // --> Area
      ITFILE : // <-- File
      ITLIB :  // <-- Library
      ITRPL :  // <-- Records in production library
      ITRSL :  // <-- Records in shadow library
      ITRTO :  // <-- Records to copy
      ITRCO :  // <-- Records copied
      ITETC :  // <-- Estimated time
      ITRDK :  // <-- Records with duplicate key
      ITCTA :  // <-- Changes to apply
      ITCAP :  // <-- Changes applied
      ITPRZ    // <-- Percent done
    );
    DFFILE = ITFILE;
    DFLIB = ITLIB;
    DFRPL = %Trim(%EditW(ITRPL:' .   .   . 0 '));
    DFRSL = %Trim(%EditW(ITRSL:' .   .   . 0 '));
    DFRTO = %Trim(%EditW(ITRTO:' .   .   . 0 '));
    DFRCO = %Trim(%EditW(ITRCO:' .   .   . 0 '));
    DFETC = ITETC;
    DFRDK = %Trim(%EditW(ITRDK:' .   .   . 0 '));
    DFCTA = %Trim(%EditW(ITCTA:' .   .   . 0 '));
    DFCAP = %Trim(%EditW(ITCAP:' .   .   . 0 '));
    DFPRZ = *Blanks;
    If ITPRZ > *Zero;
      %SubSt(DFPRZ : 1 : 1) = X'21';
      %SubSt(DFPRZ : ITPRZ + 1 : 1) = X'20';
    EndIf;
    // Auswahl initialisieren
    DFOPT = *Blanks;
    // Fehlerbezugszahlen initialisieren
    *IN(81) = *OFF;
    *IN(82) = *OFF;
    *IN(83) = *OFF;
    FOR ITOPos = 1 TO ITOpt;
      // Zum Satz wurde eine Auswahl gefunden
      If OptAct(ITOPos) = 'J' And
        OptKey(ITOPos) = FileKey;
        // Auswahl setzen
        DFOPT = OptOpt(ITOPos);
        // Der fehlerhafte Satz wurde gefunden
        If ITAct = '*POSITION' And
          ITAra = '*ERROR' And
          OptKey(ITOPos) = ErrKey;
          //  Fehlerhafte Position gefunden
          ITFnd = 'J';
          // Fehlerbezugszahlen setzen
          *IN(81) = *OFF;
          *IN(82) = *OFF;
          *IN(83) = *ON;
        EndIf;
        Leave;
      EndIf;
    ENDFOR;
    // Letzter Satz erhöhen
    ITCnt010 = ITCnt010 + 1;
    // Aktuellen Satz auf letzten Satz setzen
    ITRRN010 = ITCnt010;
    // Bezugszahl für 'Subdateisatz ist ein geänderter Satz' auf 'Nein' setzen
    *IN63 = *Off;
    // Subdateisatz schreiben
    Write DSPF010S;
    // Der Cursor soll auf die Auswahl positioniert werden
    If ITAct = '*REFRESH' And
      ITPos = '*POS' And
      FileKey = PosKey;
      DFRRN010 = ITCnt010;
    EndIf;
  ENDFOR;
  If ITCnt = ITPag010;
  // Feststellen, ob hinter dem letzten angezeigten Subdateisatz noch ein weiterer Satz kommt
    SetGt ( PIJOB : EndPOS : EndARA ) AREARF;
    DoU %EOF(AREAS) Or
      ITPrcs = '*YES';
      ReadE ( PIJOB ) AREARF;
      If Not %EOF(AREAS);
        ExSr SR092;
      EndIf;
    EndDo;
    If %EOF(AREAS);
      // Bezugszahl für 'Letzter Satz befindet sich bereits in der Subdatei' auf 'Ja' setzen
      *IN62 = *On;
    Else;
     // Bezugszahl für 'Letzter Satz befindet sich bereits in der Subdatei' auf 'Nein' setzen
      *IN62 = *Off;
    EndIf;
  Else;
    // Bezugszahl für 'Letzter Satz befindet sich bereits in der Subdatei' auf 'Ja' setzen
    *IN62 = *On;
  EndIf;
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Filter auf Primärdatei legen                                                            *
//*-----------------------------------------------------------------------------------------*

BegSr SR092;
  // 'Verarbeitung' initialisieren
  ITPrcs = '*YES';
  FOR DO_X = 1 TO 1;
    // Filtern nach 'Typ'
    Chain ( PIJOB : XDPOS ) FILERF;
    If Not (%Found and
      X3TYPE = '*PHYSICAL');
      ITPrcs = '*NO';
      Leave;
    EndIf;
  ENDFOR;
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Auswahlen in der Subfile auf Fehler überprüfen (Seite)                                  *
//*-----------------------------------------------------------------------------------------*

BegSr SR095;
  DoW ITLoop;
    // Subdateisatz lesen
    ReadC DSPF010S;
    // Ende der Datei
    If %EOF;
      Leave;
    EndIf;
    // Option linksbündig ausrichten
    DFOpt = %TrimL(DFOpt);
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

    // Aktion ermitteln
    Select;
    When DFOPT = *Blanks;
      ITACT = CHKSTSE_NONE;
    When DFOPT = '5';
      ITACT = CHKSTSE_DSPRCDDUPKEY;
    When DFOPT = '99';
      ITACT = CHKSTSE_REAPYCHG;
    Other;
      ITACT = CHKSTSE_ERROR;
    EndSl;

    // The option is not valid
    If ITMsgId = *Blanks And
      (ITACT <> CHKSTSE_NONE And
      ITACT <> CHKSTSE_DSPRCDDUPKEY And
      ITACT <> CHKSTSE_REAPYCHG);
      ITMsgId = 'ERR0001';
      ITMsgDta = *Blanks;
      ITMsgF = 'RF0013MF';
      *IN(81) = *OFF;
      *IN(82) = *OFF;
      *IN(83) = *ON;
    EndIf;

    // Check action
    If ITMsgId = *Blanks And
       Not CHKSTSE_checkAction(
         ITACT :  // --> Action
         PIJOB :  // --> Job
         DFPOS :  // --> Position
         DFARA :  // --> Area
         ITSUCC : // <-- Success
         ITXMsg   // <-- Message
       );
      // Fehler setzen
      ITMsgId = 'CPF9897';
      ITMsgDta = ITXMsg;
      ITMsgF = 'QCPFMSG';
      *IN(81) = *OFF;
      *IN(82) = *OFF;
      *IN(83) = *ON;
    EndIf;

    // Bezugszahl für 'Subdateisatz ist ein geänderter Satz' auf 'Ja' setzen
    *IN63 = *On;
    // Subdateisatz aktualisieren
    Update DSPF010S;
  EndDo;
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Fehlernachricht in die Programmnachrichtenwarteschlange schreiben                       *
//*-----------------------------------------------------------------------------------------*

BegSr SR100;
  // Bezugszahl für 'Es liegt ein Fehler vor' auf 'Ja' setzen
  *IN80 = *On;
  // Fehlernachricht in die Programmnachrichtenwarteschlange schreiben
  ITSts = SndErrMsg(
   '*SAME' :                                                             // --> Aufrufstapele
   ITMsgId :                                                             // --> Nachrichten-I
   ITMsgF :                                                              // --> Nachrichtenda
   '*LIBL' :                                                             // --> Bibliothek Na
   ITMsgDta                                                              // --> Nachrichtenda
  );
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Auswahlen in der Subfile in die Optionstabelle übertragen                               *
//*-----------------------------------------------------------------------------------------*

BegSr SR105;
  // Subdateizähler initialisieren
  If ITPos <> '*NO';
    ITRRN010 = *Zero;
  EndIf;
  DoW ITLoop;
    // Subdateisatz lesen
    If ITPos <> '*NO';
      ITRRN010 = ITRRN010 + 1;
      If ITRRN010 > ITCnt010;
        Leave;
      Else;
        Chain ITRRN010 DSPF010S;
      EndIf;
    Else;
      ReadC DSPF010S;
      // Ende der Datei
      If %EOF;
        Leave;
      EndIf;
    EndIf;
    // Feststellen, ob die geänderte Position bereits in der Auswahltabelle steht
    ITFnd = 'N';
    FOR ITOPos = 1 TO ITOpt;
      If OptAct(ITOPos) = 'J' And
        OptKey(ITOPos) = FileKey;
        ITFnd = 'J';
        Leave;
      EndIf;
    ENDFOR;
    Select;
      // Die Auswahl wurde bei einer neuen Position eingegeben
    When ITFnd = 'N' And
      DFOPT <> *Blanks;
      If ITOpt = OptCnt;
        ExSr SR110;
      EndIf;
      ITOpt = ITOpt + 1;
      OptAct(ITOpt) = 'J';
      OptOpt(ITOpt) = DFOPT;
      OptKey(ITOpt) = FileKey;
      // Die Auswahl wurde bei einer bestehenden Position zurückgenommen
    When ITFnd = 'J' And
      DFOPT = *Blanks;
      OptAct(ITOPos) = 'N';
      // Die Auswahl wurde bei einer bestehenden Position geändert
    When ITFnd = 'J' And
      DFOPT <> *Blanks;
      OptOpt(ITOPos) = DFOPT;
    EndSl;
    // Schlüssel merken
    If ITPos = '*YES' And
      DFOPT <> *Blanks;
      ITPos = '*POS';
      PosKey = FileKey;
    EndIf;
  EndDo;
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Speicher für Optionstabellen anfordern                                                  *
//*-----------------------------------------------------------------------------------------*

BegSr SR110;
  // Das erste Mal Speicher anfordern
  If OptCnt = *Zero;
    // Zähler erhöhen
    OptCnt = OptCnt + 100;
    // Speicher für Tabelle (Aktiv) anfordern
    ITLen = %Size(OptAct) * OptCnt;
    OptActPtr = %ALLOC(ITLen);
    // Speicher für Tabelle (Auswahl) anfordern
    ITLen = %Size(OptOpt) * OptCnt;
    OptOptPtr = %ALLOC(ITLen);
    // Speicher für Tabelle (Schlüssel) anfordern
    ITLen = %Size(OptKey) * OptCnt;
    OptKeyPtr = %ALLOC(ITLen);
    // Ein weiteres Mal Speicher anfordern
  Else;
    // Zähler erhöhen
    OptCnt = OptCnt + 100;
    // Speicher für Tabelle (Aktiv) anfordern
    ITLen = %Size(OptAct) * OptCnt;
    OptActPtr = %REALLOC(OptActPtr:ITLen);
    // Speicher für Tabelle (Auswahl) anfordern
    ITLen = %Size(OptOpt) * OptCnt;
    OptOptPtr = %REALLOC(OptOptPtr:ITLen);
    // Speicher für Tabelle (Schlüssel) anfordern
    ITLen = %Size(OptKey) * OptCnt;
    OptKeyPtr = %REALLOC(OptKeyPtr:ITLen);
  EndIf;
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Auswahlen in der Subfile auf Fehler überprüfen und auswerten (Alles)                    *
//*-----------------------------------------------------------------------------------------*

BegSr SR115;
  FOR ITOEvl = 1 TO ITOpt;
    // Nur aktive Positionen verarbeiten
    If OptAct(ITOEvl) = 'J';
      // Auswahlen in der Subfile auf Fehler überprüfen (Alles)
      ExSr SR120;
      // Bei Fehler aussteigen
      If ITMsgId <> *Blanks;
        Leave;
      Else;
        // Position auf inaktiv setzen
        OptAct(ITOEvl) = 'N';
        // Auswahlen der Subdatei auswerten (Alles)
        ExSr SR125;
        // Der Benutzer hat die Funktionstaste F3 oder Funktionstaste F12 gedrückt
        If PIRtCd = '*F3' Or
          PIRtCd = '*F12';
          // Zähler für Auswahlen initialisieren, wenn die letzte Position ausgewertet ist
          If ITOEvl = ITOpt;
            ITOpt = *Zero;
          EndIf;
          // Schleife verlassen
          Leave;
        EndIf;
      EndIf;
    EndIf;
  ENDFOR;
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Auswahlen in der Subfile auf Fehler überprüfen (Alles)                                  *
//*-----------------------------------------------------------------------------------------*

BegSr SR120;
  //                  Do
  // Fehler : Irgendein Fehler
  //                  If        Fehler
  //                  Eval      ITAct = '*POSITION'
  //                  Eval      ITAra = '*ERROR'
  //                  Eval      ErrKey = OptKey(ITOEvl)
  //                  ExSr      SR075
  //                  If        ITFnd = 'J'
  //                  Eval      ITMsgId = 'ERRXXXX'
  //                  Else
  //                  Eval      OptAct(ITOEvl) = 'N'
  //                  EndIf
  //                  Leave
  //                  EndIf
  //                  EndDo
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Auswahlen in der Subfile auswerten (Alles)                                              *
//*-----------------------------------------------------------------------------------------*

BegSr SR125;
  // Rückkehrcode initialisieren
  PIRtCd = '*ENTER';
  Select;
    // Auswahl : '5=Display records with duplicate key'
  When OptOpt(ITOEvl) = '5';
    SlcKey = OptKey(ITOEvl);
    ExSr SR127;
    // Auswahl : '99=Reapply all changes'
  When OptOpt(ITOEvl) = '99';
    SlcKey = OptKey(ITOEvl);
    ExSr SR130;
  EndSl;
  // Die aktuelle Funktion wurde vom Benutzer mit F12 abgebrochen
  If PIRtCd = '*F12';
    ITAct = '*POSITION';
    ITAra = '*CANCEL';
    CnlKey = OptKey(ITOEvl);
    ExSr SR075;
  EndIf;
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Display records with duplicate key                                                      *
//*-----------------------------------------------------------------------------------------*

BegSr SR127;

  Chain ( PIJOB : SlcPOS : SlcARA ) AREARF;
  If %Found;
    Chain ( PIJOB : XDLIB ) LIBRF;
    If %Found;
      EXCCMD_execute_Command(
        'RUNQRY ' +
        'QRYFILE((' + %Trim(X2SLIB) + '/' +  %Trim(XDDRK) + '))'
      );
    EndIf;
  EndIf;

EndSr;

//*-----------------------------------------------------------------------------------------*
//* Reapply all changes                                                                     *
//*-----------------------------------------------------------------------------------------*

BegSr SR130;
  RF0019PG(
  PIJOB :                                                                // --> Job
  SlcPOS :                                                               // --> Position
  SlcARA :                                                               // --> Area
  PIRtCd                                                                 // <-- Return code
  );
EndSr;
