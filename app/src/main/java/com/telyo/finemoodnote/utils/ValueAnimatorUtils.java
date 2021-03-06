package com.telyo.finemoodnote.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by Administrator on 2017/7/18.
 */

public class ValueAnimatorUtils {

    public static final String alpha = "alpha";
    public static final String scaleX = "scaleX";
    public static final String scaleY = "scaleY";
    public static final String translateY = "translationY";
    public static final String translateX = "translationX";
    public static final String rotate = "rotation";

    public static ValueAnimator doScale(final View v, final boolean isVisible, float... values) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(values);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                v.setScaleX(value);
                v.setScaleY(value);

            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (isVisible) {
                    v.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (!isVisible) {
                    v.setVisibility(View.GONE);
                }
            }
        });
        valueAnimator.setDuration(500);
        valueAnimator.setInterpolator(new BounceInterpolator());
        return valueAnimator;
    }

    public static ObjectAnimator doTranslate(final View view, float... values) {

        ObjectAnimator animTranslateY = ObjectAnimator.ofFloat(view, translateY, values);
        animTranslateY.setDuration(500);
        animTranslateY.setInterpolator(new LinearInterpolator());
        return animTranslateY;
    }
    public static ObjectAnimator doAlpha(final View view, float... values) {
        ObjectAnimator animAlpha = ObjectAnimator.ofFloat(view, alpha, values);
        animAlpha.setDuration(500);
        animAlpha.setInterpolator(new LinearInterpolator());
        return animAlpha;
    }
}
