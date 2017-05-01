// Copyright (c) 2001 by till zoppke, blaulicht@a46.de
package de.a46.memory;

import java.util.Properties;
import java.util.Vector;
import java.util.StringTokenizer;
import java.net.URL;

/**
 * Class <code>MProperties</code> provides access to external resources in form ot a properties
 * file. Properties will be loaded and parsed into String arrays. Format is<br>
 * <code>key=value_1 value_2 value_3 ... value_n</code>, different values seperated
 * by blanks.
 *
 * @author Till Zoppke
 * @version 1.0
 */
public class MProperties {

  // properties object to get loaded from file
  private Properties pts = new Properties();

  // url pointing to that file
  private URL url;

  /**
   * Constructor
   *
   * @param url a <code>URL</code> pointing to the properties resource.
   */
  public MProperties(URL url) {
    this.url = url;
  }

  /**
   * initializes this mproperties. In case of load error, the exception tree will
   * be printed to <code>System.err</code>.
   */
  public void init() {
    try { this.pts.load(this.url.openStream()); }
    catch (Exception e) { e.printStackTrace(); }
  }

  /**
   * returns a String array containing all entries mapped to the specified key.
   *
   * @param key a <code>String</code> as the key to the wanted property entries.
   * @return an array of <code>String</code>s containing the values corresponding to the key.
   */
  public String[] getPropertyValues(String key) {

    // init parsing
    Vector v = new Vector();
    StringTokenizer t = new StringTokenizer(this.pts.getProperty(key), " ");

    // parse this line into Vector of Strings
    while (t.hasMoreTokens())
      v.addElement(t.nextToken());

    // convert vector into String array
    String[] retour = new String[v.size()];
    for (int i=0; i<retour.length; ++i) {
      retour[i] = (String) v.elementAt(i);
    }
    return retour;
  }
}