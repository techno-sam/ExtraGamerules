package com.slimeist.chickenhat.client.guis.icons;

import com.slimeist.chickenhat.ChickenHat;
import net.minecraft.util.ResourceLocation;
import slimeknights.mantle.client.screen.ElementScreen;

public interface Icons {
    ResourceLocation ICONS = ChickenHat.getId("textures/guis/icons.png");

    ElementScreen PIGGYBACK_1 = new ElementScreen(18 * 13, 0, 18, 18);
    ElementScreen PIGGYBACK_2 = new ElementScreen(18 * 13, 18, 18, 18);
    ElementScreen PIGGYBACK_3 = new ElementScreen(18 * 13, 18 * 2, 18, 18);
}
