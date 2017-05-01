// Copyright (c) 2001 by till zoppke, blaulicht@a46.de
package de.a46.memory.gui;

/**
 * Interface <code>ButtonbarListener</code> defines methods that will be called if a mouseclick
 * happens to a button in the {@link ButtonbarPanel}.
 *
 * @author Till Zoppke
 * @version 1.0
 */
public interface ButtonbarListener {

  /**
   * Called when there was a mouseclick on the help button.
   */
  public void helpButtonClicked();

  /**
   * Called when there was a mouseclick on the exit button.
   */
  public void exitButtonClicked();

  /**
   * Called when there was a mouseclick on the newgame button.
   */
  public void newgameButtonClicked();
}