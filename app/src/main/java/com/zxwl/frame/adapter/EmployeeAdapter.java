package com.zxwl.frame.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.android.percent.support.PercentRelativeLayout;
import com.zxwl.frame.R;
import com.zxwl.frame.bean.ClickEvent;
import com.zxwl.frame.bean.Employee;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 *通讯录左侧人员列表适配器
 * Created by asus-pc on 2017/4/27.
 */

public class EmployeeAdapter extends BaseAdapter {
    private List<Employee> data;
    private Context context;
    private LayoutInflater inflater;
    private EventBus eventBus;
    public EmployeeAdapter(Context context, List<Employee> data, EventBus eventBus){
        this.context = context;
        this.data = data;
        this.eventBus = eventBus;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView==null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.choosedmeeting_item,parent,false);
            viewHolder.rl = (PercentRelativeLayout) convertView.findViewById(R.id.employee_rl);
            viewHolder.ivDot = (ImageView)convertView.findViewById(R.id.ivDot);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvEmployeeName);
            viewHolder.tvNo = (TextView) convertView.findViewById(R.id.tvEmployeeNo);
            viewHolder.tvType = (TextView) convertView.findViewById(R.id.tvEmployeeType);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final int id = Integer.valueOf(data.get(position).getId());
        viewHolder.tvNo.setTag(id);
        //Log.i("TAG","id=========="+data.get(position).getName()+","+"choose==="+data.get(position).ischecked());
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (finalViewHolder.tvNo.getTag()!=null&& finalViewHolder.tvNo.getTag().equals(id)){
                        if (!data.get(position).ischecked()){//通讯录左侧人员列表条目被点击选中，条目变色
                            view.setBackgroundResource(R.drawable.item_bg);
                            finalViewHolder.ivDot.setBackgroundResource(R.mipmap.dot_red);
                            finalViewHolder.tvNo.setTextColor(Color.RED);
                            finalViewHolder.tvName.setTextColor(Color.RED);
                            finalViewHolder.tvType.setTextColor(Color.RED);
                            data.get(position).setIschecked(true);
                            eventBus.post(new ClickEvent(true,position));//发送选中信息到通讯录activity
                        }else{//通讯录左侧人员列表条目被点击取消选中，条目变色
                            view.setBackgroundResource(R.drawable.item_bg1);
                            finalViewHolder.ivDot.setBackgroundResource(R.mipmap.dot_gray);
                            finalViewHolder.tvNo.setTextColor(Color.BLACK);
                            finalViewHolder.tvName.setTextColor(Color.BLACK);
                            finalViewHolder.tvType.setTextColor(Color.BLACK);
                            data.get(position).setIschecked(false);
                            eventBus.post(new ClickEvent(false,position));//发送取消选中信息到通讯录activity
                        }

                    }

                }


        });
        if (data.get(position).ischecked()){
            viewHolder.rl.setBackgroundResource(R.drawable.item_bg);
            viewHolder.ivDot.setBackgroundResource(R.mipmap.dot_red);
        }else{
            viewHolder.rl.setBackgroundResource(R.drawable.item_bg1);
            viewHolder.ivDot.setBackgroundResource(R.mipmap.dot_gray);
        }

        viewHolder.tvName.setText(data.get(position).getName());
        viewHolder.tvNo.setText(data.get(position).getTerminalId());
        viewHolder.tvType.setText(data.get(position).getTypeName());
        return convertView;
    }

    static class ViewHolder{
        TextView tvName;
        TextView tvNo;
        TextView tvType;
        ImageView ivDot;
        PercentRelativeLayout rl;
    }

}

