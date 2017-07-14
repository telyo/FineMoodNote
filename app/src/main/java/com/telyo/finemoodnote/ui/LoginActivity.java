package com.telyo.finemoodnote.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.telyo.finemoodnote.R;

import static com.telyo.finemoodnote.MainActivity.LOGIN_REQUEST_CODE;

/**
 * Created by Administrator on 2017/7/14.
 */

public class LoginActivity extends BaseThemeActivity {
    public static final String isLogin = "isLogin";
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
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

    private void doLogin(){
        //TODO 成功
        Intent intent = new Intent();
        intent.putExtra(isLogin,true);
        setResult(LOGIN_REQUEST_CODE,intent);
        finish();
        //TODO 不成功
    }

    public void loginClick(View view){
        doLogin();
    }


}
