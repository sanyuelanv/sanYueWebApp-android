package com.sanyuelanv.sanwebapp.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/**
 * Create By songhang in 2020/3/20
 */
public abstract class BaseAlertLinearLayout extends LinearLayout  implements  View.OnClickListener,View.OnTouchListener {
    protected Context mContext;
    protected int currentNightMode;
    protected OnControlBtnListener listener;
    protected float downX, downY,moveY,moveX;
    protected boolean isInit;
    public interface OnControlBtnListener {
        void onClick(int type);
    }
    public BaseAlertLinearLayout(Context context) {
        this(context,null,0,0);
    }

    public BaseAlertLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0,0);
    }

    public BaseAlertLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context,attrs,defStyleAttr,0);
    }

    public BaseAlertLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
    }
    public void setListener(OnControlBtnListener listener) {
        this.listener = listener;
    }

    protected abstract void initData(int currentNightMode);
    public abstract void changeTheme(int mode);

    @Override
    public void onClick(View v) {
        if (listener !=null){
            listener.onClick((int)v.getTag());
        }
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                moveX = 0;
                moveY = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                moveX += Math.abs(event.getX() - downX);
                moveY += Math.abs(event.getY() - downY);
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                //判断是否继续传递信号
                if(moveX>20||moveY>20){
                    return true;
                }
                break;
        }
        return false;//继续执行后面的代码:点击
    }
}
