package com.zxwl.frame.utils;

import android.content.Context;
import android.content.res.Resources;

/**
 * Created by hcc on 16/8/4 21:18
 * <p/>
 * 屏幕像素转换工具类
 */
public class DisplayUtil {

    public static int px2dp(Context context, float pxValue) {

        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    public static int getScreenWidth() {
//        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }


    public static int getScreenHeight(Context context) {
//        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }


    public static float getDisplayDensity(Context context) {
//        if (context == null) {
//            return -1;
//        }
//        return context.getResources().getDisplayMetrics().density;
        return Resources.getSystem().getDisplayMetrics().density;
    }
}
