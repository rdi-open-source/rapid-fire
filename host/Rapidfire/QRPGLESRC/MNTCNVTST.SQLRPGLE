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
//* Test MNTCNV via Stored Procedures                                                       *
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
//* 22.11.2017 I   FH   I Creation                                                          *
//*------------+--------+-------------------------------------------------------------------*

CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,');
CTL-OPT DftActGrp(*NO) ActGrp('RAPIDFIRE') BNDDIR('RAPIDFIRE');
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

/Include QCPYSRC,MNTCNV
/Include QCPYSRC,SQLSTS

DCL-S g_SUCCESS CHAR(1);       // Success
DCL-S g_FLD     CHAR(10);      // Field
DCL-S g_MSG     CHAR(256);     // Message
DCL-S g_DMY     IND;           // Dummy
DCL-S g_ACT     CHAR(10);      // Action
DCL-S g_FTC     CHAR(10);      // Field to convert
DCL-S g_RFT     CHAR(10);      // Rename field in old file to
DCL-S g_STM1    CHAR(60);      // Statement
DCL-S g_STM2    CHAR(60);      // Statement
DCL-S g_STM3    CHAR(60);      // Statement
DCL-S g_STM4    CHAR(60);      // Statement
DCL-S g_STM5    CHAR(60);      // Statement
DCL-S g_STM6    CHAR(60);      // Statement
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
      CALL "MNTCNV_openFiles";
    setIgnoreWarnings();
    if (SqlCheckState(sqlState));

      // *** CREATE conversion MYCNV ***

      EXEC SQL
        CALL "MNTCNV_initialize" (
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
            CALL "MNTCNV_getValues" (
              :g_FTC,
              :g_RFT,
              :g_STM1,
              :g_STM2,
              :g_STM3,
              :g_STM4,
              :g_STM5,
              :g_STM6
            );
          setIgnoreWarnings();
          if (SqlCheckState(sqlState));

            g_FTC = 'MYCNV';
            g_RFT = 'MYRFT';
            g_STM1 = 'MYRFT = MYCNV;';
            g_STM2 = *blanks;
            g_STM3 = *blanks;
            g_STM4 = *blanks;
            g_STM5 = *blanks;
            g_STM6 = *blanks;

            EXEC SQL
              CALL "MNTCNV_setValues" (
                :g_FTC,
                :g_RFT,
                :g_STM1,
                :g_STM2,
                :g_STM3,
                :g_STM4,
                :g_STM5,
                :g_STM6
              );
            setIgnoreWarnings();
            if (SqlCheckState(sqlState));

              EXEC SQL
                CALL "MNTCNV_check" (
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
                    CALL "MNTCNV_book";
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

      // *** CHANGE conversion MYCNV ***

      EXEC SQL
        CALL "MNTCNV_initialize" (
          '*CHANGE',
          'MYJOB',
          1,
          'MYCNV',
          :g_SUCCESS,
          :g_MSG
        );
      setIgnoreWarnings();
      if (SqlCheckState(sqlState));

        if (g_SUCCESS = 'Y');

          EXEC SQL
            CALL "MNTCNV_getValues" (
              :g_FTC,
              :g_RFT,
              :g_STM1,
              :g_STM2,
              :g_STM3,
              :g_STM4,
              :g_STM5,
              :g_STM6
            );
          setIgnoreWarnings();
          if (SqlCheckState(sqlState));

            g_RFT = 'MYCHGRFT';

            EXEC SQL
              CALL "MNTCNV_setValues" (
                :g_FTC,
                :g_RFT,
                :g_STM1,
                :g_STM2,
                :g_STM3,
                :g_STM4,
                :g_STM5,
                :g_STM6
              );
            setIgnoreWarnings();
            if (SqlCheckState(sqlState));

              EXEC SQL
                CALL "MNTCNV_check" (
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
                    CALL "MNTCNV_book";
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

      g_ACT = MNTCNV_CREATE;
      EXEC SQL
        CALL "MNTCNV_checkAction"(
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

      g_ACT = MNTCNV_CHANGE;
      EXEC SQL
        CALL "MNTCNV_checkAction"(
          :g_ACT,
          'MYJOB',
          1,
          'MYCNV',
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
        CALL "MNTCNV_getValidActions"(
          'MYJOB',
          1,
          'MYCNV',
          :g_NBRACTS,
          :g_ACTS
        );
      setIgnoreWarnings();
      if (SqlCheckState(sqlState));

        g_DMY = *on;

      endif;

      // *** CLOSE FILES ***

      EXEC SQL
        CALL "MNTCNV_closeFiles";
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
