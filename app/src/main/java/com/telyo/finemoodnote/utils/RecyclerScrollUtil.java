package com.telyo.finemoodnote.utils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Administrator on 2017/7/20.
 */

public class RecyclerScrollUtil {

    public static void moveToPosition(RecyclerView mRecyclerView,int position, LinearLayoutManager mLinearLayoutManager,boolean move) {
        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        int firstItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        int lastItem = mLinearLayoutManager.findLastVisibleItemPosition();
        //然后区分情况
        if (position <= firstItem ){
            //当要置顶的项在当前显示的第一个项的前面时
            mRecyclerView.scrollToPosition(position);
        }else if ( position <= lastItem ){
            //当要置顶的项已经在屏幕上显示时
            int top = mRecyclerView.getChildAt(position - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        }else{
            //当要置顶的项在当前显示的最后一项的后面时
            mRecyclerView.scrollToPosition(position);
            //这里这个变量是用在RecyclerView滚动监听里面的
            move = true;
        }

    }
}
