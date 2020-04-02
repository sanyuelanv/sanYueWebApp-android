package com.sanyuelanv.sanwebapp.bean;

import org.json.JSONObject;

/**
 * Create By songhang in 2020/3/7
 */
public class SanYueAppConfigItem {
    private String navBackgroundColor;
    private String title;
    private String titleColor;
    private boolean hideNav;

    private String backgroundColor;
    private String statusStyle;
    private boolean bounces;
    private int networkTimeout;

    public SanYueAppConfigItem(JSONObject jsonObject) {
        try {  backgroundColor = jsonObject.getString("backgroundColor");  }
        catch (Exception e){  backgroundColor = "#f1f1f1";  }

        try {  navBackgroundColor = jsonObject.getString("navBackgroundColor");  }
        catch (Exception e){  navBackgroundColor = "#f1f1f1";  }

        try {  statusStyle = jsonObject.getString("statusStyle");  }
        catch (Exception e){  statusStyle = "dark";  }

        try {  title = jsonObject.getString("title");  }
        catch (Exception e){  title = "";  }

        try {  titleColor = jsonObject.getString("titleColor");  }
        catch (Exception e){  titleColor = "#000000";  }

        try {  hideNav = jsonObject.getInt("hideNav") == 1;  }
        catch (Exception e){  hideNav = false;  }

        try {  bounces = jsonObject.getInt("bounces") == 1;  }
        catch (Exception e){  bounces = false;  }

        try {  networkTimeout = jsonObject.getInt("networkTimeout");  }
        catch (Exception e){  networkTimeout = 20;  }
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getNavBackgroundColor() {
        return navBackgroundColor;
    }

    public void setNavBackgroundColor(String navBackgroundColor) {
        this.navBackgroundColor = navBackgroundColor;
    }

    public String getStatusStyle() {
        return statusStyle;
    }

    public void setStatusStyle(String statusStyle) {
        this.statusStyle = statusStyle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(String titleColor) {
        this.titleColor = titleColor;
    }

    public boolean isHideNav() {
        return hideNav;
    }

    public void setHideNav(boolean hideNav) {
        this.hideNav = hideNav;
    }

    public boolean isBounces() {
        return bounces;
    }

    public void setBounces(boolean bounces) {
        this.bounces = bounces;
    }

    public int getNetworkTimeout() {
        return networkTimeout;
    }

    public void setNetworkTimeout(int networkTimeout) {
        this.networkTimeout = networkTimeout;
    }
}
