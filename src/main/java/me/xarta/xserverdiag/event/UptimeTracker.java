package me.xarta.xserverdiag.event;

import me.xarta.xserverdiag.config.ConfigHandler;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(value = Dist.DEDICATED_SERVER, modid = "xserverdiag")
public final class UptimeTracker {
    private static long startNanos = -1L;

    private UptimeTracker() {}

    @SubscribeEvent
    public static void onServerTickPost(ServerTickEvent.Post e) {
        if (startNanos == -1L) startNanos = System.nanoTime();
    }

    public static String getFormattedUptime() {
        if (startNanos <= 0L) return "0" + ConfigHandler.SECOND.get();
        long seconds = Math.max(0L, (System.nanoTime() - startNanos) / 1_000_000_000L);

        long years  = seconds / (365L * 24 * 3600);
        seconds    %= (365L * 24 * 3600);
        long months = seconds / (30L * 24 * 3600);
        seconds    %= (30L * 24 * 3600);
        long weeks  = seconds / (7L * 24 * 3600);
        seconds    %= (7L * 24 * 3600);
        long days   = seconds / (24 * 3600);
        seconds    %= (24 * 3600);
        long hours  = seconds / 3600;
        seconds    %= 3600;
        long mins   = seconds / 60;
        long secs   = seconds % 60;

        StringBuilder sb = new StringBuilder();
        if (years  > 0) sb.append(years ).append(ConfigHandler.YEAR .get()).append(' ');
        if (months > 0) sb.append(months).append(ConfigHandler.MONTH.get()).append(' ');
        if (weeks  > 0) sb.append(weeks ).append(ConfigHandler.WEEK .get()).append(' ');
        if (days   > 0) sb.append(days  ).append(ConfigHandler.DAY  .get()).append(' ');
        if (hours  > 0) sb.append(hours ).append(ConfigHandler.HOUR .get()).append(' ');
        if (mins   > 0) sb.append(mins  ).append(ConfigHandler.MINUTE.get()).append(' ');
        sb.append(secs).append(ConfigHandler.SECOND.get());

        return sb.toString().trim();
    }
}