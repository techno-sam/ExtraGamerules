package com.slimeist.chickenhat.common.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

public class RainbowSpawnEggItem extends ModSpawnEggItem{
    public RainbowSpawnEggItem(String entityType, int p_i48465_2_, int p_i48465_3_, Properties p_i48465_4_) {
        super(entityType, p_i48465_2_, p_i48465_3_, p_i48465_4_);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public int getColor(int layer) {
        int rainbowColor = super.getColor(layer);
        ClientWorld level = Minecraft.getInstance().level;
        if (level!=null) {
            long time = level.getGameTime();
            int cycle_length = 400; //match cycle length for sheep (16 wool colors, 25 ticks each)
            float hue = (time+this.hashCode()) % cycle_length;
            hue /= cycle_length;
            rainbowColor = Color.HSBtoRGB(hue, 1.0f, 1.0f);
        }
        return layer == 0 ? super.getColor(layer) : rainbowColor;
    }
}
