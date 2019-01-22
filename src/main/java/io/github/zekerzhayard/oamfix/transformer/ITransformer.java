package io.github.zekerzhayard.oamfix.transformer;

import org.objectweb.asm.tree.ClassNode;

public interface ITransformer {
    void transform(ClassNode cn);
}