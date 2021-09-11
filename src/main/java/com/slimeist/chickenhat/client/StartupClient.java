package com.slimeist.chickenhat.client;

import com.slimeist.chickenhat.ChickenHat;
import com.slimeist.chickenhat.client.render.entity.DyedChickenRenderer;
import com.slimeist.chickenhat.common.items.RainbowSpawnEggItem;
import com.slimeist.chickenhat.core.init.ItemInit;
import com.slimeist.chickenhat.core.interfaces.IDyeableItem;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.logging.log4j.Level;

import static com.slimeist.chickenhat.core.init.EntityTypeInit.DYED_CHICKEN;
import static com.slimeist.chickenhat.core.init.EntityTypeInit.DYED_EGG;

public class StartupClient {

    @SubscribeEvent
    public static void setupClient(final FMLClientSetupEvent event) {
        ItemRenderer itemRenderer = event.getMinecraftSupplier().get().getItemRenderer();

        RenderingRegistry.registerEntityRenderingHandler(DYED_CHICKEN, DyedChickenRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(DYED_EGG, (rendererManager) -> new SpriteRenderer<>(rendererManager, itemRenderer));
    }

    @SubscribeEvent
    public static void registerItemColor(final ColorHandlerEvent.Item event) {
        ItemColors itemcolors = event.getItemColors();
        itemcolors.register((p_210239_0_, p_210239_1_) -> {
            return p_210239_1_ > 0 ? -1 : ((IDyeableArmorItem)p_210239_0_.getItem()).getColor(p_210239_0_);
        }, ItemInit.CHICKEN_HELMET);
        itemcolors.register((p_210239_0_, p_210239_1_) -> {
            return p_210239_1_ > 0 ? -1 : ((IDyeableItem)p_210239_0_.getItem()).getDisplayColor(p_210239_0_);
        }, ItemInit.DYED_EGG);
        itemcolors.register((p_210239_0_, p_210239_1_) -> {
            return ((RainbowSpawnEggItem)p_210239_0_.getItem()).getColor(p_210239_0_, p_210239_1_);
        }, ItemInit.DYED_CHICKEN_SPAWN_EGG);
        ChickenHat.LOGGER.log(Level.ERROR, "Registered Item Colors");
    }
}
