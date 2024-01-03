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
//* Execute Command Line                                                                    *
//*                                                                                         *
//*-----------------------------------------------------------------------------------------*
//*  >>PRE-COMPILER<<                                                                       *
//*    >>CRTCMD<< CRTRPGMOD    MODULE(&LI/&OB) +                                            *
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
//* Prozedurergebnis                                                                        *
//* ================                                                                        *
//*                                                                                         *
//* *EXCEPTION --> Die Prozedur wurde mit einer Ausnahmebedingung beendet.                  *
//* *ERROR     --> Die Prozedur wurde mit Fehlerstatus normal beendet.                      *
//* *PROCESS   --> Die Prozedur wurde mit Verarbeitungsstatus normal beendet.               *
//*                                                                                         *
//* Prozedurschnittstelle                                                                   *
//* =====================                                                                   *
//*                                                                                         *
//* PIMode     / *EXECUTE --> Der Befehl wird normal verarbeitet.                           *
//*              *PROMPT  --> Der Befehl wird nach einer Bedienerführungsanzeige verarbeitet*
//*              *DSPHELP --> Es wird Online-Hilfetext zum Befehl angezeigt.                *
//*                                                                                         *
//*------------+--------+-------------------------------------------------------------------*
//*   Datum    I Progr. I Beschreibung                                                      *
//*------------+--------+-------------------------------------------------------------------*
//* 01.07.2014 I   FH   I Programm erstellt                                                 *
//*------------+--------+-------------------------------------------------------------------*

CTL-OPT NoMain;
CTL-OPT DatFmt(*Eur) TimFmt(*Eur) DecEdit('0,');
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

// Prozedurenprototyp für Prozedur 'ExcCmdLin'

/Copy QCPYSRC,EXCCMDLIN

// Prozedurenprototyp für Prozedur 'QMHSNDPM'

/Copy QCPYSRC,QMHSNDPM

// Prozedurenprototyp für Prozedur 'QMHRCVPM'

/Copy QCPYSRC,QMHRCVPM

// Prozedurenprototyp für Prozedur 'QMHMOVPM'

/Copy QCPYSRC,QMHMOVPM

// Prozedurenprototyp für Prozedur 'QCAPCMD'

/Copy QCPYSRC,QCAPCMD

// Datenstruktur für Format 'RCVM0100' für Prozedur 'QMHRCVPM'

/Copy QCPYSRC,RCVM0100

// Datenstruktur für Format 'CPOP0100' für Prozedur 'QCAPCMD'

/Copy QCPYSRC,CPOP0100

// Datenstruktur für Format 'ERRC0100' für Fehlercode

/Copy QCPYSRC,ERRC0100

// Sonstige Felddeklarationen

DCL-S ITMsgKey CHAR(4);                                                  // Nachrichtenschlüs
DCL-S ITCmd CHAR(1024);                                                  // Befehl
DCL-S ITLen INT(10);                                                     // Länge

//*=========================================================================================*
//* Befehl von der Befehlszeile ausführen                                                   *
//*=========================================================================================*

DCL-PROC ExcCmdLin EXPORT;

  DCL-PI ExcCmdLin CHAR(10);                                               // <-- Rückkehrwert
    PIMode CHAR(10) Const;                                                 // --> Modus
    PICmd CHAR(1024) Const;                                                // --> Befehl
  END-PI;

  // Programmnachricht mit Befehl nur in das Joblog schreiben, wenn auch ein Befehl ausgeführt
  If PICmd <> *Blanks;
    // Programmnachricht senden
    Reset ERRC0100;
    QMHSNDPM(
     *Blanks :                                                             // --> Message ident
     *Blanks :                                                             // --> Qualified mes
     PICmd :                                                               // --> Message data
     %Size(PICmd) :                                                        // --> Length of mes
     '*RQS' :                                                              // --> Message type
     '*EXT' :                                                              // --> Call stack en
     0 :                                                                   // --> Call stack co
     ITMsgKey :                                                            // <-- Message key
     ERRC0100 :                                                            // <-> Error code
     %Size('*EXT') :                                                       // --> Length of cal
     '*NONE     *NONE' :                                                   // --> Call stack en
     0
    );
    If ERRC0100.Exeption_Id <> *Blanks;
      Return '*EXCEPTION';
    EndIf;
    // Programmnachricht empfangen
    Reset ERRC0100;
    QMHRCVPM(
     RCVM0100 :                                                            // <-- Message infor
     %Size(RCVM0100) :                                                     // --> Length of mes
     'RCVM0100' :                                                          // --> Format name
     '*EXT' :                                                              // --> Call stack en
     0 :                                                                   // --> Call stack co
     '*RQS' :                                                              // --> Message type
     *Blanks :                                                             // --> Message key
     0 :                                                                   // --> Wait time
     '*SAME' :                                                             // --> Message actio
     ERRC0100 :                                                            // <-> Error code
     %Size('*EXT') :                                                       // --> Length of cal
     '*NONE     *NONE'                                                     // --> Call stack en
    );
    If ERRC0100.Exeption_Id <> *Blanks;
      Return '*EXCEPTION';
    EndIf;
  EndIf;
  // Befehl verarbeiten
  Select;
  When PIMode = '*PROMPT';
    CPOP0100.Type_Of_Command_Processing = 2;
  When PIMode = '*EXECUTE';
    CPOP0100.Type_Of_Command_Processing = 2;
  When PIMode = '*DSPHELP';
    CPOP0100.Type_Of_Command_Processing = 3;
  EndSl;
  CPOP0100.DBCS_Data_Handling = '0';
  Select;
  When PIMode = '*PROMPT';
    CPOP0100.Prompter_Action = '1';
  When PIMode = '*EXECUTE';
    CPOP0100.Prompter_Action = '2';
  When PIMode = '*DSPHELP';
    CPOP0100.Prompter_Action = '3';
  EndSl;
  CPOP0100.Command_String_Syntax = '0';
  If PICmd <> *Blanks;
    CPOP0100.Message_Retrieve_Key =
    RCVM0100.Message_Key;
  Else;
    CPOP0100.Message_Retrieve_Key =
    *Blanks;
  EndIf;
  Reset ERRC0100;
  ERRC0100.Bytes_Provided = *Zero;
  CallP(E) QCAPCMD(
    PICmd :                                                                // --> Source comman
    %Size(PICmd) :                                                         // --> Length of sou
    CPOP0100 :                                                             // --> Options contr
    %Size(CPOP0100) :                                                      // --> Options contr
    'CPOP0100' :                                                           // --> Options contr
    ITCmd :                                                                // <-- Changed comma
    %Size(ITCmd) :                                                         // --> Length availa
    ITLen :                                                                // <-- Length availa
    ERRC0100                                                               // <-> Error code
  );
  // Programmnachricht verschieben
  Reset ERRC0100;
  QMHMOVPM(
   *Blanks :                                                               // --> Message key
   '*COMP     ' +
   '*DIAG     ' +
   '*ESCAPE   ' +
   '*INFO     ' :
   4 :                                                                     // --> Number of mes
   '*' :                                                                   // --> To call stack
   1 :                                                                     // --> To call stack
   ERRC0100 :                                                              // <-> Error code
   %Size('*') :                                                            // --> Length of to
   '*NONE     *NONE' :                                                     // --> To call stack
   '*CHAR' :                                                               // --> To call stack
   '*' :                                                                   // --> From call sta
   0                                                                       // --> From call sta
  );
  If ERRC0100.Exeption_Id <> *Blanks;
    Return '*EXCEPTION';
  EndIf;
  // Prozedur verlassen
  If %Error;
    Return '*ERROR';
  Else;
    Return '*PROCESS';
  EndIf;

END-PROC ExcCmdLin;
