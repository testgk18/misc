// Copyright (c) 2001 by till zoppke, blaulicht@a46.de
package de.a46.memory;

import java.net.URL;

/**
 * Interface <code>ParameterProvider</code> bundles methods providing paths to external
 * resources. Its implementation in class <code>Starter</code> returns information,
 * that was passed from the embedding html-site via applet parameter tags.
 *
 * @author Till Zoppke
 * @version 1.0
 */
public interface ParameterProvider {

  ///////////////////// paths to external files /////////////////////

  /**
   * Returns an <code>URL</code> pointing to the properties file.
   */
  public URL getPropertiesURL();

  /**
   * Returns an <code>URL</code> pointing to the multimedias file.
   */
  public URL getMultimediasURL();

  /**
   * Returns an <code>URL</code> pointing to the helptext file.
   */
  public URL getHelptextURL();

  //////////////////// base directories for resources ////////////////////

  /**
   * Returns a <code>String</code> containing the relative path to the image base directory.
   */
  public String getImageBase();

  /**
   * Returns a <code>String</code> containing the relative path to the audio base directory.
   */
  public String getAudioBase();

  /**
   * Returns a <code>String</code> containing the relative path to the data base directory.
   */
  public String getDataBase();

  /**
   * Returns a <code>String</code> containing the relative path to the background base directory.
   */
  public String getBackgroundBase();

}