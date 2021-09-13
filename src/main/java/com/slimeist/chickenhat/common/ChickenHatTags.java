package com.slimeist.chickenhat.common;

import com.slimeist.chickenhat.ChickenHat;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class ChickenHatTags {
    public static void init() {
        Items.init();
    }

    public static class Items {
        private static void init() {}

        public static final Tags.IOptionalNamedTag<Item> WINGED_HELMET = tag("winged_helmet");

        private static Tags.IOptionalNamedTag<Item> tag(String name) {
            return ItemTags.createOptional(ChickenHat.getId(name));
        }

        private static Tags.IOptionalNamedTag<Item> forgeTag(String name) {
            return ItemTags.createOptional(new ResourceLocation("forge", name));
        }
    }
}
