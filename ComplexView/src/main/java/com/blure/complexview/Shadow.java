package com.blure.complexview;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;

import static android.graphics.drawable.GradientDrawable.Orientation;

/**
 * A simple class for generating shadows for {@link ComplexView}
 *
 * @author BluRe
 */

public class Shadow {
    private int spread;
    private int opacity;
    private String color;
    private int shape;
    private float[] radius;
    private LayerDrawable shadow;
    private Position position;

    /**
     * @param spread   Strength of shadow
     * @param opacity  Opacity of the shadow, from 0 to 255
     * @param color    String representation of the color of the shadow
     * @param shape    Shape of the shadow
     * @param radius   bottomLeftRadius x and y, bottomRightRadius x and y, topLeftRadius x and y, and topRightRadius x and y, all supplied as a float array.
     * @param position @since 1.1 (see {@link Position}) the position of the shadow
     */
    public Shadow(int spread, int opacity, String color, int shape, float[] radius, Position position) {
        this.radius = radius;
        this.spread = spread;
        this.opacity = opacity;
        this.color = color.replace("#", "");
        this.shape = shape;
        this.position = position;
        init();
    }


    private void init() {
        int hex = 0;
        spread *= 14;
        InsetDrawable[] gradientDrawables = new InsetDrawable[spread];
        int padding = 1;
        boolean center = position == Position.CENTER;
        Orientation orientation = getOrientation(position);

        for (int i = 0, step = 0; i < spread; ++i) {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(shape);
            drawable.setGradientType(shape);
            String str = Integer.toHexString(hex);
            if (hex < 16) {
                str = "0" + str;
            }
            str += color;
            int col = Color.parseColor("#" + str);
            if (!center) {
                drawable.setOrientation(orientation);
                int[] colors = {col, Color.parseColor("#00ffffff")};
                drawable.setColors(colors);
            } else {
                drawable.setColor(col);
            }
            drawable.setCornerRadii(radius);
            gradientDrawables[i] = new InsetDrawable(drawable, padding, padding, padding, padding);
            ;
            if (step == spread / 14) {
                ++hex;
                step = 0;
            }
            ++step;
        }
        shadow = new LayerDrawable(gradientDrawables);
        shadow.setAlpha(opacity);
    }

    /**
     * @return The positioning of the shadow
     * @since 1.1
     */
    public Position getShadowPosition() {
        return position;
    }


    private Orientation getOrientation(Position position) {
        Orientation orientation = Orientation.TOP_BOTTOM;
        switch (position) {
            case BOTTOM:
                orientation = Orientation.BOTTOM_TOP;
                break;
            case LEFT:
                orientation = Orientation.LEFT_RIGHT;
                break;
            case RIGHT:
                orientation = Orientation.RIGHT_LEFT;
                break;
        }
        return orientation;
    }

    Drawable getShadow() {
        return shadow;
    }

    /**
     * Controls how the shadow is positioned to ComplexViews.
     */
    public enum Position {
        /**
         * Positions the shadow to the center
         * This is the default behaviour of ComplexView shadows.
         */
        CENTER,
        /**
         * Positions the shadow to the right
         */
        RIGHT,
        /**
         * Positions the shadow to the left
         */
        LEFT,

        /**
         * Positions the shadow to the top
         */
        TOP,
        /**
         * Positions the shadow to the bottom
         */
        BOTTOM;
    }
}
