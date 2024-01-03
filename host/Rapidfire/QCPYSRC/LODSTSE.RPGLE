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

DCL-PR LODSTSE_loadStatusEntries
       EXTPROC('LODSTSE_loadStatusEntries');
  *N VARCHAR(1024) CONST; // --> Where Clause
  *N CHAR(10);            // <-- Job
  *N PACKED(6 : 0);       // <-- Position
  *N CHAR(10);            // <-- Area
  *N CHAR(10);            // <-- File
  *N CHAR(10);            // <-- Library
  *N PACKED(10 : 0);      // <-- Records in production library
  *N PACKED(10 : 0);      // <-- Records in shadow library
  *N PACKED(10 : 0);      // <-- Records to copy
  *N PACKED(10 : 0);      // <-- Records copied
  *N CHAR(11);            // <-- Estimated time
  *N PACKED(10 : 0);      // <-- Records with duplicate key
  *N PACKED(10 : 0);      // <-- Changes to apply
  *N PACKED(10 : 0);      // <-- Changes applied
  *N PACKED(3 : 0);       // <-- Percent done
  *N INT(5) CONST;        // --> (Null Ind.) Where Clause
  *N INT(5);              // <-- (Null Ind.) Job
  *N INT(5);              // <-- (Null Ind.) Position
  *N INT(5);              // <-- (Null Ind.) Area
  *N INT(5);              // <-- (Null Ind.) File
  *N INT(5);              // <-- (Null Ind.) Library
  *N INT(5);              // <-- (Null Ind.) Records in production library
  *N INT(5);              // <-- (Null Ind.) Records in shadow library
  *N INT(5);              // <-- (Null Ind.) Records to copy
  *N INT(5);              // <-- (Null Ind.) Records copied
  *N INT(5);              // <-- (Null Ind.) Estimated time
  *N INT(5);              // <-- (Null Ind.) Records with duplicate key
  *N INT(5);              // <-- (Null Ind.) Changes to apply
  *N INT(5);              // <-- (Null Ind.) Changes applied
  *N INT(5);              // <-- (Null Ind.) Percent done
  *N CHAR(5);             // <-- SQL State
  *N VARCHAR(517) CONST;  // SQL Function Name
  *N VARCHAR(128) CONST;  // --> SQL Specific Function name
  *N VARCHAR(70);         // <-- Error Message
  *N INT(10) CONST;       // --> Call Type
END-PR;
