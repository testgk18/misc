/*
 * Created on 03.11.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package einstein;

import java.awt.Image;
import java.util.StringTokenizer;
import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author zoppke
 * 
 * TODO
 */
public class TimelineHandler extends DefaultHandler {

    ////////////////////////////// encoding tag names /////////////////////////

    private static final String TIMELINE = "timeline";

    private static final String CHAPTER = "chapter";

    private static final String NAME = "name";

    private static final String WIDTH = "width";

    private static final String HEIGHT = "height";

    private static final String TEXT = "text";

    private static final String IMAGE = "image";

    private static final String X = "x";

    private static final String Y = "y";

    private static final String FILE = "file";

    //////////////////////////////// fields ///////////////////////////////////

    // reference to main instance
    private Main main;

    private Timeline timeline;

    private Chapter currentChapter = null;

    /////////////////////////////// constructor ///////////////////////////////

    public TimelineHandler(Main main) {
        this.main = main;
    }

    //////////////////////////// defaultHandler methods ///////////////////////

    public void startElement(String uri, String localName, String qName,
            Attributes attrs) throws SAXException {
        if (qName.equals(TIMELINE)) {
            timeline = new Timeline();
            timeline.setName(attrs.getValue(NAME));
            timeline.setWidth(Integer.parseInt(attrs.getValue(WIDTH)));
        } else if (qName.equals(CHAPTER)) {
            currentChapter = new Chapter();
            currentChapter.setName(attrs.getValue(NAME));
            currentChapter.setHeight(Integer.parseInt(attrs.getValue(HEIGHT)));
            timeline.addChapter(currentChapter);
            System.out.println("Reading chapter: " + attrs.getValue(NAME));
        } else if (qName.equals(TEXT) || qName.equals(IMAGE)) {
            Entry entry = new Entry();
            String s = timeline.getName() + "/" + currentChapter.getName()
                    + "/" + attrs.getValue(FILE);
            Image img = main.getResourceAsImage(s);
            if (img == null) {
                System.out.println("cannot find image: " + s);
            }
            entry.setFile(img);
            entry.setX(Integer.parseInt(attrs.getValue(X)));
            entry.setY(Integer.parseInt(attrs.getValue(Y)));
            if (qName.equals(IMAGE)) {
                entry.setName(attrs.getValue(NAME));
            }
            currentChapter.addEntry(entry);
        }
    }

    public Timeline getTimeline() {
        return timeline;
    }
}