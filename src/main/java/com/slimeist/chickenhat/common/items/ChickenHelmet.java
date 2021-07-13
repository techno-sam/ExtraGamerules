package com.slimeist.chickenhat.common.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class ChickenHelmet extends ArmorItem implements IDyeableArmorItem {

    public ChickenHelmet(IArmorMaterial material, EquipmentSlotType slottype, Properties properties) {
        super(material, slottype, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
            tooltip.add(new TranslationTextComponent("tooltip.chickenhat.chicken_helmet.advanced"));
        } else {
            tooltip.add(new TranslationTextComponent("tooltip.chickenhat.chicken_helmet.hold_shift"));
        }
    }

    @Override
    public void onArmorTick(ItemStack stackIn, World worldIn, PlayerEntity playerIn) {
        int random = playerIn.getRandom().nextInt(50);
        if (random==1 && false) {
            worldIn.playSound(playerIn, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.CHICKEN_AMBIENT, SoundCategory.AMBIENT, 1.0f, 1.0f);
        }
    }
}
