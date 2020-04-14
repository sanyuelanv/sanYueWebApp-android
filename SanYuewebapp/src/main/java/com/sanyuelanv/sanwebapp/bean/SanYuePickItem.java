package com.sanyuelanv.sanwebapp.bean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    private boolean backGroundCancel;

    private ArrayList<SanYueMultiPickListItem> multiList;


    public SanYuePickItem(JSONObject jsonObject) {
        try {  listenChange = jsonObject.getBoolean("backGroundCancel");  }
        catch (Exception e){  listenChange = false;  }

        try {  backGroundCancel = jsonObject.getBoolean("backGroundCancel");  }
        catch (Exception e){  backGroundCancel = false;  }

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
            try {  normalValue = jsonObject.getInt("value");  }
            catch (Exception e){  normalValue = 0;  }
            try {
                JSONArray arr = jsonObject.getJSONArray("list");
                int length = arr.length();
                for (int i = 0; i <length ; i++) {  list.add(arr.get(i).toString()); }
            }
            catch (Exception e){}
        }
        else if (mode.equals("multi")){
            this.mode = 1;
            multiList = new ArrayList<>();
            multiValue = new ArrayList<>();
            try {
                JSONArray arr =  jsonObject.getJSONArray("list");
                int len = arr.length();
                for (int i = 0; i <len ; i++) {
                    multiList.add(new SanYueMultiPickListItem(arr.getJSONObject(i)));
                }
            }
            catch (Exception e){

            }
            try {
                JSONArray arr =  jsonObject.getJSONArray("value");
                int len = arr.length();
                for (int i = 0; i <len ; i++) {  multiValue.add(arr.getInt(i)); }
            }
            catch (Exception e){

            }
        }
        else if (mode.equals("time")){
            this.mode = 2;
            SimpleDateFormat formatter = new SimpleDateFormat( "HH:mm");
            try {
                String timeStart = jsonObject.getString("start");
                this.timeStart = formatter.parse(timeStart);
            }
            catch (Exception e){
                this.timeEnd = getTimeByCalendar(0,0,0,1);
            }
            try {
                String timeEnd = jsonObject.getString("end");
                this.timeEnd = formatter.parse(timeEnd);
            }
            catch (Exception e){
                this.timeEnd = getTimeByCalendar(23,59,59,1);
            }
            try {
                String timeValue = jsonObject.getString("value");
                this.timeValue = formatter.parse(timeValue);
            }
            catch (Exception e){
                this.timeValue = this.timeStart;
            }
            if(timeValue.getTime() < timeStart.getTime()){  timeValue = timeStart; }
            if(timeValue.getTime() > timeEnd.getTime()){  timeValue = timeEnd; }
        }
        else if (mode.equals("date")){
            this.mode = 3;
            SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd");
            try {
                String timeStart = jsonObject.getString("start");
                this.timeStart = formatter.parse(timeStart);
            }
            catch (Exception e){
                this.timeStart = getTimeByCalendar(1900,0,1,0);
            }
            try {
                String timeEnd = jsonObject.getString("end");
                this.timeEnd = formatter.parse(timeEnd);
            }
            catch (Exception e){
                this.timeEnd = getTimeByCalendar(2100,11,31,0);
            }
            try {
                String timeValue = jsonObject.getString("value");
                this.timeValue = formatter.parse(timeValue);
            }
            catch (Exception e){
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                this.timeValue = calendar.getTime();
            }

            if(timeValue.getTime() < timeStart.getTime()){  timeValue = timeStart; }
            if(timeValue.getTime() > timeEnd.getTime()){  timeValue = timeEnd; }
        }
        else { this.mode = -1; }
    }
    private Date getTimeByCalendar(int i,int j,int k,int type){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if (type == 1){
            calendar.set(Calendar.HOUR_OF_DAY, i);
            calendar.set(Calendar.MINUTE, j);
            calendar.set(Calendar.SECOND, k);
        }
        else {
            calendar.set(Calendar.YEAR, i);
            calendar.set(Calendar.MONTH, j);
            calendar.set(Calendar.DAY_OF_MONTH, k);
        }

        return  calendar.getTime();
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

    public boolean isBackGroundCancel() {
        return backGroundCancel;
    }

    public void setBackGroundCancel(boolean backGroundCancel) {
        this.backGroundCancel = backGroundCancel;
    }

    public ArrayList<SanYueMultiPickListItem> getMultiList() {
        return multiList;
    }

    public void setMultiList(ArrayList<SanYueMultiPickListItem> multiList) {
        this.multiList = multiList;
    }
}
