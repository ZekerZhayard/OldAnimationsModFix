package io.github.zekerzhayard.oamfix.asm.mixins.com.spiderfrog.oldanimations.cosmetic;

import com.spiderfrog.oldanimations.cosmetic.LoadCosmeticData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
    value = LoadCosmeticData.class,
    remap = false
)
public abstract class MixinLoadCosmeticData implements Runnable {
    private static boolean isReady = true;

    @Shadow
    public abstract void onLoadCosmetics();

    @Inject(
        method = "Lcom/spiderfrog/oldanimations/cosmetic/LoadCosmeticData;onLoadCosmetics()V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void inject$onLoadCosmetics$0(CallbackInfo ci) {
        if (isReady) {
            isReady = false;
            new Thread(this).start();
            ci.cancel();
            return;
        }
        isReady = true;
    }

    @Override
    public void run() {
        this.onLoadCosmetics();
    }
}
