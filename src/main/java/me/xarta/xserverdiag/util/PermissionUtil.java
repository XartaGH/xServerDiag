package me.xarta.xserverdiag.util;

import me.xarta.xserverdiag.config.ConfigHandler;
import net.minecraft.commands.CommandSourceStack;

import java.lang.reflect.Method;
import java.util.function.Predicate;

public final class PermissionUtil {
    private static final String NODE_TPS = "xserverdiag.tps";
    private static final int FALLBACK_OP_LEVEL = 2;

    private PermissionUtil() {}

    public static boolean canUseTps(CommandSourceStack source) {
        if (!ConfigHandler.TPS_PERMISSION.get()) return true;
        return checkNodeOrFallback(source, NODE_TPS);
    }

    public static boolean canUse(CommandSourceStack source, String node, boolean requireNodeFlag) {
        if (!requireNodeFlag) return true;
        return checkNodeOrFallback(source, node);
    }

    private static boolean checkNodeOrFallback(CommandSourceStack source, String node) {
        try {
            Class<?> clazz = Class.forName("me.lucko.fabric.api.permissions.v0.Permissions");
            Method require = clazz.getMethod("require", String.class, int.class);
            Object predicateObj = require.invoke(null, node, FALLBACK_OP_LEVEL);
            @SuppressWarnings("unchecked")
            Predicate<Object> predicate = (Predicate<Object>) predicateObj;
            if (predicate != null && predicate.test(source)) return true;
        } catch (ClassNotFoundException e) {
            // NO API
        } catch (ReflectiveOperationException e) {
            // REFLECTION ERROR
        }
        return source.hasPermission(FALLBACK_OP_LEVEL);
    }
}