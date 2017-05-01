// Copyright (c) 2001 by till zoppke, blaulicht@a46.de
package de.a46.memory;

import de.a46.memory.gui.MainPanel;
import java.applet.Applet;
import java.awt.Color;
import java.net.URL;
import java.net.MalformedURLException;
import java.awt.Image;
import java.awt.Graphics;
import java.applet.AudioClip;
import java.awt.Component;
import java.lang.reflect.Method;

/**
 * Class <code>Starter</code> is the main class of this memory. It
 * is called by the browser to run the program. It initializes the three
 * second level objects, as there are a {@link Game}, a {@link Loader} and a
 * {@link MainPanel}. For this
 * reason it must implement several interfaces:<br>
 * <br>
 * - interface <code>UserActionListener</code> is implemented to perform reactions to events
 * coming in from the gui layer (mainPanel),<br>
 * - interface <code>LoadListener</code> enables this <code>Starter</code> to show the load status in its
 * status bar,<br>
 * - interface <code>ParameterProvider</code> bundles a few parameters, the starter is getting
 * via param-tags from its html start site, and that are essentially for class
 * loader to get to external resources.<br>
 * - interface <code>MultiMediaProvider</code> capsulates the Starters possibilities to create
 * images and AudioClips from filenames (inherited from class <code>Applet</code>). This is
 * also needed for coordinated work with a <code>Loader</code> instance.
 *
 * @author Till Zoppke
 * @version 1.0
 */
public class Starter extends Applet implements UserActionListener, LoadListener,
                                               ParameterProvider, MultimediaProvider {
  // encoding flags
  private String SOUND_FLAG = "SOUND_FLAG";
  private String DELAY_FLAG = "DELAY_FLAG";

  // encoding properties filename
  private String PROPERTIES_FILE = "PROPERTIES_FILE";
  private String MULTIMEDIAS_FILE = "MULTIMEDIAS_FILE";
  private String AUTOCLOSE_FILE = "AUTOCLOSE_FILE";
  private String HELPTEXT_FILE = "HELPTEXT_FILE";

  // encoding directories where to find external resources
  private String IMAGE_BASE = "IMAGE_BASE";
  private String AUDIO_BASE = "AUDIO_BASE";
  private String DATA_BASE = "DATA_BASE";
  private String BACKGROUND_BASE = "BACKGROUND_BASE";

  // references
  private Game game;
  private MainPanel mainPanel;
  private Loader loader;

  // fields
  private String status = "";
  private Image welcomeImage;
  private long fixedDelayMillis = 0;
  private long randomDelayMillis = 0; // defining randomized Time for loading in local version
  private boolean delay;

  /////////////////////// init() and startGame() ///////////////////////////

  /**
   * Called by the browser. initializes this instance and the loader.
   * The welcomeImage is loaded and painted. Then basicImages are loaded and the
   * mainPanel is initialized. After that an instance of class game is constructed
   * and currentData is loaded. Finally the game is started.
   */
  public void init() {

    this.delay = this.isDelay();

    // init loader
    this.loader = new Loader(this, this);
    this.loader.init();
    this.loader.addLoadListener(this);

    // load and wait for welcome image
    this.loader.loadWelcomeImages();
    this.loader.loadBasicImages();
    this.loader.waitForWelcomeImages();

    // get and paint welcomeImage
    this.welcomeImage = this.loader.getWelcomeImage();
    this.setLayout(null);
    this.setBackground(Color.white);
    this.setVisible(true);
    this.paint(this.getGraphics());

    // wait for basic images
    this.loader.waitForBasicImages();

    // create game and mainpanel
    this.mainPanel = new MainPanel(this.loader);
    this.game = new Game(this.isSound(), this.mainPanel, this.loader);
    this.mainPanel.addUserActionListener(this);
    this.mainPanel.setVisible(false);
    this.add(this.mainPanel);

    this.mainPanel.init();

    // start game
    this.startGame();
  }

  private void startGame() {

    // prepare, load and wait for cards
    this.loader.initCurrentGameData();
    this.loader.loadCurrentImages();
    this.loader.loadCurrentBackgroundImages();
    this.loader.waitForCurrentImages();
    this.loader.waitForCurrentBackgroundImages();

    // init game and mainpanel
    this.mainPanel.initCurrentData();
    this.game.initCurrentData();

    this.mainPanel.setVisible(true);
    this.mainPanel.repaintAll();
    this.setStatus("Neues Spiel.");
    this.delay = false;
  }

  //////////// UserActionListener methods /////////////////

  /**
   * Restarts the game. Cards will be refreshed, current card images and current
   * background images reloaded. Then the initCurrentData methods of mainPanel
   * and Game are called.
   */
  public void newgameAction() {
    this.loader.setRoundCounter(this.loader.getRoundCounter() - 1);
    this.startGame();
  }

  /**
   * Quits the applet by calling a autoclosing html page. The path of this site
   * is specified by parameter <code>AUTOCLOSE_FILE</code> in the html site, in which the
   * applet is embedded. If you run this applet in appletviewer nothing will
   * happen.
   */
  public void exitAction() {
    try {
      this.getAppletContext().showDocument(new URL(
        this.getCodeBase() + this.getParameter(AUTOCLOSE_FILE)
      ));
    }
    catch (MalformedURLException e) { e.printStackTrace(); }
  }

  /**
   * This method is called, when the user clicks one of the hiddencards in the
   * field. The call is passed to {@link Game#cardMove(int)}
   *
   * @param cardID  an <code>int</code> representing the clicked card's id, that
   *                is its index in the hiddencards array of class {@link de.a46.memory.gui.FieldPanel}.
   */
  public void cardClicked(int cardID) {
    this.game.cardMove(cardID);
  }

  /**
   * Called when there was a user action to a big image on the left. At first
   * {@link Game#imageMove()} is evoked. Then it is checked, whether the
   * field is empty now. In this case, after a few seconds pause, a new game is
   * started. During the pause there is a small animation in the status bar.
   */
  public void bigImageClicked() {
    this.game.imageMove();
    if (this.mainPanel.isEmptyField()) {
      this.mainPanel.setEnabled(false);
      this.mainPanel.repaintAll();
      this.setStatus("Gro\u00dfer Schmerz");
      for (int i=0; i<50; ++i) {
        try { Thread.sleep(100); }
        catch (InterruptedException e) { e.printStackTrace(); }
        this.setStatus(" " + this.getStatus());
      }
      this.startGame();
    }
  }

  //////////////////////////// ParameterProvider methods ///////////////////////

  /**
   * Returns a String containing the relative path to the directory, where the
   * background images are located. This info is taken from parameter
   * <code>BACKGROUND_BASE</code> of the embedding html file.
   */
  public String getBackgroundBase() {
    return this.getParameter(BACKGROUND_BASE);
  }

  /**
   * Returns a String containing the relative path to the directory, where the
   * basic images are located. This info is taken from parameter
   * <code>DATA_BASE</code> of the embedding html file.
   */
  public String getDataBase() {
    return this.getParameter(DATA_BASE);
  }

  /**
   * Returns a String containing the relative path to the directory, where the
   * card images are located. This info is taken from parameter
   * <code>IMAGE_BASE</code> of the embedding html file.
   */
  public String getImageBase() {
    return this.getParameter(IMAGE_BASE);
  }

  /**
   * Returns a String containing the relative path to the directory, where the
   * audio files are located. This info is taken from parameter
   * <code>AUDIO_BASE</code> of the embedding html file.
   */
  public String getAudioBase() {
    return this.getParameter(AUDIO_BASE);
  }

  /**
   * Returns a URL pointing to the properties file. This URL is created from the
   * value of parameter <code>PROPERTIES_FILE</code> of the embedding html file.
   */
  public URL getPropertiesURL() {
    try { return new URL (this.getCodeBase() + "/" + this.getParameter(PROPERTIES_FILE)); }
    catch (MalformedURLException e) { e.printStackTrace(); }
    return null;
  }

  /**
   * Returns a URL pointing to the multimedias file. This URL is created from the
   * value of parameter <code>MULTIMEDIAS_FILE</code> of the embedding html file.
   */
  public URL getMultimediasURL() {
    try { return new URL (this.getCodeBase() + "/" + this.getParameter(MULTIMEDIAS_FILE)); }
    catch (MalformedURLException e) { e.printStackTrace(); }
    return null;
  }

  /**
   * Returns a URL pointing to the helptext file. This URL is created from the
   * value of parameter <code>HELPTEXT_FILE</code> of the embedding html file.
   */
  public URL getHelptextURL() {
    try { return new URL (this.getCodeBase() + "/" + this.getParameter(HELPTEXT_FILE)); }
    catch (MalformedURLException e) { e.printStackTrace(); }
    return null;
  }

  ///////////////////////////// MultimediaProvider methods //////////////////////

  /**
   * Creates an image specified by a path relative to Applet's codebase.
   *
   * @param relativePath a <code>String</code> as the relative path for the image file,
   * @return the <code>Image</code> that was created.
   */
  public Image getImage(String relativePath) {
    return this.getImage(this.getCodeBase(), relativePath);
  }

  /**
   * Creates an audioclip specified by a path relative to Applet's codebase.
   *
   * @param relativePath a <code>String</code> as the relative path for the audio file,
   * @return the <code>AudioClip</code> that was created.
   */
  public AudioClip getAudioClip(String relativePath) {
    return this.getAudioClip(this.getCodeBase(), relativePath);
  }

  /**
   * Returns a reference to this instance of <code>Starter</code>.
   */
  public Component getComponent() {
    return this;
  }

  //////////////////////////// loadEvent reaction methods ///////////////////////

  /**
   * Notifies the user about loading events by writing text to the status bar.
   * This method is called by class <code>Loader</code> when a <code>waitForXXX</code>
   * is performed. Info what to write is taken from the <code>LoadEvent</code>
   * argument and by calling {@link Loader#getLoadingStatus()}. The text to write
   * in the status bar is hardcoded.
   *
   * @param e a <code>LoadEvent</code> specifying what happened.
   */
  public void imageLoaded(LoadEvent e) {
    switch (e.getEventType()) {

      case LoadEvent.LOAD_ERROR:
        this.setStatus(this.getStatus() + " FEHLER!");
        break;

      case LoadEvent.LOADED_ONE:
        this.setStatus(this.getStatus() + "+");
        if (this.delay) {
          try {
            Thread.sleep((long) (
              this.fixedDelayMillis + Math.floor(Math.random() * this.randomDelayMillis)
            ));
          }
          catch (InterruptedException exc) { exc.printStackTrace(); }
        }
        break;

      case LoadEvent.LOADING_STARTED:
        switch (this.loader.getLoadingStatus()) {
          case Loader.LOADING_BASIC:
            this.setStatus("lade Navigation ");
            this.randomDelayMillis = 200;
            this.fixedDelayMillis = 200;
            break;
          case Loader.LOADING_CURRENT:
            this.setStatus("lade Schmerzen ");
            this.randomDelayMillis = 400;
            this.randomDelayMillis = 300;
            break;
          case Loader.LOADING_WELCOME:
            this.setStatus("lade Willkommen ");
            this.randomDelayMillis = 0;
            this.fixedDelayMillis = 0;
            break;
          case Loader.LOADING_CURRENT_BACKGROUND:
            this.setStatus("lade gro\u00dfen Schmerz ");
            this.randomDelayMillis = 0;
            this.fixedDelayMillis = 3000;
            break;
        } // end of inner switch
        break;

      case LoadEvent.LOADED_ALL:
        this.setStatus(this.getStatus() + ".");
        break;
    } // end of outer switch
  }

  private void setStatus(String status) {
    this.status = status;
    this.showStatus(status);
  }

  private String getStatus() {
    return this.status;
  }

  /**
   * Paints the welcome image, if no <code>Mainpanel</code> is constructed yet.
   * This takes place during loading. Otherwise nothing is done.
   */
  public void paint(Graphics g) {
    if (this.mainPanel == null && this.welcomeImage != null) {
      g.drawImage(this.welcomeImage, 0, 0, this);
    }
  }

  /**
   * Called by the browser to stop this applet. The call is passed to
   * {@link Game#stop()}, because maybe a <code>Thread</code>is running there.
   */
  public void stop() {
    if (this.game != null) {
      this.game.stop();
    }
  }

  private boolean isDelay() {
    return this.getParameter(DELAY_FLAG).equalsIgnoreCase("true");
  }

  private boolean isSound() {
    return this.getParameter(SOUND_FLAG).equalsIgnoreCase("true");
  }
}