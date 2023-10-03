package app.netflix;

import app.netflix.model.Account;
import balbucio.org.ejsl.utils.ImageUtils;

import javax.swing.*;
import java.awt.*;

public class AppInfo {


    public static Image NETFLIX_SYMBOL = ImageUtils.getImage(AppInfo.class.getResourceAsStream("/netflix_symbol.png"));
    public static Image NETFLIX_LOGO = ImageUtils.getImage(AppInfo.class.getResourceAsStream("/netflix_logo.png"));
    public static Icon LOADER_GIF = new ImageIcon(AppInfo.class.getResource("/netflixloading-lento.gif"));
    public static Cursor LEFT_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(Icons.ARROW_LEFT, new Point(0,0), "CURSOR_LEFT_LISTENER");
    public static Cursor RIGHT_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(Icons.ARROW_RIGHT, new Point(0,0), "CURSOR_RIGHT_LISTENER");


    // INFORMATION
    public static boolean LOGGED = false;
    public static Account ACCOUNT = new Account("-1", "DEVELOPER", "dev@dev.com");
    public static boolean USE_LOGIN = true; // autorizar login
}
