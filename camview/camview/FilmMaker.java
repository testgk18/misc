package ti.camview;

/**
 * a dialog-window for creating a small web-cam-movie.
 * Creation date: (29.06.00 15:03:25)
 * @author: 
 */
public class FilmMaker extends java.awt.Dialog implements Runnable {
	private DateManager startField;
	private DateManager stopField;
	private IntervalManager incrementField;
	private java.awt.Button okButton;
	private java.awt.Button cancelButton;
	private FilterManager filterManager;
	private java.awt.GridBagConstraints constraints;
	private java.awt.GridBagLayout gridbag;

	private class okButtonListener implements java.awt.event.ActionListener {
		FilmMaker filmMaker;
		private okButtonListener(FilmMaker fm) {
			filmMaker = fm;
		}
		public void actionPerformed(java.awt.event.ActionEvent ev) {
			if (checkData()) {
				start();
			}
		}
	}

	private class cancelButtonListener implements java.awt.event.ActionListener {
		FilmMaker filmMaker;
		private cancelButtonListener(FilmMaker fm) {
			filmMaker = fm;
		}
		public void actionPerformed(java.awt.event.ActionEvent ev) {
			if (blinker == null) {
				camview.setEnabled(true);
				dispose();
			}
			else {
				blinker = null;
			}
		}
	}

	private class TipRelease extends java.awt.event.MouseAdapter {
		String tipText;
		private TipRelease(String s) {
			tipText = s;
		}
		public void mouseEntered(java.awt.event.MouseEvent ev) {
			status.setText(tipText);
		}
		public void mouseExited(java.awt.event.MouseEvent ev) {
			status.setText("");
		}
	}
	
	private CamView camview;
	private java.awt.Label status;
	private volatile java.lang.Thread blinker;
	private java.awt.Label incrementLabel;
/**
 * Constructs a FilmMaker-Object. You shall give a reference to the instance of the main class CamView, 
 * because access to getShow() and getCamera() is neccessary.
 * Creation date: (29.06.00 15:24:56)
 * @param c ti.camview.CamView
 */
public FilmMaker(CamView c) {
	super(c);
	camview = c;
	initialize();
}
/**
 * Insert the method's description here.
 * Creation date: (05.07.00 17:23:27)
 * @return boolean
 */
private synchronized boolean checkData() {
	return true;
}
/**
 * Insert the method's description here.
 * Creation date: (29.06.00 17:36:43)
 */
private void initialize() {
	camview.getShow().stop();  //otherwise you could not run a new one.
	camview.setEnabled(false);
	setSize(260, 280);
	setBackground(java.awt.Color.gray);
	setResizable(false);
	setTitle("Create a new Film");
	setLayout(null);
	
	java.util.GregorianCalendar dummiGreg = camview.getCamera().getMaxDate();
	stopField = new DateManager(dummiGreg,"last picture:", new java.awt.Color (223, 200, 200));
	stopField.addMouseListener(new TipRelease(" Type in, when the film should stop"));
	add(stopField);
	stopField.setBounds(10, 87, 245, 50);
	stopField.setVisible(true);
	
	dummiGreg.add(java.util.Calendar.DATE, -1);
	startField = new DateManager(dummiGreg,"first picture:", new java.awt.Color(200, 200, 250));
	startField.addMouseListener(new TipRelease(" Type in, when the film should begin"));
	add(startField); 
	startField.setBounds(10, 30, 245, 50);
	startField.setVisible(true);

	filterManager = new FilterManager(new java.awt.Color (220, 200, 160));
	filterManager.addMouseListener(new TipRelease(" What the images may deviate max."));
	add(filterManager);
	filterManager.setBounds(10, 144, 245, 45);
	filterManager.setVisible(true);

	incrementLabel = new java.awt.Label("    Incrementation");
	incrementLabel.setBackground(new java.awt.Color (180, 200, 233));
	add(incrementLabel);
	incrementLabel.setBounds(10, 195, 130, 30);
	incrementLabel.setVisible(true);
	incrementLabel.addMouseListener(new TipRelease(" Type in the period of the pictures"));
	
	incrementField = new IntervalManager(new java.awt.Color (180, 200, 233));
	incrementField.addMouseListener(new TipRelease(" Type in the period of the pictures"));
	add(incrementField);
	incrementField.setBounds(140, 195, 115, 30);
	incrementField.setVisible(true);

	cancelButton = new java.awt.Button("cancel");
	cancelButton.addMouseListener(new TipRelease(" stop this"));
	cancelButton.addActionListener(new cancelButtonListener(this));
	add(cancelButton);
	cancelButton.setBounds(10, 230, 50, 20);
	cancelButton.setVisible(true);
	cancelButton.setBackground(java.awt.Color.red);

	
	okButton = new java.awt.Button("ok");
	okButton.addMouseListener(new TipRelease(" Lets go! Create now!"));
	add(okButton);
	okButton.setBounds(220, 230, 35, 20);
	okButton.setVisible(true);
	okButton.setBackground(java.awt.Color.green);okButton.addActionListener(new okButtonListener(this));
	okButton.addKeyListener(new java.awt.event.KeyAdapter() { // funktioniert leider nicht :-(
		public void keyTyped(java.awt.event.KeyEvent ev) {
			System.out.println("KeyListener: "+ev);
			if (ev.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER)
				start();
		}
	});
		
	status = new java.awt.Label();
	add(status);
	status.setBounds(10,255,245,18);
	status.setBackground(new java.awt.Color(202,202,202));
	status.setVisible(true);
	
	toFront();
	setLocation(200, 200);
	show();
}
/**
 * This method is running as a Thread. it gets you an java.awt.Image-Array 
 * (by invoking several times the Camera.getImage()-method) and stores it to Show.pictures.
 * Creation date: (05.07.00 17:39:26)
 */
public void run() {
	Thread thisThread = Thread.currentThread();
	java.util.Vector v;
	java.util.GregorianCalendar start, stop;
	int filter;
	int increment;
	String newFile, oldFile;
	java.awt.Image[] img;
	java.awt.MediaTracker tracker;
	if (checkData()) {
		// disable GUI except cancelButton
		synchronized (this) {
			okButton.setEnabled(false);
			filterManager.setEnabled(false);
			incrementField.setEnabled(false);
			startField.setEnabled(false);
			stopField.setEnabled(false);
	
			okButton.addMouseListener(null);
			filterManager.addMouseListener(null);			
			incrementField.addMouseListener(null);	
			startField.addMouseListener(null);
			stopField.addMouseListener(null);
		}
		// initialization
		v = new java.util.Vector();
		start = startField.getDate(); System.out.println("startField.getDate()"+startField.getDate());
		stop = stopField.getDate(); System.out.println("stopField.getDate()"+stopField.getDate());
		filter = filterManager.getFilter();
		increment = incrementField.getInterval(); 
		oldFile = null;
		tracker = new java.awt.MediaTracker(camview.getShow().imagePanel);

		// don't test more than necessary
		filter = Math.min(filter, increment);
				
		// Get the Images now
		while (start.before(stop) && blinker == thisThread) {
			newFile = camview.getCamera().getImage(start, filter, oldFile);
			if (newFile != null) {
				if (oldFile == null || !newFile.equals(oldFile)) {
					v.addElement(newFile);
					oldFile = newFile;
					status.setText(v.size() + " pictures found");
				}
			}
			start.add(java.util.Calendar.MINUTE, increment);
		}
		
		img = new java.awt.Image[v.size()];
		for (int i=0; i<v.size(); i++) {
			img[i] = (java.awt.Toolkit.getDefaultToolkit()).getImage(v.get(i).toString());
		}
		
		for (int i=0; i < img.length; i++) {
			tracker.addImage(img[i], i); 
		}

		// Beachte auch die Skalierungsfunktion, falls camview doch eine fixe Größe erhalten sollte.
		
		String statusText = status.getText() + ", loaded ";
		int i = 0;
		while (i < img.length && blinker == thisThread) {
			try { tracker.waitForID(i, 10000L);}
			catch (InterruptedException exc) {System.out.println(exc);}
			status.setText(statusText + ++i);
		}
		if (blinker == thisThread) {	
			if (tracker.isErrorAny()) {
				Object[] badImages = tracker.getErrorsAny();
				java.util.Vector goodImages = new java.util.Vector();
					
				for (i=img.length-1; i>=0; i--) {
					int i1 = 0;
					while ((img[i] != badImages[i1]) & (++i1<badImages.length)) {}
					if (i1 == badImages.length)
						goodImages.add(img[i]);
				}
	
				img = new java.awt.Image[goodImages.size()];
				for (i=0; i<goodImages.size(); i++) {
					img[i] = (java.awt.Image) goodImages.get(i);
				}
			}
			camview.getShow().pictures = img;
			camview.getShow().start();
		}
		camview.setEnabled(true);
		dispose();
	}
}
/**
 * starts the run-method as a Thread. This method will be called, if You press the ok-button.
 * Creation date: (13.07.00 17:15:20)
 */
public void start() {
	if (blinker == null) {
		blinker = new Thread(this);
		blinker.start();
	}
}
}
