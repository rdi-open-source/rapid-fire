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
//*  Boolean Operations                                                                     *
//*                                                                                         *
//*-----------------------------------------------------------------------------------------*
//*  >>PRE-COMPILER<<                                                                       *
//*    >>CRTCMD<< CRTRPGMOD    MODULE(&LI/&OB) +                                            *
//*                              SRCFILE(&SL/&SF) SRCMBR(&SM);                              *
//*    >>IMPORTANT<<                                                                        *
//*      >>PARM<<  TRUNCNBR(*NO);                                                           *
//*      >>PARM<<  DBGVIEW(*ALL);                                                           *
//*      >>PARM<<  OPTION(*EVENTF);                                                         *
//*      >>PARM<<  TGTRLS(&TR);                                                             *
//*    >>END-IMPORTANT<<                                                                    *
//*    >>EXECUTE<<                                                                          *
//*  >>END-PRE-COMPILER<<                                                                   *
//*------------+--------+-------------------------------------------------------------------*
//*   Datum    I Progr. I Beschreibung                                                      *
//*------------+--------+-------------------------------------------------------------------*
//* 04.02.2016 I   TR   I Serviceprogramm erstellt.                                         *
//*------------+--------+-------------------------------------------------------------------*

// Control

CTL-OPT NoMain;
CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,');
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

// Exports

/include QCPYSRC,BOOL                       Boolean Operations

//*-----------------------------------------------------------------------------------------*
//* Tests whether one or more bits are set in a given value.                                *
//*-----------------------------------------------------------------------------------------*
//* Parameters:                                                                             *
//*  i_testBit     - The bits that are tested.                                              *
//*  i_value       - The numeric value that is tested for the bits.                         *
//*                                                                                         *
//* Returns:                                                                                *
//*  TRUE when all bits of i_value are set in i_testBit, else FALSE.                        *
//*-----------------------------------------------------------------------------------------*

DCL-PROC isBitOn EXPORT;

  DCL-PI *N IND;
    i_testBit UNS(10) value;
    i_value UNS(10) value;
  END-PI;

  return %bitand(i_testBit: i_value) = i_testBit;

END-PROC;
