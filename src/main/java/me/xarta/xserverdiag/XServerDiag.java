package me.xarta.xserverdiag;

import com.mojang.logging.LogUtils;
import me.xarta.xserverdiag.config.ConfigHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@SuppressWarnings("unused")
@Mod(XServerDiag.MODID)
public class XServerDiag {

    public static final String MODID = "xserverdiag"; // Define modification's ID
    public static final Logger LOGGER = LogUtils.getLogger(); // Create logger

    public XServerDiag(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("xServerDiag is initializing..."); // Print initialization message

        // Register config for the mod
        modContainer.registerConfig(
                ModConfig.Type.SERVER,
                ConfigHandler.SPEC,
                "xserverdiag.toml"
        );

        LOGGER.info("xServerDiag is on."); // Print success message
    }

}