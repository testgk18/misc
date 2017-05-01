package ti.camview;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

class Show extends Panel implements Runnable { 
	
	private class StopListener implements ActionListener {
		Show show;
		public StopListener(Show show) {
			this.show = show;
		}
		public StopListener() {}
		public synchronized void actionPerformed(ActionEvent ev) {
			action = 0;
			setCurrentPicture(0);
			status.setText("stop");
		}
	}

	private class ForwardListener implements ActionListener {
		Show show;
		public ForwardListener() {}
		public ForwardListener(Show show) {
			this.show = show;
		}
		public synchronized void actionPerformed(ActionEvent ev) {
			if (Integer.parseInt(currentPicture.getText()) >= pictures.length) {
				if (repeatBox.getState())  
					setCurrentPicture(0);
				else
					action = 0;
			}
			else {
				if (action == 0)    action = 1;
				setCurrentPicture(Integer.parseInt(currentPicture.getText()));
			}
			repaint();
		}
	}

	private class BackListener implements ActionListener {
		public BackListener() {}

		public synchronized void actionPerformed(ActionEvent ev) {
			if (Integer.parseInt(currentPicture.getText()) == 1) {
				if (repeatBox.getState()) {
					setCurrentPicture(pictures.length - 1);
				}
			}
			else {				
				if (action == 0)  action = 1;
				setCurrentPicture(Integer.parseInt(currentPicture.getText())-2);
			}
			repaint();
		}
	}

	private class PauseListener implements ActionListener {
		Show show;
		public PauseListener() {}
		public PauseListener(Show s) {
			show = s;
		}
		public synchronized void actionPerformed(ActionEvent ev) {
			switch(action) {
				case 0 : action = 1; status.setText("pause"); break;
				case 1 : action = 2; status.setText("play"); break;
				case 2 : action = 1; status.setText("pause"); break;
			}
		}
	}
	
	private class PlayListener implements ActionListener {
		Show show;
		public PlayListener() {}
		public PlayListener(Show s) {
			show = s;
		}
		public synchronized void actionPerformed(ActionEvent ev) {
			if (action == 0) setCurrentPicture(0);
			action = 2;
		}
	}

	private class SpeedUpListener implements ActionListener {
		Show show;
		public SpeedUpListener() {}
		public SpeedUpListener(Show s) {
			show = s;
		}
		public synchronized void actionPerformed(ActionEvent ev) {
			if (speed < millisPerPicture.length - 1)
				speed++;
		}
	}

	private class SlowDownListener implements ActionListener {
		Show show;
		public SlowDownListener() {}
		public SlowDownListener(Show s) {
			show = s;
		}
		public synchronized void actionPerformed(ActionEvent ev) {
			if (speed > 0)
				speed--;
		}
	}
	
	
	public Image[] pictures = null;
	private int[] millisPerPicture;
	private Label currentPicture;
	private Button playButton;
	private Button stopButton;
	private Button forwardButton;
	private Button backButton;
	private Label status;
	private java.awt.Checkbox repeatBox;
	private java.awt.Button pauseButton;
	private java.awt.GridBagConstraints constraints = new GridBagConstraints();
	private java.awt.GridBagLayout gridbag = new GridBagLayout();
	public java.awt.Panel imagePanel;
	/*	0 = stop
		1 = pause
		2 = play
	*/
	private int action = 0;
	private volatile java.lang.Thread blinker = null;
	private CamView camview;
	private java.awt.Button speedUpButton;
	private java.awt.Button slowDownButton;
	private int speed;
	public Show(CamView c) {
		camview = c;
		initialize();
	}
/**
 * Insert the method's description here.
 * Creation date: (08.08.00 13:20:26)
 */
private void initialize() {
	millisPerPicture = Settings.getMillisPerPicture();
	speed = millisPerPicture.length / 2;
	setLayout(gridbag);
	setBackground(new Color(1234567));
	imagePanel = new java.awt.Panel();
	
	constraints = CamView.buildConstraints(constraints, 0,0, 1,10, 300, 300);
	imagePanel.setLayout(null);
	imagePanel.setSize(camview.getCamera().getSize());
	imagePanel.setVisible(true);
	gridbag.setConstraints(imagePanel, constraints);
	add(imagePanel);
	
	constraints = CamView.buildConstraints(constraints, 1,0, 1,1, 0, 0);		
	playButton = new Button("play");
	playButton.setEnabled(false);
	playButton.setVisible(true);
	playButton.addActionListener(new PlayListener(this));
	gridbag.setConstraints(playButton, constraints);
	add(playButton);
	
	constraints = CamView.buildConstraints(constraints, 1,1, 1,1, 0, 0);
	stopButton = new Button("stop");
	stopButton.setEnabled(false);
	stopButton.setVisible(true);
	stopButton.addActionListener(new StopListener(this));
	gridbag.setConstraints(stopButton, constraints);
	add(stopButton);
	
	constraints = CamView.buildConstraints(constraints, 1,2, 1,1, 0, 0);		
	backButton = new Button("back");
	backButton.setEnabled(false);
	backButton.setVisible(true);
	backButton.addActionListener(new BackListener());
	gridbag.setConstraints(backButton, constraints);
	add(backButton);
	
	constraints = CamView.buildConstraints(constraints, 1,3, 1,1, 0, 0);
	forwardButton = new Button("forward");
	forwardButton.setEnabled(false);
	forwardButton.setVisible(true);
	forwardButton.addActionListener(new ForwardListener(this));
	gridbag.setConstraints(forwardButton, constraints);
	add(forwardButton);
	
	constraints = CamView.buildConstraints(constraints, 1,4, 1,1, 0, 0);
	pauseButton = new Button("pause");
	pauseButton.addActionListener(new PauseListener(this)); 
	pauseButton.setEnabled(false);
	pauseButton.setVisible(true);
	gridbag.setConstraints(pauseButton, constraints);
	add(pauseButton);
	
	constraints = CamView.buildConstraints(constraints, 1,5, 1,1, 0, 0);
	repeatBox = new Checkbox("repeat", false);
	repeatBox.setEnabled(false);
	add(repeatBox);
	repeatBox.setVisible(true);
	gridbag.setConstraints(repeatBox, constraints);

	constraints = CamView.buildConstraints(constraints, 1,6, 1,1, 0, 0);
	speedUpButton = new Button("speedUp");
	speedUpButton.addActionListener(new SpeedUpListener(this)); 
	speedUpButton.setEnabled(false);
	speedUpButton.setVisible(true);
	gridbag.setConstraints(speedUpButton, constraints);
	add(speedUpButton);
	
	constraints = CamView.buildConstraints(constraints, 1,7, 1,1, 0, 0);
	slowDownButton = new Button("slowDown");
	slowDownButton.addActionListener(new SlowDownListener(this)); 
	slowDownButton.setEnabled(false);
	slowDownButton.setVisible(true);
	gridbag.setConstraints(slowDownButton, constraints);
	add(slowDownButton);
		
	
	constraints = CamView.buildConstraints(constraints, 1,8, 1,1, 0, 0);		
	status = new Label("nothing");
	add (status);
	status.setVisible(true);
	gridbag.setConstraints(status, constraints);
	constraints = CamView.buildConstraints(constraints, 1,9, 1,1, 0, 0);		
	currentPicture = new Label("0");
	add (currentPicture);
	currentPicture.setVisible(true);
	gridbag.setConstraints(currentPicture, constraints);
}
	public void paint(Graphics g) {
		int index = Integer.parseInt(currentPicture.getText())-1;
		try {
			if (pictures != null && index >= 0 && index < pictures.length)
			imagePanel.getGraphics().drawImage(pictures[index],0, 0, this);
		} catch (Exception exc) {System.out.println(exc);}
		
	}
	public void run() {
		Thread thisThread = Thread.currentThread();
 		while (blinker == thisThread) {
			if (action == 2) {
				if (Integer.parseInt(currentPicture.getText())-1 >= pictures.length -1) {
					if (repeatBox.getState())
						setCurrentPicture(0);
					else 
						action = 0;
				}
				else
					setCurrentPicture(Integer.parseInt(currentPicture.getText()));
				imagePanel.getGraphics().drawImage(pictures[Integer.parseInt(currentPicture.getText())-1],0, 0, this);
			}
			try   { thisThread.sleep(millisPerPicture[speed]); }
			catch (InterruptedException exc) {System.out.println(exc);}
		}
	}
/**
 * Insert the method's description here.
 * Creation date: (21.06.00 16:25:49)
 * @param index int
 */
private void setCurrentPicture(int index) {
	if ((pictures.length >= index + 1) & (index >= 0)) 
		currentPicture.setText(""+(index + 1));
}
/**
 * Insert the method's description here.
 * Creation date: (23.06.00 16:47:20)
 */
public void start() {
	setCurrentPicture(1);
	playButton.setEnabled(true);
	stopButton.setEnabled(true);
	forwardButton.setEnabled(true);
	backButton.setEnabled(true);
	pauseButton.setEnabled(true);
	repeatBox.setEnabled(true);
	speedUpButton.setEnabled(true);
	slowDownButton.setEnabled(true);
	if (blinker == null && pictures != null) {
		repaint();
		action = 0;
		blinker = new Thread(this);
		blinker.start();
	}
}
/**
 * Insert the method's description here.
 * Creation date: (06.07.00 17:33:05)
 * @param img java.awt.Image[]
 */
public void start(Image[] img) {
	pictures = img;
	start();
	}
	public void stop() {
		blinker = null;
	}
}
