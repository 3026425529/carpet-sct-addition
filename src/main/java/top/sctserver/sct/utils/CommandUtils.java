package top.sctserver.sct.utils;

import carpet.utils.CommandHelper;
import net.minecraft.commands.CommandSourceStack;

public final class CommandUtils {
    private CommandUtils() {
    }

    public static boolean canUseCommand(CommandSourceStack source, Object commandLevel) {
        return CommandHelper.canUseCommand(source, commandLevel);
    }
}
