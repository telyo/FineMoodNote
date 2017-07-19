package com.telyo.finemoodnote.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/7/19.
 */

public class ScrollerControlRecycler extends RecyclerView {
    public ScrollerControlRecycler(Context context) {
        this(context,null);
    }

    public ScrollerControlRecycler(Context context,AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ScrollerControlRecycler(Context context,AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //请求父控件不要拦截Touch附件 为 true
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

}
