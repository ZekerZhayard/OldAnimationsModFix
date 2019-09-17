package io.github.zekerzhayard.oamfix.asm.mixins.client.renderer.entity.layers;

import com.spiderfrog.oldanimations.Settings;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LayerHeldItem.class)
public abstract class MixinLayerHeldItem {
    private String oam = "newoamForge";

    @Redirect(
        method = "Lnet/minecraft/client/renderer/entity/layers/LayerHeldItem;doRenderLayer(Lnet/minecraft/entity/EntityLivingBase;FFFFFFF)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/model/ModelBiped;postRenderArm(F)V"
        )
    )
    private void redirect$doRenderLayer$0(ModelBiped modelBiped, float $scale, EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (entitylivingbaseIn instanceof EntityPlayer && Settings.oldsword && ((EntityPlayer) entitylivingbaseIn).isBlocking()) {
            modelBiped.postRenderArm(0.325F);
            GlStateManager.scale(1.05F, 1.05F, 1.05F);
            if (entitylivingbaseIn.isSneaking()) {
                GlStateManager.translate(-0.58F, 0.32F, -0.07F);
            } else {
                GlStateManager.translate(-0.45F, 0.25F, -0.07F);
            }
            GlStateManager.rotate(-24405.0F, 137290.0F, -2009900.0F, -2654900.0F);
        } else {
            modelBiped.postRenderArm($scale);
        }
    }

    @Redirect(
        method = "Lnet/minecraft/client/renderer/entity/layers/LayerHeldItem;doRenderLayer(Lnet/minecraft/entity/EntityLivingBase;FFFFFFF)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/GlStateManager;translate(FFF)V",
            ordinal = 1
        )
    )
    private void redirect$doRenderLayer$1(float x, float y, float z, EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (entitylivingbaseIn instanceof EntityPlayer && Settings.olditemheld && !((EntityPlayer) entitylivingbaseIn).isBlocking()) {
            GlStateManager.translate(-0.0855F, 0.4775F, 0.1585F);
            GlStateManager.rotate(-19.0F, 20.0F, 0.0F, -6.0F);
        } else {
            GlStateManager.translate(x, y, z);
        }
    }
}
