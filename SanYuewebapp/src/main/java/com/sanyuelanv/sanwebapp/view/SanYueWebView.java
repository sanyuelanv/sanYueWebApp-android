package com.sanyuelanv.sanwebapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import com.sanyuelanv.sanwebapp.SanYueWebApp;
import com.sanyuelanv.sanwebapp.utils.SanYueWebAppFileUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Create By songhang in 2020/3/4
 */
public class SanYueWebView extends WebView {
    Context mContext;
    private boolean isWebViewLoad;
    private String debugJs;

    public SanYueWebView(Context context) {
        this(context, null,0,0);
    }

    public SanYueWebView(Context context, AttributeSet attrs) {
        this(context, attrs,0,0);
    }

    public SanYueWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs,defStyleAttr,0);
    }

    public SanYueWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        setConfig();
    }
    public  void  setConfig(){
        WebSettings webSettings = getSettings();
        // 内容布局
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL); // 布局算法
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        // 文件缓存
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 原生的太垃圾了，自己写一个缓存
        webSettings.setAppCacheEnabled(false); // Application Cache缓存机制： manifest文件去确定是否更新，不推荐使用了，标准也不会再支持。
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(false); //这里的 Database 是 webSQL 需要 原生SQL语句，而并非  IndexDB， 而 IndexDB 开启 JS 即可用
        webSettings.setAllowFileAccess(true); // 安卓5-10 测试过图片，只能是 HTML 根目录内部使用，出了根目录就无法访问
        // js 设置： 可用 & 不能打开新窗口
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
        // 字体
        webSettings.setDefaultFontSize(16);
        webSettings.setTextZoom(100); // WebView里的字体就不会随系统字体大小设置发生变化
        webSettings.setMinimumFontSize(1);
        webSettings.setMinimumLogicalFontSize(1);
        // UA
        webSettings.setUserAgentString("webApp/android/0.1");
        setFadingEdgeLength(0);
        setOverScrollMode(OVER_SCROLL_NEVER);

        // 拦截请求
        setWebViewClient(new WebViewClient(){
            /* 拦截策略
                # 路由
               1. A 标签跳转 / window.location.href 跳转 都不允许:shouldOverrideUrlLoading return true
               2. url#XXX 这种方案不经 shouldOverrideUrlLoading ，因此能执行
               # 资源
               1. file 开头的都不拦截
               2. HTTP 开头的转交 OK-HTTP，请求 header 看返回的 content-type
                    2.1 返回 text/html 拦截
                    2.2 其他可以通过
             */
            // 资源： 因为默认 页面内 ajax 请求不走这里,所有这里处理的只有资源请求。资源缓存策略 ：对比缓存
            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                String url = String.valueOf(request.getUrl());
                if (url.startsWith("file")){
                    Log.d("should-0", String.valueOf(request.getUrl()));
                    return super.shouldInterceptRequest(view, request);
                }
                // 非 file 协议头 资源进入 OK/HTTP
                final Call call = SanYueWebApp.getInstance().getResourcesClient().newCall(new Request.Builder()
                        .url(url)
                        .build()
                );
                try {
                    final Response response = call.execute();
                    List<String> contentTypes = response.headers("Content-Type");
                    Boolean flag = true;
                    for (String value : contentTypes){  if (value.equals("text/html")){  flag = false; }  }
                    if (flag){
                        return new WebResourceResponse(
                                response.header("content-type", "text/plain"),
                                response.header("content-encoding", "utf-8"),
                                Objects.requireNonNull(response.body()).byteStream()
                        );
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] res = {};
                return new WebResourceResponse("text/plain", "utf-8", new ByteArrayInputStream(res));
            }

            // 路由：这里是 跳转的时候用到的拦截器（A 标签跳转 / window.location.href 跳转），内部资源加载不经过这里
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return  true;
            }
        });
    }

    public void loadUrl(String url,Boolean debug) {
        super.loadUrl(url);
        if (debug){  debugJs = SanYueWebAppFileUtils.getAssetsData(mContext,"console.js");  }
    }
    public  void openDebug(){
        if (debugJs != null){
            evaluateJavascript(debugJs, null);
            debugJs = null;
        }
    }
    public void evaluateJsByName(String name, JSONObject success,JSONObject error){
        String successStr = success == null ? "null" : success.toString();
        String errorStr = error == null ? "null" : error.toString();
        String str = "SanYueWebApp.pub('" + name + "',"+ successStr +","+ errorStr +")";
        evaluateJavascript(str,null);
    }
    public void evaluateJsByID(String ID, JSONObject success,JSONObject error,boolean removeLister){
        String flag = removeLister ? "true" : "false";
        String successStr = success == null ? "null" : success.toString();
        String errorStr = error == null ? "null" : error.toString();
        String str = "SanYueWebApp.CALLBACK["+ ID +"]("+ successStr +","+ errorStr +","+ flag +")";
        evaluateJavascript(str,null);
    }

}
