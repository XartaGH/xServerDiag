package me.xarta.xserverdiag.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class ConfigHandler {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.ConfigValue<String> TPS_FORMAT;
    public static final ModConfigSpec.ConfigValue<String> GOOD_TPS_COLOR;
    public static final ModConfigSpec.ConfigValue<String> WARN_TPS_COLOR;
    public static final ModConfigSpec.ConfigValue<String> BAD_TPS_COLOR;
    public static final ModConfigSpec.ConfigValue<Boolean> TPS_PERMISSION;
    public static final ModConfigSpec.ConfigValue<String> UPTIME_FORMAT;
    public static final ModConfigSpec.ConfigValue<Boolean> UPTIME_PERMISSION;
    public static final ModConfigSpec.ConfigValue<String> SECOND;
    public static final ModConfigSpec.ConfigValue<String> MINUTE;
    public static final ModConfigSpec.ConfigValue<String> HOUR;
    public static final ModConfigSpec.ConfigValue<String> DAY;
    public static final ModConfigSpec.ConfigValue<String> WEEK;
    public static final ModConfigSpec.ConfigValue<String> MONTH;
    public static final ModConfigSpec.ConfigValue<String> YEAR;
    public static final ModConfigSpec.ConfigValue<List<String>> GC_FORMAT;
    public static final ModConfigSpec.ConfigValue<Boolean> GC_PERMISSION;
    public static final ModConfigSpec.ConfigValue<String> NO_PERMISSION_MESSAGE;
    public static final ModConfigSpec.ConfigValue<Boolean> ADD_SLASH;


    static {
        BUILDER.push("xServerDiag Configuration");
        BUILDER.comment("You can change some of mod's settings there.");

        TPS_FORMAT = BUILDER
                .comment("TPS Message (%tps%, %1mtps%, %5mtps%, %15mtps%, %minute%)")
                .define("tps-format", "&e&lTPS &fCurrent: %tps% &8| &f1%minute%: %1mtps% &8| &f5%minute%: %5mtps% &8| &f15%minute%: %15mtps%");

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

        UPTIME_FORMAT = BUILDER
                .comment("Uptime message (%uptime%)")
                .define("uptime-format", "&fUptime: &a%uptime%");

        UPTIME_PERMISSION = BUILDER
                .comment("Whether the permission xserverdiag.uptime required for /uptime command execution")
                .define("uptime-permission", true);

        SECOND = BUILDER
                .comment("Second")
                .define("second", "s");

        MINUTE = BUILDER
                .comment("Minute")
                .define("minute", "m");

        HOUR = BUILDER
                .comment("Hour")
                .define("hour", "h");

        DAY = BUILDER
                .comment("Day")
                .define("day", "d");

        WEEK = BUILDER
                .comment("Week")
                .define("week", "w");

        MONTH = BUILDER
                .comment("Month")
                .define("month", "M");

        YEAR = BUILDER
                .comment("Year")
                .define("year", "y");

        GC_FORMAT = BUILDER
                .comment("GC format, available placeholders: %uptime%, %tps%, %max_mem%, %all_mem%, %free_mem%, %world%, %chunks%, %entities%, %tiles%")
                .define(
                        "gc-format",
                        List.of(
                                "&fUptime: &a%uptime%",
                                "&fCurrent TPS = %tps%",
                                "&fMaximum memory: &a%max_mem%",
                                "&fAllocated memory: &a%all_mem%",
                                "&fFree memory: &a%free_mem%",
                                "&fWorld &a%world%&f: &a%chunks% &fchunks, &a%entities% &fentities, &a%tiles% &ftiles"
                        ),
                        o -> {
                            if (!(o instanceof List<?> list)) return false;
                            for (Object e : list) if (!(e instanceof String)) return false;
                            return true;
                        }
                );

        GC_PERMISSION = BUILDER
                .comment("Whether the permission xserverdiag.gc required for /gc command execution")
                .define("gc-permission", true);

        NO_PERMISSION_MESSAGE = BUILDER
                .comment("Message shown when player has no permission (%command% is placeholder)")
                .define("no-permission-message", "&cYou don't have permission to execute &4%command%");

        ADD_SLASH = BUILDER
                .comment("Add slash to %command%")
                .define("add-slash", true);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

}
