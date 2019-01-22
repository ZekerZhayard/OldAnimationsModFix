package io.github.zekerzhayard.oamfix;

import java.util.HashMap;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import io.github.zekerzhayard.oamfix.transformer.ITransformer;
import io.github.zekerzhayard.oamfix.transformer.LoadCosmeticDataTransformer;
import io.github.zekerzhayard.oamfix.transformer.RenderEntityItemTransformer;
import net.minecraft.launchwrapper.IClassTransformer;

public class ClassTransformer implements IClassTransformer {
    public static HashMap<String, ITransformer> transformers = new HashMap<>();

    static {
        ClassTransformer.transformers.put("com.spiderfrog.oldanimations.cosmetic.LoadCosmeticData", new LoadCosmeticDataTransformer());
        try {
            Class.forName("com.creativemd.itemphysic.ItemPatchingLoader");
            ClassTransformer.transformers.put("net.minecraft.client.renderer.entity.RenderEntityItem", new RenderEntityItemTransformer());
        } catch (ClassNotFoundException e) {

        }
    }

    @Override()
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (ClassTransformer.transformers.containsKey(transformedName)) {
            System.out.println("Found the class: " + transformedName);
            ClassNode classNode = new ClassNode();
            new ClassReader(basicClass).accept(classNode, ClassReader.EXPAND_FRAMES);
            ClassTransformer.transformers.get(transformedName).transform(classNode);
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            classNode.accept(classWriter);
            return classWriter.toByteArray();
        }
        return basicClass;
    }
}
