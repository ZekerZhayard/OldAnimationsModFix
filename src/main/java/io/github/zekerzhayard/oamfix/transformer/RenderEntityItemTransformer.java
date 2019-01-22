package io.github.zekerzhayard.oamfix.transformer;

import com.creativemd.itemphysic.physics.ClientPhysic;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import io.github.zekerzhayard.oamfix.FMLLoadingPlugin;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public class RenderEntityItemTransformer implements ITransformer {
    @Override()
    public void transform(ClassNode cn) {
        String entityClassName = FMLDeobfuscatingRemapper.INSTANCE.unmap("net/minecraft/entity/Entity");
        cn.methods.stream().filter(mn -> (mn.name.equals("a") || mn.name.equals("doRender")) && mn.desc.equals("(L" + entityClassName + ";DDDFF)V")).forEachOrdered(mn -> {
            System.out.println("Found the method: " + mn.name + mn.desc);
            mn.instructions.clear();
            mn.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
            mn.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
            mn.instructions.add(new VarInsnNode(Opcodes.DLOAD, 2));
            mn.instructions.add(new VarInsnNode(Opcodes.DLOAD, 4));
            mn.instructions.add(new VarInsnNode(Opcodes.DLOAD, 6));
            mn.instructions.add(new VarInsnNode(Opcodes.FLOAD, 8));
            mn.instructions.add(new VarInsnNode(Opcodes.FLOAD, 9));
            mn.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ClientPhysic.class.getName().replace(".", "/"), "doRender", "(L" + FMLDeobfuscatingRemapper.INSTANCE.unmap("net/minecraft/client/renderer/entity/RenderEntityItem") + ";L" + entityClassName + ";DDDFF)V", false));
            mn.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
            mn.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
            mn.instructions.add(new VarInsnNode(Opcodes.DLOAD, 2));
            mn.instructions.add(new VarInsnNode(Opcodes.DLOAD, 4));
            mn.instructions.add(new VarInsnNode(Opcodes.DLOAD, 6));
            mn.instructions.add(new VarInsnNode(Opcodes.FLOAD, 8));
            mn.instructions.add(new VarInsnNode(Opcodes.FLOAD, 9));
            mn.instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, FMLDeobfuscatingRemapper.INSTANCE.unmap("net/minecraft/client/renderer/entity/Render"), FMLLoadingPlugin.isDev ? "doRender" : "a", "(L" + entityClassName + ";DDDFF)V", false));
            mn.instructions.add(new InsnNode(Opcodes.RETURN));
        });
        cn.methods.stream().filter(mn -> mn.name.equals("shouldSpreadItems") && mn.name.equals("()Z")).findAny().orElseGet(() -> {
            MethodNode methodNode = new MethodNode(Opcodes.ACC_PUBLIC, "shouldSpreadItems", "()Z", null, null);
            methodNode.instructions.add(new InsnNode(Opcodes.ICONST_1));
            methodNode.instructions.add(new InsnNode(Opcodes.IRETURN));
            cn.methods.add(methodNode);
            return null;
        });
    }
}