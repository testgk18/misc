// Copyright (c) 2001 by till zoppke, blaulicht@a46.de
package de.a46.memory.gui;

import java.awt.Image;
import java.applet.AudioClip;

/**
 * This interface defines a number of multimedia resources needed to display
 * the memory.
 *
 * @author Till Zoppke
 * @version 1.0
 */
public interface RessourceContainer {

  /**
   * returns one image (720 x 502) to be displayed while loading the others.
   */
  public Image getWelcomeImage();

  /**
   * returns an array of 7 images (74 x 74) to be shown as cardBackImages on the
   * field.
   */
  public Image[] getHiddencardImages();

  /**
   * returns an array of 5 images (nn x 22) to be drawn on the <code>ButtonbarPanel</code>
   * as newgameButton.
   */
  public Image[] getNewgameImages();

  /**
   * returns an array of 5 images (nn x 22) to be drawn on the <code>ButtonbarPanel</code>
   * as helpButton.
   */
  public Image[] getHelpImages();

  /**
   * returns an array of 5 images (nn x 22) to be drawn on the <code>ButtonbarPanel</code>
   * as exitButton.
   */
  public Image[] getExitImages();

  /**
   * returns an array of 10 images (8 x 22) to be drawn on the <code>ButtonbarPanel</code>
   * as counter numerals.
   */
  public Image[] getNumeralImages();

  /**
   * returns one image to be drawn as trialscounter label on the <code>ButtonbarPanel</code>.
   */
  public Image getTrialsImage();

  /**
   * returns an array of 2 background images (480 x 480) to be drawn on the
   * <code>FieldPanel</code> and as hiddencard back. The first image may be <code>null</code>.
   */
  public Image[] getCurrentBackgroundImages();

  /**
   * returns an array of 36 images (234 x 234) for one game of memory.
   */
  public Image[] getCurrentCardImages();

  /**
   * returns an array of 36 <code>AudioClips</code> for one game of memory.
   */
  public AudioClip[] getCurrentCardAudios();

  /**
   * returns a 2 dimensional array of Strings containing the help text to be drawn
   * on the <code>HelpPanel</code>.
   */
  public String[][] getHelptext();
}