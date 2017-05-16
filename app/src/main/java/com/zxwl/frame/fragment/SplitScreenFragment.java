package com.zxwl.frame.fragment;


import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miao.freesizedraggablelayout.DetailView;
import com.miao.freesizedraggablelayout.FreeSizeDraggableLayout;
import com.orhanobut.logger.Logger;
import com.zxwl.frame.R;
import com.zxwl.frame.activity.ConfControlActivity;
import com.zxwl.frame.adapter.CallbackItemTouch;
import com.zxwl.frame.adapter.ConfControlGridAdapter;
import com.zxwl.frame.adapter.MyItemTouchHelperCallback;
import com.zxwl.frame.bean.ConferenceInfo;
import com.zxwl.frame.bean.ConferenceStatus;
import com.zxwl.frame.bean.Site;
import com.zxwl.frame.bean.SiteInfo;
import com.zxwl.frame.net.api.ConfApi;
import com.zxwl.frame.net.http.HttpUtils;
import com.zxwl.frame.utils.ViewUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 分屏
 */
public class SplitScreenFragment extends BaseFragment implements CallbackItemTouch, View.OnClickListener {
    private FreeSizeDraggableLayout fsdLayout;
    private List<DetailView> list = new ArrayList<>();
    private TextView tvTitle;//会议名称
    private TextView  tvSave;//保存
    private TextView  tvReset;//重置
    private TextView  tvAllSplit;//全部分屏样式

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

    private RecyclerView rvList;
    private ConfControlGridAdapter adapter;

    private Gson gson = new Gson();

    private ConferenceInfo conferenceInfo;//会议信息
    private ConferenceStatus conferenceStatus;//会议状态
    private List<Site> siteList = new ArrayList<>();

    private int currentIndex;//当前移动的下标
    private boolean falg;

    private String smcConfId;

    public SplitScreenFragment() {
    }

    public static SplitScreenFragment newInstance(String smcConfId) {
        SplitScreenFragment fragment = new SplitScreenFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ConfControlActivity.SMC_CONF_ID, smcConfId);
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


    }

    @Override
    protected void init() {
        fsdLayout.setUnitHeightNum(2);
        fsdLayout.setUnitWidthNum(2);
        list.clear();
        list.add(new DetailView(new Point(0, 0), 1, 1, createButton("0")));
        list.add(new DetailView(new Point(1, 0), 1, 1, createButton("1")));
        list.add(new DetailView(new Point(0, 1), 1, 1, createButton("2")));
        list.add(new DetailView(new Point(1, 1), 1, 1, createButton("3")));
        fsdLayout.setsubViewPadding(1);
        fsdLayout.setList(list);

        Bundle arguments = getArguments();
        smcConfId = (String) arguments.get(ConfControlActivity.SMC_CONF_ID);

        //TODO 测试代码 待删除
        Site site = null;
        for (int i = 0; i < 40; i++) {
            site = new Site();
            site.unitName = "单位" + i;
            SiteInfo siteInfo = new SiteInfo();
            siteInfo.name = "名字";
            site.siteInfo = siteInfo;
            siteList.add(site);
        }

        adapter = new ConfControlGridAdapter(siteList);
        rvList.setAdapter(adapter);
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
                        //设置新的布局样式
                        if (falg) {
                            //获得出于抬起点的view
                            DetailView detailView = list.get(eventCurrent);
                            //创建并设置新的view
                            detailView.setView(createLayout());
                            fsdLayout.setList(list);
                        }
                        break;

                    default:
                        break;
                }
                return false;
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
        ivOne.setOnClickListener(this);
        ivTwo.setOnClickListener(this);
        ivThree.setOnClickListener(this);
        ivFour.setOnClickListener(this);
        ivFive.setOnClickListener(this);
        ivSix.setOnClickListener(this);
        ivSeven.setOnClickListener(this);
        ivEight.setOnClickListener(this);
        ivNine.setOnClickListener(this);
        ivTen.setOnClickListener(this);//10
        ivThirteen.setOnClickListener(this);//13
        ivSixteen.setOnClickListener(this);//16
        ivTwenty.setOnClickListener(this);//20
        ivTwentyFour.setOnClickListener(this);//24
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
                                    siteList.get(i).showControl = false;
                                }

                                //刷新适配器
                                adapter.addAll(siteList);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }, throwable -> {
                            Toast.makeText(mContext, R.string.error_msg, Toast.LENGTH_SHORT).show();
                        }
                );
    }

    @Override
    public void itemTouchOnMove(int oldPosition, int newPosition) {
        currentIndex = oldPosition;
    }

    private Button createButton(String i) {
        final Button btn = new Button(getContext());
        btn.setText(i);
        btn.setGravity(Gravity.CENTER);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), btn.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        return btn;
    }

    private View createLayout() {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(R.mipmap.ic_launcher);
        return imageView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //保存
            case R.id.tv_save:
                break;
            
            //重置
            case R.id.tv_reset:
                break;
            
            //所有分屏样式
            case R.id.tv_all_split:
                break;

            //1
            case R.id.iv_one:
                break;

            //2
            case R.id.iv_two:
                break;

            //3
            case R.id.iv_three:
                break;
            //4
            case R.id.iv_four:
                break;

            //5
            case R.id.iv_five:
                break;

            //6
            case R.id.iv_six:
                break;

            //7
            case R.id.iv_seven:
                break;

            //8
            case R.id.iv_eight:
                break;

            //9
            case R.id.iv_nine:
                break;

            //10
            case R.id.iv_ten:
                break;

            //13
            case R.id.iv_thirteen:
                break;

            //16
            case R.id.iv_sixteen:
                break;

            //20
            case R.id.iv_twenty:
                break;

            //24
            case R.id.iv_twenty_four:
                break;

            default:
                break;
        }
    }
}
