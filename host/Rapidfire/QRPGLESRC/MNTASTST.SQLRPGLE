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
//* Test MNTAS via Stored Procedures                                                        *
//*                                                                                         *
//*-----------------------------------------------------------------------------------------*
//*  >>PRE-COMPILER<<                                                                       *
//*    >>CRTCMD<< CRTSQLRPGI   OBJ(&LI/&OB) +                                               *
//*                              SRCFILE(&SL/&SF) SRCMBR(&SM) +                             *
//*                              OBJTYPE(*PGM);                                             *
//*    >>IMPORTANT<<                                                                        *
//*      >>PARM<<  OUTPUT(*PRINT);                                                          *
//*      >>PARM<<  GENLVL(0);                                                               *
//*      >>PARM<<  DBGVIEW(*SOURCE);                                                        *
//*      >>PARM<<  OPTION(*EVENTF);                                                         *
//*      >>PARM<<  TGTRLS(&TR);                                                             *
//*    >>END-IMPORTANT<<                                                                    *
//*    >>EXECUTE<<                                                                          *
//*  >>END-PRE-COMPILER<<                                                                   *
//*------------+--------+-------------------------------------------------------------------*
//*   Datum    I Progr. I Beschreibung                                                      *
//*------------+--------+-------------------------------------------------------------------*
//* 24.11.2017 I   FH   I Creation                                                          *
//*------------+--------+-------------------------------------------------------------------*

CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,');
CTL-OPT DftActGrp(*NO) ActGrp('RAPIDFIRE') BNDDIR('RAPIDFIRE');
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

/Include QCPYSRC,MNTAS
/Include QCPYSRC,SQLSTS

DCL-S g_SUCCESS CHAR(1);   // Success
DCL-S g_MSG     CHAR(256); // Message
DCL-S g_DMY     IND;       // Dummy
DCL-S g_TIME    TIME;      // Time
DCL-S g_ACT     CHAR(10);  // Action
DCL-S g_NBRACTS PACKED(2 : 0); // Number of actions
DCL-S g_ACTS    CHAR(250);     // Actions

//*-----------------------------------------------------------------------------------------*

Exec SQL
  Set Option
  DatFmt=*ISO,
  TimFmt=*ISO,
  Commit=*NONE,
  CloSQLCsr=*ENDMOD;

// *** START ***

EXEC SQL
  CALL "RAPIDFIRE_start"(
    :g_SUCCESS,
    :g_MSG
  );
setIgnoreWarnings();
if (SqlCheckState(sqlState));

  if (g_SUCCESS = 'Y');

    // *** SET ACTIVITY ***

    g_TIME = T'00.15.00';
    EXEC SQL
      CALL "MNTAS_setActivity"(
        'MYJOB',
        :g_TIME
      );
    setIgnoreWarnings();
    if (SqlCheckState(sqlState));

      g_DMY = *on;

    endif;

    // *** CHECK ACTION ***

    g_ACT = MNTAS_ACTVTY;
    g_TIME = T'00.15.00';
    EXEC SQL
      CALL "MNTAS_checkAction"(
        :g_ACT,
        'MYJOB',
        :g_TIME,
        :g_SUCCESS,
        :g_MSG
      );
    setIgnoreWarnings();
    if (SqlCheckState(sqlState));

      if (g_SUCCESS = 'N');

        // ... Display error on screen.
        // ... g_MSG contains the message

      else;

        g_DMY = *on;

      endif;

    endif;

    // *** GET VALID ACTIONS ***

    g_TIME = T'00.15.00';
    EXEC SQL
      CALL "MNTAS_getValidActions"(
        'MYJOB',
        :g_TIME,
        :g_NBRACTS,
        :g_ACTS
      );
    setIgnoreWarnings();
    if (SqlCheckState(sqlState));

      g_DMY = *on;

    endif;

    // *** STOP ***

    EXEC SQL
      CALL "RAPIDFIRE_stop"(
        :g_SUCCESS,
        :g_MSG
      );
    setIgnoreWarnings();
    if (SqlCheckState(sqlState));

      g_DMY = *on;

    endif;

  endif;

endif;

*INLR = *ON;
