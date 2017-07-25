package com.telyo.finemoodnote.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.telyo.finemoodnote.R;
import com.telyo.finemoodnote.entity.RecyclerDescribe;
import com.telyo.finemoodnote.entity.RecyclerPlans;
import com.telyo.finemoodnote.views.CircleProgressView;

import java.util.ArrayList;
import java.util.List;

import static com.telyo.finemoodnote.utils.Constants.PLAN_DESCRIBE;
import static com.telyo.finemoodnote.utils.Constants.SHOW_PLAN_REQUEST_CONTENT;

/**
 * Created by Administrator on 2017/7/24.
 */

public class FragmentPlanData extends Fragment {
    private static FragmentPlanData mFragmentPlanData;
    private RecyclerPlans mPlan;
    private List<RecyclerDescribe> describes = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initData();
        View v = inflater.inflate(R.layout.fragment_plan_data, container, false);
        initView(v);
        return v;
    }

    private void initData() {
        Bundle bundle = getArguments();
        mPlan = bundle.getParcelable(SHOW_PLAN_REQUEST_CONTENT);
        describes = bundle.getParcelableArrayList(PLAN_DESCRIBE);
    }

    private void initView(View v) {
        CircleProgressView progressView = (CircleProgressView) v.findViewById(R.id.progressView);
        progressView.setProgress(80);
        progressView.setFocusChanges();
        //progressView.setProgress((int) (getFinishedCount() * 100 / (describes.size())));
        TextView tv_date = (TextView) v.findViewById(R.id.tv_date);
        TextView tv_plan_name = (TextView) v.findViewById(R.id.tv_plan_name);
        TextView tv_plan_count = (TextView) v.findViewById(R.id.tv_plan_count);
        TextView tv_finished = (TextView) v.findViewById(R.id.tv_finished);
        TextView tv_important = (TextView) v.findViewById(R.id.tv_important);
        TextView tv_commonly = (TextView) v.findViewById(R.id.tv_commonly);
        TextView tv_not_important = (TextView) v.findViewById(R.id.tv_not_important);
        if (mPlan != null && describes != null) {
            tv_date.setText(mPlan.getSet_time());
            tv_plan_name.setText(mPlan.getTitle());
            tv_plan_count.setText(describes.size() + "");
            tv_finished.setText(getFinishedCount() + "");
            tv_important.setText(getImportantCount()+"");
            tv_commonly.setText(getCommonlyCount()+"");
            tv_not_important.setText((describes.size()
                    - getImportantCount() - getCommonlyCount())+"");
        }
    }

    public static FragmentPlanData getInstance() {
        if (mFragmentPlanData == null) {
            mFragmentPlanData = new FragmentPlanData();
        }
        return mFragmentPlanData;
    }

    public int getFinishedCount() {
        int finishedCount = 0;
        if (describes != null) {
            for (RecyclerDescribe describe : describes) {
                if (describe.isFinished()) {
                    finishedCount += 1;
                }
            }
        }
        return finishedCount;
    }

    public int getImportantCount() {
        int importantCount = 0;
        if (mPlan.getDescribes() != null) {
            for (RecyclerDescribe describe : describes) {
                if (describe.getPlanLevel().equals(getActivity().getString(R.string.important))) {
                    importantCount += 1;
                }
            }
        }
        return importantCount;
    }

    public int getCommonlyCount() {
        int commonlyCount = 0;
        for (RecyclerDescribe describe : describes) {
            if (describe.getPlanLevel().equals(getActivity().getString(R.string.commonly))) {
                commonlyCount += 1;
            }
        }
        return commonlyCount;
    }
}


