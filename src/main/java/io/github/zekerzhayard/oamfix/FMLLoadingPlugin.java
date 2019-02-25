package io.github.zekerzhayard.oamfix;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion(value = "1.8.9")
@IFMLLoadingPlugin.SortingIndex(value = 500)
public class FMLLoadingPlugin implements IFMLLoadingPlugin {
    @Override()
    public String[] getASMTransformerClass() {
        return new String[] { (ClassTransformer.class.getName()) };
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

    }

    @Override()
    public String getAccessTransformerClass() {
        return null;
    }
}
