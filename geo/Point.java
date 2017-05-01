package geo;

import java.awt.Graphics;

public class Point implements IShape {

	public int x = -1;
	public int y = -1;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Point)) {
			return false;
		}
		Point p = (Point) obj;
		return x == p.x && y == p.y;
	}

	@Override
	public Point getCenter() {
		return this;
	}

	public double distance(Point p) {
		return Math.sqrt((x - p.x) * (x - p.x) + (y - p.y) * (y - p.y));
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

	@Override
	public void draw(Graphics g) {
		g.drawLine(x, y, x, y);
	}

	@Override
	public boolean contains(IShape shape) {
		return equals(shape);
	}

	@Override
	public void setCenter(int centerX, int centerY) {
		this.x = centerX;
		this.y = centerY;
	}

}