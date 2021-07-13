package com.slimeist.chickenhat.core.init;

import com.slimeist.chickenhat.ChickenHat;
import net.minecraft.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
            ChickenHat.MOD_ID);
}
