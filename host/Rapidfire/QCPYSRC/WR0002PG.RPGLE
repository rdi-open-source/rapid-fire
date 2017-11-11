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
//* >>PRE-COMPILER<<                                                                        *
//*   >>CRTCMD<< CRTBNDRPG PGM(&LI/&OB) SRCFILE(&SL/&SF) SRCMBR(&SM);                       *
//*   >>IMPORTANT<<                                                                         *
//*     >>PARM<< DFTACTGRP(*NO);                                                            *
//*     >>PARM<< DBGVIEW(&U1);                                                              *
//*     >>PARM<< ACTGRP(&U2);                                                               *
//*     >>PARM<< TGTRLS(&TR);                                                               *
//*     >>PARM<< OPTION(*EVENTF);                                                           *
//*   >>END-IMPORTANT<<                                                                     *
//*   >>EXECUTE<<                                                                           *
//* >>END-PRE-COMPILER<<                                                                    *
//*-----------------------------------------------------------------------------------------*

DCL-PR WR0002PG
       ExtPgm('WR0002PG');
  *N CHAR(10) Const;                                                     // --> Job
END-PR;
