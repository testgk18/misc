package einstein;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.AccessControlException;
import java.security.Permission;
import java.util.Iterator;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import einstein.unredo.AddUnredo;
import einstein.unredo.Unredo;
import einstein.unredo.UnredoListener;
import einstein.view.CollagePanel;
import einstein.view.ConfirmPanel;
import einstein.view.FinishPanel;
import einstein.view.ImagePanel;
import einstein.view.MainPanel;

/*
 * Created on 03.11.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author zoppke
 * 
 * TODO
 */
public class Main extends JApplet {

    //////////////////////////// static fields ////////////////////////////////

    public static final Point NULL_POINT = new Point(-1000, -1000);

    public static final int EDIT = 0;

    public static final int CONFIRM = 1;

    public static final int FINISH = 2;

    /////////////////////////// applet parameters /////////////////////////////

    // path to data. Default is "data"
    private String dataPath = "data";

    // path to einstein timeline
    private String einsteinFile = "einstein/einstein.xml";

    // page to be invoked after successfully saving the image to filesystem
    private String phpUpload = "dummy.php";

    // backround color for e.g. the border
    private String bgColor = "9CC3F7";

    // color for e.g. the buttons
    private String color = "C6DFF7";

    // url of the html-page for help
    private String helpURL;

    /**
     * @return Returns the phpUpload.
     */
    public String getPhpUpload() {
        return phpUpload;
    }

    /**
     * @return Returns the sessionKey.
     */
    public String getSessionKey() {
        return sessionKey;
    }

    // session key to verify whether file-upload is allowed
    // note: this is just passed to the uploading php site.
    private String sessionKey = "123abc";

    ///////////////////////////// data components /////////////////////////////

    // history timeline to be parsed from xml
    private Timeline timeline = null;

    private Collage collage = null;

    /////////////////////////// dragging fields ///////////////////////////////

    // image, that is currently dragged
    private Entry dragEntry = null;

    // left upper corner of currently dragging image
    private Point draggingPoint = NULL_POINT;

    ////////////////////////////// unredo stuff ///////////////////////////////

    // list of unredos
    private Vector unredos = new Vector();

    // pointer of which element to unredo
    private int unredoPointer = 0;

    // list of unredo listeners
    private Vector unredoListeners = new Vector();

    // main panel
    MainPanel mainPanel;

    // confirm panel
    ConfirmPanel confirmPanel;

    /////////////////////////////// other /////////////////////////////////////

    // flag indicating wether the program was started as applet or as
    // application
    private boolean isApplet = true;

    private FinishPanel finishPanel;

    //////////////////////////// applet methods ///////////////////////////////

    public void start() {

    }

    public void init() {

        // applet stuff
        if (isApplet) {

            // read parameters
            dataPath = getParameter("dataPath");
            einsteinFile = getParameter("einsteinFile");
            phpUpload = getParameter("phpUpload");
            sessionKey = getParameter("sessionKey");
            color = getParameter("color");
            bgColor = getParameter("bgColor");
            helpURL = getParameter("helpURL");

            // check file access
            try {
                // anonymous security manager granting any permission
                new SecurityManager() {
                    public void checkPermission(Permission permission) {
                        System.out.println("checked");
                        // grant any permission
                    }

                    public void checkPermission(Permission permission,
                            Object obj) {
                        System.out.println("checked");
                        // grant any permission
                    }

                    public void checkConnect(String host, int port) {

                    }
                };
                // user allowed the signed applet. Set flag.
                System.out.println("granted permission");

            } catch (AccessControlException ace) {
                // User didn't allow the signed applet.
                // Reset flag and display message.
                ace.printStackTrace();
            }
        }

        // parse timelines
        try {
            // create parser
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();

            // parse history timeline
            TimelineHandler th = new TimelineHandler(this);
            InputStream in = getResourceAsStream(einsteinFile);
            parser.parse(in, th);
            timeline = th.getTimeline();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // create collage
        collage = new Collage();

        // create panels
        mainPanel = new MainPanel(this);
        mainPanel.init();
        confirmPanel = new ConfirmPanel(this);
        confirmPanel.init();

        // set main panel as content pane
        setContentPane(mainPanel);
    }

    /**
     * @return Returns the dataPath.
     */
    public String getDataPath() {
        return dataPath;
    }

    ////////////////////////////// io methods /////////////////////////////////

    /**
     * @param path
     * @return
     */
    public Image getResourceAsImage(String name) {

        // get url. If path cannot be resolved, return null.
        URL url = Main.class.getClassLoader().getResource(name);
        if (url == null) {
            return null;
        }

        if (isApplet) {
            Image img = getImage(url);
            // create new mediatracker. Ok, do this over and over again is
            // not nice, but I don't like to have a field hanging around here.
            MediaTracker tracker = new MediaTracker(this);
            tracker.addImage(img, 1);
            try {
                tracker.waitForAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return img;
        }

        // check for png
        // hopefully this solves the bug that PNGs are not loaded
        if (name.endsWith("png")) {
            Iterator iterReader = ImageIO.getImageReadersByFormatName("PNG");
            ImageReader reader = (ImageReader) iterReader.next();
        }

        // load image
        try {
            return ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Opens an <code>inputStream</code> to the specified resource.
     * 
     * @param name
     *            a <code>string</code> specifying the resource to load
     * @return an <code>inputStream</code> if the resource could be found,
     *         <br>
     *         or <code>null</code> otherwise
     */
    public InputStream getResourceAsStream(String name) {
        return Main.class.getClassLoader().getResourceAsStream(name);
    }

    /**
     * @return Returns the draggingImage.
     */
    public Entry getDragEntry() {
        return dragEntry;
    }

    /**
     * @param draggingImage
     *            The draggingImage to set.
     */
    public void setDragEntry(Entry entry) {
        if (entry == null && dragEntry != null) {
            mainPanel.paintDraggingImage(mainPanel.getGraphics());
            CollagePanel collagePanel = mainPanel.getRightPanel()
                    .getCollagePanel();
            Point collagePoint = MainPanel.getLocationOnMainPanel(collagePanel);
            Dimension collageSize = collagePanel.getSize();
            int dragWidth = dragEntry.getFile().getWidth(mainPanel);
            int dragHeight = dragEntry.getFile().getHeight(mainPanel);
            Dimension dragSize = new Dimension(dragWidth, dragHeight);
            Rectangle collageRectangle = new Rectangle(collagePoint,
                    collageSize);
            Rectangle draggingRectangle = new Rectangle(draggingPoint, dragSize);

            // check, if we hit the collage
            if (collageRectangle.intersects(draggingRectangle)) {
                Point newPoint = new Point(draggingPoint.x - collagePoint.x,
                        draggingPoint.y - collagePoint.y);
                collage.addEntry(dragEntry, newPoint);
                addUnredo(new AddUnredo(dragEntry, newPoint, collage
                        .getActiveLayer(), collage));
            }
        }
        dragEntry = entry;
    }

    /**
     * @return Returns the collage.
     */
    public Collage getCollage() {
        return collage;
    }

    /**
     * @return Returns the draggingPoint.
     */
    public Point getDraggingPoint() {
        return draggingPoint;
    }

    /**
     * @param draggingPoint
     *            The draggingPoint to set.
     */
    public void setDraggingPoint(Point draggingPoint) {
        if (draggingPoint == null) {
            draggingPoint = NULL_POINT;
        }
        if (!this.draggingPoint.equals(draggingPoint)) {
            mainPanel.paintDraggingImage(mainPanel.getGraphics());
            this.draggingPoint = draggingPoint;
            mainPanel.paintDraggingImage(mainPanel.getGraphics());
        }
    }

    ////////////////////////// static methods /////////////////////////////////

    public static void main(String[] args) {
        final JFrame myFrame = new JFrame();
        Main main = new Main();
        main.isApplet = false;
        myFrame.setContentPane(main);
        myFrame.pack();
        myFrame.setTitle("Der Einstein Collagist");
        myFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                myFrame.dispose();
            }
        });
        main.init();
        main.start();
        myFrame.setVisible(true);

    }

    /**
     * @param unredo
     */
    public void addUnredo(Unredo unredo) {
        while (unredos.size() > unredoPointer) {
            unredos.remove(unredos.size() - 1);
        }
        unredos.add(unredo);
        unredoPointer++;
        updateUnredos();
    }

    /**
     * @param action
     */
    public void addUnredoListener(UnredoListener listener) {
        unredoListeners.add(listener);
    }

    /**
     *  
     */
    public void undo() {
        unredoPointer--;
        Unredo unredo = (Unredo) unredos.get(unredoPointer);
        unredo.undo();
        updateUnredos();
    }

    /**
     *  
     */
    private void updateUnredos() {
        boolean undo = unredoPointer > 0;
        boolean redo = unredoPointer < unredos.size();
        Iterator iter = unredoListeners.iterator();
        while (iter.hasNext()) {
            UnredoListener listener = (UnredoListener) iter.next();
            listener.unredoChanged(undo, redo);
        }
    }

    /**
     *  
     */
    public void redo() {
        Unredo unredo = (Unredo) unredos.get(unredoPointer);
        unredo.redo();
        unredoPointer++;
        updateUnredos();
    }

    /**
     * @param edit2
     */
    public void setDisplay(int mode) {
        switch (mode) {
        case EDIT:
            setContentPane(mainPanel);
            break;
        case CONFIRM:
            confirmPanel.setImage(mainPanel.getCollagePanel().getImage());
            setContentPane(confirmPanel);
            break;
        case FINISH:
            finishPanel = new FinishPanel(confirmPanel, this);
            finishPanel.init();
            setContentPane(finishPanel);
            break;
        }
        getContentPane().setSize(getSize());
        getContentPane().validate();
    }

    /**
     * @return
     */
    public Color getColor() {
        return new Color(Integer.parseInt(color, 16));
    }

    public Color getBGColor() {
        return new Color(Integer.parseInt(bgColor, 16));
    }

    public Dimension getPreferredSize() {
        return new Dimension(750, 560);
    }

    /**
     * @return
     */
    public String getHelpURL() {
        return helpURL;
    }

    /**
     * @return
     */
    public Timeline getTimeline() {
        return timeline;
    }

}