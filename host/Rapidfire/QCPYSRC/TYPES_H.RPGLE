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

/if not defined(TYPES_H)
/define TYPES_H

DCL-DS qJob_t
       qualified template;
  name CHAR(10);
  user CHAR(10);
  nbr CHAR(6);
END-DS;

DCL-DS qObj_t
       qualified template;
  name CHAR(10);
  lib CHAR(10);
END-DS;

DCL-DS qMbr_t
       qualified template;
  file CHAR(10);
  lib CHAR(10);
  name CHAR(10);
END-DS;

DCL-DS msg_t qualified template;
  ID CHAR(7);
  type CHAR(10);
  file CHAR(10);
  lib CHAR(10);
  data CHAR(512);
END-DS;

DCL-C TRUE *on;
DCL-C FALSE *off;

/endif


