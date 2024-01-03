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
//* Change Test Job                                                                         *
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
//*-----------------------------------------------------------------------------------------*

// Control

CTL-OPT NoMain;
CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,');
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

// Includes

/Include QCPYSRC,CHGTSTJOB
/Include QCPYSRC,SQLSTS

// Global field declarations

DCL-S g_Loop IND Inz(*On);

// File declarations

DCL-DS Files Extname('FILESV01') Alias Qualified;
END-DS;

//*-----------------------------------------------------------------------------------------*

// Set SQL options

Exec SQL
  Set Option
  DatFmt=*ISO,
  TimFmt=*ISO,
  Commit=*NONE,
  CloSQLCsr=*ENDMOD;

//*=========================================================================================*
//* Change Test Job                                                                         *
//*=========================================================================================*

DCL-PROC CHGTSTJOB_change_Test_Job EXPORT;

  DCL-PI CHGTSTJOB_change_Test_Job CHAR(80); // <-- Message
    p_Job CHAR(10) Const;                    // --> Job
    p_From_Row PACKED(6 : 0) Const;          // --> From row
    p_To_Row PACKED(6 : 0) Const;            // --> To row
    p_Change_Value CHAR(3) Const;            // --> Change value
  END-PI;

  // Locale field declarations

  DCL-S l_Description CHAR(35);
  DCL-S l_SQL VARCHAR(256);
  DCL-S l_SQLX CHAR(256);
  DCL-S l_To_Row PACKED(6 : 0);
  DCL-S l_Msg CHAR(80);

  // Check, if job exists
  Exec SQL
    Select Description Into :l_Description
    From Jobs
    Where Job = :p_Job;
  setIgnoreWarnings();

  // Job does not exists
  If Not SqlCheckState(sqlState);
    Return 'Job ' + %Trim(p_Job) + ' does not exist.';
  EndIf;

  // Job is not a Rapid Fire test job
  If l_Description <> 'Rapid Fire Test';
    Return 'Job ' + %Trim(p_Job) + ' is not a Rapid Fire test job.';
  EndIf;

  // From row has to be greater than zero
  If p_From_Row <= *Zero;
    Return 'From row has to be greater than zero.';
  EndIf;

  // To row has to be greater than zero
  If p_To_Row <= *Zero;
    Return 'To row has to be greater than zero.';
  EndIf;

  // To row has to be greater than or equal to from row
  If p_To_Row < p_From_Row;
    Return 'To row has to be greater than or equal to from row.';
  EndIf;

  // Change value must not be blank
  If p_Change_Value = ' ';
    Return 'Change value must not be blank.';
  EndIf;

  // Initialize message
  l_Msg = *Blanks;

  // Declare Cursor
  Exec SQL
    Declare Files_1 Cursor For
      Select *
      From Files_With_Shadow_Library
      Where Job = :p_Job And
            Type = '*PHYSICAL'
      Order By Job, Position, Area
      For Read Only;

  // Open cursor
  Exec SQL
    Open Files_1;
  setIgnoreWarnings();
  If SqlCheckState(sqlState);

    DoW g_Loop;

      // Read 'Files'
      Exec SQL
        Fetch Next
        From Files_1
        Into :Files;

      // End of file
      setIgnoreWarnings();
      If Not SqlCheckState(sqlState);
        Leave;
      EndIf;

      // Create update statement
      l_To_Row = p_To_Row + 1;
      l_SQL =
        'UPDATE ' +
        %Trim(Files.Library) + '/' +
        %Trim(Files.File) +
        ' SET COL001 = SUBSTRING(COL001, 1, 7) CONCAT ' +
        '''' + %Trim(p_Change_Value) + '''' +
        ' WHERE ' +
        'COL001 >= ''V' + %EditC(p_From_Row : 'X') + ''' AND ' +
        'COL001 <= ''V' + %EditC(l_To_Row : 'X') + '''';

      // Execute update statement
      l_SQLX = l_SQL;
      Exec SQL
        Prepare Update_Table From :l_SQLX;
      Exec SQL
        Execute Update_Table;
      setIgnoreWarnings();
      If Not SqlCheckState(sqlState);
        l_Msg = 'SQL command UPDATE for table ' + %Trim(Files.File) + ' failed.';
        Leave;
      EndIf;

    EndDo;

    // Close cursor
    Exec SQL
      Close Files_1;
    setIgnoreWarnings();
    SqlCheckState(sqlState);

  EndIf;

  // Zurück
  Return l_Msg;

END-PROC CHGTSTJOB_change_Test_Job;
