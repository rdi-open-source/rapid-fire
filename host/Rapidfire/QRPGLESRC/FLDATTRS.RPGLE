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
//*                                                                                         *
//*  Field Attributes                                                                       *
//*                                                                                         *
//*-----------------------------------------------------------------------------------------*
//*  >>PRE-COMPILER<<                                                                       *
//*    >>CRTCMD<< CRTRPGMOD    MODULE(&LI/&OB) +                                            *
//*                              SRCFILE(&SL/&SF) SRCMBR(&SM);                              *
//*    >>IMPORTANT<<                                                                        *
//*      >>PARM<<  TRUNCNBR(*NO);                                                           *
//*      >>PARM<<  DBGVIEW(*ALL);                                                           *
//*      >>PARM<<  OPTION(*EVENTF);                                                         *
//*      >>PARM<<  TGTRLS(&TR);                                                             *
//*    >>END-IMPORTANT<<                                                                    *
//*    >>EXECUTE<<                                                                          *
//*  >>END-PRE-COMPILER<<                                                                   *
//*------------+--------+-------------------------------------------------------------------*
//*   Datum    I Progr. I Beschreibung                                                      *
//*------------+--------+-------------------------------------------------------------------*
//* 04.02.2016 I   TR   I Serviceprogramm erstellt.                                         *
//*------------+--------+-------------------------------------------------------------------*
//*  Test file used when adding format FLDA0300:                                            *
//*                                                                                         *
//*     CREATE TABLE RFPRI."RUNIT.TEST" (                                                   *
//*       DATE_ DATE,                                                                       *
//*       TIME_ TIME NOT NULL WITH DEFAULT CURRENT_TIME,                                    *
//*       TIMESTAMP_ TIMESTAMP WITH DEFAULT NULL,                                           *
//*       HEX_ CHAR(10) NOT NULL WITH DEFAULT x 'C49695819384',                             *
//*       BOOL_ CHAR(1) NOT NULL WITH DEFAULT '0'                                           *
//*         CHECK(BOOL_ IN('0', '1')),                                                      *
//*       CHAR273_ CHAR(10) CCSID 273 NOT NULL WITH DEFAULT 'Hello',                        *
//*       CHAR37_ VARCHAR(10) CCSID 37 NOT NULL WITH DEFAULT 'Joe'                          *
//*       ) RCDFMT "RUNIT.TEST"                                                             *
//*                                                                                         *
//*  Values of 'Qdb_Qddfdft.Qddfdfta':                                                      *
//*     date        :  no default                                                           *
//*     time_       :  x'04'        expected: x'05'   dft + time                            *
//*     timestamp_  :  x'20'        expected: x'21'   dft + null                            *
//*     hex_        :  x'40'        expected: x'41'   dft + hex literal                     *
//*     bool_       :  x'00'        expected: x'01'   dft                                   *
//*                                                                                         *
//*  Bit 'Qddfdftk' of 'Qdb_Qddfdft.Qddfdfta' is missing for fields                         *
//*  with default values, isn't it?                                                         *
//*                                                                                         *
//*     R FTEST                                                                             *
//*       DATE_           L         ALWNULL                                                 *
//*       TIME_           T                                                                 *
//*       TIMESTAMP_      Z         ALWNULL DFT(*null)                                      *
//*       HEX_          10A         DFT(X'C4969581938440404040')                            *
//*       BOOL_          1A         DFT('0') VALUES('1' '0')                                *
//*                                                                                         *
//*  Values of 'Qdb_Qddfdft.Qddfdfta':                                                      *
//*     date        :  no default                                                           *
//*     time_       :  no default   though initialized to CURRENT_TIME                      *
//*     timestamp_  :  x'20'        expected: x'21'   dft + null                            *
//*     hex_        :  x'40'        expected: x'41'   dft + hex literal                     *
//*     bool_       :  x'00'        expected: x'01'   dft                                   *
//*-----------------------------------------------------------------------------------------*

// Control

CTL-OPT NoMain;
CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,');
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

// Exports

/include QCPYSRC,FLDATTRS                   Field Attributes

// Includes

/include QCPYsRC,TYPES_H                    Type Templates
/include QCPYSRC,BOOL                       Boolean Operations
/include QCPYSRC,STRING                     String Operations
/include QCPYSRC,QDBRTVFD                   Retrieve Database File Description
/include QCPYSRC,CEEGSI                     Get String Information (CEEGSI) API
/include QCPYSRC,ERRC0100                   Format ERRC0100

// Type templates

// Handle
DCL-DS handle_t qualified template;
  pointer POINTER;
  file CHAR(10);
  lib CHAR(10);
  lastFldNum INT(10);
  lastFldPtr POINTER;
END-DS;

// Constants

DCL-C cDOUBLE_QUOTE '"';

//*-----------------------------------------------------------------------------------------*
//* Produces the file field description of a given file and record format.                  *
//*-----------------------------------------------------------------------------------------*
//* Parameters:                                                                             *
//*  i_file        - The name of the file about which the information is to be extracted.   *
//*  i_lib         - Name of the library that contains the file.                            *
//*  i_rcdFormat   - The name of the record format in the specified file that is to be      *
//*                  used to generate the file description.                                 *
//*                  Special value: FLDATTRS_RCDFMT_FIRST                                   *
//*  io_errCode    - API error code.                                                        *
//*                                                                                         *
//* Returns:                                                                                *
//*  Handle of the file field descriptions.                                                 *
//*-----------------------------------------------------------------------------------------*

DCL-PROC FieldAttributes_new EXPORT;

  DCL-PI *N like(fldAttrs_handle_t);
    i_file CHAR(10) const;
    i_lib CHAR(10) const;
    i_rcdFormat CHAR(10) const;
    io_errCode likeds(ERRC0100)
         options(*nopass: *omit);
  END-PI;

  //  Return value
  //* o_isDone        s               n   inz(FALSE)

  //  Positions of optional parameters
  DCL-C p_errCode const(4);

  //  Handle
  DCL-DS handle likeds(handle_t) based(pHandle);

  DCL-S receiverSize INT(10);
  DCL-DS receiver qualified;
    bytRet INT(10);
    bytAvl INT(10);
  END-DS;

  DCL-S bufferSize INT(10);
  DCL-S buffer CHAR(1) based(pBuffer);

  DCL-DS errCode likeds(ERRC0100) inz;
  DCL-DS qFile likeds(qObj_t) inz;
  DCL-DS qRetFile likeds(qObj_t) inz;

  if (%parms() >= p_errCode and %addr(io_errCode) <> *null);
     errCode = io_errCode;
  else;
     clear errCode;
  endif;

  // Retrieve file description ...

  // ... Initialize receiver
  receiverSize = %size(receiver);
  clear receiver;

  // ... determine size of memory actually needed
  qFile.name = i_file;
  qFile.lib = i_lib;
  QDBRTVFD(receiver: receiverSize : qRetFile: 'FILD0200'
           : qFile : i_rcdFormat: '0': '*LCL':'*EXT': errCode);

  // ... retrieve file description
  //     (incrementing buffer size as needed; strange API)
  if (errCode.Exeption_Id = *Blanks);
     bufferSize = receiver.bytAvl;
     pBuffer = %alloc(bufferSize);
     QDBRTVFD(Buffer : bufferSize : qRetFile: 'FILD0200'
              : qRetFile: i_rcdFormat: '0': '*LCL': '*EXT': errCode);
  endif;

  // If the file description is available ...
  if (errCode.Exeption_Id = *Blanks);
     // ... then allocate handle buffer and
     //     return the handle
     pHandle = %alloc(%size(handle));
     handle.Pointer = pBuffer;
     handle.file = qRetFile.name;
     handle.lib = qRetFile.lib;
     handle.lastFldNum = 0;
     handle.lastFldPtr = *null;
  else;
  // ... else set return value to NULL
     pHandle = *null;
  endif;

  if (%parms() >= p_errCode and %addr(io_errCode) <> *null);
     io_errCode = errCode;
  endif;

  return pHandle;

END-PROC;

//*-----------------------------------------------------------------------------------------*
//* Deletes the fiele field description of a given handle.                                  *
//*-----------------------------------------------------------------------------------------*
//* Parameters:                                                                             *
//*  io_handle     - Handle create by FieldAttributes_new().                                *
//*                                                                                         *
//* Returns:                                                                                *
//*  void                                                                                   *
//*-----------------------------------------------------------------------------------------*

DCL-PROC FieldAttributes_delete EXPORT;

  DCL-PI *N;
    io_handle like(fldAttrs_handle_t);
  END-PI;

  //  Handle
  DCL-DS handle likeds(handle_t) based(pHandle);

  // Get access to handle data
  pHandle = io_handle;

  // Free file description buffer
  dealloc(N) handle.Pointer;

  // Initialize handle data
  clear   handle;
  handle.Pointer = *null;
  handle.file = '';
  handle.lib = '';

  // Deallocate handle buffer
  dealloc(N) pHandle;

  io_handle = pHandle;

END-PROC;

//*-----------------------------------------------------------------------------------------*
//* Resets the field pointer to the first field.                                            *
//*-----------------------------------------------------------------------------------------*
//* Parameters:                                                                             *
//*  i_handle      - Handle create by FieldAttributes_new().                                *
//*                                                                                         *
//* Returns:                                                                                *
//*  void                                                                                   *
//*-----------------------------------------------------------------------------------------*

DCL-PROC FieldAttributes_rewind EXPORT;

  DCL-PI *N;
    i_handle const  like(fldAttrs_handle_t);
  END-PI;

  //  Handle
  DCL-DS handle likeds(handle_t) based(pHandle);

  // Get access to handle data
  pHandle = i_handle;

  handle.lastFldNum = 0;
  handle.lastFldPtr = *null;

END-PROC;

//*-----------------------------------------------------------------------------------------*
//* Retrieves the attributes of the field identified by a given field name.                 *
//*-----------------------------------------------------------------------------------------*
//* Parameters:                                                                             *
//*  i_handle      - Handle create by FieldAttributes_new().                                *
//*  i_fldName     - Name of the field.                                                     *
//*                  Special values: FLDATTRS_FLD_FIRST                                     *
//*                                  FLDATTRS_FLD_LAST                                      *
//*                                  FLDATTRS_FLD_NEXT                                      *
//*                                  FLDATTRS_FLD_PREV                                      *
//*  i_o_date      - Returned field attributes.                                             *
//*  i_format      - Optional. Format of the returned field attributes.                     *
//*                  Default: FLDA0100                                                      *
//*                  Values:  FLDA0100                                                      *
//*                           FLDA0200                                                      *
//*                           FLDA0300                                                      *
//*                                                                                         *
//* Returns:                                                                                *
//*  -1 on success, else position of the field in the record format.                        *
//*-----------------------------------------------------------------------------------------*

DCL-PROC FieldAttributes_retrieve EXPORT;

  DCL-PI *N INT(10) opdesc;
    i_handle const  like(fldAttrs_handle_t);
    i_fldName VARCHAR(258) const   options(*varsize);
    o_data CHAR(2048) options(*varsize);
    i_format CHAR(8) const  options(*nopass);
  END-PI;

  //  Return value
  DCL-S fieldPos INT(10) inz(-1);

  //  Positions of (optional) parameters
  DCL-C p_handle const(1);
  DCL-C p_fldName const(2);
  DCL-C p_data const(3);
  DCL-C p_format const(4);

  //  Optional parameter fields
  DCL-S format like(i_format    ) inz;

  //  Handle
  DCL-DS handle likeds(handle_t  )  based(pHandle);

    //  Format Definition Header (Qdb_Qddfmt)
  DCL-DS Qdb_Qddfmt likeds(Qdb_Qddfmt_t)
   based(pQdb_Qddfmt);

    //  Field Header (Qdb_Qddffld)
  DCL-DS Qdb_Qddffld likeds(Qdb_Qddffld_t)
   based(pQdb_Qddffld);

    //  Alias name structure (Qdb_Qddfalis)
  DCL-DS Qdb_Qddfalis likeds(Qdb_Qddfalis_t)
   based(pQdb_Qddfalis);

    //  Reference Information (Qdb_Qddfrefi)
  DCL-DS Qdb_Qddfrefi likeds(Qdb_Qddfrefi_t)
   based(pQdb_Qddfrefi);

    //  Field Text (Qdb_Qddfftxt)
  DCL-DS Qdb_Qddfftxt likeds(Qdb_Qddfftxt_t)
   based(pQdb_Qddfftxt);

    //  Column Heading Information (Qdb_Qddfcolh)
  DCL-DS Qdb_Qddfcolh likeds(Qdb_Qddfcolh_t)
   based(pQdb_Qddfcolh);

    //  Edit Code/Edit Word Information (Qdb_Qddfedcw)
  DCL-DS Qdb_Qddfedcw likeds(Qdb_Qddfedcw_t)
   based(pQdb_Qddfedcw);

    //  Default Value Description Information (Qdb_Qddfdft)
  DCL-DS Qdb_Qddfdft likeds(Qdb_Qddfdft_t)
   based(pQdb_Qddfdft);

  DCL-S isFound IND inz;
  DCL-S isVarying IND inz;
  DCL-S fldNum INT(20) inz;
  DCL-S y INT(20) inz;
  DCL-S blankText like(Qdb_Qddfftxt.Qddfftxt);
  DCL-S longFldName VARCHAR(258) inz;

  DCL-DS FLDA0100 likeds(flda0100_t);
  DCL-DS FLDA0200 likeds(flda0200_t);
  DCL-DS FLDA0300 likeds(flda0300_t);

    //  String information of o_data
  DCL-DS si_data likeds(CEEGSI_inf_t) inz;

  // Get optional parameters
  if (%parms >= p_format and %addr(i_format) <> *null);
     format = i_format;
  else;
     format = 'FLDA0100';
  endif;

  // Get string information of o_data
  CEEGSI(p_data:
         si_data.dataType: si_data.currLen: si_data.maxLen: *omit);

  // Get access to handle data
  pHandle = i_handle;

  // Get access to "Format Definition Header"
  pQdb_Qddfmt = handle.pointer;

  // Initialize helper fields
  isFound = FALSE;

  // Optimization for FLDATTRS_FLD_NEXT
  if (i_fldName = FLDATTRS_FLD_NEXT and handle.lastFldNum > 0);
     y = handle.lastFldNum;
     // get access to "Field Header" of last field returned
     // to get entry length
     pQdb_Qddffld = handle.lastFldPtr;
  else;
     y = 1;
     // get access to "Field Header" of first field
     // to get entry length
     pQdb_Qddffld = %addr(Qdb_Qddfmt) + %size(Qdb_Qddfmt);
  endif;

  // Spin through the file format definition
  // and look for the field requested
  for fldNum = y to Qdb_Qddfmt.Qddffldnum;

     // Get access to alias field name
     longFldName = '';
     if (Qdb_Qddffld.Qddfxals <> 0);
        pQdb_Qddfalis = pQdb_Qddffld + Qdb_Qddffld.Qddfxals;
        if (Qdb_Qddfalis.Qddfalsl > 0);
           longFldName = %trimR(%subst(Qdb_Qddfalis.Qddfalsn
                                       : 1: Qdb_Qddfalis.Qddfalsl));
           longFldName = unquote(longFldName: cDOUBLE_QUOTE);
        endif;
     endif;

     if (longFldName = '');
        longFldName = %trimR(Qdb_Qddffld.Qddffldi);
     endif;

     // Process special value ...
     select;
     // ... FIRST
     when (i_fldName = FLDATTRS_FLD_FIRST) and
            (fldNum = 1);
        isFound = TRUE;
        leave;
     // ... LAST
     when (i_fldName = FLDATTRS_FLD_LAST)  and
            (fldNum = Qdb_Qddfmt.Qddffldnum);
        isFound = TRUE;
        leave;
     // ... NEXT
     when (i_fldName = FLDATTRS_FLD_NEXT)  and
            (fldNum = handle.lastFldNum + 1);
        isFound = TRUE;
        leave;
     // ... PREVIOUS
     when (i_fldName = FLDATTRS_FLD_PREV)  and
            (fldNum = handle.lastFldNum - 1);
        isFound = TRUE;
        leave;
     // ... and a named field
     other;
        if (i_fldName = longFldName or
            i_fldName = %trimR(Qdb_Qddffld.Qddffldi));
           isFound = TRUE;
           leave;
        endif;
     endsl;

     // get access to next field in list
     pQdb_Qddffld = pQdb_Qddffld + Qdb_Qddffld.Qddfdefl;
  endfor;

  // If the field could not be found, ...
  if (not isFound);
  // ... then return BAD
     fieldPos = -1;
     %subst(o_data: 1: si_data.currLen) = '';
  else;
  // ... else return OK and field data
     fieldPos = fldNum;
     %subst(o_data: 1: si_data.currLen) = '';

     // Get access to "Reference Information"
     if (Qdb_Qddffld.Qddfrefd > 0);
        pQdb_Qddfrefi = pQdb_Qddffld + Qdb_Qddffld.Qddfrefd;
     else;
        pQdb_Qddfrefi = *null;
     endif;

     // Get access to "Edit Code/Edit Word Information"
     if (Qdb_Qddffld.Qddfedtl = 0);
        pQdb_Qddfedcw = *null;
     else;
        pQdb_Qddfedcw = pQdb_Qddffld + Qdb_Qddffld.Qddfedtd;
     endif;

     // Get access to "Field Text"
     if (Qdb_Qddffld.Qddftxtd = 0);
        pQdb_Qddfftxt = %addr(blankText);
     else;
        pQdb_Qddfftxt = pQdb_Qddffld + Qdb_Qddffld.Qddftxtd;
     endif;

     // Get access to "Default Value Information"
     if (Qdb_Qddffld.Qddfdftd = 0);
        pQdb_Qddfdft = *null;
     else;
        pQdb_Qddfdft = pQdb_Qddffld + Qdb_Qddffld.Qddfdftd;
     endif;

     // Get access to "Column Heading Information"
     if Qdb_Qddffld.Qddfchd = 0;
        pQdb_Qddfcolh = *null;
     else;
        pQdb_Qddfcolh = pQdb_Qddffld + Qdb_Qddffld.Qddfchd;
     endif;

     // Determine if the field is a VARYING field
     if (isBitOn(x'20': Qdb_Qddffld.Qddffldst2)
         or Qdb_Qddffld.Qddfftyp = X'8004'    // = var character // Hack until PTF
         or Qdb_Qddffld.Qddfftyp = X'8005');  // = var graphic   // is available
        isVarying = TRUE;
     else;
        isVarying = FALSE;
     endif;

     // Set return data values
     clear   FLDA0100;
     FLDA0100.file      = handle.file;
     FLDA0100.lib       = handle.lib;
     FLDA0100.name      = Qdb_Qddffld.Qddffldi;
     FLDA0100.type      = intToExtDataType(Qdb_Qddffld.Qddfftyp);
     FLDA0100.use       = intToExtUsage(Qdb_Qddffld.Qddffiob);
     FLDA0100.outBufPos = Qdb_Qddffld.Qddffobo + 1;
     FLDA0100.inBufPos  = Qdb_Qddffld.Qddffibo + 1;

     if (isVarying);
        FLDA0100.length    = Qdb_Qddffld.Qddffldb - 2;
     else;
        FLDA0100.length    = Qdb_Qddffld.Qddffldb;
     endif;

     FLDA0100.digits    = Qdb_Qddffld.Qddffldd;
     FLDA0100.decPos    = Qdb_Qddffld.Qddffldp;
     FLDA0100.text      = Qdb_Qddfftxt.Qddfftxt;

     // If there is no field edit code, ...
     if (pQdb_Qddfedcw = *null);
     // ... then clear the related fields
        FLDA0100.edtc    = '';
        FLDA0100.edtWLen = 0;
        FLDA0100.edtW    = '';
     else;
     // ... else
     //     If it is an edit word, ...
        if (Qdb_Qddffld.Qddfedtl > 16); // => EditWord
     // ... then return edit word information
           FLDA0100.edtc    = '';
           FLDA0100.edtWLen = Qdb_Qddffld.Qddfedtl - 16;
           FLDA0100.edtW    = %subst(Qdb_Qddfedcw.Qddfewd :
                                     1                    :
                                     FLDA0100.edtWLen     );
        else;
     //    ... else return edit code information
           FLDA0100.edtc    = Qdb_Qddfedcw.Qddfecdi;
           FLDA0100.edtWLen = 0;
           FLDA0100.edtW    = '';
        endif;
     endif;

     if (pQdb_Qddfcolh = *null);
        FLDA0100.colHdg1 = '';
        FLDA0100.colHdg2 = '';
        FLDA0100.colHdg3 = '';
     else;
        FLDA0100.colHdg1 = Qdb_Qddfcolh.Qddfch1;
        FLDA0100.colHdg2 = Qdb_Qddfcolh.Qddfch2;
        FLDA0100.colHdg3 = Qdb_Qddfcolh.Qddfch3;
     endif;

     FLDA0100.datTimFmt = intToExtDatTimFmt(Qdb_Qddffld.Qddfftyp :
                                              Qdb_Qddffld.Qddfdttf );

     FLDA0100.datTimSep = intToExtDatTimsep(Qdb_Qddffld.Qddfftyp :
                                              Qdb_Qddffld.Qddfdtts );

     // Set additional fields of format FLDA0200
     if (format = 'FLDA0200' or
         format = 'FLDA0300');
        clear   FLDA0200;
        FLDA0200 = FLDA0100;

        // If there are reference information
        if (pQdb_Qddfrefi <> *null);
           FLDA0200.refFile = Qdb_Qddfrefi.Qddfrfil;
           FLDA0200.refLib  = Qdb_Qddfrefi.Qddfrlib;
           FLDA0200.refFmt  = Qdb_Qddfrefi.Qddfrfmt;
           FLDA0200.refFld  = Qdb_Qddfrefi.Qddfrfld;
        else;
           FLDA0200.refFile = '';
           FLDA0200.refLib  = '';
           FLDA0200.refFmt  = '';
           FLDA0200.refFld  = '';
        endif;

     endif;

     // Set additional fields of format FLDA0300
     if (format = 'FLDA0300');
        clear   FLDA0300;
        FLDA0300 = FLDA0200;

        if (isBitOn(x'80': qdb_qddffld.Qddffldst2));
           FLDA0300.alwNull = TRUE;
        else;
           FLDA0300.alwNull = FALSE;
        endif;

        FLDA0300.ccsid = qdb_qddffld.Qddfcsid;

        if (isVarying);
           FLDA0300.varLength  = TRUE;
           FLDA0300.buffLength = Qdb_Qddffld.Qddffldb;
           FLDA0300.alcLength  = Qdb_Qddffld.Qddflalc;
        else;
           FLDA0300.varLength  = FALSE;
           FLDA0300.buffLength = Qdb_Qddffld.Qddffldb;
           FLDA0300.alcLength  = Qdb_Qddffld.Qddflalc;
        endif;

        FLDA0300.alias = longFldname;
        FLDA0300.fieldIndex = fieldPos;
        FLDA0300.numFields = Qdb_Qddfmt.Qddffldnum;

        if (pQdb_Qddfdft = *null);
           FLDA0300.lenDftValue   = 0;
           FLDA0300.pDftValue     = *null;
           FLDA0300.isDbcsGraphic = FALSE;
           FLDA0300.isHexLiteral  = FALSE;
           FLDA0300.isNullDft     = FALSE;
           FLDA0300.isCurrTime    = FALSE;
           FLDA0300.hasDtfKeyword = FALSE;
        else;
           FLDA0300.lenDftValue  =
                 Qdb_Qddfdft.Qddfdftl - %size(Qdb_Qddfdft);
           FLDA0300.pDftValue    =
                 %addr(Qdb_Qddfdft) + %size(Qdb_Qddfdft);
           FLDA0300.isDbcsGraphic =
                 isBitOn(x'80':Qdb_Qddfdft.Qddfdfta);
           FLDA0300.isHexLiteral  =
                 isBitOn(x'40':Qdb_Qddfdft.Qddfdfta);
           FLDA0300.isNullDft     =
                 isBitOn(x'20':Qdb_Qddfdft.Qddfdfta);
           FLDA0300.isCurrTime    =
                 isBitOn(x'04':Qdb_Qddfdft.Qddfdfta);
           FLDA0300.hasDtfKeyword =
                 isBitOn(x'01':Qdb_Qddfdft.Qddfdfta);
        endif;
     endif;

     // Return format to caller
     select;
     when (format = 'FLDA0100');
        %subst(o_data: 1: si_data.currLen) = FLDA0100;
     when (format = 'FLDA0200');
        %subst(o_data: 1: si_data.currLen) = FLDA0200;
     when (format = 'FLDA0300');
        %subst(o_data: 1: si_data.currLen) = FLDA0300;
     other;
        %subst(o_data: 1: si_data.currLen) = '';
     endsl;

     // Remember last field information
     handle.lastFldNum = fldNum;
     handle.lastFldPtr = pQdb_Qddffld;
  endif;

  return fieldPos;

END-PROC;

//*-----------------------------------------------------------------------------------------*
//* Returns the number of fields that have been retrieved.                                  *
//*-----------------------------------------------------------------------------------------*
//* Parameters:                                                                             *
//*  i_handle      - Handle create by FieldAttributes_new().                                *
//*                                                                                         *
//* Returns:                                                                                *
//*  NUmber of fields retrieved.                                                            *
//*-----------------------------------------------------------------------------------------*

DCL-PROC FieldAttributes_length EXPORT;

  DCL-PI *N INT(10);
    i_handle const  like(fldAttrs_handle_t);
  END-PI;

  //  Handle
  DCL-DS handle likeds(handle_t) based(pHandle);

    //  Format Definition Header (Qdb_Qddfmt)
  DCL-DS Qdb_Qddfmt likeds(Qdb_Qddfmt_t)
   based(pQdb_Qddfmt );

  // Einblenden Handle
  pHandle = i_handle;

  // Ermöglichen Zugriff auf "Format Definition Header"
  pQdb_Qddfmt = handle.pointer;

  // Ermitteln Anzahl Felder
  return Qdb_Qddfmt.Qddffldnum;

END-PROC;

//*-----------------------------------------------------------------------------------------*
//* Returns the record format                                                               *
//*-----------------------------------------------------------------------------------------*
//* Parameters:                                                                             *
//*  i_handle      - Handle create by FieldAttributes_new().                                *
//*                                                                                         *
//* Returns:                                                                                *
//*  Record format.                                                                         *
//*-----------------------------------------------------------------------------------------*

DCL-PROC FieldAttributes_getRecordFormat EXPORT;

  DCL-PI *N CHAR(10);
    i_handle const  like(fldAttrs_handle_t);
  END-PI;

  //  Handle
  DCL-DS handle likeds(handle_t) based(pHandle);

    //  Format Definition Header (Qdb_Qddfmt)
  DCL-DS Qdb_Qddfmt likeds(Qdb_Qddfmt_t)
   based(pQdb_Qddfmt );

  // Einblenden Handle
  pHandle = i_handle;

  // Ermöglichen Zugriff auf "Format Definition Header"
  pQdb_Qddfmt = handle.pointer;

  // Satzformat zurückgeben
  return Qdb_Qddfmt.Qddfname;

END-PROC;

//*-----------------------------------------------------------------------------------------*
//* Returns the format level identifier used for level check.                               *
//*-----------------------------------------------------------------------------------------*
//* Parameters:                                                                             *
//*  i_handle      - Handle create by FieldAttributes_new().                                *
//*                                                                                         *
//* Returns:                                                                                *
//*  Format level identifier.                                                               *
//*-----------------------------------------------------------------------------------------*

DCL-PROC FieldAttributes_getFormatLevelId EXPORT;

  DCL-PI *N CHAR(13);
    i_handle const  like(fldAttrs_handle_t);
  END-PI;

  //  Handle
  DCL-DS handle likeds(handle_t) based(pHandle);

    //  Format Definition Header (Qdb_Qddfmt)
  DCL-DS Qdb_Qddfmt likeds(Qdb_Qddfmt_t)
   based(pQdb_Qddfmt );

  // Einblenden Handle
  pHandle = i_handle;

  // Ermöglichen Zugriff auf "Format Definition Header"
  pQdb_Qddfmt = handle.pointer;

  // Ermitteln Anzahl Felder
  return Qdb_Qddfmt.Qddfseq;

END-PROC;

//*-----------------------------------------------------------------------------------------*
//* Checks whether a given handle is null.                                                  *
//*-----------------------------------------------------------------------------------------*
//* Parameters:                                                                             *
//*  i_handle      - Handle create by FieldAttributes_new().                                *
//*                                                                                         *
//* Returns:                                                                                *
//*  TRUE for a null handle, else FALSE.                                                    *
//*-----------------------------------------------------------------------------------------*

DCL-PROC FieldAttributes_isNull EXPORT;

  DCL-PI *N IND;
    i_handle const  like(fldAttrs_handle_t);
  END-PI;

  //  Handle
  DCL-DS handle likeds(handle_t) based(pHandle);

  if (i_handle = *null);
     return TRUE;
  else;
     return FALSE;
  endif;

END-PROC;

//*-----------------------------------------------------------------------------------------*
//* Convert internal data type to external representation                                   *
//*-----------------------------------------------------------------------------------------*

DCL-PROC intToExtDataType;

  DCL-PI *N CHAR(1);
    i_intDataType CHAR(2) const;
  END-PI;

  select;
    // Binary
  when (i_inTDataType = X'0000');
    return FLDATTRS_TYPE_BINARY;
    // Float
  when (i_inTDataType = X'0001');
    return FLDATTRS_TYPE_FLOAT;
    // Zoned decimal
  when (i_inTDataType = X'0002');
    return FLDATTRS_TYPE_ZONED;
    // Packed decimal
  when (i_inTDataType = X'0003');
    return FLDATTRS_TYPE_PACKED;
    // Character
  when (i_inTDataType = X'0004');
    return FLDATTRS_TYPE_CHAR;
    // Var Character
  when (i_inTDataType = X'8004');
    return FLDATTRS_TYPE_CHAR;
    // Graphic
  when (i_inTDataType = X'0005');
    return FLDATTRS_TYPE_GRAPHIC;
    // DBCS-capable (not yet supported)
  when (i_inTDataType = X'0006');
    return FLDATTRS_TYPE_ERROR;
    // Large object BLOB/CLOB/DBCLOB/CLOB-OPEN
  when (i_inTDataType = X'4004' or
        i_inTDataType = X'4005' or
        i_inTDataType = X'4006');
    return FLDATTRS_TYPE_LOB;
    // Data link DATALINK-CHAR/DATALINK-OPEN
  when (i_inTDataType = X'8044' or
        i_inTDataType = X'8046');
    return FLDATTRS_TYPE_DATALINK;
    // Var graphic
  when (i_inTDataType = X'8005');
    return FLDATTRS_TYPE_GRAPHIC;
    // Var DBCS-capable (not yet supported)
  when (i_inTDataType = X'8006');
    return FLDATTRS_TYPE_ERROR;
    // Date
  when (i_inTDataType = X'000B');
    return FLDATTRS_TYPE_DATE;
    // Time
  when (i_inTDataType = X'000C');
    return FLDATTRS_TYPE_TIME;
    // Timestamp
  when (i_inTDataType = X'000D');
    return FLDATTRS_TYPE_TMSTMP;
    // NULL (not yet supported)
  when (i_inTDataType = X'FFFF');
    return FLDATTRS_TYPE_ERROR;
    // unknown data type
  other;
    return FLDATTRS_TYPE_ERROR;
  endsl;

END-PROC;

//*-----------------------------------------------------------------------------------------*
//* Convert internal usage to to external representation                                    *
//*-----------------------------------------------------------------------------------------*

DCL-PROC intToExtUsage;

  DCL-PI *N CHAR(1);
    i_intUsage CHAR(1) const;
  END-PI;

  select;
    // Input
  when (i_intUsage = X'01');
    return FLDATTRS_USE_INPUT;
    // Output
  when (i_intUsage = X'02');
    return FLDATTRS_USE_OUTPUT;
    // Both input and output
  when (i_intUsage = X'03');
    return FLDATTRS_USE_BOTH;
    // Neither
  when (i_intUsage = X'04');
    return FLDATTRS_USE_NEiTHER;
    // unbekannte Verwendung
  other;
    return FLDATTRS_USE_ERROR;
  endsl;

END-PROC;

//*-----------------------------------------------------------------------------------------*
//* Convert internal date/time format to external representation                            *
//*-----------------------------------------------------------------------------------------*

DCL-PROC intToExtDatTimFmt;

  DCL-PI *N CHAR(10);
    i_intDataType CHAR(2) const;
    i_intFmt CHAR(1) const;
  END-PI;

  if (i_intDataType <> X'000B' and   // = Date
      i_intDataType <> X'000C' and   // = Time
      i_intDataType <> X'000D');     // = Timestamp
    return '';
  endif;

  select;
    // The format associated with the job
  when (i_intFmt = X'FE');
    return FLDATTRS_DTFMT_JOB;
    // The format associated with the QDT
  when (i_intFmt = X'FF');
    return FLDATTRS_DTFMT_QDT;
    // *USA
  when (i_intFmt = X'01');
    return FLDATTRS_DTFMT_USA;
    // *ISO
  when (i_intFmt = X'03');
    return FLDATTRS_DTFMT_ISO;
    // *EUR
  when (i_intFmt = X'05');
    return FLDATTRS_DTFMT_EUR;
    // *JIS
  when (i_intFmt = X'07');
    return FLDATTRS_DTFMT_JIS;
    // SAA Timestamp
  when (i_intFmt = X'09');
    return FLDATTRS_DTFMT_SAA;
    // *MDY
  when (i_intFmt = X'17');
    return FLDATTRS_DTFMT_MDY;
    // *DMY
  when (i_intFmt = X'18');
    return FLDATTRS_DTFMT_DMY;
    // *YMD
  when (i_intFmt = X'19');
    return FLDATTRS_DTFMT_YMD;
    // *JUL
  when (i_intFmt = X'1A');
    return FLDATTRS_DTFMT_JUL;
    // *HMS
  when (i_intFmt = X'1B');
    return FLDATTRS_DTFMT_HMS;
    // CMDY
  when (i_intFmt = X'25');
    return FLDATTRS_DTFMT_CMDY;
    // CDMY
  when (i_intFmt = X'26');
    return FLDATTRS_DTFMT_CDMY;
    // CYMD
  when (i_intFmt = X'27');
    return FLDATTRS_DTFMT_CYMD;
    // MDYY
  when (i_intFmt = X'28');
    return FLDATTRS_DTFMT_MDYY;
    // *DMYY
  when (i_intFmt = X'29');
    return FLDATTRS_DTFMT_DMYY;
    // *YYMD
  when (i_intFmt = X'2A');
    return FLDATTRS_DTFMT_YYMD;
    // *YM
  when (i_intFmt = X'2B');
    return FLDATTRS_DTFMT_YM;
    // *MY
  when (i_intFmt = X'2C');
    return FLDATTRS_DTFMT_MY;
    // *YYM
  when (i_intFmt = X'2D');
    return FLDATTRS_DTFMT_YYM;
    // *MYY
  when (i_intFmt = X'2E');
    return FLDATTRS_DTFMT_MYY;
    // *LONGJUL
  when (i_intFmt = X'30');
    return FLDATTRS_DTFMT_LONGJUL;
    // unbekanntes Format
  other;
    return FLDATTRS_DTFMT_ERROR;
  endsl;

END-PROC;

//*-----------------------------------------------------------------------------------------*
//* Convert internal date/time separator to external representation                         *
//*-----------------------------------------------------------------------------------------*

DCL-PROC intToExtDatTimsep;

  DCL-PI *N CHAR(1);
    i_intDataType CHAR(2) const;
    i_intSep CHAR(1) const;
  END-PI;

  if (i_intDataType <> X'000B' and   // = Date
      i_intDataType <> X'000C' and   // = Time
      i_intDataType <> X'000D');     // = Timestamp
    return '';
  endif;

  select;
    // The format associated with the job
  when   i_intSep = X'00';
    return FLDATTRS_DTSEP_JOB;
    // The implied separator is used
  when   i_intSep = X'EE';
    return FLDATTRS_DTSEP_IMPL;
    // The slash ('/') ist used
  when   i_intSep = '/';
    return FLDATTRS_DTSEP_SLASH;
    // The dash ('-') ist used
  when   i_intSep = '-';
    return FLDATTRS_DTSEP_DASH;
    // The period ('.') ist used
  when   i_intSep = '.';
    return FLDATTRS_DTSEP_PERIOD;
    // The blank (' ') ist used
  when   i_intSep = ' ';
    return FLDATTRS_DTSEP_BLANK;
    // The colon (':') ist used
  when   i_intSep = ':';
    return FLDATTRS_DTSEP_COLON;
    // unknown date/time separator
  other;
    return FLDATTRS_DTSEP_ERROR;
  endsl;

END-PROC;
