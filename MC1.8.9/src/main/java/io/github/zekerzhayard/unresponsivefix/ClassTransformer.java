package io.github.zekerzhayard.unresponsivefix;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import net.minecraft.launchwrapper.IClassTransformer;

public class ClassTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (transformedName.equals("com.spiderfrog.oldanimations.cosmetic.LoadCosmeticData")) {
            System.out.println("Found the class: " + transformedName);
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            new ClassReader(basicClass).accept(new ClassVisitor(Opcodes.ASM5, classWriter) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                    MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
                    if (name.equals("onLoadCosmetics") && desc.equals("()V")) {
                        System.out.println("Found the method: " + name + desc);
                        return new MethodVisitor(Opcodes.ASM5, methodVisitor) {
                            private boolean isFirst = true;

                            @Override
                            public void visitVarInsn(int opcode, int var) {
                                if (this.mv != null) {
                                    if (this.isFirst && opcode == Opcodes.ALOAD && var == 0) {
                                        this.isFirst = false;
                                        Label label = new Label();
                                        this.mv.visitFieldInsn(Opcodes.GETSTATIC, "io/github/zekerzhayard/unresponsivefix/LoadingThread", "isReady", "Z");
                                        this.mv.visitJumpInsn(Opcodes.IFEQ, label);
                                        this.mv.visitInsn(Opcodes.ICONST_0);
                                        this.mv.visitFieldInsn(Opcodes.PUTSTATIC, "io/github/zekerzhayard/unresponsivefix/LoadingThread", "isReady", "Z");
                                        this.mv.visitTypeInsn(Opcodes.NEW, "io/github/zekerzhayard/unresponsivefix/LoadingThread");
                                        this.mv.visitInsn(Opcodes.DUP);
                                        this.mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "io/github/zekerzhayard/unresponsivefix/LoadingThread", "<init>", "()V", false);
                                        this.mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "io/github/zekerzhayard/unresponsivefix/LoadingThread", "start", "()V", false);
                                        this.mv.visitInsn(Opcodes.RETURN);
                                        this.mv.visitLabel(label);
                                        this.mv.visitInsn(Opcodes.ICONST_1);
                                        this.mv.visitFieldInsn(Opcodes.PUTSTATIC, "io/github/zekerzhayard/unresponsivefix/LoadingThread", "isReady", "Z");
                                    }
                                    this.mv.visitVarInsn(opcode, var);
                                }
                            }
                        };
                    }
                    return methodVisitor;
                }
            }, ClassReader.EXPAND_FRAMES);
            return classWriter.toByteArray();
        }
        return basicClass;
    }
}
