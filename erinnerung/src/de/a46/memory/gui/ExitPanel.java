// Copyright (c) 2001 by till zoppke, blaulicht@a46.de
package de.a46.memory.gui;

import java.awt.Panel;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Font;
import java.awt.Color;
import java.awt.Button;
import java.awt.FontMetrics;
import java.util.Vector;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class <code>ExitPanel</code> represents a confirm-close dialog. It has fixed
 * size of 480 x 480 pixel and is located as rightPanel in the {@link MainPanel}. You
 * can add a {@link YesNoListener} to recieve events, if one of the buttons is clicked.
 *
 * @author Till Zoppke
 * @version 1.0
 */
public class ExitPanel extends RightPanel {

  // array of three images (question, yesbutton, nobutton)
  private Image[] exitImages;

  // two buttons
  private Button yesButton = new Button("ja");
  private Button noButton = new Button("nein");

  // text to be displayed
  private String question = "Erinnerung wirklich beenden?";

  // a vector containing all YesNoListeners that are registered
  private Vector listenerVector = new Vector();

  /**
   * initializes this <code>ExitPanel</code> and all of its subcomponents.
   */
  public void init() {
    this.setBackground(Color.white);
    this.yesButton.setBackground(Color.white);
    this.noButton.setBackground(Color.white);
    this.yesButton.setBounds(190, 255, 25, 22);
    this.noButton.setBounds(250, 255, 40, 22);
    this.add(this.yesButton);
    this.add(this.noButton);
    this.yesButton.setFont(new Font("arial", Font.PLAIN, 17));
    this.noButton.setFont(new Font("arial", Font.PLAIN, 17));
    this.yesButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        for (int i=0; i<listenerVector.size(); ++i)
          ((YesNoListener) listenerVector.elementAt(i)).yesButtonClicked();
      }
    });
    this.noButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        for (int i=0; i<listenerVector.size(); ++i)
          ((YesNoListener) listenerVector.elementAt(i)).noButtonClicked();
      }
    });
  }

  /**
   * paints this component, that means drawing the question and a white background.
   */
  public void paint(Graphics g) {
    g.setColor(Color.white);
    g.fillRect(0, 0, 480, 480);
    g.setColor(Color.black);
    g.setFont(new Font("arial", Font.PLAIN, 18));
    FontMetrics fm = g.getFontMetrics();
    g.drawString(this.question, (480 - fm.stringWidth(this.question)) / 2, 205);
  }

  /**
   * repaints this component and all subcomponents.
   */
  public void repaintAll() {
    this.paint(this.getGraphics());
  }

  /**
   * registers a <code>YesNoListener</code>, that will be informed when a button is clicked.
   *
   * @param listener A <code>YesNoListener</code> to be registered.
   */
  public void addYesNoListener(YesNoListener listener) {
    this.listenerVector.addElement(listener);
  }

}