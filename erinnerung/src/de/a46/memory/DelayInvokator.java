// Copyright (c) 2001 by till zoppke, blaulicht@a46.de
package de.a46.memory;

import java.lang.reflect.Method;

/**
 * Class <code>DelayInvokator</code> allows to do a call on an object
 * delayed by a specific amount of time. At first you should construct an
 * instance by passing all necessary references and values. Then construct a
 * <code>Thread</code> around this and call its <code>start()</code> method.
 *
 * @author Till Zoppke
 * @version 1.0
 */

public class DelayInvokator extends Thread {

  private long delay;
  private Method method;
  private Object obj;
  private Object[] args;

  /**
   * Constructs a new <code>DelayInvokator</code> by passing all necessary infos.
   *
   * @param method  The <code>Method</code> that shall be called.
   * @param obj     The <code>Object</code>, on this the method shall be called.
   * @param args    An array of <code>Object</code>s representing the arguments for the method-call.
   * @param delay   a <code>long</code> containing the millis this
   *                <code>delayInvokator</code> should wait before calling.
   */
  public DelayInvokator(Method method, Object obj, Object[] args, long delay) {
    this.method = method;
    this.obj = obj;
    this.args = args;
    this.delay = delay;
  }

  /**
   * waits for the milliseconds specified in field <code>delay</code>,
   * then the specified <code>Method</code> will be invoked.
   */
  public void run() {
    try { Thread.sleep(this.delay); }
    catch (InterruptedException e) { e.printStackTrace(); }
    try { this.method.invoke(this.obj, this.args); }
    catch (Exception e) { e.printStackTrace(); }
  }
}