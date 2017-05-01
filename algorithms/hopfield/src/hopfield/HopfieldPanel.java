package hopfield;

import javax.swing.*;
import java.awt.*;

public class HopfieldPanel extends JPanel implements HopfieldListener {

	// colors
	private Color whiteColor = Color.white;
	private Color blackColor = Color.gray;
	private Color borderColor = new Color(150, 50, 50);

	// reference to the hopfield network to display
	private QueensHopfield hopfield;
	private Starter starter;

	// size of the hopfield
	private int hopfieldSize;

	// size of one field
	private int fieldSize;

	// //////////////////////// contructor and init //////////////////////////

	public HopfieldPanel(Starter starter) {
		this.starter = starter;
	}

	public void init() {
		this.hopfieldSize = this.hopfield.getSize();
		this.fieldSize = 400 / this.hopfieldSize;

		this.hopfield.addHopfieldListener(this);
		this.repaint();
	}

	// //////////////////////////////// methods //////////////////////////////

	public void drawField(int x, int y) {
		this.repaint(x * this.fieldSize, y * this.fieldSize, this.fieldSize,
				this.fieldSize);
	}

	public void paint(Graphics g) {

		// draw queens
		for (int x = 0; x < this.hopfieldSize; ++x) {
			for (int y = 0; y < this.hopfieldSize; ++y) {

				// determine color of field and draw
				g
						.setColor((x + y) % 2 == 0
								? this.whiteColor
								: this.blackColor);
				g.fillRect(x * this.fieldSize, y * this.fieldSize,
						this.fieldSize, this.fieldSize);

				// draw queen's image in case
				if (this.hopfield.getValue(x, y) == 1) {
					g.drawImage(this.starter.queensImage, x * this.fieldSize, y
							* this.fieldSize, this.fieldSize, this.fieldSize,
							this);
				}
			}
		}

		// draw diagonal arrows
		for (int i = 0; i < this.hopfield.southeasts.length; ++i) {
			Point p = this.hopfield.southeasts[i];
			g.drawImage(this.starter.southeastImage, p.x * this.fieldSize, p.y
					* this.fieldSize, this.fieldSize, this.fieldSize, this);
		}
		for (int i = 0; i < this.hopfield.northeasts.length; ++i) {
			Point p = this.hopfield.northeasts[i];
			g.drawImage(this.starter.northeastImage, p.x * this.fieldSize, p.y
					* this.fieldSize, this.fieldSize, this.fieldSize, this);
		}
		for (int i = 0; i < this.hopfield.southwests.length; ++i) {
			Point p = this.hopfield.southwests[i];
			g.drawImage(this.starter.southwestImage, p.x * this.fieldSize, p.y
					* this.fieldSize, this.fieldSize, this.fieldSize, this);
		}
		for (int i = 0; i < this.hopfield.northwests.length; ++i) {
			Point p = this.hopfield.northwests[i];
			g.drawImage(this.starter.northwestImage, p.x * this.fieldSize, p.y
					* this.fieldSize, this.fieldSize, this.fieldSize, this);
		}

		// draw field border
		g.setColor(this.borderColor);
		g.drawRect(this.fieldSize - 2, this.fieldSize - 2, this.fieldSize
				* (this.hopfieldSize - 2) + 4, this.fieldSize
				* (this.hopfieldSize - 2) + 4);
		g.drawRect(this.fieldSize - 1, this.fieldSize - 1, this.fieldSize
				* (this.hopfieldSize - 2) + 2, this.fieldSize
				* (this.hopfieldSize - 2) + 2);
		g.drawRect(this.fieldSize, this.fieldSize, this.fieldSize
				* (this.hopfieldSize - 2), this.fieldSize
				* (this.hopfieldSize - 2));
		g.drawRect(this.fieldSize + 1, this.fieldSize + 1, this.fieldSize
				* (this.hopfieldSize - 2) - 2, this.fieldSize
				* (this.hopfieldSize - 2) - 2);
		g.drawRect(this.fieldSize + 2, this.fieldSize + 2, this.fieldSize
				* (this.hopfieldSize - 2) - 4, this.fieldSize
				* (this.hopfieldSize - 2) - 4);
	}

	// /////////////////// implementing hopfield listener ////////////////////

	public void hopfieldChanged() {
		this.repaint();
	}

	public void setHopfield(QueensHopfield hopfield) {
		this.hopfield = hopfield;
	}
}