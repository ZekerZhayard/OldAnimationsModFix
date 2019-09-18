package io.github.zekerzhayard.oamfix.asm.mixins.client.renderer.entity;

import com.spiderfrog.oldanimations.Settings;
import com.spiderfrog.oldanimations.cosmetic.CosmeticManager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RendererLivingEntity.class)
public abstract class MixinRendererLivingEntity {
    private String oam = "newoamForge";

    @ModifyArg(
        method = "Lnet/minecraft/client/renderer/entity/RendererLivingEntity;setBrightness(Lnet/minecraft/entity/EntityLivingBase;FZ)Z",
        at = @At(
            value = "INVOKE",
            target = "Ljava/nio/FloatBuffer;put(F)Ljava/nio/FloatBuffer;",
            ordinal = 0,
            remap = false
        )
    )
    private float modifyArg$setBrightness$0(float f) {
        if (!Settings.olddmg) {
            return 1.0F;
        }
        return 0.7F;
    }

    @ModifyArg(
        method = "Lnet/minecraft/client/renderer/entity/RendererLivingEntity;renderName(Lnet/minecraft/entity/EntityLivingBase;DDD)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/entity/RendererLivingEntity;renderOffsetLivingLabel(Lnet/minecraft/entity/Entity;DDDLjava/lang/String;FD)V"
        ),
        index = 2
    )
    private double modifyArg$renderName$0(Entity entityIn, double x, double y, double z, String str, float p_177069_9_, double p_177069_10_) {
        return y + CosmeticManager.getNameTagHeight(entityIn);
    }

    @Redirect(
        method = "Lnet/minecraft/client/renderer/entity/RendererLivingEntity;renderLayers(Lnet/minecraft/entity/EntityLivingBase;FFFFFFF)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/entity/layers/LayerRenderer;shouldCombineTextures()Z"
        )
    )
    private boolean redirect$renderLayers$0(LayerRenderer layerRenderer) {
        return layerRenderer.shouldCombineTextures() || (Settings.olddmg && layerRenderer.getClass().toString().contains(LayerBipedArmor.class.getSimpleName()));
    }

    @Redirect(
        method = "Lnet/minecraft/client/renderer/entity/RendererLivingEntity;renderName(Lnet/minecraft/entity/EntityLivingBase;DDD)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/GlStateManager;translate(FFF)V",
            ordinal = 0
        )
    )
    private void redirect$renderName$0(float x, float y, float z, EntityLivingBase entity, double a, double b, double c) {
        GlStateManager.translate(x, y + CosmeticManager.getNameTagHeight(entity), z);
    }
}
