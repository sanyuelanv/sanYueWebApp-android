package com.sanyuelanv.sanwebapp.bean;

import org.json.JSONObject;

/**
 * Create By songhang in 2020/3/11
 */
public class SanYueToastItem {
    private String content;
    private Boolean mask;
    private long time;

    public SanYueToastItem(JSONObject jsonObject) {
        try {  content = jsonObject.getString("content");  }
        catch (Exception e){  content = "";  }

        try {  mask = jsonObject.getBoolean("mask");  }
        catch (Exception e){  mask = false;  }

        try {  time = (long)(jsonObject.getDouble("time") * 1000);  }
        catch (Exception e){  time = 1500;  }
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getMask() {
        return mask;
    }

    public void setMask(Boolean mask) {
        this.mask = mask;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
