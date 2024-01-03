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

/if not defined(CEEGSI)
/define CEEGSI

DCL-PR CEEGSI
       extproc('CEEGSI');
  i_posn INT(10) const;
  o_datatype INT(10);
  o_curlen INT(10);
  o_maxlen INT(10);
  o_fb CHAR(12) options(*omit);
END-PR;

//  Reference fields for CEEGSI API
DCL-DS CEEGSI_inf_t qualified template;
  datatype INT(10);
  currlen INT(10);
  maxlen INT(10);
END-DS;

/endif


