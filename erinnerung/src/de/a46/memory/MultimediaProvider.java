// Copyright (c) 2001 by till zoppke, blaulicht@a46.de
package de.a46.memory;

import java.awt.Image;
import java.applet.AudioClip;
import java.awt.Component;

/**
 * Interface <code>MultimediaProvider</code> defines methods for creating multimediaData.
 *
 * @author Till Zoppke
 * @version 1.0
 */
public interface MultimediaProvider {

  /**
   * creates and returns an image defined by a relative path.
   *
   * @param relativePath a <code>String</code> representing the relative path of the image file.
   * @return the new constructed <code>Image</code>.
   */
  public Image getImage(String relativePath);

  /**
   * creates and returns an AudioClip defined by a relative path.
   *
   * @param relativePath a <code>String</code> representing the relative path of the audio file.
   * @return the new constructed </code>AudioClip</code>.
   */
  public AudioClip getAudioClip(String relativePath);

  /**
   * returns a component. A component is needed to load images through a <code>MediaTracker</code>.
   * In the memory the class Starter just returns an instance to itself.
   *
   * @return a component.
   */
  public Component getComponent();

}