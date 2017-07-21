package com.telyo.finemoodnote.views;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.telyo.finemoodnote.R;

/**
 * Created by Administrator on 2017/7/21.
 */

public class DialogView extends Dialog {
    public DialogView(Context context, int styleId, int layoutId) {
        this(context, styleId, layoutId, Gravity.CENTER, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
    }

    public DialogView(Context context, int styleId, int layoutId, int gravity, int width, int height, int animId) {
        super(context, styleId);
        setContentView(layoutId);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = gravity;
        params.width = width;
        params.height = height;
        params.windowAnimations = animId;
        window.setAttributes(params);


    }
    public DialogView(Context context, int styleId, int layoutId, int gravity, int width, int height) {
        this(context, styleId, layoutId, gravity, width, height, R.style.pop_anim_style);
    }
}
