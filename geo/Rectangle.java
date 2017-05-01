package geo;

import java.awt.Graphics;

public class Rectangle extends Shape2D {

	private int x;
	private int y;
	private int width;
	private int height;

	public Rectangle(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public Rectangle(Point p1, Point p2) {
		this.x = Math.min(p1.x, p2.x);
		this.y = Math.min(p2.y, p2.y);
		this.width = Math.abs(p1.x = p2.x);
		this.height = Math.abs(p1.y = p2.y);
	}

	@Override
	public Point getCenter() {
		return new Point(x + width / 2, y + height / 2);
	}

	@Override
	public void draw(Graphics g) {
		g.drawRect(x, y, width, height);
	}

	@Override
	public double calculateArea() {
		return width * height;
	}

	@Override
	public double getPerimeter() {
		return width * 2 + height * 2;
	}

	public Point[] getVertices() {
		return new Point[]{new Point(x, y), new Point(x + width, y), new Point(x, y + height),
				new Point(x + width, y + width)};
	}

	@Override
	public String toString() {
		return "Rectangle: " + x + ", " + height + ", " + width + ", " + height;
	}

	@Override
	public boolean contains(IShape shape) {
		if (shape instanceof Point) {
			Point p = (Point) shape;
			return x <= p.x && x + width >= p.x && y <= p.y && y + height >= p.y;
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
			Point center = c.getCenter();
			int r = c.getRadius();
			return x <= center.x - r && x >= center.x + r && y <= center.y - r && y >= center.y + r;
		}
		// unknown subclass of shape
		return false;
	}

	@Override
	public void setCenter(int centerX, int centerY) {
		x = centerX - width / 2;
		y = centerY - height / 2;
	}

}
