package com.telyo.finemoodnote.ui;

import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
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
import com.telyo.finemoodnote.utils.DateUtil;
import com.telyo.finemoodnote.utils.L;
import com.telyo.finemoodnote.utils.OrderUtils;
import com.telyo.finemoodnote.utils.SnackBarUtils;
import com.telyo.finemoodnote.utils.ValueAnimatorUtils;
import com.telyo.finemoodnote.views.ScrollerControlRecycler;

import java.util.ArrayList;
import java.util.List;

import static com.telyo.finemoodnote.utils.Constants.DATE_PICK_REQUEST_CODE;
import static com.telyo.finemoodnote.utils.Constants.NEW_PLAN_REQUEST_CODE;
import static com.telyo.finemoodnote.utils.Constants.NEW_PLAN_REQUEST_CONTENT;
import static com.telyo.finemoodnote.utils.Constants.SHOW_PLAN_REQUEST_CODE;
import static com.telyo.finemoodnote.utils.Constants.SHOW_PLAN_REQUEST_CONTENT;

/**
 * Created by Administrator on 2017/7/14.
 */

public class PlanActivity extends BaseThemeActivity implements View.OnClickListener {

    private EditText mEt_jobTitle;
    private TextView mTv_jobTime;

    private ScrollerControlRecycler mRv_describe;
    private ImageView mImg_editOrAdd;

    private ImageView mImg_cancel;
    private ImageView mImg_confirm;

    private FloatingActionButton mFl_addNewPlan;
    private ScrollerControlRecycler mRv_plans;

    private boolean isEdit = false;
    //用一个布局来显示要添加的内容
    private View describeView;
    private LinearLayout mLl_edit;

    //编辑计划描述时的三种状态
    //点击编辑当前计划
    private static final int EDIT_RESENT_PLAN = 1001;
    //点击出添加现当前计划描述的EditText
    private static final int EDIT_AND_ADD_DESCRIBE = 1002;
    //默认为点击编辑状态
    private static int EDIT_STATE = EDIT_RESENT_PLAN;
    //计划描述
    private List<RecyclerDescribe> mDescribeData = new ArrayList<>();
    private RecyclerViewDescribeAdapter mDescribeAdapter;


    //所有计划
    private List<RecyclerPlans> mPlansData = new ArrayList<>();
    private RecyclerViewPlansAdapter mPlansAdapter;

    private EditText mEt_new_describe;
    private RadioGroup mRadioGroup;
    private boolean isScroll = true;
    private int mPlaNPosition = 0;

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
        for (int j = 0; j < 5; j++) {
            RecyclerDescribe describe = new RecyclerDescribe();
            describe.setDescribe("有难者乎？" + j);
            describe.setFinished(false);
            describe.setPlanLevel("一般");
            mDescribeData.add(describe);
        }


        for (int i = 0; i < 22; i++) {
            RecyclerPlans plan = new RecyclerPlans();
            plan.setTime(i + "time");
            plan.setDone_state("done");
            plan.setDescribes(mDescribeData);
            plan.setDate("2017-08-09");
            plan.setTitle(i + "个任务");
            plan.setSet_time("2017-07-" + (10 + i));
            mPlansData.add(plan);
        }

    }

    private void initView() {
        mEt_jobTitle = (EditText) findViewById(R.id.et_jobTitle);
        mTv_jobTime = (TextView) findViewById(R.id.tv_doneTime);
        mTv_jobTime.setOnClickListener(this);

        mRv_describe = (ScrollerControlRecycler) findViewById(R.id.rv_workDescribe);
        mImg_editOrAdd = (ImageView) findViewById(R.id.img_or_edit_add);
        mImg_editOrAdd.setOnClickListener(this);
        setEditImgResource(EDIT_RESENT_PLAN);

        mImg_cancel = (ImageView) findViewById(R.id.img_cancel);
        mImg_cancel.setOnClickListener(this);
        mImg_cancel.getBackground().setAlpha(100);
        mImg_confirm = (ImageView) findViewById(R.id.img_confirm);
        mImg_confirm.setOnClickListener(this);
        mImg_confirm.getBackground().setAlpha(100);


        mFl_addNewPlan = (FloatingActionButton) findViewById(R.id.fl_addNewPlan);
        mFl_addNewPlan.setOnClickListener(this);
        //虚化背景
        NestedScrollView mNsv_plans = (NestedScrollView) findViewById(R.id.nsv_plans);
       // mNsv_plans.getBackground().setAlpha(100);
        //初始化一个View用来编辑描述
        describeView = getLayoutInflater().inflate(R.layout.describe_plan, this.mLl_edit, false);
        mEt_new_describe = (EditText) describeView.findViewById(R.id.et_new_describe);
        mRadioGroup = (RadioGroup) describeView.findViewById(R.id.rg_plan_level);
        mRadioGroup.check(R.id.rb_commonly);
        mDescribeAdapter = new RecyclerViewDescribeAdapter(PlanActivity.this, mDescribeData);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        //TODO 虚拟数据
        initCurrentPlan();
        mRv_describe.setLayoutManager(manager);
        mRv_describe.setAdapter(mDescribeAdapter);
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
                if (isEdit) {
                    mDescribeAdapter.removeData(position);
                    upUserDate();
                }
            }
        });

        mRv_plans = (ScrollerControlRecycler) findViewById(R.id.rv_plans);
        final LinearLayoutManager pManager = new LinearLayoutManager(PlanActivity.this);
        pManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv_plans.setLayoutManager(pManager);
        mPlansAdapter = new RecyclerViewPlansAdapter(PlanActivity.this, mPlansData);
        mRv_plans.setAdapter(mPlansAdapter);
        mRv_plans.setItemAnimator(new DefaultItemAnimator());
        mRv_plans.smoothScrollToPosition(DateUtil.getCurrentPlanPosition(mPlansData, getString(R.string.date_type_ymd)));
        mPlansAdapter.setOnItemLongClickListener(new RecyclerViewPlansAdapter.OnItemLongClickListener() {
            @Override
            public void onLongClick(View itemView, int position) {
                deleteItem(itemView, position);
            }
        });
        mPlansAdapter.setOnItemClickListener(new RecyclerViewPlansAdapter.OnItemClickListener() {
            @Override
            public void onClick(View itemView, int position) {
                showItem(position);
            }
        });

        mNsv_plans.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                floatButtonAnim(scrollY, oldScrollY);
            }
        });
    }

    //移除用于编辑describe的EditText 并重设et_new的Hint
    private void initLlEditView() {
        if (mLl_edit != null && mLl_edit.getChildCount() > 3) {
            editDescribe(false);
        }
        mEt_new_describe.setText("");
        mRadioGroup.check(R.id.rb_commonly);
    }

    //初始化当前任务
    private void initCurrentPlan() {
        mPlaNPosition = DateUtil.getCurrentPlanPosition(mPlansData, getString(R.string.date_type_ymd));
        if (mPlansData != null && mPlansData.size() > mPlaNPosition) {
            mDescribeData = mPlansData.get(mPlaNPosition).getDescribes();
            mDescribeAdapter.notifyDataSetChanged();
            mEt_jobTitle.setText(mPlansData.get(mPlaNPosition).getTitle());
            mTv_jobTime.setText(mPlansData.get(mPlaNPosition).getSet_time());
        }
    }


    //查看计划详情
    private void showItem(int position) {
        RecyclerPlans plan = mPlansData.get(position);
        //L.d("详情" + plan.getDescribes().get(0));
        List<RecyclerDescribe> list = new ArrayList<>();
        for (int i = 0; i<10; i++){
            RecyclerDescribe describe = new RecyclerDescribe();
            describe.setDescribe("asdfsad" + i);
            describe.setFinished(true);
            describe.setPlanLevel("重要");
            list.add(describe);
        }
        plan.setDescribes(list);
        Bundle bundle = new Bundle();
        bundle.putParcelable(SHOW_PLAN_REQUEST_CONTENT,plan);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setClass(PlanActivity.this,ShowPlanActivity.class);
        startActivityForResult(intent,SHOW_PLAN_REQUEST_CODE);
    }

    //用SnackBar 删除计划
    private void deleteItem(final View itemView, final int position) {
        itemView.setSelected(true);
        final Snackbar snackBar = Snackbar.make(itemView, R.string.Snakcar_cancel_plan, Snackbar.LENGTH_LONG);
        SnackBarUtils.SnackbarAddView(snackBar, R.layout.snackbar_button_layout, 1);
        SnackBarUtils.SetAction(snackBar, R.id.cancel_btn, getString(R.string.no), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemView.setSelected(false);
                snackBar.dismiss();
            }
        });
        snackBar.setAction(R.string.yes, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlansAdapter.removeData(position);
                itemView.setSelected(false);
                initCurrentPlan();
                upUserDate();

            }
        });
        snackBar.show();
        snackBar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                itemView.setSelected(false);
            }
        });
    }

    //悬浮按钮的动画
    private void floatButtonAnim(int scrollY, int oldScrollY) {
        if ((oldScrollY - scrollY) < 0) {
            if (isScroll) {
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(ValueAnimatorUtils.doTranslate(mFl_addNewPlan, 0, 300))
                        .with(ValueAnimatorUtils.doAlpha(mFl_addNewPlan, 1f, 0f));
                animatorSet.start();
                isScroll = false;
            }
        } else {
            if (!isScroll) {
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(ValueAnimatorUtils.doTranslate(mFl_addNewPlan, 300, 0))
                        .with(ValueAnimatorUtils.doAlpha(mFl_addNewPlan, 0f, 1f));
                animatorSet.start();
                isScroll = true;
            }
        }
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
            case R.id.fl_addNewPlan:
                addNewPlan();
                break;
        }
    }

    private void switchEditOrAdd() {
        switch (EDIT_STATE) {
            case EDIT_RESENT_PLAN:
                editLeastPlan();
                initLlEditView();
                editDescribe(true);
                break;
            case EDIT_AND_ADD_DESCRIBE:
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
            case EDIT_AND_ADD_DESCRIBE:
                mImg_editOrAdd.setImageResource(R.drawable.icon_add);
                break;
        }
    }

    private void upUserDate() {
        //TODO 这里应该传入参数 根据参数
        // 来更新用户数据
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
            setEditImgResource(EDIT_AND_ADD_DESCRIBE);
            EDIT_STATE = EDIT_AND_ADD_DESCRIBE;
        }
    }

    //编辑计划描述
    private void editDescribe(boolean isEdit) {
        mLl_edit = (LinearLayout) this.findViewById(R.id.ll_planContent);
        if (isEdit) {
            describeView.setBackgroundColor(ContextCompat.getColor(PlanActivity.this, android.R.color.transparent));
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mEt_jobTitle.getLayoutParams();
            mLl_edit.addView(describeView, lp);
            EDIT_STATE = EDIT_AND_ADD_DESCRIBE;
        } else {
            mLl_edit.removeView(describeView);
        }
    }

    //取消
    private void saveCancel() {
        setEditEnable(false);
        isEdit = false;
        initLlEditView();
        setEditImgResource(EDIT_RESENT_PLAN);
        EDIT_STATE = EDIT_RESENT_PLAN;
    }

    //保存
    private void saveConfirm() {
        setEditEnable(false);
        isEdit = false;
        addDescribe();
        saveNowPlanDescribe();
        setEditImgResource(EDIT_RESENT_PLAN);
        EDIT_STATE = EDIT_RESENT_PLAN;
        removeEmptyPlan();
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
            mDescribeAdapter.addData(mDescribeData.size(), describe);
            mRv_describe.smoothScrollToPosition(mDescribeData.size()-1);
        } else {
            removeEmptyPlan();
        }
        initLlEditView();
        EDIT_STATE = EDIT_AND_ADD_DESCRIBE;
    }
    //移除空计划
    private void removeEmptyPlan() {
        if (mDescribeData.size() == 0) {
            mPlansData.remove(mPlaNPosition);
            mPlansAdapter.notifyDataSetChanged();
            initCurrentPlan();
        }
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
    protected void onPause() {
        super.onPause();
        EDIT_STATE = EDIT_RESENT_PLAN;
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
            case NEW_PLAN_REQUEST_CODE:
                if (data != null) {
                    RecyclerPlans plan = data.getParcelableExtra(NEW_PLAN_REQUEST_CONTENT);
                    mPlansData.add(plan);
                    mPlansData = OrderUtils.orderPlansByDate(mPlansData, getString(R.string.date_type_ymd));
                    mPlansAdapter.notifyDataSetChanged();
                    mRv_plans.smoothScrollToPosition(DateUtil.getCurrentPlanPosition(mPlansData, getString(R.string.date_type_ymd)));
                }
                break;
        }
    }

    //判断是否可编辑 为按钮添加显示动画
    private void setEditEnable(boolean isEnable) {
        mEt_jobTitle.setEnabled(isEnable);
        mTv_jobTime.setEnabled(isEnable);
        FloatingActionButton fl_addNewPlan = (FloatingActionButton) findViewById(R.id.fl_addNewPlan);
        if (isEnable) {
            fl_addNewPlan.setVisibility(View.GONE);
            ValueAnimatorUtils.doScale(mImg_cancel, isEnable, 0f, 1.2f, 1f).start();
            ValueAnimatorUtils.doScale(mImg_confirm, isEnable, 0f, 1.2f, 1f).start();
        } else {
            fl_addNewPlan.setVisibility(View.VISIBLE);
            ValueAnimatorUtils.doScale(mImg_confirm, isEnable, 1f, 1.2f, 0f).start();
            ValueAnimatorUtils.doScale(mImg_cancel, isEnable, 1f, 1.2f, 0f).start();
        }
    }

}
