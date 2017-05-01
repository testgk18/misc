package geo;

import java.awt.Graphics;

public interface IShape {

	public Point getCenter();
	public void draw(Graphics g);
	public boolean contains(IShape shape);
	public void setCenter(int centerX, int centerY);
}
