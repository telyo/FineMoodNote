package com.telyo.finemoodnote.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.telyo.finemoodnote.R;
import com.telyo.finemoodnote.adapter.RecyclerViewDescribeAdapter;
import com.telyo.finemoodnote.entity.RecyclerDescribe;
import com.telyo.finemoodnote.entity.RecyclerPlans;
import com.telyo.finemoodnote.utils.L;

import java.util.ArrayList;
import java.util.List;

import static com.telyo.finemoodnote.utils.Constants.DATE_PICK_REQUEST_CODE;
import static com.telyo.finemoodnote.utils.Constants.NEW_PLAN_REQUEST_CODE;
import static com.telyo.finemoodnote.utils.Constants.NEW_PLAN_REQUEST_CONTENT;

/**
 * Created by Administrator on 2017/7/18.
 */

public class NewPlanActivity extends BaseThemeActivity implements View.OnClickListener {

    //用来动态添加View
    private LinearLayout mLl_planContent;
    private EditText mEt_jobTitle;
    private TextView mTv_TimeChoose;
    private ImageView mImg_or_edit_add;
    private Button mBtn_confirm;

    private RecyclerView mRv_workDescribe;

    private List<RecyclerDescribe> mDescribes = new ArrayList<>();
    private RecyclerPlans mPlans;
    private RecyclerViewDescribeAdapter mAdapter;
    //动态添加和移除的View
    private View describeView;
    private EditText mEt_new_describe;
    private RadioGroup mRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_plan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initData();
        initView();

    }

    private void initData() {
        mPlans = new RecyclerPlans();
    }

    private void initView() {
        mLl_planContent = (LinearLayout) findViewById(R.id.ll_planContent);
        mEt_jobTitle = (EditText) findViewById(R.id.et_jobTitle);
        mTv_TimeChoose = (TextView) findViewById(R.id.tv_doneTime);
        mTv_TimeChoose.setOnClickListener(this);
        //RecyclerView
        mRv_workDescribe = (RecyclerView) findViewById(R.id.rv_workDescribe);
        mAdapter = new RecyclerViewDescribeAdapter(this,mDescribes);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv_workDescribe.setLayoutManager(manager);
        mRv_workDescribe.setAdapter(mAdapter);
       //
        mImg_or_edit_add = (ImageView) findViewById(R.id.img_or_edit_add);
        mImg_or_edit_add.setOnClickListener(this);
        //
        mBtn_confirm = (Button) findViewById(R.id.btn_confirm);
        mBtn_confirm.setOnClickListener(this);

        describeView = getLayoutInflater().inflate(R.layout.describe_plan,this.mLl_planContent,false);
        mEt_new_describe = (EditText) describeView.findViewById(R.id.et_new_describe);
        mRadioGroup = (RadioGroup) describeView.findViewById(R.id.rg_plan_level);
        mRadioGroup.check(R.id.rb_commonly);

    }


    private void initEditView(){
        if (mLl_planContent.getChildCount() > 3){
            mLl_planContent.removeView(describeView);
        }
        mLl_planContent.addView(describeView);
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

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_doneTime:
                toPickDate();
                break;
            case R.id.img_or_edit_add:
                toAddDescribe();
                break;
            case R.id.btn_confirm:
                doConfirmAddNewPlan();
                break;
        }
    }

    private void toAddDescribe() {
        initEditView();
        upDescribesData();
    }

    private void doConfirmAddNewPlan() {
        toAddDescribe();
        if (!TextUtils.isEmpty(mEt_jobTitle.getText().toString())
                && !mTv_TimeChoose.getText().toString().equals(getString(R.string.date_picker))){
            mPlans.setSet_time(mTv_TimeChoose.getText().toString());
            mPlans.setTitle(mEt_jobTitle.getText().toString());
            mPlans.setDone_state(getString(R.string.unfinished));
            if (mDescribes.size() > 0){
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelable(NEW_PLAN_REQUEST_CONTENT,mPlans);
                intent.putExtras(bundle);
                setResult(NEW_PLAN_REQUEST_CODE,intent);
                finish();
            }else {
                Toast.makeText(NewPlanActivity.this, R.string.toast_no_plan, Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, R.string.toast_time_pick, Toast.LENGTH_SHORT).show();
        }
    }

    private void upDescribesData() {
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
            mEt_new_describe.setText("");
            mDescribes.add(describe);
            mAdapter.notifyDataSetChanged();
            mRv_workDescribe.smoothScrollToPosition(mDescribes.size()-1);
            mPlans.setDescribes(mDescribes);
        }
    }


    private void toPickDate() {
        Intent intent = new Intent(NewPlanActivity.this, DatePickerActivity.class);
        startActivityForResult(intent, DATE_PICK_REQUEST_CODE);
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
                    mTv_TimeChoose.setText(date);
                }
                break;
        }
    }
}
