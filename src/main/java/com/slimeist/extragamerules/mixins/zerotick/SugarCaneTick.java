package com.slimeist.extragamerules.mixins.zerotick;

import com.slimeist.extragamerules.ExtraGamerules;
import net.minecraft.block.BlockState;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(SugarCaneBlock.class)
public abstract class SugarCaneTick {
    @Shadow public abstract boolean canSurvive(BlockState p_196260_1_, IWorldReader p_196260_2_, BlockPos p_196260_3_);

    @Inject(at = @At("HEAD"), method = "tick")
    private void zeroTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo callback) {
        if (this.canSurvive(state, world, pos) && world.getGameRules().getBoolean(ExtraGamerules.ZERO_TICK_FARMS.getKey())) {
            state.getBlock().randomTick(state, world, pos, random);
        }
    }
}
