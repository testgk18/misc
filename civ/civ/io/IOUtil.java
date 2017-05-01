/*
 * IOUtil.java
 * 
 * Created on 11.02.2004
 */
package civ.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import civ.util.CProperties;

/**
 * @author zoppke
 */
public class IOUtil {

    // private constructor to avoid someone creates an instance of this class
    private IOUtil() {
        // empty
    }

    public static String addIndex(String fileName, int index) {
        return addIndices(fileName, index, index)[0];
    }

    public static String[] addIndices(String fileName, int min, int max) {

        // divide fileName
        int dot = fileName.lastIndexOf('.');
        String path1 = fileName.substring(0, dot);
        String path2 = fileName.substring(dot);

        // add name and indices to return array
        int length = max - min + 1;
        String[] retour = new String[length];
        for (int i = 0; i < retour.length; ++i) {
            retour[i] = path1 + (i + min) + path2;
        }

        // return array;
        return retour;
    }

    public static void parse(InputStream in, DefaultHandler handler)
            throws IOException {
        try {
            // parse from the stream with the given handler
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(in, handler);
        } catch (SAXException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
    }

    // //////////////////////// file access methods
    // /////////////////////////////

    /**
     * Opens an <code>inputStream</code> to the specified file.
     * 
     * @param path
     *            a <code>string</code> as the name of the file to load
     * @return an <code>inputStream</code> if the file could be found, <br>
     *         or <code>null</code> otherwise
     */
    public static InputStream openInputStream(String path) {
        try {
            return new FileInputStream(path);
        } catch (IOException e) {
            return null;
        }
    }

    public static Writer openWriter(File file) throws IOException {
        return new FileWriter(file, false);
    }

    public static File getSettingsFile() {
        return new File(System.getProperty("user.home") //$NON-NLS-1$
                + File.separator
                + CProperties.getInstance().getProperty("SETTINGS_FILE"));
    }
}