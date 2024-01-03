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

/if not defined(RTVMSGD)
/define RTVMSGD

DCL-PR rtvMsgText VARCHAR(250)                                           // <-- Rückkehrwert
       extproc('RTVMSGD_+
       rtvMsgText+
       ');
  i_msgID CHAR(7) Const;                                                 // --> Nachrichten-I
  i_msgF CHAR(10) Const;                                                 // --> Nachrichtenda
  i_lib CHAR(10) Const Options(*NoPass: *Omit);                          // --> Bibliothek Na
  i_data VARCHAR(256) Const                                              // --> Nachrichtenda
       Options(*NoPass: *Omit);
END-PR;

DCL-PR rtvMsgHelp VARCHAR(3000)                                          // <-- Rückkehrwert
       extproc('RTVMSGD_+
       rtvMsgHelp+
       ');
  i_msgID CHAR(7) Const;                                                 // --> Nachrichten-I
  i_msgF CHAR(10) Const;                                                 // --> Nachrichtenda
  i_lib CHAR(10) Const Options(*NoPass: *Omit);                          // --> Bibliothek Na
  i_data VARCHAR(256) Const                                              // --> Nachrichtenda
       Options(*NoPass: *Omit);
END-PR;
/endif
