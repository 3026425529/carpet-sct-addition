package top.sctserver.sct.mixins;

import net.minecraft.network.protocol.game.ClientboundRecipeBookAddPacket;
import net.minecraft.network.protocol.game.ClientboundRecipeBookSettingsPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.RecipeBookSettings;
import net.minecraft.stats.ServerRecipeBook;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.sctserver.sct.SCTSettings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Mixin(ServerRecipeBook.class)
public abstract class ServerRecipeBookMixin {
    @Shadow @Final private ServerRecipeBook.DisplayResolver displayResolver;
    @Shadow @Final protected Set<ResourceKey<Recipe<?>>> known;

    @Inject(method = "sendInitialRecipeBook(Lnet/minecraft/server/level/ServerPlayer;)V", at = @At("HEAD"), cancellable = true)
    private void carpetsctaddition$sendAllRecipes(ServerPlayer player, CallbackInfo ci) {
        if (!SCTSettings.sendAllRecipes) {
            return;
        }

        RecipeBookSettings settings = ((ServerRecipeBook) (Object) this).getBookSettings();
        player.connection.send(new ClientboundRecipeBookSettingsPacket(settings));

        RecipeManager recipeManager = player.level().getServer().getRecipeManager();
        Collection<RecipeHolder<?>> allRecipes = recipeManager.getRecipes();
        List<ClientboundRecipeBookAddPacket.Entry> entries = new ArrayList<>(allRecipes.size());
        for (RecipeHolder<?> recipe : allRecipes) {
            ResourceKey<Recipe<?>> recipeKey = recipe.id();
            displayResolver.displaysForRecipe(recipeKey, entry -> {
                boolean shouldHighlight = known.contains(recipeKey);
                entries.add(new ClientboundRecipeBookAddPacket.Entry(entry, false, shouldHighlight));
            });
        }

        player.connection.send(new ClientboundRecipeBookAddPacket(entries, true));
        ci.cancel();
    }
}
