package ti.camview;

//package ti.camview;
/**
 <code> CamView </code> class creates a frame. it has instances of the classes <code> Camera, Film
 and Show.</code>
 *
 * @see Camera
*/
import java.awt.*;
import java.awt.event.*; 

public class CamView extends Frame {

	private Camera camera;
	private Show show;
	private java.awt.GridBagLayout gridbag = new GridBagLayout();
	private java.awt.GridBagConstraints constraints = new GridBagConstraints();
	private Mbar mbar;
	private CamView () {
		camera = Settings.getDefaultCamera();
		setBackground(new Color(687473));
		setTitle("CamView - " + camera.getWindowTitle());
		setResizable(true);
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		addWindowListener(new java.awt.event.WindowAdapter () {
		    public void windowClosing(java.awt.event.WindowEvent e) {
				destroy();
			}
		});

		mbar = new Mbar(this);
		setMenuBar(mbar);
		show = new Show(this);
		
		add (show);
		
		setLocation(150, 100);
		pack();
		show();
	}
/**
 * Insert the method's description here.
 * Creation date: (31.05.00 16:50:17)
 * @param gbc java.awt.GridBagConstraints
 * @param gx int
 * @param gy int
 * @param gw int
 * @param gh int
 * @param wx int
 * @param wy int
 */
public static GridBagConstraints buildConstraints(GridBagConstraints gbc, int gx, int gy, int gw, int gh, int wx, int wy) {
	gbc.gridx = gx;
	gbc.gridy = gy;
	gbc.gridwidth = gw;
	gbc.gridheight = gh;
	gbc.weightx = wx;
	gbc.weighty = wy;
	return gbc;
}
	public void destroy() {
		setVisible(false);	// hide the Frame
		dispose();			// free the system resources
		System.exit(0);		// close the application
	}
/**
 * Insert the method's description here.
 * Creation date: (08.08.00 13:14:00)
 * @return ti.camview.Camera
 */
public Camera getCamera() {
	return camera;
}
/**
 * Insert the method's description here.
 * Creation date: (10.08.00 12:58:58)
 * @return ti.camview.Mbar
 */
public Mbar getMbar() {
	return mbar;
}
/**
 * Insert the method's description here.
 * Creation date: (08.08.00 13:13:23)
 * @return ti.camview.Show
 */
public Show getShow() {
	return show;
}
/**
 * Insert the method's description here.
 * Creation date: (28.06.00 15:11:52)
 * @param args java.lang.String[]
 */
public static void main(String[] args) {
	CamView myframe = new CamView();
}
/**
 * Insert the method's description here.
 * Creation date: (10.08.00 13:16:02)
 */
public void setCamera() {}
/**
 * sets the Camera. this method is invoked by CameraMaker.
 * Creation date: (10.08.00 13:16:02)
 * @see CameraMaker
 */
public void setCamera(ti.camview.Camera cam) {
	camera = cam;
	}
}
