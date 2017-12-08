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
//* Maintain Activity Schedule                                                              *
//*                                                                                         *
//*------------+--------+-------------------------------------------------------------------*
//*   Datum    I Progr. I Beschreibung                                                      *
//*------------+--------+-------------------------------------------------------------------*
//* 24.11.2017 I   FH   I Creation                                                          *
//*------------+--------+-------------------------------------------------------------------*

CTL-OPT NOMAIN;
CTL-OPT DATFMT(*ISO) TIMFMT(*ISO) DECEDIT('0,');
CTL-OPT COPYRIGHT('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT BNDDIR('RAPIDFIRE');
CTL-OPT EXTBININT(*YES);

//*-----------------------------------------------------------------------------------------*

/INCLUDE QCPYSRC,MNTAS
/INCLUDE QCPYSRC,SQLSTS

//*-----------------------------------------------------------------------------------------*

// Set SQL options

Exec SQL
  Set Option
  DatFmt=*ISO,
  TimFmt=*ISO,
  Commit=*NONE,
  CloSQLCsr=*ENDMOD;

//*=========================================================================================*
//* Set Activity                                                                            *
//*=========================================================================================*

DCL-PROC MNTAS_setActivity EXPORT;

  DCL-PI MNTAS_setActivity;
    p_JOB CHAR(10) CONST; // --> Job
    p_STR TIME CONST;     // --> Start time
  END-PI;

  Exec SQL
    Update Activity_Schedule
    Set Activity = Case When Activity = 'Y' Then 'N' Else 'Y' End
    Where Job = :p_JOB And Start_Time = :p_STR;
  setIgnoreWarnings();
  SqlCheckState(sqlState);

END-PROC MNTAS_setActivity;
