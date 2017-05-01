/*
 * Created on 22.11.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package einstein.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import einstein.Main;

/**
 * @author zoppke
 * 
 * TODO
 */
public class FinishPanel extends JPanel {

    Main main;

    private ConfirmPanel panel;

    /**
     * @param errors
     */
    public FinishPanel(ConfirmPanel panel, Main main) {
        super();
        this.main = main;
        this.panel = panel;
    }

    public void init() {

        Vector errors = panel.getErrors();

        // set layout
        setLayout(new GridBagLayout());

        // set color
        setBackground(main.getColor());
        
        // check, if we saved the image successfully
        if (errors.size() > 0) {

            // headerlabel
            JLabel headerLabel = new JLabel("Fehler!");
            headerLabel.setFont(Font.decode("Arial bold 15"));
            add(headerLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.5,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE,
                    new Insets(0, 0, 0, 0), 0, 0));

            // text labels
            JLabel textLabel1 = new JLabel("Leider konnte das Bild nicht gespeichert werden.");
            JLabel textLabel2 = new JLabel(
                    "Es traten folgende Fehler auf:");
            add(textLabel1, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                    GridBagConstraints.SOUTH, GridBagConstraints.NONE,
                    new Insets(0, 0, 0, 0), 0, 0));
            add(textLabel2, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0,
                    GridBagConstraints.NORTH, GridBagConstraints.NONE,
                    new Insets(0, 0, 0, 0), 0, 0));

            // error messages
            for (int i = 0; i < errors.size(); ++i) {
                String message = (String) errors.get(i);
                JLabel label = new JLabel(message);
                label.setForeground(Color.RED);
                add(label, new GridBagConstraints(0, i + 2, 1, 1, 1.0, 0.5,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0), 0, 0));
            }
        } else {
            // headerlabel
            JLabel headerLabel = new JLabel("Bild gespeichert!");
            headerLabel.setFont(Font.decode("Arial bold 15"));
            add(headerLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.5,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE,
                    new Insets(0, 0, 0, 0), 0, 0));

            // text labels
            JLabel textLabel1 = new JLabel("Das Bild wurde gespeichert.");
            JLabel textLabel2 = new JLabel(
                    "Sie können es nun auf die Website hochladen.");
            add(textLabel1, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                    GridBagConstraints.SOUTH, GridBagConstraints.NONE,
                    new Insets(0, 0, 0, 0), 0, 0));
            add(textLabel2, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0,
                    GridBagConstraints.NORTH, GridBagConstraints.NONE,
                    new Insets(0, 0, 0, 0), 0, 0));

            // button
            JButton button = new JButton("Bild hochladen");
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    URL url;
                    try {
                        url = new URL(main.getCodeBase(), panel.getPHP());
                        main.getAppletContext().showDocument(url);
                    } catch (MalformedURLException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            add(button, new GridBagConstraints(1, 1, 1, 1, 0.5, 0.5,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE,
                    new Insets(0, 0, 0, 0), 0, 0));
            button.setBackground(main.getColor());
        }
    }
}