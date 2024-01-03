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
//* Complete Status Entry                                                                   *
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
//*------------+--------+-------------------------------------------------------------------*
//*   Datum    I Progr. I Beschreibung                                                      *
//*------------+--------+-------------------------------------------------------------------*
//* 26.10.2017 I   FH   I Creation                                                          *
//*------------+--------+-------------------------------------------------------------------*

CTL-OPT NOMAIN;
CTL-OPT DATFMT(*ISO) TIMFMT(*ISO) DECEDIT('0,');
CTL-OPT COPYRIGHT('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT BNDDIR('RAPIDFIRE');
CTL-OPT EXTBININT(*YES);

//*-----------------------------------------------------------------------------------------*

DCL-F JOBS
      DISK(*EXT)
      USAGE(*INPUT)
      KEYED
      RENAME(JOBS : JOBRF)
      USROPN;

DCL-F FILES
      DISK(*EXT)
      USAGE(*INPUT)
      KEYED
      RENAME(FILES : FILERF)
      USROPN;

DCL-F AREAS
      DISK(*EXT)
      USAGE(*INPUT)
      KEYED
      RENAME(AREAS : AREARF)
      USROPN;

DCL-F MEMBERS
      DISK(*EXT)
      USAGE(*INPUT)
      KEYED
      RENAME(MEMBERS : MBRRF)
      USROPN;

DCL-F LIBRARIES
      DISK(*EXT)
      USAGE(*INPUT)
      KEYED
      RENAME(LIBRARIES : LIBRF)
      USROPN;

//*-----------------------------------------------------------------------------------------*

/INCLUDE QCPYSRC,CPLSTSE

/INCLUDE QCPYSRC,FILE

//*=========================================================================================*
//* Open                                                                                    *
//*=========================================================================================*

DCL-PROC CPLSTSE_open EXPORT;

  DCL-PI CPLSTSE_open;
  END-PI;

  Open JOBS;
  Open FILES;
  Open AREAS;
  Open MEMBERS;
  Open LIBRARIES;

END-PROC CPLSTSE_open;

//*=========================================================================================*
//* Close                                                                                   *
//*=========================================================================================*

DCL-PROC CPLSTSE_close EXPORT;

  DCL-PI CPLSTSE_close;
  END-PI;

  Close JOBS;
  Close FILES;
  Close AREAS;
  Close MEMBERS;
  Close LIBRARIES;

END-PROC CPLSTSE_close;

//*=========================================================================================*
//* Complete Status Entry                                                                   *
//*=========================================================================================*

DCL-PROC CPLSTSE_completeStatusEntry EXPORT;

  DCL-PI CPLSTSE_completeStatusEntry;
    p_JOB  CHAR(10)       CONST; // --> Job
    p_POS  PACKED(6 : 0)  CONST; // --> Position
    p_ARA  CHAR(10)       CONST; // --> Area
    p_FILE CHAR(10);             // <-- File
    p_LIB  CHAR(10);             // <-- Library
    p_RPL  PACKED(10 : 0);       // <-- Records in production library
    p_RSL  PACKED(10 : 0);       // <-- Records in shadow library
    p_RTO  PACKED(10 : 0);       // <-- Records to copy
    p_RCO  PACKED(10 : 0);       // <-- Records copied
    p_ETC  CHAR(11);             // <-- Estimated time
    p_RDK  PACKED(10 : 0);       // <-- Records with duplicate key
    p_CTA  PACKED(10 : 0);       // <-- Changes to apply
    p_CAP  PACKED(10 : 0);       // <-- Changes applied
    p_PRC  PACKED(3 : 0);        // <-- Percent done
  END-PI;

  DCL-S l_RTO PACKED(10 : 0);   // Records to copy
  DCL-S l_SEC PACKED(9 : 0);    // Seconds
  DCL-S l_MIN PACKED(9 : 0);    // Minutes
  DCL-S l_HRS PACKED(9 : 0);    // Hours
  DCL-S l_DAY PACKED(9 : 0);    // Days

  p_FILE = *Blanks;
  p_LIB = *Blanks;
  p_RPL = *Zero;
  p_RSL = *Zero;
  p_RTO = *Zero;
  p_RCO = *Zero;
  p_ETC = *Blanks;
  p_RDK = *Zero;
  p_CTA = *Zero;
  p_CAP = *Zero;
  p_PRC = *Zero;
  Chain ( p_JOB ) JOBRF;
  If %Found;
    Chain ( p_JOB : p_POS ) FILERF;
    If %Found;
      Chain ( p_JOB : p_POS : p_ARA ) AREARF;
      If %Found;
        p_FILE = X3FILE;
        p_LIB = XDLIB;
        p_RPL = *Zero;
        p_RSL = *Zero;
        SetLL ( p_JOB : p_POS : p_ARA ) MBRRF;
        DoU %EOF;
          ReadE ( p_JOB : p_POS : p_ARA ) MBRRF;
          If Not %EOF;
            p_RPL += FILE_get_Number_Of_Records(
              XDLIB :  // --> Library
              XDFILE : // --> File
              X7MBR    // --> Member
            );
            Chain ( p_JOB : XDLIB ) LIBRF;
            If %Found;
              p_RSL += FILE_get_Number_Of_Records(
                X2SLIB : // --> Library
                XDFILE : // --> File
                X7MBR    // --> Member
              );
            EndIf;
            p_RDK += X7RDK;
          EndIf;
        EndDo;
        l_RTO = XDRTO;
        p_RCO = XDRCO;
        If p_RCO > l_RTO;
          l_RTO = p_RCO;
        EndIf;
        p_RTO = l_RTO - p_RCO;
        p_CTA = XDCTA - XDCAP;
        p_CAP = XDCAP;
        p_ETC = *Blanks;
        p_PRC = *Zero;
        If X1STS <> '*RDY';
          If p_RCO = l_RTO;
            p_ETC = '*COMPLETED';
            p_PRC = 100;
          Else;
            If p_RCO = *Zero;
              p_ETC = '*PENDING';
            Else;
              l_SEC = (XDTCS / p_RCO * l_RTO) - XDTCS;
              l_MIN = l_SEC / 60;
              l_SEC = l_SEC - (l_MIN * 60);
              l_HRS = l_MIN / 60;
              l_MIN = l_MIN - (l_HRS * 60);
              l_DAY = l_HRS / 24;
              l_HRS = l_HRS - (l_DAY * 24);
              p_ETC =
                %SubSt(%EditC(l_DAY : 'X') : 8 : 2) + '-' +
                %SubSt(%EditC(l_HRS : 'X') : 8 : 2) + ':' +
                %SubSt(%EditC(l_MIN : 'X') : 8 : 2) + ':' +
                %SubSt(%EditC(l_SEC : 'X') : 8 : 2);
              p_PRC = 100 / l_RTO * p_RCO;
            EndIf;
          EndIf;
        EndIf;
      EndIf;
    EndIf;
  EndIf;

END-PROC CPLSTSE_completeStatusEntry;
