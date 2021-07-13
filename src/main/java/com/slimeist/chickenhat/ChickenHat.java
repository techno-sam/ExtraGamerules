package com.slimeist.chickenhat;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.slimeist.chickenhat.client.render.entity.FakeChickenRenderer;
import com.slimeist.chickenhat.common.entities.FakeChickenEntity;
import com.slimeist.chickenhat.common.items.ChickenHelmet;
import com.slimeist.chickenhat.common.items.DyeableItem;
import com.slimeist.chickenhat.core.init.EntityTypeInit;
import io.netty.handler.logging.LogLevel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.slimeist.chickenhat.core.init.ItemInit;
import com.slimeist.chickenhat.core.init.BlockInit;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.opengl.GL11;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(ChickenHat.MOD_ID)
public class ChickenHat
{
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "chickenhat";

    public ChickenHat() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);

        bus.addGenericListener(EntityType.class, EntityTypeInit::registerAll);
        bus.addGenericListener(Item.class, ItemInit::registerAll);

        bus.addListener(EntityTypeInit::setup);
        bus.addListener(EntityTypeInit::setupClient);

        bus.addListener(this::registerItemColor);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        LOGGER.log(Level.ERROR, "Setting up ChickenHat mod");
    }

    private void registerItemColor(ColorHandlerEvent.Item event) {
        ItemColors itemcolors = event.getItemColors();
        itemcolors.register((p_210239_0_, p_210239_1_) -> {
            return p_210239_1_ > 0 ? -1 : ((IDyeableArmorItem)p_210239_0_.getItem()).getColor(p_210239_0_);
        }, ItemInit.CHICKEN_HELMET);
        itemcolors.register((p_210239_0_, p_210239_1_) -> {
            return p_210239_1_ > 0 ? -1 : ((DyeableItem)p_210239_0_.getItem()).getColor(p_210239_0_);
        }, ItemInit.DYED_EGG);
        LOGGER.log(Level.ERROR, "Registered Item Colors");
    }

    /*@SubscribeEvent
    public void renderPlayerChicken(RenderPlayerEvent.Post event) {
        PlayerEntity player = event.getPlayer();

        ItemStack head_stack = player.inventory.getArmor(3);
        Item head_item = head_stack.getItem();
        //LOGGER.log(Level.INFO,head_item.getName(head_stack).getString());


        if (head_item instanceof ArmorItem) {
            ArmorItem head_armor_item = (ArmorItem) head_item;
            //LOGGER.log(Level.INFO, "Helmet is armor!");
            if (head_armor_item instanceof ChickenHelmet) {
                //LOGGER.log(Level.INFO, "Helmet is chicken");
                ChickenHelmet chickenHelmet = (ChickenHelmet) head_item;
                int helmetColor = chickenHelmet.getColor(head_stack);
                float r = (float) (helmetColor >> 16 & 255) / 255.0F;
                float g = (float) (helmetColor >> 8 & 255) / 255.0F;
                float b = (float) (helmetColor & 255) / 255.0F;

                ChickenEntity chickenEntity = new ChickenEntity(EntityType.CHICKEN, player.level);
                FakeChickenRenderer chickenRenderer = new FakeChickenRenderer(event.getRenderer().getDispatcher());

                //LOGGER.log(Level.INFO, "Wearing helmet with color: (" + r + ", " + g + ", " + b + ")");

                chickenRenderer.getModel().setColor(r, g, b);

                float yaw = 0;
                chickenEntity.yBodyRot = player.yHeadRot;
                chickenEntity.yHeadRot = chickenEntity.yBodyRot;
                chickenEntity.yBodyRotO = chickenEntity.yBodyRot;
                chickenEntity.yHeadRotO = chickenEntity.yHeadRot;

                //event.getMatrixStack().pushPose();
                event.getMatrixStack().translate(0, player.getEyeHeight(), 0);
                //event.getMatrixStack().mulPose(Vector3f.YP.rotationDegrees(player.yHeadRot)); //player.yHeadRot is head yaw
                //event.getMatrixStack().mulPose(Vector3f.XP.rotationDegrees(player.xRot)); //player.xRot is head pitch;

                //LOGGER.log(Level.INFO, "player.getEyeHeight() = "+player.getEyeHeight());

                float partialTicks = event.getPartialRenderTick();
                int packedLight = event.getLight();//8;

                //RenderSystem.color4f(1.0f,0.0f,0.0f, 1.0f);
                chickenRenderer.render(chickenEntity, yaw, partialTicks, event.getMatrixStack(), event.getBuffers(), packedLight);
            //event.getBuffers().getBuffer(RenderType.entitySolid(chickenRenderer.getTextureLocation(chickenEntity))).color(1.0f,0.0f, 1.0f, 1.0f);
            //RenderSystem.color4f(1.0f,1.0f,1.0f, 1.0f);
            }
        }
    }*/



    @SubscribeEvent
    public void renderLivingEntityChicken(RenderLivingEvent.Post event) {
        LivingEntity livingEntity = event.getEntity();

        ItemStack head_stack = livingEntity.getItemBySlot(EquipmentSlotType.HEAD);//inventory.getArmor(3);
        Item head_item = head_stack.getItem();
        //LOGGER.log(Level.INFO,head_item.getName(head_stack).getString());


        if (head_item instanceof ArmorItem) {
            ArmorItem head_armor_item = (ArmorItem) head_item;
            //LOGGER.log(Level.INFO, "Helmet is armor!");
            if (head_armor_item instanceof ChickenHelmet) {
                //LOGGER.log(Level.INFO, "Helmet is chicken");
                ChickenHelmet chickenHelmet = (ChickenHelmet) head_item;
                int helmetColor = chickenHelmet.getColor(head_stack);
                /*float r = (float) (helmetColor >> 16 & 255) / 255.0F;
                float g = (float) (helmetColor >> 8 & 255) / 255.0F;
                float b = (float) (helmetColor & 255) / 255.0F;*/

                FakeChickenEntity chickenEntity = new FakeChickenEntity(EntityTypeInit.FAKE_CHICKEN, livingEntity.level);
                FakeChickenRenderer chickenRenderer = new FakeChickenRenderer(event.getRenderer().getDispatcher());

                //LOGGER.log(Level.INFO, "Wearing helmet with color: (" + r + ", " + g + ", " + b + ")");

                chickenEntity.setColor(helmetColor);

                boolean isFalling = livingEntity.isOnGround();

                float yaw = 0;
                chickenEntity.yBodyRot = livingEntity.yHeadRot;
                chickenEntity.yHeadRot = chickenEntity.yBodyRot;
                chickenEntity.yBodyRotO = chickenEntity.yBodyRot;
                chickenEntity.yHeadRotO = chickenEntity.yHeadRot;

                //event.getMatrixStack().pushPose();
                event.getMatrixStack().translate(0, livingEntity.getEyeHeight(), 0);
                //event.getMatrixStack().mulPose(Vector3f.YP.rotationDegrees(player.yHeadRot)); //player.yHeadRot is head yaw
                //event.getMatrixStack().mulPose(Vector3f.XP.rotationDegrees(player.xRot)); //player.xRot is head pitch;

                //LOGGER.log(Level.INFO, "player.getEyeHeight() = "+player.getEyeHeight());

                float partialTicks = event.getPartialRenderTick();
                int packedLight = event.getLight();//8;

                //RenderSystem.color4f(1.0f,0.0f,0.0f, 1.0f);
                chickenRenderer.render(chickenEntity, yaw, partialTicks, event.getMatrixStack(), event.getBuffers(), packedLight);
            //event.getBuffers().getBuffer(RenderType.entitySolid(chickenRenderer.getTextureLocation(chickenEntity))).color(1.0f,0.0f, 1.0f, 1.0f);
            //RenderSystem.color4f(1.0f,1.0f,1.0f, 1.0f);
            }
        }
    }

    public static ResourceLocation getId(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}