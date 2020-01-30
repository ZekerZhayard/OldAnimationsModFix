package io.github.zekerzhayard.oamfix.asm;

import java.io.File;
import java.util.List;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

@SuppressWarnings("unchecked")
public class Tweaker implements ITweaker {
    private Logger logger = LogManager.getLogger("OAMFix");

    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {

    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        classLoader.addTransformerExclusion("io.github.zekerzhayard.oamfix.asm.");
        ((List<String>) Launch.blackboard.get("TweakClasses")).add(DeobfTweaker.class.getName());

        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.oamfix.json");
        MixinEnvironment.getCurrentEnvironment().setObfuscationContext("searge");
    }

    @Override
    public String getLaunchTarget() {
        return "net.minecraft.client.main.Main";
    }

    @Override
    public String[] getLaunchArguments() {
        try {
            List<IClassTransformer> transformers = (List<IClassTransformer>) FieldUtils.readDeclaredField(Launch.classLoader, "transformers", true);
            boolean isDetected = false;
            for (int i = transformers.size() - 1; i >= 0; i--) {
                if (transformers.get(i).getClass().getName().equals("$wrapper.com.spiderfrog.main.ClassTransformer")) {
                    this.logger.info("Detected the class transformer from Old Animations Mod!");
                    transformers.remove(i);
                    isDetected = true;
                }
            }
            if (!isDetected) {
                this.logger.error("Counld not detect the class transformer from Old Animations Mod!");
                this.logger.error(" Class Transformers:");
                for (Object o : transformers) {
                    this.logger.error("  - " + o.getClass().getName());
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return new String[0];
    }
}
