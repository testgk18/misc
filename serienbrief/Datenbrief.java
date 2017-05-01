package kuzsd;
import java.io.*;
import java.util.Vector;
import java.util.StringTokenizer;
import java.text.NumberFormat;

class Datenbrief {
	
	// declare constants
	static final String DELIMITER = ";";
	static final String SHEET_HEADER = "Kwan Um Zen Schule Deutschland -- Datenbrief Mai 2011\n"
	+ "\n"
	+ "Liebes Mitglied!\n"
	+ "\n"
	+ "Im folgenden findest Du einen Auszug aus der Mitgliedsdatenbank mit allen\n"
	+ "personenbezogenen Daten, die Kwan Um Zen Schule über Dich gespeichert hat.\n"
	+ "Könntest Du Dir die Daten bitte anschauen und überprüfen?\n"
	+ "Bei fehlenden Daten, oder wenn etwas falsch ist, gib uns bitte bescheid!\n"
	+ "Deine Änderungen nehmen wir gerne per email entgegen: buero@kwanumzen.de\n"
	+ "Oder trage Deine Korrekturen in dieses Blatt ein und schicke es an uns zurück.\n"
	+ "\n"
	+ "Vielen Danke für Deine Mithilfe!\n"
	+ "Euer Vorstand (Arne, Heike, Till)\n"
	+ "------------------------------------------------------------------------------\n"
	
	;
	
	public static void main(String[] args) {
		
		// declare local variables
		String line = ""; 
		String[] attributeNames;
		Vector v = new Vector();
		int memberCounter = 0;
		File memberFile;
		StringTokenizer tt;
		String s;
		
		// check for minimum argument length
		if (args.length == 0) {
			System.out.println("usage: Datenbrief <CSV file name>");
			System.exit(0);
		}
		
		// open file
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(args[0]));
		}
		catch (FileNotFoundException e) {
			System.out.println("File not found");
			System.exit(0);
		}
		
		// parse file
		try {
			// read header line, containing the attribute names
			line = in.readLine();
			if (line == null) {
				System.out.println("File is empty");
				System.exit(0);
			}
			tt = new StringTokenizer(line, DELIMITER);
			attributeNames = new String[tt.countTokens()];
			for (int i=0; i<attributeNames.length; ++i) {
				attributeNames[i] = tt.nextToken();
			}
			
			
			// init number format
			NumberFormat nf=NumberFormat.getInstance(); 
			nf.setMinimumIntegerDigits(3);
			nf.setMaximumIntegerDigits(3);
			
			// iterate on the members
			while ((line = in.readLine()) != null) {
	
				// create output file
				memberFile = new File ("member" + nf.format(memberCounter++) + ".txt");
				if (memberFile.exists()) {
					System.out.println("Output File already exists. Please delete old files.");
					System.exit(0);
				}
				BufferedWriter out = new BufferedWriter(new FileWriter(memberFile));
				
				// write header
				out.write(SHEET_HEADER + "\n\n");
			
				// iterate on attributes
				tt = new StringTokenizer(line, DELIMITER, true);
				for (int i=0; i<attributeNames.length; ++i) {					
					out.write(attributeNames[i] + ":\t");
					if (tt.hasMoreTokens() && !(s = tt.nextToken()).equals(DELIMITER)) {
						out.write(s);
						if (tt.hasMoreTokens()) {
							tt.nextToken();
						}
					}
					out.write("\n");
				}
				out.flush();
				out.close();
			}
				
		}
		catch (IOException e) {
			System.out.println("Error while reading file");
			System.exit(0);
		}
				
				
	}
}
