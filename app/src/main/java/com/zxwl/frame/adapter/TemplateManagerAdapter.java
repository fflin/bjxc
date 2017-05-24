package com.zxwl.frame.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.zxwl.frame.R;
import com.zxwl.frame.bean.MeetingTemplate;

import java.util.ArrayList;

/**
 * Created by asus-pc on 2017/5/22.
 */

public class TemplateManagerAdapter extends RecyclerView.Adapter<TemplateManagerAdapter.TemplateManagerViewHolder> {

    private Activity mContext;
    private ArrayList<MeetingTemplate> datas;
    private MaterialDialog dialog;

    public TemplateManagerAdapter(Activity mContext, ArrayList<MeetingTemplate> datas) {
        this.mContext = mContext;
        this.datas = datas;
    }

    @Override
    public TemplateManagerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_template_manager,parent,false);
        return new TemplateManagerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TemplateManagerViewHolder holder, final int position) {
        holder.tvItemNum.setText(datas.get(position).getNo());
        holder.tvItemMeetingName.setText(datas.get(position).getMeetingName());
        holder.tvItemMeetingNo.setText(datas.get(position).getMeetingNo());
        holder.tvItemMeetingType.setText(datas.get(position).getMeetingType());
        holder.tvItemMeetingLevel.setText(datas.get(position).getMeetingLevel());
        //修改
        holder.tvItemMeetingUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"暂未开发!",Toast.LENGTH_SHORT).show();
            }
        });
        if (datas.get(position).isDefault()){
            holder.tvItemMeetingSetting.setText("默认模板");
            holder.tvItemMeetingSetting.setBackgroundResource(R.drawable.bg_template_has_setting_bt);
        }else{
            holder.tvItemMeetingSetting.setText("设为默认");
            holder.tvItemMeetingSetting.setBackgroundResource(R.drawable.bg_template_setting_bt);
        }
        //设置默认模板
        holder.tvItemMeetingSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datas.get(position).setDefault(true);
                holder.tvItemMeetingSetting.setText("默认模板");
                holder.tvItemMeetingSetting.setBackgroundResource(R.drawable.bg_template_has_setting_bt);
            }
        });
        //删除
        holder.tvItemMeetingDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new MaterialDialog.Builder(mContext)
                        .customView(initDialogContent(datas.get(position).getMeetingName(),position), false)
                        .build();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                Window dialogWindow = dialog.getWindow();
                WindowManager m = mContext.getWindowManager();
                Display d = m.getDefaultDisplay(); // 获取屏幕宽、高度
                WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
                p.height = (int) (d.getHeight() * 0.4); // 高度设置为屏幕的0.4，根据实际情况调整
                p.width = (int) (d.getWidth() * 0.5); // 宽度设置为屏幕的0.5，根据实际情况调整
                dialogWindow.setAttributes(p);
            }
        });
        //立即召开
        holder.tvItemMeetingImmateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"暂未开发!",Toast.LENGTH_SHORT).show();
            }
        });
        //预约召开
        holder.tvItemMeetingReservationStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"暂未开发!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private View initDialogContent(String meetingName, final int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_template_manager_delete_dialog,null);
        TextView tvContent = (TextView) view.findViewById(R.id.tvDeleteContent);
        TextView delete = (TextView) view.findViewById(R.id.tv_delete);
        TextView cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tvContent.setText("确定要删除"+"“"+meetingName+"”？");
        ImageView ivDelete = (ImageView) view.findViewById(R.id.iv_template_delete);
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datas.remove(position);
                notifyItemRemoved(position);
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        return view;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class TemplateManagerViewHolder extends RecyclerView.ViewHolder{

        TextView tvItemNum;
        private final TextView tvItemMeetingName;
        private final TextView tvItemMeetingNo;
        private final TextView tvItemMeetingType;
        private final TextView tvItemMeetingLevel;
        private final TextView tvItemMeetingUpdate;
        private final TextView tvItemMeetingSetting;
        private final TextView tvItemMeetingDelete;
        private final TextView tvItemMeetingImmateStart;
        private final TextView tvItemMeetingReservationStart;

        public TemplateManagerViewHolder(View itemView) {
            super(itemView);
            tvItemNum = (TextView) itemView.findViewById(R.id.tvItemNum);
            tvItemMeetingName = (TextView) itemView.findViewById(R.id.tvItemMeetingName);
            tvItemMeetingNo = (TextView) itemView.findViewById(R.id.tvItemMeetingNo);
            tvItemMeetingType = (TextView) itemView.findViewById(R.id.tvItemMeetingType);
            tvItemMeetingLevel = (TextView) itemView.findViewById(R.id.tvItemMeetingLevel);
            tvItemMeetingUpdate = (TextView) itemView.findViewById(R.id.tvItemMeetingUpdate);
            tvItemMeetingSetting = (TextView) itemView.findViewById(R.id.tvItemMeetingSetting);
            tvItemMeetingDelete = (TextView) itemView.findViewById(R.id.tvItemMeetingDelete);
            tvItemMeetingImmateStart = (TextView) itemView.findViewById(R.id.tvItemMeetingImmateStart);
            tvItemMeetingReservationStart = (TextView) itemView.findViewById(R.id.tvItemMeetingReservationStart);


        }


    }
}
