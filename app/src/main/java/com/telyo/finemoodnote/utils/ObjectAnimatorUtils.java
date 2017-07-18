package com.telyo.finemoodnote.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.BounceInterpolator;

/**
 * Created by Administrator on 2017/7/18.
 */

public class ObjectAnimatorUtils {

    public static final String alpha = "alpha";
    public static final String scaleX = "scaleX";
    public static final String scaleY = "scaleY";
    public static final String translate = "translate";
    public static final String rotate = "rotate";

    public static AnimatorSet doScale(View v, float... values){
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator oScaleX = ObjectAnimator.ofFloat(v,scaleX,values);
        ObjectAnimator oScaleY = ObjectAnimator.ofFloat(v,scaleY,values);
        animatorSet.setDuration(500);
        animatorSet.setInterpolator(new BounceInterpolator());
        animatorSet.play(oScaleX).with(oScaleY);
        return animatorSet;
    }
}
