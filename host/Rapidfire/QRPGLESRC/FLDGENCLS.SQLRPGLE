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

  DCL-PI FLDGENCLS_build;
    p_Job CHAR(10) Const; // --> Job
    p_Result INT(10);     // <-- Result
  END-PI;

  // Locale field declarations

  DCL-S l_Contains Ind;
  DCL-S l_Library CHAR(10);
  DCL-S l_File CHAR(10);
  DCL-S l_Field CHAR(10);
  DCL-S l_Text CHAR(50);

  // Initialize result
  p_Result = *Zero;

  // Reset "Contains fields with generated clause"
  l_Contains = *Off;

  // Delete from work file
  Exec SQL
    Delete From Fields_With_Generated_Clause
    Where Job = :p_Job;
  setIgnoreWarnings();
  setIgnoreNoData();
  If Not SqlCheckState(sqlState);

    // Set result
    p_Result = -1;

  Else;

    // Declare Cursor
    Exec SQL
      Declare Files Cursor For
        Select Library, File
        From Files_With_Shadow_Library
        Where Job = :p_Job And
              Type = '*PHYSICAL' And
              (Library, File) Not In
                (
                 Select Library, File
                 From Ignore_Fields_With_Generated_Clause
                 Where Job = :p_Job
                )
        For Read Only;

    // Open cursor
    Exec SQL
      Open Files;
    setIgnoreWarnings();
    If Not SqlCheckState(sqlState);

      // Set result
      p_Result = -2;

    Else;

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
        If Not SqlCheckState(sqlState);

          // Set result
          p_Result = -3;

          // Leave loop
          Leave;

        Else;

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
                 Text,
                 Replication_Override
                )
                Values (
                 :p_Job,
                 :l_Library,
                 :l_File,
                 :l_Field,
                 :l_Text,
                 'N'
                );
            setIgnoreWarnings();
            If Not SqlCheckState(sqlState);

              // Set result
              p_Result = -4;

              // Leave loop
              Leave;

            EndIf;

          EndDo;

          // Close cursor
          Exec SQL
            Close Columns;
          setIgnoreWarnings();
          If Not SqlCheckState(sqlState);

            If p_Result = *Zero;

              // Set result
              p_Result = -5;

            EndIf;

          EndIf;

          If p_Result <> *Zero;

            // Leave loop
            Leave;

          EndIf;

        EndIf;

      EndDo;

      // Close cursor
      Exec SQL
        Close Files;
      setIgnoreWarnings();
      If Not SqlCheckState(sqlState);

        If p_Result = *Zero;

          // Set result
          p_Result = -6;

        EndIf;

      EndIf;

    EndIf;

  EndIf;

  If p_Result = *Zero;

    If l_Contains;

      // Set result
      p_Result = 1;

    EndIf;

  EndIf;

  // Return
  Return;

END-PROC FLDGENCLS_build;

//*=========================================================================================*
//* Is Replication Override Available                                                       *
//*=========================================================================================*

DCL-PROC FLDGENCLS_isReplicationOverrideAvailable EXPORT;

  DCL-PI FLDGENCLS_isReplicationOverrideAvailable;
    p_Result INT(10); // <-- Result
  END-PI;

  // Locale field declarations

  DCL-S l_Number Int(10);

  // Determine if the global variable REPLICATION_OVERRIDE exists
  Exec SQL
    Select Count(*) Into :l_Number
    From QSYS2/SYSVARS
    Where VARIABLE_SCHEMA = 'QSYS2' And
          VARIABLE_NAME = 'REPLICATION_OVERRIDE';
  setIgnoreWarnings();
  If Not SqlCheckState(sqlState);

    // Set result
    p_Result = -1;

  Else;

    Select;

    When l_Number < *Zero;

      // Set "Result"
      p_Result = -2;

    When l_Number = *Zero;

      // Set "Result"
      p_Result = *Zero;

    When l_Number > *Zero;

      // Set "Result"
      p_Result = 1;

    EndSl;

  EndIf;

  // Return
  Return;

END-PROC FLDGENCLS_isReplicationOverrideAvailable;

//*=========================================================================================*
//* Is Replication Override Authorized                                                      *
//*=========================================================================================*

DCL-PROC FLDGENCLS_isReplicationOverrideAuthorized EXPORT;

  DCL-PI FLDGENCLS_isReplicationOverrideAuthorized;
    p_Result INT(10); // <-- Result
  END-PI;

  // Locale field declarations

  DCL-S l_Integer Int(10);

  // Determine if the user is authorized to the global variable REPLICATION_OVERRIDE
  Exec SQL
    Values QSYS2.SQL_CHECK_FUNCTION_USAGE('QIBM_DB_GENCOL_OVERRIDE')
    Into :l_Integer;
  setIgnoreWarnings();
  If Not SqlCheckState(sqlState);

    // Set result
    p_Result = -1;

  Else;

    Select;

    When l_Integer = *Zero;

      // Set "Result"
      p_Result = *Zero;

    When l_Integer = 1;

      // Set "Result"
      p_Result = 1;

    Other;

      // Set "Result"
      p_Result = -2;

    EndSl;

  EndIf;

  // Return
  Return;

END-PROC FLDGENCLS_isReplicationOverrideAuthorized;

//*=========================================================================================*
//* Set Replication Override                                                                *
//*=========================================================================================*

DCL-PROC FLDGENCLS_setReplicationOverride EXPORT;

  DCL-PI FLDGENCLS_setReplicationOverride;
    p_Value CHAR(1) Const; // --> Value
    p_Result INT(10);      // <-- Result
  END-PI;

  DCL-S l_SQL CHAR(256);

  // Create statement
  l_SQL =
    'SET QSYS2.REPLICATION_OVERRIDE = ' + p_Value;

  // Prepare statement
  Exec SQL
    Prepare Set_Rep_Ovr From :l_SQL;

  // Execute statement
  Exec SQL
    Execute Set_Rep_Ovr;
  setIgnoreWarnings();
  If Not SqlCheckState(sqlState);

    // Set "Result"
    p_Result = -1;

  Else;

    // Set "Result"
    p_Result = *Zero;

  EndIf;

  // Return
  Return;

END-PROC FLDGENCLS_setReplicationOverride;

//*=========================================================================================*
//* Is Fields With Generated Clause                                                         *
//*=========================================================================================*

DCL-PROC FLDGENCLS_isFieldsWithGeneratedClause EXPORT;

  DCL-PI FLDGENCLS_isFieldsWithGeneratedClause;
    p_Job CHAR(10) Const;      // --> Job
    p_Library CHAR(10) Const;  // --> Library
    p_File CHAR(10) Const;     // --> File
    p_ChkRplOvr CHAR(1) Const; // --> Check Replication Override
    p_Result INT(10);          // <-- Result
  END-PI;

  // Locale field declarations

  DCL-S l_Number Int(10);

  // Get number of fields with generated clause
  select;
  when (p_ChkRplOvr = 'N');
    p_Result = *Zero;
    Exec SQL
      Select Count(*) Into :l_Number
      From Fields_With_Generated_Clause
      Where Job = :p_Job And
            Library = :p_Library And
            File = :p_File;
  when (p_ChkRplOvr = 'Y');
    p_Result = *Zero;
    Exec SQL
      Select Count(*) Into :l_Number
      From Fields_With_Generated_Clause
      Where Job = :p_Job And
            Library = :p_Library And
            File = :p_File And
            Replication_Override = 'Y';
  other;
    p_Result = -1;
  endsl;

  if (p_Result = *Zero);

    setIgnoreWarnings();
    If (Not SqlCheckState(sqlState));

      // Set "Result"
      p_Result = -2;

    Else;

      Select;

      When l_Number < *Zero;

        // Set "Result"
        p_Result = -3;

      When l_Number = *Zero;

        // Set "Result"
        p_Result = *Zero;

      When l_Number > *Zero;

        // Set "Result"
        p_Result = 1;

      EndSl;

    EndIf;

  EndIf;

  // Return
  Return;

END-PROC FLDGENCLS_isFieldsWithGeneratedClause;

//*=========================================================================================*
//* Set Fields With Generated Clause                                                        *
//*=========================================================================================*

DCL-PROC FLDGENCLS_setFieldsWithGeneratedClause EXPORT;

  DCL-PI FLDGENCLS_setFieldsWithGeneratedClause;
    p_Job CHAR(10) Const;   // --> Job
    p_RplOvr CHAR(1) Const; // --> Replication Override
    p_Result INT(10);       // <-- Result
  END-PI;

  // Locale field declarations

  DCL-S l_Updated_Rows Int(10);

  Exec SQL
    Update Fields_With_Generated_Clause
    Set Replication_Override = :p_RplOvr
    Where Job = :p_Job;
  setIgnoreWarnings();
  If Not SqlCheckState(sqlState);

    // Set "Result"
    p_Result = -1;

  Else;

    // Get updated rows
    Exec SQL
      GET DIAGNOSTICS :l_Updated_Rows = ROW_COUNT;

    If l_Updated_Rows <= *Zero;

      // Set "Result"
      p_Result = -2;

    Else;

      // Set "Result"
      p_Result = *Zero;

    EndIf;

  EndIf;

  // Return
  Return;

END-PROC FLDGENCLS_setFieldsWithGeneratedClause;
