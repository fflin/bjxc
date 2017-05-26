package com.zxwl.frame.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.footer.LoadingView;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.zxwl.frame.R;
import com.zxwl.frame.adapter.ExpandableConfControlAdapter;
import com.zxwl.frame.bean.ConfBean;
import com.zxwl.frame.bean.ConfBeanParent;
import com.zxwl.frame.bean.DataList;
import com.zxwl.frame.bean.UserInfo;
import com.zxwl.frame.constant.Account;
import com.zxwl.frame.net.api.ConfApi;
import com.zxwl.frame.net.callback.RxSubscriber;
import com.zxwl.frame.net.exception.ResponeThrowable;
import com.zxwl.frame.net.http.HttpUtils;
import com.zxwl.frame.utils.UserHelper;
import com.zxwl.frame.utils.sharedpreferences.PreferencesHelper;
import com.zxwl.frame.views.spinner.NiceSpinner;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * 会议控制列表
 */
public class ExpandableConfControlListActivity extends BaseActivity implements View.OnClickListener {
    /*头部公用控件-start*/
    private TextView tvLogOut;
    private TextView tvIssue;
    private TextView tvHome;
    private TextView tvName;
    /*头部公用控件-end*/

    private EditText etTopTitleSearch;//搜索框
    private NiceSpinner timeSpinner;//时间选择

    /*列表刷新-start*/
    private TwinklingRefreshLayout refreshLayout;
    private RecyclerView rvList;
    private int PAGE_SIZE = 3;
    private int PAGE_NUM = 0;
    private ExpandableConfControlAdapter adapter;
    private List<ConfBeanParent> list = new ArrayList<>();
    /*列表刷新-end*/

    private Subscription subscribe;
    private boolean enableLoadmore = true;//加载更多是否可用

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ExpandableConfControlListActivity.class));
    }

    @Override
    protected void findViews() {
        tvLogOut = (TextView) findViewById(R.id.tv_log_out);
        tvIssue = (TextView) findViewById(R.id.tv_issue);
        tvHome = (TextView) findViewById(R.id.tv_home);
        tvName = (TextView) findViewById(R.id.tv_name);

        etTopTitleSearch = (EditText) findViewById(R.id.et_top_title_search);
        timeSpinner = (NiceSpinner) findViewById(R.id.time_spinner);
        refreshLayout = (TwinklingRefreshLayout) findViewById(R.id.refreshLayout);
        rvList = (RecyclerView) findViewById(R.id.rv_list);
    }

    @Override
    protected void initData() {
        //顶部退出按钮
        tvLogOut.setVisibility(View.VISIBLE);
        tvIssue.setVisibility(View.VISIBLE);
        tvHome.setVisibility(View.VISIBLE);
        tvName.setVisibility(View.VISIBLE);

        UserInfo userInfo = UserHelper.getSavedUser();
        if (null != userInfo) {
            tvName.setText(userInfo.name);
        }

        //设置适配器
        adapter = new ExpandableConfControlAdapter(list);
        adapter.setExpandCollapseListener(new ExpandableRecyclerAdapter.ExpandCollapseListener() {
            @Override
            public void onParentExpanded(int parentPosition) {
            }

            @Override
            public void onParentCollapsed(int parentPosition) {
                List<ConfBean> childList = adapter.getParentList().get(parentPosition).getChildList();
                for (ConfBean confBean : childList) {
                    confBean.showControl = false;
                }
                adapter.notifyParentChanged(parentPosition);
            }
        });

        adapter.setOnItemClickListener(new ExpandableConfControlAdapter.onItemClickListener() {
            @Override
            public void onClick(int parentPosition, int childPosition) {
                setAdapterShow(parentPosition, childPosition);
            }

            @Override
            public void onControl(int parentPosition, int childPosition) {
                setAdapterShow(parentPosition, childPosition);
                ConfBean confBean = list.get(parentPosition).getChildList().get(childPosition);
                //控制会议
                ConfControlActivity.startActivity(ExpandableConfControlListActivity.this, confBean.smcConfId, confBean.id);
            }

            @Override
            public void onFinish(int parentPosition, int childPosition) {
                setAdapterShow(parentPosition, childPosition);
                ConfBean confBean = list.get(parentPosition).getChildList().get(childPosition);
                //结束会议的网络请求
                finishConfRequest(confBean.id, confBean.smcConfId, parentPosition, childPosition);
            }
        });
        rvList.setLayoutManager(new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false));
        rvList.setAdapter(adapter);

        //初始化recyclerview
        initRefresh();
        refreshLayout.startRefresh();
    }

    @Override
    protected void setListener() {
        tvLogOut.setOnClickListener(this);//退出登录
        tvIssue.setOnClickListener(this);//帮助
        tvHome.setOnClickListener(this);//返回主页
        tvName.setOnClickListener(this);//名字

        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                //刷新数据
                getData(1);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                //判断当前条数是否大于数据总条数
                //结束加载更多，需手动调用
                getData(PAGE_NUM + 1);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_conf_control_list;
    }

    /**
     * 初始化刷新控件
     */
    private void initRefresh() {
        //设置刷新的头部
        ProgressLayout progressLayout = new ProgressLayout(this);
        refreshLayout.setHeaderView(progressLayout);
        //设置加载的底部
        LoadingView loadingView = new LoadingView(this);
        refreshLayout.setBottomView(loadingView);
        //设置像SwipeRefreshLayout一样的悬浮刷新模式了
        refreshLayout.setFloatRefresh(true);
        //设置是否回弹
        refreshLayout.setEnableOverScroll(false);
        //设置头部高度
        refreshLayout.setHeaderHeight(140);
        //设置头部的最大高度
        refreshLayout.setMaxHeadHeight(200);
        //设置刷新的view
        refreshLayout.setTargetView(rvList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
    }

    /**
     * 设置adapter的显示
     *
     * @param parentPosition 在父层级的位置
     * @param childPosition  在子层级的位置
     */
    private void setAdapterShow(int parentPosition, int childPosition) {
        List<ConfBean> childList = list.get(parentPosition).getChildList();
        for (int i = 0, count = childList.size(); i < count; i++) {
            if (i != childPosition) {
                childList.get(i).showControl = false;
            }
        }
        ConfBean bean = childList.get(childPosition);
        bean.showControl = !bean.showControl;
        adapter.notifyDataSetChanged();
    }

    /**
     * 获得会议列表数据
     *
     * @param pageNum 页码
     */
    private void getData(final int pageNum) {
        ConfApi builder = HttpUtils.getInstance(this)
                .getRetofitClinet()
                .builder(ConfApi.class);

        //通过压合将正在召开的会议和即将召开的会议取到
        subscribe = Observable.zip(
                builder.getConfBeinglList(PAGE_SIZE, pageNum),
                builder.getConfWaitlList(PAGE_SIZE, pageNum),
                new Func2<DataList<ConfBean>, DataList<ConfBean>, List<ConfBeanParent>>() {
                    @Override
                    public List<ConfBeanParent> call(DataList<ConfBean> beingConfList, DataList<ConfBean> waitConfList) {
                        List<ConfBeanParent> parentList = adapter.getParentList();
                        List<ConfBeanParent> newList = new ArrayList<ConfBeanParent>();

                        if (1 == pageNum) {
                            parentList.clear();
                            newList.add(new ConfBeanParent("正在召开的会议(" + beingConfList.dataList.size() + ")", beingConfList.dataList));
                            newList.add(new ConfBeanParent("预约会议(" + waitConfList.dataList.size() + ")", waitConfList.dataList));
                        } else {//加载更多
                            //获得之前列表里的数据
                            List<ConfBean> beingList = parentList.get(0).getChildList();
                            List<ConfBean> waitList = parentList.get(1).getChildList();
                            //判断正在召开的会议是否重复
                            for (int i = 0; i < beingConfList.dataList.size(); i++) {
                                if (!beingList.contains(beingConfList.dataList.get(i))) {
                                    beingList.add(beingConfList.dataList.get(i));
                                }
                            }

                            //判断预约的会议是否重复
                            for (int i = 0; i < waitConfList.dataList.size(); i++) {
                                if (!waitList.contains(waitConfList.dataList.get(i))) {
                                    waitList.add(waitConfList.dataList.get(i));
                                }
                            }

                            //添加到返回的新列表当中
                            newList.add(new ConfBeanParent("正在召开的会议(" + beingList.size() + ")", beingList));
                            newList.add(new ConfBeanParent("预约会议(" + waitList.size() + ")", waitList));

                            //如果当前条数大于或等于总条数则禁用加载更多
                            if (beingList.size() >= Integer.parseInt(beingConfList.rowSum) &&
                                    waitList.size() >= Integer.parseInt(waitConfList.rowSum)) {
                                enableLoadmore = false;
                            }
                        }
                        return newList;
                    }
                })
                .compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<List<ConfBeanParent>>() {
                    @Override
                    public void onSuccess(List<ConfBeanParent> confBeanParents) {
                        //1为刷新，否则为加载更多
                        if (1 == pageNum) {
                            PAGE_NUM = 1;
                            refreshLayout.finishRefreshing();
                            //刷新的时候设置加载更多可以使用
                            refreshLayout.setEnableLoadmore(true);
                            enableLoadmore = true;
                            Toast.makeText(ExpandableConfControlListActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
                        } else {
                            PAGE_NUM++;
                            refreshLayout.finishLoadmore();
                            //根据设置的值判断加载更多是否可用
                            refreshLayout.setEnableLoadmore(enableLoadmore);
                            Toast.makeText(ExpandableConfControlListActivity.this, "加载成功", Toast.LENGTH_SHORT).show();
                        }

                        list.clear();
                        list.addAll(confBeanParents);
                        adapter.setResetFalg(true);
                        adapter.notifyParentDataSetChanged(true);
                    }

                    @Override
                    protected void onError(ResponeThrowable responeThrowable) {
                        Toast.makeText(ExpandableConfControlListActivity.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 结束会议
     *
     * @param confId
     * @param smcConfId
     * @param parentPosition
     * @param childPosition
     */
    private void finishConfRequest(String confId, String smcConfId, final int parentPosition, int childPosition) {
        HttpUtils.getInstance(this)
                .getRetofitClinet()
                .builder(ConfApi.class)
                .finishConf(confId, smcConfId)
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        s -> {
                            Toast.makeText(ExpandableConfControlListActivity.this, "会议结束", Toast.LENGTH_SHORT).show();
                            adapter.reomve(parentPosition, childPosition);
                        },
                        //产生异常
                        responeThrowable -> {
                            Toast.makeText(ExpandableConfControlListActivity.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
                        }
                );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //退出登录
            case R.id.tv_log_out:
                new MaterialDialog.Builder(this)
                        .title("提示")
                        .content("是否确认退出？")
                        .negativeText("取消")
                        .positiveText("确认")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                //修改登录状态
                                PreferencesHelper.saveData(Account.IS_LOGIN, "false");
                                //删除用户信息
                                UserHelper.clearUserInfo(UserInfo.class);
                                //跳转到登录页面
                                LoginActivity.startActivity(ExpandableConfControlListActivity.this);
                                dialog.dismiss();
                            }
                        })
                        .build()
                        .show();
                break;

            //帮助
            case R.id.tv_issue:
                Toast.makeText(this, "帮助", Toast.LENGTH_SHORT).show();
                break;

            //返回主页
            case R.id.tv_home:
                HomeActivity.startActivity(this);
                break;

            //名字
            case R.id.tv_name:
                Toast.makeText(this, "名字", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

}
