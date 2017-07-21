package com.telyo.finemoodnote.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.telyo.finemoodnote.R;
import com.telyo.finemoodnote.entity.RecyclerDescribe;
import com.telyo.finemoodnote.entity.RecyclerPlans;

import java.util.List;

/**
 * Created by Administrator on 2017/7/17.
 */

public class RecyclerViewDescribeAdapter extends RecyclerView.Adapter<RecyclerViewDescribeAdapter.DescribeViewHolder> {
    private List<RecyclerDescribe> mData;
    private OnItemClickListener mOnItemClickListener;
    private OnDeleteItemClickListener mOnDeleteItemClickListener;
    private Context mContext;




    public void setmOnDeleteItemClickListener(OnDeleteItemClickListener mOnDeleteItemClickListener) {
        this.mOnDeleteItemClickListener = mOnDeleteItemClickListener;
    }

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public RecyclerViewDescribeAdapter(Context context, List<RecyclerDescribe> data) {
        this.mContext = context;
        this.mData = data;

    }

    @Override
    public DescribeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recyceler_describe_item, parent, false);
        return new DescribeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DescribeViewHolder holder, final int position) {
        holder.tv_describe.setText(mData.get(position).getDescribe());
        holder.img_delete.setEnabled(!mData.get(position).isFinished());
        holder.tv_level.setText(mData.get(position).getPlanLevel());
        //if (holder.tv_level.getText().toString().equals("重要"))
        //任务的重要程度
        switch (holder.tv_level.getText().toString()){
            case "重要":
                holder.tv_level.setBackgroundColor(ContextCompat.getColor(mContext, R.color.important));
                break;
            case "一般":
                holder.tv_level.setBackgroundColor(ContextCompat.getColor(mContext, R.color.commonly));
                break;
            case "不重要":
                holder.tv_level.setBackgroundColor(ContextCompat.getColor(mContext, R.color.not_important));
                break;
        }

        //标记完成任务
        if (mData.get(position).isFinished()) {
            holder.img_delete.setAlpha(0.0f);
            holder.tv_isFinished.setText(R.string.finished);
            holder.tv_isFinished.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAccent));
        } else {
            holder.img_delete.setAlpha(1.0f);
            holder.tv_isFinished.setText(R.string.unfinished);
            holder.tv_isFinished.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorTextNormal));
        }

        holder.tv_isFinished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(holder, position);
                }
            }
        });
        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnDeleteItemClickListener != null){
                    mOnDeleteItemClickListener.onDeleteItemClick(holder,position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class DescribeViewHolder extends RecyclerView.ViewHolder {
        TextView tv_describe;
        TextView tv_isFinished;
        TextView tv_level;
        ImageView img_delete;
        public DescribeViewHolder(View itemView) {
            super(itemView);
            tv_describe = (TextView) itemView.findViewById(R.id.tv_describe);
            tv_isFinished = (TextView) itemView.findViewById(R.id.tv_isFinished);
            tv_level = (TextView) itemView.findViewById(R.id.tv_level);
            img_delete = (ImageView) itemView.findViewById(R.id.img_delete);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DescribeViewHolder holder, int position);
    }
    public interface OnDeleteItemClickListener {
        void onDeleteItemClick(DescribeViewHolder holder, int position);
    }
    public void addData(int position, RecyclerDescribe data) {
        mData.add(position, data);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, mData.size() - position);
    }

    public void removeData(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
        if (position != mData.size()) { // 这个判断的意义就是如果移除的是最后一个，就不用管它了，
            notifyItemRangeChanged(position, mData.size() - position);
        }
    }

}
