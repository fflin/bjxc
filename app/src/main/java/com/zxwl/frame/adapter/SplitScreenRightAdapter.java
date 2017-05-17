package com.zxwl.frame.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxwl.frame.R;
import com.zxwl.frame.bean.Site;

import java.util.List;

/**
 * author：hw
 * data:2017/5/17 11:32
 */

public class SplitScreenRightAdapter extends RecyclerView.Adapter<SplitScreenRightAdapter.SplitRightHolder> {
    private List<Site> siteList;

    public SplitScreenRightAdapter(List<Site> siteList) {
        this.siteList = siteList;
    }

    @Override
    public SplitRightHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SplitRightHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_split_right,parent,false));
    }

    @Override
    public void onBindViewHolder(SplitRightHolder holder, int position) {
        holder.tvContent.setText(siteList.get(position).siteInfo.name);
    }

    @Override
    public int getItemCount() {
        return null != siteList ? siteList.size() : 0;
    }

    public void addAll(List<Site> siteList) {
        this.siteList.addAll(siteList);
        notifyDataSetChanged();
    }

    public static class SplitRightHolder extends RecyclerView.ViewHolder {
        private TextView tvContent;

        public SplitRightHolder(View itemView) {
            super(itemView);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }
}
