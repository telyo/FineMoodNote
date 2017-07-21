package com.telyo.finemoodnote.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.telyo.finemoodnote.R;
import com.telyo.finemoodnote.utils.L;

/**
 * Created by Administrator on 2017/7/21.
 */

public class CircleProgressView extends View {
    //边长
    private int width;
    private int mProgress_color;
    private String mText;
    private int mText_color;

    private Paint mTextPaint;
    private Paint mProgressPaint;
    private float mTextSize;
    private int mRadio;
    public CircleProgressView(Context context) {
        this(context,null);
    }

    public CircleProgressView(Context context,AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context,attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressView);
        mProgress_color = ta.getColor(R.styleable.CircleProgressView_progress_color,
                ContextCompat.getColor(context,R.color.colorPrimary));
        mText_color = ta.getColor(R.styleable.CircleProgressView_progress_color,
                ContextCompat.getColor(context,R.color.colorTextNormal));
        mText = ta.getString(R.styleable.CircleProgressView_text);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        setMeasuredDimension(width,width);
        mTextSize = width*1/3;
        mRadio = width*8/9;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mTextPaint = new Paint();
        Typeface font = Typeface.create("微软雅黑",Typeface.BOLD);
        mTextPaint.setColor(mText_color);
        mTextPaint.setTypeface(font);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float textWidth = (fm.descent -fm.ascent);
        float textHeight = fm.bottom - fm.top;
        L.i("字体宽度 =" +textWidth+ " 字体高度=" +textHeight+ " 画布边长=" + getMeasuredHeight()+ " 字符个数" + mText.length());
        canvas.drawText(mText, (getMeasuredWidth() - textWidth)/2,(getMeasuredHeight() + textHeight/2)/2  ,mTextPaint);
    }
}
