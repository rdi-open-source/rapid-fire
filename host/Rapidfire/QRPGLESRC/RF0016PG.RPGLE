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
//* Maintain Areas                                                                          *
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
//* 01      --> Modus : 'Pflegen'                                                           *
//* 02      --> Modus : 'Selektieren'                                                       *
//* 05      --> Modus : 'Anzeigen'                                                          *
//* 06 - 07 --> Funktionstasten                                                             *
//* 09      --> Befehlszeile modifiziert                                                    *
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
//* PIMode     / *EDIT    --> Modus : 'Pflegen'                                             *
//*              *SELECT  --> Modus : 'Selektieren'                                         *
//*              *DISPLAY --> Modus : 'Anzeigen'                                            *
//*                                                                                         *
//* PIRtCd     / *F3      --> Der Benutzer hat die Taste F3 gedrückt                        *
//*              *F12     --> Der Benutzer hat die Taste F12 gedrückt                       *
//*              *ENTER   --> Der Benutzer hat die Taste ENTER gedrückt                     *
//*              *SELECT  --> Der Benutzer hat die Taste ENTER mit Auswahl 1 gedrückt       *
//*                                                                                         *
//*------------+--------+-------------------------------------------------------------------*
//*   Datum    I Progr. I Beschreibung                                                      *
//*------------+--------+-------------------------------------------------------------------*
//* 15.10.2015 I   FH   I Programm erstellt                                                 *
//*------------+--------+-------------------------------------------------------------------*
//* 13.06.2016 I   TR   I Enabled program for '*DISPLAY' mode.                              *
//*------------+--------+-------------------------------------------------------------------*

// Control

CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,');
CTL-OPT DftActGrp(*NO) ActGrp('RAPIDFIRE') BNDDIR('RAPIDFIRE');
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

DCL-F RF0016DF Workstn(*EXT) USAGE(*INPUT:*OUTPUT)
      SFile(DSPF010S : ITRRN010) InfDS(FIDS_RF0016DF);
DCL-F JOBS Disk(*EXT) USAGE(*INPUT) KEYED Rename(JOBS : JOBRF);
DCL-F FILES Disk(*EXT) USAGE(*INPUT) KEYED Rename(FILES : FILERF);
DCL-F AREAS Disk(*EXT) USAGE(*INPUT) KEYED Rename(AREAS : AREARF);

//*-----------------------------------------------------------------------------------------*

// Prozedurenprototyp für Prozedur 'RF0016PG'

/Copy QCPYSRC,RF0016PG

// Prozedurenschnittstelle für Prozedur 'RF0016PG'

DCL-PI RF0016PG;
  PIMode CHAR(10) Const;                                                 // --> Modus
  PIJOB CHAR(10) Const;                                                  // --> Job
  PIPOS PACKED(6) Const;                                                 // --> Position
  PIARA CHAR(10);                                                        // <-> Area
  PIRtCd CHAR(10);                                                       // <-- Rückkehrcode
END-PI;

//*-----------------------------------------------------------------------------------------*

// Prozedurenprototyp für Prozedur 'RtvFktKey'

/Copy QCPYSRC,RTVFKTKEY

// Prozedurenprototyp für Prozedur 'RmvErrMsg'

/Copy QCPYSRC,RMVERRMSG

// Prozedurenprototyp für Prozedur 'SndErrMsg'

/Copy QCPYSRC,SNDERRMSG

// Prozedurenprototyp für Prozedur 'RtvCmdLin'

/Copy QCPYSRC,RTVCMDLIN

// Prozedurenprototyp für Prozedur 'ExcCmdLin'

/Copy QCPYSRC,EXCCMDLIN

// Prozedurenprototyp für Prozedur 'QUHDSPH'

/Copy QCPYSRC,QUHDSPH

// Prozedurenprototyp für Prozedur 'RF0017PG'

/Copy QCPYSRC,RF0017PG

// Datenstruktur für Format 'ERRC0100' für Fehlercode

/Copy QCPYSRC,ERRC0100

// Prozedurenprototyp für Prozedur 'MNTAREA'

/Copy QCPYSRC,MNTAREA

// Dateiinformationsdatenstruktur für RF0016DF

DCL-DS FIDS_RF0016DF;
  AIDCde CHAR(1) POS(369);
END-DS;

// Anzahl Subdateisätze pro Seite

DCL-C RcdPerPage 8;

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

DCL-S StrARA CHAR(10);                                                   // (Start) Area

DCL-S EndARA CHAR(10);                                                   // (Ende) Area

// Felder für Speicherung der Primärschlüssel

DCL-DS *N;
  FileKey;                                                               // Schlüssel
  DFARA CHAR(10) Overlay(FileKey);                                       // Area
END-DS;

DCL-DS *N;
  ErrKey;                                                                // Schlüssel
  ErrARA CHAR(10) Overlay(ErrKey);                                       // Area
END-DS;

DCL-DS *N;
  PosKey;                                                                // Schlüssel
  PosARA CHAR(10) Overlay(PosKey);                                       // Area
END-DS;

DCL-DS *N;
  SlcKey;                                                                // Schlüssel
  SlcARA CHAR(10) Overlay(SlcKey);                                       // Area
END-DS;

DCL-DS *N;
  CnlKey;                                                                // Schlüssel
  CnlARA CHAR(10) Overlay(CnlKey);                                       // Area
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
DCL-S ITRtvType CHAR(10);                                                // Abrufart
DCL-S ITMsgKey CHAR(4);                                                  // Nachrichtenschlüs
DCL-S ITCmd CHAR(1024);                                                  // Befehl
DCL-S ITRtvCmd CHAR(10);                                                 // Befehl abgerufen
DCL-S ITDsp INT(10) Dim(2);                                              // Anzeige Hilfetext
DCL-S ITNbr INT(10);                                                     // Anzahl Hilfetextm
DCL-S ITLftC INT(10) Dim(2);                                             // Linke Ecke
DCL-S ITRghC INT(10) Dim(2);                                             // Rechte Ecke
DCL-S ITCsrL INT(10) Dim(2);                                             // Cursorposition
DCL-S ITHlp CHAR(1);                                                     // Kontextbezogene H
DCL-S ITMoreKeys ZONED(1);                                               // Weitere Tasten
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
  // Datensatz lesen
  Chain ( PIJOB : PIPOS ) FILERF;
  // Fehler : Der Datensatz konnte nicht gefunden werden
  If Not %Found;
    PIRtCd = '*ERROR';
  Else;
    // Bildschirm initialisieren
    ExSr SR005;
    // Bildschirm verarbeiten
    ExSr SR010;
    // Schlüsselwert zurückgeben
    If Not (PIMode = '*SELECT' And
      PIRtCd = '*SELECT');
      PIARA = *Blanks;
    EndIf;
    // Heap-Speicher freigeben
    DeAlloc OptActPtr;
    DeAlloc OptOptPtr;
    DeAlloc OptKeyPtr;
  EndIf;
EndIf;
// Programm nicht hauptspeicherresident verlassen
*INLR = *On;

//*-----------------------------------------------------------------------------------------*
//* Bildschirm initialisieren                                                               *
//*-----------------------------------------------------------------------------------------*

BegSr SR005;
  // Job initialisieren
  DFJOB = PIJOB;
  // Position initialisieren
  DFPOS = PIPOS;
  // Positionierung initialisieren
  DFPOARA = *Blanks;
  // Befehl initialisieren
  DFCMD = *Blanks;
  ITCMD = *Blanks;
  // Funktionstastenzähler initialisieren
  ITMoreKeys = 1;
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
  // Abrufart für Befehl auffinden initialisieren
  ITRtvType = '*LAST';
  ITMsgKey = *Blanks;
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
    Select;
      // Überschrift für Modus 'Pflegen' anzeigen
    When PIMode = '*EDIT';
      *IN(1) = *ON;
      *IN(2) = *OFF;
      *IN(3) = *OFF;
      *IN(4) = *OFF;
      *IN(5) = *OFF;
      // Überschrift für Modus 'Selektieren' anzeigen
    When PIMode = '*SELECT';
      *IN(1) = *OFF;
      *IN(2) = *ON;
      *IN(3) = *OFF;
      *IN(4) = *OFF;
      *IN(5) = *OFF;
      // Überschrift für Modus 'Anzeigen' anzeigen
    When PIMode = '*DISPLAY';
      *IN(1) = *OFF;
      *IN(2) = *OFF;
      *IN(3) = *OFF;
      *IN(4) = *OFF;
      *IN(5) = *ON;
    EndSl;
    // Funktionstastenzeile initialisieren
    Select;
    When ITMoreKeys = 1;
      *IN(6) = *OFF;
      *IN(7) = *OFF;
    When ITMoreKeys = 2;
      *IN(6) = *OFF;
      *IN(7) = *ON;
    EndSl;
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
      // Wenn die Befehlszeile modifiziert ist, dann diese übernehmen
      If *IN09 = *On;
        ITCmd = DFCMD;
      EndIf;
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
    ENDIF;
    // Funktionstaste : 'F4=Bedienerführung'
    If ITFktKey = '*F4';
      ExSr SR025;
      If PIRtCd = '*F3';
        Leave;
      Else;
        Iter;
      EndIf;
    EndIf;
    // Funktionstaste : 'F6=Erstellen'
    If ITFktKey = '*F6';
      ExSr SR030;
      If PIRtCd = '*F3';
        Leave;
      Else;
        Iter;
      EndIf;
    EndIf;
    // Funktionstaste : 'F9=Auffinden'
    If ITFktKey = '*F9';
      ExSr SR035;
      Iter;
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
    // Funktionstaste : 'F24=Weitere Tasten'
    If ITFktKey = '*F24';
      ExSr SR050;
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
      If DFPOARA <> *Blanks;
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
        If PIRtCd = '*F3' Or
          (PIMode = '*SELECT' And
          PIRtCd = '*SELECT');
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
      // Abrufart für Befehl auffinden initialisieren
      ITRtvType = '*LAST';
      ITMsgKey = *Blanks;
      // Befehl verarbeiten
      If DFCMD <> *Blanks;
        ITMode = '*EXECUTE';
        ExSr SR015;
        Iter;
      EndIf;
      // Programm nach Drücken von Enter ohne sonstige Auswahl verlassen
      PIRtCd = '*ENTER';
      Leave;
    EndIf;
  EndDo;
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Befehl verarbeiten                                                                      *
//*-----------------------------------------------------------------------------------------*

BegSr SR015;
  // Befehl verarbeiten
  ITSts = ExcCmdLin(
   ITMode :                                                              // --> Modus
   ITCmd                                                                 // --> Befehl
  );
  // Befehlszeile aktualisieren
  If ITMode <> '*DSPHELP';
    DFCMD = *Blanks;
    ITCMD = *Blanks;
    If ItSts = '*ERROR';
      ExSr SR035;
    EndIf;
  EndIf;
  // Cursor auf die Befehlszeile positionieren
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
  DFROW010 = 22;
  DFCOL010 = 7;
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Hilfe                                                                                   *
//*-----------------------------------------------------------------------------------------*

BegSr SR020;
  // Hilfe für einen Befehl angefordert
  If DFFLD010 = 'DFCMD' And
    DFCMD <> *Blanks;
    ITMode = '*DSPHELP';
    ExSr SR015;
  Else;
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
      ITHIPG(ITNbr) = 'AREAS';
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
      // Modul 'POARA'
      ITNbr = ITNbr + 1;
      ITHIPG(ITNbr) = 'GENERAL';
      ITHILP(ITNbr) = '*LIBL';
      ITHIMO(ITNbr) = 'POSITIONING';
      If DFFLD010 = 'DFPOARA';
        ITHlp = 'J';
        ITDsp(1) = ITNbr;
        ITDsp(2) = ITNbr;
      EndIf;
      // Modul 'OPT'
      ITNbr = ITNbr + 1;
      ITHIPG(ITNbr) = 'RF0016GP';
      ITHILP(ITNbr) = '*LIBL';
      Select;
      When PIMode = '*EDIT';
        ITHIMO(ITNbr) = 'OPT/EDT';
      When PIMode = '*SELECT';
        ITHIMO(ITNbr) = 'OPT/SLC';
      When PIMode = '*DISPLAY';
        ITHIMO(ITNbr) = 'OPT/DSP';
      EndSl;
      If DFFLD010 = 'DFOPT';
        ITHlp = 'J';
        ITDsp(1) = ITNbr;
        ITDsp(2) = ITNbr;
      EndIf;
      // Modul 'ARA'
      ITNbr = ITNbr + 1;
      ITHIPG(ITNbr) = 'AREAS';
      ITHILP(ITNbr) = '*LIBL';
      ITHIMO(ITNbr) = 'ARA';
      If DFFLD010 = 'DFARA';
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
      // Modul 'LIBL'
      ITNbr = ITNbr + 1;
      ITHIPG(ITNbr) = 'AREAS';
      ITHILP(ITNbr) = '*LIBL';
      ITHIMO(ITNbr) = 'LIBL';
      If DFFLD010 = 'DFLIBL';
        ITHlp = 'J';
        ITDsp(1) = ITNbr;
        ITDsp(2) = ITNbr;
      EndIf;
      // Modul 'CCSID'
      ITNbr = ITNbr + 1;
      ITHIPG(ITNbr) = 'AREAS';
      ITHILP(ITNbr) = '*LIBL';
      ITHIMO(ITNbr) = 'CCSID';
      If DFFLD010 = 'DFCCSID';
        ITHlp = 'J';
        ITDsp(1) = ITNbr;
        ITDsp(2) = ITNbr;
      EndIf;
      // Modul 'CMD'
      If PIMode = '*EDIT' Or
        PIMode = '*DISPLAY';
        ITNbr = ITNbr + 1;
        ITHIPG(ITNbr) = 'GENERAL';
        ITHILP(ITNbr) = '*LIBL';
        ITHIMO(ITNbr) = 'COMMAND';
        If DFFLD010 = 'DFCMD';
          ITHlp = 'J';
          ITDsp(1) = ITNbr;
          ITDsp(2) = ITNbr;
        EndIf;
      EndIf;
      // Modul 'FKT'
      ITNbr = ITNbr + 1;
      ITHIPG(ITNbr) = 'RF0016GP';
      ITHILP(ITNbr) = '*LIBL';
      Select;
      When PIMode = '*EDIT';
        ITHIMO(ITNbr) = 'FKT/EDT';
      When PIMode = '*SELECT';
        ITHIMO(ITNbr) = 'FKT/SLC';
      When PIMode = '*DISPLAY';
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
     ITHlpId :                                                           // --> Help identifi
     ITNbr :                                                             // --> Number of hel
     ITDsp :                                                             // --> Help type
     *Blanks :                                                           // --> Full display
     '*NONE' :                                                           // --> Qualified sea
     'N' :                                                               // --> Display type
     ITLftC :                                                            // --> Upper left co
     ITRghC :                                                            // --> Lower right c
     ITCsrL :                                                            // --> Cursor locati
     ERRC0100                                                            // <-> Error code
    );
  EndIf;
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

BegSr SR025;
  Select;
    // Bedienerführung für das Feld 'Auswahl'
  When DFFLD010 = 'DFOPT';
    // Bedienerführung für das Feld 'Befehl'
  When DFFLD010 = 'DFCMD';
    ITMode = '*PROMPT';
    ExSr SR015;
    // No prompt for the requested field available
  Other;
    ITMsgId = 'ERR0004';
    ITMsgDta = *Blanks;
    ITMsgF = 'RF0016MF';
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
//* Erstellen                                                                               *
//*-----------------------------------------------------------------------------------------*

BegSr SR030;

  // Check action
  If Not MNTAREA_checkAction(
       MNTAREA_CREATE : // --> Action
       PIJOB :          // --> Job
       PIPOS :          // --> Position
       *Blanks :        // --> Area
       ITSUCC :         // <-- Success
       ITXMsg           // <-- Message
     );
    ITMsgId = 'CPF9897';
    ITMsgDta = ITXMsg;
    ITMsgF = 'QCPFMSG';
    LeaveSr;
  EndIf;

  // Funktionstaste : 'F6=Erstellen'
  ITMode = MNTAREA_CREATE;
  SlcARA = *Blanks;
  ExSr SR130;
  // Eine neue Position wurde erstellt
  If PIRtCd = '*ENTER';
    // Die aktuellen Sätze nocheinmal in die Subfile stellen
    ITAct = '*REFRESH';
    ExSr SR075;
    // Wenn keine Sätze gelesen werden konnten, dann die letzten Sätze in die Subfile stellen
    If ITMore = '*NO';
      ITAct = '*BOTTOM';
      ExSr SR075;
    EndIf;
  EndIf;
EndSr;

//*-----------------------------------------------------------------------------------------*
//* Auffinden                                                                               *
//*-----------------------------------------------------------------------------------------*

BegSr SR035;
  // Abrufart für Befehl auffinden initialisieren
  If DFCMD = *Blanks;
    ITRtvType = '*LAST';
    ITMsgKey = *Blanks;
  EndIf;
  // Nächsten Befehl abrufen
  ITSts = RtvCmdLin(
   ITRtvType :                                                           // --> Abrufart
   ITMsgKey :                                                            // <-> Nachrichtensc
   ITCmd :                                                               // <-- Befehl
   ITRtvCmd                                                              // <-- Befehl abgeru
  );
  Select;
    // *** Befehl abgerufen ***
  When ITRtvCmd = '*YES';
    // Befehl in die Befehlszeile übernehmen
    DFCMD = ITCMD;
    // Abrufart für Befehl auffinden initialisieren
    ITRtvType = '*PRV';
    // *** Keinen Befehl abgerufen ***
  When ITRtvCmd = '*NO';
    // Befehl in der Befehlszeile löschen
    DFCMD = *Blanks;
    ITCMD = *Blanks;
    // Abrufart für Befehl auffinden initialisieren
    ITRtvType = '*LAST';
  EndSl;
  // Cursor auf die Befehlszeile positionieren
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
  DFROW010 = 22;
  DFCOL010 = 7;
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
//* Weitere Tasten                                                                          *
//*-----------------------------------------------------------------------------------------*

BegSr SR050;
  If ITMoreKeys = 2;
    // Die erste Funktionstastenzeile anzeigen
    ITMoreKeys = 1;
  Else;
    // Die nächste Funktionstastenzeile anzeigen
    ITMoreKeys = ITMoreKeys + 1;
  EndIf;
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
    ITMsgF = 'RF0016MF';
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
    ITMsgF = 'RF0016MF';
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
  // Befehlszeile initialisieren
  DFCMD = *Blanks;
  ITCMD = *Blanks;
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
    SetGt ( PIJOB : PIPOS : EndARA ) AREARF;
    // *** Rückwärts ***
  When ITAct = '*BACKWARD';
    SetLL ( PIJOB : PIPOS : StrARA ) AREARF;
    // *** Positionieren ***
  When ITAct = '*POSITION';
    Select;
    When ITAra = '*SCREEN';
      SetLL ( PIJOB : PIPOS : DFPOARA ) AREARF;
    When ITAra = '*CANCEL';
      SetLL ( PIJOB : PIPOS : CnlARA ) AREARF;
    When ITAra = '*ERROR';
      SetLL ( PIJOB : PIPOS : ErrARA ) AREARF;
    EndSl;
    // *** Anfang ***
  When ITAct = '*TOP';
    ITCnt = *Zero;
    ITCntC = *Zero;
    ITCntS = *Zero;
    StrARA = *Loval;
    EndARA = *Loval;
    SetGt ( PIJOB : PIPOS : StrARA ) AREARF;
    // *** Ende ***
  When ITAct = '*BOTTOM';
    ITCnt = *Zero;
    ITCntC = *Zero;
    ITCntS = *Zero;
    StrARA = *Hival;
    EndARA = *Hival;
    SetGt ( PIJOB : PIPOS : EndARA ) AREARF;
    // *** Aktualisieren ***
  When ITAct = '*REFRESH';
    SetLL ( PIJOB : PIPOS : StrARA ) AREARF;
  EndSl;
  DoU %EOF Or
    ITCntC = ITPag010;
    // Primärdatei lesen
    Select;
      // *** Vorwärts, Positionierung, Anfang, Aktualisieren ***
    When ITAct = '*FOREWARD' Or
      ITAct = '*POSITION' Or
      ITAct = '*TOP' Or
      ITAct = '*REFRESH';
      ReadE ( PIJOB : PIPOS ) AREARF;
      // *** Rückwärts, Ende ***
    When ITAct = '*BACKWARD' Or
      ITAct = '*BOTTOM';
      ReadPE ( PIJOB : PIPOS ) AREARF;
    EndSl;
    If Not %EOF;
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
      StrARA = XDARA;
    EndIf;
    // Schlüssel für Endsatz der Subfile initialisieren
    If ITCnt1 = ITCnt;
      EndARA = XDARA;
    EndIf;
    // Felder im Subdateisatz füllen
    DFARA = XDARA;
    DFLIB = XDLIB;
    DFLIBL = XDLIBL;
    DFCCSID = XDCCSID;
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
    SetGt ( PIJOB : PIPOS : EndARA ) AREARF;
    ReadE ( PIJOB : PIPOS ) AREARF;
    If %EOF;
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
      ITACT = MNTAREA_NONE;
    When DFOPT = '1';
      ITACT = MNTAREA_SELECT;
    When DFOPT = '2';
      ITACT = MNTAREA_CHANGE;
    When DFOPT = '3';
      ITACT = MNTAREA_COPY;
    When DFOPT = '4';
      ITACT = MNTAREA_DELETE;
    When DFOPT = '5';
      ITACT = MNTAREA_DISPLAY;
    Other;
      ITACT = MNTAREA_ERROR;
    EndSl;

    // The option is not valid
    If ITMsgId = *Blanks And
      ((PIMode = '*EDIT' And
      ITACT <> MNTAREA_NONE And
      ITACT <> MNTAREA_CHANGE And
      ITACT <> MNTAREA_COPY And
      ITACT <> MNTAREA_DELETE And
      ITACT <> MNTAREA_DISPLAY) Or
      (PIMode = '*SELECT' And
      ITACT <> *Blanks And
      ITACT <> MNTAREA_SELECT) Or
      (PIMode = '*DISPLAY' And
      ITACT <> MNTAREA_NONE And
      ITACT <> MNTAREA_DISPLAY));
      ITMsgId = 'ERR0001';
      ITMsgDta = *Blanks;
      ITMsgF = 'RF0016MF';
      *IN(81) = *OFF;
      *IN(82) = *OFF;
      *IN(83) = *ON;
    EndIf;

    // Check action
    If ITMsgId = *Blanks And
       Not MNTAREA_checkAction(
         ITACT :  // --> Action
         PIJOB :  // --> Job
         PIPOS :  // --> Position
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
    // Auswahl : '1=Auswählen'
  When OptOpt(ITOEvl) = '1';
    SlcKey = OptKey(ITOEvl);
    PIARA = SlcARA;
    PIRtCd = '*SELECT';
    // Auswahl : '2=Ändern'
  When OptOpt(ITOEvl) = '2';
    ITMode = MNTAREA_CHANGE;
    SlcKey = OptKey(ITOEvl);
    ExSr SR130;
    // Auswahl : '3=Kopieren'
  When OptOpt(ITOEvl) = '3';
    ITMode = MNTAREA_COPY;
    SlcKey = OptKey(ITOEvl);
    ExSr SR130;
    // Auswahl : '4=Löschen'
  When OptOpt(ITOEvl) = '4';
    ITMode = MNTAREA_DELETE;
    SlcKey = OptKey(ITOEvl);
    ExSr SR130;
    // Auswahl : '5=Anzeigen'
  When OptOpt(ITOEvl) = '5';
    ITMode = MNTAREA_DISPLAY;
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
//* Prozedur 'Einzelsatz' aufrufen                                                          *
//*-----------------------------------------------------------------------------------------*

BegSr SR130;
  RF0017PG(
   ITMode :                                                              // --> Modus
   PIJOB :                                                               // --> Job
   PIPOS :                                                               // --> Position
   SlcARA :                                                              // <-> Area
   PIRtCd                                                                // <-- Rückkehrcode
  );
EndSr;
