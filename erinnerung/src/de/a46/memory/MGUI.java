// Copyright (c) 2001 by till zoppke, blaulicht@a46.de
package de.a46.memory;

import java.awt.Image;

/**
 * Interface <code>MGUI</code> has to be implemented by the gui-layer.
 * All methods, that are called by a {@link Game} are
 * collected here.
 *
 * @author Till Zoppke
 * @version 1.0
 */

public interface MGUI {

  /**
   * sets the bigImage at a specified bigImagePanel.
   *
   * @param locationID an <code>int</code> as defined by {@link de.a46.memory.gui.LeftPanel#UPPER}
   *                   / {@link de.a46.memory.gui.LeftPanel#LOWER},
   * @param imageID an <code>int</code> as the image's ID in the currentCardImages array
   */
  public void setBigImage(int locationID, int imageID);

  /**
   * removes a card from the field.
   *
   * @param cardID an <code>int</code> (0..35) to identify the card.
   */
  public void removeCard(int cardID);

  /**
   * sets the status of the specified card.
   *
   * @param cardID an int (0..35) to identify the card,
   * @param status a <code>boolean</code>. <code>true</code> = enabled,
   *               <code>false</code> = disabled.
   */
  public void setCardEnabled(int cardID, boolean status);

  /**
   * checks, if there are two same images open.
   *
   * @return <code>true</code> if a pair is found, <code>false</code> otherwise.
   */
  public boolean hasEqualImages();

  /**
   * increments the trialscounter by one.
   */
  public void incrementTrialsCounter();

  /**
   * sets the enabled status of the card field
   *
   * @param status  a <code>boolean</code>. <code>true</code> = enabled,
   *                <code>false</code> = disabled.
   */
  public void setFieldEnabled(boolean status);

  /**
   * shows or hides the bigImage hint, where to click the cards back to the field.
   *
   * @param b a <code>boolean<code>. <code>true</code> = show, <code>false</code> = hide.
   */
  public void setBigimageHint(boolean b);
}