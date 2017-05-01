package hopfield;

import java.awt.Point;

public class QueensHopfield extends Hopfield {

	public Point[] southwests;
	public Point[] northwests;
	public Point[] southeasts;
	public Point[] northeasts;
	public Point[] ups;
	public Point[] downs;
	public Point[] unuseds;

//	private int queens;

	public QueensHopfield() {
		// empty
	}

	public void init() {
		super.init();

		// init diagonals arrays
		this.northwests = new Point[this.size - 2];
		this.southwests = new Point[this.size - 2];
		this.northeasts = new Point[this.size - 3];
		this.southeasts = new Point[this.size - 3];
		this.ups = new Point[this.northeasts.length + this.northwests.length];
		this.downs = new Point[this.southeasts.length + this.southwests.length];
		this.unuseds = new Point[6];

		for (int i = 0; i < this.northwests.length; ++i) {
			this.southwests[i] = new Point(i + 2, 0);
			this.northwests[i] = new Point(this.size - 1, i + 2);
		}
		for (int i = 0; i < this.northeasts.length; ++i) {
			this.northeasts[i] = new Point(i + 1, this.size - 1);
			this.southeasts[i] = new Point(0, i + 1);
		}
		for (int i = 0; i < this.northwests.length; ++i) {
			this.ups[i] = this.southwests[i];
			this.downs[i] = this.northwests[i];
		}
		for (int i = 0; i < this.northeasts.length; ++i) {
			this.ups[i + this.southwests.length] = this.northeasts[i];
			this.downs[i + this.southwests.length] = this.southeasts[i];
		}
		this.unuseds[0] = new Point(0, 0);
		this.unuseds[1] = new Point(1, 0);
		this.unuseds[2] = new Point(this.size - 1, 1);
		this.unuseds[3] = new Point(this.size - 2, this.size - 1);
		this.unuseds[4] = new Point(0, this.size - 1);
		this.unuseds[5] = new Point(0, this.size - 2);

		// rows and columns as multiple flip-flops
		for (int y = 1; y < this.size - 1; ++y) {
			for (int x = 1; x < this.size - 1; ++x) {
				for (int i = 1; i < this.size - 1; ++i) {
					this.weights[x][y][i][y] = -2D;
					this.weights[x][y][x][i] = -2D;
				}
			}
		}

		// southwest weights
		for (int x = 2; x < this.size; ++x) {
			for (int i = 0; i < x; ++i) {
				for (int j = 0; j < x; ++j) {
					this.weights[x - i][i][x - j][j] = -2D;
				}
			}
		}

		// northeast weights
		for (int x = 1; x < this.size - 2; ++x) {
			for (int i = 0; i < this.size - x - 1; ++i) {
				for (int j = 0; j < this.size - x - 1; ++j) {
					this.weights[x + i][this.size - i - 1][x + j][this.size - j
							- 1] = -2D;
				}
			}
		}

		// southeast weights
		for (int y = 1; y < this.size - 2; ++y) {
			for (int i = 0; i < this.size - y - 1; ++i) {
				for (int j = 0; j < this.size - y - 1; ++j) {
					this.weights[i][y + i][j][y + j] = -2D;
				}
			}
		}

		// northwest weights
		for (int y = 2; y < this.size; ++y) {
			for (int i = 0; i < y; ++i) {
				for (int j = 0; j < y; ++j) {
					this.weights[this.size - i - 1][y - i][this.size - j - 1][y
							- j] = -2D;
				}
			}
		}

		// outer weights
		for (int i = 0; i < this.downs.length; ++i) {
			for (int j = 0; j < this.downs.length; ++j) {
				double d = -2D / this.size;
				this.weights[this.downs[i].x][this.downs[i].y][this.downs[j].x][this.downs[j].y] = d;
				this.weights[this.ups[i].x][this.ups[i].y][this.ups[j].x][this.ups[j].y] = d;
			}
		}

		// finally clear all self referencing weights
		for (int x = 0; x < this.size; ++x) {
			for (int y = 0; y < this.size; ++y) {
				this.weights[x][y][x][y] = 0D;
			}
		}

		// init inner thresholds
		for (int x = 1; x < this.size - 1; ++x) {
			for (int y = 1; y < this.size - 1; ++y) {
				this.thresholds[x][y] = -1D;
			}
		}

		// init outer thresholds
		for (int i = 0; i < this.size; ++i) {
			this.thresholds[0][i] = -1D;
			this.thresholds[i][0] = -1D;
			this.thresholds[this.size - 1][i] = -1D;
			this.thresholds[i][this.size - 1] = -1D;
		}

		// disable unused outer thresholds
		for (int i = 0; i < this.unuseds.length; ++i) {
			Point p = this.unuseds[i];
			this.thresholds[p.x][p.y] = 1D;
		}
	}

	// checks whether the problem is solved
	public boolean isCorrect() {

		// check rows
		for (int y = 1; y < this.size - 1; ++y) {
			int count = 0;
			for (int x = 1; x < this.size - 1; ++x) {
				count += this.values[x][y];
			}
			if (count != 1) {
				return false;
			}
		}

		// check columns
		for (int x = 1; x < this.size - 1; ++x) {
			int count = 0;
			for (int y = 1; y < this.size - 1; ++y) {
				count += this.values[x][y];
			}
			if (count != 1) {
				return false;
			}
		}

		// check diagonals '\'
		for (int i = 4 - this.size; i < this.size - 2; ++i) {
			int count = 0;
			for (int j = 1; j < this.size - 1; ++j) {
				if (i + j < 1) {
					continue;
				}
				if (i + j > this.size - 2) {
					break;
				}
				count += this.values[j][i + j];
			}
			if (count > 1) {
				return false;
			}
		}

		// check diagonals '/'
		for (int i = 4 - this.size; i < this.size - 2; ++i) {
			int count = 0;
			for (int j = 1; j < this.size - 1; ++j) {
				if (i + j < 1) {
					continue;
				}
				if (i + j > this.size - 2) {
					break;
				}
				count += this.values[i + j][j];
			}
			if (count > 1) {
				return false;
			}
		}

		// if we get to this point, we are true
		return true;
	}

	public void setQueens(int queens) {
//		this.queens = queens;
		super.setSize(queens + 2);
	}

}
