// Copyright (c) 2001 by till zoppke, blaulicht@a46.de
package de.a46.memory;

import java.util.Vector;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.URL;

/**
 * Class <code>TextGetter</code> is used to read a text from a file. This file should be
 * ascii, linefeeds can be WINDOWS, MAC or UNIX. An empty line marks, that a new
 * period is starting. More than one empty line will cause empty periods.
 *
 * @author Till Zoppke
 * @version 1.0
 */
public class TextGetter {

  // pointing to the helptext file
  private URL url;

  // text in periods and lines
  private String[][] text = null;

  /**
   * Constructs a new <code>TextGetter</code>.
   *
   * @param url an <code>URL</code> pointing to the textFile to get read in.
   */
  public TextGetter(URL url) {
    this.url = url;
  }

  /**
   * reads the text from the url specified in the constructor. To get the text,
   * call {@link #getText()}. If there is any error during reading the text, the
   * exception tree is printed to <code>System.err</code>. In this case
   * <code>getText()</code> will return <code>null</code>.
   */
  public void init() {
    Vector allText = new Vector();
    Vector onePeriod = new Vector();
    try {
      // init stream
      BufferedReader in = new BufferedReader(new InputStreamReader(this.url.openStream()));

      // parse file
      String line = "";
      while ((line = in.readLine()) != null) {
        if (!line.equals("")) {
          onePeriod.addElement(line);
        }
        else {
          allText.addElement(onePeriod);
          onePeriod = new Vector();
        }
      }// end of while
      allText.addElement(onePeriod);
    }

    catch (Exception e) { e.printStackTrace(); }

    // convert to String Arrays
    this.text = new String[allText.size()][];

    for (int i = 0; i < allText.size(); i++) {
      Vector periodVector = (Vector) allText.elementAt(i);
      String[] periodString = new String[periodVector.size()];
      for (int j=0; j<periodVector.size(); ++j) {
        periodString[j] = (String) periodVector.elementAt(j);
      }
      this.text[i] = periodString;
    }
  }  // end of init

  /**
   * returns the text as an 2-dimensional array of Strings. First dimension
   * contains all periods, second represents the lines.
   *
   * @return a String[][] that is previously read in by {@link #init()}, otherwise
   *          <code>null</code> if init() not yet has been called, or any error
   *          happened by reading and converting the file.
   */
  public String[][] getText() {
    return this.text;
  }
}