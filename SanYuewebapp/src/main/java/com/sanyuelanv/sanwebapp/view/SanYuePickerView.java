package com.sanyuelanv.sanwebapp.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.sanyuelanv.sanwebapp.base.BaseAlertLinearLayout;
import com.sanyuelanv.sanwebapp.bean.SanYuePickItem;
import com.sanyuelanv.sanwebapp.utils.SanYueUIUtils;

/**
 * Create By songhang in 2020/4/1
 */
public class SanYuePickerView extends BaseAlertLinearLayout {
    private SanYuePickItem item;
    private LinearLayout mainBox;
    private TextView cancelBtn;
    private TextView successBtn;
    private View topLine;
    private SanYueMyPicker picker;
    private int normalIndex;
    protected OnSelectListener selectListener;
    public interface OnSelectListener {
        void onSelect(int pos,int type);
        void onSelectMulti(int[] pos,int type);
        void onSelectDate(String res,int type);
    }
    private View.OnClickListener clickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (item.getMode()){
                case 0:{
                    if (selectListener == null) return;
                    int type = (int)v.getTag();
                    if (type < 0){
                        selectListener.onSelect(0,type);
                    }
                    else {
                        selectListener.onSelect(normalIndex,type);
                    }
                    break;
                }
            }
        }
    };

    public void setSelectListener(OnSelectListener selectListener) {
        this.selectListener = selectListener;
    }

    public SanYuePickerView(Context context, SanYuePickItem item, int currentNightMode) {
        super(context);
        this.item = item;
        initData(currentNightMode);
    }
    @Override
    protected void initData(int currentNightMode) {
        setOrientation(VERTICAL);
        setGravity(Gravity.BOTTOM);
        mainBox = new LinearLayout(mContext);
        LinearLayout.LayoutParams mainBoxParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        mainBox.setOrientation(VERTICAL);
        mainBox.setGravity(Gravity.BOTTOM);
        addView(mainBox,mainBoxParams);
        // 顶部 height= 50
        creatTopView();
        // list : 5 个 height= 56
        creatList();
        // 改变主题
        changeTheme(currentNightMode);
        // mask 取消
        if (item.isBackGroundCancel()){
            setTag(-2);
            setOnTouchListener(this);
            setOnClickListener(clickListener);
        }

    }
    private void creatTopView(){
        LinearLayout topView = new LinearLayout(mContext);
        int topHeight = SanYueUIUtils.dp2px(mContext,50);
        LinearLayout.LayoutParams topViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,topHeight);
        mainBox.addView(topView,topViewParams);

        cancelBtn = new TextView(mContext);
        cancelBtn.setText("取消");
        cancelBtn.setGravity(Gravity.CENTER);
        cancelBtn.getPaint().setFakeBoldText(true);
        cancelBtn.setTag(-1);
        cancelBtn.setOnClickListener(clickListener);

        successBtn = new TextView(mContext);
        successBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        successBtn.setText("确认");
        successBtn.setGravity(Gravity.CENTER);
        successBtn.getPaint().setFakeBoldText(true);
        successBtn.setTag(0);
        successBtn.setOnClickListener(clickListener);
        cancelBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(SanYueUIUtils.dp2px(mContext,70),ViewGroup.LayoutParams.MATCH_PARENT);

        View view = new View(mContext);
        LinearLayout.LayoutParams centerParams = new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.MATCH_PARENT,1);

        topView.addView(cancelBtn,btnParams);
        topView.addView(view,centerParams);
        topView.addView(successBtn,btnParams);

        topLine = new View(mContext);
        int lineHeight = SanYueUIUtils.dp2px(mContext,1)/2;
        LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,lineHeight);
        mainBox.addView(topLine,lineParams);
    }
    private void creatList(){
        switch (item.getMode()){
            case 0:{
                normalIndex = item.getNormalValue();
                picker = new SanYueMyPicker(mContext,item.getList());
                picker.setCurrentItem(normalIndex);
                picker.setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int index) {
                        normalIndex = index;
                        if (item.isListenChange()){
                            selectListener.onSelect(index,1);
                        }
                    }
                });
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mainBox.addView(picker,params);
                break;
            }
        }


    }
    @Override
    public void changeTheme(int mode) {
        if (isInit && item.getSenseMode() !=0) return;
        if (currentNightMode == mode) return;

        if (item.getSenseMode() == 1){
            currentNightMode = Configuration.UI_MODE_NIGHT_NO;
        }
        else if (item.getSenseMode() == 2){
            currentNightMode = Configuration.UI_MODE_NIGHT_YES;
        }
        else {
            currentNightMode = mode;
        }
        if (!isInit){ isInit = true; }
        int mainBgColor;
        int[] cancelTextColors = new int[] { Color.argb(125,136,136,136),Color.rgb(136,136,136) };
        int[] successTextColors;
        int lineColor;
        float radius = SanYueUIUtils.dp2px(mContext,12);
        float[] radii = {radius,radius,radius,radius,0,0,0,0};

        if (currentNightMode == Configuration.UI_MODE_NIGHT_NO){
            mainBgColor = Color.rgb(245,245,245);
            successTextColors = new int[]{Color.argb(125,31,162,20),Color.rgb(31,162,20)};
            lineColor = Color.rgb(221,221,221);
        }
        else {
            mainBgColor = Color.rgb(24,24,24);
            successTextColors = new int[]{Color.argb(125,27,142,19),Color.rgb(27,142,19)};
            lineColor = Color.rgb(80,80,80);
        }
        if (picker != null){
            picker.changeMode(mode);
        }
        mainBox.setBackground(SanYueUIUtils.getDrawable(radii,0,0,mainBgColor));
        successBtn.setTextColor(getTextColorList(successTextColors));
        cancelBtn.setTextColor(getTextColorList(cancelTextColors));
        topLine.setBackgroundColor(lineColor);
        mainBox.setClipToOutline(true);
    }
    private ColorStateList getTextColorList(int[] colors){
        int[][] states = new int[2][];
        states[0] = new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled };
        states[1] = new int[] {};
        ColorStateList colorList = new ColorStateList(states,colors);
        return colorList;
    }
}
