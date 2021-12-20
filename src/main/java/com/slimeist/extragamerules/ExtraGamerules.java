package com.slimeist.extragamerules;

import com.slimeist.extragamerules.client.ClientProxy;
import com.slimeist.extragamerules.client.ClientSideOnlyModEventRegistrar;
import com.slimeist.extragamerules.common.CommonEventHandler;
import com.slimeist.extragamerules.common.CommonProxy;
import com.slimeist.extragamerules.common.StartupCommon;
import com.slimeist.extragamerules.core.Rule;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.GameRules;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.MavenVersionStringHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.List;


@Mod(ExtraGamerules.MOD_ID)
public class ExtraGamerules
{
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "extragamerules";
    public static final String VERSION = getVersion();
    public static CommonProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);


    //RULES
    public static Rule.BooleanRule SNOW_MELT = new Rule.BooleanRule(gameruleId("doSnowMelt"), GameRules.Category.MISC, true);
    public static Rule.BooleanRule ICE_MELT = new Rule.BooleanRule(gameruleId("doIceMelt"), GameRules.Category.MISC, true);


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

    public static String gameruleId(String name) {
        return MOD_ID + "." + name;
    }

    public static void info(String msg) {
        log(Level.INFO, msg);
    }

    public static void log(Level level, String msg) {
        LOGGER.log(level, msg);
    }

    private static String getVersion() {
        String versionString = "BROKEN";

        List<IModInfo> infoList = ModList.get().getModFileById(MOD_ID).getMods();
        if (infoList.stream().count()>1) {
            LOGGER.error("Multiple mods for MOD_ID: "+MOD_ID);
        }
        for (IModInfo info : infoList) {
            if (info.getModId().equals(MOD_ID)) {
                versionString = MavenVersionStringHelper.artifactVersionToString(info.getVersion());
                break;
            }
        }
        return versionString;
    }
}