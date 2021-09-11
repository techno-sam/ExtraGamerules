package com.slimeist.chickenhat.common.mob_spawning;

public class CostSensitiveEntitySpawnConfig extends EntitySpawnConfig {

    public double maxCost;

    public double spawnCost;

    public CostSensitiveEntitySpawnConfig(int spawnWeight, int minGroupSize, int maxGroupSize, double maxCost, double spawnCost, IBiomeConfig biomes) {
        super(spawnWeight, minGroupSize, maxGroupSize, biomes);
        this.maxCost = maxCost;
        this.spawnCost = spawnCost;
    }

}
