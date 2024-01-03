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
//* Test MNTSTBN via Stored Procedures                                                      *
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

/Include QCPYSRC,MNTSTBN
/Include QCPYSRC,SQLSTS

DCL-S g_SUCCESS CHAR(1);       // Success
DCL-S g_FLD     CHAR(10);      // Field
DCL-S g_MSG     CHAR(256);     // Message
DCL-S g_DMY     IND;           // Dummy
DCL-S g_ACT     CHAR(10);      // Action
DCL-S g_POS     PACKED(3 : 0); // Position
DCL-S g_TYPE    CHAR(10);      // Type
DCL-S g_USR     CHAR(10);      // User
DCL-S g_MSGQL   CHAR(10);      // Message queue (Library)
DCL-S g_MSGQ    CHAR(10);      // Message queue
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
      CALL "MNTSTBN_openFiles";
    setIgnoreWarnings();
    if (SqlCheckState(sqlState));

      // *** CREATE subject MYSUB ***

      EXEC SQL
        CALL "MNTSTBN_initialize" (
          '*CREATE',
          'MYJOB',
          0,
          :g_SUCCESS,
          :g_MSG
        );
      setIgnoreWarnings();
      if (SqlCheckState(sqlState));

        if (g_SUCCESS = 'Y');

          EXEC SQL
            CALL "MNTSTBN_getValues" (
              :g_POS,
              :g_TYPE,
              :g_USR,
              :g_MSGQL,
              :g_MSGQ
            );
          setIgnoreWarnings();
          if (SqlCheckState(sqlState));

            g_POS = 1;
            g_TYPE = '*USR';
            g_USR = 'MYSUB';
            g_MSGQL = *blanks;
            g_MSGQ = *blanks;

            EXEC SQL
              CALL "MNTSTBN_setValues" (
                :g_POS,
                :g_TYPE,
                :g_USR,
                :g_MSGQL,
                :g_MSGQ
              );
            setIgnoreWarnings();
            if (SqlCheckState(sqlState));

              EXEC SQL
                CALL "MNTSTBN_check" (
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
                    CALL "MNTSTBN_book";
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

      // *** CHANGE subject MYSUB ***

      EXEC SQL
        CALL "MNTSTBN_initialize" (
          '*CHANGE',
          'MYJOB',
          1,
          :g_SUCCESS,
          :g_MSG
        );
      setIgnoreWarnings();
      if (SqlCheckState(sqlState));

        if (g_SUCCESS = 'Y');

          EXEC SQL
            CALL "MNTSTBN_getValues" (
              :g_POS,
              :g_TYPE,
              :g_USR,
              :g_MSGQL,
              :g_MSGQ
            );
          setIgnoreWarnings();
          if (SqlCheckState(sqlState));

            g_USR = 'MYCHGSUB';

            EXEC SQL
              CALL "MNTSTBN_setValues" (
                :g_POS,
                :g_TYPE,
                :g_USR,
                :g_MSGQL,
                :g_MSGQ
              );
            setIgnoreWarnings();
            if (SqlCheckState(sqlState));

              EXEC SQL
                CALL "MNTSTBN_check" (
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
                    CALL "MNTSTBN_book";
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

      g_ACT = MNTSTBN_CREATE;
      EXEC SQL
        CALL "MNTSTBN_checkAction"(
          :g_ACT,
          'MYJOB',
          0,
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

      g_ACT = MNTSTBN_CHANGE;
      EXEC SQL
        CALL "MNTSTBN_checkAction"(
          :g_ACT,
          'MYJOB',
          1,
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
        CALL "MNTSTBN_getValidActions"(
          'MYJOB',
          1,
          :g_NBRACTS,
          :g_ACTS
        );
      setIgnoreWarnings();
      if (SqlCheckState(sqlState));

        g_DMY = *on;

      endif;

      // *** CLOSE FILES ***

      EXEC SQL
        CALL "MNTSTBN_closeFiles";
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
