package com.slimeist.chickenhat.core.capabilities;

import com.google.common.collect.Maps;
import com.slimeist.chickenhat.core.interfaces.IPiggyback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

public class PiggybackSerializer implements ICapabilitySerializable<CompoundNBT> {

    private final PlayerEntity player;
    private final IPiggyback piggyback;
    private LazyOptional<IPiggyback> providerCap;

    public PiggybackSerializer(@Nonnull PlayerEntity player) {
        this.player = player;
        this.piggyback = new PiggybackHander();
        this.piggyback.setRiddenPlayer(player);
        this.providerCap = LazyOptional.of(() -> this.piggyback);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityPiggyback.PIGGYBACK) {
            return this.providerCap.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compoundNBT = new CompoundNBT();
        ListNBT riderList = new ListNBT();

        // save riders
        for (Entity entity : this.player.getIndirectPassengers()) {
            String id = entity.getEncodeId();
            if (id != null && !"".equals(id)) {
                CompoundNBT entityTag = new CompoundNBT();
                CompoundNBT entityDataTag = new CompoundNBT();
                entity.saveWithoutId(entityDataTag);
                entityDataTag.putString("id", entity.getEncodeId());
                entityTag.putUUID("Attach", entity.getVehicle().getUUID());
                entityTag.put("Entity", entityDataTag);
                riderList.add(entityTag);
            }
        }

        compoundNBT.put("riders", riderList);

        if (riderList.isEmpty()) {
            return new CompoundNBT();
        }

        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        ListNBT riderList = nbt.getList("riders", 10);

        Map<UUID, Entity> attachedTo = Maps.newHashMap();

        if (this.player.getCommandSenderWorld() instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) this.player.getCommandSenderWorld();

            for (int i = 0; i < riderList.size(); i++) {
                CompoundNBT entityTag = riderList.getCompound(i);
                Entity entity = EntityType.loadEntityRecursive(entityTag.getCompound("Entity"), serverWorld, (p_217885_1_) -> !serverWorld.addFreshEntity(p_217885_1_) ? null : p_217885_1_);
                if (entity != null) {
                    UUID uuid = entityTag.getUUID("Attach");

                    attachedTo.put(uuid, entity);
                }
            }
        }
    }
}
