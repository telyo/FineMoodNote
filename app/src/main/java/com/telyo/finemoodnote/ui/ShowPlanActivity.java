package com.telyo.finemoodnote.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.telyo.finemoodnote.R;
import com.telyo.finemoodnote.entity.RecyclerPlans;
import com.telyo.finemoodnote.fragment.FragmentPlanData;
import com.telyo.finemoodnote.fragment.FragmentPlanDetail;
import com.telyo.finemoodnote.utils.L;

import java.util.ArrayList;
import java.util.List;

import static com.telyo.finemoodnote.utils.Constants.SHOW_PLAN_REQUEST_CONTENT;

/**
 * Created by Administrator on 2017/7/21.
 */

public class ShowPlanActivity extends BaseThemeActivity{

    private ViewPager mViewPager;
    private FragmentPlanData mFragmentPlanData;
    private FragmentPlanDetail mFragmentPlanDetail;
    private List<Fragment> mFragments;

    private ImageView mImg_selected;
    private ImageView mImg_unSelected;
    private ActionBar mActionBar;
    private RecyclerPlans mPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_plan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        if (mActionBar != null){
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
        initDate();
        initView();
    }

    private void initDate() {
        mFragmentPlanData = FragmentPlanData.getInstance();
        mFragmentPlanDetail = FragmentPlanDetail.getInstance();
        mFragments = new ArrayList<>();
        mFragments.add(mFragmentPlanDetail);
        mFragments.add(mFragmentPlanData);

        //拿到数据
        Intent intent = getIntent();
        mPlan = intent.getParcelableExtra(SHOW_PLAN_REQUEST_CONTENT);
        L.d("详情" + mPlan.getDescribes().get(0));
        Bundle bundle = new Bundle();
        bundle.putParcelable(SHOW_PLAN_REQUEST_CONTENT,mPlan);
        mFragmentPlanDetail.setArguments(bundle);
        mFragmentPlanData.setArguments(bundle);
    }



    private void initView() {
        mImg_selected = (ImageView) findViewById(R.id.img_select);
        mImg_unSelected = (ImageView) findViewById(R.id.img_unSelected);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        mImg_selected.setImageResource(R.drawable.selected);
                        mImg_unSelected.setImageResource(R.drawable.unselected);
                        break;
                    case 1:
                        mImg_selected.setImageResource(R.drawable.unselected);
                        mImg_unSelected.setImageResource(R.drawable.selected);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
