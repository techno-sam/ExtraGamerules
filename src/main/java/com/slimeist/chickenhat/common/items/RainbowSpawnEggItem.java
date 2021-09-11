package com.slimeist.chickenhat.common.items;

import com.slimeist.chickenhat.ChickenHat;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class RainbowSpawnEggItem extends ModSpawnEggItem {

    public RainbowSpawnEggItem(String entityType, int p_i48465_2_, int p_i48465_3_, Properties p_i48465_4_) {
        super(entityType, p_i48465_2_, p_i48465_3_, p_i48465_4_);
    }

    public int getColor(ItemStack stack, int layer) {
        long time = ChickenHat.proxy.getGameTime();
        int cycle_length = 400; //match cycle length for sheep (16 wool colors, 25 ticks each)
        float hue = (time + (stack.hashCode()*25)) % cycle_length;
        hue /= cycle_length;
        int rainbowColor = Color.HSBtoRGB(hue, 1.0f, 1.0f);
        return layer == 0 ? super.getColor(layer) : rainbowColor;
    }
}
