// Copyright (c) 2001 by till zoppke, blaulicht@a46.de
package de.a46.memory.gui;

/**
 * This interface defines three methods, that will be called when events
 * occure on a {@link MPanel}. This interface is similar to interface
 * <code>MouseListener</code>.
 *
 * @author Till Zoppke
 * @version 1.0
 */
public interface MPanelListener {

  /**
   * this method is called, when the mouse clicks a <code>MPanel</code>.
   *
   * @param source the <code>MPanel</code> on which the event occured.
   */
  public void mpanelClicked(MPanel source);

  /**
   * this method is called, when the mouse enters a <code>MPanel</code>.
   *
   * @param source the <code>MPanel</code> on which the event occured.
   */
  public void mpanelEntered(MPanel source);

  /**
   * this method is called, when the mouse exits a <code>MPanel</code>.
   *
   * @param source the <code>MPanel</code> on which the event occured.
   */
  public void mpanelExited(MPanel source);
}