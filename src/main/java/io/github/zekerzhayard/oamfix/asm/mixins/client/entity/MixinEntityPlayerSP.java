package io.github.zekerzhayard.oamfix.asm.mixins.client.entity;

import com.mojang.authlib.GameProfile;
import com.spiderfrog.oldanimations.utils.TranslateGameSettings;
import com.spiderfrog.togglesneak.CustomMovementInput;
import com.spiderfrog.togglesneak.ToggleSneakMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Transform EntityPlayerSP.onLivingUpdate()V
 *
 * <pre>
 *     boolean flag1 = <b>this.modifyVariable$onLivingUpdate$0</b>(this.movementInput.sneak);
 *     float f = 0.8F;
 *     boolean flag2 = this.movementInput.moveForward >= f;
 *     <b>this.redirect$onLivingUpdate$0</b>(this.movementInput);
 *
 *     if (this.isUsingItem() && !this.isRiding())
 *     {
 *         this.movementInput.moveStrafe *= 0.2F;
 *         this.movementInput.moveForward *= 0.2F;
 *         this.sprintToggleTimer = 0;
 *     }
 *
 *     this.pushOutOfBlocks(this.posX - (double)this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ + (double)this.width * 0.35D);
 *     this.pushOutOfBlocks(this.posX - (double)this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ - (double)this.width * 0.35D);
 *     this.pushOutOfBlocks(this.posX + (double)this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ - (double)this.width * 0.35D);
 *     this.pushOutOfBlocks(this.posX + (double)this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ + (double)this.width * 0.35D);
 *     boolean flag3 = <b>this.modifyVariable$onLivingUpdate$1</b>((float)this.getFoodStats().getFoodLevel() > 6.0F || <b>this.redirect$onLivingUpdate$1</b>(this.capabilities));
 *
 *     if (<b>this.redirect$onLivingUpdate$2</b>(this) && !flag1 && !flag2 && this.movementInput.moveForward >= f && !this.isSprinting() && flag3 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness))
 *     {
 *         if (this.sprintToggleTimer <= 0 && !<b>this.redirect$onLivingUpdate$3</b>(this.mc.gameSettings.keyBindSprint))
 *         {
 *             this.sprintToggleTimer = 7;
 *         }
 *         else
 *         {
 *             this.setSprinting(true);
 *             <b>this.inject$onLivingUpdate$0</b>(new CallbackInfo("inject$onLivingUpdate$0", false));
 *         }
 *     }
 *
 *     if (!<b>this.redirect$onLivingUpdate$4</b>(this) && this.movementInput.moveForward >= f && flag3 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness) && <b>this.redirect$onLivingUpdate$5</b>(this.mc.gameSettings).isKeyDown())
 *     {
 *         this.setSprinting(true);
 *         <b>this.inject$onLivingUpdate$1</b>(new CallbackInfo("inject$onLivingUpdate$1", false));
 *     }
 *
 *     if (this.isSprinting() && (this.movementInput.moveForward < f || this.isCollidedHorizontally || !flag3))
 *     {
 *         this.setSprinting(false);
 *         <b>this.inject$onLivingUpdate$2</b>(new CallbackInfo("inject$onLivingUpdate$2", false));
 *     }
 * </pre>
 *
 * @author ZekerZhayard
 */
@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer {
    @Shadow
    public MovementInput movementInput;
    @Shadow
    protected Minecraft mc;
    @Shadow
    protected int sprintToggleTimer;

    private String oam = "newoamForge";
    private CustomMovementInput customMovementInput = new CustomMovementInput();

    private boolean oam_isSprintDisabled = false;
    private boolean oam_enoughHunger = false;

    private MixinEntityPlayerSP(World worldIn, GameProfile playerProfile) {
        super(worldIn, playerProfile);
    }

    @Inject(
        method = "Lnet/minecraft/client/entity/EntityPlayerSP;onLivingUpdate()V",
        at = @At(
            value = "INVOKE",
            shift = At.Shift.AFTER,
            target = "Lnet/minecraft/client/entity/EntityPlayerSP;setSprinting(Z)V",
            ordinal = 1
        )
    )
    private void inject$onLivingUpdate$0(CallbackInfo ci) {
        this.customMovementInput.UpdateSprint(true, !this.oam_isSprintDisabled);
        if (!this.oam_isSprintDisabled) {
            this.sprintToggleTimer = 0;
        }
    }

    @Inject(
        method = "Lnet/minecraft/client/entity/EntityPlayerSP;onLivingUpdate()V",
        at = @At(
            value = "INVOKE",
            shift = At.Shift.AFTER,
            target = "Lnet/minecraft/client/entity/EntityPlayerSP;setSprinting(Z)V",
            ordinal = 2
        )
    )
    private void inject$onLivingUpdate$1(CallbackInfo ci) {
        this.customMovementInput.UpdateSprint(true, false);
    }

    @Inject(
        method = "Lnet/minecraft/client/entity/EntityPlayerSP;onLivingUpdate()V",
        at = @At(
            value = "INVOKE",
            shift = At.Shift.AFTER,
            target = "Lnet/minecraft/client/entity/EntityPlayerSP;setSprinting(Z)V",
            ordinal = 3
        )
    )
    private void inject$onLivingUpdate$2(CallbackInfo ci) {
        if (this.customMovementInput.sprintHeldAndReleased || this.oam_isSprintDisabled || this.customMovementInput.sprintDoubleTapped || this.capabilities.isFlying || this.isRiding()) {
            this.customMovementInput.UpdateSprint(false, false);
        }
    }

    @ModifyVariable(
        method = "Lnet/minecraft/client/entity/EntityPlayerSP;onLivingUpdate()V",
        at = @At("STORE"),
        ordinal = 1
    )
    private boolean modifyVariable$onLivingUpdate$0(boolean isSneaking) {
        return false;
    }

    @ModifyVariable(
        method = "Lnet/minecraft/client/entity/EntityPlayerSP;onLivingUpdate()V",
        at = @At("STORE"),
        ordinal = 3
    )
    private boolean modifyVariable$onLivingUpdate$1(boolean enoughHunger) {
        this.oam_enoughHunger = enoughHunger;
        return enoughHunger;
    }

    @Redirect(
        method = "Lnet/minecraft/client/entity/EntityPlayerSP;onLivingUpdate()V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/MovementInput;updatePlayerMoveState()V"
        )
    )
    private void redirect$onLivingUpdate$0(MovementInput movementInput) {
        this.customMovementInput.update(this.mc, (MovementInputFromOptions) this.movementInput, (EntityPlayerSP) (Object) this);
    }

    @Redirect(
        method = "Lnet/minecraft/client/entity/EntityPlayerSP;onLivingUpdate()V",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/entity/player/PlayerCapabilities;allowFlying:Z",
            ordinal = 0
        )
    )
    private boolean redirect$onLivingUpdate$1(PlayerCapabilities capabilities) {
        return capabilities.isFlying;
    }

    @Redirect(
        method = "Lnet/minecraft/client/entity/EntityPlayerSP;onLivingUpdate()V",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/entity/EntityPlayerSP;onGround:Z",
            ordinal = 0
        )
    )
    private boolean redirect$onLivingUpdate$2(EntityPlayerSP entity) {
        this.oam_isSprintDisabled = !ToggleSneakMod.optionToggleSprint;
        boolean canDoubleTap = ToggleSneakMod.optionDoubleTap;
        if (ToggleSneakMod.wasSprintDisabled) {
            this.setSprinting(false);
            this.customMovementInput.UpdateSprint(false, false);
            ToggleSneakMod.wasSprintDisabled = false;
        }
        if (!this.oam_isSprintDisabled) {
            boolean state = this.customMovementInput.sprint;
            if (this.oam_enoughHunger && !this.isUsingItem() && !entity.isPotionActive(Potion.blindness) && !this.customMovementInput.sprintHeldAndReleased && (!canDoubleTap || !entity.isSprinting())) {
                entity.setSprinting(state);
            }
            return canDoubleTap && !state && entity.onGround;
        }
        return ToggleSneakMod.optionDoubleTap && entity.onGround;
    }

    @Redirect(
        method = "Lnet/minecraft/client/entity/EntityPlayerSP;onLivingUpdate()V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/settings/KeyBinding;isKeyDown()Z",
            ordinal = 0
        )
    )
    private boolean redirect$onLivingUpdate$3(KeyBinding keyBindSprint) {
        return (!this.oam_isSprintDisabled || TranslateGameSettings.keyBindSprint().isKeyDown()) && (this.oam_isSprintDisabled || this.sprintToggleTimer != 0);
    }

    @Redirect(
        method = "Lnet/minecraft/client/entity/EntityPlayerSP;onLivingUpdate()V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/entity/EntityPlayerSP;isSprinting()Z",
            ordinal = 1
        )
    )
    private boolean redirect$onLivingUpdate$4(EntityPlayerSP entity) {
        return !this.oam_isSprintDisabled || entity.isSprinting();
    }

    @Redirect(
        method = "Lnet/minecraft/client/entity/EntityPlayerSP;onLivingUpdate()V",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/settings/GameSettings;keyBindSprint:Lnet/minecraft/client/settings/KeyBinding;",
            ordinal = 1
        )
    )
    private KeyBinding redirect$onLivingUpdate$5(GameSettings gameSettings) {
        return TranslateGameSettings.keyBindSprint();
    }
}
