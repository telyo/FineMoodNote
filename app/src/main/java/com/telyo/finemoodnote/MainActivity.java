package com.telyo.finemoodnote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.telyo.finemoodnote.ui.ContactsActivity;
import com.telyo.finemoodnote.ui.LoginActivity;
import com.telyo.finemoodnote.ui.MemoActivity;
import com.telyo.finemoodnote.ui.MessageActivity;
import com.telyo.finemoodnote.ui.PlanActivity;
import com.telyo.finemoodnote.views.HomeGideView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private HomeGideView mHomePlan;
    private HomeGideView mHomeMemo;
    private HomeGideView mHomeContacts;
    private HomeGideView mHomeMessage;
    private RelativeLayout mRl_beforeLogin;
    private LinearLayout mLl_afterLogin;
    private Button mBtn_login;
    public static final int LOGIN_REQUEST_CODE = 01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.home_menu);
        }

        initView();
    }

    private void initView() {
        mHomePlan = (HomeGideView) findViewById(R.id.home_plan);
        mHomePlan.setOnClickListener(this);
        mHomeMemo = (HomeGideView) findViewById(R.id.home_memo);
        mHomeMemo.setOnClickListener(this);
        mHomeContacts = (HomeGideView) findViewById(R.id.home_contact);
        mHomeContacts.setOnClickListener(this);
        mHomeMessage = (HomeGideView) findViewById(R.id.home_message);
        mHomeMessage.setOnClickListener(this);

        mRl_beforeLogin = (RelativeLayout) findViewById(R.id.rl_beforeLogin);
        mLl_afterLogin = (LinearLayout) findViewById(R.id.ll_afterLogin);

        mBtn_login = (Button) findViewById(R.id.btn_login);
        mBtn_login.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Toast.makeText(this, "to 我", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_plan:
                startPlanActivity();
                break;
            case R.id.home_memo:
                startMemoActivity();
                break;
            case R.id.home_contact:
                startContactActivity();
                break;
            case R.id.home_message:
                startMessageActivity();
                break;

            case R.id.btn_login:
                startLoginActivity();
                break;
        }
    }

    //四大主要功能页面跳转
    private void startPlanActivity() {
        Intent intent = new Intent(this,PlanActivity.class);
        startActivity(intent);
    }

    private void startMemoActivity() {
        Intent intent = new Intent(this,MemoActivity.class);
        startActivity(intent);
    }

    private void startContactActivity() {
        Intent intent = new Intent(this,ContactsActivity.class);
        startActivity(intent);
    }

    private void startMessageActivity() {
        Intent intent = new Intent(this,MessageActivity.class);
        startActivity(intent);
    }

    //登录
    private void startLoginActivity() {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivityForResult(intent, LOGIN_REQUEST_CODE);

    }

    private void setIsLoginView(boolean isLogin){
        if (isLogin){
            mRl_beforeLogin.setVisibility(View.GONE);
            mLl_afterLogin.setVisibility(View.VISIBLE);
        }else {
            mLl_afterLogin.setVisibility(View.GONE);
            mRl_beforeLogin.setVisibility(View.VISIBLE);
        }
    }
    private void refreshView() {
        //TODO 刷新主界面显示
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != MainActivity.this.RESULT_CANCELED){
            switch (requestCode){
                case LOGIN_REQUEST_CODE:
                    //TODO 判断登录返回值
                    setIsLoginView(true);
                    refreshView();
                    break;
            }
        }
    }

}
