package com.slimeist.extragamerules;

import com.slimeist.extragamerules.client.ClientProxy;
import com.slimeist.extragamerules.client.ClientSideOnlyModEventRegistrar;
import com.slimeist.extragamerules.common.CommonEventHandler;
import com.slimeist.extragamerules.common.CommonProxy;
import com.slimeist.extragamerules.common.StartupCommon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExtraGamerules.MOD_ID)
public class ExtraGamerules
{
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "extragamerules";
    public static CommonProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public ExtraGamerules() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        final ClientSideOnlyModEventRegistrar clientSideOnlyModEventRegistrar = new ClientSideOnlyModEventRegistrar(bus);

        bus.register(StartupCommon.class);
        MinecraftForge.EVENT_BUS.register(CommonEventHandler.class);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> clientSideOnlyModEventRegistrar::registerClientOnlyEvents);
    }

    public static ResourceLocation getId(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static void info(String msg) {
        log(Level.INFO, msg);
    }

    public static void log(Level level, String msg) {
        LOGGER.log(level, msg);
    }
}