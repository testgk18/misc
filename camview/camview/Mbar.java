package ti.camview;

/**
 * The Menu-Bar at the top. Only possibilities so far:
 * - create a new film (by constructing a new FilmMaker-object)
 * - create a new Camera (by constructing a new CameraMaker-object)
 * Creation date: (05.07.00 16:08:28)
 * @author: 
 */
public class Mbar extends java.awt.MenuBar {
	private CamView camview;
	private java.awt.Menu cameraMenu;
	private java.awt.Menu filmMenu;
	private java.awt.MenuItem camera_new;
	private java.awt.MenuItem camera_open;
	private java.awt.MenuItem film_new;

	private class Film_new_Listener implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent ev) {
			new FilmMaker(camview);
		}
	}

	private class Camera_new_Listener implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent ev) {
			new CameraMaker(camview);
		}
	}

/**
 * Insert the method's description here.
 * Creation date: (05.07.00 16:09:38)
 * @param s ti.camview.Show
 * @param c ti.camview.CamView
 */
public Mbar(CamView c) {
	super();
	camview = c;
	initialize();	
}
/**
 * Insert the method's description here.
 * Creation date: (05.07.00 16:12:23)
 */
private void initialize() {
	cameraMenu = new java.awt.Menu("Camera");
	filmMenu = new java.awt.Menu("Film");
	add(cameraMenu);
	add(filmMenu);

	camera_new = new java.awt.MenuItem("new");
	camera_new.addActionListener(new Camera_new_Listener());
	camera_open = new java.awt.MenuItem("open");
	film_new = new java.awt.MenuItem("new");
	film_new.addActionListener(new Film_new_Listener());

	cameraMenu.add(camera_open);
	cameraMenu.add(camera_new);
	filmMenu.add(film_new);
	
}
}
