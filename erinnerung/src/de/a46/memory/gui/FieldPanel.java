// Copyright (c) 2001 by till zoppke, blaulicht@a46.de
package de.a46.memory.gui;

import java.awt.Panel;
import java.awt.Image;
import java.awt.Color;
import java.awt.Graphics;

/**
 * Class <code>FieldPanel</code> belongs to the rightPanel family. It has a fixed size of
 * 480 x 480 pixels. It displays 36 {@link HiddencardPanel}s in a square. BackgroundImages
 * are taken from a {@link RessourceContainer}. One is painted as background in the words
 * meaning, instead of a removed card a part will be painted. The other is taken
 * as the card's greenback. ("Vordergrund wird Hintergrund").
 *
 * @author Till Zoppke
 * @version 1.0
 */
public class FieldPanel extends RightPanel {

  // encoding backgroundImage indices
  private static final int BACKGROUND_OLD = 0;
  private static final int BACKGROUND_NEW = 1;

  // resourceContainer to get fresh background images from
  private RessourceContainer ressourceContainer;

  // 36 hiddencardPanels in a square
  private HiddencardPanel[] hiddencardPanels = new HiddencardPanel[36];

  // array of two backgroundImages
  private Image[] backgroundImages;

  /**
   * constructs a new <code>FieldPanel</code>.
   *
   * @param ressourceContainer  A <code>RessourceContainer</code> as reference to get
   *                            background images from.
   */
  public FieldPanel(RessourceContainer ressourceContainer) {
    this.ressourceContainer = ressourceContainer;
  }

  /**
   * initializes this component and all subcomponents.
   */
  public void init() {
    this.setLayout(null);
    for (int i = 0; i < 36; i++) {
      this.hiddencardPanels[i] = new HiddencardPanel(
        this.ressourceContainer.getHiddencardImages(), i, 3, 3
      );
      this.hiddencardPanels[i].init();
      this.hiddencardPanels[i].setBounds((i % 6) * 80, (i / 6) * 80, 80, 80);
    }
  }

  /**
   * refreshes this <code>FieldPanel</code>. This method should be called when starting a new
   * game. All hiddencardPanel will be added back, new backgroundImages are taken
   * from the resourceContainer.
   */
  public void initCurrentData() {
    this.setEnabled(true);
    this.backgroundImages = this.ressourceContainer.getCurrentBackgroundImages();
    for (int i = 0; i < 36; i++) {
      this.hiddencardPanels[i].setBackgroundImage(this.backgroundImages[BACKGROUND_OLD]);
      this.setCardStatus(i, HiddencardPanel.ENABLED);
    }
  }

  /**
   * checks, whether there are any cards visible on the field.
   *
   * @return <code>true</code>, if all hiddencardPanels are removed,<br>
   *         <code>false</code>, if still cards on the field.
   */
  public boolean isEmpty() {
    boolean retour = true;
    for (int i=0; i<this.hiddencardPanels.length; ++i) {
      retour = retour && (this.hiddencardPanels[i].getStatus() == HiddencardPanel.OFF_STATE);
    }
    return retour;
  }

  /**
   * registers an <code>MPanelListener</code>. This listener will be informed, when a card is
   * clicked. The <code>MPanelEvent</code> source is an {@link HiddencardPanel}.
   *
   * @param listener the listener to get registered.
   */
  public void addMPanelListener(MPanelListener listener) {
    for (int i=0; i<this.hiddencardPanels.length; ++i) {
      this.hiddencardPanels[i].addMPanelListener(listener);
    }
  }

  /**
   * sets the status of a hiddenCard. If the card is currently OFF and the new
   * state is not, it will be added and becomes visible again. Otherwise if the
   * new state is OFF, the card will be removed.
   *
   * @param cardID index of the card on the field (0..35)
   * @param status the new state for the <code>HiddencardPanel</code> as encoded by static fields.
   */
  public void setCardStatus(int cardID, int status) {
    int oldState = this.hiddencardPanels[cardID].getStatus();

    // check whether card must be added
    if (oldState == HiddencardPanel.OFF_STATE && status != HiddencardPanel.OFF_STATE)
      this.add(this.hiddencardPanels[cardID]);

    // check whether card must be removed
    if (oldState != HiddencardPanel.OFF_STATE && status == HiddencardPanel.OFF_STATE)
      this.remove(this.hiddencardPanels[cardID]);

    this.hiddencardPanels[cardID].setStatus(status);
  }

  /**
   * paints this component, that means drawing the backgroundImage.
   */
  public void paint(Graphics g) {
    g.drawImage(this.backgroundImages[BACKGROUND_NEW], 0, 0, 480, 480, this);
  }

  /**
   * causes to repaint this component and all hiddenCards.
   */
  public void repaintAll() {
    this.paint(this.getGraphics());
    for (int i=0; i<this.hiddencardPanels.length; i++) {
      if (this.hiddencardPanels[i].getStatus() != HiddencardPanel.OFF_STATE) {
        this.hiddencardPanels[i].repaint();
      }
    }
  }

  /**
   * sets the enabled-state of this fieldPanel. That means, all hiddencards will
   * be enabled/disabled to cast MPanelEvents or to to rolloverEffekts.
   *
   * @param b the <code>boolean</code> value describing the wanted enabled/disabled state.
   */
  public void setEnabled(boolean b) {
    for (int i=0; i<36; ++i) {
      if (this.hiddencardPanels[i].getStatus() != HiddencardPanel.OFF_STATE) {
        if (b)
          this.hiddencardPanels[i].setStatus(HiddencardPanel.ENABLED);
        else
          if (this.hiddencardPanels[i].getStatus() == HiddencardPanel.ENABLED)
            this.hiddencardPanels[i].setStatus(HiddencardPanel.DISABLED);
      }
    }
  }
}// end of fieldpanel