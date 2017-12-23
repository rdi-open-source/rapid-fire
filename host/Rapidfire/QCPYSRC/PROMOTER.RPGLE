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

DCL-PR PROMOTER_initialize;
  *N CHAR(10) Const;                                                     // --> Job
  *N CHAR(10) Const;                                                     // --> Test
END-PR;

DCL-PR PROMOTER_promote;
  *N CHAR(10) Const;                                                     // --> Job
  *N CHAR(10) Const;                                                     // --> File Library
  *N CHAR(10) Const;                                                     // --> File
END-PR;

DCL-PR PROMOTER_apply_Changes;
  *N CHAR(10) Const;                                                     // --> Job
  *N CHAR(10) Const;                                                     // --> File Library
  *N CHAR(10) Const;                                                     // --> File
END-PR;

DCL-PR PROMOTER_clean;
  *N CHAR(10) Const;                                                     // --> Job
END-PR;

DCL-PR PROMOTER_generate_Copy_Program;
  p_Job
  CHAR(10) Const;
  p_Position
  ZONED(6) Const;
  p_Library
  CHAR(10) Const;
  p_Shadow_Library
  CHAR(10) Const;
  p_File
  CHAR(10) Const;
  p_Conversion_Program_Library
  CHAR(10) Const;
  p_Conversion_Program
  CHAR(10) Const;
  p_test_mode
  IND Const Options(*NoPass);
  p_Source_Member
       Const LikeDs(qMbr_t) Options(*NoPass);
END-PR;

DCL-PR PROMOTER_reapply_All_Changes;
  p_Job
  CHAR(10) Const;
  p_Position
  ZONED(6) Const;
  p_Area
  CHAR(10) Const;
END-PR;

/copy QCPYSRC,TYPES_H

