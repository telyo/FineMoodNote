package com.telyo.finemoodnote.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.telyo.finemoodnote.R;
import com.telyo.finemoodnote.adapter.RecyclerViewSetTimeAdapter;

/**
 * Created by Administrator on 2017/7/14.
 */

public class MemoActivity extends BaseThemeActivity {
    private RecyclerViewSetTimeAdapter mHourAdapter;
    private RecyclerView mHourRecyclerView;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        initView();
    }

    private void initView() {
        mHourRecyclerView = (RecyclerView) findViewById(R.id.rv_hour);
        LinearLayoutManager hourManager = new LinearLayoutManager(
                this,LinearLayoutManager.VERTICAL,false);
        mHourAdapter = new RecyclerViewSetTimeAdapter(0,24);
        mHourRecyclerView.setLayoutManager(hourManager);
        mHourRecyclerView.setAdapter(mHourAdapter);
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
