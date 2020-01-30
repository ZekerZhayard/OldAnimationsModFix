package io.github.zekerzhayard.oamfix.asm.mixins.oldanimations;

import com.spiderfrog.oldanimations.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(
    value = Settings.class,
    remap = false
)
public abstract class MixinSettings {
    @Redirect(
        method = "Lcom/spiderfrog/oldanimations/Settings;<clinit>()V",
        at = @At(
            value = "FIELD",
            target = "Lcom/spiderfrog/oldanimations/Settings;buildandeat:Z"
        )
    )
    private static void redirect$_clinit_$0(boolean buildandeat) {
        Settings.buildandeat = true;
    }

    @Redirect(
        method = "Lcom/spiderfrog/oldanimations/Settings;<clinit>()V",
        at = @At(
            value = "FIELD",
            target = "Lcom/spiderfrog/oldanimations/Settings;oldtablist:Z"
        )
    )
    private static void redirect$_clinit_$1(boolean oldtablist) {
        Settings.oldtablist = false;
    }

    @Redirect(
        method = "Lcom/spiderfrog/oldanimations/Settings;<clinit>()V",
        at = @At(
            value = "FIELD",
            target = "Lcom/spiderfrog/oldanimations/Settings;olditemheld:Z"
        )
    )
    private static void redirect$_clinit_$2(boolean olditemheld) {
        Settings.olditemheld = true;
    }
}
