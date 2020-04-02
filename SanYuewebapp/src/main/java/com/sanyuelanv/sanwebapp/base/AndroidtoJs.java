package com.sanyuelanv.sanwebapp.base;

/**
 * Create By songhang in 2020/3/4
 */
import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.sanyuelanv.sanwebapp.SanYueWebApp;

import org.json.JSONException;
import org.json.JSONObject;

public class AndroidtoJs {
    Context mContext;
    private onMessageListener messageListener;
    public interface onMessageListener{
        public void onMessage(String name,JSONObject jsonObject);
    }
    public AndroidtoJs(Context mContext,onMessageListener messageListener) {
        this.mContext = mContext;
        this.messageListener = messageListener;
    }
    public boolean checkNameInMethods(String name){
        SanYueWebApp app = SanYueWebApp.getInstance();
        String[] defaultJsMethods  = app.getDefaultJsMethods();
        String[] customMethods  = app.getCustomMethods();
        boolean flag = false;
        for (int i = 0; i < defaultJsMethods.length; i++) {
            String defaultJsMethod = defaultJsMethods[i];
            if (defaultJsMethod.equals(name)){
                flag = true;
                break;
            }
        }
        if (customMethods != null){
            for (int i = 0; i < customMethods.length; i++) {
                String defaultJsMethod = customMethods[i];
                if (defaultJsMethod.equals(name)){
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }
    @JavascriptInterface
    public void postMessage(String str){
        try{
            JSONObject jsonObject = new JSONObject(str);
            String name = jsonObject.getString("name");
            Log.d("SanYueMethod",name);
            JSONObject para = jsonObject.getJSONObject("data");
            if (checkNameInMethods(name)){
                messageListener.onMessage(name,para);
            }
        }
        catch (JSONException e){

        }
    }
}
