package com.zxwl.frame.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.zxwl.frame.App;
import com.zxwl.frame.AppManager;
import com.zxwl.frame.R;
import com.zxwl.frame.bean.UserInfo;
import com.zxwl.frame.constant.Account;
import com.zxwl.frame.utils.ACache;
import com.zxwl.frame.utils.UserHelper;
import com.zxwl.frame.utils.sharedpreferences.PreferencesHelper;

/**
 * 主页
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvLogOut;//退出登录
    private TextView tvIssue;//帮助
    private TextView tvHome;//返回主页
    private TextView tvName;//名字

    private TextView tvConfSubscribe;
    private TextView tvConfApprove;
    private TextView tvConfControl;
    private TextView tvTemplateManage;

    /**
     * 判断是否已经点击过一次回退键
     */
    private boolean isBackPressed = false;
    private ACache mCache;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, HomeActivity.class));
    }

    @Override
    protected void findViews() {
        tvLogOut = (TextView) findViewById(R.id.tv_log_out);
        tvIssue = (TextView) findViewById(R.id.tv_issue);
        tvHome = (TextView) findViewById(R.id.tv_home);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvConfSubscribe = (TextView) findViewById(R.id.tv_conf_subscribe);
        tvConfApprove = (TextView) findViewById(R.id.tv_conf_approve);
        tvConfControl = (TextView) findViewById(R.id.tv_conf_control);
        tvTemplateManage = (TextView) findViewById(R.id.tv_template_manage);
    }

    @Override
    protected void initData() {

        judgePower();

        tvLogOut.setVisibility(View.VISIBLE);
        tvIssue.setVisibility(View.VISIBLE);
        tvHome.setVisibility(View.VISIBLE);
        tvName.setVisibility(View.VISIBLE);

        if (null != UserHelper.getSavedUser()) {
            tvName.setText(UserHelper.getSavedUser().name);
        }
    }

    /**
     * TODO 后期需要删除
     * 判断权限,如果权限过期则不可用
     */
    private void judgePower() {
        mCache = ACache.get(App.getInstance());
        //获得缓存的对象
        String asString = mCache.getAsString(Account.LOGIN_TIME);
        //判断是否存在这个值
        if (TextUtils.isEmpty(asString)) {
            new MaterialDialog.Builder(HomeActivity.this)
                    .title("提示")
                    .content("授权已过期,如需使用,请联系中讯网联")
                    .positiveText("确认")
                    .dismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            finish();
                            AppManager.getInstance().appExit();
                        }
                    })
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    })
                    .build()
                    .show();
        }
    }

    @Override
    protected void setListener() {
        tvConfSubscribe.setOnClickListener(this);
        tvConfApprove.setOnClickListener(this);
        tvConfControl.setOnClickListener(this);
        tvTemplateManage.setOnClickListener(this);

        tvLogOut.setOnClickListener(this);//退出登录
        tvIssue.setOnClickListener(this);//帮助
        tvHome.setOnClickListener(this);//返回主页
        tvName.setOnClickListener(this);//名字
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //会议预约
            case R.id.tv_conf_subscribe:
                NewConfActivity.startActivity(this);
                break;

            //会议审批
            case R.id.tv_conf_approve:
                ConfApprovalListActivity.startActivity(this);
                break;

            //会议控制
            case R.id.tv_conf_control:
                ExpandableConfControlListActivity.startActivity(this);
                break;

            //会议模板
            case R.id.tv_template_manage:
                Toast.makeText(this, "此功能正在开发中", Toast.LENGTH_SHORT).show();
                break;

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
                                LoginActivity.startActivity(HomeActivity.this);
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

    private void doublePressBackToast() {
        if (!isBackPressed) {
            Log.i("doublePressBackToast", "再次点击返回退出程序");
            isBackPressed = true;
            Toast.makeText(this, "再次点击返回退出程序", Toast.LENGTH_SHORT).show();
        } else {
            Log.i("doublePressBackToast", "exit");
            finish();
            AppManager.getInstance().appExit();
        }
        new Handler().postDelayed(
                () -> {
                    isBackPressed = false;
                }, 2000);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            doublePressBackToast();
            return true;
        } else {
            return super.onKeyUp(keyCode, event);
        }
    }
}
