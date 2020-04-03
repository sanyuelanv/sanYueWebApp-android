package com.sanyuelanv.sanwebapp.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sanyuelanv.sanwebapp.base.BaseAlertLinearLayout;
import com.sanyuelanv.sanwebapp.bean.SanYueActionSheetItem;
import com.sanyuelanv.sanwebapp.utils.SanYueUIUtils;

/**
 * Create By songhang in 2020/3/16
 */
public class SanYueActionSheetView extends BaseAlertLinearLayout implements View.OnClickListener,View.OnTouchListener {
    private SanYueActionSheetItem item;
    private LinearLayout mainBox;
    private boolean hasTitle;

    public SanYueActionSheetView(Context context, SanYueActionSheetItem item, int currentNightMode) {
        super(context);
        this.item = item;
        initData(currentNightMode);
    }
    @Override
    protected void  initData(int currentNightMode){
        setOrientation(VERTICAL);
        setGravity(Gravity.BOTTOM);
        mainBox = new LinearLayout(mContext);
        LinearLayout.LayoutParams mainBoxParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        mainBox.setOrientation(VERTICAL);
        mainBox.setGravity(Gravity.BOTTOM);
        addView(mainBox,mainBoxParams);
        hasTitle = false;

        int h = SanYueUIUtils.dp2px(mContext,55);
        int lineHeight = SanYueUIUtils.dp2px(mContext,1);
        int paddingH = SanYueUIUtils.dp2px(mContext,24);
        // 顶部标题 or 可能不存在
        if (!item.getTitle().equals("") && item.getTitle() != null){
            TextView textView = createTextView(2,-3,item.getTitle(),14,paddingH,h);
            textView.getPaint().setFakeBoldText(true);
            addLineInView(lineHeight);
            hasTitle = true;
        }
        // 中间列表
        for (int i = 0; i < item.getItemList().length; i++) {
            String text = item.getItemList()[i];
            TextView textView =  createTextView(1,i,text,17,paddingH,h);
            textView.setOnClickListener(this);
            if (i < item.getItemList().length - 1){
                addLineInView(lineHeight);
            }
            else {
                addLineInView(SanYueUIUtils.dp2px(mContext,8));
            }
        }
        // 底部取消按钮
        TextView textView =  createTextView(1,-1,item.getCancelText(),17,paddingH,h);
        textView.setOnClickListener(this);
        changeTheme(currentNightMode);

        if (item.isBackGroundCancel()){
            setTag(-2);
            setOnTouchListener(this);
            setOnClickListener(this);
        }
    }
    @Override
    public void changeTheme(int mode){
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

        int itemBgColor;
        int itemBgPressColor;
        int itemTextColor;
        int titleColor;
        int cancelTextColor;
        int mainBgColor;
        int lineColor;
        float radius = SanYueUIUtils.dp2px(mContext,12);
        float[] radii = {radius,radius,radius,radius,0,0,0,0};
        float[] radiiNone = {0,0,0,0,0,0,0,0};
        if (currentNightMode == Configuration.UI_MODE_NIGHT_NO){
            mainBgColor = Color.rgb(245,245,245);
            titleColor = Color.argb(125,0,0,0);
            itemBgColor = Color.WHITE;
            itemBgPressColor = Color.argb(25,50,50,50);
            lineColor = Color.rgb(239,239,239);
            itemTextColor = Color.parseColor(item.getItemColor());
            cancelTextColor = Color.parseColor(item.getCancelColor());
        }
        else {
            mainBgColor = Color.rgb(24,24,24);
            titleColor = Color.argb(125,255,255,255);
            itemBgColor = Color.rgb(35,35,35);
            itemBgPressColor = Color.argb(50,153,153,153);
            lineColor = Color.rgb(30,30,30);
            itemTextColor = Color.parseColor(item.getItemColorDark());
            cancelTextColor = Color.parseColor(item.getCancelColorDark());
        }
        for (int i = 0; i < mainBox.getChildCount(); i++) {
            View view = mainBox.getChildAt(i);
            int tag = (int) view.getTag();
            switch (tag){
                // line
                case -4:{
                    view.setBackgroundColor(lineColor);
                    break;
                }
                // title
                case -3:{
                    TextView textView = (TextView)view;
                    textView.setBackground(SanYueUIUtils.getDrawable(radii,0,0,itemBgColor));
                    textView.setTextColor(titleColor);
                    break;
                }
                // 背景
                case -2:{
                    break;
                }
                // 取消
                case -1:{
                    TextView textView = (TextView)view;
                    textView.setTextColor(cancelTextColor);
                    textView.setBackground(createStateDrawable(itemBgColor,itemBgPressColor, radiiNone));
                    break;
                }
                default:{
                    TextView textView = (TextView)view;
                    textView.setTextColor(itemTextColor);
                    if (tag == 1 && !hasTitle){
                        textView.setBackground(createStateDrawable(itemBgColor,itemBgPressColor,radii));
                        textView.setBackground(createStateDrawable(itemBgColor,itemBgPressColor,radii));
                    }
                    else {
                        textView.setBackground(createStateDrawable(itemBgColor,itemBgPressColor,radiiNone));
                        textView.setBackground(createStateDrawable(itemBgColor,itemBgPressColor,radiiNone));
                    }

                    break;
                }
            }
        }
        mainBox.setBackground(SanYueUIUtils.getDrawable(radii,0,0,mainBgColor));
        mainBox.setClipToOutline(true);
    }

    private TextView createTextView(int line,int tag,String text,int textSize,int paddingH,int h){
        TextView textView = new TextView(mContext);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(paddingH,0,paddingH,0);
        textView.setLines(line);
        textView.setTag(tag);
        textView.setText(text);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        LinearLayout.LayoutParams titleViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,h);
        mainBox.addView(textView,titleViewParams);
        return textView;
    }
    private void addLineInView(int height){
        View line = new View(mContext);
        line.setTag(-4);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height);
        mainBox.addView(line,params);
    }
    private StateListDrawable createStateDrawable(int normalColor,int pressColor,float[] radii){
        StateListDrawable bg = new StateListDrawable();
        Drawable pressed  = SanYueUIUtils.getDrawable(radii,0,0,pressColor);
        Drawable normal = SanYueUIUtils.getDrawable(radii,0,0,normalColor);
        bg.addState(new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled }, pressed);
        bg.addState(new int[] {  }, normal);
        return bg;
    }
}
