package com.slimeist.chickenhat.common.items;

import com.slimeist.chickenhat.ChickenHat;
import com.slimeist.chickenhat.core.init.EntityTypeInit;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.fml.Logging;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.logging.log4j.Level;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//this is copied from https://github.com/Silverminer007/MoreOre-1.16/blob/master/src/main/java/com/silverminer/moreore/common/objects/items/ModSpawnEggItem.java
public class ModSpawnEggItem extends SpawnEggItem {

    protected static final List<ModSpawnEggItem> UNADDED_EGGS = new ArrayList<>();
    private final String entityType;

    public ModSpawnEggItem(final String entityType, final int p_i48465_2_,
                           final int p_i48465_3_, final Properties p_i48465_4_) {
        super(null, p_i48465_2_, p_i48465_3_, p_i48465_4_);
        this.entityType = entityType;
        UNADDED_EGGS.add(this);
    }

    /**
     * Adds all the supplier based spawn eggs to vanilla's map and registers an
     * IDispenseItemBehavior for each of them as normal spawn eggs have one
     * registered for each of them during
     * {@link net.minecraft.dispenser.IDispenseItemBehavior#init()} but supplier
     * based ones won't have had their EntityTypes created yet.
     */
    public static void initUnaddedEggs() {
        final Map<EntityType<?>, SpawnEggItem> EGGS;
        EGGS = ObfuscationReflectionHelper.getPrivateValue(SpawnEggItem.class,
                null, "BY_ID");//"field_195987_b");
        DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior() {
            public ItemStack execute(IBlockSource source, ItemStack stack) {
                //ChickenHat.LOGGER.log(Level.INFO, "Trying to spawn from dispenser");
                Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
                EntityType<?> entitytype = ((ModSpawnEggItem) stack.getItem()).getType(stack.getTag());
                entitytype.spawn(source.getLevel(), stack, null, source.getPos().offset(direction.getNormal()),
                        SpawnReason.DISPENSER, direction != Direction.UP, false);
                stack.shrink(1);
                return stack;
            }
        };
        for (final ModSpawnEggItem mod_egg : UNADDED_EGGS) {
            SpawnEggItem egg = (SpawnEggItem) mod_egg;
            EGGS.put(mod_egg.getType(null), egg);
            DispenserBlock.registerBehavior(mod_egg, defaultDispenseItemBehavior);
            //ChickenHat.LOGGER.log(Level.INFO, "Registering dispense item behavior for: "+mod_egg.entityType);
            // ItemColors for each spawn egg don't need to be registered because this method
            // is called before ItemColors is created
        }
        UNADDED_EGGS.clear();
    }

    @Override
    public EntityType<?> getType(@Nullable final CompoundNBT p_208076_1_) {
        ChickenHat.log(Level.DEBUG, "Getting EntityType of spawn egg with entityType "+this.entityType);
        return EntityTypeInit.getType(this.entityType);
    }

}
