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
//* Retrieve Message Description                                                            *
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
//*------------+--------+-------------------------------------------------------------------*
//*   Datum    I Progr. I Beschreibung                                                      *
//*------------+--------+-------------------------------------------------------------------*
//* 17.03.2016 I   TR   I Programm erstellt                                                 *
//*------------+--------+-------------------------------------------------------------------*

CTL-OPT NoMain;
CTL-OPT DatFmt(*Eur) TimFmt(*Eur) DecEdit('0,');
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

// Prozedurenprototyp für Prozedur 'SndErrMsg'

/Copy QCPYSRC,RTVMSGD

// Prozedurenprototyp für Prozedur 'QMHSNDPM'

/Copy QCPYSRC,QMHRTVM

// Datenstruktur für Format 'ERRC0100' für Fehlercode

/Copy QCPYSRC,ERRC0100

// Sonstige Felddeklarationen

/Copy QCPYSRC,TYPES_H

//*=========================================================================================*
//* Abrufen Text einer Nachrichtenbeschreibung                                              *
//*=========================================================================================*

DCL-PROC rtvMsgText EXPORT;

  DCL-PI *N VARCHAR(250);
    i_msgID CHAR(7) Const;
    i_msgF CHAR(10) Const;
    i_lib CHAR(10) Const Options(*NoPass: *Omit);
    i_data VARCHAR(256) Const
         Options(*NoPass: *Omit);
  END-PI;

  DCL-S msgText VARCHAR(250);

  DCL-S data Like(i_data);
  DCL-DS qMsgF LikeDs(qObj_t);
  DCL-DS rtvm0100 LikeDs(rtvm0100_t);

  if (%parms() >= %parmnum(i_data) and %addr(i_data) <> *null);
     data = i_data;
  else;
     data = '';
  endif;

  qMsgF.name = i_msgF;
  qMsgF.lib = i_lib;

  if (rtvMsgD(rtvm0100: 'RTVM0100': i_msgID: %trimR(data):
              qMsgF: '*YES': '*NO') <= 0);
     return '*' + i_msgID;
  endif;

  if (rtvm0100.lenMsgR <= 0);
     return '';
  endif;

  msgText = %subst(rtvm0100.data: 1: rtvm0100.lenMsgR);

  return msgText;

END-PROC;

//*=========================================================================================*
//* Abrufen Hilfetext einer Nachrichtenbeschreibung                                         *
//*=========================================================================================*

DCL-PROC rtvMsgHelp EXPORT;

  DCL-PI *N VARCHAR(3000);
    i_msgID CHAR(7) Const;
    i_msgF CHAR(10) Const;
    i_lib CHAR(10) Const Options(*NoPass: *Omit);
    i_data VARCHAR(256) Const
         Options(*NoPass: *Omit);
  END-PI;

  DCL-S msgHelp VARCHAR(3000);

  DCL-S data Like(i_data);
  DCL-DS qMsgF LikeDs(qObj_t);
  DCL-DS rtvm0100 LikeDs(rtvm0100_t);

  if (%parms() >= %parmnum(i_data) and %addr(i_data) <> *null);
     data = i_data;
  else;
     data = '';
  endif;

  qMsgF.name = i_msgF;
  qMsgF.lib = i_lib;

  if (rtvMsgD(rtvm0100: 'RTVM0100': i_msgID: %trimR(data):
              qMsgF: '*YES': '*NO') <= 0);
     return '*' + i_msgID;
  endif;

  if (rtvm0100.lenMsgHlpR <= 0);
     return '';
  endif;

  msgHelp = %subst(rtvm0100.data
                   : 1 + rtvm0100.lenMsgR
                   : rtvm0100.lenMsgHlpR);

  return msgHelp;

END-PROC;

//*-----------------------------------------------------------------------------------------*
//* *** Private ***                                                                         *
//* Retrieve message description                                                            *
//*-----------------------------------------------------------------------------------------*

DCL-PROC rtvMsgD;

  DCL-PI *N INT(10);
    o_rtnData CHAR(4096);
    i_format CHAR(8) const;
    i_msgID CHAR(7) const;
    i_rplData VARCHAR(4096) const;
    i_qMsgF const  likeds(qObj_t);
    i_rplVars CHAR(10) const;
    i_rtnCtrlChr CHAR(10) const;
  END-PI;

  //  Local fields
  DCL-DS errCode likeds(ERRC0100) inz;
  DCL-DS rtnInf qualified based(pRtnInf);
    bytRet INT(10);
    bytAvl INT(10);
  END-DS;

  QMHRTVM(
     o_rtnData: %size(o_rtnData): i_format: i_msgID: i_qMsgF:
     i_rplData: %len(i_rplData): i_rplVars: i_rtnCtrlChr: errCode);

  if (errCode.Exeption_Id <> '');
    clear o_rtnData;
    return -1;
  endif;

  pRtnInf = %addr(o_rtnData);

  return rtnInf.bytRet;

END-PROC;
