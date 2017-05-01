package civ.world;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Message {

	ServerSocket serverSocket = new ServerSocket(port);    //Serversocket mit bestimmter Port-Nummer erstellen
	 while(true) {
	   Socket clientSocket = serverSocket.accept();         //auf Anfragen warten
	   InputStream input   = clientSocket.getInputStream(); //InputStream-Objekt öffnen
	   byte[] data         = new byte[1024];                //Datenpuffer deklarieren (anlegen)
	   int numBytes        = 0;                             //Variable für Anzahl der tatsächlich gelesenen Bytes
	   numBytes            = input.read(data);              //Daten lesen
	   /*** gelesene Daten verarbeiten ***/
	   clientSocket.close();                                //Verbindung schließen
	 }
	
}
