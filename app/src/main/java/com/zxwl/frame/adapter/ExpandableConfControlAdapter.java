package com.zxwl.frame.adapter;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.zxwl.frame.R;
import com.zxwl.frame.bean.ConfBean;
import com.zxwl.frame.bean.ConfBeanParent;

import java.util.List;

/**
 * author：hw
 * data:2017/5/22 14:48
 * 会议控制列表的适配器
 */
public class ExpandableConfControlAdapter extends ExpandableRecyclerAdapter<ConfBeanParent, ConfBean, ExpandableConfControlAdapter.ParentHolder, ExpandableConfControlAdapter.ChildHolder> {
    //刷新时，是否重置右边图标的标志
    private boolean resetFalg = true;

    public void setResetFalg(boolean resetFalg) {
        this.resetFalg = resetFalg;
    }

    /**
     * Primary constructor. Sets up {@link #mParentList} and {@link #mFlatItemList}.
     * <p>
     * Any changes to {@link #mParentList} should be made on the original instance, and notified via
     * {@link #notifyParentInserted(int)}
     * {@link #notifyParentRemoved(int)}
     * {@link #notifyParentChanged(int)}
     * {@link #notifyParentRangeInserted(int, int)}
     * {@link #notifyChildInserted(int, int)}
     * {@link #notifyChildRemoved(int, int)}
     * {@link #notifyChildChanged(int, int)}
     * methods and not the notify methods of RecyclerView.Adapter.
     *
     * @param parentList List of all parents to be displayed in the RecyclerView that this
     *                   adapter is linked to
     */
    public ExpandableConfControlAdapter(@NonNull List<ConfBeanParent> parentList) {
        super(parentList);
    }

    @NonNull
    @Override
    public ParentHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        ParentHolder parentHolder = new ParentHolder(LayoutInflater.from(parentViewGroup.getContext()).inflate(R.layout.item_expandable_head, parentViewGroup, false));
        return parentHolder;
    }

    @NonNull
    @Override
    public ChildHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        return new ChildHolder(LayoutInflater.from(childViewGroup.getContext()).inflate(R.layout.item_conf_control, childViewGroup, false));
    }

    @Override
    public void onBindParentViewHolder(@NonNull ParentHolder parentViewHolder, int parentPosition, @NonNull ConfBeanParent parent) {
        parentViewHolder.tvContent.setText(parent.type);
        //当重新设置parentList的时候要调用这个方法，强制设置右边的图标转换过来
        if(resetFalg){
            parentViewHolder.onExpansionToggled(true);
        }
    }

    @Override
    public void onBindChildViewHolder(@NonNull ChildHolder childViewHolder, int parentPosition, int childPosition, @NonNull ConfBean child) {
        ConfBean confBean = getParentList().get(parentPosition).getConfBeanForPosition(childPosition);
        //0=View.VISIBLE    4=View.INVISIBLE
        if (confBean.showControl) {
            childViewHolder.rlControl.setVisibility(View.VISIBLE);
            childViewHolder.rlInfo.setVisibility(View.INVISIBLE);
        } else {
            childViewHolder.rlControl.setVisibility(View.INVISIBLE);
            childViewHolder.rlInfo.setVisibility(View.VISIBLE);
        }

        childViewHolder.tvName.setText(confBean.name);//会议名称
        childViewHolder.tvTime.setText(confBean.beginTime);//会议时间
        childViewHolder.tvPhone.setText(confBean.contactTelephone);//联系电话
        childViewHolder.tvPattern.setText("讨论模式");//会议模式

//0=周期性视频会议、1=周期性非视频会议、2=视频会议、3=非视频会议、4=即时视频会议、5=即时非视频会议、6、周期性子视频会议、7、周期性子非视频会议
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
        childViewHolder.tvType.setText(confType);//会议类型

        //设置点击事件
        childViewHolder.rlContent.setOnClickListener(
                v -> {
                    if (null != itemClickListener) {
                        resetFalg = false;
                        itemClickListener.onClick(parentPosition, childPosition);
                    }
                });

        childViewHolder.tvControl.setOnClickListener(
                v -> {
                    if (null != itemClickListener) {
                        resetFalg = false;
                        itemClickListener.onControl(parentPosition, childPosition);
                    }
                });
        childViewHolder.tvFinish.setOnClickListener(
                v -> {
                    if (null != itemClickListener) {
                        resetFalg = false;
                        itemClickListener.onFinish(parentPosition, childPosition);
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();

        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                //返回的数字代表显示多少行
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    switch (type) {
                        case TYPE_PARENT:
                            return 4;

                        case TYPE_CHILD:
                            return 1;

                        default:
                            return 2;
                    }
                }
            });
        }
    }

    public void reomve(int parentPosition, int childPosition) {
        getParentList().get(parentPosition).getChildList().remove(childPosition);
        notifyDataSetChanged();
    }

    class ParentHolder extends ParentViewHolder {
        TextView tvContent;

        public ParentHolder(View itemView) {
            super(itemView);
            tvContent = (TextView) itemView;
        }

        @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
        @Override
        public void onExpansionToggled(boolean expanded) {
            super.onExpansionToggled(expanded);

            tvContent.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_item_control_left, 0, expanded ? R.mipmap.icon_expand : R.mipmap.item_pack_up, 0);
        }
    }

    class ChildHolder extends ChildViewHolder {
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

        public ChildHolder(View itemView) {
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

    public void setOnItemClickListener(onItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface onItemClickListener {
        void onClick(int parentPosition, int childPosition);

        void onControl(int parentPosition, int childPosition);

        void onFinish(int parentPosition, int childPosition);
    }
}
