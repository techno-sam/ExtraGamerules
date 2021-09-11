package com.slimeist.chickenhat.common.mob_spawning;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.world.BiomeLoadingEvent;

public interface IBiomeConfig {

    public default boolean canSpawn(Biome b) {
        return canSpawn(b.getRegistryName());
    }

    public default boolean canSpawn(BiomeLoadingEvent b) {
        return canSpawn(b.getName());
    }

    public boolean canSpawn(ResourceLocation b);

}
