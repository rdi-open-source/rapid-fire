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
//* Add Activity Schedule                                                                   *
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
//* 20.04.2016 I   FH   I Programm erstellt                                                 *
//*------------+--------+-------------------------------------------------------------------*

// Control

CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,');
CTL-OPT DftActGrp(*NO) ActGrp('RAPIDFIRE') BNDDIR('RAPIDFIRE');
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

DCL-F ACTSCHD Disk(*EXT) USAGE(*OUTPUT) Rename(ACTSCHD : ACTSCHDRF)
      Commit(g_CLKL) Infsr(*PSSR) UsrOpn;

//*-----------------------------------------------------------------------------------------*

// Prozedurenprototyp für Prozedur 'ADDACTSCHD'

DCL-PR ADDACTSCHD;
  *N CHAR(96);                                                           // <-> Parameter
  *N INT(10) Const;                                                      // --> Länge "Parame
END-PR;

// Prozedurenschnittstelle für Prozedur 'ADDACTSCHD'

DCL-PI ADDACTSCHD;
  pep_Prm CHAR(96);                                                      // <-> Parameter
  pep_LngPrm INT(10) Const;                                              // --> Länge "Parame
END-PI;

//*-----------------------------------------------------------------------------------------*

// Parameter

DCL-DS g_Prm LEN(96);
  g_FILE CHAR(10);                                                       // File
  g_LIB CHAR(10);                                                        // Library
  g_MBR CHAR(10);                                                        // Member
  g_EVNT CHAR(1);                                                        // Trigger event
                                                                         // 1 = *INSERT
                                                                         // 2 = *DELETE
                                                                         // 3 = *UPDATE
  g_TIME CHAR(1);                                                        // Trigger time
                                                                         // 1 = *AFTER
                                                                         // 2 = *BEFORE
  g_CLKL CHAR(1);                                                        // Commit Lock Level
  g_RSV1 CHAR(3);                                                        // Reserved
  g_CSID INT(10);                                                        // CCSID
  g_RSV2 CHAR(8);                                                        // ReservdD
  g_BEOF INT(10);                                                        // Offset to (Before
  g_BELN INT(10);                                                        // Length of (Before
  g_BENO INT(10);                                                        // Offset to (Before
  g_BENL INT(10);                                                        // Length of (Before
  g_AFOF INT(10);                                                        // Offset to (After)
  g_AFLN INT(10);                                                        // Length of (After)
  g_AFNO INT(10);                                                        // Offset to (After
  g_AFNL INT(10);                                                        // Length of (After
  g_RSV3 CHAR(16);                                                       // Reserved
END-DS;

// *BEFORE-Image

DCL-DS BEF_JOBS ExtName('JOBS')
       Prefix(BEF_)
       Based(BEF_JOBS_Ptr);
END-DS;

DCL-S BEF_JOBS_Ptr POINTER;

// *AFTER-Image

DCL-DS AFT_JOBS ExtName('JOBS')
       Prefix(AFT_)
       Based(AFT_JOBS_Ptr);
END-DS;

DCL-S AFT_JOBS_Ptr POINTER;

// Felddeklarationen

DCL-S g_Loop IND Inz(*On);                                               // Schleife
DCL-S g_STR TIME(*ISO);                                                  // Start
DCL-S g_CNT ZONED(5);                                                    // Zähler

//*-----------------------------------------------------------------------------------------*

// Parameter übertragen
g_Prm = pep_Prm;

// *BEFORE-Image ermitteln
BEF_JOBS_Ptr = %Addr(pep_Prm) + g_BEOF;

// *AFTER-Image ermitteln
AFT_JOBS_Ptr = %Addr(pep_Prm) + g_AFOF;

// Ein Satz wurde eingefügt
If g_EVNT = '1';
  Open ACTSCHD;
  g_STR = *LoVal;
  Clear *All ACTSCHDRF;
  XFJOB = AFT_X1JOB;
  For g_CNT = 1 To 288 By 1;
    XFSTR = g_STR;
    If XFSTR = *LoVal;
      XFSTR = XFSTR + %Seconds(1);
    EndIf;
    XFEND = g_STR + %Minutes(5) - %Seconds(1);
    XFACT = 'Y';
    Write ACTSCHDRF;
    g_STR = g_STR + %Minutes(5);
  EndFor;
  Close ACTSCHD;
EndIf;

// Programm nicht hauptspeicherresident verlassen
*INLR = *On;

//*-----------------------------------------------------------------------------------------*
//* Error handler                                                                           *
//*-----------------------------------------------------------------------------------------*

BegSr *PSSR;
EndSr '*CANCL';
