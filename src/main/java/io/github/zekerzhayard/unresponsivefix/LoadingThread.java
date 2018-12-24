package io.github.zekerzhayard.unresponsivefix;

import com.spiderfrog.oldanimations.cosmetic.LoadCosmeticData;

public class LoadingThread extends Thread {
    public static boolean isFirst = true;
    
    @Override
    public void run() {
        LoadCosmeticData.instance.onLoadCosmetics();
    }
}
