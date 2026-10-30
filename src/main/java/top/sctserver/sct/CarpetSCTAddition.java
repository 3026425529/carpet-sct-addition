package top.sctserver.sct;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.utils.Translations;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.sctserver.sct.commands.MigrateScoresCommand;

import java.util.Map;

public class CarpetSCTAddition implements CarpetExtension, ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "carpet-sct-addition";
    public static String MOD_VERSION = "26.3";
    public static String MOD_NAME = "CarpetSCTAddition";

    @Override
    public void onInitialize() {
        ModContainer container = FabricLoader.getInstance().getModContainer(MOD_ID)
                .orElseThrow(() -> new IllegalStateException("Missing mod metadata for " + MOD_ID));
        MOD_NAME = container.getMetadata().getName();
        MOD_VERSION = container.getMetadata().getVersion().getFriendlyString();
        CarpetServer.manageExtension(this);
    }

    @Override
    public void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        MigrateScoresCommand.register(dispatcher);
    }

    @Override
    public void onGameStarted() {
        CarpetServer.settingsManager.parseSettingsClass(SCTSettings.class);
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        return Translations.getTranslationFromResourcePath(
                "assets/carpet-sct-addition/lang/%s.json".formatted(lang));
    }
}
