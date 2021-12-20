package com.slimeist.extragamerules.core;

import net.minecraft.world.GameRules;

public class Rule {
    protected final String name;
    protected final GameRules.Category category;
    protected GameRules.RuleKey<?> key;

    public Rule(String name, GameRules.Category category) {
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return this.name;
    }

    public GameRules.Category getCategory() {
        return this.category;
    }

    public GameRules.RuleKey<?> getKey() {
        return key;
    }

    public void setKey(GameRules.RuleKey<?> key) {
        this.key = key;
    }

    public static class IntegerRule extends Rule {

        protected final int defaultValue;

        public IntegerRule(String name, GameRules.Category category, int defaultValue) {
            super(name, category);
            this.defaultValue = defaultValue;
        }

        @Override
        @SuppressWarnings("unchecked")
        public GameRules.RuleKey<GameRules.IntegerValue> getKey() {
            return (GameRules.RuleKey<GameRules.IntegerValue>) super.getKey();
        }

        public int getDefault() {
            return this.defaultValue;
        }
    }

    public static class BooleanRule extends Rule {

        protected final boolean defaultValue;

        public BooleanRule(String name, GameRules.Category category, boolean defaultValue) {
            super(name, category);
            this.defaultValue = defaultValue;
        }

        @Override
        @SuppressWarnings("unchecked")
        public GameRules.RuleKey<GameRules.BooleanValue> getKey() {
            return (GameRules.RuleKey<GameRules.BooleanValue>) super.getKey();
        }

        public boolean getDefault() {
            return this.defaultValue;
        }
    }
}
