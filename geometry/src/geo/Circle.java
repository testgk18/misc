package geo;

import java.awt.Graphics;

public class Circle extends Shape2D {

	private Point center;
	private int radius;

	public Circle(Point center, int r) {
		this.center = center;
		this.radius = r;
	}

	@Override
	public double calculateArea() {
		return Math.PI * radius * radius;
	}

	@Override
	public double getPerimeter() {
		return Math.PI * 2 * radius;
	}

	@Override
	public Point getCenter() {
		return center;
	}

	public int getRadius() {
		return radius;
	}

	@Override
	public void draw(Graphics g) {
		g.drawOval(center.x - radius, center.y - radius, radius * 2, radius * 2);
	}

	@Override
	public boolean contains(IShape shape) {
		if (shape instanceof Point) {
			Point p = (Point) shape;
			return p.distance(center) < radius;
		}
		else if (shape instanceof Rectangle) {
			Rectangle r = (Rectangle) shape;
			for (Point p : r.getVertices()) {
				if (!contains(p)) {
					return false;
				}
			}
			return true;
		}
		else if (shape instanceof Circle) {
			Circle c = (Circle) shape;
			return center.distance(c.center) + c.radius< radius;
		}
		// unknown subclass of shape
		return false;
	}
	
	@Override
	public void setCenter(int centerX, int centerY) {
		center = new Point(centerX, centerY);
	}
}