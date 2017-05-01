package ti.camview;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.*;
import java.io.*;

public class DateField extends Container {

	private DayField dayField;
	private TimeField timeField;
	private java.util.GregorianCalendar date;
	private java.awt.Label dayLabel;
	private java.awt.Label timeLabel;
/**
 * Insert the method's description here.
 * Creation date: (04.07.00 16:30:15)
 */
public DateField() {
	this(Settings.getGregorian(), "","  ");
}
	public DateField(Calendar cal) {
		this(cal, "", "");
	}
	public DateField(Calendar cal, String dl, String tl) {
		super();
		date = (GregorianCalendar) cal.clone();
		dayLabel = new Label(dl);
		timeLabel = new Label(tl);
		initialize();
	
	}
	public GregorianCalendar getDate() {
		int[] yearMonthDay = dayField.getDate();
		date.set(Calendar.YEAR, yearMonthDay[0]);
		date.set(Calendar.MONTH, yearMonthDay[1]);
		date.set(Calendar.DATE, yearMonthDay[2]);
		date.set(Calendar.HOUR_OF_DAY, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);
		date.add(Calendar.MINUTE, (timeField.getTimeInMinutes()));
		return (GregorianCalendar) date.clone();
	}
/**
 * Insert the method's description here.
 * Creation date: (03.07.00 16:29:37)
 */
private void initialize() {
	setLayout(null);
			
	dayField = new DayField();
	timeField = new TimeField();
	setFields();

	//add(dayLabel, constraints);
	
	add(dayField);
	dayField.setVisible(true);
	dayField.setBounds(1, 1, 170, 27);
	
	//add(timeLabel, constraints);	
	
	add(timeField);
	timeField.setVisible(true);
	timeField.setBounds(172, 1, 50, 23);
}
	public void setDate(GregorianCalendar greg) {
		date = (GregorianCalendar) greg.clone();
		setFields();
	}
/**
 * Insert the method's description here.
 * Creation date: (03.07.00 16:43:33)
 */
private void setFields() {
	dayField.setDate(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE));
	timeField.setTime(date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE));
}
}
