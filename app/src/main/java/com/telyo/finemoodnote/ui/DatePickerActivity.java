package com.telyo.finemoodnote.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.telyo.finemoodnote.R;

import static com.telyo.finemoodnote.ui.PlanActivity.DATE_PICK_REQUEST_CODE;

/**
 * Created by Administrator on 2017/7/18.
 */

public class DatePickerActivity extends BaseThemeActivity {
    private DatePicker mDatePicker;
    private Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initView();
    }

    private void initView() {
        mDatePicker = (DatePicker) findViewById(R.id.date_picker);
        mButton = (Button) findViewById(R.id.btn_confirm);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = mDatePicker.getYear() + "-"
                        + (mDatePicker.getMonth() + 1) + "-"
                        + mDatePicker.getDayOfMonth();
                Intent intent = new Intent();
                intent.putExtra("date",date);
                DatePickerActivity.this.setResult(DATE_PICK_REQUEST_CODE,intent);
                finish();
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
