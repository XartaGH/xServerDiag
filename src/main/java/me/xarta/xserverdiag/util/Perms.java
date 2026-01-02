
package me.xarta.xserverdiag.util;

import me.xarta.xserverdiag.config.ConfigHandler;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public final class Perms {
    private Perms() {}

    private static LuckPerms getLp() {
        try {
            return LuckPermsProvider.get();
        } catch (IllegalStateException e) {
            return null;
        }
    }

    public static boolean has(CommandSourceStack source, String node, boolean requireNode, int fallbackOpLevel) {
        if (!requireNode) return true;
        ServerPlayer sp = source.getPlayer();
        if (sp == null) {
            return source.hasPermission(fallbackOpLevel);
        }
        LuckPerms lp = getLp();
        if (lp == null) {
            return source.hasPermission(fallbackOpLevel);
        }
        User user = lp.getPlayerAdapter(ServerPlayer.class).getUser(sp);
        var queryOptions = lp.getContextManager().getQueryOptions(sp);
        var permData = user.getCachedData().getPermissionData(queryOptions);
        return permData.checkPermission(node).asBoolean();
    }

    public static void sendNoPermission(CommandSourceStack source, String commandLiteral) {
        String raw = ConfigHandler.NO_PERMISSION_MESSAGE.get();
        boolean addSlash = ConfigHandler.ADD_SLASH.get();
        String cmd = addSlash ? ("/" + commandLiteral) : commandLiteral;
        String msg = me.xarta.xserverdiag.util.ColorUtil.ampersandToSection(raw.replace("%command%", cmd));
        source.sendSystemMessage(Component.literal(msg));
    }

    public static boolean denied(CommandSourceStack source, String node, boolean requireNode, int fallbackOpLevel, String commandLiteral) {
        boolean ok = has(source, node, requireNode, fallbackOpLevel);
        if (!ok) {
            sendNoPermission(source, commandLiteral);
            return true;
        }
        return false;
    }

    public static boolean denied(CommandSourceStack source, String node, boolean requireNode, String commandLiteral) {
        return denied(source, node, requireNode, 2, commandLiteral);
    }
}