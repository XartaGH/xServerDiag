package me.xarta.xserverdiag.command;

import com.mojang.brigadier.CommandDispatcher;
import me.xarta.xserverdiag.config.ConfigHandler;
import me.xarta.xserverdiag.event.UptimeTracker;
import me.xarta.xserverdiag.util.ColorUtil;
import me.xarta.xserverdiag.util.PermissionUtil;
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

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> d = event.getDispatcher();

        d.register(
                Commands.literal("uptime")
                        .requires(src -> PermissionUtil.canUse(src, NODE, ConfigHandler.UPTIME_PERMISSION.get()))
                        .executes(ctx -> {
                            String uptime = UptimeTracker.getFormattedUptime();
                            String msg = ConfigHandler.UPTIME_FORMAT.get().replace("%uptime%", uptime);
                            msg = ColorUtil.ampersandToSection(msg);
                            ctx.getSource().sendSystemMessage(Component.literal(msg));
                            return 1;
                        })
        );
    }
}