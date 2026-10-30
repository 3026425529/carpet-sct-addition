package top.sctserver.sct.utils;

import carpet.utils.CommandHelper;
import net.minecraft.server.command.ServerCommandSource;

public final class CommandUtils {
    private CommandUtils() {
    }

    public static boolean canUseCommand(ServerCommandSource source, Object commandLevel) {
        return CommandHelper.canUseCommand(source, commandLevel);
    }
}
