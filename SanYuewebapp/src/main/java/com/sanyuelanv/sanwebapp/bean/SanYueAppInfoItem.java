package com.sanyuelanv.sanwebapp.bean;

import android.content.Context;
import android.os.Build;

import com.sanyuelanv.sanwebapp.utils.SanYueUIUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Create By songhang in 2020/3/9
 */
public class SanYueAppInfoItem {
    private String phoneName;
    private String system;
    private String systemVersion;
    private int screenWidth;
    private int screenHeight;
    private int windowWidth;
    private int windowHeight;
    private int statusBarHeight;
    private int statusBarHeightPX;

    private int windowWidthPx;
    private int windowHeightPx;
    private int fullHeight;
    private int actionSheetHeight;
    private Context mContext;

    public SanYueAppInfoItem(Context context) {
        mContext = context;
        phoneName =  getDeviceName();
        system = "android";
        systemVersion = Build.VERSION.RELEASE;
        windowWidthPx = SanYueUIUtils.getScreenWidth(context);
        windowHeightPx = SanYueUIUtils.getScreenHeight(context);

        screenWidth = SanYueUIUtils.px2dp(context,windowWidthPx);
        screenHeight = SanYueUIUtils.px2dp(context,windowHeightPx);
        statusBarHeightPX = SanYueUIUtils.getStatusBarHeight(context,true);
        statusBarHeight = SanYueUIUtils.px2dp(context,statusBarHeightPX);
        windowHeight = 0;
        windowWidth = screenWidth;
    }

    public int getFullHeight() {
        if (fullHeight == 0){
            int h = windowHeightPx;
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP){
                h -= SanYueUIUtils.getStatusBarHeight(mContext,false);
            }
            fullHeight = h;
        }
        return fullHeight;
    }

    public void setFullHeight(int fullHeight) {
        this.fullHeight = fullHeight;
    }

    public JSONObject getJavaScriptResult(Boolean hideNav) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phoneName",phoneName);
        jsonObject.put("system",system);
        jsonObject.put("systemVersion",systemVersion);
        jsonObject.put("screenWidth",screenWidth);
        jsonObject.put("screenHeight",screenHeight);
        jsonObject.put("windowWidth",windowWidth);
        int h = screenHeight;
        if (hideNav){
            // 安卓5.0
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP){
                h = SanYueUIUtils.px2dp(mContext,windowHeightPx - SanYueUIUtils.getStatusBarHeight(mContext,false));
            }
        }
        else {
            h = SanYueUIUtils.px2dp(mContext,windowHeightPx - SanYueUIUtils.getStatusBarHeight(mContext,false)) - 44;
        }
        jsonObject.put("windowHeight",h);
        jsonObject.put("statusBarHeight",statusBarHeight);
        return jsonObject;
    }
    private String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }
    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public int getStatusBarHeight() {
        return statusBarHeight;
    }

    public void setStatusBarHeight(int statusBarHeight) {
        this.statusBarHeight = statusBarHeight;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public void setWindowWidth(int windowWidth) {
        this.windowWidth = windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public void setWindowHeight(int windowHeight) {
        this.windowHeight = windowHeight;
    }

    public int getWindowWidthPx() {
        return windowWidthPx;
    }

    public void setWindowWidthPx(int windowWidthPx) {
        this.windowWidthPx = windowWidthPx;
    }

    public int getWindowHeightPx() {
        return windowHeightPx;
    }

    public void setWindowHeightPx(int windowHeightPx) {
        this.windowHeightPx = windowHeightPx;
    }

    public int getStatusBarHeightPX() {
        return statusBarHeightPX;
    }

    public void setStatusBarHeightPX(int statusBarHeightPX) {
        this.statusBarHeightPX = statusBarHeightPX;
    }

    public int getActionSheetHeight() {
        if (actionSheetHeight == 0){
            actionSheetHeight = getFullHeight() - statusBarHeightPX;
        }
        return actionSheetHeight;
    }
}
