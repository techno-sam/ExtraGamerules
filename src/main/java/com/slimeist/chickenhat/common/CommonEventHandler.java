package com.slimeist.chickenhat.common;

import com.slimeist.chickenhat.ChickenHat;
import com.slimeist.chickenhat.common.mob_spawning.EntitySpawnHandler;
import com.slimeist.chickenhat.core.capabilities.ChickencarrySerializer;
import com.slimeist.chickenhat.core.init.ItemInit;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.slimeist.chickenhat.ChickenHat.getId;

public class CommonEventHandler {

    @SubscribeEvent
    public void missingMappingsEvent(final RegistryEvent.MissingMappings<Item> event) {
        for (RegistryEvent.MissingMappings.Mapping<Item> entry : event.getAllMappings()) {
            if (entry.key.equals(getId("chicken_helmet"))) {
                entry.remap(ItemInit.CHICKEN_HELMET);
            } else if (entry.key.equals(getId("fake_chicken_spawn_egg"))) {
                entry.remap(ItemInit.DYED_CHICKEN_SPAWN_EGG);
            }
        }
    }

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(getId("chickencarry"), new ChickencarrySerializer((PlayerEntity) event.getObject()));
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onBiomeLoaded(BiomeLoadingEvent event) {
        ChickenHat.LOGGER.info("onBiomeLoadedEvent with: "+event.getName());
        EntitySpawnHandler.onBiomeLoaded(event);
    }
}
