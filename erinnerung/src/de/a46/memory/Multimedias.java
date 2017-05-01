// Copyright (c) 2001 by till zoppke, blaulicht@a46.de
package de.a46.memory;

import java.util.Vector;
import java.util.StringTokenizer;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.URL;

/**
 * Class <code>Multimedias</code> provides access to an external resource file. This file
 * containes a list of cardImages and cardAudios. Format is<br>
 * <code>_imageFile_ _audioFile_</code> per one line, separated by any number of spaces or tabs.
 *
 * @author Till Zoppke
 * @version 1.0
 */
public class Multimedias {

  // url pointing to the multimedias file
  private URL url;

  // paths to all image and audio resources
  private String[] imageStrings = null;
  private String[] audioStrings = null;

  /**
   * Constructor.
   *
   * @param url a <code>URL</code> pointing to the multimedias file.
   */
  public Multimedias(URL url) {
    this.url = url;
  }

  /**
   * initializes multimediaString arrays by parsing the multimedias file. If any
   * error occures, there will be a exception tree printout to the <code>System.err</code>.
   */
  public void init() {
    Vector vec = new Vector();
    try {
      // init stream
      BufferedReader in = new BufferedReader(new InputStreamReader(this.url.openStream()));

      // parse file
      String line = "";
      while ((line = in.readLine()) != null) {
        if (!line.startsWith("#")) {
          line = line.trim();
          if (!line.equals("")) {
            vec.addElement(line);
          }
        }
      }// end of while
    }

    catch (Exception e) { e.printStackTrace(); }

    // convert to String Arrays
    this.imageStrings = new String[vec.size()];
    this.audioStrings = new String[vec.size()];
    String line;
    for (int i = 0; i < vec.size(); i++) {
      line = (String) vec.elementAt(i);
      line = line.replace('\t', ' ');
      this.imageStrings[i] = line.substring(0, line.indexOf(' '));
      this.audioStrings[i] = line.substring(line.lastIndexOf(' '));
    }
  }  // end of init

  /**
   * returns an array of imageStrings as previously parsed.
   *
   * @return  a <code>String</code> array containing paths to image files.
   *          <code>null</code> if any error by parsing.
   */
  public String[] getImageStrings() {
    return this.imageStrings;
  }

  /**
   * returns an array of audioStrings as previously parsed.
   *
   * @return  a <code>String</code> array containing paths to audio files.
   *          <code>null</code> if any error by parsing.
   */
  public String[] getAudioStrings() {
    return this.audioStrings;
  }
}
