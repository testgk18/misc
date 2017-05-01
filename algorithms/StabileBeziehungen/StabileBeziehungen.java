import java.util.Vector;
import java.util.Hashtable;
import java.util.Arrays;
import java.awt.Dimension;

/*
Der Algorithmus im Pseudocode:
--------------------------------------------------------------------------------
1. Initialisiere Prioritätstabellen. 0 ist höchste Zuneigung.
2. Berechne alle möglichen stabilen Paare, sortiert nach Männern
   Bedingung für ein stabiles Paar ist, dass mindestens eine
   Priorität null ist.
3. Initialisiere alle Frauen mit "frei".
4. Setze Mann auf 0.
5. Rekursion:
   Falls Mann >= Dimension, return erfolgreich.
   Andernfalls wähle das erste stabile Paar für diesen Mann (existiert immer).
6. Falls die entsprechende Frau frei, markiere sie als unfrei und setze
   Mann = Mann + 1. Rufe die Rekursion erneut auf.
7. Beim Rückkehr der Rekursion prüfe, ob der Aufruf erfolgreich war.
   Falls ja, hänge das aktuelle Paar an die Rückgabe an und return erfolgreich.
   Falls nein, markiere die aktuelle Frau als unfrei.
8. Prüfe, ob es noch ein weiteres stabiles Paar für diesen Mann gibt.
   Falls mindestens ein Paar vorhanden, wähle das nächste und mache weiter mit 6.
   Falls nicht vorhanden, return nicht erfolgreich.
9. Ausgabe RückgabeString mit möglichen Paaren oder Nachricht: "nicht erfolgreich".


Anmerkung: Der Algorithmus lässt sich noch optimieren, indem man die Männer nach
Anzahl ihrer stabilen Bindungsmöglichkeiten sortiert. Beim Rekursiven Aufruf nimmt
man die Männer mit wenigen Bindungsmöglichkeiten als erste, die mit vielen als letzte.

*/

public class StabileBeziehungen {

        private int dimension; // Anzahl Frauen und Anzahl Männer
	private int[][] frauen; // Prioritätstabelle. 0 = höchste Zuneigung.
	private int[][] maenner; // Prioritätstabelle. 0 = höchste Zuneigung.
	private int[][] partners; // mögliche stabile Partner, vom Mann aus gesehen;
	private boolean[] freieFrauen; // für rekursion: Frauen, die noch zu haben sind.

	// Konstruktor
	public StabileBeziehungen (int dimension) {
	  	this.dimension = dimension;
	}

	// Initialisierung des Objektes
	public void init() {
	  this.frauen = initGraph(this.dimension);
	  this.maenner = initGraph(this.dimension);
	  this.computePartners();
	  this.freieFrauen = new boolean[this.dimension];
	}

	// initialisiert einen Graphen mit Zufallswerten von 0 <= i < dimension.
	private static int[][] initGraph(int dimension) {

		// initialisiere Graphen.
		Vector values = new Vector();
		int[][] retour = new int[dimension][dimension];
		for (int i=0; i<dimension; ++i) {

			// initialisiere Vector mit Werten
			for (int j=0; j<dimension; ++j) {
				values.add(new Integer(j));
			}

			// nehme zufällig nach und nach einen Wert aus dem Vector
			for (int j=0; j<dimension; ++j) {
				int location = (int) Math.floor(Math.random() * values.size());
				retour[i][j] = ((Integer) values.get(location)).intValue();
				values.removeElementAt(location);
			}
		}
		return retour;
	}

	// gibt zurück, ob eine Beziehung stabil ist
	// notwendige und hinreichende VBedingung für eine stabile Beziehung ist,
	// dass einer der beiden Partner den anderen mit höchster Priorität (0) liebt.
	private boolean istStabil(int mann, int frau) {
	  return this.maenner[mann][frau] * this.frauen[frau][mann] == 0;
	}

	// Kopf der rekursiven Berechnung von stabilen Paaren
	private Dimension[] getPairs() {
	  Arrays.fill(this.freieFrauen, true);
	  Vector result = this.recursePairs(0);

	  // kein Ergebnis gefunden -- gebe null zurück
	  if (result == null)
	     return null;

	  // falls Ergebnis, konvertiere Vector in Array.
	  Dimension[] retour = new Dimension[this.dimension];
	  for (int i=0; i<result.size(); ++i) {
	      retour[i] = (Dimension) result.get(i);
	  }
	  return retour;
	}

	// rekursive Berechnung von stabilen Paaren
	private Vector recursePairs(int mann) {

	  // prüfe, ob Alle Paare gefunden
	  if (mann == this.dimension)
	     return new Vector();

	  // andernfalls bilde mögliche Paare mit diesem Mann und gehe zum nächsten
	  for (int i=0; this.partners[mann][i] >= 0; ++i) {
	    if (this.freieFrauen[this.partners[mann][i]]) {
	      this.freieFrauen[this.partners[mann][i]] = false;
	      Vector v = this.recursePairs(mann+1);

	      // falls ein positives Ergebnis aus diesem Zweig resultierte,
	      // füge aktuelles Paar an und returniere.
	      if (v != null) {
		v.addElement(new Dimension(mann, this.partners[mann][i]));
		return v;
	      }
	      this.freieFrauen[this.partners[mann][i]] = true;
	    }
	  }
	  // falls kein Ergebnis aus diesem Zwei, return null;
	  return null;
	}

	// berechnet alle möglichen stabilen Partner sortiert nach Männern
	private void computePartners() {
	  this.partners = new int[this.dimension][this.dimension];
	  for (int i=0; i<this.dimension; ++i) {
	    int counter = 0;

	    // suche nach stabilen Paaren und übernehme ins Array
	    for (int j=0; j<this.dimension; ++j) {
	      if (istStabil(i, j))
	        this.partners[i][counter++] = j;
	    }

	    // fülle Array mit Negativeinträgen
	    for (int j=counter; j<this.dimension; ++j) {
	      this.partners[i][j] = -1;
	    }
	  }
	}

	// Ausgabefunktion für die Tabellen
	private static String graphToString(int[][] graph) {
		String retour = "";
		for (int i=0; i<graph.length; ++i) {
			for (int j=0; j<graph.length; ++j) {
				retour = retour + graph[i][j] + "\t";
			}
			retour += "\n";
		}
		return retour;
	}

	// ausgabe gefundene paare
	private static String PairsToString(Dimension[] pairs) {
	  String s = "";
	  for (int i=0; i<pairs.length; ++i) {
	    s = s + "(" + pairs[i].width + ", " + pairs[i].height + "); ";
	  }
	  return s;
	}

	public static void main(String args[]) {

	        // falls dimension nicht angegeben, gib Nachricht und beende das Programm.
                if (args.length != 1) {
                  System.out.println("usage: java StabileBeziehungen <dimension>");
                  System.exit(0);
                }

		int dimension = Integer.parseInt(args[0]);

		// lege neue Instanz von StabileBeziehungen an.
		StabileBeziehungen st = new StabileBeziehungen (dimension);
		st.init();

		// Ausgabe der beiden Affinitätstabellen
		System.out.println("Maenner: ");
		System.out.println(graphToString(st.maenner));
		System.out.println("Frauen: ");
		System.out.println(graphToString(st.frauen));

		// Berechnung und Ausgabe der stabilen Paare
		Dimension[] d = st.getPairs();
		if (d == null) {
		  System.out.println("leider keine Stabilen Beziehungen gefunden.");
		}
		else {
		  System.out.println("Stabile Beziehungen: " + PairsToString(d));
		}
	}

}