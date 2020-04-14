package com.sanyuelanv.sanwebapp.view;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.listener.OnItemSelectedListener;
import com.sanyuelanv.sanwebapp.bean.SanYueMultiPickListItem;
import com.sanyuelanv.sanwebapp.fragment.SanYuePicker;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Create By songhang in 2020/4/7
 */
public class SanYueMultiPicker extends LinearLayout {
    Context mContext;
    ArrayList<ArrayList<String>> list;
    ArrayList<SanYueMultiPickListItem> multList;
//    ArrayList<Integer> originValue;
    ArrayList<Integer> value;
    ArrayList<SanYueMyPicker> pickers;
    private int type;
    protected OnSelectListener selectListener;

    private Date start;
    private Date end;
    private Calendar calendar;
    private Calendar startCalendar;
    private Calendar endCalendar;

    public interface OnSelectListener {
        void onSelect(ArrayList<Integer> value);
    }

    public SanYueMultiPicker(Context context, ArrayList<SanYueMultiPickListItem> list, ArrayList<Integer> originValue) {
        super(context);
        this.mContext = context;
        this.multList = list;
        this.value = originValue;
        type = 1;
        //initData();
    }
    public SanYueMultiPicker(Context context, ArrayList<String> singleList, Integer singleValue) {
        super(context);
        this.mContext = context;
        this.list = new ArrayList<ArrayList<String>>();
        this.value = new ArrayList<Integer>();
        this.list.add(singleList);
        this.value.add(singleValue);
        type = 0;
        initData();
    }
    public SanYueMultiPicker(Context context, Date start,Date end,Date value,int type){
        super(context);
        this.mContext = context;
        this.list = new ArrayList<ArrayList<String>>();
        if (type == 3){
            this.value =  initTimeType(start,end,value);
        }
        else if(type == 4){
            this.value = initDateType(start,end,value);
        }
        this.type = type;
        initData();
    }

    private void initData(){
        setOrientation(HORIZONTAL);
        int pos = 0;
        pickers = new ArrayList<SanYueMyPicker>();
        for (ArrayList<String> itemList:list){
            Boolean isCyclic = type == 3 || (type == 4 && pos > 0);
            final SanYueMyPicker picker = new SanYueMyPicker(mContext,itemList,isCyclic);
            picker.setCurrentItem(value.get(pos));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1);
            addView(picker,params);
            final Integer listPos = pos;
            picker.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int index) {
                    if (selectListener != null){
                        value.set(listPos, index);
                        if (type == 3){  checkTimeSelect();  }
                        else if (type == 4){ checkDateSelect(listPos); }
                        else {
                            selectListener.onSelect(value);
                        }
                    }
                }
            });
            pickers.add(picker);
            pos += 1;
        }
    }
    public void setSelectListener(OnSelectListener selectListener) {
        this.selectListener = selectListener;
    }
    public void changeMode(int mode){
        for (int i = 0;i<pickers.size();i++){
            pickers.get(i).changeMode(mode);
        }
    }

    // time type event
    private ArrayList<Integer> initTimeType(Date start,Date end,Date value){
        for (int i = 0 ;i<2;i++){
            ArrayList<String> arr = new ArrayList<String>();
            int max= i== 0 ? 24:60;
            for (int j=0;j <  max ;j++){
                String str = j < 10 ? "0"+j : j+ "" ;
                arr.add(str);
            }
            this.list.add(arr);
        }
        this.start = start;
        this.end = end;
        calendar = Calendar.getInstance();
        calendar.setTime(value);
        ArrayList<Integer> res = new ArrayList<>();
        res.add(calendar.get(Calendar.HOUR_OF_DAY));
        res.add(calendar.get(Calendar.MINUTE));
        return res;
    }
    private void checkTimeSelect(){
        int hour = value.get(0);
        int min = value.get(1);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        Date time = calendar.getTime();
        Date selectTime = null;
        if (time.getTime() < start.getTime()){  selectTime = start;  }
        else if (time.getTime() > end.getTime()){  selectTime = end;  }
        if (selectTime != null){
            Calendar selectCalendar = Calendar.getInstance();
            selectCalendar.setTime(selectTime);
            value.set(0,selectCalendar.get(Calendar.HOUR_OF_DAY));
            value.set(1,selectCalendar.get(Calendar.MINUTE));
            pickers.get(0).setCurrentItem(value.get(0));
            pickers.get(1).setCurrentItem(value.get(1));
        }
        selectListener.onSelect(value);
    }

    // date type event
    private ArrayList<Integer> initDateType(Date start,Date end,Date value){
        ArrayList<Integer> res = new ArrayList<>();
        calendar = Calendar.getInstance();
        calendar.setTime(value);
        res.add(calendar.get(Calendar.YEAR));
        res.add(calendar.get(Calendar.MONTH));
        res.add(calendar.get(Calendar.DAY_OF_MONTH));

        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        endCalendar.setTime(end);

        for (int i = 0 ;i<3;i++){
            ArrayList<String> arr = new ArrayList<String>();
            if (i == 0){
                for (int j = startCalendar.get(Calendar.YEAR);j<= endCalendar.get(Calendar.YEAR);j++){ arr.add(j+""); }
            }
            else if (i == 1){
                for (int j = 1;j<13;j++){ arr.add(j+""); }
            }
            else {
                arr = getDates(res.get(0),res.get(1));
            }
            this.list.add(arr);
        }

        this.start = start;
        this.end = end;

        res.set(0,res.get(0) - startCalendar.get(Calendar.YEAR));
        res.set(2,res.get(2) - 1);
        return res;
    }
    private void checkDateSelect(int listPos){
        // 选择年份/月份 会改变 日期的内容
        if (listPos == 0 || listPos == 1){
            int moth = value.get(1);
            int year = value.get(0) ;
            ArrayList<String> dates = getDates(year,moth);
            pickers.get(2).setAdapter(new ArrayWheelAdapter(dates));
            // 日期最大改动
            if (value.get(2) > dates.size() - 1){
                value.set(2,dates.size() - 1);
                pickers.get(2).setCurrentItem(value.get(2));
            }
            pickers.get(2).invalidate();
        }
        // 判断选择的日期是否在[max,min] 内
        int year = value.get(0);
        int month = value.get(1);
        int date = value.get(2);
        calendar.set(Calendar.YEAR, year + startCalendar.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, date + 1);
        Date time = calendar.getTime();
        Date selectTime = null;
        if (time.getTime() < start.getTime()){  selectTime = start;  }
        else if (time.getTime() > end.getTime()){  selectTime = end;  }
        if (selectTime != null){
            Calendar selectCalendar = Calendar.getInstance();
            selectCalendar.setTime(selectTime);
            value.set(0,selectCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR));
            value.set(1,selectCalendar.get(Calendar.MONTH));
            value.set(2,selectCalendar.get(Calendar.DAY_OF_MONTH) - 1);
            pickers.get(0).setCurrentItem(value.get(0));
            pickers.get(1).setCurrentItem(value.get(1));
            pickers.get(2).setCurrentItem(value.get(2));
            calendar = selectCalendar;
        }
        ArrayList<Integer>res = getSelectValueByCalendar();
        selectListener.onSelect(res);
    }
    private ArrayList<String> getDates(int year,int moth){
        ArrayList<String> dates = new ArrayList<>();
        int max = 0;
        moth += 1;
        if (moth == 1 || moth == 3 || moth == 5 || moth == 7 || moth == 8 || moth == 10 || moth == 12){  max = 31;  }
        else if (moth == 4 || moth == 6 || moth == 9 || moth == 11){ max = 30; }
        else  if(moth == 2){
            Boolean flag = false;
            if (year % 100 == 0 && year % 400 == 0) flag = true;
            if (year % 100 !=0 && year % 4 == 0) flag = true;
            max = flag ? 29 : 28;
        }
        for (int j=0;j<max;j++){  dates.add((j+1)+""); }
        return dates;
    }

    public ArrayList<Integer> getValue() {
        return value;
    }
    public ArrayList<Integer> getSelectValueByCalendar(){
        ArrayList<Integer>res = new ArrayList<>();
        if(calendar == null) return res;
        res.add(calendar.get(Calendar.YEAR));
        res.add(calendar.get(Calendar.MONTH) + 1);
        res.add(calendar.get(Calendar.DAY_OF_MONTH));
        return res;
    }
}
