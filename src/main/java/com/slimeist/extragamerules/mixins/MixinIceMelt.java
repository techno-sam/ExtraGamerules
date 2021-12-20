package com.slimeist.extragamerules.mixins;

import com.slimeist.extragamerules.ExtraGamerules;
import net.minecraft.block.BlockState;
import net.minecraft.block.IceBlock;
import net.minecraft.block.SnowBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(IceBlock.class)
public class MixinIceMelt {
    @Inject(at = @At("HEAD"), method = "randomTick", cancellable = true)
    private void noMelting(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo callback) {
        if (!world.getGameRules().getBoolean(ExtraGamerules.ICE_MELT.getKey())) {
            callback.cancel();
        }
    }
}
