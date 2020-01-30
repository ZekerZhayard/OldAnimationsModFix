package io.github.zekerzhayard.oamfix.asm.mixins.directionhud;

import com.spiderfrog.directionhud.DirectionHUD;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(
    value = DirectionHUD.class,
    remap = false
)
public abstract class MixinDirectionHUD {
    @Redirect(
        method = "Lcom/spiderfrog/directionhud/DirectionHUD;<clinit>()V",
        at = @At(
            value = "FIELD",
            target = "Lcom/spiderfrog/directionhud/DirectionHUD;optionEnable:Z"
        )
    )
    private static void redirect$_clinit_$0(boolean optionEnable) {
        DirectionHUD.optionEnable = false;
    }
}
