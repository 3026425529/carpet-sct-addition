package top.sctserver.sct.mixins;

import net.minecraft.network.packet.s2c.play.RecipeBookAddS2CPacket;
import net.minecraft.network.packet.s2c.play.RecipeBookSettingsS2CPacket;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.ServerRecipeManager;
import net.minecraft.recipe.book.RecipeBookOptions;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerRecipeBook;
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
    @Shadow @Final private ServerRecipeBook.DisplayCollector collector;
    @Shadow @Final protected Set<RegistryKey<Recipe<?>>> highlighted;

    @Inject(method = "sendInitRecipesPacket(Lnet/minecraft/server/network/ServerPlayerEntity;)V", at = @At("HEAD"), cancellable = true)
    private void carpetsctaddition$sendAllRecipes(ServerPlayerEntity player, CallbackInfo ci) {
        if (!SCTSettings.sendAllRecipes) {
            return;
        }

        RecipeBookOptions settings = ((ServerRecipeBook) (Object) this).getOptions();
        player.networkHandler.sendPacket(new RecipeBookSettingsS2CPacket(settings));

        ServerRecipeManager recipeManager = player.getWorld().server.getRecipeManager();
        Collection<RecipeEntry<?>> allRecipes = recipeManager.getAllRecipes();
        List<RecipeBookAddS2CPacket.Entry> entries = new ArrayList<>(allRecipes.size());
        for (RecipeEntry<?> recipe : allRecipes) {
            RegistryKey<Recipe<?>> recipeKey = recipe.id();
            collector.displaysForRecipe(recipeKey, entry -> {
                boolean shouldHighlight = highlighted.contains(recipeKey);
                entries.add(new RecipeBookAddS2CPacket.Entry(entry, false, shouldHighlight));
            });
        }

        player.networkHandler.sendPacket(new RecipeBookAddS2CPacket(entries, true));
        ci.cancel();
    }
}
