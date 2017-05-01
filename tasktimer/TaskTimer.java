/*
 * Created on 02.01.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package tasktimer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * @author zoppke
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TaskTimer extends JFrame {

	private JButton button = new JButton("start");
	private JLabel label = new JLabel("0:00:00");
	private boolean running = false;
	private Runnable labelThread;

	private long totalDayTime = 0;
	private long runningSince;

	////////////////////////////// lifecycle ///////////////////////////////////

	// Constructor
	private TaskTimer() {
		super("0:00:00");
	}

	// initializes this taskTimer
	private void init() {
		// init labelThread
		labelThread = new Runnable() {
			public void run() {
				while (running) {
					String s = getCurrentDayTimeString();
					label.setText(s);
					setTitle(s);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};

		// add listener to the button
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performAction();
			}
		});

		// add windowListener
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// set running flag to false, so that labelthread stops.
				running = false;
				System.exit(0);
			}
		});

		// layout
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(button, BorderLayout.WEST);
		getContentPane().add(label, BorderLayout.EAST);

		// to the screen
		pack();
		show();
	}

	///////////////////////////// other methods ////////////////////////////////

	// returns the currently accumulated daytime
	private long getCurrentDayTime() {
		return totalDayTime + System.currentTimeMillis() - runningSince;
	}

	// returns the currently accumulated daytime as String
	private String getCurrentDayTimeString() {
		return getTimeString(getCurrentDayTime());
	}

	// action performing when the button is clicked
	private void performAction() {
		// determine current state
		if (running) {
			button.setText("start");
			running = false;
			totalDayTime += (System.currentTimeMillis() - runningSince);
		} else {
			button.setText("stop");
			running = true;
			(new Thread(labelThread)).start();
			runningSince = System.currentTimeMillis();
		}
	}

	///////////////////////////// static methods ///////////////////////////////

	// string representation of a given time in hh:mm:ss
	private static String getTimeString(long t) {
		int i = (int) (t / 1000);
		int hours = i / 3600;
		int minutes = (i / 60) % 60;
		int seconds = i % 60;
		return (hours < 10 ? "0" : "")
			+ hours
			+ (minutes < 10 ? ":0" : ":")
			+ minutes
			+ (seconds < 10 ? ":0" : ":")
			+ seconds;
	}

	// main method
	public static void main(String args[]) {
		TaskTimer t = new TaskTimer();
		t.init();
	}
}
