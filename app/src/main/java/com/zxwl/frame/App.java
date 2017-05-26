package com.zxwl.frame;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;
import com.zxwl.frame.net.Urls;
import com.zxwl.frame.net.config.NetWorkConfiguration;
import com.zxwl.frame.net.http.HttpUtils;
import com.zxwl.frame.utils.sharedpreferences.PreferencesHelper;

import java.io.File;

/**
 * Copyright 2015 蓝色互动. All rights reserved.
 * author：hw
 * data:2017/4/14 17:17
 */
public class App extends Application {
    private static App mInstance;

    public static App getInstance() {
        return mInstance;
    }

    public static File appDir;

    public static File getFile() {
        return appDir;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        //初始化缓存文件
        File cache = getCacheDir();
        appDir = new File(cache, "ACache");

        //初始化SharedPreferences
        PreferencesHelper.init(mInstance);

        //设置网络请求的基本参数
        NetWorkConfiguration configuration = new NetWorkConfiguration(this)
                .baseUrl(Urls.BASE_URL);
        HttpUtils.setConFiguration(configuration);

        //内存泄漏检测
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app include_common_operation this process.
            return;
        }
        LeakCanary.install(this);

        //腾讯bug线上搜集工具
        CrashReport.initCrashReport(getApplicationContext(), "568d7ac2da", false);
    }
}
