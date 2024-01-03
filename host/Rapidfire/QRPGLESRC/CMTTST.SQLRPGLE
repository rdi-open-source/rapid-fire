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
//* Test Commit via Stored Procedures                                                       *
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
//* 14.12.2017 I   FH   I Creation                                                          *
//*------------+--------+-------------------------------------------------------------------*

CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,');
CTL-OPT DftActGrp(*NO) ActGrp('RAPIDFIRE') BNDDIR('RAPIDFIRE');
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

/Include QCPYSRC,MNTJOB
/Include QCPYSRC,SQLSTS

DCL-S g_SUCCESS CHAR(1);       // Success
DCL-S g_FLD     CHAR(10);      // Field
DCL-S g_MSG     CHAR(256);     // Message
DCL-S g_DMY     IND;           // Dummy
DCL-S g_JOB     CHAR(10);      // Job
DCL-S g_DSCR    CHAR(35);      // Description
DCL-S g_CRTE    CHAR(10);      // Create environment
DCL-S g_JQ      CHAR(10);      // Job queue
DCL-S g_JQL     CHAR(10);      // Job queue (Library)
DCL-S g_LIB     CHAR(10);      // Library
DCL-S g_SLIB    CHAR(10);      // Shadow library
DCL-S g_OK      IND;           // OK

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

    // *** SET AUTO COMMIT TO OFF ***

    EXEC SQL
      CALL "RAPIDFIRE_setAutoCommit"(
        'N',
        :g_SUCCESS,
        :g_MSG
      );
    setIgnoreWarnings();
    if (SqlCheckState(sqlState));

      if (g_SUCCESS = 'Y');

        // *** OPEN FILES ***

        EXEC SQL
          CALL "MNTJOB_openFiles";
        setIgnoreWarnings();
        if (SqlCheckState(sqlState));

          EXEC SQL
            CALL "MNTLIB_openFiles";
          setIgnoreWarnings();
          if (SqlCheckState(sqlState));

            // Set OK
            g_OK = *on;

            // *** CREATE job MYJOB ***

            if (g_OK);

              g_OK = *off;

              EXEC SQL
                CALL "MNTJOB_initialize" (
                  '*CREATE',
                  '',
                  :g_SUCCESS,
                  :g_MSG
                );
              setIgnoreWarnings();
              if (SqlCheckState(sqlState));

                if (g_SUCCESS = 'Y');

                  g_JOB = 'MYJOB';
                  g_DSCR = 'MYDSCR';
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
                  setIgnoreWarnings();
                  if (SqlCheckState(sqlState));

                    // *** BOOK ***

                    EXEC SQL
                      CALL "MNTJOB_book";
                    setIgnoreWarnings();
                    if (SqlCheckState(sqlState));

                      g_OK = *on;

                    endif;

                  endif;

                endif;

              endif;

            endif;

            // *** CREATE library MYLIB ***

            if (g_OK);

              g_OK = *off;

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

                  g_LIB = 'MYLIB';
                  g_SLIB = 'MYSLIB';

                  EXEC SQL
                    CALL "MNTLIB_setValues" (
                      :g_LIB,
                      :g_SLIB
                    );
                  setIgnoreWarnings();
                  if (SqlCheckState(sqlState));

                    // *** BOOK ***

                    EXEC SQL
                      CALL "MNTLIB_book";
                    setIgnoreWarnings();
                    if (SqlCheckState(sqlState));

                      g_OK = *on;

                    endif;

                  endif;

                endif;

              endif;

            endif;

            // *** COMMIT OR ROLLBACK ***

            if (g_OK);

              EXEC SQL
                CALL "RAPIDFIRE_commit"(
                  :g_SUCCESS,
                  :g_MSG
                );
              setIgnoreWarnings();
              if (SqlCheckState(sqlState));

                if (g_SUCCESS = 'Y');

                  g_DMY = *on;

                endif;

              endif;

            else;

              EXEC SQL
                CALL "RAPIDFIRE_rollback"(
                  :g_SUCCESS,
                  :g_MSG
                );
              setIgnoreWarnings();
              if (SqlCheckState(sqlState));

                if (g_SUCCESS = 'Y');

                  g_DMY = *on;

                endif;

              endif;

            endif;

            // *** CLOSE FILES ***

            EXEC SQL
              CALL "MNTLIB_closeFiles";
            setIgnoreWarnings();
            if (SqlCheckState(sqlState));

              g_DMY = *on;

            endif;

          endif;

          EXEC SQL
            CALL "MNTJOB_closeFiles";
          setIgnoreWarnings();
          if (SqlCheckState(sqlState));

            g_DMY = *on;

          endif;

        endif;

        // *** SET AUTO COMMIT TO ON ***

        EXEC SQL
          CALL "RAPIDFIRE_setAutoCommit"(
            'Y',
            :g_SUCCESS,
            :g_MSG
          );
        setIgnoreWarnings();
        if (SqlCheckState(sqlState));

          if (g_SUCCESS = 'Y');

            g_DMY = *on;

          endif;

        endif;

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
