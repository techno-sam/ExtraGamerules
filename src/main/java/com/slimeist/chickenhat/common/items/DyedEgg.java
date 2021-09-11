package com.slimeist.chickenhat.common.items;

import com.slimeist.chickenhat.ChickenHat;
import com.slimeist.chickenhat.common.entities.DyedEggEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

public class DyedEgg extends DyeableItem {
    public DyedEgg(Properties properties) {
        super(properties, false);
    }

    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        worldIn.playSound((PlayerEntity)null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
        if (!worldIn.isClientSide) {
            DyedEggEntity eggentity = new DyedEggEntity(worldIn, playerIn);
            eggentity.setItem(itemstack);
            eggentity.shootFromRotation(playerIn, playerIn.xRot, playerIn.yRot, 0.0F, 1.5F, 1.0F);
            worldIn.addFreshEntity(eggentity);
        }

        //playerIn.awardStat(Stats.ITEM_USED.get(this));
        if (!playerIn.abilities.instabuild) {
            itemstack.shrink(1);
        }

        return ActionResult.sidedSuccess(itemstack, worldIn.isClientSide());
    }

    @Override
    public int getColor(ItemStack stack) {
        CompoundNBT compoundnbt = stack.getTagElement("display");
        return compoundnbt != null && compoundnbt.contains("color", 99) ? compoundnbt.getInt("color") : 8355969;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public int getDisplayColor(ItemStack stack) {
        long time = ChickenHat.proxy.getGameTime();
        int cycle_length = 400; //match cycle length for sheep (16 wool colors, 25 ticks each)
        int offset = stack.hashCode();
        if (stack.getEntityRepresentation()!=null) {
            offset = stack.getEntityRepresentation().getId();
        }
        float hue = (time + (offset*25)) % cycle_length;
        hue /= cycle_length;
        int rainbowColor = Color.HSBtoRGB(hue, 1.0f, 1.0f);
        CompoundNBT compoundnbt = stack.getTagElement("display");
        return compoundnbt != null && compoundnbt.contains("color", 99) ? compoundnbt.getInt("color") : rainbowColor;
    }
}
