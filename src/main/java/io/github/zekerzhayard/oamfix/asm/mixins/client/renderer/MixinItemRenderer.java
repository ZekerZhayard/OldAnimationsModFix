package io.github.zekerzhayard.oamfix.asm.mixins.client.renderer;

import com.spiderfrog.oldanimations.Settings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemCarrotOnAStick;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {
    @Final
    @Shadow
    private Minecraft mc;
    @Shadow
    private ItemStack itemToRender;
    @Shadow
    private float equippedProgress;
    @Shadow
    private float prevEquippedProgress;
    @Shadow
    private int equippedItemSlot;

    private String oam = "newoamForge";
    private float oam_swingProgress = 0.0F;

    @Shadow
    public abstract void updateEquippedItem();

    @Inject(
        method = "Lnet/minecraft/client/renderer/ItemRenderer;transformFirstPersonItem(FF)V",
        at = @At("HEAD")
    )
    private void inject$transformFirstPersonItem$0(CallbackInfo ci) {
        if (this.mc != null && this.mc.thePlayer != null) {
            if (Settings.oldbow && this.mc.thePlayer.getItemInUse() != null && this.mc.thePlayer.getItemInUse().getItem() instanceof ItemBow) {
                GlStateManager.translate(-0.01F, 0.05F, -0.06F);
            }
            if (this.mc.thePlayer.getCurrentEquippedItem() != null) {
                Item item = this.mc.thePlayer.getCurrentEquippedItem().getItem();
                if (Settings.oldrod && (item instanceof ItemFishingRod || item instanceof ItemCarrotOnAStick)) {
                    GlStateManager.translate(0.08F, -0.027F, -0.33F);
                    GlStateManager.scale(0.93F, 1.0F, 1.0F);
                }
                if (Settings.oldswing && this.mc.thePlayer.isSwingInProgress && !this.mc.thePlayer.isEating() && !this.mc.thePlayer.isBlocking()) {
                    GlStateManager.scale(0.85F, 0.85F, 0.85F);
                    GlStateManager.translate(-0.078F, 0.003F, 0.05F);
                }
            }
        }
    }

    @Inject(
        method = "Lnet/minecraft/client/renderer/ItemRenderer;renderItemInFirstPerson(F)V",
        at = @At(
            value = "INVOKE",
            shift = At.Shift.AFTER,
            target = "Lnet/minecraft/client/renderer/GlStateManager;pushMatrix()V"
        )
    )
    private void inject$renderItemInFirstPerson$0(CallbackInfo ci) {
        if (Settings.lefthand) {
            GlStateManager.scale(-1.0, 1.0, 1.0);
            GlStateManager.disableCull();
        }
    }

    @Inject(
        method = "Lnet/minecraft/client/renderer/ItemRenderer;renderItemInFirstPerson(F)V",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/item/ItemStack;getItemUseAction()Lnet/minecraft/item/EnumAction;"
        ),
        locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void inject$renderItemInFirstPerson$1(float partialTicks, CallbackInfo ci, float f, @Coerce AbstractClientPlayer player, float f1, float f2, float f3, EnumAction enumaction) {
        this.oam_swingProgress = 0.0F;
        if (Settings.oldblockhit) {
            this.oam_swingProgress = f1;
        }
    }

    @Inject(
        method = "Lnet/minecraft/client/renderer/ItemRenderer;renderItemInFirstPerson(F)V",
        at = @At(
            value = "INVOKE",
            shift = At.Shift.AFTER,
            target = "Lnet/minecraft/client/renderer/ItemRenderer;transformFirstPersonItem(FF)V",
            ordinal = 1
        )
    )
    private void inject$renderItemInFirstPerson$2(CallbackInfo ci) {
        if (Settings.oldeat) {
            GlStateManager.scale(0.8F, 1.0F, 1.0F);
            GL11.glTranslatef(-0.2F, -0.1F, 0.0F);
        }
    }

    @Inject(
        method = "Lnet/minecraft/client/renderer/ItemRenderer;renderItemInFirstPerson(F)V",
        at = @At(
            value = "INVOKE",
            shift = At.Shift.AFTER,
            target = "Lnet/minecraft/client/renderer/ItemRenderer;doBlockTransformations()V"
        )
    )
    private void inject$renderItemInFirstPerson$3(CallbackInfo ci) {
        if (Settings.oldblockhit) {
            GlStateManager.scale(0.83F, 0.88F, 0.85F);
            GlStateManager.translate(-0.3F, 0.1F, 0.0F);
        }
    }

    @Inject(
        method = "Lnet/minecraft/client/renderer/ItemRenderer;updateEquippedItem()V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void inject$updateEquippedItem$0(CallbackInfo ci) {
        if (Settings.olditemupdate) {
            this.updateEquippedItemOld();
            ci.cancel();
        }
    }

    @ModifyArg(
        method = "Lnet/minecraft/client/renderer/ItemRenderer;renderItemInFirstPerson(F)V",
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/renderer/ItemRenderer;performDrinking(Lnet/minecraft/client/entity/AbstractClientPlayer;F)V"
            ),
            to = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/renderer/ItemRenderer;doBowTransformations(FLnet/minecraft/client/entity/AbstractClientPlayer;)V"
            )
        ),
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/ItemRenderer;transformFirstPersonItem(FF)V"
        ),
        index = 1
    )
    private float modifyArg$renderItemInFirstPerson$0(float swingProgress) {
        return this.oam_swingProgress;
    }

    public void updateEquippedItemOld() {
        this.prevEquippedProgress = this.equippedProgress;
        EntityPlayer entityplayer = this.mc.thePlayer;
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();
        boolean flag = this.equippedItemSlot == entityplayer.inventory.currentItem && itemstack == this.itemToRender;

        if (this.itemToRender == null && itemstack == null) {
            flag = true;
        }

        if (itemstack != null && this.itemToRender != null && itemstack != this.itemToRender && itemstack.getItem() == this.itemToRender.getItem() && itemstack.getItemDamage() == this.itemToRender.getItemDamage()) {
            this.itemToRender = itemstack;
            flag = true;
        }

        float f = 0.4F;
        float f1 = flag ? 1.0F : 0.0F;
        float f2 = f1 - this.equippedProgress;

        if (f2 < -f) {
            f2 = -f;
        }

        if (f2 > f) {
            f2 = f;
        }

        this.equippedProgress += f2;

        if (this.equippedProgress < 0.1F) {
            this.itemToRender = itemstack;
            this.equippedItemSlot = entityplayer.inventory.currentItem;
        }
    }

    public void updateEquippedItemNew() {
        boolean temp = Settings.olditemupdate;
        Settings.olditemupdate = false;
        this.updateEquippedItem();
        Settings.olditemupdate = temp;
    }
}
