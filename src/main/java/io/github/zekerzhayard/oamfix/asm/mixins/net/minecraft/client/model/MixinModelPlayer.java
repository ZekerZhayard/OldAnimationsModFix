package io.github.zekerzhayard.oamfix.asm.mixins.net.minecraft.client.model;

import io.github.zekerzhayard.oamfix.asm.transformers.ModelPlayerTransformer;
import net.minecraft.client.model.ModelPlayer;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Transform the super class of {@link ModelPlayer}.
 *
 * @see ModelPlayerTransformer
 */
@Mixin(ModelPlayer.class)
public abstract class MixinModelPlayer /*extends com.spiderfrog.oldanimations.cosmetic.CosmeticRenderer*/ {
    private String oam = "newoamForge";
}
