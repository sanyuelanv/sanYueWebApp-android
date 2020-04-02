package com.sanyuelanv.sanwebapp.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sanyuelanv.sanwebapp.base.BaseAlertLinearLayout;
import com.sanyuelanv.sanwebapp.bean.SanYueModalItem;
import com.sanyuelanv.sanwebapp.utils.SanYueUIUtils;

/**
 * Create By songhang in 2020/3/15
 */
public class SanYueModalView extends BaseAlertLinearLayout{
    private TextView titleView;
    private TextView descView;
    private TextView cancelBtn;
    private TextView successBtn;
    private SanYueModalItem item;
    private LinearLayout box;
    private View hLine;
    private View wLine;

    public SanYueModalView(Context context, SanYueModalItem item, int currentNightMode) {
        super(context);
        this.item = item;
        initData(currentNightMode);
    }
    @Override
    protected void initData(int mode){
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        setBackgroundResource(android.R.color.transparent);
        // 添加最外层 layout
        box = new LinearLayout(mContext);
        box.setOrientation(VERTICAL);
        LinearLayout.LayoutParams boxParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        int horizontal  = SanYueUIUtils.dp2px(mContext,50);
        boxParams.setMargins(horizontal,0,horizontal,0);
        addView(box,boxParams);

        titleView = createTextView(box,0);
        descView = createTextView(box,1);
        hLine = createLine(box,0);

        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(HORIZONTAL);
        box.addView(layout,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SanYueUIUtils.dp2px(mContext,56)));

        if (item.isShowCancel()){
            cancelBtn = addBtn(layout,0);
            cancelBtn.setText(item.getCancelText());
            wLine = createLine(layout,1);
            cancelBtn.setTag(0);
            cancelBtn.setOnClickListener(this);
        }
        successBtn = addBtn(layout,1);
        successBtn.setText(item.getConfirmText());
        successBtn.setOnClickListener(this);
        successBtn.setTag(1);
        if (item.isBackGroundCancel()){
            setTag(-1);
            setOnClickListener(this);
            setOnTouchListener(this);
        }
        changeTheme(mode);
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
        Drawable bg;
        Drawable cancelBg;
        Drawable successBg;
        int lineColor;
        int cancelColor;
        int successColor;
        int titleColor;
        int descColor;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_NO){
            bg = SanYueUIUtils.getDrawable(SanYueUIUtils.dp2px(mContext,12),0,0,Color.WHITE);
            lineColor = Color.argb(25,0,0,0);
            cancelColor = Color.parseColor(item.getCancelColor());
            successColor = Color.parseColor(item.getConfirmColor());
            titleColor = Color.parseColor("#000000");
            descColor = Color.argb(200,0,0,0);
            cancelBg = SanYueUIUtils.getStateDrawable(0);
            successBg = SanYueUIUtils.getStateDrawable(0);
        }
        else{
            bg = SanYueUIUtils.getDrawable(SanYueUIUtils.dp2px(mContext,12),0,0,Color.parseColor("#313131"));
            lineColor = Color.argb(25,255,255,255);
            cancelColor = Color.parseColor(item.getCancelColorDark());
            successColor = Color.parseColor(item.getConfirmColorDark());
            titleColor = Color.parseColor("#FFFFFF");
            descColor = Color.argb(240,255,255,255);
            cancelBg = SanYueUIUtils.getStateDrawable(1);
            successBg = SanYueUIUtils.getStateDrawable(1);
        }
        hLine.setBackgroundColor(lineColor);
        if (item.isShowCancel()){
            wLine.setBackgroundColor(lineColor);
            cancelBtn.setTextColor(cancelColor);
            cancelBtn.setBackground(cancelBg);
        }
        successBtn.setTextColor(successColor);
        titleView.setTextColor(titleColor);
        descView.setTextColor(descColor);

        successBtn.setBackground(successBg);
        box.setBackground(bg);
        box.setClipToOutline(true);
    }

    private TextView createTextView(LinearLayout layout,int type){
        TextView textView = new TextView(mContext);
        int textSize = 17;
        String text = "";
        int top  = SanYueUIUtils.dp2px(mContext,32);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        if (type == 0){
            // 标题
            int horizontal  = SanYueUIUtils.dp2px(mContext,24);
            int bottom  = SanYueUIUtils.dp2px(mContext,16);
            params.setMargins(horizontal,top,horizontal,bottom);
            textView.getPaint().setFakeBoldText(true);
            text = item.getTitle();
        }
        else {
            // 内容
            int horizontal  = SanYueUIUtils.dp2px(mContext,24);
            params.setMargins(horizontal,0,horizontal,top);
            text = item.getContent();
        }

        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
        layout.addView(textView,params);
        textView.setText(text);
        return textView;
    }
    private View  createLine(LinearLayout layout,int type){
        View view = new View(mContext);
        LinearLayout.LayoutParams params = type == 0 ? new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)(SanYueUIUtils.dp2px(mContext,1))) : new LinearLayout.LayoutParams((int)(SanYueUIUtils.dp2px(mContext,1)),ViewGroup.LayoutParams.MATCH_PARENT);
        layout.addView(view,params);
        return view;
    }
    private TextView addBtn(LinearLayout layout,int type){
        Drawable bg = SanYueUIUtils.getStateDrawable(0);
        TextView textView = new TextView(mContext);
        textView.setMaxLines(1);
        textView.setLines(1);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.MATCH_PARENT,1);
        if (type == 0){
            textView.setText(item.getCancelText());
        }
        else {
            textView.setText(item.getConfirmText());
        }

        textView.setGravity(Gravity.CENTER);
        int padding = SanYueUIUtils.dp2px(mContext,24);
        textView.setPadding(padding,0,padding,0);
        textView.setLines(1);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        layout.addView(textView,params);
        textView.setBackground(bg);
        return textView;
    }

}
