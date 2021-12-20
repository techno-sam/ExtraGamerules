package com.slimeist.extragamerules.client;

import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class StartupClient {

    @SubscribeEvent
    public static void setupClient(final FMLClientSetupEvent event) {
    }

    @SubscribeEvent
    public static void registerItemColor(final ColorHandlerEvent.Item event) {
    }
}
