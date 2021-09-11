package com.slimeist.chickenhat.core.capabilities;

import com.slimeist.chickenhat.core.interfaces.IChickencarry;
import com.slimeist.chickenhat.core.util.ChickenHatNetworkUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SSetPassengersPacket;

import java.util.List;

public class ChickencarryHander implements IChickencarry {

    private PlayerEntity riddenPlayer;
    private List<Entity> lastPassengers;

    @Override
    public void setRiddenPlayer(PlayerEntity player) {
        this.riddenPlayer = player;
    }

    @Override
    public void updatePassengers() {
        if (this.riddenPlayer!=null) {
            if (!this.riddenPlayer.getPassengers().equals(this.lastPassengers)) {
                if (this.riddenPlayer instanceof ServerPlayerEntity) {
                    ChickenHatNetworkUtils.sendVanillaPacket(this.riddenPlayer, new SSetPassengersPacket(this.riddenPlayer));
                }
            }
            this.lastPassengers = this.riddenPlayer.getPassengers();
        }
    }
}
