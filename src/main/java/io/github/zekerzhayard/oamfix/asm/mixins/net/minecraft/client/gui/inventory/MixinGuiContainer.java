package io.github.zekerzhayard.oamfix.asm.mixins.net.minecraft.client.gui.inventory;

import com.spiderfrog.oldanimations.Settings;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiContainer.class)
public abstract class MixinGuiContainer {
    private String oam = "newoamForge";

    @Redirect(
        method = "Lnet/minecraft/client/gui/inventory/GuiContainer;drawScreen(IIF)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/inventory/GuiContainer;drawDefaultBackground()V"
        )
    )
    private void redirect$drawScreen$0(GuiContainer guiContainer) {
        if (Settings.invbackground) {
            guiContainer.drawDefaultBackground();
        }
    }
}
