package io.github.zekerzhayard.oamfix;

import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;

import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

public class OldAnimationsModFix extends DummyModContainer {
    public OldAnimationsModFix() {
        super(new ModMetadata());
        ModMetadata meta = this.getMetadata();
        meta.modId = "oldanimationsmodfix";
        meta.name = "OldAnimationsModFix";
        meta.version = "@version@";
        meta.url = "https://github.com/ZekerZhayard/OldAnimationsModFix/";
        meta.authorList = Lists.newArrayList("ZekerZhayard");
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        return true;
    }
}
