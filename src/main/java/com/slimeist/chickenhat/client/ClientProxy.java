package com.slimeist.chickenhat.client;

import com.slimeist.chickenhat.common.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;

public class ClientProxy extends CommonProxy {
    @Override
    public long getGameTime() {
        ClientWorld level = Minecraft.getInstance().level;
        if (level!=null) {
            return level.getGameTime();
        }
        return 0;
    }
}
