// Copyright (c) 2001 by till zoppke, blaulicht@a46.de
package de.a46.memory.gui;

import java.awt.Color;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.FontMetrics;

/**
 * This grafical class allows to show an unlimited number of images. Also a line of text
 * can be drawn on this <code>MultiImagePanel</code>. In the memory this class is used to show
 * the bigImages on the {@link LeftPanel}. Events are casted according to class <code>MPanel</code>.
 *
 * @author Till Zoppke
 * @version 1.0
 */
public class MultiImagePanel extends MPanel {

  // images to be shown
  private Image[] images;

  // remembers currently shown image. Default value -1 means: ne image.
  private int imageID = -1;

  // line of text to be drawn on this component
  private String text = null;

  /**
   * Constructor, that sumply calls the superconstructor.
   *
   * @param panelID an <code>int</code> as ID for this <code>MultiImagePanel</code>.
   */
  public MultiImagePanel(int panelID) {
    super(panelID);
  }

  /**
   * returns the current imageID
   *
   * @return an <code>int</code> as the current image's index in the images array.
   */
  public int getImageID() {
    return this.imageID;
  }

  /**
   * sets the current image by its index in the images array. If the new one is
   * different, a repaint will be done.
   *
   * @param imageID an <code>int</code> identifying the new image in the images array.
   */
  public void setImage(int imageID) {
    if (this.imageID != imageID) {
      this.imageID = imageID;
      this.imageIDChanged();
    }
  }

  /**
   * sets the line of text to a new value. If this is different from the old value,
   * a repaint will be caused.
   *
   * @param text  a <code>String</code> containing the text to be drawn. If its
   *              value is <code>null</code>, no text will be drawn.
   */
  public void setText(String text) {
    if (this.text == null && text == null) return;
    if ( (this.text == null && text != null)
      || (this.text != null && text == null)
      || !this.text.equals(text))
    {
      this.text = text;
      this.textChanged();
    }
  }

  // just call repaint
  private void textChanged() {
    this.repaint();
  }

  // just call repaint
  private void imageIDChanged() {
    this.repaint();
  }

  /**
   * sets the array of images.
   *
   * @param images the new image array.
   */
  public void setImages(Image[] images) {
    this.images = images;
  }

  /**
   * returns the currently shown image.
   *
   * @return the image currently shown. If no image shown, <code>null</code> will be returned.
   */
  public Image getImage() {
    if (this.imageID == -1)
      return null;
    else
      return this.images[this.imageID];
  }

  /**
   * paints this component. At first the current Image will be drawn, if one is
   * selected. Then the current text, if not <code>null</code>.
   */
  public void paint (Graphics g) {

    // draw image
    if (this.imageID >= 0) {
      g.drawImage(this.images[this.imageID],0 ,0 , this);
    }

    // draw text
    if (this.text != null) {
      Font font = new Font("arial", Font.PLAIN, 17);
      g.setFont(font);
      FontMetrics fontMetrics = g.getFontMetrics();
      int w = fontMetrics.stringWidth(this.text);
      int h = fontMetrics.getHeight();
      int x = (this.getSize().width - w) / 2;
      int y = (this.getSize().height - h) / 2;
      g.setColor(new Color(255, 255, 231));
      g.fillRect(x-4, y-1, w+8, h+3);
      g.setColor(Color.black);
      g.drawRect(x-4, y-1, w+8, h+3);
      g.drawString(this.text, x, y+h-3);
    }
  }
}