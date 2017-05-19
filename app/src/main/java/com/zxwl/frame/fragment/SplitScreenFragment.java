package com.zxwl.frame.fragment;


import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miao.freesizedraggablelayout.DetailView;
import com.miao.freesizedraggablelayout.FreeSizeDraggableLayout;
import com.orhanobut.logger.Logger;
import com.zxwl.frame.R;
import com.zxwl.frame.activity.ConfControlActivity;
import com.zxwl.frame.activity.SplitScreenDialogActivity;
import com.zxwl.frame.adapter.CallbackItemTouch;
import com.zxwl.frame.adapter.MyItemTouchHelperCallback;
import com.zxwl.frame.adapter.SplitScreenItemAdapter;
import com.zxwl.frame.adapter.SplitScreenRightAdapter;
import com.zxwl.frame.bean.ConferenceInfo;
import com.zxwl.frame.bean.ConferenceStatus;
import com.zxwl.frame.bean.Site;
import com.zxwl.frame.net.api.ConfApi;
import com.zxwl.frame.net.callback.RxSubscriber;
import com.zxwl.frame.net.exception.ResponeThrowable;
import com.zxwl.frame.net.http.HttpUtils;
import com.zxwl.frame.rx.RxBus;
import com.zxwl.frame.utils.DisplayUtil;
import com.zxwl.frame.utils.ViewUtil;
import com.zxwl.frame.views.WheelView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.ielse.view.SwitchView;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 分屏
 */
public class SplitScreenFragment extends BaseFragment implements CallbackItemTouch, View.OnClickListener {
    private FreeSizeDraggableLayout fsdLayout;
    private List<DetailView> detailViewList = new ArrayList<>();
    private TextView tvTitle;//会议名称
    private TextView tvSave;//保存
    private TextView tvReset;//重置
    private TextView tvAllSplit;//全部分屏样式

    private ImageView ivOne;
    private ImageView ivTwo;
    private ImageView ivThree;
    private ImageView ivFour;
    private ImageView ivFive;
    private ImageView ivSix;
    private ImageView ivSeven;
    private ImageView ivEight;
    private ImageView ivNine;
    private ImageView ivTen;//10
    private ImageView ivThirteen;//13
    private ImageView ivSixteen;//16
    private ImageView ivTwenty;//20
    private ImageView ivTwentyFour;//24
    private SwitchView svPoll;//轮询多画面

    private boolean falgPoll;//是否是轮询多画面
    private String pollTime;//轮询时间

    private RecyclerView rvList;
    private SplitScreenRightAdapter rightAdapter;

    private Gson gson = new Gson();

    private ConferenceInfo conferenceInfo;//会议信息
    private ConferenceStatus conferenceStatus;//会议状态
    private List<Site> siteList = new ArrayList<>();

    private int currentIndex;//当前移动的下标

    private String smcConfId;
    private String confId;
    private String presenceMode;//分屏模式

    private Subscription subscription;

    public SplitScreenFragment() {
    }

    public static SplitScreenFragment newInstance(String smcConfId, String confId) {
        SplitScreenFragment fragment = new SplitScreenFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ConfControlActivity.SMC_CONF_ID, smcConfId);
        bundle.putString(ConfControlActivity.CONF_ID, confId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View inflateContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_split_screen, container, false);
    }

    @Override
    protected void findViews(View view) {
        fsdLayout = (FreeSizeDraggableLayout) view.findViewById(R.id.fsd_layout);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        rvList = (RecyclerView) view.findViewById(R.id.rv_content);

        tvSave = (TextView) view.findViewById(R.id.tv_save);
        tvReset = (TextView) view.findViewById(R.id.tv_reset);
        tvAllSplit = (TextView) view.findViewById(R.id.tv_all_split);
        ivOne = (ImageView) view.findViewById(R.id.iv_one);
        ivTwo = (ImageView) view.findViewById(R.id.iv_two);
        ivThree = (ImageView) view.findViewById(R.id.iv_three);
        ivFour = (ImageView) view.findViewById(R.id.iv_four);
        ivFive = (ImageView) view.findViewById(R.id.iv_five);
        ivSix = (ImageView) view.findViewById(R.id.iv_six);
        ivSeven = (ImageView) view.findViewById(R.id.iv_seven);
        ivEight = (ImageView) view.findViewById(R.id.iv_eight);
        ivNine = (ImageView) view.findViewById(R.id.iv_nine);
        ivTen = (ImageView) view.findViewById(R.id.iv_ten);
        ivThirteen = (ImageView) view.findViewById(R.id.iv_thirteen);
        ivSixteen = (ImageView) view.findViewById(R.id.iv_sixteen);
        ivTwenty = (ImageView) view.findViewById(R.id.iv_twenty);
        ivTwentyFour = (ImageView) view.findViewById(R.id.iv_twenty_four);
        svPoll = (SwitchView) view.findViewById(R.id.sv_poll);
    }

    @Override
    protected void init() {
        presenceMode = "CP_1_1";
        fsdLayout.setUnitWidthNum(1);
        fsdLayout.setUnitHeightNum(1);
        detailViewList.clear();
        detailViewList.add(new DetailView(new Point(0, 0), 1, 1, createRecycler()));
        fsdLayout.setsubViewPadding(1);
        fsdLayout.setList(detailViewList);

        Bundle arguments = getArguments();
        smcConfId = (String) arguments.get(ConfControlActivity.SMC_CONF_ID);
        confId = (String) arguments.get(ConfControlActivity.CONF_ID);

        rightAdapter = new SplitScreenRightAdapter(siteList);
        rvList.setAdapter(rightAdapter);
        rvList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        //设置recyclerview的拖动事件
        final ItemTouchHelper.Callback callback = new MyItemTouchHelperCallback(this);// create MyItemTouchHelperCallback
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback); // Create ItemTouchHelper and pass with parameter the MyItemTouchHelperCallback
        touchHelper.attachToRecyclerView(rvList); // Attach ItemTouchHelper to RecyclerView

        rvList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    //抬起
                    case MotionEvent.ACTION_UP:
                        //手指抬起时所处于的view在fsdLayout子列表中的下标
                        int eventCurrent = -1;
                        boolean falg = false;
                        float rawX = event.getRawX();
                        float rawY = event.getRawY();
                        //判断手指抬起时候的点是否在右边的布局当中
                        for (int i = 0, count = fsdLayout.getChildCount(); i < count; i++) {
                            View childAt = fsdLayout.getChildAt(i);
                            falg = ViewUtil.isTouchPointInView(childAt, (int) rawX, (int) rawY);
                            if (falg) {
                                eventCurrent = i;
                                break;
                            }
                        }
                        //如果falg为true代表手指抬起的地方有view
                        if (falg) {
                            //获得出于抬起点的view
                            DetailView detailView = detailViewList.get(eventCurrent);
                            RecyclerView detailRecycler = (RecyclerView) detailView.getView();
                            SplitScreenItemAdapter adapter = (SplitScreenItemAdapter) detailRecycler.getAdapter();
                            //被拖动的site
                            Site currentSite = siteList.get(currentIndex);
                            //如果是轮询多画面则清空所有的site
                            if (!falgPoll) {
                                adapter.removeAll();
                            }
                            adapter.add(currentSite);
                            adapter.notifyDataSetChanged();
                            detailRecycler.smoothScrollToPosition(adapter.getItemCount() + 1);
                        }
                        break;

                    default:
                        break;
                }
                return false;
            }
        });

        subscription = RxBus.getInstance()
                .toObserverable(Intent.class)
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Intent>() {
                    @Override
                    public void call(Intent intent) {
                        int currentIndex = intent.getIntExtra(SplitScreenDialogActivity.CURRENT_INDEX, -1);
                        int currentChlidIndex = intent.getIntExtra(SplitScreenDialogActivity.CURRENT_CHLID_INDEX, -1);
                        showNewSplit(currentIndex, currentChlidIndex);
                    }
                });

        //获得会议信息
        getConfInfo();
    }

    @Override
    protected void addListeners() {
        tvSave.setOnClickListener(this);//保存
        tvReset.setOnClickListener(this);//重置
        tvAllSplit.setOnClickListener(this);//全部分屏样式

        ivOne.setOnClickListener(this);//1
        ivTwo.setOnClickListener(this);//2
        ivThree.setOnClickListener(this);//3
        ivFour.setOnClickListener(this);//4
        ivFive.setOnClickListener(this);//5
        ivSix.setOnClickListener(this);//6
        ivSeven.setOnClickListener(this);//7
        ivEight.setOnClickListener(this);//8
        ivNine.setOnClickListener(this);//9
        ivTen.setOnClickListener(this);//10
        ivThirteen.setOnClickListener(this);//13
        ivSixteen.setOnClickListener(this);//16
        ivTwenty.setOnClickListener(this);//20
        ivTwentyFour.setOnClickListener(this);//24

        svPoll.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView view) {
                falgPoll = true;
                svPoll.setOpened(true);
            }

            @Override
            public void toggleToOff(SwitchView view) {
                falgPoll = false;
                svPoll.setOpened(false);
            }
        });
    }

    @Override
    public void itemTouchOnMove(int oldPosition, int newPosition) {
        currentIndex = oldPosition;
    }

    /**
     * 创建recyclerview
     *
     * @return
     */
    private View createRecycler() {
        RecyclerView rvList = new RecyclerView(getContext());
        rvList.setBackgroundResource(R.drawable.bg_split_lable);
        List<Site> sites = new ArrayList<>();

        SplitScreenItemAdapter adapter = new SplitScreenItemAdapter(sites);
        adapter.setOnItemClickListener(new SplitScreenItemAdapter.onItemClickListener() {
            @Override
            public void onClick(int position) {
                Site site = adapter.getItemSite(position);
                site.splitCheck = !site.splitCheck;
                adapter.notifyItemChanged(position);
                rvList.smoothScrollToPosition(position);
            }
        });

        rvList.setAdapter(adapter);
        rvList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        return rvList;
    }

    /**
     * 获得会议信息
     */
    private void getConfInfo() {
        HttpUtils.getInstance(getContext())
                .getRetofitClinet()
                .builder(ConfApi.class)
                .joinTOConf(smcConfId)
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        s -> {
                            try {
                                if (TextUtils.isEmpty(s)) {
                                    Toast.makeText(mContext, R.string.error_msg, Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                JSONObject object = new JSONObject(s);
                                JSONArray array = object.getJSONArray("conference");
                                //会议信息
                                String s1 = array.getString(0);
                                conferenceInfo = gson.fromJson(s1, ConferenceInfo.class);

                                //会议状态
                                String s2 = array.getString(1);
                                Logger.i(s2);
                                conferenceStatus = gson.fromJson(s2, ConferenceStatus.class);

                                //参会的会场列表
                                String s3 = array.getString(2);
                                Logger.i(s3);
                                siteList = gson.fromJson(s3, new TypeToken<List<Site>>() {
                                }.getType());

                                //设置bean的操作状态
                                for (int i = 0, count = siteList.size(); i < count; i++) {
                                    siteList.get(i).showRemoveControl = false;
                                }
                                //刷新适配器
                                rightAdapter.addAll(siteList);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }, throwable -> {
                            Toast.makeText(mContext, R.string.error_msg, Toast.LENGTH_SHORT).show();
                        }
                );
    }

    /**
     * 设置分屏
     *
     * @param smcConfId       会议id
     * @param target          多画面标识，一般为空串
     * @param presenceMode    分屏模式
     * @param subPics         会场标识列表
     * @param splitScreenTime 轮询间隔时间 单位秒
     */
    public void splitScreen(String smcConfId,
                            String target,
                            String presenceMode,
                            String subPics,
                            String splitScreenTime) {
        HttpUtils.getInstance(getContext())
                .getRetofitClinet()
                .builder(ConfApi.class)
                .setSplitScreen(smcConfId, target, presenceMode, subPics, splitScreenTime)
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Toast.makeText(mContext, "分屏成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    protected void onError(ResponeThrowable responeThrowable) {
                        Toast.makeText(mContext, R.string.error_msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != subscription) {
            subscription.unsubscribe();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //保存
            case R.id.tv_save:
                StringBuilder subPics = new StringBuilder();
                for (int i = 0, count = detailViewList.size(); i < count; i++) {
                    StringBuilder childSubPics = new StringBuilder();
                    DetailView detail = detailViewList.get(i);
                    RecyclerView recycler = (RecyclerView) detail.getView();
                    SplitScreenItemAdapter adapter = (SplitScreenItemAdapter) recycler.getAdapter();
                    List<Site> siteList = adapter.getSiteList();
                    for (int j = 0; j < siteList.size(); j++) {
                        if (i == count - 1 && j == siteList.size() - 1) {
                            childSubPics.append(siteList.get(j).siteInfo.uri);
                        } else {
                            childSubPics.append(siteList.get(j).siteInfo.uri + ",");
                        }
                    }
                    subPics.append(childSubPics + "%20");
                }

                Logger.i(subPics.toString().trim());

                if (TextUtils.isEmpty(subPics.toString().trim())) {
                    Toast.makeText(mContext, "请选择分屏", Toast.LENGTH_SHORT).show();
                    return;
                }

                //设置分屏
                if (falgPoll) {
                    if (TextUtils.isEmpty(pollTime) || TextUtils.equals("0", pollTime)) {
                        Toast.makeText(mContext, "请选择轮询时间", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    splitScreen(smcConfId, "", presenceMode, subPics.toString().trim(), pollTime);
                } else {
                    splitScreen(smcConfId, "", presenceMode, subPics.toString().trim(), "0");
                }
                break;

            //重置
            case R.id.tv_reset:
                View outerView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_wheel_view, null);
                WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
                wv.setItems(Arrays.asList("5", "10", "15", "20"));

                MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                        .title("请选择轮询时间")
                        .customView(outerView, false)
                        .positiveText("确定")
                        .negativeText("取消")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                pollTime = wv.getSeletedItem();
                                Toast.makeText(getContext(), pollTime + "秒", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .build();
                //设置对话框的宽度
                dialog.getWindow().setLayout(DisplayUtil.getScreenWidth() / 2, ViewGroup.LayoutParams.WRAP_CONTENT);
                //点击对话框以外的地方，对话框不消失
                dialog.setCanceledOnTouchOutside(false);
                //点击对话框意外的地方和返回键，对话框都不消失
//              dialog.setCancelable(false);
                dialog.show();
                break;

            //所有分屏样式
            case R.id.tv_all_split:
                SplitScreenDialogActivity.startActivity(getActivity());
                break;

            //1
            case R.id.iv_one:
                showNewSplit(1, 0);
                break;

            //2
            case R.id.iv_two:
                showNewSplit(2, 0);
                break;

            //3
            case R.id.iv_three:
                showNewSplit(3, 0);
                break;
            //4
            case R.id.iv_four:
                showNewSplit(4, 0);
                break;

            //5
            case R.id.iv_five:
                showNewSplit(5, 0);
                break;

            //6
            case R.id.iv_six:
                showNewSplit(6, 0);
                break;

            //7
            case R.id.iv_seven:
                showNewSplit(7, 0);
                break;

            //8
            case R.id.iv_eight:
                showNewSplit(8, 0);
                break;

            //9
            case R.id.iv_nine:
                showNewSplit(9, 0);
                break;

            //10
            case R.id.iv_ten:
                showNewSplit(10, 0);
                break;

            //13
            case R.id.iv_thirteen:
                showNewSplit(13, 0);
                break;

            //16
            case R.id.iv_sixteen:
                showNewSplit(16, 0);
                break;

            //20
            case R.id.iv_twenty:
                showNewSplit(20, 0);
                break;

            //24
            case R.id.iv_twenty_four:
                showNewSplit(24, 0);
                break;

            default:
                break;
        }
    }

    /**
     * 显示新的分屏样式
     *
     * @param currentIndex
     * @param currentChlidIndex
     */
    private void showNewSplit(int currentIndex, int currentChlidIndex) {
        detailViewList.clear();
        switch (currentIndex) {
            case 1:
                presenceMode = "1";
                set1_1();
                break;

            case 2:
                switch (currentChlidIndex) {
                    case 0:
                        presenceMode = "2";
                        set2_1();
                        break;

                    case 1:
                        presenceMode = "3";
                        set2_2();
                        break;
                }
                break;

            case 3:
                switch (currentChlidIndex) {
                    case 0:
                        presenceMode = "5";
                        set3_1();
                        break;

                    case 1:
                        presenceMode = "6";
                        set3_2();
                        break;

                    case 2:
                        presenceMode = "7";
                        set3_3();
                        break;

                    case 3:
                        presenceMode = "8";
                        set3_4();
                        break;

                    case 4:
                        presenceMode = "10";
                        set3_5();
                        break;
                }
                break;

            case 4:
                switch (currentChlidIndex) {
                    case 0:
                        presenceMode = "11";
                        set4_1();
                        break;

                    case 1:
                        presenceMode = "12";
                        set4_2();
                        break;

                    case 2:
                        presenceMode = "13";
                        set4_3();
                        break;

                    case 3:
                        presenceMode = "14";
                        set4_4();
                        break;

                    case 4:
                        presenceMode = "15";
                        set4_5();
                        break;
                }
                break;

            case 5:
                switch (currentChlidIndex) {
                    case 0:
                        presenceMode = "17";
                        set5_1();
                        break;

                    case 1:
                        presenceMode = "18";
                        set5_2();
                        break;

                    case 2:
                        presenceMode = "19";
                        set5_3();
                        break;

                    case 3:
                        presenceMode = "20";
                        set5_4();
                        break;
                }
                break;

            case 6:
                switch (currentChlidIndex) {
                    case 0:
                        presenceMode = "21";
                        set6_1();
                        break;

                    case 1:
                        presenceMode = "22";
                        set6_2();
                        break;

                    case 2:
                        presenceMode = "23";
                        set6_3();
                        break;

                    case 3:
                        presenceMode = "24";
                        set6_4();
                        break;

                    case 4:
                        presenceMode = "25";
                        set6_5();
                        break;
                }
                break;

            case 7:
                switch (currentChlidIndex) {
                    case 0:
                        presenceMode = "26";
                        set7_1();
                        break;

                    case 1:
                        presenceMode = "27";
                        set7_2();
                        break;

                    case 2:
                        presenceMode = "28";
                        set7_3();
                        break;

                    case 3:
                        presenceMode = "29";
                        set7_4();
                        break;
                }
                break;

            case 8:
                switch (currentChlidIndex) {
                    case 0:
                        presenceMode = "31";
                        set8_1();
                        break;

                    case 1:
                        presenceMode = "32";
                        set8_2();
                        break;
                    case 2:
                        presenceMode = "33";
                        set8_3();
                        break;
                    case 3:
                        presenceMode = "34";
                        set8_4();
                        break;
                }
                break;

            case 9:
                presenceMode = "35";
                set9_1();
                break;

            case 10:
                switch (currentChlidIndex) {
                    case 0:
                        presenceMode = "36";
                        set10_1();
                        break;
                    case 1:
                        presenceMode = "37";
                        set10_2();
                        break;
                    case 2:
                        presenceMode = "38";
                        set10_3();
                        break;
                    case 3:
                        presenceMode = "39";
                        set10_4();
                        break;
                    case 4:
                        presenceMode = "40";
                        set10_5();
                        break;
                    case 5:
                        presenceMode = "41";
                        set10_6();
                        break;
                }
                break;

            case 13:
                switch (currentChlidIndex) {
                    case 0:
                        presenceMode = "42";
                        set13_1();
                        break;
                    case 1:
                        presenceMode = "43";
                        set13_2();
                        break;
                    case 2:
                        presenceMode = "44";
                        set13_3();
                        break;
                    case 3:
                        presenceMode = "45";
                        set13_4();
                        break;
                    case 4:
                        presenceMode = "46";
                        set13_5();
                        break;
                }
                break;

            case 16:
                presenceMode = "47";
                set16_1();
                break;

            case 20:
                presenceMode = "48";
                set20_1();
                break;

            case 24:
                presenceMode = "49";
                set24_1();
                break;
        }
        fsdLayout.setList(detailViewList);
    }

    private void set1_1() {
        fsdLayout.setUnitWidthNum(1);
        fsdLayout.setUnitHeightNum(1);

        detailViewList.add(new DetailView(new Point(0, 0), 1, 1, createRecycler()));
    }


    private void set2_1() {
        fsdLayout.setUnitWidthNum(4);
        fsdLayout.setUnitHeightNum(4);

        detailViewList.add(new DetailView(new Point(0, 1), 2, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 1), 2, 2, createRecycler()));
    }

    private void set2_2() {
        fsdLayout.setUnitWidthNum(4);
        fsdLayout.setUnitHeightNum(4);

        detailViewList.add(new DetailView(new Point(1, 0), 2, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 2), 2, 2, createRecycler()));
    }

    private void set3_1() {
        fsdLayout.setUnitWidthNum(4);
        fsdLayout.setUnitHeightNum(4);

        detailViewList.add(new DetailView(new Point(0, 0), 2, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 0), 2, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 2), 2, 2, createRecycler()));
    }

    private void set3_2() {
        fsdLayout.setUnitWidthNum(4);
        fsdLayout.setUnitHeightNum(4);

        detailViewList.add(new DetailView(new Point(1, 0), 2, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 2), 2, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 2), 2, 2, createRecycler()));
    }

    /**
     * 1 *
     * * 2
     * 3 *
     */
    private void set3_3() {
        fsdLayout.setUnitHeightNum(4);
        fsdLayout.setUnitWidthNum(4);

        detailViewList.add(new DetailView(new Point(0, 0), 2, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 1), 2, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 2), 2, 2, createRecycler()));
    }

    /**
     * * 2
     * 1 *
     * * 3
     */
    private void set3_4() {
        fsdLayout.setUnitHeightNum(4);
        fsdLayout.setUnitWidthNum(4);

        detailViewList.add(new DetailView(new Point(0, 1), 2, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 0), 2, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 2), 2, 2, createRecycler()));
    }

    /**
     * 1 2 3
     */
    private void set3_5() {
        fsdLayout.setUnitHeightNum(3);
        fsdLayout.setUnitWidthNum(3);

        detailViewList.add(new DetailView(new Point(0, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 1), 1, 1, createRecycler()));
    }

    /**
     * 1 2
     * 3 4
     */
    private void set4_1() {
        fsdLayout.setUnitHeightNum(2);
        fsdLayout.setUnitWidthNum(2);

        detailViewList.add(new DetailView(new Point(0, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 1), 1, 1, createRecycler()));
    }

    /**
     * * 2
     * 1 3
     * * 4
     */
    private void set4_2() {
        fsdLayout.setUnitHeightNum(3);
        fsdLayout.setUnitWidthNum(3);
        detailViewList.add(new DetailView(new Point(0, 0), 2, 3, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 2), 1, 1, createRecycler()));
    }

    /**
     * 1 *
     * 3 2
     * 4 *
     */
    private void set4_3() {
        fsdLayout.setUnitHeightNum(3);
        fsdLayout.setUnitWidthNum(3);
        detailViewList.add(new DetailView(new Point(0, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 0), 2, 3, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 2), 1, 1, createRecycler()));
    }

    /**
     * 1 1 1
     * 2 3 4
     */
    private void set4_4() {
        fsdLayout.setUnitHeightNum(3);
        fsdLayout.setUnitWidthNum(3);
        detailViewList.add(new DetailView(new Point(0, 0), 3, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 2), 1, 1, createRecycler()));
    }


    /**
     * 1 2 3
     * 4 4 4
     */
    private void set4_5() {
        fsdLayout.setUnitHeightNum(3);
        fsdLayout.setUnitWidthNum(3);
        detailViewList.add(new DetailView(new Point(0, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 1), 3, 2, createRecycler()));
    }


    /**
     * 1 1 1 1
     * 2 3 4 5
     */
    private void set5_1() {
        fsdLayout.setUnitHeightNum(4);
        fsdLayout.setUnitWidthNum(4);
        detailViewList.add(new DetailView(new Point(0, 0), 4, 3, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 3), 1, 1, createRecycler()));
    }

    /**
     * 1 2 3 4
     * 5 5 5 5
     */
    private void set5_2() {
        fsdLayout.setUnitHeightNum(4);
        fsdLayout.setUnitWidthNum(4);
        detailViewList.add(new DetailView(new Point(0, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 1), 4, 3, createRecycler()));
    }

    /**
     * 1 2
     * 1 3
     * 1 4
     * 1 5
     */
    private void set5_3() {
        fsdLayout.setUnitWidthNum(3);
        fsdLayout.setUnitHeightNum(4);
        detailViewList.add(new DetailView(new Point(0, 0), 2, 4, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 3), 1, 1, createRecycler()));
    }

    /**
     * 1 2
     * 3 2
     * 4 2
     * 5 2
     */
    private void set5_4() {
        fsdLayout.setUnitWidthNum(3);
        fsdLayout.setUnitHeightNum(4);
        detailViewList.add(new DetailView(new Point(0, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 0), 2, 4, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 3), 1, 1, createRecycler()));
    }

    /**
     * 1 1 2
     * 1 1 3
     * 4 5 6
     */
    private void set6_1() {
        fsdLayout.setUnitWidthNum(3);
        fsdLayout.setUnitHeightNum(3);

        detailViewList.add(new DetailView(new Point(0, 0), 2, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 2), 1, 1, createRecycler()));
    }

    /**
     * 1 2 2
     * 3 2 2
     * 4 5 6
     */
    private void set6_2() {
        fsdLayout.setUnitWidthNum(3);
        fsdLayout.setUnitHeightNum(3);

        detailViewList.add(new DetailView(new Point(0, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 0), 2, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 2), 1, 1, createRecycler()));
    }

    /**
     * 1 2 3
     * 4 5 5
     * 6 5 5
     */
    private void set6_3() {
        fsdLayout.setUnitWidthNum(3);
        fsdLayout.setUnitHeightNum(3);

        detailViewList.add(new DetailView(new Point(0, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 1), 2, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 2), 1, 1, createRecycler()));
    }

    /**
     * 1 2 3
     * 4 4 5
     * 4 4 6
     */
    private void set6_4() {
        fsdLayout.setUnitWidthNum(3);
        fsdLayout.setUnitHeightNum(3);

        detailViewList.add(new DetailView(new Point(0, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 1), 2, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 2), 1, 1, createRecycler()));
    }

    /**
     * 1 2 3
     * 4 5 6
     */
    private void set6_5() {
        fsdLayout.setUnitWidthNum(3);
        fsdLayout.setUnitHeightNum(4);

        detailViewList.add(new DetailView(new Point(0, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 2), 1, 1, createRecycler()));
    }

    /**
     * 1 1 2 2
     * 1 1 2 2
     * 3 3 4 5
     * 3 3 6 7
     */
    private void set7_1() {
        fsdLayout.setUnitWidthNum(4);
        fsdLayout.setUnitHeightNum(4);

        detailViewList.add(new DetailView(new Point(0, 0), 2, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 0), 2, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 2), 2, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 3), 1, 1, createRecycler()));
    }


    /**
     * 1 1 2 2
     * 1 1 2 2
     * 3 4 5 5
     * 6 7 5 5
     */
    private void set7_2() {
        fsdLayout.setUnitWidthNum(4);
        fsdLayout.setUnitHeightNum(4);

        detailViewList.add(new DetailView(new Point(0, 0), 2, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 0), 2, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 2), 2, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 3), 1, 1, createRecycler()));
    }

    /**
     * 1 1 2 3
     * 1 1 4 5
     * 6 6 7 7
     * 6 6 7 7
     */
    private void set7_3() {
        fsdLayout.setUnitWidthNum(4);
        fsdLayout.setUnitHeightNum(4);

        detailViewList.add(new DetailView(new Point(0, 0), 2, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 2), 2, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 2), 2, 2, createRecycler()));
    }

    /**
     * 1 2 3 3
     * 4 5 3 3
     * 6 6 7 7
     * 6 6 7 7
     */
    private void set7_4() {
        fsdLayout.setUnitWidthNum(4);
        fsdLayout.setUnitHeightNum(4);

        detailViewList.add(new DetailView(new Point(0, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 0), 2, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 2), 2, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 2), 2, 2, createRecycler()));
    }

    /**
     * 1 1 1 2
     * 1 1 1 3
     * 1 1 1 4
     * 5 6 7 8
     */
    private void set8_1() {
        fsdLayout.setUnitWidthNum(4);
        fsdLayout.setUnitHeightNum(4);

        detailViewList.add(new DetailView(new Point(0, 0), 3, 3, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 3), 1, 1, createRecycler()));
    }


    /**
     * 1 2 2 2
     * 3 2 2 2
     * 4 2 2 2
     * 5 6 7 8
     */
    private void set8_2() {
        fsdLayout.setUnitWidthNum(4);
        fsdLayout.setUnitHeightNum(4);

        detailViewList.add(new DetailView(new Point(0, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 0), 3, 3, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 3), 1, 1, createRecycler()));
    }

    /**
     * 1 2 3 4
     * 5 6 6 6
     * 6 6 6 6
     * 8 6 6 6
     */
    private void set8_3() {
        fsdLayout.setUnitWidthNum(4);
        fsdLayout.setUnitHeightNum(4);

        detailViewList.add(new DetailView(new Point(0, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 1), 3, 3, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 3), 1, 1, createRecycler()));
    }

    /**
     * 1 2 3 4
     * 5 5 5 6
     * 5 5 5 7
     * 5 5 5 8
     */
    private void set8_4() {
        fsdLayout.setUnitWidthNum(4);
        fsdLayout.setUnitHeightNum(4);

        detailViewList.add(new DetailView(new Point(0, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 1), 3, 3, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 3), 1, 1, createRecycler()));
    }

    /**
     * 1 2 3
     * 4 5 6
     * 7 8 9
     */
    private void set9_1() {
        fsdLayout.setUnitWidthNum(3);
        fsdLayout.setUnitHeightNum(3);

        detailViewList.add(new DetailView(new Point(0, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 2), 1, 1, createRecycler()));
    }

    /**
     * 1 1 2 2
     * 1 1 2 2
     * 3 4 5 6
     * 7 8 9 10
     */
    private void set10_1() {
        fsdLayout.setUnitWidthNum(4);
        fsdLayout.setUnitHeightNum(4);

        detailViewList.add(new DetailView(new Point(0, 0), 2, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 0), 2, 2, createRecycler()));

        detailViewList.add(new DetailView(new Point(0, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 3), 1, 1, createRecycler()));
    }

    /**
     * 1 2 3 4
     * 5 6 7 8
     * 9 9 10 10
     * 9 9 10 10
     */
    private void set10_2() {
        fsdLayout.setUnitWidthNum(4);
        fsdLayout.setUnitHeightNum(4);

        detailViewList.add(new DetailView(new Point(0, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 2), 2, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 2), 2, 2, createRecycler()));
    }

    /**
     * 1 1 2 3
     * 1 1 4 5
     * 6 6 7 8
     * 6 6 9 10
     */
    private void set10_3() {
        fsdLayout.setUnitWidthNum(4);
        fsdLayout.setUnitHeightNum(4);

        detailViewList.add(new DetailView(new Point(0, 0), 2, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 2), 2, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 3), 1, 1, createRecycler()));
    }

    /**
     * 1 2 3 3
     * 4 5 3 3
     * 6 7 8 8
     * 9 10 8 8
     */
    private void set10_4() {
        fsdLayout.setUnitWidthNum(4);
        fsdLayout.setUnitHeightNum(4);

        detailViewList.add(new DetailView(new Point(0, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 0), 2, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 2), 2, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 3), 1, 1, createRecycler()));
    }

    /**
     * 1 2 3 4
     * 5 5 6 6
     * 5 5 6 6
     * 7 8 9 10
     */
    private void set10_5() {
        fsdLayout.setUnitWidthNum(4);
        fsdLayout.setUnitHeightNum(4);

        detailViewList.add(new DetailView(new Point(0, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 1), 2, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 1), 2, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 3), 1, 1, createRecycler()));
    }

    /**
     * 1 2 2 3
     * 4 2 2 5
     * 6 7 7 8
     * 9 7 7 10
     */
    private void set10_6() {
        fsdLayout.setUnitWidthNum(4);
        fsdLayout.setUnitHeightNum(4);

        detailViewList.add(new DetailView(new Point(0, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 0), 2, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 2), 2, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 3), 1, 1, createRecycler()));
    }


    /**
     * 1 1 2 3
     * 1 1 4 5
     * 6 7 8 9
     * 10 11 12 13
     */
    private void set13_1() {
        fsdLayout.setUnitWidthNum(4);
        fsdLayout.setUnitHeightNum(4);

        detailViewList.add(new DetailView(new Point(0, 0), 2, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 1), 1, 1, createRecycler()));

        detailViewList.add(new DetailView(new Point(0, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 2), 1, 1, createRecycler()));

        detailViewList.add(new DetailView(new Point(0, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 3), 1, 1, createRecycler()));
    }

    /**
     * 1 2 3 3
     * 4 5 3 3
     * 6 7 8 9
     * 10 11 12 13
     */
    private void set13_2() {
        fsdLayout.setUnitWidthNum(4);
        fsdLayout.setUnitHeightNum(4);

        detailViewList.add(new DetailView(new Point(0, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 0), 2, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 3), 1, 1, createRecycler()));
    }

    /**
     * 1 2 3 3
     * 4 5 3 3
     * 6 7 8 9
     * 10 11 12 13
     */
    private void set13_3() {
        fsdLayout.setUnitWidthNum(4);
        fsdLayout.setUnitHeightNum(4);

        detailViewList.add(new DetailView(new Point(0, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 1), 1, 1, createRecycler()));

        detailViewList.add(new DetailView(new Point(0, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 2), 2, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 3), 1, 1, createRecycler()));
    }

    /**
     * 1 2 3 4
     * 5 6 7 8
     * 9 9 10 11
     * 9 9 12 13
     */
    private void set13_4() {
        fsdLayout.setUnitWidthNum(4);
        fsdLayout.setUnitHeightNum(4);

        detailViewList.add(new DetailView(new Point(0, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 1), 1, 1, createRecycler()));

        detailViewList.add(new DetailView(new Point(0, 2), 2, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 3), 1, 1, createRecycler()));
    }

    /**
     * 1 2 3 4
     * 5 6 6 7
     * 8 6 6 9
     * 10 11 12 13
     */
    private void set13_5() {
        fsdLayout.setUnitWidthNum(4);
        fsdLayout.setUnitHeightNum(4);

        detailViewList.add(new DetailView(new Point(0, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 1), 2, 2, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(0, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 3), 1, 1, createRecycler()));
    }

    /**
     * 1  2  3  4
     * 5  6  7  8
     * 9 10 11 12
     * 13 14 15 16
     */
    private void set16_1() {
        fsdLayout.setUnitWidthNum(4);
        fsdLayout.setUnitHeightNum(4);

        detailViewList.add(new DetailView(new Point(0, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 0), 1, 1, createRecycler()));

        detailViewList.add(new DetailView(new Point(0, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 1), 1, 1, createRecycler()));

        detailViewList.add(new DetailView(new Point(0, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 2), 1, 1, createRecycler()));

        detailViewList.add(new DetailView(new Point(0, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 3), 1, 1, createRecycler()));
    }

    /**
     * 1  2  3  4 5
     * 6  7  8  9 10
     * 11  12  13  14 15
     * 16  17  18  19 20
     */
    private void set20_1() {
        fsdLayout.setUnitWidthNum(5);
        fsdLayout.setUnitHeightNum(4);

        detailViewList.add(new DetailView(new Point(0, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(4, 0), 1, 1, createRecycler()));

        detailViewList.add(new DetailView(new Point(0, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(4, 1), 1, 1, createRecycler()));

        detailViewList.add(new DetailView(new Point(0, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(4, 2), 1, 1, createRecycler()));

        detailViewList.add(new DetailView(new Point(0, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(4, 3), 1, 1, createRecycler()));

    }

    /**
     * 1  2  3  4
     * 5  6  7  8
     * 9 10 11 12
     * 13 14 15 16
     */
    private void set24_1() {
        fsdLayout.setUnitWidthNum(6);
        fsdLayout.setUnitHeightNum(4);

        detailViewList.add(new DetailView(new Point(0, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(4, 0), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(5, 0), 1, 1, createRecycler()));

        detailViewList.add(new DetailView(new Point(0, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(4, 1), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(5, 1), 1, 1, createRecycler()));

        detailViewList.add(new DetailView(new Point(0, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(4, 2), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(5, 2), 1, 1, createRecycler()));

        detailViewList.add(new DetailView(new Point(0, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(1, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(2, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(3, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(4, 3), 1, 1, createRecycler()));
        detailViewList.add(new DetailView(new Point(5, 3), 1, 1, createRecycler()));
    }

}
