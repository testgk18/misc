import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Container;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Buchstabensuppe zeigt ein Textfeld und einen Schaltfläche, mit der man den
 * Versuppungsalgorithmus starten kann. Dieser Algorithmus vertauscht die 
 * inneren Buchstaben einzelner Wörter. Der erstaunliche Effekt ist, dass
 * der Text trotz der verdrehten Wörter noch lesbar bleibt.
 * @author till zoppke (kinnla at kinnla punkt de)
 */
public class Buchstabensuppe extends Applet {

	public static String text =
		"Gemäß einer Studie einer englischen Universität ist es nicht wichtig, \n"
			+ "in welcher Reihenfolge die Buchstaben in einem Wort sind. Das einzige, \n"
			+ "was wichtig ist, ist dass der erste und der letzte Buchstabe an der\n"
			+ "richtigen Position sind. Der Rest kann ein totaler Blödsinn sein, \n"
			+ "trotzdem kann man ihn ohne Probleme lesen. Das ist so, weil wir nicht\n"
			+ "jeden Buchstaben einzeln lesen, sondern das Wort als gesamtes.";

	// Indikator, ob das Programm als applet oder als Application gestartet 
	// wurde.wird von der main-methode auf false gesetzt.
	public static boolean isApplet = true;

	// grafische Komponenten
	private TextArea area = new TextArea(text);
	private Button button = new Button("Suppe!");

	/**
	 * init methode. Wird vom Browser aufgerufen oder von der main-methode.
	 */
	public void init() {
		// der Container ist entweder das Applet oder der Frame
		Container container = this;
		if (!isApplet) {
			// falls das Programm als application gestartet wurde, brauchen wir
			// ein Fenster, welches die grafischen Komponenten anzeigt
			Frame frame = new Frame("Buchstabensuppe");
			frame.setSize(400, 300);
			frame.setVisible(true);
			// der listener wird gebraucht, damit sich das Fenster durch
			// Mausklick schließen lässt.
			frame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});
			container = frame;
		}
		// actionlistener für den button
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				versuppen();
			}
		});
		// Füge die beiden Koomponenten hinzu. Es genügt ein Borderlayout.
		container.setLayout(new BorderLayout());
		container.add(area, BorderLayout.CENTER);
		container.add(button, BorderLayout.SOUTH);
	}

	/**
	 * Versuppungsalgorithmus. Nimmt den Text aus der TextArea, verdreht ihn
	 * und schreibt ihn wieder hinein. 
	 */
	public void versuppen() {
		char[] words = area.getText().toCharArray();
		// linke und rechte Grenze beim Parsen der Wörter. Der linke Zeiger 
		// zeigt auf den anfang eines Wortes, der rechte auf das Ende 
		int l = -1;
		int r = -1;
		// Äußere Schleife. Wenn der linke Zeiger aus dem Feld hinausgewandert
		// ist, haben wir alle Wörter verdreht.
		while (l < words.length) {
			// Innere SChleife (nur eine Zeile). Hier läuft der rechte Zeiger
			// bis ans Ende der Wortes.
			while (++r < words.length && Character.isLetter(words[r]));
			if (r - l == 5) {
				// falls das Wort genau vier Buchstaben hat, tausche die
				// beiden inneren
				char c = words[l + 2];
				words[l + 2] = words[l + 3];
				words[l + 3] = c;
			}
			else if (r - l > 5) {
				// Vertausche zwei zufällig ausgewählte Buchstaben
				for (int i = 0; i < (r - l - 3) * 5; ++i) {
					int z1 = (int) Math.floor(Math.random() * (r - l - 3));
					int z2 = (int) Math.floor(Math.random() * (r - l - 3));
					char c = words[l + z1 + 2];
					words[l + z1 + 2] = words[l + z2 + 2];
					words[l + z2 + 2] = c;
				}
			}
			// setze linken Zeiger ans Ende des Wortes
			l = r;
		}
		// Alles vertauscht. Schreibe Text zurück in das Textfeld
		area.setText(new String(words));
	}

	/**
	 * Main methode als Einstiegspunkt für die Applikation.
	 * @param args Wird nicht gebraucht.
	 */
	public static void main(String[] args) {
		isApplet = false;
		Buchstabensuppe suppe = new Buchstabensuppe();
		suppe.init();
	}
}
