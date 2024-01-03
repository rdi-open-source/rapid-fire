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

/if not defined(QUSROBJD)
/define QUSROBJD

//  Retrieve Object Description (QUSROBJD) API
DCL-PR QUSROBJD
       extpgm('QUSROBJD');
  o_rcvVar CHAR(65535) options(*varsize);
  i_lenRcvVar INT(10) const;
  i_format CHAR(8) const;
  i_qObj CHAR(20) const;
  i_type CHAR(10) const;
  io_errCode CHAR(65535) options(*nopass: *varsize);                     // | OptGrp 1
  i_auxStgCtrl CHAR(65535) const  options(*nopass: *varsize);            // | OptGrp 2
END-PR;

//  Auxiliary Storage Pool (ASP) Control Format
DCL-DS auxStgCtrl_t qualified template;
  length INT(10);
  device CHAR(10);
  searchType CHAR(10);
END-DS;

DCL-DS objd0100_t qualified template;
  bytRet INT(10);
  bytAvl INT(10);
  name CHAR(10);
  lib CHAR(10);
  type CHAR(10);
  rtnLib CHAR(10);
  auxStgP INT(10);
  owner CHAR(10);
  domain CHAR(2);
  crtDatTim CHAR(13);
  chgDatTim CHAR(13);
END-DS;

DCL-DS objd0200_t qualified template;
  //    OBJD0100 ...
  bytRet INT(10);
  bytAvl INT(10);
  name CHAR(10);
  lib CHAR(10);
  type CHAR(10);
  rtnLib CHAR(10);
  auxStgP INT(10);
  owner CHAR(10);
  domain CHAR(2);
  crtDatTim CHAR(13);
  chgDatTim CHAR(13);
  //    OBJD0200 ...
  extObjAtr CHAR(10);
  text CHAR(50);
  srcFile CHAR(10);
  srcLib CHAR(10);
  srcMbr CHAR(10);
END-DS;

DCL-DS objd0300_t qualified template;
  //    OBJD0100 ...
  bytRet INT(10);
  bytAvl INT(10);
  name CHAR(10);
  lib CHAR(10);
  type CHAR(10);
  rtnLib CHAR(10);
  auxStgP INT(10);
  owner CHAR(10);
  domain CHAR(2);
  crtDatTim CHAR(13);
  chgDatTim CHAR(13);
  //    OBJD0200 ...
  extObjAtr CHAR(10);
  text CHAR(50);
  srcFile CHAR(10);
  srcLib CHAR(10);
  srcMbr CHAR(10);
  //    OBJD0300 ...
  srcFDatTim CHAR(13);
  savDatTim CHAR(13);
  rstDatTim CHAR(13);
  crtUsrPrf CHAR(10);
  crtSys CHAR(8);
  resDat CHAR(7);
  savSize INT(10);
  savSeqNbr INT(10);
  stg CHAR(10);
  savCmd CHAR(10);
  savVolID CHAR(71);
  savDev CHAR(10);
  savFName CHAR(10);
  savFLibName CHAR(10);
  savLbl CHAR(17);
  sysLvl CHAR(9);
  compiler CHAR(16);
  objLvl CHAR(8);
  usrChg CHAR(1);
  licPgm CHAR(16);
  ptf CHAR(10);
  apar CHAR(10);
END-DS;

DCL-DS objd0400_t qualified template;
  //    OBJD0100 ...
  bytRet INT(10);
  bytAvl INT(10);
  name CHAR(10);
  lib CHAR(10);
  type CHAR(10);
  rtnLib CHAR(10);
  auxStgP INT(10);
  owner CHAR(10);
  domain CHAR(2);
  crtDatTim CHAR(13);
  chgDatTim CHAR(13);
  //    OBJD0200 ...
  extObjAtr CHAR(10);
  text CHAR(50);
  srcFile CHAR(10);
  srcLib CHAR(10);
  srcMbr CHAR(10);
  //    OBJD0300 ...
  srcFDatTim CHAR(13);
  savDatTim CHAR(13);
  rstDatTim CHAR(13);
  crtUsrPrf CHAR(10);
  crtSys CHAR(8);
  resDat CHAR(7);
  savSize INT(10);
  savSeqNbr INT(10);
  stg CHAR(10);
  savCmd CHAR(10);
  savVolID CHAR(71);
  savDev CHAR(10);
  savFName CHAR(10);
  savFLibName CHAR(10);
  savLbl CHAR(17);
  sysLvl CHAR(9);
  compiler CHAR(16);
  objLvl CHAR(8);
  usrChg CHAR(1);
  licPgm CHAR(16);
  ptf CHAR(10);
  apar CHAR(10);
  //    OBJD0400 ...
  lastUseDat CHAR(7);
  useInfUpd CHAR(1);
  daysUseCnt INT(10);
  objSize INT(10);
  objSizeMlt INT(10);
  objCmpSts CHAR(1);
  alwChgPgm CHAR(1);
  chgByPgm CHAR(1);
  usrDfnAtr CHAR(10);
  aspOflw CHAR(1);
  savActDatTim CHAR(13);
  objAudVal CHAR(10);
  priGrp CHAR(10);
  jrnStat CHAR(1);
  jrnName CHAR(10);
  jrnLibName CHAR(10);
  jrnImages CHAR(1);
  jrnOmitE CHAR(1);
  jrnStrDatTim CHAR(13);
  dgtSigned CHAR(1);
  savSizeUnits INT(10);
  savSizeMlt INT(10);
  libASPNbr INT(10);
  objASPDev CHAR(10);
  libASPDev CHAR(10);
  dgtSignedTrst CHAR(1);
  dgtSignedMult CHAR(1);
  reserved_1 CHAR(2);
  priAscSpcSize INT(10);
  optSpcAlgn CHAR(1);
  objASPGrp CHAR(10);
  libASPGrp CHAR(10);
  strJrnRcv CHAR(10);
  strJrnRcvLib CHAR(10);
  strJrnRcvLibASPDev
  CHAR(10);
  strJrnRcvLibASPGrp
  CHAR(10);
  reserved_2 CHAR(1);
END-DS;

/endif
