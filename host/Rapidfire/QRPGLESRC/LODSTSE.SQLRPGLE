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
//* Load Status Entries                                                                     *
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
//* 26.10.2017 I   FH   I Creation                                                          *
//*------------+--------+-------------------------------------------------------------------*

CTL-OPT NOMAIN;
CTL-OPT DATFMT(*ISO) TIMFMT(*ISO) DECEDIT('0,');
CTL-OPT COPYRIGHT('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT BNDDIR('RAPIDFIRE');
CTL-OPT EXTBININT(*YES);

//*-----------------------------------------------------------------------------------------*

/INCLUDE QCPYSRC,LODSTSE

/INCLUDE QCPYSRC,CPLSTSE

/INCLUDE QCPYSRC,SQLSTS

DCL-C CALL_OPEN -1;
DCL-C CALL_FETCH 0;
DCL-C CALL_CLOSE 1;
DCL-C PARM_NULL -1;
DCL-C PARM_NOTNULL 0;

DCL-S g_OPEN IND;

//*-----------------------------------------------------------------------------------------*

// Set SQL options

Exec SQL
  Set Option
  DatFmt=*ISO,
  TimFmt=*ISO,
  Commit=*NONE,
  CloSQLCsr=*ENDACTGRP;

//*=========================================================================================*
//* Load Status Entries                                                                     *
//*=========================================================================================*

DCL-PROC LODSTSE_loadStatusEntries EXPORT;

  DCL-PI LODSTSE_loadStatusEntries;
    p_WHERE     VARCHAR(1024) CONST; // --> Where Clause
    p_JOB       CHAR(10);            // <-- Job
    p_POS       PACKED(6 : 0);       // <-- Position
    p_ARA       CHAR(10);            // <-- Area
    p_FILE      CHAR(10);            // <-- File
    p_LIB       CHAR(10);            // <-- Library
    p_RPL       PACKED(10 : 0);      // <-- Records in production library
    p_RSL       PACKED(10 : 0);      // <-- Records in shadow library
    p_RTO       PACKED(10 : 0);      // <-- Records to copy
    p_RCO       PACKED(10 : 0);      // <-- Records copied
    p_ETC       CHAR(11);            // <-- Estimated time
    p_RDK       PACKED(10 : 0);      // <-- Records with duplicate key
    p_CTA       PACKED(10 : 0);      // <-- Changes to apply
    p_CAP       PACKED(10 : 0);      // <-- Changes applied
    p_PRC       PACKED(3 : 0);       // <-- Percent done
    p_WHERE_n   INT(5) CONST;        // --> (Null Ind.) Where Clause
    p_JOB_n     INT(5);              // <-- (Null Ind.) Job
    p_POS_n     INT(5);              // <-- (Null Ind.) Position
    p_ARA_n     INT(5);              // <-- (Null Ind.) Area
    p_FILE_n    INT(5);              // <-- (Null Ind.) File
    p_LIB_n     INT(5);              // <-- (Null Ind.) Library
    p_RPL_n     INT(5);              // <-- (Null Ind.) Records in production library
    p_RSL_n     INT(5);              // <-- (Null Ind.) Records in shadow library
    p_RTO_n     INT(5);              // <-- (Null Ind.) Records to copy
    p_RCO_n     INT(5);              // <-- (Null Ind.) Records copied
    p_ETC_n     INT(5);              // <-- (Null Ind.) Estimated time
    p_RDK_n     INT(5);              // <-- (Null Ind.) Records with duplicate key
    p_CTA_n     INT(5);              // <-- (Null Ind.) Changes to apply
    p_CAP_n     INT(5);              // <-- (Null Ind.) Changes applied
    p_PRC_n     INT(5);              // <-- (Null Ind.) Percent done
    p_SQLState  CHAR(5);             // <-- SQL State
    p_SQLFnc    VARCHAR(517) CONST;  // SQL Function Name
    p_SQLSpcFnc VARCHAR(128) CONST;  // --> SQL Specific Function name
    p_ErrMsg    VARCHAR(70);         // <-- Error Message
    p_CallType  INT(10) CONST;       // --> Call Type
  END-PI;

  DCL-S l_SQLStmt CHAR(1024); // SQL Statement

  select;

  when (p_CallType = CALL_OPEN);

    CPLSTSE_open();

    l_SQLStmt =
      'SELECT ' +
      'AREAS.JOB, ' +
      'AREAS.POSITION, ' +
      'AREAS.AREA ' +
      'FROM ' +
      'AREAS ' +
      'INNER JOIN ' +
      'FILES ' +
      'ON ' +
      'AREAS.JOB = FILES.JOB AND ' +
      'AREAS.POSITION = FILES.POSITION AND ' +
      'FILES.TYPE = ''*PHYSICAL''';
    if (p_Where <> *blanks);
      l_SQLStmt = %trim(l_SQLStmt) +
      ' WHERE ' +
      %trim(p_Where);
    endif;

    g_OPEN = *Off;

    Exec SQL
      PREPARE S1 FROM :l_SQLStmt;

    setIgnoreWarnings();
    If SqlCheckState(sqlState);

      Exec SQL
        DECLARE C1 CURSOR FOR S1;

      setIgnoreWarnings();
      If SqlCheckState(sqlState);

        Exec SQL
          OPEN C1;

        setIgnoreWarnings();
        If SqlCheckState(sqlState);

          g_OPEN = *On;

        EndIf;

      EndIf;

    EndIf;

  when (p_CallType = CALL_CLOSE);

    If g_OPEN;

      Exec SQL
        CLOSE C1;

      setIgnoreWarnings();
      SqlCheckState(sqlState);

    EndIf;

    CPLSTSE_close();

  when (p_CallType = CALL_FETCH);

    If Not g_OPEN;

      p_SQLState = '02000';

    Else;

      Exec SQL
        FETCH C1 INTO
           :p_JOB,
           :p_POS,
           :p_ARA;

      setIgnoreWarnings();
      If Not SqlCheckState(sqlState);

        p_SQLState = '02000';

      else;

        CPLSTSE_completeStatusEntry(
          p_JOB  : // --> Job
          p_POS  : // --> Position
          p_ARA  : // --> Area
          p_FILE : // <-- File
          p_LIB  : // <-- Library
          p_RPL  : // <-- Records in production library
          p_RSL  : // <-- Records in shadow library
          p_RTO  : // <-- Records to copy
          p_RCO  : // <-- Records copied
          p_ETC  : // <-- Estimated time
          p_RDK  : // <-- Records with duplicate key
          p_CTA  : // <-- Changes to apply
          p_CAP  : // <-- Changes applied
          p_PRC    // <-- Percent done
        );

      endif;

    endif;

  endsl;

END-PROC LODSTSE_loadStatusEntries;
