package com.slimeist.extragamerules.common;

import com.slimeist.extragamerules.ExtraGamerules;
import com.slimeist.extragamerules.core.Rule;
import net.minecraft.world.GameRules;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.Level;

public class StartupCommon {

    @SubscribeEvent
    public static void setup(final FMLCommonSetupEvent event)
    {
        ExtraGamerules.log(Level.INFO, "Setting up ExtraGamerules mod");
        ExtraGamerulesTags.init();

        event.enqueueWork(() -> makeBooleanRule(ExtraGamerules.SNOW_MELT));
        event.enqueueWork(() -> makeBooleanRule(ExtraGamerules.ICE_MELT));
    }

    private static void makeBooleanRule(Rule.BooleanRule rule) {
        GameRules.RuleType<GameRules.BooleanValue> type = GameRules.BooleanValue.create(rule.getDefault());
        rule.setKey(GameRules.register(rule.getName(), rule.getCategory(), type));
    }
}
