package ti.camview;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.*;
import java.io.*;

public class DayField extends Container {
	private java.awt.TextField dayField;
	private java.awt.TextField yearField;
	private java.awt.Choice monthChoice;
	private int year;	
	private int month;
	private int day;
/**
 * Insert the method's description here.
 * Creation date: (03.07.00 17:00:48)
 */
public DayField() {
	this(2000, 1, 1);
}
public DayField(int y, int m, int d) {
	super();
	year = y;
	month = m;
	day = d;
	initialize();
}
	public int[] getDate() {
		parseFields();
		int[] time = {year, month, day};
		return time;
	}
/**
 * Insert the method's description here.
 * Creation date: (03.07.00 15:25:27)
 */
private void initialize() {
	setLayout(null);

	dayField = new TextField(2);	
	add(dayField);
	dayField.setVisible(true);
	dayField.setBounds(1, 1,32,23);

	monthChoice = new Choice();	
	monthChoice.add("JAN"); monthChoice.add("FEB"); monthChoice.add("MAR");
	monthChoice.add("APR"); monthChoice.add("MAY"); monthChoice.add("JUN");
	monthChoice.add("JUL"); monthChoice.add("AUG"); monthChoice.add("SEP");
	monthChoice.add("OCT"); monthChoice.add("NOV"); monthChoice.add("DEC");
	monthChoice.setVisible(true);
	monthChoice.setBounds(33, 1, 50, 23);
	add(monthChoice);

	yearField = new TextField(4);
	yearField.setVisible(true);
	yearField.setBounds(85, 1, 50, 23);
	add(yearField);
	
	
	addFocusListener ( new FocusAdapter(){
		public void focusLost(FocusEvent ev) {
			parseFields();
			setFields();
		}
	});
	setFields();
}
/**
 * Insert the method's description here.
 * Creation date: (03.07.00 14:48:27)
 */
private void parseFields() {
	month = monthChoice.getSelectedIndex();
	try {
		day = Integer.parseInt(dayField.getText());
	}
	catch (NumberFormatException exc) {
		day = 1;
	}
	try {
		year = Integer.parseInt(yearField.getText());
	}
	catch (NumberFormatException exc) {
		year = Settings.getGregorian().get(Calendar.YEAR);
	}
	if (year < 40) year = year + 2000;
	if (year < 100) year = year + 1900;
}
	public void setDate(int y, int m, int d) {
		year = y;
		month = m;
		day = d;
		setFields();
	}
/**
 * Insert the method's description here.
 * Creation date: (30.06.00 16:23:27)
 */
private void setFields() {
	yearField.setText(""+year);
	monthChoice.select(month);
	dayField.setText(""+day);	
}
}
