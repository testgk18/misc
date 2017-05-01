// Copyright (c) 2001 by till zoppke, blaulicht@a46.de
package de.a46.memory.gui;

import java.awt.Image;
import java.awt.Graphics;

/**
 * Class <code>HiddenCardPanel</code> represents one card in the field. It has inherited states
 * from class <code>MButton</code> plus three additional ones. You also can pass a background
 * image. If this is <code>null</code>, no changes in display are happening. If
 * this is not <code>null</code>, it will be partially painted instead of the
 * common image of the inherited images-array, when status is either DISABLED or
 * ENABLED. That means: not by HIGHLIGHT, not by ROLLOVER.
 *
 * @author Till Zoppke
 * @version 1.0
 */
public class HiddencardPanel extends MButton {

  /**
   * encoding additional state. Card reactes like disabled, but shows a different
   * image, symbolizing that this card's image is shown on the upper left.
   */
  public static final int HIGHLIGHT_UPPER = 5;

  /**
   * encoding additional state. Card reactes like disabled, but shows a different
   * image, symbolizing that this card's image is shown on the lower left.
   */
  public static final int HIGHLIGHT_LOWER = 6;

  /**
   * encoding additional state. Card is removed from its parent component, so
   * invisible, and nothing must be painted.
   */
  public static final int OFF_STATE = -1;

  // backgroundImage to be painted instead common Images, if not null.
  private Image backgroundImage = null;

  /**
   * constructs a new <code>HiddenCardPanel</code>.
   *
   * @param images  an <code>Image[]</code> array of size 7. Images to be painted corresponding
   *                to predefined states.
   * @param id      an <code>int</code> for identifying this instance. Also this will be used
   *                to determine, which part of the backgroundImage is painted.
   * @param x       an <code>int</code> as the number of pixel space between border and image
   *                in horizontal direction.
   * @param y       an <code>int</code> as the number of pixel space between border and image
   *                in vertical direction.
   */
  public HiddencardPanel(Image[] images, int id, int x, int y) {
    super(images, id, x, y);
    this.status = OFF_STATE;
  }

  /**
   * setting the background image. This may be <code>null</code>.
   *
   * @param img the new background image.
   */
  public void setBackgroundImage(Image img) {
    this.backgroundImage = img;
  }

  /**
   * paints this component corresponding to its state.
   */
  public void paint(Graphics g) {
    if (this.backgroundImage != null && (
          this.getStatus() == MPanel.ENABLED || this.getStatus() == MPanel.DISABLED))
    {
      g.drawImage(
        this.backgroundImage,
        this.x,
        this.y,
        80 - this.x,
        80 - this.y,
        (this.id % 6) * this.backgroundImage.getWidth(this) / 6,
        (this.id / 6) * this.backgroundImage.getHeight(this) / 6,
        ((this.id % 6) + 1) * this.backgroundImage.getWidth(this) / 6,
        ((this.id / 6) + 1) * this.backgroundImage.getHeight(this) / 6,
        this
      );
    }
    else
      super.paint(g);
  }

  /**
   * sets the status of this <code>HiddencardPanel<code>. A repaint will be done if
   * necessary.
   */
  public void setStatus(int status) {
    if ( this.status == ENABLED && status == DISABLED
      || this.status == DISABLED && status == ENABLED)
    {
      this.status = status; // avoid repainting
    }
    else
      super.setStatus(status);
  }
}