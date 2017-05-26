package com.zxwl.frame.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.zxwl.frame.App;
import com.zxwl.frame.R;
import com.zxwl.frame.constant.Account;
import com.zxwl.frame.utils.ACache;
import com.zxwl.frame.utils.sharedpreferences.PreferencesHelper;
import com.zxwl.frame.utils.statusbar.SystemUiVisibilityUtil;

/**
 * 欢迎界面
 */
public class WelcomActivity extends AppCompatActivity {

    private static final long DELAY_TIME = 2000;
    private ACache mCache;
    //是否登录过
    private String hsaLogin = "false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        SystemUiVisibilityUtil.hideStatusBar(getWindow(), true);

        mCache = ACache.get(App.getInstance());
    }

    @Override
    protected void onStart() {
        super.onStart();

        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        //获得是否登录过
                        hsaLogin = PreferencesHelper.getData(Account.IS_LOGIN);
                        if ("true".equals(hsaLogin)) {
                            HomeActivity.startActivity(WelcomActivity.this);
                        } else {
                            LoginActivity.startActivity(WelcomActivity.this);
                        }
                        finish();
                    }
                }, DELAY_TIME);
    }
}
