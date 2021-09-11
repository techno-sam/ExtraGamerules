package com.slimeist.chickenhat.common.mob_spawning;

public class EntitySpawnConfig {

    public int spawnWeight = 40;

    public int minGroupSize = 1;

    public int maxGroupSize = 3;

    public IBiomeConfig biomes;

    public EntitySpawnConfig(int spawnWeight, int minGroupSize, int maxGroupSize, IBiomeConfig biomes) {
        this.spawnWeight = spawnWeight;
        this.minGroupSize = minGroupSize;
        this.maxGroupSize = maxGroupSize;
        this.biomes = biomes;
    }
}
