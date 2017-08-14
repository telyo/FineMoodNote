

# 随心记中自定义的View： 

## 一、 CircleProgressView

在这个项目里，根据需求自定义了一些View，下面这个是一个自定义圆形进度条、先上一波图：

![](https://raw.githubusercontent.com/telyo/FineMoodNote/master/GIF.gif)

效果分析：

1、一个背景圆，一个弧度不固定的弧表示进度

2、圆背景灰色，弧形颜色随进度的增加改变

3、圆环类部用文字实时显示进度的百分比

4、环与文字的颜色随着进度的增加而改变

5、进度加上一个动画显示，在一定时间类增加到最终进度

5、监听焦点变化，从失去焦点到重新获取焦点，就重新开始显示动画

### 代码：

一、测量宽高

```java
@Override
protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  	//设置控件的宽高相等
    width = getMeasuredWidth();
    setMeasuredDimension(width,width);
  	//文字的大小
    mTextSize = width*1/3;
  	//圆的半径
    mRadio = width/2-20;

}
```

二、画图

```java
//画字体
Paint mTextPaint = new Paint();
Typeface font = Typeface.create("微软雅黑",Typeface.BOLD);
mTextPaint.setColor(mProgress_color);
mTextPaint.setTypeface(font);
mTextPaint.setTextSize(mTextSize);
mTextPaint.setTextAlign(Paint.Align.LEFT);
mTextPaint.setAntiAlias(true);
//获取字体的高度
Paint.FontMetrics fm = mTextPaint.getFontMetrics();
float textHeight = fm.bottom - fm.top;
//获取字体的宽度
Rect bound = new Rect();
mTextPaint.getTextBounds(mShowText,0,mShowText.length(),bound);
int textBoundsWidth = bound.width();
//开始画字体
canvas.drawText(mShowText, (getMeasuredWidth() - textBoundsWidth)/2
        ,(getMeasuredHeight() + textHeight/2)/2  ,mTextPaint);

//画背景环形
Paint circlePaint = new Paint();
circlePaint.setStrokeWidth(mStrokeWidth);
circlePaint.setStyle(Paint.Style.STROKE);
circlePaint.setColor(ContextCompat.getColor(mContext,R.color.colorTextNormal));
circlePaint.setAntiAlias(true);
canvas.drawCircle(width/2,width/2,mRadio,circlePaint);
//画进度弧
Paint progressPaint = new Paint();
progressPaint.setColor(mProgress_color);
progressPaint.setStyle(Paint.Style.STROKE);
progressPaint.setStrokeWidth(mStrokeWidth);
progressPaint.setAntiAlias(true);

if (mSweepAngle <= mProgressAngle) {
  	//动画效果
    canvas.drawArc(new RectF(20,20,width-20,width-20), mStartAngle, mSweepAngle,false,progressPaint);
    invalidate();
}
```

四、属性动画控制进度和画笔颜色

```java
ValueAnimator animator = ValueAnimator.ofFloat(0f,1);
animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        float value = (float) valueAnimator.getAnimatedValue();
      //弧度缓慢增加到最终进度
        mSweepAngle = mProgressAngle*value;
      //颜色也随子变化 （由红到绿）
        mProgress_color = Color.argb(255
                ,(int)(255*(1-mProgress*value/100))
                ,(int)(255*(mProgress*value/100))
                ,0);
      //文字也随之变化
        mShowText = (int)(mProgress*value) + "%";
    }
});
animator.setInterpolator(new FastOutSlowInInterpolator());
animator.setDuration(2000);
animator.start();
```

五、需要用到的自己设置的属性，我这里谢了进度与环的宽度，其他的比如说背景圆的颜色等也可以自己设置

```java
TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressView);
mProgress = ta.getInt(R.styleable.CircleProgressView_progress,0);
//环的宽度
mStrokeWidth  = ta.getInt(R.styleable.CircleProgressView_strokeWidth,35);
```

最终在布局中就可以用了

```xml-dtd
<baoming.views.CircleProgressView
    android:id="@+id/progressView"
    android:layout_width="230dp"
    android:layout_height="wrap_content"
    android:layout_centerHorizontal="true"
    android:layout_marginTop="10dp"
    app:progress="80" />
```

六、很多时候要动态的设置进度直接加上

```java
public void setProgress(int progress){
    mSweepAngle = 0;
    this.mProgress = progress;
    invalidate();
    }
```

七、焦点监听

```java
public void setFocusChanges() {
    final int tempProgress  = mProgress;
  	//设置主动获取焦点
    setFocusableInTouchMode(true);
    setFocusable(true);
    requestFocusFromTouch();
    requestFocus();
  	//添加监听
    setOnFocusChangeListener(new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus){
              //获取焦点时进度为当前进度开始动画
                setProgress(tempProgress);
            }else {
               //失去焦点时设置进度为0
                setProgress(0);
            }
        }
    });
}
```

代码里的引用就是这样了

```java
CircleProgressView progressView = (CircleProgressView) v.findViewById(R.id.progressView);
progressView.setProgress(80);
progressView.setFocusChanges();
```

## 二、TimeKeeperView

备忘录需要一个计时器，勉强写了一个计时器的View

还是先看看效果图：

![](https://raw.githubusercontent.com/telyo/FineMoodNote/master/time.gif)

### 一、功能分析

1、背景是一个灰色的像时钟刻度组成的环

2、还是有表示进度的弧（也是由类似时钟的刻度组成的）

3、里面也有随进度变化的文字

4、最下面有一个旋转动画的图片

5、当倒计时完成的时候改变了View的显示

6、完成时的View显示为一张做摆动动画的图片

7、重整个View 的中心画4个半径的等差关系并且逐渐变大（动画效果）的圆，画笔设置一样的透明度。就成了水波纹效果啦。（很丑还在改进中....）

好了直接上代码：

先看看onDraw()里的代码：

```java
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
```

主要是根据进度的状态作画，没有完成就画背景的刻度环、进度的刻度弧、显示进度的文字；计时完成就直接画完成后的状态，也就是水波纹哪个。总的控制就是一个boolean类型的isFinished。

然后就是画画了！谁怕谁

画刻度，这个还是很好理解的 效果是相当于把一个环分成了N等分，但是怎么做呢？

```java
//画背景刻度
private void drawBg(Canvas canvas) {
    Paint scalePaintBg = new Paint();
    scalePaintBg.setAntiAlias(true);
    scalePaintBg.setStyle(Paint.Style.STROKE);
    scalePaintBg.setStrokeWidth(5);
    scalePaintBg.setColor(ContextCompat.getColor(mContext, R.color.colorTextNormal));
  //关键逻辑是每个刻度看成是两个圆点相同，半径不同的圆，每隔一段角度 用大圆的半径减去小圆的半径,一圈后就得到了
  //一圈的刻度，实际上就是按照这些路径画N条直线就形成了环形刻度效果。
  //这样的就用到了正余弦函数，通过大圆半径、小圆半径、和偏移角度，来计算每条刻度的开始坐标和终点坐标
  // for循环，划一圈
    for (double angle = pi; angle < 3 * pi; angle += (2 * pi / 100)) {
        float startX = (float) (mHeight / 2 - mSmallRadio * Math.sin(angle));
        float startY = (float) (mHeight / 2 + mSmallRadio * Math.cos(angle));
        float endX = (float) (mHeight / 2 - mLargeRadio * Math.sin(angle));
        float endY = (float) (mHeight / 2 + mLargeRadio * Math.cos(angle));
        canvas.drawLine(startX, startY, endX, endY, scalePaintBg);
    }
}
```

背景刻度完成、进度的刻度也一样

```java
//画进度
private void drawProgress(Canvas canvas) {
    Paint scalePaintPro = new Paint();
    scalePaintPro.setAntiAlias(true);
    scalePaintPro.setStyle(Paint.Style.STROKE);
    scalePaintPro.setStrokeWidth(5);
    scalePaintPro.setColor(ContextCompat.getColor(mContext, R.color.colorAccent));
  	//这个弧度就表示进度了
    double sweepAngle = pi + 2 * pi * mProgress / 100;
    for (double angle = pi; angle < sweepAngle; angle += (2 * pi / 100)) {
        float startX = (float) (mHeight / 2 - mSmallRadio * Math.sin(angle));
        float startY = (float) (mHeight / 2 + mSmallRadio * Math.cos(angle));
        float endX = (float) (mHeight / 2 - mLargeRadio * Math.sin(angle));
        float endY = (float) (mHeight / 2 + mLargeRadio * Math.cos(angle));
        canvas.drawLine(startX, startY, endX, endY, scalePaintPro);
    }
}
```

然后就是画计时的文字：

```java
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
  //这里我设置了根据计时，是否超过一小时的显示状态，超过一小时就画“小时：分钟：秒钟”，没有超过就画“分钟：秒钟”
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
    //开始画，mTimeText通过set方法传入
    canvas.drawText(mTimeText, (getMeasuredWidth() - textBoundsWidth) / 2
            , (getMeasuredHeight() + textHeight / 2) / 2, timePaint);
}
```

完成时画的水波纹，有点简陋，将就一下

```java
//画计时完成的水波纹背景
private void drawFinish(Canvas canvas) {
    Paint finishPaint = new Paint();
    finishPaint.setStyle(Paint.Style.FILL);
    finishPaint.setColor(ContextCompat.getColor(mContext, R.color.color_green));
    int x = mHeight / 2;
    int y = mHeight / 2;
 	 //设置透明度
    finishPaint.setAlpha(60);
  //开始画圆
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
```

这四个圆的半径关系，以及变化的动画就直接设置在下面了

```java
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
```

主要的显示就是上面这些了，如何计时，我是用的RxJava异步实现的具体操作如下

```java
public void startKeepTime() {
  //需要做一下毫秒的转换
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
             	//递减
                currentTime -= 1000;
                e.onNext(currentTime);
            }
        }
    }).subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<Long>() {
                @Override
                public void accept(Long second) throws Exception {
                  //接收到的时间为0的时候就表示完成了  
                  if (second == 0) {
                    //完成标记
                        isFinished = true;
                        if (getChildCount()!=0){
                          //移除计时的ImageView
                            removeAllViews();
                        }
                     //添加完成时闹铃的ImageView
                        initFinishImg();
                    //设置水波纹动画4个View的半径
                        setRadius();
                    }
                  //计算进度
                    mProgress = (second * 100 / mTotalTime);
                  //更新计时的文字
                    mTimeText = DateUtil.getTime(second);
                    invalidate();
                }
            });
}
```

当然还是有很多需要修改的地方，现在先记录下思路..继续码代码去了！