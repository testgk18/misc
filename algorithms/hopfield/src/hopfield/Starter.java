package hopfield;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Starter extends JApplet {

	// image paths
	public static final String QUEENS_IMAGE = "data/dame.gif";
	public static final String NORTHEAST_IMAGE = "data/northeast.gif";
	public static final String NORTHWEST_IMAGE = "data/northwest.gif";
	public static final String SOUTHEAST_IMAGE = "data/southeast.gif";
	public static final String SOUTHWEST_IMAGE = "data/southwest.gif";

	// images that are preloaded
	public Image queensImage;
	public Image northeastImage;
	public Image northwestImage;
	public Image southeastImage;
	public Image southwestImage;

	private boolean areImagesLoaded = false;

	// data objects
	private QueensHopfield hopfield = null;
	private Control control = null;

	// gui objects
	private HopfieldPanel hopfieldPanel = null;
	private ControlPanel controlPanel = null;

	// flag indicating, whether this is an applet
	private boolean isApplet = true;

	public Starter() {
		this.hopfieldPanel = new HopfieldPanel(this);
		this.controlPanel = new ControlPanel();
	}

	// constructs and initializes all objects bottom-up
	public void init() {
		System.out.println("init");

		loadImages();
		
		// construct hopfield and control
		this.hopfield = new QueensHopfield();
		this.control = new Control(this, this.hopfield);

		// set references
		this.hopfieldPanel.setHopfield(this.hopfield);
		this.controlPanel.setControl(this.control);
		this.controlPanel.setHopfield(this.hopfield);
		
		// init applet
		this.setLayout(null);
		this.add(this.hopfieldPanel);
		this.add(this.controlPanel);
		this.hopfieldPanel.setBounds(0, 0, 400, 400);
		this.controlPanel.setBounds(400, 0, 200, 400);
	}

	public void start() {
		System.out.println("start");

		// ask for number of queens
		int queens = 0;
		while (queens <= 0) {
			String s = JOptionPane.showInputDialog("How many queens?");
			try {
				queens = Integer.parseInt(s);
			} catch (NumberFormatException e) {
				// nop
			}
		}
		this.hopfield.setQueens(queens);

		// init hopfield and control
		this.hopfield.init();
		this.control.init();

		// init Panels
		this.hopfieldPanel.init();
		this.controlPanel.init();
	}

	public void exit() {

		// check, whether we are running as application or as applet
		if (this.isApplet) {

			// stop the applet and return to main HTML page
			this.stop();
			try {
				URL url = new URL(this.getParameter("EXIT_URL"));
				this.getAppletContext().showDocument(url);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		} else {

			// just exit
			System.exit(0);
		}
	}

	public void restart() {
//		SwingUtilities.invokeLater(new Restarter());
		init();
		start();
	}

	public Dimension getPreferredSize() {
		return new Dimension(600, 400);
	}

	public static void main(String[] args) {
		Starter starter = new Starter();
		starter.isApplet = false;
		starter.init();
		starter.start();

		JFrame frame = new JFrame("Acht Damen");
		frame.setContentPane(starter);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		frame.pack();
		frame.setVisible(true);
	}

	private void loadImages() {

		// if already loaded, just return
		if (areImagesLoaded) {
			return;
		}

		// load images
		if (this.isApplet) {
			this.queensImage = this.getImage(this.getCodeBase(), QUEENS_IMAGE);
			this.northeastImage = this.getImage(this.getCodeBase(),
					NORTHEAST_IMAGE);
			this.northwestImage = this.getImage(this.getCodeBase(),
					NORTHWEST_IMAGE);
			this.southeastImage = this.getImage(this.getCodeBase(),
					SOUTHEAST_IMAGE);
			this.southwestImage = this.getImage(this.getCodeBase(),
					SOUTHWEST_IMAGE);
		} else {
			this.queensImage = Toolkit.getDefaultToolkit().createImage(
					QUEENS_IMAGE);
			this.northeastImage = Toolkit.getDefaultToolkit().createImage(
					NORTHEAST_IMAGE);
			this.northwestImage = Toolkit.getDefaultToolkit().createImage(
					NORTHWEST_IMAGE);
			this.southeastImage = Toolkit.getDefaultToolkit().createImage(
					SOUTHEAST_IMAGE);
			this.southwestImage = Toolkit.getDefaultToolkit().createImage(
					SOUTHWEST_IMAGE);

			// wait for images to be loaded
			MediaTracker tracker = new MediaTracker(this);
			tracker.addImage(this.queensImage, 1);
			tracker.addImage(this.northeastImage, 1);
			tracker.addImage(this.northwestImage, 1);
			tracker.addImage(this.southeastImage, 1);
			tracker.addImage(this.southwestImage, 1);
			try {
				tracker.waitForAll();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// check if images are loaded
			if (tracker.isErrorAny()) {
				JOptionPane.showMessageDialog(this,
						"Errors while loading images", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		areImagesLoaded = true;
	}

	class Restarter implements Runnable {

		public void run() {
			init();
			start();
		}

	}

}