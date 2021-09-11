package com.slimeist.chickenhat.common;

import com.slimeist.chickenhat.ChickenHat;
import com.slimeist.chickenhat.core.capabilities.CapabilityChickencarry;
import com.slimeist.chickenhat.core.init.EntityTypeInit;
import com.slimeist.chickenhat.core.init.ItemInit;
import com.slimeist.chickenhat.core.init.PotionsInit;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.Level;

public class StartupCommon {

    @SubscribeEvent
    public static void setup(final FMLCommonSetupEvent event)
    {
        ChickenHat.LOGGER.log(Level.ERROR, "Setting up ChickenHat mod");
        CapabilityChickencarry.register();
        ChickenHatTags.init();
    }

    @SubscribeEvent
    public static void onItemsRegistration(final RegistryEvent.Register<Item> event) {
        ItemInit.registerAll(event);
    }

    @SubscribeEvent
    public static void onEntityTypeRegistration(final RegistryEvent.Register<EntityType<?>> event) {
        EntityTypeInit.registerAll(event);
    }

    @SubscribeEvent
    public static void onEffectRegistration(final RegistryEvent.Register<Effect> event) {
        PotionsInit.registerAll(event);
    }

    @SubscribeEvent
    public static void setupEntityAttributes(final EntityAttributeCreationEvent event) {
        EntityTypeInit.setup(event);
    }
}
