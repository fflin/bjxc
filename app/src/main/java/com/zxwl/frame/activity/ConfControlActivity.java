package com.zxwl.frame.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxwl.frame.R;
import com.zxwl.frame.bean.ConfirmEvent;
import com.zxwl.frame.bean.MenuBean;
import com.zxwl.frame.fragment.AssemblyRoomControlFragment;
import com.zxwl.frame.fragment.ConfControlFragment;
import com.zxwl.frame.fragment.SplitScreenFragment;
import com.zxwl.frame.rx.RxBus;
import com.zxwl.frame.utils.DisplayUtil;
import com.zxwl.frame.views.CustomViewPager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.adapter.TabAdapter;
import q.rorbin.verticaltablayout.widget.TabView;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 会议控制面板
 */
public class ConfControlActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvLogOut;
    private TextView tvIssue;
    private TextView tvHome;
    private TextView tvName;

    private TextView tvPreview;//画面预览
    private TextView tvExtendConf;//延长会议
    private TextView tvFinishTime;//会议结束时间
    private TextView tvSetError;//设为异常
    private TextView tvFinish;//结束会议
    private TextView tvDirectory;//从地址簿添加
    private TextView tvAddParty;//添加与会方

    private CustomViewPager vpContent;
    private VerticalTabLayout tablayout;
    private ImageView ivUp;
    private ImageView ivDown;

    private List<Fragment> fragmentList = new ArrayList<>();
    private MyPagerAdapter pagerAdapter;

    public static final String SMC_CONF_ID = "smc_conf_id";
    public static final String CONF_ID = "conf_id";

    private Subscription subscribe;

    public static void startActivity(Context context, String smcConfId, String confId) {
        Intent intent = new Intent(context, ConfControlActivity.class);
        intent.putExtra(SMC_CONF_ID, smcConfId);
        intent.putExtra(CONF_ID, confId);
        context.startActivity(intent);
    }

    @Override
    protected void findViews() {
        tvLogOut = (TextView) findViewById(R.id.tv_log_out);
        tvIssue = (TextView) findViewById(R.id.tv_issue);
        tvHome = (TextView) findViewById(R.id.tv_home);
        tvName = (TextView) findViewById(R.id.tv_name);

        tvPreview = (TextView) findViewById(R.id.tv_preview);
        tvExtendConf = (TextView) findViewById(R.id.tv_extend_conf);
        tvFinishTime = (TextView) findViewById(R.id.tv_finish_time);
        tvSetError = (TextView) findViewById(R.id.tv_set_error);
        tvFinish = (TextView) findViewById(R.id.tv_finish);
        tvDirectory = (TextView) findViewById(R.id.tv_directory);
        tvAddParty = (TextView) findViewById(R.id.tv_add_party);
        vpContent = (CustomViewPager) findViewById(R.id.vp_content);
        tablayout = (VerticalTabLayout) findViewById(R.id.tablayout);
        ivUp = (ImageView) findViewById(R.id.iv_up);
        ivDown = (ImageView) findViewById(R.id.iv_down);
    }

    @Override
    protected void initData() {
        //获得会议的id
        String smcConfId = getIntent().getStringExtra(SMC_CONF_ID);
        String confId = getIntent().getStringExtra(CONF_ID);

        fragmentList.add(ConfControlFragment.newInstance(smcConfId, confId));
        fragmentList.add(AssemblyRoomControlFragment.newInstance(smcConfId, confId));
        fragmentList.add(SplitScreenFragment.newInstance(smcConfId, confId));
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragmentList);
        vpContent.setAdapter(pagerAdapter);
        //设置缓存的数量
        vpContent.setOffscreenPageLimit(3);
        tablayout.setupWithViewPager(vpContent);
        tablayout.setTabAdapter(new MyTabAdapter());

        //通过动画设置箭头的方向
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.icon_down);
        animation.setFillAfter(true);
        ivDown.startAnimation(animation);

        //设置tablayout的宽度为高度的1/3
        ViewTreeObserver vto = tablayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tablayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int heigh = tablayout.getHeight();
                int width = heigh / 3;

                ViewGroup.LayoutParams layoutParams = tablayout.getLayoutParams();
                layoutParams.width = width;
                tablayout.setLayoutParams(layoutParams);
            }
        });

        initRxBus();
    }

    @Override
    protected void setListener() {
        ivUp.setOnClickListener(this);
        ivDown.setOnClickListener(this);

        tvPreview.setOnClickListener(this);//画面预览
        tvExtendConf.setOnClickListener(this);//延长会议
        tvFinishTime.setOnClickListener(this);//会议结束时间
        tvSetError.setOnClickListener(this);//设为异常
        tvFinish.setOnClickListener(this);//结束会议
        tvDirectory.setOnClickListener(this);//从地址簿添加
        tvAddParty.setOnClickListener(this);//添加与会方
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_conf_control;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_up:
                int currentIndex = vpContent.getCurrentItem();
                if (currentIndex == 0) {
                    return;
                }
                vpContent.setCurrentItem(currentIndex - 1, true);

                break;

            case R.id.iv_down:
                int current = vpContent.getCurrentItem();
                if (current == fragmentList.size()) {
                    return;
                }
                vpContent.setCurrentItem(current + 1, true);
                break;

            //画面预览
            case R.id.tv_preview:
                break;

            //从地址簿添加
            case R.id.tv_directory:
                ContactBookDialogActivity.startActivity(this);
                break;

            default:
                break;
        }
    }

    private void initRxBus() {
        subscribe = RxBus.getInstance()
                .toObserverable(ConfirmEvent.class)
                .compose(this.<ConfirmEvent>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Action1<ConfirmEvent>() {
                            @Override
                            public void call(ConfirmEvent confirmEvent) {
                                Log.i("Main", confirmEvent.toString());
                            }
                        }
                );
    }


    class MyTabAdapter implements TabAdapter {
        List<MenuBean> menus;

        public MyTabAdapter() {
            menus = new ArrayList<>();
            Collections.addAll(menus,
                    new MenuBean(R.mipmap.tab_conf_control_on, R.mipmap.tab_conf_control_off, "会议控制")
                    , new MenuBean(R.mipmap.tab_meetingplace_control_on, R.mipmap.tab_meetingplace_control_off, "会场控制")
                    , new MenuBean(R.mipmap.tab_split_screen_on, R.mipmap.tab_split_screen_off, "分屏控制"));

        }

        @Override
        public int getCount() {
            return menus.size();
        }

        @Override
        public TabView.TabBadge getBadge(int position) {
            return null;
        }

        @Override
        public TabView.TabIcon getIcon(int position) {
            MenuBean menu = menus.get(position);
            return new TabView.TabIcon.Builder()
                    .setIcon(menu.mSelectIcon, menu.mNormalIcon)
                    .setIconGravity(Gravity.TOP)
                    .setIconMargin(DisplayUtil.dp2px(ConfControlActivity.this, 10))
                    .setIconSize(DisplayUtil.dp2px(ConfControlActivity.this, 30), DisplayUtil.dp2px(ConfControlActivity.this, 30))
                    .build();
        }

        @Override
        public TabView.TabTitle getTitle(int position) {
            MenuBean menu = menus.get(position);
            return new TabView.TabTitle.Builder()
                    .setContent(menu.mTitle)
                    .setTextColor(0xFFFFFFFF, 0xFF333333)
                    .setTextSize(14)
                    .build();
        }

        @Override
        public int getBackground(int position) {
            return -1;
        }
    }


    /**
     * viewpager的适配器
     */
    static class MyPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList = new ArrayList<>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public MyPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public int getCount() {
            return null != fragmentList ? fragmentList.size() : 0;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }
    }

}
