package com.sanyuelanv.sanwebapp.bean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Create By songhang in 2020/3/21
 */
public class SanYuePickItem {
    private int mode;
    private boolean listenChange;
    private int senseMode;

    private ArrayList<String> list = new ArrayList<>();
    private int normalValue;
    private ArrayList<Integer> multiValue;

    private Date timeStart;
    private Date timeEnd;
    private Date timeValue;
    private String originTimeValue;

    //private ArrayList<T> multiList;


    public SanYuePickItem(JSONObject jsonObject) {
        try {  listenChange = jsonObject.getBoolean("backGroundCancel");  }
        catch (Exception e){  listenChange = false;  }

        String senseMode = "auto";
        try {  senseMode = jsonObject.getString("senseMode");  }
        catch (Exception e){  senseMode = "light";  }
        if (senseMode.equals("auto")){  this.senseMode = 0; }
        else if (senseMode.equals("light")){  this.senseMode = 1;  }
        else if (senseMode.equals("dark")){  this.senseMode = 2;  }

        String mode = "none";
        try {  mode = jsonObject.getString("mode");  }
        catch (Exception e){  mode = "none";  }
        if (mode.equals("normal")){
            this.mode = 0;
            try {  normalValue = jsonObject.getInt("normalValue");  }
            catch (Exception e){  normalValue = 0;  }
            try {
                JSONArray arr = jsonObject.getJSONArray("list");
                int length = arr.length();
                for (int i = 0; i <length ; i++) {  list.add(arr.get(i).toString()); }
            }
            catch (Exception e){}
        }
        else if (mode.equals("multi")){  this.mode = 1;  }
        else if (mode.equals("time")){  this.mode = 2;  }
        else if (mode.equals("date")){  this.mode = 3;  }
        else { this.mode = -1; }
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public boolean isListenChange() {
        return listenChange;
    }

    public void setListenChange(boolean listenChange) {
        this.listenChange = listenChange;
    }

    public int getSenseMode() {
        return senseMode;
    }

    public void setSenseMode(int senseMode) {
        this.senseMode = senseMode;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    public int getNormalValue() {
        return normalValue;
    }

    public void setNormalValue(int normalValue) {
        this.normalValue = normalValue;
    }

    public ArrayList<Integer> getMultiValue() {
        return multiValue;
    }

    public void setMultiValue(ArrayList<Integer> multiValue) {
        this.multiValue = multiValue;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Date getTimeValue() {
        return timeValue;
    }

    public void setTimeValue(Date timeValue) {
        this.timeValue = timeValue;
    }

    public String getOriginTimeValue() {
        return originTimeValue;
    }

    public void setOriginTimeValue(String originTimeValue) {
        this.originTimeValue = originTimeValue;
    }
}
