package com.slimeist.chickenhat.common;

import com.slimeist.chickenhat.ChickenHat;
import com.slimeist.chickenhat.common.items.ChickenHelmet;
import com.slimeist.chickenhat.common.mob_spawning.EntitySpawnHandler;
import com.slimeist.chickenhat.core.capabilities.ChickencarrySerializer;
import com.slimeist.chickenhat.core.init.EntityTypeInit;
import com.slimeist.chickenhat.core.init.ItemInit;
import com.slimeist.chickenhat.core.init.PotionsInit;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.slimeist.chickenhat.ChickenHat.getId;

public class CommonEventHandler {

    @SubscribeEvent
    public static void missingItemMappingsEvent(final RegistryEvent.MissingMappings<Item> event) {
        for (RegistryEvent.MissingMappings.Mapping<Item> entry : event.getAllMappings()) {
            if (entry.key.equals(getId("chicken_helmet")) || entry.key.equals(getId("winged_cap"))) {
                entry.remap(ItemInit.CHICKEN_HELMET);
            } else if (entry.key.equals(getId("fake_chicken_spawn_egg"))) {
                entry.remap(ItemInit.DYED_CHICKEN_SPAWN_EGG);
            }
        }
    }

    @SubscribeEvent
    public static void missingEntityTypeMappingsEvent(final RegistryEvent.MissingMappings<EntityType<?>> event) {
        for (RegistryEvent.MissingMappings.Mapping<EntityType<?>> entry : event.getAllMappings()) {
            if (entry.key.equals(getId("fake_chicken"))) {
                entry.remap(EntityTypeInit.DYED_CHICKEN);
            }
        }
    }

    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(getId("chickencarry"), new ChickencarrySerializer((PlayerEntity) event.getObject()));
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onBiomeLoaded(BiomeLoadingEvent event) {
        //ChickenHat.LOGGER.info("onBiomeLoadedEvent with: "+event.getName());
        EntitySpawnHandler.onBiomeLoaded(event);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase==TickEvent.Phase.START) {
            return;
        }

        PlayerEntity target = event.player;

        if (!target.hasEffect(PotionsInit.CARRY_EFFECT)) {
            return;
        }

        boolean isFlying = target.abilities.flying;

        Vector3d v3d = target.getDeltaMovement();

        if (!target.isOnGround() && v3d.y < 0.0D && !isFlying && !target.isFallFlying() && !target.isCrouching())
        {
            target.setDeltaMovement(v3d.multiply(1.0D, 0.6D, 1.0D));
            target.fallDistance = 0.0F;
        }
    }
}
