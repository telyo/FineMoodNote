package com.telyo.finemoodnote.views;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.telyo.finemoodnote.R;
import com.telyo.finemoodnote.utils.DateUtil;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.telyo.finemoodnote.utils.Constants.pi;

/**
 * Created by Administrator on 2017/7/25.
 */

public class CountDownView extends LinearLayout {
    private int mHeight;
    private int mLargeRadio;
    private int mSmallRadio;
    private Context mContext;
    private float mProgress;

    private String hour;
    private String min;
    private String second;

    private static final int WITH_HOUR = 001;

    private static final int WITHOUT_HOUR = 000;
    private static int TIME_STATE = WITHOUT_HOUR;
    private long mTotalTime;
    private String mTimeText;

    private ImageView mImg;
    private int mImgRes;

    public CountDownView(Context context) {
        this(context, null);
    }


    public CountDownView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        mContext = context;
        initAttrs(context, attrs);
        initText();
        initImg();
        startKeepTime();
    }

    private void initImg() {
        setGravity(Gravity.BOTTOM | Gravity.CENTER);
        mImg = new ImageView(mContext);
        LinearLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
        , ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,0,0,100);
        mImg.setImageResource(mImgRes);
        addView(mImg,params);
        ObjectAnimator animator = ObjectAnimator.ofFloat(mImg,"rotation",0f,360f);
        animator.setDuration(2000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }

    private void initText() {
        hour = "00";
        min = "00";
        second = "10";
        mTimeText = min + ":" + second;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = getMeasuredWidth();
        int width = getMeasuredHeight();
        setMeasuredDimension(mHeight, width);
        mLargeRadio = mHeight / 2 - 10;
        mSmallRadio = mHeight / 2 - 75;
    }


    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CountDownView);
        mImgRes = ta.getResourceId(R.styleable.CountDownView_img_resource,R.drawable.waiting);
        ta.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBg(canvas);
        drawProgress(canvas);
        drawTime(canvas);
    }

    //画时间
    private void drawTime(Canvas canvas) {
        Paint timePaint = new Paint();
        timePaint.setColor(ContextCompat.getColor(mContext, android.R.color.black));
        Typeface font = Typeface.create("微软雅黑", Typeface.BOLD);
        timePaint.setTypeface(font);
        timePaint.setTextAlign(Paint.Align.LEFT);
        //获取字体的高度
        Paint.FontMetrics fm = timePaint.getFontMetrics();
        float textHeight = fm.bottom - fm.top;
        //获取字体的宽度
        Rect bound = new Rect();
        switch (TIME_STATE) {
            case WITH_HOUR:
                timePaint.setTextSize(mHeight / 8);

                break;
            case WITHOUT_HOUR:
                timePaint.setTextSize(mHeight / 7);
                break;
        }
        timePaint.getTextBounds(mTimeText, 0, mTimeText.length(), bound);
        int textBoundsWidth = bound.width();
        //开始画
        canvas.drawText(mTimeText, (getMeasuredWidth() - textBoundsWidth) / 2
                , (getMeasuredHeight() + textHeight / 2) / 2, timePaint);
    }

    //画进度
    private void drawProgress(Canvas canvas) {
        Paint scalePaintPro = new Paint();
        scalePaintPro.setAntiAlias(true);
        scalePaintPro.setStyle(Paint.Style.STROKE);
        scalePaintPro.setStrokeWidth(5);
        scalePaintPro.setColor(ContextCompat.getColor(mContext, R.color.colorAccent));
        double sweepAngle = pi + 2 * pi * mProgress / 100;
        for (double angle = pi; angle < sweepAngle; angle += (2 * pi / 100)) {
            float startX = (float) (mHeight / 2 - mSmallRadio * Math.sin(angle));
            float startY = (float) (mHeight / 2 + mSmallRadio * Math.cos(angle));
            float endX = (float) (mHeight / 2 - mLargeRadio * Math.sin(angle));
            float endY = (float) (mHeight / 2 + mLargeRadio * Math.cos(angle));
            canvas.drawLine(startX, startY, endX, endY, scalePaintPro);
        }
    }


    //画背景刻度
    private void drawBg(Canvas canvas) {
        Paint scalePaintBg = new Paint();
        scalePaintBg.setAntiAlias(true);
        scalePaintBg.setStyle(Paint.Style.STROKE);
        scalePaintBg.setStrokeWidth(5);
        scalePaintBg.setColor(ContextCompat.getColor(mContext, R.color.colorTextNormal));
        for (double angle = pi; angle < 3 * pi; angle += (2 * pi / 100)) {
            float startX = (float) (mHeight / 2 - mSmallRadio * Math.sin(angle));
            float startY = (float) (mHeight / 2 + mSmallRadio * Math.cos(angle));
            float endX = (float) (mHeight / 2 - mLargeRadio * Math.sin(angle));
            float endY = (float) (mHeight / 2 + mLargeRadio * Math.cos(angle));
            canvas.drawLine(startX, startY, endX, endY, scalePaintBg);
        }
    }

    public void setTime(String hour, String min, String second) {
        this.hour = hour;
        this.min = min;
        this.second = second;
        if (hour.equals("00")) {
            TIME_STATE = WITHOUT_HOUR;
            mTimeText = min + ":" + second;
        } else {
            TIME_STATE = WITH_HOUR;
            mTimeText = hour + ":" + min + ":" + second;
        }
        startKeepTime();
        invalidate();
    }

    public void startKeepTime() {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        mTotalTime = Integer.valueOf(hour) * hh
                + Integer.valueOf(min) * mi + Integer.valueOf(second) * ss;

        Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> e) throws Exception {
                long currentTime = mTotalTime;
                while (currentTime > 0) {
                    Thread.sleep(1000);
                    currentTime -= 1000;
                    e.onNext(currentTime);
                }
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long second) throws Exception {
                        mTimeText = DateUtil.getTime(second);
                        mProgress = (second * 100 / mTotalTime);
                        invalidate();
                    }
                });
    }
}
