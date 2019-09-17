package io.github.zekerzhayard.oamfix.asm.mixins.client.renderer.entity;

import com.spiderfrog.oldanimations.Settings;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(RenderEntityItem.class)
public abstract class MixinRenderEntityItem extends Render<EntityItem> {
    private String oam = "newoamForge";
    boolean olditems = false;

    private MixinRenderEntityItem(RenderManager renderManager) {
        super(renderManager);
    }

    @Inject(
        method = "Lnet/minecraft/client/renderer/entity/RenderEntityItem;func_177077_a(Lnet/minecraft/entity/item/EntityItem;DDDFLnet/minecraft/client/resources/model/IBakedModel;)I",
        at = @At(
            value = "INVOKE",
            shift = At.Shift.AFTER,
            target = "Lnet/minecraft/client/renderer/GlStateManager;translate(FFF)V",
            ordinal = 0
        )
    )
    private void inject$transformModelCount$0(CallbackInfoReturnable<Integer> cir) {
        this.olditems = Settings.oldfastitems;
    }

    @Inject(
        method = "Lnet/minecraft/client/renderer/entity/RenderEntityItem;func_177077_a(Lnet/minecraft/entity/item/EntityItem;DDDFLnet/minecraft/client/resources/model/IBakedModel;)I",
        at = @At(
            value = "INVOKE",
            shift = At.Shift.AFTER,
            target = "Lnet/minecraft/client/renderer/GlStateManager;rotate(FFFF)V"
        )
    )
    private void inject$transformModelCount$1(CallbackInfoReturnable<Integer> cir) {
        this.olditems = false;
    }

    @Inject(
        method = "Lnet/minecraft/client/renderer/entity/RenderEntityItem;doRender(Lnet/minecraft/entity/item/EntityItem;DDDFF)V",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/client/renderer/entity/RenderEntityItem;func_177077_a(Lnet/minecraft/entity/item/EntityItem;DDDFLnet/minecraft/client/resources/model/IBakedModel;)I"
        )
    )
    private void inject$doRender$0(CallbackInfo ci) {
        if (this.olditems) {
            GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.scale(1.0F, 1.0F, 0.001F);
        }
    }

    @Inject(
        method = "Lnet/minecraft/client/renderer/entity/RenderEntityItem;doRender(Lnet/minecraft/entity/item/EntityItem;DDDFF)V",
        at = @At(
            value = "FIELD",
            shift = At.Shift.BY,
            by = 2,
            target = "Lorg/lwjgl/util/vector/Vector3f;z:F",
            remap = false
        ),
        locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void inject$doRender$1(EntityItem entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci, ItemStack itemstack, boolean flag, IBakedModel ibakedmodel, int i, int j, float f3, float f4, float f5) {
        if (this.olditems) {
            GlStateManager.translate(-0.05F * f3, 0.05F * f4, -0.1 * (double)f5);
        }
    }

    @Redirect(
        method = "Lnet/minecraft/client/renderer/entity/RenderEntityItem;func_177077_a(Lnet/minecraft/entity/item/EntityItem;DDDFLnet/minecraft/client/resources/model/IBakedModel;)I",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/renderer/entity/RenderManager;options:Lnet/minecraft/client/settings/GameSettings;"
        )
    )
    private GameSettings redirect$transformModelCount$0(RenderManager renderManager) {
        if (renderManager.options != null && renderManager.options.fancyGraphics) {
            return renderManager.options;
        }
        return null;
    }

    @Redirect(
        method = "Lnet/minecraft/client/renderer/entity/RenderEntityItem;doRender(Lnet/minecraft/entity/item/EntityItem;DDDFF)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/GlStateManager;translate(FFF)V",
            ordinal = 1
        )
    )
    private void redirect$doRender$0(float x, float y, float z) {
        if (!this.olditems) {
            GlStateManager.translate(x, y, z);
        }
    }
}
