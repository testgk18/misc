package ti.camview;

/**
 * Insert the type's description here.
 * Creation date: (10.08.00 13:13:31)
 * @author: 
 */
public class CameraMaker extends java.awt.Dialog {
	
	private CamView camview;
	private java.awt.Label pathLabel;
	private java.awt.TextField pathField;
	private java.awt.Button okButton;

	private class okButtonListener implements java.awt.event.ActionListener {
		CamView camview;
		private okButtonListener(CamView c) {
			camview = c;
		}
		public void actionPerformed(java.awt.event.ActionEvent ev) {
			camview.setCamera(new ti.camview.Camera(pathField.getText()));
			camview.setEnabled(true);
			dispose();
		}
	}
/**
 * Insert the method's description here.
 * Creation date: (10.08.00 14:58:39)
 * @param c ti.camview.CamView
 */
public CameraMaker(CamView c) {
	super (c);
	camview = c;
	initialize();
}
/**
 * Insert the method's description here.
 * Creation date: (10.08.00 15:03:38)
 */
public void initialize() {
	camview.setEnabled(false);
	setResizable(false);
	setBackground(java.awt.Color.lightGray);
	setTitle("create new Camera");
	setLocation(123,143);
	setSize(200, 120);
	setLayout(null);
	
	pathLabel = new java.awt.Label("Please enter sourcepath:");
	add (pathLabel);
	pathLabel.setBounds(10,30,180, 21);
	pathLabel.setVisible(true);
	
	pathField = new java.awt.TextField(23);
	add(pathField);
	pathField.setBounds(10, 60, 180, 21);
	pathField.setVisible(true);
	
	okButton = new java.awt.Button("ok");
	okButton.setVisible(true);
	add(okButton);
	okButton.addActionListener(new okButtonListener(camview));
	okButton.setBounds(75, 95, 50, 20);
	
	show();
	toFront();
	}
}
