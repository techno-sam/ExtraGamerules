package com.slimeist.chickenhat.common.mob_spawning;

import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.world.MobSpawnInfoBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MobBiomeConfig implements IBiomeConfig {

    private List<EntityType<?>> entityTypes;

    private boolean isBlacklist;

    protected MobBiomeConfig(boolean isBlacklist, EntityType<?>... types) {
        this.isBlacklist = isBlacklist;

        entityTypes = new LinkedList<>();
        entityTypes.addAll(Arrays.asList(types));
    }

    @Override
    public boolean canSpawn(BiomeLoadingEvent b) {
        MobSpawnInfoBuilder spawns = b.getSpawns();
        for (EntityType<?> type : entityTypes) {
            boolean matches = spawns.getCost(type)!=null;
            if (matches&&isBlacklist) { //if this biome has an entity spawn that is on our blacklist, we can't spawn
                return false;
            } else if (!(matches||isBlacklist)) { //if this biome doesn't have an entity spawn that is on our whitelist, we can't spawn
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean canSpawn(ResourceLocation b) {
        return false;
    }

}
