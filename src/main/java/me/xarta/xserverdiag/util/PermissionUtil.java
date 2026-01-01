package me.xarta.xserverdiag.util;

import me.xarta.xserverdiag.config.ConfigHandler;
import net.minecraft.commands.CommandSourceStack;

import java.lang.reflect.Method;
import java.util.function.Predicate;

public final class PermissionUtil {
    private static final String NODE = "xserverdiag.tps";
    private static final int FALLBACK_OP_LEVEL = 2;

    private PermissionUtil() {}

    public static boolean canUseTps(CommandSourceStack source) {
        if (!ConfigHandler.TPS_PERMISSION.get()) {
            return true;
        }

        try {
            Class<?> clazz = Class.forName("me.lucko.fabric.api.permissions.v0.Permissions");
            Method require = clazz.getMethod("require", String.class, int.class);
            Object predicateObj = require.invoke(null, NODE, FALLBACK_OP_LEVEL);
            @SuppressWarnings("unchecked")
            Predicate<Object> predicate = (Predicate<Object>) predicateObj;
            if (predicate != null && predicate.test(source)) {
                return true;
            }
        } catch (ClassNotFoundException e) {
            // API isn't presented
        } catch (ReflectiveOperationException e) {
            // Reflection error
        }

        return source.hasPermission(FALLBACK_OP_LEVEL);
    }
}