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
//* Test Service Program SQLSTS                                                             *
//*                                                                                         *
//*------------+--------+-------------------------------------------------------------------*
//*   Datum    I Progr. I Beschreibung                                                      *
//*------------+--------+-------------------------------------------------------------------*
//* 20.12.2016 I   FH   I STRPREPRC Header hinzugefügt.                                     *
//*-----------------------------------------------------------------------------------------*
//* >>PRE-COMPILER<<                                                                        *
//*   >>CRTCMD<< CRTSQLRPGI OBJ(&LI/&OB) SRCFILE(&SL/&SF) SRCMBR(&SM) OBJTYPE(*PGM);        *
//*   >>IMPORTANT<<                                                                         *
//*     >>PARM<< CLOSQLCSR(*ENDMOD);                                                        *
//*     >>PARM<< OUTPUT(*PRINT);                                                            *
//*     >>PARM<< GENLVL(0);                                                                 *
//*     >>PARM<< COMPILEOPT('DFTACTGRP(*NO) DBGVIEW(&U1) ACTGRP(&U2) TRUNCNBR(*NO)');       *
//*     >>PARM<< TGTRLS(&TR);                                                               *
//*     >>PARM<< OPTION(*EVENTF);                                                           *
//*   >>END-IMPORTANT<<                                                                     *
//*   >>EXECUTE<<                                                                           *
//* >>END-PRE-COMPILER<<                                                                    *
//*-----------------------------------------------------------------------------------------*

// Control

CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,') AlwNull(*USRCTL);
CTL-OPT DftActGrp(*NO) ActGrp('RAPIDFIRE') BNDDIR('RAPIDFIRE');
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

// Includes

/Include QCPYSRC,WHOAMI
/Include QCPYSRC,SQLSTS

//*-----------------------------------------------------------------------------------------*

// Set SQL options

Exec SQL
  Set Option
  DatFmt=*ISO,
  TimFmt=*ISO,
  Commit=*NONE,
  CloSQLCsr=*ENDMOD;

//*-----------------------------------------------------------------------------------------*

// Execute an SQL Statement with a warning
Exec SQL
  Delete From SQLLOG;
setIgnoreWarnings();
SqlCheckState(sqlState);

// End of program
*INLR = *On;
