<?php
// Globale Variablen
 
$dbc_host="localhost";            // Datenbank-Host
$dbc_uname="einstein"; 		  // Datenbankbenutzer
$dbc_pass="1!n5+1!n"; 		  // Passwort
$dbc_dbname="einstein"; 	  // Datebankename

$style_position="style/einsteinjahr.css"; // Pfad zum Stylesheet
 
$verified=false;		  // Anzahl der Schlagzeilen, die auf der Übersicht angezeigt werden

// sessionverwaltung --------------------------------------------------------------------------------
if (isset($verify)) {
		
	$sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
	mysql_select_db($dbc_dbname, $sql_con);   	
	$sql_q="SELECT verify FROM projektleiter WHERE id = '$id'";
	$sql_r=mysql_query($sql_q,$sql_con);
	$sql_row= @mysql_fetch_array($sql_r);
	$sql_verify=$sql_row[0];
	
	if ($verify ==$sql_verify) {
		$sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
		mysql_select_db($dbc_dbname, $sql_con);   	
		$sql_q="UPDATE projektleiter set verify = 'true' WHERE id = '$id'";
	        $sql_r=mysql_query($sql_q,$sql_con);
		$verified=true;
	}
}
// html starts here ---------------------------------------------------------------------------------

?>

<html>
  <head>
    <title>FU-Berlin: Institut für Informatik</TITLE>
    <link rel="stylesheet" href=<? echo $style_position ?>>
  </head>

  <body> 
<?
if ( $verified ==false ) {

	echo "

		<h2>Bitte erneut versuchen. Registrierung fehlgeschlagen.</h2>

       ";
} else {        

	echo   "

		<h2>Registrierung erfolgreich.</h2>
		<h4>Sie können sich nun <a href=index.php>hier</a> einloggen.</h4>

	";
}   
?>

        <div id=footer>
          <address><font color=#000000>&nbsp;e-mail&nbsp;to: </font><a href="mailto:koenig@inf.fu-berlin.de"><font color=#000000>koenig@inf.fu-berlin.de</a></font></address>

        </div>
  </body>
</html>
