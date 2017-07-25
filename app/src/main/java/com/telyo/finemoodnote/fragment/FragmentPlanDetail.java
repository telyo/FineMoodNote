package com.telyo.finemoodnote.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.telyo.finemoodnote.R;
import com.telyo.finemoodnote.adapter.RecyclerViewDescribeAdapter;
import com.telyo.finemoodnote.entity.RecyclerDescribe;
import com.telyo.finemoodnote.entity.RecyclerPlans;
import com.telyo.finemoodnote.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

import static com.telyo.finemoodnote.utils.Constants.PLAN_DESCRIBE;
import static com.telyo.finemoodnote.utils.Constants.SHOW_PLAN_REQUEST_CONTENT;

/**
 * Created by Administrator on 2017/7/24.
 */

public class FragmentPlanDetail extends Fragment {
    private static FragmentPlanDetail mFragmentPlanDetail;
    private RecyclerPlans mPlan;
    private List<RecyclerDescribe> describes = new ArrayList<>();
    private RecyclerView mRv_plans;
    private RecyclerViewDescribeAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        initData();
        View v = inflater.inflate(R.layout.fragment_plan_detail,container,false);
        initView(v);
        return v;
    }

    private void initView(View v) {
        mRv_plans = (RecyclerView) v.findViewById(R.id.rv_plans);
        mAdapter = new RecyclerViewDescribeAdapter(getActivity(),describes);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRv_plans.setLayoutManager(manager);
        mRv_plans.setLayoutManager(manager);
        mRv_plans.setItemAnimator(new DefaultItemAnimator());
        mRv_plans.setAdapter(mAdapter);
        mAdapter.setmOnItemClickListener(new RecyclerViewDescribeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewDescribeAdapter.DescribeViewHolder holder, int position) {
                if (DateUtil.isUsefulDate(mPlan.getSet_time(),"yyyy-MM-dd")){
                    describes.get(position).setFinished(!describes.get(position).isFinished());
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initData() {
        Bundle bundle = getArguments();
        mPlan = bundle.getParcelable(SHOW_PLAN_REQUEST_CONTENT);
        describes = bundle.getParcelableArrayList(PLAN_DESCRIBE);
    }
    public static FragmentPlanDetail getInstance(){
        if (mFragmentPlanDetail == null){
            mFragmentPlanDetail = new FragmentPlanDetail();
        }
        return mFragmentPlanDetail;
    }

}
