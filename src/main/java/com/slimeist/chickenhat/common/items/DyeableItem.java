package com.slimeist.chickenhat.common.items;

import com.slimeist.chickenhat.core.interfaces.IDyeableItem;
import net.minecraft.item.Item;

public class DyeableItem extends Item implements IDyeableItem {
    boolean dyeable;
    public DyeableItem(Item.Properties properties) {
        this(properties, true);
    }
    public DyeableItem(Item.Properties properties, boolean dyeable) {
        super(properties);
        this.dyeable = dyeable;
    }
}
