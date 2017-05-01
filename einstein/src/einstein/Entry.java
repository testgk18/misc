/*
 * Created on 12.12.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package einstein;

import java.awt.Image;

/**
 * @author zoppke
 * 
 * TODO
 */
public class Entry {

    private String name;

    private int x;

    private int y;

    private Image file;

    /**
     * @return Returns the file.
     */
    public Image getFile() {
        return file;
    }

    /**
     * @param file
     *            The file to set.
     */
    public void setFile(Image file) {
        this.file = file;
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

    /**
     * @return Returns the x.
     */
    public int getX() {
        return x;
    }

    /**
     * @param x
     *            The x to set.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return Returns the y.
     */
    public int getY() {
        return y;
    }

    /**
     * @param y
     *            The y to set.
     */
    public void setY(int y) {
        this.y = y;
    }

    public String toString() {
        return name;
    }

    /**
     * @return
     */
    public boolean isText() {
        return name == null;
    }
}