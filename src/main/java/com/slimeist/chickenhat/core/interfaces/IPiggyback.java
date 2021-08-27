package com.slimeist.chickenhat.core.interfaces;

import net.minecraft.entity.player.PlayerEntity;

public interface IPiggyback {

    void setRiddenPlayer(PlayerEntity player);

    void updatePassengers();
}
