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
//* Test MNTAREA via Stored Procedures                                                      *
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
//* 21.11.2017 I   FH   I Creation                                                          *
//*------------+--------+-------------------------------------------------------------------*

CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,');
CTL-OPT DftActGrp(*NO) ActGrp('RAPIDFIRE') BNDDIR('RAPIDFIRE');
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

/Include QCPYSRC,MNTAREA
/Include QCPYSRC,SQLSTS

DCL-S g_SUCCESS CHAR(1);       // Success
DCL-S g_FLD     CHAR(10);      // Field
DCL-S g_MSG     CHAR(256);     // Message
DCL-S g_DMY     IND;           // Dummy
DCL-S g_ACT     CHAR(10);      // Action
DCL-S g_ARA     CHAR(10);      // Area
DCL-S g_LIB     CHAR(10);      // Library
DCL-S g_LIBL    CHAR(10);      // Library list
DCL-S g_CCSID   CHAR(10);      // Coded character set ID
DCL-S g_CEXT    CHAR(1024);    // Command extension
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
      CALL "MNTAREA_openFiles";
    setIgnoreWarnings();
    if (SqlCheckState(sqlState));

      // *** CREATE area MYAREA ***

      EXEC SQL
        CALL "MNTAREA_initialize" (
          '*CREATE',
          'MYJOB',
          1,
          '',
          :g_SUCCESS,
          :g_MSG
        );
      setIgnoreWarnings();
      if (SqlCheckState(sqlState));

        if (g_SUCCESS = 'Y');

          EXEC SQL
            CALL "MNTAREA_getValues" (
              :g_ARA,
              :g_LIB,
              :g_LIBL,
              :g_CCSID,
              :g_CEXT
            );
          setIgnoreWarnings();
          if (SqlCheckState(sqlState));

            g_ARA = 'MYAREA';
            g_LIB = 'MYLIB';
            g_LIBL = 'MYLIBL';
            g_CCSID = '273';
            g_CEXT = *blanks;

            EXEC SQL
              CALL "MNTAREA_setValues" (
                :g_ARA,
                :g_LIB,
                :g_LIBL,
                :g_CCSID,
                :g_CEXT
              );
            setIgnoreWarnings();
            if (SqlCheckState(sqlState));

              EXEC SQL
                CALL "MNTAREA_check" (
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
                    CALL "MNTAREA_book";
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

      // *** CHANGE area MYAREA ***

      EXEC SQL
        CALL "MNTAREA_initialize" (
          '*CHANGE',
          'MYJOB',
          1,
          'MYAREA',
          :g_SUCCESS,
          :g_MSG
        );
      setIgnoreWarnings();
      if (SqlCheckState(sqlState));

        if (g_SUCCESS = 'Y');

          EXEC SQL
            CALL "MNTAREA_getValues" (
              :g_ARA,
              :g_LIB,
              :g_LIBL,
              :g_CCSID,
              :g_CEXT
            );
          setIgnoreWarnings();
          if (SqlCheckState(sqlState));

            g_CCSID = '1141';

            EXEC SQL
              CALL "MNTAREA_setValues" (
                :g_ARA,
                :g_LIB,
                :g_LIBL,
                :g_CCSID,
                :g_CEXT
              );
            setIgnoreWarnings();
            if (SqlCheckState(sqlState));

              EXEC SQL
                CALL "MNTAREA_check" (
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
                    CALL "MNTAREA_book";
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

      g_ACT = MNTAREA_CREATE;
      EXEC SQL
        CALL "MNTAREA_checkAction"(
          :g_ACT,
          'MYJOB',
          1,
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

      g_ACT = MNTAREA_CHANGE;
      EXEC SQL
        CALL "MNTAREA_checkAction"(
          :g_ACT,
          'MYJOB',
          1,
          'MYAREA',
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
        CALL "MNTAREA_getValidActions"(
          'MYJOB',
          1,
          'MYAREA',
          :g_NBRACTS,
          :g_ACTS
        );
      setIgnoreWarnings();
      if (SqlCheckState(sqlState));

        g_DMY = *on;

      endif;

      // *** CLOSE FILES ***

      EXEC SQL
        CALL "MNTAREA_closeFiles";
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
