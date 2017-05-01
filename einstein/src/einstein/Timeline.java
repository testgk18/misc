/*
 * Created on 12.12.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package einstein;

import java.util.Vector;

/**
 * @author zoppke
 *
 * TODO
 */
public class Timeline {

    private Vector chapters = new Vector();
    private String name;
    private int width;
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return Returns the width.
     */
    public int getWidth() {
        return width;
    }
    /**
     * @param width The width to set.
     */
    public void setWidth(int width) {
        this.width = width;
    }
    
    public Chapter[] getChapters() {
        Chapter[] retour = new Chapter[chapters.size()];
        chapters.toArray(retour);
        return retour;
    }

    public void addChapter(Chapter chapter) {
        chapters.add(chapter);
    }
    
}
