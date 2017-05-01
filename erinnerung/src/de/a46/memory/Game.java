// Copyright (c) 2001 by till zoppke, blaulicht@a46.de
package de.a46.memory;

import de.a46.memory.gui.RessourceContainer;
import java.awt.Image;
import java.awt.Color;
import java.util.Vector;
import java.applet.AudioClip;

/**
 * Class game is a final state machine controlling moves in the memory. There
 * are two types of moves: A cardmove is performed when the player detects one
 * of the 36 hiddencards from the field. An Imagemove is performed, when the
 * player clicks one of two big images from the leftPanel.<br>
 * <br>
 * this class is not a listener itself. User actions must be notified by another
 * instance, that then calls {@link #cardMove(int)} / {@link #imageMove()}.<br>
 * <br>
 * but there is an instance of interface MGUI referenced over here. So all display-
 * stuff corresponding to moves is done by this methods.<br>
 * <br>
 * before exiting the program, call {@link #stop()}. The first time, two cards
 * are clicked, a {@link DelayInvokator}-thread will be started, that must be stopped in case.<br>
 * <br>
 * class game is also responsible for playing audioClips if sound is wanted. These
 * audioClips are updated, whenever {@link #initCurrentData()} is called.
 *
 * @author Till Zoppke
 * @version 1.0
 */
public class Game {

  // constants describing status of game
  private static final short NONE_SELECTED = 0;
  private static final short ONE_SELECTED = 1;
  private static final short TWO_SELECTED = 2;

  // flag whether sounds should played
  private boolean sound;

  // current status of game as encoded in static fields.
  private short status;

  // array containing audioclips
  private AudioClip[] audios;

  // reference to gui layer
  private MGUI mgui;

  // reference to where to get audioclips from
  private RessourceContainer ressourceContainer;

  // indices to remember choosed Cards
  private int upperIndex;
  private int lowerIndex;

  // delayThread to paint a hint after a while
  private Thread hintThread = null;

  /**
   * constructor
   *
   * @param sound              encoding whether there should be sound or no sound.
   * @param mgui               a <code>MGUI</code> reference to pass moveActions to the gui-layer
   * @param ressourceContainer A <code>RessourceContainer</code> to get AudioClips from
   */
  public Game(boolean sound, MGUI mgui, RessourceContainer ressourceContainer) {
    this.sound = sound;
    this.mgui = mgui;
    this.ressourceContainer = ressourceContainer;
  }

  /**
   * resets this game to a default state. Audioclips are refreshed and the
   * hintThread is reset to null, too. Call this when starting a game.
   */
  public void initCurrentData() {
    this.audios = this.ressourceContainer.getCurrentCardAudios();
    this.status = NONE_SELECTED;
    this.stop();
    this.hintThread = null;
  }

  /**
   * performing an imageMove as reaction to player's click on one big image on the
   * left. If less than two cards are open, this method just returns immediatly.
   * Otherwise the LeftPanel is cleared. If the images were equal, they will be
   * removed from the field. Otherwise they are reset to ENABLED state and the
   * trialsCounter is incremented. Finally the fieldPanel becomes enabled again.
   */
  public void imageMove() {

    // if less then two cards selected, just return
    if (this.status != Game.TWO_SELECTED)
      return;

    // check for current bigImageHint and kill it in case
    this.stop();

    // if two same images, remove cards
    if (this.mgui.hasEqualImages()) {       //////////////////// debug: true) {/////
      this.mgui.removeCard(this.upperIndex);
      this.mgui.removeCard(this.lowerIndex);
    }

    //else draw normal backs again and increment trialscounter
    else {
      this.mgui.incrementTrialsCounter();
      this.mgui.setCardEnabled(this.upperIndex, true);
      this.mgui.setCardEnabled(this.lowerIndex, true);
    }

    // clear big fields on the left
    this.mgui.setBigImage(0, -1);
    this.mgui.setBigImage(1, -1);

    // enable hiddenCards
    this.mgui.setFieldEnabled(true);

    // next round
    this.status = NONE_SELECTED;
  }

  /**
   * performing a cardMove as reaction to player's click on a hiddencardPanel.
   * the Image corresponding to the clicked hiddenCard is displayed on the
   * leftpanel. If sound is selcted, the audioclip will played. If this card was
   * the second of a pair, the fieldPanel becomes disabled. If this card was the
   * second at all, a delayInvokator is started to show the bigImageHint.
   *
   * @param cardID the id of the clicked card as its index in the fieldPanel.
   */
  public void cardMove(int cardID) {

    switch (this.status) {

      case NONE_SELECTED:
        this.upperIndex = cardID;
        this.mgui.setBigImage(0, cardID);
        if (this.sound)
          audios[cardID].play();
        this.status = ONE_SELECTED;
        break;

      case ONE_SELECTED:

        // check whether we should start hintThread
        if (this.hintThread == null) {
          try {
            this.hintThread = new Thread(new DelayInvokator(
              Class.forName("de.a46.memory.MGUI").getMethod("setBigimageHint",
                new Class[] { Boolean.TYPE }),
              this.mgui,
              new Object[] { Boolean.TRUE },
              4000L
            ));
            this.hintThread.start();
          }
          catch(ClassNotFoundException e) { e.printStackTrace(); }
          catch(NoSuchMethodException e) { e.printStackTrace(); }
        }

        this.lowerIndex = cardID;
        this.mgui.setBigImage(1, cardID);
        if (this.sound)
          audios[cardID].play();
        this.status = TWO_SELECTED;
        this.mgui.setFieldEnabled(false);
        break;

    }// end of switch
  }

  /**
   * stops the delayInvokator for the bigImageHint, if it is active.
   */
  public void stop() {
    if (this.hintThread != null && this.hintThread.isAlive())
      this.hintThread.stop();
    this.mgui.setBigimageHint(false);
  }
}