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

/if not defined(CHKOBJ)
/define CHKOBJ

//  Tests whether or not a given object exists.
DCL-PR Object_exist
       IND
       extproc('CHKOBJ_+
       Object_exist+
       ');
  i_object CHAR(10) const;
  i_library CHAR(10) const options(*omit);
  i_type CHAR(10) const;
  i_member CHAR(10) const options(*nopass: *omit);
END-PR;

//  Convenience method to test whether or not a given library exists.
DCL-PR Library_exist
       IND
       extproc('CHKOBJ_+
       Library_exist+
       ');
  i_library CHAR(10) const;
END-PR;

//  Convenience method to test whether or not a given file exists.
DCL-PR File_exist
       IND
       extproc('CHKOBJ_+
       File_exist+
       ');
  i_file CHAR(10) const;
  i_library CHAR(10) const options(*nopass: *omit);
END-PR;

//  Convenience method to test whether or not a given member exists.
DCL-PR Member_exist
       IND
       extproc('CHKOBJ_+
       Member_exist+
       ');
  i_file CHAR(10) const;
  i_library CHAR(10) const options(*omit);
  i_member CHAR(10) const;
END-PR;

//  Tests whether or not a given file is a source physical file.
DCL-PR File_isSourceFile
       IND
       extproc('CHKOBJ_+
       File_isSourceFile+
       ');
  i_file CHAR(10) const;
  i_library CHAR(10) const options(*nopass: *omit);
END-PR;

/endif

