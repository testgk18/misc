package hopfield;

import java.util.Vector;
import java.awt.Point;

public class Hopfield {

	// codes for update results
	public static final short NO_SWAP = 0;
	public static final short SWAP_DOWN = 1;
	public static final short SWAP_UP = 2;
	public static final short STEPS = 3;

	// statisticsStrings
	public static String[] statisticsStrings = new String[]{"no swap:",
			"swap down:", "swap up:", "steps:"};

	// data fields
	protected int size;
	protected int[][] values;
	protected double[][] thresholds;
	protected double[][][][] weights;
	protected int[] statistics;

	// help array for random computing
	private Point[] neurons;

	// flag indicating whether any changes during last step
	private boolean changes = true;

	// listeners
	private Vector hopfieldListeners = new Vector();

	// ///////////////////// constructor and init ////////////////////////////

	public Hopfield() {
		// empty
	}

	public void init() {
		this.reset();
		this.thresholds = new double[size][size];
		this.weights = new double[size][size][size][size];
		this.neurons = new Point[size * size];
		for (int i = 0; i < this.neurons.length; ++i) {
			this.neurons[i] = new Point(i / size, i % size);
		}
	}

	public void reset() {
		this.values = new int[size][size];
		this.statistics = new int[4];
		this.hopfieldChanged();
	}

	// //////////////////////////// computing ////////////////////////////////

	public void compute(Point p) {
		this.compute(p.x, p.y);
	}

	public void compute(int x, int y) {

		// compute new value
		double sum = 0;
		int oldValue = this.values[x][y];
		for (int i = 0; i < this.size; ++i) {
			for (int j = 0; j < this.size; ++j) {
				sum += this.values[i][j] * this.weights[i][j][x][y];
			}
		}
		this.values[x][y] = sum > this.thresholds[x][y] ? 1 : 0;

		// compare old value with new one
		if (this.values[x][y] > oldValue) {
			this.statistics[SWAP_UP]++;
			this.changes = true;
		} else if (this.values[x][y] < oldValue) {
			this.statistics[SWAP_DOWN]++;
			this.changes = true;
		} else {
			this.statistics[NO_SWAP]++;
		}
	}

	// computes the new values for all neurons in a random order
	public void computeRandomStep() {

		// generate new random order
		for (int i = 0; i < this.neurons.length; ++i) {
			Point swap = this.neurons[i];
			int swapIndex = (int) Math.floor(Math.random()
					* this.neurons.length);
			this.neurons[i] = this.neurons[swapIndex];
			this.neurons[swapIndex] = swap;
		}

		// reset changes, increment steps
		this.changes = false;
		this.statistics[STEPS]++;

		// compute new values
		for (int i = 0; i < this.neurons.length; ++i) {
			this.compute(this.neurons[i]);
		}

		// call listeners
		this.hopfieldChanged();
	}

	public static int signum(double d) {
		return d >= 0 ? 1 : -1;
	}

	public void swapRandom() {
		Point p = this.neurons[(int) Math.floor(Math.random()
				* this.neurons.length)];
		if (this.values[p.x][p.y] == 0) {
			this.values[p.x][p.y] = 1;
			this.statistics[SWAP_UP]++;
		} else {
			this.values[p.x][p.y] = 0;
			this.statistics[SWAP_DOWN]++;
		}
		this.changes = true;
		this.hopfieldChanged();
	}
	// //////////////////////// getters and setters //////////////////////////

	public int getSize() {
		return this.size;
	}
	
	public void setSize(int size) {
		this.size = size;
	}

	// set wights to the specified value. weihts are symmetrically
	public void setWeight(int x1, int y1, int x2, int y2, double value) {
		this.weights[x1][y1][x2][y2] = value;
		this.weights[x2][y2][x1][y1] = value;
	}

	public int getValue(int x, int y) {
		return this.values[x][y];
	}

	public boolean hasChanges() {
		return this.changes;
	}

	public void setChanges(boolean b) {
		this.changes = b;
	}

	public int getStatistics(short type) {
		return this.statistics[type];
	}

	public void setStatistics(short type, int value) {
		this.statistics[type] = value;
	}

	// ////////////////////// hopfieldListener stuff /////////////////////////

	public void addHopfieldListener(HopfieldListener listener) {
		this.hopfieldListeners.add(listener);
	}

	private void hopfieldChanged() {
		for (int i = 0; i < this.hopfieldListeners.size(); ++i) {
			HopfieldListener hl = (HopfieldListener) this.hopfieldListeners
					.get(i);
			hl.hopfieldChanged();
		}
	}
}
