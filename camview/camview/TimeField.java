package ti.camview;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.*;
import java.io.*;

public class TimeField extends TextField {

	private int hour;
	private int minutes;

	
/**
 * Insert the method's description here.
 * Creation date: (30.06.00 15:48:57)
 */
public TimeField() {
	this(12,0);
}
/**
 * Insert the method's description here.
 * Creation date: (30.06.00 15:51:37)
 * @param h int
 * @param m int
 */
public TimeField(int h, int m) {
	super(5);
	hour = h;
	minutes = m;
	initialize();
	
}
/**
 * @returns the number of milliseconds since midnight, the noon-number by default.
 */
 
public long getTime() {
	long retour = 1000L * 60 * minutes;
	retour += hour * 3600 * 100 * 1000;
	return retour;
}
/**
 * @returns the number of milliseconds since midnight, the noon-number by default.
 */
 
public int getTimeInMinutes() {
	return minutes + hour * 60;
}
/**
 * Insert the method's description here.
 * Creation date: (30.06.00 15:26:12)
 */
private void initialize() {
	addFocusListener ( new FocusAdapter(){
		public void focusLost(FocusEvent ev) {
			parseText();
			setText(timeToString());
		}
	});
	setText(timeToString());
}
/**
 * Insert the method's description here.
 * Creation date: (30.06.00 13:12:13)
 * @return long
 */
private void parseText() {
	hour = 0;
	minutes = 0;
	char[] ch = getText().toCharArray();
	int index = 0;
	while ((index < ch.length) && (Character.isDigit(ch[index]))) {
		hour = hour * 10;
		hour = hour + Character.getNumericValue(ch[index]);
		index++;
	}
	while ((index < ch.length) && !(Character.isDigit(ch[index]))) {
		index++;
	}
	while ((index < ch.length) && (Character.isDigit(ch[index]))) {
		minutes = minutes * 10;
		minutes = minutes + Character.getNumericValue(ch[index]);
		index++;
	}
	if (minutes > 59) minutes = 59;
	if (hour > 23) hour = 23;
}
/**
 * Insert the method's description here.
 * Creation date: (30.06.00 16:03:59)
 * @param h int
 * @param m int
 */
public void setTime(int h, int m) {
	hour = h;
	minutes = m;
	setText(timeToString());
}
/**
 * Insert the method's description here.
 * Creation date: (30.06.00 15:19:43)
 * @return java.lang.String
 */
private String timeToString() {
	String retour = "";
	retour += hour;
	retour += ":";
	if (minutes<10) retour += "0";
	retour += minutes;
	return retour;
}
}
