package ti.camview;

import java.io.File;
import java.util.*;
import java.text.*;
import java.awt.*;

/**
 * Handles a webcam. Contains all specific information you will need to produce a <code>Film</code>-Object.
 * @author till zoppke
 * @version %I%, %G%
 * @see ti.camview.Show
*/

public class Camera {
	private String title;  // The Camera's name; to be displayed in the window
	private String archivePath; // Pathname containing the picture archive
	private GregorianCalendar firstPicture; // When the Camera shot its first picture
	private GregorianCalendar lastPicture; // value == null, if Camera still is shoting

	private java.awt.Dimension size = null;
	private java.text.SimpleDateFormat fileMask;
	private java.text.SimpleDateFormat pathMask;
/**
 * Insert the method's description here.
 * Creation date: (10.08.00 15:27:21)
 * @param path java.lang.String
 */
public Camera(String path) {
	this("no name", path, new java.util.GregorianCalendar(1950,1,1));
	
	}
	public Camera (String name, String pfad, Calendar d) {
		this (name, pfad, d, null);
	}
	public Camera(String n, String p, Calendar d1, Calendar d2) {
		title = n;
		archivePath = p;
		try {
			firstPicture = (GregorianCalendar) d1.clone();
		} catch (NullPointerException exc) {
			firstPicture = null;
		}
		if (d2 == null)
			lastPicture = null;
		else
			lastPicture = (GregorianCalendar) d2.clone();
		initialize();
	}
	public Camera(String n, String p, Calendar d1, Calendar d2, Dimension dim) {
		size = dim;
		title = n;
		archivePath = p;
		try {
			firstPicture = (GregorianCalendar) d1.clone();
		} catch (NullPointerException exc) {
			firstPicture = null;
		}
		if (d2 == null)
			lastPicture = null;
		else
			lastPicture = (GregorianCalendar) d2.clone();
		initialize();
	}
/**
 * Insert the method's description here.
 * Creation date: (07.07.00 16:12:06)
 * @return java.lang.String
 * @param one java.lang.String
 * @param two java.lang.String
 */
private String bestOfTwo(GregorianCalendar g, String downFile, String upFile, int radius, File downPath, File upPath) {
		long gregorLong, downLong, upLong;

	gregorLong = (g.getTime()).getTime();
	downLong = gregorLong - (fileMask.parse(downFile, new ParsePosition(0))).getTime();
	upLong = fileMask.parse(upFile, new ParsePosition(0)).getTime() - gregorLong;

	if (downLong < upLong)
		{if (downLong < radius * 60000) return downPath + downFile;}
	else
		{if (upLong < radius * 60000) return upPath + upFile;}
	return null;
}
/**
 * Insert the method's description here.
 * Creation date: (02.06.00 15:35:46)
 * @return java.awt.Image
 */
public String getCurrentPicture() {
	GregorianCalendar greg = this.getMaxDate();
	File verzeichnis;
	String f = null;
	Image img;
	SimpleDateFormat filemask = new SimpleDateFormat("/yyyy/MM/dd/", Settings.getLocale());
	filemask.setTimeZone (Settings.getTimeZone());
	String[] fileArray;
	while (f == null) {
		verzeichnis = new File(this.archivePath + filemask.format(greg.getTime()));
		if (verzeichnis.canRead()) {
			fileArray = verzeichnis.list();
			f = "" + verzeichnis + "\\" + fileArray[fileArray.length-1];
		}	else   {
			greg.roll(Calendar.DATE, false);
		}
	}
	return f;
}
	/**
	 * returns the File from a sorted archive,
	 * that is as closest to the requested date.
	*/
	public String getImage (GregorianCalendar gregor, int radius, String oldFile) {
		File path;
		String[] fileArray;

		path = new File(archivePath + pathMask.format(gregor.getTime()));

		if (path.canRead()) {
			fileArray = path.list();
			if (fileArray.length > 0) {
				int gregMinutes = gregor.get(Calendar.HOUR_OF_DAY) * 60 + gregor.get(Calendar.MINUTE);
				int startMinutes = getMinutes(fileArray[0]); 
				int stopMinutes = getMinutes(fileArray[fileArray.length-1]);
				int index = (gregMinutes - startMinutes) * fileArray.length / (stopMinutes - startMinutes);
				if (index < 0) {    //look the day before as well
					GregorianCalendar gregor2 = (GregorianCalendar) gregor.clone();
					gregor2.add(Calendar.DATE, -1);
					File path2 = new File(archivePath + pathMask.format(gregor2.getTime()));
					if (path2.canRead()) {
						String[] fileArray2 = path2.list();
						if (fileArray.length > 0) {
							return bestOfTwo(gregor, fileArray2[fileArray2.length-1], fileArray[0], radius, path2, path);
						}
					}
					return null;
				}
				if (index >= fileArray.length) {    //look the day after as well
					GregorianCalendar gregor2 = (GregorianCalendar) gregor.clone();
					gregor2.add(Calendar.DATE, 1);
					File path2 = new File(archivePath + pathMask.format(gregor2.getTime()));
					if (path2.canRead()) {
						String[] fileArray2 = path2.list();
						if (fileArray.length > 0) {
							return bestOfTwo(gregor, fileArray[fileArray.length-1], fileArray2[0], radius, path, path2);
						}
					}
					return null;
				}
				else {
					// intra-Day search
					while (getMinutes(fileArray[index]) < gregMinutes & index < fileArray.length-1)
						index++;
					while (getMinutes(fileArray[index]) > gregMinutes & index > 0)
						index--;
					int diff1 = gregMinutes - getMinutes(fileArray[index]);
					int diff2 = getMinutes(fileArray[index+1]) - gregMinutes;
					if (Math.min(diff1, diff2) <= radius) {
						if (diff1 <= diff2)
							return path+"\\"+fileArray[index];
						else
							return path+"\\"+fileArray[index + 1];
					}
					return null;
				}
			}
		}

		// look in the other days
		// creating limits
		GregorianCalendar minTime = (GregorianCalendar) gregor.clone();
		minTime.add(Calendar.MINUTE, -radius);
		if (oldFile != null) {
		    GregorianCalendar oldTime = (GregorianCalendar) gregor.clone();
		    String dumString = oldFile.substring(archivePath.length() + 11, archivePath.length() + 24);
		    oldTime.setTime(fileMask.parse(dumString, new ParsePosition(0)));
			if (minTime.before(oldTime)) {
			    minTime = (GregorianCalendar) oldTime.clone();
			}
		}
		else {
			if (minTime.before(lastPicture)) {
	    	    minTime = (GregorianCalendar) lastPicture.clone();
	    	}
		}

		GregorianCalendar maxTime = (GregorianCalendar) gregor.clone();
		maxTime.add(Calendar.MINUTE, radius);
		if (getMaxDate().before(maxTime)) {
			maxTime = getMaxDate();
		}

		GregorianCalendar upTime = (GregorianCalendar) gregor.clone();
		boolean upFound = false;
		String[] upArray = {""};
		File upPath = null;
		while (upTime.before(maxTime) && !upFound) {
			upTime.add(Calendar.DATE, 1);
			upPath = new File(archivePath + pathMask.format(upTime.getTime()));
			if (upPath.canRead()) {
				upArray = upPath.list();
				if (upArray.length > 0) {
					upFound = true;
				}
			}
		}

		GregorianCalendar downTime = (GregorianCalendar) gregor.clone();
		boolean downFound = false;
		String[] downArray = {""};
		File downPath = null;
		while (downTime.after(minTime) && !downFound) {
			downTime.add(Calendar.DATE, -1);
			downPath = new File(archivePath + pathMask.format(downTime.getTime()));
			if (downPath.canRead()) {
				downArray = downPath.list();
				if (downArray.length > 0) {
					downFound = true;
				}
			}
		}

		if (upFound) {
			if (downFound) {
				return bestOfTwo(gregor, downArray[downArray.length-1], upArray[0], radius, downPath, upPath);
			}
			else {
		    	upTime.setTime(fileMask.parse(upArray[0], new ParsePosition(0)));
		    	if (upTime.before(maxTime))
					return upPath+"\\"+upArray[0];
				else
					return null;
			}
		}
		else {
			if (downFound) {
			 	downTime.setTime(fileMask.parse(downArray[downArray.length-1], new ParsePosition(0)));
		    	if (maxTime.before(downTime))
					return downPath+"\\"+downArray[downArray.length-1];
				else
					return null;
			}
			else return null;
		}
	}
	/**
	 * returns the latest reasonable date to request this camera for
	 * (if this camera finished shooting last year - don't ask it for a today's picture!)
	*/
	public GregorianCalendar getMaxDate() {
		if (lastPicture != null)
			return (GregorianCalendar) lastPicture.clone();
		else {
			GregorianCalendar gregor = new GregorianCalendar(Settings.getTimeZone());
			gregor.setTime(new Date());
			return gregor;
		}
	}
	/**
	 * returns Camera's first shot picture
	 * @return java.util.GregorianCalendar
	*/

	public Calendar getMinDate() {
		return (GregorianCalendar) firstPicture.clone();
	}
/**
 * Insert the method's description here.
 * Creation date: (07.07.00 15:10:51)
 * @return int
 */
private static int getMinutes(String s) throws java.lang.NumberFormatException {
	int h, m;
	try {
		h = Integer.parseInt(s.substring(9, 11));
		m = Integer.parseInt(s.substring(11,13));
	}
	catch (java.lang.IndexOutOfBoundsException exc) {
		throw new java.lang.NumberFormatException();
	}
	return m+h*60;
}
/**
 * Insert the method's description here.
 * Creation date: (10.08.00 13:44:37)
 * @return java.awt.Dimension
 */
public Dimension getSize() {
	return size;
}
/**
 * Insert the method's description here.
 * Creation date: (10.08.00 13:48:20)
 * @return java.lang.String
 */
public String getWindowTitle() {
	return title;
}
/**
 * Insert the method's description here.
 * Creation date: (07.07.00 14:56:57)
 */
private void initialize() {
	pathMask = new SimpleDateFormat("/yyyy/MM/dd/", Settings.getLocale());
	pathMask.setTimeZone (Settings.getTimeZone());
	fileMask = new SimpleDateFormat("yyyyMMdd.hhmm", Settings.getLocale());
	fileMask.setTimeZone (Settings.getTimeZone());

}
}
