package com.slimeist.chickenhat.core.init;

import com.slimeist.chickenhat.ChickenHat;
import com.slimeist.chickenhat.common.items.ArmorMaterials;
import com.slimeist.chickenhat.common.items.ChickenHelmet;
import com.slimeist.chickenhat.common.items.DyedEgg;
import com.slimeist.chickenhat.common.items.ModSpawnEggItem;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;

public final class PotionsInit {
    public static ChickenHelmet.CarryPotionEffect CARRY_EFFECT;

    private PotionsInit() {}

    public static void registerAll(RegistryEvent.Register<Effect> event) {
        CARRY_EFFECT = register("carry", new ChickenHelmet.CarryPotionEffect());
    }

    private static <T extends Effect> T register(String name, T effect) {
        ResourceLocation id = ChickenHat.getId(name);
        effect.setRegistryName(id);
        ForgeRegistries.POTIONS.register(effect);
        return effect;
    }
}
