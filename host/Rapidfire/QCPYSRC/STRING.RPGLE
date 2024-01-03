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

/if not defined(STRING)
/define STRING

DCL-C WORDWRAP_PARAGRAPH_NONE
      const(1);
DCL-C WORDWRAP_PARAGRAPH_MSGD
      const(2);
DCL-C WORDWRAP_PARAGRAPH_TEXT
      const(4);
DCL-C WORDWRAP_PARAGRAPH_TRIMR
      const(8);

//  Removes the quotes from a quoted string.
DCL-PR unquote
       VARCHAR(4096)
       extproc('STRING_+
       unquote+
       ');
  i_string VARCHAR(4096) const  options(*varsize);
  i_quote CHAR(1) const options(*nopass);
END-PR;

//  Tests whether a given string starts with a specified prefix.
DCL-PR startsWith
       IND
       extproc('STRING_+
       startsWith+
       ');
  i_string VARCHAR(4096) const  options(*varsize);
  i_prefix VARCHAR(4096) const  options(*varsize);
END-PR;

//  Tests whether a given string ends with a specified suffix.
DCL-PR endsWith
       IND
       extproc('STRING_+
       endsWith+
       ');
  i_string VARCHAR(4096) const  options(*varsize);
  i_suffix VARCHAR(4096) const  options(*varsize);
END-PR;

//  Converts a given string to lower case.
DCL-PR lcase
       VARCHAR(4096)
       extproc('STRING_+
       lcase+
       ');
  i_string VARCHAR(4096) const  options(*varsize);
END-PR;

//  Converts a given string to upper case.
DCL-PR ucase
       VARCHAR(4096)
       extproc('STRING_+
       ucase+
       ');
  i_string VARCHAR(4096) const  options(*varsize);
END-PR;

//  Produces a string of spaces with a given length.
DCL-PR space
       VARCHAR(500)
       extproc('STRING_+
       space+
       ');
  i_length INT(10) const;
  i_char CHAR(1) const options(*nopass);
END-PR;

//  Performs word wrapping on a given string.
//  Returns the next line of i_string with each call to f_wordWrap().
DCL-PR wordWrap
       VARCHAR(4096)
       extproc('STRING_+
       wordWrap+
       ');
  i_string VARCHAR(4096) const;
  i_width UNS(10) const;
  io_startPos UNS(10);
  i_options UNS(10) const  options(*nopass);
END-PR;

/endif
