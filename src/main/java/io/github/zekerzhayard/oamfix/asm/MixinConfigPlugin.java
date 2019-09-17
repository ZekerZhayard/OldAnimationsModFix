package io.github.zekerzhayard.oamfix.asm;

import java.util.List;
import java.util.Set;

import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class MixinConfigPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        if (targetClassName.equals("net.minecraft.client.model.ModelPlayer") && mixinClassName.equals("io.github.zekerzhayard.oamfix.asm.mixins.client.model.MixinModelPlayer")) {
            targetClass.superName = "com/spiderfrog/oldanimations/cosmetic/CosmeticRenderer";
            for (MethodNode mn : targetClass.methods) {
                for (AbstractInsnNode ain : mn.instructions.toArray()) {
                    if (ain.getOpcode() == Opcodes.INVOKESPECIAL && ((MethodInsnNode) ain).owner.equals("net/minecraft/client/model/ModelBiped")) {
                        ((MethodInsnNode) ain).owner = "com/spiderfrog/oldanimations/cosmetic/CosmeticRenderer";
                    }
                }
            }
        }
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
