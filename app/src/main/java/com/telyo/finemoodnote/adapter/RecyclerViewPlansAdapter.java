package com.telyo.finemoodnote.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.telyo.finemoodnote.R;
import com.telyo.finemoodnote.entity.RecyclerPlans;

import java.util.List;

/**
 * Created by Administrator on 2017/7/17.
 */

public class RecyclerViewPlansAdapter extends RecyclerView.Adapter<RecyclerViewPlansAdapter.PlansViewHolder>{
    private List<RecyclerPlans> mData;
    private Context mContext;
    private OnItemLongClickListener onItemLongClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public RecyclerViewPlansAdapter(Context context, List<RecyclerPlans> data){
        this.mData = data;
        this.mContext = context;
    }

    @Override
    public PlansViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recyceler_plans_item,parent,false);
        return new PlansViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PlansViewHolder holder, final int position) {
        holder.tv_date.setText(mData.get(position).getDate());
        holder.tv_done_state.setText(mData.get(position).getDone_state());
        holder.tv_time.setText(mData.get(position).getTime());
        holder.tv_set_time.setText(mData.get(position).getSet_time());
        holder.tv_title.setText(mData.get(position).getTitle());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null){
                    onItemLongClickListener.onLongClick(holder.itemView,position);
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class PlansViewHolder extends RecyclerView.ViewHolder{
        TextView tv_date;
        TextView tv_time;
        TextView tv_done_state;
        TextView tv_title;
        TextView tv_set_time;
        ImageView imageView;

        public PlansViewHolder(View itemView) {
            super(itemView);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_done_state = (TextView) itemView.findViewById(R.id.tv_done_state);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_set_time = (TextView) itemView.findViewById(R.id.tv_set_time);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
    public interface OnItemLongClickListener{
        void onLongClick(View itemView,int position);
    }
}
