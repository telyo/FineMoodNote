package com.telyo.finemoodnote.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.telyo.finemoodnote.R;
import com.telyo.finemoodnote.adapter.RecyclerViewDescribeAdapter;
import com.telyo.finemoodnote.adapter.RecyclerViewPlansAdapter;
import com.telyo.finemoodnote.entity.RecyclerDescribe;
import com.telyo.finemoodnote.entity.RecyclerPlans;
import com.telyo.finemoodnote.utils.L;
import com.telyo.finemoodnote.utils.ValueAnimatorUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/14.
 */

public class PlanActivity extends BaseThemeActivity implements View.OnClickListener {

    private EditText mEt_jobTitle;
    private TextView mTv_jobTime;

    private RecyclerView mRv_describe;
    private ImageView mImg_editOrAdd;

    private ImageView mImg_cancel;
    private ImageView mImg_confirm;

    private TextView mTv_addNewPlan;
    private RecyclerView mRv_plans;

    private boolean isEdit = false;
    private View describeView;
    private LinearLayout ll;

    //编辑计划描述时的三种状态
    //点击编辑当前计划
    private static final int EDIT_RESENT_PLAN = 1001;
    //点击出添加现当前计划描述的EditText
    private static final int EDIT_DESCRIBE = 1002;
    //点击加入计划描述并显示
    private static final int ADD_DESCRIBE = 1003;
    //默认为点击编辑状态
    private static int EDIT_STATE = EDIT_RESENT_PLAN;
    //计划描述
    private List<RecyclerDescribe> mDescribeData = new ArrayList<>();
    private RecyclerViewDescribeAdapter mDescribeAdapter;
    //选着计划执行的日期
    public static final int DATE_PICK_REQUEST_CODE = 0002;

    //所有计划
    private List<RecyclerPlans> mPlansData = new ArrayList<>();
    private RecyclerViewPlansAdapter mPlansAdapter;
    private static final int NEW_PLAN_REQUEST_CODE = 0003;
    private EditText mEt_new_describe;
    private RadioGroup mRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        initData();
        initView();

    }

    private void initData() {
        //TODO 1、从服务器拿到数据 赋值给plans 2、根据position从plan里取出mDescribeData
    }

    private void initView() {
        mEt_jobTitle = (EditText) findViewById(R.id.et_jobTitle);
        mTv_jobTime = (TextView) findViewById(R.id.tv_doneTime);
        mTv_jobTime.setOnClickListener(this);

        mRv_describe = (RecyclerView) findViewById(R.id.rv_workDescribe);
        mImg_editOrAdd = (ImageView) findViewById(R.id.img_or_edit_add);
        mImg_editOrAdd.setOnClickListener(this);
        setEditImgResource(EDIT_STATE);

        mImg_cancel = (ImageView) findViewById(R.id.img_cancel);
        mImg_cancel.setOnClickListener(this);
        mImg_cancel.getBackground().setAlpha(100);
        mImg_confirm = (ImageView) findViewById(R.id.img_confirm);
        mImg_confirm.setOnClickListener(this);
        mImg_confirm.getBackground().setAlpha(100);

        mTv_addNewPlan = (TextView) findViewById(R.id.tv_addNewPlan);
        mTv_addNewPlan.setOnClickListener(this);

        //虚化背景
        NestedScrollView mNsv_plans = (NestedScrollView) findViewById(R.id.nsv_plans);
        mNsv_plans.getBackground().setAlpha(100);

        //初始化一个View用来编辑描述
        describeView = getLayoutInflater().inflate(R.layout.describe_plan, this.ll, false);
        mEt_new_describe = (EditText) describeView.findViewById(R.id.et_new_describe);
        mRadioGroup = (RadioGroup) describeView.findViewById(R.id.rg_plan_level);
        mRadioGroup.check(R.id.rb_commonly);
        mDescribeAdapter = new RecyclerViewDescribeAdapter(PlanActivity.this, mDescribeData);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv_describe.setLayoutManager(manager);
        mRv_describe.setAdapter(mDescribeAdapter);
        showRvdescribe();
        //标记是否已完成计划条目
        mDescribeAdapter.setmOnItemClickListener(new RecyclerViewDescribeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewDescribeAdapter.DescribeViewHolder holder, int position) {
                if (isEdit) {
                    mDescribeData.get(position).setFinished(!mDescribeData.get(position).isFinished());
                    mDescribeAdapter.notifyDataSetChanged();
                    upUserDate();
                }
            }
        });
        //删除一个计划条目
        mDescribeAdapter.setmOnDeleteItemClickListener(new RecyclerViewDescribeAdapter.OnDeleteItemClickListener() {
            @Override
            public void onDeleteItemClick(RecyclerViewDescribeAdapter.DescribeViewHolder holder, int position) {
                if (isEdit) {
                    mDescribeData.remove(position);
                    mDescribeAdapter.notifyDataSetChanged();
                    upUserDate();
                }
            }
        });

        mRv_plans = (RecyclerView) findViewById(R.id.rv_plans);
        LinearLayoutManager pManager = new LinearLayoutManager(PlanActivity.this);
        pManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv_plans.setLayoutManager(pManager);
        mPlansAdapter = new RecyclerViewPlansAdapter(PlanActivity.this, mPlansData);
        mRv_plans.setAdapter(mPlansAdapter);
        mPlansAdapter.setOnItemLongClickListener(new RecyclerViewPlansAdapter.OnItemLongClickListener() {
            @Override
            public void onLongClick(View itemView, int position) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_doneTime:
                toPickDate();
                break;
            case R.id.img_or_edit_add:
                switchEditOrAdd();
                break;
            case R.id.img_cancel:
                saveCancel();
                break;
            case R.id.img_confirm:
                saveConfirm();
                break;
            case R.id.tv_addNewPlan:
                addNewPlan();
                break;
        }
    }


    private void switchEditOrAdd() {
        switch (EDIT_STATE) {
            case EDIT_RESENT_PLAN:
                editLeastPlan();
                break;
            case EDIT_DESCRIBE:
                editDescribe(true);
                break;
            case ADD_DESCRIBE:
                addDescribe();
                editDescribe(true);
                break;
        }
    }

    private void setEditImgResource(int i) {
        switch (i) {
            case EDIT_RESENT_PLAN:
                mImg_editOrAdd.setImageResource(R.drawable.icon_edit);
                break;
            case EDIT_DESCRIBE:
                mImg_editOrAdd.setImageResource(R.drawable.icon_add);
                break;
        }
    }

    private void upUserDate() {
        //TODO 这里应该传入参数 根据参数来更新用户数据
    }

    private void toPickDate() {
        Intent intent = new Intent(PlanActivity.this, DatePickerActivity.class);
        startActivityForResult(intent, DATE_PICK_REQUEST_CODE);
    }


    //编辑最近一次的计划
    private void editLeastPlan() {
        if (!isEdit) {
            setEditEnable(true);
            isEdit = true;
            setEditImgResource(EDIT_DESCRIBE);
            EDIT_STATE = EDIT_DESCRIBE;
        }
    }

    //编辑计划描述
    private void editDescribe(boolean isEdit) {
        ll = (LinearLayout) this.findViewById(R.id.ll_planContent);
        if (isEdit) {
            describeView.setBackgroundColor(ContextCompat.getColor(PlanActivity.this, android.R.color.transparent));
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mEt_jobTitle.getLayoutParams();
            ll.addView(describeView, lp);
            EDIT_STATE = ADD_DESCRIBE;
        } else {
            ll.removeView(describeView);
        }
    }

    //取消
    private void saveCancel() {
        setEditEnable(false);
        isEdit = false;
        initLlView();
        setEditImgResource(EDIT_RESENT_PLAN);
        EDIT_STATE = EDIT_RESENT_PLAN;
    }

    //移除用于编辑describe的EditText 并重设et_new的Hint
    private void initLlView() {
        if (ll != null && ll.getChildCount() > 3) {
            editDescribe(false);
        }
        mEt_new_describe.setText("");
        mRadioGroup.check(R.id.rb_commonly);
    }

    //保存
    private void saveConfirm() {
        setEditEnable(false);
        isEdit = false;
        addDescribe();
        saveNowPlanDescribe();
        setEditImgResource(EDIT_RESENT_PLAN);
        EDIT_STATE = EDIT_RESENT_PLAN;

    }

    //添加计划的描述并显示
    private void addDescribe() {
        if (!TextUtils.isEmpty(mEt_new_describe.getText().toString())) {
            RecyclerDescribe describe = new RecyclerDescribe();
            describe.setDescribe(mEt_new_describe.getText().toString());
            describe.setFinished(false);
            for (int i = 0; i < mRadioGroup.getChildCount(); i++) {
                RadioButton radioButton = (RadioButton) mRadioGroup.getChildAt(i);
                if (radioButton.isChecked()) {
                    L.i(radioButton.getText().toString());
                    describe.setPlanLevel(radioButton.getText().toString());
                }
            }

            mDescribeData.add(describe);
            mDescribeAdapter.notifyDataSetChanged();
            showRvdescribe();
        }

        initLlView();
        EDIT_STATE = ADD_DESCRIBE;
    }

    private void saveNowPlanDescribe() {
        //TODO save things
    }

    private void addNewPlan() {
        Intent intent = new Intent(PlanActivity.this, NewPlanActivity.class);
        startActivityForResult(intent, NEW_PLAN_REQUEST_CODE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_CANCELED) {
        }
        switch (requestCode) {
            case DATE_PICK_REQUEST_CODE:
                if (data != null) {
                    String date = data.getStringExtra("date");
                    mTv_jobTime.setText(date);
                }
                break;
        }
    }

    //判断是否可编辑 为按钮添加显示动画
    private void setEditEnable(boolean isEnable) {
        mEt_jobTitle.setEnabled(isEnable);
        mTv_jobTime.setEnabled(isEnable);
        if (isEnable) {
            ValueAnimatorUtils.doScale(mImg_cancel, isEnable, 0f, 1.2f, 1f).start();
            ValueAnimatorUtils.doScale(mImg_confirm, isEnable, 0f, 1.2f, 1f).start();
        } else {
            ValueAnimatorUtils.doScale(mImg_confirm, isEnable, 1f, 1.2f, 0f).start();
            ValueAnimatorUtils.doScale(mImg_cancel, isEnable, 1f, 1.2f, 0f).start();
        }
    }

    //判断工作计划的条目是否显示
    private void showRvdescribe() {
        if (mDescribeData.size() > 0) {
            mRv_describe.setVisibility(View.VISIBLE);
        }
    }
}
