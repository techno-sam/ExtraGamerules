package com.slimeist.extragamerules.common;

import com.slimeist.extragamerules.ExtraGamerules;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.Level;

public class StartupCommon {

    @SubscribeEvent
    public static void setup(final FMLCommonSetupEvent event)
    {
        ExtraGamerules.log(Level.INFO, "Setting up ExtraGamerules mod");
        ExtraGamerulesTags.init();
    }
}
