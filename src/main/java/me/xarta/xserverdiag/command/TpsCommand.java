package me.xarta.xserverdiag.command;

import com.mojang.brigadier.CommandDispatcher;
import me.xarta.xserverdiag.config.ConfigHandler;
import me.xarta.xserverdiag.event.TpsTracker;
import me.xarta.xserverdiag.util.ColorUtil;
import me.xarta.xserverdiag.util.Perms;
import me.xarta.xserverdiag.util.TpsFormatUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = "xserverdiag", value = Dist.DEDICATED_SERVER)
public final class TpsCommand {
    private static final String NODE = "xserverdiag.tps";
    private static final String CMD = "tps";

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> d = event.getDispatcher();

        d.register(
                Commands.literal(CMD)
                        .executes(ctx -> {
                            CommandSourceStack src = ctx.getSource();
                            if (Perms.denied(src, NODE, ConfigHandler.TPS_PERMISSION.get(), CMD)) {
                                return 0;
                            }

                            double tNow  = TpsTracker.getTpsCurrent();
                            double t1m   = TpsTracker.getTps1m();
                            double t5m   = TpsTracker.getTps5m();
                            double t15m  = TpsTracker.getTps15m();

                            String format = ConfigHandler.TPS_FORMAT.get()
                                    .replace("%minute%", ConfigHandler.MINUTE.get());

                            String msg = format
                                    .replace("%tps%",    TpsFormatUtil.coloredTps(tNow))
                                    .replace("%1mtps%",  TpsFormatUtil.coloredTps(t1m))
                                    .replace("%5mtps%",  TpsFormatUtil.coloredTps(t5m))
                                    .replace("%15mtps%", TpsFormatUtil.coloredTps(t15m));

                            msg = ColorUtil.ampersandToSection(msg);
                            src.sendSystemMessage(Component.literal(msg));
                            return 1;
                        })
        );
    }
}