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
//* Rapid Fire                                                                              *
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
//* 01.07.2014 I   FH   I Programm erstellt                                                 *
//*------------+--------+-------------------------------------------------------------------*

// Control

CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,');
CTL-OPT DftActGrp(*NO) ActGrp('RAPIDFIRE') BNDDIR('RAPIDFIRE');
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

DCL-F RFINTRO WORKSTN(*EXT) USAGE(*INPUT : *OUTPUT) USROPN;

//*-----------------------------------------------------------------------------------------*

// Procedure interface

/Include QCPYSRC,RF

DCL-PI RF;
END-PI;

//*-----------------------------------------------------------------------------------------*

// Includes

/Include QCPYSRC,RF0001PG
/Include QCPYSRC,RF0022PG
/Include QCPYSRC,EXCCMD
/Include QCPYSRC,FILE
/Include QCPYSRC,QTNRCMTI
/Include QCPYSRC,CMTI0100
/Include QCPYSRC,ERRC0100
/Include QCPYSRC,LANGUAGE

// Global field declarations

DCL-S g_Sts CHAR(10);  // Message-Id.
DCL-S g_Prc CHAR(10);  // Processing
DCL-S g_JOB CHAR(10);  // Job
DCL-S g_RtCd CHAR(10); // Return code
DCL-S g_Rslt CHAR(1); // Result

//*-----------------------------------------------------------------------------------------*

If Not FILE_is_File_Journaled(
  '*LIBL' : // --> Library - File
  'JOBS'    // --> File
);

  // The Rapid Fire files are not journaled
  RF0022PG();

Else;

  // Set languge
  g_RSLT = LANGUAGE_setLanguage();

  // Retrieve commitment informations
  Reset ERRC0100;
  QTNRCMTI(
   CMTI0100 :        // <-- Receiver variable
   %Size(CMTI0100) : // --> Length of receiver variable
   'CMTI0100' :      // --> Format name
   ERRC0100          // <-> Error code
  );

  // commitment informations retrieved
  If ERRC0100.Exeption_Id = *Blanks;

    // Initialize processing
    g_Prc = '*YES';

    // Commitment control has to be started
    If CMTI0100.Commit_Definition_Status = 'I';

      // Start commitment control
      EXCCMD_execute_Command(
       'STRCMTCTL ' +
       'LCKLVL(*CHG) ' +
       'CMTSCOPE(*ACTGRP) ' +
       'TEXT(''Rapid Fire'')'
      );

      // commitment control could not be started
      If EXCCMD_Message_Id <> *Blanks;
        g_Prc = '*NO';
      EndIf;

    EndIf;

    If g_Prc = '*YES';

      // Show main panel
      Open RFINTRO;
      ExFmt DSPF010;
      Close RFINTRO;

      // Maintain Jobs
      RF0001PG(
       '*EDIT' : // --> Modus
       *Blanks : // --> Filter Job
       g_JOB :   // <-> Job
       g_RtCd    // <-- Rückkehrcode
      );

      // Commitment control has to be ended
      If CMTI0100.Commit_Definition_Status = 'I';
        // Commitment-Control beenden
        EXCCMD_execute_Command(
        'ENDCMTCTL'
        );
      EndIf;

    EndIf;

  EndIf;

  // Reset languge
  If g_RSLT = 'Y';
    LANGUAGE_resetLanguage();
  EndIf;

EndIf;

// End of program
*INLR = *On;
