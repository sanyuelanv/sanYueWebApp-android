package com.sanyuelanv.sanwebapp.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Create By songhang in 2020-02-26
 */
public class SanYueUIUtils {
    public static int getColor(int colorId, Context context){
        return context.getResources().getColor(colorId);
    }
    public static boolean isDeepColor(final String hexColor) {
        boolean flag = true;
        if (hexColor.startsWith("#") && hexColor.length() == 7){
            final int[] ret = new int[3];
            for (int i = 0; i < 3; i++) {
                ret[i] = Integer.parseInt(hexColor.substring(i * 2 + 1, i * 2 + 3), 16);
            }
            if(ret[0]*0.299 + ret[0]*0.578 + ret[0]*0.114 >= 192){  flag = false;  }
        }
        return flag;
    }
    public static GradientDrawable getDrawable(int radius,int strokeWidth,int strokeColor,int bgColor){
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        if (radius > 0){
            drawable.setCornerRadius(radius);
        }
        if (strokeWidth > 0){
            drawable.setStroke(strokeWidth,strokeColor);
        }
        drawable.setColor(bgColor);
        return drawable;
    }
    public static GradientDrawable getDrawable(float[] radii,int strokeWidth,int strokeColor,int bgColor){
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadii(radii);
        if (strokeWidth > 0){
            drawable.setStroke(strokeWidth,strokeColor);
        }
        drawable.setColor(bgColor);
        return drawable;
    }
    public static StateListDrawable getStateDrawable(int type){
        StateListDrawable bg = new StateListDrawable();
        Drawable pressed = type == 0 ? getDrawable(0,0,0,Color.parseColor("#30323232")) : getDrawable(0,0,0,Color.parseColor("#30999999"));
        bg.addState(new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled }, pressed);
        return bg;
    }
    public static int dp2px(Context context,int dp){
        //获取手机密度
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.9);//向上取整
    }
    public static int px2dp(Context context,int px){
        //获取手机密度
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.9);//向上取整
    }
    public static int getScreenWidth(Context context) {
        Resources resource = context.getResources();
        DisplayMetrics displayMetrics = resource.getDisplayMetrics();
        return displayMetrics.widthPixels;
    }
    public static int getScreenHeight(Context context) {
        // 该高度不包括 底部虚拟键的高度，但包括状态栏。
        Resources resource = context.getResources();
        DisplayMetrics displayMetrics = resource.getDisplayMetrics();
        return displayMetrics.heightPixels;
    }
    public static int getStatusBarHeight(Context context,Boolean flag) {
        if (flag){
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) return  0;
        }
        Resources resource = context.getResources();
        int result = 0;
        int resourceId = resource.getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0 ) {  result = resource.getDimensionPixelSize(resourceId);  }
        return result;
    }
    public static void changeStausBarStyle(Window window,String type){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            if(type.equals("dark")){
                // 灰字
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN  | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            else {
                // 白字
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN  | View.SYSTEM_UI_FLAG_LAYOUT_STABLE );
            }

        }
    }
    public  static void  hideStatusBar(Window window){
        // 状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            View decorView = window.getDecorView();
            int option =  View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP){
                window.setStatusBarColor(Color.rgb(21,24,27));
                decorView.setSystemUiVisibility(option);
            }
            else {
                option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN  | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                window.setStatusBarColor(Color.TRANSPARENT);
            }
            decorView.setSystemUiVisibility(option);
        }
    }
}
