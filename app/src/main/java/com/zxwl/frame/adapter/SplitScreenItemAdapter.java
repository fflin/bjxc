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
 * data:2017/5/16 14:34
 */
public class SplitScreenItemAdapter extends RecyclerView.Adapter<SplitScreenItemAdapter.SplitScreenHolder> {
    private List<Site> siteList;

    public SplitScreenItemAdapter(List<Site> siteList) {
        this.siteList = siteList;
    }

    @Override
    public SplitScreenHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SplitScreenHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_check_text, parent,false));
    }

    @Override
    public void onBindViewHolder(SplitScreenHolder holder, int position) {
        Site site = siteList.get(position);
        holder.tvContent.setCompoundDrawablesWithIntrinsicBounds(site.splitCheck ? R.mipmap.icon_login_check_on : R.mipmap.icon_login_check_off, 0, 0, 0);
        holder.tvContent.setText(site.siteInfo.name);
        holder.tvContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=itemClickListener){
                    itemClickListener.onClick(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return siteList.size();
    }

    public Site getItemSite(int position) {
        return siteList.get(position);
    }

    public static class SplitScreenHolder extends RecyclerView.ViewHolder {
        private TextView tvContent;

        public SplitScreenHolder(View itemView) {
            super(itemView);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }

    public void add(Site site) {
        siteList.add(site);
    }

    public List<Site> getSiteList() {
        return siteList;
    }


    public void removeAll() {
        siteList.clear();
    }

    private onItemClickListener itemClickListener;

    public void setOnItemClickListener(onItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface onItemClickListener {
        void onClick(int position);
    }
}
