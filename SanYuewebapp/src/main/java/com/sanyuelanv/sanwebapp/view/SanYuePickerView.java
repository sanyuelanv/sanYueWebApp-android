package com.sanyuelanv.sanwebapp.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sanyuelanv.sanwebapp.adapter.PickerAdapter;
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

        changeTheme(currentNightMode);
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
        cancelBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        successBtn = new TextView(mContext);
        successBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        successBtn.setText("确认");
        successBtn.setGravity(Gravity.CENTER);
        successBtn.getPaint().setFakeBoldText(true);
        successBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

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
        int recyclerViewHeight = SanYueUIUtils.dp2px(mContext,56 * 5);
        //int margin = SanYueUIUtils.dp2px(mContext,15);
        SanYuePickerListView recyclerView = new SanYuePickerListView(mContext,item.getList(),1);
        LinearLayout.LayoutParams recyclerViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,recyclerViewHeight);
        mainBox.addView(recyclerView,recyclerViewParams);
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
        float[] radiiNone = {0,0,0,0,0,0,0,0};

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
