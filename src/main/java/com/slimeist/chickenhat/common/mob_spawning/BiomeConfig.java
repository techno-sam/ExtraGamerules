package com.slimeist.chickenhat.common.mob_spawning;

import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.BiomeDictionary;

public class BiomeConfig implements IBiomeConfig {

    //Types of biomes this should spawn in. Must match both this and 'biomes' and 'mobs' to spawn.
    BiomeTypeConfig types;

   //Biome names this should spawn in. Must match both this and 'types' and 'mobs' to spawn.
    StrictBiomeConfig biomes;

    //EntityTypes this should spawn in. Must match both this and 'types' and 'biomes' to spawn.
    MobBiomeConfig mobs;

    private BiomeConfig(BiomeTypeConfig types, StrictBiomeConfig biomes, MobBiomeConfig mobs) {
        this.types = types;
        this.biomes = biomes;
        this.mobs = mobs;
    }

    public static BiomeConfig fromBiomeTypes(boolean isBlacklist, BiomeDictionary.Type... typesIn) {
        return new BiomeConfig(new BiomeTypeConfig(isBlacklist, typesIn), noSBC(), noMBC());
    }

    public static BiomeConfig fromBiomeTypeStrings(boolean isBlacklist, String... typesIn) {
        return new BiomeConfig(new BiomeTypeConfig(isBlacklist, typesIn), noSBC(), noMBC());
    }

    public static BiomeConfig fromBiomeReslocs(boolean isBlacklist, String... typesIn) {
        return new BiomeConfig(noBTC(), new StrictBiomeConfig(isBlacklist, typesIn), noMBC());
    }

    public static BiomeConfig fromEntityTypes(boolean isBlacklist, EntityType<?>... typesIn) {
        return new BiomeConfig(noBTC(), noSBC(), new MobBiomeConfig(isBlacklist, typesIn));
    }

    public static BiomeConfig all() {
        return new BiomeConfig(noBTC(), noSBC(), noMBC());
    }

    private static BiomeTypeConfig noBTC() {
        return new BiomeTypeConfig(true, new BiomeDictionary.Type[0]);
    }


    private static StrictBiomeConfig noSBC() {
        return new StrictBiomeConfig(true, new String[0]);
    }

    private static MobBiomeConfig noMBC() {
        return new MobBiomeConfig(true, new EntityType<?>[0]);
    }

    @Override
    public boolean canSpawn(ResourceLocation b) {
        return b != null && types.canSpawn(b) && biomes.canSpawn(b);
    }

}
