package top.sctserver.sct.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.network.ClientConnection;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.sctserver.sct.SCTSettings;
import top.sctserver.sct.utils.MigrateScoresUtil;

@Mixin(PlayerManager.class)
public abstract class PlayerListMixin {
    @Shadow @Final private MinecraftServer server;

    @Inject(
            method = "onPlayerConnect(Lnet/minecraft/network/ClientConnection;Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/server/network/ConnectedClientData;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;sendScoreboard(Lnet/minecraft/scoreboard/ServerScoreboard;Lnet/minecraft/server/network/ServerPlayerEntity;)V")
    )
    private void onPlayerConnect(
            ClientConnection connection,
            ServerPlayerEntity player,
            ConnectedClientData cookie,
            CallbackInfo ci,
            @Local(ordinal = 0) String oldName
    ) {
        String currentName = player.getGameProfile().name();
        if (SCTSettings.migrateScores && !currentName.equalsIgnoreCase(oldName)) {
            MigrateScoresUtil.migrateScores(server, oldName, currentName);
        }
    }
}
