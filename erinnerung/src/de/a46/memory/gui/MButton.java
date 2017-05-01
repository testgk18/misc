// Copyright (c) 2001 by till zoppke, blaulicht@a46.de
package de.a46.memory.gui;

import java.awt.Graphics;
import java.awt.Image;

/**
 * Class <code>MButton</code> represents a button that is drawing different images corresponding
 * to its state. States are inherited from class <code>MPanel</code>.
 *
 * @author Till Zoppke
 * @version 1.0
 */
public class MButton extends MPanel {

  // images (5) to draw on this button
  private Image[] images;

  /**
   * the upper left x index to paint the image
   */
  protected int x = 0;

  /**
   * the upper left y index to paint the image
   */
  protected int y = 0;

  /**
   * Constructor.
   *
   * @param id      an <code>int</code> to identify this <code>MButton</code>.
   * @param images  an array of <code>Image</code>, size 5. They correspond to 5 possible states
   *                defined by class <code>MPanel</code>.
   */
  public MButton(int id, Image[] images) {
    super(id);
    this.images = images;
  }

  /**
   * Constructor.
   *
   * @param images  an array of <code>Image</code>, size 5. They correspond to 5 possible states
   *                defined by class <code>MPanel</code>.
   * @param id      an <code>int</code> to identify this <code>MButton</code>.
   * @param x       an <code>int</code> as x-coordinate of the upper-left corner for the image
   * @param y       an <code>int</code> as y-coordinate of the upper-left corner for the image
   */
  public MButton(Image[] images, int id, int x, int y) {
    this(id, images);
    this.x = x;
    this.y = y;
  }

  /**
   * just calles a repaint.
   */
  protected void statusChanged() {
    this.repaint();
  }

  /**
   * draws the image corresponding to the actual state.
   */
  public void paint (Graphics g) {
    g.drawImage(this.images[this.getStatus()], this.x, this.y, this);
  }

}