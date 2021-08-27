package com.slimeist.chickenhat.core.capabilities;

import com.slimeist.chickenhat.core.interfaces.IPiggyback;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class CapabilityPiggyback implements Capability.IStorage<IPiggyback> {

    @CapabilityInject(IPiggyback.class)
    public static Capability<IPiggyback> PIGGYBACK = null;

    private static final CapabilityPiggyback INSTANCE = new CapabilityPiggyback();

    private CapabilityPiggyback() {
    }

    public static void register() {
        CapabilityManager.INSTANCE.register(IPiggyback.class, INSTANCE, PiggybackHander::new);
    }

    @Nullable
    @Override
    public INBT writeNBT(Capability<IPiggyback> capability, IPiggyback instance, Direction side) {
        return null;
    }

    @Override
    public void readNBT(Capability<IPiggyback> capability, IPiggyback instance, Direction side, INBT nbt) {

    }
}
