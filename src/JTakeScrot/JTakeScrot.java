package JTakeScrot;


import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: Joska
 * Date: 31/08/13
 * Time: 21:29
 * To change this template use File | Settings | File Templates.
 */

public class JTakeScrot {

    // Take a screen shot
    public static BufferedImage scrot() throws Exception {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle screenRectangle = new Rectangle(screenSize);
        Robot robot = new Robot();
        return robot.createScreenCapture(screenRectangle);
    }
    // Get extension
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
    public static void takeScreenshot() throws Exception {
        // Take the screen shot
        BufferedImage screenShot = scrot();
        // Choose file
        final JFileChooser fc = new JFileChooser();
        fc.addChoosableFileFilter(new ImageFilter());
        int returnVal = fc.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
                ImageIO.write(screenShot, getExtension(file), file);
            } catch (Exception e) {
                System.out.println("Error: You selected an invalid file type!");
            }
        }
    }
    //Obtain the image URL
    protected static Image createImage(String path, String description) {
        URL imageURL = JTakeScrot.class.getResource(path);

        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }

    public static void main(String[] args) {
        // Create the tray icon
        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon =
                new TrayIcon(createImage("images/bulb.gif", "JTakeScrot"));
        final SystemTray tray = SystemTray.getSystemTray();
        // Create a popup menu components
        MenuItem exitItem = new MenuItem("Exit");

        //Add components to popup menu
        popup.add(exitItem);

        trayIcon.setPopupMenu(popup);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
            return;
        }

        trayIcon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    takeScreenshot();
                } catch (Exception e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        });

        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tray.remove(trayIcon);
                System.exit(0);
            }
        });
    }
}
    class ImageFilter extends FileFilter {

        //Accept all directories and all gif, jpg, tiff, or png files.
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }

            String extension = Utils.getExtension(f);
            if (extension != null) {
                if (extension.equals(Utils.gif) ||
                        extension.equals(Utils.jpeg) ||
                        extension.equals(Utils.jpg) ||
                        extension.equals(Utils.png)) {
                    return true;
                } else {
                    return false;
                }
            }

            return false;
        }

        //The description of this filter
        public String getDescription() {
            return "Just Images";
        }
    }

 class Utils {

    public final static String jpeg = "jpeg";
    public final static String jpg = "jpg";
    public final static String gif = "gif";
    public final static String tiff = "tiff";
    public final static String tif = "tif";
    public final static String png = "png";

    /*
     * Get the extension of a file.
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
}