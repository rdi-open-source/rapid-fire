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
/Include QCPYSRC,QDBRTVFD
/Include QCPYSRC,FILD0100
/Include QCPYSRC,ERRC0100

//*=========================================================================================*
//* Get Number Of Records                                                                   *
//*=========================================================================================*

DCL-PROC FILE_get_Number_Of_Records EXPORT;

  DCL-PI FILE_get_Number_Of_Records UNS(10); // <-- Number of records
    p_Library CHAR(10) Const;                // --> Library
    p_File CHAR(10) Const;                   // --> File
    p_Member CHAR(10) Const;                 // --> Member
  END-PI;

  // Locale field declarations

  DCL-S l_Number_Of_Records UNS(10);

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

//*=========================================================================================*
//* Is File Journaled                                                                       *
//*=========================================================================================*

DCL-PROC FILE_is_File_Journaled EXPORT;

  DCL-PI FILE_is_File_Journaled IND;             // <-- Result
    p_Library_File CHAR(10) Const;               // --> Library - File
    p_File CHAR(10) Const;                       // --> File
    p_Library_Journal CHAR(10) Options(*NoPass); // <-- Library - Journal
    p_Journal CHAR(10) Options(*NoPass);         // <-- Journal
  END-PI;

  // Locale field declarations

  DCL-S l_Journaled IND;
  DCL-S l_Qualified_File CHAR(20);

  // Initialize 'Journaled'
  l_Journaled = *Off;
  If %Parms() >= %ParmNum(p_Library_Journal);
    p_Library_Journal = *Blanks;
    p_Journal = *Blanks;
  EndIf;

  // Get number of bytes needed
  Reset ERRC0100;
  QDBRTVFD(
    FILD0100 :         // <-- Receiver variable
    %Size(FILD0100) :  // --> Length of receiver variable
    l_Qualified_File : // <-- Qualified returned file name
    'FILD0100' :       // --> Format name
    p_File +
    p_Library_File :   // --> Qualified file name
    '*FIRST' :         // --> Record format name
    '0' :              // --> Override processing
    '*LCL' :           // --> System
    '*INT' :           // --> Format type
    ERRC0100           // <-> Error code
  );

  If ERRC0100.Exeption_Id = *Blanks;

    // Allocate heap storage
    QDBRTVFD_Heap_Pointer = %Alloc(Qdbfyavl);

    // Retrieve informations
    Reset ERRC0100;
    QDBRTVFD(
      QDBRTVFD_Heap :    // <-- Receiver variable
      Qdbfyavl :         // --> Length of receiver variable
      l_Qualified_File : // <-- Qualified returned file name
      'FILD0100' :       // --> Format name
      p_File +
      p_Library_File :   // --> Qualified file name
      '*FIRST' :         // --> Record format name
      '0' :              // --> Override processing
      '*LCL' :           // --> System
      '*INT' :           // --> Format type
      ERRC0100           // <-> Error code
    );

    If ERRC0100.Exeption_Id = *Blanks;

      // Set pointer to 'File Definition Header'
      Qdb_QdbfhP = QDBRTVFD_Heap_Pointer;

      // Set pointer to 'Journal Informations'
      Qdb_QdbfjoalP = Qdb_QdbfhP + Qdbfjorn;

      // The file is journaled
      If Qdbfjact = '1';
        l_Journaled = *On;
        If %Parms() >= %ParmNum(p_Library_Journal);
          p_Library_Journal = Qdbfolib;
          p_Journal = Qdbfojrn;
        EndIf;
      EndIf;

    EndIf;

    // Deallocate heap storate
    DeAlloc QDBRTVFD_Heap_Pointer;

  EndIf;

  // Return 'Journaled'
  Return l_Journaled;

END-PROC FILE_is_File_Journaled;
