package com.sanyuelanv.sanwebapp.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sanyuelanv.sanwebapp.R;
import com.sanyuelanv.sanwebapp.base.BaseAlertLinearLayout;
import com.sanyuelanv.sanwebapp.base.SanYueBaseBottomDialog;
import com.sanyuelanv.sanwebapp.bean.SanYueActionSheetItem;
import com.sanyuelanv.sanwebapp.view.SanYueActionSheetView;
import com.sanyuelanv.sanwebapp.view.SanYueModalView;

/**
 * Create By songhang in 2020/3/16
 */
public class SanYueActionSheet extends SanYueBaseBottomDialog {
    private SanYueActionSheetItem actionSheetItem;
    private SanYueActionSheetView actionSheetView;
    protected BaseAlertLinearLayout.OnControlBtnListener listener;
    public void setListener(BaseAlertLinearLayout.OnControlBtnListener listener) {
        this.listener = listener;
    }

    public SanYueActionSheet(SanYueActionSheetItem actionSheetItem, int currentNightMode,int height) {
        super(height,currentNightMode);
        this.actionSheetItem = actionSheetItem;
    }
    public void changStyle(int mode){
        if (actionSheetView != null) actionSheetView.changeTheme(mode);
    }
    @Override
    protected View createView() {
        actionSheetView = new SanYueActionSheetView(getContext(),actionSheetItem,currentNightMode);
        actionSheetView.setListener(listener);
        return actionSheetView;
    }
}
