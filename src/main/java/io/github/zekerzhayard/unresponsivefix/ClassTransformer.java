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
            ClassReader classReader = new ClassReader(basicClass);
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            classReader.accept(new ClassVisitor(Opcodes.ASM5, classWriter) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                    MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
                    if (name.equals("onLoadCosmetics") && desc.equals("()V")) {
                        System.out.println("Found the method: " + name);
                        return new MethodVisitor(Opcodes.ASM5, methodVisitor) {
                            private boolean isFirst = true;
                            
                            @Override
                            public void visitInsn(int opcode) {
                                if (this.mv == null) {
                                    return;
                                }
                                if (opcode == Opcodes.RETURN) {
                                    mv.visitInsn(Opcodes.ICONST_1);
                                    mv.visitFieldInsn(Opcodes.PUTSTATIC, "io/github/zekerzhayard/unresponsivefix/LoadingThread", "isReady", "Z");
                                }
                                mv.visitInsn(opcode);
                            }

                            @Override
                            public void visitVarInsn(int opcode, int var) {
                                if (this.mv == null) {
                                    return;
                                }
                                if (this.isFirst && opcode == Opcodes.ALOAD && var == 0) {
                                    this.isFirst = false;
                                    Label label = new Label();
                                    mv.visitFieldInsn(Opcodes.GETSTATIC, "io/github/zekerzhayard/unresponsivefix/LoadingThread", "isReady", "Z");
                                    mv.visitJumpInsn(Opcodes.IFEQ, label);
                                    mv.visitInsn(Opcodes.ICONST_0);
                                    mv.visitFieldInsn(Opcodes.PUTSTATIC, "io/github/zekerzhayard/unresponsivefix/LoadingThread", "isReady", "Z");
                                    mv.visitTypeInsn(Opcodes.NEW, "io/github/zekerzhayard/unresponsivefix/LoadingThread");
                                    mv.visitInsn(Opcodes.DUP);
                                    mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "io/github/zekerzhayard/unresponsivefix/LoadingThread", "<init>", "()V", false);
                                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "io/github/zekerzhayard/unresponsivefix/LoadingThread", "start", "()V", false);
                                    mv.visitInsn(Opcodes.RETURN);
                                    mv.visitLabel(label);
                                }
                                mv.visitVarInsn(opcode, var);
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
