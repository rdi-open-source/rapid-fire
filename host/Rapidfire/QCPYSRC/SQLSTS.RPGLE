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

Dcl-Pr SqlCheckState Ind;
  pi_State CHAR(5) Const;

End-Pr;

Dcl-Pr SqlGetLastErrorMessage VARCHAR(1024);
End-Pr;

Dcl-Pr SqlHandleRowLock Ind;
End-Pr;

Dcl-Pr SqlHandleNoData Ind;
End-Pr;

Dcl-Pr SqlHandleDupRcd Ind;
End-Pr;

Dcl-Pr SqlHandleCstViolation Ind;
End-Pr;

Dcl-Pr SqlProblem VARCHAR(1024);
End-Pr;

// Dcl-Pr SqlGetLogMsg Char(1024);
// End-Pr;

Dcl-Pr SetIgnoreDupRcd;
End-Pr;

Dcl-Pr SetIgnoreNoData;
End-Pr;

Dcl-Pr SetIgnoreRowLock;
End-Pr;

Dcl-Pr SetIgnoreCstViolation;
End-Pr;

Dcl-Pr SetLogStatement;
End-Pr;

Dcl-Pr SetNoLogStatement;
End-Pr;

Dcl-Pr SqLTrace;
  statement VarChar(28672) const;
End-Pr;

Dcl-Pr SqlLastStatement Varchar(32767);
End-Pr;

Dcl-Pr SetIgnoreMoreThanOneRow;
End-Pr;

Dcl-Pr SetIgnoreCharacterConversionError;
End-Pr;

Dcl-Pr SetIgnoreWarnings;
End-Pr;

Dcl-Pr SqlHandleIgnoreWarnings Ind;
End-Pr;
