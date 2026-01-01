package me.xarta.xserverdiag.event;

import me.xarta.xserverdiag.config.ConfigHandler;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(value = Dist.DEDICATED_SERVER, modid = "xserverdiag")
public final class UptimeTracker {
    private static long startNanos = -1L;

    private static final int MAX_PARTS = 0;

    private UptimeTracker() {}

    @SubscribeEvent
    public static void onServerTickPost(ServerTickEvent.Post e) {
        if (startNanos == -1L) startNanos = System.nanoTime();
    }


    public static String getFormattedUptime() {
        if (startNanos <= 0L) {
            return "0" + ConfigHandler.SECOND.get();
        }

        long seconds = Math.max(0L, (System.nanoTime() - startNanos) / 1_000_000_000L);

        long years  = seconds / (365L * 24 * 3600); seconds %= (365L * 24 * 3600);
        long months = seconds / (30L  * 24 * 3600); seconds %= (30L  * 24 * 3600);
        long weeks  = seconds / (7L   * 24 * 3600); seconds %= (7L   * 24 * 3600);
        long days   = seconds / (24   * 3600);      seconds %= (24   * 3600);
        long hours  = seconds / 3600;               seconds %= 3600;
        long mins   = seconds / 60;
        long secs   = seconds % 60;

        java.util.List<String> parts = new java.util.ArrayList<>(7);
        append(parts, years,  ConfigHandler.YEAR.get());
        append(parts, months, ConfigHandler.MONTH.get());
        append(parts, weeks,  ConfigHandler.WEEK.get());
        append(parts, days,   ConfigHandler.DAY.get());
        append(parts, hours,  ConfigHandler.HOUR.get());
        append(parts, mins,   ConfigHandler.MINUTE.get());
        append(parts, secs,   ConfigHandler.SECOND.get());

        if (parts.isEmpty()) {
            return "0" + ConfigHandler.SECOND.get();
        }

        int limit = (MAX_PARTS > 0) ? Math.min(MAX_PARTS, parts.size()) : parts.size();
        StringBuilder sb = new StringBuilder(parts.size() * 6);
        for (int i = 0; i < limit; i++) {
            if (i > 0) sb.append(' ');
            sb.append(parts.get(i));
        }
        return sb.toString();
    }

    private static void append(java.util.List<String> out, long value, String suffix) {
        if (value > 0) out.add(value + suffix);
    }

}