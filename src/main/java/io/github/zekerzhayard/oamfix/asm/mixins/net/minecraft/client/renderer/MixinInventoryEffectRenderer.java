package io.github.zekerzhayard.oamfix.asm.mixins.net.minecraft.client.renderer;

import com.spiderfrog.oldanimations.Settings;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.inventory.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InventoryEffectRenderer.class)
public abstract class MixinInventoryEffectRenderer extends GuiContainer {
    private String oam = "newoamForge";

    private MixinInventoryEffectRenderer(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    @Redirect(
        method = "Lnet/minecraft/client/renderer/InventoryEffectRenderer;updateActivePotionEffects()V",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/renderer/InventoryEffectRenderer;guiLeft:I",
            ordinal = 0
        )
    )
    private void redirect$updateActivePotionEffects$0(InventoryEffectRenderer inventoryEffectRenderer, int guiLeft) {
        if (!Settings.noinvmove) {
            this.guiLeft = guiLeft;
        }
    }
}
