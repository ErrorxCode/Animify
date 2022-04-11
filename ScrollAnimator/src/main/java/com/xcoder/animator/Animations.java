package com.xcoder.animator;

import android.animation.TimeInterpolator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

public class Animations {
    public static final String ANIMATION_ALPHA = "alpha";
    public static final String ANIMATION_SLIDE_FROM_TOP = "slide4top";
    public static final String ANIMATION_SLIDE_FROM_BOTTOM = "slide4bottom";
    public static final String ANIMATION_SLIDE_FROM_LEFT = "slide4left";
    public static final String ANIMATION_SLIDE_FROM_RIGHT = "slide4right";
    public static final String ANIMATION_ROTATE = "rotate";
    public static final String ANIMATION_DROP_FROM_TOP = "drop4top";
    public static final String ANIMATION_DROP_FROM_BOTTOM = "drop4bottom";

    public static final int DURATION_FAST = 500;
    public static final int DURATION_SLOW = 2000;
    public static final int DURATION_NORMAL = 1000;
    public static final int DURATION_LONG = 3000;

    public static final TimeInterpolator INTERPOLATOR_FAST_OUT_SLOW = new FastOutSlowInInterpolator();
    public static final TimeInterpolator INTERPOLATOR_OVERSHOOT = new OvershootInterpolator();
    public static final TimeInterpolator INTERPOLATOR_ANTICIPATE = new AnticipateInterpolator();
    public static final TimeInterpolator INTERPOLATOR_ACCELERATE = new AccelerateInterpolator();
    public static final TimeInterpolator INTERPOLATOR_DECELERATE = new DecelerateInterpolator();
    public static final TimeInterpolator INTERPOLATOR_ACCELERATE_DECELERATE = new AccelerateDecelerateInterpolator();
    public static final TimeInterpolator INTERPOLATOR_BOUNCE = new BounceInterpolator();
}
