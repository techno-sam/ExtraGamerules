package com.slimeist.chickenhat.core.init;

import com.slimeist.chickenhat.ChickenHat;
import com.slimeist.chickenhat.client.render.entity.FakeChickenRenderer;
import com.slimeist.chickenhat.common.entities.DyedEggEntity;
import com.slimeist.chickenhat.common.entities.FakeChickenEntity;
import com.slimeist.chickenhat.common.items.ModSpawnEggItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityTypeInit {
    public static final Logger LOGGER = LogManager.getLogger();
    public static EntityType<FakeChickenEntity> FAKE_CHICKEN;
    public static EntityType<DyedEggEntity> DYED_EGG;
    public static void registerAll(RegistryEvent.Register<EntityType<?>> event) {
        FAKE_CHICKEN = EntityType.Builder.<FakeChickenEntity>of(FakeChickenEntity::new, EntityClassification.CREATURE)
                .sized(0.9F, 0.5F)
                .clientTrackingRange(8)
                //.setCustomClientFactory((spawnEntity, world) -> new FakeChickenEntity(fakeChickenType, world))
                .setCustomClientFactory((spawnEntity, world) -> new FakeChickenEntity(FAKE_CHICKEN, world))
                .build(ChickenHat.getId("fake_chicken").toString());
        DYED_EGG = EntityType.Builder.<DyedEggEntity>of(DyedEggEntity::new, EntityClassification.MISC)
                .sized(0.25F, 0.25F)
                .clientTrackingRange(4)
                .updateInterval(10)
                .setCustomClientFactory((spawnEntity, world) -> new DyedEggEntity(DYED_EGG, world))
                .build(ChickenHat.getId("dyed_egg").toString());
        FAKE_CHICKEN.setRegistryName(ChickenHat.getId("fake_chicken"));
        DYED_EGG.setRegistryName(ChickenHat.getId("dyed_egg"));
        ForgeRegistries.ENTITIES.register(FAKE_CHICKEN);
        ForgeRegistries.ENTITIES.register(DYED_EGG);
        //event.getRegistry().register(FAKE_CHICKEN);
        EntitySpawnPlacementRegistry.register(FAKE_CHICKEN, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, FakeChickenEntity::spawnPredicate);
    }

    public static EntityType<?> getType(String name) {
        if (name=="fake_chicken") {
            return FAKE_CHICKEN;
        }
        return EntityType.PIG;
    }

    public static void setup(EntityAttributeCreationEvent event) {
        event.put(FAKE_CHICKEN, ChickenEntity.createAttributes().build());
    }

    public static void setupClient(FMLClientSetupEvent event) {
        //EntityRendererManager entityRendererManager = event.getMinecraftSupplier().get().getEntityRenderDispatcher();
        ItemRenderer itemRenderer = event.getMinecraftSupplier().get().getItemRenderer();

        RenderingRegistry.registerEntityRenderingHandler(FAKE_CHICKEN, FakeChickenRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(DYED_EGG, (rendererManager) -> new SpriteRenderer<>(rendererManager, itemRenderer));
    }
}
