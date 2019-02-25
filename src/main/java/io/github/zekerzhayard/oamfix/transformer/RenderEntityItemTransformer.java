package io.github.zekerzhayard.oamfix.transformer;

import com.creativemd.itemphysic.ItemTransformer;
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
        boolean hasOAM = true;
        for (MethodNode mn : cn.methods) {
        	String mappedMethodName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(cn.name, mn.name, mn.desc);
        	if ((mappedMethodName.equals("doRender") || mappedMethodName.equals("func_76986_a")) && FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(mn.desc).equals("(Lnet/minecraft/entity/Entity;DDDFF)V")) {
                System.out.println("Found the method: " + mn.name + mn.desc);
                mn.instructions.clear();
                mn.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                mn.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
                mn.instructions.add(new VarInsnNode(Opcodes.DLOAD, 2));
                mn.instructions.add(new VarInsnNode(Opcodes.DLOAD, 4));
                mn.instructions.add(new VarInsnNode(Opcodes.DLOAD, 6));
                mn.instructions.add(new VarInsnNode(Opcodes.FLOAD, 8));
                mn.instructions.add(new VarInsnNode(Opcodes.FLOAD, 9));
                mn.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ClientPhysic.class.getName().replace(".", "/"), "doRender", ItemTransformer.patch("(Lnet/minecraft/client/renderer/entity/RenderEntityItem;Lnet/minecraft/entity/Entity;DDDFF)V"), false));
                mn.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                mn.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
                mn.instructions.add(new VarInsnNode(Opcodes.DLOAD, 2));
                mn.instructions.add(new VarInsnNode(Opcodes.DLOAD, 4));
                mn.instructions.add(new VarInsnNode(Opcodes.DLOAD, 6));
                mn.instructions.add(new VarInsnNode(Opcodes.FLOAD, 8));
                mn.instructions.add(new VarInsnNode(Opcodes.FLOAD, 9));
                mn.instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, cn.superName, mn.name, mn.desc, false));
                mn.instructions.add(new InsnNode(Opcodes.RETURN));
        	} else if (mn.name.equals("shouldSpreadItems") && mn.desc.equals("()Z")) {
        		hasOAM = false;
        	}
        }
        if (hasOAM) {
            MethodNode methodNode = new MethodNode(Opcodes.ACC_PUBLIC, "shouldSpreadItems", "()Z", null, null);
            methodNode.instructions.add(new InsnNode(Opcodes.ICONST_1));
            methodNode.instructions.add(new InsnNode(Opcodes.IRETURN));
            cn.methods.add(methodNode);
        }
    }
}