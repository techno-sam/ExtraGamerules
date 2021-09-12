package com.slimeist.chickenhat.common.entities;

import com.slimeist.chickenhat.common.items.DyedEgg;
import com.slimeist.chickenhat.core.init.EntityTypeInit;
import com.slimeist.chickenhat.core.init.ItemInit;
import com.slimeist.chickenhat.core.interfaces.IDyeableItem;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;

public class DyedEggEntity extends ProjectileItemEntity {
    public DyedEggEntity(EntityType<? extends DyedEggEntity> p_i50154_1_, World p_i50154_2_) {
        super(p_i50154_1_, p_i50154_2_);
    }

    public DyedEggEntity(World p_i1780_1_, LivingEntity p_i1780_2_) {
        super(EntityTypeInit.DYED_EGG, p_i1780_2_, p_i1780_1_);
    }

    public DyedEggEntity(World p_i1781_1_, double p_i1781_2_, double p_i1781_4_, double p_i1781_6_) {
        super(EntityTypeInit.DYED_EGG, p_i1781_2_, p_i1781_4_, p_i1781_6_, p_i1781_1_);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleEntityEvent(byte p_70103_1_) {
        if (p_70103_1_ == 3) {
            double d0 = 0.08D;

            ItemStack stack = this.getItem();

            for(int i = 0; i < 8; ++i) {
                this.level.addParticle(new ItemParticleData(ParticleTypes.ITEM, stack), this.getX(), this.getY(), this.getZ(), ((double)this.random.nextFloat() - 0.5D) * 0.08D, ((double)this.random.nextFloat() - 0.5D) * 0.08D, ((double)this.random.nextFloat() - 0.5D) * 0.08D);
            }
        } else {
            super.handleEntityEvent(p_70103_1_);
        }
    }

    protected void onHitEntity(EntityRayTraceResult p_213868_1_) {
        super.onHitEntity(p_213868_1_);
        p_213868_1_.getEntity().hurt(DamageSource.thrown(this, this.getOwner()), 0.0F);
    }

    protected void onHit(RayTraceResult p_70227_1_) {
        super.onHit(p_70227_1_);
        if (!this.level.isClientSide) {
            if (this.random.nextInt(8) == 0) {
                int i = 1;
                if (this.random.nextInt(32) == 0) {
                    i = 4;
                }

                for(int j = 0; j < i; ++j) {
                    DyedChickenEntity chickenentity = EntityTypeInit.DYED_CHICKEN.create(this.level);
                    chickenentity.setAge(-24000);
                    ItemStack itemStack = this.getItem();
                    Item item = itemStack.getItem();
                    DyedEgg eggitem = (DyedEgg) item;
                    int color = eggitem.getColor(itemStack);
                    chickenentity.setColor(color);
                    chickenentity.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, 0.0F);
                    this.level.addFreshEntity(chickenentity);
                }
            }

            this.level.broadcastEntityEvent(this, (byte)3);
            this.remove();
        }

    }

    protected Item getDefaultItem() {
        return ItemInit.DYED_EGG;
    }

    @Nonnull
    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
