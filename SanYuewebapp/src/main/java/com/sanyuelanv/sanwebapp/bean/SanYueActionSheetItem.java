package com.sanyuelanv.sanwebapp.bean;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Create By songhang in 2020/3/16
 */
public class SanYueActionSheetItem {
    private String cancelText;
    private String itemColor;
    private String cancelColor;
    private String cancelColorDark;
    private String itemColorDark;
    private String title;
    private String[] itemList;

    private int senseMode;
    private boolean backGroundCancel;

    public boolean isBackGroundCancel() {
        return backGroundCancel;
    }

    public void setBackGroundCancel(boolean backGroundCancel) {
        this.backGroundCancel = backGroundCancel;
    }

    public SanYueActionSheetItem(JSONObject jsonObject) {
        try {  backGroundCancel = jsonObject.getBoolean("backGroundCancel");  }
        catch (Exception e){  backGroundCancel = false;  }
        try {  itemColor = jsonObject.getString("itemColor");  }
        catch (Exception e){  itemColor = "#353535";  }
        try {  title = jsonObject.getString("title");  }
        catch (Exception e){  title = "";  }
        try {  cancelColor = jsonObject.getString("cancelColor");  }
        catch (Exception e){  cancelColor = "#e64340";  }
        try {  cancelColorDark = jsonObject.getString("cancelColorDark");  }
        catch (Exception e){  cancelColorDark = "#CD5C5C";  }
        try {  itemColorDark = jsonObject.getString("itemColorDark");  }
        catch (Exception e){  itemColorDark = "#BBBBBB";  }

        try {  cancelText = jsonObject.getString("cancelText");  }
        catch (Exception e){  cancelText = "取消";  }

        try {
            JSONArray arr = jsonObject.getJSONArray("itemList");
            int length = arr.length();
            itemList = new String[length > 6 ? 6 : length];
            for (int i = 0; i <length ; i++) {
                if (i <= 5){
                    itemList[i] = arr.get(i).toString();
                }

            }
        }
        catch (Exception e){
            itemList = new String[0];
        }
        String mode = "auto";
        try {  mode = jsonObject.getString("senseMode");  }
        catch (Exception e){  mode = "light";  }
        if (mode.equals("auto")){  senseMode = 0; }
        else if (mode.equals("light")){  senseMode = 1;  }
        else if (mode.equals("dark")){  senseMode = 2;  }
    }

    public String getCancelText() {
        return cancelText;
    }

    public void setCancelText(String cancelText) {
        this.cancelText = cancelText;
    }

    public String getItemColor() {
        return itemColor;
    }

    public void setItemColor(String itemColor) {
        this.itemColor = itemColor;
    }

    public String getCancelColor() {
        return cancelColor;
    }

    public void setCancelColor(String cancelColor) {
        this.cancelColor = cancelColor;
    }

    public String getCancelColorDark() {
        return cancelColorDark;
    }

    public void setCancelColorDark(String cancelColorDark) {
        this.cancelColorDark = cancelColorDark;
    }

    public String getItemColorDark() {
        return itemColorDark;
    }

    public void setItemColorDark(String itemColorDark) {
        this.itemColorDark = itemColorDark;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getItemList() {
        return itemList;
    }

    public void setItemList(String[] itemList) {
        this.itemList = itemList;
    }

    public int getSenseMode() {
        return senseMode;
    }

    public void setSenseMode(int senseMode) {
        this.senseMode = senseMode;
    }
}
