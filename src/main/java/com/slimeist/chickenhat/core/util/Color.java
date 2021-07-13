package com.slimeist.chickenhat.core.util;

import com.slimeist.chickenhat.ChickenHat;
import org.apache.logging.log4j.Level;

public class Color {
    private float[] color;

    public Color() {
        this.color = new float[]{1.0f, 1.0f, 1.0f};
    }

    public Color(float r, float g, float b) {
        this.color = new float[]{r, g, b};
    }

    public Color(float[] color) {
        this.color = color.clone();
    }

    public Color(int color) {
        this.setColor(color);
    }

    public void setColor(float[] color) {
        this.color = color.clone();
    }

    public void setColor(float r, float g, float b) {
        this.color = new float[]{r, g, b};
    }

    public float[] getColor() {
        return this.color;
    }

    public void setRed(float r) {
        this.color[0] = r;
    }

    public void setGreen(float g) {
        this.color[1] = g;
    }

    public void setBlue(float b) {
        this.color[2] = b;
    }

    public float getRed() {
        return this.color[0];
    }

    public float getGreen() {
        return this.color[1];
    }

    public float getBlue() {
        return this.color[2];
    }

    public void setColor(int packedColor) {
        float r = (float)(packedColor >> 16 & 255) / 255.0F;
        float g = (float)(packedColor >> 8 & 255) / 255.0F;
        float b = (float)(packedColor & 255) / 255.0F;
        this.setColor(r, g, b);
    }

    public int getPackedColor() {

        int r = (int)this.getRed()*255;
        int g = (int)this.getBlue()*255;
        int b = (int)this.getBlue()*255;

        int packedColor = r<<16 | g<<8 | b;
        ChickenHat.LOGGER.log(Level.INFO, "Packing color: ("+r+", "+g+", "+b+")"+", to: "+packedColor);

        return packedColor;
    }

    public static int packColor(float r, float g, float b) {
        Color temp = new Color(r, g, b);
        return temp.getPackedColor();
    }

    public static int packColor(float[] color) {
        Color temp = new Color(color);
        return temp.getPackedColor();
    }

    public static float[] unpackColor(int color) {
        Color temp = new Color(color);
        return temp.getColor();
    }
}
