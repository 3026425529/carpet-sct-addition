package top.sctserver.sct.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.network.Connection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.sctserver.sct.SCTSettings;
import top.sctserver.sct.utils.MigrateScoresUtil;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {
    @Shadow @Final private MinecraftServer server;

    @Inject(
            method = "placeNewPlayer(Lnet/minecraft/network/Connection;Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/server/network/CommonListenerCookie;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;updateEntireScoreboard(Lnet/minecraft/server/ServerScoreboard;Lnet/minecraft/server/level/ServerPlayer;)V")
    )
    private void onPlaceNewPlayer(
            Connection connection,
            ServerPlayer player,
            CommonListenerCookie cookie,
            CallbackInfo ci,
            @Local(ordinal = 0) String oldName
    ) {
        String currentName = player.getGameProfile().name();
        if (SCTSettings.migrateScores && !currentName.equalsIgnoreCase(oldName)) {
            MigrateScoresUtil.migrateScores(server, oldName, currentName);
        }
    }
}
