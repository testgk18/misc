package civ.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ServerThread extends Thread {
	private Socket s;

	public ServerThread(Socket s) {
		this.s = s;
	}

	public void run() {
		try {
			// lesen
			BufferedReader in = new BufferedReader(new InputStreamReader(
					s.getInputStream()));
			String text = in.readLine();
			// schreiben
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					s.getOutputStream()));
			out.write(text.toUpperCase());
			out.newLine();
			out.flush();
			// aufräumen
			out.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public enum ProcessingPhase {
		UNITS, CITIES, GLOBAL
	}

}
