package com.telyo.finemoodnote.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/26.
 */

public class SetTimeView extends LinearLayout {
    private ViewPager mHourViewPager;
    private TextView mHourTv;

    private ViewPager mMinViewPager;
    private TextView mMinTv;

    private ViewPager mSecondViewPager;
    private TextView mSecondTv;

    private List<String> mHour = new ArrayList<>();
    private List<String> mMin= new ArrayList<>();
    private List<String> mSecond = new ArrayList<>();

    public SetTimeView(Context context) {
        this(context,null);

    }

    public SetTimeView(Context context,AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SetTimeView(Context context,AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
