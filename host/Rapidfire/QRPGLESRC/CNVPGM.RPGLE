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
//* Conversion program                                                                      *
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
//* At least you have to modify the statements starting with ### in this template.          *
//*                                                                                         *
//*-----------------------------------------------------------------------------------------*
//* >>PRE-COMPILER<<                                                                        *
//*   >>CRTCMD<<  CRTBNDRPG SRCFILE(&SL/&SF) SRCMBR(&SM) +                                  *
//*                 PGM(&LI/&OB);                                                           *
//*   >>IMPORTANT<<                                                                         *
//*     >>PARM<<  DFTACTGRP(*NO);                                                           *
//*     >>PARM<<  ACTGRP(RAPIDFIRE);                                                        *
//*   >>END-IMPORTANT<<                                                                     *
//*   >>EXECUTE<<                                                                           *
//* >>END-PRE-COMPILER<<                                                                    *
//*------------+--------+-------------------------------------------------------------------*
//*   Datum    I Progr. I Beschreibung                                                      *
//*------------+--------+-------------------------------------------------------------------*
//* 03.02.2016 I   TR   I STRPREPRC Header hinzugefügt.                                     *
//*-----------------------------------------------------------------------------------------*

// Control

CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,') AlwNull(*USRCTL);
CTL-OPT DftActGrp(*NO) ActGrp('RAPIDFIRE');
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

// ### Define in keyword EXTNAME the file in the way - Extname('MYLIB/MYFILE') -
// You have to specify the file with the new format and NOT the file with the old format.

DCL-DS o_after_Img
       Extname('*LIBL/JOBS') Based(o_pAfter_Img)

       Qualified;
END-DS;

DCL-DS i_before_Img
       Extname('*LIBL/JOBS') Based(i_pBefore_Img)

       Qualified;
END-DS;
// Prototype of this program
// Replace ### with the actual program name

DCL-PR Cnv_Pgm
       ExtPgm('###');
  i_Mode CHAR(10) Const;
  o_pAfter_Img POINTER Const;
  o_after_nMap IND Dim(8000);
  i_after_Count INT(10) Const;
  i_pBefore_Img POINTER Const;
  i_before_nMap IND Const Dim(8000);
  i_before_Count INT(10) Const;
END-PR;

//*-----------------------------------------------------------------------------------------*

// Parameter for this program
//  i_Mode          - Mode: *OPN  Called once before any data is copied.
//                          *CNV  Called for the record that is being copied.
//                          *CLS  Called once when the copy process ends.
//  o_pAfter_Img    - Pointer to the after image data for iMode = '*CNV', else *Null.
//  o_after_nMap    - Array with the null indicators of the after image.
//  i_after_Count   - Number of fields off the after image for iMode = '*CNV', else 0.
//  i_pBefore_Img   - Pointer to the before image data for iMode = '*CNV', else *Null.
//  i_before_nMap   - Array with the null indicators of the before image.
//  i_before_Count  - Number of fields of the before image for iMode = '*CNV', else 0.

DCL-PI Cnv_Pgm;
  i_Mode CHAR(10) Const;
  o_pAfter_Img POINTER Const;
  o_after_nMap IND Dim(8000);
  i_after_Count INT(10) Const;
  i_pBefore_Img POINTER Const;
  i_before_nMap IND Const Dim(8000);
  i_before_Count INT(10) Const;
END-PI;

//*-----------------------------------------------------------------------------------------*

// Actions
Select;
When i_Mode = '*CNV';
  ExSr convert;
  Return;
When i_Mode = '*OPN';
  ExSr open;
  Return;
When i_Mode = '*CLS';
  ExSr close;
  *INLR = *On;
EndSl;

//*-----------------------------------------------------------------------------------------*
//* Convert                                                                                 *
//*-----------------------------------------------------------------------------------------*

BegSr convert;

  // ### Do some conversion stuff

EndSr;

//*-----------------------------------------------------------------------------------------*
//* Open                                                                                    *
//*-----------------------------------------------------------------------------------------*

BegSr open;

  // Open files defined in F-Specifications and/or do some initialization stuff

EndSr;

//*-----------------------------------------------------------------------------------------*
//* Close                                                                                   *
//*-----------------------------------------------------------------------------------------*

BegSr close;

  // Close files defined in F-Specifications and/or do some cleaning stuff

EndSr;
