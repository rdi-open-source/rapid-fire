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
//* File                                                                                    *
//*                                                                                         *
//*-----------------------------------------------------------------------------------------*
//*  >>PRE-COMPILER<<                                                                       *
//*    >>CRTCMD<< CRTRPGMOD    MODULE(&LI/&OB) +                                            *
//*                              SRCFILE(&SL/&SF) SRCMBR(&SM);                              *
//*    >>IMPORTANT<<                                                                        *
//*      >>PARM<<  DBGVIEW(*ALL);                                                           *
//*      >>PARM<<  OPTION(*EVENTF);                                                         *
//*      >>PARM<<  TGTRLS(&TR);                                                             *
//*    >>END-IMPORTANT<<                                                                    *
//*    >>EXECUTE<<                                                                          *
//*  >>END-PRE-COMPILER<<                                                                   *
//*-----------------------------------------------------------------------------------------*

// Control

CTL-OPT NoMain;
CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,') AlwNull(*USRCTL);
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

// Includes

/Include QCPYSRC,QUSRMBRD
/Include QCPYSRC,MBRD0300
/Include QCPYSRC,ERRC0100

// Global field declarations

DCL-S g_Job
      CHAR(10);

//*=========================================================================================*
//* Get Number Of Records                                                                   *
//*=========================================================================================*

DCL-PROC FILE_get_Number_Of_Records EXPORT;

  DCL-PI FILE_get_Number_Of_Records
         UNS(10);                                                          // <-- Number of rec
    p_Library
    CHAR(10) Const;
    p_File
    CHAR(10) Const;
    p_Member
    CHAR(10) Const;
  END-PI;

  // Locale field declarations

  DCL-S l_Number_Of_Records
        UNS(10);

  // Initialize number of records
  l_Number_Of_Records = *Zero;

  // Get number of bytes needed
  Reset ERRC0100;
  QUSRMBRD(
    MBRD0300 :        // <-- Receiver variable
    %Size(MBRD0300) : // --> Length of receiver variable
    'MBRD0300' :      // --> Format name
    p_File +
    p_Library :       // --> Qualified database file name
    p_Member :        // --> Database member name
    '0' :             // --> Override processing
    ERRC0100          // <-> Error code
  );

  If ERRC0100.Exeption_Id = *Blanks;

    // Allocate heap storage
    QUSRMBRD_Heap_Pointer = %Alloc(MBRD0300.Bytes_Available);

    // Retrieve informations
    Reset ERRC0100;
    QUSRMBRD(
      QUSRMBRD_Heap :            // <-- Receiver variable
      MBRD0300.Bytes_Available : // --> Length of receiver variable
      'MBRD0300' :               // --> Format name
      p_File +
      p_Library :                // --> Qualified database file name
      p_Member :                 // --> Database member name
      '0' :                      // --> Override processing
      ERRC0100                   // <-> Error code
    );

    If ERRC0100.Exeption_Id = *Blanks;

      // Set pointer of information section
      MBRD0300_A_Pointer = QUSRMBRD_Heap_Pointer;

      // Set number of records
      l_Number_Of_Records = MBRD0300_A.Number_Of_Current_Records;

    EndIf;

    // Deallocate heap storate
    DeAlloc QUSRMBRD_Heap_Pointer;

  EndIf;

  // Return number of records
  Return l_Number_Of_Records;

END-PROC FILE_get_Number_Of_Records;
