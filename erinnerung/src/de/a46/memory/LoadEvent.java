// Copyright (c) 2001 by till zoppke, blaulicht@a46.de
package de.a46.memory;

/**
 * Class <code>LoadEvent</code> wraps information about what is happening during loading images.
 * a <code>LoadEvent</code> is casted by a {@link Loader} and is received by a {@link LoadListener}.
 *
 * @author Till Zoppke
 * @version 1.0
 */
public class LoadEvent {

  /**
   * encoding that loading just started.
   */
  public static final int LOADING_STARTED = 0;

  /**
   * encoding that all images belonging to current loadlevel have been loaded.
   */
  public static final int LOADED_ONE = 1;

  /**
   * encoding that all images belonging to this load-job have been loaded.
   */
  public static final int LOADED_ALL = 2;

  /**
   * encoding that there was an error while loading.
   */
  public static final int LOAD_ERROR = 3;

  private int eventType;

  /**
   * constructor
   *
   * @param eventType the type of event this instance shall represent.
   */
  public LoadEvent(int eventType) {
    this.eventType = eventType;
  }

  /**
   * returns the event type, this loadEvent represents.
   *
   * @return an <code>int</code> encoding the eventType as the static field above.
   */
  public int getEventType() {
    return this.eventType;
  }
}