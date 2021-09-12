package com.slimeist.chickenhat.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.slimeist.chickenhat.client.render.model.DyedChickenModel;
import com.slimeist.chickenhat.common.entities.DyedChickenEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class DyedChickenRenderer extends MobRenderer<DyedChickenEntity, DyedChickenModel<DyedChickenEntity>> {
    private static final ResourceLocation CHICKEN_LOCATION = new ResourceLocation("textures/entity/chicken.png");

    public DyedChickenRenderer(EntityRendererManager p_i47211_1_) {
        super(p_i47211_1_, new DyedChickenModel<>(), 0.0F);
        this.shadowStrength = 0.0F;
        this.shadowRadius = 0.0F;
        shadowStrength = 0.0F;
        shadowRadius = 0.0F;
    }

    public ResourceLocation getTextureLocation(DyedChickenEntity p_110775_1_) {
        return CHICKEN_LOCATION;
    }

    protected float getBob(DyedChickenEntity p_77044_1_, float p_77044_2_) {
        float f = MathHelper.lerp(p_77044_2_, p_77044_1_.oFlap, p_77044_1_.flap);
        float f1 = MathHelper.lerp(p_77044_2_, p_77044_1_.oFlapSpeed, p_77044_1_.flapSpeed);
        return (MathHelper.sin(f) + 1.0F) * f1;
    }

    public static int packColor(int r, int g, int b) {
        return new Color(r, g, b).getRGB();
    }

    public static int[] unpackColor(int rgb) {
        Color c = new Color(rgb);
        return new int[]{c.getRed(), c.getGreen(), c.getBlue()};
    }

    public void render(DyedChickenEntity entity, float p_225623_2_, float partialTicks, MatrixStack p_225623_4_, IRenderTypeBuffer p_225623_5_, int p_225623_6_) {
        int color = entity.getColor();

        int[] c = unpackColor(color);

        if (entity.hasCustomName()) {
            if ("slimeist_".equals(entity.getName().getContents())) { //modern rainbow
                int cycle_length = 400; //match cycle length for sheep (16 wool colors, 25 ticks each)
                float hue = (entity.tickCount+partialTicks+(entity.getId()*25)) % cycle_length;
                hue /= cycle_length;

                /*int ticks = 25;
                int stages = DyeColor.values().length;
                int i = entity.tickCount / ticks + entity.getId();
                int nowId = i % stages;
                int nextId = (i + 1) % stages;
                float between = ((float)(entity.tickCount % ticks) + partialTicks) / (float)ticks;
                float hue = nowId * (1.0F - between) + nextId * between;*/
                int overridingColor = Color.HSBtoRGB(hue, 1.0f, 1.0f);
                c = unpackColor(overridingColor);
                //ChickenHat.LOGGER.log(Level.INFO, "Thanks jeb_");
                //ChickenHat.LOGGER.log(Level.INFO, "tickCount: "+entity.tickCount+", hue: "+hue);
            }
            else if ("jeb_".equals(entity.getName().getContents())) { //classic jeb_ sheep rainbow
                // Copied from net.minecraft.client.renderer.entity.layers.SheepWoolLayer
                int cycle = 25;
                int i = entity.tickCount / cycle + entity.getId();
                int j = DyeColor.values().length;
                int nowId = i % j;
                int nextId = (i + 1) % j;
                float f3 = ((float)(entity.tickCount % cycle) + partialTicks) / (float)cycle;
                float[] nowColor = SheepEntity.getColorArray(DyeColor.byId(nowId));
                float[] nextColor = SheepEntity.getColorArray(DyeColor.byId(nextId));
                float r1 = nowColor[0] * (1.0F - f3) + nextColor[0] * f3;
                float g1 = nowColor[1] * (1.0F - f3) + nextColor[1] * f3;
                float b1 = nowColor[2] * (1.0F - f3) + nextColor[2] * f3;
                int r2 = (int)(r1*255);
                int g2 = (int)(g1*255);
                int b2 = (int)(b1*255);
                c = new int[]{r2, g2, b2};
            } else if ("creeper".equals(entity.getName().getContents())) { //disguise with grass color!
                double divisor = 1.514690406168031;
                c = unpackColor(BiomeColors.getAverageGrassColor(entity.level, entity.blockPosition()));
                c[0] = (int) (c[0]/divisor);
                c[1] = (int) (c[1]/divisor);
                c[2] = (int) (c[2]/divisor);
            }
        }

        float r = (float) c[0]/255.0f;
        float g = (float) c[1]/255.0f;
        float b = (float) c[2]/255.0f;

        this.model.setColor(r, g, b);
        super.render(entity, p_225623_2_, partialTicks, p_225623_4_, p_225623_5_, p_225623_6_);
        this.model.setColor(1.0F, 1.0F, 1.0F);
    }
}