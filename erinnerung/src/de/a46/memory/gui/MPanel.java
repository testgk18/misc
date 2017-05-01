// Copyright (c) 2001 by till zoppke, blaulicht@a46.de
package de.a46.memory.gui;

import java.awt.Panel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

/**
 * this class reacts like a button. There are 5 states defined. A {@link MPanelListener}
 * can be registered to receive MPanel events similar to <code>MouseEvent</code>s.<br>
 * <br>
 * Note: it is not possible achieve the implemented behaviour by a common
 * MouseListener. The order << mousePressed(), mouse dragged, mouseReleased() >>
 * is not taken as a mouseClicked. So eventcasting is reimplemented here.
 *
 * @author Till Zoppke
 * @version 1.0
 */
public class MPanel extends Panel {

  /**
   * encoding normal state: rollover and klicking possible, enter and exit events
   * are casted.
   */
  public static final int ENABLED = 0;

  /**
   * disabled state. rollover and clicking disabled, but enter and exit events
   * still casted.
   */
  public static final int DISABLED = 1;

  /**
   * rollover state, when the mouse entered an enabled mpanel.
   */
  public static final int ROLLOVER = 2;

  /**
   * pressed state, when the mouse is down.
   */
  public static final int PRESSED = 3;

  /**
   * dragged state, when the mouse moves by staying down.
   */
  public static final int DRAGGED = 4;

  /**
   * remembers the actual state of this component. Subclasses have protected access
   * to this field to avoid causing a {@link #statusChanged()} call.
   */
  protected int status = 0;

  /**
   * an <code>int</code> representing an ID for this <code>MPanel</code>.
   */
  protected int id;

  // collects all MPanelListeners
  private Vector listenerVector = new Vector();

  /**
   * Constructor.
   *
   * @param id an int as ID for this instance.
   */
  public MPanel(int id) {
    this.id = id;
    this.setLayout(null);
  }

  /**
   * initializes this component, registers all internal listeners.
   */
  public void init() {
    this.addMouseListener(new MouseAdapter() {
      public void mouseEntered(MouseEvent e) { mEntered(e); }
      public void mouseExited(MouseEvent e) { mExited(e); }
      public void mousePressed(MouseEvent e) { mPressed(e); }
      public void mouseReleased(MouseEvent e) { mReleased(e); }
      public void mouseClicked(MouseEvent e) { mClicked(e); }
    });
    this.addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) { mDragged(e); }
    });
  }

  // mouse pressed. set new status in case.
  private void mPressed(MouseEvent e) {
    if (this.status == ROLLOVER)
      this.setStatus(PRESSED);
  }

  // mouse entered. set rollover in case and call mpanelListener.
  private void mEntered(MouseEvent e) {
    if (this.status == ENABLED) {
      this.setStatus(ROLLOVER);
    }
    for (int i=0; i<this.listenerVector.size(); ++i) {
      ((MPanelListener) this.listenerVector.elementAt(i)).mpanelEntered(this);
    }
  }

  // mouse exited. set status back to enabled in case. inform mpanelListener.
  private void mExited(MouseEvent e) {
    if (this.status == ROLLOVER || this.status == PRESSED || this.status == DRAGGED ) {
      this.setStatus(ENABLED);
    }
    for (int i=0; i<this.listenerVector.size(); ++i) {
      ((MPanelListener) this.listenerVector.elementAt(i)).mpanelExited(this);
    }
  }

  // mouse released. if dragged before, take this as mouseclick.
  private void mReleased(MouseEvent e) {
    if (this.status == DRAGGED) {
      this.mClicked(e);
    }
  }

  // mouse clicked. inform mpanelListener and set rollover state.
  private void mClicked(MouseEvent e) {
    if (this.status == PRESSED || this.status == DRAGGED) {
      this.setStatus(ROLLOVER);
      for (int i=0; i<this.listenerVector.size(); ++i) {
        ((MPanelListener) this.listenerVector.elementAt(i)).mpanelClicked(this);
      }
    }
  }

  // mouse dragged. set status.
  private void mDragged(MouseEvent e) {
    if (this.status == PRESSED) {
      this.setStatus(DRAGGED);
    }
  }


  //////////////////////// getting and setting ////////////////////////////

  /**
   * returns the id of this <code>MPanel</code>.
   *
   * @return an <code>int</code> as ID.
   */
  public int getID() {
    return this.id;
  }

  /**
   * sets the status of this <code<MPanel</code>. If the new status is different from the
   * old one, {@link #statusChanged()} is called.
   *
   * @param status an <code>int</code> as the new status of this <code>MPanel</code>.
   */
  public void setStatus(int status) {
    if (this.status != status) {
      this.status = status;
      this.statusChanged();
    }
  }

  /**
   * returns the actual state of this component.
   *
   * @return the current value for field status.
   */
  public int getStatus() {
    return this.status;
  }

  /**
   * defines reaction to a changed status. This method can be overwritten by
   * subclasses. Over here it is empty.
   */
  protected void statusChanged() { }

  /**
   * registers a <code>MPanelListener</code> to this <code>MPanel</code>.
   *
   * @param listener a <code>MPanelListener</code> that will receive events.
   */
  public void addMPanelListener(MPanelListener listener) {
    this.listenerVector.addElement(listener);
  }
}