package com.sanyuelanv.sanwebapp.base;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/**
 * Create By songhang in 2020/3/20
 */
public abstract class BaseLinearLayout extends LinearLayout {
    protected Context mContext;
    public BaseLinearLayout(Context context) {
        this(context,null,0,0);
    }

    public BaseLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0,0);
    }

    public BaseLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context,attrs,defStyleAttr,0);
    }

    public BaseLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        initData();
    }
    protected abstract void initData();
}
