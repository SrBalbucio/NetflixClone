package app.netflix;

import balbucio.org.ejsl.utils.ImageUtils;

import javax.swing.*;
import java.awt.*;

public class AppInfo {


    public static Image NETFLIX_SYMBOL = ImageUtils.getImage(AppInfo.class.getResourceAsStream("/netflix_symbol.png"));
    public static Image NETFLIX_LOGO = ImageUtils.getImage(AppInfo.class.getResourceAsStream("/netflix_symbol.png"));
    public static Icon LOADER_GIF = new ImageIcon(AppInfo.class.getResource("/netflixloading-lento.gif"));
}
