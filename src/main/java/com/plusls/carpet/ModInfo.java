package com.plusls.carpet;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.util.Identifier;

import java.util.Optional;

//#if MC >= 11802
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
//#else
//$$ import org.apache.logging.log4j.LogManager;
//$$ import org.apache.logging.log4j.Logger;
//#endif

public class ModInfo {
    public static final String MOD_ID = "pca-protocol";
    public static final String MOD_PROTOCOL_ID = "pca";
    public static final Logger LOGGER =
            //#if MC >= 11802
            LogUtils.getLogger();
            //#else
            //$$ LogManager.getLogger();
            //#endif
    public static String MOD_VERSION;

    static {
        Optional<ModContainer> modContainerOptional = FabricLoader.getInstance().getModContainer(MOD_ID);
        modContainerOptional.ifPresent(modContainer -> MOD_VERSION = modContainer.getMetadata().getVersion().getFriendlyString());
    }

    public static Identifier id(String path) {
        //#if MC >= 12100
        //$$ return Identifier.of(MOD_PROTOCOL_ID, path);
        //#else
        return new Identifier(MOD_PROTOCOL_ID, path);
        //#endif
    }
}
