package com.slimeist.chickenhat.common.items;

import com.google.common.collect.ImmutableMultimap;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.slimeist.chickenhat.client.guis.icons.Icons;
import com.slimeist.chickenhat.common.entities.DyedChickenEntity;
import com.slimeist.chickenhat.common.potions.BaseEffect;
import com.slimeist.chickenhat.core.capabilities.CapabilityChickencarry;
import com.slimeist.chickenhat.core.init.ItemInit;
import com.slimeist.chickenhat.core.init.PotionsInit;
import com.slimeist.chickenhat.core.interfaces.IChickencarry;
import com.slimeist.chickenhat.core.util.ChickenHatNetworkUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.network.play.server.SSetPassengersPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.items.ItemHandlerHelper;
import com.google.common.collect.Multimap;
import slimeknights.mantle.client.screen.ElementScreen;

import javax.annotation.Nonnull;
import java.util.List;

public class ChickenHelmet extends ArmorItem implements IDyeableArmorItem { //derived from slimeknight's Tinker's Construct Piggybackpack
    private static final int MAX_ENTITY_STACK = 1; // how many entities can ride on top of each other
    public ChickenHelmet(IArmorMaterial material, EquipmentSlotType slottype, Properties properties) {
        super(material, slottype, properties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (Screen.hasShiftDown()) {
            tooltip.add(new TranslationTextComponent("tooltip.chickenhat.winged_helmet.advanced"));
        } else {
            tooltip.add(new TranslationTextComponent("tooltip.chickenhat.winged_helmet.hold_shift"));
        }
    }

    @Override
    public ActionResultType interactLivingEntity(ItemStack stackIn, PlayerEntity playerIn, LivingEntity entityIn, Hand handIn) {
        //check if head slot is empty
        ItemStack headArmor = playerIn.getItemBySlot(EquipmentSlotType.HEAD);

        //need enough space for exchanging helmets
        if (headArmor.getItem()!=this && playerIn.inventory.getFreeSlot() == -1) {
            return ActionResultType.PASS;
        }

        //try to carry this entity
        if (this.pickupEntity(playerIn, entityIn)) {
            //unequip old helmet
            if (headArmor.getItem() != this) {
                ItemHandlerHelper.giveItemToPlayer(playerIn, headArmor);
                headArmor = ItemStack.EMPTY;
            }

            //we could pick it up, check if we need to equip more
            if (headArmor.isEmpty()) {
                playerIn.setItemSlot(EquipmentSlotType.HEAD, stackIn.split(1));
            } else if (headArmor.getCount()<this.getEntitiesCarriedCount(playerIn)) {
                stackIn.split(1);
                headArmor.grow(1);
            }
            //successfully picked up entity
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    private static boolean isValidEntity(Entity entity) {
        return entity instanceof DyedChickenEntity;
    }

    private boolean pickupEntity(PlayerEntity playerIn, Entity target) {
        if (playerIn.getCommandSenderWorld().isClientSide) {
            return false;
        }
        if (!isValidEntity(target)) {
            return false;
        }
        //idiots think they have to pick entities back up that are already riding on them, or that they are riding on
        if (target.getVehicle() == playerIn || playerIn.getVehicle() == target) {
            return false;
        }

        int count = 0;
        Entity toRide = playerIn;
        while (toRide.isVehicle() && count < MAX_ENTITY_STACK) {
            toRide = toRide.getPassengers().get(0);
            count++;
            //no players riding players, causes endless loops
            if (toRide instanceof PlayerEntity && target instanceof PlayerEntity) {
                return false;
            }
        }

        //can only ride one entity each
        if (!toRide.isVehicle() && count<MAX_ENTITY_STACK) {
            if (target.startRiding(playerIn, true)) {
                if (playerIn instanceof ServerPlayerEntity) {
                    ChickenHatNetworkUtils.sendVanillaPacket(playerIn, new SSetPassengersPacket(playerIn));
                }
                return true;
            }
        }
        return false;
    }

    private int getEntitiesCarriedCount(LivingEntity player) {
        int count = 0;
        Entity ridden = player;
        while (ridden.isVehicle()) {
            count++;
            ridden = ridden.getPassengers().get(0);
        }
        return count;
    }

    public void matchCarriedEntitiesToCount(LivingEntity player, int maxCount) {
        int count = 0;

        Entity ridden = player;
        while (ridden.isVehicle()) {
            ridden = ridden.getPassengers().get(0);
            count++;

            if (count>maxCount) {
                ridden.stopRiding();
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entityIn;
            if (livingEntity.getItemBySlot(EquipmentSlotType.HEAD) == stack && entityIn.isVehicle()) {
                int amplifier = this.getEntitiesCarriedCount(livingEntity) - 1;
                livingEntity.addEffect(new EffectInstance(PotionsInit.CARRY_EFFECT, 2, amplifier, true, false));
            }
        }
    }

    /*@Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot, ItemStack stack) {
        return ImmutableMultimap.of(); //potion effect handles them
    }*/

    public static class CarryPotionEffect extends BaseEffect {

        static final String SPEED_UUID = "ff4de63a-2b24-11e6-b67b-9e71128cae77";
        static final String GRAVITY_UUID = "ff4de63a-2b24-11e6-b67b-9e71128cae78";

        public CarryPotionEffect() {
            super(EffectType.NEUTRAL, true);

            this.addAttributeModifier(Attributes.MOVEMENT_SPEED, SPEED_UUID,-0.05D, AttributeModifier.Operation.MULTIPLY_TOTAL);
            //this.addAttributeModifier(ForgeMod.ENTITY_GRAVITY.get(), GRAVITY_UUID, -0.04D, AttributeModifier.Operation.MULTIPLY_TOTAL);
        }

        @Override
        public boolean isDurationEffectTick(int duration, int amplifier) {
            return true; //whether to apply this effect
        }

        @Override
        public void applyEffectTick(@Nonnull LivingEntity livingEntityIn, int amplifier) {
            ItemStack headArmor = livingEntityIn.getItemBySlot(EquipmentSlotType.HEAD);
            if (headArmor.isEmpty() || headArmor.getItem() != ItemInit.CHICKEN_HELMET) {
                ItemInit.CHICKEN_HELMET.matchCarriedEntitiesToCount(livingEntityIn, 0);
            } else {
                ItemInit.CHICKEN_HELMET.matchCarriedEntitiesToCount(livingEntityIn, headArmor.getCount());
                if (!livingEntityIn.getCommandSenderWorld().isClientSide) {
                    livingEntityIn.getCapability(CapabilityChickencarry.CHICKENCARRY, null).ifPresent(IChickencarry::updatePassengers);
                    /*Vector3d vector3d = livingEntityIn.getDeltaMovement();
                    boolean isFlying = livingEntityIn instanceof PlayerEntity && ((PlayerEntity) livingEntityIn).abilities.flying;
                    if (!livingEntityIn.isOnGround() && vector3d.y < 0.0D && !isFlying && !livingEntityIn.isFallFlying() && !livingEntityIn.isCrouching()) {
                        //livingEntityIn.setDeltaMovement(vector3d.multiply(1.0D, 0.6D, 1.0D)); //0.6D
                        livingEntityIn.fallDistance = 0;
                    }*/
                }
            }
        }

        @OnlyIn(Dist.CLIENT)
        @Override
        public void renderInventoryEffect(EffectInstance effect, DisplayEffectsScreen<?> gui, MatrixStack matrices, int x, int y, float z) {
            this.renderHUDEffect(effect, gui, matrices, x, y, z, 1f);
        }

        @OnlyIn(Dist.CLIENT)
        @Override
        public void renderHUDEffect(EffectInstance effect, AbstractGui gui, MatrixStack matrices, int x, int y, float z, float alpha) {
            Minecraft.getInstance().getTextureManager().bind(Icons.ICONS);
            ElementScreen element = Icons.CHICKENCARRY;

            element.draw(matrices, x+6, y+7);
        }
    }
}
