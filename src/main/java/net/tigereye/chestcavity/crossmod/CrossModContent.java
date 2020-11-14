package net.tigereye.chestcavity.crossmod;

import net.fabricmc.loader.api.FabricLoader;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.crossmod.anthropophagy.CCAnthropophagyItems;
import net.tigereye.chestcavity.crossmod.anthropophagy.CCAnthropophagyListeners;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CrossModContent {
    public static final Logger LOGGER = LogManager.getLogger();

    public static void register(){
        if (FabricLoader.getInstance().isModLoaded("anthropophagy")){
            LOGGER.info("[Chest Cavity] Anthropophagy Detected!");
            if(ChestCavity.config.WENDIGOISM_INTEGRATION) {
                LOGGER.info("[Chest Cavity] Integrating with Anthropophagy");
                CCAnthropophagyItems.register();
                CCAnthropophagyListeners.register();
            }
            else{
                LOGGER.info("[Chest Cavity] Anthropophagy integration has been disabled in the config.");
            }
        }
    }
}