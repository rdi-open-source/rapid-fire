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
//*  Check Object                                                                           *
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
//* 24.03.2016 I   TR   I Serviceprogramm erstellt.                                         *
//*------------+--------+-------------------------------------------------------------------*

// Control

CTL-OPT NoMain;
CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,');
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

// Type Template

/include QCPYSRC,TYPES_H                    Type Template
/include QCPYSRC,ERRC0100                   API error code
/include QCPYSRC,MBRD0100                   Member Description Format MBRD0100

// Imports

/include QCPYSRC,QUSROBJD                   Retrieve Object Description (QUSROBJD) API
/include QCPYSRC,QUSRMBRD                   Retrieve Member Description (QUSRMBRD) API
/include QCPYSRC,QDBRTVFD                   Retrieve Database File Description

// Exports

/include QCPYSRC,CHKOBJ                     Check Object

//*-----------------------------------------------------------------------------------------*
//* Tests whether or not a given object exists.                                             *
//*-----------------------------------------------------------------------------------------*
//* Parameters:                                                                             *
//*  i_object      - Object that is checked for existance.                                  *
//*  i_library     - Omittable. Library that contains the object.                           *
//*                  Default: *LIBL                                                         *
//*  i_type        - Type of the object.                                                    *
//*  i_member      - Optional. Member, if the object is a file.                             *
//*                  Default: *FIRST                                                        *
//*                                                                                         *
//* Returns:                                                                                *
//*  TRUE when the object exists, else FALSE.                                               *
//*-----------------------------------------------------------------------------------------*

DCL-PROC Object_exist EXPORT;

  DCL-PI *N IND;
    i_object CHAR(10) const;
    i_library CHAR(10) const options(*omit);
    i_type CHAR(10) const;
    i_member CHAR(10) const options(*nopass: *omit);
  END-PI;

  DCL-S member like(i_member);
  DCL-S library like(i_library);

  DCL-DS qObj likeds(qObj_t) inz;
  DCL-DS errCode likeds(ERRC0100) inz;
  DCL-DS mbrd0100 likeds(objd0100_t) inz;
  DCL-DS objd0100 likeds(objd0100_t) inz;

  if (%parms() >= %parmnum(i_library) and %addr(i_library) <> *null);
     library = i_library;
  else;
     library = '*LIBL';
  endif;

  if (i_type = '*FILE');
     if (%parms() >= %parmnum(i_member) and %addr(i_member) <> *null);
        member = i_member;
     else;
        member = '*FIRST';
     endif;
  else;
     member = '*NONE';
  endif;

  qObj.name = i_object;
  qObj.lib = library;

  clear errCode;
  errCode.Bytes_Provided = %size(errCode);

  if (i_type = '*FILE' and member <> '*NONE');
     QUSRMBRD(mbrd0100: %size(mbrd0100): 'MBRD0100'
              : qObj: member: '0': errCode);
  else;
     QUSROBJD(objd0100: %size(objd0100): 'OBJD0100'
              : qObj: i_type: errCode);
  endif;

  if (errCode.Exeption_Id <> '');
     return FALSE;
  endif;

  return TRUE;

END-PROC;

//*-----------------------------------------------------------------------------------------*
//* Convenience method to test whether or not a given library exists.                       *
//*-----------------------------------------------------------------------------------------*
//* Parameters:                                                                             *
//*  i_library     - Name of the library that is checked for existance.                     *
//*                                                                                         *
//* Returns:                                                                                *
//*  TRUE when the library exists, else FALSE.                                              *
//*-----------------------------------------------------------------------------------------*

DCL-PROC Library_exist EXPORT;

  DCL-PI *N IND;
    i_library CHAR(10) const;
  END-PI;

  return Object_exist(i_library: *omit: '*LIB');

END-PROC;

//*-----------------------------------------------------------------------------------------*
//* Convenience method to test whether or not a given file exists.                          *
//*-----------------------------------------------------------------------------------------*
//* Parameters:                                                                             *
//*  i_file        - Name of the file that is checked for existance.                        *
//*  i_library     - Optional. Library that contains the object.                            *
//*                  Default: *LIBL                                                         *
//*                                                                                         *
//* Returns:                                                                                *
//*  TRUE when the file exists, else FALSE.                                                 *
//*-----------------------------------------------------------------------------------------*

DCL-PROC File_exist EXPORT;

  DCL-PI *N IND;
    i_file CHAR(10) const;
    i_library CHAR(10) const options(*nopass: *omit);
  END-PI;

  DCL-S library like(i_library);

  if (%parms() >= %parmnum(i_library) and %addr(i_library) <> *null);
    library = i_library;
  else;
    library = '*LIBL';
  endif;

  return Object_exist(i_file: library: '*FILE');

END-PROC;

//*-----------------------------------------------------------------------------------------*
//* Convenience method to test whether or not a given member exists.                        *
//*-----------------------------------------------------------------------------------------*
//* Parameters:                                                                             *
//*  i_file        - Name of the file that contains the member.                             *
//*  i_library     - Omittable. Library that contains the file.                             *
//*                  Default: *LIBL                                                         *
//*  i_member      - Name of the member that is checked for existance.                      *
//*                                                                                         *
//* Returns:                                                                                *
//*  TRUE when the member exists, else FALSE.                                               *
//*-----------------------------------------------------------------------------------------*

DCL-PROC Member_exist EXPORT;

  DCL-PI *N IND;
    i_file CHAR(10) const;
    i_library CHAR(10) const options(*omit);
    i_member CHAR(10) const;
  END-PI;

  DCL-S library like(i_library);

  if (%parms() >= %parmnum(i_library) and %addr(i_library) <> *null);
    library = i_library;
  else;
    library = '*LIBL';
  endif;

  return Object_exist(i_file: library: '*FILE': i_member);

END-PROC;

//*-----------------------------------------------------------------------------------------*
//* Tests whether or not a given file is a source physical file.                            *
//*-----------------------------------------------------------------------------------------*
//* Parameters:                                                                             *
//*  i_file        - Name of the file that contains the member.                             *
//*  i_library     - Optional. Library that contains the file.                              *
//*                  Default: *LIBL                                                         *
//*                                                                                         *
//* Returns:                                                                                *
//*  TRUE when the file is a source physical file, else FALSE.                              *
//*-----------------------------------------------------------------------------------------*

DCL-PROC File_isSourceFile EXPORT;

  DCL-PI *N IND;
    i_file CHAR(10) const;
    i_library CHAR(10) const options(*nopass: *omit);
  END-PI;

  DCL-S library like(i_library);

  DCL-DS qFile likeds(qObj_t) inz;
  DCL-DS qRetFile likeds(qObj_t) inz;
  DCL-DS errCode likeds(ERRC0100) inz;
  DCL-DS Qdb_Qdbfh likeds(Qdb_Qdbfh_t) inz;

    // File type (FILETYPE). If on, the file is a
    // source file (*SRC). If off, a data file (*DATA).
  DCL-C QDBFHFSU x'0800';

  if (%parms() >= %parmnum(i_library) and %addr(i_library) <> *null);
    library = i_library;
  else;
    library = '*LIBL';
  endif;

  qFile.name = i_file;
  qFile.lib = library;

  QDBRTVFD(Qdb_Qdbfh: %size(Qdb_Qdbfh): qRetFile: 'FILD0100'
           : qFile: '*FIRST': '0': '*LCL': '*EXT': errCode);

  if (errCode.Exeption_Id <> '');
    return FALSE;
  endif;

  if (%bitand(Qdb_Qdbfh.Qdbfhflg: QDBFHFSU) = QDBFHFSU);
    return TRUE;
  endif;

  return FALSE;

END-PROC;
