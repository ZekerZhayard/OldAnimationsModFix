package io.github.zekerzhayard.oamfix;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion(value = "1.8.9")
@IFMLLoadingPlugin.SortingIndex(value = 500)
public class FMLLoadingPlugin implements IFMLLoadingPlugin {
    public static boolean isDev = false;

    @Override()
    public String[] getASMTransformerClass() {
        String target = ClassTransformer.class.getName();
        return new String[] { target };
    }

    @Override()
    public String getModContainerClass() {
        return OldAnimationsModFix.class.getName();
    }

    @Override()
    public String getSetupClass() {
        return null;
    }

    @Override()
    public void injectData(Map<String, Object> data) {
        FMLLoadingPlugin.isDev = !(boolean) data.get("runtimeDeobfuscationEnabled");
    }

    @Override()
    public String getAccessTransformerClass() {
        return null;
    }
}
