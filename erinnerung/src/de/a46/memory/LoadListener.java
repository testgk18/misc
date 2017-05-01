// Copyright (c) 2001 by till zoppke, blaulicht@a46.de
package de.a46.memory;

/**
 * Interface <code>LoadListener</code> defines one method, that will be called by
 * a {@link Loader}, when a {@link LoadEvent} is casted.
 *
 * @author Till Zoppke
 * @version 1.0
 */
public interface LoadListener {

  /**
   * Predefines an event-reaction method. It is called by start of loading,
   * complete loading of images of a certain level, finish of loading or a
   * loading error.
   *
   * @param e the <code>LoadEvent</code> being casted.
   * @see Starter#imageLoaded(LoadEvent)
   */
  public void imageLoaded(LoadEvent e);
}