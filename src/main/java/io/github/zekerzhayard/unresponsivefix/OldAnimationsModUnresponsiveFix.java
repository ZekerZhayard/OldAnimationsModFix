package io.github.zekerzhayard.unresponsivefix;

import com.google.common.collect.Lists;

import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.ModMetadata;

public class OldAnimationsModUnresponsiveFix extends DummyModContainer {
    public OldAnimationsModUnresponsiveFix() {
        super(new ModMetadata());
        ModMetadata meta = this.getMetadata();
        meta.modId = "oldanimationsmodunresponsivefix";
        meta.name = "OldAnimationsModUnresponsiveFix";
        meta.version = "@version@";
        meta.url = "https://github.com/ZekerZhayard/OldAnimationsModUnresponsiveFix/";
        meta.authorList = Lists.newArrayList("ZekerZhayard");
    }
}
