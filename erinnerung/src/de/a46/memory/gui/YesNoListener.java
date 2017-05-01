// Copyright (c) 2001 by till zoppke, blaulicht@a46.de
package de.a46.memory.gui;

/**
 * Interface <code>YesNoListener</code> describes a dialog, where the user can
 * answer yes or no. The object wanting info about those click has to implement
 * this interface and register itself as listener.
 *
 * @author Till Zoppke
 * @version 1.0
 */
public interface YesNoListener {

  /**
   * Called, when the user confirms the action.
   */
  public void yesButtonClicked();

  /**
   * Called, when the user cancels the action.
   */
  public void noButtonClicked();
}