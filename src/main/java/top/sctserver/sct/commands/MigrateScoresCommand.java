package top.sctserver.sct.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import top.sctserver.sct.SCTSettings;
import top.sctserver.sct.utils.CommandUtils;
import top.sctserver.sct.utils.MigrateScoresUtil;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public final class MigrateScoresCommand {
    private MigrateScoresCommand() {
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("migratescores")
                .requires(source -> CommandUtils.canUseCommand(source, SCTSettings.migrateScoresCommand))
                .then(argument("source", StringArgumentType.string())
                        .then(argument("target", StringArgumentType.string())
                                .executes(context -> {
                                    String sourceName = StringArgumentType.getString(context, "source");
                                    String targetName = StringArgumentType.getString(context, "target");
                                    ServerCommandSource source = context.getSource();
                                    MigrateScoresUtil.migrateScores(source.getServer(), sourceName, targetName);
                                    source.sendFeedback(
                                            () -> Text.translatable(
                                                    "sct.command.migratescorescommand.success",
                                                    sourceName, targetName),
                                            true);
                                    return 1;
                                }))));
    }
}
