package ti.camview;

/**
 * Insert the type's description here.
 * Creation date: (04.07.00 16:14:27)
 * @author: 
 */
public class DateManager extends java.awt.Panel {
	private DateField dateField;
	private java.awt.Label label;
/**
 * DateManager constructor comment.
 */
public DateManager() {
	this(Settings.getGregorian(), "", java.awt.Color.gray);
}
/**
 * Insert the method's description here.
 * Creation date: (04.07.00 16:20:02)
 * @param text java.lang.String
 * @param bgr java.awt.Color
 */
public DateManager(String text, java.awt.Color bgr) {
	this(Settings.getGregorian(), text, bgr);
}
/**
 * DateManager constructor comment.
 */
public DateManager(java.util.GregorianCalendar greg) {
	this(greg, "", java.awt.Color.gray);
}
/**
 * Insert the method's description here.
 * Creation date: (04.07.00 16:20:02)
 * @param text java.lang.String
 * @param bgr java.awt.Color
 */
public DateManager(java.util.GregorianCalendar greg, String text, java.awt.Color bgr) {
	super();
	label = new java.awt.Label(text);
	setBackground(bgr);
	dateField = new DateField();
	dateField.setDate(greg);
	initialize();
}
/**
 * Insert the method's description here.
 * Creation date: (04.07.00 16:16:29)
 * @return java.util.GregorianCalendar
 */
public java.util.GregorianCalendar getDate() {
	return dateField.getDate();
}
/**
 * Insert the method's description here.
 * Creation date: (04.07.00 16:51:29)
 */
private void initialize() {

	setLayout(null);

	add(label);
	label.setBounds (8, 5, 180, 17);
	label.setVisible(true);

	add(dateField);
	dateField.setBounds (5, 22, 230, 25);
	dateField.setVisible(true);

}
}
