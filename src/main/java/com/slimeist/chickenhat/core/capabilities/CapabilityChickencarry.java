package com.slimeist.chickenhat.core.capabilities;

import com.slimeist.chickenhat.core.interfaces.IChickencarry;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class CapabilityChickencarry implements Capability.IStorage<IChickencarry> {

    @CapabilityInject(IChickencarry.class)
    public static Capability<IChickencarry> CHICKENCARRY = null;

    private static final CapabilityChickencarry INSTANCE = new CapabilityChickencarry();

    private CapabilityChickencarry() {
    }

    public static void register() {
        CapabilityManager.INSTANCE.register(IChickencarry.class, INSTANCE, ChickencarryHander::new);
    }

    @Nullable
    @Override
    public INBT writeNBT(Capability<IChickencarry> capability, IChickencarry instance, Direction side) {
        return null;
    }

    @Override
    public void readNBT(Capability<IChickencarry> capability, IChickencarry instance, Direction side, INBT nbt) {

    }
}
