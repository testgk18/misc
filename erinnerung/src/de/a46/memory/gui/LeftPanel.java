// Copyright (c) 2001 by till zoppke, blaulicht@a46.de
package de.a46.memory.gui;

import java.awt.Panel;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;

/**
 * Class <code>LeftPanel</code> is the left part of the memory screen. Size is 240 x 480 pixels.
 * It is child to {@link MainPanel} and father of two {@link MultiImagePanel}s, that can display
 * card Images. To receive events, when the user clicks an Image, you can register
 * a {@link MPanelListener}.
 *
 * @author Till Zoppke
 * @version 1.0
 */
public class LeftPanel extends Panel {

  /**
   * encoding location of <code>MultiImagePanel</code>.
   */
  public static final int UPPER = 0;

  /**
   * encoding location of <code>MultiImagePanel</code>.
   */
  public static final int LOWER = 1;

  // array of two bigImagePanels
  private MultiImagePanel[] bigImagePanels;

  // reference to resourceContainer
  private RessourceContainer ressourceContainer;

  /**
   * Constructor. Subcomponents will be constructed as well.
   *
   * @param ressourceContainer a <code>RessourceContainer</code> providing the currentCardImages.
   */
  public LeftPanel(RessourceContainer ressourceContainer) {
    super(null);
    this.ressourceContainer = ressourceContainer;
    this.bigImagePanels = new MultiImagePanel[] {
      new MultiImagePanel(0),
      new MultiImagePanel(1)
    };
  }

  /**
   * initializes this component and all subcomponents.
   */
  public void init() {
    for (int i = 0; i < 2; i++) {
      this.add(this.bigImagePanels[i]);
      this.bigImagePanels[i].setBounds(3, 3 + i * 240, 234, 234);
      this.bigImagePanels[i].init();
    }
  }

  /**
   * resets this leftPanel. Both imagePanels will be cleared and get new images.
   */
  public void initCurrentData() {
    Image[] currentImages = this.ressourceContainer.getCurrentCardImages();
    for (int i = 0; i < 2; i++) {
      this.bigImagePanels[i].setImages(currentImages);
      this.bigImagePanels[i].setImage(-1);
    }
  }

  /**
   * registeres an <code>MPanelListener</code> to receive events when a bigImage is clicked.
   * This listener is passed immediatly to both <code>MultiImagePanels</code>.
   */
  public void addMPanelListener(MPanelListener listener) {
    this.bigImagePanels[UPPER].addMPanelListener(listener);
    this.bigImagePanels[LOWER].addMPanelListener(listener);
  }

  /**
   * returns the shown-index of an image specified by its index in the images-
   * array. If this image is not shown, -1 will be returned
   *
   * @param imageID an <code>int</code> specifying an image by its fieldIndex.
   * @return        an <code>int</code> encoding the location of the image on this leftPanel.
   *                value is corresponding to static fields UPPER and LOWER.
   *                Returns -1 if image is not shown at all.
   */
  public int getPanelIndex(int imageID) {
    if (this.bigImagePanels[UPPER].getImageID() == imageID)
      return UPPER;
    if (this.bigImagePanels[LOWER].getImageID() == imageID)
      return LOWER;
    return -1;
  }

  /**
   * Returns the index in the images array for the image that is currently shown
   * on the <code>MultiImagePanel</code> encoded by <code>panelID</code>.
   *
   * @param panelID an <code>int</code> encoding the location of the requested
   *                <code>MultiImagePanel</code>.
   * @return        an <code>int</code> as the index in the currentCardImages-Array.
   */
  public int getImageID(int panelID) {
    return this.bigImagePanels[panelID].getImageID();
  }

  /**
   * Checks, whether both multiImagePanels are showing the same motives.
   *
   * @return  <code>true</code> if both images are equal by its reference,<br>
   *          <code>false</code> otherwise.
   */
  public boolean hasEqualImages() {
    return (this.bigImagePanels[UPPER].getImageID() >= 0
        && this.bigImagePanels[LOWER].getImageID() >= 0
        && this.bigImagePanels[UPPER].getImage() == this.bigImagePanels[LOWER].getImage());
  }

  /**
   * Sets the image of an <code>MultiImagePanel</code>. The image is specified by its index in
   * the currentCardImages array, the panel is specified by the <code>UPPER</code>
   * / <code>LOWER</code> constants.
   *
   * @param panelID an <code>int</code> specifying the bigImagePanel to be target
   * @param imageID an <code>int</code> specifying the image to be set.
   */
  public void setBigImage(int panelID, int imageID) {
    this.bigImagePanels[panelID].setImage(imageID);
  }

  /**
   * shows or hides the hint for both multiImagePanels.
   *
   * @param b a <code>boolean</code> describing, whether the hint shall be shown or not.
   */
  public void setBigimageHint(boolean b) {
    this.bigImagePanels[0].setText(b ? "klick" : null);
    this.bigImagePanels[1].setText(b ? "hier" : null);
  }

  /**
   * enables or disables this component.
   *
   * @param a <code>boolean</code>, whether to enable or disable.
   */
  public void setEnabled(boolean b) {
    this.bigImagePanels[0].setEnabled(b);
    this.bigImagePanels[1].setEnabled(b);
  }
}