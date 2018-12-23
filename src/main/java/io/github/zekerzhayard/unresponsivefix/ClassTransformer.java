package io.github.zekerzhayard.unresponsivefix;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import net.minecraft.launchwrapper.IClassTransformer;

public class ClassTransformer implements IClassTransformer {
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if (transformedName.equals("com.spiderfrog.oldanimations.cosmetic.LoadCosmeticData")) {
			System.out.println("Found the class: " + transformedName);
			ClassReader classReader = new ClassReader(basicClass);
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            classReader.accept(new ClassVisitor(Opcodes.ASM5, classWriter) {
            	@Override
                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            		MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
            		if (name.equals("onLoadCosmetics") && desc.equals("()V")) {
            			System.out.println("Found the method: " + name);
            			methodVisitor.visitCode();
            			methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
            			methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "io/github/zekerzhayard/unresponsivefix/LoadCosmeticDataHandler", "onLoadCosmetics", "(Lcom/spiderfrog/oldanimations/cosmetic/LoadCosmeticData;)V", false);
            			methodVisitor.visitInsn(Opcodes.RETURN);
            			methodVisitor.visitEnd();
            			return new MethodVisitor(Opcodes.ASM5) {};
            		}
            		return methodVisitor;
            	}
            }, ClassReader.SKIP_FRAMES);
            return classWriter.toByteArray();
		}
		return basicClass;
	}
}
