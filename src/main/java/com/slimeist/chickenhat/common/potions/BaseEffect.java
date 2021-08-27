package com.slimeist.chickenhat.common.potions;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;

public class BaseEffect extends Effect { //derived from Tinker's Construct by slimeknight

    private final boolean show;

    public BaseEffect(EffectType typeIn, boolean show) {
        this(typeIn, 0xffffff, show);
    }

    public BaseEffect(EffectType typeIn, int color, boolean show) {
        super(typeIn, color);
        this.show = show;
    }

    @Override
    public boolean shouldRender(EffectInstance effect) {
        return this.show;
    }

    @Override
    public boolean shouldRenderInvText(EffectInstance effect) {
        return this.show;
    }

    @Override
    public boolean shouldRenderHUD(EffectInstance effect) {
        return this.show;
    }

    public EffectInstance apply(LivingEntity entity, int duration) {
        return this.apply(entity, duration, 0);
    }

    public EffectInstance apply(LivingEntity entity, int duration, int level) {
        EffectInstance effect = new EffectInstance(this, duration, level, false, false);
        entity.addEffect(effect);
        return effect;
    }

    public int getLevel(LivingEntity entity) {
        EffectInstance effect = entity.getEffect(this);
        if (effect!=null) {
            return effect.getAmplifier();
        }
        return -1;
    }
}
