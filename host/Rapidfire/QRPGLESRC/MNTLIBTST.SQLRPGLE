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
//* Test MNTLIB via Stored Procedures                                                       *
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
//* 17.11.2017 I   FH   I Creation                                                          *
//*------------+--------+-------------------------------------------------------------------*

CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,');
CTL-OPT DftActGrp(*NO) ActGrp('RAPIDFIRE') BNDDIR('RAPIDFIRE');
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

/Include QCPYSRC,MNTLIB
/Include QCPYSRC,SQLSTS

DCL-S g_SUCCESS CHAR(1);       // Success
DCL-S g_FLD     CHAR(10);      // Field
DCL-S g_MSG     CHAR(256);     // Message
DCL-S g_DMY     IND;           // Dummy
DCL-S g_ACT     CHAR(10);      // Action
DCL-S g_LIB     CHAR(10);      // Library
DCL-S g_SLIB    CHAR(10);      // Shadow library
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

    // *** OPEN FILES ***

    EXEC SQL
      CALL "MNTLIB_openFiles";
    setIgnoreWarnings();
    if (SqlCheckState(sqlState));

      // *** CREATE library MYLIB ***

      EXEC SQL
        CALL "MNTLIB_initialize" (
          '*CREATE',
          'MYJOB',
          '',
          :g_SUCCESS,
          :g_MSG
        );
      setIgnoreWarnings();
      if (SqlCheckState(sqlState));

        if (g_SUCCESS = 'Y');

          EXEC SQL
            CALL "MNTLIB_getValues" (
              :g_LIB,
              :g_SLIB
            );
          setIgnoreWarnings();
          if (SqlCheckState(sqlState));

            g_LIB = 'MYLIB';
            g_SLIB = 'MYSLIB';

            EXEC SQL
              CALL "MNTLIB_setValues" (
                :g_LIB,
                :g_SLIB
              );
            setIgnoreWarnings();
            if (SqlCheckState(sqlState));

              EXEC SQL
                CALL "MNTLIB_check" (
                  :g_SUCCESS,
                  :g_FLD,
                  :g_MSG
                );
              setIgnoreWarnings();
              if (SqlCheckState(sqlState));

                if (g_SUCCESS = 'N');

                  // ... Display error on screen.
                  // ... g_FLD contains the field which caused the error and g_MSG contains the message

                else;

                  EXEC SQL
                    CALL "MNTLIB_book";
                  setIgnoreWarnings();
                  if (SqlCheckState(sqlState));

                    g_DMY = *on;

                  endif;

                endif;

              endif;

            endif;

          endif;

        endif;

      endif;

      // *** CHANGE library MYLIB ***

      EXEC SQL
        CALL "MNTLIB_initialize" (
          '*CHANGE',
          'MYJOB',
          'MYLIB',
          :g_SUCCESS,
          :g_MSG
        );
      setIgnoreWarnings();
      if (SqlCheckState(sqlState));

        if (g_SUCCESS = 'Y');

          EXEC SQL
            CALL "MNTLIB_getValues" (
              :g_LIB,
              :g_SLIB
            );
          setIgnoreWarnings();
          if (SqlCheckState(sqlState));

            g_SLIB = 'MYCHGSLIB';

            EXEC SQL
              CALL "MNTLIB_setValues" (
                :g_LIB,
                :g_SLIB
              );
            setIgnoreWarnings();
            if (SqlCheckState(sqlState));

              EXEC SQL
                CALL "MNTLIB_check" (
                  :g_SUCCESS,
                  :g_FLD,
                  :g_MSG
                );
              setIgnoreWarnings();
              if (SqlCheckState(sqlState));

                if (g_SUCCESS = 'N');

                  // ... Display error on screen.
                  // ... g_FLD contains the field which caused the error and g_MSG contains the message

                else;

                  EXEC SQL
                    CALL "MNTLIB_book";
                  setIgnoreWarnings();
                  if (SqlCheckState(sqlState));

                    g_DMY = *on;

                  endif;

                endif;

              endif;

            endif;

          endif;

        endif;

      endif;

      // *** CHECK ACTION ***

      g_ACT = MNTLIB_CREATE;
      EXEC SQL
        CALL "MNTLIB_checkAction"(
          :g_ACT,
          'MYJOB',
          ' ',
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

      g_ACT = MNTLIB_CHANGE;
      EXEC SQL
        CALL "MNTLIB_checkAction"(
          :g_ACT,
          'MYJOB',
          'MYLIB',
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

      EXEC SQL
        CALL "MNTLIB_getValidActions"(
          'MYJOB',
          'MYLIB',
          :g_NBRACTS,
          :g_ACTS
        );
      setIgnoreWarnings();
      if (SqlCheckState(sqlState));

        g_DMY = *on;

      endif;

      // *** CLOSE FILES ***

      EXEC SQL
        CALL "MNTLIB_closeFiles";
      setIgnoreWarnings();
      if (SqlCheckState(sqlState));

        g_DMY = *on;

      endif;

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
