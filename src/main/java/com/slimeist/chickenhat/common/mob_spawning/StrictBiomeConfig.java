package com.slimeist.chickenhat.common.mob_spawning;

import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class StrictBiomeConfig implements IBiomeConfig {

    private List<String> biomeStrings;

    private boolean isBlacklist;

    protected StrictBiomeConfig(boolean isBlacklist, String... biomes) {
        this.isBlacklist = isBlacklist;

        biomeStrings = new LinkedList<>();
        biomeStrings.addAll(Arrays.asList(biomes));
    }

    @Override
    public boolean canSpawn(ResourceLocation res) {
        return biomeStrings.contains(res.toString()) != isBlacklist;
    }

}