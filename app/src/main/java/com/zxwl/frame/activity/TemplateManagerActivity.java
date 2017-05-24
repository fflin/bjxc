package com.zxwl.frame.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.footer.LoadingView;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.zxwl.frame.R;
import com.zxwl.frame.adapter.TemplateManagerAdapter;
import com.zxwl.frame.bean.MeetingTemplate;
import com.zxwl.frame.utils.UserHelper;

import java.util.ArrayList;

/**
 * 模板管理
 * Created by asus-pc on 2017/5/22.
 */

public class TemplateManagerActivity extends BaseActivity {
    private TextView tvLogOut;
    private TextView tvIssue;
    private TextView tvHome;
    private TextView tvName;
    private TextView tvSubscribe;
    private TextView tvImmediate;
    private TextView tvStartMeeting;
    private TextView tvOutTemplate;
    private TextView tvInTemplate;
    private EditText etTopTitleSearch;
    private TwinklingRefreshLayout recyclerRefreshLayout;
    private ArrayList<MeetingTemplate> meetings = new ArrayList<>();
    private TemplateManagerAdapter adapter;
    private RecyclerView rvList;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, TemplateManagerActivity.class));
    }

    @Override
    protected void findViews() {
        tvLogOut = (TextView) findViewById(R.id.tv_log_out);
        tvIssue = (TextView) findViewById(R.id.tv_issue);
        tvHome = (TextView) findViewById(R.id.tv_home);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvSubscribe = (TextView) findViewById(R.id.tv_subscribe);//预约会议
        tvImmediate = (TextView) findViewById(R.id.tv_immediate);//即时会议
        tvStartMeeting = (TextView)findViewById(R.id.tv_startmeeting);//召开会议
        tvOutTemplate = (TextView) findViewById(R.id.tv_out_template);
        tvInTemplate = (TextView) findViewById(R.id.tv_in_template);
        etTopTitleSearch = (EditText) findViewById(R.id.et_top_title_search);
        recyclerRefreshLayout = (TwinklingRefreshLayout)findViewById(R.id.recyclerRefreshLayout);
        rvList = (RecyclerView) findViewById(R.id.rv_list);
    }

    @Override
    protected void initData() {
        //顶部退出按钮
        tvLogOut.setVisibility(View.VISIBLE);
        tvIssue.setVisibility(View.VISIBLE);
        tvHome.setVisibility(View.VISIBLE);
        tvName.setVisibility(View.VISIBLE);
        tvName.setText(UserHelper.getSavedUser().name);

        MeetingTemplate template1 = new MeetingTemplate();
        template1.setNo("1");
        template1.setMeetingName("武汉研发中心技术讨论");
        template1.setMeetingNo("191111883");
        template1.setMeetingType("宣讲会议");
        template1.setMeetingLevel("即时会议");
        template1.setDefault(true);
        meetings.add(template1);

        MeetingTemplate template2 = new MeetingTemplate();
        template2.setNo("2");
        template2.setMeetingName("深圳研发中心技术讨论");
        template2.setMeetingNo("192222883");
        template2.setMeetingType("宣讲会议");
        template2.setMeetingLevel("即时会议");
        template2.setDefault(false);
        meetings.add(template2);

        MeetingTemplate template3 = new MeetingTemplate();
        template3.setNo("3");
        template3.setMeetingName("北京研发中心技术讨论");
        template3.setMeetingNo("193333883");
        template3.setMeetingType("宣讲会议");
        template3.setMeetingLevel("即时会议");
        template3.setDefault(false);
        meetings.add(template3);

        MeetingTemplate template4 = new MeetingTemplate();
        template4.setNo("4");
        template4.setMeetingName("上海研发中心技术讨论");
        template4.setMeetingNo("194444223");
        template4.setMeetingType("宣讲会议");
        template4.setMeetingLevel("即时会议");
        template4.setDefault(false);
        meetings.add(template4);

        MeetingTemplate template5 = new MeetingTemplate();
        template5.setNo("5");
        template5.setMeetingName("杭州研发中心技术讨论");
        template5.setMeetingNo("195555883");
        template5.setMeetingType("宣讲会议");
        template5.setMeetingLevel("即时会议");
        template5.setDefault(false);
        meetings.add(template5);

        adapter = new TemplateManagerAdapter(this,meetings);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        rvList.setAdapter(adapter);
        initRefresh();
        //recyclerRefreshLayout.startRefresh();
    }

    @Override
    protected void setListener() {
        recyclerRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {

            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);

            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_template_manager;
    }


    private void initRefresh() {
        //设置刷新的头部
        ProgressLayout progressLayout = new ProgressLayout(this);
        recyclerRefreshLayout.setHeaderView(progressLayout);
        //设置加载的底部
        LoadingView loadingView = new LoadingView(this);
        recyclerRefreshLayout.setBottomView(loadingView);
        //设置像SwipeRefreshLayout一样的悬浮刷新模式了
        recyclerRefreshLayout.setFloatRefresh(true);
        //设置是否回弹
        recyclerRefreshLayout.setEnableOverScroll(false);
        //设置头部高度
        recyclerRefreshLayout.setHeaderHeight(140);
        //设置头部的最大高度
        recyclerRefreshLayout.setMaxHeadHeight(200);
        //设置刷新的view
        recyclerRefreshLayout.setTargetView(rvList);
    }
}
