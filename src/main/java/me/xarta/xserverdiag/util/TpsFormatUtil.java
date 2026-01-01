package me.xarta.xserverdiag.util;

import me.xarta.xserverdiag.config.ConfigHandler;

public final class TpsFormatUtil {
    private static final double GOOD_EDGE = 19.8;
    private static final double WARN_EDGE = 18.0;

    private TpsFormatUtil() {}

    public static String coloredTps(double tps) {
        return applyColor(formatTps(tps), pickColor(tps));
    }

    public static String formatTps(double t) {
        return String.format(java.util.Locale.ROOT, "%.2f", Math.min(20.0, t));
    }

    private static String applyColor(String text, String color) {
        return color + text + "&r";
    }

    private static String pickColor(double tps) {
        String good = ConfigHandler.GOOD_TPS_COLOR.get();
        String warn = ConfigHandler.WARN_TPS_COLOR.get();
        String bad  = ConfigHandler.BAD_TPS_COLOR.get();
        if (tps >= GOOD_EDGE) return good;
        if (tps >= WARN_EDGE) return warn;
        return bad;
    }
}