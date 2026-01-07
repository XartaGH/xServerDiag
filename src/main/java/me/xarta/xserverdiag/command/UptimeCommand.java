package me.xarta.xserverdiag.command;

import com.mojang.brigadier.CommandDispatcher;
import me.xarta.xserverdiag.config.ConfigHandler;
import me.xarta.xserverdiag.event.UptimeTracker;
import me.xarta.xserverdiag.util.ColorUtil;
import me.xarta.xserverdiag.util.Perms;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = "xserverdiag", value = Dist.DEDICATED_SERVER)
public final class UptimeCommand {
    private static final String NODE = "xserverdiag.uptime";
    private static final String CMD = "uptime";

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> d = event.getDispatcher();

        d.register(
                Commands.literal(CMD)
                        .requires(src -> Perms.has(src, NODE, ConfigHandler.UPTIME_PERMISSION.get(), Commands.LEVEL_ADMINS))
                        .executes(ctx -> {
                            CommandSourceStack src = ctx.getSource();
                            if (Perms.denied(src, NODE, ConfigHandler.UPTIME_PERMISSION.get(), CMD)) {
                                return 0;
                            }
                            String uptime = UptimeTracker.getFormattedUptime();
                            String msg = ConfigHandler.UPTIME_FORMAT.get().replace("%uptime%", uptime);
                            msg = ColorUtil.ampersandToSection(msg);
                            src.sendSystemMessage(Component.literal(msg));
                            return 1;
                        })
        );
    }
}