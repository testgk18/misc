package geo;

import geo.gui.GeoFrame;

public class Main {

	public static void main(String[] args) {

		// create Frame and add shapes
		GeoFrame frame = new GeoFrame();

		// add shapes
		frame.addShape(new Rectangle(0, 0, 100, 150));
		frame.addShape(new Rectangle(50, 50, 150, 100));
		frame.addShape(new Circle(new Point(150, 150), 75));
		frame.setVisible(true);
	}

	public static void mainOld(String[] args) {
		if (args.length < 2) {
			System.out.println("Leere Eingabe. Bitte zwei Zahlen eingeben");
			System.exit(0);
		}
		int a = Integer.parseInt(args[0]);
		int b = Integer.parseInt(args[1]);
		if (a < 0 || b < 0) {
			System.out.println("Bitte keine negativen Zahlen eingeben");
			System.exit(0);
		}
		Rectangle r = new Rectangle(0, 0, a, b);
		System.out.println("Das Rechteck hat einen Flaecheinhalt von: " + r.calculateArea());
	}
}
