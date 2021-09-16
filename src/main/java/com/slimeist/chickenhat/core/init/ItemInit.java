package com.slimeist.chickenhat.core.init;

import com.slimeist.chickenhat.ChickenHat;
import com.slimeist.chickenhat.common.items.*;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;
/*
public class ItemInit {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
            com.slimeist.chickenhat.ChickenHat.MOD_ID);

    public static final RegistryObject<ChickenHelmet> CHICKEN_HELMET = ITEMS.register("chicken_helmet",
            () -> new ChickenHelmet(ArmorMaterial.LEATHER, EquipmentSlotType.HEAD, (new Item.Properties()).tab(ItemGroup.TAB_COMBAT)));
}
*/

public final class ItemInit {
    public static ChickenHelmet CHICKEN_HELMET;
    public static RainbowSpawnEggItem DYED_CHICKEN_SPAWN_EGG;
    public static DyedEgg DYED_EGG;

    private ItemInit() {}

    public static void registerAll(RegistryEvent.Register<Item> event) {
        CHICKEN_HELMET = register("winged_helmet", new ChickenHelmet(ArmorMaterials.WINGED_LEATHER, EquipmentSlotType.HEAD, (new Item.Properties()).tab(ItemGroup.TAB_COMBAT)));
        DYED_CHICKEN_SPAWN_EGG = register("dyed_chicken_spawn_egg",
                new RainbowSpawnEggItem("dyed_chicken", 10592673, 16711680, (new Item.Properties()).tab(ItemGroup.TAB_MISC))
        );
        DYED_EGG = register("dyed_egg",
                new DyedEgg((new Item.Properties()).tab(ItemGroup.TAB_MATERIALS).stacksTo(16)) //default color: DFCE9B
        );
        initializeSpawnEggs();
    }

    private static <T extends Item> T register(String name, T item) {
        ResourceLocation id = ChickenHat.getId(name);
        item.setRegistryName(id);
        ForgeRegistries.ITEMS.register(item);
        return item;
    }

    public static void initializeSpawnEggs() {
        ModSpawnEggItem.initUnaddedEggs();
    }
}