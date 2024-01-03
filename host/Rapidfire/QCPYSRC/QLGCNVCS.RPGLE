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

/if not defined(QLGCNVCS)
/define QLGCNVCS

//  Convert Case (QLGCNVCS, QlgConvertCase) API
DCL-PR QlgConvertCase
       extproc('QlgConvertCase');
  i_reqCtrlBlk CHAR(32767) const  options(*varsize);
  i_inData CHAR(32767) const  options(*varsize);
  o_outData CHAR(32767) options(*varsize);
  i_length INT(10) const;
  io_ErrCode CHAR(32767) options(*nopass: *varsize);
END-PR;

DCL-DS QLGCNVCS_reqCtrlBlk_t
       qualified;
  type INT(10);
  CCSID INT(10);
  case INT(10);
  reserved CHAR(10);
END-DS;

DCL-C CVTCASE_TYPE_CCSID
      const(1);
DCL-C CVTCASE_TYPE_TABLE
      const(2);
DCL-C CVTCASE_TYPE_USER_DEF
      const(3);
DCL-C CVTCASE_TO_UPPER
      const(0);
DCL-C CVTCASE_TO_LOWER
      const(1);
DCL-C CVTCASE_CCSID_Job
      const(0);

/endif
