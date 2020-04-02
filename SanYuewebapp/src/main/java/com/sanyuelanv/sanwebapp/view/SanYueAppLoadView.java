package com.sanyuelanv.sanwebapp.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.sanyuelanv.sanwebapp.base.BaseLinearLayout;
import com.sanyuelanv.sanwebapp.utils.SanYueUIUtils;

import java.util.ArrayList;

/**
 * Create By songhang in 2020/3/4
 */
public class SanYueAppLoadView extends BaseLinearLayout {
    private static int maxNumber = 3;
    private static int TIME = 250;
    private Drawable drawableNormal;
    private Drawable drawableActive;
    private  int selectIndex = 0;
    private ArrayList<View> views;
    private Handler handler;
    private Runnable runnable;

    public SanYueAppLoadView(Context context) {
        super(context);
    }

    @Override
    protected void initData(){
        views = new ArrayList<View>();
        this.setOrientation(HORIZONTAL);
        this.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        this.setLayoutParams(layoutParams);
        int width = SanYueUIUtils.dp2px(mContext,8);
        int marginLeft = SanYueUIUtils.dp2px(mContext,20);
        drawableNormal = SanYueUIUtils.getDrawable(SanYueUIUtils.dp2px(mContext,4),0,0, Color.parseColor("#FFF1F1F1"));
        drawableActive = SanYueUIUtils.getDrawable(SanYueUIUtils.dp2px(mContext,4),0,0, Color.parseColor("#FFC8C8C8"));
        for (int i = 0; i<maxNumber;i++ ){
            View view = new View(mContext);
            LinearLayout.LayoutParams viewLayoutParams = new LinearLayout.LayoutParams(width,width);
            if (i > 0){
                viewLayoutParams.setMargins(marginLeft,0,0,0);
                view.setBackground(drawableNormal);
            }
            else {
                view.setBackground(drawableActive);
                selectIndex = 0;
            }
            view.setLayoutParams(viewLayoutParams);
            views.add(view);
            addView(view);
        }

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                View view1 =  views.get(selectIndex);
                view1.setBackground(drawableNormal);
                selectIndex = selectIndex >= (maxNumber - 1) ? 0 : selectIndex + 1;
                View view2 =  views.get(selectIndex);
                view2.setBackground(drawableActive);
                handler.postDelayed(this,TIME);
            }
        };
        handler.postDelayed(runnable, TIME);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (handler != null){
            handler.removeCallbacks(runnable);
            handler = null;
            runnable = null;
        }
    }
}
