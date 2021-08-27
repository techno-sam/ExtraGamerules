package com.slimeist.chickenhat.core.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.IPacket;

public class ChickenHatNetworkUtils {

    public static void sendVanillaPacket(Entity player, IPacket<?> packet) {
        if (player instanceof ServerPlayerEntity && ((ServerPlayerEntity) player).connection != null) {
            ((ServerPlayerEntity) player).connection.send(packet);
        }
    }
}
