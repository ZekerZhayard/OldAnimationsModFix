package io.github.zekerzhayard.oamfix.asm.mixins.client.model;

import com.spiderfrog.oldanimations.Settings;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ModelBiped.class)
public abstract class MixinModelBiped {
    private String oam = "newoamForge";

    @Redirect(
        method = "Lnet/minecraft/client/model/ModelBiped;setRotationAngles(FFFFFFLnet/minecraft/entity/Entity;)V",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/model/ModelRenderer;rotateAngleY:F",
            ordinal = 6
        )
    )
    private void redirect$setRotationAngles$0(ModelRenderer bipedRightArm, float rotateAngleY) {
        if (!Settings.oldsword) {
            bipedRightArm.rotateAngleY = rotateAngleY;
        }
    }
}
