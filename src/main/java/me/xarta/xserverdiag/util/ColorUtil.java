package me.xarta.xserverdiag.util;

public final class ColorUtil {
    private ColorUtil() {}
    public static String ampersandToSection(String s) {
        if (s == null || s.isEmpty()) return s;
        s = s.replace("&amp;", "&");
        return s.replace("&", "§");
    }
}
