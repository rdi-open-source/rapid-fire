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
  p_Area
  CHAR(10) Const;
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
  p_is_option_81
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

DCL-PR PROMOTER_get_Source_Field_Prefix_Of_Last_Generated_Copy_Program CHAR(2);
END-PR;

DCL-PR PROMOTER_get_Target_Field_Prefix_Of_Last_Generated_Copy_Program CHAR(2);
END-PR;

DCL-PR PROMOTER_get_Source_Field_Prefix CHAR(2);
  p_Conversion_Program
  CHAR(10) Const;
  p_SrcLibrary
  CHAR(10) Const;
  p_SrcFile
  CHAR(10) Const;
  p_TgtLibrary
  CHAR(10) Const;
  p_TgtFile
  CHAR(10) Const;
  p_Src_Pfx
  CHAR(2);
END-PR;

DCL-PR PROMOTER_get_Target_Field_Prefix CHAR(2);
  p_Conversion_Program
  CHAR(10) Const;
  p_SrcLibrary
  CHAR(10) Const;
  p_SrcFile
  CHAR(10) Const;
  p_TgtLibrary
  CHAR(10) Const;
  p_TgtFile
  CHAR(10) Const;
  p_Tgt_Pfx
  CHAR(2);
END-PR;

/copy QCPYSRC,TYPES_H

