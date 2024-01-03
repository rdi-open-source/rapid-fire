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
//* Test Service Program EXCSQL.                                                            *
//*                                                                                         *
//*-----------------------------------------------------------------------------------------*
//*  >>PRE-COMPILER<<                                                                       *
//*    >>CRTCMD<< CRTBNDRPG    PGM(&LI/&OB) +                                               *
//*                              SRCFILE(&SL/&SF) SRCMBR(&SM);                              *
//*    >>IMPORTANT<<                                                                        *
//*      >>PARM<<  DBGVIEW(*ALL);                                                           *
//*      >>PARM<<  OPTION(*EVENTF);                                                         *
//*      >>PARM<<  TGTRLS(&TR);                                                             *
//*    >>END-IMPORTANT<<                                                                    *
//*    >>EXECUTE<<                                                                          *
//*  >>END-PRE-COMPILER<<                                                                   *
//*------------+--------+-------------------------------------------------------------------*
//*   Datum    I Progr. I Beschreibung                                                      *
//*------------+--------+-------------------------------------------------------------------*
//* 04.02.2018 I   TR   I Creation                                                          *
//*------------+--------+-------------------------------------------------------------------*

CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,');
CTL-OPT DftActGrp(*NO) ActGrp('RAPIDFIRE') BNDDIR('RAPIDFIRE');
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

/Include QCPYSRC,EXCSQL

/Include QCPYSRC,QMHSNDPM

/Include QCPYSRC,ERRC0100

DCL-S Rc IND;
DCL-S Message CHAR(52); // Last error message

//*-----------------------------------------------------------------------------------------*

// Test a successful execution of a SET statement

Rc = EXCSQL_execute_Stmt('SET SCHEMA QGPL');
If (Not Rc);
  Kill('Rc must be set *ON for SUCCESS');
EndIf;

Message = EXCSQL_get_Message();
If (Message <> '');
  Kill('Message must be blank for successful executions.');
EndIf;

// Test execution of a forbidden SELECT statement

Rc = EXCSQL_execute_Stmt('SELECT * FROM SYSIBM.SYSDUMMY1');
If (Rc);
  Kill('Rc must be set *OFF for ERROR');
EndIf;

Message = EXCSQL_get_Message();
If (Message = '');
  Kill('Message must not be blanks on errors.');
EndIf;

Info('*** SUCCESS ***');

*INLR = *ON;

//*=========================================================================================*
//* Display an *INFO message.                                                               *
//*=========================================================================================*
Dcl-Proc Info;
Dcl-Pi *N;
  i_text varchar(512) const;
End-Pi;

SndMsg(i_text : '*INFO');

End-Proc;

//*=========================================================================================*
//* Kills the program by sending an *ESCAPE mesage.                                         *
//*=========================================================================================*
Dcl-Proc Kill;
Dcl-Pi *N;
  i_text varchar(512) const;
End-Pi;

SndMsg(i_text : '*ESCAPE');

End-Proc;

//*=========================================================================================*
//* Sends a message of the specified text and type.                                         *
//*=========================================================================================*
Dcl-Proc SndMsg;
Dcl-Pi *N;
  i_text varchar(512) const;
  i_msgType char(10) const;
End-Pi;

dcl-ds qMsgF qualified;
  file char(10);
  library char(10);
End-Ds;

Dcl-s msgKey char(4);

Clear qMsgF;
qMsgF.file = 'QCPFMSG';
qMsgF.library = '*LIBL';

Clear errc0100;

qmhsndpm('CPF9898': qMsgF: i_text: %len(i_text)
         : i_msgType: '*PGMBDY': 1: msgKey: errc0100);

End-Proc;
