package com.blure.complexview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

/**
 * A view that can be manipulated to produce different UI effects
 *
 * @author BluRe
 */

public class ComplexView extends RelativeLayout implements View.OnClickListener, Animation.AnimationListener {
    private float amplitude;
    private OnClickListener pClick;
    private float frequency;
    private boolean init = true;
    private Animation animation;
    private int animationDuration;
    private float toXScale;
    private float fromXScale;
    private float toYScale;
    private float fromYScale;
    private float pivotX;
    private float pivotY;
    private int onclickColor;
    private int color;
    private GradientDrawable gd = new GradientDrawable();
    private float rad;
    private float topRightRadius;
    private float topLeftRadius;
    private float bottomRightRadius;
    private float bottomLeftRadius;
    private int shape;
    private View view;
    private boolean animate;
    private int[] colors = new int[3];
    private boolean transferClick;
    private boolean clickAfterAnimation;
    private RelativeLayout main;
    private String gradientType = "linear";
    private String gradientAngle = "TOP_BOTTOM";
    private boolean interpolate;
    private boolean selfClickable;
    private boolean fromChild;
    private Interpolator interpolator;
    private boolean first = true;
    private Shadow shadow;
    private Shadow.Position shadowPosition = Shadow.Position.CENTER;


    public ComplexView(Context context) {
        super(context);
        setBackground(gd);
    }

    public ComplexView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }


    private void init(AttributeSet set) {
        TypedArray ta = getContext().obtainStyledAttributes(set, R.styleable.ComplexView);
        boolean shadow = ta.getBoolean(R.styleable.ComplexView_shadow, false);
        String shadowColor = ta.getString(R.styleable.ComplexView_shadowColor);
        if (shadowColor == null) {
            shadowColor = "000000";
        }
        transferClick = ta.getBoolean(R.styleable.ComplexView_clickTransferable, false);
        selfClickable = ta.getBoolean(R.styleable.ComplexView_selfClickable, true);
        gradientAngle = ta.getString(R.styleable.ComplexView_gradientAngle);
        clickAfterAnimation = ta.getBoolean(R.styleable.ComplexView_clickAfterAnimation, true);
        if (gradientAngle == null) gradientAngle = "TOP_BOTTOM";
        int gradientCenterColor = ta.getColor(R.styleable.ComplexView_gradientCenterColor, 0);
        int gradientEndColor = ta.getInteger(R.styleable.ComplexView_gradientEndColor, 0);
        int gradientStartColor = ta.getInt(R.styleable.ComplexView_gradientStartColor, 0);
        colors[0] = gradientStartColor;
        colors[1] = gradientCenterColor;
        colors[2] = gradientEndColor;
        gradientType = ta.getString(R.styleable.ComplexView_gradientType);
        if (gradientType == null) {
            gradientType = "linear";
        }
        int spread = ta.getInt(R.styleable.ComplexView_shadowSpread, 1);
        int shadowAlpha = ta.getInt(R.styleable.ComplexView_shadowAlpha, 255);
        animationDuration = ta.getInteger(R.styleable.ComplexView_animationDuration, 2000);
        toXScale = ta.getFloat(R.styleable.ComplexView_toXScale, 1.0f);
        fromXScale = ta.getFloat(R.styleable.ComplexView_fromXScale, .3f);
        toYScale = ta.getFloat(R.styleable.ComplexView_toYScale, 1.0f);
        fromYScale = ta.getFloat(R.styleable.ComplexView_fromYScale, .3f);
        pivotX = ta.getFloat(R.styleable.ComplexView_pX, -1);
        pivotY = ta.getFloat(R.styleable.ComplexView_pY, -1);
        animate = ta.getBoolean(R.styleable.ComplexView_animate, false);
        rad = ta.getDimension(R.styleable.ComplexView_radius, 0);
        topRightRadius = ta.getDimension(R.styleable.ComplexView_topRightRadius, 0);
        topLeftRadius = ta.getDimension(R.styleable.ComplexView_topLeftRadius, 0);
        bottomRightRadius = ta.getDimension(R.styleable.ComplexView_bottomRightRadius, 0);
        bottomLeftRadius = ta.getDimension(R.styleable.ComplexView_bottomLeftRadius, 0);
        color = ta.getColor(R.styleable.ComplexView_color, android.R.attr.colorBackground);
        onclickColor = ta.getColor(R.styleable.ComplexView_onclickColor, -1);
        interpolate = ta.getBoolean(R.styleable.ComplexView_interpolate, false);
        amplitude = ta.getFloat(R.styleable.ComplexView_amplitude, 1.0f);
        frequency = ta.getFloat(R.styleable.ComplexView_frequency, .3f);
        String sh = ta.getString(R.styleable.ComplexView_shape);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (first && animate) {
                    animation = new ScaleAnimation(fromXScale, toXScale, fromYScale, toYScale, pivotX == -1 ? (float) getWidth() / 2 : pivotX, pivotY == -1 ? (float) getHeight() / 2 : pivotY);
                    animation.setDuration(animationDuration);
                    animation.setAnimationListener(ComplexView.this);
                    if (interpolate) {
                        Interpolator interpolator = new DefaultInterpolator();
                        animation.setInterpolator(interpolator);
                    }
                    first = false;
                }
            }
        });


        if (sh != null) {
            switch (sh) {
                case "oval":
                    shape = GradientDrawable.OVAL;
                    break;
                case "ring":
                    shape = GradientDrawable.RING;
                    break;
                case "line":
                    shape = GradientDrawable.LINE;
                    break;
            }
        }
        String sPosition = ta.getString(R.styleable.ComplexView_shadowPosition);
        if (sPosition != null) {
            switch (sPosition) {
                case "top":
                    shadowPosition = Shadow.Position.TOP;
                    break;
                case "left":
                    shadowPosition = Shadow.Position.LEFT;
                    break;
                case "right":
                    shadowPosition = Shadow.Position.RIGHT;
                    break;
                case "bottom":
                    shadowPosition = Shadow.Position.BOTTOM;
                    break;
            }
        }

        setShape(shape);
        if (!isEmpty(colors)) {
            gd.setColors(colors);
        } else {
            gd.setColor(color);

        }
        setGradientType(gradientType);
        setGradientAngle(gradientAngle);
        float[] initRad = new float[]{rad, rad, rad, rad, rad, rad, rad, rad};
        float[] radius = new float[]{topLeftRadius, topLeftRadius, topRightRadius, topRightRadius, bottomRightRadius, bottomRightRadius, bottomLeftRadius, bottomLeftRadius};
        float[] radToSet = rad == 0 ? radius : initRad;
        setCornerRadii(radToSet);
        if (shadow) {
            this.shadow = new Shadow(spread, shadowAlpha, shadowColor, shape, radToSet, shadowPosition);
            setBackground(this.shadow.getShadow());
        } else
            setBackground(gd);
        setOnClickListener(this);
        ta.recycle();
    }

    /**
     * Set the shape of ComplexView.
     *
     * @param shape Can be either GradientDrawable.OVAL, GradientDrawable.RECTANGLE, GradientDrawable.RING or GradientDrawable.LINE
     */
    public void setShape(int shape) {
        gd.setShape(shape);
    }

    /**
     * Returns if onclick is set to be performed before or after animation
     */
    public boolean isClickAfterAnimation() {
        return clickAfterAnimation;
    }

    /**
     * When set to true, animation completes before onclick is performed
     */
    public void setClickAfterAnimation(boolean afterAnimation) {
        this.clickAfterAnimation = afterAnimation;
    }

    public float getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(float amplitude) {
        this.amplitude = amplitude;
    }

    public float getFrequency() {
        return frequency;
    }

    public void setFrequency(float frequency) {
        this.frequency = frequency;
    }

    /**
     * Returns color set when ComplexView is clicked
     */
    public int getOnclickColor() {
        return this.onclickColor;
    }

    /**
     * Sets the color shown when a ComplexView is clicked.
     * Works only when an animation is set.
     *
     * @param color onclick color
     */
    
    public void setOnclickColor(int color) {
        this.onclickColor = color;
    }

    /**
     * Returns the duration of the animation set.
     */
    public int getAnimationDuration() {
        return animationDuration;
    }


    /**
     * Sets the duration of the default animation.
     *
     * @param animationDuration duration of animation in millis.
     */
    public void setAnimationDuration(int animationDuration) {
        this.animationDuration = animationDuration;
    }

    /**
     * Returns the radius of ComplexView
     */
    public float getRadius() {
        return this.rad;
    }

    /**
     * Sets the radius ComplexView
     *
     * @param radius Preferred radius
     */
    public void setRadius(float radius) {
        this.rad = radius;
        gd.setCornerRadius(radius);
    }

    /**
     * @return The shadow Object applied to this ComplexView if any
     * @since 1.1
     */

    public Shadow getShadow() {
        return shadow;
    }

    /**
     * Converts ComplexView into a shadow object.
     * This is to be used as a parent to another view.
     *
     * @param shadow An initialized shadow object.
     */
    public void setShadow(Shadow shadow) {
        setBackground(shadow.getShadow());
    }


    public float getToXScale() {
        return toXScale;
    }

    public void setToXScale(float toXScale) {
        this.toXScale = toXScale;
    }

    public float getToYScale() {
        return toYScale;
    }

    public void setToYScale(float toYScale) {
        this.toYScale = toYScale;
    }

    public void setFromXScale(float fromXScale) {
        this.fromXScale = fromXScale;
    }

    public void setFromYScale(float fromYScale) {
        this.fromYScale = fromYScale;
    }

    public float getTopRightRadius() {
        return topRightRadius;
    }

    public void setTopRightRadius(float topRightRadius) {
        this.topRightRadius = topRightRadius;
    }

    public Interpolator getInterpolator() {
        return interpolator;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    public float getTopLeftRadius() {
        return topLeftRadius;
    }

    public void setTopLeftRadius(float topLeftRadius) {
        this.topLeftRadius = topLeftRadius;
    }

    public float getBottomRightRadius() {
        return bottomRightRadius;
    }

    public void setBottomRightRadius(float bottomRightRadius) {
        this.bottomRightRadius = bottomRightRadius;
    }

    /**
     * Sets gradient color of ComplexView
     */
    public void setGradientColor(int[] colors) {
        gd.setColors(colors);
    }

    /**
     * Since our underlying technique of achieving curved edges is based on setting the background drawable resource to a gradient drawable,
     * we simply can not set another background during runtime.
     */
    @Override
    public void setBackgroundResource(int resid) {
        throw new RuntimeException("setBackgroundResource not supported in ComplexView");
    }


    /**
     * Returns if ComplexView's parent can detect clicks made to its child.
     */
    public boolean isClickTransferable() {
        return transferClick;
    }

    /**
     * Sets if ComplexView can send clicks to its parent.
     * Set true if the parent has work to do.
     */
    public void setClickTransferable(boolean transfer) {
        this.transferClick = transfer;
    }

    public float getBottomLeftRadius() {
        return bottomLeftRadius;
    }

    /**
     * Sets the bottom left radius value of the ComplexView
     */
    public void setBottomLeftRadius(float bottomLeftRadius) {
        this.bottomLeftRadius = bottomLeftRadius;
    }

    private boolean isEmpty(int[] ints) {
        for (int i : ints) {
            if (i != 0) return false;
        }
        return true;
    }

    public void setColor(int color) {
        gd.setColor(color);
    }

    /**
     * Sets the corner radius of ComplexView
     *
     * @param radii Must be of 4 float values comprising of topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius
     */
    public void setCornerRadii(float[] radii) {
        gd.setCornerRadii(radii);
    }


    public void setInterpolate(boolean interpolate) {
        this.interpolate = interpolate;
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        if (init) {
            super.setOnClickListener(l);
            init = false;
            return;
        }
        pClick = l;
    }

    @Override
    public void onClick(View v) {
        if (!selfClickable && !fromChild) {
            return;
        }
        ViewParent parent = getParent();
        if (parent instanceof ComplexView && transferClick) {
            ComplexView complexView = (ComplexView) parent;
            complexView.fromChild = true;
            complexView.onClick(complexView);
        }

        if (onclickColor != -1) {
            gd.setColor(onclickColor);
        }
        if (animate) {
            if (!clickAfterAnimation) {
                if (pClick != null) pClick.onClick(v);
            }
            startAnimation(animation);
            return;
        }
        if (pClick != null) pClick.onClick(v);
        fromChild = false;
    }


    public void startAnimation() {
        startAnimation(animation);
    }

    /**
     * We can not change background color due to our manipulations
     */
    @Override
    public void setBackgroundColor(int color) {
        throw new RuntimeException("setBackgroundColor not supported!");
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        gd.setColor(color);
        if (clickAfterAnimation && pClick != null) {
            pClick.onClick(view);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public String getGradientType() {
        return gradientType;
    }

    /**
     * @param gradientType The gradient type to used.
     */
    public void setGradientType(String gradientType) {
        int gradient = GradientDrawable.LINEAR_GRADIENT;
        switch (gradientType) {
            case "sweep":
                gradient = GradientDrawable.SWEEP_GRADIENT;
                break;
            case "radial":
                gradient = GradientDrawable.RADIAL_GRADIENT;
                break;
        }
        this.gradientType = gradientType;
        gd.setGradientType(gradient);
    }

    /**
     * @return returns the gradient angle set
     */
    public String getGradientAngle() {
        return gradientAngle;
    }

    /**
     * @param gradientAngle sets the angle used by the gradient
     */
    private void setGradientAngle(String gradientAngle) {
        this.gradientAngle = gradientAngle;
        gd.setOrientation(GradientDrawable.Orientation.valueOf(gradientAngle.toUpperCase()));
    }

    /**
     * Returns a boolean value indicating if ComplexView can be clicked as a parent view.
     * If false, only the child's clicks are detected.
     * please refer to {@link #setSelfClickable(boolean)}
     */
    public boolean isSelfClickable() {
        return selfClickable;
    }

    /**
     * @param selfClickable Sets if ComplexView as a parent can detect clicks.
     *                      This is usually set to true to detect clicks on visible areas of the view as a parent.
     *                      Visible areas may include padding or margin created by its child or by effects such as shadows.
     */
    public void setSelfClickable(boolean selfClickable) {
        this.selfClickable = selfClickable;
    }

    /**
     * Default interpolator used if {@link #interpolate} is true
     */
    private class DefaultInterpolator implements Interpolator {
        @Override
        public float getInterpolation(float time) {
            return (float) (-1 * Math.pow(Math.E, -time / amplitude) * Math.cos(frequency * time) + 1);
        }
    }

}
