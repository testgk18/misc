package proinf.geo.gui;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import proinf.geo.IShape;
import proinf.geo.Point;

public class GeoPanel extends JPanel {

	Point pressPoint = null;
	IShape dragShape = null;
	Point dragShapeCenter = null;
	List<IShape> shapes = new ArrayList<>();

	public GeoPanel() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// check, if the user clicked into a shape
				Point p = new Point(e.getX(),e.getY());
				for (IShape shape : shapes) {
					if (shape.contains(p)) {
						pressPoint = p;
						dragShape = shape;
						dragShapeCenter = shape.getCenter();
						break;
					}
				}
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				pressPoint = null;
				dragShape = null;
				dragShapeCenter = null;
			}
		});
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int centerX = dragShapeCenter.x + e.getX() - pressPoint.x;
				int centerY = dragShapeCenter.y + e.getY() - pressPoint.y;
				dragShape.setCenter(centerX, centerY);
				repaint();
			}
		});
	}

	public GeoPanel(List<IShape> shapes) {
		this.shapes = shapes;
	}

	@Override
	public void paint(Graphics g) {
		for (IShape shape : shapes) {
			shape.draw(g);
		}
	}

	public void addShape(IShape shape) {
		shapes.add(shape);
		repaint();
	}
}
