package ti.camview;

/**
 * Insert the type's description here.
 * Creation date: (04.07.00 17:02:22)
 * @author: 
 */
public class IntervalManager extends java.awt.Panel {
	private java.awt.TextField numberField;
	private java.awt.Choice unitChoice;
/**
 * IntervalManager constructor comment.
 */
public IntervalManager() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (04.07.00 17:22:59)
 * @param bgr java.awt.Color
 * @param text java.lang.String
 */
public IntervalManager(java.awt.Color bgr) {
	this (bgr, 1, 1);
}
/**
 * Insert the method's description here.
 * Creation date: (04.07.00 17:22:59)
 * @param bgr java.awt.Color
 * @param text java.lang.String
 */
public IntervalManager(java.awt.Color bgr, int amount, int field) {
	super();
	setBackground(bgr);
	initialize();
	unitChoice.select(field);
	numberField.setText(Integer.toString(amount));
}
/**
 * Insert the method's description here.
 * Creation date: (04.07.00 17:07:38)
 * @return int[]
 */
public int getInterval() {
	int retour = parseText();
	switch (unitChoice.getSelectedIndex()) {
		case 0 : break;
		case 1 : retour *= 60; break;
		case 2 : retour *= 1440; break;
	}
	return retour;
}
/**
 * Insert the method's description here.
 * Creation date: (04.07.00 17:25:05)
 */
private void initialize() {
	setLayout(null);

	numberField = new java.awt.TextField("5",3);
	unitChoice = new java.awt.Choice();
	unitChoice.add("minutes");
	unitChoice.add("hours");
	unitChoice.add("days");

	
	add(numberField);
	numberField.setBounds(5, 5, 35, 23);
	numberField.setVisible(true);

	add(unitChoice);
	unitChoice.setBounds (40, 5, 70, 23);
	unitChoice.setVisible(true);
}
/**
 * Insert the method's description here.
 * Creation date: (04.07.00 17:15:05)
 * @return int
 */
private int parseText() {
	int retour = 1;
	try {retour = (new Integer(numberField.getText())).intValue(); }
	catch (NumberFormatException exc) {}
	return retour;
}
}
