package com.telyo.finemoodnote.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.telyo.finemoodnote.R;

/**
 * Created by Administrator on 2017/7/24.
 */

public class FragmentPlanDetail extends Fragment {
    private static FragmentPlanDetail mFragmentPlanDetail;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_plan_detail,container,false);
        initView(v);
        return v;
    }

    private void initView(View v) {

    }
    public static FragmentPlanDetail getInstance(){
        if (mFragmentPlanDetail == null){
            mFragmentPlanDetail = new FragmentPlanDetail();
        }
        return mFragmentPlanDetail;
    }

}
