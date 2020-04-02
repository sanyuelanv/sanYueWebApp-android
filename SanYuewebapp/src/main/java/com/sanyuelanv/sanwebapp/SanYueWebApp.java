package com.sanyuelanv.sanwebapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.sanyuelanv.sanwebapp.activity.MainWebAppActivity;
import com.sanyuelanv.sanwebapp.bean.SanYueAppInfoItem;
import com.sanyuelanv.sanwebapp.utils.SanYueNetCacheInterceptor;
import com.sanyuelanv.sanwebapp.utils.SanYueWebAppFileUtils;

import java.io.File;
import java.util.ArrayList;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Create By songhang in 2020-02-26
 */
public class SanYueWebApp {
    // 启动类 - 单例
    // 传入 URL - 分割url 判定版本 - 读取存储 - 判定是否更新 - 下载 - 打开
    // 传入 context ，创建handle
    // 创建
    private static SanYueWebApp instance = null;
    private static File cacheDirectory;
    public static String cacheDirectoryString = "sanyueWebCache";

    private  OkHttpClient resourcesClient;
    private Context context;
    private Handler handler;
    private Thread mainThread; // 主线程对象
    private int mainThreadId; // 主线程 ID
    private SanYueAppInfoItem appInfoItem;
    private String [] customMethods;
    private String [] defaultJsMethods = {
        "SanYue_init",
        "SanYue_fetch",
        "SanYue_getSystemInfo",
        "SanYue_setStatusStyle",
        "SanYue_showModal",
        "SanYue_showActionSheet",
        "SanYue_vibrateLong",
        "SanYue_vibrateShort",
        "SanYue_getNetworkType",
        "SanYue_setClipboardData",
        "SanYue_getClipboardData",
        "SanYue_showToast",
        "SanYue_showLoad",
        "SanYue_hiddenLoad",
        "SanYue_setNavBar",
        "SanYue_showPick",
        "SanYue_fetch_stop",
        "SanYue_navPush",
        "SanYue_navPop",
        "SanYue_setPopExtra",
        "SanYue_navReplace",
    };

    private ArrayList<Activity> router = new ArrayList<Activity>();
    private   int timeout = 20;
    private   boolean debug = false;

    public static SanYueWebApp initInstance(Context context){
        if (instance == null){
            instance = new SanYueWebApp(context);
        }
        return instance;
    }

    public SanYueWebApp(Context context) {
        init(context);
    }

    public static SanYueWebApp getInstance(){
        return instance;
    }

    public SanYueAppInfoItem getAppInfoItem() {
        if(appInfoItem == null){
            appInfoItem = new SanYueAppInfoItem(context);
        }
        return appInfoItem;
    }

    public static String getCacheSize(Context context){
        File rootFile = new File(context.getCacheDir(), SanYueWebAppFileUtils.rootPath);
        long size =  SanYueWebAppFileUtils.getDirSize(rootFile);
        return  SanYueWebAppFileUtils.formatFileSize(size);
    }
    public static String removeCache(Context context){
        File rootFile = new File(context.getCacheDir(), SanYueWebAppFileUtils.rootPath);
        SanYueWebAppFileUtils.removeFile(rootFile);
        long size =  SanYueWebAppFileUtils.getDirSize(rootFile);
        return  SanYueWebAppFileUtils.formatFileSize(size);
    }
    public  void create(Activity activity, String url){
        init(activity.getApplicationContext());
        Context myContext = activity.getBaseContext();
        Intent intent = new Intent(myContext , MainWebAppActivity.class);
        intent.putExtra("init",true);
        intent.putExtra("url",url);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivityForResult(intent,200);
        activity.overridePendingTransition(R.anim.sanyue_anim_present_in,R.anim.sanyue_anim_present);
    }
    public void  init(Context appContext){
        context = appContext;
        handler = handler == null ?   new Handler() : handler;
        mainThread = Thread.currentThread();
        mainThreadId = android.os.Process.myTid();
    }
    public  void destroy(){
        instance = null;
        handler = null;
        mainThread = null;
        mainThreadId = 0;
        context = null;
    }
    //路由
    public int pushRouter(Activity activity) {
        this.router.add(activity);
        return this.router.size();
    }
    public int popRouter(Activity activity) {
        this.router.remove(activity);
        return this.router.size();
    }
    public int replaceRouter(Activity activity) {
        int size = this.router.size();
        this.router.set(size - 1,activity);
        return size;
    }
    public int getRouterNumber() {
        return this.router.size();
    }
    // bean

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public Context getContext() {
        return context;
    }

    public Handler getHandler() {
        return handler;
    }

    public Thread getMainThread() {
        return mainThread;
    }

    public int getMainThreadId() {
        return mainThreadId;
    }

    public String[] getDefaultJsMethods() {
        return defaultJsMethods;
    }

    public String[] getCustomMethods() {
        return customMethods;
    }
    public void setCustomMethods(String[] customMethods) {
        this.customMethods = customMethods;
    }

    public OkHttpClient getResourcesClient() {
        if (resourcesClient == null){
            cacheDirectory = new File(context.getCacheDir(),cacheDirectoryString);
            int cacheSize = 1024 * 1024 * 1024; // 10 MiB
            Cache cache = new Cache(cacheDirectory, cacheSize);
            resourcesClient = new OkHttpClient.Builder()
                    //.addNetworkInterceptor(new SanYueNetCacheInterceptor(60*60*24*7))
                    .cache(cache).build();
        }
        return resourcesClient;
    }
}
