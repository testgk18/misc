// Copyright (c) 2001 by till zoppke, blaulicht@a46.de
package de.a46.memory.gui;

import de.a46.memory.MGUI;
import de.a46.memory.UserActionListener;
import java.util.Vector;
import java.awt.Panel;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Class <code>MainPanel</code> is the main gui class. its father-component is the applet-extending
 * {@link de.a46.memory.Starter}, child components are a {@link ButtonbarPanel},
 * a {@link LeftPanel} and a {@link RightPanel}. its size is fixed 720 x 502 pixel.<br>
 * <br>
 * this is also an active component, because it performs reaction to buttonbar
 * clicked events and to <code>ActionEvent</code>s from the {@link HelpPanel}
 * and the {@link ExitPanel}. Only moves (cardClicked, imageClicked) and heavy
 * actions (newgameAction, exitAction) are passed to a registered
 * {@link UserActionListener}.<br>
 * <br>
 * Beside the three listener interfaces also an interface <code>MGUI</code> is implemented.
 * So a <code>MainPanel</code> can perform commands given by an instance of class
 * {@link de.a46.memory.Game}.
 *
 * @author Till Zoppke
 * @version 1.0
 */
public class MainPanel extends Panel implements MPanelListener, MGUI,
                                                YesNoListener, ButtonbarListener {

  // encoding types of rightpanel.
  private static final int RIGHTPANEL_FIELD = 0;
  private static final int RIGHTPANEL_HELP = 1;
  private static final int RIGHTPANEL_EXIT = 2;

  // childs
  private ButtonbarPanel buttonbarPanel;
  private LeftPanel leftPanel;
  private RightPanel[] rightPanels;
  private FieldPanel fieldPanel;

  private Vector listenerVector = new Vector();

  // currently shown rightpanel
  private int rightpanelType = RIGHTPANEL_FIELD;

  /**
   * Constructor.
   *
   * @param ressourceContainer a <code>RessourceContainer</code> to get images and other data from.
   */
  public MainPanel(RessourceContainer ressourceContainer) {
    this.fieldPanel = new FieldPanel(ressourceContainer);
    this.rightPanels = new RightPanel[] {
      this.fieldPanel,
      new HelpPanel(ressourceContainer.getHelptext()),
      new ExitPanel()
    };
    this.leftPanel = new LeftPanel(ressourceContainer);
    this.buttonbarPanel = new ButtonbarPanel(ressourceContainer);
    this.setLayout(null);
    this.setSize(720, 502);
  }

  /**
   * initializes this component and all subcomponents.
   */
  public void init() {

    // add childs
    this.add(this.buttonbarPanel);
    this.buttonbarPanel.setBounds(0, 0, 720, 22);
    this.add(this.leftPanel);
    this.leftPanel.setBounds(0, 22, 240, 480);

    // add fieldPanel for default.
    this.add(this.fieldPanel);

    // init childs
    for (int i=0; i<this.rightPanels.length; ++i) {
      this.rightPanels[i].setBounds(240, 22, 480, 480);
      this.rightPanels[i].init();
    }
    this.leftPanel.init();
    this.buttonbarPanel.init();

    // add listeners to childs
    this.buttonbarPanel.addButtonbarListener(this);
    this.leftPanel.addMPanelListener(this);
    ((ExitPanel) this.rightPanels[RIGHTPANEL_EXIT]).addYesNoListener(this);
    ((FieldPanel) this.rightPanels[RIGHTPANEL_FIELD]).addMPanelListener(this);
    ((HelpPanel) this.rightPanels[RIGHTPANEL_HELP]).addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) { setRightpanelType(RIGHTPANEL_FIELD); }
    });
  }

  /**
   * reinits this component for a new game. Childs are reseted, too.
   */
  public void initCurrentData() {
    this.setEnabled(true);
    this.fieldPanel.initCurrentData();
    this.leftPanel.initCurrentData();
    this.buttonbarPanel.initCurrentData();
  }

  ///////////////////// MPanelListener methods ///////////////////////////

  /**
   * This method is called either when (1) a
   * hiddencard is clicked or when (2) a bigImage is clicked. In case (1) the
   * correct highlightimage will be set to the hiddencard, then
   * {@link UserActionListener#cardClicked(int)} will be called. In case (2)
   * {@link UserActionListener#bigImageClicked()} is called immediately.
   *
   * @param e the <code>MPanelEvent</code> that occures.
   */
  public void mpanelClicked(MPanel source) {

    // case 1: hiddencardPanel clicked
    if (source instanceof HiddencardPanel) {

      // notify UserActionListeners
      for (int i = 0; i < this.listenerVector.size(); i++)
        ((UserActionListener) this.listenerVector.elementAt(i)).cardClicked(source.getID());

      // set highlight Image and status
      int bigimageIndex = this.leftPanel.getPanelIndex(source.getID());
      if (bigimageIndex == LeftPanel.UPPER)
        this.fieldPanel.setCardStatus(source.getID(), HiddencardPanel.HIGHLIGHT_UPPER);
      if (bigimageIndex == LeftPanel.LOWER)
        this.fieldPanel.setCardStatus(source.getID(), HiddencardPanel.HIGHLIGHT_LOWER);
    }

    // case 2: bigImage Clicked.
    else if (source instanceof MultiImagePanel) {
      for (int i = 0; i < this.listenerVector.size(); i++)
        ((UserActionListener) this.listenerVector.elementAt(i)).bigImageClicked();
    }
  }

  /**
   * empty method
   */
  public void mpanelExited(MPanel source) { }

  /**
   * empty method
   */
  public void mpanelEntered(MPanel source) { }

  ////////////////////////////// MGUI methods //////////////////////////////////

  /**
   * Checks whether there are any hiddencards left on the rightPanel.
   *
   * @return <code>true</code> if all hiddenCards at the fieldPanel are in
   *         {@link HiddencardPanel#OFF_STATE},<br>
   *         <code>false</code> if still some cards are there.
   */
  public boolean isEmptyField() {
    return this.fieldPanel.isEmpty();
  }

  /**
   * enables / disables the <code>FieldPanel</code> to cast events and perform rollovers.
   *
   * @param status a <code>boolean</code> describing whether to enable or to disable.
   */
  public void setFieldEnabled(boolean status) {
    this.fieldPanel.setEnabled(status);
  }

  /**
   * increments the trials counter of the <code>ButtonbarPanel</code>.
   */
  public void incrementTrialsCounter() {
    this.buttonbarPanel.incrementTrialsCounter();
  }

  /**
   * checks, whether the images shown on the <code>LeftPanel</code> are equal. If less than
   * two images are open, <code>false</code> will be returned.
   *
   * @return  <code>true</code> if both images are equal,<br>
   *          <code>false</code> if they are different or less than two are displayed.
   */
  public boolean hasEqualImages() {
    return this.leftPanel.hasEqualImages();
  }

  /**
   * sets the status of a specified card in the fieldPanel.
   *
   * @param cardID an <code>int</code> determining the hiddencard by its id,
   * @param status if <code>true</code>, the new status is {@link HiddencardPanel#ENABLED},<br>
   *               if <code>false</code>, the new status is {@link HiddencardPanel#DISABLED}.
   */
  public void setCardEnabled(int cardID, boolean status) {
    this.fieldPanel.setCardStatus(
      cardID, status ? HiddencardPanel.ENABLED : HiddencardPanel.DISABLED
    );
  }

  /**
   * removes one card from the <code>FieldPanel</code>. That means setting it to
   * {@link HiddencardPanel#OFF_STATE}.
   *
   * @param cardID  an int determining the hiddencard by its id.
   */
  public void removeCard(int cardID) {
    this.fieldPanel.setCardStatus(cardID, HiddencardPanel.OFF_STATE);
  }

  /**
   * Sets one image of the <code>LeftPanel</code>.
   *
   * @param index an <code>int</code> representing the index of the target Panel as encoded by
   *              LeftPanel.UPPER, LeftPanel.LOWER
   * @param imageID an <code>int</code> as the index of this image in the currentCardImages array.
   *                value -1 represents, that no image should be displayed.
   */
  public void setBigImage(int index, int imageID) {
    this.leftPanel.setBigImage(index, imageID);
  }

  /**
   * registers a <code>UserActionListener</code> at this <code>MainPanel</code>.
   *
   * @param listener a listener to receive events from this component.
   */
  public void addUserActionListener(UserActionListener listener) {
    this.listenerVector.addElement(listener);
  }

  /**
   * activates / deactivates the bigImagehint on the leftPanel.
   *
   * @param b  <code>true</code> if the hint should be painted,<br>
   *           <code>false</code> if not.
   */
  public void setBigimageHint(boolean b) {
    this.leftPanel.setBigimageHint(b);
  }

  //////////// display setting methods ////////////////

  private void setRightpanelType(int rightpanelType) {
    this.remove(this.rightPanels[this.rightpanelType]);
    this.rightpanelType = rightpanelType;
    this.add(this.rightPanels[this.rightpanelType]);
    this.rightPanels[this.rightpanelType].repaintAll();
    this.buttonbarPanel.setEnabled(this.rightpanelType == RIGHTPANEL_FIELD);
    this.leftPanel.setEnabled(this.rightpanelType == RIGHTPANEL_FIELD);
  }

  /**
   * repaints this <code>MainPanel</code> and all childs.
   */
  public void repaintAll() {
    this.buttonbarPanel.repaint();
    this.leftPanel.repaint();
    this.rightPanels[this.rightpanelType].repaintAll();
  }

  //////////////////// YesNoListener methods /////////////////////

  /**
   * is called, when the user
   * clicks the no button on the <code>ExitPanel</code>. The <code>RightPanel</code> is set to the
   * <code>FieldPanel</code> over here.
   */
  public void noButtonClicked() {
    this.setRightpanelType(RIGHTPANEL_FIELD);
  }

  /**
   * is called, when the user clicks the yes button on the <code>ExitPanel</code>.
   * All registered <code>UserActionListeners</code>
   * will be informed by calling {@link UserActionListener#exitAction()}.
   */
  public void yesButtonClicked() {
    for (int i=0; i<this.listenerVector.size(); ++i)
      ((UserActionListener) this.listenerVector.elementAt(i)).exitAction();
  }

  //////////////////////// ButtonbarListener methods ///////////////////////

  /**
   * is called, when the user clicks the exit button. The <code>RightPanel</code>
   * will be set to <code>ExitPanel</code>.
   */
  public void exitButtonClicked() {
    this.setRightpanelType(RIGHTPANEL_EXIT);
  }

  /**
   * is called, when the user
   * clicks the newgame button. All registered <code>UserActionListeners</code> will be
   * informed by calling {@link UserActionListener#newgameAction()}.
   */
  public void newgameButtonClicked() {
    for (int i=0; i<this.listenerVector.size(); ++i)
      ((UserActionListener) this.listenerVector.elementAt(i)).newgameAction();
  }

  /**
   * is called, when the user clicks the exit button. The <code>RightPanel</code>
   * will be set to <code>HelpPanel</code>.
   */
  public void helpButtonClicked() {
    this.setRightpanelType(RIGHTPANEL_HELP);
  }
}