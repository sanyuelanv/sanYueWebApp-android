package com.sanyuelanv.sanwebapp.view;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.view.WheelView;
import com.sanyuelanv.sanwebapp.R;
import com.sanyuelanv.sanwebapp.utils.SanYueUIUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;

/**
 * Create By songhang in 2020/4/3
 */
public class SanYueMyPicker extends WheelView {
    private Context mContext;

    public SanYueMyPicker(Context context, ArrayList<String>list,Boolean isCyclic) {
        super(context);
        mContext = context;
        setAdapter(new ArrayWheelAdapter(list));
        initData(isCyclic);
    }
    private void  initData(Boolean isCyclic){
        setAlphaGradient(true);
        setLineSpacingMultiplier(2.4f);
        setTextSize(20f);
        setCyclic(isCyclic);
        setDividerWidth(SanYueUIUtils.dp2px(mContext,1)/2);
    }
    public void changeMode(int mode){
        if (mode == Configuration.UI_MODE_NIGHT_NO){
            setTextColorCenter(Color.BLACK);
            setTextColorOut(Color.GRAY);
            setDividerColor(Color.argb(25,0,0,0));
        }
        else {
            setTextColorCenter(Color.WHITE);
            setTextColorOut(Color.GRAY);
            setDividerColor(Color.argb(25,255,255,255));
        }
        invalidate();
    }
}
