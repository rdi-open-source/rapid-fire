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
//* Language                                                                                *
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
//*    Date    I Progr. I Description                                                       *
//*------------+--------+-------------------------------------------------------------------*
//* 18.09.2018 I   FH   I Program created.                                                  *
//*------------+--------+-------------------------------------------------------------------*

// Control

CTL-OPT NoMain;
CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,');
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

// Includes

/Include QCPYSRC,LANGUAGE
/Include QCPYSRC,QUSRJOBI
/Include QCPYSRC,JOBI0700
/Include QCPYSRC,ERRC0100
/Include QCPYSRC,EXCCMD

// Program status data structure

DCL-DS PSDS LEN(429) Qualified PSDS;
  Job CHAR(10) POS(244);
  User CHAR(10) POS(254);
  Number CHAR(6) POS(264);
END-DS;

//*=========================================================================================*
//* Set Language                                                                            *
//*=========================================================================================*

DCL-PROC LANGUAGE_setLanguage EXPORT;

  DCL-PI LANGUAGE_setLanguage CHAR(1); // <-- Result
  END-PI;

  // Locale field declarations

  DCL-S l_Result CHAR(1);
  DCL-S l_Library CHAR(10);
  DCL-S l_Exist CHAR(1);
  DCL-S l_Counter INT(10);

  // Initialize result
  l_Result = 'N';

  // Check, if secondary language english has installed
  EXCCMD_execute_Command(
    'CHKOBJ ' +
    'OBJ(QSYS/QSYS2924) ' +
    'OBJTYPE(*LIB)'
  );

  if (EXCCMD_Message_Id = *blanks);

    // Retrieve job informations
    reset ERRC0100;
    QUSRJOBI(
      JOBI0700 :        // <-- Receiver variable
      %size(JOBI0700) : // --> Length of receiver variable
      'JOBI0700' :      // --> Format name
      PSDS.Job +
      PSDS.User +
      PSDS.Number :     // --> Qualified job name
      *blanks :         // --> Internal job identifier
      ERRC0100          // <-> Error code
    );

    If ERRC0100.Exeption_Id = *Blanks;

      // Check, if english library is in the library list
      l_Exist = 'N';
      for l_Counter = 1 to JOBI0700.System_Library_List_Number by 1;
        l_Library =
          %Subst(
            JOBI0700.Library_Area :
            1 + ((l_Counter - 1) * 11) :
            10
          );
        if (l_Library = 'QSYS2924');
          l_Exist = 'Y';
          leave;
        endif;
      endfor;

      // Add english language library to system library list
      if (l_Exist = 'N');
        EXCCMD_execute_Command(
          'CHGSYSLIBL ' +
          'LIB(QSYS2924) ' +
          'OPTION(*ADD)'
        );
        if (EXCCMD_Message_Id = *blanks);
          l_Result = 'Y';
        endif;
      endif;

    endif;

  endif;

  // Return
  Return l_Result;

END-PROC LANGUAGE_setLanguage;

//*=========================================================================================*
//* Reset Language                                                                          *
//*=========================================================================================*

DCL-PROC LANGUAGE_resetLanguage EXPORT;

  DCL-PI LANGUAGE_resetLanguage CHAR(1); // <-- Result
  END-PI;

  // Locale field declarations

  DCL-S l_Result CHAR(1);

  // Initialize result
  l_Result = 'N';

  // Add english language library to system library list
  EXCCMD_execute_Command(
    'CHGSYSLIBL ' +
    'LIB(QSYS2924) ' +
    'OPTION(*REMOVE)'
  );
  if (EXCCMD_Message_Id = *blanks);
    l_Result = 'Y';
  endif;

  // Return
  Return l_Result;

END-PROC LANGUAGE_resetLanguage;
