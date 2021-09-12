package com.slimeist.chickenhat.client;

import com.slimeist.chickenhat.ChickenHat;
import com.slimeist.chickenhat.client.render.entity.DyedChickenRenderer;
import com.slimeist.chickenhat.common.ChickenHatTags;
import com.slimeist.chickenhat.common.entities.DyedChickenEntity;
import com.slimeist.chickenhat.common.items.ChickenHelmet;
import com.slimeist.chickenhat.core.init.EntityTypeInit;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientEventHandler {

    @SubscribeEvent
    public static void renderLivingEntityChicken(RenderLivingEvent.Post event) {
        LivingEntity livingEntity = event.getEntity();

        if(!(livingEntity instanceof PlayerEntity)) {

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

                    DyedChickenEntity chickenEntity = new DyedChickenEntity(EntityTypeInit.DYED_CHICKEN, livingEntity.level);
                    DyedChickenRenderer chickenRenderer = new DyedChickenRenderer(event.getRenderer().getDispatcher());
                    if (head_stack.hasCustomHoverName()) {
                        chickenEntity.setCustomName(head_stack.getHoverName());
                    }

                    //LOGGER.log(Level.INFO, "Wearing helmet with color: (" + r + ", " + g + ", " + b + ")");

                    chickenEntity.setColor(helmetColor);

                    boolean onGround = livingEntity.isOnGround();

                    float yaw = 0;
                    chickenEntity.yBodyRot = livingEntity.yHeadRot;
                    chickenEntity.yHeadRot = chickenEntity.yBodyRot;
                    chickenEntity.yBodyRotO = chickenEntity.yBodyRot;
                    chickenEntity.yHeadRotO = chickenEntity.yHeadRot;
                    chickenEntity.setOnGround(onGround);
                    chickenEntity.calculateFlapping();
                    chickenEntity.setId(livingEntity.getId());
                    chickenEntity.tickCount = livingEntity.tickCount;
                    chickenEntity.setBlockPosition(livingEntity.blockPosition());

                    event.getMatrixStack().pushPose();
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
                    event.getMatrixStack().popPose();
                }
            }
        }
    }

    /*@SubscribeEvent
    public static void addToolTips(final ItemTooltipEvent event) {
        if (event.getItemStack().isEmpty()) {
            return;
        }
        if (event.getItemStack().getItem().is(ChickenHatTags.Items.WINGED_CAP)) {
            if (Screen.hasShiftDown()) {
                event.getToolTip().add(1, new TranslationTextComponent("tooltip.chickenhat.winged_cap.advanced"));
            } else {
                event.getToolTip().add(1, new TranslationTextComponent("tooltip.chickenhat.winged_cap.hold_shift"));
            }
        }
    }*/
}
