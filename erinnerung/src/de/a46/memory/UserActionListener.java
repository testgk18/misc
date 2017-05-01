// Copyright (c) 2001 by till zoppke, blaulicht@a46.de
package de.a46.memory;

/**
 * Interface <code>UserActionListener</code> bundels all methods, that can be
 * called by the gui layer, when an event occured. In the memory this is done
 * by class {@link de.a46.memory.gui.MainPanel}.
 *
 * @author Till Zoppke
 * @version 1.0
 */
public interface UserActionListener {

  /**
   * This is called when the user clicks a card in the field.
   *
   * @param cardID an <code>int</code> specifying the clicked card.
   */
  public void cardClicked(int cardID);

  /**
   * This is called when the user clicks a big image to the left.
   */
  public void bigImageClicked();

  /**
   * This is called when the user wants to quit the game.
   */
  public void exitAction();

  /**
   * This is called when the user wants to restart the game.
   */
  public void newgameAction();
}