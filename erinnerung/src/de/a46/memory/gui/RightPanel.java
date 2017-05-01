// Copyright (c) 2001 by till zoppke, blaulicht@a46.de
package de.a46.memory.gui;

import java.awt.Panel;

/**
 * Class <code>RightPanel</code> is an abstract superclass for all panels been shown as right
 * components in the mainPanel. These are the classes {@link FieldPanel}, {@link HelpPanel}
 * and {@link ExitPanel}. All have same size of 480 x 480 pixel and need to implement the two
 * methods defined below.
 *
 * @author Till Zoppke
 * @version 1.0
 */
public abstract class RightPanel extends Panel {

  /**
   * Constructor. Layout is set to <code>null</code>
   */
  public RightPanel() {
    super(null);
  }

  /**
   * repaints this component and all subcomponents.
   */
  public abstract void repaintAll();

  /**
   * initializes this component and all subcomponents.
   */
  public abstract void init();
}