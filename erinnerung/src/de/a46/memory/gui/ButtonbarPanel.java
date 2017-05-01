// Copyright (c) 2001 by till zoppke, blaulicht@a46.de
package de.a46.memory.gui;

import java.awt.Panel;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Vector;

/**
 * Class <code>ButtonbarPanel</code> represents the button bar of the memory gui. Its size
 * is fixed 720 x 22 pixel, it is located to the upper border.
 * Interface <code>MPanelListener</code> is implemented, because three
 * <code>MButton</code>s are contained. A {@link ButtonbarListener} can
 * be added to get Events, when those buttons are clicked.
 *
 * @author Till Zoppke
 * @version 1.0
 */
public class ButtonbarPanel extends Panel implements MPanelListener {

  // encoding buttonPanel IDs
  private static final int NEWGAME = 0;
  private static final int HELP = 1;
  private static final int EXIT = 2;

  // vector containing registered UserActionListeners
  private Vector listenerVector = new Vector(1);

  // counting fails of getting equal images. Displayed to the upper right.
  private int trialsCounter = 0;

  // MButtons
  private MButton newgameButton;
  private MButton helpButton;
  private MButton exitButton;

  // labeling the trialsCounter ("Zähler").
  private Image trialsImage;

  // array of 10 images representing the ten numerals 0..9
  private Image[] numeralImages;

  /**
   * Constructs a new instance by getting images from the
   * <code>ressourceContgainer</code>. All Subcomponents (mbuttons) getting
   * constructed, too.
   *
   * @param ressourceContainer A <code>RessourceContainer</code> providing images for this
   *                           component and all subcomponents.
   */
  public ButtonbarPanel(RessourceContainer ressourceContainer) {
    this.setLayout(null);
    this.trialsImage = ressourceContainer.getTrialsImage();
    this.numeralImages = ressourceContainer.getNumeralImages();

    // create buttons
    this.newgameButton = new MButton(NEWGAME, ressourceContainer.getNewgameImages());
    this.helpButton = new MButton(HELP, ressourceContainer.getHelpImages());
    this.exitButton = new MButton(EXIT, ressourceContainer.getExitImages());
  }

  /**
   * initializes this component. Subcomponents are sized, added, and initialized.
   */
  public void init() {

    // add buttons and label
    this.add(this.newgameButton);
    this.newgameButton.setBounds(5, 0, 105, 22);
    this.add(this.helpButton);
    this.helpButton.setBounds(200, 0, 140, 22);
    this.add(this.exitButton);
    this.exitButton.setBounds(110, 0, 90, 22);

    // init buttons
    this.newgameButton.init();
    this.helpButton.init();
    this.exitButton.init();

    // add listeners
    this.newgameButton.addMPanelListener(this);
    this.helpButton.addMPanelListener(this);
    this.exitButton.addMPanelListener(this);
  }

  /**
   * Paints this component. That means, the current trialsCounter will be drawn.
   */
  public void paint(Graphics g) {
    g.drawImage(this.trialsImage, 617, 0, this);
    g.drawImage(this.numeralImages[this.trialsCounter / 100], 692, 0, this);
    g.drawImage(this.numeralImages[(this.trialsCounter / 10) % 10], 700, 0, this);
    g.drawImage(this.numeralImages[this.trialsCounter % 10], 708, 0, this);
  }

  /**
   * Sets field <code>trialsCounter</code> to the specified value.
   *
   * @param value the new value for field <code>trialsCounter</code>.
   */
  public void incrementTrialsCounter() {
    this.trialsCounter++;
    this.repaint(692, 0, 24, 22);
  }

  /**
   * Registers a <code>UserActionListener</code>.
   *
   * @param listener A <code>UserActionListener</code> to be added.
   */
  public void addButtonbarListener(ButtonbarListener listener) {
    this.listenerVector.addElement(listener);
  }

  //////////////////// MPanelListener methods ////////////////

  /**
   * This method is called
   * automatically, if one of the three buttons will be clicked. Corresponding
   * methods will of any registered <code>userActionListener</code> will be
   * called. Also this buttonbarPanel will be disabled.
   *
   * @param e incoming <code>MPanelEvent</code>, that will be examined by {@link MButton#getID()}.
   */
  public void mpanelClicked(MPanel source) {
    this.setEnabled(false);

    switch (source.getID()) {

      case NEWGAME:
        for (int i=0; i<this.listenerVector.size(); ++i)
          ((ButtonbarListener) this.listenerVector.elementAt(i)).newgameButtonClicked();
        break;

      case HELP:
        for (int i=0; i<this.listenerVector.size(); ++i)
          ((ButtonbarListener) this.listenerVector.elementAt(i)).helpButtonClicked();
        break;

      case EXIT:
        for (int i=0; i<this.listenerVector.size(); ++i)
          ((ButtonbarListener) this.listenerVector.elementAt(i)).exitButtonClicked();
    }// end of switch
  }

  /**
   * empty
   */
  public void mpanelExited(MPanel source) {}

  /**
   * empty
   */
  public void mpanelEntered(MPanel source) {}

  /**
   * Changes the eabled-state of this <code>buttonbarPanel</code> and all
   * subcomponents. That means, rollover and clicks are switched on/off.
   *
   * @param b a <code>boolean</code> describing whether to switch on or off.
   */
  public void setEnabled(boolean b) {
    this.newgameButton.setEnabled(b);
    this.helpButton.setEnabled(b);
    this.exitButton.setEnabled(b);
  }

  /**
   * resets this component to the default state for a new game.
   */
  public void initCurrentData() {
    this.setEnabled(true);
    this.trialsCounter = 0;
  }
}