/*
 * Created on 05.11.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package einstein;

import java.awt.Image;
import java.util.Vector;

/**
 * @author zoppke
 * 
 * TODO
 */
public class Chapter {

    private String name;

    private int height;

    private Vector entries = new Vector();

    /**
     * @return Returns the entries.
     */
    public Vector getEntries() {
        return entries;
    }

    /**
     * @param entries
     *            The entries to set.
     */
    public void setEntries(Vector entries) {
        this.entries = entries;
    }

    /**
     * @return Returns the height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param height
     *            The height to set.
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    public void addEntry(Entry entry) {
        entries.add(entry);
    }
}