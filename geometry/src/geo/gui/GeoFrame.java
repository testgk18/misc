package geo.gui;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import geo.IShape;

public class GeoFrame extends JFrame{

	public GeoFrame() {
		super.setBounds(100, 100, 600, 400);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setContentPane(new GeoPanel());
	}
	
	public void addShape(IShape shape) {
		((GeoPanel) getContentPane()).addShape(shape);
	}

}
