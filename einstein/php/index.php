<?php
// Globale Variablen

$site_url="http://schueler.mi.fu-berlin.de";					// URL der Seite

$adminID=5;                       						// Id des Admins
 
$dbc_host="localhost";            						// Datenbank-Host
$dbc_uname="einstein"; 		  						// Datenbankbenutzer
$dbc_pass="1!n5+1!n"; 		  						// Passwort
$dbc_dbname="einstein"; 	  						// Datebankename

$style_position="style/einsteinjahr.css"; 					// Pfad zum Stylesheet
 
$schlag_anzahl=5;		  						// Anzahl der Schlagzeilen, die auf der Übersicht angezeigt werden



// sessionverwaltung --------------------------------------------------------------------------------
if (!isset($angemeldet)) {
$angemeldet=false;
}
if ($angemeldet ==true ){
$time=time();
$sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
mysql_select_db($dbc_dbname, $sql_con);   	
$sql_q="DELETE from sessions where timestamp < $time";
$sql_r=mysql_query($sql_q,$sql_con);	
$sql_q="SELECT * from sessions WHERE SID ='$SID'";
$sql_r=mysql_query($sql_q,$sql_con);
$sql_row= @mysql_fetch_array($sql_r);
$sql_i2=$sql_row[IP];
$sql_ip2=getenv("REMOTE_ADDR");

	if($sql_i2 !=$sql_ip2) {
		$angemeldet =false;
	} else { 
		$exp_time=time()+259200;
		$sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
		mysql_select_db($dbc_dbname, $sql_con);   	
		$sql_q="UPDATE sessions set timestamp = '$exp_time' where SID ='$SID'";
       		$sql_r=mysql_query($sql_q,$sql_con);
		$angemeldet =true;
	}	
} else {
	if (($UID != "") && ($PWD != "")) {
		$pwd_tmp = md5($PWD);
		$sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
		mysql_select_db($dbc_dbname, $sql_con);   	
		$sql_q="SELECT id, email, password, verify from projektleiter where email ='$UID' and password = '$pwd_tmp'";
		$sql_r=mysql_query($sql_q,$sql_con);
		$sql_row= @mysql_fetch_array($sql_r);
		$sql_c=$sql_row[email];
		$sql_i=$sql_row[id];
		$sql_v=$sql_row[verify];

		if(($sql_c ='$UID') && ($sql_i != "") && ($sql_v =='true')) {
	    		$angemeldet =true;
   			$PWD = "";	
			$ip=getenv("REMOTE_ADDR");
			$exp_time=time()+259200;
			$time=time();

       			$new_sid=substr(md5(uniqid(rand())),12);

			$sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
			mysql_select_db($dbc_dbname, $sql_con);   	
      			$sql_q="DELETE from sessions where timestamp < $time";
      			$sql_r=mysql_query($sql_q,$sql_con);	
      		
      			$sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
			mysql_select_db($dbc_dbname, $sql_con);   
      			$sql_q="INSERT INTO sessions (SID,IP,timestamp,USERSID) VALUES ('$new_sid','$ip','$exp_time','$sql_i')";
      			$sql_r=mysql_query($sql_q,$sql_con);
      			if ($sql_r) {
	         		$SID = $new_sid;
	         		$angemeldet =true;
	        	} else { 
	        		echo "<html>Fehler:\n<br>".mysql_errno($sql_con)."<br>\n".mysql_error($sql_con)."<br>";
	        	}
	      	} 
   	} else {
		$angemeldet =false;
	}
}

// event handling --------------------------------------------------------------------------------

if (!isset($action)) {} 
else {		


// Hier folgen die Hauptdatenbanktransaktionen
// 
// anmeldung1 = Registrierung
// anmeldung2 = Profildaten ändern
// booking = Projekt anlegen
// erase = Projekt löschen
// modify = Projekt ändern
// postnews = News anlegen
// erasenews = News löschen

if ($action =="anmeldung1") {
// Registrierung ---------------------------------------------------------------------------------
	if(($nname !="") && ($vname !="") && ($schule !="") && ($styp !="") && ($email !="") && ($adresse !="") && ($lpasswort !="") && ($lpasswortcheck !="")){
	   if($lpasswort ==$lpasswortcheck) { 	
	   	$pwd_tmp = md5($lpasswort);
       		$tmp_code=substr(md5(uniqid(rand())),12);
       		$tmp_c=$tmp_code;
		$sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
		mysql_select_db($dbc_dbname, $sql_con);   
		$sql_q="INSERT INTO projektleiter (name,vname,schulname,schultyp,email,schuladr,password,verify) VALUES ('$nname','$vname','$schule','$styp','$email','$adresse','$pwd_tmp','$tmp_c')";	
		$sql_r=mysql_query($sql_q,$sql_con);
      		if ($sql_r)
        		{
			$sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
			mysql_select_db($dbc_dbname, $sql_con);   	
			$sql_q="SELECT id FROM projektleiter WHERE name = '$nname' AND vname = '$vname' AND schulname = '$schule' AND schultyp = '$styp' AND email = '$email'";
	       	  	$sql_r=mysql_query($sql_q,$sql_con);
			$sql_row= @mysql_fetch_array($sql_r);
			$tmp_id=$sql_row[0];
        		
        		mail("$email", "Anmeldung zur Einsteinjahr-Projektseite", 
        			"Hallo $vname $nname.\n\nHerzlich Willkommen auf der Einsteinjahr-Projektseite!\nDeine Anmeldung war erfolgreich.\n\n Bevor du dich einloggen kannst bestätige bitte deine Anmeldung durch klicken auf den folgenden Link:\n\n"
     				."<a href='$site_url/verify.php?id=$tmp_id&verify=$tmp_c'>$site_url/einsteinjahr/verify.php?id=$tmp_id&verify=$tmp_c</a>\n\nDu kannst Dich nun mit den folgenden Userdaten anmelden.\n\nLogin: $email\nPasswort: $lpasswort\n\n"
        			."\n\nViel Spass.",
     				"From: koenig@inf.fu-berlin.de\r\n"
    				."Reply-To: koenig@inf.fu-berlin.de\r\n"
    			);
    			$registriert=true;
         	} else {echo "<html>Fehler:\n<br>".mysql_errno($sql_con)."<br>\n".mysql_error($sql_con)."<br>"; }
	    } else {$modus=3; $anmeldefehler=2;}
	} else {$modus=3; $anmeldefehler=1;}
} else if(($action =="anmeldung2") && ($angemeldet ==true)) {
// Daten ändern ------------------------------------------------------------------------------------
	if(($nname!="") && ($vname!="") && ($schule!="") && ($styp!="") && ($adresse!="") && ($email!="") && ($lpasswort!="") && ($lpasswortcheck !="")){
	    if($lpasswort ==$lpasswortcheck) { 	
	    	$pwd_tmp = md5($lpasswort);	
		$sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
		mysql_select_db($dbc_dbname, $sql_con);   	
		$sql_q="SELECT USERSID FROM sessions WHERE SID ='$SID'";
		$sql_r=mysql_query($sql_q,$sql_con);
		$sql_row= @mysql_fetch_array($sql_r);
		$sql_tmpid=$sql_row[USERSID];

		$sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
		mysql_select_db($dbc_dbname, $sql_con);   
		$sql_q="UPDATE projektleiter SET name='$nname', vname='$vname', schulname='$schule', schultyp='$styp', email='$email', schuladr='$adresse', password='$pwd_tmp' WHERE id ='$sql_tmpid'";	
		$sql_r=mysql_query($sql_q,$sql_con);
      		if ($sql_r)
        		{
       		} else { echo "<html>Fehler:\n<br>".mysql_errno($sql_con)."<br>\n".mysql_error($sql_con)."<br>"; }
	    } else {$modus=3; $anmeldefehler=2;}
	} else {$modus=3; $anmeldefehler=1;}	
} else if(($action =="booking") && ($angemeldet ==true)) {
// Projekt anlegen -------------------------------------------------------------------------------
		$sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
		mysql_select_db($dbc_dbname, $sql_con);   	
	 	$sql_q="SELECT USERSID FROM sessions WHERE SID ='$SID'";
	   	$sql_r=mysql_query($sql_q,$sql_con);
	   	$sql_row= @mysql_fetch_array($sql_r);
	   	$sql_usrbooking=$sql_row[USERSID];
		
		$sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
		mysql_select_db($dbc_dbname, $sql_con);   
		$sql_q="INSERT INTO projekt (name,beschreibung,wettbewerb,themenbereich,teilnehmerzahl,klassenstufe,url,bildurl,leiter_id) VALUES ('$pname','$beschr','$wettbewerb','$themen','$teiln','$kstufe','$purl','$burl','$sql_usrbooking')";	
		echo $sql_q;
		$sql_r=mysql_query($sql_q,$sql_con);
      		if ($sql_r)
        		{
       		} else { echo "<html>Fehler:\n<br>".mysql_errno($sql_con)."<br>\n".mysql_error($sql_con)."<br>"; }
       		
       		if (!isset($projekt)) {
			$sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
			mysql_select_db($dbc_dbname, $sql_con);   	
			$sql_q="SELECT id FROM projekt WHERE name ='$pname'";
			$sql_r=mysql_query($sql_q,$sql_con);
			$sql_row= @mysql_fetch_array($sql_r);
			$projekt=$sql_row[id];
      		}
} else if(($action =="erase") && ($angemeldet ==true)) {	
// Projekt löschen -------------------------------------------------------------------------------
		$sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
		mysql_select_db($dbc_dbname, $sql_con);   	
	 	$sql_q="SELECT USERSID FROM sessions WHERE SID ='$SID'";
	   	$sql_r=mysql_query($sql_q,$sql_con);
	   	$sql_row= @mysql_fetch_array($sql_r);
		$sql_usrbooking=$sql_row[USERSID];
	
	   	$sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
		mysql_select_db("enistein", $sql_con);   	
		$sql_q="DELETE from projekt where id = '$projekt'";
		$sql_r=mysql_query($sql_q,$sql_con);
		if ($sql_r)
        	{
       		} else { echo "<html>Fehler:\n<br>".mysql_errno($sql_con)."<br>\n".mysql_error($sql_con)."<br>"; }				
} else if(($action =="modify") && ($angemeldet ==true)) {	
// Projekt verändern -------------------------------------------------------------------------------
		$sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
		mysql_select_db($dbc_dbname, $sql_con);   	
	 	$sql_q="SELECT USERSID FROM sessions WHERE SID ='$SID'";
	   	$sql_r=mysql_query($sql_q,$sql_con);
	   	$sql_row= @mysql_fetch_array($sql_r);
		$sql_usrbooking=$sql_row[USERSID];
	
	   	$sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
		mysql_select_db("enistein", $sql_con);   	
		$sql_q="UPDATE projekt SET name='$pname', beschreibung='$beschr', wettbewerb='$wettbewerb', themenbereich='$themen', teilnehmerzahl='$teiln', klassenstufe='$kstufe', url='$purl', bildurl='$burl' WHERE id ='$projekt'";	
		$sql_r=mysql_query($sql_q,$sql_con);
		if ($sql_r)
        	{
       		} else { echo "<html>Fehler:\n<br>".mysql_errno($sql_con)."<br>\n".mysql_error($sql_con)."<br>"; }		
} else if(($action =="postnews") && ($angemeldet ==true)) {	
// News anlegen -------------------------------------------------------------------------------
		$sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
		mysql_select_db($dbc_dbname, $sql_con);   	
	 	$sql_q="SELECT USERSID FROM sessions WHERE SID ='$SID'";
	   	$sql_r=mysql_query($sql_q,$sql_con);
	   	$sql_row= @mysql_fetch_array($sql_r);
		$sql_usrbooking=$sql_row[USERSID];
	
		$tmp_date=date("Y-m-d H:i:s");
	
		echo "TMPDATE";
	
	   	$sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
		mysql_select_db("enistein", $sql_con);   	
		$sql_q="INSERT into news (schlagzeile, newstext, datum, art) VALUES ('$schlag','$ntext','$tmp_date','$art')";
		echo $sql_q;	
		$sql_r=mysql_query($sql_q,$sql_con);
		if ($sql_r)
        	{
       		} else { echo "<html>Fehler:\n<br>".mysql_errno($sql_con)."<br>\n".mysql_error($sql_con)."<br>"; }	

		echo "TMPDATE2";

	   	$sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
		mysql_select_db("enistein", $sql_con);   	
		$sql_q="SELECT id FROM news WHERE schlagzeile='$schlag' AND newstext='$ntext' AND datum='$tmp_date' AND art='$art'";	
		$sql_r=mysql_query($sql_q,$sql_con);
	   	$sql_row= @mysql_fetch_array($sql_r);
		$news=$sql_row[0];
} else if(($action =="erasenews") && ($angemeldet ==true)) {	
// News löschen -------------------------------------------------------------------------------
		$sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
		mysql_select_db($dbc_dbname, $sql_con);   	
	 	$sql_q="SELECT USERSID FROM sessions WHERE SID ='$SID'";
	   	$sql_r=mysql_query($sql_q,$sql_con);
	   	$sql_row= @mysql_fetch_array($sql_r);
		$sql_usrbooking=$sql_row[USERSID];
	
	   	$sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
		mysql_select_db("enistein", $sql_con);   	
		$sql_q="DELETE from news where id = '$news'";
		$sql_r=mysql_query($sql_q,$sql_con);
		if ($sql_r)
        	{
       		} else { echo "<html>Fehler:\n<br>".mysql_errno($sql_con)."<br>\n".mysql_error($sql_con)."<br>"; }				
} 

}

// Hier beginnt nun das HTML ---------------------------------------------------------------------------------

?>

<html>

  <head>
    <title>FU-Berlin: Institut für Informatik</TITLE>
    <link rel="stylesheet" href=<? echo $style_position ?>>
  </head>

  <body>

        <p class=up>
	        <a href="http://www.fu-berlin.de/">Freie Universit&auml;t Berlin</a><br>
	        <a href="http://www.math.fu-berlin.de/">Fachbereich Mathematik und Informatik</a><br>
	</p>
 
<?


// das Menue -----------------------------------------------------------------------------------------
if ( $angemeldet ==false ) {
// Dieses Menue erscheint, wenn man nicht eingeloggt ist
echo "
	<div id=imenue>
		<p><a href=\"index.php\">Home</a></p>
		<form action=\"index.php\" METHOD=POST>
		<p>
		E-Mail:<input name=\"UID\" class=margininput>
		<br>
		Password:<input type=\"password\" name=\"PWD\" class=margininput>
		</p>
		<input type=\"submit\" value=\"Login\">
		</form>
		<br>
		<br>
		<p><a href=\"gebuehr.html\">Informationen</a></p>
		<p><a href=\"index.php\">Projekte </a></p>
		<p><a href=\"index.php?modus=3\">Anmelden </a></p>
		<p><a href=\"index.php?modus=2\">Passwort vergessen </a></p>
	</div> 
       ";
} else {        
           	$sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
		mysql_select_db($dbc_dbname, $sql_con);   	
	 	$sql_q="SELECT USERSID FROM sessions WHERE SID ='$SID'";
	   	$sql_r=mysql_query($sql_q,$sql_con);
	   	$sql_row= @mysql_fetch_array($sql_r);
	   	$sql_anmelder=$sql_row[USERSID];
		$sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
		mysql_select_db($dbc_dbname, $sql_con);   	
		$sql_q="SELECT id, name, vname FROM projektleiter WHERE id = '$sql_anmelder'";
         	$sql_r=mysql_query($sql_q,$sql_con);
		$sql_row= @mysql_fetch_array($sql_r);
		$sql_schid=$sql_row[0];
		$sql_schname=$sql_row[1];
		$sql_schvname=$sql_row[2];
	
// Diese Menue erscheint, wenn man eingeloggt ist
echo   "
        <div id=imenue>
		<font size=-1>$sql_schvname $sql_schname<br> (ID: $sql_schid)</font>
		<br>
		<br>
		<p><a href=\"index.php?SID=$SID&angemeldet=true\">Übersicht</a></p>
		<p><a href=\"index.php?SID=$SID&angemeldet=true\">Alle Projekte anzeigen</a></p>
		<p><a href=\"index.php?SID=$SID&modus=4&angemeldet=true\">Eigene Projekte anzeigen</a></p>
		<p><a href=\"index.php?SID=$SID&modus=5&angemeldet=true\">Projekt anlegen</a></p>
		<br>
		<p><a href=\"index.php?SID=$SID&modus=12&angemeldet=true\">News anzeigen</a></p>
		<p><a href=\"index.php?SID=$SID&modus=10&angemeldet=true\">News verfassen</a></p>
	";
// Hier folgen nun die untersten Menuepunkte, die immer im Menue sein sollen
echo	"
		<br>
		<p><a href=\"index.php?SID=$SID&modus=3&angemeldet=true\">Profil</a></p>
		<p><a href=\"index.php\">Logout</a></p>
       </div>
       ";
}   
?>

<?
// Hier folgt nun der Code zu den einzelnen Unterseiten (Modi)
// Je nachdem wie die Variable modus im Uebergabestring gesetzt ist wird die entsprechende Seite generiert
//
// modus | eigenschaft
// ~~~~~~~~~~~~~~~~~~~
//  kein | hauptseite
//   2	 | passwort vergessen
//   3	 | neu anmelden / profil bearbeiten
//   3a	 | bestätigung neu anmelden 
//   4	 | eigene projekte ansehen
//   5	 | projekt anlegen
//   6a	 | projekt ändern
//   7a	 | bestätigung projekt löschen
//   8	 | projekt anzeigen 
//   9	 | news anzeigen
//  10	 | news posten
//  11a	 | bestätigung news löschen
//  12	 | alle news anzeigen


if(!isset($modus)) {
// *************************************************************************************************************************	
// kein modus uebergeben == infopage -------------------------------------------------------------------------------
// *************************************************************************************************************************	

?>
	<h3>News</h3>

	<table border="0" cellspacing="0" cellpadding="0">
	
	<tr>
		<td>Datum</td>
		<td width=10>&nbsp;</td>
		<td>Schlagzeile</td>
		<td width=10>&nbsp;</td>
		<td>Art</td>
	</tr>
	<tr>
		<td colspan=5><hr></td>
	</tr>
	
	<? 
	   $sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
	   mysql_select_db($dbc_dbname, $sql_con);   	
	   $sql_q="SELECT USERSID FROM sessions WHERE SID ='$SID'";
	   $sql_r=mysql_query($sql_q,$sql_con);
	   $sql_row= @mysql_fetch_array($sql_r);
	   $sql_usrtmpid=$sql_row[USERSID];
	   $sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
   	   mysql_select_db($dbc_dbname, $sql_con);
	   $sql_q="SELECT id, datum, schlagzeile, art from news ORDER BY datum DESC";
	   $sql_r=mysql_query($sql_q,$sql_con);
       	   if ($sql_r) {
		   $menge = mysql_num_rows($sql_r);
		   if ($menge != 0) {
		   	$i = 0;
		   	while (($sql_row= @mysql_fetch_array($sql_r))&&($i<$schlag_anzahl)) {	
				$tmp_id = $sql_row[0];
				$tmp_datum = $sql_row[1];
				$tmp_schlag = $sql_row[2];
				$tmp_art = $sql_row[3];

				echo 	"
					<tr>
					<td valign=\"top\">$tmp_datum&nbsp;</td>
					<td>&nbsp;</td>
					<td valign=\"top\">
						<a href=\"index.php?SID=$SID&angemeldet=$angemeldet&modus=9&news=$tmp_id\">$tmp_schlag&nbsp;</a>
					</td>
					<td></td>
					<td>$tmp_art&nbsp;</td>
					</tr>
				";
				
				$i=$i+1;
		   	}
		   } else { 
		   	// noche keine News verfügbar
		   	echo "<tr><td colspan=5>Noch keine News verfügbar!</td></tr>"; 
		   }
	   } else {
		echo "Fehler:\n<br>".mysql_errno($sql_con)."<br>\n".mysql_error($sql_con)."<br>";
	   }

	?>
	
	</table>       
	
	<br>
	<br>
	<h3>Projekte</h3>
	<table border="0" cellspacing="0" cellpadding="0">

	<tr>
		<td>Datum</td>
		<td width=10>&nbsp;</td>
		<td>Titel</td>
		<td width=10>&nbsp;</td>
		<td>Sonstiges</td>
	</tr>
	<tr>
		<td colspan=5><hr></td>
	</tr>
	
	<? 
	   $sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
	   mysql_select_db($dbc_dbname, $sql_con);   	
	   $sql_q="SELECT USERSID FROM sessions WHERE SID ='$SID'";
	   $sql_r=mysql_query($sql_q,$sql_con);
	   $sql_row= @mysql_fetch_array($sql_r);
	   $sql_usrtmpid=$sql_row[USERSID];



	   $sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
   	   mysql_select_db($dbc_dbname, $sql_con);
	   $sql_q="SELECT id, name from projekt ORDER BY id";
	   $sql_r=mysql_query($sql_q,$sql_con);
       	   if ($sql_r) {
		   $menge = mysql_num_rows($sql_r);
		   if ($menge != 0) {
		   	while ($sql_row= @mysql_fetch_array($sql_r)) {	
			
				$tmp_id = $sql_row[0];
				$tmp_name = $sql_row[1];

				echo 	"<tr><td valign=\"top\" width=\"40\">$tmp_id&nbsp;</td><td>&nbsp;</td><td valign=\"top\"><a href=\"index.php?SID=$SID&angemeldet=$angemeldet&modus=8&projekt=$tmp_id&mybooked=true\">$tmp_name&nbsp;</td><td></td><td></td></tr>";
		   	}
		   } else { echo "<tr><td colspan=5>Noch kein Projekt verfügbar!</td></tr>"; }
	   } else {
			echo "Fehler:\n<br>".mysql_errno($sql_con)."<br>\n".mysql_error($sql_con)."<br>";
	   }

	?>
	
	</table>       

<?
}
if($modus =="2"){
// *************************************************************************************************************************	
// modus2 = pass vergessen ------------------------------------------------------------------------------------------------------
// *************************************************************************************************************************	
?>	
Hier passwort vergessen!!
<?
} 
if($modus =="3"){
// *************************************************************************************************************************	
// modus3 = neu anmelden / profil bearbeiten ------------------------------------------------------------------------------------
// *************************************************************************************************************************	
?>	
<?	
if ( $angemeldet ==false ) {
// *************************************************************************************************************************	
// neu anmelden -----------------------------------------------------------------------------------------------------------------
// *************************************************************************************************************************	
?>
	<h4>Anmeldung</h4>


	<form action="index.php" method=post>
	<input type=hidden name="SID" value="<? echo $SID ?>">
	<input type=hidden name="angemeldet" value="<? echo $angemeldet ?>">
	<input type=hidden name="action" value="anmeldung1">
	<input type=hidden name="modus" value="3a">
<?
if($anmeldefehler =="1") {
 	echo "Fehler: Bitte füllen Sie alle Felder aus!";
} else if ($anmeldefehler =="2") {
        echo "Fehler: Bitte versichern Sie sich das Sie das Passwort korrekt in beiden Passwortfeldern eingeben!";
}
?>

	<table border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>&nbsp;</td>
		<td width=10>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td><b>Name:</b></td>
		<td width=10>&nbsp;</td>
		<td><input size=70 name="nname"></td>
	</tr>
	<tr>
		<td><b>Vorname:</b></td>
		<td width=10>&nbsp;</td>
		<td><input size=70 name="vname"></td>
	</tr>
	<tr>
		<td><b>Name der Schule:</b></td>
		<td width=10>&nbsp;</td>
		<td><input size=70 name="schule"></td>
	</tr>
	<tr>
		<td><b>Schultyp:</b></td>
		<td width=10>&nbsp;</td>
		<td>
		    <select name="styp">
			<option value="Gesamtschule">Gesamtschule</option>
			<option value="Gymnasium">Gymnasium</option>
			<option value="Realschule">Realschule</option>
			<option value="Hauptschule">Hauptschule</option>
		    </select>
		</td>
	</tr>
	<tr>
		<td><b>E-mail-Adresse:</b></td>
		<td width=10>&nbsp;</td>
		<td><input size=50 name="email"></td>
	</tr>
	<tr>
		<td><b>Adresse der Schule: </b></td>
		<td>&nbsp;</td>
		<td><textarea cols=60 rows=6 wrap=virtual name="adresse"></textarea></td>
	</tr>
	<tr>
		<td><b>Passwort:</b></td>
		<td width=10>&nbsp;</td>
		<td><input size=20 type="password" name="lpasswort"></td>
	</tr>
	<tr>	
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td><nobr>Bitte geben Sie Ihr Passwort ein zweites mal ein!</nobr></td>
	</tr>
	<tr>
		<td><b>Passwort Check:</b></td>
		<td width=10>&nbsp;</td>
		<td><input size=20 name="lpasswortcheck" type="password" value="<? echo $sql_tmp8 ?>"></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td width=10>&nbsp;</td>
		<td align=right><input type=reset value="Reset"><input type=submit value="Anmelden"></td>
	</tr>
	
	</table>
	</form>
<?
} else {
// *************************************************************************************************************************	
// profil bearbeiten ------------------------------------------------------------------------------------------------------------
// *************************************************************************************************************************	

	$sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
	mysql_select_db($dbc_dbname, $sql_con);   	
	$sql_q="SELECT USERSID FROM sessions WHERE SID ='$SID'";
	$sql_r=mysql_query($sql_q,$sql_con);
	$sql_row= @mysql_fetch_array($sql_r);
	$sql_tmpid=$sql_row[USERSID];
	$sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
	mysql_select_db($dbc_dbname, $sql_con);   	
	$sql_q="SELECT name, vname, schulname, schultyp, email, schuladr, password FROM projektleiter WHERE id = '$sql_tmpid'";
	$sql_r=mysql_query($sql_q,$sql_con);	
	if ($sql_r) {
			$sql_row= mysql_fetch_row($sql_r);
			$sql_tmp1=$sql_row[0];
			$sql_tmp2=$sql_row[1];
			$sql_tmp3=$sql_row[2];
			$sql_tmp4=$sql_row[3];
			$sql_tmp5=$sql_row[4];
			$sql_tmp6=$sql_row[5];
			$sql_tmp7=$sql_row[6];
		}
	else {
			echo "Fehler:\n<br>".mysql_errno($sql_con)."<br>\n".mysql_error($sql_con)."<br>";
		}
?>

        <h3>Mein Profil</h3>

        <br>

	<form action="index.php" method=post>
	<input type=hidden name="SID" value="<? echo $SID ?>">
	<input type=hidden name="angemeldet" value="<? echo $angemeldet ?>">
	<input type=hidden name="action" value="anmeldung2">
	<input type=hidden name="modus" value="3">
	
<?
if($anmeldefehler =="1") {
 	echo "Fehler: Bitte füllen Sie alle Felder aus!";
} else if ($anmeldefehler =="2") {
        echo "Fehler: Bitte versichern Sie sich das Sie das Passwort korrekt in beiden Passwortfeldern eingeben!";
}
?>
	<table border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>&nbsp;</td>
		<td width=10>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td><b>ID</b></td>
		<td width=10>&nbsp;</td>
		<td><? echo $sql_tmpid ?></td>
	</tr>
	<tr>
		<td><b>Name:</b></td>
		<td width=10>&nbsp;</td>
		<td><input size=100 name="nname" value="<? echo $sql_tmp1 ?>"></td>
	</tr>
	<tr>
		<td><b>Vorname:</b></td>
		<td width=10>&nbsp;</td>
		<td><input size=100 name="vname" value="<? echo $sql_tmp2 ?>"></td>
	</tr>
	<tr>
		<td><b>E-mail-Adresse:</b></td>
		<td width=10>&nbsp;</td>
		<td><input size=100 name="email" value="<? echo $sql_tmp5 ?>"></td>
	</tr>
	<tr>
		<td><b>Name der Schule:</b></td>
		<td width=10>&nbsp;</td>
		<td><input size=100 name="schule" value="<? echo $sql_tmp3 ?>"></td>
	</tr>
	<tr>
		<td><b>Schultyp:</b></td>
		<td width=10>&nbsp;</td>
		<td><select name="styp">
	  	    <? if ($sql_tmp4=="Gesamtschule") {
	  	    ?>
			<option value="Gesamtschule" selected>Gesamtschule</option>
			<option value="Gymnasium">Gymnasium</option>
			<option value="Realschule">Realschule</option>
			<option value="Hauptschule">Hauptschule</option>
	  	    <? } else if ($sql_tmp4=="Gymnasium") {
	  	    ?>
			<option value="Gesamtschule">Gesamtschule</option>
			<option value="Gymnasium" selected>Gymnasium</option>
			<option value="Realschule">Realschule</option>
			<option value="Hauptschule">Hauptschule</option>
	  	    <? } else if ($sql_tmp4=="Realschule") {
	  	    ?>
			<option value="Gesamtschule">Gesamtschule</option>
			<option value="Gymnasium">Gymnasium</option>
			<option value="Realschule" selected>Realschule</option>
			<option value="Hauptschule">Hauptschule</option>
	  	    <? } else if ($sql_tmp4=="Hauptschule") {
	  	    ?>
			<option value="Gesamtschule">Gesamtschule</option>
			<option value="Gymnasium">Gymnasium</option>
			<option value="Realschule">Realschule</option>
			<option value="Hauptschule" selected>Hauptschule</option>
	  	    <? } 
	  	    ?>
		    </select>
		</td>
	</tr>
	<tr>
		<td><b>Adresse der Schule:</b></td>
		<td width=10>&nbsp;</td>
		<td><textarea cols=60 rows=6 wrap=virtual name="adresse"><? echo $sql_tmp6 ?></textarea>"></td>
	</tr>
	<tr>
		<td><b>Passwort:</b></td>
		<td width=10>&nbsp;</td>
		<td><input size=100 name="lpasswort" type="password" value="<? echo $sql_tmp7 ?>"></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td><nobr>Bitte geben Sie Ihr Passwort zu Überprüfung ein zweites mal ein!</nobr></td>
	</tr>
	<tr>
		<td><b>Passwort Check:</b></td>
		<td width=10>&nbsp;</td>
		<td><input size=100 name="lpasswortcheck" type="password"></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td width=10>&nbsp;</td>
		<td align=right><input type=reset value="Reset"><input type=submit value="Ändern"></td>
	</tr>
	
	</table>
	</form>
<?
}
?>
<?
} 
if(($modus =="3a") && ($registriert ==true)) {
// *************************************************************************************************************************	
// modus 3a = neu anmelden Bestätigung
// *************************************************************************************************************************	
?>

<b>Herzlich Willkommen!</b>
<br>
<br>
Du kannst Dich ab sofort mit Deinen Login-Daten anmelden.
Die Login-Daten wurden per mail an die angegebene E-Mail-Adresse (<? echo $email ?>) geschickt.

Viel Spass Wettbewerb.

<?
}
?>
<?
if(($modus =="4") && ( $angemeldet ==true)){
// *************************************************************************************************************************	
// modus4 = eigene projekte --------------------------------------------------------------------------------------------------
// *************************************************************************************************************************	
?>	
        <h3>Meine Projekte</h3>
 
        <br>

	<table border="0" cellspacing="0" cellpadding="0">

	<tr>
		<td>Nummer</td>
		<td width=10>&nbsp;</td>
		<td>Titel</td>
		<td width=10>&nbsp;</td>
		<td></td>
		<td width=10>&nbsp;</td>
		<td></td>
	</tr>
	<tr>
		<td colspan=7><hr></td>
	</tr>
	
	<? 
	   $sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
	   mysql_select_db($dbc_dbname, $sql_con);   	
	   $sql_q="SELECT USERSID FROM sessions WHERE SID ='$SID'";
	   $sql_r=mysql_query($sql_q,$sql_con);
	   $sql_row= @mysql_fetch_array($sql_r);
	   $sql_usrtmpid=$sql_row[USERSID];
	   $sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
   	   mysql_select_db($dbc_dbname, $sql_con);
	   $sql_q="SELECT id, name from projekt where leiter_id = '$sql_usrtmpid' ORDER BY id";
	   $sql_r=mysql_query($sql_q,$sql_con);
       	   if ($sql_r) {
		   $menge = mysql_num_rows($sql_r);
		   if ($menge != 0) {
		   	while ($sql_row= @mysql_fetch_array($sql_r)) {	
			
				$tmp_id = $sql_row[0];
				$tmp_name = $sql_row[1];
				$tmp_beschr = $sql_row[2];

				echo 	"
					<tr>
						<td valign=\"top\" width=\"40\">$tmp_id&nbsp;</td>
						<td>&nbsp;</td>
						<td valign=\"top\">
							<a href=\"index.php?SID=$SID&angemeldet=$angemeldet&modus=8&projekt=$tmp_id&mybooked=true\">$tmp_name&nbsp;</a>
						</td>
						<td>&nbsp;</td>
						<td>
							<form action=\"index.php\" method=post>
								<input type=hidden name=\"SID\" value=\"$SID\">
								<input type=hidden name=\"angemeldet\" value=\"$angemeldet\">
								<input type=hidden name=\"modus\" value=\"6a\">
								<input type=hidden name=\"projekt\" value=\"$tmp_id\">
								<input type=submit value=\"ändern\">
							</form>
						</td>
						<td>&nbsp;</td>
						<td>
							<form action=\"index.php\" method=post>
								<input type=hidden name=\"SID\" value=\"$SID\">
								<input type=hidden name=\"angemeldet\" value=\"$angemeldet\">
								<input type=hidden name=\"modus\" value=\"7a\">
								<input type=hidden name=\"projekt\" value=\"$tmp_id\">
								<input type=submit value=\"löschen\">
							</form>
						</td>
					</tr>
				";
		   	}
		   } else { 
		   	// wenn noch kein projekt vorhanden ist
		   	echo "<tr><td colspan=3>Noch kein Projekt angelegt!</td></tr>"; 
		   }
	   } else {
			echo "Fehler:\n<br>".mysql_errno($sql_con)."<br>\n".mysql_error($sql_con)."<br>";
	   }
	?>
	
	</table>       
<?
} 
?>				
<?
if(($modus =="5") && ( $angemeldet ==true)){
// *************************************************************************************************************************	
// modus5 = Projekt anlegen --------------------------------------------------------------------------------------------------
// *************************************************************************************************************************	
?>	
	
	<h4>Anlegen eines Projektes</h4>

	<form action="index.php" method=post>
	<input type=hidden name="SID" value="<? echo $SID ?>">
	<input type=hidden name="angemeldet" value="<? echo $angemeldet ?>">
	<input type=hidden name="action" value="booking">
	<input type=hidden name="modus" value="8">

	<table border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>&nbsp;</td>
		<td width=10>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td><b>Name:</b></td>
		<td width=10>&nbsp;</td>
		<td><input size=70 name="pname"></td>
	</tr>
	<tr>
		<td><b>Wettbewerb:</b></td>
		<td width=10>&nbsp;</td>
		<td>
		    <select name="wettbewerb">
			<option value="1">Wettbewerb 1</option>
			<option value="2">Wettbewerb 2</option>
			<option value="3">Wettbewerb 3</option>
		    </select>
		</td>
	</tr>
	<tr>
		<td><b>Themenbereich:</b></td>
		<td width=10>&nbsp;</td>
		<td>
		    <select name="themen">
			<option value="1">A</option>
			<option value="2">B</option>
			<option value="3">C</option>
		    </select>
		</td>
	</tr>
	<tr>
		<td><b>Klassenstufe:</b></td>
		<td width=10>&nbsp;</td>
		<td><input size=70 name="kstufe"></td>
	</tr>
	<tr>
		<td><b>Teilnehmerzahl:</b></td>
		<td width=10>&nbsp;</td>
		<td><input size=70 name="teiln"></td>
	</tr>
	<tr>
		<td><b>Homepage:</b></td>
		<td width=10>&nbsp;</td>
		<td><input size=50 name="purl"></td>
	</tr>
	<tr>
		<td><b>Bild-URL:</b></td>
		<td width=10>&nbsp;</td>
		<td><input size=50 name="burl"></td>
	</tr>
	<tr>
		<td><b>Beschreibung: </b></td>
		<td>&nbsp;</td>
		<td><textarea cols=60 rows=9 wrap=virtual name="beschr"></textarea></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td width=10>&nbsp;</td>
		<td align=right><input type=reset value="Reset"><input type=submit value="Projekt Anlegen"></td>
	</tr>
	
	</table>
	</form>


<?
} 
?>				
<?
if(($modus =="6a") && ( $angemeldet ==true)){
// *************************************************************************************************************************	
// modus6 = projekt ändern --------------------------------------------------------------------------------------------------
// *************************************************************************************************************************	
	
	$sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
	mysql_select_db($dbc_dbname, $sql_con);   	
	$sql_q="SELECT USERSID FROM sessions WHERE SID ='$SID'";
	$sql_r=mysql_query($sql_q,$sql_con);
	$sql_row= @mysql_fetch_array($sql_r);
	$sql_tmpid=$sql_row[USERSID];
	$sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
	mysql_select_db($dbc_dbname, $sql_con);   	
	$sql_q="SELECT name, beschreibung, themenbereich, teilnehmerzahl, klassenstufe, url, bildurl, wettbewerb FROM projekt WHERE id = '$projekt'";
	$sql_r=mysql_query($sql_q,$sql_con);	
	if ($sql_r) {
			$sql_row= mysql_fetch_row($sql_r);
			$sql_tmp1=$sql_row[0];
			$sql_tmp2=$sql_row[1];
			$sql_tmp3=$sql_row[2];
			$sql_tmp4=$sql_row[3];
			$sql_tmp5=$sql_row[4];
			$sql_tmp6=$sql_row[5];
			$sql_tmp7=$sql_row[6];
			$sql_tmp8=$sql_row[7];
		}
	else {
			echo "Fehler:\n<br>".mysql_errno($sql_con)."<br>\n".mysql_error($sql_con)."<br>";
		}
?>

        <h3>Projekt ändern</h3>

        <br>

	<form action="index.php" method=post>
	<input type=hidden name="SID" value="<? echo $SID ?>">
	<input type=hidden name="angemeldet" value="<? echo $angemeldet ?>">
	<input type=hidden name="action" value="modify">
	<input type=hidden name="modus" value="8">
	<input type=hidden name="projekt" value="<? echo $projekt ?>">	

	<table border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>&nbsp;</td>
		<td width=10>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td><b>Name:</b></td>
		<td width=10>&nbsp;</td>
		<td><input size=100 name="pname" value="<? echo $sql_tmp1 ?>"></td>
	</tr>
	<tr>
		<td><b>Wettbewerb:</b></td>
		<td width=10>&nbsp;</td>
		<td><select name="wettbewerb">
	  	    <? if ($sql_tmp8=="0") {
	  	    ?>
			<option value="0" selected>Kein Wettbewerb ausgewählt!</option>
			<option value="1">Wettbewerb 1</option>
			<option value="2">Wettbewerb 2</option>
			<option value="3">Wettbewerb 3</option>
	  	    <? } else if ($sql_tmp8=="1") {
	  	    ?>
			<option value="1" selected>Wettbewerb 1</option>
			<option value="2">Wettbewerb 2</option>
			<option value="3">Wettbewerb 3</option>
	  	    <? } else if ($sql_tmp8=="2") {
	  	    ?>
			<option value="1">Wettbewerb 1</option>
			<option value="2" selected>Wettbewerb 2</option>
			<option value="3">Wettbewerb 3</option>
	  	    <? } else if ($sql_tmp8=="3") {
	  	    ?>
			<option value="1">Wettbewerb 1</option>
			<option value="2">Wettbewerb 2</option>
			<option value="3" selected>Wettbewerb 3</option>
	  	    <? } 
	  	    ?>
		    </select></td>
	</tr>    
	<tr>
		<td><b>Themenbereich:</b></td>
		<td width=10>&nbsp;</td>
		<td><select name="themen">
	  	    <? if ($sql_tmp3=="1") {
	  	    ?>
			<option value="1" selected>A</option>
			<option value="2">B</option>
			<option value="3">C</option>
	  	    <? } else if ($sql_tmp3=="2") {
	  	    ?>
			<option value="1">A</option>
			<option value="2" selected>B</option>
			<option value="3">C</option>
	  	    <? } else if ($sql_tmp3=="3") {
	  	    ?>
			<option value="1">A</option>
			<option value="2">B</option>
			<option value="3" selected>C</option>
	  	    <? } 
	  	    ?>
		    </select></td>
	</tr>    
	<tr>
		<td><b>Teilnehmerzahl:</b></td>
		<td width=10>&nbsp;</td>
		<td><input size=100 name="teiln" value="<? echo $sql_tmp4 ?>"></td>
	</tr>
	<tr>
		<td><b>Klassenstufe:</b></td>
		<td width=10>&nbsp;</td>
		<td><input size=100 name="kstufe" value="<? echo $sql_tmp5 ?>"></td>
	</tr>
	<tr>
		<td><b>Homepage:</b></td>
		<td width=10>&nbsp;</td>
		<td><input size=100 name="purl" value="<? echo $sql_tmp6 ?>"></td>
	</tr>
	<tr>
		<td><b>Bild-URL:</b></td>
		<td width=10>&nbsp;</td>
		<td><input size=100 name="burl" value="<? echo $sql_tmp7 ?>"></td>
	</tr>
	<tr>
		<td><b>Beschreibung:</b></td>
		<td width=10>&nbsp;</td>
		<td><textarea cols=60 rows=9 wrap=virtual name="beschr"><? echo $sql_tmp2 ?></textarea></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td width=10>&nbsp;</td>
		<td align=right><input type=reset value="Reset"><input type=submit value="Ändern"></td>
	</tr>
	
	</table>
	</form>


<?
} 
?>
<?
if(($modus =="7a") && ( $angemeldet ==true)){
// *************************************************************************************************************************	
// modus7a = projekt löschen Bestätigung--------------------------------------------------------------------------------------------------
// *************************************************************************************************************************	
?>	

        <h3>Bestätigung zum löschen eines Projektes</h3>
        <br>
        
        Bitte bestätigen sie das löschen des folgenden Projektes:
        <br>
        <br>
        <?
        	$sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
		mysql_select_db($dbc_dbname, $sql_con);
		$sql_q="SELECT id, name, beschreibung, themenbereich, teilnehmerzahl, klassenstufe, url, bildurl FROM projekt WHERE id = $projekt";
		$sql_r=mysql_query($sql_q,$sql_con);
		if ($sql_r) {
			$sql_row= mysql_fetch_row($sql_r);
			$tmp_id = $sql_row[0];
			$tmp_name = $sql_row[1];
			$tmp_beschr = $sql_row[2];
			$tmp_themen = $sql_row[3];
			$tmp_teiln = $sql_row[4];
			$tmp_kstufe = $sql_row[5];
			$tmp_url = $sql_row[6];
			$tmp_bildurl = $sql_row[7];
		}
		else {
			
			echo "<br>Fehler:\n<br>".mysql_errno($sql_con)."<br>\n".mysql_error($sql_con)."<br>";
			echo "\n";
			echo $sql_q;
		}
	?>	

    	<h2>Projektinformationen</h2>
	<br>
	<h3></h3>
	<h2><font size=+1>#<? echo $tmp_id ?></font> <? echo $tmp_name ?></h2>

	<table border="0" cellspacing="0" cellpadding="0" width=90%>
		<tr>
			<td width=10>&nbsp;</td>
			<td><b>Themenbereich</b></td>
			<td>
			<?
			if ($tmp_themen =="1") {
				echo "A";
			} else if ($tmp_themen =="2") {
				echo "B";
			} else if ($tmp_themen =="3") {
				echo "C";
			}
			?>
			
			</td>
			<td width=10 rowspan=4>&nbsp;</td>
			<td rowspan=4><img src="<? echo $tmp_bildurl; ?>">BILD</td>
		</tr>
		<tr>
			<td width=10>&nbsp;</td>
			<td><b>Klassenstufe</b></td>
			<td><? echo $tmp_kstufe; ?></td>
		</tr>
		<tr>
			<td width=10>&nbsp;</td>
			<td><b>Teilnehmerzahl</b></td>
			<td><? echo $tmp_teiln; ?></td>
		</tr>
		<tr>
			<td width=10>&nbsp;</td>
			<td><b>Homepage</b></td>
			<td><a href="<? echo $tmp_url; ?>" target="_new"><? echo $tmp_url; ?></a></td>
		</tr>
		<tr>
			<td width=10>&nbsp;</td>
			<td colspan=4><b>Beschreibung</b></td>
		</tr>
		<tr>
			<td width=10>&nbsp;</td>
			<td colspan=4>
			<? 
				$tmp_beschr2=nl2br($tmp_beschr);
				echo $tmp_beschr2;
			?>
			</td>
		</tr>
	</table>
	
	<table border="0" cellspacing="0" cellpadding="0" width=90%>
		<tr>
			<td>
				<form action="index.php" method=post>
				<input type=hidden name="SID" value="<? echo $SID ?>">
				<input type=hidden name="angemeldet" value="<? echo $angemeldet ?>">
				<input type=hidden name="action" value="erase">
				<input type=hidden name="modus" value="4">
				<input type=hidden name="projekt" value="<? echo $tmp_id ?>">
				<input type="button" name="back" value="zurück" onClick="self.location.href='index.php?SID=<? echo $SID ?>&angemeldet=<? echo $angemeldet ?>&modus=4'">
			</td>
			<td>
				<input type=submit value="Projekt löschen">
				</form>
			</td>
		</tr>
	</table>
<?
} 
?>				

<?
if(($modus =="8")){
// *************************************************************************************************************************	
// modus8 = projekt anzeigen --------------------------------------------------------------------------------------------------
// *************************************************************************************************************************	

	$sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
	mysql_select_db($dbc_dbname, $sql_con);
	$sql_q="SELECT id, name, beschreibung, themenbereich, teilnehmerzahl, klassenstufe, url, bildurl FROM projekt WHERE id = $projekt";
	$sql_r=mysql_query($sql_q,$sql_con);
	if ($sql_r) {
		$sql_row= mysql_fetch_row($sql_r);
		$tmp_id = $sql_row[0];
		$tmp_name = $sql_row[1];
		$tmp_beschr = $sql_row[2];
		$tmp_themen = $sql_row[3];
		$tmp_teiln = $sql_row[4];
		$tmp_kstufe = $sql_row[5];
		$tmp_url = $sql_row[6];
		$tmp_bildurl = $sql_row[7];
	}
	else {
		
		echo "<br>Fehler:\n<br>".mysql_errno($sql_con)."<br>\n".mysql_error($sql_con)."<br>";
		echo "\n";
		echo $sql_q;
	}
?>	

    	<h2>Projektinformationen</h2>

	<br>

	<h2><font size=+1>#<? echo $tmp_id ?></font> <? echo $tmp_name ?></h2>

	<table border="0" cellspacing="0" cellpadding="0" width=90%>
		<tr>
			<td width=10>&nbsp;</td>
			<td><b>Themenbereich</b></td>
			<td>
			<?
			if ($tmp_themen =="1") {
				echo "A";
			} else if ($tmp_themen =="2") {
				echo "B";
			} else if ($tmp_themen =="3") {
				echo "C";
			}
			?>
			
			</td>
			<td width=10 rowspan=4>&nbsp;</td>
			<td rowspan=4><img src="<? echo $tmp_bildurl; ?>">BILD</td>
		</tr>
		<tr>
			<td width=10>&nbsp;</td>
			<td><b>Klassenstufe</b></td>
			<td><? echo $tmp_kstufe; ?></td>
		</tr>
		<tr>
			<td width=10>&nbsp;</td>
			<td><b>Teilnehmerzahl</b></td>
			<td><? echo $tmp_teiln; ?></td>
		</tr>
		<tr>
			<td width=10>&nbsp;</td>
			<td><b>Homepage</b></td>
			<td><a href="<? echo $tmp_url; ?>" target="_new"><? echo $tmp_url; ?></a></td>
		</tr>
		<tr>
			<td width=10>&nbsp;</td>
			<td colspan=4><b>Beschreibung</b></td>
		</tr>
		<tr>
			<td width=10>&nbsp;</td>
			<td colspan=4>
			<? 
				$tmp_beschr2=nl2br($tmp_beschr);
				echo $tmp_beschr2;
			?>
			</td>
		</tr>
	</table>

<?
} 
?>				
<?
if($modus =="9"){
// *************************************************************************************************************************	
// modus9 = News anzeigen --------------------------------------------------------------------------------------------------
// *************************************************************************************************************************	

	$sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
	mysql_select_db($dbc_dbname, $sql_con);
	$sql_q="SELECT id, schlagzeile, newstext, datum, art FROM news WHERE id = $news";
	$sql_r=mysql_query($sql_q,$sql_con);
	if ($sql_r) {
		$sql_row= mysql_fetch_row($sql_r);
		$tmp_id = $sql_row[0];
		$tmp_schlag = $sql_row[1];
		$tmp_text = $sql_row[2];
		$tmp_datum = $sql_row[3];
		$tmp_art = $sql_row[4];
	}
	else {
		echo "<br>Fehler:\n<br>".mysql_errno($sql_con)."<br>\n".mysql_error($sql_con)."<br>";
		echo "\n";
		echo $sql_q;
	}
	$sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
	mysql_select_db($dbc_dbname, $sql_con);
	$sql_q="SELECT pid_id FROM newszuordnung WHERE news_id = $news";
	$sql_r=mysql_query($sql_q,$sql_con);
	if ($sql_r) {
		$sql_row= mysql_fetch_row($sql_r);
		$tmp_proid = $sql_row[0];
	}
	else {
		
		echo "<br>Fehler:\n<br>".mysql_errno($sql_con)."<br>\n".mysql_error($sql_con)."<br>";
		echo "\n";
		echo $sql_q;
	}
	if ($tmp_proid!="") {
		$sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
		mysql_select_db($dbc_dbname, $sql_con);
		$sql_q="SELECT id, name FROM projekt WHERE id = $tmp_proid";
		$sql_r=mysql_query($sql_q,$sql_con);
		if ($sql_r) {
			$sql_row= mysql_fetch_row($sql_r);
			$tmp_pid = $sql_row[0];
			$tmp_pname = $sql_row[1];
		}
		else {
			echo "<br>Fehler:\n<br>".mysql_errno($sql_con)."<br>\n".mysql_error($sql_con)."<br>";
			echo "\n";
			echo $sql_q;
		}
	}
?>	

    	<h2>News</h2>
	<br>
	<h3></h3>

	<table border="0" cellspacing="0" cellpadding="0" width=90%>
		<tr>
			<td width=10><? echo $tmp_id ?>&nbsp;</td>
			<td rowspan=2><b><? echo $tmp_schlag ?></b></td>
			<td><? echo $tmp_art ?></td>
		</tr>
		<tr>
			<td width=10>&nbsp;</td>
			<td><? echo $tmp_datum ?></td>
		</tr>
		<tr>
			<td width=10>&nbsp;</td>
			<td colspan=2>
			<?
			if ($tmp_proid!="") {
			?>
			<a href="index.php?SID=<? echo $SID ?>&angemeldet=<? echo $angemeldet ?>&modus=8&projekt=<? echo $tmp_pid ?>"><? echo $tmp_pname ?></a>
			<?
			}
			?>
			</td>
		</tr>
		<tr>
			<td width=10>&nbsp;</td>
			<td colspan=2><? echo $tmp_text ?></td>
		</tr>
	</table>


<?
} 
?>				

<?
if(($modus =="10") && ( $angemeldet ==true)){
// *************************************************************************************************************************	
// modus10 = news posten --------------------------------------------------------------------------------------------------
// *************************************************************************************************************************	


	   $sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
	   mysql_select_db($dbc_dbname, $sql_con);   	
	   $sql_q="SELECT USERSID FROM sessions WHERE SID ='$SID'";
	   $sql_r=mysql_query($sql_q,$sql_con);
	   $sql_row= @mysql_fetch_array($sql_r);
	   $sql_usrtmpid=$sql_row[USERSID];
?>

	<h4>News posten</h4>
	
	<form action="index.php" method=post>
	<input type=hidden name="SID" value="<? echo $SID ?>">
	<input type=hidden name="angemeldet" value="<? echo $angemeldet ?>">
	<input type=hidden name="action" value="postnews">
	<input type=hidden name="modus" value="9">
	<?
	if ($sql_usrtmpid!=$adminID) {	
	?>
	<input type=hidden name="art" value="1">
	<?
	}
	?>	

	<table border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>&nbsp;</td>
		<td width=10>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td><b>Schlagzeile:</b></td>
		<td width=10>&nbsp;</td>
		<td><input size=70 name="schlag"></td>
	</tr>
	
	<?
	if ($sql_usrtmpid==$adminID) {
	// wenn der admin eingeloggt ist kann die Newsart eingestellt werden
	?>
	<tr>
		<td><b>Newsart:</b></td>
		<td width=10>&nbsp;</td>
		<td>
		    <select name="art">
			<option value="1">A</option>
			<option value="2">B</option>
			<option value="3">C</option>
		    </select>
		</td>
	</tr>
	<?
	}
	?>
	<tr>
		<td><b>Newstext:</b></td>
		<td width=10>&nbsp;</td>
		<td><textarea cols=60 rows=5 wrap=virtual name="ntext"></textarea></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td width=10>&nbsp;</td>
		<td align=right><input type=reset value="Reset"><input type=submit value="Abschicken"></td>
	</tr>
	</table>
	</form>


<?
} 
?>				
<?
if(($modus =="11a") && ( $angemeldet ==true)){
// *************************************************************************************************************************	
// modus 11a = news löschen Bestätigung--------------------------------------------------------------------------------------------------
// *************************************************************************************************************************	
?>	

        <h3>Bestätigung zum Löschen der News</h3>

        <br>
        
        Bitte bestätigen sie das löschen der folgenden News:
        <br>
        <br>
        <?
        	$sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
		mysql_select_db($dbc_dbname, $sql_con);
		$sql_q="SELECT id, schlagzeile, newstext, datum, art FROM news WHERE id = $news";
		$sql_r=mysql_query($sql_q,$sql_con);
		if ($sql_r) {
			$sql_row= mysql_fetch_row($sql_r);
			$tmp_id = $sql_row[0];
			$tmp_schlagzeile = $sql_row[1];
			$tmp_ntext = $sql_row[2];
			$tmp_date = $sql_row[3];
			$tmp_art = $sql_row[4];
		}
		else {
			
			echo "<br>Fehler:\n<br>".mysql_errno($sql_con)."<br>\n".mysql_error($sql_con)."<br>";
			echo "\n";
			echo $sql_q;
		}
	?>	

    	<h2>Newsinformationen</h2>
	<br>
	<h3><? echo $tmp_id ?></h3>
	<h3><? echo $tmp_schlagzeile ?></h3>

	<table border="0" cellspacing="0" cellpadding="0" width=90%>
		<tr>
			<td width=10>&nbsp;</td>
			<td><b>Newsart</b></td>
			<td><? echo $tmp_art ?></td>
		</tr>
		<tr>
			<td width=10>&nbsp;</td>
			<td><b>Datum</b></td>
			<td><? echo $tmp_date; ?></td>
		</tr>
		<tr>
			<td width=10>&nbsp;</td>
			<td><b>Newstext</b></td>
			<td><? echo $tmp_ntext; ?></td>
		</tr>
	</table>
	
	<table border="0" cellspacing="0" cellpadding="0" width=90%>
		<tr>
			<td>
				<form action="index.php" method=post>
				<input type=hidden name="SID" value="<? echo $SID ?>">
				<input type=hidden name="angemeldet" value="<? echo $angemeldet ?>">
				<input type=hidden name="action" value="erasenews">
				<input type=hidden name="modus" value="12">
				<input type=hidden name="news" value="<? echo $tmp_id ?>">
				<input type="button" name="back" value="zurück" onClick="self.location.href='index.php?SID=<? echo $SID ?>&angemeldet=<? echo $angemeldet ?>&modus=12'">
			</td>
			<td>
				<input type=submit value="News löschen">
				</form>
			</td>
		</tr>
	</table>
<?
} 
?>
<?
if(($modus =="12") && ( $angemeldet ==true)){
// *************************************************************************************************************************	
// modus12 = alle news anzeigen --------------------------------------------------------------------------------------------------
// *************************************************************************************************************************	

	   $sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
	   mysql_select_db($dbc_dbname, $sql_con);   	
	   $sql_q="SELECT USERSID FROM sessions WHERE SID ='$SID'";
	   $sql_r=mysql_query($sql_q,$sql_con);
	   $sql_row= @mysql_fetch_array($sql_r);
	   $sql_usrtmpid=$sql_row[USERSID];
?>
	<table border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>Datum</td>
		<td width=10>&nbsp;</td>
		<td>Schlagzeile</td>
		<td width=10>&nbsp;</td>
		<td>Art</td>
<?
	if ($sql_usrtmpid==$adminID) {
	// wenn der Admin angemeldet ist, werden noch die löschen-buttons angezeigt	
		echo "
			<td width=10>&nbsp;</td>
			<td>&nbsp;</td>
			</tr>
			<tr>
			<td colspan=7><hr></td>
			</tr>
		";
	 } else {
	   	echo "
			</tr>
			<tr>
			<td colspan=5><hr></td>
			</tr>
	   	";
	 }
?>
	
<? 
	   $sql_con = mysql_connect( $dbc_host, $dbc_uname, $dbc_pass);
   	   mysql_select_db($dbc_dbname, $sql_con);
	   $sql_q="SELECT id, datum, schlagzeile, art from news ORDER BY datum";
	   $sql_r=mysql_query($sql_q,$sql_con);
       	   if ($sql_r) {
		   $menge = mysql_num_rows($sql_r);
		   if ($menge != 0) {
		   	while ($sql_row= @mysql_fetch_array($sql_r)) {	
				$tmp_id = $sql_row[0];
				$tmp_datum = $sql_row[1];
				$tmp_schlag = $sql_row[2];
				$tmp_art = $sql_row[3];
		   	        if ($sql_usrtmpid==$adminID) {
				// wenn der Admin angemeldet ist ...
					echo 	"
						<tr>
							<td valign=\"top\">$tmp_datum&nbsp;</td>
							<td>&nbsp;</td>
							<td valign=\"top\"><a href=\"index.php?SID=$SID&angemeldet=$angemeldet&modus=9&news=$tmp_id\">$tmp_schlag&nbsp;</a></td>
							<td></td>
							<td>$tmp_art&nbsp;</td>
							<td></td>
							<td>
								<form action=\"index.php\" method=post>
						       			<input type=hidden name=\"SID\" value=\"$SID\">
						       			<input type=hidden name=\"angemeldet\" value=\"$angemeldet\">
						       			<input type=hidden name=\"modus\" value=\"11a\">
						       			<input type=hidden name=\"news\" value=\"$tmp_id\">
						       			<input type=submit value=\"löschen\">
						   		</form>
							</td>
						</tr>
					";
				} else {
				// sonnstige anzeige
					echo 	"
						<tr>
							<td valign=\"top\" width=\"40\">$tmp_datum&nbsp;</td>
							<td>&nbsp;</td>
							<td valign=\"top\"><a href=\"index.php?SID=$SID&angemeldet=$angemeldet&modus=9&news=$tmp_id\">$tmp_schlag&nbsp;</a></td>
							<td></td>
							<td>$tmp_art&nbsp;</td>
						</tr>
					";
				}
		   	}
		   } else { 
		   	// wenn noch keine News verfügbar
		   	echo "<tr><td colspan=5>Noch keine News verfügbar!</td></tr>"; 
		   }
	   } else {
			echo "Fehler:\n<br>".mysql_errno($sql_con)."<br>\n".mysql_error($sql_con)."<br>";
	   }
?>
	</table>       
<?
} 

// *************************************************************************************************************************	
// Nun folgt nur noch das Ende der Seite
// *************************************************************************************************************************	
?>				

        <div id=footer>
          <address><font color=#000000>&nbsp;e-mail&nbsp;to: </font><a href="mailto:koenig@inf.fu-berlin.de"><font color=#000000>koenig@inf.fu-berlin.de</a></font></address>

        </div>


  </body>
</html>
