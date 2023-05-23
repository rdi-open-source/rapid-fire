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
//* Fields with GENERATED clause                                                            *
//*                                                                                         *
//*-----------------------------------------------------------------------------------------*
//*  >>PRE-COMPILER<<                                                                       *
//*    >>CRTCMD<< CRTSQLRPGI   OBJ(&LI/&OB) +                                               *
//*                              SRCFILE(&SL/&SF) SRCMBR(&SM) +                             *
//*                              OBJTYPE(*MODULE);                                          *
//*    >>IMPORTANT<<                                                                        *
//*      >>PARM<<  OUTPUT(*PRINT);                                                          *
//*      >>PARM<<  GENLVL(0);                                                               *
//*      >>PARM<<  DBGVIEW(*NONE);                                                          *
//*      >>PARM<<  OPTION(*EVENTF);                                                         *
//*      >>PARM<<  TGTRLS(&TR);                                                             *
//*      >>PARM<<  COMPILEOPT('DBGVIEW(*ALL)');                                             *
//*    >>END-IMPORTANT<<                                                                    *
//*    >>EXECUTE<<                                                                          *
//*  >>END-PRE-COMPILER<<                                                                   *
//*------------+--------+-------------------------------------------------------------------*
//*   Datum    I Progr. I Beschreibung                                                      *
//*------------+--------+-------------------------------------------------------------------*
//* 16.05.2023 I   FH   I Programm erstellt                                                 *
//*------------+--------+-------------------------------------------------------------------*

// Control

CTL-OPT NoMain;
CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,');
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

// Includes

/Include QCPYSRC,FLDGENCLS
/Include QCPYSRC,SQLSTS

// Global field declarations

DCL-S g_Loop IND Inz(*On);

//*-----------------------------------------------------------------------------------------*

// Set SQL options

Exec SQL
  Set Option
  DatFmt=*ISO,
  TimFmt=*ISO,
  Commit=*NONE,
  CloSQLCsr=*ENDMOD;

//*=========================================================================================*
//* Build                                                                                   *
//*=========================================================================================*

DCL-PROC FLDGENCLS_build EXPORT;

  DCL-PI FLDGENCLS_build Ind;                   // <-- Contains fields with generated clause?
    p_Job CHAR(10) Const;                       // --> Job
    p_Contains CHAR(1);                         // <-- Contains fields with generated clause?
  END-PI;

  // Locale field declarations

  DCL-S l_Contains Ind;
  DCL-S l_Library CHAR(10);
  DCL-S l_File CHAR(10);
  DCL-S l_Field CHAR(10);
  DCL-S l_Text CHAR(50);

  // Reset "Contains fields with generated clause"
  l_Contains = *Off;

  // Delete from work file
  Exec SQL
    Delete From Fields_With_Generated_Clause
    Where Job = :p_Job;
  setIgnoreWarnings();
  SqlCheckState(sqlState);

  // Declare Cursor
  Exec SQL
    Declare Files Cursor For
      Select Library, File
      From Files_With_Shadow_Library
      Where Job = :p_Job And
            Type = '*PHYSICAL'
      For Read Only;

  // Open cursor
  Exec SQL
    Open Files;
  setIgnoreWarnings();
  If SqlCheckState(sqlState);

    DoW g_Loop;

      // Read 'Files'
      Exec SQL
        Fetch Next
        From Files
        Into :l_Library, :l_File;

      // End of file
      setIgnoreWarnings();
      If Not SqlCheckState(sqlState);
        Leave;
      EndIf;

      // Declare Cursor
      Exec SQL
        Declare Columns Cursor For
          Select SYSTEM_COLUMN_NAME, COALESCE(COLUMN_TEXT, '*BLANKS')
          From QSYS2/SYSCOLUMNS
          Where SYSTEM_TABLE_SCHEMA = :l_Library And
                SYSTEM_TABLE_NAME = :l_File And
                HAS_DEFAULT Not In ('N', 'Y', 'D', 'F', 'J')
          For Read Only;

      // Open cursor
      Exec SQL
        Open Columns;
      setIgnoreWarnings();
      If SqlCheckState(sqlState);

        DoW g_Loop;

          // Read 'Columns'
          Exec SQL
            Fetch Next
            From Columns
            Into :l_Field, :l_Text;

          // End of file
          setIgnoreWarnings();
          If Not SqlCheckState(sqlState);
            Leave;
          EndIf;

          // Set "Contains fields with generated clause"
          l_Contains = *On;

          // Insert into work file
          Exec SQL
            Insert Into Fields_With_Generated_Clause
              (
               Job,
               Library,
               File,
               Field,
               Text
              )
              Values (
               :p_Job,
               :l_Library,
               :l_File,
               :l_Field,
               :l_Text
              );
          setIgnoreWarnings();
          SqlCheckState(sqlState);

        EndDo;

        // Close cursor
        Exec SQL
          Close Columns;
        setIgnoreWarnings();
        SqlCheckState(sqlState);

      EndIf;

    EndDo;

    // Close cursor
    Exec SQL
      Close Files;
    setIgnoreWarnings();
    SqlCheckState(sqlState);

  EndIf;

  // Return "Contains fields with generated clause"
  If Not l_Contains;
    p_Contains = 'N';
  Else;
    p_Contains = 'Y';
  EndIf;

  // Return
  Return l_Contains;

END-PROC FLDGENCLS_build;
