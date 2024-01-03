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

/if not defined(FLDATTRS)
/define FLDATTRS

DCL-S fldAttrs_handle_t
      POINTER based(pDummy);

//  Open Field Attributes Dictionary
DCL-PR FieldAttributes_new
       like(fldAttrs_handle_t)
       extproc('FLDATTRS+
       _FieldAttributes_new+
       ');
  i_fileName CHAR(10) const;
  i_fileLib CHAR(10) const;
  i_rcdFormat CHAR(10) const;
  io_errc0100 likeds(ERRC0100)
       options(*nopass: *omit);
END-PR;

//  Close Field Attributes Dictionary
DCL-PR FieldAttributes_delete
       extproc('FLDATTRS+
       _FieldAttributes_delete+
       ');
  io_handle like(fldAttrs_handle_t);
END-PR;

//  Resets the field pointer to the first field.
DCL-PR FieldAttributes_rewind
       extproc('FLDATTRS+
       _FieldAttributes_rewind+
       ');
  i_handle const  like(fldAttrs_handle_t);
END-PR;

//  Retrieve Field Attributes from Dictionary
DCL-PR FieldAttributes_retrieve
       INT(10) opdesc
       extproc('FLDATTRS+
       _FieldAttributes_retrieve+
       ');
  i_handle const  like(fldAttrs_handle_t);
  i_fldName VARCHAR(258) const   options(*varsize);
  o_data CHAR(2048) options(*varsize);
  i_format CHAR(8) const  options(*nopass);
END-PR;

//  Get Number of Entries available
DCL-PR FieldAttributes_length
       INT(10)
       extproc('FLDATTRS+
       _FieldAttributes_length+
       ');
  i_handle const  like(fldAttrs_handle_t);
END-PR;

//  Returns the record format
DCL-PR FieldAttributes_getRecordFormat
       CHAR(10)
       extproc('FLDATTRS+
       _FieldAttributes_getRecordFormat+
       ');
  i_handle const  like(fldAttrs_handle_t);
END-PR;

//  Returns the format level identifier used for level check.
DCL-PR FieldAttributes_getFormatLevelId
       CHAR(13)
       extproc('FLDATTRS+
       _FieldAttributes_getFormatLevelId+
       ');
  i_handle const  like(fldAttrs_handle_t);
END-PR;

//  Returns cTrue if a given handle is NULL, else cFalse.
DCL-PR FieldAttributes_isNull
       IND
       extproc('FLDATTRS+
       _FieldAttributes_isNull+
       ');
  i_handle const like(fldAttrs_handle_t);
END-PR;

//  Constants

//  Record format ...
DCL-C FLDATTRS_RCDFMT_FIRST
      const('*FIRST');

//  Field types ...
DCL-C FLDATTRS_TYPE_ERROR
      const('?');
DCL-C FLDATTRS_TYPE_BINARY
      const('B');
DCL-C FLDATTRS_TYPE_FLOAT
      const('F');
DCL-C FLDATTRS_TYPE_ZONED
      const('S');
DCL-C FLDATTRS_TYPE_PACKED
      const('P');
DCL-C FLDATTRS_TYPE_CHAR
      const('A');
DCL-C FLDATTRS_TYPE_GRAPHIC
      const('G');
DCL-C FLDATTRS_TYPE_DATE
      const('L');
DCL-C FLDATTRS_TYPE_TIME
      const('T');
DCL-C FLDATTRS_TYPE_TMSTMP
      const('Z');
DCL-C FLDATTRS_TYPE_LOB
      const('1');
DCL-C FLDATTRS_TYPE_DATALINK
      const('2');

//  Field usage ...
DCL-C FLDATTRS_USE_ERROR
      const('?');
DCL-C FLDATTRS_USE_INPUT
      const('I');
DCL-C FLDATTRS_USE_OUTPUT
      const('O');
DCL-C FLDATTRS_USE_BOTH
      const('B');
DCL-C FLDATTRS_USE_NEITHER
      const('N');

//  Time stamp data formats ...
//  ... of physical files
//      (must be the same as defined for the CSV converter)
DCL-C FLDATTRS_DTFMT_SAA
      const('*SAA');

//  Time data formats ...
//  ... of physical files
//      (must be the same as defined for the CSV converter)
DCL-C FLDATTRS_DTFMT_HMS
      const('*HMS');

//  Date and time data formats ...
//  ... of physical files
//      (must be the same as defined for the CSV converter)
DCL-C FLDATTRS_DTFMT_ISO
      const('*ISO');
DCL-C FLDATTRS_DTFMT_USA
      const('*USA');
DCL-C FLDATTRS_DTFMT_EUR
      const('*EUR');
DCL-C FLDATTRS_DTFMT_JIS
      const('*JIS');

//  Data data formats ...
//  ... of physical files
//      (must be the same as defined for the CSV converter)
DCL-C FLDATTRS_DTFMT_ERROR
      const('?');
DCL-C FLDATTRS_DTFMT_JOB
      const('*JOB');
DCL-C FLDATTRS_DTFMT_QDT
      const('*QDT');
DCL-C FLDATTRS_DTFMT_MDY
      const('*MDY');
DCL-C FLDATTRS_DTFMT_DMY
      const('*DMY');
DCL-C FLDATTRS_DTFMT_YMD
      const('*YMD');
DCL-C FLDATTRS_DTFMT_JUL
      const('*JUL');
//  ... of logical files
DCL-C FLDATTRS_DTFMT_MDYY
      const('*MDYY');
DCL-C FLDATTRS_DTFMT_DMYY
      const('*DMYY');
DCL-C FLDATTRS_DTFMT_YYMD
      const('*YYMD');
DCL-C FLDATTRS_DTFMT_LONGJUL
      const('*LONGJUL');
DCL-C FLDATTRS_DTFMT_CMDY
      const('*CMDY');
DCL-C FLDATTRS_DTFMT_CDMY
      const('*CDMY');
DCL-C FLDATTRS_DTFMT_CYMD
      const('*CYMD');
DCL-C FLDATTRS_DTFMT_MY
      const('*MY');
DCL-C FLDATTRS_DTFMT_YM
      const('*YM');
DCL-C FLDATTRS_DTFMT_MYY
      const('*MYY');
DCL-C FLDATTRS_DTFMT_YYM
      const('*YYM');

//  Date separaters ...
DCL-C FLDATTRS_DTSEP_ERROR
      const('?');
DCL-C FLDATTRS_DTSEP_JOB
      const('*JOB');
DCL-C FLDATTRS_DTSEP_IMPL
      const('*IMPLIED');
DCL-C FLDATTRS_DTSEP_SLASH
      const('/');
DCL-C FLDATTRS_DTSEP_DASH
      const('-');
DCL-C FLDATTRS_DTSEP_PERIOD
      const('.');
DCL-C FLDATTRS_DTSEP_BLANK
      const(' ');
DCL-C FLDATTRS_DTSEP_COLON
      const(':');

//  Field name special values ...
DCL-C FLDATTRS_FLD_FIRST
      const('*FIRST');
DCL-C FLDATTRS_FLD_LAST
      const('*LAST');
DCL-C FLDATTRS_FLD_NEXT
      const('*NEXT');
DCL-C FLDATTRS_FLD_PREV
      const('*PREV');

// -------------------------------------------------------------
//  Format FLDA0100
// -------------------------------------------------------------
DCL-DS flda0100_t
       qualified template;
  // File name
  file CHAR(10);
  // Library name
  lib CHAR(10);
  // Field name
  name CHAR(10);
  // Data type
  type CHAR(1);
  // Use
  use CHAR(1);
  // Output buffer position
  outBufPos INT(10);
  // Input buffer position
  inBufPos INT(10);
  // Field length in bytes
  length INT(10);
  // Digits
  digits INT(10);
  // Decimal position
  decPos INT(10);
  // Field text description
  text CHAR(50);
  // Edit code
  edtC CHAR(2);
  // Edit word length
  edtWLen INT(10);
  // Edit word
  edtW CHAR(64);
  // Column heading 1
  colHdg1 CHAR(20);
  // Column heading 2
  colHdg2 CHAR(20);
  // Column heading 3
  colHdg3 CHAR(20);
  // Date and time format
  datTimFmt CHAR(10);
  // Date and time separator
  datTimSep CHAR(1);
END-DS;

// -------------------------------------------------------------
//  Format FLDA0200
// -------------------------------------------------------------
DCL-DS flda0200_t
       qualified template;
  // File name
  file CHAR(10);
  // Library name
  lib CHAR(10);
  // Field name
  name CHAR(10);
  // Data type
  type CHAR(1);
  // Use
  use CHAR(1);
  // Output buffer position
  outBufPos INT(10);
  // Input buffer position
  inBufPos INT(10);
  // Field length in bytes
  length INT(10);
  // Digits
  digits INT(10);
  // Decimal position
  decPos INT(10);
  // Field text description
  text CHAR(50);
  // Edit code
  edtC CHAR(2);
  // Edit word length
  edtWLen INT(10);
  // Edit word
  edtW CHAR(64);
  // Column heading 1
  colHdg1 CHAR(20);
  // Column heading 2
  colHdg2 CHAR(20);
  // Column heading 3
  colHdg3 CHAR(20);
  // Date and time format
  datTimFmt CHAR(10);
  // Date and time separator
  datTimSep CHAR(1);
  //  Additional fields of format FLDA0200
  // Reference file name
  refFile CHAR(10);
  // Reference library name
  refLib CHAR(10);
  // Reference record format name
  refFmt CHAR(10);
  // Reference field name
  refFld CHAR(10);
END-DS;

// -------------------------------------------------------------
//  Format FLDA0300
// -------------------------------------------------------------
DCL-DS flda0300_t
       qualified template;
  // File name
  file CHAR(10);
  // Library name
  lib CHAR(10);
  // Field name
  name CHAR(10);
  // Data type
  type CHAR(1);
  // Use
  use CHAR(1);
  // Output buffer position
  outBufPos INT(10);
  // Input buffer position
  inBufPos INT(10);
  // Field length in bytes
  length INT(10);
  // Digits
  digits INT(10);
  // Decimal position
  decPos INT(10);
  // Field text description
  text CHAR(50);
  // Edit code
  edtC CHAR(2);
  // Edit word length
  edtWLen INT(10);
  // Edit word
  edtW CHAR(64);
  // Column heading 1
  colHdg1 CHAR(20);
  // Column heading 2
  colHdg2 CHAR(20);
  // Column heading 3
  colHdg3 CHAR(20);
  // Date and time format
  datTimFmt CHAR(10);
  // Date and time separator
  datTimSep CHAR(1);
  // Additional fields of format FLDA0200
  // Reference file name
  refFile CHAR(10);
  // Reference library name
  refLib CHAR(10);
  // Reference record format name
  refFmt CHAR(10);
  // Reference field name
  refFld CHAR(10);
  // Additional fields of format FLDA0300
  // Allow null value (ALWNULL)
  alwNull IND;
  // Common coded character set identifier (CCSID)
  ccsid UNS(10);
  // Varying length field
  varLength IND;
  // The length of the field
  buffLength INT(10);
  // Allocated length of varying length fields
  alcLength INT(10);
  alias VARCHAR(258);
  // Position of the field in the record format
  fieldIndex INT(10);
  // Number of fields in the record format
  numFields INT(10);
  // Length of default value
  lenDftValue INT(10);
  // Pointer to the default value
  pDftValue POINTER;
  // DBCS or graphic default. If on, indicates
  // the default is a DBCS or graphic literal
  isDbcsGraphic IND;
  // Hex default. If on, indicates the default
  // is a hexadecimal literal.
  isHexLiteral IND;
  // Null default. If on, indicates the default
  // is null.
  isNullDft IND;
  // DATE, TIME, or TIMESTAMP default. On indicates
  // the default is CURRENT_DATE, CURRENT_TIME, or
  // CURRENT_TIMESTAMP.
  isCurrTime IND;
  // DFT or DFTVAL keyword. If on, indicates the
  // DFTVAL keyword was specified.
  hasDtfKeyword IND;
END-DS;

/endif

