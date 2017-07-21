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
import com.telyo.finemoodnote.utils.DateUtil;
import com.telyo.finemoodnote.utils.OrderUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/7/17.
 */

public class RecyclerViewPlansAdapter extends RecyclerView.Adapter<RecyclerViewPlansAdapter.PlansViewHolder> {
    private List<RecyclerPlans> mData;
    private Context mContext;
    private OnItemLongClickListener onItemLongClickListener;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public RecyclerViewPlansAdapter(Context context, List<RecyclerPlans> data) {
        data = OrderUtils.orderPlansByDate(data, context.getString(R.string.date_type_ymd));
        this.mData = data;
        this.mContext = context;
    }

    @Override
    public PlansViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recyceler_plans_item, parent, false);
        return new PlansViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PlansViewHolder holder, final int position) {
        holder.tv_date.setText(mData.get(position).getDate());
        holder.tv_done_state.setText(mData.get(position).getDone_state());
        holder.tv_time.setText(mData.get(position).getTime());
        holder.tv_set_time.setText(mData.get(position).getSet_time());
        holder.tv_title.setText(mData.get(position).getTitle());
        if (position == DateUtil.getCurrentPlanPosition(mData, mContext.getString(R.string.date_type_ymd))) {
            holder.imageView.setImageResource(R.drawable.icon_ring_resent);
            holder.itemView.setBackgroundResource(R.drawable.click_curennt_plan_bg);
        } else {
            holder.imageView.setImageResource(R.drawable.icon_ring_normal);
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onLongClick(holder.itemView, position);
                    return true;
                }
                return false;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(holder.itemView, position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class PlansViewHolder extends RecyclerView.ViewHolder {
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

    public interface OnItemLongClickListener {
        void onLongClick(View itemView, int position);
    }

    public interface OnItemClickListener {
        void onClick(View itemView, int position);
    }

    public void addData(int position, RecyclerPlans data) {
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
