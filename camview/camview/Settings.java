package ti.camview;

import java.util.*;


class Settings {
	private static Locale LOKAL = new Locale("GERMAN", "GERMANY");
	private static SimpleTimeZone TZone = new SimpleTimeZone (1*60*60*1000, "EUROPE/BERLIN");
	private static GregorianCalendar GREGOR;
	//numberOfCameras = 1;

	public static Camera getDefaultCamera() {
		Camera cam;
		GregorianCalendar gregor1 = getGregorian();
		gregor1.set(Calendar.YEAR,1999);
		gregor1.set(Calendar.MONTH,8);
		gregor1.set(Calendar.DATE,18);
		gregor1.set(Calendar.HOUR,17);
		gregor1.set(Calendar.MINUTE,45);
		return new Camera ("Baustelle", "g:\\", gregor1, null, new java.awt.Dimension(352, 288));
	}
	public static GregorianCalendar getGregorian() {
		GREGOR = new GregorianCalendar(TZone, LOKAL);
		return GREGOR;
	}
	public static Locale getLocale() {
		return (Locale) LOKAL.clone();
	}
/**
 * Insert the method's description here.
 * Creation date: (08.08.00 15:21:44)
 * @return int[]
 */
public static int[] getMillisPerPicture() {
	int[] retour = {5000,3000,2000,1200,800, 500, 300, 200, 120, 80, 50, 30, 20, 10};
	return retour;
}
	public static SimpleTimeZone getTimeZone() {
		return (SimpleTimeZone) TZone.clone();
	}
}
