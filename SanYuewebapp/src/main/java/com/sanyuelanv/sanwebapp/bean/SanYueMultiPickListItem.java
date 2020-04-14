package com.sanyuelanv.sanwebapp.bean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Create By songhang in 2020/4/13
 */
public class SanYueMultiPickListItem {
    private String name;
    private ArrayList<SanYueMultiPickListItem> list;

    public SanYueMultiPickListItem(JSONObject jsonObject) {
        try {  name = jsonObject.getString("name");  }
        catch (Exception e){  name = "";  }

        list = new ArrayList<>();
        try {
            JSONArray arr =  jsonObject.getJSONArray("children");
            int len = arr.length();
            for (int i = 0; i <len ; i++) {
                list.add(new SanYueMultiPickListItem(arr.getJSONObject(i)));
            }
        }
        catch (Exception e){

        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<SanYueMultiPickListItem> getList() {
        return list;
    }

    public void setList(ArrayList<SanYueMultiPickListItem> list) {
        this.list = list;
    }
}
