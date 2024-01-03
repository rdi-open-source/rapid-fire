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
//* Test                                                                                    *
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
//* 07.09.2020 I   FH   I Programm erstellt                                                 *
//*------------+--------+-------------------------------------------------------------------*

/Include QCPYSRC,QYASPOL
/Include QCPYSRC,YASP0200
/Include QCPYSRC,ERRC0100
/Include QCPYSRC,OPNLSTINF

DCL-DS QYASPOL_FLTINF Qualified;
  *N         INT(10) INZ(16);  // --> Size of filter entry
  *N         INT(10) INZ(1);   // --> Filter key
  *N         INT(10) INZ(4);   // --> Size of filter data
  ASP_Number INT(10);          // --> Filter data
END-DS;

QYASPOL_FLTINF.ASP_Number = 1;

Reset ERRC0100;
QYASPOL(
  YASP0200 :                   // <-- Receiver variable
  %Size(YASP0200) :            // --> Length of receiver variable
  OPNLSTINF :                  // <-- List information
  -1 :                         // --> Number of records to return
  1 :                          // --> Number of filters
  QYASPOL_FLTINF :             // --> Filter information
  'YASP0200' :                 // --> Format name
  ERRC0100                     // <-> Error code
);

If ERRC0100.Exeption_Id = *Blanks;
EndIf;

*INLR = *On;
