**FREE

//*-----------------------------------------------------------------------------------------*
//*                                                                                         *
//* Copyright (c) 2018 Task Force IT-Consulting GmbH, Waltrop (Germany)                     *
//* This software and the accompanying materials are made available                         *
//* under the terms of the GNU General Public License version 2.0 (GPLv2)                   *
//* which accompanies this distribution and is available at                                 *
//* http://www.gnu.org/licenses/gpl-2.0.html                                                *
//*                                                                                         *
//*-----------------------------------------------------------------------------------------*
//*                                                                                         *
//* Execute SQL                                                                             *
//*                                                                                         *
//*-----------------------------------------------------------------------------------------*
//*  >>PRE-COMPILER<<                                                                       *
//*    >>CRTCMD<< CRTSQLRPGI   OBJ(&LI/&OB) +                                               *
//*                              SRCFILE(&SL/&SF) SRCMBR(&SM) +                             *
//*                              OBJTYPE(*MODULE);                                          *
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
//* 24.11.2017 I   TR   I Creation                                                          *
//*------------+--------+-------------------------------------------------------------------*

// Control

CTL-OPT NoMain;
CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,');
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

DCL-F SCRIPT Disk(*EXT) USAGE(*INPUT)
      Qualified
      Extdesc('QSQLSRC')
      Extfile(SqlScriptFile.Name)
      ExtMbr(SqlScriptFile.Member)
      Rename(QSQLSRC : QSQLSRCF)
      UsrOpn;

Dcl-ds SqlScriptFile qualified;
  Name Char(21);
  Member Char(10);
End-Ds;

//*-----------------------------------------------------------------------------------------*

// Includes

/Include QCPYSRC,SQLSTS

//*-----------------------------------------------------------------------------------------*

// Global constants

DCL-C DOUBLE_QUOTES '"';

// Fields to export

DCL-S EXCSQL_Message
      VARCHAR(1024) Export;                                                // Error Message

//*=========================================================================================*
//* Execute SQL statement                                                                   *
//*=========================================================================================*

DCL-PROC EXCSQL_execute_Stmt EXPORT;

  DCL-PI EXCSQL_execute_Stmt Ind;
    p_SQLStmt
    CHAR(4096) Const;                                                      // --> SQL statement
    p_Schema
    CHAR(10) Const Options(*NoPass: *Omit);                                // --> SQL Schema
  END-PI;

  // Locale field declarations

  DCL-S l_SQLStmt
        CHAR(4096);                                                        // SQL statement

  // Initialize export fields
  EXCSQL_Message = '';

  // Set SQL schema
  if (%parmnum(p_Schema) >= %parms() and %addr(p_Schema) <> *null);

    l_SQLStmt = 'SET SCHEMA ' + addQuotes(p_Schema);

    Exec SQL
      EXECUTE IMMEDIATE :l_SQLStmt;

    setIgnoreWarnings();
    If Not SqlCheckState(sqlState);
      EXCSQL_Message = SqlGetLastErrorMessage();
      Return *off;
    EndIf;

  EndIf;

  // Execute SQL statement
  l_SQLStmt = p_SQLStmt;

  Exec SQL
    EXECUTE IMMEDIATE :l_SQLStmt;

  setIgnoreWarnings();
  If Not SqlCheckState(sqlState);
    EXCSQL_Message = SqlGetLastErrorMessage();
    Return *off;
  EndIf;

  Return *on;

END-PROC EXCSQL_execute_Stmt;

//*=========================================================================================*
//* Execute SQL script                                                                      *
//*=========================================================================================*

DCL-PROC EXCSQL_execute_Script EXPORT;

  DCL-PI EXCSQL_execute_Script Ind;
    p_Library
    CHAR(10) Const;                                                        // --> Library
    p_File
    CHAR(10) Const;                                                        // --> Src File
    p_Member
    CHAR(10) Const;                                                        // --> Src Member
    p_Schema
    CHAR(10) Const Options(*NoPass: *Omit);                                // --> SQL Schema
  END-PI;

  // Locale field declarations

  DCL-DS line LikeRec(SCRIPT.QSQLSRCF: *INPUT);
  DCL-S data VarChar(250);

  DCL-S l_SQLStmt VarChar(4096);

  // Initialize export fields
  EXCSQL_Message = '';

  // Set SQL schema
  if (%parmnum(p_Schema) >= %parms() and %addr(p_Schema) <> *null);

    l_SQLStmt = 'SET SCHEMA ' + addQuotes(p_Schema);

    Exec SQL
      EXECUTE IMMEDIATE :l_SQLStmt;

    setIgnoreWarnings();
    If Not SqlCheckState(sqlState);
      EXCSQL_Message = SqlGetLastErrorMessage();
      Return *off;
    EndIf;

  EndIf;

  // Execute SQL statements
  l_SQLStmt = '';

  SqlScriptFile.Name = %trim(p_Library) + '/' + %trim(p_File);
  SqlScriptFile.Member = p_Member;

  Monitor;

    Open SCRIPT;

    Setll 1 SCRIPT;
    DoW (%found(SCRIPT));
      Read SCRIPT.QSQLSRCF line;
      If (%eof(SCRIPT));
        Leave;
      EndIf;

      data = %trimR(line.srcdta);
      If (data = '');
        Iter;
      EndIf;

      If (%len(%trimL(data)) > 2 and %subst(%trimL(data): 1: 2) = '--');
        Iter;
      EndIf;

      If (%subst(data: %len(data): 1) = ';');

        l_SQLStmt += ' ';
        l_SQLStmt += %subst(data: 1: %len(data) - 1);

        Exec SQL
          EXECUTE IMMEDIATE :l_SQLStmt;

        setIgnoreWarnings();
        If Not SqlCheckState(sqlState);
          EXCSQL_Message = SqlGetLastErrorMessage();
          Leave;
        EndIf;

        l_SQLStmt = '';

      Else;
        l_SQLStmt += ' ';
        l_SQLStmt += data;
      EndIf;

    EndDo;

  On-Error;
    EXCSQL_Message = 'Unexpected error in procedure EXCSQL_execute_Script()';
  EndMon;

  Close SCRIPT;

  If (EXCSQL_Message <> '');
    Return *off;
  EndIf;

  Return *on;

END-PROC EXCSQL_execute_Script;

//*=========================================================================================*
//* Get error message                                                                       *
//*=========================================================================================*

DCL-PROC EXCSQL_get_Message EXPORT;

  DCL-PI EXCSQL_get_Message
         VARCHAR(1024);                                                    // Message data
  END-PI;

  Return EXCSQL_Message;

END-PROC EXCSQL_get_Message;

//*=========================================================================================*
//* Puts a given string in double quotes. The string is trimmed beforehand.                 *
//*=========================================================================================*

DCL-PROC addQuotes;

  DCL-PI addQuotes VARCHAR(1024);                                          // Quoted string
    p_string
    VARCHAR(1024) Value;                                                   // --> String to quote
  END-PI;

  p_string = %trim(p_string);

  Return DOUBLE_QUOTES + p_string + DOUBLE_QUOTES;

END-PROC addQuotes;

