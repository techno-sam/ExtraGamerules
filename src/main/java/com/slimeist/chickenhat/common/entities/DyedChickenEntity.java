package com.slimeist.chickenhat.common.entities;

import com.slimeist.chickenhat.ChickenHat;
import com.slimeist.chickenhat.common.items.DyedEgg;
import com.slimeist.chickenhat.core.init.ItemInit;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import com.slimeist.chickenhat.core.init.EntityTypeInit;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Random;

public class DyedChickenEntity extends AnimalEntity implements IEntityAdditionalSpawnData {
    public LivingEntity masterEntity = null;
    public static final DataParameter<Integer> COLOR = EntityDataManager.defineId(DyedChickenEntity.class, DataSerializers.INT);
    //public static final DataParameter<Optional<UUID>> MASTER_UUID = EntityDataManager.defineId(FakeChickenEntity.class, DataSerializers.OPTIONAL_UUID);

    private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS);
    public float flap;
    public float flapSpeed;
    public float oFlapSpeed;
    public float oFlap;
    public float flapping = 1.0F;
    public int eggTime = this.random.nextInt(6000) + 6000;
    public boolean isChickenJockey;
    //private Color colorManager = new Color(1.0f, 1.0f, 1.0f);

    public DyedChickenEntity(EntityType<? extends DyedChickenEntity> type, World world) {
        super(type, world);
        this.setPathfindingMalus(PathNodeType.WATER, 0.0F);
    }

    public DyedChickenEntity(EntityType<? extends DyedChickenEntity> type, World world, LivingEntity masterEntity) {
        super(type, world);
        this.masterEntity = masterEntity;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.4D));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.0D, false, FOOD_ITEMS));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
    }

    protected float getStandingEyeHeight(Pose p_213348_1_, EntitySize p_213348_2_) {
        return this.isBaby() ? p_213348_2_.height * 0.85F : p_213348_2_.height * 0.92F;
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0D).add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    public void calculateFlapping() {
        this.oFlap = this.flap;
        this.oFlapSpeed = this.flapSpeed;
        this.flapSpeed = (float)((double)this.flapSpeed + (double)(this.onGround ? -1 : 4) * 0.3D);
        this.flapSpeed = MathHelper.clamp(this.flapSpeed, 0.0F, 1.0F);
        if (!this.onGround && this.flapping < 1.0F) {
            this.flapping = 1.0F;
        }

        this.flapping = (float)((double)this.flapping * 0.9D);
    }

    @Override
    public void aiStep() {
        this.onGround = this.isOnGround();
        super.aiStep();
        this.calculateFlapping();
        Vector3d vector3d = this.getDeltaMovement();
        if (!this.onGround && vector3d.y < 0.0D) {
            this.setDeltaMovement(vector3d.multiply(1.0D, 0.6D, 1.0D));
        }

        this.flap += this.flapping * 2.0F;
        if (!this.level.isClientSide && this.isAlive() && !this.isBaby() && !this.isChickenJockey() && --this.eggTime <= 0) {
            this.playSound(SoundEvents.CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            ItemStack egg_stack = new ItemStack(ItemInit.DYED_EGG);
            egg_stack.setCount(1);
            DyedEgg egg_item = (DyedEgg) egg_stack.getItem();
            egg_item.setColor(egg_stack, this.getColor());
            this.spawnAtLocation(egg_stack);
            this.eggTime = this.random.nextInt(6000) + 6000;
        }

    }

    public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
        return false;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.CHICKEN_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.CHICKEN_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.CHICKEN_DEATH;
    }

    protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
        this.playSound(SoundEvents.CHICKEN_STEP, 0.15F, 1.0F);
    }

    public DyedChickenEntity getBreedOffspring(ServerWorld p_241840_1_, AgeableEntity p_241840_2_) {
        DyedChickenEntity otherParent = (DyedChickenEntity) p_241840_2_;
        DyedChickenEntity baby = EntityTypeInit.DYED_CHICKEN.create(p_241840_1_);
        baby.setColor(this.getOffspringColor(this, otherParent));
        return baby;
    }

    private int getOffspringColor(DyedChickenEntity parent1, DyedChickenEntity parent2) {
        int[] color1 = unpackColor(parent1.getColor());
        int[] color2 = unpackColor(parent2.getColor());

        int r1 = color1[0];
        int g1 = color1[1];
        int b1 = color1[2];

        int r2 = color2[0];
        int g2 = color2[1];
        int b2 = color2[2];

        int r3 = (r1+r2)/2;
        int g3 = (g1+g2)/2;
        int b3 = (b1+b2)/2;
        int color3 = packColor(r3, g3, b3);
        return color3;
    }

    private static void log(String msg) {
        ChickenHat.LOGGER.info(msg);
    }

    private static String colorToString(int c) {
        float[] rgb = unpackColorFloat(c);
        return "(r: "+rgb[0]+", g: "+rgb[1]+", b: "+rgb[2]+")";
    }

    @Override
    public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.getItem() instanceof DyeItem) {
            DyeItem dyeItem = (DyeItem) itemstack.getItem();
            DyeColor dyeColor = dyeItem.getDyeColor();//.getColorValue();
            int currentColor = this.getColor();

            boolean thisIsLight = packColorFloat(createChickenColor(dyeColor, false)) == currentColor;
            boolean thisIsDark = packColorFloat(createChickenColor(dyeColor, true)) == currentColor;
            //log("Our color: "+colorToString(currentColor)+", dyeColor: "+dyeColor.getName()+", lightColor: "+colorToString(packColorFloat(createChickenColor(dyeColor, false)))+", darkColor: "+colorToString(packColorFloat(createChickenColor(dyeColor, true)))+", thisIsLight: "+thisIsLight+", thisIsDark: "+thisIsDark);
            if (!this.level.isClientSide) {
                if (!thisIsLight && !thisIsDark) { //we are neither the light nor the dark version of this dye, randomly make us light or dark
                    this.setColor(packColorFloat(createChickenColor(dyeColor, this.random.nextBoolean())));
                    itemstack.shrink(1);
                    return ActionResultType.SUCCESS;
                } else if (!thisIsLight && thisIsDark) { //we are the dark version of this dye, make us light
                    this.setColor(packColorFloat(createChickenColor(dyeColor, false)));
                    //itemstack.shrink(1);
                    return ActionResultType.SUCCESS;
                } else if (thisIsLight && !thisIsDark) { //we are the light version of this dye, make us dark
                    this.setColor(packColorFloat(createChickenColor(dyeColor, true)));
                    //itemstack.shrink(1);
                    return ActionResultType.SUCCESS;
                } else {
                    return ActionResultType.PASS;
                }
            } else {
                return ActionResultType.CONSUME;
            }
        } else {
            return super.mobInteract(player, hand);
        }
    }

    public boolean isFood(ItemStack p_70877_1_) {
        return FOOD_ITEMS.test(p_70877_1_);
    }

    protected int getExperienceReward(PlayerEntity p_70693_1_) {
        return this.isChickenJockey() ? 10 : super.getExperienceReward(p_70693_1_);
    }

    public void readAdditionalSaveData(CompoundNBT nbt) {
        super.readAdditionalSaveData(nbt);
        this.isChickenJockey = nbt.getBoolean("IsChickenJockey");
        if (nbt.contains("EggLayTime")) {
            this.eggTime = nbt.getInt("EggLayTime");
        }

        if (nbt.contains("Color")) {
            this.setColor(nbt.getInt("Color"));
        }
    }

    public void addAdditionalSaveData(CompoundNBT nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("IsChickenJockey", this.isChickenJockey);
        nbt.putInt("EggLayTime", this.eggTime);
        nbt.putInt("Color", this.getColor());
    }

    public boolean removeWhenFarAway(double p_213397_1_) {
        return this.isChickenJockey();
    }

    public void positionRider(Entity p_184232_1_) {
        super.positionRider(p_184232_1_);
        float f = MathHelper.sin(this.yBodyRot * ((float)Math.PI / 180F));
        float f1 = MathHelper.cos(this.yBodyRot * ((float)Math.PI / 180F));
        float f2 = 0.1F;
        float f3 = 0.0F;
        p_184232_1_.setPos(this.getX() + (double)(0.1F * f), this.getY(0.5D) + p_184232_1_.getMyRidingOffset() + 0.0D, this.getZ() - (double)(0.1F * f1));
        if (p_184232_1_ instanceof LivingEntity) {
            ((LivingEntity)p_184232_1_).yBodyRot = this.yBodyRot;
        }

    }

    public boolean isChickenJockey() {
        return this.isChickenJockey;
    }

    public void setChickenJockey(boolean p_152117_1_) {
        this.isChickenJockey = p_152117_1_;
    }

    public boolean isPuppet() {
        return this.masterEntity!=null;
    }

    @Override
    public boolean isOnGround() {
        if (this.isPuppet()) {
            return this.masterEntity.isOnGround();
        }
        return super.isOnGround();
    }

    @Override
    public Vector3d getDeltaMovement() {
        if (this.isPuppet()) {
            return this.masterEntity.getDeltaMovement();
        }
        return super.getDeltaMovement();
    }

    @Override
    public void setDeltaMovement(Vector3d deltaMovement) {
        if (this.isPuppet()) {
            this.masterEntity.setDeltaMovement(deltaMovement);
        }
        super.setDeltaMovement(deltaMovement);
    }

    @Override
    public void setDeltaMovement(double x, double y, double z) {
        if (this.isPuppet()) {
            this.masterEntity.setDeltaMovement(x, y, z);
        }
        super.setDeltaMovement(x, y, z);
    }

    @Override
    public float getHealth() {
        if (this.isPuppet()) {
            return this.getMaxHealth();
        }
        return super.getHealth();
    }

    public static boolean spawnPredicate(EntityType<? extends AnimalEntity> type, IWorld world, SpawnReason reason, BlockPos pos, Random random) {
        return AnimalEntity.checkAnimalSpawnRules(type, world, reason, pos, random);//world.getBlockState(pos.below()).getBlock().is(Blocks.GRASS_BLOCK) && world.getRawBrightness(pos, 0) > 8;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        int defaultColorInt = packColor(127, 128, 129);
        this.entityData.define(COLOR, defaultColorInt);
    }

    /*public void setColor(float r, float g, float b) {
        int packedColor = Color.packColor(r, g, b);
        if (this.level.isClientSide) {
            LOGGER.log(Level.ERROR, "Setting color on client is not recommended");
        }
        this.entityData.set(this.COLOR, packedColor);
    }*/

    public void setColor(int packedColor) {
        /*if (level.isClientSide) {
            LOGGER.log(Level.ERROR, "Setting color on client is not recommended");
        }*/
        this.entityData.set(COLOR, packedColor);
    }

    public int getColor() {
        return this.entityData.get(COLOR);
    }

    public static int packColor(int r, int g, int b) {
        return new Color(r, g, b, 0).getRGB();
    }

    public static int[] unpackColor(int rgb) {
        Color c = new Color(rgb);
        return new int[]{c.getRed(), c.getGreen(), c.getBlue()};
    }

    private static float[] unpackColorFloat(int rgb) {
        Color c = new Color(rgb);
        return new float[]{c.getRed()/255.0f, c.getGreen()/255.0f, c.getBlue()/255.0f};
    }

    private static int packColorFloat(float r, float g, float b) {
        return new Color(r, g, b, 0).getRGB();
    }

    private static int packColorFloat(float[] rgb) {
        return new Color(rgb[0], rgb[1], rgb[2], 0).getRGB();
    }

    private static float[] createChickenColor(DyeColor dyeColor, Boolean darken) {
        if (dyeColor == DyeColor.WHITE) {
            if (darken) {
                return new float[]{0.9019608F, 0.9019608F, 0.9019608F};
            } else {
                return new float[]{1.0F, 1.0F, 1.0F};
            }
        } else {
            int[] aint = unpackColor(dyeColor.getColorValue());
            float[] afloat = new float[] {aint[0]/255.0f, aint[1]/255.0f, aint[2]/255.0f};
            float f = 0.75F;
            if (!darken) {
                f = 1.0F;
            }
            return new float[]{afloat[0] * f, afloat[1] * f, afloat[2] * f};
        }
    }

    public void generateNewColor() {
        /*int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        this.setColor(packColor(r, g, b));*/
        int colorId = random.nextInt(DyeColor.values().length-1);
        float[] color = createChickenColor(DyeColor.byId(colorId), this.random.nextBoolean()); //SheepEntity.getColorArray(DyeColor.byId(colorId));
        int r = (int) (color[0] * 255.0f);
        int g = (int) (color[1] * 255.0f);
        int b = (int) (color[2] * 255.0f);
        this.setColor(packColor(r, g, b));
        //LOGGER.log(Level.INFO, "Setting random color to: colorId: "+colorId+", color: ("+r+", "+g+", "+b+")");
    }

    @Override
    public void tick() {
        super.tick();
        int default_color = packColor(127, 128, 129);
        if (!this.level.isClientSide) {
            if (this.getColor() == default_color) {
                //LOGGER.log(Level.INFO, "Current color: "+this.getColor()+", default_color: "+default_color);
                this.generateNewColor();
                //LOGGER.log(Level.INFO, "Set random color to: ("+r+", "+g+", "+b+")"+", current packed color: "+this.getColor());
                int[] c = unpackColor(this.getColor());
                int r1 = c[0];
                int g1 = c[1];
                int b1 = c[2];
                //LOGGER.log(Level.INFO, "Which unpacks to: "+this.getColor()+" : ("+r1+", "+g1+", "+b1+")");
            }
            if (this.isPuppet()) {
                double x = this.masterEntity.getX();
                double y = this.masterEntity.getY();
                double z = this.masterEntity.getZ();
                this.setPos(x, y, z);
            }
        }
    }

    @Override
    public void onSyncedDataUpdated(@Nonnull DataParameter<?> parameter) {
        if (parameter.equals(COLOR)) {
            int[] c = unpackColor(this.getColor());
            int r = c[0];
            int g = c[1];
            int b = c[2];
            //LOGGER.log(Level.INFO, "Updated color to: ("+r+", "+g+", "+b+")");
        }
        super.onSyncedDataUpdated(parameter);
    }

    @Nonnull
    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeInt(this.getColor());
    }

    @Override
    public void readSpawnData(PacketBuffer buffer) {
        this.entityData.set(COLOR, buffer.readInt());
    }

    @Override
    public double getMyRidingOffset() {
        if (this.masterEntity instanceof PlayerEntity || this.getVehicle() instanceof PlayerEntity) {
            return super.getMyRidingOffset() + 0.5D;
        }
        return super.getMyRidingOffset();
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(ItemInit.DYED_CHICKEN_SPAWN_EGG);
    }

    public void setOnGround(boolean onGround) { //warning: this must be used very carefully
        this.onGround = onGround;
    }
}