package me.xarta.xserverdiag.command;

import com.mojang.brigadier.CommandDispatcher;
import me.xarta.xserverdiag.config.ConfigHandler;
import me.xarta.xserverdiag.util.PermissionUtil;
import me.xarta.xserverdiag.util.ColorUtil;
import me.xarta.xserverdiag.event.TpsTracker;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = "xserverdiag", value = Dist.DEDICATED_SERVER)
public final class TpsCommand {
    private static final double GOOD_EDGE = 19.8;
    private static final double WARN_EDGE = 18.0;

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> d = event.getDispatcher();

        d.register(
                Commands.literal("tps")
                        .requires(PermissionUtil::canUseTps)
                        .executes(ctx -> {
                            double tNow  = TpsTracker.getTpsCurrent();
                            double t1m   = TpsTracker.getTps1m();
                            double t5m   = TpsTracker.getTps5m();
                            double t15m  = TpsTracker.getTps15m();

                            String format = ConfigHandler.TPS_FORMAT.get();

                            String msg = format
                                    .replace("%tps%",     coloredTps(tNow))
                                    .replace("%1mtps%",   coloredTps(t1m))
                                    .replace("%5mtps%",   coloredTps(t5m))
                                    .replace("%15mtps%",  coloredTps(t15m));

                            msg = ColorUtil.ampersandToSection(msg);

                            ctx.getSource().sendSystemMessage(Component.literal(msg));
                            return 1;
                        })
        );
    }

    private static String coloredTps(double tps) {
        String color = pickColor(tps);
        return color + formatTps(tps) + "&r";
    }

    private static String pickColor(double tps) {
        String good = ConfigHandler.GOOD_TPS_COLOR.get();
        String warn = ConfigHandler.WARN_TPS_COLOR.get();
        String bad  = ConfigHandler.BAD_TPS_COLOR.get();
        if (tps >= GOOD_EDGE) return good;
        if (tps >= WARN_EDGE) return warn;
        return bad;
    }

    private static String formatTps(double t) {
        return String.format(java.util.Locale.ROOT, "%.2f", Math.min(20.0, t));
    }
}