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
//* SQL Status                                                                              *
//*                                                                                         *
//*-----------------------------------------------------------------------------------------*
//*  >>PRE-COMPILER<<                                                                       *
//*    >>CRTCMD<< CRTSQLRPGI   OBJ(&LI/&OB) +                                               *
//*                              SRCFILE(&SL/&SF) SRCMBR(&SM) +                             *
//*                              OBJTYPE(*MODULE);                                          *
//*    >>IMPORTANT<<                                                                        *
//*      >>PARM<<  OUTPUT(*PRINT);                                                          *
//*      >>PARM<<  GENLVL(0);                                                               *
//*      >>PARM<<  DBGVIEW(*SOURCE);                                                        *
//*      >>PARM<<  OPTION(*EVENTF);                                                         *
//*      >>PARM<<  TGTRLS(&TR);                                                             *
//*    >>END-IMPORTANT<<                                                                    *
//*    >>EXECUTE<<                                                                          *
//*  >>END-PRE-COMPILER<<                                                                   *
//*------------+--------+-------------------------------------------------------------------*
//*   Datum    I Progr. I Beschreibung                                                      *
//*------------+--------+-------------------------------------------------------------------*
//* 18.03.2016 I   FH   I Creation                                                          *
//*------------+--------+-------------------------------------------------------------------*

// Control

CTL-OPT NoMain;
CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,') AlwNull(*USRCTL);
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT BndDir('RAPIDFIRE');
CTL-OPT ExtBinInt(*YES);

   //********************************************************************
   //* Prototype(s) / Copys
   //********************************************************************

/include QCPYSRC,SqlSts
/include QCPYSRC,WHOAMI

   //********************************************************************
   //* Prozedurbeschreibungen
   //********************************************************************
   // Prototype/Procedure        Beschreibung
   // -----------------------    ----------------------------------------
   // PR - SqlCheckState         Status prüfen
   // PR - SqlHandleRowLock      RowLock bearbeiten
   // PR - SqlHandleNoData       Keine Daten gefunden
   // PR - SqlHandleDupRcd       Doppelter Satzschlüssel
   // PR - SqlProblem            Unbehandelte Ausnahme
   // PR - SqlHandleCstViolation Constraint violation
   // PR - SqlTrace              Letztes Statement loggen
   // PR - SqlLastStatement      Letztes Statement mit API holen
   // PR - SqlHandleIgnoreWarnings

   //********************************************************************
   // D a t e n s t r u k t u r
   //********************************************************************
   //-----------------------------------------------------
   // GET DIAGNOSTICS
   //-----------------------------------------------------

Dcl-S Cmd_Function Varchar(128);
Dcl-S Dyn_Function Varchar(128);
Dcl-S Err_Code1 Int(10);
Dcl-S Err_Code2 Int(10);
Dcl-S Err_Code3 Int(10);
Dcl-S Err_Code4 Int(10);
Dcl-S Line# Int(10);
Dcl-S Msg_Id Char(10);
Dcl-S Msg_Id1 Varchar(7);
Dcl-S Msg_Id2 Varchar(7);
Dcl-S Msg_Key Int(10);
Dcl-S Msg_Text Varchar(32740);
Dcl-S Errd1 Int(10);
Dcl-S Errd2 Int(10);
Dcl-S Errd3 Int(10);
Dcl-S Errd4 Int(10);
Dcl-S Errd5 Int(10);
Dcl-S Errd6 Int(10);
Dcl-S Ret_SqlCode Int(10);
Dcl-S Ret_State Char(5);
Dcl-S Auth_Id Varchar(128);
Dcl-S LastErrorMessage Varchar(1024);
   //********************************************************************
   // Interne Prototypes
   //********************************************************************
Dcl-Pr UnsetAll;
End-Pr;

   //********************************************************************
   // Globale Variablen
   //********************************************************************
   // Setzen von Konditionen
   //---------------------------------------------
Dcl-S ignoreDupRcd Ind inz;
Dcl-S ignoreNoData Ind inz;
Dcl-S ignoreCstViolation
 Ind inz;
Dcl-S ignoreRowLock Ind inz;
Dcl-S logStatement Ind inz;
Dcl-S noLogStatement
 Ind inz;
Dcl-S ignoreMoreThanOneRow
 Ind inz;
Dcl-S ignoreCharacterConversionError
 Ind inz;
Dcl-S ignoreWarnings
 Ind inz;
   //---------------------------------------------
   // Global verwendete Variablen
   //---------------------------------------------
Dcl-S stmt Varchar(30000);
   //********************************************************************
   // P R O Z E D U R E N
   //********************************************************************

// Set SQL options

Exec SQL
  Set Option
  DatFmt=*ISO,
  TimFmt=*ISO,
  Commit=*NONE,
  CloSQLCsr=*ENDMOD;

   //********************************************************************
   // SqlCheckState        |
   //********************************************************************
Dcl-Proc SqlCheckState Export;
Dcl-Pi SqlCheckState Ind;
  pi_State CHAR(5) Const;
End-Pi;

LastErrorMessage = '';

   //----------------------------------------------------
   // Letztes Statement loggen
   //----------------------------------------------------
stmt = '';
stmt = %trim(SqlLastStatement());

   // pi_State prüfen:
Select;
     //Klassencode 00: Nicht qualifizierter erfolgreicher Abschluss
When pi_State = '00000';
     //Abschluss der Operation war erfolgreich und hatte keine
     //Warnung oder Ausnahmebedingung zur Folge.

  unsetAll();
  return *on;


     //Klassencode 01: Warnung
When pi_State = '01002';
     //Ein DISCONNECT-Fehler ist aufgetreten.
  return SqlHandleIgnoreWarnings();

When pi_State = '01003';
     //Nullwerte wurden aus dem Argument einer Spaltenfunktion
     //gelöscht.

  return SqlHandleIgnoreWarnings();

When pi_State = '01004';
     //Der Wert einer Zeichenfolge wurde beim Zuordnen zu einem
     //anderen Zeichenfolgedatentyp mit kürzerer Länge
     //abgeschnitten.

  unsetAll();
  return *on;

When pi_State = '01005';
     //Nicht genügend Einträge in einem SQL-Deskriptorbereich
     //(SQLDA).

  return SqlHandleIgnoreWarnings();

When pi_State = '01006';
     //Eine Berechtigung wurde nicht entzogen.

  return SqlHandleIgnoreWarnings();

When pi_State = '01007';
     //Eine Berechtigung wurde nicht erteilt.

  return SqlHandleIgnoreWarnings();

When pi_State = '01009';
     //Die Suchbedingung ist für das Informationsschema zu lang.

  return SqlHandleIgnoreWarnings();

When pi_State = '0100A';
     //Der Abfrageausdruck der Sicht ist für das
     //Informationsschema zu lang.

  return SqlHandleIgnoreWarnings();

When pi_State = '0100C';
     //Mindestens eine Ad-hoc-Ergebnismenge wurde von der Prozedur
     //zurückgegeben.

  return SqlHandleIgnoreWarnings();

When pi_State = '0100D';
     //Der Cursor, der geschlossen wurde, wurde für die nächste
     //Ergebnismenge in der Kette erneut geöffnet.

  return SqlHandleIgnoreWarnings();

When pi_State = '0100E';
     //Die Prozedur hat zu viele Ergebnismengen zurückgegeben.

  return SqlHandleIgnoreWarnings();

When pi_State = '01503';
     //Die Anzahl Ergebnisspalten überschreitet die Anzahl
     //bereitgestellter Hostvariablen.

  return SqlHandleIgnoreWarnings();

When pi_State = '01504';
     //Die Anweisung UPDATE oder DELETE enthält keine Klausel
     //WHERE.

  return SqlHandleIgnoreWarnings();

When pi_State = '01505';
     //Die Anweisung wurde nicht ausgeführt, da sie in dieser
     //Umgebung nicht akzeptiert wird.

  return SqlHandleIgnoreWarnings();

When pi_State = '01506';
     //Ein DATE- oder TIMESTAMP-Wert wurde angepasst, um ein
     //ungültiges Datum zu korrigieren, das von einer
     //Rechenoperation zurückgegeben wurde.

  return SqlHandleIgnoreWarnings();

When pi_State = '01513';
     // Ein Zeichen das nicht konvertiert werden konnte
     // wurde durch einen Platzhalter ersetzt

  return SqlHandleIgnoreWarnings();

When pi_State = '01515';
     //Der Nullwert wurde einer Hostvariablen zugeordnet, da der
     //Wert ungleich Null der Spalte nicht innerhalb des
     //zulässigen Bereichs der Hostvariablen liegt.

  return SqlHandleIgnoreWarnings();

When pi_State = '01517';
     //Ein Zeichen, das nicht konvertiert werden kann, wurde durch
     //ein Substitutionszeichen ersetzt.

  if ignoreCharacterConversionError;
    return *on;
  endIf;

  return SqlHandleIgnoreWarnings();

When pi_State = '01519';
     //Der Nullwert wurde einer Hostvariablen zugeordnet, da ein
     //numerischer Wert außerhalb des gültigen Bereichs
     //liegt.

  return SqlHandleIgnoreWarnings();

When pi_State = '01520';
     //Der Nullwert wurde einer Hostvariablen zugeordnet, da die
     //Zeichen nicht konvertiert werden können.

  return SqlHandleIgnoreWarnings();

When pi_State = '01522';
     //Der Name der lokalen Tabelle oder lokalen Sicht, der in der
     //Anweisung CREATE ALIAS verwendet wird, ist nicht
     //definiert.

  return SqlHandleIgnoreWarnings();

When pi_State = '01526';
     //Isolationsstufe wurde eskaliert.

  return SqlHandleIgnoreWarnings();

When pi_State = '01527';
     //Eine Anweisung SET verweist auf ein Sonderregister, das bei
     //AS nicht vorhanden ist.

  return SqlHandleIgnoreWarnings();

When pi_State = '01528';
     //WHERE NOT NULL wird ignoriert, da der Indexschlüssel keine
     //Nullwerte enthalten kann.

  return SqlHandleIgnoreWarnings();

When pi_State = '01532';
     //Ein nicht definierter Objektname wurde gefunden.

  return SqlHandleIgnoreWarnings();

When pi_State = '01534';
     //Die Zeichenfolgedarstellung eines Werts für Datum und
     //Uhrzeit ist ungültig.

  return SqlHandleIgnoreWarnings();

When pi_State = '01535';
     //Eine Rechenoperation für ein Datum oder eine Zeitmarke
     //hatte ein Ergebnis zur Folge, das nicht innerhalb des
     //gültigen Bereichs für Datumsangaben liegt.

  return SqlHandleIgnoreWarnings();

When pi_State = '01536';
     //Beim Binden über Remotezugriff, bei dem die Überprüfung auf
     //Vorhandensein verzögert wird, stimmt der angegebene
     //Servername nicht mit dem aktuellen Server überein.

  return SqlHandleIgnoreWarnings();

When pi_State = '01539';
     //Der Verbindungsaufbau ist erfolgreich, es sollten aber nur
     //SBCS-Zeichen verwendet werden.

  return SqlHandleIgnoreWarnings();

When pi_State = '01542';
     //Die Berechtigungs-ID hat nicht die Berechtigung, die
     //Operation wie angegeben auszuführen.

  return SqlHandleIgnoreWarnings();

When pi_State = '01544';
     //Der Nullwert wurde einer Hostvariablen zugeordnet, da ein
     //Unterzeichenfolgefehler aufgetreten ist;
     //beispielsweise liegt ein Argument von SUBSTR
     //außerhalb des gültigen Bereichs.

  return SqlHandleIgnoreWarnings();

When pi_State = '01545';
     //Ein nicht qualifizierter Spaltenname wurde als
     //Korrelationsbezug interpretiert.

  return SqlHandleIgnoreWarnings();

When pi_State = '01547';
     //Ein gemischter Datenwert wurde nicht korrekt gebildet.

  return SqlHandleIgnoreWarnings();

When pi_State = '01548';
     //Die Berechtigungs-ID hat nicht die Berechtigung, die
     //Operation für das angegebene Objekt auszuführen.

  return SqlHandleIgnoreWarnings();

When pi_State = '01557';
     //In der Anweisung SELECT INTO oder FETCH wurden zu viele
     //Hostvariablen angegeben.

  return SqlHandleIgnoreWarnings();

When pi_State = '01564';
     //Der Nullwert wurde einer Hostvariablen zugeordnet, da eine
     //Division durch Null erfolgte.

  return SqlHandleIgnoreWarnings();

When pi_State = '01565';
     //Der Nullwert wurde einer Hostvariablen zugeordnet, da ein
     //Datenfehler aufgetreten ist, der verschiedene
     //Ursachen haben kann; beispielsweise ist der
     //Zeichenwert für die Skalarfunktion CAST, DECIMAL,
     //FLOAT oder INTEGER ungültig, oder es wurde eine
     //Gleitkomma-Nichtzahl (keine Zahl) oder es wurden
     //ungültige Daten in einem Feld mit gepacktem
     //Dezimalformat gefunden.

  return SqlHandleIgnoreWarnings();

When pi_State = '01567';
     //Die Tabelle wurde erstellt, aber nicht aufgezeichnet.

  return SqlHandleIgnoreWarnings();

When pi_State = '01587';
     //Die Arbeitseinheit wurde festgeschrieben oder rückgängig
     //gemacht, das Ergebnis ist aber nicht an allen
     //Standorten vollständig bekannt.

  return SqlHandleIgnoreWarnings();

When pi_State = '01593';
     //Eine Anweisung ALTER TABLE kann zum Abschneiden von Daten
     //führen.

  return SqlHandleIgnoreWarnings();

When pi_State = '01594';
     //Nicht genügend Einträge in einem SQL-Deskriptorbereich
     //(SQLDA) für ALLE Informationen (d. h. es sind nicht
     //genügend Deskriptoren vorhanden, um den einzigartigen
     //Namen zurückzugeben).

  return SqlHandleIgnoreWarnings();

When pi_State = '01623';
     //Der Wert von DEGREE wird ignoriert.

  return SqlHandleIgnoreWarnings();

When pi_State = '01627';
     //Der DATALINK-Wert ist möglicherweise ungültig, da sich die
     //Tabelle im Status "Reconcile Pending" oder "Reconcile
     //Not Possible" befindet.

  return SqlHandleIgnoreWarnings();

When pi_State = '01634';
     //Der Name des einzigartigen Datentyps ist zu lang und kann
     //nicht in den SQL-Deskriptorbereich (SQLDA)
     //zurückgegeben werden. Stattdessen wird der Kurzname
     //zurückgegeben.

  return SqlHandleIgnoreWarnings();

When pi_State = '01643';
     //Zuordnung zur Variablen SQLCODE oder pi_State signalisiert
     //keine Warnung und keinen Fehler.

  return SqlHandleIgnoreWarnings();

When pi_State = '01646';
     //Es kann keine Ergebnismenge zurückgegeben werden, da der
     //Cursor geschlossen wurde.

  return SqlHandleIgnoreWarnings();

When pi_State = '01647';
     //Ein Auslöser DB2SQL BEFORE wurde in DB2ROW geändert.

  return SqlHandleIgnoreWarnings();

When pi_State = '01658';
     //Binärdaten sind für DECRYPT_CHAR und DECYRYPT_DB ungültig.

  return SqlHandleIgnoreWarnings();

When pi_State = '01660';
     //Die Routine wurde erstellt, der Katalog wird durch eine
     //Wiederherstellung aber nicht aktualisiert.

  return SqlHandleIgnoreWarnings();

When pi_State = '01662';
     //Option zum Freigeben des Satzes in Anweisung CLOSE
     //ignoriert.

  return SqlHandleIgnoreWarnings();

When %Subst(pi_State:1:3) = '01H';
     //Gültige pi_State-Warnungen von benutzerdefinierter Funktion
     //oder externem Prozeduraufruf (CALL) zurückgegeben.

  return SqlHandleIgnoreWarnings();


     //Klassencode 02: Keine Daten
When pi_State = '02000';
     //Eine der folgenden Ausnahmen ist aufgetreten:
     //* Das Ergebnis der Anweisung SELECT INTO oder der Subselect
     //der Anweisung INSERT war eine leere Tabelle.
     //* Die Anzahl Zeilen in der durchsuchten Anweisung UPDATE
     //oder DELETE war null.
     //* Die Position des Cursors, auf den in in der Anweisung
     //FETCH verwiesen wurde, lag hinter der letzten Zeile
     //der Ergebnistabelle.
     //* Die FETCH-Ausrichtung ist ungültig.

  return SqlHandleNoData();

When pi_State = '02001';
     //Keine weiteren Ergebnismengen zurückgegeben.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '02505';
     //GET DESCRIPTOR VALUE ist größer als COUNT.

  LastErrorMessage = SqlProblem();
  return *off;


     //Klassencode 07: Fehler in dynamischem SQL
When pi_State = '07001';
     //Die Anzahl Hostvariablen ist für die Anzahl Parametermarken
     //nicht korrekt.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '07002';
     //Die Aufrufparameterliste oder der Steuerblock ist ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '07003';
     //Die in der Anweisung EXECUTE angegebene Anweisung ist eine
     //Auswahlanweisung, oder sie befindet sich nicht in
     //einem vorbereiteten Status.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '07004';
     //Die Klausel USING oder die Klausel INTO ist für dynamische
     //Parameter erforderlich.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '07005';
     //Der Anweisungsname des Cursors gibt eine vorbereitete
     //Anweisung an, die keinem Cursor zugeordnet werden
     //kann.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '07006';
     //Eine Eingabehostvariable, eine Übergangsvariable oder eine
     //Parametermarke kann aufgrund ihres Datentyps nicht
     //verwendet werden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '07008';
     //Die Deskriptoranzahl ist ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '07009';
     //Der Deskriptorindex ist ungültig.

  LastErrorMessage = SqlProblem();
  return *off;


     //Klassencode 08: Verbindungsausnahme
When pi_State = '08001';
     //Der Anwendungsrequester kann die Verbindung nicht
     //herstellen.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '08002';
     //Die Verbindung ist bereits vorhanden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '08003';
     //Die Verbindung ist nicht vorhanden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '08004';
     //Der Anwendungsserver hat das Herstellen der Verbindung
     //zurückgewiesen.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '08501';
     //Eine Anweisung DISCONNECT ist nicht zulässig, wenn die
     //Verbindung einen mit LI6.2 geschützten Dialog
     //verwendet.

  LastErrorMessage = SqlProblem();
  return *off;


     //Klassencode 09: Ausnahme für ausgelöste Aktion
When pi_State = '09000';
     //Eine ausgelöste SQL-Anweisung ist fehlgeschlagen.

  LastErrorMessage = SqlProblem();
  return *off;


     //Klassencode 0A: Funktion nicht unterstützt
When pi_State = '0A001';
     //Die Anweisung CONNECT ist ungültig, da sich der Prozess
     //nicht in einem verbindungsfähigen Status befindet.

  LastErrorMessage = SqlProblem();
  return *off;


     //Klassencode 0E: Ungültige Schemanamenslistenangabe
When pi_State = '0E000';
     //Die Schemanamensliste in einer Anweisung SET PATH ist
     //ungültig.

  LastErrorMessage = SqlProblem();
  return *off;


     //Klassencode 0F: Ungültiges Token
When pi_State = '0F001';
     //Der Querverweiswert stellt momentan keinen Wert dar.

  LastErrorMessage = SqlProblem();
  return *off;


     //Klassencode 0K: Signal erneut senden, wenn Handler nicht
     //aktiv
When pi_State = '0K000';
     //Eine Anweisung RESIGNAL wurde ausgegeben, es ist aber kein
     //Handler aktiv.

  LastErrorMessage = SqlProblem();
  return *off;


     //Klassencode 0W: Unzulässige Anweisung bei Auslöser gefunden
When pi_State = '0W000';
     //Die Anweisung ist in einem Auslöser nicht zulässig.

  LastErrorMessage = SqlProblem();
  return *off;


     //Klassencode 0Z: Diagnoseausnahme
When pi_State = '0Z001';
     //Maximale Anzahl Diagnosebereiche im Stack überschritten.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '0Z002';
     //Zugriff auf Diagnose im Stack erfolgte ohne aktiven Handler.

  LastErrorMessage = SqlProblem();
  return *off;


     //Klassencode 20: Angabe für Groß-/Kleinschreibung für
     //Anweisung CASE nicht gefunden
When pi_State = '20000';
     //Die Angabe für Groß-/Kleinschreibung wurde für die
     //Anweisung CASE nicht gefunden.

  LastErrorMessage = SqlProblem();
  return *off;


     //Klassencode 21: Ungültiger Beziehungstyp
When pi_State = '21000';
     //Das Ergebnis einer Anweisung SELECT INTO, eines skalaren
     //Fullselect oder einer Unterabfrage für ein
     //Basisprädikat enthält mehr als einen einzigen Wert.

  if ignoreMoreThanOneRow;
    return *on;
  endIf;

  LastErrorMessage = SqlProblem();
  return *off;



     //Klassencode 22: Datenfehler
When pi_State = '22001';
     //Zeichendaten wurden auf der rechten Seite abgeschnitten;
     //beispielsweise handelt es sich bei einem
     //Aktualisierungs- oder Einfügewert um eine
     //Zeichenfolge, die für die Spalte zu lang ist, oder
     //ein Wert für Datum und Uhrzeit kann einer
     //Hostvariablen nicht zugeordnet werden, da sie zu
     //klein ist.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '22002';
     //Ein Nullwert oder das Fehlen eines Bezugszahlparameters
     //wurde erkannt; beispielsweise kann der Nullwert
     //keiner Hostvariablen zugeordnet werden, da keine
     //Bezugszahlvariable angegeben ist.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '22003';
     //Ein numerischer Wert liegt außerhalb des gültigen Bereichs.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '22004';
     //Ein Nullwert ist nicht zulässig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '22005';
     //Bei der Zuordnung ist ein Fehler aufgetreten.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '22006';
     //Die FETCH-Ausrichtung ist ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '22007';
     //Ein ungültiges Format für Datum und Uhrzeit wurde erkannt,
     //d. h., es wurde eine ungültige
     //Zeichenfolgedarstellung oder ein ungültiger Wert
     //angegeben.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '22008';
     //Überlauf für Datums-/Zeitfeld aufgetreten; beispielsweise
     //hatte eine Rechenoperation für ein Datum oder eine
     //Zeitmarke ein Ergebnis zur Folge, das nicht innerhalb
     //des gültigen Bereichs für Datumsangaben liegt.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '22011';
     //Ein Unterzeichenfolgefehler ist aufgetreten; beispielsweise
     //liegt ein Argument von SUBSTR außerhalb des gültigen
     //Bereichs.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '22012';
     //Division durch Null ist ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '22018';
     //Der Zeichenwert für die Skalarfunktion CAST, DECIMAL, FLOAT
     //oder INTEGER ist ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '22019';
     //Das Prädikat LIKE enthält ein ungültiges Escapezeichen.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '22021';
     //Ein Zeichen befindet sich nicht in dem codierten
     //Zeichensatz, oder die Konvertierung wird nicht
     //unterstützt.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '22023';
     //Ein Parameterwert oder ein Hostvariablenwert ist ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '22024';
     //Eine auf NUL endende Eingabehostvariable oder ein auf NUL
     //endender Parameter enthielt nicht NUL.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '22025';
     //Das Zeichenfolgemuster des Prädikats LIKE enthält ein
     //ungültiges Vorkommen eines Escapezeichens.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '22501';
     //Die Länge des Steuerfelds einer Zeichenfolge variabler
     //Länge ist negativ oder größer als der Maximalwert.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '22503';
     //Die Zeichenfolgedarstellung eines Namens ist ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '22504';
     //Ein gemischter Datenwert ist ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '22511';
     //ADT-Länge überschreitet maximale Spaltenlänge. Der Wert für
     //eine Zeilenkennungs- (ROWID) oder Referenzspalte ist
     //ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '22522';
     //Ein CCSID-Wert ist entweder generell ungültig oder für den
     //Datentyp oder Subtyp ungültig oder für das Schema für
     //Codeumsetzung ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '22524';
     //Zeichenkonvertierung hatte das Abschneiden von Daten zur
     //Folge.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '22525';
     //Wert für Partitionierungsschlüssel ist ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '22527';
     //Ungültige Eingabedaten für das Einfügen mehrerer Zeilen
     //gefunden.

  LastErrorMessage = SqlProblem();
  return *off;


     //Klassencode 23: Ungültige Integritätsbedingung
When pi_State = '23001';
     //Das Aktualisieren oder Löschen eines übergeordneten
     //Schlüssels wird durch eine Aktualisierungs- oder
     //-Löschregel RESTRICT verhindert.

  return SqlHandleCstViolation();

When pi_State = '23502';
     //Ein Einfüge- oder Aktualisierungswert ist null, die Spalte
     //darf jedoch keine Nullwerte enthalten.

  return SqlHandleCstViolation();

When pi_State = '23503';
     //Der Einfüge- oder Aktualisierungswert eines Fremdschlüssels
     //ist ungültig.
  return SqlHandleCstViolation();

When pi_State = '23504';
     //Das Aktualisieren oder Löschen eines übergeordneten
     //Schlüssels wird durch eine Aktualisierungs- oder
     //-Löschregel NO ACTION verhindert.

  return SqlHandleCstViolation();

When pi_State = '23505';
     //Es kam zu einem Verstoß gegen eine Integritätsbedingung,
     //die durch einen eindeutigen Index oder eine
     //eindeutige Integritätsbedingung auferlegt wird.

  return SqlHandleDupRcd();

When pi_State = '23511';
     //Eine übergeordnete Zeile kann nicht gelöscht werden, da die
     //Prüfung auf Integritätsbedingung das Löschen nicht
     //erlaubt.

  return SqlHandleCstViolation();

When pi_State = '23512';
     //Die Prüfung auf Integritätsbedingung kann nicht hinzugefügt
     //werden, da die Tabelle Zeilen enthält, die nicht der
     //Definition der Integritätsbedingung entsprechen.
When pi_State = '23513';
     //Die Ergebniszeilen der Anweisung INSERT oder UPDATE
     //entsprechen nicht der Definition der Prüfung auf
     //Integritätsbedingung.

  return SqlHandleCstViolation();

When pi_State = '23515';
     //Der eindeutige Index kann nicht erstellt werden, oder eine
     //eindeutige Integritätsbedingung kann nicht
     //hinzugefügt werden, da die Tabelle doppelte Werte für
     //den angegebenen Schlüssel enthält.

  return SqlHandleCstViolation();

When pi_State = '23520';
     //Der Fremdschlüssel kann nicht definiert werden, da keiner
     //seiner Werte mit dem eines übergeordneten Schlüssels
     //in der übergeordneten Tabelle übereinstimmt.
When pi_State = '23522';
     //Der Wertebereich für die Identitätsspalte oder Reihenfolge
     //wurde vollständig ausgeschöpft.

  LastErrorMessage = SqlProblem();
  return *off;


     //Klassencode 24: Ungültiger Cursorstatus
When pi_State = '24501';
     //Der angegebene Cursor ist nicht geöffnet.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '24502';
     //Der in einer Anweisung OPEN angegebene Cursor ist bereits
     //geöffnet.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '24504';
     //Der in der Anweisung UPDATE, DELETE, SET oder GET
     //angegebene Cursor ist nicht in einer Zeile
     //positioniert.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '24506';
     //Die in PREPARE angegebene Anweisung ist die Anweisung für
     //einen geöffneten Cursor.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '24507';
     //FETCH CURRENT wurde angegeben, die aktuelle Zeile wurde
     //jedoch gelöscht, oder ein Wert einer ORDER BY-Spalte
     //der aktuellen Zeile wurde geändert.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '24513';
     //FETCH NEXT, PRIOR, CURRENT oder RELATIVE ist nicht
     //zulässig, da die Cursorposition nicht bekannt ist.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '24514';
     //Dieser Cursor wurde durch einen vorhergehenden Fehler
     //inaktiviert.

  LastErrorMessage = SqlProblem();
  return *off;


     //Klassencode 25: Ungültiger Transaktionsstatus
When pi_State = '25000';
     //Eine Aktualisierungsoperation ist für die
     //Anwendungsausführungsumgebung ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '25006';
     //Eine Aktualisierungsoperation ist ungültig, da die
     //Transaktion schreibgeschützt ist.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '25501';
     //Die Anweisung ist nur als erste Anweisung in einer
     //Arbeitseinheit zulässig.

  LastErrorMessage = SqlProblem();
  return *off;


     //Klassencode 26: Ungültige SQL-Anweisungs-ID
When pi_State = '26501';
     //Die angegebene Anweisung ist nicht vorhanden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '26510';
     //Dem in einer Anweisung DECLARE CURSOR angegebenen
     //Anweisungsnamen ist bereits ein Cursor zugeordnet.

  LastErrorMessage = SqlProblem();
  return *off;


     //Klassencode 27: Ungültige ausgelöste Datenänderung
When pi_State = '27000';
     //Es wurde versucht, die Zieltabelle der Anweisung MERGE
     //durch eine Integritätsbedingung oder einen Auslöser
     //zu ändern.

  LastErrorMessage = SqlProblem();
  return *off;


     //Klassencode 28: Ungültige Berechtigungsangabe
When pi_State = '28000';
     //Berechtigungsname ist ungültig.

  LastErrorMessage = SqlProblem();
  return *off;


     //Klassencode 2D: Ungültige Transaktionsbeendigung
When pi_State = '2D522';
     //COMMIT und ROLLBACK sind in einer zusammengesetzten
     //Anweisung ATOMIC nicht zulässig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '2D528';
     //Dynamische Prozedur COMMIT oder COMMIT ON RETURN ist für
     //die Anwendungsausführungsumgebung ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '2D529';
     //Dynamisches ROLLBACK ist für die
     //Anwendungsausführungsumgebung ungültig.

  LastErrorMessage = SqlProblem();
  return *off;


     //Klassencode 2E: Ungültiger Verbindungsname
When pi_State = '2E000';
     //Verbindungsname ist ungültig.

  LastErrorMessage = SqlProblem();
  return *off;


     //Klassencode 2F: SQL-Funktionsausnahme
When pi_State = '2F002';
     //Die SQL-Funktion hat versucht, Daten zu ändern, die
     //Funktion war jedoch nicht als MODIFIES SQL DATA
     //definiert.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '2F003';
     //Die Anweisung ist in einer Funktion oder Prozedur nicht
     //zulässig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '2F004';
     //Die SQL-Funktion hat versucht, Daten zu lesen, die Funktion
     //war jedoch nicht als READS SQL DATA definiert.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '2F005';
     //Die Funktion hat keine Anweisung RETURN ausgeführt.

  LastErrorMessage = SqlProblem();
  return *off;


     //Klassencode 33: Ungültiger SQL-Deskriptorname
When pi_State = '33000';
     //Der SQL-Deskriptorname ist ungültig.

  LastErrorMessage = SqlProblem();
  return *off;


     //Klassencode 34: Ungültiger Cursorname
When pi_State = '34000';
     //Cursorname ist ungültig.

  LastErrorMessage = SqlProblem();
  return *off;


     //Klassencode 35: Ungültige Bedingungszahl
When pi_State = '35000';
     //Bedingungszahl ist ungültig.

  LastErrorMessage = SqlProblem();
  return *off;


     //Klassencode 36: Cursorempfindlichkeitsausnahme
When pi_State = '36001';
     //Ein mit SENSITIVE definierter Cursor kann nicht für die
     //angegebene Auswahlanweisung definiert werden.

  LastErrorMessage = SqlProblem();
  return *off;


     //Klassencode 38: Ausnahme für externe Funktion
When %Subst(pi_State:1:2) = '38';
     //Gültige pi_State-Werte für einen Fehler von externer
     //Routine oder Auslöser zurückgegeben.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '38001';
     //Die externe Routine ist nicht zum Ausführen von
     //SQL-Anweisungen berechtigt.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '38002';
     //Die externe Routine hat versucht, Daten zu ändern, die
     //Routine war jedoch nicht als MODIFIES SQL DATA
     //definiert.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '38003';
     //Die Anweisung ist in einer Routine nicht zulässig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '38004';
     //Die externe Routine hat versucht, Daten zu lesen, die
     //Routine war jedoch nicht als READS SQL DATA definiert.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '38501';
     //Beim Aufrufen einer benutzerdefinierten Funktion, einer
     //externen Prozedur oder eines Auslösers (unter
     //Verwendung der Aufrufkonvention SIMPLE CALL oder
     //SIMPLE CALL WITH NULLS) ist ein Fehler aufgetreten.

  LastErrorMessage = SqlProblem();
  return *off;


     //Klassencode 39: Ausnahme für externen Funktionsaufruf
When pi_State = '39004';
     //Ein Nullwert ist für ein Argument IN oder INOUT nicht
     //zulässig, wenn PARAMETER STYLE GENERAL oder ein
     //Argument verwendet wird, das ein
     //Java-Primitive-Datentyp ist.

  LastErrorMessage = SqlProblem();
  return *off;


     //Klassencode 3B: Sicherungspunktausnahme
When pi_State = '3B001';
     //Der Sicherungspunkt ist ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '3B002';
     //Die maximale Anzahl Sicherungspunkte wurde erreicht.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '3B501';
     //Ein doppelter Sicherungspunktname wurde gefunden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '3B502';
     //Es wurde eine Anweisung RELEASE oder ROLLBACK TO SAVEPOINT
     //angegeben, es ist jedoch kein Sicherungspunkt
     //vorhanden.

  LastErrorMessage = SqlProblem();
  return *off;


     //Klassencode 3C: Mehrdeutiger Cursorname
When pi_State = '3C000';
     //Der Cursorname ist mehrdeutig.

  LastErrorMessage = SqlProblem();
  return *off;


     //Klassencode 3F: Ungültiger Schemaname (Datensammlungsname)
When pi_State = '3F000';
     //Der Schemaname (Datensammlungsname) ist ungültig.

  LastErrorMessage = SqlProblem();
  return *off;


     //Klassencode 42: Syntaxfehler oder ungültige Zugriffsregel
When pi_State = '42501';
     //Die Berechtigungs-ID hat nicht die Berechtigung, die
     //Operation für das angegebene Objekt auszuführen.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42502';
     //Die Berechtigungs-ID hat nicht die Berechtigung, die
     //Operation wie angegeben auszuführen.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42506';
     //Eignerberechtigungsfehler aufgetreten.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42601';
     //Ein Zeichen, ein Token oder eine Klausel ist ungültig oder
     //fehlt.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42602';
     //In einem Namen wurde ein ungültiges Zeichen gefunden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42603';
     //Es wurde eine nicht beendete Zeichenfolgekonstante gefunden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42604';
     //Es wurde eine ungültige numerische Konstante oder
     //Zeichenfolgekonstante gefunden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42605';
     //Die für eine Skalarfunktion angegebene Anzahl Argumente ist
     //ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42606';
     //Es wurde eine ungültige Hexadezimalkonstante gefunden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42607';
     //Ein Operand einer Spaltenfunktion oder eines Operators für
     //CONCAT ist ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42609';
     //Alle Operanden eines Operators oder Prädikats sind
     //Parametermarken.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42610';
     //Eine Parametermarke ist nicht zulässig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42611';
     //Die Definition einer Spalte oder eines Arguments ist
     //ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42612';
     //Die Anweisungszeichenfolge ist eine SQL-Anweisung, die in
     //dem verwendeten Kontext nicht gültig ist.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42613';
     //Klauseln schließen sich gegenseitig aus.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42614';
     //Ein doppeltes Schlüsselwort oder eine doppelte Klausel ist
     //ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42615';
     //Eine ungültige Alternative wurde gefunden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42616';
     //Ungültige Optionen angegeben.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42617';
     //Die Anweisungszeichenfolge besteht aus Leerzeichen oder ist
     //leer.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42618';
     //Eine Hostvariable ist nicht zulässig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42620';
     //Schreibgeschütztes SCROLL wurde mit der Klausel UPDATE
     //angegeben.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42621';
     //Der durch die Prüfung auf Integritätsbedingung generierte
     //Spaltenausdruck ist ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42622';
     //Ein Name oder ein Kennsatz ist zu lang.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42623';
     //Eine Klausel DEFAULT kann nicht angegeben werden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42625';
     //Ein Ausdruck CASE ist ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42629';
     //Für SQL-Routinen müssen Parameternamen angegeben werden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42631';
     //In einer Anweisung RETURN in einer SQL-Funktion muss ein
     //Ausdruck angegeben werden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42633';
     //Eine Klausel AS ist für ein Argument XMLATTRIBUTES oder
     //XMLFOREST erforderlich.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42634';
     //Der XML-Name ist ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42635';
     //Das XML-Namensbereichspräfix ist ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42701';
     //In einer Operation INSERT oder UPDATE oder der
     //Übergangsvariablen SET wurde ein doppelter
     //Spaltenname gefunden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42702';
     //Ein Spaltenbezug ist aufgrund von doppelten Namen
     //mehrdeutig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42703';
     //Ein nicht definierter Spalten- oder Parametername wurde
     //gefunden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42704';
     //Ein nicht definierter Objekt- oder
     //Integritätsbedingungsname wurde gefunden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42705';
     //Ein nicht definierter Servername wurde gefunden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42707';
     //Ein Spaltenname in ORDER BY gibt keine Spalte der
     //Ergebnistabelle an.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42709';
     //In einer Schlüsselspaltenliste wurde ein doppelter
     //Spaltenname angegeben.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42710';
     //Ein doppelter Objekt- oder Integritätsbedingungsname wurde
     //gefunden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42711';
     //In der Objektdefinition oder der Anweisung ALTER TABLE
     //wurde ein doppelter Spaltenname gefunden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42712';
     //In der Klausel FROM oder der Klausel REFERENCING einer
     //Anweisung CREATE TRIGGER wurde eine doppelte
     //Tabellenbezeichnung gefunden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42713';
     //In einer Liste wurde ein doppeltes Objekt gefunden, oder
     //das Objekt stimmt mit einem vorhandenen Objekt
     //überein.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42718';
     //Der Name für den lokalen Server ist nicht definiert.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42723';
     //Eine Funktion mit derselben Kennung ist bereits in dem
     //Schema vorhanden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42724';
     //Zugriff auf ein externes Programm, das für eine
     //benutzerdefinierte Funktion oder eine Prozedur
     //verwendet wird, ist nicht möglich.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42725';
     //Auf eine Routine oder Methode wurde direkt verwiesen (nicht
     //über eine Kennung oder einen bestimmten
     //Instanznamen), es gibt jedoch mehr als eine
     //spezifische Instanz dieser Routine oder Methode.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42726';
     //Doppelte Namen für allgemeine Tabellenausdrücke gefunden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42732';
     //In der Anweisung SET CURRENT PATH wurde ein doppelter
     //Schemaname gefunden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42733';
     //Eine Prozedur mit dem angegebenen Namen kann dem Schema
     //nicht hinzugefügt werden, da die Prozedurüberlastung
     //in dieser Datenbank nicht zulässig ist und bereits
     //eine Prozedur mit demselben Namen in dem Schema
     //vorhanden ist.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42734';
     //Ein doppelter Parametername, ein doppelter
     //SQL-Variablennamen, eine doppelte Bezeichnung oder
     //ein doppelter Bedingungsname wurde gefunden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42736';
     //Die in der Anweisung GOTO, ITERATE oder LEAVE angegebene
     //Marke wurde nicht gefunden oder ist ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42737';
     //Die angegebene Bedingung ist nicht definiert.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42738';
     //Ein doppelter Spaltenname oder eine nicht benannte Spalte
     //wurde in der Anweisung DECLARE CURSOR einer Anweisung
     //FOR angegeben.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42747';
     //Dasselbe Deskriptorelement wurde in der Anweisung SET
     //DESCRIPTOR mehrmals angegeben.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42802';
     //Die Anzahl Einfüge- oder Aktualisierungswerte stimmt nicht
     //mit der Anzahl Spalten überein.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42803';
     //Ein Spaltenbezug in der Klausel SELECT oder HAVING ist
     //ungültig, da es sich nicht um eine Gruppierungsspalte
     //handelt; oder ein Spaltenbezug in der Klausel GROUP
     //BY ist ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42804';
     //Die Ergebnisausdrücke in einem Ausdruck CASE sind nicht
     //kompatibel.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42805';
     //Eine ganze Zahl in der Klausel ORDER BY gibt keine Spalte
     //in der Ergebnistabelle an.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42806';
     //Ein Wert konnte einer Hostvariablen nicht zugeordnet
     //werden, da die Datentypen nicht kompatibel sind.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42807';
     //INSERT, UPDATE oder DELETE ist für dieses Objekt nicht
     //zulässig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42808';
     //Eine in der Operation INSERT oder UPDATE angegebene Spalte
     //kann nicht aktualisiert werden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42809';
     //Das angegebene Objekt hat nicht den Objekttyp, für den die
     //Anweisung gilt.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42810';
     //In einer Klausel FOREIGN KEY ist keine Basistabelle
     //angegeben.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42811';
     //Die angegebene Anzahl Spalten stimmt nicht mit der Anzahl
     //Spalten in der Klausel SELECT überein.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42813';
     //WITH CHECK OPTION kann für die angegebene Sicht nicht
     //verwendet werden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42814';
     //Die Spalte kann nicht gelöscht werden, da sie die einzige
     //Spalte in der Tabelle ist.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42815';
     //Der Datentyp, die Länge, die Anzahl der Kommastellen, der
     //Wert oder die CCSID ist ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42816';
     //Ein Wert für Datum und Uhrzeit oder die Dauer in einem
     //Ausdruck ist ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42817';
     //Die Spalte kann nicht gelöscht werden, da RESTRICT
     //angegeben wurde und eine Sicht oder
     //Integritätsbedingung von der Spalte abhängig ist oder
     //die Spalte Teil des Partitionierungsschlüssels ist.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42818';
     //Die Operanden eines Operators oder einer Funktion sind
     //nicht kompatibel.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42819';
     //Ein Operand einer Rechenoperation oder ein Operand einer
     //Funktion, die eine Zahl erfordert, ist keine Zahl.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42820';
     //Eine numerische Konstante ist zu lang, oder sie enthält
     //einen Wert, der nicht innerhalb des zulässigen
     //Bereichs für den Datentyp liegt.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42821';
     //Ein Datentyp für eine Zuordnung zu einer Spalte oder
     //Variable ist nicht mit dem Datentyp kompatibel.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42822';
     //Ein Ausdruck in der Klausel ORDER BY oder GROUP BY ist
     //ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42823';
     //Von einer Unterabfrage, für die nur eine einzige Spalte
     //zulässig ist, werden mehrere Spalten zurückgegeben.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42824';
     //Ein Operand von LIKE ist keine Zeichenfolge, oder der erste
     //Operand ist keine Spalte.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42825';
     //Die Zeilen von UNION, INTERSECT, EXCEPT oder VALUES haben
     //keine kompatiblen Spalten.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42826';
     //Die Zeilen von UNION, INTERSECT, EXCEPT oder VALUES
     //enthalten nicht dieselbe Anzahl Spalten.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42827';
     //Die in der Anweisung UPDATE oder DELETE angegebene Tabelle
     //stimmt nicht mit der vom Cursor bezeichneten Tabelle
     //überein.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42828';
     //Die durch den Cursor in einer Anweisung UPDATE oder DELETE
     //bezeichnete Tabelle kann nicht geändert werden, oder
     //der Cursor ist schreibgeschützt.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42829';
     //FOR UPDATE OF ist ungültig, da die durch den Cursor
     //bezeichnete Ergebnistabelle nicht geändert werden
     //kann.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42830';
     //Der Fremdschlüssel entspricht nicht der Beschreibung des
     //übergeordneten Schlüssels.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42832';
     //Die Operation ist für Systemobjekte nicht zulässig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42833';
     //Der qualifizierte Objektname entspricht nicht der
     //Benennungsoption.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42834';
     //SET NULL kann nicht angegeben werden, da der Nullwert
     //keiner Spalte des Fremdschlüssels zugeordnet werden
     //kann.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42835';
     //Zyklische Verweise können zwischen benannten abgeleiteten
     //Tabellen nicht angegeben werden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42836';
     //Die Angabe einer rekursiven benannten abgeleiteten Tabelle
     //ist ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42837';
     //Die Spalte kann nicht geändert werden, da ihre Attribute
     //mit den Attributen der aktuellen Spalte nicht
     //kompatibel sind.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42841';
     //Eine Parametermarke darf kein benutzerdefinierter Datentyp
     //oder Verweistyp sein.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42842';
     //Eine Spalten- oder Parameterdefinition ist ungültig, da
     //eine angegebene Option nicht der Spaltenbeschreibung
     //entspricht.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42845';
     //Eine ungültige Verwendung der Funktion NOT DETERMINISTIC
     //oder EXTERNAL ACTION wurde gefunden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42846';
     //Umsetzung des Quellentyps in Zieltyp wird nicht unterstützt.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42847';
     //Ein Befehl OVRDBF wurde für eine der Dateien ausgegeben,
     //auf die verwiesen wird, einer der Parameter ist
     //jedoch für SQL nicht gültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42848';
     //Isolationsstufe CS WITH KEEP LOCKS ist nicht zulässig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42850';
     //Eine logische Datei in CREATE VIEW ist ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42851';
     //Eine Datei, auf die verwiesen wird, ist keine Tabelle,
     //Sicht oder physische Datei.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42852';
     //Die in GRANT oder REVOKE angegebenen Berechtigungen sind
     //ungültig oder nicht konsistent. (Beispiel: GRANT
     //ALTER in einer Sicht.)

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42854';
     //Der Datentyp einer Ergebnisspalte in der Auswahlliste ist
     //mit dem definierten Typ in einer typisierten Sicht
     //oder einer MQT-Definition nicht kompatibel.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42855';
     //Die Zuordnung des großen Objekts (LOB) zu dieser
     //Hostvariablen ist nicht zulässig. Die
     //Zielhostvariable für alle Abrufe dieses LOB-Werts für
     //diesen Cursor muss eine Querverweisvariable oder eine
     //LOB-Variable sein.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42857';
     //Eine Datei, auf die verwiesen wird, hat mehr als ein Format.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42858';
     //Operation kann auf das angegebene Objekt nicht angewendet
     //werden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42860';
     //Die Integritätsbedingung kann nicht gelöscht werden, da sie
     //einen Primärschlüssel oder eine Zeilenkennung (ROWID)
     //umsetzt.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42862';
     //Eine erweiterte dynamische Anweisung kann nicht für ein
     //nicht erweitertes dynamisches Paket ausgeführt werden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42863';
     //In REXX wurde eine nicht definierte Hostvariable gefunden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42866';
     //Der Datentyp in der Klausel RETURNS oder der Klausel CAST
     //FROM in der Anweisung CREATE FUNCTION ist für den
     //Datentyp, der von der Quellenfunktion oder der
     //Anweisung RETURN im Funktionsrumpf zurückgegebenen
     //wird, nicht geeignet.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42872';
     //Klauseln der Anweisung FETCH sind mit der Cursordefinition
     //nicht kompatibel.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42873';
     //In einer Anweisung FETCH für mehrere Zeilen oder einer
     //Anweisung INSERT für mehrere Zeilen wurde eine
     //ungültige Anzahl Zeilen angegeben.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42874';
     //ALWCPYDTA(*NO) wurde angegeben, es ist jedoch eine Kopie
     //erforderlich, um die Auswahlanweisung zu
     //implementieren.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42875';
     //Der Teil mit dem Schemanamen in einem qualifizierten Namen
     //muss mit dem Schemanamen übereinstimmen.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42876';
     //Unterschiedliche CCSIDs für Schlüssel in CREATE INDEX sind
     //nur bei Angabe von *HEX für die Sortierfolge zulässig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42877';
     //Der Spaltenname kann nicht qualifiziert werden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42878';
     //Ein ungültiger Funktions- oder Prozedurname wurde zusammen
     //mit dem Schlüsselwort EXTERNAL verwendet.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42879';
     //Der Datentyp mindestens eines Eingabeparameters in der
     //Anweisung CREATE/FUNCTION ist für den entsprechenden
     //Datentyp in der Quellenfunktion nicht geeignet.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42880';
     //Die Datentypen für CAST TO und CAST FROM sind nicht
     //kompatibel oder hätten immer das Abschneiden einer
     //Zeichenfolge fester Länge zur Folge.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42881';
     //Ungültige Verwendung einer Funktion.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42882';
     //Das spezifische Qualifikationsmerkmal für den Instanznamen
     //stimmt nicht mit dem Qualifikationsmerkmal für den
     //Funktionsnamen überein.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42883';
     //Es wurde keine Funktion oder Methode mit übereinstimmender
     //Kennung gefunden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42884';
     //Es wurde keine Routine mit dem angegebenen Namen und
     //kompatiblen Argumenten gefunden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42885';
     //Die Anzahl der in einer Anweisung CREATE FUNCTION
     //angegebenen Eingabeparameter stimmt nicht mit der
     //Anzahl überein, die durch die in der Klausel SOURCE
     //angegebene Funktion bereitgestellt wird.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42886';
     //Die Attribute des Parameters IN, OUT oder INOUT stimmen
     //nicht überein.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42887';
     //Die Funktion ist in dem Kontext, in dem sie auftritt, nicht
     //gültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42888';
     //Die Tabelle hat keinen Primärschlüssel.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42889';
     //Die Tabelle hat bereits einen Primärschlüssel.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42890';
     //In der Klausel REFERENCES wurde eine Spaltenliste
     //angegeben, die angegebene übergeordnete Tabelle
     //verfügt jedoch über keine eindeutige
     //Integritätsbedingung mit den angegebenen Spaltennamen.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42891';
     //Eine zweite identische Integritätsbedingung UNIQUE ist
     //bereits vorhanden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42892';
     //Die referenzielle Integritätsbedingung und der Auslöser
     //sind nicht zulässig, da die Löschregel (DELETE) und
     //das Auslöseereignis nicht kompatibel sind.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42893';
     //Das Objekt oder die Integritätsbedingung kann nicht
     //gelöscht werden, oder Berechtigungen können nicht
     //entzogen werden, da andere Objekte davon abhängig
     //sind.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42894';
     //Der Wert einer Spalte oder eines Reihenfolgeattributs ist
     //ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42895';
     //Für statisches SQL kann eine Eingabehostvariable nicht
     //verwendet werden, da ihr Datentyp mit dem Parameter
     //einer Prozedur oder benutzerdefinierten Funktion
     //nicht kompatibel ist.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42896';
     //Die ASP-Nummer ist ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42898';
     //Ein ungültiger Korrelationsbezug oder eine ungültige
     //Übergangstabelle wurde in einem Auslöser gefunden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42899';
     //Korrelationsbezüge und Spaltennamen sind für ausgelöste
     //Aktionen mit der Klausel FOR EACH STATEMENT nicht
     //zulässig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '428A1';
     //Zugriff auf eine Datei, auf die durch eine
     //Dateireferenzvariable verwiesen wird, ist nicht
     //möglich.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '428B3';
     //Es wurde ein ungültiger pi_State-Wert angegeben.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '428B7';
     //Eine in einer SQL-Anweisung angegebene Zahl liegt außerhalb
     //des gültigen Bereichs.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '428B8';
     //Der in einer Umbenennung angegebene Name ist nicht gültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '428BA';
     //Mit WITHOUT RETURN definierte Cursor dürfen nicht in SET
     //RESULT SETS angegeben werden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '428C1';
     //Für eine Tabelle kann nur eine einzige Zeilenkennungs-
     //(ROWID) oder Identitätsspalte (IDENTITY) angegeben
     //werden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '428C4';
     //Die Anzahl Elemente auf jeder Seite des Prädikatoperators
     //stimmt nicht überein.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '428C7';
     //Eine Angabe für eine Zeilenkennungs- (ROWID) oder
     //Referenzspalte ist ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '428C9';
     //Eine Zeilenkennungs- (ROWID) oder Identitätsspalte
     //(IDENTITY) kann nicht als Zielspalte einer Anweisung
     //INSERT oder UPDATE angegeben werden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '428D1';
     //Zugriff auf eine Datei, auf die durch einen DATALINK-Wert
     //verwiesen wird, ist nicht möglich.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '428D2';
     //AS LOCATOR kann nicht für einen Parameter angegeben werden,
     //der kein LOB (großes Objekt) ist.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '428D4';
     //Auf einen in einer Anweisung FOR angegebenen Cursor kann
     //nicht in einer Anweisung OPEN, CLOSE oder FETCH
     //verwiesen werden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '428D5';
     //Die Endmarke entspricht nicht der Startmarke.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '428D6';
     //UNDO ist für zusammengesetzte Anweisungen NOT ATOMIC nicht
     //zulässig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '428D7';
     //Der Bedingungswert ist nicht zulässig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '428D8';
     //Die Deklaration der Variablen SQLCODE oder pi_State ist
     //ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '428DE';
     //Der Wert für PAGESIZE wird nicht unterstützt.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '428EC';
     //Der für die MQT (Materialized Query Table) angegebene
     //Fullselect ist ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '428EK';
     //Das Qualifikationsmerkmal für den Namen einer deklarierten
     //globalen temporären Tabelle oder für einen Index in
     //einer deklarierten globalen temporären Tabelle muss
     //SESSION lauten.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '428EW';
     //Die Tabelle kann nicht in eine MQT (Materialized Query
     //Table) oder aus einer MQT konvertiert werden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '428F1';
     //Eine SQL-Funktion TABLE muss ein Tabellenergebnis
     //zurückgeben.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '428F2';
     //In einer Anweisung RETURN in einer SQL-Prozedur muss ein
     //ganzzahliger Ausdruck angegeben werden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '428F9';
     //Ein Sequenzausdruck darf in diesem Kontext nicht angegeben
     //werden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '428FA';
     //Die Anzahl der Kommastellen der Dezimalzahl muss null sein.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '428FC';
     //Die Länge des Verschlüsselungskennworts ist ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '428FD';
     //Das für die Entschlüsselung verwendete Kennwort stimmt
     //nicht mit dem für die Verschlüsselung der Daten
     //verwendeten Kennwort überein.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '428FE';
     //Die Daten sind nicht das Ergebnis der Funktion ENCRYPT.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '428FI';
     //Die Klausel ORDER OF wurde angegeben, die Bezeichnung der
     //Tabelle, auf die verwiesen wird, ist jedoch nicht
     //sortiert.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '428FP';
     //Für jede Art von Operation in einer Sicht ist nur ein
     //einziger INSTEAD OF-Auslöser zulässig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '428FQ';
     //Ein INSTEAD OF-Auslöser darf keine Sicht angeben, die mit
     //WITH CHECK OPTION definiert ist, keine Sicht, die in
     //einer anderen Sicht definiert ist, die wiederum mit
     //WITH CHECK OPTION definiert ist, und keine Sicht, die
     //in einer Sicht verschachtelt ist, die mit der Klausel
     //WITH ROW MOVEMENT definiert ist.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '428FR';
     //Eine Spalte kann nicht wie angegeben geändert werden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '428FT';
     //Die Tabelle ist mit der angegebenen
     //Datenpartitionierungsoperation nicht kompatibel.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '428FY';
     //In einer MQT (Materialized Query Table) kann keine Spalte
     //hinzugefügt, gelöscht oder geändert werden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '428G0';
     //Eine logische Datei verhindert das Ändern der
     //Partitionsattribute.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '428G2';
     //Die letzte Datenpartition kann nicht aus der Tabelle
     //gelöscht werden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42903';
     //Ungültige Verwendung einer Aggregatfunktion oder einer
     //OLAP-Funktion.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42904';
     //Die SQL-Prozedur wurde aufgrund eines Kompilierungsfehlers
     //nicht erstellt.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42906';
     //Eine Spaltenfunktion in einer Unterabfrage einer Klausel
     //HAVING umfasst einen Ausdruck, der einen Operator auf
     //einen Korrelationsbezug anwendet.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42907';
     //Die Zeichenfolge ist zu lang.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42908';
     //Die Anweisung umfasst nicht eine erforderliche Spaltenliste.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42910';
     //Die Anweisung ist in einer zusammengesetzten Anweisung
     //nicht zulässig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42911';
     //Eine Division durch eine Dezimalzahl ist ungültig, da das
     //Ergebnis eine negative Anzahl Kommastellen hätte.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42912';
     //Eine Spalte kann nicht aktualisiert werden, da sie nicht in
     //der Klausel UPDATE der Auswahlanweisung des Curors
     //angegeben ist.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42914';
     //DELETE ist ungültig, da die Tabelle, auf die in einer
     //Unterabfrage verwiesen wird, von der Operation
     //betroffen sein kann.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42917';
     //Das Objekt kann nicht explizit gelöscht oder geändert
     //werden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42918';
     //Ein benutzerdefinierter Datentyp kann nicht mit einem
     //systemdefinierten Datentypnamen (z. B. INTEGER)
     //erstellt werden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42919';
     //Verschachtelte zusammengesetzte Anweisungen sind nicht
     //zulässig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42922';
     //DROP SCHEMA kann unter COMMIT-Steuerung nicht ausgeführt
     //werden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42923';
     //Programm oder Paket muss erneut erstellt werden, um auf
     //einen Aliasnamen zu verweisen.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42924';
     //Ein Aliasname wird statt in eine Tabelle oder Sicht an dem
     //fernen Standort in einen anderen Aliasnamen aufgelöst.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42925';
     //Rekursive benannte abgeleitete Tabellen können SELECT
     //DISTINCT nicht angeben und müssen UNION ALL angeben.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42926';
     //Querverweise sind mit COMMIT(*NONE) nicht zulässig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42928';
     //WITH EMPTY TABLE kann nicht angegeben werden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42929';
     //FOR ALL PARTITIONS ist für einen verschlüsselten
     //Vektorindex nicht zulässig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42930';
     //Dieselbe Spalte wurde in FOR UPDATE OF und ORDER BY
     //angegeben.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42932';
     //Die Voraussetzungen für die Programmerstellung sind nicht
     //korrekt.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42937';
     //Die Parameter dürfen keinen gemischten Subtyp haben.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42939';
     //Der Name kann nicht verwendet werden, da die angegebene
     //Kennung für die Verwendung durch das System
     //reserviert ist.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42944';
     //Die Berechtigungs-ID kann nicht sowohl Eigner als auch
     //Primärgruppe/Eigner sein.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42961';
     //Der angegebene Servername stimmt nicht mit dem aktuellen
     //Server überein.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42962';
     //Eine Spalte für große Objekte (LOBs), eine Spalte für
     //strukturierte Typen oder eine Datenverknüpfungsspalte
     //kann nicht in einem Index, einem Schlüssel oder einer
     //Integritätsbedingung verwendet werden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42969';
     //Das Paket wurde nicht erstellt, und die aktuelle
     //Arbeitseinheit wurde rückgängig gemacht, da interne
     //Einschränkungen vorliegen oder eine Abschnittsnummer
     //ungültig ist.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42970';
     //COMMIT HOLD oder ROLLBACK HOLD ist nur für einen DB2 UDB
     //für iSeries-Anwendungsserver zulässig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42971';
     //SQL-Anweisungen können nicht unter COMMIT-Steuerung
     //ausgeführt werden, da die COMMIT-Steuerung bereits
     //für eine andere relationale Datenbank aktiv ist.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42972';
     //Ein Ausdruck in einer Joinbedingung oder Klausel ON in
     //einer Anweisung MERGE verweist auf Spalten in
     //mehreren Operandentabellen.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42977';
     //Die Berechtigungs-ID kann nicht geändert werden, wenn die
     //Verbindung zum lokalen Server hergestellt wird.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42978';
     //Eine Bezugszahlvariable ist keine ganze Zahl ohne
     //erweiterte Genauigkeit.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42981';
     //CREATE SCHEMA ist nicht zulässig, wenn in der
     //Arbeitseinheit Änderungen anstehen.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42984';
     //Die Berechtigung kann der Sicht nicht erteilt werden, da
     //eine abhängige Sicht oder Tabelle die Berechtigung
     //*OBJOPR oder *OBJMGT hat und der Berechtigte nicht
     //die Berechtigung *ALLOBJ oder die angegebene
     //Berechtigung für die abhängige Tabelle oder Sicht hat.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42985';
     //Die Anweisung ist in einer Routine nicht zulässig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42987';
     //Die Anweisung ist in einem Auslöser nicht zulässig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42990';
     //Ein eindeutiger Index oder eine eindeutige
     //Integritätsbedingung ist nicht zulässig, da die
     //Schlüsselspalten keine Obermenge der
     //Partitionierungsschlüsselspalten sind.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42995';
     //Die aufgeforderte Funktion gilt nicht für globale temporäre
     //Tabellen.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42996';
     //Der Partitionierungsschlüssel kann keine Spalte für Datum
     //und Uhrzeit und keine Spalte mit Gleitbereich sein.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42998';
     //Eine referenzielle Integritätsbedingung ist nicht zulässig,
     //da die Spalten des Fremdschlüssels keine Obermenge
     //der Partitionierungsschlüsselspalten sind oder die
     //Knotengruppe nicht mit der übergeordneten Tabelle
     //übereinstimmt.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '42999';
     //Die Abfrage ist in einer verteilten Tabelle oder Sicht
     //nicht zulässig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '429B6';
     //Zeilen aus einer verteilten Tabelle können nicht umverteilt
     //werden, da die Tabelle eine Datenverknüpfungsspalte
     //mit FILE LINK CONTROL enthält.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '429B7';
     //Eine referenzielle Integritätsbedingung mit einer
     //Löschregel mit dem Wert CASCADE ist für eine Tabelle
     //mit einer Datenverknüpfungsspalte mit FILE LINK
     //CONTROL nicht zulässig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '429BD';
     //RETURN muss die letzte SQL-Anweisung der atomaren
     //zusammengesetzten Anweisung in einer SQL-Zeilen- oder
     //-Tabellenfunktion sein.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '429BH';
     //Eine partitionierte Tabelle kann keine Identitätsspalte
     //enthalten.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '429BM';
     //Die ICU-Sortierfolge kann in diesem Kontext nicht verwendet
     //werden.

  LastErrorMessage = SqlProblem();
  return *off;


     //Klassencode 44: Ungültige Angabe WITH CHECK OPTION
When pi_State = '44000';
     //INSERT oder UPDATE ist nicht zulässig, da die Ergebniszeile
     //nicht der Sichtdefinition entspricht.

  LastErrorMessage = SqlProblem();
  return *off;


     //Klassencode 46: Java-Fehler
When pi_State = '46001';
     //Die beim Installieren oder Ersetzen einer JAR-Prozedur
     //angegebene URL gab keine gültige JAR-Datei an.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '46002';
     //Der beim Installieren, Ersetzen oder Entfernen einer
     //Java-Prozedur angegebene JAR-Name ist ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '46003';
     //Die JAR-Datei kann nicht entfernt werden, da eine Klasse
     //gerade von einer Prozedur verwendet wird.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '46007';
     //Eine Java-Funktion hat eine Java-Methode mit einem
     //ungültigen Funktionstyp.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '46008';
     //Eine Java-Funktion kann keiner einzelnen Java-Methode
     //zugeordnet werden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '46501';
     //Beim Installieren oder Entfernen einer JAR-Prozedur für
     //"<JAR-ID>" wurde die Verwendung eines
     //Deploymentdeskriptors angegeben.

  LastErrorMessage = SqlProblem();
  return *off;


     //Klassencode 51: Ungültiger Anwendungsstatus
When pi_State = '51002';
     //Das Paket für die Anforderung zur Ausführung einer
     //SQL-Anweisung wurde nicht gefunden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '51003';
     //Konsistenztoken stimmen nicht überein.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '51004';
     //Eine Adresse im SQL-Deskriptorbereich (SQLDA) ist ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '51009';
     //COMMIT oder ROLLBACK ist nicht zulässig, da die
     //COMMIT-Steuerung nicht gestartet wurde.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '51015';
     //Es wurde versucht, einen Abschnitt auszuführen, für den zur
     //Bindezeit ein Fehler festgestellt wurde.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '51021';
     //SQL-Anweisungen können erst ausgeführt werden, nachdem der
     //Anwendungsprozess eine Operation ROLLBACK ausgeführt
     //hat.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '51035';
     //Ein Ausdruck PREVVAL kann nicht verwendet werden, da in
     //dieser Sitzung für die Reihenfolge noch kein Wert
     //generiert wurde.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '51036';
     //Das implizite Herstellen der Verbindung zu einem fernen
     //Server ist nicht zulässig, da ein Sicherungspunkt
     //aussteht.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '51037';
     //Die Operation ist nicht zulässig, da ein Auslöser als
     //funktionsunfähig markiert wurde.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '51039';
     //Der Wert für ENCRYPTION PASSWORD ist nicht definiert.

  LastErrorMessage = SqlProblem();
  return *off;


     //Klassencode 53: Ungültiger Operand oder nicht konsistente
     //Angabe
When pi_State = '53038';
     //Die Anzahl Schlüsselgrenzwerte ist null oder größer als die
     //Anzahl Spalten in dem Schlüssel.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '53039';
     //Die Klausel PART der Anweisung ALTER fehlt oder ist
     //ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '53045';
     //Der Datentyp der Konstante für den Schlüsselgrenzwert
     //stimmt nicht mit dem Datentyp der Spalte überein.

  LastErrorMessage = SqlProblem();
  return *off;


     //Klassencode 54: SQL- oder Produktgrenzwert überschritten
When pi_State = '54001';
     //Die Anweisung ist zu lang oder zu komplex.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '54002';
     //Eine Zeichenfolgekonstante ist zu lang.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '54004';
     //Die Anweisung enthält zu viele Tabellennamen oder zu viele
     //Einträge in einer SELECT- oder INSERT-Liste.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '54005';
     //Der Sortierschlüssel ist zu lang oder enthält zu viele
     //Spalten.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '54006';
     //Das Ergebnis der Verknüpfung ist zu lang.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '54008';
     //Der Schlüssel ist zu lang, eine Spalte des Schlüssels ist
     //zu lang, oder der Schlüssel enthält zu viele Spalten.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '54009';
     //In einer Anweisung GRANT oder REVOKE wurden zu viele
     //Benutzer angegeben.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '54010';
     //Die Satzlänge der Tabelle ist zu groß.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '54011';
     //Für eine Tabelle, Sicht oder Tabellenfunktion wurden zu
     //viele Spalten angegeben.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '54018';
     //Die Zeile ist zu lang.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '54019';
     //Die maximale Anzahl verzögerter Deskriptoren wurde
     //überschritten; wahrscheinlich wurden zu viele
     //unterschiedliche CCSIDs verwendet.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '54021';
     //Zu viele Integritätsbedingungen oder die
     //Integritätsbedingung ist zu groß.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '54023';
     //Der Grenzwert für die Anzahl Parameter oder Argumente für
     //eine Funktion oder Prozedur wurde überschritten.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '54028';
     //Die maximale Anzahl gleichzeitiger Handles für große
     //Objekte (LOBs) wurde erreicht.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '54035';
     //Ein interner Objektgrenzwert wurde überschritten.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '54038';
     //Die maximale Verschachtelungstiefe verschachtelter Routinen
     //oder Auslöser wurde überschritten.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '54044';
     //Eine Mehrbyte-(UCS-)Sortierfolgetabelle kann nicht in DRDA
     //unterstützt werden, da sie zu groß ist.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '54054';
     //Die Kombination aus der Anzahl Tabellenbereichspartitionen
     //und der zugehörigen Länge des
     //Partitionierungsbegrenzungsschlüssels ist zu groß.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '54057';
     //Ein XML-Elementname, ein Attributname, ein
     //Namensbereichspräfix oder eine URL ist zu lang.

  LastErrorMessage = SqlProblem();
  return *off;


     //Klassencode 55: Objekt nicht in erforderlichem Status
When pi_State = '55005';
     //Die Rekursion wird nur für einen DB2 UDB für
     //iSeries-Anwendungsserver unterstützt.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '55006';
     //Das Objekt kann nicht gelöscht werden, da es momentan von
     //demselben Anwendungsprozess verwendet wird.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '55007';
     //Das Objekt kann nicht geändert werden, da es momentan von
     //demselben Anwendungsprozess verwendet wird.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '55018';
     //Das Schema kann nicht gelöscht werden, da es sich in der
     //Bibliotheksliste befindet.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '55019';
     //Der Status des Objekts ist für die Operation ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '55029';
     //Das lokale Programm hat versucht, die Verbindung zu einer
     //fernen Datenbank herzustellen.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '55042';
     //Der Aliasname ist nicht zulässig, da er eine einzelne
     //Teildatei einer Datei angibt, die mehrere Teildateien
     //enthält.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '55048';
     //Verschlüsselte Daten können nicht verschlüsselt werden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '55050';
     //Ein Objekt kann nicht in einem geschützten Schema erstellt
     //werden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '55058';
     //Der Debugmodus (DEBUG MODE) kann für eine Routine, die mit
     //DISABLE DEBUG MODE erstellt wurde, nicht geändert
     //werden.

  LastErrorMessage = SqlProblem();
  return *off;


     //Klassencode 56: Sonstiger SQL- oder Produktfehler
When pi_State = '56016';
     //Die für Datenpartitionen angegebenen Bereiche sind ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '56084';
     //Ein nicht unterstützter SQLTYPE wurde in einer Auswahlliste
     //oder Eingabeliste gefunden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '56095';
     //Eine Bindeoption ist ungültig.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '560BF';
     //Die Verschlüsselungs- und Entschlüsselungsfunktion wurde
     //nicht installiert.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '560C4';
     //Die Option ist für die ARD-Schnittstelle nicht gültig.

  LastErrorMessage = SqlProblem();
  return *off;


     //Klassencode 57: Ressource nicht verfügbar oder
     //Bedienereingriff
When pi_State = '57005';
     //Die Anweisung kann nicht ausgeführt werden, da das
     //Zeitlimit für ein Dienstprogramm oder den Governor
     //überschritten wurde.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '57006';
     //Das Objekt kann nicht erstellt werden, da eine Operation
     //DROP oder CREATE ansteht.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '57007';
     //Das Objekt kann nicht verwendet werden, da eine Operation
     //ansteht.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '57011';
     //Virtuelle Speicher- oder Datenbankressource ist nicht
     //verfügbar.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '57012';
     //Eine Ressource (keine Datenbankressource) ist nicht
     //verfügbar. Dies hat keine Auswirkungen auf die
     //erfolgreiche Ausführung nachfolgender Anweisungen.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '57013';
     //Eine Ressource (keine Datenbankressource) ist nicht
     //verfügbar. Dies hat Auswirkungen auf die erfolgreiche
     //Ausführung nachfolgender Anweisungen.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '57014';
     //Die Verarbeitung wurde wie angefordert abgebrochen.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '57017';
     //Zeichenkonvertierung ist nicht definiert.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '57033';
     //Deadlock oder Zeitlimitüberschreitung ohne automatisches
     //Rückgängigmachen aufgetreten.

  return SqlHandleRowLock();

When pi_State = '57042';
     //DDM-Rekursion ist aufgetreten.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '57043';
     //Ein lokales SQL-Anwendungsprogramm kann auf einem
     //Anwendungsserver nicht ausgeführt werden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '57050';
     //Der Dateiserver ist momentan nicht verfügbar.

  LastErrorMessage = SqlProblem();
  return *off;


     //Klassencode 58: Systemfehler
When pi_State = '58002';
     //Ein Exit hat einen Fehler oder ungültige Daten
     //zurückgegeben.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '58003';
     //Es wurde eine ungültige Abschnittsnummer gefunden.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '58004';
     //Ein Systemfehler (der nicht notwendigerweise die
     //erfolgreiche Ausführung nachfolgender SQL-Anweisungen
     //verhindert) ist aufgetreten.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '58008';
     //Die Ausführung ist aufgrund eines Verteilerprotokollfehlers
     //fehlgeschlagen, der keine Auswirkungen auf die
     //erfolgreiche Ausführung nachfolgender DDM-Befehle
     //oder SQL-Anweisungen hat.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '58009';
     //Die Ausführung ist aufgrund eines Verteilerprotokollfehlers
     //fehlgeschlagen, der die Freigabe des Datenaustauschs
     //zur Folge hatte.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '58010';
     //Die Ausführung ist aufgrund eines Verteilerprotokollfehlers
     //fehlgeschlagen, der Auswirkungen auf die erfolgreiche
     //Ausführung nachfolgender DDM-Befehle oder
     //SQL-Anweisungen hat.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '58011';
     //Der DDM-Befehl ist ungültig, während der Bindeprozess aktiv
     //ist.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '58012';
     //Der Bindeprozess mit dem angegebenen Paketnamen und dem
     //angegebenen Konsistenztoken ist nicht aktiv.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '58014';
     //Der DDM-Befehl wird nicht unterstützt.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '58015';
     //Das DDM-Objekt wird nicht unterstützt.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '58016';
     //Der DDM-Parameter wird nicht unterstützt.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '58017';
     //Der DDM-Parameterwert wird nicht unterstützt.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '58018';
     //Die DDM-Antwortnachricht wird nicht unterstützt.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '58028';
     //Die Festschreibungsoperation ist fehlgeschlagen, da eine
     //Ressource in der Arbeitseinheit ihre Ressourcen nicht
     //festschreiben konnte.

  LastErrorMessage = SqlProblem();
  return *off;

When pi_State = '58033';
     //Bei dem Versuch, auf einen Clienttreiber zuzugreifen, ist
     //ein unerwarteter Fehler aufgetreten.

  LastErrorMessage = SqlProblem();
  return *off;


other;
     // Unerwarteter Fehler
  LastErrorMessage = SqlProblem();
  return *off;

Endsl;


End-Proc SqlCheckState;

   //************************************************************************
   // SqlHandleRowLock     |
   //************************************************************************
Dcl-Proc SqlHandleRowLock Export;
Dcl-Pi SqlHandleRowLock Ind;
End-Pi;


if ( ignoreRowLock );
  ignoreRowLock = *off;
  if ( logStatement );
    SqlLogState();
  endIf;
  // SndMsg('Ignore RowLock wurde aktiviert':4);
  return *on;
endIf;

   //--------------------------------------
   // Log schreiben
   //--------------------------------------
SqlLogState();

// Select;

   //--------------------------------------
   // Interaktiv
   //--------------------------------------
// when ( GetJobStat = 'I' );
   // ShowMsgBox( (GetCpfMsg(  '*'
   //                        : 'CPF5027'
   //             )          )
   //            );

   //--------------------------------------
   // Batch
   //--------------------------------------
// when ( GetJobStat = 'B' );

// EndSl;

return *off;


End-Proc SqlHandleRowLock;

   //************************************************************************
   // SqlHandleNoData      | Keine Daten gefunden
   //************************************************************************
Dcl-Proc SqlHandleNoData Export;
Dcl-Pi SqlHandleNoData Ind;
End-Pi;


if ( ignoreNoData );
  ignoreNoData = *off;
  if ( logStatement );
    SqlLogState();
  endIf;
  // SndMsg('Ignore NoData wurde aktiviert':4);
  return *on;
endIf;

   //-------------------------------------------------
   // Log schreiben
   //-------------------------------------------------
// SqlLogState();

return *off;


End-Proc SqlHandleNoData;

   //************************************************************************
   // SqlHandleDupRcd       | Doppelter Satzschlüssel
   //************************************************************************
Dcl-Proc SqlHandleDupRcd Export;
Dcl-Pi SqlHandleDupRcd Ind;
End-Pi;


if ( ignoreDupRcd );
  ignoreDupRcd = *off;
  if ( logStatement );
    SqlLogState();
  endIf;
  // SndMsg('Ignore DupRcd wurde aktiviert':4);
  return *on;
endIf;
   //--------------------------------------
   // Log schreiben
   //--------------------------------------
SqlLogState();

return *off;


End-Proc SqlHandleDupRcd;

   //************************************************************************
   // SqlHandleCstViolation | Constraint Violation
   //************************************************************************
Dcl-Proc SqlHandleCstViolation Export;
Dcl-Pi SqlHandleCstViolation Ind;
End-Pi;


if ( ignoreCstViolation );
  ignoreCstViolation = *off;
  if ( logStatement );
    SqlLogState();
  endIf;
  // SndMsg('Ignore CstViolation wurde aktiviert':4);
  return *on;
endIf;
   //--------------------------------------
   // Log schreiben
   //--------------------------------------
SqlLogState();

return *off;


End-Proc SqlHandleCstViolation;

   //************************************************************************
   // SqlProblem           | Unbehandeltes Problem
   //************************************************************************
Dcl-Proc SqlProblem Export;
Dcl-Pi SqlProblem VARCHAR(1024);
End-Pi;


Exec Sql
  Get Diagnostics Condition 1
          :Msg_Text = MESSAGE_TEXT;

// SndMsg(Msg_Text:3);

   //------------------------------------------------
   // Log schreiben
   //------------------------------------------------
SqlLogState();


return Msg_Text;


End-Proc SqlProblem;

   //************************************************************************
   // SqlLogState          | In Logdatei schreiben
   //************************************************************************
Dcl-Proc SqlLogState;
Dcl-Pi SqlLogState;
End-Pi;

Dcl-S myCaller Varchar(128);

   //------------------------------------------------
   // Wenn nicht gelogged werden soll, return
   //------------------------------------------------
if noLogStatement;
  noLogStatement = *off;
  return;
endIf;

myCaller = Who_Am_I_With_Call_Stack_Entry(
  'SQLSTS' : 'SQLSTS' : 'SQLCHECKSTATE');

Exec Sql
  Get Diagnostics
          :Cmd_Function = COMMAND_FUNCTION,
          :Dyn_Function = DYNAMIC_FUNCTION;
Exec Sql
  Get Diagnostics Condition 1
          :Ret_SqlCode  = DB2_RETURNED_SQLCODE,
          :Ret_State    = RETURNED_SQLSTATE,
          :Auth_Id      = DB2_AUTHORIZATION_ID,
          :Msg_Text     = MESSAGE_TEXT;

Exec Sql
  Insert Into SqlLog
           (
            SqUser,
            SqCaller,
            SqCmdFct,
            SqDynFct,
            SqCode,
            SqState,
            SqText,
            SqStamp,
            SqStmt
           )
    Values (
            :Auth_Id,
            :myCaller,
            :Cmd_Function,
            :Dyn_Function,
            :Ret_SqlCode,
            :Ret_State,
            :Msg_Text,
            Current_Timestamp,
            :stmt
           )
    With NC;

logStatement = *off;

Return;


End-Proc SqlLogState;

   //************************************************************************
   // SqlGetLogMsg         | Aus Logdatei lesen
   //************************************************************************

// Dcl-Proc SqlGetLogMsg Export;
// Dcl-Pi SqlGetLogMsg Char(1024);
// End-Pi;

// Dcl-S Msg Char(1024) inz;

// exec sql
//   select  trim(sqText)
//     into  :msg
//     from  sqllogp
//     where sqId = identity_val_local();

// Return %trim(msg);

// End-Proc SqlGetLogMsg;

   //************************************************************************
   // SetIgnoreDupRcd      | Doppelte Schlüssel ignorieren
   //************************************************************************
Dcl-Proc SetIgnoreDupRcd export;
Dcl-Pi SetIgnoreDupRcd;
End-Pi;
ignoreDupRcd = *on;
Return;

End-Proc SetIgnoreDupRcd;

   //************************************************************************
   // SetIgnoreNoData      | Keine Daten gefunen ignorieren
   //************************************************************************
Dcl-Proc SetIgnoreNoData export;
Dcl-Pi SetIgnoreNoData;
End-Pi;
ignoreNoData = *on;
Return;

End-Proc SetIgnoreNoData;

   //************************************************************************
   // SetIgnoreRowLock     | Satzsperre ignorieren
   //************************************************************************
Dcl-Proc SetIgnoreRowLock export;
Dcl-Pi SetIgnoreRowLock;
End-Pi;
ignoreRowLock = *on;
Return;

End-Proc SetIgnoreRowLock;

   //************************************************************************
   // SetIgnoreCstViolation| Constraint violation ignorieren
   //************************************************************************
Dcl-Proc SetIgnoreCstViolation export;
Dcl-Pi SetIgnoreCstViolation;
End-Pi;
ignoreCstViolation = *on;
Return;

End-Proc SetIgnoreCstViolation;

   //************************************************************************
   // SetLogStatement      | Protokollieren
   //************************************************************************
Dcl-Proc SetLogStatement export;
Dcl-Pi SetLogStatement;
End-Pi;
logStatement = *on;
Return;

End-Proc SetLogStatement;

   //************************************************************************
   // SetNoLogStatement    | nicht protokollieren
   //************************************************************************
Dcl-Proc SetNoLogStatement export;
Dcl-Pi SetNoLogStatement;
End-Pi;
noLogStatement = *on;
Return;

End-Proc SetNoLogStatement;

   //************************************************************************
   // UnsetAll             | Alle Kenner auf aus setzen
   //************************************************************************
Dcl-Proc UnsetAll;
Dcl-Pi UnsetAll;
End-Pi;
ignoreDupRcd = *off;
ignoreNoData = *off;
ignoreCstViolation = *off;
ignoreRowLock = *off;
logStatement = *off;
noLogStatement = *off;
ignoreMoreThanOneRow = *off;
ignoreCharacterConversionError = *off;
ignoreWarnings = *off;
Return;

End-Proc UnsetAll;

   //************************************************************************
   // SqlTrace             | Letztes Statement loggen
   //************************************************************************

Dcl-Proc SqlTrace export;
Dcl-Pi SqLTrace;
  statement Varchar(28672) const;
End-Pi;

Dcl-S myCaller Varchar(128);
Dcl-S rowCount Packed(31) inz;

   //-------------------------------------------------
   // Wer hat mich aufgerufen?
   //-------------------------------------------------

myCaller = Who_Am_I_With_Call_Stack_Entry(
  'SQLSTS' : 'SQLSTS' : 'SQLTRACE');

exec sql
  get diagnostics
       :rowCount = ROW_COUNT;

   //-------------------------------------------------
   // Insert in Logdatei
   //-------------------------------------------------

exec sql
  insert into sqltrace
    values( default,
            :myCaller,
            session_user,
            current_timestamp,
            :statement,
            :rowCount
          );

return;

End-Proc SqlTrace;

   //************************************************************************
   // SqlLastStatement     | Letztes Statement holen
   //************************************************************************
Dcl-Proc SqlLastStatement export;
Dcl-Pi SqlLastStatement Varchar(32767);
End-Pi;
   //-------------------------------------------------------------------------
Dcl-Pr RtvJobInf ExtPgm('QUSRJOBI');
  RcvVar Char(32766) options(*varsize);
  RcvVarLen Int(10) CONST;
  Format Char(8) CONST;
  JobName Char(26) CONST;
  IntJobID Char(16) CONST;
  ErrorCode Char(32766) options(*varsize);
End-Pr;
   //-------------------------------------------------------------------------
Dcl-Ds curStmt based(p_CurStmt);
  offSetCurStmt
  Int(10) POS(77);
  stmtSize Int(10) POS(81);
  status Int(10) POS(85);
  ccsid Int(10) POS(89);
End-Ds;

Dcl-Ds statement based(p_Statement);
  stmt Char(32767);
End-Ds;

Dcl-Ds dsEC;
  dsECBytesP Int(10) INZ(256);
  dsECBytesA Int(10) INZ(0);
  dsECMsgID Char(7);
  dsECReserv Char(1);
  dsECMsgDta Char(240);
End-Ds;

Dcl-S DataSize Int(10);
Dcl-S i Packed(5);


DataSize = 32767;
p_CurStmt = %alloc(DataSize);

   // Jobinformationen besorgen
RtvJobInf( curStmt
          :DataSize
          :'JOBI0900'
          :'*'
          :*BLANKS
          :dsEC
         );

p_Statement = p_CurStmt + offSetCurStmt;

dealloc p_CurStmt;

if stmtSize > 0;
  return %subst(stmt : 1 : stmtSize);
else;
  return 'Statement konnte nicht ermittelt werden.';
endIf;


End-Proc SqlLastStatement;

   //************************************************************************
   // SetIgnoreMoreThanOneRow
   //************************************************************************
Dcl-Proc SetIgnoreMoreThanOneRow export;
Dcl-Pi SetIgnoreMoreThanOneRow;
End-Pi;
ignoreMoreThanOneRow = *on;
Return;

End-Proc SetIgnoreMoreThanOneRow;

   //************************************************************************
   // SetIgnoreCharacterConversionError
   //************************************************************************
Dcl-Proc SetIgnoreCharacterConversionError export;
Dcl-Pi SetIgnoreCharacterConversionError;
End-Pi;
ignoreCharacterConversionError = *on;
Return;

End-Proc SetIgnoreCharacterConversionError;

   //************************************************************************
   // SetIgnoreWarnings
   //************************************************************************
Dcl-Proc SetIgnoreWarnings export;
Dcl-Pi SetIgnoreWarnings;
End-Pi;
ignoreWarnings = *on;
Return;

End-Proc SetIgnoreWarnings;

   //************************************************************************
   // SqlHandleIgnoreWarnings
   //************************************************************************
Dcl-Proc SqlHandleIgnoreWarnings Export;
Dcl-Pi SqlHandleIgnoreWarnings Ind;
End-Pi;


   //--------------------------------------
   // Log schreiben
   //--------------------------------------
SqlLogState();

if ( ignoreWarnings );
  ignoreWarnings = *off;
  // SndMsg('Ignore Warnings wurde aktiviert':4);
  return *on;
endIf;

return *off;


End-Proc SqlHandleIgnoreWarnings;

   //************************************************************************
   // GetLastErrorMessage
   //************************************************************************
Dcl-Proc SqlGetLastErrorMessage export;
Dcl-Pi SqlGetLastErrorMessage VARCHAR(1024);
End-Pi;
Return %trimR(LastErrorMessage);

End-Proc SqlGetLastErrorMessage;

