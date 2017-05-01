// Copyright (c) 2001 by till zoppke, blaulicht@a46.de
package de.a46.memory;

import de.a46.memory.gui.RessourceContainer;
import java.applet.Applet;
import java.util.Vector;
import java.net.URL;
import java.awt.Image;
import java.applet.AudioClip;
import java.awt.MediaTracker;
import java.util.Hashtable;
import java.io.File;

/**
 * Class <code>Loader</code> is responsible for connecting to outer resources. It takes
 * necessary paths from a {@link ParameterProvider}, which is in this memory implemented
 * by class {@link Starter}. Also a {@link MultimediaProvider} is needed to get Images and
 * AudioClips from, that is implemented by class <code>Starter</code>, too.<br>
 * <br>
 * <code>Loader</code> should be a unique instance, that is passed to each component, which
 * needs ressources. To control the current state of loading you can add a
 * {@link LoadListener}, that receives {@link LoadEvent}s.<br>
 * <br>
 * All media is grouped. There are<br>
 * - welcomeImages: to be shown during loading the others,<br>
 * - basicImages: all stuff needed for layout,<br>
 * - currentCardImages: 18 image-pairs mixed in an array of 36, corresponding to cards,<br>
 * - currentCardAudios: 18 audioclip-pairs, mixed in an array of 36, corresponding to cards,<br>
 * - currentBackgroundImages: an array of two backgroundImages,<br>
 * - helpText: a twoDimensional String-array containing a help-text.<br>
 * references to all data are stored internal into a hashtable.<br>
 * <br>
 * Each group has its own methods. Call them in following order (eg basicImages):<br>
 * - {@link #init()} initializes all data except currentGameData. Call this only once.<br>
 * - {@link #loadBasicImages()} creates the images, that will start loading automatically.<br>
 * - {@link #waitForBasicImages()} waits for the images. LoadEvents are casted.<br>
 * - {@link #getTrialsImage()}, {@link #getHiddencardImages()} etc. to get the images.<br>
 * unfortunately <code>AudioClips</code> cannot controlled to been loaded.<br>
 * <br>
 * There are two helpers, to get paths to all resources.<br>
 * Class {@link Multimedias} reads from a file, that containes an unlimited number of
 * lines representing cards, both images and AudioClips.<br>
 * Class {@link MProperties} reads from a file, that contains keys with point to welcomeImages,
 * basicImages and backgroundImages. This file has java-properties format.<br>
 * Also there is a textfile containing the helptext, which is accessed by class
 * {@link TextGetter}.
 *
 * @author Till Zoppke
 * @version 1.0
 */
public class Loader implements RessourceContainer {

  ///////////// static encoding of load status ////////////

  /**
   * encoding current state of loading. Default state - no waitFor() is active.
   */
  public static final int LOADING_NOTHING = 0;

  /**
   * encoding current state of loading. Currently welcomeImages are loading.
   */
  public static final int LOADING_WELCOME = 1;

  /**
   * encoding current state of loading. Currently waiting for basic images.
   */
  public static final int LOADING_BASIC = 2;

  /**
   * encoding current state of loading. Waiting for currentCardImages.
   */
  public static final int LOADING_CURRENT = 3;

  /**
   * encoding current state of loading. Waiting for backgroundImages.
   */
  public static final int LOADING_CURRENT_BACKGROUND = 4;

  /////////////////// property keys ///////////////////////

  // basic images
  private static final String HIDDENCARD_IMAGES="HIDDENCARD_IMAGES";
  private static final String NEWGAME_IMAGES="NEWGAME_IMAGES";
  private static final String HELP_IMAGES="HELP_IMAGES";
  private static final String EXIT_IMAGES="EXIT_IMAGES";
  private static final String TRIALS_IMAGES="TRIALS_IMAGES";
  private static final String NUMERAL_IMAGES="NUMERAL_IMAGES";

  // game data
  private static final String BACKGROUND_IMAGES = "BACKGROUND_IMAGES";
  private static final String CARD_IMAGES = "CARD_IMAGES";
  private static final String CARD_AUDIOS = "CARD_AUDIOS";

  // welcome images
  private static final String WELCOME_IMAGES="WELCOME_IMAGES";

  // current data
  private static final String CURRENT_CARD_IMAGES = "CURRENT_CARD_IMAGES";
  private static final String CURRENT_CARD_AUDIOS = "CURRENT_CARD_AUDIOS";
  private static final String CURRENT_BACKGROUND_IMAGES = "CURRENT_BACKGROUND_IMAGES";

  // arrays containing all keys grouped
  private static final String[] BASIC_IMAGES = {
    HIDDENCARD_IMAGES, NEWGAME_IMAGES, HELP_IMAGES, EXIT_IMAGES,
    TRIALS_IMAGES, NUMERAL_IMAGES
  };

  /////////////////////////////// fields /////////////////////////////////////

  // references
  private Multimedias multimedias;
  private MProperties mproperties;
  private MultimediaProvider multimediaProvider;
  private ParameterProvider parameterProvider;

  // fields
  private Vector listenerVector = new Vector();
  private Hashtable table;
  private MediaTracker mediaTracker;
  private int idCounter = 0;
  private int loadingStatus = LOADING_NOTHING;
  private int roundCounter = 0;
  private String[][] helptext;

  // mediatracker image ids to wait for
  private int[] welcomeMinMaxIDs = new int[2];
  private int[] basicMinMaxIDs = new int[2];
  private int[] currentMinMaxIDs = new int[2];
  private int[] currentBackgroundMinMaxIDs = new int[2];

  // bases
  private String dataBase;
  private String backgroundBase;
  private String imageBase;
  private String audioBase;

  ////////////////////////// constructor //////////////////////////////

  /**
   * constructs a new <code>Loader</code>.
   *
   * @param pp  a <code>ParameterProvider</code> to take paths from,
   * @param mp  a <code>MultimediaProvider</code> to get images and AudioClips from.
   */
  public Loader(ParameterProvider pp, MultimediaProvider mp) {
    this.parameterProvider = pp;
    this.multimediaProvider = mp;
    this.mediaTracker = new MediaTracker(this.multimediaProvider.getComponent());
    this.mproperties = new MProperties(this.parameterProvider.getPropertiesURL());
    this.multimedias = new Multimedias(this.parameterProvider.getMultimediasURL());
    this.imageBase = this.parameterProvider.getImageBase();
    this.audioBase = this.parameterProvider.getAudioBase();
    this.dataBase = this.parameterProvider.getDataBase();
    this.backgroundBase = this.parameterProvider.getBackgroundBase();
  }

  //////////////////////// init methods //////////////////////////////

  /**
   * initialises all helpers and all static ressources (welcomeImage, all cardImages,
   * all cardAudios, basicImages, helpText, all background Images).
   */
  public void init() {
    this.table = new Hashtable(20);
    this.mproperties.init();
    this.multimedias.init();
    this.initWelcomeImages();
    this.initAllGameData();
    this.initBasicImages();
    this.initHelpText();
  }

  // initializes the welcome images and adds a reference to the hashtable
  private void initWelcomeImages() {
    this.addImages(WELCOME_IMAGES, this.dataBase);
  }

  // initializes all background images, all card images and all card audios.
  // References are addede to the hashtable.
  private void initAllGameData() {
    this.initMultimedias();
    this.addImages(BACKGROUND_IMAGES, this.backgroundBase);

    // rotate backgroundImages for a random start background
    Image[] oldImages = (Image[]) this.table.get(BACKGROUND_IMAGES);
    int offset = (int) Math.floor(Math.random() * oldImages.length);
    Image[] newImages = new Image[oldImages.length];
    for (int i=0; i<newImages.length; ++i) {
      newImages[i] = oldImages[(i + offset) % newImages.length];
    }
    this.table.put(BACKGROUND_IMAGES, newImages);
  }

  // gets allGame-paths from multimedias.
  // creates images and audios, stores them in the hashtable
  private void initMultimedias() {
    String[] audioStrings = this.multimedias.getAudioStrings();
    String[] imageStrings = this.multimedias.getImageStrings();
    AudioClip[] audios = new AudioClip[audioStrings.length];
    Image[] images = new Image[imageStrings.length];

    for (int i=0; i<audios.length; ++i) {
      audios[i] = this.multimediaProvider.getAudioClip(this.audioBase + File.separatorChar + audioStrings[i]);
      images[i] = this.multimediaProvider.getImage(this.imageBase + File.separatorChar + imageStrings[i]);
    }
    this.table.put(CARD_AUDIOS, audios);
    this.table.put(CARD_IMAGES, images);
  }

  // adds basicImages to the hashtable
  private void initBasicImages() {
    for (int i=0; i<this.BASIC_IMAGES.length; ++i) {
      this.addImages(this.BASIC_IMAGES[i], this.dataBase);
    }
  }

  // initializes the TextGetter and gets the helptext, stores it in its field.
  private void initHelpText() {
    TextGetter getter = new TextGetter(this.parameterProvider.getHelptextURL());
    getter.init();
    this.helptext = getter.getText();
  }

  /**
   * initializes the current game data (background, images, audios). Cards are
   * mixed by random. After this call, data can be loaded.
   */
  public void initCurrentGameData() {

    // get allData from Hashtable
    Image[] allCardImages = (Image[]) this.table.get(CARD_IMAGES);
    AudioClip[] allCardAudios = (AudioClip[]) this.table.get(CARD_AUDIOS);

    // create arrays to put in Hashtable
    Image[] currentCardImages = new Image[36];
    Image[] currentBackgroundImages = new Image[1];
    AudioClip[] currentCardAudios = new AudioClip[36];

    // copy String array into Vectors
    Vector allImageVector = new Vector(allCardImages.length);
    Vector allAudioVector = new Vector(allCardAudios.length);
    for (int i = 0; i < allCardImages.length; i++) {
      allImageVector.addElement(allCardImages[i]);
      allAudioVector.addElement(allCardAudios[i]);
    }

    // create images and audios
    Vector newImageVector = new Vector(36);
    Vector newAudioVector = new Vector(36);

    for (int i = 0; i < 18; i++) {
      // randomly take image and audio Strings.
      int index = (int) Math.floor(Math.random() * allImageVector.size());

      // store in new Vectors.
      newImageVector.addElement(allImageVector.elementAt(index));
      newImageVector.addElement(allImageVector.elementAt(index));
      newAudioVector.addElement(allAudioVector.elementAt(index));
      newAudioVector.addElement(allAudioVector.elementAt(index));

      // delete used strings to avoid taking twice.
      allImageVector.removeElementAt(index);
      allAudioVector.removeElementAt(index);
    }

    // now convert Vectors to arrays by mixing.
    for (int i = 0; i < 36; i++) {
      int index = (int) Math.floor(Math.random() * newImageVector.size());
      currentCardImages[i] = (Image) newImageVector.elementAt(index);
      currentCardAudios[i] = (AudioClip) newAudioVector.elementAt(index);
      newImageVector.removeElementAt(index);
      newAudioVector.removeElementAt(index);
    }

    // put images and audios to table
    this.table.put(CURRENT_CARD_IMAGES, currentCardImages);
    this.table.put(CURRENT_CARD_AUDIOS, currentCardAudios);

    // choose backgroundImage
    Image[] allbgi = (Image[]) this.table.get(BACKGROUND_IMAGES);
    if (this.roundCounter++ == 0)
      this.table.put(CURRENT_BACKGROUND_IMAGES, new Image[] { null, allbgi[0] });
    else
      this.table.put(CURRENT_BACKGROUND_IMAGES, new Image[] {
        allbgi[(this.roundCounter-2) % allbgi.length],
        allbgi[(this.roundCounter-1) % allbgi.length]
      });

  }// end of initCurrentGameData


  ///////////////////// public getImages methods ///////////////////////////

  /**
   * returns the welcome Image
   *
   * @return an <code>Image</code> to be painted during other images are loading.
   */
  public Image getWelcomeImage() {
    return ((Image[]) this.table.get(WELCOME_IMAGES))[0];
  }

  /**
   * returns the currentCardImages for one game of memory. Images are corresponding
   * to the currentCardAudios.
   *
   * @return an array of 36 Images
   */
  public Image[] getCurrentCardImages() {
    return (Image[]) this.table.get(CURRENT_CARD_IMAGES);
  }

  /**
   * returns the hiddenCardImages to be painted on the field.
   *
   * @return an array of 7 images
   */
  public Image[] getHiddencardImages() {
    return (Image[]) this.table.get(HIDDENCARD_IMAGES);
  }

  /**
   * returns the newgameImages to be painted on the buttonbar: newgameButton
   *
   * @return an array of 5 images
   */
  public Image[] getNewgameImages() {
    return (Image[]) this.table.get(NEWGAME_IMAGES);
  }

  /**
   * returns the helpImages to be painted on the buttonbar: helpButton
   *
   * @return an array of five images
   */
  public Image[] getHelpImages() {
    return (Image[]) this.table.get(HELP_IMAGES);
  }

  /**
   * returns the exitImages to be painted on the buttonbar: exitButton
   *
   * @return an array of 5 images
   */
  public Image[] getExitImages() {
    return (Image[]) this.table.get(EXIT_IMAGES);
  }

  /**
   * returns the numeralImages to be painted on the buttonbar: trialsCounter
   *
   * @return an array of 10 images
   */
  public Image[] getNumeralImages() {
    return (Image[]) this.table.get(NUMERAL_IMAGES);
  }

  /**
   * returns the trialsImage to be painted on the buttonbar: "Zähler"
   *
   * @return an <code>Image</code>.
   */
  public Image getTrialsImage() {
    return ((Image[]) this.table.get(TRIALS_IMAGES))[0];
  }

  /**
   * returns an array of two background images from the <code>BACKGROUND_IMAGES</code> array.
   * These images are valid for one game of memory.
   *
   * @return <code>{ null, BACKGROUND_IMAGES[0] }</code>, if (id == 0),<br>
   *         <code>{BACKGROUND_IMAGES[(id-1) % length], BACKGROUND_IMAGES[id % length] }</code> otherwise.
   */
  public Image[] getCurrentBackgroundImages() {
    return (Image[]) this.table.get(CURRENT_BACKGROUND_IMAGES);
  }

  /**
   * returns the audioClips for one game of memory, corresponding to the
   * currentCardImages.
   *
   * @return an array of 36 <code>AudioClip</code>s.
   */
  public AudioClip[] getCurrentCardAudios() {
    return (AudioClip[]) this.table.get(CURRENT_CARD_AUDIOS);
  }

  /**
   * returns the helpText to be displayed on the helppanel.
   *
   * @return a two-dimensional String array.
   */
  public String[][] getHelptext() {
    return this.helptext;
  }

  /////////////// public loadImages and waitImages methods ////////////////////

  /**
   * starts loading the basic images. That means adding them to a <code>MediaTracker</code>.
   */
  public void loadBasicImages() {
    this.basicMinMaxIDs[0] = ++this.idCounter;
    this.prepareImages(this.BASIC_IMAGES, 5);
    this.basicMinMaxIDs[1] = this.idCounter;
  }

  /**
   * starts loading the welcome images. That means adding them to a <code>MediaTracker</code>.
   */
  public void loadWelcomeImages() {
    this.welcomeMinMaxIDs[0] = ++this.idCounter;
    this.prepareImages(new String[] {this.WELCOME_IMAGES}, 3);
    this.welcomeMinMaxIDs[1] = this.idCounter;
  }

  /**
   * starts loading the currentCard images. That means adding them to a <code>MediaTracker</code>.
   */
  public void loadCurrentImages() {
    this.currentMinMaxIDs[0] = ++this.idCounter;
    this.prepareImages( new String[] { this.CURRENT_CARD_IMAGES }, 3);
    this.currentMinMaxIDs[1] = this.idCounter;
  }

  /**
   * starts loading the current background images. That means adding them to a <code>MediaTracker</code>.
   */
  public void loadCurrentBackgroundImages() {
    this.currentBackgroundMinMaxIDs[0] = ++this.idCounter;
    this.prepareImages( new String[] { this.CURRENT_BACKGROUND_IMAGES }, 1);
    this.currentBackgroundMinMaxIDs[1] = this.idCounter;
  }

  /**
   * waits for the basic images to get loaded. Events are casted while loading
   * and when finished.
   */
  public void waitForBasicImages() {
    this.loadingStatus = LOADING_BASIC;
    this.waitForImages(this.basicMinMaxIDs);
    this.loadingStatus = LOADING_NOTHING;
  }

  /**
   * waits for the welcome image to get loaded. Events are casted while loading
   * an d when finished.
   */
  public void waitForWelcomeImages() {
    this.loadingStatus = LOADING_WELCOME;
    this.waitForImages(this.welcomeMinMaxIDs);
    this.loadingStatus = LOADING_NOTHING;
  }

  /**
   * waits for the current card images to get loaded. Events are casted while loading
   * and when finished.
   */
  public void waitForCurrentImages() {
    this.loadingStatus = LOADING_CURRENT;
    this.waitForImages(this.currentMinMaxIDs);
    this.loadingStatus = LOADING_NOTHING;
  }

  /**
   * waits for the current background images to get loaded. Events are casted
   * while loading and when finished.
   */
  public void waitForCurrentBackgroundImages() {
    this.loadingStatus = LOADING_CURRENT_BACKGROUND;
    this.waitForImages(this.currentBackgroundMinMaxIDs);
    this.loadingStatus = LOADING_NOTHING;
  }

  ////////////////////// prepareImages and loadImages //////////////////////////

  private void addImages(String propertyKey, String base) {
    this.table.put(propertyKey, this.makeImages(
      this.mproperties.getPropertyValues(propertyKey), base));
  }

  private Image[] makeImages(String[] imageStrings, String base) {
    Image[] images = new Image[imageStrings.length];
    for (int i=0; i<imageStrings.length; ++i) {
      images[i] = this.multimediaProvider.getImage(base + File.separatorChar + imageStrings[i]);
    }
    return images;
  }

  private void prepareImages(String[] keys, int imagesPerID) {
    int counter = this.idCounter * imagesPerID;
    for (int i=0; i<keys.length; ++i) {
      Image[] images = (Image[]) this.table.get(keys[i]);
      for (int j=0; j<images.length; ++j) {
        if (images[j] != null) // first backgroundImage is null
          this.mediaTracker.addImage(images[j], counter++ / imagesPerID);
      }
    }
    this.idCounter = (--counter) / imagesPerID;
  }

  private void waitForImages(int[] minMaxIDs) {

    // cast event for starting
    this.castEvent(new LoadEvent(LoadEvent.LOADING_STARTED));

    // recurse on all IDs
    for (int i=minMaxIDs[0]; i<=minMaxIDs[1]; i++) {

      // wait for loading
      try { this.mediaTracker.waitForID(i);}
      catch (InterruptedException e) { e.printStackTrace(); }

      // cast event for loaded id
      if (this.mediaTracker.isErrorID(i))
        this.castEvent(new LoadEvent(LoadEvent.LOAD_ERROR));
      else
        this.castEvent(new LoadEvent(LoadEvent.LOADED_ONE));
    }

    // cast event for finishing
    this.castEvent(new LoadEvent(LoadEvent.LOADED_ALL));
  }

  ////////////////////////// other public methods ////////////////////////////

  /**
   * registers a <code>LoadListener</code> to this <code>Loader</code>.
   *
   * @param listener a <code>LoadListener</code> to receive <code>LoadEvents</code>.
   */
  public void addLoadListener(LoadListener listener) {
    this.listenerVector.addElement(listener);
  }

  // casts an Event to all registered listeners
  private void castEvent(LoadEvent e) {
    for (int i=0; i<this.listenerVector.size(); i++) {
      ((LoadListener) this.listenerVector.elementAt(i)).imageLoaded(e);
    }
  }

  /**
   * returns the actual loading status as encoded by the <code>LOADING_XXXX</code> fields.
   */
  public int getLoadingStatus() {
    return this.loadingStatus;
  }

  /**
   * sets the roundCounter, that is counting the number of memory games played.
   * This method is needed when newgame is clicked to avoid the automatic increment.
   *
   * @param rounds the new number for the roundcounter
   */
  public void setRoundCounter(int rounds) {
    this.roundCounter = rounds;
  }

  /**
   * returns the number of memory games played.
   *
   * @return the actual value of the roundCounter.
   */
  public int getRoundCounter() {
    return this.roundCounter;
  }
}