package com.telyo.finemoodnote.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.telyo.finemoodnote.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/27.
 */

public class RecyclerViewSetTimeAdapter extends RecyclerView.Adapter<RecyclerViewSetTimeAdapter.SetTimeHolder> {
    private int mMinCount;
    private int mMaxCount;
    private List<String> mData;
    public RecyclerViewSetTimeAdapter(int minCount,int maxCount){
        this.mMaxCount = maxCount;
        this.mMinCount = minCount;
        mData = new ArrayList<>();
        initDate();
    }

    private void initDate() {
        for (int i = mMinCount;i<mMaxCount;i++){
            String timeNumber = "";
            if (i < 10){
                timeNumber = "0" + (i );
            }else {
                timeNumber = String.valueOf(i);
            }
            mData.add(timeNumber);
        }
    }

    @Override
    public SetTimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SetTimeHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_count_item,parent,false));
    }

    @Override
    public void onBindViewHolder(SetTimeHolder holder, int position) {
        holder.mNumber.setText(mData.get(position%mData.size()));
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    class SetTimeHolder extends RecyclerView.ViewHolder{
        TextView mNumber;
        public SetTimeHolder(View itemView) {
            super(itemView);
            mNumber = (TextView) itemView.findViewById(R.id.tv_count);
        }
    }
}
