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

DCL-PR FLDGENCLS_build
       EXTPROC('FLDGENCLS_build');
  *N CHAR(10) Const; // --> Job
  *N INT(10);        // <-- Result
END-PR;

DCL-PR FLDGENCLS_isReplicationOverrideAvailable
       EXTPROC('FLDGENCLS_isReplicationOverrideAvailable');
  *N INT(10); // <-- Result
END-PR;

DCL-PR FLDGENCLS_isReplicationOverrideAuthorized
       EXTPROC('FLDGENCLS_isReplicationOverrideAuthorized');
  *N INT(10); // <-- Result
END-PR;

DCL-PR FLDGENCLS_setReplicationOverride
       EXTPROC('FLDGENCLS_setReplicationOverride');
  *N CHAR(1) Const; // --> Value
  *N INT(10);       // <-- Result
END-PR;

DCL-PR FLDGENCLS_isFieldsWithGeneratedClause
       EXTPROC('FLDGENCLS_isFieldsWithGeneratedClause');
  *N CHAR(10) Const; // --> Job
  *N CHAR(10) Const; // --> Library
  *N CHAR(10) Const; // --> File
  *N CHAR(1) Const;  // --> Check Replication Override
  *N INT(10);        // <-- Result
END-PR;

DCL-PR FLDGENCLS_setFieldsWithGeneratedClause
       EXTPROC('FLDGENCLS_setFieldsWithGeneratedClause');
  *N CHAR(10) Const; // --> Job
  *N CHAR(1) Const;  // --> Replication Override
  *N INT(10);        // <-- Result
END-PR;
