package io.github.zekerzhayard.oamfix.asm.mixins.com.spiderfrog.oldanimations.animations;

import com.spiderfrog.oldanimations.animations.OldDebugOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(
    value = OldDebugOverlay.class,
    remap = false
)
public abstract class MixinOldDebugOverlay {
    @Redirect(
        method = "Lcom/spiderfrog/oldanimations/animations/OldDebugOverlay;ofIsH7()Z",
        at = @At(
            value = "INVOKE",
            target = "Ljava/lang/String;equalsIgnoreCase(Ljava/lang/String;)Z",
            ordinal = 3
        )
    )
    private boolean redirect$ofIsH7$0(String ofver, String i7) {
        return ofver.equalsIgnoreCase(i7) || ofver.equalsIgnoreCase("L5");
    }
}
