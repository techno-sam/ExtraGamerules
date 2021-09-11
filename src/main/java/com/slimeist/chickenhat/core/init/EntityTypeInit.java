package com.slimeist.chickenhat.core.init;

import com.slimeist.chickenhat.ChickenHat;
import com.slimeist.chickenhat.common.entities.DyedChickenEntity;
import com.slimeist.chickenhat.common.entities.DyedEggEntity;
import com.slimeist.chickenhat.common.mob_spawning.BiomeConfig;
import com.slimeist.chickenhat.common.mob_spawning.EntitySpawnConfig;
import com.slimeist.chickenhat.common.mob_spawning.EntitySpawnHandler;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityTypeInit {
    public static final Logger LOGGER = LogManager.getLogger();
    public static EntityType<DyedChickenEntity> DYED_CHICKEN;
    public static EntityType<DyedEggEntity> DYED_EGG;
    public static void registerAll(RegistryEvent.Register<EntityType<?>> event) {
        DYED_CHICKEN = EntityType.Builder.<DyedChickenEntity>of(DyedChickenEntity::new, EntityClassification.CREATURE)
                .sized(0.4F, 0.7F)
                .clientTrackingRange(10)
                //.setCustomClientFactory((spawnEntity, world) -> new FakeChickenEntity(fakeChickenType, world))
                .setCustomClientFactory((spawnEntity, world) -> new DyedChickenEntity(DYED_CHICKEN, world))
                .build(ChickenHat.getId("fake_chicken").toString());
        DYED_EGG = EntityType.Builder.<DyedEggEntity>of(DyedEggEntity::new, EntityClassification.MISC)
                .sized(0.25F, 0.25F)
                .clientTrackingRange(4)
                .updateInterval(10)
                .setCustomClientFactory((spawnEntity, world) -> new DyedEggEntity(DYED_EGG, world))
                .build(ChickenHat.getId("dyed_egg").toString());
        DYED_CHICKEN.setRegistryName(ChickenHat.getId("fake_chicken"));
        DYED_EGG.setRegistryName(ChickenHat.getId("dyed_egg"));
        ForgeRegistries.ENTITIES.register(DYED_CHICKEN);
        ForgeRegistries.ENTITIES.register(DYED_EGG);
        //event.getRegistry().register(FAKE_CHICKEN);
        //EntitySpawnPlacementRegistry.register(DYED_CHICKEN, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, DyedChickenEntity::spawnPredicate);
        //spawnWeight is 5, because the weight for vanilla chickens is 10, 50% of vanilla chickens are now dyed chickens.
        EntitySpawnConfig dyedChickenSpawnConfig = new EntitySpawnConfig(10, 4, 4, BiomeConfig.fromEntityTypes(false, EntityType.CHICKEN));
        EntitySpawnHandler.registerSpawn(DYED_CHICKEN, EntityClassification.CREATURE, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, DyedChickenEntity::spawnPredicate, dyedChickenSpawnConfig);
        //EntitySpawnHandler.track(EntityType.CHICKEN, EntityClassification.CREATURE, dyedChickenSpawnConfig, false);
    }

    public static EntityType<?> getType(String name) {
        if (name.equals("fake_chicken")) {
            return DYED_CHICKEN;
        }
        return EntityType.PIG;
    }

    public static void setup(EntityAttributeCreationEvent event) {
        event.put(DYED_CHICKEN, ChickenEntity.createAttributes().build());
    }
}
