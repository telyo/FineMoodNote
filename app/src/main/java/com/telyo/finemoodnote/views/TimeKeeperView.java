package com.telyo.finemoodnote.views;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.telyo.finemoodnote.R;
import com.telyo.finemoodnote.utils.DateUtil;
import com.telyo.finemoodnote.utils.L;

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

public class TimeKeeperView extends RelativeLayout {
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

    private ImageView mWaitingImg;
    private ImageView mFinishImg;
    private int mImgWaitingRes;
    private int mImgFinishRes;
    private int mImg_margin;

    private boolean isFinished = false;
    private int mRadius_1;
    private int mRadius_2;
    private int mRadius_3;
    private int mRadius_4;

    public TimeKeeperView(Context context) {
        this(context, null);
    }


    public TimeKeeperView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeKeeperView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        mContext = context;
        mProgress = 100;
        mRadius_1 = 0;
        mRadius_2 = 0;
        mRadius_3 = 0;
        mRadius_4 = 0;
        initAttrs(context, attrs);
        initText();
        startKeepTime();
    }

    private void initWaitingImg() {

        L.d("initWaitingImg: " + isFinished);
        mWaitingImg = new ImageView(mContext);
        mWaitingImg.setImageResource(mImgWaitingRes);
        RelativeLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.setMargins(0, 0, 0, mImg_margin);
        addView(mWaitingImg, params);
        doRotation(mWaitingImg, new LinearInterpolator(), 2000, 0f, 360f);
        if (getChildCount()>2){
            removeViews(0,getChildCount() - 1);
        }
    }

    private void initFinishImg() {
        L.d("initFinishImg: " + isFinished);
        mFinishImg = new ImageView(mContext);
        mFinishImg.setImageResource(mImgFinishRes);
        RelativeLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        doRotation(mFinishImg, new LinearInterpolator(), 2000, 0f, 50f, 0);
        addView(mFinishImg, params);
        if (getChildCount()>2){
            removeViewAt(0);
            removeViewAt(1);
        }

    }

    private void doRotation(View view, TimeInterpolator value, int duration, float... values) {
        ObjectAnimator rotaAnimator = ObjectAnimator.ofFloat(view, "rotation", values);
        rotaAnimator.setDuration(duration);
        rotaAnimator.setRepeatCount(ValueAnimator.INFINITE);
        rotaAnimator.setInterpolator(value);
        rotaAnimator.start();
    }

    private void initText() {
        hour = "00";
        min = "00";
        second = "03";
        mTimeText = min + ":" + second;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = getMeasuredWidth();
        setMeasuredDimension(mHeight, mHeight);
        mLargeRadio = mHeight * 9 / 20;
        mSmallRadio = mHeight * 8 / 20;
        mImg_margin = mHeight / 6;
        measureChildren(widthMeasureSpec,heightMeasureSpec);
        if (!isFinished) {
            initWaitingImg();
        } else {
            initFinishImg();
        }
        L.d("ChildCount = " + getChildCount());
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TimeKeeperView);
        mImgWaitingRes = ta.getResourceId(R.styleable.TimeKeeperView_img_waiting_resource, R.drawable.waiting);
        mImgFinishRes = ta.getResourceId(R.styleable.TimeKeeperView_img_finish_resource, R.drawable.icon_music);
        ta.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画布抗锯齿
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        if (isFinished) {
            drawFinish(canvas);
        } else {
            drawBg(canvas);
            drawProgress(canvas);
            drawTime(canvas);
        }
    }

    //画计时完成的水波纹背景
    private void drawFinish(Canvas canvas) {
        Paint finishPaint = new Paint();
        finishPaint.setStyle(Paint.Style.FILL);
        finishPaint.setColor(ContextCompat.getColor(mContext, R.color.color_green));
        int x = mHeight / 2;
        int y = mHeight / 2;
        finishPaint.setAlpha(60);
        canvas.drawCircle(x, y, getChildAt(0).getWidth() / 2, finishPaint);
        if (mRadius_1>getChildAt(0).getWidth() / 2) {
            canvas.drawCircle(x, y, mRadius_1, finishPaint);
        }
        if (mRadius_1>getChildAt(0).getWidth() / 2) {
            canvas.drawCircle(x, y, mRadius_2, finishPaint);
        }
        if(mRadius_1>getChildAt(0).getWidth() / 2) {
            canvas.drawCircle(x, y, mRadius_3, finishPaint);
        }
        if(mRadius_1>getChildAt(0).getWidth() / 2) {
            canvas.drawCircle(x, y, mRadius_4, finishPaint);
        }
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
        isFinished = false;
        if (getChildCount() != 0) {
            removeAllViews();
        }
        initWaitingImg();
        startKeepTime();
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
                        if (second == 0) {
                            isFinished = true;
                            if (getChildCount()!=0){
                                removeAllViews();
                            }
                            initFinishImg();
                            setRadius();
                        }
                        mProgress = (second * 100 / mTotalTime);
                        mTimeText = DateUtil.getTime(second);
                        invalidate();
                    }
                });
    }

    private void setRadius() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mRadius_1 = (int) ((mHeight / 2) * value);
                mRadius_2 = mRadius_1 - mHeight / 8;
                if (mRadius_1 > mHeight / 8) {
                }
                mRadius_3 = mRadius_2 - mHeight / 8;
                if (mRadius_2 > mHeight / 8) {
                }
                mRadius_4 = mRadius_3 - mHeight / 8;
                if (mRadius_3 > mHeight / 8) {
                }
                invalidate();
            }
        });
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(2000);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }
}
