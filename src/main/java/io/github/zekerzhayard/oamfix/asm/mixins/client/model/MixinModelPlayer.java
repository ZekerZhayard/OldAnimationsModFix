package io.github.zekerzhayard.oamfix.asm.mixins.client.model;

import net.minecraft.client.model.ModelPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Transform the super class of {@link ModelPlayer}.
 *
 * @see io.github.zekerzhayard.oamfix.asm.MixinConfigPlugin#preApply
 */
@Mixin(ModelPlayer.class)
public abstract class MixinModelPlayer /*extends com.spiderfrog.oldanimations.cosmetic.CosmeticRenderer*/ {
    private String oam;

    @Inject(
        method = "Lnet/minecraft/client/model/ModelPlayer;<init>(FZ)V",
        at = @At("RETURN")
    )
    private void inject$_init_$0(CallbackInfo ci) {
        this.oam = "newoamForge";
    }
}
