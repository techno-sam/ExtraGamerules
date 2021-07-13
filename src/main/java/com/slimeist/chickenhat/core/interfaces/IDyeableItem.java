package com.slimeist.chickenhat.core.interfaces;

import net.minecraft.item.DyeItem;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import java.util.List;

public interface IDyeableItem { //copied from net.minecraft.item.IDyeableArmorItem
    default boolean hasCustomColor(ItemStack p_200883_1_) {
        CompoundNBT compoundnbt = p_200883_1_.getTagElement("display");
        return compoundnbt != null && compoundnbt.contains("color", 99);
    }

    default int getColor(ItemStack p_200886_1_) {
        CompoundNBT compoundnbt = p_200886_1_.getTagElement("display");
        return compoundnbt != null && compoundnbt.contains("color", 99) ? compoundnbt.getInt("color") : 10511680;
    }

    default void clearColor(ItemStack p_200884_1_) {
        CompoundNBT compoundnbt = p_200884_1_.getTagElement("display");
        if (compoundnbt != null && compoundnbt.contains("color")) {
            compoundnbt.remove("color");
        }

    }

    default void setColor(ItemStack p_200885_1_, int p_200885_2_) {
        p_200885_1_.getOrCreateTagElement("display").putInt("color", p_200885_2_);
    }

    static ItemStack dyeItem(ItemStack p_219975_0_, List<DyeItem> p_219975_1_) {
        ItemStack itemstack = ItemStack.EMPTY;
        int[] aint = new int[3];
        int i = 0;
        int j = 0;
        IDyeableItem idyeableitem = null;
        Item item = p_219975_0_.getItem();
        if (item instanceof IDyeableItem) {
            idyeableitem = (IDyeableItem)item;
            if (!idyeableitem.canBeDyed()) {
                return ItemStack.EMPTY;
            }
            itemstack = p_219975_0_.copy();
            itemstack.setCount(1);
            if (idyeableitem.hasCustomColor(p_219975_0_)) {
                int k = idyeableitem.getColor(itemstack);
                float f = (float)(k >> 16 & 255) / 255.0F;
                float f1 = (float)(k >> 8 & 255) / 255.0F;
                float f2 = (float)(k & 255) / 255.0F;
                i = (int)((float)i + Math.max(f, Math.max(f1, f2)) * 255.0F);
                aint[0] = (int)((float)aint[0] + f * 255.0F);
                aint[1] = (int)((float)aint[1] + f1 * 255.0F);
                aint[2] = (int)((float)aint[2] + f2 * 255.0F);
                ++j;
            }

            for(DyeItem dyeitem : p_219975_1_) {
                float[] afloat = dyeitem.getDyeColor().getTextureDiffuseColors();
                int i2 = (int)(afloat[0] * 255.0F);
                int l = (int)(afloat[1] * 255.0F);
                int i1 = (int)(afloat[2] * 255.0F);
                i += Math.max(i2, Math.max(l, i1));
                aint[0] += i2;
                aint[1] += l;
                aint[2] += i1;
                ++j;
            }
        }

        if (idyeableitem == null) {
            return ItemStack.EMPTY;
        } else {
            int j1 = aint[0] / j;
            int k1 = aint[1] / j;
            int l1 = aint[2] / j;
            float f3 = (float)i / (float)j;
            float f4 = (float)Math.max(j1, Math.max(k1, l1));
            j1 = (int)((float)j1 * f3 / f4);
            k1 = (int)((float)k1 * f3 / f4);
            l1 = (int)((float)l1 * f3 / f4);
            int j2 = (j1 << 8) + k1;
            j2 = (j2 << 8) + l1;
            idyeableitem.setColor(itemstack, j2);
            return itemstack;
        }
    }

    default boolean canBeDyed() {
        return true;
    }
}