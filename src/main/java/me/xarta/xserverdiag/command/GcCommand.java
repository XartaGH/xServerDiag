package me.xarta.xserverdiag.command;

import com.mojang.brigadier.CommandDispatcher;
import me.xarta.xserverdiag.config.ConfigHandler;
import me.xarta.xserverdiag.event.TpsTracker;
import me.xarta.xserverdiag.event.UptimeTracker;
import me.xarta.xserverdiag.event.WorldStatsTracker;
import me.xarta.xserverdiag.util.ColorUtil;
import me.xarta.xserverdiag.util.Perms;
import me.xarta.xserverdiag.util.TpsFormatUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@EventBusSubscriber(modid = "xserverdiag", value = Dist.DEDICATED_SERVER)
public final class GcCommand {
    private static final String NODE = "xserverdiag.gc";
    private static final String CMD = "gc";

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> d = event.getDispatcher();

        d.register(
                Commands.literal(CMD)
                        .executes(ctx -> {
                            CommandSourceStack source = ctx.getSource();
                            if (Perms.denied(source, NODE, ConfigHandler.GC_PERMISSION.get(), CMD)) {
                                return 0;
                            }

                            List<String> lines = ConfigHandler.GC_FORMAT.get();
                            if (lines.isEmpty()) {
                                source.sendSystemMessage(Component.literal("§cNo gc-format lines configured."));
                                return 1;
                            }

                            ServerLevel level = source.getLevel();

                            long maxMem   = Runtime.getRuntime().maxMemory();
                            long totalMem = Runtime.getRuntime().totalMemory();
                            long freeMem  = Runtime.getRuntime().freeMemory();

                            String maxStr   = mbWithGrouping(maxMem);
                            String totalStr = mbWithGrouping(totalMem);
                            String freeStr  = mbWithGrouping(freeMem);

                            String uptimeStr = UptimeTracker.getFormattedUptime();
                            String worldName = level.dimension().location().toString();

                            WorldStatsTracker.Stats s = WorldStatsTracker.snapshot(level);

                            double tpsNow = TpsTracker.getTpsCurrent();
                            String tpsColored = TpsFormatUtil.coloredTps(tpsNow);

                            for (String raw : lines) {
                                String msg = raw
                                        .replace("%uptime%",   uptimeStr)
                                        .replace("%tps%",      tpsColored)
                                        .replace("%max_mem%",  maxStr)
                                        .replace("%all_mem%",  totalStr)
                                        .replace("%free_mem%", freeStr)
                                        .replace("%world%",    worldName)
                                        .replace("%chunks%",   String.valueOf(s.chunks))
                                        .replace("%entities%", String.valueOf(s.entities))
                                        .replace("%tiles%",    String.valueOf(s.tiles));

                                msg = ColorUtil.ampersandToSection(msg);
                                source.sendSystemMessage(Component.literal(msg));
                            }
                            return 1;
                        })
        );
    }

    private static String mbWithGrouping(long bytes) {
        long mb = Math.max(0L, bytes / (1024L * 1024L));
        NumberFormat nf = NumberFormat.getIntegerInstance(Locale.ROOT);
        String grouped = nf.format(mb);
        return grouped.replace(',', ' ') + " MB";
    }
}