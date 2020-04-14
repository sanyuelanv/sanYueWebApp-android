package com.sanyuelanv.sanwebapp.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.sanyuelanv.sanwebapp.R;
import com.sanyuelanv.sanwebapp.SanYueWebApp;
import com.sanyuelanv.sanwebapp.base.AndroidtoJs;
import com.sanyuelanv.sanwebapp.base.BaseActivity;
import com.sanyuelanv.sanwebapp.base.BaseAlertLinearLayout;
import com.sanyuelanv.sanwebapp.base.SanYueBaseBottomDialog;
import com.sanyuelanv.sanwebapp.bean.SanYueActionSheetItem;
import com.sanyuelanv.sanwebapp.bean.SanYueAppMessage;
import com.sanyuelanv.sanwebapp.bean.SanYueAppConfigItem;
import com.sanyuelanv.sanwebapp.bean.SanYueModalItem;
import com.sanyuelanv.sanwebapp.bean.SanYuePickItem;
import com.sanyuelanv.sanwebapp.bean.SanYueToastItem;
import com.sanyuelanv.sanwebapp.fragment.SanYueActionSheet;
import com.sanyuelanv.sanwebapp.fragment.SanYueModal;
import com.sanyuelanv.sanwebapp.fragment.SanYuePicker;
import com.sanyuelanv.sanwebapp.utils.SanYueWebAppFileUtils;
import com.sanyuelanv.sanwebapp.utils.SanYueUIUtils;
import com.sanyuelanv.sanwebapp.view.SanYueAppLoadView;
import com.sanyuelanv.sanwebapp.view.SanYueCapsuleBtn;
import com.sanyuelanv.sanwebapp.view.SanYueErrorView;
import com.sanyuelanv.sanwebapp.view.SanYuePickerView;
import com.sanyuelanv.sanwebapp.view.SanYueToast;
import com.sanyuelanv.sanwebapp.view.SanYueNavView;
import com.sanyuelanv.sanwebapp.view.SanYueWebView;

import org.json.JSONObject;

import java.lang.reflect.Method;

/**
 * Create By songhang in 2020-02-26
 */
public class MainWebAppActivity extends BaseActivity implements AndroidtoJs.onMessageListener {
    private SanYueWebApp app = SanYueWebApp.getInstance();
    private Boolean isInit;
    private Boolean isLoad;

    private String url;
    private String htmlName = "index.html";
    private SanYueAppMessage sanYueAppMessage;
    private SanYueAppConfigItem appConfigItem;
    private JSONObject extraMsg;

    private SanYueNavView navView;
    private SanYueWebView mWebView;

    private SanYueToast loadDialog;
    private SanYueModal modal;
    private int currentNightMode;
    private SanYueActionSheet actionSheet;
    private SanYuePicker picker;

    // region Override
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        currentNightMode = newConfig.uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (modal != null){
            modal.changStyle(currentNightMode);
        }
        if (actionSheet != null){
            actionSheet.changStyle(currentNightMode);
        }
        if (picker != null){
            picker.changStyle(currentNightMode);
        }
//        switch (currentNightMode){
//            case Configuration.UI_MODE_NIGHT_NO:{
//                // 白天模式
//                break;
//            }
//            case Configuration.UI_MODE_NIGHT_YES:{
//                // 黑夜模式
//                break;
//            }
//        }
        super.onConfigurationChanged(newConfig);
    }
    @Override
    protected void initData() {
        currentNightMode = this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        app.pushRouter(this);
        setUpCapsuleBtn();
        Intent intent = getIntent();
        isInit = intent.getBooleanExtra("init",false);
        if (isInit){  setUpStartView();  }
        else {  setUpAppView(); }
    }
    @Override
    protected void onDestroy() {
        if (mWebView != null){
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            ViewGroup viewGroup = ((ViewGroup) mWebView.getParent());
            if (viewGroup != null){
                viewGroup.removeView(mWebView);
            }
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }
    @Override
    public void finish() {
        super.finish();
        int num = app.popRouter(this);
        if (num == 0){
            overridePendingTransition(R.anim.sanyue_anim_dismiss_in,R.anim.sanyue_anim_dismiss);
            app.destroy();
        }
        else {

        }
    }
    // endregion Override

    // region capsuleBtn
    private void setUpCapsuleBtn(){
        int statusBarHeight = SanYueUIUtils.getStatusBarHeight(this,true);
        SanYueCapsuleBtn capsuleBtn = mainView.createBtn(statusBarHeight);
        capsuleBtn.setMorBtnOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
            }
        });
        capsuleBtn.setCloseBtnOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // frameLayout 设置
//        frameLayout = mainView.getFrameLayout();
    }
    // endregion capsuleBtn

    // region StartView
    private void setUpStartView(){
        FrameLayout frameLayout = mainView.getFrameLayout();
        SanYueUIUtils.hideStatusBar(this.getWindow());
        frameLayout.removeAllViews();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        frameLayout.addView(setLoadView(),layoutParams);
        url = getIntent().getStringExtra("url");
        SanYueWebAppFileUtils.checkNeedDown(url, this, new SanYueWebAppFileUtils.onCheckNeedLoadListener() {
            @Override
            public void onFailed(Exception e) {
                setUpErrView(e.getLocalizedMessage());
            }
            @Override
            public void onSuccess(SanYueAppMessage app, int type) {
                sanYueAppMessage = app;
                Log.d("MainWebAppActivity",type + "");
                setUpAppView();
            }
        });
    }
    private void setUpErrView(String errText){
        FrameLayout frameLayout = mainView.getFrameLayout();
        frameLayout.removeAllViews();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        frameLayout.addView(setErrorView(errText),layoutParams);
    }
    // 自定义类的时候自己改 load or error 界面
    protected View setLoadView(){
        return new SanYueAppLoadView(this);
    }
    protected View setErrorView(String errText){
        SanYueErrorView errorView =  new SanYueErrorView(this);
        errorView.setErrText(errText);
        errorView.setReloadBtnOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpStartView();
            }
        });
        errorView.setCloseBtnOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        return errorView;
    }
    // endregion StartView

    // region AppView
    private void setUpAppView(){
        // 1. creat webview
        mWebView = new SanYueWebView(this);
        mWebView.loadUrl("file://" + sanYueAppMessage.getRootPath() + "/" + htmlName,app.isDebug());
        AndroidtoJs androidtoJs =  new AndroidtoJs(this,this);
        mWebView.addJavascriptInterface(androidtoJs,"AndroidNative");
    }
    // endregion AppView

    // region javaScriptMethod
    @Override
    public void onMessage(final String name,final JSONObject jsonObject) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        final MainWebAppActivity activity = this;
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                try{
                    Method method =  activity.getClass().getDeclaredMethod(name,JSONObject.class);
                    method.setAccessible(true);
                    method.invoke(activity,jsonObject);
                }
                catch (Exception e){
                    Log.d("SanYueException",e.getLocalizedMessage());
                }
            }
        });
    }
    private void SanYue_init(JSONObject jsonObject){
        String ID = null;
        // 执行 debug
        mWebView.openDebug();
        try{
            ID = jsonObject.getString("id");
            appConfigItem = new SanYueAppConfigItem(jsonObject.getJSONObject("data"));
        }
        catch (Exception e){ }
        FrameLayout frameLayout = mainView.getFrameLayout();
        if (ID != null && appConfigItem != null){
            frameLayout.removeAllViews();
            frameLayout.setVisibility(View.GONE);
            LinearLayout layoutView = new LinearLayout(this);
            layoutView.setOrientation(LinearLayout.VERTICAL);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            frameLayout.addView(layoutView,layoutParams);
            LinearLayout.LayoutParams webViewLayoutParams = null;
            int routerNumber = app.getRouterNumber();
            // 看配置是否需要设置 navBar并且设置内容
            if (!appConfigItem.isHideNav()){
                webViewLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1);
                // 创建 navBar
                boolean needBack = routerNumber > 1;
                navView = new SanYueNavView(this,needBack,appConfigItem);
                layoutView.addView(navView, navView.getLinearLayoutParams());
            }
            else {
                webViewLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,app.getAppInfoItem().getFullHeight());
            }
            layoutView.addView(mWebView,webViewLayoutParams);
            // bg Color
            mWebView.setBackgroundColor(Color.parseColor(appConfigItem.getBackgroundColor()));
            // statusBar
            SanYueUIUtils.changeStausBarStyle(this.getWindow(),appConfigItem.getStatusStyle());
            // 过渡动画
            isLoad = true;
            Fade fade = new Fade();
            fade.setDuration(250);
            TransitionManager.beginDelayedTransition(frameLayout,fade);
            frameLayout.setVisibility(View.VISIBLE);
            JSONObject res = new JSONObject();
            try {
                res.put("navIndex",routerNumber);
                res.put("extra",extraMsg);
                res.put("style",currentNightMode == Configuration.UI_MODE_NIGHT_YES ? "dark" : "light");
                mWebView.evaluateJsByID(ID,res,null,false);
            }
            catch (Exception e){

            }

        }
    }
    private void SanYue_getSystemInfo(JSONObject jsonObject){
        String ID = null;
        try{
            ID = jsonObject.getString("id");
        }
        catch (Exception e){ }
        if (ID !=null){
            try {
                JSONObject res = app.getAppInfoItem().getJavaScriptResult(appConfigItem.isHideNav());
                mWebView.evaluateJsByID(ID,res,null,false);
            }
            catch (Exception e){

            }
        }
    }
    private void SanYue_getNetworkType(JSONObject jsonObject){}
    private void SanYue_showToast(JSONObject jsonObject){
        SanYueToastItem item = null;
        try {  item = new SanYueToastItem(jsonObject.getJSONObject("data"));  }
        catch (Exception e){ }
        if (item != null){
            if (loadDialog == null){
                loadDialog = new SanYueToast(this);
            }
            loadDialog.start(mainView.getFrameLayout(),item.getContent(),item.getMask(),item.getTime());
        }
    }
    private void SanYue_hiddenLoad(JSONObject jsonObject){
        if(loadDialog != null){  loadDialog.stop(0); }
    }
    private void SanYue_setStatusStyle(JSONObject jsonObject){
        String style = null;
        try {
            JSONObject item = jsonObject.getJSONObject("data");
            style = item.getString("style");
        }
        catch (Exception e){

        }
        if (style != null){
            SanYueUIUtils.changeStausBarStyle(this.getWindow(),style);
            appConfigItem.setStatusStyle(style);
        }
    }
    private void SanYue_setNavBar(JSONObject jsonObject){
        if (navView == null) { return; }
        try {
            JSONObject item = jsonObject.getJSONObject("data");
            if (item.has("statusStyle")){  appConfigItem.setStatusStyle(item.getString("statusStyle"));  }
            if (item.has("navBackgroundColor")){  appConfigItem.setNavBackgroundColor(item.getString("navBackgroundColor"));  }
            if (item.has("title")){  appConfigItem.setTitle(item.getString("title"));  }
            if (item.has("titleColor")){  appConfigItem.setTitleColor(item.getString("titleColor"));  }
        }
        catch (Exception e){

        }
        SanYueUIUtils.changeStausBarStyle(this.getWindow(),appConfigItem.getStatusStyle());
        navView.changeNavBar(appConfigItem);
    }
    private void SanYue_showModal(JSONObject jsonObject){
        SanYueModalItem item = null;
        String ID = null;
        if (modal != null){
            modal.dismiss();
            modal = null;
        }
        try{
            item = new  SanYueModalItem(jsonObject.getJSONObject("data"));
            ID = jsonObject.getString("id");
        }
        catch (Exception e){

        }
        if (item == null || ID == null) return;
        modal = new SanYueModal(item,currentNightMode);
        final String finalID = ID;
        modal.setListener(new BaseAlertLinearLayout.OnControlBtnListener() {
            @Override
            public void onClick(int type) {
                try {
                    JSONObject res = new JSONObject();
                    res.put("result",type);
                    mWebView.evaluateJsByID(finalID,res,null,false);
                }
                catch (Exception e){

                }
                modal.dismiss();
                modal = null;
            }
        });
        modal.show(getSupportFragmentManager(),"sanYue_modal");
    }
    private void SanYue_showActionSheet(JSONObject jsonObject){
        SanYueActionSheetItem item = null;
        String ID = null;
        if (actionSheet != null){
            actionSheet.dismiss();
            actionSheet = null;
        }
        try{
            item = new  SanYueActionSheetItem(jsonObject.getJSONObject("data"));
            ID = jsonObject.getString("id");
        }
        catch (Exception e){
        }
        if (item == null || ID == null) return;
        actionSheet = new SanYueActionSheet(item,currentNightMode,app.getAppInfoItem().getActionSheetHeight());
        final String finalID = ID;
        actionSheet.setListener(new BaseAlertLinearLayout.OnControlBtnListener() {
            @Override
            public void onClick(int type) {
                try {
                    JSONObject res = new JSONObject();
                    res.put("result",type);
                    mWebView.evaluateJsByID(finalID,res,null,false);
                }
                catch (Exception e){
                }
                actionSheet.dismiss();
                actionSheet = null;
            }
        });
        actionSheet.show(getSupportFragmentManager(),"sanYue_actionSheet");
    }
    private void SanYue_showPick(JSONObject jsonObject){
        String ID = null;
        SanYuePickItem item = null;
        if (picker != null){
            picker.dismiss();
            picker = null;
        }
        try{
            ID = jsonObject.getString("id");
            item = new SanYuePickItem(jsonObject.getJSONObject("data"));
        }
        catch (Exception e){
        }
        if (item == null || ID == null || item.getMode() < 0) return;
        picker = new SanYuePicker(item,currentNightMode,app.getAppInfoItem().getActionSheetHeight());
        final String finalID = ID;
        picker.setListener(new SanYuePickerView.OnSelectListener() {
            @Override
            public void onSelect(int pos, int type) {
                try {
                    JSONObject res = new JSONObject();
                    if (type <= 0){
                        if (type < 0){  res.put("result",type);  }
                        else {  res.put("result",pos);  }
                        mWebView.evaluateJsByID(finalID,res,null,false);
                        picker.dismiss();
                        picker = null;
                    }
                    else {
                        res.put("result",pos);
                        mWebView.evaluateJsByID(finalID,res,null,true);
                    }
                }
                catch (Exception e){
                }
            }

            @Override
            public void onSelectMulti(int[] pos, int type) {

            }

            @Override
            public void onSelectDate(String select, int type) {
                try {
                    JSONObject res = new JSONObject();
                    if (type <= 0){
                        if (type < 0){  res.put("result",type);  }
                        else {  res.put("result",select);  }
                        mWebView.evaluateJsByID(finalID,res,null,false);
                        picker.dismiss();
                        picker = null;
                    }
                    else {
                        res.put("result",select);
                        mWebView.evaluateJsByID(finalID,res,null,true);
                    }
                }
                catch (Exception e){
                }
            }
        });
        picker.show(getSupportFragmentManager(),"sanYue_picker");
    }
    // endregion javaScriptMethod
}
