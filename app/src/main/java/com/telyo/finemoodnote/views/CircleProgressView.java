package com.telyo.finemoodnote.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;

import com.telyo.finemoodnote.R;

/**
 * Created by Administrator on 2017/7/21.
 */

public class CircleProgressView extends View {
    //边长
    private int width;
    //进度弧的颜色
    private int mProgress_color;
    //进度的文本
    private String mShowText;

    //进度的文本颜色

    //进度文本的size
    private float mTextSize;
    //背景园的半径
    private int mRadio;
    private Context mContext;

    //进度的弧度
    private float mStartAngle;
    private float mSweepAngle;
    private int mProgressAngle;
    private int mProgress;

    public CircleProgressView(Context context) {
        this(context,null);
    }

    public CircleProgressView(Context context,AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context,attrs);

        this.mContext = context;
        initProgressAngle();
        mProgress_color = 0;
        mStartAngle = 360/4;
        mShowText = "0%";
        initSweepAngle();

    }

    private void initProgressAngle() {
        this.mProgressAngle = mProgress *360/100;
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressView);
        mProgress = ta.getInt(R.styleable.CircleProgressView_progress,0);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        setMeasuredDimension(width,width);
        mTextSize = width*1/3;
        mRadio = width/2-20;

    }

    private void initSweepAngle() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f,1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                mSweepAngle = mProgressAngle*value;
                mProgress_color = Color.argb(255
                        ,(int)(255*(1-mProgress*value/100))
                        ,(int)(255*(mProgress*value/100))
                        ,0);
                mShowText = (int)(mProgress*value) + "%";
            }
        });
        animator.setInterpolator(new FastOutSlowInInterpolator());
        animator.setDuration(2000);
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int strockWidth = 16;
        //画字体
        Paint mTextPaint = new Paint();
        Typeface font = Typeface.create("微软雅黑",Typeface.BOLD);
        mTextPaint.setColor(mProgress_color);
        mTextPaint.setTypeface(font);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float textHeight = fm.bottom - fm.top;
        Rect bound = new Rect();
        mTextPaint.getTextBounds(mShowText,0,mShowText.length(),bound);
        int textBoundsWidth = bound.width();
        canvas.drawText(mShowText, (getMeasuredWidth() - textBoundsWidth)/2
                ,(getMeasuredHeight() + textHeight/2)/2  ,mTextPaint);
        //画背景环形
        Paint circlePaint = new Paint();
        circlePaint.setStrokeWidth(strockWidth);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setColor(ContextCompat.getColor(mContext,R.color.colorTextNormal));
        canvas.drawCircle(width/2,width/2,mRadio,circlePaint);
        //画进度弧
        Paint progressPaint = new Paint();
        progressPaint.setColor(mProgress_color);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(strockWidth);

        if (mSweepAngle <= mProgressAngle) {
            canvas.drawArc(new RectF(20,20,width-20,width-20), mStartAngle, mSweepAngle,false,progressPaint);
            invalidate();
        }
    }

    public void setProgress(int progress){
        mSweepAngle = 0;
        mProgress = progress;
        initProgressAngle();
        initSweepAngle();
        invalidate();
    }


}
