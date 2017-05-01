/*
 * Created on 03.11.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package einstein.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import einstein.Collage;
import einstein.Main;
import einstein.action.DeleteAction;
import einstein.action.PushDownAction;
import einstein.action.PushUpAction;
import einstein.action.RedoAction;
import einstein.action.UndoAction;
import einstein.unredo.ActiveLayerUnredo;

/**
 * @author zoppke
 * 
 * TODO
 */
public class RightPanel extends JPanel implements ListSelectionListener,
        Observer {

    // button for undo action
    private JButton undoButton;

    // button for redo action
    private JButton redoButton;

    // button for saving the collage to the database
    private JButton okButton;

    // the collage panel to create the collage on
    private CollagePanel collagePanel;

    // reference to main class
    private Main main;

    // reference to the collage
    private Collage collage;

    // a list containing all layers
    private JList list;

    // a scrollpane containing the list
    private JScrollPane scrollPane;

    // button for pushing the focused layer up
    private JButton pushUpButton;

    // button for pushing the focused layer down
    private JButton pushDownButton;

    // button for deleting the focused layer
    private JButton deleteButton;

    // button for opening the help-site in another browser window
    // private JButton helpButton;

    // flag indicating whether we are busy
    // this is used to suppress a listSelectionChanged-event
    private boolean busy = false;

    public RightPanel(Collage collage, Main main) {
        this.collage = collage;
        this.main = main;
    }

    public void init() {

        // set layout
        setLayout(null);

        // add this layersPanel as observer to the collage
        collage.addObserver(this);

        // create list and scrollpane
        String[] listData = { "Hintergrund" };
        list = new JList(listData);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(this);
        scrollPane = new JScrollPane(list);
        collagePanel = new CollagePanel(collage, main);

        // create buttons
        pushUpButton = new JButton(new PushUpAction(main, collage));
        pushDownButton = new JButton(new PushDownAction(main, collage));
        deleteButton = new JButton(new DeleteAction(main, collage));
        undoButton = new JButton(new UndoAction(main));
        redoButton = new JButton(new RedoAction(main));
        okButton = new JButton(new ImageIcon(main
                .getResourceAsImage("images/ok.gif")));
        okButton.setToolTipText("fertig!");
        //helpButton = new JButton("?");
        //helpButton.setToolTipText("Hilfeseite anzeigen");

        // add action listener to okButton
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performOkAction();
            }
        });

        // add action listener to helpButton
//        helpButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                performHelpAction();
//            }
//        });

        // init components
        collagePanel.init();

        // set colors
        setBackground(main.getBGColor());
        undoButton.setBackground(main.getColor());
        redoButton.setBackground(main.getColor());
        pushUpButton.setBackground(main.getColor());
        pushDownButton.setBackground(main.getColor());
        deleteButton.setBackground(main.getColor());
        okButton.setBackground(main.getBGColor());
        //helpButton.setBackground(main.getBGColor());

        // set borders
        okButton.setBorder(null);
        //helpButton.setBorder(null);

        // add components
        add(undoButton);
        add(redoButton);
        add(okButton);
        add(collagePanel);
        add(scrollPane);
        add(pushUpButton);
        add(pushDownButton);
        add(deleteButton);
        //add(helpButton);

        // set bounds of components
        collagePanel.setBounds(0, 0, 360, 360);
        undoButton.setBounds(0, 510, 140, 50);
        redoButton.setBounds(140, 510, 140, 50);
        okButton.setBounds(280, 460, 80, 100);
        scrollPane.setBounds(50, 360, 180, 150);
        pushUpButton.setBounds(230, 360, 50, 75);
        pushDownButton.setBounds(230, 435, 50, 75);
        deleteButton.setBounds(0, 360, 50, 150);
        //helpButton.setBounds(360,0,20,20);
    }

    /**
     *  
     */
    void performHelpAction() {
        try {
            URL url = new URL(main.getCodeBase(), main.getHelpURL());
            main.getAppletContext().showDocument(url, "_blank");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return Returns the collagePanel.
     */
    public CollagePanel getCollagePanel() {
        return collagePanel;
    }

    public void update(Observable o, Object arg) {
        busy = true;
        if (arg == Collage.IMAGE_ADDED) {
            Vector v = new Vector(collage.getEntries());
            v.add("Hintergrund");
            list.setListData(v);
            repaint();
        } else if (arg == Collage.ACTIVE_LAYER_CHANGED) {
            int activeLayer = collage.getActiveLayer();
            if (activeLayer >= 0) {
                list.setSelectedIndex(activeLayer);
            } else {
                list.clearSelection();
            }
        }
        busy = false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
     */
    public void valueChanged(ListSelectionEvent e) {
        if (!busy) {
            int oldLayer = collage.getActiveLayer();
            int newLayer = list.getSelectedIndex();
            if (oldLayer != newLayer) {
                collage.setActiveLayer(newLayer);
                main.addUnredo(new ActiveLayerUnredo(oldLayer, newLayer,
                        collage));
            }
        }
    }

    void performOkAction() {
        main.setDisplay(Main.CONFIRM);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.Container#preferredSize()
     */
    public Dimension getPreferredSize() {
        return new Dimension(360, 560);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#getMinimumSize()
     */
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

}