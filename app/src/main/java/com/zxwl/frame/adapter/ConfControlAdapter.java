package com.zxwl.frame.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zxwl.frame.R;
import com.zxwl.frame.bean.ConfBean;

import java.util.List;

/**
 * Copyright 2015 蓝色互动. All rights reserved.
 * author：hw
 * data:2017/5/3 16:12
 * 会议控制列表的适配器
 */
public class ConfControlAdapter extends RecyclerView.Adapter<ConfControlAdapter.ConfControlHolder> {

    private List<ConfBean> list;

    public ConfControlAdapter(List<ConfBean> list) {
        this.list = list;
    }


    @Override
    public ConfControlHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ConfControlHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conf_control, parent, false));
    }

    @Override
    public void onBindViewHolder(final ConfControlHolder holder, int position) {
        ConfBean confBean = list.get(position);
        //0=View.VISIBLE    4=View.INVISIBLE
        if (confBean.showControl) {
            holder.rlControl.setVisibility(View.VISIBLE);
            holder.rlInfo.setVisibility(View.INVISIBLE);
        } else {
            holder.rlControl.setVisibility(View.INVISIBLE);
            holder.rlInfo.setVisibility(View.VISIBLE);
        }

        holder.tvName.setText(confBean.name);//会议名称
        holder.tvTime.setText(confBean.beginTime);
        holder.tvPhone.setText(confBean.contactTelephone);
        holder.tvPattern.setText("讨论模式");
        //0=周期性视频会议、
        //1=周期性非视频会议、
        //2=视频会议、
        //3=非视频会议、
        //4=即时视频会议、
        //5=即时非视频会议
        //6、周期性子视频会议
        //7、周期性子非视频会议
        String confType = "周期性视频会议";
        switch (confBean.confType) {
            case "0":
                confType = "周期性视频会议";
                break;

            case "1":
                confType = "周期性非视频会议";
                break;

            case "2":
                confType = "视频会议";
                break;

            case "3":
                confType = "非视频会议";
                break;

            case "4":
                confType = "即时视频会议";
                break;

            case "5":
                confType = "即时非视频会议";
                break;

            case "6":
                confType = "周期性子视频会议";
                break;

            case "7":
                confType = "周期性子非视频会议";
                break;

            default:
                break;
        }
        holder.tvType.setText(confType);

        holder.rlContent.setOnClickListener(
                v->{
                    if (null != itemClickListener) {
                        itemClickListener.onClick(holder.getAdapterPosition());
                    }
                });

        holder.tvControl.setOnClickListener(
                v->{
                    if (null != itemClickListener) {
                        itemClickListener.onControl(holder.getAdapterPosition());
                    }
                });
        holder.tvFinish.setOnClickListener(
                v -> {
                    if (null != itemClickListener) {
                        itemClickListener.onFinish(holder.getAdapterPosition());
                    }
                });
    }

    public ConfBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getItemCount() {
        return null != list ? list.size() : 0;
    }


    public void reomve(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }

    public static class ConfControlHolder extends RecyclerView.ViewHolder {
        RelativeLayout rlContent;
        TextView tvName;
        RelativeLayout rlInfo;
        TextView tvTime;
        TextView tvPhone;
        TextView tvPattern;
        TextView tvType;
        RelativeLayout rlControl;
        TextView tvControl;
        TextView tvFinish;

        public ConfControlHolder(View itemView) {
            super(itemView);

            rlContent = (RelativeLayout) itemView.findViewById(R.id.rl_content);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            rlInfo = (RelativeLayout) itemView.findViewById(R.id.rl_info);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvPhone = (TextView) itemView.findViewById(R.id.tv_phone);
            tvPattern = (TextView) itemView.findViewById(R.id.tv_pattern);
            tvType = (TextView) itemView.findViewById(R.id.tv_type);
            rlControl = (RelativeLayout) itemView.findViewById(R.id.rl_control);
            tvControl = (TextView) itemView.findViewById(R.id.tv_control);
            tvFinish = (TextView) itemView.findViewById(R.id.tv_finish);
        }
    }

    private onItemClickListener itemClickListener;

    public void setOnItemClickListener(ConfControlAdapter.onItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface onItemClickListener {
        void onClick(int position);

        void onControl(int position);

        void onFinish(int position);
    }

}
