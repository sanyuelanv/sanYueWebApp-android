package com.sanyuelanv.sanwebapp.bean;

import org.json.JSONObject;

/**
 * Create By songhang in 2020/3/13
 */
public class SanYueModalItem {
    private boolean showCancel;
    private boolean backGroundCancel;

    private String title;
    private String content;
    private String confirmText;
    private String cancelText;

    private String cancelColor;
    private String confirmColor;
    private String cancelColorDark;
    private String confirmColorDark;
    private int senseMode;

    public SanYueModalItem(JSONObject jsonObject) {
        try {  showCancel = jsonObject.getBoolean("showCancel");  }
        catch (Exception e){  showCancel = false;  }

        try {  backGroundCancel = jsonObject.getBoolean("backGroundCancel");  }
        catch (Exception e){  backGroundCancel = false;  }

        try {  title = jsonObject.getString("title");  }
        catch (Exception e){  title = "";  }

        try {  content = jsonObject.getString("content");  }
        catch (Exception e){  content = "";  }

        try {  confirmText = jsonObject.getString("confirmText");  }
        catch (Exception e){  confirmText = "确定";  }

        try {  cancelText = jsonObject.getString("cancelText");  }
        catch (Exception e){  cancelText = "取消";  }

        try {  cancelColor = jsonObject.getString("cancelColor");  }
        catch (Exception e){  cancelColor = "#e64340";  }

        try {  confirmColor = jsonObject.getString("confirmColor");  }
        catch (Exception e){  confirmColor = "#353535";  }

        try {  cancelColorDark = jsonObject.getString("cancelColorDark");  }
        catch (Exception e){  cancelColorDark = "#CD5C5C";  }

        try {  confirmColorDark = jsonObject.getString("confirmColorDark");  }
        catch (Exception e){  confirmColorDark = "#BBBBBB";  }

        String mode = "auto";
        try {  mode = jsonObject.getString("senseMode");  }
        catch (Exception e){  mode = "light";  }
        if (mode.equals("auto")){  senseMode = 0; }
        else if (mode.equals("light")){  senseMode = 1;  }
        else if (mode.equals("dark")){  senseMode = 2;  }

    }

    public boolean isShowCancel() {
        return showCancel;
    }

    public void setShowCancel(boolean showCancel) {
        this.showCancel = showCancel;
    }

    public boolean isBackGroundCancel() {
        return backGroundCancel;
    }

    public void setBackGroundCancel(boolean backGroundCancel) {
        this.backGroundCancel = backGroundCancel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getConfirmText() {
        return confirmText;
    }

    public void setConfirmText(String confirmText) {
        this.confirmText = confirmText;
    }

    public String getCancelText() {
        return cancelText;
    }

    public void setCancelText(String cancelText) {
        this.cancelText = cancelText;
    }

    public String getCancelColor() {
        return cancelColor;
    }

    public void setCancelColor(String cancelColor) {
        this.cancelColor = cancelColor;
    }

    public String getConfirmColor() {
        return confirmColor;
    }

    public void setConfirmColor(String confirmColor) {
        this.confirmColor = confirmColor;
    }

    public String getCancelColorDark() {
        return cancelColorDark;
    }

    public void setCancelColorDark(String cancelColorDark) {
        this.cancelColorDark = cancelColorDark;
    }

    public String getConfirmColorDark() {
        return confirmColorDark;
    }

    public void setConfirmColorDark(String confirmColorDark) {
        this.confirmColorDark = confirmColorDark;
    }

    public int getSenseMode() {
        return senseMode;
    }

    public void setSenseMode(int senseMode) {
        this.senseMode = senseMode;
    }
}
