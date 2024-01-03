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
//* Create Test Job                                                                         *
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

/Include QCPYSRC,CRTTSTJOB
/Include QCPYSRC,SQLSTS
/Include QCPYSRC,EXCCMD

//*-----------------------------------------------------------------------------------------*

// Set SQL options

Exec SQL
  Set Option
  DatFmt=*ISO,
  TimFmt=*ISO,
  Commit=*NONE,
  CloSQLCsr=*ENDMOD;

//*=========================================================================================*
//* Create Test Job                                                                         *
//*=========================================================================================*

DCL-PROC CRTTSTJOB_create_Test_Job EXPORT;

  DCL-PI CRTTSTJOB_create_Test_Job CHAR(80); // <-- Message
    p_Job CHAR(10) Const;                    // --> Job
    p_Production_Library CHAR(10) Const;     // --> Production library
    p_Shadow_Library CHAR(10) Const;         // --> Shadow library
    p_Number_Of_Tables PACKED(3 : 0) Const;  // --> Number of tables
    p_Number_Of_Columns PACKED(3 : 0) Const; // --> Number of columns
    p_Number_Of_Rows PACKED(6 : 0) Const;    // --> Number of rows
    p_Number_Of_Indices PACKED(3 : 0) Const; // --> Number of indices
    p_Additional_Column CHAR(10) Const;      // --> Additional column (Shadow)
    p_Job_Queue_Library CHAR(10) Const;      // --> Job queue library
    p_Job_Queue CHAR(10) Const;              // --> Job queue
  END-PI;

  // Locale field declarations

  DCL-S l_Job CHAR(10);
  DCL-S l_Msg CHAR(80);
  DCL-S l_Number_Of_Columns PACKED(3 : 0);

  // Check, if job exists
  Exec SQL
    Select Job Into :l_Job
    From Jobs
    Where Job = :p_Job;
  setIgnoreWarnings();

  // Job exists
  If SqlCheckState(sqlState);
    Return 'Job ' + %Trim(p_Job) + ' does already exist.';
  EndIf;

  // Check, if production library exists
  EXCCMD_execute_Command(
    'CHKOBJ ' +
    'OBJ(QSYS/' + %Trim(p_Production_Library) + ') ' +
    'OBJTYPE(*LIB)'
  );

  // Production library exists
  If EXCCMD_Message_Id = *Blanks;
    Return 'Production library ' + %Trim(p_Production_Library) + ' does already exist.';
  EndIf;

  // Check, if shadow library exists
  EXCCMD_execute_Command(
    'CHKOBJ ' +
    'OBJ(QSYS/' + %Trim(p_Shadow_Library) + ') ' +
    'OBJTYPE(*LIB)'
  );

  // Shadow library exists
  If EXCCMD_Message_Id = *Blanks;
    Return 'Shadow library ' + %Trim(p_Shadow_Library) + ' does already exist.';
  EndIf;

  // Production and shadow library are equal
  If p_Production_Library = p_Shadow_Library;
    Return 'Production library ' + %Trim(p_Production_Library) + ' and ' +
           'shadow library ' + %Trim(p_Shadow_Library) + ' are equal.';
  EndIf;

  // Number of tables has to be greater than zero
  If p_Number_Of_Tables <= *Zero;
    Return 'Number of tables has to be greater than zero.';
  EndIf;

  // Number of columns has to be greater than zero
  If p_Number_Of_Columns <= *Zero;
    Return 'Number of columns has to be greater than zero.';
  EndIf;

  // Number of rows has to be greater than zero
  If p_Number_Of_Rows <= *Zero;
    Return 'Number of rows has to be greater than zero.';
  EndIf;

  // Number of indices has to be lower than or equal to number of columns
  If p_Number_Of_Indices > p_Number_Of_Columns;
    Return 'Number of indices has to be lower than or equal to number of columns.';
  EndIf;

  // Create production library
  EXCCMD_execute_Command(
    'CRTLIB ' +
    'LIB(' + %Trim(p_Production_Library) + ') ' +
    'TEXT(''Rapid Fire Test - Production Library'')'
  );

  // Production library could not be created
  If EXCCMD_Message_Id <> *Blanks;
    Return 'Production library ' + %Trim(p_Production_Library) +
           ' could not be created. ' + '(' + EXCCMD_Message_Id + ')';
  EndIf;

  // Create shadow library
  EXCCMD_execute_Command(
    'CRTLIB ' +
    'LIB(' + %Trim(p_Shadow_Library) + ') ' +
    'TEXT(''Rapid Fire Test - Shadow Library'')'
  );

  // Shadow library could not be created
  If EXCCMD_Message_Id <> *Blanks;
    Return 'Shadow library ' + %Trim(p_Shadow_Library) +
           ' could not be created. ' + '(' + EXCCMD_Message_Id + ')';
  EndIf;

  // Create sql objects in production library
  l_Msg = create_SQL_Objects(
    p_Production_Library : // --> Production library
    p_Number_Of_Tables :   // --> Number of tables
    p_Number_Of_Columns :  // --> Number of columns
    p_Number_Of_Indices    // --> Number of indices
  );

  // Error occurred while creating sql objects in production library
  if l_Msg <> *Blanks;
    Return l_Msg;
  EndIf;

  // Create sql objects in shadow library
  If p_Additional_Column = '*YES';
    l_Number_Of_Columns = p_Number_Of_Columns + 1;
  Else;
    l_Number_Of_Columns = p_Number_Of_Columns;
  EndIf;
  l_Msg = create_SQL_Objects(
    p_Shadow_Library :    // --> Shadow library
    p_Number_Of_Tables :  // --> Number of tables
    l_Number_Of_Columns : // --> Number of columns
    p_Number_Of_Indices   // --> Number of indices
  );

  // Error occurred while creating sql objects in shadow library
  if l_Msg <> *Blanks;
    Return l_Msg;
  EndIf;

  // Create rows in tables in productions library
  l_Msg = create_Rows(
    p_Production_Library : // --> Production library
    p_Number_Of_Tables :   // --> Number of tables
    p_Number_Of_Columns :  // --> Number of columns
    p_Number_Of_Rows       // --> Number of rows
  );

  // Error occurred while creating sql objects in shadow library
  if l_Msg <> *Blanks;
    Return l_Msg;
  EndIf;

  // Create Rapid Fire Job
  l_Msg = create_Rapid_Fire_Job(
    p_Job :                // --> Job
    p_Production_Library : // --> Production library
    p_Shadow_Library :     // --> Shadow library
    p_Job_Queue_Library :  // --> Job queue library
    p_Job_Queue :          // --> Job queue
    p_Number_Of_Tables     // --> Number of tables
  );

  // Error occurred while creating Rapid Fire job
  if l_Msg <> *Blanks;
    Return l_Msg;
  EndIf;

  // Zurück
  Return *Blanks;

END-PROC CRTTSTJOB_create_Test_Job;

//*=========================================================================================*
//* Create SQL Objects                                                                      *
//*=========================================================================================*

DCL-PROC create_SQL_Objects;

  DCL-PI create_SQL_Objects CHAR(80);        // <-- Message
    p_Library CHAR(10) Const;                // --> Library
    p_Number_Of_Tables PACKED(3 : 0) Const;  // --> Number of tables
    p_Number_Of_Columns PACKED(3 : 0) Const; // --> Number of columns
    p_Number_Of_Indices PACKED(3 : 0) Const; // --> Number of indices
  END-PI;

  // Locale field declarations

  DCL-S l_SQL VARCHAR(32766);
  DCL-S l_SQLX CHAR(32766);
  DCL-S l_Tbl CHAR(10);
  DCL-S l_TblIdx ZONED(3 : 0);
  DCL-S l_Col CHAR(10);
  DCL-S l_ColIdx ZONED(3 : 0);
  DCL-S l_Idx CHAR(10);
  DCL-S l_IdxIdx ZONED(3 : 0);

  For l_TblIdx = 1 To p_Number_Of_Tables By 1;

    // Build CREATE TABLE
    l_Tbl = 'TBL' + %EditC(l_TblIdx : 'X');
    l_SQL =
      'CREATE TABLE ' +
      %Trim(p_Library) + '/' + %Trim(l_Tbl) + ' ' +
      '(';
    For l_ColIdx = 1 To p_Number_Of_Columns By 1;
      l_Col = 'COL' + %EditC(l_ColIdx : 'X');
      If l_ColIdx > 1;
        l_SQL =
          %Trim(l_SQL) + ',';
      EndIf;
      l_SQL =
        %Trim(l_SQL) +
        %Trim(l_Col) + ' CHARACTER(10) NOT NULL WITH DEFAULT';
    EndFor;
    l_SQL =
      %Trim(l_SQL) + ')';

    // Execute CREATE TABLE
    l_SQLX = l_SQL;
    Exec SQL
      Prepare Create_Table From :l_SQLX;
    Exec SQL
      Execute Create_Table;
    setIgnoreWarnings();
    If Not SqlCheckState(sqlState);
      Return 'SQL command CREATE TABLE for table ' + %Trim(l_Tbl) + ' failed.';
    EndIf;

    For l_IdxIdx = 1 To p_Number_Of_Indices By 1;

      // Build CREATE INDEX
      l_Idx = 'IDX' + %EditC(l_TblIdx : 'X') + %EditC(l_IdxIdx : 'X');
      l_Col = 'COL' + %EditC(l_IdxIdx : 'X');
      l_SQL =
        'CREATE UNIQUE INDEX ' +
        %Trim(p_Library) + '/' + %Trim(l_Idx) + ' ' +
        'ON ' +
        %Trim(p_Library) + '/' + %Trim(l_Tbl) + ' ' +
        '(' + %Trim(l_Col) + ')';

      // Execute CREATE INDEX
      l_SQLX = l_SQL;
      Exec SQL
        Prepare Create_Index From :l_SQLX;
      Exec SQL
        Execute Create_Index;
      setIgnoreWarnings();
      If Not SqlCheckState(sqlState);
        Return 'SQL command CREATE INDEX for index ' + %Trim(l_Idx) + ' failed.';
      EndIf;

    EndFor;

  EndFor;

  // Zurück
  Return *Blanks;

END-PROC create_SQL_Objects;

//*=========================================================================================*
//* Create Rows                                                                             *
//*=========================================================================================*

DCL-PROC create_Rows;

  DCL-PI create_Rows CHAR(80);               // <-- Message
    p_Production_Library CHAR(10) Const;     // --> Production library
    p_Number_Of_Tables PACKED(3 : 0) Const;  // --> Number of tables
    p_Number_Of_Columns PACKED(3 : 0) Const; // --> Number of columns
    p_Number_Of_Rows PACKED(6 : 0) Const;    // --> Number of rows
  END-PI;

  // Locale field declarations

  DCL-S l_SQL VARCHAR(32766);
  DCL-S l_SQLX CHAR(32766);
  DCL-S l_Tbl CHAR(10);
  DCL-S l_TblIdx ZONED(3 : 0);
  DCL-S l_Col CHAR(10);
  DCL-S l_ColIdx ZONED(3 : 0);
  DCL-S l_Val CHAR(10);
  DCL-S l_RowIdx ZONED(6 : 0);
  DCL-S l_SQLTblCol VARCHAR(16384);
  DCL-S l_SQLVal VARCHAR(16384);

  For l_TblIdx = 1 To p_Number_Of_Tables By 1;

    l_Tbl = 'TBL' + %EditC(l_TblIdx : 'X');

    If l_TblIdx = 1;

      ExSr getTableColumns;

      For l_RowIdx = 1 To p_Number_Of_Rows By 1;

        ExSr getValues;

        l_SQL =
          %Trim(l_SQLTblCol) +
          ' VALUES ' + '(' + %Trim(l_SQLVal) + ')';

        l_SQLX = l_SQL;
        Exec SQL
          Prepare Insert_Row From :l_SQLX;
        Exec SQL
          Execute Insert_Row;
        setIgnoreWarnings();
        If Not SqlCheckState(sqlState);
          Return 'SQL command INSERT for table ' + %Trim(l_Tbl) + ' failed.';
        EndIf;

      EndFor;

    Else;

      EXCCMD_execute_Command(
        'CPYF ' +
        'FROMFILE(' + %Trim(p_Production_Library) + '/TBL001) ' +
        'TOFILE(' + %Trim(p_Production_Library) + '/' + %Trim(l_Tbl) + ') ' +
        'MBROPT(*ADD)'
      );

      If EXCCMD_Message_Id <> *Blanks;
        Return 'Command CPYF for table ' + %Trim(l_Tbl) + ' failed.';
      EndIf;

    EndIf;

  EndFor;

  // Zurück
  Return *Blanks;

  // Tabelle und Spalten holen
  BegSr getTableColumns;
    l_SQLTblCol =
      'INSERT INTO ' +
      %Trim(p_Production_Library) + '/' + %Trim(l_Tbl) + ' ' +
      '(';
    For l_ColIdx = 1 To p_Number_Of_Columns By 1;
      l_Col = 'COL' + %EditC(l_ColIdx : 'X');
      If l_ColIdx > 1;
        l_SQLTblCol =
          %Trim(l_SQLTblCol) + ',';
      EndIf;
      l_SQLTblCol =
        %Trim(l_SQLTblCol) +
        %Trim(l_Col);
    EndFor;
    l_SQLTblCol =
      %Trim(l_SQLTblCol) + ')';
  EndSr;

  // Werte holen
  BegSr getValues;
    l_SQLVal = *Blanks;
    For l_ColIdx = 1 To p_Number_Of_Columns By 1;
      l_Val = 'V' + %EditC(l_RowIdx : 'X') + %EditC(l_ColIdx : 'X');
      If l_ColIdx > 1;
        l_SQLVal =
          %Trim(l_SQLVal) + ',';
      EndIf;
      l_SQLVal =
        %Trim(l_SQLVal) +
        '''' + %Trim(l_Val) + '''';
    EndFor;
  EndSr;

END-PROC create_Rows;

//*=========================================================================================*
//* Create Rapid Fire Job                                                                   *
//*=========================================================================================*

DCL-PROC create_Rapid_Fire_Job;

  DCL-PI create_Rapid_Fire_Job CHAR(80);    // <-- Message
    p_Job CHAR(10) Const;                   // --> Job
    p_Production_Library CHAR(10) Const;    // --> Production library
    p_Shadow_Library CHAR(10) Const;        // --> Shadow library
    p_Job_Queue_Library CHAR(10) Const;     // --> Job queue library
    p_Job_Queue CHAR(10) Const;             // --> Job queue
    p_Number_Of_Tables PACKED(3 : 0) Const; // --> Number of tables
  END-PI;

  // Locale field declarations

  DCL-S l_Tbl CHAR(10);
  DCL-S l_TblIdx ZONED(3 : 0);

  // Create job
  Exec SQL
    Insert Into Jobs
    (
     Job,
     Description,
     Create_Environment,
     Job_Queue_Library,
     Job_Queue,
     Cancel_ASP_Threshold_Exceeds,
     Status,
     Phase,
     Error,
     Error_Text,
     Batch_Job,
     Batch_User,
     Batch_Number,
     Stop_Apply_Changes,
     CMOne_Form
    )
    Values
    (
     :p_Job,
     'Rapid Fire Test',
     '*NO',
     :p_Job_Queue_Library,
     :p_Job_Queue,
     '*YES',
     '*RDY',
     '*NONE',
     'N',
     '',
     '',
     '',
     '',
     'N',
     ''
    );
  setIgnoreWarnings();
  If Not SqlCheckState(sqlState);
    Return 'SQL command INSERT for table JOBS failed.';
  EndIf;

  // Create library
  Exec SQL
    Insert Into Libraries
    (
     Job,
     Library,
     Shadow_Library,
     ASP_Number,
     ASP_Device
    )
    Values
    (
     :p_Job,
     :p_Production_Library,
     :p_Shadow_Library,
     0,
     ' '
    );
  setIgnoreWarnings();
  If Not SqlCheckState(sqlState);
    Return 'SQL command INSERT for table LIBRARIES failed.';
  EndIf;

  // Create library list
  Exec SQL
    Insert Into Library_Lists
    (
     Job,
     Library_List,
     Description
    )
    Values
    (
     :p_Job,
     '*NONE',
     'Rapid Fire Test'
    );
  setIgnoreWarnings();
  If Not SqlCheckState(sqlState);
    Return 'SQL command INSERT for table LIBRARY_LISTS failed.';
  EndIf;

  For l_TblIdx = 1 To p_Number_Of_Tables By 1;

    // Set table name
    l_Tbl = 'TBL' + %EditC(l_TblIdx : 'X');

    // Create file
    Exec SQL
      Insert Into Files
      (
       Job,
       Position,
       File,
       Type,
       Copy_Program,
       Copy_Program_Library,
       Conversion_Program,
       Conversion_Program_Library
      )
      Values
      (
       :p_Job,
       :l_TblIdx,
       :l_Tbl,
       '*PHYSICAL',
       '*GEN',
       '',
       '*NONE',
       ''
      );
    setIgnoreWarnings();
    If Not SqlCheckState(sqlState);
      Return 'SQL command INSERT for table FILES failed.';
    EndIf;

    // Create area
    Exec SQL
      Insert Into Areas
      (
       Job,
       Position,
       Area,
       Library,
       Library_List,
       CCSID,
       Command_Extension
      )
      Values
      (
       :p_Job,
       :l_TblIdx,
       '*NONE',
       :p_Production_Library,
       '*NONE',
       '*JOB',
       ''
      );
    setIgnoreWarnings();
    If Not SqlCheckState(sqlState);
      Return 'SQL command INSERT for table AREAS failed.';
    EndIf;

  EndFor;

  // Zurück
  Return *Blanks;

END-PROC create_Rapid_Fire_Job;
