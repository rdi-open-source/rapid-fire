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

DCL-DS FILD0100;
  Qdbfyret INT(10);                                                      // Bytes returned
  Qdbfyavl INT(10);                                                      // Bytes provided
END-DS;

// File Definition Header (Qdb_Qdbfh)

DCL-DS Qdb_Qdbfh Based(Qdb_QdbfhP);
  *N INT(10);                                                            // Bytes returned
  *N INT(10);                                                            // Bytes provided
  Qdbfhflg CHAR(2);                                                      // Attributes bytes.
  *N CHAR(4);                                                            // Reserved.
  Qdbflbnum INT(5);                                                      // Number of data me
  Qdbfknum INT(5);                                                       // Number of key fie
  Qdbfkmxl INT(5);                                                       // Maximum key lengt
  Qdbfkflg CHAR(1);                                                      // Keyed sequence ac
  Qdbfkfdm CHAR(1);                                                      // Access path maint
  *N CHAR(8);                                                            // Reserved.
  Qdbfhaut CHAR(10);                                                     // Public authority
  Qdbfhupl CHAR(1);                                                      // Preferred storage
  Qdbfhmxm INT(5);                                                       // Maximum members
  Qdbfwtfi INT(5);                                                       // Maximum file wait
  Qdbfhfrt INT(5);                                                       // Records to force
  Qdbfhmnum INT(5);                                                      // Number of members
  *N CHAR(9);                                                            // Reserved.
  Qdbfbrwt INT(5);                                                       // Maximum record wa
  Qaaf CHAR(1);                                                          // Additional attrib
  Qdbffmtnum INT(5);                                                     // Total number of r
  Qdbfhfl2 CHAR(2);                                                      // Additional attrib
  Qdbfvrm INT(5);                                                        // First supported V
  Qaaf2 CHAR(2);                                                         // Additional attrib
  Qdbfhcrt CHAR(13);                                                     // File level identi
  *N CHAR(2);                                                            // Reserved.
  Qdbfhtxt CHAR(50);                                                     // Text description
  *N CHAR(13);                                                           // Reserved.
  Qdbfsrcf CHAR(10);                                                     // Source file name.
  Qdbfsrcm CHAR(10);                                                     // Source file membe
  Qdbfsrcl CHAR(10);                                                     // Source file libra
  Qdbfkrcv CHAR(1);                                                      // Access path recov
  *N CHAR(23);                                                           // Reserved.
  Qdbftcid INT(5);                                                       // CCSID for text de
  Qdbfasp CHAR(2);                                                       // Auxiliary storage
  *N CHAR(1);                                                            // Reserved.
  Qdbfmxfnum INT(5);                                                     // Maximum number of
  *N CHAR(76);                                                           // Reserved.
  Qdbfodic INT(10);                                                      // Offset from Qdb_Q
  *N CHAR(14);                                                           // Reserved.
  Qdbffigl INT(5);                                                       // File generic key
  Qdbfmxrl INT(5);                                                       // Maximum record le
  *N CHAR(8);                                                            // Reserved.
  Qdbfgkct INT(5);                                                       // File generic key
  Qdbfos INT(10);                                                        // Offset from Qdb_Q
  *N CHAR(8);                                                            // Reserved.
  Qdbfocs INT(10);                                                       // Offset from Qdb_Q
  *N CHAR(4);                                                            // Reserved.
  Qdbfpact CHAR(2);                                                      // Access path type.
  Qdbfhrls CHAR(6);                                                      // File VRML
  *N CHAR(20);                                                           // Reserved.
  Qdbpfof INT(10);                                                       // Offset from Qdb_Q
  Qdblfof INT(10);                                                       // Offset from Qdb_Q
  Qdbfnlsb CHAR(1);                                                      // Flags.
  Qdbflang CHAR(3);                                                      // Language identifi
  Qdbfcnty CHAR(2);                                                      // Country identifie
  Qdbfjorn INT(10);                                                      // Offset from Qdb_Q
  Qdbfevid INT(10);                                                      // Initial number of
  *N CHAR(14);                                                           // Reserved.
END-DS;
DCL-S Qdb_QdbfhP POINTER;
DCL-S Qdbfhfpl INT(5);                                                   // Type of file.
DCL-S Qdbfhfsu INT(5);                                                   // File type
DCL-S Qdbfhfky INT(5);                                                   // Access path.
DCL-S Qdbfhflc INT(5);                                                   // Record format lev
DCL-S Qdbfkfso INT(5);                                                   // Select/omit.
DCL-S Qdbfigcd INT(5);                                                   // DBCS or Graphic d
DCL-S Qdbfigcl INT(5);                                                   // DBCS or Graphic l
DCL-S Qdbfkfcs INT(5);                                                   // Alternate collati
DCL-S Qdbfkfrc INT(5);                                                   // Force keyed acces
DCL-S Qdbfkflt INT(5);                                                   // Floating point ke
DCL-S Qdbfpgmd INT(5);                                                   // Program described
DCL-S Qdbfjnap INT(5);                                                   // Access path journ
DCL-S Qdbfrdcp INT(5);                                                   // Allow read operat
DCL-S Qdbfwtcp INT(5);                                                   // Allow write opera
DCL-S Qdbfupcp INT(5);                                                   // Allow update oper
DCL-S Qdbfdlcp INT(5);                                                   // Allow delete oper
DCL-S Qdbfkfnd INT(5);                                                   // Null values cause
DCL-S Qdbfhmcs INT(5);                                                   // Multiple CCSID
DCL-S Qdbfknll INT(5);                                                   // Allow null value
DCL-S Qdbf_nfld INT(5);                                                  // Allow null value
DCL-S Qdbfvfld INT(5);                                                   // Variable length d
DCL-S Qdbftfld INT(5);                                                   // Date/time/timesta
DCL-S Qdbfgrph INT(5);                                                   // Graphic data.
DCL-S Qdbfpkey INT(5);                                                   // Primary key
DCL-S Qdbfunqc INT(5);                                                   // Unique constraint
DCL-S Qdbfapsz INT(5);                                                   // Access path size
DCL-S Qdbfdisf INT(5);                                                   // Distributed file.
DCL-S Qdbfsscs INT(5);                                                   // Sort sequence tab

// Physical File Specific Attributes (Qdb_Qdbfphys)

DCL-DS Qdb_Qdbfphys Based(Qdb_QdbfphysP);
  Qdbfpalc CHAR(2);                                                      // Allocate/contiguo
  Qdbfcmps CHAR(1);                                                      // Maximum perc. of
  *N CHAR(8);                                                            // Reserved
  Qdbfprnum INT(10);                                                     // Initial number of
  Qdbfpri INT(5);                                                        // Increment number
  Qdbfprinum INT(5);                                                     // Maximum number of
  Qdbforid INT(10);                                                      // Offset from Qdb_Q
  Qflags CHAR(1);                                                        // Flags
  Qdbfotrg INT(10);                                                      // Offset from Qdb_Q
  Qdbftrgn INT(5);                                                       // Number of trigger
  Qdbfofcs INT(10);                                                      // Offset from Qdb_Q
  Qdbfcstn INT(10);                                                      // Number of constra
  Qdbfodl INT(10);                                                       // Offset from Qdb_Q
  Qdbfovw_mqt INT(10);                                                   // Offset from Qdb_Q
  *N CHAR(2);                                                            // Reserved
END-DS;
DCL-S Qdb_QdbfphysP POINTER;
DCL-S Qdbfrdel INT(5);                                                   // Reuse deleted rec
DCL-S Qdbfsqlt INT(5);                                                   // SQL table
DCL-S Qdbfmqtt INT(5);                                                   // SQL materialized

// Logical File Specific Attributes (Qdb_Qdbflogl)

DCL-DS Qdb_Qdbflogl Based(Qdb_QdbfloglP);
  Qdbfoj INT(10);                                                        // Offset from Qdb_Q
  Qdbfscsn INT(5);                                                       // Total number of s
  Qdbflxp CHAR(10);                                                      // Record format sel
  Qdbflxl CHAR(10);                                                      // Record format sel
  Qdbfovw INT(10);                                                       // Offset from Qdb_Q
  Qlfa CHAR(1);                                                          // Logical file attr
  Qdbfjtyp CHAR(1);                                                      // Join file type
  Qdbfsrcd INT(5);                                                       // CCSID for select/
  Qdbfwchk CHAR(1);                                                      // With check option
  *N CHAR(13);                                                           // Reserved
END-DS;
DCL-S Qdb_QdbfloglP POINTER;
DCL-S Qdbfjoin INT(5);                                                   // Join logical file
DCL-S Qdbfdyns INT(5);                                                   // Dynamic selection
DCL-S Qdbfsqlv INT(5);                                                   // SQL view indicato
DCL-S Qdbfsqli INT(5);                                                   // SQL index indicat

// Constraint Definition Header (Qdb_Qdbf_Cst)

DCL-DS Qdb_Qdbf_Cst Based(Qdb_Qdbf_CstP);
  Qdbf_csto INT(10);                                                     // Offset from Qdb_Q
  Qdbf_hlen INT(10);                                                     // Constraint entry
  Qdbf_type CHAR(1);                                                     // Constraint type
  Qdbf_chkpd CHAR(1);                                                    // Check pending att
  Qdbf_state CHAR(1);                                                    // Constraint state
  Qdbf_abled CHAR(1);                                                    // Constraint enable
  Qdbf_add_ts CHAR(13);                                                  // Constraint date
  Qdbf_cst_lin CHAR(10);                                                 // Constraint librar
  Qdbf_cst_lp2 INT(10);                                                  // Constraint name (
  Revcst_2 CHAR(25);                                                     // Reserved
  Qdbf_cst_name CHAR(258);                                               // Constraint name
END-DS;
DCL-S Qdb_Qdbf_CstP POINTER;

// Constraint Keys (Qdb_Qdbf_Keyn)

DCL-DS Qdb_Qdbf_Keyn Based(Qdb_Qdbf_KeynP);
  Qdbf_kslen INT(10);                                                    // Offset from Qdb_Q
  Qdbf_nokys INT(10);                                                    // Number of keys
  Qdbf_klen INT(10);                                                     // Constraint key le
  Revcst_7 CHAR(52);                                                     // Reserved
END-DS;
DCL-S Qdb_Qdbf_KeynP POINTER;

// Key Name Array (Qdb_Qdbf_Narray)

DCL-DS Qdb_Qdbf_Nary Based(Qdb_Qdbf_NaryP);
  Qdbf_kname CHAR(10);                                                   // Key name
  Revcst_6 CHAR(22);                                                     // Reserved
END-DS;
DCL-S Qdb_Qdbf_NaryP POINTER;

// Referential Constraint Definition (Qdb_Qdbf_Afkd)

DCL-DS Qdb_Qdbf_Afkd Based(Qdb_Qdbf_AfkdP);
  Qdbf_pkfn CHAR(10);                                                    //  Parent file name
  Qdbf_pkln CHAR(10);                                                    //  Parent file libr
  Revcst_3 CHAR(44);                                                     //  Reserved
  Qdbf_fkcdr CHAR(1);                                                    //  Delete rule
  Revcst_4 CHAR(1);                                                      //  Reserved
  Qdbf_fkcur CHAR(1);                                                    //  Update rule
  Revcst_5 CHAR(61);                                                     //  Reserved
END-DS;
DCL-S Qdb_Qdbf_AfkdP POINTER;

// Check Constraint (Qdb_Qdbf_Chk)

DCL-DS Qdb_Qdbf_Chk Based(Qdb_Qdbf_ChkP);
  Qdbf_chkcst_le INT(10);                                                //  Check constraint
  Qdbf_chkexpr_l INT(10);                                                //  Check constraint
  Revcst_8 CHAR(24);                                                     //  Reserved
  Qdbf_chkexpr CHAR(2000);                                               //  Check constraint
END-DS;
DCL-S Qdb_Qdbf_ChkP POINTER;

// File Scope Array (Qdb_Qdbfb)

DCL-DS Qdb_Qdbfb Based(Qdb_QdbfbP);
  *N CHAR(48);                                                           //  Reserved
  Qdbfbf CHAR(10);                                                       //  Based on physica
  Qdbfbfl CHAR(10);                                                      //  Based on physica
  Qdbft CHAR(10);                                                        //  Record format na
  *N CHAR(37);                                                           //  Reserved
  Qdbfbgky INT(5);                                                       //  Record format ge
  *N CHAR(2);                                                            //  Reserved
  Qdbfblky INT(5);                                                       //  Record format ma
  *N CHAR(2);                                                            //  Reserved
  Qdbffogl INT(5);                                                       //  Record format ge
  *N CHAR(3);                                                            //  Reserved
  Qdbfsoon INT(5);                                                       //  Number of select
  Qdbfsoof INT(10);                                                      //  Offset from Qdb_
  Qdbfksof INT(10);                                                      //  Offset from Qdb_
  Qdbfkyct INT(5);                                                       //  Record format fu
  Qdbfgenf INT(5);                                                       //  Generic key fiel
  Qdbfodis INT(10);                                                      //  Offset from Qdb_
  *N CHAR(14);                                                           //  Reserved
END-DS;
DCL-S Qdb_QdbfbP POINTER;

// Journal Information (Qdb_Qdbfjoal)

DCL-DS Qdb_Qdbfjoal Based(Qdb_QdbfjoalP);
  Qdbfojrn CHAR(10);                                                     //  Journal name
  Qdbfolib CHAR(10);                                                     //  Journal library
  Qdbfojpt CHAR(1);                                                      //  Journaling optio
  Qdbfjact CHAR(1);                                                      //  Journaling optio
  Qdbfljrn CHAR(13);                                                     //  Last journaling
  *N CHAR(29);                                                           //  Reserved
END-DS;
DCL-S Qdb_QdbfjoalP POINTER;
DCL-S Qdbfjbim INT(5);                                                   //  Before image ind
DCL-S Qdbfjaim INT(5);                                                   //  After image indi
DCL-S Qdbfjomt INT(5);                                                   //  Omit journal ent

// SQL Area (Qdb_Qdbfv)

DCL-DS Qdb_Qdbfv Based(Qdb_QdbfvP);
  *N CHAR(16);                                                           //  Reserved
  Qdbfvs_start INT(10);                                                  //  Starting offset
  Qdbfvs_end INT(10);                                                    //  Ending offset of
  Qdbfvs_ccsid INT(5);                                                   //  CCSID for the SE
  Qdbfmqtd_o INT(10);                                                    //  Offset Qdb_Qdbfh
  Qdbfmqt_cmt CHAR(2);                                                   //  MQT isolation le
  Qdbfvflgs CHAR(1);                                                     //  MQT flags
  Qdbfvs_sqli_wh_start
  INT(10);                                                               //  Starting offset
  Qdbfvs_sqli_wh_len
  INT(10);                                                               //  Length of the WH
  *N CHAR(15);                                                           //  Reserved
  Qdbfvssl INT(10);                                                      //  statement length
  Qdbfvsst_area CHAR(1);                                                 //  Statement area
END-DS;
DCL-S Qdb_QdbfvP POINTER;

DCL-S Qdbfvsst CHAR(32767) Based(QdbfvsstP);                             //  Statement
DCL-S QdbfvsstP POINTER;

// SQL Materialized Query Table Dependency Header (Qdb_Qdbfmqtd_Head)

DCL-DS Qdb_Qdbfmqtd_Head
       Based(Qdb_Qdbfmqtd_HeadP);
  Qdbfmqtd_len_sum
  INT(10);                                                               //  length of the de
  Qdbfmqtd_#_deps
  INT(10);                                                               //  Number of entrie
  *N CHAR(56);                                                           //  Reserved
  Qdb_Qdbfmqtd_Area
  CHAR(1);                                                               //  SQL MQT Dependen
END-DS;
DCL-S Qdb_Qdbfmqtd_HeadP
      POINTER;

// SQL Materialized Query Table Dependency Entry (Qdb_Qdbfmqtd)

DCL-DS Qdb_Qdbfmqtd
       Based(Qdb_QdbfmqtdP);
  *N CHAR(16);                                                           //  Reserved
  Qdbfmqtd_len INT(10);                                                  //  Length of depend
  Qdbfmqtd_file CHAR(10);                                                //  Dependent file
  Qdbfmqtd_lib CHAR(10);                                                 //  Dependent librar
  *N CHAR(56);                                                           //  Reserved
END-DS;
DCL-S Qdb_QdbfmqtdP
      POINTER;
