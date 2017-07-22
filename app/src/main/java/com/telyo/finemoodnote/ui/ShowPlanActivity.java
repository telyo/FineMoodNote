package com.telyo.finemoodnote.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.telyo.finemoodnote.R;
import com.telyo.finemoodnote.views.CircleProgressView;

/**
 * Created by Administrator on 2017/7/21.
 */

public class ShowPlanActivity extends BaseThemeActivity{

    private CircleProgressView mProgressView;
    private Button mBtn_set_progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_plan);

        mProgressView = (CircleProgressView) findViewById(R.id.progressView);
        mBtn_set_progress = (Button) findViewById(R.id.btn_setProgress);
        mBtn_set_progress.setOnClickListener(new View.OnClickListener() {
            int i = 0;
            @Override
            public void onClick(View view) {
                mProgressView.setProgress(50);
                i +=10;
            }
        });
    }
}
