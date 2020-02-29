package io.github.zekerzhayard.oamfix.asm.mixins.com.spiderfrog.togglesneak;

import com.spiderfrog.togglesneak.ToggleSneakMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(
    value = ToggleSneakMod.class,
    remap = false
)
public abstract class MixinToggleSneakMod {
    @Redirect(
        method = "Lcom/spiderfrog/togglesneak/ToggleSneakMod;<clinit>()V",
        at = @At(
            value = "FIELD",
            target = "Lcom/spiderfrog/togglesneak/ToggleSneakMod;optionToggleSprint:Z"
        )
    )
    private static void redirect$_clinit_$0(boolean optionToggleSprint) {
        ToggleSneakMod.optionToggleSprint = false;
    }

    @Redirect(
        method = "Lcom/spiderfrog/togglesneak/ToggleSneakMod;<clinit>()V",
        at = @At(
            value = "FIELD",
            target = "Lcom/spiderfrog/togglesneak/ToggleSneakMod;optionToggleSneak:Z"
        )
    )
    private static void redirect$_clinit_$1(boolean optionToggleSneak) {
        ToggleSneakMod.optionToggleSneak = false;
    }

    @Redirect(
        method = "Lcom/spiderfrog/togglesneak/ToggleSneakMod;<clinit>()V",
        at = @At(
            value = "FIELD",
            target = "Lcom/spiderfrog/togglesneak/ToggleSneakMod;optionShowHUDText:Z"
        )
    )
    private static void redirect$_clinit_$2(boolean optionShowHUDText) {
        ToggleSneakMod.optionShowHUDText = false;
    }

    @Redirect(
        method = "Lcom/spiderfrog/togglesneak/ToggleSneakMod;<clinit>()V",
        at = @At(
            value = "FIELD",
            target = "Lcom/spiderfrog/togglesneak/ToggleSneakMod;optionDoubleTap:Z"
        )
    )
    private static void redirect$_clinit_$3(boolean optionDoubleTap) {
        ToggleSneakMod.optionDoubleTap = true;
    }
}
