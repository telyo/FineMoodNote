package com.telyo.finemoodnote.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.telyo.finemoodnote.R;

import java.util.List;

/**
 * Created by Administrator on 2017/7/17.
 */

public class RecyclerViewPlans extends RecyclerView.Adapter<RecyclerViewPlans.PlansViewHolder>{
    private List<String> mData;

    public RecyclerViewPlans(List<String> data){
        this.mData = data;
    }

    @Override
    public PlansViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recyceler_plans_item,parent,false);
        return new PlansViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlansViewHolder holder, int position) {
        holder.tv_describe.setText(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class PlansViewHolder extends RecyclerView.ViewHolder{
        TextView tv_describe;
        public PlansViewHolder(View itemView) {
            super(itemView);
            tv_describe = (TextView) itemView.findViewById(R.id.tv_describe);
        }
    }
}
