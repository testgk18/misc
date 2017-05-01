/*
 * Created on 13.11.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package einstein.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.Format;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import einstein.Main;

/**
 * @author zoppke
 * 
 * TODO
 */
public class ConfirmPanel extends JPanel {

    ///////////////////////////// database stuff //////////////////////////////

    public static final String DATABASE_DRIVER = "org.gjt.mm.mysql.Driver";

    public static final String DATABASE_URL = "jdbc:mysql://petrus.mi.fu-berlin.de:59009/swp";

    //public static final String DATABASE_URL =
    // "jdbc:mysql://page.mi.fu-berlin.de:3306/einstein";

    public static final String DATABASE_USER = "swp";

    //public static final String DATABASE_USER = "einstein";

    public static final String DATABASE_PASS = "swp";

    //public static final String DATABASE_PASS = "1!n5+1!n";

    /////////////////////////////// fields ////////////////////////////////////

    private Main main;

    private BufferedImage image;

    private Vector errors = null;

    private String file = null;

    /////////////////////////////// components ////////////////////////////////

    private JLabel headerLabel;

    private JLabel nameLabel;

    private JTextField nameField;

    private JLabel ageLabel;

    private JTextField ageField;

    private JPanel imgPanel;

    private JLabel titleLabel;

    private JTextField titleField;

    private JLabel descriptionLabel;

    private JTextField descriptionField;

    private JButton saveButton;

    private JButton backButton;

    /////////////////////////////// constructor ///////////////////////////////

    public ConfirmPanel(Main main) {
        this.main = main;
    }

    //////////////////////////////// methods //////////////////////////////////

    public void init() {

        // set layout
        setLayout(new GridBagLayout());

        // ceate components
        imgPanel = new JPanel() {
            public Dimension getPreferredSize() {
                return new Dimension(image.getWidth(), image.getHeight());
            }

            public void paint(Graphics g) {
                g.drawImage(image, 0, 0, imgPanel);
            }
        };
        titleLabel = new JLabel("Titel: ");
        titleField = new JTextField(15);
        descriptionLabel = new JLabel("Beschreibung: ");
        descriptionField = new JTextField(30);
        backButton = new JButton("Zurück");
        saveButton = new JButton("Speichern");
        headerLabel = new JLabel("Das ist die Collage von:");
        nameLabel = new JLabel("Name");
        nameLabel.setFont(Font.decode("Arial plain 10"));
        nameField = new JTextField(12);
        ageLabel = new JLabel("Alter");
        ageLabel.setFont(Font.decode("Arial plain 10"));
        NumberFormat.getNumberInstance().setParseIntegerOnly(true);
        Format formatter = NumberFormat.getNumberInstance();
        ageField = new JFormattedTextField(formatter);
        ageField.setColumns(2);

        // add listeners to buttons
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performOkAction();
            }
        });
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performCancelAction();
            }
        });

        // add components
        add(headerLabel, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.5,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
                        0, 0, 0, 0), 0, 0));
        add(nameField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.5,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        add(ageField, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.5,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        add(nameLabel, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.5,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        add(ageLabel, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.5,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        add(imgPanel, new GridBagConstraints(1, 3, 2, 1, 0.0, 0.5,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
                        0, 0, 0, 0), 0, 0));
        add(titleLabel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.5,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0,
                        0, 0, 0), 0, 0));
        add(titleField, new GridBagConstraints(1, 4, 2, 1, 0.0, 0.5,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
                        0, 0, 0), 0, 0));
        add(descriptionLabel, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.5,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0,
                        0, 0, 0), 0, 0));
        add(descriptionField, new GridBagConstraints(1, 5, 2, 1, 0.0, 0.5,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        add(backButton, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.5,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
                        0, 0, 0, 0), 0, 0));
        add(saveButton, new GridBagConstraints(3, 3, 1, 1, 1.0, 0.5,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
                        0, 0, 0, 0), 0, 0));

        // set colors
        setBackground(main.getColor());
        backButton.setBackground(main.getColor());
        saveButton.setBackground(main.getColor());
    }

    void performCancelAction() {
        main.setDisplay(Main.EDIT);
    }

    void performOkAction() {

        // open fileChooser
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileFilter() {
            public boolean accept(File f) {
                String s = f.getAbsolutePath().toLowerCase();
                return s.endsWith(".jpg") || s.endsWith(".jpeg");
            }

            public String getDescription() {
                return "JPEG Bilder";
            }
        });
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setFileHidingEnabled(true);
        chooser.setDialogTitle("Collage speichern");
        chooser.setSelectedFile(new File("./einstein.jpeg"));
        
        chooser.showSaveDialog(this);
        file = chooser.getSelectedFile().getAbsolutePath();

        // List of error messages
        errors = new Vector();

        // byte array as buffer for JPEG image data
        //byte[] data = null;

        try {
            // create output streams
            //ByteArrayOutputStream bos = new ByteArrayOutputStream(100000);
            FileOutputStream bos = new FileOutputStream(file);
            ImageOutputStream ios = ImageIO.createImageOutputStream(bos);

            // get suitable writer to create a JPEG and connect it to streams
            Iterator iter = ImageIO.getImageWritersByFormatName("JPEG");
            ImageWriter imgWriter = (ImageWriter) iter.next();
            imgWriter.setOutput(ios);

            // write image to byte array
            imgWriter.write(image);
            //data = bos.toByteArray();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
            errors.add("Bild konnte nicht gerendert werden.");
        }

        try {
            file = URLEncoder.encode(file, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            errors.add("Dateiname konnte nicht kodiert werden.");
        }

        //        // create driver
        //        try {
        //            Class.forName(DATABASE_DRIVER);
        //        } catch (ClassNotFoundException e) {
        //            e.printStackTrace();
        //            errors.add("Datenbanktreiber wurde nicht gefunden.");
        //        }
        //
        //        // get connection
        //        Connection con = null;
        //        try {
        //            con = DriverManager.getConnection(DATABASE_URL, DATABASE_USER,
        //                    DATABASE_PASS);
        //        } catch (SQLException e) {
        //            e.printStackTrace();
        //            errors.add("Keine Verbindung zur Datenbank.");
        //        }
        //
        //        // get new id
        //        int id = 0;
        //        System.out.println("get new id");
        //        try {
        //            String sql = "SELECT MAX(ID) FROM collage";
        //            Statement statement = con.createStatement();
        //            statement.execute(sql);
        //            java.sql.ResultSet rs = statement.getResultSet();
        //            rs.next();
        //            String s = rs.getString(1);
        //            id = s.equalsIgnoreCase("null") ? 0 : Integer.parseInt(s) + 1;
        //        } catch (SQLException e) {
        //            e.printStackTrace();
        //            errors.add("Maximale user-ID konnte nicht ausgelesen werden.");
        //        }
        //
        //        // save image as BLOB
        //        System.out.println("insert row");
        //        try {
        //            ByteArrayInputStream bis = new ByteArrayInputStream(data);
        //            String sql = "INSERT INTO `collage` (`id`, `projekt_id`, `titel`, "
        //                    + "`beschreibung`, `name`, `bild`, `alter`) "
        //                    + "VALUES ( ? , ? , ? , ? , ? , ? , ? ) ";
        //            PreparedStatement pst = con.prepareStatement(sql);
        //            pst.setInt(1, id);
        //            pst.setString(2, main.getProjectID());
        //            pst.setString(3, titleField.getText());
        //            pst.setString(4, descriptionField.getText());
        //            pst.setString(5, nameField.getText());
        //            pst.setBinaryStream(6, bis, data.length);
        //            pst.setString(7, ageField.getText());
        //            pst.execute();
        //        } catch (SQLException e) {
        //            e.printStackTrace();
        //            errors
        //                    .add("Bild konnte nicht in der Datenbank gespeichert werden.");
        //        }
        //
        //        // close connection
        //        if (con != null) {
        //            try {
        //                con.close();
        //            } catch (SQLException e) {
        //                e.printStackTrace();
        //                errors.add("Fehler beim Schließen der Datenbankverbindung.");
        //            }
        //        }

        // go on to finish panel
        main.setDisplay(Main.FINISH);
    }

    /**
     * @param image
     */
    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public Vector getErrors() {
        return errors;
    }

    /**
     * @return
     */
    public String getPHP() {
        return main.getPhpUpload() + "?path=" + file + "?sessionKey="
                + main.getSessionKey() + "?name=" + nameField.getText()
                + "?alter=" + ageField.getText() + "?titel="
                + titleField.getText() + "?beschreibung="
                + descriptionField.getText();
    }
}