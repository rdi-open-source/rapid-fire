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
//*  String Operations                                                                      *
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
//*-----------------------------------------------------------------------------------------*

// Control

CTL-OPT NoMain;
CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,');
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

// Exports

/include QCPYSRC,STRING                     String Operations

// Includes

/include QCPYSRC,TYPES_H                    Type Templates
/include QCPYSRC,QLGCNVCS                   Convert Case (QLGCNVCS, QlgConvertCase) API
/include QCPYSRC,ERRC0100                   API error code
/include QCPYSRC,BOOL                       Boolean operations

// Constants

DCL-C SINGLE_QUOTE '''';

//*-----------------------------------------------------------------------------------------*
//* Removes the quotes from a quoted string.                                                *
//*-----------------------------------------------------------------------------------------*
//* Parameters:                                                                             *
//*  i_string      - The string the quotes are removed from.                                *
//*  i_quote       - Optional. Character value the string is quoted with.                   *
//*                  Default: single quote                                                  *
//*                                                                                         *
//* Returns:                                                                                *
//*  The string without quotes.                                                             *
//*-----------------------------------------------------------------------------------------*

DCL-PROC unquote EXPORT;

  DCL-PI *N VARCHAR(4096);
    i_string VARCHAR(4096) const  options(*varsize);
    i_quote CHAR(1) const options(*nopass);
  END-PI;

  DCL-S string like(i_string);

  DCL-C p_quote 2;

  DCL-S quote like(i_quote);
  DCL-S lenRemaining INT(10);

  if (%parms() >= p_quote and %addr(i_quote) <> *null);
    quote = i_quote;
  else;
    quote = SINGLE_QUOTE;
  endif;

  if (not startsWith(quote: i_string) or
      not endsWith(quote: i_string));
    return i_string; // not a quoted string
  endif;

  lenRemaining = %len(i_string) - (%len(quote) * 2);

  // empty quoted string
  if (lenRemaining = 0);
    return '';
  endif;

  // remove leading and trailing quotes
  string = %subst(i_string: 1 + %len(quote): lenRemaining);

  return string;

END-PROC;

//*-----------------------------------------------------------------------------------------*
//* Tests whether a given string starts with a specified prefix.                            *
//*-----------------------------------------------------------------------------------------*
//* Parameters:                                                                             *
//*  i_string      - The string, that is tested for a given prefix.                         *
//*  i_prefix      - Prefix.                                                                *
//*                                                                                         *
//* Returns:                                                                                *
//*  TRUE when i_string starts with i_prefix, else FALSE.                                   *
//*-----------------------------------------------------------------------------------------*

  DCL-PROC startsWith EXPORT;

  DCL-PI *N IND;
    i_string VARCHAR(4096) const  options(*varsize);
    i_prefix VARCHAR(4096) const  options(*varsize);
  END-PI;

  if (%len(i_string) = 0  or
      %len(i_prefix) = 0  or
      %len(i_string) < %len(i_prefix));
    return FALSE;
  endif;

  if (%subst(i_string: 1: %len(i_prefix)) <> i_prefix);
    return FALSE;
  endif;

  return TRUE;

END-PROC;

//*-----------------------------------------------------------------------------------------*
//* Tests whether a given string ends with a specified suffix.                              *
//*-----------------------------------------------------------------------------------------*
//* Parameters:                                                                             *
//*  i_string      - The string, that is tested for a given suffix.                         *
//*  i_suffix      - Suffix.                                                                *
//*                                                                                         *
//* Returns:                                                                                *
//*  TRUE when i_string ends with i_suffix, else FALSE.                                     *
//*-----------------------------------------------------------------------------------------*

DCL-PROC endsWith EXPORT;

  DCL-PI *N IND;
    i_string VARCHAR(4096) const  options(*varsize);
    i_suffix VARCHAR(4096) const  options(*varsize);
  END-PI;

  if (%len(i_string) = 0  or
      %len(i_suffix) = 0  or
      %len(i_string) < %len(i_suffix));
    return FALSE;
  endif;

  if %subst(i_string: %len(i_string) - %len(i_suffix) + 1) <> i_suffix;
    return FALSE;
  endif;

  return TRUE;

END-PROC;

//*-----------------------------------------------------------------------------------------*
//* Converts a given string to lower case.                                                  *
//*-----------------------------------------------------------------------------------------*
//* Parameters:                                                                             *
//*  i_string      - The string, that is converted to lower case.                           *
//*                                                                                         *
//* Returns:                                                                                *
//*  Lower case representation of i_string.                                                 *
//*-----------------------------------------------------------------------------------------*

DCL-PROC lcase EXPORT;

  DCL-PI *N VARCHAR(4096);
    i_string VARCHAR(4096) const  options(*varsize);
  END-PI;

  DCL-DS lowerCase qualified;
    string VARCHAR(4096);
    length INT(5) overlay(string);
    data CHAR(4096) overlay(string: *next);
  END-DS;

  DCL-DS reqCtrlBlk likeds(QLGCNVCS_reqCtrlBlk_t) inz;
  DCL-DS errCode likeds(ERRC0100) inz;

  if (%len(i_string) = 0);
     return i_string;
  endif;

  reqCtrlBlk.type = CVTCASE_TYPE_CCSID;
  reqCtrlBlk.CCSID = CVTCASE_CCSID_JOB;
  reqCtrlBlk.case = CVTCASE_TO_LOWER;
  reqCtrlBlk.reserved = *ALLx'00';
  clear errCode;
  QlgConvertCase(
    reqCtrlBlk: i_string: lowerCase.data: %len(i_string): errCode);

  lowerCase.length = %len(i_string);

  return lowerCase.string;

END-PROC;

//*-----------------------------------------------------------------------------------------*
//* Converts a given string to upper case.                                                  *
//*-----------------------------------------------------------------------------------------*
//* Parameters:                                                                             *
//*  i_string      - The string, that is converted to upper case.                           *
//*                                                                                         *
//* Returns:                                                                                *
//*  Upper case representation of i_string.                                                 *
//*-----------------------------------------------------------------------------------------*

DCL-PROC ucase EXPORT;

  DCL-PI *N VARCHAR(4096);
    i_string VARCHAR(4096) const  options(*varsize);
  END-PI;

  DCL-DS upperCase qualified;
    string VARCHAR(4096);
    length INT(5) overlay(string);
    data CHAR(4096) overlay(string: *next);
  END-DS;

  DCL-DS reqCtrlBlk likeds(QLGCNVCS_reqCtrlBlk_t) inz;
  DCL-DS errCode likeds(ERRC0100) inz;

  if (%len(i_string) = 0);
     return i_string;
  endif;

  reqCtrlBlk.type = CVTCASE_TYPE_CCSID;
  reqCtrlBlk.CCSID = CVTCASE_CCSID_JOB;
  reqCtrlBlk.case = CVTCASE_TO_UPPER;
  reqCtrlBlk.reserved = *ALLx'00';
  clear errCode;
  QlgConvertCase(
    reqCtrlBlk: i_string: upperCase.data: %len(i_string): errCode);

  upperCase.length = %len(i_string);

  return upperCase.string;

END-PROC;

//*-----------------------------------------------------------------------------------------*
//*  Produces a string of spaces with a given length                                        *
//*-----------------------------------------------------------------------------------------*

DCL-PROC space EXPORT;

  DCL-PI *N VARCHAR(500);
    i_length INT(10) const;
    i_char CHAR(1) const options(*nopass);
  END-PI;

  DCL-C p_char 2;

  DCL-S tmpString CHAR(500) Inz;
  DCL-DS char Qualified;
    value CHAR(1);
    ebcdic INT(5);
  END-DS;

  DCL-PR memset POINTER extproc('memset');
    i_pDest POINTER value;
    i_char INT(10) value;
    i_count UNS(10) value;
  END-PR;
  // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

  if (i_length <= 0);
    return '';
  endif;

  if (%parms() >= p_char and i_length < %len(tmpString));
    memset(%addr(tmpString): char.ebcdic: i_length);
  endif;

  return %subst(tmpString: 1: i_length);

END-PROC;

//*-----------------------------------------------------------------------------------------*
//* Performs word wrapping on a given string.                                               *
//* Returns the next line of i_string with each call to f_wordWrap().                       *
//*-----------------------------------------------------------------------------------------*
//* Parameters:                                                                             *
//*  i_string      - The string to be word wrapped.                                         *
//*  i_width       - Line length.                                                           *
//*  io_startPos   - The position to start from.                                            *
//*                  (Should not be changed between calls.)                                 *
//*  i_option      - Options to set.                                                        *
//*                  Default:        WORDWRAP_PARAGRAPH_NONE +                              *
//*                                  WORDWRAP_PARAGRAPH_TRIMR                               *
//*                  WORDWRAP_PARAGRAPH_NONE   Do not respect                               *
//*                                            end-of-paragraph markers.                    *
//*                  WORDWRAP_PARAGRAPH_MSGD   Respect end-of-paragraph                     *
//*                                            markers of message descriptions:             *
//*                                            &N, &B and &P.                               *
//*                  WORDWRAP_PARAGRAPH_TEXT   Respect standard text                        *
//*                                            end-of-paragraph markers: \n                 *
//*                  WORDWRAP_PARAGRAPH_TRIMR  Remove trailing blanks from                  *
//*                                            result string.                               *
//*                                                                                         *
//* Returns:                                                                                *
//*  text          - The next line of i_string.                                             *
//*-----------------------------------------------------------------------------------------*

DCL-PROC wordWrap EXPORT;

  DCL-PI *N VARCHAR(4096);
    i_string VARCHAR(4096) const;
    i_width UNS(10) const;
    io_startPos UNS(10);
    i_options UNS(10) const  options(*nopass);
  END-PI;

  //  Return value
  DCL-S text VARCHAR(4096) inz;

  //  Parameter positions
  DCL-C p_options const(4);

  //  Optional parameter fields
  DCL-S options like(i_options) inz;

  //  Local fields
  DCL-DS theEOP qualified;
    isFound IND inz(*OFF);
    position INT(10);
    string VARCHAR(2);
  END-DS;
  DCL-S x INT(10);
  DCL-S tmpEOP VARCHAR(2);
  DCL-S numEOP INT(10);
  DCL-S EOP like(tmpEOP) dim(3);
  DCL-S ESC CHAR(1);
  DCL-S startPos like(io_startPos);
  DCL-S string like(i_string);

  // Process optional parameter values ...
  // ... options
  if (%parms() >= p_options and %addr(i_options) <> *null);
    options = i_options;
  else;
    options = WORDWRAP_PARAGRAPH_NONE +
              WORDWRAP_PARAGRAPH_TRIMR;
  endif;

  // TRIM trailing blanks
  string = %trimR(i_string);

  // Determine how to treat end-of-paragraph markers:
  select;
    // ... do not respect end-of-paragraph marker
  when (isBitOn(WORDWRAP_PARAGRAPH_NONE: options));
    clear EOP;
    clear ESC;
    numEOP = 0;
    // ... respect end-of-paragraph marker of message descriptions: &N, &P, &B
  when (isBitOn(WORDWRAP_PARAGRAPH_MSGD: options));
    clear EOP;
    EOP(1) = '&N';
    EOP(2) = '&P';
    EOP(3) = '&B';
    ESC    = '&';
    numEOP = 3;
    // ... respect end-of-paragraph marker of text: \n
  when (isBitOn(WORDWRAP_PARAGRAPH_TEXT: options));
    clear EOP;
    EOP(1) = '\N';
    ESC    = '\';
    numEOP = 1;
  endsl;

  // ---------------------------------------------------
  //  Perform word-wrap with no respect to
  //  end-of-paragraph markers.
  // ---------------------------------------------------
  if (numEOP = 0);
    text = wordWrapInternal(string: i_width: io_startPos);
  else;
    // ---------------------------------------------------
    //  Perform word-wrap with respect to
    //  end-of-paragraph markers.
    // ---------------------------------------------------

    // Find next end-of-paragraph marker
    reset theEOP;
    theEOP.isFound = *OFF;
    theEOP.position = %scan(ESC: string: io_startPos);
    dow (not theEOP.isFound and theEOP.position > 0);
      // If there is an escape character, ...
      if (%subst(string: theEOP.position: 1) = ESC);
        // ... then check it against the list of EOP marker
        for x = 1 to numEOP;
          // If there is enough space for a EOP after the escape character
          // and if it is a known EOP, ...
          if (theEOP.position <= %len(string) - %len(EOP(x)) + 1);
            tmpEOP = %subst(string: theEOP.position: %len(EOP(x)));
            if (ucase(tmpEOP) = ucase(EOP(x)));
              // ... then set flag "EOP found"
              theEOP.string  = EOP(x);
              theEOP.isFound = *ON;
              leave;
            endif;
          endif;
        endfor;
      endif;

      if (not theEOP.isFound);
        if (theEOP.position < %len(string));
          theEOP.position = %scan(ESC: string: theEOP.position + 1);
        else;
          theEOP.position = 0;
        endif;
      endif;
    enddo;

    // If no end-of-paragraph marker is found, ...
    if (not theEOP.isFound);
      // ... then perform standard word wrap with no respect to EOP
      text = wordWrapInternal(string: i_width: io_startPos);
    else;
      // ... else perform word wrap for text portion
      //     before end-of-paragraph marker
      if (theEOP.position - io_startPos) > 0;
        string   = %subst(string: io_startPos:
                          theEOP.position - io_startPos);
        startPos = 1;
        text = wordWrapInternal(string: i_width: startPos);
      else;
        text = '';
      endif;

      // If all we got is a EOP, ...
      if (%len(text) = %len(theEOP.string)) and
         (ucase(text) = ucase(theEOP.string));
        // ... then delete it
        text = '';
      endif;
      // Set new start position. Skip EOP if EOP
      // caused new line.
      io_startPos = io_startPos + %len(text);
      if (theEOP.position = io_startPos);
        io_startPos = io_startPos + %len(theEOP.string);
      endif;
      // Set start position to 0 if new start position
      // exceeds maximum length of text.
      if (io_startPos > %len(i_string));
        io_startPos = 0;
      endif;
    endif;

  endif;

  // If the user requested to remove BLANKS, ...
  if (isBitOn(WORDWRAP_PARAGRAPH_TRIMR: options));
    // ... then remove BLANKS.
    text = %trimR(text);
  endif;

  return   text;

END-PROC;

//*-----------------------------------------------------------------------------------------*
//* Performs word wrapping on a given string without respect to                             *
//* end-of-paragraph markers.                                                               *
//*-----------------------------------------------------------------------------------------*

DCL-PROC wordWrapInternal;

  DCL-PI *N VARCHAR(4096);
    i_string VARCHAR(4096) const;
    i_width UNS(10) const;
    io_startPos UNS(10);
  END-PI;

  //  Return value
  DCL-S text VARCHAR(4096) inz;

  //  Local fields
  DCL-S sPos INT(10) inz;
  DCL-S ePos INT(10) inz;
  DCL-S ePosConst INT(10) inz;
  DCL-S length INT(10) inz;
  DCL-S char CHAR(1) inz;
  DCL-S x INT(10) inz;

  //  Valid separater characters
  DCL-C DELIMITER const(' -/\.:');

  // Initialise helper fields
  text = '';
  sPos = io_startPos;
  ePos = 0;
  length = 0;

  // Set range
  ePos = sPos + i_width;
  ePosConst = ePos;

  // If the end position is out of range ...
  if (ePos > %len(i_string));
    // ... then
    //     assume the last character to be the delimiter
    ePos = %len(i_string);
  else;
    // ... else
    //     look backwards for a deliniter.
    dow (ePos >= sPos);
      char = %subst(i_string: ePos: 1);
      // At position width + 1 ...
      if (ePos = ePosConst);
        // ... only BLANKS are allowed
        x = %check(' ': char);
      else;
        // ... else every delimiter is allowed
        x = %check(DELIMITER: char);
      endif;
      // If there is a delimiter, ...
      if (x = 0);
        // ... then stop searching
        leave;
      endif;

      ePos = ePos - 1;
    enddo;
  endif;

  // If there is no delimiter, ...
  if (ePos < sPos);
    // ... then assume the last character to be the delimiter.
    ePos = sPos + i_width - 1;
  endif;

  // Retrieve text (incl. delimiter) ...
  length = ePos - sPos + 1;
  text = %subst(i_string: sPos: length);

  // ... and set new start position.
  sPos = sPos + length;
  if (sPos <= %len(i_string));
    io_startPos = %check(' ': i_string: sPos);
  else;
    io_startPos = 0;
  endif;

  return text;

END-PROC;
