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
//* Test MNTLIBL via Stored Procedures                                                      *
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
//* 20.11.2017 I   FH   I Creation                                                          *
//*------------+--------+-------------------------------------------------------------------*

CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,');
CTL-OPT DftActGrp(*NO) ActGrp('RAPIDFIRE') BNDDIR('RAPIDFIRE');
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

/Include QCPYSRC,MNTLIBL
/Include QCPYSRC,SQLSTS

DCL-S g_SUCCESS CHAR(1);           // Success
DCL-S g_FLD     CHAR(10);          // Field
DCL-S g_RCD     PACKED(4 :0);      // Record
DCL-S g_MSG     CHAR(256);         // Message
DCL-S g_DMY     IND;               // Dummy
DCL-S g_ACT     CHAR(10);          // Action
DCL-S g_LIBL    CHAR(10);          // Library list
DCL-S g_DSCR    CHAR(35);          // Description
DCL-DS *N;                         // Sequence
  g_SEQX CHAR(1000);
  g_SEQ CHAR(4) DIM(250) OVERLAY(g_SEQX);
END-DS;
DCL-DS *N;                         // Library
  g_LIBX CHAR(2500);
  g_LIB CHAR(10) DIM(250) OVERLAY(g_LIBX);
END-DS;
DCL-S g_NBRACTS PACKED(2 : 0);     // Number of actions
DCL-S g_ACTS    CHAR(250);         // Actions

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
      CALL "MNTLIBL_openFiles";
    setIgnoreWarnings();
    if (SqlCheckState(sqlState));

      // *** CREATE library list MYLIBL ***

      EXEC SQL
        CALL "MNTLIBL_initialize" (
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
            CALL "MNTLIBL_getValues" (
              :g_LIBL,
              :g_DSCR,
              :g_SEQX,
              :g_LIBX
            );
          setIgnoreWarnings();
          if (SqlCheckState(sqlState));

            g_LIBL = 'MYLIBL';
            g_DSCR = 'MYDSCR';
            g_SEQ(1) = '0006';
            g_LIB(1) = 'A';
            g_SEQ(2) = '0004';
            g_LIB(2) = 'B';
            g_SEQ(3) = '0002';
            g_LIB(3) = 'C';

            EXEC SQL
              CALL "MNTLIBL_setValues" (
                :g_LIBL,
                :g_DSCR,
                :g_SEQX,
                :g_LIBX
              );
            setIgnoreWarnings();
            if (SqlCheckState(sqlState));

              EXEC SQL
                CALL "MNTLIBL_check" (
                  :g_SUCCESS,
                  :g_FLD,
                  :g_RCD,
                  :g_MSG
                );
              setIgnoreWarnings();
              if (SqlCheckState(sqlState));

                if (g_SUCCESS = 'N');

                  // ... Display error on screen.
                  // ... g_FLD contains the field which caused the error and g_MSG contains the message

                else;

                  EXEC SQL
                    CALL "MNTLIBL_book";
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

      // *** CHANGE library list MYLIBL ***

      EXEC SQL
        CALL "MNTLIBL_initialize" (
          '*CHANGE',
          'MYJOB',
          'MYLIBL',
          :g_SUCCESS,
          :g_MSG
        );
      setIgnoreWarnings();
      if (SqlCheckState(sqlState));

        if (g_SUCCESS = 'Y');

          EXEC SQL
            CALL "MNTLIBL_getValues" (
              :g_LIBL,
              :g_DSCR,
              :g_SEQX,
              :g_LIBX
            );
          setIgnoreWarnings();
          if (SqlCheckState(sqlState));

            g_DSCR = 'MYCHGDSCR';
            g_SEQ(1) = '0002';
            g_LIB(1) = 'X';
            g_SEQ(2) = '0004';
            g_LIB(2) = 'Y';
            g_SEQ(3) = '0006';
            g_LIB(3) = 'Z';

            EXEC SQL
              CALL "MNTLIBL_setValues" (
                :g_LIBL,
                :g_DSCR,
                :g_SEQX,
                :g_LIBX
              );
            setIgnoreWarnings();
            if (SqlCheckState(sqlState));

              EXEC SQL
                CALL "MNTLIBL_check" (
                  :g_SUCCESS,
                  :g_FLD,
                  :g_RCD,
                  :g_MSG
                );
              setIgnoreWarnings();
              if (SqlCheckState(sqlState));

                if (g_SUCCESS = 'N');

                  // ... Display error on screen.
                  // ... g_FLD contains the field which caused the error and g_MSG contains the message

                else;

                  EXEC SQL
                    CALL "MNTLIBL_book";
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

      g_ACT = MNTLIBL_CREATE;
      EXEC SQL
        CALL "MNTLIBL_checkAction"(
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

      g_ACT = MNTLIBL_CHANGE;
      EXEC SQL
        CALL "MNTLIBL_checkAction"(
          :g_ACT,
          'MYJOB',
          'MYLIBL',
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
        CALL "MNTLIBL_getValidActions"(
          'MYJOB',
          'MYLIBL',
          :g_NBRACTS,
          :g_ACTS
        );
      setIgnoreWarnings();
      if (SqlCheckState(sqlState));

        g_DMY = *on;

      endif;

      // *** CLOSE FILES ***

      EXEC SQL
        CALL "MNTLIBL_closeFiles";
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
