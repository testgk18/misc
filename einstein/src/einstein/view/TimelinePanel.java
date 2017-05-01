/*
 * Created on 03.11.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package einstein.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.Scrollable;

import einstein.Chapter;
import einstein.Main;
import einstein.Timeline;

/**
 * @author zoppke
 * 
 * TODO
 */
public class TimelinePanel extends JPanel {//implements Scrollable{

    private Main main;

    private Timeline timeline;

    private int height;

    /**
     * 
     * @param timeline
     * @param main
     */
    public TimelinePanel(Timeline timeline, Main main) {
        this.timeline = timeline;
        this.main = main;
    }

    public void init() {

        // set layout
        setLayout(null);
        setBackground(Color.WHITE);
        //setBackground(main.getColor());

        // add panels
        height = 0;
        Chapter[] chapters = timeline.getChapters();
        for (int i = 0; i < chapters.length; ++i) {
            ChapterPanel panel = new ChapterPanel(chapters[i], main);
            add(panel);
            panel.setBounds(0, height, timeline.getWidth(), chapters[i].getHeight());
            height += chapters[i].getHeight();
            panel.init();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#getPreferredSize()
     */
    public Dimension getPreferredSize() {
        return new Dimension(timeline.getWidth(), height);
    }
    
    //////////////////////////// scrollable methods ////////////////////////////
    
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect,
            int orientation, int direction) {
        return 30;
    }

    public int getScrollableUnitIncrement(Rectangle visibleRect,
            int orientation, int direction) {
        return 30;
    }


}