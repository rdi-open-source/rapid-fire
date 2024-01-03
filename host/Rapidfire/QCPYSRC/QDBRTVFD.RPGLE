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

/if not defined(QDBRTVFD)
/define QDBRTVFD

//  Retrieve Database File Description (QDBRTVFD) API
DCL-PR QDBRTVFD ExtPgm('QDBRTVFD');
  o_rcvVar CHAR(32767) Options(*VarSize);                                // <-- Receiver vari
  i_lenRcvVar INT(10) Const;                                             // --> Length of rec
  o_retFileName CHAR(20);                                                // <-- Qualified ret
  i_format CHAR(8) Const;                                                // --> Format name
  i_qFile CHAR(20) Const;                                                // --> Qualified fil
  i_rcdFormat CHAR(10) Const;                                            // --> Record format
  i_override CHAR(1) Const;                                              // --> Override proc
  i_system CHAR(10) Const;                                               // --> System
  i_formatType CHAR(10) Const;                                           // --> Format type
  io_errCode CHAR(32767) Options(*VarSize : *NoPass);                    // <-> Error code
END-PR;

DCL-S QDBRTVFD_Heap
      CHAR(1) Based(QDBRTVFD_Heap_Pointer);

DCL-S QDBRTVFD_Heap_Pointer POINTER;

// ---------------------------------------------------
//  FILD0100:
// ---------------------------------------------------

//  File Definition Header (Qdb_Qdbfh)
DCL-DS Qdb_Qdbfh_t qualified template;
  Qdbfyret INT(10);
  Qdbfyavl INT(10);
  Qdbfhflg UNS(5);
  Reserved_7 CHAR(4);
  Qdbflbnum INT(5);
  Qdbfknum INT(5);
  Qdbfkmxl INT(5);
  Qdbfkflg CHAR(1);
  Qdbfkfdm CHAR(1);
  Reserved_10 CHAR(8);
  Qdbfhaut CHAR(10);
  Qdbfhupl CHAR(1);
  Qdbfhmxm INT(5);
  Qdbfwtfi INT(5);
  Qdbfhfrt INT(5);
  Qdbfhmnum INT(5);
  Qdbfapsize INT(5);
  Reserved_11 CHAR(7);
  Qdbfbrwt INT(5);
  Qaaf CHAR(1);
  Qdbffmtnum INT(5);
  Qdbfhfl2 CHAR(2);
  Qdbfvrm INT(5);
  Qaaf2 CHAR(2);
  Qdbfhcrt CHAR(13);
  Reserved_18 CHAR(2);
  Qdbfhtxt CHAR(50);
  Reserved_19 CHAR(13);
  Qdbfsrcf CHAR(10);
  Qdbfsrcm CHAR(10);
  Qdbfsrcl CHAR(10);
  Qdbfkrcv CHAR(1);
  Reserved_20 CHAR(23);
  Qdbftcid INT(5);
  Qdbfasp INT(5);
  Qdbfnbit CHAR(1);
  Qdbfmxfnum INT(5);
  Reserved_22 CHAR(76);
  Qdbfodic INT(10);
  Reserved_23 CHAR(14);
  Qdbffigl INT(5);
  Qdbfmxrl INT(5);
  Reserved_24 CHAR(8);
  Qdbfgkct INT(5);
  Qdbfos INT(10);
  Reserved_25 CHAR(8);
  Qdbfocs INT(10);
  Reserved_26 CHAR(4);
  Qdbfpact CHAR(2);
  Qdbfhrls CHAR(6);
  Reserved_27 CHAR(20);
  Qdbpfof INT(10);
  Qdblfof INT(10);
  Qdbfnlsb CHAR(1);
  Qdbflang CHAR(3);
  Qdbfcnty CHAR(2);
  Qdbfjorn INT(10);
  Qdbfevid INT(10);
  Reserved_28 CHAR(14);
END-DS;

//  File Scope Array (Qdb_Qdbfb)
DCL-DS Qdb_Qdbfb_t qualified template;
  Reserved_48 CHAR(48);
  Qdbfbf CHAR(10);
  Qdbfbfl CHAR(10);
  Qdbft CHAR(10);
  Reserved_49 CHAR(37);
  Qdbfbgky INT(5);
  Reserved_50 CHAR(2);
  Qdbfblky INT(5);
  Reserved_51 CHAR(2);
  Qdbffogl INT(5);
  Reserved_52 CHAR(3);
  Qdbfsoon INT(5);
  Qdbfsoof INT(10);
  Qdbfksof INT(10);
  Qdbfkyct INT(5);
  Qdbfgenf INT(5);
  Qdbfodis INT(10);
  Reserved_53 CHAR(15) POS(146);
END-DS;

//  Key Specification Array (Qdb_Qdbfk)
DCL-DS Qdb_Qdbfk_t qualified template;
  Qdbfkfld CHAR(10);
  Reserved_59 CHAR(3);
  Qdbfksq CHAR(1);
  Reserved_61 CHAR(18);
END-DS;

// ---------------------------------------------------
//  FILD0200
// ---------------------------------------------------

//  Format Definition Header (Qdb_Qddfmt)
DCL-DS Qdb_Qddfmt_t LEN(256) qualified template;
  bytRet INT(10);
  bytAvl INT(10);
  Reserved_62 CHAR(24);
  Qddfmtf CHAR(1);
  Qddfxlto INT(10);
  Qddfrcao INT(10);
  Qddfdico INT(10);
  Qddfrcid INT(5);
  Qddfsrcd INT(5);
  Qddfrtcd INT(5);
  Qddfrlcd INT(5);
  Reserved_64 CHAR(7);
  Qddftflgs CHAR(1);
  Qddflgs UNS(3);
  Reserved_67 CHAR(4);
  Qddfrlen INT(10);
  Qddfname CHAR(10);
  Qddfseq CHAR(13);
  Qddftext CHAR(50);
  Qddffldnum INT(5);
  Qddf_Identity_Off
  INT(10) POS(146);
END-DS;
// Qddffldx            257    CHAR(*)   Start of field definition array (Qdb_Qddffld).

//  Field Header (Qdb_Qddffld)
DCL-DS Qdb_Qddffld_t qualified template;
  Qddfdefl INT(10);
  Qddffldi CHAR(30);
  Qddfflde CHAR(30);
  Qddfftyp CHAR(2);
  Qddffiob CHAR(1);
  Qddffobo INT(10);
  Qddffibo INT(10);
  Qddffldb INT(5);
  Qddffldd INT(5);
  Qddffldp INT(5);
  //  ...
  Qddffldst2 UNS(3) POS(86);
  //  ...
  Qddflalc UNS(5) POS(92);
  Qddfdttf CHAR(1);
  Qddfdtts CHAR(1);
  Qddfcsid UNS(5);
  //  ...
  Qddfdftd UNS(10) POS(195);
  //  ...
  Qddftxtd INT(10) POS(209);
  Reserved_102 CHAR(2);
  Qddfrefd INT(10);
  Qddfedtl INT(5);
  Qddfedtd INT(10);
  Reserved_76 INT(5);
  Qddfchd INT(10);
  //  ...
  Qddfxals INT(10) POS(237);
END-DS;

//  Reference Information (Qdb_Qddfrefi)
DCL-DS Qdb_Qddfrefi_t qualified template;
  Qddfrcde CHAR(1);
  Qddfrfil CHAR(10);
  Qddfrlib CHAR(10);
  Qddfrfmt CHAR(10);
  Qddfrfld CHAR(30);
  Reserved_78 CHAR(19);
END-DS;

//  Edit Code/Edit Word Information (Qdb_Qddfedcw)
DCL-DS Qdb_Qddfedcw_t qualified template;
  Qddfecdi CHAR(2);
  Qddfecde CHAR(1) OVERLAY(Qddfecdi);
  Qddfecdx CHAR(1) OVERLAY(Qddfecdi:2);
  Reserved_79 CHAR(14);
  Qddfewd CHAR(64);
END-DS;

//  Field Text (Qdb_Qddfftxt)
DCL-DS Qdb_Qddfftxt_t qualified template;
  Qddfftxt CHAR(50);
END-DS;

//  Alias name structure (Qdb_Qddfalis)
DCL-DS Qdb_Qddfalis_t qualified template;
  Qddfalsl INT(5);
  Reserved_85 CHAR(14);
  Qddfalsn CHAR(258);
END-DS;

//  Default Value Description Information (Qdb_Qddfdft)
DCL-DS Qdb_Qddfdft_t qualified template;
  Qddfdftl INT(5);
  Qddfdfta UNS(3);
  Reserved_87 CHAR(13);
END-DS;
// Qddfdftv             17    CHAR(*)   Default Value.

DCL-C QDDFDFTA_GRAPHIC
      128;                                                               // DBCS/graphic d
DCL-C QDDFDFTA_HEX
      32;                                                                // Hex default.
DCL-C QDDFDFTA_NULL
      4;                                                                 // Null default.
DCL-C QDDFDFTA_DATE_TIME
      2;                                                                 // Current date/t
DCL-C QDDFDFTA_DFT_KEYWORD
      1;                                                                 // DFT/DFTVAL spe

//  Column Heading Information (Qdb_Qddfcolh)
DCL-DS Qdb_Qddfcolh_t qualified template;
  Qddfch1 CHAR(20);
  Qddfch2 CHAR(20);
  Qddfch3 CHAR(20);
END-DS;

/endif

