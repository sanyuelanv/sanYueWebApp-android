package com.sanyuelanv.sanwebapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.sanyuelanv.sanwebapp.utils.SanYueUIUtils;

/**
 * Create By songhang in 2020/3/19
 */
public class SanYueMainView extends RelativeLayout {
    private Context mContext;
    private FrameLayout frameLayout;
    private SanYueCapsuleBtn capsuleBtn;
    public SanYueMainView(Context context) {
        this(context, null,0, 0);
    }

    public SanYueMainView(Context context, AttributeSet attrs) {
        this(context, attrs, 0, 0);
    }

    public SanYueMainView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SanYueMainView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        initData();
    }
    private void initData(){
        frameLayout = new FrameLayout(mContext);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        addView(frameLayout,layoutParams);


    }
    public SanYueCapsuleBtn createBtn(int statusBarHeight){
        if (capsuleBtn == null){
            capsuleBtn = new SanYueCapsuleBtn(mContext);
            RelativeLayout.LayoutParams layoutParams  = new RelativeLayout.LayoutParams(SanYueUIUtils.dp2px(mContext,65), SanYueUIUtils.dp2px(mContext,28));
            int top = statusBarHeight + SanYueUIUtils.dp2px(mContext,8);
            int right = SanYueUIUtils.dp2px(mContext,12);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
            layoutParams.setMargins(0,top,right,0);
            addView(capsuleBtn,layoutParams);
        }
        return capsuleBtn;
    }

    public FrameLayout getFrameLayout() {
        return frameLayout;
    }
    public SanYueCapsuleBtn getCapsuleBtn() {
        return capsuleBtn;
    }
}
