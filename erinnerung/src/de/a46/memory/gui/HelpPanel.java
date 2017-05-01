// Copyright (c) 2001 by till zoppke, blaulicht@a46.de
package de.a46.memory.gui;

import java.awt.Panel;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.Button;
import java.awt.event.ActionListener;

/**
 * Class <code>HelpPanel</code> has the specific size of
 * 480 x 480 pixel and shows a help text. The possible action to a <code>HelpPanel</code> is
 * to close it by clicking a button. To get this event, you have to register an
 * <code>ActionListener</code>.
 *
 * @author Till Zoppke
 * @version 1.0
 */
public class HelpPanel extends RightPanel {

  // button for closing helppanel
  private Button backButton = new Button("zurück zum Spiel");

  // text to be drawn
  private String[][] text;

  /**
   * Constructor.
   *
   * @param text an 2-dimensional Array of Strings. In first Dimension periods
   *              are defined, second Dimension contains lines of text.
   */
  public HelpPanel(String[][] text) {
    this.text = text;
  }

  /**
   * initializes this component and the backbutton as its subcomponent.
   */
  public void init() {
    this.backButton.setBounds(325, 430, 125, 24);
    this.backButton.setFont(new Font("arial", Font.PLAIN, 16));
    this.add(this.backButton);
    this.backButton.setBackground(Color.white);
  }

  /**
   * registers an <code>ActionListener</code>.
   *
   * @param listener  an <code>ActionListener</code>, that will be informed when the backButton
   *                  is clicked.
   */
  public void addActionListener(ActionListener listener) {
    this.backButton.addActionListener(listener);
  }

  /**
   * Draws the helpText to the screen. Font is arial, size by 16 points.
   */
  public void paint(Graphics g) {
    synchronized(this) {
      g.setColor(Color.white);
      g.drawRect(0, 0, 480, 480);
    }
    g.setColor(Color.black);
    g.setFont(new Font("arial", Font.PLAIN, 16));
    int y = 30;
    for (int i=0; i<this.text.length; ++i) {
      for (int j=0; j<this.text[i].length; ++j) {
        g.drawString(this.text[i][j], 20, y+=20);
      }
      y+=15;
    }
  }

  /**
   * repaints this component and the subcomponent-backbutton.
   */
  public void repaintAll() {
    this.paint(this.getGraphics());
  }
}