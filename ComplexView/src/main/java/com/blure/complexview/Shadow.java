package com.blure.complexview;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;

/**
 * A simple class for generating shadows as a drawable
 *
 * @author BluRe
 */

public class Shadow {
    private int spread;
    private int opacity;
    private String color;
    private int shape;
    private float[] radii;
    private Drawable shadow;

    /**
     * @param spread  Strength of shadow
     * @param opacity Opacity of the shadow, from 0 to 255
     * @param color   String representation of the color of the shadow
     * @param shape   Shape of the shadow
     * @param radii   bottomLeftRadius x and y, bottomRightRadius x and y, topLeftRadius x and y, and topRightRadius x and y, all supplied as a float array.
     */
    public Shadow(int spread, int opacity, String color, int shape, float[] radii) {
        this.radii = radii;
        this.spread = spread;
        this.opacity = opacity;
        this.color = color;
        this.shape = shape;
        init();
    }


    private void init() {
        int hex = 0;
        spread *= 14;
        Drawable[] gradientDrawables = new Drawable[spread];
        int padding = 1;
        for (int i = 0, step = 0; i < spread; ++i) {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(shape);
            String str = Integer.toHexString(hex);
            if (hex < 16) {
                str = "0" + str;
            }
            str += color.replace("#", "");
            int col = Color.parseColor("#" + str);
            drawable.setColor(col);
            drawable.setCornerRadii(radii);
            InsetDrawable insetDrawable = new InsetDrawable(drawable, padding, padding, padding, padding);
            gradientDrawables[i] = insetDrawable;
            if (step == spread / 14) {
                ++hex;
                step = 0;
            }
            ++step;
        }
        shadow = new LayerDrawable(gradientDrawables);
        shadow.setAlpha(opacity);
    }

    Drawable getShadow() {
        return shadow;
    }
}
