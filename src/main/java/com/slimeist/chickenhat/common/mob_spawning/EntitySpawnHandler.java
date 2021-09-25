package com.slimeist.chickenhat.common.mob_spawning;

import com.slimeist.chickenhat.ChickenHat;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.world.MobSpawnInfoBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BooleanSupplier;

public class EntitySpawnHandler {

    private static List<TrackedSpawnConfig> trackedSpawnConfigs = new LinkedList<>();

    public static <T extends MobEntity> void registerSpawn(EntityType<T> entityType, EntityClassification classification, EntitySpawnPlacementRegistry.PlacementType placementType, Heightmap.Type heightMapType, EntitySpawnPlacementRegistry.IPlacementPredicate<T> placementPredicate, EntitySpawnConfig config) {
        EntitySpawnPlacementRegistry.register(entityType, placementType, heightMapType, placementPredicate);

        track(entityType, classification, config, false);
    }

    public static <T extends MobEntity> void track(EntityType<T> entityType, EntityClassification classification, EntitySpawnConfig config, boolean secondary) {
        trackedSpawnConfigs.add(new TrackedSpawnConfig(entityType, classification, config, secondary));
    }

    private static void log(String msg) {
        ChickenHat.info(msg);
    }

    public static void onBiomeLoaded(BiomeLoadingEvent ev) {
        MobSpawnInfoBuilder builder = ev.getSpawns();
        //log("Running onBiomeLoaded event");

        for(TrackedSpawnConfig c : trackedSpawnConfigs) {
            List<MobSpawnInfo.Spawners> l = builder.getSpawner(c.classification);
            //log("Trying to add spawn for: "+c.entry.toString());
            if(!c.secondary)
                l.removeIf(e -> e.type.equals(c.entityType));

            if(c.config.biomes.canSpawn(ev)) {
                l.add(c.entry);
                //log("Adding spawn: "+c.entry.toString());
            }

            if(c.config instanceof CostSensitiveEntitySpawnConfig) {
                CostSensitiveEntitySpawnConfig csc = (CostSensitiveEntitySpawnConfig) c.config;
                builder.addMobCharge(c.entityType, csc.spawnCost, csc.maxCost);
            }
        }
    }

    private static class TrackedSpawnConfig {

        final EntityType<?> entityType;
        final EntityClassification classification;
        final EntitySpawnConfig config;
        final boolean secondary;
        MobSpawnInfo.Spawners entry;

        TrackedSpawnConfig(EntityType<?> entityType, EntityClassification classification, EntitySpawnConfig config, boolean secondary) {
            this.entityType = entityType;
            this.classification = classification;
            this.config = config;
            this.secondary = secondary;
            refresh();
        }

        void refresh() {
            entry = new MobSpawnInfo.Spawners(entityType, config.spawnWeight, Math.min(config.minGroupSize, config.maxGroupSize), Math.max(config.minGroupSize, config.maxGroupSize));
        }

    }

}
