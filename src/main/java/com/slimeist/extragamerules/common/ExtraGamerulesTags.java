package com.slimeist.extragamerules.common;

import com.slimeist.extragamerules.ExtraGamerules;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class ExtraGamerulesTags {
    public static void init() {
        Items.init();
    }

    public static class Items {
        private static void init() {}

        private static Tags.IOptionalNamedTag<Item> tag(String name) {
            return ItemTags.createOptional(ExtraGamerules.getId(name));
        }

        private static Tags.IOptionalNamedTag<Item> forgeTag(String name) {
            return ItemTags.createOptional(new ResourceLocation("forge", name));
        }
    }
}
