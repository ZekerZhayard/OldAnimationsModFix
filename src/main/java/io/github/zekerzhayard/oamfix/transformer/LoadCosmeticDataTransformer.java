package io.github.zekerzhayard.oamfix.transformer;

import com.spiderfrog.oldanimations.cosmetic.LoadCosmeticData;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;

public class LoadCosmeticDataTransformer extends Thread implements ITransformer {
    public static boolean isReady = true;
    
    @Override()
    public void run() {
        LoadCosmeticData.instance.onLoadCosmetics();
    }

    @Override()
    public void transform(ClassNode cn) {
        cn.methods.stream().filter(mn -> mn.name.equals("onLoadCosmetics") && mn.desc.equals("()V")).forEachOrdered(mn -> {
            System.out.println("Found the method: " + mn.name + mn.desc);
            InsnList insnList = new InsnList();
            LabelNode labelNode = new LabelNode();
            insnList.add(new FieldInsnNode(Opcodes.GETSTATIC, LoadCosmeticDataTransformer.class.getName().replace(".", "/"), "isReady", "Z"));
            insnList.add(new JumpInsnNode(Opcodes.IFEQ, labelNode));
            insnList.add(new InsnNode(Opcodes.ICONST_0));
            insnList.add(new FieldInsnNode(Opcodes.PUTSTATIC, LoadCosmeticDataTransformer.class.getName().replace(".", "/"), "isReady", "Z"));
            insnList.add(new TypeInsnNode(Opcodes.NEW, LoadCosmeticDataTransformer.class.getName().replace(".", "/")));
            insnList.add(new InsnNode(Opcodes.DUP));
            insnList.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, LoadCosmeticDataTransformer.class.getName().replace(".", "/"), "<init>", "()V", false));
            insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, LoadCosmeticDataTransformer.class.getName().replace(".", "/"), "start", "()V", false));
            insnList.add(new InsnNode(Opcodes.RETURN));
            insnList.add(labelNode);
            insnList.add(new InsnNode(Opcodes.ICONST_1));
            insnList.add(new FieldInsnNode(Opcodes.PUTSTATIC, LoadCosmeticDataTransformer.class.getName().replace(".", "/"), "isReady", "Z"));
            mn.instructions.insert(insnList);
        });
    }
}