package hopfield;

import java.util.Vector;

public class Control implements Runnable {

	// status
	public static final short RUNNING = 0;
	public static final short SUCCESS = 1;
	public static final short STOPPED = 2;

	// commands
	public static final short START = 0;
	public static final short STEP = 1;
	public static final short STOP = 2;
	public static final short RESET = 3;
	public static final short RESTART = 4;
	public static final short EXIT = 5;

	// command strings
	public static final String[] commandStrings = new String[]{"start", "step",
			"stop", "reset", "restart", "exit"};

	// /////////////////////////// fields ////////////////////////////////////

	// references
	private QueensHopfield hopfield = null;
	private Starter starter;

	// vector for collecting controlListeners
	private Vector controlListeners = new Vector();

	// settings
	//private boolean automaticRestart = true;

	// status
	private short status = STOPPED;

	// //////////////////////// constructor and init /////////////////////////

	// constructor
	public Control(Starter starter, QueensHopfield hopfield) {
		this.starter = starter;
		this.hopfield = hopfield;
	}

	public void init() {
		//int hsize = this.hopfield.getSize();
	}

	// //////////////////////// action respond methods ///////////////////////

	public void doCommand(short command) {
		switch (command) {
			case START : {
				this.setStatus(RUNNING);
				Thread t = new Thread(this);
				t.setPriority(Thread.MIN_PRIORITY); // graphics must be updated
				t.start();
				break;
			}
			case STEP : {
				this.hopfield.computeRandomStep();
				if (!this.hopfield.hasChanges()) {
					if (this.hopfield.isCorrect()) {
						this.setStatus(SUCCESS);
					} else {
						this.hopfield.swapRandom();
					}
				}
				break;
			}
			case STOP : {
				this.setStatus(STOPPED);
				break;
			}
			case RESET : {
				this.setStatus(STOPPED);
				this.hopfield.reset();
				break;
			}
			case RESTART : {
				this.setStatus(STOPPED);
				this.starter.restart();
				break;
			}
			case EXIT : {
				this.setStatus(STOPPED);
				this.starter.exit();
			}
		}
	}

	// /////////////////////////// other methods /////////////////////////////

	public void run() {
		while (this.status == RUNNING) {
			this.doCommand(STEP);
		}
	}

	// ////////////////////// getter and setter //////////////////////////////

	public short getStatus() {
		return this.status;
	}

	public void setStatus(short status) {
		if (this.status != status) {

			// if new status different from old one, change it and inform
			// listeners
			this.status = status;
			for (int i = 0; i < this.controlListeners.size(); ++i) {
				((ControlListener) this.controlListeners.get(i))
						.statusChanged(status);
			}
		}
	}

	// ///////////////////// controllistener stuff ///////////////////////////

	public void addControlListener(ControlListener listener) {
		this.controlListeners.add(listener);
	}
}
