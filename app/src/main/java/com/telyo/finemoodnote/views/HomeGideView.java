package com.telyo.finemoodnote.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.telyo.finemoodnote.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/7/14.
 */

public class HomeGideView extends LinearLayout {

    private int imgSrc;
    private int imgBorderColor;
    private int imgBorderWidth;

    private String titleText;
    private float titleSize;
    private int titleColor;

    private String contentText;
    private float contentSize;
    private int contentColor;

    private float cardRadius;
    private CardView cardView;
    private CircleImageView circleImageView;
    private TextView titleView;
    private TextView contentView;

    public HomeGideView(Context context) {
        this(context,null);
    }

    public HomeGideView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HomeGideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.HomeGideView);
        View view = LayoutInflater.from(context).inflate(R.layout.home_item_layout,this,false);
        cardView = (CardView) view.findViewById(R.id.card_view);
        circleImageView = (CircleImageView) view.findViewById(R.id.circleImageView);
        titleView = (TextView) view.findViewById(R.id.tv_title);
        contentView = (TextView) view.findViewById(R.id.tv_content);


        cardRadius = ta.getFloat(R.styleable.HomeGideView_card_radius,6);
        cardView.setRadius(cardRadius);
        imgSrc = ta.getResourceId(R.styleable.HomeGideView_img_src,0);
        imgBorderColor = ta.getColor(R.styleable.HomeGideView_img_border_color,ContextCompat.getColor(context,R.color.colorTextNormal));
        imgBorderWidth = ta.getInt(R.styleable.HomeGideView_img_border_width,0);


        titleText = ta.getString(R.styleable.HomeGideView_title_text);
        titleSize = ta.getDimension(R.styleable.HomeGideView_title_text_size,15);
        titleColor = ta.getColor(R.styleable.HomeGideView_title_text_color,
                ContextCompat.getColor(context,android.R.color.black));

        contentText = ta.getString(R.styleable.HomeGideView_content_text);
        contentSize = ta.getDimension(R.styleable.HomeGideView_content_text_size,13);
        contentColor = ta.getColor(R.styleable.HomeGideView_content_text_color,
                ContextCompat.getColor(context,R.color.colorTextNormal));

        circleImageView.setImageResource(imgSrc);
        circleImageView.setBorderWidth(imgBorderWidth);
        circleImageView.setBorderColor(imgBorderColor);


        titleView.setText(titleText);
        titleView.setTextSize(titleSize);
        titleView.setTextColor(titleColor);

        contentView.setText(contentText);
        contentView.setTextSize(contentSize);
        contentView.setTextColor(contentColor);

        addView(view);
    }


}
