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

/if not defined(BOOL)
/define BOOL

//  Tests whether one or more bits are set in a given value.
DCL-PR isBitOn
       IND
       extproc('BOOL_+
       isBitOn+
       ');
  i_testBit UNS(10) value;
  i_value UNS(10) value;
END-PR;

/endif

