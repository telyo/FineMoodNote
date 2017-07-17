package com.telyo.finemoodnote.ui;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.telyo.finemoodnote.R;
import com.telyo.finemoodnote.adapter.RecyclerViewDescribeAdapter;
import com.telyo.finemoodnote.entity.RecyclerDescribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/14.
 */

public class PlanActivity extends BaseThemeActivity implements View.OnClickListener {
    private CardView mCardView;

    private EditText mEt_jobTitle;
    private EditText mEt_jobTime;

    private RecyclerView mRv_describe;
    private ImageView mImg_addDescribe;

    LinearLayout mLl_select;
    private ImageView mImg_cancel;
    private ImageView mImg_confirm;

    private TextView mTv_addNewPlan;
    private RecyclerView mRv_plans;

    private boolean isEdit = false;
    private EditText et_new;
    private LinearLayout ll;

    private List<RecyclerDescribe> mDescribeData = new ArrayList<>();
    private RecyclerViewDescribeAdapter mDescribeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        initView();

    }

    private void initView() {
        mCardView = (CardView) findViewById(R.id.card_view);
        mCardView.setOnClickListener(this);
        mEt_jobTitle = (EditText) findViewById(R.id.et_jobTitle);
        mEt_jobTime = (EditText) findViewById(R.id.et_castTime);

        mRv_describe = (RecyclerView) findViewById(R.id.rv_workDescribe);
        mImg_addDescribe = (ImageView) findViewById(R.id.img_addDescribe);
        mImg_addDescribe.setOnClickListener(this);

        mLl_select = (LinearLayout) findViewById(R.id.ll_select);
        mImg_cancel = (ImageView) findViewById(R.id.imgb_cancel);
        mImg_cancel.setOnClickListener(this);
        mImg_cancel.getBackground().setAlpha(100);
        mImg_confirm = (ImageView) findViewById(R.id.imgb_confirm);
        mImg_confirm.setOnClickListener(this);
        mImg_confirm.getBackground().setAlpha(100);

        mTv_addNewPlan = (TextView) findViewById(R.id.tv_addNewPlan);
        mTv_addNewPlan.setOnClickListener(this);
        mRv_plans = (RecyclerView) findViewById(R.id.rv_plans);

        NestedScrollView mNsv_plans = (NestedScrollView) findViewById(R.id.nsv_plans);
        mNsv_plans.getBackground().setAlpha(100);

        et_new = new EditText(this);
        mDescribeAdapter = new RecyclerViewDescribeAdapter(PlanActivity.this,mDescribeData);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv_describe.setLayoutManager(manager);
        mRv_describe.setAdapter(mDescribeAdapter);
        showRvdescribe();
        //标记是否已完成计划条目
        mDescribeAdapter.setmOnItemClickListener(new RecyclerViewDescribeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewDescribeAdapter.DescribeViewHolder holder, int position) {
                mDescribeData.get(position).setFinished(!mDescribeData.get(position).isFinished());
                mDescribeAdapter.notifyDataSetChanged();
                upUserDate();
            }
        });
        //删除一个计划条目
        mDescribeAdapter.setmOnDeleteItemClickListener(new RecyclerViewDescribeAdapter.OnDeleteItemClickListener() {
            @Override
            public void onDeleteItemClick(RecyclerViewDescribeAdapter.DescribeViewHolder holder, int position) {
                mDescribeData.remove(position);
                mDescribeAdapter.notifyDataSetChanged();
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.card_view:
                editNowPlan();
                break;
            case R.id.img_addDescribe:
                editDescribe(true);
                break;
            case R.id.imgb_cancel:
                saveCancel();
                break;
            case R.id.imgb_confirm:
                saveConfirm();
                break;
            case R.id.tv_addNewPlan:
                break;
        }
    }


    private void upUserDate() {
        //TODO 这里应该传入参数 根据参数来更新用户数据
    }

    private void editNowPlan() {
        if (!isEdit) {
            setEditEnable(true);
            isEdit = true;
        }
    }

    //编辑
    private void editDescribe(boolean isEdit) {
        ll = (LinearLayout) this.findViewById(R.id.ll_planContent);
        if (isEdit) {
            et_new.setBackgroundColor(ContextCompat.getColor(PlanActivity.this, android.R.color.transparent));
            et_new.setTextSize(14);
            et_new.setTextColor(ContextCompat.getColor(PlanActivity.this,R.color.colorTextNormal));
            et_new.setHintTextColor(ContextCompat.getColor(PlanActivity.this,R.color.colorTextNormal));
            et_new.setHint(R.string.describe_the_plan);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mImg_addDescribe.getLayoutParams();
            ll.addView(et_new, lp);
            mImg_addDescribe.setVisibility(View.GONE);
        } else {
            ll.removeView(et_new);
        }
    }

    //取消
    private void saveCancel() {
        setEditEnable(false);
        isEdit = false;
        if (ll != null && ll.getChildCount() > 4) {
            editDescribe(false);
        }
        et_new.setText("");
    }

    //保存
    private void saveConfirm() {
        setEditEnable(false);
        isEdit = false;
        if (ll != null && ll.getChildCount() > 4) {
            editDescribe(false);
        }
        if (!TextUtils.isEmpty(et_new.getText().toString())) {
            RecyclerDescribe describe = new RecyclerDescribe();
            describe.setDescribe(et_new.getText().toString());
            describe.setFinished(false);
            mDescribeData.add(describe);
            mDescribeAdapter.notifyDataSetChanged();
            showRvdescribe();
        }
        et_new.setText("");
        saveNowPlanData();

    }

    private void saveNowPlanData() {
        //TODO save things
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    //判断是否可编辑 为按钮添加显示动画
    private void setEditEnable(boolean isEnable) {
        mEt_jobTitle.setEnabled(isEnable);
        mEt_jobTime.setEnabled(isEnable);
        if (isEnable) {
            mImg_addDescribe.setVisibility(View.VISIBLE);
            mImg_addDescribe.setAnimation(AnimationUtils.loadAnimation(PlanActivity.this,R.anim.anim_scaling));
            mLl_select.setVisibility(View.VISIBLE);
            mImg_cancel.setAnimation(AnimationUtils.loadAnimation(PlanActivity.this,R.anim.anim_scaling));
            mImg_confirm.setAnimation(AnimationUtils.loadAnimation(PlanActivity.this,R.anim.anim_scaling));
        } else {
            mImg_addDescribe.setVisibility(View.GONE);
            mLl_select.setVisibility(View.GONE);
        }
    }
    //判断工作计划的条目是否显示
    private void showRvdescribe(){
        if (mDescribeData.size()>0){
            mRv_describe.setVisibility(View.VISIBLE);
        }
    }

}
