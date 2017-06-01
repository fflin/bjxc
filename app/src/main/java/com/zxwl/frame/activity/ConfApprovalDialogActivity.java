package com.zxwl.frame.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dinuscxj.itemdecoration.GridOffsetsItemDecoration;
import com.zxwl.frame.R;
import com.zxwl.frame.adapter.NewConfExpandableListViewAdapter;
import com.zxwl.frame.bean.ConfBean;
import com.zxwl.frame.bean.DataList;
import com.zxwl.frame.bean.DepartBean;
import com.zxwl.frame.bean.Employee;
import com.zxwl.frame.net.api.ConfApi;
import com.zxwl.frame.net.callback.RxSubscriber;
import com.zxwl.frame.net.exception.ResponeThrowable;
import com.zxwl.frame.net.http.HttpUtils;
import com.zxwl.frame.net.transformer.ListDefaultTransformer;
import com.zxwl.frame.rx.RxBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jp.wasabeef.richeditor.RichEditor;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 会议审批对话框
 */
public class ConfApprovalDialogActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvTitle;//标题
    private TextView tvName;//会议名称
    private TextView tvContactsName;//联系人
    private TextView tvPhone;//联系电话
    private TextView tvConfNumber;//会议号码
    private TextView tvDeviceInfo;//设备信息
    private TextView tvCheck;//查看会议室详情
    private TextView tvConfTime;//会议时间
    private TextView tvDuration;//会议时长
    private TextView tvEmailCheck;//是否用邮件通知
    private TextView tvSmsCheck;//是否用短信通知
    private RichEditor etSmsContent;//短信内容
    private EditText etOpinion;//审批意见
    private TextView tvPass;//通过
    private TextView tvReject;//驳回

    public static final String CONF_BEAN = "conf_bean";

    private ConfBean confBean;//从上个页面传递过来的会议bean
    private String contactList;//参会列表

    private List<Employee> hisEmployee = new ArrayList<>();
    private HashMap<String, List<Employee>> maps;
    private List<String> org1Names;
    private NewConfExpandableListViewAdapter expAdapter;
    private ExpandableListView explv;
    private AlertDialog alertDialog;

    @Override
    protected void findViews() {
        tvTitle = (TextView) findViewById(R.id.tv_title_lable);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvContactsName = (TextView) findViewById(R.id.tv_contacts_name);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvConfNumber = (TextView) findViewById(R.id.tv_conf_number);
        tvDeviceInfo = (TextView) findViewById(R.id.tv_device_info);
        tvCheck = (TextView) findViewById(R.id.tv_check);
        tvConfTime = (TextView) findViewById(R.id.tv_conf_time);
        tvDuration = (TextView) findViewById(R.id.tv_duration);
        tvEmailCheck = (TextView) findViewById(R.id.tv_email_check);
        tvSmsCheck = (TextView) findViewById(R.id.tv_sms_check);
        etSmsContent = (RichEditor) findViewById(R.id.et_sms_content);
        etOpinion = (EditText) findViewById(R.id.et_opinion);
        tvPass = (TextView) findViewById(R.id.tv_pass);
        tvReject = (TextView) findViewById(R.id.tv_reject);
    }

    @Override
    protected void initData() {
        confBean = (ConfBean) getIntent().getSerializableExtra(CONF_BEAN);

        getHistoryById(confBean.id);

        if (null != confBean) {
            tvName.setText(confBean.name);//会议名称
            tvContactsName.setText(confBean.contactPeople);//联系人名称
            tvPhone.setText(confBean.contactTelephone);//电话
            tvConfNumber.setText(confBean.smcConfId);//会议号
            tvConfTime.setText(confBean.beginTime);//开始时间
            tvDuration.setText("预计" + confBean.endTime + "结束会议,时长" + confBean.duration + "分钟");//结束时间
            etSmsContent.setHtml(textToHtml(confBean.smsContext));//短信内容
        }
    }

    @Override
    protected void setListener() {
        tvTitle.setOnClickListener(this);
        tvCheck.setOnClickListener(this);
        tvPass.setOnClickListener(this);
        tvReject.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_conf_approval_dialog;
    }

    public static void startActivity(Context context, ConfBean confBean) {
        Intent intent = new Intent(context, ConfApprovalDialogActivity.class);
        intent.putExtra(CONF_BEAN, confBean);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //关闭按钮
            case R.id.tv_title_lable:
                finish();
                break;

            case R.id.tv_check:
                //创建对话框
                alertDialog = new AlertDialog.Builder(this, R.style.dialogstyle)
                        .setView(initDialogContent(null,null,null))
                        .create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
                //设置对话框的尺寸
                Window dialogWindow = alertDialog.getWindow();
                WindowManager m = getWindowManager();
                Display d = m.getDefaultDisplay(); // 获取屏幕宽、高度
                WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
                p.height = (int) (d.getHeight() * 0.8); // 高度设置为屏幕的0.8，根据实际情况调整
                p.width = (int) (d.getWidth() * 0.6); // 宽度设置为屏幕的0.6，根据实际情况调整
                dialogWindow.setAttributes(p);
                break;

            //通过
            case R.id.tv_pass:
                passRequest();
                break;

            //驳回
            case R.id.tv_reject:
                rejectRquest();
                break;

            default:
                break;
        }
    }

    @NonNull
    private View initDialogContent(RecyclerView.Adapter adapter, RecyclerView.LayoutManager layoutManager, GridOffsetsItemDecoration decoration) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.activity_meeting_detail_dialog, null);
        TextView tv_close = (TextView) dialogView.findViewById(R.id.tv_title_lable);
        explv = (ExpandableListView) dialogView.findViewById(R.id.expand_list);
        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        if (maps.size() != 0 && org1Names.size() != 0) {
            expAdapter = new NewConfExpandableListViewAdapter(ConfApprovalDialogActivity.this, org1Names, maps);
            explv.setAdapter(expAdapter);
        }
        return dialogView;
    }

    /**
     * 会议审批通过的接口
     */
    private void passRequest() {
        String vetos = etOpinion.getText().toString();
        HttpUtils.getInstance(this)
                .getRetofitClinet()
                .builder(ConfApi.class)
                .approveEntity(
                        contactList,
                        confBean.confParameters,
                        confBean.name,
                        confBean.schedulingTime,
                        confBean.endTime,
                        confBean.duration,
                        confBean.isEmail,
                        confBean.isSms,
                        confBean.smsId,
                        confBean.smsTitle,
                        confBean.smsContext,
                        confBean.contactPeople,
                        confBean.contactTelephone,
                        confBean.peopleIdOa,
                        confBean.id,
                        vetos)
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<String>() {
                    @Override
                    public void onSuccess(String s) {
                        RxBus.getInstance().post(0);
                        Toast.makeText(ConfApprovalDialogActivity.this, "会议通过", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    protected void onError(ResponeThrowable responeThrowable) {
                        Toast.makeText(ConfApprovalDialogActivity.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    /**
     * 驳回的网络请求
     */
    private void rejectRquest() {
        HttpUtils.getInstance(this)
                .getRetofitClinet()
                .builder(ConfApi.class)
                .approveCancel(confBean.id)
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<String>() {
                    @Override
                    public void onSuccess(String s) {
                        RxBus.getInstance().post(0);
                        Toast.makeText(ConfApprovalDialogActivity.this, "会议驳回", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    protected void onError(ResponeThrowable responeThrowable) {
                        Toast.makeText(ConfApprovalDialogActivity.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    /**
     * 通过id查询历史会议
     *
     * @param id 会议Id
     */
    private void getHistoryById(String id) {
        HttpUtils.getInstance(this)
                .getRetofitClinet()
                .builder(ConfApi.class)
                .findHistoryById(id)
                .flatMap(new Func1<String, Observable<DataList<DepartBean>>>() {
                    @Override
                    public Observable<DataList<DepartBean>> call(String s) {
                        contactList = s;
                        return HttpUtils//
                                .getInstance(ConfApprovalDialogActivity.this)
                                .getRetofitClinet()
                                .builder(ConfApi.class)
                                .getPeopleList(s);
                    }
                })
                .compose(new ListDefaultTransformer<DepartBean>())
                .subscribe(new RxSubscriber<List<DepartBean>>() {
                    @Override
                    public void onSuccess(List<DepartBean> departments) {
                        //根据得到的参会人员列表departments设置右边的数据
                        Log.i("TAG", departments.toString());
                        maps = new HashMap<>();
                        org1Names = new ArrayList<>();
                        hisEmployee.clear();
                        for (int i = 0; i < departments.size(); i++) {
                            String[] strName = departments.get(i).getEmployeeName().split(",");
                            String[] strId = departments.get(i).getEmployeeId().split(",");
                            String orgName = departments.get(i).getDeptName();
                            for (int j = 0; j < strName.length; j++) {
                                Employee employee = new Employee(strId[j],strName[j]);
                                employee.setName(strName[j]);
                                employee.setId(strId[j]);
                                employee.setOrgName(orgName);
                                hisEmployee.add(employee);
                            }
                        }

                        maps.clear();
                        org1Names.clear();
                        for (int i = 0; i < hisEmployee.size(); i++) {
                            //EmployeeBean bean = new EmployeeBean();
                            String orgName = hisEmployee.get(i).getOrgName();
                            String name = hisEmployee.get(i).getName();
                            Employee employee = hisEmployee.get(i);

                            List<Employee> list = maps.get(orgName);
                            if (list == null) {
                                list = new ArrayList<>();
                            }
                            if (!list.contains(employee)) {
                                list.add(employee);
                            }
                            if (!maps.containsKey(orgName)) {
                                maps.put(orgName, list);
                            }
                        }

                        Set<String> set = maps.keySet();
                        Iterator<String> iterator = set.iterator();
                        for (int i = 0; i < maps.size(); i++) {
                            String key = iterator.next();
                            if (!org1Names.contains(key)) {
                                org1Names.add(key);
                            }
                            List<Employee> values = maps.get(key);
                        }
                    }

                    @Override
                    protected void onError(ResponeThrowable responeThrowable) {
                        Toast.makeText(ConfApprovalDialogActivity.this, responeThrowable.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 将里面的文本转换成图片
     *
     * @param context 传递过来的文本数据
     * @return 返回包含图片的数据
     */
    private String textToHtml(String context) {
        //会场名称
        //String imgUrl = <img src="file:///android_asset/ic_launcher.png" alt="">;
        String hcmc = "<img src=\"file:///android_asset/icon_hcmc.png\" alt=\"\">";
        String hyhm = "<img src=\"file:///android_asset/icon_hyhm.png\" alt=\"\">";
        String hymc = "<img src=\"file:///android_asset/icon_hymc.png\" alt=\"\">";
        String hysj = "<img src=\"file:///android_asset/icon_hysj.png\" alt=\"\">";
        String sjr = "<img src=\"file:///android_asset/icon_sjr.png\" alt=\"\">";
        return context.replaceAll("&amp;lt;--会场名称--&amp;gt", hcmc)
                .replaceAll("&amp;lt;--会议号码--&amp;gt", hyhm)
                .replaceAll("&amp;lt;--会议名称--&amp;gt", hymc)
                .replaceAll("&amp;lt;--会议时间--&amp;gt", hysj)
                .replaceAll("&amp;lt;--收件人--&amp;gt", sjr);
    }
}
