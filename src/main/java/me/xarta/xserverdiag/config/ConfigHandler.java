package me.xarta.xserverdiag.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ConfigHandler {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.ConfigValue<String> TPS_FORMAT;
    public static final ModConfigSpec.ConfigValue<String> GOOD_TPS_COLOR;
    public static final ModConfigSpec.ConfigValue<String> WARN_TPS_COLOR;
    public static final ModConfigSpec.ConfigValue<String> BAD_TPS_COLOR;
    public static final ModConfigSpec.ConfigValue<Boolean> TPS_PERMISSION;

    static {
        BUILDER.push("xServerDiag Configuration");
        BUILDER.comment("You can change some of mod's settings there.");

        TPS_FORMAT = BUILDER
                .comment("TPS Message (%tps%, %1mtps%, %5mtps%, %15mtps%")
                .define("tps-format", "&e&lTPS &fCurrent: %tps% &8| &f1m: %1mtps% &8| &f5m: %5mtps% &8| &f15m: %15mtps%");

        GOOD_TPS_COLOR = BUILDER
                .comment("Color of good TPS")
                .define("good-tps-color", "&a");

        WARN_TPS_COLOR = BUILDER
                .comment("Color of medium TPS")
                .define("warn-tps-color", "&6");

        BAD_TPS_COLOR = BUILDER
                .comment("Color of bad TPS")
                .define("bad-tps-color", "&c");

        TPS_PERMISSION = BUILDER
                .comment("Whether the permission xserverdiag.tps required for /tps command execution")
                .define("tps-permission", true);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

}
