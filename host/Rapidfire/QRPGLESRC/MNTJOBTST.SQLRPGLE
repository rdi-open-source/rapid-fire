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
//* Test MNTJOB via Stored Procedures                                                       *
//*                                                                                         *
//*------------+--------+-------------------------------------------------------------------*
//*   Datum    I Progr. I Beschreibung                                                      *
//*------------+--------+-------------------------------------------------------------------*
//* 27.10.2017 I   FH   I Creation                                                          *
//*------------+--------+-------------------------------------------------------------------*
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

CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,');
CTL-OPT DftActGrp(*NO) ActGrp('RAPIDFIRE') BNDDIR('RAPIDFIRE');
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

DCL-S g_SUCCESS CHAR(1); // Success
DCL-S g_JOB  CHAR(10); // Job
DCL-S g_DSCR CHAR(35); // Description
DCL-S g_CRTE CHAR(10); // Create environment
DCL-S g_JQ   CHAR(10); // Job queue
DCL-S g_JQL  CHAR(10); // Job queue (Library)
DCL-S g_FLD CHAR(10);  // <-- Feld
DCL-S g_MSG CHAR(256); // <-- Nachricht

// **************************************
// ********** CREATE job MYJOB **********
// **************************************

EXEC SQL
  CALL "MNTJOB_openFiles";

EXEC SQL
  CALL "MNTJOB_initialize" (
    '*CREATE',
    '',
    :g_SUCCESS
  );

if (g_SUCCESS = 'Y');

  EXEC SQL
    CALL "MNTJOB_getValues" (
      :g_JOB,
      :g_DSCR,
      :g_CRTE,
      :g_JQ,
      :g_JQL
    );

  g_JOB = 'MYJOB';
  g_DSCR = 'MY_DESCRIPTION';
  g_CRTE = '*YES';
  g_JQ = 'QBATCH';
  g_JQL = 'QGPL';

  EXEC SQL
    CALL "MNTJOB_setValues" (
      :g_JOB,
      :g_DSCR,
      :g_CRTE,
      :g_JQ,
      :g_JQL
    );

  EXEC SQL
    CALL "MNTJOB_check" (
      :g_FLD,
      :g_MSG,
      :g_SUCCESS
    );

  if (g_SUCCESS = 'N');

    // ... Display error on screen. g_FLD contains the field which caused the error and g_MSG contains the message ...

  else;

    EXEC SQL
      CALL "MNTJOB_book";

  endif;

endif;

EXEC SQL
  CALL "MNTJOB_closeFiles";

// **************************************
// ********** CHANGE job MYJOB **********
// **************************************

EXEC SQL
  CALL "MNTJOB_openFiles";

EXEC SQL
  CALL "MNTJOB_initialize" (
    '*CHANGE',
    'MYJOB',
    :g_SUCCESS
  );

if (g_SUCCESS = 'Y');

  EXEC SQL
    CALL "MNTJOB_getValues" (
      :g_JOB,
      :g_DSCR,
      :g_CRTE,
      :g_JQ,
      :g_JQL
    );

  g_DSCR = 'MY_CHANGED_DESCRIPTION';

  EXEC SQL
    CALL "MNTJOB_setValues" (
      :g_JOB,
      :g_DSCR,
      :g_CRTE,
      :g_JQ,
      :g_JQL
    );

  EXEC SQL
    CALL "MNTJOB_check" (
      :g_FLD,
      :g_MSG,
      :g_SUCCESS
    );

  if (g_SUCCESS = 'N');

    // ... Display error on screen. g_FLD contains the field which caused the error and g_MSG contains the message ...

  else;

    EXEC SQL
      CALL "MNTJOB_book";

  endif;

endif;

EXEC SQL
  CALL "MNTJOB_closeFiles";

*INLR = *ON;
