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

/if not defined(QMHRTVM)
/define QMHRTVM

//  Retrieve Message (QMHRTVM) API
DCL-PR QMHRTVM
       extpgm('QMHRTVM');
  o_msgInf CHAR(65535) options(*varsize);
  i_msgInfLen INT(10) const;
  i_format CHAR(8) const;
  i_msgID CHAR(7) const;
  i_qMsgF CHAR(20) const;
  i_rplData CHAR(65535) const  options(*varsize );
  i_rpldataLen INT(10) const;
  i_rplVars CHAR(10) const;
  i_rtnCtrlChrs CHAR(10) const;
  io_ErrCode CHAR(65535) options(*varsize );
  i_rtvOpt CHAR(10) const  options(*nopass  );
  i_toCcsid INT(10) const  options(*nopass  );
  i_ccsidRplDta INT(10) const  options(*nopass  );
END-PR;

DCL-DS rtvm0100_t qualified           based(pDummy);
  //  Bytes returned
  bytRet INT(10);
  //  Bytes available
  bytAvl INT(10);
  //  Length of message returned
  lenMsgR INT(10);
  //  Length of message available
  lenMsgA INT(10);
  //  Length of msg help returned
  lenMsgHlpR INT(10);
  //  Length of msg help available
  lenMsgHlpA INT(10);
  //  Variable length data
  data CHAR(4096);
END-DS;
//                       CHAR(*)  Message
//                       CHAR(*)  Message help

DCL-DS rtvm0200_t qualified           based(pDummy);
  //  Bytes returned
  bytRet INT(10);
  //  Bytes available
  bytAvl INT(10);
  //  Message severity
  msgSev INT(10);
  //  Alert index
  altIdx INT(10);
  // Alert Option
  altOpt CHAR(9);
  // Log indicator
  logInd CHAR(1);
  // Reserved
  reserved_1 CHAR(2);
  // Length of default reply ret.
  lenDftRpyR INT(10);
  // Length of default reply avl.
  lenDftRpyA INT(10);
  // Length of message returned
  lenMsgR INT(10);
  // Length of message available
  lenMsgA INT(10);
  // Length of message help ret.
  lenMsgHlpR INT(10);
  // Length of message help avl.
  lenMsgHlpA INT(10);
  //  Variable length data
  data CHAR(4096);
END-DS;
//                       CHAR(*)  Default reply
//                       CHAR(*)  Message
//                       CHAR(*)  Message help

DCL-DS rtvm0300_t qualified           based(pDummy);
  //  Bytes returned
  bytRet INT(10);
  //  Bytes available
  bytAvl INT(10);
  //  Message severity
  msgSev INT(10);
  //  Alert index
  altIdx INT(10);
  // Alert Option
  altOpt CHAR(9);
  // Log indicator
  logInd CHAR(1);
  // Message ID
  msgID CHAR(7);
  // Reserved
  reserved_01 CHAR(3);
  // Nbr. of subst. var. formats
  numVarFmt INT(10);
  // CCSID conv. stat. ind. text
  csIndTxt INT(10);
  // CCSID conv. stat. ind. data
  csIndDta INT(10);
  // CCSID of Text returned
  ccsidTxt INT(10);
  // Offset of default reply
  ofsDftR INT(10);
  // Length of default reply ret.
  lenDftRpyR INT(10);
  // Length of default reply avl.
  lenDftRpyA INT(10);
  // Offset of message
  ofsMsg INT(10);
  // Length of message returned
  lenMsgR INT(10);
  // Length of message available
  lenMsgA INT(10);
  // Offset of message help
  ofsMsgHlp INT(10);
  // Length of message help ret.
  lenMsgHlpR INT(10);
  // Length of message help avl.
  lenMsgHlpA INT(10);
  // Ofs. of subst. var. formats
  ofsVarFmt INT(10);
  // Length subst. var. fmt. ret.
  lenVarFmtR INT(10);
  // Length subst. var. fmt. avl.
  lenVarFmtA INT(10);
  // Length subst. var. element
  lenVarFmtE INT(10);
  //  Variable length data
  data CHAR(4096);
END-DS;
//                       CHAR(*)  Default reply
//                       CHAR(*)  Message
//                       CHAR(*)  Message help
//                       CHAR(*)  Substitution variable formats

DCL-DS rtvm0400_t qualified           based(pDummy);
  //  Bytes returned
  bytRet INT(10);
  //  Bytes available
  bytAvl INT(10);
  //  Message severity
  msgSev INT(10);
  //  Alert index
  altIdx INT(10);
  // Alert Option
  altOpt CHAR(9);
  // Log indicator
  logInd CHAR(1);
  // Message ID
  msgID CHAR(7);
  // Reserved
  reserved_01 CHAR(3);
  // Nbr. of subst. var. formats
  numVarFmt INT(10);
  // CCSID conv. stat. ind. text
  csIndTxt INT(10);
  // CCSID conv. stat. ind. data
  csIndDta INT(10);
  // CCSID of Text returned
  ccsidTxt INT(10);
  // Offset of default reply
  ofsDftR INT(10);
  // Length of default reply ret.
  lenDftRpyR INT(10);
  // Length of default reply avl.
  lenDftRpyA INT(10);
  // Offset of message
  ofsMsg INT(10);
  // Length of message returned
  lenMsgR INT(10);
  // Length of message available
  lenMsgA INT(10);
  // Offset of message
  ofsMsgHlp INT(10);
  // Length of message help ret.
  lenMsgHlpR INT(10);
  // Length of message help avl.
  lenMsgHlpA INT(10);
  // Ofs. of subst. var. formats
  ofsVarFmt INT(10);
  // Length subst. var. fmt. ret.
  lenVarFmtR INT(10);
  // Length subst. var. fmt. avl.
  lenVarFmtA INT(10);
  // Length subst. var. element
  lenVarFmtE INT(10);
  // Reply type
  rpyType CHAR(10);
  // Reserved
  reserved_02 CHAR(2);
  // Maximum reply length
  maxRpyLen INT(10);
  // Maximum reply decimal positions
  maxRpyDecPos INT(10);
  // Offset of valid reply value entries
  ofsRpyE INT(10);
  // Number of valid reply values entries returned
  numRpyER INT(10);
  // Length of valid reply value entries returned
  lenRpyER INT(10);
  // Length of valid reply value entries available
  lenRpyEA INT(10);
  // Length of valid reply value entry
  lenRpyE INT(10);
  // Offset of special reply value entries
  ofsSpcRpyE INT(10);
  // Number of special reply values returned
  numSpcRpyER INT(10);
  // Length of special reply value entries returned
  lenSpcRpyER INT(10);
  // Length of special reply value entries available
  lenSpcRpyEA INT(10);
  // Length of special reply value entry
  lenSpcRpyE INT(10);
  // Offset of lower range reply value
  ofsLowRpyE INT(10);
  // Length of lower range reply value returned
  lenLowRpyER INT(10);
  // Length of lower range reply value available
  lenLowRpyEA INT(10);
  // Offset of upper range reply value
  ofsUpRpyE INT(10);
  // Length of upper range reply value returned
  ofsUpRpyER INT(10);
  // Length of upper range reply value available
  ofsUpRpyEA INT(10);
  // Offset of relational test entry
  ofsRelTestE INT(10);
  // Length of relational test entry returned
  lenRelTestER INT(10);
  // Length of relational test entry available
  lenRelTestEA INT(10);
  // Message creation date
  crtDate CHAR(7);
  // Reserved
  reserved_03 CHAR(1);
  // Message creation level number
  crtLvlNbr INT(10);
  // Message modification date
  modDate CHAR(7);
  // Reserved
  reserved_04 CHAR(1);
  // Message modification level number
  modLvlNbr INT(10);
  // Stored CCSID of message
  storedCCSID INT(10);
  // Offset of dump list entries
  ofsDmpListE INT(10);
  // Number of dump list entries returned
  numDmpListER INT(10);
  // Length of dump list entries returned
  lenDmpListER INT(10);
  // Length of dump list entries available
  lenDmpListEA INT(10);
  // Default program name
  dftPgmName CHAR(10);
  // Default program library name
  dftPgmLib CHAR(10);
  //  Variable length data
  data CHAR(4096);
END-DS;
//                       CHAR(*)  Default reply
//                       CHAR(*)  Message
//                       CHAR(*)  Message help
//                       CHAR(*)  Substitution variable formats
//                       CHAR(*)  Valid reply value entries
//                       CHAR(*)  Special reply value entries
//                       CHAR(*)  Lower range reply value
//                       CHAR(*)  Upper range reply value
//                       CHAR(*)  Relational test entry
//                       CHAR(*)  Dump list entries

DCL-DS qmhrtvm_substVarFmt_t
       qualified           based(pDummy);
  length INT(10) POS(1);
  sizeOrDecPos INT(10) POS(5);
  size INT(10) overlay(sizeOrDecPos);
  decPos INT(10) overlay(sizeOrDecPos);
  type CHAR(10) POS(9);
END-DS;
//                       CHAR(*)  Reserved
/endif
