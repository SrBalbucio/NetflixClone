package app.netflix.utils;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public class SanitizerUtils {

    public static String clean(String input) {
        return Jsoup.clean(input, Safelist.simpleText()).replaceAll("[`']", "");
    }
}
